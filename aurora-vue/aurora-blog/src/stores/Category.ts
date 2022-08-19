import { defineStore } from 'pinia'

export const useCategoryStore = defineStore('categoryStore', {
  state: () => {
    return {
        categories:[] as any[]
    }
  },
  actions: {}
})
