<template>
  <div class="flex flex-col">
    <div class="post-header">
      <h1 class="post-title text-white uppercase">{{ tagName }}</h1>
    </div>
    <div class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block min-h-screen">
      <ul class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-10">
        <template v-if="haveArticles === true">
          <li v-for="article in articles" :key="article.id">
            <ArticleCard class="tag-article" :data="article" />
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
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, reactive, toRefs } from 'vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { ArticleCard } from '@/components/ArticleCard'
import Paginator from '@/components/Paginator.vue'
import { useRoute } from 'vue-router'
import api from '@/api/api'

export default defineComponent({
  name: 'ArticleList',
  components: { Breadcrumb, ArticleCard, Paginator },
  setup() {
    const route = useRoute()
    let md = require('markdown-it')()
    const pagination = reactive({
      size: 12,
      total: 0,
      current: 1
    })
    const reactiveData = reactive({
      articles: [] as any,
      tagName: '' as any,
      haveArticles: false
    })
    onMounted(() => {
      reactiveData.tagName = route.query.tagName
      fetchArticles()
    })
    const fetchArticles = () => {
      reactiveData.haveArticles = false
      api
        .getArticlesByTagId({
          tagId: route.params.tagId,
          current: pagination.current,
          size: pagination.size
        })
        .then(({ data }) => {
          data.data.records.forEach((item: any) => {
            item.articleContent = md
              .render(item.articleContent)
              .replace(/<\/?[^>]*>/g, '')
              .replace(/[|]*\n/, '')
              .replace(/&npsp;/gi, '')
          })
          reactiveData.articles = data.data.records
          pagination.total = data.data.count
          reactiveData.haveArticles = true
        })
    }
    const backToPageTop = () => {
      window.scrollTo({
        top: 0
      })
    }
    const pageChangeHanlder = (current: number) => {
      reactiveData.articles = []
      pagination.current = current
      backToPageTop()
      fetchArticles()
    }
    return {
      pagination,
      pageChangeHanlder,
      ...toRefs(reactiveData)
    }
  }
})
</script>
<style lang="scss">
.tag-article {
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
