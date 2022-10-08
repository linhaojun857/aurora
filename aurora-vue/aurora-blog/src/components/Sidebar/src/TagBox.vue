<template>
  <div class="sidebar-box">
    <SubTitle :title="'titles.tag_list'" icon="tag" />
    <TagList>
      <template v-if="tags != '' && tags.length > 0">
        <TagItem v-for="tag in tags" :key="tag.id" :id="tag.id" :name="tag.tagName" :count="tag.count" size="xs" />
        <div class="flex flex-row items-center hover:opacity-50 mr-2 mb-2 cursor-pointer transition-all">
          <span class="text-center px-3 py-1 rounded-md text-sm">
            <b class="border-b-2 border-ob hover:text-ob">
              <router-link to="/tags"> {{ t('settings.more-tags') }} ... </router-link>
            </b>
          </span>
        </div>
      </template>
    </TagList>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, toRef } from 'vue'
import { SubTitle } from '@/components/Title'
import { useTagStore } from '@/stores/tag'
import { TagList, TagItem } from '@/components/Tag'
import { useI18n } from 'vue-i18n'
import api from '@/api/api'

export default defineComponent({
  name: 'ObTag',
  components: { SubTitle, TagList, TagItem },
  setup() {
    const tagStore = useTagStore()
    const { t } = useI18n()
    onMounted(() => {
      fetchTopTenTags()
    })
    const fetchTopTenTags = () => {
      api.getTopTenTags().then(({ data }) => {
        tagStore.homeTags = data.data
      })
    }
    return {
      tags: toRef(tagStore.$state, 'homeTags'),
      t
    }
  }
})
</script>

<style lang="scss">
.sidebar-box li.ob-skeleton {
  @apply mr-2 mb-2;
}
</style>
