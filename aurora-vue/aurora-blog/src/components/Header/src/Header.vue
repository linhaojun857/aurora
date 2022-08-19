<template>
  <div class="header-container">
    <header class="site-header">
      <Logo />
      <Navigation />
      <Controls />
    </header>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Logo, Navigation, Controls } from '../index'

export default defineComponent({
  name: 'Header',
  components: {
    Logo,
    Navigation,
    Controls
  },
  props: {
    msg: String
  }
})
</script>

<style lang="scss" scoped>
.header-container {
  .site-header {
    max-width: var(--max-width);
    @apply relative flex z-50 my-0 mx-auto py-4;
  }
}
</style>
