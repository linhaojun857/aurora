<template>
  <div class="flex space-x-3 xl:space-x-5">
    <Avatar :url="avatar" />
    <div class="comment flex flex-col flex-wrap-reverse w-full max-w-full-calc">
      <textarea
        v-model="commentContent"
        class="w-full shadow-md rounded-md p-4 focus:outline-none input"
        placeholder="Add comment..."
        cols="30"
        rows="5" />
      <div class="justify-between" style="text-align: right">
        <button
          @click="saveComment"
          id="submit-button"
          class="mt-5 w-32 text-white p-2 rounded-lg shadow-lg transition transform hover:scale-105 flex float-right">
          <span class="text-center flex-grow commit">Add Comment</span>
        </button>
      </div>
      <div class="w-full border-b-2 mt-6 wire"></div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, toRefs, reactive, getCurrentInstance, computed } from 'vue'
import Avatar from '@/components/Avatar.vue'
import { SubTitle } from '@/components/Title'
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'
import { useCommentStore } from '@/stores/comment'
import { useAppStore } from '@/stores/app'
import api from '@/api/api'
import emitter from '@/utils/mitt'

export default defineComponent({
  name: 'CommentItem',
  components: { SubTitle, Avatar },
  setup() {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const userStore = useUserStore()
    const commentStore = useCommentStore()
    const appStore = useAppStore()
    const route = useRoute()
    const reactiveData = reactive({
      commentContent: '' as any
    })
    const saveComment = () => {
      if (userStore.userInfo === '') {
        proxy.$notify({
          title: 'Warning',
          message: '请登录后评论',
          type: 'warning'
        })
        return
      }
      if (reactiveData.commentContent.trim() == '') {
        proxy.$notify({
          title: 'Warning',
          message: '评论不能为空',
          type: 'warning'
        })
        return
      }
      const path = route.path
      const arr = path.split('/')
      const params: any = {
        commentContent: reactiveData.commentContent,
        type: commentStore.type
      }
      params.topicId = arr[2]
      api.saveComment(params).then(({ data }) => {
        if (data.flag) {
          fetchComments()
          let isCommentReview = appStore.websiteConfig.isCommentReview
          if (isCommentReview) {
            proxy.$notify({
              title: 'Warning',
              message: '评论成功,正在审核中',
              type: 'warning'
            })
          } else {
            proxy.$notify({
              title: 'Success',
              message: '评论成功',
              type: 'success'
            })
          }
          reactiveData.commentContent = ''
        }
      })
    }
    const fetchComments = () => {
      switch (commentStore.type) {
        case 1:
          emitter.emit('articleFetchComment')
          break
        case 2:
          emitter.emit('messageFetchComment')
          break
        case 3:
          emitter.emit('aboutFetchComment')
          break
        case 4:
          emitter.emit('friendLinkFetchComment')
          break
        case 5:
          emitter.emit('talkFetchComment')
      }
    }
    return {
      ...toRefs(reactiveData),
      avatar: computed(() => userStore.userInfo.avatar),
      saveComment
    }
  }
})
</script>

<style lang="scss" scoped>
.input {
  background: var(--background-primary);
  resize: none;
}
#submit-button {
  outline: none;
  background: var(--main-gradient);
}
.wire {
  border-color: var(--text-normal);
}
</style>
