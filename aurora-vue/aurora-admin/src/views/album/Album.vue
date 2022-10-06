<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openModel(null)"> 新建相册 </el-button>
      <div style="margin-left: auto">
        <el-button type="text" size="small" icon="el-icon-delete" style="margin-right: 1rem" @click="checkDelete">
          回收站
        </el-button>
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入相册名"
          style="width: 200px"
          @keyup.enter.native="searchAlbums" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchAlbums">
          搜索
        </el-button>
      </div>
    </div>
    <el-row class="album-container" :gutter="12" v-loading="loading">
      <el-empty v-if="albums == null" description="暂无相册" />
      <el-col v-for="item of albums" :key="item.id" :md="6">
        <div class="album-item" @click="checkPhoto(item)">
          <div class="album-opreation">
            <el-dropdown @command="handleCommand">
              <i class="el-icon-more" style="color: #fff" />
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="'update' + JSON.stringify(item)">
                  <i class="el-icon-edit" />编辑
                </el-dropdown-item>
                <el-dropdown-item :command="'delete' + item.id"> <i class="el-icon-delete" />删除 </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="album-photo-count">
            <div>{{ item.photoCount }}</div>
            <i v-if="item.status == 2" class="iconfont el-icon-mymima" />
          </div>
          <el-image fit="cover" class="album-cover" :src="item.albumCover" />
          <div class="album-name">{{ item.albumName }}</div>
        </div>
      </el-col>
    </el-row>
    <el-pagination
      :hide-on-single-page="true"
      class="pagination-container"
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      layout="prev, pager, next" />
    <el-dialog :visible.sync="addOrEdit" width="35%" top="10vh">
      <div class="dialog-title-container" slot="title" ref="albumTitle" />
      <el-form label-width="80px" size="medium" :model="albumForum">
        <el-form-item label="相册名称">
          <el-input style="width: 220px" v-model="albumForum.albumName" />
        </el-form-item>
        <el-form-item label="相册描述">
          <el-input style="width: 220px" v-model="albumForum.albumDesc" />
        </el-form-item>
        <el-form-item label="相册封面">
          <el-upload
            class="upload-cover"
            drag
            :headers="headers"
            :before-upload="beforeUpload"
            action="/api/admin/photos/albums/upload"
            multiple
            :on-success="uploadCover">
            <i class="el-icon-upload" v-if="albumForum.albumCover == ''" />
            <div class="el-upload__text" v-if="albumForum.albumCover == ''">将文件拖到此处，或<em>点击上传</em></div>
            <img v-else :src="albumForum.albumCover" width="360px" height="180px" />
          </el-upload>
        </el-form-item>
        <el-form-item label="发布形式">
          <el-radio-group v-model="albumForum.status">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="2">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditAlbum"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isdelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除该相册？</div>
      <div slot="footer">
        <el-button @click="isdelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteAlbum"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  created() {
    this.listAlbums()
  },
  data: function () {
    return {
      keywords: '',
      loading: true,
      isdelete: false,
      addOrEdit: false,
      albumForum: {
        id: null,
        albumName: '',
        albumDesc: '',
        albumCover: '',
        status: 1
      },
      albums: [],
      current: 1,
      size: 8,
      count: 0,
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
    }
  },
  methods: {
    openModel(item) {
      if (item) {
        console.log(item)
        this.albumForum = JSON.parse(item)
        this.$refs.albumTitle.innerHTML = '修改相册'
      } else {
        this.albumForum = {
          id: null,
          albumName: '',
          albumLabel: '',
          albumCover: '',
          status: 1
        }
        this.$refs.albumTitle.innerHTML = '新建相册'
      }
      this.addOrEdit = true
    },
    checkPhoto(item) {
      this.$router.push({ path: '/albums/' + item.id })
    },
    checkDelete() {
      this.$router.push({ path: '/photos/delete' })
    },
    listAlbums() {
      this.axios
        .get('/api/admin/photos/albums', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.albums = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    addOrEditAlbum() {
      if (this.albumForum.albumName.trim() == '') {
        this.$message.error('相册名称不能为空')
        return false
      }
      if (this.albumForum.albumDesc.trim() == '') {
        this.$message.error('相册描述不能为空')
        return false
      }
      if (this.albumForum.albumCover == null) {
        this.$message.error('相册封面不能为空')
        return false
      }
      this.axios.post('/api/admin/photos/albums', this.albumForum).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listAlbums()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
      this.addOrEdit = false
    },
    uploadCover(response) {
      this.albumForum.albumCover = response.data
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
    handleCommand(command) {
      const type = command.substring(0, 6)
      const data = command.substring(6)
      if (type == 'delete') {
        this.albumForum.id = data
        this.isdelete = true
      } else {
        console.log(data)
        this.openModel(data)
      }
    },
    deleteAlbum() {
      this.axios.delete('/api/admin/photos/albums/' + this.albumForum.id).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listAlbums()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isdelete = false
      })
    },
    searchAlbums() {
      this.current = 1
      this.listAlbums()
    },
    sizeChange(size) {
      this.size = size
      this.listAlbums()
    },
    currentChange(current) {
      this.current = current
      this.listAlbums()
    }
  }
}
</script>

<style scoped>
.album-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}
.album-cover::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}
.album-photo-count {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.5rem;
  z-index: 1000;
  position: absolute;
  left: 0;
  right: 0;
  padding: 0 0.5rem;
  bottom: 2.6rem;
  color: #fff;
}
.album-name {
  text-align: center;
  margin-top: 0.5rem;
}
.album-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.album-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>
