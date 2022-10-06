<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation">
      <div class="all-check">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">
          全选
        </el-checkbox>
        <div class="check-count">已选择{{ selectPhotoIds.length }}张</div>
      </div>
      <el-button
        type="success"
        @click="updatePhotoDelete(null)"
        :disabled="selectPhotoIds.length == 0"
        size="small"
        icon="el-icon-deleteItem">
        批量恢复
      </el-button>
      <el-button
        type="danger"
        @click="batchDeletePhoto = true"
        :disabled="selectPhotoIds.length == 0"
        size="small"
        icon="el-icon-deleteItem">
        批量删除
      </el-button>
    </div>
    <el-row class="photo-container" :gutter="10" v-loading="loading">
      <el-empty v-if="photos.length == 0" description="暂无照片" />
      <el-checkbox-group v-model="selectPhotoIds" @change="handleCheckedPhotoChange">
        <el-col :md="4" v-for="item of photos" :key="item.id">
          <el-checkbox :label="item.id">
            <div class="photo-item">
              <el-image fit="cover" class="photo-img" :src="item.photoSrc" :preview-photoSrc-list="photos" />
              <div class="photo-name">{{ item.photoName }}</div>
            </div>
          </el-checkbox>
        </el-col>
      </el-checkbox-group>
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
    <el-dialog :visible.sync="batchDeletePhoto" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中照片？</div>
      <div slot="footer">
        <el-button @click="batchDeletePhoto = false">取 消</el-button>
        <el-button type="primary" @click="deletePhotos"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listPhotos()
  },
  data: function () {
    return {
      loading: true,
      batchDeletePhoto: false,
      isIndeterminate: false,
      checkAll: false,
      photos: [],
      photoIds: [],
      selectPhotoIds: [],
      current: 1,
      size: 18,
      count: 0
    }
  },
  methods: {
    listPhotos() {
      this.axios
        .get('/api/admin/photos', {
          params: {
            current: this.current,
            size: this.size,
            isDelete: 1
          }
        })
        .then(({ data }) => {
          this.photos = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    sizeChange(size) {
      this.size = size
      this.listPhotos()
    },
    currentChange(current) {
      this.current = current
      this.listPhotos()
    },
    updatePhotoDelete(id) {
      var param = {}
      if (id == null) {
        param = { ids: this.selectPhotoIds, isDelete: 0 }
      } else {
        param = { ids: [id], isDelete: 0 }
      }
      this.axios.put('/api/admin/photos/delete', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listPhotos()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
      this.batchDeletePhoto = false
    },
    deletePhotos() {
      this.axios.delete('/api/admin/photos', { data: this.selectPhotoIds }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listPhotos()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
      this.batchDeletePhoto = false
    },
    handleCheckAllChange(val) {
      this.selectPhotoIds = val ? this.photoIds : []
      this.isIndeterminate = false
    },
    handleCheckedPhotoChange(value) {
      let checkedCount = value.length
      this.checkAll = checkedCount === this.photoIds.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.photoIds.length
    }
  },
  watch: {
    photos() {
      this.photoIds = []
      this.photos.forEach((item) => {
        this.photoIds.push(item.id)
      })
    }
  }
}
</script>

<style scoped>
.operation {
  display: flex;
  justify-content: flex-end;
  margin-top: 2.25rem;
  margin-bottom: 2rem;
}
.all-check {
  display: inline-flex;
  align-items: center;
  margin-right: 1rem;
}
.check-count {
  margin-left: 1rem;
  font-size: 12px;
}
.photo-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.photo-img {
  width: 100%;
  height: 7rem;
  border-radius: 4px;
}
.photo-name {
  font-size: 14px;
  margin-top: 0.3rem;
  text-align: center;
}
</style>
