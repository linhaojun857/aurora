<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openMenuModel(null)"> 新增 </el-button>
      <el-button
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="this.roleIds.length == 0"
        @click="isDelete = true">
        批量删除
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入角色名"
          style="width: 200px"
          @keyup.enter.native="searchRoles" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchRoles">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="roles" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="roleName" label="角色名" align="center" />
      <el-table-column prop="roleLabel" label="权限标签" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.roleName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="150" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="openMenuModel(scope.row)">
            <i class="el-icon-edit" /> 菜单权限
          </el-button>
          <el-button type="text" size="mini" @click="openResourceModel(scope.row)">
            <i class="el-icon-folder-checked" /> 资源权限
          </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left: 10px" @confirm="deleteRoles(scope.row.id)">
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
    <el-dialog :visible.sync="roleMenu" width="30%">
      <div class="dialog-title-container" slot="title" ref="roleTitle" />
      <el-form label-width="80px" size="medium" :model="roleForm">
        <el-form-item label="角色名">
          <el-input v-model="roleForm.roleName" style="width: 250px" />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree :data="menus" :default-checked-keys="roleForm.menuIds" show-checkbox node-key="id" ref="menuTree" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleMenu = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateRoleMenu"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="roleResource" width="30%" top="9vh">
      <div class="dialog-title-container" slot="title">修改资源权限</div>
      <el-form label-width="80px" size="medium" :model="roleForm">
        <el-form-item label="角色名">
          <el-input v-model="roleForm.roleName" style="width: 250px" />
        </el-form-item>
        <el-form-item label="资源权限">
          <el-tree
            :data="resources"
            :default-checked-keys="roleForm.resourceIds"
            show-checkbox
            node-key="id"
            ref="resourceTree" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleResource = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateRoleResource"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoles(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.role
    this.listRoles()
  },
  data: function () {
    return {
      loading: true,
      isDelete: false,
      roles: [],
      roleIds: [],
      keywords: null,
      current: 1,
      size: 10,
      count: 0,
      roleMenu: false,
      roleResource: false,
      resources: [],
      menus: [],
      roleForm: {
        roleName: '',
        roleLabel: '',
        resourceIds: [],
        menuIds: []
      }
    }
  },
  methods: {
    searchRoles() {
      this.current = 1
      this.listRoles()
    },
    sizeChange(size) {
      this.size = size
      this.listRoles()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateRolePageState', current)
      this.listRoles()
    },
    selectionChange(roles) {
      this.roleIds = []
      roles.forEach((item) => {
        this.roleIds.push(item.id)
      })
    },
    listRoles() {
      this.axios
        .get('/api/admin/roles', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.roles = data.data.records
          this.count = data.data.count
          this.loading = false
        })
      this.axios.get('/api/admin/role/resources').then(({ data }) => {
        this.resources = data.data
      })
      this.axios.get('/api/admin/role/menus').then(({ data }) => {
        this.menus = data.data
      })
    },
    deleteRoles(id) {
      var param = {}
      if (id == null) {
        param = { data: this.roleIds }
      } else {
        param = { data: [id] }
      }
      this.axios.delete('/api/admin/roles', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listRoles()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isDelete = false
      })
    },
    openMenuModel(role) {
      this.$nextTick(function () {
        this.$refs.menuTree.setCheckedKeys([])
      })
      this.$refs.roleTitle.innerHTML = role ? '修改角色' : '新增角色'
      if (role != null) {
        this.roleForm = JSON.parse(JSON.stringify(role))
      } else {
        this.roleForm = {
          roleName: '',
          roleLabel: '',
          resourceIds: [],
          menuIds: []
        }
      }
      this.roleMenu = true
    },
    openResourceModel(role) {
      this.$nextTick(function () {
        this.$refs.resourceTree.setCheckedKeys([])
      })
      this.roleForm = JSON.parse(JSON.stringify(role))
      this.roleResource = true
    },
    saveOrUpdateRoleResource() {
      this.roleForm.menuIds = null
      this.roleForm.resourceIds = this.$refs.resourceTree.getCheckedKeys()
      this.axios.post('/api/admin/role', this.roleForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listRoles()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.roleResource = false
      })
    },
    saveOrUpdateRoleMenu() {
      if (this.roleForm.roleName.trim() == '') {
        this.$message.error('角色名不能为空')
        return false
      }
      this.roleForm.resourceIds = null
      this.roleForm.menuIds = this.$refs.menuTree.getCheckedKeys().concat(this.$refs.menuTree.getHalfCheckedKeys())
      this.axios.post('/api/admin/role', this.roleForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listRoles()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.roleMenu = false
      })
    }
  }
}
</script>
