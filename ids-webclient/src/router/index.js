import Vue from 'vue'
import Router from 'vue-router'
import { SET_PERMISSION_ROUTES, RESOURCES } from '@/store/mutation-types'
import { cookie } from 'cookie_js'
import store from '../store'

import io from '@/router/io'
import pm from '@/router/pm'
import pam from '@/router/pam'
import todo from '@/router/todo'
import settings from '@/router/settings'
import exhibition from '@/router/exhibition'
import station from '@/router/station'

Vue.use(Router)

const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')
const Login = resolve => require.ensure([], () => resolve(require('@/pages/login/index.vue')), 'login')
const Home = resolve => require.ensure([], () => resolve(require('@/pages/home/index.vue')), 'home')
const Error = resolve => require.ensure([], () => resolve(require('@/pages/error/index.vue')), 'error')
// const NoRight = resolve => require.ensure([], () => resolve(require('@/pages/error/index.vue')), 'noRight')

const Wisdom = resolve => require.ensure([], () => resolve(require('@/pages/wisdom/index.vue')), 'wisdom')

const whiteList = ['/login']

const router = new Router({
  // mode: 'history',
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      if (from.meta.keepAlive) {
        from.meta.savedPosition = document.body.scrollTop
      }
      return {x: 0, y: to.meta.savedPosition || 0}
    }
  },
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
  ]
})

const asyncRouterMap = [
  {
    path: '/home',
    name: 'menu.home',
    icon: 'icon-home',
    type: 'menu',
    redirect: '/home/index',
    noDropdown: true,
    component: Layout,
    children: [{
      path: 'index',
      component: Home,
      meta: {role: ['home'], showNav: true}
    }]
  },
  ...io,
  ...pm,
  ...pam,
  {
    path: '/wisdom',
    component: Layout,
    name: 'menu.wisdom',
    redirect: '/wisdom/index',
    icon: 'icon-wisdom',
    noDropdown: true,
    type: 'menu',
    children: [
      {
        path: 'index',
        component: Wisdom,
        type: 'menu',
        // meta: {role: ['wisdom']}
      }
    ]
  },
  ...settings,
  ...todo,
  ...exhibition,
  ...station,
  {path: '/', redirect: '/home', name: 'ROOT', hidden: true},
  {path: '/404', component: Error, name: '404', hidden: true},
  // {path: '/401', component: NoRight, name: '401', hidden: true},
  {path: '*', redirect: '/404', name: 'no-router', hidden: true},
]

const hasPermission = (roles, route) => {
  if (route.meta && route.meta.role) {
    return roles.some(role => route.meta.role.indexOf(role) >= 0)
  }
  return true
}

const filterAsyncRouter = (asyncRouter, roles) => {
  const accessedRouters = asyncRouter.filter(route => {
    if (hasPermission(roles, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, roles)
      }
      return true
    }
    return false
  })
  return accessedRouters
}

const generateRoutes = (roles) => {
  if (!roles) return []
  const resources = {
    1001: 'home',
    1002: 'io',
    1006: 'pm',
    // 1030: 'pam',

    1009: 'todo',
    1010: 'exhibition',

    1012: 'station_home',
    1032: 'devUpgrade',
    1033: 'station_alarm',
    1034: 'station_report',

    1004: 'enterprise',
    1028: 'organization',

    1015: 'regionManager',
    1016: 'roleManager',
    1017: 'accountManager',

    1018: 'dataRecovery',
    1019: 'kpiCorrection',
    1020: 'kpiRecalculation',

    1021: 'pointManage',
    1022: 'alarmConfig',
    1023: 'stationParam',

    1007: 'stationManage',
    1024: 'devManage',
    1025: 'devConfig',

    // 1041: 'oneWorkTicket',
    // 1042: 'twoWorkTicket',
    // 1043: 'operationTicket',
    // 1044: 'operationTicketTemplate',

    1026: 'systemParam',

    1027: 'licenceManage',
  }
  const settingsResources = [1004, 1028, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1007, 1024, 1025, 1026,
    1027, 1029, 1030, 1031, 1032, 1035]
  const stationResources = [1012]
  const todoResources = [1009]
  const exhibitionResources = [1010]

  let settings = false
  let station = false
  let todo = false
  let exhibition = false

  let results = roles.map(item => {
    if (settingsResources.indexOf(item) !== -1) {
      settings = true
    }
    if (stationResources.indexOf(item) !== -1) {
      station = true
    }
    if (todoResources.indexOf(item) !== -1) {
      todo = true
    }
    if (exhibitionResources.indexOf(item) !== -1) {
      exhibition = true
    }
    return resources[item]
  })
  results = results.filter(item => !!item)
  store.commit(RESOURCES, {
    has: results && results.length > 0,
    settings,
    station,
    todo,
    exhibition,
  })
  let accessedRouters
  if (results.indexOf('admin') >= 0) {
    accessedRouters = asyncRouterMap
  } else {
    accessedRouters = filterAsyncRouter(asyncRouterMap, results)
  }
  return accessedRouters
}
/**
 * 清除logo信息
 */
const clearLogo = () => {
  // 清除logo信息
  sessionStorage.getItem("store_logo") && sessionStorage.removeItem('store_logo')
}

router.beforeEach((to, from, next) => {
  const userToken = cookie.get('tokenId')
  if (userToken && userToken !== 'undefined' && userToken !== 'null') { // 判断是否有token
    if (to.path === '/login') {
      next({path: '/'})
    } else {
      if (!store.state.roles || store.state.roles.length === 0) {
        store.dispatch('GetUserAuthorize').then((e) => {
          if (e.code === 1) {
            const permissionRoutes = generateRoutes(store.state.roles)
            if (store.state.resources.has) {
              store.commit(SET_PERMISSION_ROUTES, permissionRoutes)
              router.addRoutes(permissionRoutes)
              next({...to})
            } else {
              cookie.remove('tokenId')
              clearLogo()
              next('/login')
            }
          } else {
            cookie.remove('tokenId')
            clearLogo()
            next('/login')
          }
        })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      clearLogo()
      next('/login') // 否则全部重定向到登录页
    }
  }
})

export default router
