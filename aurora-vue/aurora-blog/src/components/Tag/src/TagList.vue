<template>
  <div class="flex justify-event flex-wrap pt-2">
    <slot />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'ObTagList'
})
</script>
