<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="talk-container">
      <Editor ref="editor" class="editor-wrapper" v-model="talk.content" placeholder="说点什么吧" />
      <div class="operation-wrapper">
        <div class="left-wrapper">
          <el-upload
            action="/api/admin/talks/images"
            multiple
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="upload"
            :show-file-list="false">
            <i class="iconfont el-icon-mytupian operation-btn tupian" />
          </el-upload>
        </div>
        <div class="right-wrapper">
          <el-switch
            style="margin-right: 16px"
            v-model="talk.isTop"
            inactive-text="置顶"
            :active-value="1"
            :inactive-value="0" />
          <el-dropdown trigger="click" @command="handleCommand" style="margin-right: 16px">
            <span class="talk-status"> {{ dropdownTitle }}<i class="el-icon-arrow-down el-icon--right" /> </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(item, index) of statuses" :key="index" :command="item.status">
                {{ item.desc }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button type="primary" size="small" @click="saveOrUpdateTalk" :disabled="talk.content == ''">
            发布
          </el-button>
        </div>
      </div>
      <el-upload
        class="talk-image-upload"
        v-show="uploads.length > 0"
        action="/api/admin/talks/images"
        list-type="picture-card"
        multiple
        :headers="headers"
        :file-list="uploads"
        :before-upload="beforeUpload"
        :on-success="upload"
        :on-remove="handleRemove">
        <i class="el-icon-plus" />
      </el-upload>
    </div>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'
import Editor from '@/components/Editor.vue'
export default {
  components: {
    Editor
  },
  created() {
    if (this.$route.params.talkId) {
      this.axios.get('/api/admin/talks/' + this.$route.params.talkId).then(({ data }) => {
        this.talk = data.data
        if (data.data.imgs) {
          data.data.imgs.forEach((item) => {
            this.uploads.push({ url: item })
          })
        }
      })
    }
  },
  data: function () {
    return {
      talk: {
        id: null,
        content: '',
        isTop: 0,
        status: 1,
        images: ''
      },
      statuses: [
        { status: 1, desc: '公开' },
        { status: 2, desc: '私密' }
      ],
      uploads: [],
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
    }
  },
  methods: {
    handleCommand(command) {
      this.talk.status = command
    },
    handleRemove(file) {
      this.uploads.forEach((item, index) => {
        if (item.url == file.url) {
          this.uploads.splice(index, 1)
        }
      })
    },
    upload(response) {
      this.uploads.push({ url: response.data })
    },
    beforeUpload(file) {
      return new Promise((resolve) => {
        if (file.size / 1024 < this.config.UPLOAD_SIZE) {
          resolve(file)
        }
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          resolve(res)
        })
      })
    },
    saveOrUpdateTalk() {
      if (this.talk.content.trim() == '') {
        this.$message.error('说说内容不能为空')
        return false
      }
      if (this.uploads.length > 0) {
        var img = []
        this.uploads.forEach((item) => {
          img.push(item.url)
        })
        this.talk.images = JSON.stringify(img)
      } else {
        this.talk.images = ''
      }
      this.axios.post('/api/admin/talks', this.talk).then(({ data }) => {
        if (data.flag) {
          this.$refs.editor.clear()
          this.uploads = []
          this.$router.push({ path: '/talk-list' })
          this.$notify.success({
            title: '成功',
            message: data.message
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
    }
  },
  computed: {
    dropdownTitle() {
      var desc = ''
      this.statuses.forEach((item) => {
        if (item.status == this.talk.status) {
          desc = item.desc
        }
      })
      return desc
    }
  }
}
</script>

<style scoped>
.tupian {
  margin-left: 3px;
}
.talk-container {
  margin-top: 40px;
}
.editor-wrapper {
  min-height: 150px;
}
.operation-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}
.operation-btn {
  cursor: pointer;
  color: #838383;
  font-size: 20px;
  margin-right: 12px;
}
.talk-status {
  cursor: pointer;
  font-size: 12px;
  color: #999;
}
.emoji {
  user-select: none;
  margin: 0.25rem;
  display: inline-block;
  vertical-align: middle;
}
.emoji-item {
  cursor: pointer;
  display: inline-block;
}
.emoji-item:hover {
  transition: all 0.2s;
  border-radius: 0.25rem;
  background: #dddddd;
}
.left-wrapper {
  display: flex;
  width: 50%;
}
.talk-image-upload {
  margin-top: 8px;
}
</style>
