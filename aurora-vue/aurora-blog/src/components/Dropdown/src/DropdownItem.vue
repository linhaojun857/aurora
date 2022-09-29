<template>
  <div
    @click.stop.prevent="handleClick"
    class="block cursor-pointer hover:bg-ob-trans my-1 px-4 py-1 font-medium hover:text-ob-bright">
    <slot />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useDropdownStore } from '@/stores/dropdown'

export default defineComponent({
  name: 'DropdownItem',
  props: {
    name: String
  },
  setup(props) {
    const dropdownStore = useDropdownStore()
    const handleClick = () => {
      dropdownStore.setCommand(String(props.name))
    }
    return { handleClick }
  }
})
</script>
