<template>
  <div class="mt-5 max-w-full">
    <div class="flex space-x-3 xl:space-x-5">
      <Avatar :url="comment.avatar" />
      <div class="max-w-full-calc space-y-5">
        <div class="bg-white text-primary p-4 rounded-md relative shadow-md reply" style="width: fit-content">
          <p class="commentContent" v-html="comment.commentContent.replaceAll('\n', '<br>')" />
          <div class="flex justify-between mt-3 text-xs text-gray-400 space-x-3 md:space-x-16">
            <span>{{ comment.nickname }} | {{ time }}</span>
            <div>
              <span @click="clickOnReply" class="cursor-pointer reply-button">Reply</span>
            </div>
          </div>
        </div>
        <CommentReplyForm
          v-show="show"
          :replyUserId="comment.userId"
          :initialContent="replyContent"
          @changeShow="changeShow" />
        <transition-group name="fade">
          <CommentReplyItem
            v-for="reply in comment.replyDTOs"
            :key="reply.id"
            :reply="reply"
            :commentUserId="comment.userId" />
        </transition-group>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, toRefs, provide } from 'vue'
import Avatar from '@/components/Avatar.vue'
import CommentReplyItem from './CommentReplyItem.vue'
import CommentReplyForm from './CommentReplyForm.vue'

export default defineComponent({
  components: {
    Avatar,
    CommentReplyItem,
    CommentReplyForm
  },
  props: ['comment', 'index'],
  setup(props) {
    const comment: any = props.comment
    provide('parentId', comment.id)
    provide('index', props.index)
    const formatTime = (time: any): any => {
      let date = new Date(time)
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      return year + '-' + month + '-' + day
    }
    const reactiveData = reactive({
      replyContent: '' as any,
      time: formatTime(props.comment.createTime) as any,
      show: false as any
    })
    const changeShow = () => {
      reactiveData.show = false
    }
    const clickOnReply = () => {
      reactiveData.replyContent = 'add reply...'
      reactiveData.show = true
    }
    return {
      ...toRefs(reactiveData),
      clickOnReply,
      changeShow
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
.reply {
  background: var(--background-primary);
}
.reply-button {
  color: var(--text-accent);
}
.commentContent {
  line-height: 26px;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
</style>
