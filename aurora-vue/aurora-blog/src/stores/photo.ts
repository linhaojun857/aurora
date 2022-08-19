import { defineStore } from 'pinia'

export const usePhotoStore = defineStore('photoStore', {
  state: () => {
    return {
      photoAlbumVisible: true,
      photos: '' as any,
      id: '' as any
    }
  },
  actions: {}
})
