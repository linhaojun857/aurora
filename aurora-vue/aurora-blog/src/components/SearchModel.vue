<template>
  <div
    v-if="openModal"
    id="search-modal"
    @keydown.esc="handleStatusChange(false)"
    @keydown.meta.k.stop.prevent="handleStatusChange(false)"
    @keydown.arrow-up.stop.prevent="handleArrowUp"
    @keydown.arrow-down.stop.prevent="handleArrowDown"
    @keydown.enter.stop.prevent="handleEnterDown"
    @click.self="handleStatusChange(false)"
    tabindex="-1">
    <transition name="fade-bounce-pure-y" mode="out-in">
      <div class="search-container" v-if="openSearchContainer">
        <header class="flex pt-4 pr-4 pl-4">
          <form class="search-form" action="">
            <label id="search-label" class="items-center flex justify-center" for="search-input">
              <svg
                class="text-ob fill-current stroke-current"
                width="32"
                height="32"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                data-reactroot="">
                <path
                  stroke-linejoin="round"
                  stroke-linecap="round"
                  stroke-width="1"
                  stroke=""
                  d="M15.9996 15.2877L15.2925 15.9948L21.2958 21.9981L22.0029 21.291L15.9996 15.2877Z"></path>
                <path
                  stroke-linejoin="round"
                  stroke-linecap="round"
                  stroke-width="1"
                  stroke=""
                  fill="rgba(0,0,0,0)"
                  d="M10 18C14.4183 18 18 14.4183 18 10C18 5.58172 14.4183 2 10 2C5.58172 2 2 5.58172 2 10C2 14.4183 5.58172 18 10 18Z"></path>
              </svg>
            </label>
            <input
              type="search"
              id="search-input"
              ref="searchInput"
              class="search-input"
              autocomplete="off"
              v-model="keyword"
              @input="searchKeyword" />
            <button
              v-show="keyword.length > 0"
              class="search-btn"
              type="reset"
              title="Clear the query"
              @click="handleResetInput"></button>
          </form>
        </header>
        <div id="Search-Dropdown" class="search-dropdown" v-if="searchResults !== null">
          <div>
            <section v-if="searchResults.length > 0">
              <div class="search-hit-label">Found {{ searchResults.length }} records</div>
              <ul id="search-menu">
                <li
                  v-for="(result, index) in searchResults"
                  :key="result.id"
                  :class="{
                    'search-hit': true,
                    active: index == menuActiveIndex
                  }"
                  :id="'search-hit-item-' + index">
                  <a href="javascript:void(0)" @click="handleLinkClick(result)">
                    <div class="search-hit-container">
                      <div class="search-hit-icon">
                        <svg width="20" height="20" viewBox="0 0 20 20">
                          <path
                            d="M17 6v12c0 .52-.2 1-1 1H4c-.7 0-1-.33-1-1V2c0-.55.42-1 1-1h8l5 5zM14 8h-3.13c-.51 0-.87-.34-.87-.87V4"
                            stroke="currentColor"
                            fill="none"
                            fill-rule="evenodd"
                            stroke-linejoin="round"></path>
                        </svg>
                      </div>
                      <div class="search-hit-content-wrapper">
                        <span class="search-hit-title" v-html="result.articleContent"></span>
                        <span class="search-hit-path" v-html="result.articleTitle"></span>
                      </div>
                      <div class="search-hit-action">
                        <svg class="DocSearch-Hit-Select-Icon" width="20" height="20" viewBox="0 0 20 20">
                          <g
                            stroke="currentColor"
                            fill="none"
                            fill-rule="evenodd"
                            stroke-linecap="round"
                            stroke-linejoin="round">
                            <path d="M18 3v4c0 2-2 4-4 4H2"></path>
                            <path d="M8 17l-6-6 6-6"></path>
                          </g>
                        </svg>
                      </div>
                    </div>
                  </a>
                </li>
              </ul>
            </section>
            <section v-else>
              <div class="search-hit-label">
                {{ t('settings.recently-search') }}
              </div>
              <ul id="search-menu">
                <li
                  v-for="(result, index) in recentResults"
                  :key="result.id"
                  :class="{
                    'search-hit': true,
                    active: index == menuActiveIndex
                  }"
                  :id="'search-hit-item-' + index">
                  <a href="javascript:void(0)" @click="handleLinkClick(result)">
                    <div class="search-hit-container">
                      <div class="search-hit-icon">
                        <svg width="20" height="20" viewBox="0 0 20 20">
                          <path
                            d="M17 6v12c0 .52-.2 1-1 1H4c-.7 0-1-.33-1-1V2c0-.55.42-1 1-1h8l5 5zM14 8h-3.13c-.51 0-.87-.34-.87-.87V4"
                            stroke="currentColor"
                            fill="none"
                            fill-rule="evenodd"
                            stroke-linejoin="round"></path>
                        </svg>
                      </div>
                      <div class="search-hit-content-wrapper">
                        <span class="search-hit-title" v-html="result.articleContent"></span>
                        <span class="search-hit-path" v-html="result.articleTitle"></span>
                      </div>
                      <div class="search-hit-action">
                        <svg class="DocSearch-Hit-Select-Icon" width="20" height="20" viewBox="0 0 20 20">
                          <g
                            stroke="currentColor"
                            fill="none"
                            fill-rule="evenodd"
                            stroke-linecap="round"
                            stroke-linejoin="round">
                            <path d="M18 3v4c0 2-2 4-4 4H2"></path>
                            <path d="M8 17l-6-6 6-6"></path>
                          </g>
                        </svg>
                      </div>
                    </div>
                  </a>
                </li>
              </ul>
            </section>
          </div>
        </div>
        <div class="search-startscreen" v-else-if="!isEmpty">
          <p>{{ t('settings.no-recent-search') }}</p>
        </div>
        <div class="search-startscreen" v-else>
          <p>{{ t('settings.no-search-result') }}</p>
        </div>
        <div class="search-footer">
          <div class="search-logo">
            <a href="" target="_blank" rel="noopener noreferrer">
              <span class="search-label">{{ t('settings.searched-by') }}</span>
              <!-- <svg width="77" height="19" style="color: #5468ff">
              <path
                d="M2.5067 0h14.0245c1.384.001 2.5058 1.1205 2.5068 2.5017V16.5c-.0014 1.3808-1.1232 2.4995-2.5068 2.5H2.5067C1.1232 18.9995.0014 17.8808 0 16.5V2.4958A2.495 2.495 0 01.735.7294 2.505 2.505 0 012.5068 0zM37.95 15.0695c-3.7068.0168-3.7068-2.986-3.7068-3.4634L34.2372.3576 36.498 0v11.1794c0 .2715 0 1.9889 1.452 1.994v1.8961zm-9.1666-1.8388c.694 0 1.2086-.0397 1.5678-.1088v-2.2934a5.3639 5.3639 0 00-1.3303-.1679 4.8283 4.8283 0 00-.758.0582 2.2845 2.2845 0 00-.688.2024c-.2029.0979-.371.2362-.4919.4142-.1268.1788-.185.2826-.185.5533 0 .5297.185.8359.5205 1.0375.3355.2016.7928.3053 1.365.3053v-.0008zm-.1969-8.1817c.7463 0 1.3768.092 1.8856.2767.5088.1838.9195.4428 1.2204.7717.3068.334.5147.7777.6423 1.251.1327.4723.196.991.196 1.5603v5.798c-.5235.1036-1.05.192-1.5787.2649-.7048.1037-1.4976.156-2.3774.156-.5832 0-1.1215-.0582-1.6016-.167a3.385 3.385 0 01-1.2432-.5364 2.6034 2.6034 0 01-.8037-.9565c-.191-.3922-.29-.9447-.29-1.5208 0-.5533.11-.905.3246-1.2863a2.7351 2.7351 0 01.8849-.9329c.376-.242.8029-.415 1.2948-.5187a7.4517 7.4517 0 011.5381-.156 7.1162 7.1162 0 011.6667.2024V8.886c0-.259-.0296-.5061-.093-.7372a1.5847 1.5847 0 00-.3245-.6158 1.5079 1.5079 0 00-.6119-.4158 2.6788 2.6788 0 00-.966-.173c-.5206 0-.9948.0634-1.4283.1384a6.5481 6.5481 0 00-1.065.259l-.2712-1.849c.2831-.0986.7048-.1964 1.2491-.2943a9.2979 9.2979 0 011.752-.1501v.0008zm44.6597 8.1193c.6947 0 1.2086-.0405 1.567-.1097v-2.2942a5.3743 5.3743 0 00-1.3303-.1679c-.2485 0-.503.0177-.7573.0582a2.2853 2.2853 0 00-.688.2024 1.2333 1.2333 0 00-.4918.4142c-.1268.1788-.1843.2826-.1843.5533 0 .5297.1843.8359.5198 1.0375.3414.2066.7927.3053 1.365.3053v.0009zm-.191-8.1767c.7463 0 1.3768.0912 1.8856.2759.5087.1847.9195.4436 1.2204.7717.3.329.5147.7786.6414 1.251a5.7248 5.7248 0 01.197 1.562v5.7972c-.3466.0742-.874.1602-1.5788.2648-.7049.1038-1.4976.1552-2.3774.1552-.5832 0-1.1215-.0573-1.6016-.167a3.385 3.385 0 01-1.2432-.5356 2.6034 2.6034 0 01-.8038-.9565c-.191-.3922-.2898-.9447-.2898-1.5216 0-.5533.1098-.905.3245-1.2854a2.7373 2.7373 0 01.8849-.9338c.376-.2412.8029-.4141 1.2947-.5178a7.4545 7.4545 0 012.325-.1097c.2781.0287.5672.081.879.156v-.3686a2.7781 2.7781 0 00-.092-.738 1.5788 1.5788 0 00-.3246-.6166 1.5079 1.5079 0 00-.612-.415 2.6797 2.6797 0 00-.966-.1729c-.5205 0-.9947.0633-1.4282.1384a6.5608 6.5608 0 00-1.065.259l-.2712-1.8498c.283-.0979.7048-.1957 1.2491-.2935a9.8597 9.8597 0 011.752-.1494zm-6.79-1.072c-.7576.001-1.373-.6103-1.3759-1.3664 0-.755.6128-1.3664 1.376-1.3664.764 0 1.3775.6115 1.3775 1.3664s-.6195 1.3664-1.3776 1.3664zm1.1393 11.1507h-2.2726V5.3409l2.2734-.3568v10.0845l-.0008.0017zm-3.984 0c-3.707.0168-3.707-2.986-3.707-3.4642L59.7069.3576 61.9685 0v11.1794c0 .2715 0 1.9889 1.452 1.994V15.0703zm-7.3512-4.979c0-.975-.2138-1.7873-.6305-2.3516-.4167-.571-.9998-.852-1.747-.852-.7454 0-1.3302.281-1.7452.852-.4166.5702-.6195 1.3765-.6195 2.3516 0 .9851.208 1.6473.6254 2.2183.4158.576.9998.8587 1.7461.8587.7454 0 1.3303-.2885 1.747-.8595.4158-.5761.6237-1.2315.6237-2.2184v.0009zm2.3132-.006c0 .7609-.1099 1.3361-.3356 1.9654a4.654 4.654 0 01-.9533 1.6076A4.214 4.214 0 0155.613 14.69c-.579.2412-1.4697.3795-1.9143.3795-.4462-.005-1.3303-.1324-1.9033-.3795a4.307 4.307 0 01-1.474-1.0316c-.4115-.4445-.7293-.9801-.9609-1.6076a5.3423 5.3423 0 01-.3465-1.9653c0-.7608.104-1.493.3356-2.1155a4.683 4.683 0 01.9719-1.5958 4.3383 4.3383 0 011.479-1.0257c.5739-.242 1.2043-.3567 1.8864-.3567.6829 0 1.3125.1197 1.8906.3567a4.1245 4.1245 0 011.4816 1.0257 4.7587 4.7587 0 01.9592 1.5958c.2426.6225.3643 1.3547.3643 2.1155zm-17.0198 0c0 .9448.208 1.9932.6238 2.431.4166.4386.955.6579 1.6142.6579.3584 0 .6998-.0523 1.0176-.1502.3186-.0978.5721-.2134.775-.3517V7.0784a8.8706 8.8706 0 00-1.4926-.1906c-.8206-.0236-1.4452.312-1.8847.8468-.4335.5365-.6533 1.476-.6533 2.3516v-.0008zm6.2863 4.4485c0 1.5385-.3938 2.662-1.1866 3.3773-.791.7136-2.0005 1.0712-3.6308 1.0712-.5958 0-1.834-.1156-2.8228-.334l.3643-1.7865c.8282.173 1.9202.2193 2.4932.2193.9077 0 1.555-.1847 1.943-.5533.388-.3686.578-.916.578-1.643v-.3687a6.8289 6.8289 0 01-.8848.3349c-.3634.1096-.786.167-1.261.167-.6246 0-1.1917-.0979-1.7055-.2944a3.5554 3.5554 0 01-1.3244-.8645c-.3642-.3796-.6541-.8579-.8561-1.4289-.2028-.571-.3068-1.59-.3068-2.339 0-.7034.1099-1.5856.3245-2.1735.2198-.5871.5316-1.0949.9542-1.515.4167-.42.9255-.743 1.5213-.98a5.5923 5.5923 0 012.052-.3855c.7353 0 1.4114.092 2.0707.2024.6592.1088 1.2204.2236 1.6776.35v8.945-.0008zM11.5026 4.2418v-.6511c-.0005-.4553-.3704-.8241-.8266-.8241H8.749c-.4561 0-.826.3688-.8265.824v.669c0 .0742.0693.1264.1445.1096a6.0346 6.0346 0 011.6768-.2362 6.125 6.125 0 011.6202.2185.1116.1116 0 00.1386-.1097zm-5.2806.852l-.3296-.3282a.8266.8266 0 00-1.168 0l-.393.3922a.8199.8199 0 000 1.164l.3237.323c.0524.0515.1268.0397.1733-.0117.191-.259.3989-.507.6305-.7372.2374-.2362.48-.4437.7462-.6335.0575-.0354.0634-.1155.017-.1687zm3.5159 2.069v2.818c0 .081.0879.1392.1622.0987l2.5102-1.2964c.0574-.0287.0752-.0987.0464-.1552a3.1237 3.1237 0 00-2.603-1.574c-.0575 0-.115.0456-.115.1097l-.0008-.0009zm.0008 6.789c-2.0933.0005-3.7915-1.6912-3.7947-3.7804C5.9468 8.0821 7.6452 6.39 9.7387 6.391c2.0932-.0005 3.7911 1.6914 3.794 3.7804a3.7783 3.7783 0 01-1.1124 2.675 3.7936 3.7936 0 01-2.6824 1.1054h.0008zM9.738 4.8002c-1.9218 0-3.6975 1.0232-4.6584 2.6841a5.359 5.359 0 000 5.3683c.9609 1.661 2.7366 2.6841 4.6584 2.6841a5.3891 5.3891 0 003.8073-1.5725 5.3675 5.3675 0 001.578-3.7987 5.3574 5.3574 0 00-1.5771-3.797A5.379 5.379 0 009.7387 4.801l-.0008-.0008z"
                fill="currentColor"
                fill-rule="evenodd"
              ></path>
            </svg> -->
              <img
                class="mr-1.5"
                src="https://img-blog.csdnimg.cn/20210313122054101.png"
                alt="ObsidianNext Logo"
                height="20"
                width="20" />
              <span class="text-ob">ElasticSearch</span>
            </a>
          </div>
          <ul class="search-commands">
            <li>
              <span class="search-commands-key">
                <svg width="15" height="15">
                  <g
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="1.2">
                    <path d="M12 3.53088v3c0 1-1 2-2 2H4M7 11.53088l-3-3 3-3"></path>
                  </g>
                </svg>
              </span>
              <span class="search-commands-label">
                {{ t('settings.cmd-to-select') }}
              </span>
            </li>
            <li>
              <span class="search-commands-key">
                <svg width="15" height="15">
                  <g
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="1.2">
                    <path d="M7.5 3.5v8M10.5 8.5l-3 3-3-3"></path>
                  </g>
                </svg>
              </span>
              <span class="search-commands-key">
                <svg width="15" height="15">
                  <g
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="1.2">
                    <path d="M7.5 11.5v-8M10.5 6.5l-3-3-3 3"></path>
                  </g>
                </svg>
              </span>
              <span class="search-commands-label">
                {{ t('settings.cmd-to-navigate') }}
              </span>
            </li>
            <li>
              <span class="search-commands-key">
                <svg width="15" height="15">
                  <g
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="1.2">
                    <path
                      d="M13.6167 8.936c-.1065.3583-.6883.962-1.4875.962-.7993 0-1.653-.9165-1.653-2.1258v-.5678c0-1.2548.7896-2.1016 1.653-2.1016.8634 0 1.3601.4778 1.4875 1.0724M9 6c-.1352-.4735-.7506-.9219-1.46-.8972-.7092.0246-1.344.57-1.344 1.2166s.4198.8812 1.3445.9805C8.465 7.3992 8.968 7.9337 9 8.5c.032.5663-.454 1.398-1.4595 1.398C6.6593 9.898 6 9 5.963 8.4851m-1.4748.5368c-.2635.5941-.8099.876-1.5443.876s-1.7073-.6248-1.7073-2.204v-.4603c0-1.0416.721-2.131 1.7073-2.131.9864 0 1.6425 1.031 1.5443 2.2492h-2.956"></path>
                  </g>
                </svg>
              </span>
              <span class="search-commands-label">
                {{ t('settings.cmd-to-close') }}
              </span>
            </li>
          </ul>
        </div>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import { useSearchStore } from '@/stores/search'
