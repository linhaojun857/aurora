<template>
  <el-card class="main-card">
    <el-form ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="searchParams.jobName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          @keyup.enter.native="listJobs" />
      </el-form-item>
      <el-form-item label="任务组名" prop="jobGroup">
        <el-select
          @change="listJobs"
          v-model="searchParams.jobGroup"
          placeholder="请选择任务组名"
          clearable
          size="small">
          <el-option v-for="jobGroup in jobGroups" :key="jobGroup" :label="jobGroup" :value="jobGroup" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select @change="listJobs" v-model="searchParams.status" placeholder="请选择任务状态" clearable size="small">
          <el-option label="正常" :value="1" />
          <el-option label="暂停" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="listJobs">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-s-operation" size="small" @click="openLog">日志</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          size="small"
          icon="el-icon-delete"
          :disabled="jobIds.length == 0"
          @click="isDelete = true">
          批量删除
        </el-button>
      </el-col>
    </el-row>
    <div style="margin-top: 5px">
      <el-table border style="width: 100%" :data="jobs" @selection-change="selectionChange" v-loading="loading">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="任务名称" width="160" align="center" prop="jobName" :show-overflow-tooltip="true" />
        <el-table-column label="任务组名" align="center" prop="jobGroup">
          <template slot-scope="scope">
            <el-tag>
              {{ scope.row.jobGroup }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
        <el-table-column label="cron执行表达式" align="center" prop="cronExpression" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :active-value="1"
              :inactive-value="0"
              @change="changeStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="160">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateTime }}
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" width="160">
          <template slot-scope="scope">
            {{ scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleChange(scope.row.id)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" style="margin-left: 10px" @confirm="deleteJobs(scope.row.id)">
              <el-button size="mini" type="text" slot="reference">删除</el-button>
            </el-popconfirm>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
              <el-button size="mini" type="text" style="margin-left: 9px">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="handleRun" icon="el-icon-caret-right">执行一次</el-dropdown-item>
                <el-dropdown-item command="handleView" icon="el-icon-view">任务详细</el-dropdown-item>
                <el-dropdown-item command="handleJobLog" icon="el-icon-s-operation">调度日志</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>
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
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteJobs(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="800px" append-to-body>
      <el-form ref="dataForm" :model="job" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="job.jobName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组" prop="jobName">
              <el-input v-model="job.jobGroup" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：auroraQuartz.blogParams('blog')
                    <br />Class类调用示例：com.aurora.quartz.AuroraQuartz.blogParams('blog')
                    <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="job.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="job.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="错误策略" prop="misfirePolicy">
              <el-radio-group v-model="job.misfirePolicy">
                <el-radio-button :label="0">默认策略</el-radio-button>
                <el-radio-button :label="1">立即执行</el-radio-button>
                <el-radio-button :label="2">执行一次</el-radio-button>
                <el-radio-button :label="3">放弃执行</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发" prop="concurrent">
              <el-radio-group v-model="job.concurrent" size="small">
                <el-radio-button :label="0">允许</el-radio-button>
                <el-radio-button :label="1">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="job.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">暂停</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="job.remark" placeholder="备注信息" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEditOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
      <crontab @hide="openCron = false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
    <el-dialog title="任务详细" :visible.sync="openView" @closed="afterClosed" width="700px" append-to-body>
      <el-form ref="form" :model="job" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号：">{{ job.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ job.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">
              {{ job.jobGroup }}
            </el-form-item>
            <el-form-item label="创建时间：">{{ job.createTime | dateTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式：">{{ job.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次执行时间：">{{ job.nextValidTime | dateTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用目标方法：">{{ job.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态：">
              <div v-if="job.status === 1">正常</div>
              <div v-else-if="job.status === 0">暂停</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发：">
              <div v-if="job.concurrent === 1">允许</div>
              <div v-else-if="job.concurrent === 0">禁止</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略：">
              <div v-if="job.concurrent === 0">默认策略</div>
              <div v-else-if="job.concurrent === 1">立即执行</div>
              <div v-else-if="job.concurrent === 2">执行一次</div>
              <div v-else-if="job.concurrent === 3">放弃执行</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注：">
              <div>{{ job.remark }}</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import Crontab from '@/components/Crontab'
import router from '@/router'
export default {
  components: { Crontab },
  created() {
    this.current = this.$store.state.pageState.quartz
    this.listJobGroups()
    this.listJobs()
  },
  data() {
    return {
      job: {},
      copyJob: {},
      title: '',
      loading: true,
      isDelete: false,
      current: 1,
      size: 10,
      count: 0,
      dialogFormVisible: false,
      openCron: false,
      expression: '',
      openView: false,
      editOrUpdate: true,
      searchParams: {},
      jobGroups: [],
      jobs: [],
      jobIds: [],
      rules: {
        jobName: [{ required: true, message: '任务名称不能为空', trigger: 'blur' }],
        invokeTarget: [{ required: true, message: '调用目标字符串不能为空', trigger: 'blur' }],
        cronExpression: [{ required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }]
      }
    }
  },
  methods: {
    sizeChange(size) {
      this.size = size
      this.listJobs()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateQuartzPageState', current)
      this.listJobs()
    },
    listJobGroups() {
      this.axios.get('/api/admin/jobs/jobGroups').then(({ data }) => {
        this.jobGroups = data.data
      })
    },
    listJobs() {
      this.searchParams.current = this.current
      this.searchParams.size = this.size
      this.axios
        .get('/api/admin/jobs', {
          params: this.searchParams
        })
        .then(({ data }) => {
          this.jobs = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    reset() {
      this.searchParams = {}
      this.listJobs()
    },
    selectionChange(jobs) {
      this.jobIds = []
      jobs.forEach((item) => {
        this.jobIds.push(item.id)
      })
    },
    changeStatus(job) {
      this.axios
        .put('/api/admin/jobs/status', {
          id: job.id,
          status: job.status
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: '修改成功'
            })
            this.listJobs()
          } else {
            this.$notify.error({
              title: '失败',
              message: '修改失败'
            })
          }
        })
    },
    deleteJobs(id) {
      let param = {}
      if (id == null) {
        param = { data: this.jobIds }
      } else {
        param = { data: [id] }
      }
      this.axios.delete('/api/admin/jobs', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
          this.listJobs()
        } else {
          this.$notify.error({
            title: '失败',
            message: '删除失败'
          })
        }
        this.isDelete = false
      })
    },
    handleShowCron() {
      this.expression = this.job.cronExpression
      this.openCron = true
    },
    handleAdd() {
      this.editOrUpdate = false
      this.job = {}
      this.title = '新增任务'
      this.dialogFormVisible = true
    },
    handleChange(jobId) {
      this.editOrUpdate = true
      this.title = '编辑任务'
      this.axios.get('/api/admin/jobs/' + jobId).then(({ data }) => {
        this.job = data.data
      })
      this.dialogFormVisible = true
    },
    crontabFill(value) {
      this.job.cronExpression = value
    },
    handleEditOrUpdate() {
      if (this.editOrUpdate === true) {
        this.axios.put('/api/admin/jobs', this.job).then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '修改成功',
              message: data.message
            })
            this.listJobs()
          } else {
            this.$notify.error({
              title: '修改失败',
              message: data.message
            })
          }
          this.dialogFormVisible = false
        })
      } else if (this.editOrUpdate === false) {
        this.axios.post('/api/admin/jobs', this.job).then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '添加成功',
              message: data.message
            })
            this.listJobs()
          } else {
            this.$notify.error({
              title: '添加失败',
              message: data.message
            })
          }
          this.dialogFormVisible = false
        })
      }
    },
    handleCommand(command, row) {
      switch (command) {
        case 'handleRun':
          this.handleRun(row)
          break
        case 'handleView':
          this.handleView(row)
          break
        case 'handleJobLog':
          this.handleJobLog(row.id)
          break
        default:
          break
      }
    },
    handleRun(job) {
      let params = {
        id: job.id,
        jobGroup: job.jobGroup
      }
      this.axios.put('/api/admin/jobs/run', params).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '执行成功',
            message: data.message
          })
        } else {
          this.$notify.error({
            title: '执行失败',
            message: data.message
          })
        }
      })
    },
    handleView(job) {
      this.openView = true
      this.job = job
    },
    handleJobLog(jobId) {
      router.push({ path: '/quartz/log/' + jobId })
    },
    openLog() {
      router.push({ path: '/quartz/log/all' })
    },
    afterClosed() {
      this.job = ''
    }
  }
}
</script>
<style>
.el-textarea__inner {
  resize: none !important;
}
</style>
