<template>
  <div class="sidebar-box">
    <SubTitle :title="'titles.notice'" icon="notice" />
    <div class="mx-auto">
      <span class="text-sm font-medium text-right notice"> {{ notice }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'
import { SubTitle } from '@/components/Title'
import { useAppStore } from '@/stores/app'

export default defineComponent({
  name: 'Notice',
  components: { SubTitle },
  setup() {
    const appStore = useAppStore()
    return {
      notice: computed(() => {
        return appStore.websiteConfig.notice
      })
    }
  }
})
</script>

<style lang="scss" scoped>
.notice {
  word-wrap: break-word;
  word-break: break-all;
}
</style>
