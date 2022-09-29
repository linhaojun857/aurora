<template>
  <Toggle :status="defaultStatus" @changeStatus="handleChange">
    <template v-if="theme === 'theme-light'">
      <svg-icon icon-class="sun" :style="svg" />
    </template>
    <template v-else>
      <svg-icon icon-class="moon" :style="svg" />
    </template>
  </Toggle>
</template>

<script lang="ts">
import { computed, defineComponent, reactive } from 'vue'
import Toggle from './Toggle.vue'
import { useAppStore } from '@/stores/app'

export default defineComponent({
  name: 'ThemeToggle',
  components: {
    Toggle
  },
  setup() {
    const appStore = useAppStore()
    let defaultStatus = appStore.themeConfig.theme === 'theme-dark' ? true : false
    const svgStyle = reactive({
      fill: 'yellow',
      margin: '5.5px 3px 2px 5.5px'
    })
    const handleChange = (status: boolean) => {
      appStore.toggleTheme(status)
    }
    return {
      theme: computed(() => {
        return appStore.themeConfig.theme
      }),
      svg: computed(() => svgStyle),
      handleChange,
      defaultStatus
    }
  }
})
</script>
