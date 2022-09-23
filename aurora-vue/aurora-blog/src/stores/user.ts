import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
  state: () => {
    return {
      currentUrl: '' as any,
      userVisible: false,
      userInfo: '' as any,
      token: '' as any,
      accessArticles: [] as any
    }
  },
  actions: {},
  persist: {
    storage: window.sessionStorage
  }
})
