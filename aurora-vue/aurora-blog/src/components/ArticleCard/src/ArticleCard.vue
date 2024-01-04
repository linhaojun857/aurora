<template>
  <div class="article-container" @click="toArticle">
    <span v-if="article.isTop" class="article-tag">
      <b>
        <svg-icon icon-class="pin" />
        {{ t('settings.pinned') }}
      </b>
    </span>
    <span v-else-if="article.isFeatured" class="article-tag">
      <b>
        <svg-icon icon-class="hot" />
        {{ t('settings.featured') }}
      </b>
    </span>
    <div class="article">
      <div class="article-thumbnail">
        <img v-if="article.articleCover" v-lazy="article.articleCover" alt="" />
        <img v-else src="@/assets/default-cover.jpg" />
        <span class="thumbnail-screen" :style="gradientBackground" />
      </div>
      <div class="article-content">
        <span>
          <b v-if="article.categoryName">
            {{ article.categoryName }}
          </b>
          <ob-skeleton v-else tag="b" height="20px" width="35px" />
          <ul v-if="article.tags && article.tags.length > 0">
            <li v-for="tag in article.tags" :key="tag.id">
              <em># {{ tag.tagName }}</em>
            </li>
          </ul>
          <ul v-else-if="article.tags && article.tags.length <= 0">
            <li>
              <em># {{ t('settings.default-tag') }}</em>
            </li>
          </ul>
          <ul v-else>
            <ob-skeleton v-if="!article.tags" :count="2" tag="li" height="16px" width="35px" />
          </ul>
        </span>
        <h1 class="article-title" v-if="article.articleTitle">
          <a>
            <span data-dia="article-link"> {{ article.articleTitle }}</span>
            <svg-icon v-if="article.status == 2" icon-class="lock" class="lock-svg" />
          </a>
        </h1>
        <ob-skeleton v-else tag="h1" height="3rem" />
        <p v-if="article.articleContent" class="article-content-main">{{ article.articleContent }}</p>
        <ob-skeleton v-else tag="p" :count="5" height="16px" />
        <div class="article-footer" v-if="article.author && article.createTime">
          <div class="flex flex-row items-center">
            <img
              class="hover:opacity-50 cursor-pointer"
              :src="article.author.avatar || ''"
              alt="author avatar"
              @click="handleAuthorClick(article.author.website)" />
            <span class="text-ob-dim">
              <strong
                class="text-ob-normal pr-1.5 hover:text-ob hover:opacity-50 cursor-pointer"
                @click="handleAuthorClick(article.author.website)">
                {{ article.author.nickname }}
              </strong>
              {{ t('settings.shared-on') }} {{ t(`settings.months[${new Date(article.createTime).getMonth()}]`) }}
              {{ new Date(article.createTime).getDate() }}, {{ new Date(article.createTime).getFullYear() }}
            </span>
          </div>
        </div>
        <div class="article-footer" v-else>
          <div class="flex flex-row items-center mt-6">
            <ob-skeleton class="mr-2" height="28px" width="28px" :circle="true" />
            <span class="text-ob-dim mt-1">
              <ob-skeleton height="20px" width="150px" />
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, toRefs, getCurrentInstance } from 'vue'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/mitt'

export default defineComponent({
  name: 'ArticleCard',
  props: ['data'],
  setup(props) {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const appStore = useAppStore()
    const userStore = useUserStore()
    const router = useRouter()
    const { t } = useI18n()
    const handleAuthorClick = (link: string) => {
      if (link === '') link = window.location.href
      window.open(link)
    }
    const toArticle = () => {
      let isAccess = false
      userStore.accessArticles.forEach((item: any) => {
        if (item == props.data.id) {
          isAccess = true
        }
      })
      if (props.data.status === 2 && isAccess == false) {
        if (userStore.userInfo === '') {
          proxy.$notify({
            title: 'Warning',
            message: '该文章受密码保护,请登录后访问',
            type: 'warning'
          })
        } else {
          emitter.emit('changeArticlePasswordDialogVisible', props.data.id)
        }
      } else {
        router.push({ path: '/articles/' + props.data.id })
      }
    }
    return {
      gradientBackground: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      article: toRefs(props).data,
      handleAuthorClick,
      toArticle,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.article-title:hover {
  cursor: default;
}
.article-content-main:hover {
  cursor: default;
}
</style>
