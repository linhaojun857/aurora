<template>
  <div class="flex flex-col">
    <div class="main-grid">
      <div class="post-header">
        <span class="post-labels">
          <ob-skeleton v-if="loading" tag="b" height="20px" width="35px" />
          <b v-else-if="!loading && article.categoryName">
            <span>{{ article.categoryName }}</span>
          </b>
          <b v-else>{{ t('settings.default-category') }}</b>
          <ul>
            <ob-skeleton v-if="loading" :count="2" tag="li" height="16px" width="35px" class="mr-2" />
            <template v-else-if="!loading && article.tags && article.tags.length > 0">
              <li v-for="tag in article.tags" :key="tag.id">
                <em class="opacity-50">#</em>
                {{ tag.tagName }}
              </li>
            </template>
            <template v-else>
              <li>
                <b class="opacity-50">#</b>
                {{ t('settings.default-tag') }}
              </li>
            </template>
          </ul>
        </span>
        <h1 v-if="article.articleTitle" class="post-title text-white">
          {{ article.articleTitle }}
        </h1>
        <ob-skeleton
          v-else
          class="post-title text-white uppercase"
          width="100%"
          height="clamp(1.2rem, calc(1rem + 3.5vw), 4rem)" />
        <div class="flex flex-row items-center justify-start mt-8 mb-4">
          <div class="post-footer" v-if="article.author">
            <img
              class="hover:opacity-50 cursor-pointer"
              v-lazy="article.author.avatar || ''"
              alt="author avatar"
              @click="handleAuthorClick(article.author.website)" />
            <span class="text-white opacity-80">
              <strong
                class="text-white pr-1.5 hover:opacity-50 cursor-pointer"
                @click="handleAuthorClick(article.author.website)">
                {{ article.author.nickname }}
              </strong>
              <span class="opacity-70">
                {{ t('settings.shared-on') }} {{ t(`settings.months[${new Date(article.createTime).getMonth()}]`) }}
                {{ new Date(article.createTime).getDate() }}, {{ new Date(article.createTime).getFullYear() }}
              </span>
            </span>
          </div>
          <div class="post-footer" v-else>
            <div class="flex flex-row items-center">
              <ob-skeleton class="mr-2" height="28px" width="28px" :circle="true" />
              <span class="text-ob-dim mt-1">
                <ob-skeleton height="20px" width="150px" />
              </span>
            </div>
          </div>
          <div class="post-stats" v-if="wordNum !== '' && readTime !== ''">
            <span>
              <svg-icon icon-class="text-outline" style="stroke: white" />
              <span class="pl-2 opacity-70">
                {{ wordNum }}
              </span>
            </span>
            <span>
              <svg-icon icon-class="clock-outline" style="stroke: white" />
              <span class="pl-2 opacity-70">
                {{ readTime }}
              </span>
            </span>
          </div>
          <div v-else class="post-stats">
            <span>
              <svg-icon icon-class="clock" />
              <span class="pl-2">
                <ob-skeleton width="40px" height="16px" />
              </span>
            </span>
            <span>
              <svg-icon icon-class="text" />
              <span class="pl-2">
                <ob-skeleton width="40px" height="16px" />
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div class="main-grid">
      <div>
        <template v-if="article.articleContent">
          <div class="post-html">
            <div class="markdown-body" ref="articleRef" v-html="article.articleContent" />
          </div>
        </template>
        <div v-else class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block min-h-screen">
          <ob-skeleton tag="div" :count="1" height="36px" width="150px" class="mb-6" />
          <br />
          <ob-skeleton tag="div" :count="35" height="16px" width="100px" class="mr-2" />
          <br />
          <br />
          <ob-skeleton tag="div" :count="25" height="16px" width="100px" class="mr-2" />
        </div>
        <div class="flex flex-col lg:flex-row justify-start items-end my-8 my-gap">
          <div class="w-full h-full self-stretch mr-0 lg:mr-4" v-if="preArticleCard">
            <SubTitle title="settings.paginator.pre" icon="arrow-left-circle" />
            <ArticleCard class="pre-and-next-article" :data="preArticleCard" />
          </div>
          <div class="w-full h-full self-stretch mt-0" v-if="nextArticleCard">
            <SubTitle title="settings.paginator.next" :side="!isMobile ? 'right' : 'left'" icon="arrow-right-circle" />
            <ArticleCard class="pre-and-next-article" :data="nextArticleCard" />
          </div>
        </div>
        <Comment />
      </div>
      <div>
        <Sidebar>
          <Profile />
          <Sticky :stickyTop="32" endingElId="footer" dynamicElClass="#sticky-sidebar">
            <div id="sticky-sidebar">
              <transition name="fade-slide-y" mode="out-in">
                <div class="sidebar-box mb-4">
                  <SubTitle :title="'titles.toc'" icon="toc" />
                  <div id="toc1"></div>
                </div>
              </transition>
              <Navigator />
            </div>
          </Sticky>
        </Sidebar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Sidebar, Profile, Navigator } from '@/components/Sidebar'
