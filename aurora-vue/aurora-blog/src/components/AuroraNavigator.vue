<template>
  <div
    id="Ob-Navigator"
    :class="{
      'Ob-Navigator--open': openNavigator,
      'Ob-Navigator--scrolling': scrolling
    }">
    <transition name="fade-bounce-y" mode="out-in">
      <div v-if="!openNavigator && showProgress" @click.stop.prevent="handleBackToTop" class="Ob-Navigator-btt">
        <div>
          <svg-icon class="text-ob-bright stroke-current" icon-class="nav-top" />
        </div>
        <span class="Ob-Navigator-tips">
          {{ t('settings.tips-back-to-top') }}
        </span>
      </div>
    </transition>
    <div class="Ob-Navigator-ball" @click.stop.prevent="handleNavigatorToggle">
      <div :style="gradient">
        <transition name="fade-bounce-y" mode="out-in">
          <svg-icon v-if="openNavigator" class="text-base stroke-2" icon-class="close" />
          <svg-icon v-else-if="!showProgress" icon-class="dots" />
          <span class="text-sm" v-else>{{ progress }}%</span>
        </transition>
      </div>
    </div>
    <ul class="Ob-Navigator-submenu">
      <li id="Ob-Navigator-top" :style="gradient" @click.stop.prevent="handleBackToTop">
        <div>
          <svg-icon class="text-ob-bright stroke-current" icon-class="nav-top" />
        </div>
        <span class="Ob-Navigator-tips">
          {{ t('settings.tips-back-to-top') }}
        </span>
      </li>
      <li id="Ob-Navigator-menu" :style="gradient" @click.stop.prevent="handleOpenMenu" v-if="isMobile">
        <div>
          <svg-icon class="text-ob-bright stroke-current" icon-class="nav-menu" />
        </div>
        <span class="Ob-Navigator-tips">
          {{ t('settings.tips-open-menu') }}
        </span>
      </li>
      <li id="Ob-Navigator-home" :style="gradient" @click.stop.prevent="handleGoHome">
        <div>
          <svg-icon class="text-ob-bright stroke-current" icon-class="nav-home" />
        </div>
        <span class="Ob-Navigator-tips">
          {{ t('settings.tips-back-to-home') }}
        </span>
      </li>
      <li id="Ob-Navigator-search" :style="gradient" @click.stop.prevent="handleSearch">
        <div>
          <svg-icon class="text-ob-bright stroke-current" icon-class="search" />
        </div>
        <span class="Ob-Navigator-tips">
          {{ t('settings.tips-open-search') }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
// @ts-nocheck
import { useAppStore } from '@/stores/app'
import { computed, defineComponent, onMounted, onUnmounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useNavigatorStore } from '@/stores/navigator'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/search'
import { useCommonStore } from '@/stores/common'

export default defineComponent({
  name: 'AuroraNavigator',
  setup() {
    const appStore = useAppStore()
    const commonStore = useCommonStore()
    const { t } = useI18n()
    const navigatorStore = useNavigatorStore()
    const searchStore = useSearchStore()
    const router = useRouter()
    const progress = ref(0)
    const scrolling = ref(false)
    let time = ref(0)
    let scrollingHandler: any
    let menuReopenHandler: any
    let needReopen = ref(false)
    onMounted(() => {
      document.addEventListener('scroll', scrollHandler)
    })
    onUnmounted(() => {
      document.removeEventListener('scroll', scrollHandler)
    })
    const scrollHandler = () => {
      clearTimeout(scrollingHandler)
      clearTimeout(menuReopenHandler)
      scrolling.value = true
      scrollingHandler = setTimeout(() => {
        scrolling.value = false
      }, 700)
      if (needReopen.value || navigatorStore.openNavigator === true) {
        if (navigatorStore.openNavigator === true) navigatorStore.setOpenNavigator(false)
        needReopen.value = true
        menuReopenHandler = setTimeout(() => {
          navigatorStore.openNavigator = true
          needReopen.value = false
        }, 700)
      }
      setTimeout(() => {
        progress.value = Number(
          ((window.pageYOffset / (document.documentElement.scrollHeight - window.innerHeight)) * 100).toFixed(0)
        )
      }, 0)
    }
    const handleNavigatorToggle = () => {
      const timeNow = new Date().getTime()
      if (timeNow - time.value < 10) return
      time.value = timeNow
      if (navigatorStore.openNavigator === true && needReopen.value === true) needReopen.value = false
      setTimeout(() => {
        navigatorStore.toggleOpenNavigator()
      }, 10)
    }
    const handleBackToTop = () => {
      navigatorStore.setOpenNavigator(false)
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
    const handleOpenMenu = () => {
      navigatorStore.toggleMobileMenu()
    }
    const handleGoHome = () => {
      navigatorStore.setOpenNavigator(false)
      router.push('/')
    }
    const handleSearch: any = (status: boolean) => {
      navigatorStore.setOpenNavigator(false)
      searchStore.setOpenModal(status)
    }
    return {
      gradient: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      showProgress: computed(() => {
        return progress.value > 5
      }),
      isMobile: computed(() => commonStore.isMobile),
      openNavigator: computed(() => navigatorStore.openNavigator),
      progress,
      handleNavigatorToggle,
      handleBackToTop,
      handleOpenMenu,
      handleGoHome,
      handleSearch,
      scrolling,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
#Ob-Navigator {
  @apply fixed flex justify-center items-center bottom-4 right-4 w-12 h-12 rounded-full z-40 shadow-lg text-white text-2xl stroke-0 border-2 border-ob-deep-900 cursor-pointer;
  transition: all 0.55s cubic-bezier(0, 1.8, 1, 1.2);
  opacity: 1;
  svg {
    pointer-events: none;
    stroke: currentColor !important;
  }
  .Ob-Navigator-submenu {
    @apply absolute top-0 left-0 m-0 p-0 list-none;
    li {
      @apply flex justify-center items-center bg-ob-deep-900 absolute rounded-full w-12 h-12 p-0.5;
      opacity: 0;
      transition: all 0.55s cubic-bezier(0, 1.8, 1, 1.2);
      &:hover {
        .Ob-Navigator-tips {
          opacity: 1;
          transform: translateX(-15%);
        }
      }
      div {
        @apply flex justify-center items-center bg-ob-deep-800 w-full h-full rounded-full;
      }
    }
  }
  &.Ob-Navigator--open .Ob-Navigator-submenu {
    li {
      opacity: 1;
      &:first-of-type {
        transform: translateX(calc(3rem * -1.6));
      }
      &:nth-of-type(2) {
        transform: translate(calc(3rem * -1.2), calc(3rem * -1.2));
      }
      &:nth-of-type(3) {
        transform: translateY(calc(3rem * -1.6));
      }
      &:nth-of-type(4) {
        transform: translateY(calc(3rem * -2.8));
      }
    }
  }
  &.Ob-Navigator--scrolling {
    transform: translateX(calc(3rem * 0.8));
    opacity: 0.6;
  }
  .Ob-Navigator-tips {
    @apply absolute bg-ob-deep-800 py-1 px-1.5 z-50 text-xs text-ob-bright whitespace-nowrap rounded-md shadow;
    pointer-events: none;
    opacity: 0;
    right: 60%;
    transition: all 0.55s cubic-bezier(0, 1.8, 1, 1.2);
  }
  .Ob-Navigator-ball {
    @apply relative flex justify-center items-center bg-ob-deep-800 w-full h-full p-0.5 rounded-full;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1), 0 12px 28px rgba(0, 0, 0, 0.2);
    z-index: 200;
    div {
      @apply flex justify-center items-center w-full h-full rounded-full;
    }
  }
  .Ob-Navigator-btt {
    @apply absolute flex justify-center items-center bg-ob-deep-800 w-full h-full p-0.5 rounded-full;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1), 0 12px 28px rgba(0, 0, 0, 0.2);
    top: calc(3rem * -1.1);
    left: 0;
    div {
      @apply flex justify-center items-center w-full h-full rounded-full;
    }
  }
}
</style>
