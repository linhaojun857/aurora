<template>
  <div class="flex flex-col">
    <div class="post-header">
      <h1 class="post-title text-white uppercase">{{ tagName }}</h1>
    </div>
    <div class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block min-h-screen">
      <ul class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-10">
        <template v-if="articles != ''">
          <li v-for="article in articles" :key="article.id">
            <ArticleCard :data="article" />
          </li>
        </template>
        <template v-else>
          <li v-for="n in 3" :key="n">
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
    const pagination = reactive({
      size: 12,
      total: 0,
      current: 1
    })

    const reactiveData = reactive({
      articles: '' as any,
      tagName: '' as any
    })

    const fetchData = () => {
      reactiveData.tagName = route.query.tagName
      api
        .getArticlesByTagId({
          tagId: route.params.tagId,
          current: pagination.current,
          size: pagination.size
        })
        .then(({ data }) => {
          reactiveData.articles = data.data.records
          pagination.total = data.data.count
        })
    }
    onMounted(fetchData)

    const backToPageTop = () => {
      window.scrollTo({
        top: 0
      })
    }

    const pageChangeHanlder = (current: number) => {
      reactiveData.articles = ''
      backToPageTop()
      pagination.current = current
      fetchData()
    }

    return {
      pagination,
      pageChangeHanlder,
      ...toRefs(reactiveData)
    }
  }
})
</script>
<style lang="scss" scoped></style>
