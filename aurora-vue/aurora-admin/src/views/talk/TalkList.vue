<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="status-menu">
      <span>状态</span>
      <span @click="changeStatus(null)" :class="isActive(null)">全部</span>
      <span @click="changeStatus(1)" :class="isActive(1)"> 公开 </span>
      <span @click="changeStatus(2)" :class="isActive(2)"> 私密 </span>
    </div>
    <el-empty v-if="talks == null" description="暂无说说" />
    <div class="talk-item" v-for="item of talks" :key="item.id">
      <div class="user-info-wrapper">
        <el-avatar class="user-avatar" :src="item.avatar" :size="36" />
        <div class="user-detail-wrapper">
          <div class="user-nickname">
            <div>{{ item.nickname }}</div>
            <el-dropdown trigger="click" @command="handleCommand">
              <i class="el-icon-more" style="color: #333; cursor: pointer" />
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="'1,' + item.id"> <i class="el-icon-edit" />编辑 </el-dropdown-item>
                <el-dropdown-item :command="'2,' + item.id"> <i class="el-icon-delete" />删除 </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="time">
            {{ item.createTime | dateTime }}
            <span class="top" v-if="item.isTop == 1"> <i class="iconfont el-icon-myzhiding" /> 置顶 </span>
            <span class="secret" v-if="item.status == 2"> <i class="iconfont el-icon-mymima" /> 私密 </span>
          </div>
          <div class="talk-content" v-html="item.content" />
          <el-row :gutter="4" class="talk-images" v-if="item.imgs">
            <el-col :md="8" :cols="6" v-for="(img, index) of item.imgs" :key="index">
              <el-image class="images-items" :src="img" :preview-src-list="previews" />
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <el-pagination
      :hide-on-single-page="false"
      class="pagination-container"
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      layout="prev, pager, next" />
    <el-dialog :visible.sync="isdelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除该说说？</div>
      <div slot="footer">
        <el-button @click="isdelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteTalk"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.talkList
    this.listTalks()
  },
  data: function () {
    return {
      current: 1,
      size: 5,
      count: 0,
      status: null,
      isdelete: false,
      talks: [],
      previews: [],
      talkId: null
    }
  },
  methods: {
    handleCommand(command) {
      var arr = command.split(',')
      this.talkId = arr[1]
      switch (arr[0]) {
        case '1':
          this.$router.push({ path: '/talks/' + this.talkId })
          break
        case '2':
          this.isdelete = true
          break
      }
    },
    listTalks() {
      this.axios
        .get('/api/admin/talks', {
          params: {
            current: this.current,
            size: this.size,
            status: this.status
          }
        })
        .then(({ data }) => {
          this.talks = data.data.records
          this.talks.forEach((item) => {
            if (item.imgs) {
              this.previews.push(...item.imgs)
            }
          })
          this.count = data.data.count
        })
    },
    sizeChange(size) {
      this.previews = []
      this.size = size
      this.listTalks()
    },
    currentChange(current) {
      this.previews = []
      this.current = current
      this.$store.commit('updateTalkListPageState', current)
      this.listTalks()
    },
    changeStatus(status) {
      this.current = 1
      this.previews = []
      this.status = status
      this.listTalks()
    },
    deleteTalk() {
      this.axios.delete('/api/admin/talks', { data: [this.talkId] }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listTalks()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isdelete = false
      })
    }
  },
  computed: {
    isActive() {
      return function (status) {
        return this.status == status ? 'active-status' : 'status'
      }
    }
  }
}
</script>

<style scoped>
.status-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}
.status-menu span {
  margin-right: 24px;
}
.status {
  cursor: pointer;
}
.active-status {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}
.talk-item:not(:first-child) {
  margin-top: 20px;
}
.talk-item {
  padding: 16px 20px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 3px 8px 6px rgb(7 17 27 / 6%);
  transition: all 0.3s ease 0s;
}
.talk-item:hover {
  box-shadow: 0 5px 10px 8px rgb(7 17 27 / 16%);
  transform: translateY(-3px);
}
.user-info-wrapper {
  width: 100%;
  display: flex;
}
.user-avatar {
  border-radius: 50%;
}
.user-avatar {
  transition: all 0.5s;
}
.user-avatar:hover {
  transform: rotate(360deg);
}
.user-detail-wrapper {
  margin-left: 10px;
  width: 100%;
}
.user-nickname {
  font-size: 15px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
}
.user-sign {
  margin-left: 4px;
}
.time {
  color: #999;
  margin-top: 2px;
  font-size: 12px;
}
.top {
  color: #ff7242;
  margin-left: 10px;
}
.secret {
  color: #999;
  margin-left: 10px;
}
.talk-content {
  margin-top: 8px;
  font-size: 14px;
  line-height: 26px;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.talk-images {
  margin-top: 8px;
}
.images-items {
  cursor: pointer;
  object-fit: cover;
  height: 200px;
  width: 100%;
  border-radius: 4px;
}
</style>
