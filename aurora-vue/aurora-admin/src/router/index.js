import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: '登录',
    hidden: true,
    component: () => import('../views/login/Login.vue')
  }
]
const createRouter = () =>
  new VueRouter({
    mode: 'history',
    routes: routes
  })
const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