import { computed, defineComponent, onBeforeMount, onMounted, onUnmounted, onUpdated, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import api from '@/api/api'
import { useLocalStore } from '@/stores/local'
import { da } from 'element-plus/es/locale'

/**
 * Lodash package is imported through CDN.
 *
 * For version 4.17.21
 */
// declare const _: any

export default defineComponent({
  name: 'SearchModel',
  setup() {
    const searchStore = useSearchStore()
    const localStore = useLocalStore()
    const searchInput = ref<HTMLDivElement>()
    const searchIndexStatus = ref(false)
    const searchResults = ref<any>([])
    const router = useRouter()
    const openModal = ref(false)
    const openSearchContainer = ref(false)
    const keyword = ref('')
    const recentResults = ref()
    const menuActiveIndex = ref(0)
    const menuMaxIndex = ref(0)
    const isEmpty = ref(false)
    const { t } = useI18n()

    /**
     * Handlers
     */
    const handleStatusChange = (status: boolean) => {
      searchStore.setOpenModal(status)
    }

    const handleLinkClick = (result: any) => {
      saveRecentSearch(result)
      router.push({ path: '/articles/' + result.id })
      searchStore.setOpenModal(false)
    }

    const saveRecentSearch = async (result: any) => {
      let temp = localStore.weight
      result.weight = localStore.weight
      localStore.weight++
      localStore.recentSearch.forEach((item: any) => {
        if (item.id === result.id) {
          item.weight = -1
        }
      })
      localStore.recentSearch = localStore.recentSearch.filter((item: any) => item.weight > 0)
      result.weight = temp
      localStore.recentSearch.push(result)
      localStore.recentSearch = localStore.recentSearch.sort((a: any, b: any) => b.weight - a.weight)
    }

    const handleResetInput = () => {
      keyword.value = ''
      searchResults.value = []
      isEmpty.value = false
      resetIndex(recentResults.value.length)
    }

    const handleArrowUp = () => {
      if (isEmpty.value === true) return
      if (menuActiveIndex.value === 0) {
        menuActiveIndex.value = menuMaxIndex.value
      } else {
        menuActiveIndex.value = menuActiveIndex.value - 1
      }
      moveMenuWindow()
    }

    const handleArrowDown = () => {
      if (isEmpty.value === true) return
      if (menuActiveIndex.value === menuMaxIndex.value) {
        menuActiveIndex.value = 0
      } else {
        menuActiveIndex.value = menuActiveIndex.value + 1
      }
      moveMenuWindow()
    }

    const moveMenuWindow = () => {
      const searchDropdownEl = document.getElementById('Search-Dropdown')
      const activeMenuEl = document.getElementById(`search-hit-item-${menuActiveIndex.value}`)
      const containerHeight = searchDropdownEl?.getBoundingClientRect().height
      const menuElHeight = activeMenuEl?.getBoundingClientRect().height

      if (menuElHeight && containerHeight && searchDropdownEl) {
        const currentMenuElheight = 36 + menuElHeight * (menuActiveIndex.value + 1)
        const heightDiff = currentMenuElheight - containerHeight
        if (heightDiff > 0) {
          searchDropdownEl.scrollTo({
            top: heightDiff
          })
        }
      }

      if (searchDropdownEl && menuActiveIndex.value === 0) {
        searchDropdownEl.scrollTo({
          top: 0
        })
      }
    }

    const handleEnterDown = () => {
      if (searchResults.value.length === 0 && recentResults.value.length > 0) {
        console.log(recentResults)
        handleLinkClick(recentResults.value[menuActiveIndex.value])
      } else if (searchResults.value.length > 0) {
        handleLinkClick(searchResults.value[menuActiveIndex.value])
      }
    }

    /**
     * Search for records, debounce this function for 500ms
     * for user to finish typing. Prevent uncessary searches
     * between typing of a keyword.
     */
    const searchKeyword = (e: any) => {
      if (e.target.value !== '') {
        let params = {
          keywords: e.target.value
        }
        api.searchArticles(params).then(({ data }) => {
          searchResults.value = data.data
          if (searchResults.value.length > 0) {
            resetIndex(searchResults.value.length)
            isEmpty.value = false
          } else {
            isEmpty.value = true
          }
        })
      } else {
        isEmpty.value = false
        searchResults.value = []
        resetIndex(recentResults.value.length)
      }
    }

    /** Refetch the data of recent search results. */
    const reloadRecentResult = () => {
      recentResults.value = localStore.recentSearch.reverse()
      resetIndex(recentResults.value.length)
    }

    const resetIndex = (max: number) => {
      menuActiveIndex.value = 0
      menuMaxIndex.value = max - 1
    }

    /**
     * Initialize search modual default data.
     */
    const initSearch = async () => {
      searchIndexStatus.value = false
      isEmpty.value = false
      //   await searchStore.fetchSearchIndex().then(() => {
      //     searchIndexStatus.value = true
      //   })
    }

    onBeforeMount(initSearch)

    onMounted(() =>
      /** Delay focus for animation to finish. */
      setTimeout(() => {
        if (searchInput.value) searchInput.value.focus()
      }, 200)
    )

    onUpdated(() => {
      /** Reset default values. */
      keyword.value = ''
      searchResults.value = []

      /** Delay focus for animation to finish. */
      setTimeout(() => {
        if (searchInput.value) searchInput.value.focus()
      }, 200)
    })

    onUnmounted(() => {
      document.body.classList.remove('modal--active')
    })

    watch(
      () => searchStore.openModal,
      (status: any) => {
        /**
         * This watch is used to delay the animation
         * of the search box container.
         */
        if (!(status instanceof Boolean)) {
          reloadRecentResult()
        }
        openModal.value = status
        setTimeout(() => {
          openSearchContainer.value = status
        }, 200)
      }
    )

    return {
      openModal: computed(() => openModal.value),
      openSearchContainer: computed(() => openSearchContainer.value),
      searchResultsCount: computed(() => {
        return t('settings.search-result').replace('[total]', String(searchResults.value.length))
      }),
      handleStatusChange,
      handleLinkClick,
      searchInput,
      searchResults,
      keyword,
      isEmpty,
      searchKeyword,
      recentResults,
      handleResetInput,
      handleArrowUp,
      handleArrowDown,
      handleEnterDown,
      menuActiveIndex,
      t
    }
  }
})
</script>
