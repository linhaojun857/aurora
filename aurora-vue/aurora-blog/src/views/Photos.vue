<template>
  <div>
    <Breadcrumb :current="t('menu.album')" />
    <div class="flex flex-col">
      <div class="post-header">
        <h1 v-if="photoAlbumName != ''" class="post-title text-white uppercase">{{ photoAlbumName }}</h1>
        <ob-skeleton
          v-else
          class="post-title text-white uppercase"
          width="30%"
          height="clamp(1.2rem, calc(1rem + 3.5vw), 4rem)" />
      </div>
      <div class="main-grid">
        <div class="relative">
          <div class="post-html">
            <div
              class="list-lis"
              v-infinite-scroll="loadDataFromServer"
              :infinite-scroll-immediate-check="false"
              :infinite-scroll-disabled="noResult"
              infinite-scroll-watch-disabled="scrollDisabled"
              :infinite-scroll-distance="isMobile ? 0 : 30">
              <div class="photo-wrap">
                <img
                  v-for="(item, index) of photos"
                  class="photo"
                  :key="index"
                  :src="item"
                  @click="handlePreview(index)" />
              </div>
            </div>
          </div>
        </div>
        <div class="col-span-1">
          <Sidebar>
            <Profile />
          </Sidebar>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, reactive, toRefs } from 'vue'
import { useI18n } from 'vue-i18n'
import { useCommonStore } from '@/stores/common'
import { useRoute, onBeforeRouteUpdate } from 'vue-router'
import { Sidebar, Profile } from '../components/Sidebar'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { v3ImgPreviewFn } from 'v3-img-preview'
import api from '@/api/api'

export default defineComponent({
  name: 'Photos',
  components: { Breadcrumb, Sidebar, Profile },
  setup() {
    const { t } = useI18n()
    const route = useRoute()
    const commonStore = useCommonStore()
    const reactiveData = reactive({
      photoAlbumName: '' as any,
      noResult: false,
      photos: [] as any,
      current: 1,
      size: 10,
      albumId: route.params.albumId
    })
    onBeforeRouteUpdate((to) => {
      reactiveData.photoAlbumName = ''
      reactiveData.photos = []
      reactiveData.noResult = false
      reactiveData.current = 1
      reactiveData.albumId = to.params.albumId
      loadDataFromServer()
    })
    const handlePreview = (index: any) => {
      v3ImgPreviewFn({ images: reactiveData.photos, index })
    }
    const loadDataFromServer = () => {
      let params = {
        current: reactiveData.current,
        size: reactiveData.size
      }
      api.getPhotosBuAlbumId(reactiveData.albumId, params).then(({ data }) => {
        if (data.data.photos.length > 0) {
          reactiveData.current++
          reactiveData.photoAlbumName = data.data.photoAlbumName
          reactiveData.photos.push(...data.data.photos)
        } else {
          reactiveData.noResult = true
        }
      })
    }
    return {
      ...toRefs(reactiveData),
      handlePreview,
      loadDataFromServer,
      isMobile: computed(() => commonStore.isMobile),
      t
    }
  }
})
</script>
<style lang="scss" scoped>
.photo-wrap {
  display: flex;
  flex-wrap: wrap;
}
.photo {
  margin: 3px;
  cursor: pointer;
  flex-grow: 1;
  object-fit: cover;
  height: 200px;
}
.photo-wrap::after {
  content: '';
  display: block;
  flex-grow: 9999;
}
@media (max-width: 759px) {
  .photo {
    width: 100%;
  }
}
</style>
