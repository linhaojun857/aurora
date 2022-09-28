<template>
  <ul
    id="sidebar-navigator"
    class="flex flex-row bg-ob-deep-800 rounded-xl shadow-2xl justify-items-center overflow-hidden">
    <li
      class="border-r-4 border-ob-deep-900 flex justify-center py-3 w-full hover:opacity-50 hover:text-ob transition-all cursor-pointer"
      @click="goBack">
      <svg-icon class="inline-block text-3xl" icon-class="go-back" />
    </li>
    <li
      class="border-r-4 border-ob-deep-900 flex justify-center py-3 w-full hover:opacity-50 hover:text-ob transition-all cursor-pointer"
      @click="toPageTop">
      <svg-icon class="inline-block text-3xl" icon-class="back-to-top" />
    </li>
    <li
      class="flex justify-center py-3 w-full hover:opacity-50 hover:text-ob transition-all cursor-pointer"
      @click="jumpToComments"
      data-dia="jump-to-comment">
      <svg-icon class="inline-block text-3xl" icon-class="quote" />
    </li>
  </ul>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'Example',
  setup() {
    const router = useRouter()
    const commentOffset = ref(0)
    const commentEl = ref()
    const toPageTop = () => {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
    const goBack = () => {
      router.back()
    }
    const jumpToComments = () => {
      commentEl.value = document.getElementById('comments')
      if (commentEl.value) {
        commentOffset.value =
          commentEl.value && commentEl.value instanceof HTMLElement ? commentEl.value.offsetTop + 120 - 30 : 0
      }
      window.scrollTo({
        top: commentOffset.value,
        behavior: 'smooth'
      })
    }
    return {
      goBack,
      toPageTop,
      jumpToComments
    }
  }
})
</script>

<style lang="scss" scoped>
#sidebar-navigator {
  svg {
    pointer-events: none;
  }
}
</style>
