import { defineStore } from 'pinia'

export const useSearchStore = defineStore('searchStore', {
  state: () => ({
    openModal: false
  }),
  getters: {},
  actions: {
    setOpenModal(status: boolean) {
      this.openModal = status
      if (status === true) document.body.classList.add('modal--active')
      else document.body.classList.remove('modal--active')
      document.getElementById('App-Container')?.focus()
    }
  }
})
