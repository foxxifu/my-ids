import { TOGGLE_SIDEBAR } from '@/store/mutation-types'
import stationOverviewService from '@/service/stationOverview'

import localStorage from '@/utils/localStorage'

import HeaderTop from './children/header/index.vue'
import SideBar from './children/sidebar/index.vue'
import NavBar from './children/navbar/index.vue'
import ContentArea from './children/content_area/index.vue'

export default {
  name: 'Layout',
  components: {
    HeaderTop,
    SideBar,
    NavBar,
    ContentArea,
  },
  inject: ['reload'],
  data () {
    return {
      sideBarOpen: false,
      isSubMenuShow: false,
      isShowStationList: false, // 是否显示电站列表
      stationName: '', // 搜索电站的名称
      stationList: [], // 电站列表的信息
      timerStationList: null,
    }
  },
  created () {
    this.showSubMenu()
    this.getStationList() // 获取电站信息
  },
  mounted () {
    const _this = this
    _this.$nextTick(() => {
      document.title = _this.$t('systemName')
    })
    // 5分钟请求一次数据
    _this.timerStationList = setInterval(function () {
      if (_this.$route.meta.showNav && _this.$store.state.resources.station) {
        _this.getStationList()
      }
    }, 5 * 60 * 1000)
  },
  computed: {
    showSidebar (vm) {
      return vm.$route.meta.showSidebar
    },
    settingsMenuList (vm) {
      let resultArr = []
      if (vm.$route.meta.showSidebar === true) {
        resultArr = vm.$store.state.permissionRoutes.filter(item => item.type === 'settings')
        resultArr = resultArr && resultArr.length > 0 ? resultArr[0].children : []
      } else if (vm.$route.meta.showSidebar === 'epc') {
        resultArr = vm.$store.state.permissionRoutes.filter(item => {
          return item.filterType === 'epc'
        })
        resultArr = resultArr && resultArr.length > 0 ? resultArr[0].children : []
      }
      return resultArr
    },
    stationFilter () { // 电站的过滤
      let _this = this
      return _this.stationList.filter(item => {
        return (item.stationName || '').toLowerCase().indexOf((_this.stationName || '').toLowerCase()) >= 0
      })
    }
  },
  methods: {
    /**
     * 左侧菜单 收起/展开 切换
     */
    toggleSideBar () {
      this.sideBarOpen = !this.sideBarOpen
      this.$store.commit(TOGGLE_SIDEBAR, this.sideBarOpen)
      setTimeout(window.onresize, 200)
    },
    /**
     * 获取电站列表的数据
     */
    getStationList () {
      let data = []
      stationOverviewService.getPowerStationList({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}// 数据
        let stationSize = datas.length
        if (stationSize > 0) {
          for (let i = 0; i < stationSize; i++) {
            data.push({
              id: datas[i].id,
              status: datas[i].stationCurrentState,
              stationName: datas[i].stationName,
              stationCode: datas[i].stationCode
            })
          }
        }
      })
      this.$set(this, 'stationList', data)
    },
    showStationList () {
      this.isShowStationList = true
    },
    toStation (station) {
      let routeData = this.$router.resolve({
        path: '/station'
      })
      localStorage.setStore('stationParams', {stationCode: station.stationCode, stationName: station.stationName})
      if (this.$route.fullPath.indexOf('/station/') >= 0) {
        this.reload()
      } else {
        window.open(routeData.href, '_blank')
      }
    },

    getCurrentMenu (menuList = [], index = 0) {
      if (index + 1 > menuList.length) return menuList
      let routes = []
      let route = menuList[index]
      if (route.noDropdown) {
        if (!route.children || route.children.length === 0) {
          routes = this.getCurrentMenu(menuList, index + 1)
        } else {
          routes = route
        }
      } else {
        routes = this.getCurrentMenu(route.children)
      }
      return routes
    },
    showSubMenu () {
      // debugger
      // let leafRoute = this.getCurrentMenu(this.$store.state.permissionRoutes)
      // if (leafRoute && this.$route.path.indexOf(leafRoute.path) != -1) {
      //   this.$router.push(leafRoute.children && leafRoute.children[0])
      // } else {
      // }
      this.isSubMenuShow = this.$route.meta.showSidebar && this.$route.fullPath.indexOf('/settings') >= 0
    },
  },
  watch: {
    $route () {
      this.showSubMenu()
    }
  },
  beforeDestroy () {
    if (this.timerStationList) {
      clearInterval(this.timerStationList)
    }
  }
}
