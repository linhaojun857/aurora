<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-title-container">
      <el-input v-model="article.articleTitle" size="medium" placeholder="输入文章标题" />
      <el-button
        type="danger"
        size="medium"
        class="save-btn"
        @click="saveArticleDraft"
        v-if="article.id == null || article.status == 3">
        保存草稿
      </el-button>
      <el-button type="danger" size="medium" @click="openModel" style="margin-left: 10px"> 发布文章 </el-button>
    </div>
    <mavon-editor ref="md" v-model="article.articleContent" @imgAdd="uploadImg" style="height: calc(100vh - 260px)" />
    <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
      <div class="dialog-title-container" slot="title">发布文章</div>
      <el-form label-width="80px" size="medium" :model="article">
        <el-form-item label="文章分类">
          <el-tag
            type="success"
            v-show="article.categoryName"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeCategory">
            {{ article.categoryName }}
          </el-tag>
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="!article.categoryName">
            <div class="popover-title">分类</div>
            <el-autocomplete
              style="width: 100%"
              v-model="categoryName"
              :fetch-suggestions="searchCategories"
              placeholder="请输入分类名搜索，enter可添加自定义分类"
              :trigger-on-focus="false"
              @keyup.enter.native="saveCategory"
              @select="handleSelectCategories">
              <template slot-scope="{ item }">
                <div>{{ item.categoryName }}</div>
              </template>
            </el-autocomplete>
            <div class="popover-container">
              <div v-for="item of categorys" :key="item.id" class="category-item" @click="addCategory(item)">
                {{ item.categoryName }}
              </div>
            </div>
            <el-button type="success" plain slot="reference" size="small"> 添加分类 </el-button>
          </el-popover>
        </el-form-item>
        <el-form-item label="文章标签">
          <el-tag
            v-for="(item, index) of article.tagNames"
            :key="index"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeTag(item)">
            {{ item }}
          </el-tag>
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="article.tagNames.length < 3">
            <div class="popover-title">标签</div>
            <el-autocomplete
              style="width: 100%"
              v-model="tagName"
              :fetch-suggestions="searchTags"
              placeholder="请输入标签名搜索，enter可添加自定义标签"
              :trigger-on-focus="false"
              @keyup.enter.native="saveTag"
              @select="handleSelectTag">
              <template slot-scope="{ item }">
                <div>{{ item.tagName }}</div>
              </template>
            </el-autocomplete>
            <div class="popover-container">
              <div style="margin-bottom: 1rem">添加标签</div>
              <el-tag v-for="(item, index) of tagList" :key="index" :class="tagClass(item)" @click="addTag(item)">
                {{ item.tagName }}
              </el-tag>
            </div>
            <el-button type="primary" plain slot="reference" size="small"> 添加标签 </el-button>
          </el-popover>
        </el-form-item>
        <el-form-item label="文章类型">
          <el-select v-model="article.type" placeholder="请选择类型">
            <el-option v-for="item in typeList" :key="item.type" :label="item.desc" :value="item.type" />
          </el-select>
        </el-form-item>
        <el-form-item label="原文地址" v-if="article.type != 1">
          <el-input v-model="article.originalUrl" placeholder="请填写原文链接" />
        </el-form-item>
        <el-form-item label="上传封面">
          <el-upload
            class="upload-cover"
            drag
            action="/api/admin/articles/images"
            multiple
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="uploadCover">
            <i class="el-icon-upload" v-if="article.articleCover == ''" />
            <div class="el-upload__text" v-if="article.articleCover == ''">将文件拖到此处，或<em>点击上传</em></div>
            <img v-else :src="article.articleCover" width="360px" height="180px" />
          </el-upload>
        </el-form-item>
        <el-form-item label="置顶">
          <el-switch
            v-model="article.isTop"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0" />
        </el-form-item>
        <el-form-item label="推荐">
          <el-switch
            v-model="article.isFeatured"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0" />
        </el-form-item>
        <el-form-item label="发布形式">
          <el-radio-group v-model="article.status">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="2">密码</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="访问密码" v-if="article.status == 2">
          <el-input v-model="article.password" placeholder="请填写文章访问密码" />
        </el-form-item>
        <el-form-item label="文章摘要">
          <el-input type="textarea" autosize="true" v-model="article.articleAbstract" placeholder="默认取文章前500个字符" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateArticle"> 发 表 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  created() {
    const path = this.$route.path
    const arr = path.split('/')
    const articleId = arr[2]
    if (articleId) {
      this.axios.get('/api/admin/articles/' + articleId).then(({ data }) => {
        this.article = data.data
      })
    } else {
      const article = sessionStorage.getItem('article')
      if (article) {
        this.article = JSON.parse(article)
      }
    }
  },
  destroyed() {
    this.autoSaveArticle()
  },
  data: function () {
    return {
      addOrEdit: false,
      autoSave: true,
      categoryName: '',
      tagName: '',
      categorys: [],
      tagList: [],
      typeList: [
        {
          type: 1,
          desc: '原创'
        },
        {
          type: 2,
          desc: '转载'
        },
        {
          type: 3,
          desc: '翻译'
        }
      ],
      article: {
        id: null,
        articleTitle: this.$moment(new Date()).format('YYYY-MM-DD'),
        articleContent: '',
        articleAbstract: '',
        articleCover: '',
        categoryName: null,
        tagNames: [],
        isTop: 0,
        type: 1,
        status: 1
      },
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
    }
  },
  methods: {
    listCategories() {
      this.axios.get('/api/admin/categories/search').then(({ data }) => {
        this.categorys = data.data
      })
    },
    listTags() {
      this.axios.get('/api/admin/tags/search').then(({ data }) => {
        this.tagList = data.data
      })
    },
    openModel() {
      if (this.article.articleTitle.trim() == '') {
        this.$message.error('文章标题不能为空')
        return false
      }
      if (this.article.articleContent.trim() == '') {
        this.$message.error('文章内容不能为空')
        return false
      }
      this.listCategories()
      this.listTags()
      this.addOrEdit = true
    },
    uploadCover(response) {
      this.article.articleCover = response.data
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
    saveArticleDraft() {
      if (this.article.articleTitle.trim() == '') {
        this.$message.error('文章标题不能为空')
        return false
      }
      if (this.article.articleContent.trim() == '') {
        this.$message.error('文章内容不能为空')
        return false
      }
      this.article.status = 3
      this.axios.post('/api/admin/articles', this.article).then(({ data }) => {
        if (data.flag) {
          if (this.article.id === null) {
            this.$store.commit('removeTab', '发布文章')
          } else {
            this.$store.commit('removeTab', '修改文章')
          }
          sessionStorage.removeItem('article')
          this.$router.push({ path: '/article-list' })
          this.$notify.success({
            title: '成功',
            message: '保存草稿成功'
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: '保存草稿失败'
          })
        }
      })
      this.autoSave = false
    },
    saveOrUpdateArticle() {
      if (this.article.articleTitle.trim() == '') {
        this.$message.error('文章标题不能为空')
        return false
      }
      if (this.article.articleContent.trim() == '') {
        this.$message.error('文章内容不能为空')
        return false
      }
      if (this.article.categoryName == null) {
        this.$message.error('文章分类不能为空')
        return false
      }
      if (this.article.tagNames.length == 0) {
        this.$message.error('文章标签不能为空')
        return false
      }
      if (this.article.articleCover.trim() == '') {
        this.$message.error('文章封面不能为空')
        return false
      }
      this.axios.post('/api/admin/articles', this.article).then(({ data }) => {
        if (data.flag) {
          if (this.article.id === null) {
            this.$store.commit('removeTab', '发布文章')
          } else {
            this.$store.commit('removeTab', '修改文章')
          }
          sessionStorage.removeItem('article')
          this.$router.push({ path: '/article-list' })
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
        this.addOrEdit = false
      })
      this.autoSave = false
    },
    autoSaveArticle() {
      if (
        this.autoSave &&
        this.article.articleTitle.trim() != '' &&
        this.article.articleContent.trim() != '' &&
        this.article.id != null
      ) {
        this.axios.post('/api/admin/articles', this.article).then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: '自动保存成功'
            })
          } else {
            this.$notify.error({
              title: '失败',
              message: '自动保存失败'
            })
          }
        })
      }
      if (this.autoSave && this.article.id == null) {
        sessionStorage.setItem('article', JSON.stringify(this.article))
      }
    },
    searchCategories(keywords, cb) {
      this.axios
        .get('/api/admin/categories/search', {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data)
        })
    },
    handleSelectCategories(item) {
      this.addCategory({
        categoryName: item.categoryName
      })
    },
    saveCategory() {
      if (this.categoryName.trim() != '') {
        this.addCategory({
          categoryName: this.categoryName
        })
        this.categoryName = ''
      }
    },
    addCategory(item) {
      this.article.categoryName = item.categoryName
    },
    removeCategory() {
      this.article.categoryName = null
    },
    searchTags(keywords, cb) {
      this.axios
        .get('/api/admin/tags/search', {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data)
        })
    },
    handleSelectTag(item) {
      this.addTag({
        tagName: item.tagName
      })
    },
    saveTag() {
      if (this.tagName.trim() != '') {
        this.addTag({
          tagName: this.tagName
        })
        this.tagName = ''
      }
    },
    addTag(item) {
      if (this.article.tagNames.indexOf(item.tagName) == -1) {
        this.article.tagNames.push(item.tagName)
      }
    },
    removeTag(item) {
      const index = this.article.tagNames.indexOf(item)
      this.article.tagNames.splice(index, 1)
    }
  },
  computed: {
    tagClass() {
      return function (item) {
        const index = this.article.tagNames.indexOf(item.tagName)
        return index != -1 ? 'tag-item-select' : 'tag-item'
      }
    }
  }
}
</script>

<style scoped>
.article-title-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem;
  margin-top: 2.25rem;
}
.save-btn {
  margin-left: 0.75rem;
  background: #fff;
  color: #f56c6c;
}
.tag-item {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}
.tag-item-select {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: not-allowed;
  color: #ccccd8 !important;
}
.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}
.category-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}
</style>
