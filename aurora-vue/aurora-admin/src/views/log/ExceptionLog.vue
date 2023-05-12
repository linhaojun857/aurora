<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="logIds.length === 0"
        @click="isDelete = true">
        批量删除
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入操作描述"
          style="width: 200px"
          @keyup.enter.native="searchLogs" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchLogs">
          搜索
        </el-button>
      </div>
    </div>
    <el-table @selection-change="selectionChange" v-loading="loading" :data="logs">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="optUri" label="请求接口" align="center" width="160" />
      <el-table-column prop="optDesc" label="操作描述" align="center" width="150" />
      <el-table-column prop="requetMethod" label="请求方式" align="center" width="150">
        <template slot-scope="scope" v-if="scope.row.requestMethod">
          <el-tag :type="tagType(scope.row.requestMethod)">
            {{ scope.row.requestMethod }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="ipAddress" label="登录ip" align="center" width="160" />
      <el-table-column prop="ipSource" label="登录地址" align="center" width="190" />
      <el-table-column prop="createTime" label="操作日期" align="center" width="210">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" slot="reference" @click="check(scope.row)">
            <i class="el-icon-view" /> 查看
          </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left: 10px" @confirm="deleteLog(scope.row.id)">
            <el-button size="mini" type="text" slot="reference"> <i class="el-icon-delete" /> 删除 </el-button>
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
    <el-dialog :visible.sync="isCheck" append-to-body top="20px" width="80%" destroy-on-close>
      <div class="dialog-title-container" slot="title"><i class="el-icon-more" />详细信息</div>
      <el-form ref="form" :model="exceptionLog" label-width="100px" size="mini">
        <el-form-item label="操作接口：">
          {{ exceptionLog.optUri }}
        </el-form-item>
        <el-form-item label="操作方法：">
          {{ exceptionLog.optMethod }}
        </el-form-item>
        <el-form-item label="请求方式：">
          <el-tag :type="tagType(exceptionLog.requestMethod)">
            {{ exceptionLog.requestMethod }}
          </el-tag>
        </el-form-item>
        <el-form-item label="请求参数：">
          {{ exceptionLog.requestParam }}
        </el-form-item>
        <div>
          <pre>
	    <code class="language-java">{{ exceptionInfo }}</code>
	  </pre>
        </div>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteLog(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.exceptionLog
    this.listLogs()
  },
  data() {
    return {
      logs: [],
      logIds: [],
      isDelete: false,
      loading: true,
      keywords: '',
      current: 1,
      size: 10,
      count: 0,
      exceptionLog: '',
      isCheck: false,
      exceptionInfo: ''
    }
  },
  methods: {
    selectionChange(logs) {
      this.logIds = []
      logs.forEach((item) => {
        this.logIds.push(item.id)
      })
    },
    sizeChange(size) {
      this.size = size
      this.listLogs()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateExceptionLogPageState', current)
      this.listLogs()
    },
    searchLogs() {
      this.current = 1
      this.$store.commit('updateExceptionLogPageState', this.current)
      this.listLogs()
    },
    listLogs() {
      this.axios
        .get('/api/admin/exception/logs', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.logs = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    deleteLog(id) {
      var param = {}
      if (id != null) {
        param = { data: [id] }
      } else {
        param = { data: this.logIds }
      }
      this.axios.delete('/api/admin/exception/logs', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listLogs()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isDelete = false
      })
    },
    check(exceptionLog) {
      this.exceptionLog = exceptionLog
      this.exceptionInfo = '\n' + this.exceptionLog.exceptionInfo
      this.isCheck = true
      this.$nextTick(() => {
        Prism.highlightAll()
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

<style scoped></style>
