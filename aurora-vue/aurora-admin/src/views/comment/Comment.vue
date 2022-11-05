<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="review-menu">
      <span>状态</span>
      <span @click="changeReview(null)" :class="isReview == null ? 'active-review' : 'review'"> 全部 </span>
      <span @click="changeReview(1)" :class="isReview == 1 ? 'active-review' : 'review'"> 正常 </span>
      <span @click="changeReview(0)" :class="isReview == 0 ? 'active-review' : 'review'"> 审核中 </span>
    </div>
    <div class="operation-container">
      <el-button
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="commentIds.length == 0"
        @click="remove = true">
        批量删除
      </el-button>
      <el-button
        type="success"
        size="small"
        icon="el-icon-success"
        :disabled="commentIds.length == 0"
        @click="updateCommentReview(null)">
        批量通过
      </el-button>
      <div style="margin-left: auto">
        <el-select clearable v-model="type" placeholder="请选择来源" size="small" style="margin-right: 1rem">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="searchComments" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchComments">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="comments" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="avatar" label="头像" align="center" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="评论人" align="center" width="120" />
      <el-table-column prop="replyNickname" label="回复人" align="center" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.replyNickname">
            {{ scope.row.replyNickname }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column prop="articleTitle" label="文章标题" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.articleTitle">
            {{ scope.row.articleTitle }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column prop="commentContent" label="评论内容" align="center">
        <template slot-scope="scope">
          <span v-html="scope.row.commentContent" class="comment-content" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="评论时间" width="150" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="isReview" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isReview == 0" type="warning">审核中</el-tag>
          <el-tag v-if="scope.row.isReview == 1" type="success">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="来源" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == 1">文章</el-tag>
          <el-tag v-if="scope.row.type == 2" type="danger">留言</el-tag>
          <el-tag v-if="scope.row.type == 3" type="success">关于我</el-tag>
          <el-tag v-if="scope.row.type == 4" type="warning">友链</el-tag>
          <el-tag v-if="scope.row.type == 5" type="warning">说说</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isReview == 0"
            size="mini"
            type="success"
            slot="reference"
            @click="updateCommentReview(scope.row.id)">
            通过
          </el-button>
          <el-popconfirm style="margin-left: 10px" title="确定删除吗？" @confirm="deleteComments(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"> 删除 </el-button>
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
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteComments(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.comment
    this.listComments()
  },
  data: function () {
    return {
      loading: true,
      remove: false,
      options: [
        {
          value: 1,
          label: '文章'
        },
        {
          value: 2,
          label: '留言'
        },
        {
          value: 3,
          label: '关于我'
        },
        {
          value: 4,
          label: '友链'
        }
      ],
      comments: [],
      commentIds: [],
      type: null,
      keywords: null,
      isReview: null,
      current: 1,
      size: 10,
      count: 0
    }
  },
  methods: {
    selectionChange(comments) {
      this.commentIds = []
      comments.forEach((item) => {
        this.commentIds.push(item.id)
      })
    },
    searchComments() {
      this.current = 1
      this.listComments()
    },
    sizeChange(size) {
      this.size = size
      this.listComments()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateCommentPageState', current)
      this.listComments()
    },
    changeReview(review) {
      this.current = 1
      this.isReview = review
    },
    updateCommentReview(id) {
      let param = {}
      if (id != null) {
        param.ids = [id]
      } else {
        param.ids = this.commentIds
      }
      param.isReview = 1
      this.axios.put('/api/admin/comments/review', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listComments()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
      })
    },
    deleteComments(id) {
      var param = {}
      if (id == null) {
        param = { data: this.commentIds }
      } else {
        param = { data: [id] }
      }
      this.axios.delete('/api/admin/comments', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listComments()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.remove = false
      })
    },
    listComments() {
      this.axios
        .get('/api/admin/comments', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords,
            type: this.type,
            isReview: this.isReview
          }
        })
        .then(({ data }) => {
          this.comments = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    }
  },
  watch: {
    isReview() {
      this.current = 1
      this.listComments()
    },
    type() {
      this.current = 1
      this.listComments()
    }
  }
}
</script>

<style scoped>
.comment-content {
  display: inline-block;
}
.operation-container {
  margin-top: 1.5rem;
}
.review-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}
.review-menu span {
  margin-right: 24px;
}
.review {
  cursor: pointer;
}
.active-review {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}
</style>
