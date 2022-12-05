<template>
  <div class="sidebar-box">
    <SubTitle :title="'titles.website_info'" icon="website-info" />
    <ul class="mx-auto">
      <li class="pb-3">
        <span class="text-sm font-medium">{{ t('settings.running-time') }}:</span>
        <span class="text-sm font-medium text-right float-right" v-if="websiteCreateTime != ''">
          {{ websiteCreateTime }}
        </span>
        <ob-skeleton v-else class="float-right" tag="span" width="136px" height="16px" />
      </li>
      <li class="pb-2">
        <span class="text-sm font-medium">{{ t('settings.view-count') }}:</span>
        <span class="text-sm font-medium text-right float-right" v-if="viewCount">{{ viewCount }}</span>
        <ob-skeleton v-else class="float-right" tag="span" width="60px" height="16px" />
      </li>
    </ul>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, onUnmounted, ref } from 'vue'
import { SubTitle } from '@/components/Title'
import { useAppStore } from '@/stores/app'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'WebsiteInfo',
  components: { SubTitle },
  setup() {
    const { t } = useI18n()
    const appStore = useAppStore()
    const websiteCreateTime = ref('')
    const viewCount = ref(0)
    let timer: any
    onMounted(() => {
      runTime()
      timer = setInterval(runTime, 1000)
    })
    onUnmounted(() => {
      clearInterval(timer)
    })
    const runTime = () => {
      if (!appStore.websiteConfig.websiteCreateTime) {
        return
      }
      let timeold = new Date().getTime() - new Date(appStore.websiteConfig.websiteCreateTime).getTime()
      let msPerDay = 24 * 60 * 60 * 1000
      let daysold = Math.floor(timeold / msPerDay)
      let str = ''
      let day = new Date()
      str += daysold + '天'
      str += day.getHours() + '时'
      str += day.getMinutes() + '分'
      str += day.getSeconds() + '秒'
      websiteCreateTime.value = str
      viewCount.value = appStore.viewCount
    }
    return {
      websiteCreateTime,
      viewCount,
      t
    }
  }
})
</script>
