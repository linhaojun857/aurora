<template>
  <div>
    <div class="nav-bar">
      <div class="hambuger-container" @click="trigger">
        <i :class="isFold" />
      </div>
      <el-breadcrumb>
        <el-breadcrumb-item v-for="item of breadcrumbs" :key="item.path">
          <span v-if="item.redirect">{{ item.name }}</span>
          <router-link v-else :to="item.path">{{ item.name }}</router-link>
        </el-breadcrumb-item>
      </el-breadcrumb>
      <div class="right-menu">
        <div class="screen-full" @click="fullScreen">
          <i class="iconfont el-icon-myicwindowzoom48px" />
        </div>
        <el-dropdown @command="handleCommand">
          <el-avatar :size="40" :src="this.$store.state.userInfo.avatar" />
          <i class="el-icon-caret-bottom" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="setting"> <i class="el-icon-s-custom" />个人中心 </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <i class="iconfont el-icon-mytuichu" />退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="tabs-view-container">
      <div class="tabs-wrapper">
        <span :class="isActive(item)" v-for="item of this.$store.state.tabList" :key="item.path" @click="goTo(item)">
          {{ item.name }}
          <i class="el-icon-close" v-if="item.path != '/'" @click.stop="removeTab(item)" />
        </span>
      </div>
      <div class="tabs-close-item" style="float: right" @click="closeAllTab">全部关闭</div>
    </div>
  </div>
</template>

<script>
import { resetRouter } from '@/router'
export default {
  created() {
    let matched = this.$route.matched.filter((item) => item.name)
    const first = matched[0]
    if (first && first.name !== '首页') {
      matched = [{ path: '/', name: '首页' }].concat(matched)
    }
    this.breadcrumbs = matched
    this.$store.commit('saveTab', this.$route)
  },
  data: function () {
    return {
      isSearch: false,
      fullscreen: false,
      breadcrumbs: []
    }
  },
  methods: {
    goTo(tab) {
      this.$router.push({ path: tab.path })
    },
    removeTab(tab) {
      this.$store.commit('removeTab', tab)
      if (tab.path == this.$route.path) {
        var tabList = this.$store.state.tabList
        this.$router.push({ path: tabList[tabList.length - 1].path })
      }
    },
    trigger() {
      this.$store.commit('trigger')
    },
    handleCommand(command) {
      if (command == 'setting') {
        this.$router.push({ path: '/setting' })
      }
      if (command == 'logout') {
        this.axios.post('/api/users/logout').then(({ data }) => {
          this.$store.commit('logout')
          this.$store.commit('resetTab')
          resetRouter()
          this.$router.push({ path: '/login' })
        })
      }
    },
    closeAllTab() {
      this.$store.commit('resetTab')
      this.$router.push({ path: '/' })
    },
    fullScreen() {
      let element = document.documentElement
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    }
  },
  computed: {
    isActive() {
      return function (tab) {
        if (tab.path == this.$route.path) {
          return 'tabs-view-item-active'
        }
        return 'tabs-view-item'
      }
    },
    isFold() {
      return this.$store.state.collapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    }
  }
}
</script>

<style scoped>
.nav-bar {
  display: flex;
  align-items: center;
  padding-left: 15px;
  padding-right: 30px;
  height: 50px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.hambuger-container {
  font-size: 1.25rem;
  cursor: pointer;
  margin-right: 24px;
}
.tabs-wrapper {
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  width: 95%;
}
.tabs-view-container {
  display: flex;
  position: relative;
  padding-left: 10px;
  padding-right: 10px;
  height: 33px;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
}
.tabs-view-item {
  display: inline-block;
  cursor: pointer;
  height: 25px;
  line-height: 25px;
  border: 1px solid #d8dce5;
  color: #495060;
  background: #fff;
  padding: 0 8px;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 5px;
}
.tabs-close-item {
  position: absolute;
  right: 10px;
  display: inline-block;
  cursor: pointer;
  height: 25px;
  line-height: 25px;
  border: 1px solid #d8dce5;
  color: #495060;
  background: #fff;
  padding: 0 8px;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 5px;
}
.tabs-view-item-active {
  display: inline-block;
  cursor: pointer;
  height: 26px;
  line-height: 26px;
  padding: 0 8px;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 5px;
  background-color: #42b983;
  color: #fff;
  border-color: #42b983;
}
.tabs-view-item-active:before {
  content: '';
  background: #fff;
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  position: relative;
  margin-right: 2px;
}
.el-icon-close {
  padding: 0.1rem;
}
.el-icon-close:hover {
  border-radius: 50%;
  background: #b4bccc;
  transition-duration: 0.3s;
}
.right-menu {
  margin-left: auto;
  display: flex;
  align-items: center;
}
.el-icon-caret-bottom {
  margin-left: 0.5rem;
  font-size: 0.75rem;
}
.screen-full {
  cursor: pointer;
  margin-right: 1rem;
  font-size: 1.25rem;
}
*::-webkit-scrollbar {
  width: 0.5rem;
  height: 6px;
}
*::-webkit-scrollbar-thumb {
  border-radius: 0.5rem;
  background-color: rgba(144, 147, 153, 0.3);
}
</style>
