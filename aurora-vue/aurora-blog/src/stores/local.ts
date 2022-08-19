import { defineStore } from 'pinia'

export const useLocalStore = defineStore('localStore', {
  state: () => {
    return {
      weight: 1 as any,
      recentSearch: [] as any
    }
  },
  actions: {},
  persist: true
})
