<template>
  <el-card class="main-card">
    <el-form :inline="true" ref="form" label-width="68px">
      <el-row>
        <el-form-item label="任务名称">
          <el-input
            style="width: 200px"
            size="small"
            v-model="searchParams.jobName"
            placeholder="请输入任务名称"
            @keyup.enter.native="listJobLogs" />
        </el-form-item>
        <el-form-item label="任务组名">
          <el-select
            size="small"
            @change="listJobLogs"
            clearable
            style="margin-left: 5px"
            v-model="searchParams.jobGroup"
            placeholder="请选择组名">
            <el-option v-for="jobGroup in jobGroups" :key="jobGroup" :label="jobGroup" :value="jobGroup" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select
            size="small"
            @change="listJobLogs"
            clearable
            style="margin-left: 5px"
            v-model="searchParams.status"
            placeholder="请选择任务状态">
            <el-option value="1" label="成功" />
            <el-option value="0" label="失败" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间">
          <el-date-picker
            size="small"
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="searchLogs">查找</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="clearSearch">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button :disabled="!jobLogIds.length" type="danger" icon="el-icon-delete" size="small" @click="deleteJobLogs"
          >批量删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="small" @click="clean">清空</el-button>
      </el-col>
    </el-row>

    <el-table
      :default-sort="{ prop: 'createTime', order: 'descending' }"
      style="width: 100%"
      :data="jobLogs"
      v-loading="loading"
      @selection-change="selectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志编号" width="80" align="center" prop="id">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="任务组名" align="center" prop="jobGroup" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.jobGroup }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="日志信息" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
      <el-table-column label="执行状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">成功</el-tag>
          <el-tag v-if="scope.row.status === 0" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="执行时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | dateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="changeOpen(scope.row)">详细</el-button>
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
    <el-dialog
      title="调度日志详细"
      :visible.sync="open"
      :width="jobLog.status == 1 ? '700px' : '80%'"
      append-to-body
      destroy-on-close>
      <el-form ref="form" :model="jobLog" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ jobLog.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ jobLog.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ jobLog.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{ jobLog.startTime | dateTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ jobLog.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ jobLog.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="jobLog.status === 1">成功</div>
              <div v-else-if="jobLog.status === 0">失败</div>
            </el-form-item>
          </el-col>
          <el-col v-if="jobLog.status == 0" :span="24">
            <div>
              <pre>
		<code class="language-java">{{ jobLog.exceptionInfo }}</code>
	      </pre>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    if (this.$route.params.quartzId == 'all') {
      this.jobId = 0
    } else if (this.$route.params.quartzId !== null) {
      this.jobId = this.$route.params.quartzId
    }
    if (this.jobId == this.$store.state.pageState.quartzLog.jobId) {
      this.current = this.$store.state.pageState.quartzLog.current
    } else {
      this.current = 1
      this.$store.commit('updateQuartzLogPageState', {
        jobId: this.jobId,
        current: this.current
      })
    }
    this.listJobLogs()
    this.listJobGroups()
  },
  data() {
    return {
      loading: true,
      current: 1,
      size: 10,
      count: 0,
      open: false,
      jobId: 0,
      jobLog: {},
      searchParams: {},
      jobGroups: [],
      jobLogIds: [],
      jobLogs: [],
      dateRange: []
    }
  },
  methods: {
    selectionChange(jobLogs) {
      this.jobLogIds = []
      jobLogs.forEach((item) => {
        this.jobLogIds.push(item.id)
      })
    },
    listJobGroups() {
      this.axios.get('/api/admin/jobLogs/jobGroups').then(({ data }) => {
        this.jobGroups = data.data
      })
    },
    listJobLogs() {
      if (this.jobId === 0) {
        this.searchParams.jobId = null
      } else {
        this.searchParams.jobId = this.jobId
      }
      this.searchParams.current = this.current
      this.searchParams.size = this.size
      this.searchParams.startTime = this.dateRange[0]
      this.searchParams.endTime = this.dateRange[1]
      this.axios
        .get('/api/admin/jobLogs', {
          params: this.searchParams
        })
        .then(({ data }) => {
          this.jobLogs = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    searchLogs() {
      this.current = 1
      this.$store.commit('updateQuartzLogPageState', {
        jobId: this.jobId,
        current: this.current
      })
      this.listJobLogs()
    },
    clearSearch() {
      this.searchParams = {}
      this.dateRange = []
      this.listJobLogs()
    },
    sizeChange(size) {
      this.size = size
      this.listJobLogs()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateQuartzLogPageState', {
        jobId: this.jobId,
        current: this.current
      })
      this.listJobLogs()
    },
    deleteJobLogs() {
      let param = {}
      param = { data: this.jobLogIds }
      this.axios.delete('/api/admin/jobLogs', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
          this.listJobLogs()
        } else {
          this.$notify.error({
            title: '失败',
            message: '删除失败'
          })
        }
      })
    },
    clean() {
      this.axios.delete('/api/admin/jobLogs/clean').then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: '清空成功'
          })
          this.listJobLogs()
        } else {
          this.$notify.error({
            title: '失败',
            message: '清空失败'
          })
        }
      })
    },
    changeOpen(jobLog) {
      this.jobLog = jobLog
      this.jobLog.exceptionInfo = '\n' + this.jobLog.exceptionInfo
      this.open = true
      this.$nextTick(() => {
        Prism.highlightAll()
      })
    }
  }
}
</script>

<style scoped></style>
