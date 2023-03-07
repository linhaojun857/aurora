<template>
  <div class="h-98 w-full rounded-2xl relative shadow-xl mb-8" :style="gradientBackground">
    <div
      class="ob-gradient-cut-plate absolute bg-ob-deep-900 rounded-xl opacity-90 flex justify-center items-center pt-4 px-6 shadow-lg hover:shadow-2xl duration-300"
      data-dia="author">
      <div class="profile absolute w-full flex flex-col justify-center items-center">
        <div class="flex flex-col justify-center items-center">
          <img v-if="websiteConfig.authorAvatar" :class="avatarClass" :src="websiteConfig.authorAvatar" />
          <img v-else :class="avatarClass" :src="default" />
          <h2 class="text-center pt-4 text-4xl font-semibold text-ob-bright">
            <template v-if="websiteConfig.author">
              {{ websiteConfig.author }}
            </template>
            <ob-skeleton v-else height="2.25rem" width="7rem" />
          </h2>
          <span class="h-1 w-14 rounded-full mt-2" :style="gradientBackground" />
          <p
            v-if="websiteConfig.authorIntro"
            class="pt-6 px-10 w-full text-s text-center"
            v-html="websiteConfig.authorIntro" />
          <p v-else class="pt-6 px-10 w-full text-sm text-center flex flex-col gap-2">
            <ob-skeleton :count="2" height="20px" width="10rem" />
          </p>
        </div>
        <div class="h-full w-full flex flex-col flex-1 justify-end items-end">
          <Social />
          <ul class="grid grid-cols-4 pt-4 w-full px-2 text-lg">
            <li class="col-span-1 text-center">
              <span class="text-ob-bright">
                {{ articleCount }}
              </span>
              <p class="text-base">{{ t('settings.articles') }}</p>
            </li>
            <li class="col-span-1 text-center">
              <span class="text-ob-bright">{{ talkCount }}</span>
              <p class="text-base">{{ t('settings.talks') }}</p>
            </li>
            <li class="col-span-1 text-center">
              <span class="text-ob-bright">{{ categoryCount }}</span>
              <p class="text-base">{{ t('settings.categories') }}</p>
            </li>
            <li class="col-span-1 text-center">
              <span class="text-ob-bright">{{ tagCount }}</span>
              <p class="text-base">{{ t('settings.tags') }}</p>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { useAppStore } from '@/stores/app'
import { computed, defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'
import Social from '@/components/Social.vue'

export default defineComponent({
  name: 'Profile',
  components: { Social },
  setup() {
    const appStore = useAppStore()
    const { t } = useI18n()
    return {
      default: 'https://static.linhaojun.top/aurora/config/52a81cd2772167b645569342e81ce312.jpg',
      avatarClass: computed(() => {
        return {
          'ob-avatar': true,
          [appStore.themeConfig.profile_shape]: true
        }
      }),
      themeConfig: computed(() => appStore.themeConfig),
      gradientBackground: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      websiteConfig: computed(() => {
        return appStore.websiteConfig
      }),
      articleCount: computed(() => appStore.articleCount),
      talkCount: computed(() => appStore.talkCount),
      categoryCount: computed(() => appStore.categoryCount),
      tagCount: computed(() => appStore.tagCount),
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.profile {
  top: -7%;
  height: 100%;
  max-height: 100%;
}
</style>
