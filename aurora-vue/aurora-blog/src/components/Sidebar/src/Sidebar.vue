<template>
  <div v-show="!isMobile">
    <slot />
  </div>
</template>

<script lang="ts">
import { useCommonStore } from '@/stores/common'
import { computed, defineComponent } from 'vue'

export default defineComponent({
  name: 'Sidebar',
  setup() {
    const commonStore = useCommonStore()
    return { isMobile: computed(() => commonStore.isMobile) }
  }
})
</script>
