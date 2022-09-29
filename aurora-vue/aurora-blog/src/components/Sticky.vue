<template>
  <div id="sticky" :style="{ height: height + 'px', zIndex: zIndex }">
    <div
      :class="className"
      :style="{
        top: isSticky ? (top === -1 ? 'initial' : top + 'px') : '',
        bottom: isBottom ? 0 : 'initial',
        zIndex: zIndex,
        position: position,
        width: width,
        height: height + 'px'
      }">
      <slot>
        <div>sticky</div>
      </slot>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

export default defineComponent({
  name: 'Sticky',
  props: {
    stickyTop: {
      type: Number,
      default: 0
    },
    zIndex: {
      type: Number,
      default: 1
    },
    className: {
      type: String,
      default: ''
    },
    stickyBottom: {
      type: Number,
      default: 0
    },
    endingElId: {
      type: String,
      default: ''
    },
    dynamicElClass: {
      type: String,
      default: ''
    }
  },
  setup() {
    let active = ref(false),
      position = ref('' as any),
      width = ref(),
      height = ref(),
      isSticky = ref(false),
      newTop = ref(0),
      top = ref(0),
      isBottom = ref(false)
    return {
      active,
      position,
      width,
      height,
      isSticky,
      newTop,
      top,
      isBottom
    }
  },
  mounted() {
    this.height = this.$el.getBoundingClientRect().height
    window.addEventListener('scroll', this.handleScroll)
    window.addEventListener('resize', this.handleResize)
  },
  activated() {
    this.handleScroll()
  },
  unmounted() {
    window.removeEventListener('scroll', this.handleScroll)
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    sticky(top: number, position: any) {
      if (this.active) {
        return
      }
      this.top = top
      this.position = position
      this.active = true
      this.width = this.width + 'px'
      this.isSticky = true
    },
    handleReset() {
      if (!this.active) {
        return
      }
      this.reset()
    },
    reset() {
      this.position = ''
      this.width = 'auto'
      this.active = false
      this.isSticky = false
    },
    handleScroll() {
      setTimeout(() => {
        const documentHeight = document.documentElement.scrollHeight
        const width = this.$el.getBoundingClientRect().width
        const height = this.$el.getBoundingClientRect().height
        if (this.dynamicElClass !== '') {
          const dynamicEl = this.$el.querySelector(this.dynamicElClass)
          this.height = dynamicEl.getBoundingClientRect().height || height
        }
        const scrollTop = window.scrollY
        this.width = width || 'auto'
        const offsetTop = this.$el.getBoundingClientRect().top
        const endingEl = this.endingElId !== '' ? document.getElementById(this.endingElId) : null
        const wrapperEl = document.getElementById('App-Wrapper')
        const endingElSpacing = parseInt(
          window.getComputedStyle(wrapperEl || document.documentElement).paddingBottom,
          10
        )
        const endingPos =
          endingEl && endingEl instanceof HTMLElement
            ? documentHeight -
              scrollTop -
              height -
              this.stickyTop -
              this.stickyBottom -
              endingEl.getBoundingClientRect().height -
              endingElSpacing
            : documentHeight

        if (offsetTop < this.stickyTop) {
          this.active = false
          if (endingPos <= 0) {
            this.isBottom = true
            this.sticky(-1, 'absolute')
          } else {
            this.isBottom = false
            this.sticky(this.stickyTop, 'fixed')
          }
          return
        }
        this.handleReset()
      }, 16)
    },
    handleResize() {
      if (this.isSticky) {
        this.width = this.$el.getBoundingClientRect().width + 'px'
      }
    }
  }
})
</script>
