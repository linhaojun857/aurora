<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="album-info">
      <el-image fit="cover" class="album-cover" :src="albumInfo.albumCover" />
      <div class="album-detail">
        <div style="margin-bottom: 0.6rem">
          <span class="album-name">{{ albumInfo.albumName }}</span>
          <span class="photo-count">{{ albumInfo.photoCount }}张</span>
        </div>
        <div>
          <span v-if="albumInfo.albumDesc" class="album-desc">
            {{ albumInfo.albumDesc }}
          </span>
          <el-button icon="el-icon-picture" type="primary" size="small" @click="uploadPhoto = true">
            上传照片
          </el-button>
        </div>
      </div>
      <div class="operation">
        <div class="all-check">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">
            全选
          </el-checkbox>
          <div class="check-count">已选择{{ selectphotoIds.length }}张</div>
        </div>
        <el-button
          type="success"
          @click="movePhoto = true"
          :disabled="selectphotoIds.length == 0"
          size="small"
          icon="el-icon-deleteItem">
          移动到
        </el-button>
        <el-button
          type="danger"
          @click="batchDeletePhoto = true"
          :disabled="selectphotoIds.length == 0"
          size="small"
          icon="el-icon-deleteItem">
          批量删除
        </el-button>
      </div>
    </div>
    <el-row class="photo-container" :gutter="10" v-loading="loading">
      <el-empty v-if="photos.length == 0" description="暂无照片" />
      <el-checkbox-group v-model="selectphotoIds" @change="handleCheckedPhotoChange">
        <el-col :md="4" v-for="item of photos" :key="item.id">
          <el-checkbox :label="item.id">
            <div class="photo-item">
              <div class="photo-opreation">
                <el-dropdown @command="handleCommand">
                  <i class="el-icon-more" style="color: #fff" />
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="JSON.stringify(item)">
                      <i class="el-icon-edit" />编辑
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
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
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      layout="prev, pager, next" />
    <el-dialog :visible.sync="uploadPhoto" width="70%" top="10vh">
      <div class="dialog-title-container" slot="title">上传照片</div>
      <div class="upload-container">
        <el-upload
          v-show="uploads.length > 0"
          action="/api/admin/photos/upload"
          list-type="picture-card"
          :file-list="uploads"
          multiple
          :headers="headers"
          :before-upload="beforeUpload"
          :on-success="upload"
          :on-remove="handleRemove">
          <i class="el-icon-plus" />
        </el-upload>
        <div class="upload">
          <el-upload
            v-show="uploads.length == 0"
            drag
            action="/api/admin/photos/upload"
            multiple
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="upload"
            :show-file-list="false">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">支持上传jpg/png文件</div>
          </el-upload>
        </div>
      </div>
      <div slot="footer">
        <div class="upload-footer">
          <div class="upload-count">共上传{{ uploads.length }}张照片</div>
          <div style="margin-left: auto">
            <el-button @click="uploadPhoto = false">取 消</el-button>
            <el-button @click="savePhotos" type="primary" :disabled="uploads.length == 0"> 开始上传 </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editPhoto" width="30%">
      <div class="dialog-title-container" slot="title">修改信息</div>
      <el-form label-width="80px" size="medium" :model="photoForm">
        <el-form-item label="照片名称">
          <el-input style="width: 220px" v-model="photoForm.photoName" />
        </el-form-item>
        <el-form-item label="照片描述">
          <el-input style="width: 220px" v-model="photoForm.photoDesc" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editPhoto = false">取 消</el-button>
        <el-button type="primary" @click="updatePhoto"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="batchDeletePhoto" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中照片？</div>
      <div slot="footer">
        <el-button @click="batchDeletePhoto = false">取 消</el-button>
        <el-button type="primary" @click="updatePhotoDelete(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="movePhoto" width="30%">
      <div class="dialog-title-container" slot="title">移动照片</div>
      <el-empty v-if="albumList.length < 2" description="暂无其他相册" />
      <el-form v-else label-width="80px" size="medium" :model="photoForm">
        <el-radio-group v-model="albumId">
          <div class="album-check-item">
            <template v-for="item of albumList">
              <el-radio v-if="item.id != albumInfo.id" :key="item.id" :label="item.id" style="margin-bottom: 1rem">
                <div class="album-check">
                  <el-image fit="cover" class="album-check-cover" :src="item.albumCover" />
                  <div style="margin-left: 0.5rem">{{ item.albumName }}</div>
                </div>
              </el-radio>
            </template>
          </div>
        </el-radio-group>
      </el-form>
      <div slot="footer">
        <el-button @click="movePhoto = false">取 消</el-button>
        <el-button :disabled="albumId == null" type="primary" @click="updatePhotoAlbum"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  created() {
    this.albumId = this.$route.params.albumId
    if (this.albumId == this.$store.state.pageState.photo.albumId) {
      this.current = this.$store.state.pageState.photo.current
    } else {
      this.current = 1
      this.$store.commit('updatePhotoPageState', {
        albumId: this.$route.params.albumId,
        current: this.current
      })
    }
    this.getAlbumInfo()
    this.listAlbums()
    this.listPhotos()
  },
  data: function () {
    return {
      loading: true,
      checkAll: false,
      isIndeterminate: false,
      uploadPhoto: false,
      editPhoto: false,
      movePhoto: false,
      batchDeletePhoto: false,
      uploads: [],
      photos: [],
      photoIds: [],
      selectphotoIds: [],
      albumList: [],
      albumInfo: {
        id: null,
        albumName: '',
        albumDesc: '',
        albumCover: '',
        photoCount: 0
      },
      photoForm: {
        id: null,
        photoName: '',
        photoDesc: ''
      },
      albumId: null,
      current: 1,
      size: 18,
      count: 0,
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
    }
  },
  methods: {
    getAlbumInfo() {
      this.axios.get('/api/admin/photos/albums/' + this.$route.params.albumId + '/info').then(({ data }) => {
        this.albumInfo = data.data
      })
    },
    listAlbums() {
      this.axios.get('/api/admin/photos/albums/info').then(({ data }) => {
        this.albumList = data.data
      })
    },
    listPhotos() {
      this.axios
        .get('/api/admin/photos', {
          params: {
            current: this.current,
            size: this.size,
            albumId: this.$route.params.albumId,
            isDelete: 0
          }
        })
        .then(({ data }) => {
          this.photos = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updatePhotoPageState', {
        albumId: this.$route.params.albumId,
        current: this.current
      })
      this.listPhotos()
    },
    savePhotos() {
      var photoUrls = []
      this.uploads.forEach((item) => {
        photoUrls.push(item.url)
      })
      this.axios
        .post('/api/admin/photos', {
          albumId: this.$route.params.albumId,
          photoUrls: photoUrls
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: data.message
            })
            this.uploads = []
            this.listPhotos()
            this.getAlbumInfo()
          } else {
            this.$notify.error({
              title: '失败',
              message: data.message
            })
          }
        })
      this.uploadPhoto = false
    },
    updatePhoto() {
      if (this.photoForm.photoName.trim() == '') {
        this.$message.error('照片名称不能为空')
        return false
      }
      this.axios.put('/api/admin/photos', this.photoForm).then(({ data }) => {
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
        this.editPhoto = false
      })
    },
    updatePhotoAlbum() {
      this.axios
        .put('/api/admin/photos/album', {
          albumId: this.albumId,
          photoIds: this.selectphotoIds
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: data.message
            })
            this.getAlbumInfo()
            this.listPhotos()
          } else {
            this.$notify.error({
              title: '失败',
              message: data.message
            })
          }
          this.movePhoto = false
        })
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
    handleCheckAllChange(val) {
      this.selectphotoIds = val ? this.photoIds : []
      this.isIndeterminate = false
    },
    handleCheckedPhotoChange(value) {
      let checkedCount = value.length
      this.checkAll = checkedCount === this.photoIds.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.photoIds.length
    },
    handleCommand(command) {
      this.photoForm = JSON.parse(command)
      this.editPhoto = true
    },
    updatePhotoDelete(id) {
      var param = {}
      if (id == null) {
        param = { ids: this.selectphotoIds, isDelete: 1 }
      } else {
        param = { ids: [id], isDelete: 1 }
      }
      this.axios.put('/api/admin/photos/delete', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listPhotos()
          this.getAlbumInfo()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
      this.batchDeletePhoto = false
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
.album-info {
  display: flex;
  margin-top: 2.25rem;
  margin-bottom: 2rem;
}
.album-cover {
  border-radius: 4px;
  width: 5rem;
  height: 5rem;
}
.album-check-cover {
  border-radius: 4px;
  width: 4rem;
  height: 4rem;
}
.album-detail {
  padding-top: 0.4rem;
  margin-left: 0.8rem;
}
.album-desc {
  font-size: 14px;
  margin-right: 0.8rem;
}
.operation {
  padding-top: 1.5rem;
  margin-left: auto;
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
.album-name {
  font-size: 1.25rem;
}
.photo-count {
  font-size: 12px;
  margin-left: 0.5rem;
}
.photo-item {
  width: 100%;
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
.upload-container {
  height: 400px;
}
.upload {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.upload-footer {
  display: flex;
  align-items: center;
}
.photo-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.3rem;
  right: 0.5rem;
}
.album-check {
  display: flex;
  align-items: center;
}
</style>
