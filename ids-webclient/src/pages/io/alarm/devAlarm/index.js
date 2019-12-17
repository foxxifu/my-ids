import alarmService from '@/service/io/alarm'
import workFlowService from '@/service/workFlow'
import AlarmDetailDialgContent from '@/pages/io/alarm/alarmDetail/index.vue'
import AddDefectDialog from '@/pages/io/taskManage/addDefect/index.vue'

export default {
  name: 'devAlarm',
  components: {
    AlarmDetailDialgContent,
    AddDefectDialog
  },
  data () {
    return {
      fit: true,
      maxHeight: 120, // 最大高度
      list: [], // 表数据
      total: 0, // 总记录数,
      searchData: {
        page: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      multipleSelection: [], // table中多选的参数
      isShowDetailDialog: false, // 是否显示告警详情的对话框
      isShowAddDefect: false, // 是否显示转工单的对话框
      addDefectFormData: null, // 转工单的数据
      vm: this
    }
  },
  filters: {
    alarmLeveFilter (val, vm) { // 告警级别的过滤器
      if (!val || isNaN(val)) {
        return ''
      }
      if (val === 1) {
        // 严重
        return vm.$t('io.alarmLevelArr')[0]
      }
      if (val === 2) {
        // 一般
        return vm.$t('io.alarmLevelArr')[1]
      }
      if (val === 3) {
        // 提示
        return vm.$t('io.alarmLevelArr')[2]
      }
      // 未知
      return vm.$t('io.unKnow')
    },
    // '1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）；'
    alarmStatusFilter (val, vm) { // 告警处理状态的过滤器
      if (isNaN(val)) {
        return ''
      }
      if (val === 1) {
        // 未处理
        return vm.$t('io.alarmStatusArr')[0]
      }
      if (val === 2) {
        // 已确认
        return vm.$t('io.alarmStatusArr')[1]
      }
      if (val === 3) {
        // 处理中
        return vm.$t('io.alarmStatusArr')[2]
      }
      if (val === 4) {
        // 已处理
        return vm.$t('io.alarmStatusArr')[3]
      }
      if (val === 5) {
        // 已清除
        return vm.$t('io.alarmStatusArr')[4]
      }
      if (val === 6) {
        // 已恢复
        return vm.$t('io.alarmStatusArr')[5]
      }
      // '未知'
      return vm.$t('io.unKnow')
    },
  },
  created () {
    this.calcMaxHeight()
  },
  computed: {},

  methods: {
    calcMaxHeight () {
      let height = +(document.documentElement.clientHeight * 0.94).toFixed() - 470
      if (height < 120) {
        height = 120
      }
      this.maxHeight = height
    },
    repeatDetailClick: function (row) { // 修复建议的点击事件
      this.isShowDetailDialog = true
      this.rowData = row // 当前显示的详情的数据 ，可以传递needSearch参数查询后台数 [true,{alarmId: 55}等参数, '/api/xxx/xxx'查询的url地址或者service的具体方法]
    },
    searchAlarmList: function (isRestPage) {
      var self = this
      if (isRestPage) { // 判断是否重新设置页数
        this.resetPage()
      }
      this.searchData.index = this.searchData.page
      alarmService.alarmList(this.searchData).then(e => {
        if (e.code === 1 && e.results) {
          self.list = e.results.list
          self.total = e.results.count
        } else {
          self.list = []
          self.total = 0
        }
      })
    },
    resetPage: function () {
      this.searchData.page = 1
    },
    dateFormat: function (value, fmt) {
      if (!value) {
        return ''
      }
      // let date = new Date(value)
      // if (/(y+)/.test(fmt)) {
      //   fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
      // }
      // let o = {
      //   'M+': date.getMonth() + 1,
      //   'd+': date.getDate(),
      //   'h+': date.getHours(),
      //   'm+': date.getMinutes(),
      //   's+': date.getSeconds()
      // }
      // for (let k in o) {
      //   if (new RegExp(`(${k})`).test(fmt)) {
      //     let str = o[k] + ''
      //     fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str))
      //   }
      // }
      // return fmt
      return new Date(value).format(fmt)
    },
    // padLeftZero (str) {
    //   return ('00' + str).substr(str.length)
    // },
    pageSizeChange (val) {
      this.searchData.pageSize = val
      this.searchAlarmList(true)
    },
    pageChange (val) {
      this.searchData.page = val
      this.searchAlarmList(false)
    },

    // table中选择的项改变的事件
    tableSelectionChange (val) {
      this.multipleSelection = val
    },
    /**
     * 告警确认或者清除的事件
     * @param type : type=='clear' 清除告警； type=='sure'确认告警
     */
    clearOrSureAlarm: function (type) {
      let len = this.multipleSelection.length
      if (len === 0) { // 请选择要操作的记录
        this.$message(this.$t('io.alarm.selectClearAlarm'))
        return
      }
      let self = this
      // 清除选中的告警?
      let message = this.$t('io.alarm.clearSelectAlarm')
      if (type === 'sure') {
        // 确认选中的告警？
        message = this.$t('io.alarm.sureSelectAlarm')
      }
      this.$confirm(message, this.$t('confirm'), {
        confirmButtonText: this.$t('sure'),
      }).then(() => {
        let ids = self.multipleSelection.map(item => {
          return item.id
        })
        let funStr = 'devAlarmClear' // 清除的方法名称
        let tipMess = this.$t('delete')
        if (type === 'sure') {
          funStr = 'devAlarmAck' // 确认的方法名称
          tipMess = this.$t('sure')
        }
        alarmService[funStr](ids).then(resp => {
          if (resp.code === 1) {
            this.$message(tipMess + this.$t('operatSuccess')) // 操作成功
            this.searchAlarmList()
          } else {
            this.$message(tipMess + this.$t('operatFailed')) // 操作失败
          }
        })
      }).catch(() => {
        this.$message(this.$t('io.alarm.cancelDelete')) // 已取消操作
      })
    },
    clickToDefect () { // 转工单的弹出框
      let alarmArr = this.multipleSelection
      // 1. 判断是否选择了告警
      let len = alarmArr.length
      if (len === 0) {
        this.$message(this.$t('io.alarm.selectToDefectAlarm')) // '请选择要要转工单的告警'
        return
      }
      // 2. 判断选中的告警是不是相同的
      let alarmIds = []; // 保存要转工单的告警的id传递给后台获取数据
      alarmIds.push(alarmArr[0].id)
      if (len > 1) {
        let devName = alarmArr[0].devAlias
        for (let i = 1; i < len; i++) {
          if (devName !== alarmArr[i].devAlias) {
            this.$message(this.$t('io.alarm.selectSameDevToDefect')) // '请选择同一个设备的告警转工单'
            return
          }
          // 判断告警的处理状态，只有是未完成的才能处理
          alarmIds.push(alarmArr[i].id)
        }
      }
      let self = this;
      new Promise(function(resolve, reject){ // 做一些异步操作
        workFlowService.alarmToDefect({alarmIds: alarmIds.join(','), type: '1'}).then(resp => {
          resolve(resp) // 请求成功之后调用 调用then中的方法
        });
      }).then(function(result){ // 如果还要继续调用使用 return下一个then调用的参数就是return的值
        let datas = (result.code === 1 && result.results) || {}
        self.addDefectFormData = datas
        self.isShowAddDefect = true
      }).catch(() => {
        self.$message(this.$t('io.alarm.getDataFailed')) // '获取数据出错'
      });
    },
    toggleSelection (row, event, column) { // 点击行的事件
      this.$refs.devAlarmTable.toggleRowSelection(row);
    },
  },
  mounted () {
    var self = this
    window.onresize = () => self.calcMaxHeight()
    this.searchAlarmList(true)
  }
}
