<template>
  <div>
    <div>
      <div class="flex flex-col">
        <div class="post-header">
          <h1 class="post-title text-white uppercase">{{ t('titles.talks') }}</h1>
        </div>
        <div class="main-grid">
          <div class="relative space-y-5">
            <div class="bg-ob-deep-800 flex p-4 lg:p-8 rounded-2xl shadow-xl mb-8 lg:mb-0">
              <el-avatar :size="45" :src="talk.avatar" />
              <div class="talk-info">
                <div class="user-nickname text-sm">
                  {{ talk.nickname }}
                </div>
                <div class="time">
                  {{ t('settings.shared-on') }}
                  {{ formatTime(talk.createTime) }},
                  {{ t(`settings.months[${new Date(talk.createTime).getMonth()}]`) }}
                  {{ new Date(talk.createTime).getDate() }}, {{ new Date(talk.createTime).getFullYear() }}
                  <svg-icon icon-class="message" class="message-svg" />{{
                    talk.commentCount == null ? 0 : talk.commentCount
                  }}
                </div>
                <div class="talk-content" v-html="talk.content" />
                <el-row class="talk-images" v-if="talk.imgs">
                  <el-col :md="4" v-for="(img, index) of talk.imgs" :key="index">
                    <el-image
                      class="images-talks"
                      :src="img"
                      aspect-ratio="1"
                      max-height="200"
                      @click.prevent="handlePreview(img)" />
                  </el-col>
                </el-row>
              </div>
            </div>
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
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, toRefs, provide, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { Sidebar, Profile } from '../components/Sidebar'
import { Comment } from '../components/Comment'
import { useCommentStore } from '@/stores/comment'
import { v3ImgPreviewFn } from 'v3-img-preview'
import { useRoute } from 'vue-router'
import emitter from '@/utils/mitt'
import api from '@/api/api'

export default defineComponent({
  name: 'talks',
  components: { Breadcrumb, Sidebar, Profile, Comment },
  setup() {
    const { t } = useI18n()
    const commentStore = useCommentStore()
    commentStore.type = 5
    const route = useRoute()
    const reactiveData = reactive({
      talk: '' as any,
      comments: [] as any,
      haveMore: false as any,
      isReload: false as any,
      images: [] as any
    })
    const pageInfo = reactive({
      current: 1,
      size: 7
    })
    const handlePreview = (index: any) => {
      v3ImgPreviewFn({ images: reactiveData.images, index: reactiveData.images.indexOf(index) })
    }
    const formatTime = (data: any): string => {
      let hours = new Date(data).getHours()
      let minutes = new Date(data).getMinutes()
      let seconds = new Date(data).getSeconds()
      return hours + ':' + minutes + ':' + seconds
    }
    const fetchTalk = () => {
      api.getTalkById(route.params.talkId).then(({ data }) => {
        reactiveData.talk = data.data
        if (reactiveData.talk.imgs) {
          reactiveData.images.push(...reactiveData.talk.imgs)
        }
      })
    }
    const path = route.path
    const arr = path.split('/')
    const fetchComments = () => {
      const params = {
        type: 5,
        topicId: arr[2],
        current: pageInfo.current,
        size: pageInfo.size
      }
      api.getComments(params).then(({ data }) => {
        if (reactiveData.isReload) {
          reactiveData.comments = data.data.records
          reactiveData.isReload = false
        } else {
          reactiveData.comments.push(...data.data.records)
        }
        if (data.data.count <= reactiveData.comments.length) {
          reactiveData.haveMore = false
        } else {
          reactiveData.haveMore = true
        }
        pageInfo.current++
      })
    }
    const fetchReplies = (index: any) => {
      api.getRepliesByCommentId(reactiveData.comments[index].id).then(({ data }) => {
        reactiveData.comments[index].replyDTOs = data.data
      })
    }
    provide(
      'comments',
      computed(() => reactiveData.comments)
    )
    provide(
      'haveMore',
      computed(() => reactiveData.haveMore)
    )
    emitter.on('talkFetchComment', () => {
      pageInfo.current = 1
      reactiveData.isReload = true
      fetchComments()
    })
    emitter.on('talkFetchReplies', (index) => {
      fetchReplies(index)
    })
    emitter.on('talkLoadMore', () => {
      fetchComments()
    })
    const fetchData = () => {
      fetchTalk()
      fetchComments()
    }
    onMounted(fetchData)
    return {
      ...toRefs(reactiveData),
      handlePreview,
      formatTime,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.message-svg {
  margin-left: 5px;
  font-size: 15px;
}
.el-card {
  background: var(--background-primary);
  border-radius: 10px;
  border: 0;
}
.talk-user-avatar {
  flex: 1;
}
.talk-info {
  flex: 1;
  margin-left: 10px;
}
.user-nickname {
  font-weight: 530;
}
.time {
  color: #999;
  margin-top: 6px;
  font-size: 13px;
}
.talk-content {
  margin-top: 10px;
  font-size: 14px;
  line-height: 26px;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.talk-images {
  margin-top: 8px;
}
.images-items {
  cursor: pointer;
  border-radius: 3px;
  margin-right: 5px;
}
</style>
