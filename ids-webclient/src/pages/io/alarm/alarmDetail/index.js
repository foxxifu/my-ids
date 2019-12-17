// 告警详情的弹出框

// import alarmService from "@/service/io/alarm";
import operationService from '@/service/operation'

// 默认的属性配置项
const props = {
  rowData: { // 传递的行数据
    type: Object,
    default: null
  },
  needSearch: { // 是否在这个界面通过ajax请求去获取数据 [boolean, 请求后台的数据(告警id), 类型(1:设备告警 2：智能告警), stationName]
    type: [Object, Array],
    default: () => [false, {id: 0}, 1, {stationName: ''}]
  }
}

export default {
  props: props,
  created () {
    let needSearch = this.needSearch;
    if (needSearch[0]) { // 如果需要查询数据
      // 通过ajax请求 needSearch[1] 是告警详情的查询数据
      let id = this.needSearch[1].id
      operationService.getAlarmDetail({alarmId: id, alarmType: this.needSearch[2]}).then(resp => {
        if (resp.code === 1) {
          let datas = resp.results || {}
          this.$set(this, 'currentData', datas);
        } else {
          this.$message(resp.message);
        }
      })
      // if (this.needSearch[2] === 1) {
      //   alarmService.infoRepair({id: id}).then(resp => {
      //     if (resp && resp.code === 1) {
      //       let datas = resp.results
      //       datas.firstHappenTime = datas.firstDate
      //       datas.recoverTime = datas.lastDate
      //       datas.devAddr = datas.alarmPosition
      //       datas.devName = datas.devAlias
      //       datas.alarmLevel = datas.alarmLeve;
      //       datas.stationName = this.needSearch[3].stationName
      //       this.$set(this, 'currentData', datas);
      //     } else {
      //       this.$message(resp.message);
      //     }
      //   });
      // } else { // 查询智能告警
      //
      // }
    } else {
      this.currentData = this.rowData || {};
    }
  },
  data () {
    return {
      currentData: {}, // 当前的数据
      vm: this
    }
  },
  filters: {
    alarmLeveFilter (val, vm) { // 告警级别的过滤器
      if (!val || isNaN(val)) {
        return '';
      }
      if (val === 1){
        // 严重
        return vm.$t('io.alarmLevelArr')[0]
      }
      if (val === 2){
        // 一般
        return vm.$t('io.alarmLevelArr')[1]
      }
      if (val === 3){
        // 提示
        return vm.$t('io.alarmLevelArr')[2]
      }
      // 未知
      return vm.$t('io.unKnow')
    },
  },
  mounted: function () {
    this.$nextTick(function () {
    })
  },
  methods: {
    textHtmlFilter (val) {
      if (!val) {
        return '-'
      }
      return val.replace(/\n|\r\n/g, "<br/>")
    },
    getValueNoReplace(orgVal, alarmId, key) { // 获取不替换的信息
      if (this.needSearch[2] === 2 && alarmId) { // 如果是智能告警
        return this.$t('dataConverter.analysis.' + alarmId + '.' + key)
      }
      return orgVal;
    },
    getWithReplace(orgVal, alarmId, key) { // 需要替换的内容
      if (this.needSearch[2] === 2 && alarmId) { // 如果是智能告警
        return this.$t('dataConverter.analysis.' + alarmId + '.' + key) + (orgVal && (orgVal.lastIndexOf('#') > 0 ? orgVal.substring(orgVal.lastIndexOf('#')) : ''))
      }
      return orgVal
    }
  },
  watch: {},
  computed: {},
  beforeDestroy () {
    console.log('destroy detail');
  }
}
