import { defineStore } from 'pinia'

export const useTagStore = defineStore('tagStore', {
  state: () => {
    return {
      homeTags: '' as any,
      tags: '' as any
    }
  },
  actions: {}
})
