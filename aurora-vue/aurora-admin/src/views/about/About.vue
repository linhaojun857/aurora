<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <mavon-editor
      ref="md"
      @imgAdd="uploadImg"
      v-model="aboutContent"
      style="height: calc(100vh - 250px); margin-top: 2.25rem" />
    <el-button type="danger" size="medium" class="edit-btn" @click="updateAbout"> 修改 </el-button>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  created() {
    this.getAbout()
  },
  data: function () {
    return {
      aboutContent: ''
    }
  },
  methods: {
    getAbout() {
      this.axios.get('/api/about').then(({ data }) => {
        this.aboutContent = data.data.content
      })
    },
    uploadImg(pos, file) {
      var formdata = new FormData()
      if (file.size / 1024 < this.config.UPLOAD_SIZE) {
        formdata.append('file', file)
        this.axios.post('/api/admin/articles/images', formdata).then(({ data }) => {
          this.$refs.md.$img2Url(pos, data.data)
        })
      } else {
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          formdata.append('file', new window.File([res], file.name, { type: file.type }))
          this.axios.post('/api/admin/articles/images', formdata).then(({ data }) => {
            this.$refs.md.$img2Url(pos, data.data)
          })
        })
      }
    },
    updateAbout() {
      this.axios
        .put('/api/admin/about', {
          content: this.aboutContent
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: data.message
            })
          } else {
            this.$notify.error({
              title: '失败',
              message: data.message
            })
          }
        })
    }
  }
}
</script>

<style scoped>
.edit-btn {
  float: right;
  margin: 1rem 0;
}
</style>
