// 电站列表的js脚本
import operationService from '@/service/operation'
let statusArr; // 状态的数组
let stationTypeArr; // 电站类型

const getDisStatus = (val) => {
  if (!val) {
    return '';
  }
  if (val === 1) { // 故障
    return statusArr[0]
  }
  if (val === 2) { // 通讯中断
    return statusArr[1]
  }
  if (val === 3) { // 正常
    return statusArr[2]
  }
  return statusArr[3]
}

export default {
  data () {
    return {
      vm: this,
      // 搜索栏的表单数据
      searchBarData: {
        page: 1,
        pageSize: 10
      },
      list: [], // 当前页的表格数据
      total: 0, // 总记录数
      maxHeight: 120, // 表格内容的最大高度，最小值是120
    }
  },
  filters: {
    devStatusFilter (devStatus, vm) { // 设置连接状态
      var str = '';
      if (!devStatus) {
        return str;
      }
      if (devStatus.hasOwnProperty('1')) { // 逆变器
        str += vm.$t('io.plantform.devTypeArr')[0] + '[' + getDisStatus(devStatus['1'], vm) + '] | ';
      }
      if (devStatus.hasOwnProperty('8')) { // 箱变
        str += vm.$t('io.plantform.devTypeArr')[1] + '[' + getDisStatus(devStatus['8'], vm) + '] | ';
      }
      if (devStatus.hasOwnProperty('15')) { // 直流汇流箱
        str += vm.$t('io.plantform.devTypeArr')[2] + '[' + getDisStatus(devStatus['15'], vm) + '] | ';
      }
      let len = str.length;
      if (len > 0) {
        str = str.substring(0, len - 3);
      }
      return str;
    },
    statusFilter (val) { // 电站状态的过滤器
      return getDisStatus(val);
    },
    stationTypeFilter (val) { // 电站类型的过滤器
      if (!val) {
        return '';
      }
      if (val === 1) { // 地面式
        return stationTypeArr[0]
      }
      if (val === 2) { // 分布式
        return stationTypeArr[1]
      }
      if (val === 3) { // 户用
        return stationTypeArr[2]
      }
      return stationTypeArr[3] // 错误的类型
    }
  },
  created () {
    this.calMaxHeight(); // 计算当前表格的最大高度
    // 为了解决filter的国际化，定义的这几个数组
    statusArr = this.$t('io.stationStatusArr')
    stationTypeArr = this.$t('io.plantform.plantTypeArr')
  },
  mounted () {
    let self = this;
    self.searchDatas(); // 查询list的数据
    this.$nextTick(function () {
    });
    window.onresize = () => self.calMaxHeight()
  },
  methods: {
    // 查询数据
    searchDatas (isToInitData) {
      let self = this;
      if (isToInitData) { // 是否将数据返回为初始状态,除了当前显示的页数
        self.searchBarData.page = 1;
        self.searchBarData.orderFiled = null; // 排序的字段colName
        self.searchBarData.order = null; // 排序的方式具体是升序还是降序
        self.$refs.stationList.clearSort(); // 清除排序的信息
      }
      // 查询数据
      self.searchBarData.index = self.searchBarData.page
      operationService.getStationDetailList(self.searchBarData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.total = datas.count || 0;
        self.list = datas.list || [];
      });
    },
    isShowStatusImage (devState, key) { // 判断是否显示当前的图片
      return devState && devState.hasOwnProperty(key)
    },
    getImageClass (devState, key) { // 获取当前元素的class
      return 's s_' + key + ' s' + key + '_' + devState[key]
    },
    calMaxHeight () { // 计算最大的高度
      let height = +(document.documentElement.clientHeight * 0.94).toFixed() - 360;
      if (height < 120) {
        height = 120;
      }
      this.maxHeight = height;
    },
    pageChange (val) { // 页数改变的事件
      this.searchBarData.page = val;
      this.searchDatas();
    },
    pageSizeChange (val) { // 每页显示的数量改变的事件
      this.searchBarData.pageSize = val;
      this.searchDatas(true);
    },
    sortChange (params) { // 排序方式改变
      let self = this;
      self.searchBarData.orderFiled = params.prop; // 排序的字段colName
      self.searchBarData.order = params.order; // 排序的方式具体是升序还是降序
      self.searchBarData.page = 1;
      self.searchDatas();
    },
    toOneStation (row) { // 跳转到单电站
      this.$confirm(this.$t('io.plantform.goToOneStation')).then(() => {
        let routeData = this.$router.resolve({
          name: 'station',
          path: '/station',
          query: {sid: row.id, stationCode: row.stationCode},
          params: {sid: row.id, stationCode: row.stationCode}
        })
        let target = '_blank'
        if (this.$route.meta.role && this.$route.meta.role.indexOf('station') >= 0) {
          target = '_self'
        }
        window.open(routeData.href, target)
      })
    }
  },
  watch: {},
  computed: {}
}
