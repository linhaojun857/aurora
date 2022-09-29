<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openModel(null)"> 新增模块 </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入资源名"
          style="width: 200px"
          @keyup.enter.native="listResources" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="listResources">
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="resources"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column prop="resourceName" label="资源名" width="220" />
      <el-table-column prop="url" label="资源路径" width="300" />
      <el-table-column prop="requetMethod" label="请求方式">
        <template slot-scope="scope" v-if="scope.row.requestMethod">
          <el-tag :type="tagType(scope.row.requestMethod)">
            {{ scope.row.requestMethod }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isAnonymous" label="匿名访问" align="center">
        <template slot-scope="scope">
          <el-switch
            v-if="scope.row.url"
            v-model="scope.row.isAnonymous"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
            @change="changeResource(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="openAddResourceModel(scope.row)" v-if="scope.row.children">
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button type="text" size="mini" @click="openEditResourceModel(scope.row)">
            <i class="el-icon-edit" /> 修改
          </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left: 10px" @confirm="deleteResource(scope.row.id)">
            <el-button size="mini" type="text" slot="reference"> <i class="el-icon-delete" /> 删除 </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addModule" width="30%">
      <div class="dialog-title-container" slot="title" ref="moduleTitle" />
      <el-form label-width="80px" size="medium" :model="resourceForm">
        <el-form-item label="模块名">
          <el-input v-model="resourceForm.resourceName" style="width: 220px" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addModule = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditResource"> 确 定 </el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="addResource" width="30%">
      <div class="dialog-title-container" slot="title" ref="resourceTitle" />
      <el-form label-width="80px" size="medium" :model="resourceForm">
        <el-form-item label="资源名">
          <el-input v-model="resourceForm.resourceName" style="width: 220px" />
        </el-form-item>
        <el-form-item label="资源路径">
          <el-input v-model="resourceForm.url" style="width: 220px" />
        </el-form-item>
        <el-form-item label="请求方式">
          <el-radio-group v-model="resourceForm.requestMethod">
            <el-radio :label="'GET'">GET</el-radio>
            <el-radio :label="'POST'">POST</el-radio>
            <el-radio :label="'PUT'">PUT</el-radio>
            <el-radio :label="'DELETE'">DELETE</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addResource = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditResource"> 确 定 </el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listResources()
  },
  data() {
    return {
      loading: true,
      keywords: '',
      resources: [],
      addModule: false,
      addResource: false,
      resourceForm: {}
    }
  },
  methods: {
    listResources() {
      this.axios
        .get('/api/admin/resources', {
          params: {
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.resources = data.data
          this.loading = false
        })
    },
    changeResource(resource) {
      this.axios.post('/api/admin/resources', resource).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listResources()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
    },
    openModel(resource) {
      if (resource != null) {
        this.resourceForm = JSON.parse(JSON.stringify(resource))
        this.$refs.moduleTitle.innerHTML = '修改模块'
      } else {
        this.resourceForm = {}
        this.$refs.moduleTitle.innerHTML = '添加模块'
      }
      this.addModule = true
    },
    openEditResourceModel(resource) {
      if (resource.url == null) {
        this.openModel(resource)
        return false
      }
      this.resourceForm = JSON.parse(JSON.stringify(resource))
      this.$refs.resourceTitle.innerHTML = '修改资源'
      this.addResource = true
    },
    openAddResourceModel(resource) {
      console.log(resource)
      this.resourceForm = {}
      this.resourceForm.parentId = resource.id
      this.$refs.resourceTitle.innerHTML = '添加资源'
      this.addResource = true
    },
    deleteResource(id) {
      this.axios.delete('/api/admin/resources/' + id).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listResources()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
    },
    addOrEditResource() {
      if (this.resourceForm.resourceName.trim() == '') {
        this.$message.error('资源名不能为空')
        return false
      }
      this.axios.post('/api/admin/resources', this.resourceForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listResources()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.addModule = false
        this.addResource = false
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