import {
  computed,
  defineComponent,
  nextTick,
  onUnmounted,
  onMounted,
  reactive,
  ref,
  toRefs,
  provide,
  getCurrentInstance
} from 'vue'
import { useRoute, useRouter, onBeforeRouteUpdate } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { Comment } from '@/components/Comment'
import { SubTitle } from '@/components/Title'
import { ArticleCard } from '@/components/ArticleCard'
import '@/styles/prism-aurora-future.css'
import { useCommonStore } from '@/stores/common'
import { useCommentStore } from '@/stores/comment'
import Sticky from '@/components/Sticky.vue'
import Prism from 'prismjs'
import tocbot from 'tocbot'
import emitter from '@/utils/mitt'
import { v3ImgPreviewFn } from 'v3-img-preview'
import api from '@/api/api'
import markdownToHtml from '@/utils/markdown'

export default defineComponent({
  name: 'Article',
  components: { Sidebar, Comment, SubTitle, ArticleCard, Profile, Sticky, Navigator },
  setup() {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const commonStore = useCommonStore()
    const commentStore = useCommentStore()
    const route = useRoute()
    const router = useRouter()
    const { t } = useI18n()
    const loading = ref(true)
    const articleRef = ref()
    const reactiveData = reactive({
      articleId: '' as any,
      article: '' as any,
      wordNum: '' as any,
      readTime: '' as any,
      comments: [] as any,
      images: [] as any,
      preArticleCard: '' as any,
      nextArticleCard: '' as any,
      haveMore: false as any,
      isReload: false as any
    })
    const pageInfo = reactive({
      current: 1,
      size: 7
    })
    commentStore.type = 1
    onMounted(() => {
      reactiveData.articleId = route.params.articleId
      toPageTop()
      fetchArticle()
      fetchComments()
    })
    onUnmounted(() => {
      commonStore.resetHeaderImage()
      reactiveData.article = ''
      tocbot.destroy()
    })
    onBeforeRouteUpdate((to) => {
      reactiveData.article = ''
      reactiveData.readTime = ''
      reactiveData.wordNum = ''
      reactiveData.comments = []
      reactiveData.images = []
      reactiveData.preArticleCard = ''
      reactiveData.nextArticleCard = ''
      reactiveData.articleId = to.params.articleId
      pageInfo.current = 1
      reactiveData.isReload = true
      toPageTop()
      fetchArticle()
      fetchComments()
    })
    provide(
      'comments',
      computed(() => reactiveData.comments)
    )
    provide(
      'haveMore',
      computed(() => reactiveData.haveMore)
    )
    emitter.on('articleFetchComment', () => {
      pageInfo.current = 1
      reactiveData.isReload = true
      fetchComments()
    })
    emitter.on('articleFetchReplies', (index) => {
      fetchReplies(index)
    })

    emitter.on('articleLoadMore', () => {
      fetchComments()
    })
    const handlePreview = (index: any) => {
      v3ImgPreviewFn({ images: reactiveData.images, index: reactiveData.images.indexOf(index) })
    }
    const initTocbot = () => {
      let nodes = articleRef.value.children
      if (nodes.length) {
        for (let i = 0; i < nodes.length; i++) {
          let node = nodes[i]
          let reg = /^H[1-4]{1}$/
          if (reg.exec(node.tagName)) {
            node.id = i
          }
        }
      }
      tocbot.init({
        tocSelector: '#toc1',
        contentSelector: '.post-html',
        headingSelector: 'h1, h2, h3',
        collapseDepth: 3,
        disableTocScrollSync: true,
        onClick: function (e) {
          e.preventDefault()
        }
      })
      const imgs = articleRef.value.getElementsByTagName('img')
      for (var i = 0; i < imgs.length; i++) {
        reactiveData.images.push(imgs[i].src)
        imgs[i].addEventListener('click', function (e: any) {
          handlePreview(e.target.currentSrc)
        })
      }
    }
    const fetchArticle = () => {
      loading.value = true
      api.getArticeById(reactiveData.articleId).then(({ data }) => {
        if (data.code === 52003) {
          proxy.$notify({
            title: 'Error',
            message: '文章密码认证未通过',
            type: 'error'
          })
          router.push({ path: '/出错啦' })
          return
        }
        if (data.data === null) {
          router.push({ path: '/出错啦' })
          return
        }
        commonStore.setHeaderImage(data.data.articleCover)
        new Promise((resolve) => {
          data.data.articleContent = markdownToHtml(data.data.articleContent)
          resolve(data.data)
        }).then((article: any) => {
          reactiveData.article = article
          reactiveData.wordNum = Math.round(deleteHTMLTag(article.articleContent).length / 100) / 10 + 'k'
          reactiveData.readTime = Math.round(deleteHTMLTag(article.articleContent).length / 400) + 'mins'
          loading.value = false
          nextTick(() => {
            Prism.highlightAll()
            initTocbot()
          })
        })
        new Promise((resolve) => {
          data.data.preArticleCard.articleContent = markdownToHtml(data.data.preArticleCard.articleContent)
            .replace(/<\/?[^>]*>/g, '')
            .replace(/[|]*\n/, '')
            .replace(/&npsp;/gi, '')
          resolve(data.data.preArticleCard)
        }).then((preArticleCard: any) => {
          reactiveData.preArticleCard = preArticleCard
        })
        new Promise((resolve) => {
          data.data.nextArticleCard.articleContent = markdownToHtml(data.data.nextArticleCard.articleContent)
            .replace(/<\/?[^>]*>/g, '')
            .replace(/[|]*\n/, '')
            .replace(/&npsp;/gi, '')
          resolve(data.data.nextArticleCard)
        }).then((nextArticleCard) => {
          reactiveData.nextArticleCard = nextArticleCard
        })
      })
    }
    const fetchComments = () => {
      const params = {
        type: 1,
        topicId: reactiveData.articleId,
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
    const handleAuthorClick = (link: string) => {
      if (link === '') link = window.location.href
      window.location.href = link
    }
    const toPageTop = () => {
      window.scrollTo({
        top: 0
      })
    }
    const deleteHTMLTag = (content: any) => {
      return content
        .replace(/<\/?[^>]*>/g, '')
        .replace(/[|]*\n/, '')
        .replace(/&npsp;/gi, '')
    }
    return {
      articleRef,
      ...toRefs(reactiveData),
      isMobile: computed(() => commonStore.isMobile),
      handleAuthorClick,
      loading,
      t
    }
  }
})
</script>
<style lang="scss">
.post-html {
  word-wrap: break-word;
  word-break: break-all;
}
#toc1 {
  max-height: 470px;
  overflow: hidden scroll;
}
#toc1 > ol {
  list-style: none;
  counter-reset: li;
  padding-left: 1.5rem;

  > li {
    @apply font-medium pb-1;
    &.is-active-li > .node-name--H1 {
      @apply text-ob;
    }
    &.is-active-li > .node-name--H2 {
      @apply text-ob;
    }
    &.is-active-li > .node-name--H3 {
      @apply text-ob;
    }
  }

  ol li {
    @apply font-medium mt-1.5 mb-1.5;
    padding-left: 1.5rem;
    &.is-active-li > .node-name--H2 {
      @apply text-ob;
    }
    &.is-active-li > .node-name--H3 {
      @apply text-ob;
    }
    ol li {
      @apply font-medium mt-1.5 mb-1.5;
      padding-left: 1.5rem;
      &.is-active-li .node-name--H3 {
        @apply text-ob;
      }
    }
  }

  ol,
  ol ol {
    position: relative;
  }

  > li::before,
  ol > li::before,
  ol ol > li::before,
  ol ol ol > li::before,
  ol ol ol ol > li::before {
    content: '•';
    color: var(--text-accent);
    display: inline-block;
    width: 1em;
    margin-left: -1.15em;
    padding: 0;
    font-weight: medium;
    text-shadow: 0 0 0.5em var(--accent-2);
  }

  > li::before {
    @apply text-xl;
  }

  > li > ol::before,
  > li > ol > li > ol::before {
    content: '';
    border-left: 1px solid var(--text-accent);
    position: absolute;
    opacity: 0.35;
    left: -1em;
    top: 0;
    bottom: 0;
  }

  > li > ol::before {
    left: -1.25em;
    border-left: 2px solid var(--text-accent);
  }
}
.pre-and-next-article {
  .article-content {
    p {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 5;
      -webkit-box-orient: vertical;
    }
    .article-footer {
      margin-top: 13px;
    }
  }
}
.markdown-body .hljs-center {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
<style lang="scss" scoped>
.my-gap {
  gap: 1rem;
}
</style>
