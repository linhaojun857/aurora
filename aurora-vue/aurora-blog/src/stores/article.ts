import { defineStore } from 'pinia'

export const useArticleStore = defineStore('articleStore', {
  state: () => {
    return {
      topArticle: '' as any,
      featuredArticles: [] as any,
      articles: [] as any,
      categories: [] as any,
      archives: [] as any
    }
  },
  actions: {}
})
