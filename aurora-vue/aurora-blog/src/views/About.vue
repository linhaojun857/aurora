<template>
  <div>
    <Breadcrumb :current="t('menu.about')" />
    <div class="flex flex-col">
      <div class="post-header">
        <h1 v-if="about" class="post-title text-white uppercase">
          {{ t('titles.about') }}
        </h1>
        <ob-skeleton
          v-else
          class="post-title text-white uppercase"
          width="100%"
          height="clamp(1.2rem, calc(1rem + 3.5vw), 4rem)" />
      </div>
      <div class="main-grid">
        <div class="relative">
          <div v-if="about" class="post-html" ref="postRef" v-html="about" />
          <div v-else class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block min-h-screen">
            <ob-skeleton tag="div" :count="1" height="36px" width="150px" class="mb-6" />
            <br />
            <ob-skeleton tag="div" :count="35" height="16px" width="100px" class="mr-2" />
            <br />
            <br />
            <ob-skeleton tag="div" :count="25" height="16px" width="100px" class="mr-2" />
          </div>
          <Comment />
        </div>
        <div class="col-span-1">
          <Sidebar>
            <Profile author="blog-author" />
            <Sticky :stickyTop="32" endingElId="footer" dynamicElClass="#sticky-sidebar">
              <div id="sticky-sidebar">
                <transition name="fade-slide-y" mode="out-in">
                  <div class="sidebar-box mb-4">
                    <SubTitle :title="'titles.toc'" icon="toc" />
                    <div id="toc2"></div>
                  </div>
                </transition>
                <Navigator />
              </div>
            </Sticky>
          </Sidebar>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, onUnmounted, toRefs, ref, reactive, nextTick, provide, computed } from 'vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { useI18n } from 'vue-i18n'
import { Sidebar, Profile, Navigator } from '@/components/Sidebar'
import { useCommonStore } from '@/stores/common'
import { useCommentStore } from '@/stores/comment'
import Sticky from '@/components/Sticky.vue'
import { SubTitle } from '@/components/Title'
import { Comment } from '@/components/Comment'
import tocbot from 'tocbot'
import Prism from 'prismjs'
import '@/styles/prism-aurora-future.css'
import api from '@/api/api'
import emitter from '@/utils/mitt'
import { v3ImgPreviewFn } from 'v3-img-preview'
import markdownToHtml from '@/utils/markdown'

export default defineComponent({
  name: 'About',
  components: { Breadcrumb, Sidebar, Profile, Navigator, Sticky, SubTitle, Comment },
  setup() {
    const commonStore = useCommonStore()
    const commentStore = useCommentStore()
    const { t } = useI18n()
    const postRef = ref()
    const reactiveData = reactive({
      about: '' as any,
      comments: [] as any,
      haveMore: false as any,
      isReload: false as any,
      images: [] as any
    })
    const pageInfo = reactive({
      current: 1,
      size: 7
    })
    commentStore.type = 3
    onMounted(() => {
      fetchComments()
      fetchAbout()
    })
    onUnmounted(() => {
      commonStore.resetHeaderImage()
      tocbot.destroy()
    })
    provide(
      'comments',
      computed(() => reactiveData.comments)
    )
    provide(
      'haveMore',
      computed(() => reactiveData.haveMore)
    )
    emitter.on('aboutFetchComment', () => {
      pageInfo.current = 1
      reactiveData.isReload = true
      fetchComments()
    })
    emitter.on('aboutFetchReplies', (index) => {
      fetchReplies(index)
    })
    emitter.on('aboutLoadMore', () => {
      fetchComments()
    })
    const handlePreview = (index: any) => {
      v3ImgPreviewFn({ images: reactiveData.images, index: reactiveData.images.indexOf(index) })
    }
    const initTocbot = () => {
      let nodes = postRef.value.children
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
        tocSelector: '#toc2',
        contentSelector: '.post-html',
        headingSelector: 'h1, h2, h3',
        onClick: function (e) {
          e.preventDefault()
        }
      })
      const imgs = postRef.value.getElementsByTagName('img')
      for (var i = 0; i < imgs.length; i++) {
        reactiveData.images.push(imgs[i].src)
        imgs[i].addEventListener('click', function (e: any) {
          handlePreview(e.target.currentSrc)
        })
      }
    }
    const fetchAbout = () => {
      api.getAbout().then(({ data }) => {
        data.data.content = markdownToHtml(data.data.content)
        reactiveData.about = data.data.content
        nextTick(() => {
          Prism.highlightAll()
          initTocbot()
        })
      })
    }
    const fetchComments = () => {
      const params = {
        type: 3,
        topicId: null,
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
    return {
      postRef,
      ...toRefs(reactiveData),
      t
    }
  }
})
</script>

<style lang="scss">
.post-title {
  @apply my-2;
  font-size: clamp(1.2rem, calc(1rem + 3.5vw), 4rem);
  text-shadow: 0 2px 2px rgba(0, 0, 0, 0.5);
}
.post-stats {
  @apply w-full flex flex-row text-sm lg:text-base mb-6;
  span {
    @apply text-white stroke-current flex flex-row items-center pr-4;
  }
}
.post-html {
  word-wrap: break-word;
  word-break: break-all;
}
#toc2 > ol {
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
</style>
