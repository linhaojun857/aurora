import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    collapse: false,
    tabList: [{ name: '首页', path: '/' }],
    userInfo: null,
    userMenus: [],
    pageState: {
      articleList: 1,
      category: 1,
      tag: 1,
      comment: 1,
      talkList: 1,
      user: 1,
      online: 1,
      role: 1,
      quartz: 1,
      friendLinks: 1
    }
  },
  mutations: {
    saveTab(state, tab) {
      if (state.tabList.findIndex((item) => item.path === tab.path) == -1) {
        state.tabList.push({ name: tab.name, path: tab.path })
      }
    },
    removeTab(state, tab) {
      var index = state.tabList.findIndex((item) => item.name === tab.name)
      state.tabList.splice(index, 1)
    },
    resetTab(state) {
      state.tabList = [{ name: '首页', path: '/' }]
    },
    trigger(state) {
      state.collapse = !state.collapse
    },
    login(state, user) {
      sessionStorage.setItem('token', user.token)
      state.userInfo = user
    },
    saveUserMenus(state, userMenus) {
      state.userMenus = userMenus
    },
    logout(state) {
      state.userInfo = null
      sessionStorage.removeItem('token')
      state.userMenus = []
    },
    updateAvatar(state, avatar) {
      state.userInfo.avatar = avatar
    },
    updateUserInfo(state, user) {
      state.userInfo.nickname = user.nickname
      state.userInfo.intro = user.intro
      state.userInfo.webSite = user.webSite
    },
    updateArticleListPageState(state, current) {
      state.pageState.articleList = current
    },
    updateCategoryPageState(state, current) {
      state.pageState.category = current
    },
    updateTagPageState(state, current) {
      state.pageState.tag = current
    },
    updateCommentPageState(state, current) {
      state.pageState.comment = current
    },
    updateTalkListPageState(state, current) {
      state.pageState.talkList = current
    },
    updateUserPageState(state, current) {
      state.pageState.user = current
    },
    updateOnlinePageState(state, current) {
      state.pageState.online = current
    },
    updateRolePageState(state, current) {
      state.pageState.role = current
    },
    updateQuartzPageState(state, current) {
      state.pageState.quartz = current
    },
    updateFriendLinkPageState(state, current) {
      state.pageState.current = current
    }
  },
  actions: {},
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
