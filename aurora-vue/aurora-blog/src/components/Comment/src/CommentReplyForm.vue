<template>
  <div class="flex space-x-3 xl:space-x-5">
    <Avatar :url="avatar" />
    <div class="reply flex flex-col flex-wrap-reverse w-full max-w-full-calc" style="width: fit-content">
      <textarea
        v-model="commentContent"
        class="w-full shadow-md rounded-md p-4 focus:outline-none input"
        :placeholder="initialContent"
        cols="30"
        rows="5" />
      <div class="justify-between" style="text-align: right">
        <button
          @click="saveReply"
          id="submit-button"
          class="mt-5 w-16 text-white p-2 rounded-lg shadow-lg transition transform hover:scale-105 flex float-right">
          <span class="text-center flex-grow commit">Reply</span>
        </button>
        <button
          @click="CancelReply"
          id="submit-button"
          class="mt-5 mr-3 w-16 text-white p-2 rounded-lg shadow-lg transition transform hover:scale-105 flex float-right">
          <span class="text-center flex-grow commit">Cancel</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, reactive, getCurrentInstance, inject, computed } from 'vue'
import Avatar from '@/components/Avatar.vue'
import emitter from '@/utils/mitt'
import { useUserStore } from '@/stores/user'
import { useCommentStore } from '@/stores/comment'
import { useAppStore } from '@/stores/app'
import { useRoute } from 'vue-router'
import api from '@/api/api'

export default defineComponent({
  components: {
    Avatar
  },
  props: ['replyUserId', 'initialContent'],
  setup(props, { emit }) {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const userStore = useUserStore()
    const commentStore = useCommentStore()
    const appStore = useAppStore()
    const route = useRoute()
    const reactiveData = reactive({
      commentContent: '' as any
    })
    const parentId = inject('parentId')
    const index = inject('index')
    const saveReply = () => {
      if (userStore.userInfo === '') {
        proxy.$notify({
          title: 'Warning',
          message: '请登录后回复',
          type: 'warning'
        })
        return
      }
      if (reactiveData.commentContent.trim() == '') {
        proxy.$notify({
          title: 'Warning',
          message: '回复不能为空',
          type: 'warning'
        })
        return
      }
      const path = route.path
      const arr = path.split('/')
      const params: any = {
        type: commentStore.type,
        replyUserId: props.replyUserId,
        parentId: parentId,
        commentContent: reactiveData.commentContent
      }
      params.topicId = arr[2]
      api.saveComment(params).then(({ data }) => {
        if (data.flag) {
          emit('changeShow')
          fetchReplies()
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
              message: '回复成功',
              type: 'success'
            })
          }
          reactiveData.commentContent = ''
        } else {
          proxy.$notify({
            title: 'Error',
            message: data.message,
            type: 'error'
          })
        }
      })
    }
    const fetchReplies = async () => {
      switch (commentStore.type) {
        case 1:
          emitter.emit('articleFetchReplies', index)
          break
        case 2:
          emitter.emit('messageFetchReplies', index)
          break
        case 3:
          emitter.emit('aboutFetchReplies', index)
          break
        case 4:
          emitter.emit('friendLinkFetchReplies', index)
          break
        case 5:
          emitter.emit('talkFetchReplies', index)
      }
    }
    const CancelReply = () => {
      emit('changeShow')
    }
    return {
      ...toRefs(reactiveData),
      avatar: computed(() => userStore.userInfo.avatar),
      saveReply,
      CancelReply
    }
  }
})
</script>
<style lang="scss" scoped>
.reply::before {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  border-right: 8px solid var(--background-primary);
  border-top: 6px solid transparent;
  border-bottom: 6px solid transparent;
  left: -8px;
  top: 14px;
}
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
