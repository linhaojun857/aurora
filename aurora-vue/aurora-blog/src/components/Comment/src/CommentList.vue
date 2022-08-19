<template>
  <transition-group name="fade">
    <CommentItem v-for="comment in comments" :key="comment.id" :comment="comment"> </CommentItem>
  </transition-group>
</template>

<script lang="ts">
// @ts-nocheck
import { defineComponent, inject } from 'vue'
import CommentItem from './CommentItem.vue'

export default defineComponent({
  components: {
    CommentItem
  },
  setup() {
    return {
      comments: inject('comments')
    }
  }
})
</script>
