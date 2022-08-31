import { defineStore } from 'pinia'
import { i18n } from '@/locales/index'
import nProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'

nProgress.configure({
  showSpinner: false,
  trickleSpeed: 100,
  parent: '#loading-bar-wrapper'
}) // NProgress Configuration

//获取系统模式
const getSystemMode = (): string => {
  const matched = window.matchMedia('(prefers-color-scheme: dark)').matches
  if (matched) return 'theme-dark'
  else return 'theme-light'
}

const setTheme = (theme: string) => {
  if (theme === 'theme-dark') {
    document.body.classList.remove('theme-light')
    document.body.classList.add('theme-dark')
  } else {
    document.body.classList.remove('theme-dark')
    document.body.classList.add('theme-light')
  }
}

export const useAppStore = defineStore('appStore', {
  state: () => {
    return {
      themeConfig: {
        theme: 'theme-dark',
        profile_shape: 'circle-avatar',
        feature: true,
        gradient: {
          color_1: '#24c6dc',
          color_2: '#5433ff',
          color_3: '#ff0099'
        },
        header_gradient_css: 'linear-gradient(130deg, #24c6dc, #5433ff 41.07%, #ff0099 76.05%)',
        background_gradient_style: {
          background: 'linear-gradient(130deg, #24c6dc, #5433ff 41.07%, #ff0099 76.05%)',
          '-webkit-background-clip': 'text',
          '-webkit-text-fill-color': 'transparent',
          '-webkit-box-decoration-break': 'clone',
          'box-decoration-break': 'clone'
        }
      },
      appLoading: false,
      websiteConfig: '' as any,
      viewCount: 0,
      articleCount: 0,
      talkCount: 0,
      categoryCount: 0,
      tagCount: 0,
      NPTimeout: -1,
      loadingTimeout: -1,
      aurora_bot_enable:true
    }
  },
  actions: {
    changeLocale(locale: string) {
      // Cookies.set('locale', locale)
      i18n.global.locale = locale
    },
    initializeTheme(mode: string) {
      // if (!Cookies.get('theme') && isDarkMode !== 'auto') {
      //   this.themeConfig.theme = isDarkMode ? 'theme-dark' : 'theme-light'
      //   // Cookies.set('theme', this.themeConfig.theme)
      //   setTheme(this.themeConfig.theme)
      // }
      setTheme(mode)
    },
    toggleTheme(isDark?: boolean) {
      this.themeConfig.theme =
        isDark === true || this.themeConfig.theme === 'theme-light' ? 'theme-dark' : 'theme-light'
      // Cookies.set('theme', this.theme)
      setTheme(this.themeConfig.theme)
    },
    /** Start the global loading status of the application */
    startLoading() {
      if (this.appLoading === true) return
      if (this.NPTimeout !== -1) clearTimeout(this.NPTimeout)
      if (this.loadingTimeout !== -1) clearTimeout(this.loadingTimeout)

      nProgress.start()
      this.appLoading = true
    },
    /** Stops the global loading status of the application */
    endLoading() {
      // Leaving the timeout, so the animation have enough time to display
      // in a situation where data loads almost instantly.
      this.NPTimeout = <any>setTimeout(() => {
        nProgress.done()
      }, 100)

      this.loadingTimeout = <any>setTimeout(() => {
        this.appLoading = false
      }, 300)
    }
  }
})
