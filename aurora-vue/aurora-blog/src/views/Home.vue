<template>
  <div class="block">
    <Feature v-if="themeConfig.feature">
      <FeatureList />
    </Feature>
    <span v-if="themeConfig.feature">
      <Title id="article-list" :title="'titles.articles'" icon="article" />
    </span>
    <div class="main-grid">
      <div class="flex flex-col relative">
        <ul :class="tabClass">
          <li :class="{ active: activeTab === 0 }" @click="handleTabChange(0)">
            <span class="first-tab" :style="activeTabStyle(0)">
              {{ t('settings.button-all') }}
            </span>
          </li>
          <template v-if="categories && categories.length > 0">
            <li
              v-for="category in categories"
              :key="category.id"
              :class="{ active: activeTab === category.id }"
              @click="handleTabChange(category.id)">
              <span :style="activeTabStyle(category.id)">
                {{ category.categoryName }}
              </span>
              <b>
                {{ category.articleCount }}
              </b>
            </li>
          </template>
          <template v-else-if="(categories.length = 0)">
            <li v-for="i in 6" :key="i" style="position: relative; top: -4px">
              <ob-skeleton tag="span" width="60px" height="33px" />
            </li>
          </template>
        </ul>
        <span :class="expanderClass" @click="expandHandler">
          <svg-icon icon-class="chevron" />
        </span>
        <ul class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
          <template v-if="haveArticles === true">
            <li v-for="article in articles" :key="article.id">
              <ArticleCard class="home-article" :data="article" />
            </li>
          </template>
          <template v-else>
            <li v-for="n in 12" :key="n">
              <ArticleCard :data="{}" />
            </li>
          </template>
        </ul>
        <Paginator
          :pageSize="pagination.size"
          :pageTotal="pagination.total"
          :page="pagination.current"
          @pageChange="pageChangeHanlder" />
      </div>
      <div>
        <Sidebar>
          <Profile />
          <RecentComment v-if="true" />
          <TagBox />
          <Notice />
          <WebsiteInfo />
        </Sidebar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, toRefs, toRef, reactive } from 'vue'
import { Feature, FeatureList } from '@/components/Feature'
import { ArticleCard, HorizontalArticle } from '@/components/ArticleCard'
import { Title } from '@/components/Title'
import { Sidebar, Profile, RecentComment, TagBox, Notice, WebsiteInfo } from '@/components/Sidebar'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useArticleStore } from '@/stores/article'
import { useCategoryStore } from '@/stores/Category'
import { useI18n } from 'vue-i18n'
import Paginator from '@/components/Paginator.vue'
import api from '@/api/api'

