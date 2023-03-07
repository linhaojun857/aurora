<template>
  <div class="sidebar-box">
    <SubTitle :title="'titles.recent_comment'" icon="quote" />
    <ul>
      <template v-if="comments.length > 0">
        <li
          class="bg-ob-deep-900 px-2 py-3 mb-1.5 rounded-lg flex flex-row justify-items-center items-center shadow-sm hover:shadow-ob transition-shadow"
          v-for="comment in comments"
          :key="comment.id">
          <div class="flex-shrink-0 mr-2">
            <div class="rounded-full ring-gray-100 overflow-hidden shaodw-lg w-9">
              <template v-if="comment.avatar != null">
                <img class="avatar-img" :src="comment.avatar" alt="" />
              </template>
              <template v-else>
                <img class="avatar-img" :src="default" alt="" />
              </template>
            </div>
          </div>
          <div class="flex-1 text-xs comment">
            <div class="text-xs">
              <span class="text-ob pr-2">
                {{ comment.nickname }}
              </span>
              <p class="text-gray-500">{{ comment.createTime }}</p>
            </div>
            <div class="text-xs text-ob-bright commentContent">
              {{ comment.commentContent }}
            </div>
          </div>
        </li>
      </template>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, toRef } from 'vue'
import { SubTitle } from '@/components/Title'
import { useCommentStore } from '@/stores/comment'
import { useI18n } from 'vue-i18n'
import api from '@/api/api'

export default defineComponent({
  name: 'RecentComment',
  components: { SubTitle },
  setup() {
    const commentStore = useCommentStore()
    const { t } = useI18n()
    onMounted(() => {
      initRecentComment()
    })
    const initRecentComment = () => {
      api.getTopSixComments().then(({ data }) => {
        if (data.data.length === 0) {
          commentStore.recentComment = []
        }
        data.data.forEach((itme: any) => {
          itme.createTime = formatTime(itme.createTime)
        })
        commentStore.recentComment = data.data
      })
    }
    const formatTime = (time: any): any => {
      let date = new Date(time)
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      return year + '-' + month + '-' + day
    }
    return {
      comments: toRef(commentStore.$state, 'recentComment'),
      default: 'https://static.linhaojun.top/aurora/config/0af1901da1e64dfb99bb61db21e716c4.jpeg',
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.comment {
  width: 70%;
}
.commentContent {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.avatar-img {
  transition-property: transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 800ms;
  transform: rotate(-360deg);
}
.avatar-img:hover {
  transform: rotate(360deg);
}
</style>
