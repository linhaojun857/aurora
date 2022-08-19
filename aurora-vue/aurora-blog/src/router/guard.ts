import router from '@/router'
import { useAppStore } from '@/stores/app'

router.beforeEach(async (to, from, next) => {
  const appStore = useAppStore()
  appStore.startLoading()
  next()
})

router.afterEach(() => {
  const appStore = useAppStore()
  appStore.endLoading()
  document.getElementById('App-Container')?.focus()
})