export default defineComponent({
  name: 'Home',
  components: {
    Feature,
    FeatureList,
    ArticleCard,
    HorizontalArticle,
    Title,
    Paginator,
    Sidebar,
    Profile,
    RecentComment,
    TagBox,
    Notice,
    WebsiteInfo
  },
  setup() {
    const appStore = useAppStore()
    const userStore = useUserStore()
    const articleStore = useArticleStore()
    const categoryStore = useCategoryStore()
    const { t } = useI18n()
    const expanderClass = ref({
      'tab-expander': true,
      expanded: false
    })
    const tabClass = ref({
      tab: true,
      'expanded-tab': false
    })
    const activeTab = ref(0)
    const articleOffset = ref(0)
    const reactiveData = reactive({
      haveArticles: false
    })
    const pagination = reactive({
      size: 12,
      total: 0,
      current: 1
    })
    let nowCategoryId = 0
    let md = require('markdown-it')()
    onMounted(() => {
      fetchTopAndFeatured()
      fetchCategories()
      fetchArticles()
      const articleListEl = document.getElementById('article-list')
      articleOffset.value = articleListEl && articleListEl instanceof HTMLElement ? articleListEl.offsetTop + 120 : 0
    })
    const fetchTopAndFeatured = () => {
      api.getTopAndFeaturedArticles().then(({ data }) => {
        data.data.topArticle.articleContent = md
          .render(data.data.topArticle.articleContent)
          .replace(/<\/?[^>]*>/g, '')
          .replace(/[|]*\n/, '')
          .replace(/&npsp;/gi, '')
        data.data.featuredArticles.forEach((item: any) => {
          item.articleContent = md
            .render(item.articleContent)
            .replace(/<\/?[^>]*>/g, '')
            .replace(/[|]*\n/, '')
            .replace(/&npsp;/gi, '')
        })
        articleStore.topArticle = data.data.topArticle
        articleStore.featuredArticles = data.data.featuredArticles
      })
    }
    const fetchArticles = () => {
      activeTab.value = userStore.tab
      nowCategoryId = userStore.tab
      pagination.current = userStore.page
      if (userStore.tab === 0) {
        reactiveData.haveArticles = false
        api
          .getArticles({
            current: pagination.current,
            size: pagination.size
          })
          .then(({ data }) => {
            if (data.flag) {
              data.data.records.forEach((item: any) => {
                item.articleContent = md
                  .render(item.articleContent)
                  .replace(/<\/?[^>]*>/g, '')
                  .replace(/[|]*\n/, '')
                  .replace(/&npsp;/gi, '')
              })
              articleStore.articles = data.data.records
              pagination.total = data.data.count
              reactiveData.haveArticles = true
            }
          })
      } else {
        fetchArticlesByCategoryId(userStore.tab)
      }
    }
    const fetchArticlesByCategoryId = (categoryId: any) => {
      reactiveData.haveArticles = false
      api
        .getArticlesByCategoryId({
          current: pagination.current,
          size: pagination.size,
          categoryId: categoryId
        })
        .then(({ data }) => {
          data.data.records.forEach((item: any) => {
            item.articleContent = md
              .render(item.articleContent)
              .replace(/<\/?[^>]*>/g, '')
              .replace(/[|]*\n/, '')
              .replace(/&npsp;/gi, '')
          })
          articleStore.articles = data.data.records
          pagination.total = data.data.count
          reactiveData.haveArticles = true
        })
    }
    const fetchCategories = () => {
      categoryStore.categories = []
      api.getAllCategories().then(({ data }) => {
        categoryStore.categories.push(...data.data)
      })
    }
    const expandHandler = () => {
      expanderClass.value.expanded = !expanderClass.value.expanded
      tabClass.value['expanded-tab'] = !tabClass.value['expanded-tab']
    }
    const handleTabChange = (categoryId: any) => {
      userStore.tab = categoryId
      userStore.page = 1
      pagination.current = 1
      activeTab.value = categoryId
      nowCategoryId = categoryId
      toArticleOffset()
      if (categoryId === 0) {
        fetchArticles()
      } else {
        fetchArticlesByCategoryId(categoryId)
      }
    }
    const toArticleOffset = () => {
      window.scrollTo({
        top: articleOffset.value
      })
    }
    const activeTabStyle = (catagoryId: any) => {
      if (catagoryId === activeTab.value) return { background: appStore.themeConfig.header_gradient_css }
      return {}
    }
    const pageChangeHanlder = (current: number) => {
      userStore.page = current
      pagination.current = current
      toArticleOffset()
      if (nowCategoryId === 0) {
        fetchArticles()
      } else {
        fetchArticlesByCategoryId(nowCategoryId)
      }
    }
    return {
      ...toRefs(reactiveData),
      ...toRefs(articleStore.$state),
      categories: toRef(categoryStore.$state, 'categories'),
      gradientText: computed(() => appStore.themeConfig.background_gradient_style),
      gradientBackground: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      themeConfig: computed(() => appStore.themeConfig),
      expanderClass,
      tabClass,
      expandHandler,
      handleTabChange,
      activeTabStyle,
      activeTab,
      pagination,
      pageChangeHanlder,
      t
    }
  }
})
</script>
<style lang="scss">
.home-article {
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
</style>
