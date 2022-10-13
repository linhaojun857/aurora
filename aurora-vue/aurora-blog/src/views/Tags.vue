<template>
  <div class="flex flex-col">
    <div class="post-header">
      <Breadcrumb :current="t('menu.tags')" />
      <h1 class="post-title text-white uppercase">{{ t('menu.tags') }}</h1>
    </div>
    <div class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block">
      <TagList>
        <template v-if="tags != '' && tags.length > 0">
          <TagItem v-for="tag in tags" :key="tag.id" :id="tag.id" :name="tag.tagName" :count="tag.count" size="xl" />
        </template>
      </TagList>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, onUnmounted, toRef } from 'vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { useI18n } from 'vue-i18n'
import { useTagStore } from '@/stores/tag'
import { TagList, TagItem } from '@/components/Tag'
import { useCommonStore } from '@/stores/common'
import api from '@/api/api'

export default defineComponent({
  name: 'Tag',
  components: { Breadcrumb, TagList, TagItem },
  setup() {
    const commonStore = useCommonStore()
    const { t } = useI18n()
    const tagStore = useTagStore()
    onMounted(() => {
      fetchTags()
    })
    onUnmounted(() => {
      commonStore.resetHeaderImage()
    })
    const fetchTags = () => {
      api.getAllTags().then(({ data }) => {
        tagStore.tags = data.data
      })
    }
    return {
      tags: toRef(tagStore.$state, 'tags'),
      t
    }
  }
})
</script>

<style lang="scss" scoped></style>
