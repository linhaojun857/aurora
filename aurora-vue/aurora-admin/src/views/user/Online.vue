<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <div style="margin-left: auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="listOnlineUsers" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="listOnlineUsers">
          搜索
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="users">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="avatar" label="头像" align="center" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" align="center" />
      <el-table-column prop="ipAddress" label="ip地址" align="center" />
      <el-table-column prop="ipSource" label="登录地址" align="center" width="200" />
      <el-table-column prop="browser" label="浏览器" align="center" width="160" />
      <el-table-column prop="os" label="操作系统" align="center" />
      <el-table-column prop="lastLoginTime" label="登录时间" align="center" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.lastLoginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-popconfirm title="确定下线吗？" style="margin-left: 10px" @confirm="removeOnlineUser(scope.row)">
            <el-button size="mini" type="text" slot="reference"> <i class="el-icon-delete" /> 下线 </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination-container"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper" />
  </el-card>
</template>

<script>
import router from '@/router'

export default {
  created() {
    this.current = this.$store.state.pageState.online
    this.listOnlineUsers()
  },
  data() {
    return {
      loading: true,
      users: [],
      keywords: null,
      current: 1,
      size: 10,
      count: 0,
      isCheck: false,
      optLog: {}
    }
  },
  methods: {
    listOnlineUsers() {
      this.axios
        .get('/api/admin/users/online', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.users = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    sizeChange(size) {
      this.size = size
      this.listOnlineUsers()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateOnlinePageState', current)
      this.listOnlineUsers()
    },
    removeOnlineUser(user) {
      this.axios.delete('/api/admin/users/' + user.userInfoId + '/online').then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          if (user.userInfoId == this.$store.state.userInfo.id) {
            router.push({ path: '/login' })
            sessionStorage.removeItem('token')
          }
          this.listOnlineUsers()
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
    tagType() {
      return function (type) {
        switch (type) {
          case 'GET':
            return ''
          case 'POST':
            return 'success'
          case 'PUT':
            return 'warning'
          case 'DELETE':
            return 'danger'
        }
      }
    }
  }
}
</script>

<style scoped>
label {
  font-weight: bold !important;
}
</style>
