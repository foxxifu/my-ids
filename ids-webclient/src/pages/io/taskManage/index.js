import { cookie } from 'cookie_js'

import workFlowService from '@/service/workFlow'
import AddDefectDialog from '@/pages/io/taskManage/addDefect/index.vue'
import DefectDetalDailog from '@/pages/io/taskManage/defectDetail/index.vue'

export default {
  components: {
    AddDefectDialog,
    DefectDetalDailog
  },
  data () {
    return {
      maxHeight: 120, // 表格table的最大高度
      taskNumObj: { // 数量的obj
        total: 0,
        reback: 0, // 回退
        dealing: 0, // 消缺中
        notsure: 0, // 待确认
        waiting: 0 // 待审核
      },
      searchBarData: { // 搜索栏的数据
        page: 1,
        pageSize: 10
      },
      total: 0, // 分页的总记录数
      list: [], // 表格的当前数据的数据
      isShowAddDefect: false,
      addDefectFormData: {},
      multipleSelection: null, // 选择的选择rows
      isShowDefectDetail: false, // 是否显示任务详情的弹出框
      defectId: null, // 任务详情的缺陷id
      isDoExect: false, // 是否是执行
      loginUserId: null, // 当前登录的用户
      vm: this
    }
  },
  created () {
    this.calMaxHeight();
    try {
      this.loginUserId = +JSON.parse(cookie.get('userInfo')).id
    } catch (e) {
      console.error('excpeiotn::', e)
    }
  },
  filters: {
    procStateFliter (val, vm) {
      // 流程状态 0:待分配 1：消缺中 2：待审核 3：已完成
      if (val === null || val === undefined || isNaN(val)) {
        return '-'
      }
      if (val === '0') {
        // 待分配
        return vm.$t('io.defectStatusArr')[0]
      }
      if (val === '1') {
        // 消缺中
        return vm.$t('io.defectStatusArr')[1]
      }
      if (val === '2') {
        // 待审核
        return vm.$t('io.defectStatusArr')[2]
      }
      if (val === '3') {
        // 已完成
        return vm.$t('io.defectStatusArr')[3]
      }
      // '未知'
      return vm.$t('io.unKnow')
    }
  },
  mounted: function () {
    let self = this;
    self.getWorkDatas();
    this.$nextTick(function () {
    })
    window.onresize = () => self.calMaxHeight()
  },
  methods: {
    getWorkDatas (isInit) { // 获取任务列表的总数
      if (isInit) {
        this.searchBarData.page = 1;
      }
      this.searchBarData.index = this.searchBarData.page
      this.getDefectCount();
      this.getWorkFlowList();
    },
    getDefectCount () { // 获取缺陷的数量
      let self = this
      // 获取缺陷的数量
      workFlowService.getWorkFlowCount(self.searchBarData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.taskNumObj.allocated = datas.allocated || 0 // 待分配
        self.taskNumObj.dealing = datas.dealing || 0 // 消缺中
        self.taskNumObj.waiting = datas.waiting || 0 // 待审核
        self.taskNumObj.today = datas.today || 0 // 今日已消缺
        self.taskNumObj.total = datas.count || 0 // 总数
      })
    },
    getWorkFlowList () { // 获取列表的数据
      let self = this;
      workFlowService.getWorkFlowDefectsByCondition(self.searchBarData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.list = datas.list || []
        self.total = datas.count || 0;
      })
    },
    calMaxHeight() {
      let height = document.documentElement.clientHeight * 0.94 - 520;
      if (height < 120) {
        height = 120;
      }
      this.maxHeight = height;
    },
    addDefectClick (ev) { // 新增缺陷的弹出框
      this.isShowAddDefect = true;
      this.addDefectFormData = {};
    },
    showDefectDetalClick (row) { // 查看任务详情的页面
      this.defectId = row.defectId;
      this.isDoExect = false;
      this.isShowDefectDetail = true;
    },
    executeDefectDetalClick (row) { // 执行任务详情的页面
      this.defectId = row.defectId;
      this.isDoExect = true;
      this.isShowDefectDetail = true;
    },
    copyDefectClick (ev) { // 复制缺陷按钮的点击事件
      // 判断是否选择了缺陷的内容
      let len = (this.multipleSelection && this.multipleSelection.length) || 0;
      if (len === 0 || len > 1) {
        this.$message((len === 0 && this.$t('io.defect.selectCopyDefect')) || this.$t('io.defect.onlyCanSelectOne') + len);
        return;
      }
      let self = this;
      self.$confirm(this.$t('io.defect.sureCopyDefect'), this.$t('confirm'), { // '确认复制选中的缺陷?', '提示',
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        let defectId = self.multipleSelection[0].defectId;
        // 通过ajax请求去查询数据
        workFlowService.getWorkFlowDefectDetails(defectId).then(resp => {
          if (resp.code === 1) {
            self.addDefectFormData = resp.results || {};
            setTimeout(function () {
              self.isShowAddDefect = true;
            }, 1)
            // self.isShowAddDefect = true;
          } else {
            self.$message(this.$t('io.alarm.getDataFailed'));
          }
        });
      });
    },
    saveSuccess () { // 新增缺陷成功后刷新列表
      this.getWorkDatas()
    },
    handleSelectionChange(val) { // 选择的数据改变的事件
      this.multipleSelection = val;
    },
    toggleSelection (row, event, column) {
      this.$refs.multipleDefectTable.toggleRowSelection(row);
    },
    pageSizeChange (val) {
      this.searchBarData.pageSize = val;
      this.getWorkDatas(true);
    },
    pageChange (val) {
      this.searchBarData.page = val;
      this.getWorkDatas();
    }
  },
  watch: {},
  computed: {}
}
