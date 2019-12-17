import platformService from '@/service/io/platform'
// 引入自定义组件plat-card
import PlatCard from '@/components/platformComponent/index.vue'

export default {
  name: 'platform',
  components: {
    PlatCard
  },
  filters: {},
  created () {
    this.calMaxHeight(); // 计算最大高度
  },
  data () {
    return {
      parentWidth: 0,
      maxHeight: 120, // 表格最大高度
      total: 0,
      list: [],
      searchBarData: {},
      searchData: {
        page: 1,
        pageSize: 10
      },
      // 加载图片错误的时候的图片
      errorImg01: 'this.src="/assets/images/defaultPic.png"'
    }
  },
  computed: {
  },
  methods: {
    calMaxHeight () { // 计算最大高度
      let height = +(document.documentElement.clientHeight * 0.94).toFixed() - 360;
      if (height < 120) {
        height = 120;
      }
      this.maxHeight = height;
    },
    /**
     * 查询数据
     * @param isToFirstPage 是否跳转到第一页
     */
    searchPlatformList (isToFirstPage) {
      let self = this
      isToFirstPage && (this.searchData.page = 1)
      platformService.platformList(this.searchData).then(e => {
        if (e.code === 1 && e.results) {
          self.list = e.results.list
          self.total = e.results.total
        } else {
          self.list = []
          self.total = 0
        }
      })
    },
    // 查询的事件
    searchBarClick () {
      let searchDataParms = ['stationName', 'status']// 查询参数的字段名称
      for (let i in searchDataParms) {
        let param = searchDataParms[i]
        if (this.searchBarData[param]) {
          this.searchData[param] = this.searchBarData[param]
        } else {
          delete this.searchData[param]
        }
      }
      this.searchPlatformList(true)
    },
    isShowStatusImage (row, key) {
      return row && row.hasOwnProperty(key)
    },
    getImageClass (row, key) {
      return 's s' + key + '_' + row[key]
    },
    getStatusImage (status) {
      if (status === 1) {
        return '/assets/images/io/table/blue.png'
      }
      if (status === 2) {
        return '/assets/images/io/table/red.png'
      }
      return '/assets/images/io/table/gray.png'
    },
    // 获取图片的路径
    getPicImgUrl (fileId) {
      let path = ''
      if (fileId) {
        path = '/biz/fileManager/downloadFile?fileId=' + fileId + '&time=' + new Date().getTime()
      } else {
        // 默认图片
        path = '/assets/images/defaltPic.png'
      }
      return path
    },
  },
  mounted () {
    let self = this;
    self.searchPlatformList();
    window.onresize = () => self.calMaxHeight();
  }
}
