import platformService from '@/service/io/platform'

export default {
  name: 'platCard',
  components: {},
  created () {
    // 在创建之前根据传递过来的参数来确定当前显示的信息
    let devStatus = this.row.deviceStatus
    if (devStatus) {
      for (let i in this.showDetalDevTypeIds) {
        let devTypeId = this.showDetalDevTypeIds[i]
        if (devStatus[devTypeId]) {
          let tmpName = devTypeId + '';
          !this.searchData.activeName && (this.searchData.activeName = tmpName)
          this.showTabs.push({text: this.getNameByDevTypeId(devTypeId), name: tmpName})
        }
      }
    }
    this.searchData.stationCode = this.row.stationCode// 当前的电站
  },
  // 声明 props ['展开这一行的数据','他的父节点的宽度']
  props: ['row'],
  // 就像 data 一样，prop 也可以在模板中使用
  // 同样也可以在 vm 实例中通过 this.row 来使用
  data () {
    return {
      // 展示的信息 {text：xxx，name：xxxx}
      showTabs: [],
      showList: {
        1: [{text: this.$t('components.platformComponent.activePow'), pro: 'activePower'}, {text: this.$t('components.platformComponent.reactivePow'), pro: 'reactivePower'}],
        8: [{text: this.$t('components.platformComponent.activePow'), pro: 'activePower'}, {text: this.$t('components.platformComponent.reactivePow'), pro: 'reactivePower'}],
        15: [{text: this.$t('components.platformComponent.averageCurrent'), pro: 'photcI'}, {text: this.$t('components.platformComponent.averageVoltage'), pro: 'photcU'}],
      },
      // 显示详情的设备id,分别对应设备的设备类型id
      showDetalDevTypeIds: [1, 8, 15],
      list: [],
      total: 0,
      searchData: {
        activeName: '',
        page: 1,
        pageSize: 10,
        stationCode: ''
      }
    }
  },
  computed: {
  },
  methods: {
    /**
     * 查询分页的数据
     * @param isGotFirstPage 是否到第一页
     */
    searchDevList (isGotFirstPage) {
      var self = this
      isGotFirstPage && (this.searchData.page = 1)
      platformService.platformDevList(this.searchData).then(e => {
        if (e.code === 1 && e.results) {
          self.list = e.results.list
          self.total = e.results.total
        } else {
          self.list = []
          self.total = 0
        }
      })
    },
    // 当tab标签被点击的时候
    handleTabClick (tab, event) {
      // 都重新初始
      this.searchData.pageSize = 10
      // 数据清空？？
      this.searchDevList(true)
    },
    getNameByDevTypeId (devTypeId) {
      if (devTypeId === 1) {
        return this.$t('components.platformComponent.inverter')
      }
      if (devTypeId === 8) {
        return this.$t('components.platformComponent.boxTransformel')
      }
      return this.$t('components.platformComponent.junctionBox')
    }
  },
  mounted () {
    let self = this
    // TODO 查询数据
    self.searchDevList()
  }
}
