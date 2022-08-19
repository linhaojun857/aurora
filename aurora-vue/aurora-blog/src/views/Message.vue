<template>
  <div>
    <Breadcrumb :current="t('menu.message')" />
    <div class="flex flex-col">
      <div class="post-header">
        <h1 class="post-title text-white uppercase">{{ t('titles.message') }}</h1>
      </div>
      <div class="main-grid">
        <div class="relative">
          <div class="post-html" v-html="`这是一个留言版<br><br>欢迎大家前来留言💖`" />
          <Comment />
        </div>
        <div class="col-span-1">
          <Sidebar>
            <Profile />
          </Sidebar>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, reactive, toRefs, computed, provide } from 'vue'
import { useI18n } from 'vue-i18n'
import { Sidebar, Profile } from '../components/Sidebar'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { Comment } from '../components/Comment'
import { useCommentStore } from '@/stores/comment'
import api from '@/api/api'
import emitter from '@/utils/mitt'

export default defineComponent({
  name: 'Message',
  components: { Breadcrumb, Comment, Sidebar, Profile },
  setup() {
    const { t } = useI18n()
    const commentStore = useCommentStore()
    commentStore.type = 2
    const reactiveData = reactive({
      comments: '' as any
    })
    const fetchData = () => {
      fetchComments()
    }
    const fetchComments = () => {
      const params = {
        type: 2,
        topicId: null
      }
      api.getComments(params).then(({ data }) => {
        reactiveData.comments = data.data
      })
    }
    fetchData()
    emitter.on('messageFetchComment', () => {
      fetchComments()
    })
    provide(
      'comments',
      computed(() => reactiveData.comments)
    )
    onMounted(fetchData)
    return {
      ...toRefs(reactiveData),
      t
    }
  }
})
</script>
