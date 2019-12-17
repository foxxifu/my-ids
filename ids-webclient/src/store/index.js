import Vue from 'vue'
import Vuex from 'vuex'

import mutations from './mutations'
import actions from './actions'

Vue.use(Vuex)

const state = {
  roles: null,
  permissionRoutes: null,
  toggleSidebar: false,
  resources: {
    has: false,
    todo: false,
    exhibition: false,
    settings: false,
    station: false,
  },
  logoImgPath: '/assets/images/logo.png', // 显示企业的logo内容,默认是使用前端的图片
  lang: localStorage.getItem('lang') || 'zh' // 当前的语言，默认是中文
}

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production', // 严格模式,如果 state的改变不是由mutations 触发,throw error
  state,
  mutations,
  actions,
})
