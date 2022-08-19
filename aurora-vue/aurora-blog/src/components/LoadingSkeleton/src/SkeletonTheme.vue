<script lang="ts">
import { defineComponent, h } from 'vue'

export const DEFAULT_BACKGROUND = 'var(--skeleton-bg, #eeeeee)'
export const DEFAULT_HIGHLIGHT = 'var(--skeleton-hl, #f5f5f5)'
export const SkeletonStyle = {
  backgroundColor: DEFAULT_BACKGROUND,
  backgroundImage: `linear-gradient(
    90deg,
    ${DEFAULT_BACKGROUND},
    ${DEFAULT_HIGHLIGHT},
    ${DEFAULT_BACKGROUND}
  )`,
  animation: '',
  height: 'inherit',
  width: 'inherit',
  borderRadius: '3px',
  content: '"&zwnj;"'
}

export default defineComponent({
  name: 'ObSkeletonTheme',
  props: {
    color: {
      type: String,
      default: DEFAULT_BACKGROUND
    },
    highlight: {
      type: String,
      default: DEFAULT_HIGHLIGHT
    },
    duration: {
      type: Number,
      default: 1.5
    },
    tag: {
      type: String,
      default: 'div'
    },
    loading: Boolean
  },
  provide() {
    return {
      _themeStyle: this.themeStyle,
      _skeletonTheme: this
    }
  },
  setup() {
    const themeStyle = { ...SkeletonStyle }
    return { themeStyle }
  },
  render() {
    const { color, highlight, duration } = this
    this.themeStyle.backgroundColor = color
    this.themeStyle.backgroundImage = `linear-gradient(
      90deg,
      ${color},
      ${highlight},
      ${color}
    )`
    if (duration) {
      this.themeStyle.animation = `SkeletonLoading ${duration}s ease-in-out infinite`
    } else {
      this.themeStyle.animation = ''
      this.themeStyle.backgroundImage = ''
    }
    if (this.tag) {
      return h(this.tag, this.$slots.default)
    }
    return this.$slots.default
  }
})
</script>
