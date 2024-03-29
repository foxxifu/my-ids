// 智能告警界面的脚本
import alarmService from '@/service/io/alarm'
import workFlowService from '@/service/workFlow'
import AlarmDetailDialgContent from '@/pages/io/alarm/alarmDetail/index.vue'
import AddDefectDialog from '@/pages/io/taskManage/addDefect/index.vue'

export default {
  created () {
    this.calMaxHeight();
  },
  components: {
    AlarmDetailDialgContent,
    AddDefectDialog
  },
  data () {
    return {
      maxHeight: 120,
      searchData: { // 搜索查询的数据
        page: 1,
        pageSize: 10
      },
      list: [{id: 1}],
      total: 0,
      multipleSelection: null, // 多选的数据
      isShowDetailDialog: false,
      rowData: null,
      isShowAddDefect: false,
      addDefectFormData: {},
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
    // '1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）；'
    alarmStatusFilter (val, vm) { // 告警处理状态的过滤器
      if (isNaN(val)) {
        return '-';
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
  mounted: function () {
    let self = this;
    self.searchListData();
    this.$nextTick(function () {
    })
    window.onresize = () => self.calMaxHeight();
  },
  methods: {
    calMaxHeight () { // 计算当前表格内容的最大高度
      let height = +(document.documentElement.clientHeight * 0.94).toFixed() - 470;
      if (height < 120) {
        height = 120;
      }
      this.maxHeight = height;
    },
    searchListData (isFirstPage) { // 查询智能告警的数据
      let self = this;
      if (isFirstPage) {
        self.searchData.page = 1;
      }
      self.searchData.index = self.searchData.page
      alarmService.analysisList(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
      });
    },
    tableSelectionChange (val) { // 多选的数据
      this.multipleSelection = val
    },
    showReperDialog (row) { // 显示弹出框的数据
      // let tmpData = Object.assign({}, row)
      this.rowData = row;
      this.isShowDetailDialog = true;
    },
    pageSizeChange (val) {
      this.searchData.pageSize = val;
      this.searchListData(true);
    },
    pageChange (val) {
      this.searchData.page = val;
      this.searchListData();
    },
    /**
     * 告警确认或者清除的事件
     * @param type : type=='clear' 清除告警； type=='sure'确认告警
     */
    clearOrSureAlarm: function (type) {
      let len = this.multipleSelection.length
      if (len === 0) {
        this.$message(this.$t('io.alarm.selectClearAlarm')) // 选择要清除的告警
        return
      }
      let self = this
      let message = this.$t('io.alarm.clearSelectAlarm') // '清除选中的告警?'
      if (type === 'sure') {
        message = this.$t('io.alarm.sureSelectAlarm') // '确认选中的告警？'
      }
      this.$confirm(message, this.$t('confirm'), {
        confirmButtonText: this.$t('sure'),
      }).then(() => {
        let ids = self.multipleSelection.map(item => {
          return item.id
        })
        let funStr = 'smartAlarmClear' // 清除的方法名称
        let tipMess = this.$t('delete')
        if (type === 'sure') {
          funStr = 'smartAlarmAck' // 确认的方法名称
          tipMess = this.$t('sure')
        }
        alarmService[funStr](ids).then(resp => {
          if (resp.code === 1) {
            this.$message(tipMess + this.$t('operatSuccess')) // 操作成功
            this.searchListData()
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
          alarmIds.push(alarmArr[i].id)
        }
      }
      let self = this;
      new Promise(function(resolve, reject){ // 做一些异步操作
        workFlowService.alarmToDefect({alarmIds: alarmIds.join(','), type: '2'}).then(resp => {
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
      this.$refs.smartAlarmTable.toggleRowSelection(row);
    },
  },
  watch: {},
  computed: {}
}
