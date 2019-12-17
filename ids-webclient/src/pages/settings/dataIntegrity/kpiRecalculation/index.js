import kpiRecalcService from '@/service/kpiRecalculation'
import StationInput from '@/components/chooseStation/index.vue'
import {cookie} from 'cookie_js'

export default {
  components: {
    StationInput
  },
  data () {
    return {
      vm: this,
      exeProcess: 0,
      isNew: true,
      isShowDialog: false,
      searchParam: {
        taskStatus: -1,
        taskName: null,
        stationName: null,
      },
      calcTaskData: {
        index: 1,
        pageSize: 10,
        total: 0,
        list: []
      },
      calcTaskModel: {},
      formValidRules: {
        taskName: [
          {required: true, message: this.$t('kpiRec.taskName_req'), trigger: 'blur'}
        ],
        stationCode: [
          {required: true, message: this.$t('kpiRec.stationName_req'), trigger: 'blue'}
        ],
        taskTimeDuration: [
          {required: true, message: this.$t('kpiRec.taskTime_req'), trigger: 'blue'}
        ]
      },
      status: [
        {
          label: this.$t('kpiRec.allSelect'),
          value: -1
        },
        {
          label: this.$t('kpiRec.taskStatus_undo'),
          value: 1
        },
        {
          label: this.$t('kpiRec.taskStatus_doing'),
          value: 2
        },
        {
          label: this.$t('kpiRec.taskStatus_done'),
          value: 3
        },
        {
          label: this.$t('kpiRec.taskStatus_error'),
          value: 4
        }
      ]
    }
  },
  filters: {
    // -1:全部，1：未执行 2：正在执行 3：已完成 4：执行失败
    taskStatusFilter (val, vm) {
      if (val === 1) {
        return vm.$t('kpiRec.taskStatus_undo');
      } else if (val === 2){
        return vm.$t('kpiRec.taskStatus_doing');
      } else if (val === 3){
        return vm.$t('kpiRec.taskStatus_done');
      } else if (val === 4){
        return vm.$t('kpiRec.taskStatus_error');
      }
    },
    // 展示时间格式化
    taskTimeFilter (val, type = 'yyyy-MM-dd HH:mm:ss') {
      if (val){
        return new Date(val).format(type)
      } else {
        return ''
      }
    }
  },
  mounted: function () {
    let self = this
    this.searchCalcTask() // 查询分页数据
    this.$nextTick(function () {
    });

    if ("WebSocket" in window) {
      // 初始化websocket
      kpiRecalcService.getWebsocketUrl().then(resp => {
        if (resp.code === 1 && resp.results) {
          let wsUrl = resp.results;
          let tokenId = cookie.get('tokenId');
          wsUrl += '?tokenId=' + tokenId;
          let websocket = new WebSocket(wsUrl);

          if (websocket) {
            // 发生错误
            websocket.onerror = function(error){
              console.error(error);
            }

            // 关闭连接
            websocket.onclose = function(){
              console.info("websocket connection closed.");
            }

            websocket.onmessage = function(message){
              if (message && message.data){
                let receiveData = JSON.parse(message.data);
                if (receiveData && receiveData.data) {
                  self.calcTaskData.list.forEach((task) => {
                    if (task.id == receiveData.data.taskId) {
                      task.taskStatus = receiveData.data.taskStatus;
                      self.formatTaskProcess();
                    }
                  });
                }
              }
            }
          }
        }
      });
    } else {
      console.warn("the browser can't support WebSocket.")
    }
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },
    /* 前端触发任务查询 */
    queryCalcTask: function(){
      this.searchCalcTask();
    },
    /* 从服务端获取请求数据 */
    searchCalcTask: function (noCondition) {
      let self = this;
      if (noCondition){
        this.searchParam = {};
        this.searchParam.index = 1;
      }
      kpiRecalcService.getCalcTask(this.searchParam).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.calcTaskData.list = datas.list || []
        self.calcTaskData.total = datas.count || 0
        self.currentRow = null // 默认没有选中的记录

        self.formatTaskProcess();
      });
    },
    /* 任务进度状态处理 */
    formatTaskProcess: function(){
      if (!this.calcTaskData.list) {
        return false;
      }
      this.calcTaskData.list.forEach((task) => {
        switch (task.taskStatus) {
          case 1:
            task.percentage = 0;
            break;
          case 2:
            task.percentage = 40;
            break;
          case 3:
            task.percentage = 100;
            task.procStatus = 'success';
            break;
          case 4:
            task.percentage = 60;
            task.procStatus = 'exception';
            break;
          default:
        }
      });
    },
    /* 新增计算任务 */
    createCalcTask: function () {
      this.isNew = true;
      this.showDialog();
    },
    /* 删除未执行任务 */
    removeCalcTask: function (row, event) {
      let self = this;
      if (row.taskStatus !== 1) {
        console.log("only none exectued task can be removed.")
        return false;
      }
      this.$confirm(this.$t('kpiRec.confirm_delte')).then(() => {
        kpiRecalcService.removeCalcTask({id: row.id}).then(resp => {
          if (resp.code === 1){
            this.$message(this.$t('kpiRec.delete_success'));
            self.searchCalcTask();
          } else {
            this.$message(this.$t('kpiRec.delete_fail'))
          }
        });
      });
    },
    /* 修改任务 */
    modifyCalcTask: function (choosedRow) {
      this.isNew = false;
      this.calcTaskModel.taskName = choosedRow.taskName;
      this.calcTaskModel.stationName = choosedRow.stationName;
      this.calcTaskModel.stationCode = choosedRow.stationCode;
      this.calcTaskModel.taskTimeDuration = [new Date(choosedRow.startTime), new Date(choosedRow.endTime)];
      this.calcTaskModel.id = choosedRow.id;
      this.showDialog();
    },
    /* 执行任务 */
    executeCalcTask: function (row) {
      kpiRecalcService.executeCalcTask({id: row.id}).then(resp => {
        if (resp.code === 1){
          console.log("kpi calculation task start success.");
          this.searchCalcTask();
        }
      });
    },
    /* 分页信息改变 */
    pageSizeChange: function (val) {
      this.searchParam.pageSize = val;
      this.searchCalcTask();
    },
    pageChange: function (val) {
      this.searchParam.index = val;
      this.searchCalcTask();
    },
    /* 显示弹框 */
    showDialog: function () {
      this.isShowDialog = true
    },
    /* 关闭弹出框 */
    closeDialog: function () {
      this.calcTaskModel = {};
      this.isShowDialog = false;
      this.$refs.kpiRecalcForm.resetFields();
    },
    /* 确认提交 */
    submitForm: function () {
      let self = this;
      this.$refs.kpiRecalcForm.validate((valid) => {
        if (valid){
          // 新增任务 or 修改任务
          if (!this.calcTaskModel.startTime){
            this.calcTaskModel.startTime = this.calcTaskModel.taskTimeDuration[0];
            this.calcTaskModel.startTime = this.calcTaskModel.startTime.getTime();
          }
          if (!this.calcTaskModel.endTime){
            this.calcTaskModel.endTime = this.calcTaskModel.taskTimeDuration[1];
            this.calcTaskModel.endTime = this.calcTaskModel.endTime.getTime();
          }

          let fnName = (this.isNew && 'createCalcTask') || 'modifyCalcTask';
          kpiRecalcService[fnName](this.calcTaskModel).then(resp => {
            if (resp.code == 1){
              self.closeDialog();
              self.searchCalcTask(this.isNew);
              self.$message(this.$t('kpiRec.submit_sucess'));
            }else {
              self.$message(this.$t('kpiRec.submit_fail'));
            }
          });
        }else{
          console.log("params is not correct.");
          return false;
        }
      });
    },
    stationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.$set(this.calcTaskModel, 'stationCode', vals)
      this.$set(this.calcTaskModel, 'stationName', texts)
    }
  },
  computed: {
    /* 动态获取弹出框标题 */
    getDialogTitle: function () {
      if (this.isNew) {
        return this.$t('kpiRec.add') + this.$t('kpiRec.task')
      } else {
        return this.$t('kpiRec.edit') + this.$t('kpiRec.task')
      }
    }
  }
}
