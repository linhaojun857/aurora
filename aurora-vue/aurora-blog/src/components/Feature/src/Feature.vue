<template>
  <div id="feature">
    <horizontal-article/>
    <slot />
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import HorizontalArticle from '@/components/ArticleCard/src/HorizontalArticle.vue'

export default defineComponent({
  name: 'Feature',
  components: { HorizontalArticle },
  setup() {
    return {
    }
  }
})
</script>

<style lang="scss" scoped></style>
