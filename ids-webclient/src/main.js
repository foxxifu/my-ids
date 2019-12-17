import Vue from 'vue'
import Element from 'element-ui'

import axios from 'axios'
import _ from 'lodash'
import ECharts from '@/components/ECharts/index.vue'
import FastClick from 'fastclick'
import VueLazyLoad from 'vue-lazyload'
import { directive as Clickaway } from 'vue-clickaway'
import VueDND from 'awe-dnd'

import 'normalize.css'
import 'font-awesome/css/font-awesome.min.css'
import './assets/css/index.css'
// import '../theme/index.css'
import './assets/css/stylus/main.less'
import './assets/css/lib.less'
import './assets/css/reset.less'

// import $ from 'expose-loader?$!jquery'

import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './i18n'
import * as filters from '@/utils/filter'

Vue.use(Element, {
  size: 'medium',
  i18n: (key, value) => i18n.t(key, value)
})

require('@/utils/util.js')
require('@/utils/prototype.js')

// add lodash 插件，用于用户行为管理
Object.defineProperty(Vue.prototype, '_', {value: _, enumerable: false})
// add axios 插件，用于ajax请求处理
Object.defineProperty(Vue.prototype, '$http', {value: axios})
// add moment 插件，用于时间格式化处理
const moment = require('moment')
require('moment/locale/' + (localStorage.getItem('lang') || 'zh') + '-' + (localStorage.getItem('region') || 'cn'))
// Object.defineProperty(Vue.prototype, '$moment', {value: moment})
Vue.use(require('vue-moment'), {
  moment
})
// add jQuery
// Object.defineProperty(Vue.prototype, '$', { value: $, enumerable: false })

if ('addEventListener' in document) {
  document.addEventListener('DOMContentLoaded', () => {
    FastClick.attach(document.body)
  }, false)
}

Vue.use(VueLazyLoad, {
  error: require('@/assets/images/defaultPic.png'),
  loading: require('@/assets/images/img_loading.gif')
})
// 注册filter
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
Vue.component('e-chart', ECharts)
Vue.directive('clickoutside', Clickaway)
Vue.use(VueDND)

Vue.config.productionTip = false
Vue.config.devtools = false
Vue.config.silent = true
Vue.config.errorHandler = () => {}
Vue.config.ignoredElements = [
  'my-custom-web-component', 'another-web-component'
]

/* eslint-disable no-new */
new Vue({
  el: '#main',
  i18n,
  router,
  store,
  template: '<App/>',
  components: {
    App
  },
  created() { // 在页面刷新的时候做一下保存
    // 在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem("store_logo")) { // 在加载的时候获取session缓存的数据，避免重新去查询
      this.$store.replaceState(Object.assign({}, this.$store.state, {'logoImgPath': sessionStorage.getItem("store_logo")}))
    }
  }
})
