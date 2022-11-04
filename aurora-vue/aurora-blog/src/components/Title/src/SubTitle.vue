<template>
  <p class="relative flex items-center pb-2 mb-4 text-xl text-ob-bright uppercase">
    <svg-icon v-if="icon && side === 'left'" :icon-class="icon" class="inline-block mr-2" />
    <span :class="titleClass">{{ t(titleStr) }}</span>
    <svg-icon v-if="icon && side === 'right'" :icon-class="icon" class="inline-block ml-2" />
    <span :class="lineClass" :style="gradientBackground" />
  </p>
</template>

<script lang="ts">
import { useAppStore } from '@/stores/app'
import { computed, defineComponent, toRefs } from 'vue'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'SubTitle',
  props: {
    title: {
      type: String,
      default: '',
      requried: true
    },
    side: {
      type: String,
      default: 'left'
    },
    icon: String
  },
  setup(props) {
    const appStore = useAppStore()
    const { t } = useI18n()
    const titleStr = toRefs(props).title
    const side = toRefs(props).side

    return {
      gradientBackground: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      titleClass: computed(() => {
        return {
          'w-full': true,
          block: true,
          'text-right': side.value === 'right' ? true : false
        }
      }),
      lineClass: computed(() => {
        return {
          absolute: true,
          'bottom-0': true,
          'h-1': true,
          'w-14': true,
          'rounded-full': true,
          'right-0': side.value === 'right' ? true : false
        }
      }),
      titleStr,
      t
    }
  }
})
</script>
