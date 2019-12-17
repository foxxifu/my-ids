import pointManageService from '@/service/settings';
import fileUpload from '@/components/fileUpload/index.vue'
import deviceService from '@/service/device'

import signal from '@/service/signal';

export default {
  components: {
    fileUpload
  },
  data () {
    return {
      myFileName: '', // 上传的文件名称
      myFileType: ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'application/vnd.ms-excel', 'text/csv'],
      parentWidth: 0,
      // table的max高度
      tableContentHeight: 120,
      // 每一个表格宽度的百分比
      celWidthPreArr: [0.2, 0.2, 0.2, 0.2, 0.2],
      popCelWidthPreArr: [0.3, 0.2, 0.15, 0.15, 0.1, 0.1],
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      popCelRealWidthArr: [],
      multipleSelection: [],
      popMultipleSelection: [],
      singleTable: {
        selection: [],
        maxHeight: 0,
      },
      popTable: {
        selection: [],
        maxHeight: 0,
        popList: [], // 当前页的数据
      },
      currentRow: null, // 当前选中的数据
      PopCurrentRow: null, // 当前选中的数据
      list: [], // 当前页的数据
      searchData: {
        index: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      popSearchData: {
        index: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      // 总记录数
      total: 0,
      popTotal: 0,
      popEnterModel: {},
      dialogVisible: false, // 对话框是否可见
      signalTypes: [
        {
          label: this.$t('poiMan.allSelect'),
          value: "",
        },
        {
          label: this.$t('poiMan.telemeter'), // 遥测
          value: "1",
        },
        {
          label: this.$t('poiMan.poiCom'), // 单点遥信
          value: "2",
        },
        {
          label: this.$t('poiMan.twoCom'), // 双点遥信
          value: "3",
        },
        {
          label: this.$t('poiMan.sinPoi'), // 单点遥控
          value: "4",
        },
        {
          label: this.$t('poiMan.twoPoi'), // 双点遥控
          value: "5",
        },
        {
          label: this.$t('poiMan.relPum'), // 遥脉
          value: "6",
        },
        {
          label: this.$t('poiMan.telAdj'), // 遥调
          value: "7",
        },
        {
          label: this.$t('poiMan.event'), // 事件
          value: "8",
        },
        {
          label: this.$t('poiMan.alarm'), // 告警
          value: "9",
        },
      ],
      toAlarmDialog: {
        isShow: false,
        selectRow: "",
        alarmSettingDialog: {
          isShow: false,
        }
      },
      toShakeInfoDialog: {
        isShow: false,
        alarmSettingDialog: {
          isShow: false,
          pointName: "",
          alarmType: "",
          alarmLevel: "",
        }
      },
      toAlarmForm: {
        devType: "",
        pointName: "",
        modelVersion: "",
      },
      toShakeInfoForm: {
        devType: "",
        pointName: "",
        modelVersion: "",
      },
      devTypes: [
      ],
      alarmTypes: [
        {
          label: this.$t('poiMan.defSig'), // 变位信号
          value: "1",
        },
        {
          label: this.$t('poiMan.abnAlarm'), // 异常告警
          value: "2",
        },
        {
          label: this.$t('poiMan.proEvn'), // 保护事件
          value: "3",
        },
        {
          label: this.$t('poiMan.comSta'), // 通信状态
          value: "4",
        },
        {
          label: this.$t('poiMan.information'), // 告知信息
          value: "5",
        },
      ],
      alarmLevels: [
        {
          label: this.$t('poiMan.alarmLevelArr')[0], // 严重
          value: "1",
        },
        {
          label: this.$t('poiMan.alarmLevelArr')[1], // 一般
          value: "2",
        },
        {
          label: this.$t('poiMan.alarmLevelArr')[2], // 提示
          value: "3",
        },
      ],
      elementSize: "",
      toAlarmTable: {
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        params: {},
        selection: [],
        loading: true,
        maxHeight: 0,
        list: [],
      },
      pointParamSettingTable: {
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        params: {},
        selection: [],
        maxHeight: 0,
        loading: false,
        list: [],
      },
      toShakeInfoTable: {
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 1,
        index: 1,
        params: {},
        selection: [],
        loading: true,
        maxHeight: 0,
        list: [
        ],
      },
      pointParamSettingForm: {
        devType: null,
        modelVersion: "",
      },
      modelVersions: [],
      importUrl: '/biz/signal/importSignalVersion?lang=' + (localStorage.getItem('lang') || 'zh') // 导入的url
    }
  },
  filters: {
  },
  created() {
    this.pointParamSettingTable.maxHeight = window.innerHeight - 240;
    this.toAlarmTable.maxHeight = window.innerHeight - 450;
    this.toShakeInfoTable.maxHeight = window.innerHeight - 450;
    this.singleTable.maxHeight = window.innerHeight - 270;
    this.popTable.maxHeight = window.innerHeight - 450;
  },
  mounted () {
    let self = this;
    this.getPointManageDatas(); // 查询分页数据
    this.$nextTick(function () {
      self.setDevTypes();
    });
    this.recalWidths();
    window.addEventListener("resize", this.resize);
  },
  methods: {
    resize() {
      this.recalWidths();
      this.pointParamSettingTable.maxHeight = window.innerHeight - 240;
      this.toAlarmTable.maxHeight = window.innerHeight - 450;
      this.toShakeInfoTable.maxHeight = window.innerHeight - 450;
      this.singleTable.maxHeight = window.innerHeight - 270;
      this.popTable.maxHeight = window.innerHeight - 450;
    },
    // 当上传点表成功之后的回调函数
    onUploadSuccess() {
      // 1.重新查询列表
      this.getPointManageDatas(false)
      // 2.通知后台去处理数据，因为不知道当前的协议是什么，就在所有的点表改变，都通知mqqt相关信息的更新
      deviceService.clearMqqtToDbCache().then(resp => {
        if (resp.code === 1) {
          console.log(this.$t('poiMan.clearMQTTSuc'))
        } else {
          console.log(this.$t('poiMan.clearMQTTFail'))
        }
      })
    },
    // 获取点表的分页数据
    getPointManageDatas (isToFirstPage) {
      let self = this;
      if (isToFirstPage) { // 是否查询第一页
        self.searchData.index = 1
      }
      pointManageService.getPointManageByCondition(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
        self.singleTable.selection = [] // 默认没有选中的记录
      })
    },
    getDevSignalInfo (isToFirstPage) {
      let self = this;
      if (isToFirstPage) { // 是否查询第一页
        self.popSearchData.index = 1;
      }
      pointManageService.getDevSignalInfo(self.popSearchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.popTable.popList = datas.list || [];
        self.popTotal = datas.count || 0;
        self.popCurrentRow = null // 默认没有选中的记录
      })
    },
    setInputText (file) { // 当验证通过的时候设置输入框的内容
      this.myFileName = file.name
    },
    // 关闭对话框
    closeDialog () {
      this.dialogVisible = false;
    },
    // 删除点表
    delPointManage () {
      if (!this.singleTable.selection || !this.singleTable.selection.length) {
        this.$alert(this.$t('poiMan.selectDel'), this.$t('poiMan.tip'));
        return;
      }
      this.$confirm(this.$t('poiMan.sureDel'), this.$t('poiMan.tip'), {
        confirmButtonText: this.$t('poiMan.sure'),
        cancelButtonText: this.$t('poiMan.cancel'),
        type: 'warning'
      }).then(() => {
        let ids = '';
        let tnVersinIds = []
        let selectRows = this.singleTable.selection
        for (let i = 0; i < selectRows.length; i++) {
          ids += selectRows[i].id + ',';
          if (selectRows[i].devTypeId == 37) {
            tnVersinIds.push(selectRows[i].id)
          }
        }
        ids = ids.substr(0, ids.length - 1);
        // 1.通知修改连接状态
        new Promise(function (resolve, reject) {
          if (tnVersinIds.length > 0) { // 如果删除了铁牛数采
            // type 1：新增 2：删除 3：修改
            // delType 1: 传递的ids代表设备  其他的代表是版本信息
            deviceService.getTnDevIdByVersionId({ids: tnVersinIds.join(',')}).then(resp => {
              // 先修改对应的关系在删除对应的点表
              resolve(resp.results)
            })
          } else {
            resolve()
          }
        }).then((tnDevIds) => {
          pointManageService.delPointManage({'ids': ids}).then(resp => {
            if (resp.code === 1) {
              this.$message({
                type: 'success',
                message: this.$t('poiMan.delSuc')
              });
              this.getPointManageDatas(false);
              if (tnDevIds) {
                deviceService.notifyTnDevUpdateChange({ids: tnDevIds, type: 2}).then(resp => {
                  console.log('send change success')
                })
              } else { // 一定不是铁牛的数采，所以在非铁牛数采的时候去清除mqtt的缓存
                // 2.通知后台去处理数据，因为不知道当前的协议是什么，就在所有的点表改变，都通知mqqt相关信息的更新
                deviceService.clearMqqtToDbCache().then(resp => {
                  if (resp.code === 1) {
                    console.log(this.$t('poiMan.clearMQTTSuc'))
                  } else {
                    console.log(this.$t('poiMan.clearMQTTFail'))
                  }
                })
              }
            } else {
              this.$message({
                type: 'error',
                message: this.$t('poiMan.delFail')
              });
            }
          });
          this.singleTable.selection = [];
        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('poiMan.cancelDel')
        });
      });
    },
    // 显示详情
    showDetail (row, event, isToFirstPage) {
      this.$refs.popTableForm && this.$refs.popTableForm.resetFields();
      event && event.stopPropagation();
      this.popSearchData.versionId = row.id;
      this.getDevSignalInfo(isToFirstPage);
      this.dialogVisible = true;
    },
    handleSelectionChange(val) {
      this.singleTable.selection = val;
    },
    toAlarmTableSelect(selection) {
      this.toAlarmTable.selection = selection;
    },
    toShakeInfoTableSelect(selection) {
      this.toShakeInfoTable.selection = selection;
    },
    popHandleSelectionChange(val) {
      this.popTable.selection = val;
    },
    // 获取设备类型的过滤器
    getDevTypeName (value) {
      if (value === '2') { // 数采
        return this.$t('poiMan.numCol');
      }
      if (value === '13') { // 通管机
        return this.$t('poiMan.pipMac');
      }
      return value;
    },
    // 获取点表类型的过滤器
    getSigTypeType (value) {
      for (var i = 0; i < this.signalTypes.length; i++) {
        if (parseInt(this.signalTypes[i].value) == value) {
          return this.signalTypes[i].label;
        }
      }
      return this.$t('poiMan.noSigFound') + value;
    },
    getDateTime (value) {
      return Date.parse(value).format(this.$t('dateFormat.yyyymmddhhmmss'));
    },
    pageSizeChange (val) {
      this.searchData.pageSize = val;
      this.getPointManageDatas(true)
    },
    pageChange (val) {
      this.searchData.index = val;
      this.getPointManageDatas()
    },
    popPageSizeChange (val) {
      this.popSearchData.pageSize = val;
      this.getDevSignalInfo(true)
    },
    popPageChange (val) {
      this.popSearchData.index = val;
      this.getDevSignalInfo()
    },
    recalWidths () {
      let self = this;
      this.parentWidth = 0;
      setTimeout(function () {
        self.parentWidth = self.$el.offsetWidth - 60 - (self.list.length > 10 ? 18 : 0)
      }, 1);
    },
    // 计算表格的最大高度
    calcTableMaxHeight () {
      let height = window.innerHeight;
      let relHeight = height - 300;
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight
    },
    modifySigGainAndOffset () {
      this.$refs.popTableForm.validate((valid) => {
        if (valid) {
          pointManageService.updatePointManage({ list: JSON.stringify(this.popTable.popList), modelVersionId: this.popSearchData.versionId }).then(resp => {
            if (resp.code === 1) {
              this.$message.success(this.$t('poiMan.saveSuc'));
              this.getDevSignalInfo(false);
            } else {
              this.$message.error(this.$t('poiMan.saveFail'));
            }
          });
        }
      })
    },
    popTableCellClass({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 2 || columnIndex === 3) {
        return "inputClass"
      }
    },
    shakeInfoConvertToAlarm() {
      if (this.singleTable.selection && this.singleTable.selection.length === 1) {
        this.toAlarmDialog.isShow = true;
        this.getToAlarmTableData(this.getToAlarmParam());
      } else {
        this.$alert(this.$t('poiMan.selectRec'), this.$t('poiMan.tip'), {
          confirmButtonText: this.$t('poiMan.sure'),
        });
      }
    },
    alarmConvertToShakeInfo() {
      if (this.singleTable.selection && this.singleTable.selection.length === 1) {
        this.toShakeInfoDialog.isShow = true;
        this.getToShakeInfoTableData(this.getToShakeInfoParam());
      } else {
        this.$alert(this.$t('poiMan.selectRec'), this.$t('poiMan.tip'), {
          confirmButtonText: this.$t('poiMan.sure'),
        });
      }
    },
    searchToAlarm() {
      this.getToAlarmTableData(this.getToAlarmParam());
    },
    searchToShakeInfo() {
      this.getToShakeInfoTableData(this.getToShakeInfoParam());
    },
    toAlarm() {
      if (this.toAlarmTable.selection && this.toAlarmTable.selection.length > 0) {
        this.toAlarmDialog.alarmSettingDialog.isShow = true;
      } else {
        this.$alert(this.$t('poiMan.leastSelRec'), this.$t('poiMan.tip'), {
          confirmButtonText: this.$t('poiMan.sure'),
        });
      }
    },
    toShakeInfo() {
      if (this.toShakeInfoTable.selection && this.toShakeInfoTable.selection.length > 0) {
        this.toShakeInfoDialog.alarmSettingDialog.isShow = true;
      } else {
        this.$alert(this.$t('poiMan.leastSelRec'), this.$t('poiMan.tip'), {
          confirmButtonText: this.$t('poiMan.sure'),
        });
      }
    },
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
    // 通知后台数据获取后的告警的信号点的告警信息配置改变了
    resetMasterServiceSignals (versionId, devTypeId) {
      if (devTypeId === 13 || devTypeId === 37) { // 通管机,获取铁牛数采
        deviceService.resetMasterServiceSignals(versionId).then(resp => {
          if (resp.code === 1) {
            console.log('success')
          } else {
            console.log(`failed::devid = ${versionId} , faild message = ${resp.message}`)
          }
        })
      }
    },
    confirmAlarmSettig() {
      this.setShakeInfoToAlarm(this.getShakeInfoToAlarmParam(), () => {
        this.toAlarmDialog.alarmSettingDialog.isShow = false;
        let devTypeId = this.singleTable.selection[0].devTypeId
        let versionId = this.singleTable.selection[0].id
        this.resetMasterServiceSignals(versionId, devTypeId)
      });
    },
    confirmToShakeInfo() {
      this.setAlarmToShakeInfo(this.getAlarmToShakeInfoParam(), () => {
        this.toShakeInfoDialog.alarmSettingDialog.isShow = false;
        let devTypeId = this.singleTable.selection[0].devTypeId
        let versionId = this.singleTable.selection[0].id
        this.resetMasterServiceSignals(versionId, devTypeId)
      });
    },
    savePointParamSetting() {
      this.$confirm(this.$t('poiMan.sureSave'), this.$t('poiMan.tip'), {
        confirmButtonText: this.$t('poiMan.sure'),
        cancelButtonText: this.$t('poiMan.cancel'),
        type: 'warning'
      }).then(() => {
        this.normalizedAdapter(this.getNormalizedAdapterParam());
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('poiMan.cancelSave')
        });
      });
    },
    resetPointParamSetting() {
      this.$confirm(this.$t('poiMan.sureRest'), this.$t('poiMan.tip'), {
        confirmButtonText: this.$t('poiMan.sure'),
        cancelButtonText: this.$t('poiMan.cancel'),
        type: 'warning'
      }).then(() => {
        this.clearNormalizedAdapter();
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('poiMan.cancelRest')
        });
      });
    },
    getToAlarmParam(){
      let devTypeId, signalName, signalVersion, index, pageSize, signalVersionId, signalType;
      devTypeId = this.toAlarmForm.devType;
      signalName = this.toAlarmForm.pointName;
      signalVersion = this.toAlarmForm.modelVersion;
      index = this.toAlarmTable.index;
      pageSize = this.toAlarmTable.pageSize;
      signalVersionId = this.singleTable.selection[0].id;
      signalType = 2; // 单点遥信
      return {devTypeId, signalName, signalVersion, index, pageSize, signalVersionId, signalType};
    },
    getToShakeInfoParam(){
      let devTypeId, signalName, signalVersion, index, pageSize, signalVersionId, signalType;
      devTypeId = this.toShakeInfoForm.devType;
      signalName = this.toShakeInfoForm.pointName;
      signalVersion = this.toShakeInfoForm.modelVersion;
      index = this.toShakeInfoTable.index;
      pageSize = this.toShakeInfoTable.pageSize;
      signalVersionId = this.singleTable.selection[0].id;
      signalType = 9; // 单点遥信
      return {devTypeId, signalName, signalVersion, index, pageSize, signalVersionId, signalType};
    },
    getToAlarmTableData(params, callback) {
      this.toAlarmTable.loading = true;
      signal.getSignalModel(params).then(resp => {
        if (resp.code === 1) {
          this.toAlarmTable.list = resp.results.list;
          this.toAlarmTable.count = resp.results.count;
          callback && callback();
        }
        this.toAlarmTable.loading = false;
      });
    },
    getToShakeInfoTableData(params, callback) {
      this.toShakeInfoTable.loading = true;
      signal.getSignalModel(params).then(resp => {
        if (resp.code === 1) {
          this.toShakeInfoTable.list = resp.results.list;
          this.toShakeInfoTable.count = resp.results.count;
          callback && callback();
        }
        this.toShakeInfoTable.loading = true;
      });
    },
    getShakeInfoToAlarmParam() {
      let alarmLevel, modelIds = "", signalVersionId, teleType;
      alarmLevel = this.toAlarmDialog.alarmSettingDialog.alarmLevel;
      this.toAlarmTable.selection.forEach(e => {
        modelIds += e.modelId + ",";
      });
      if (modelIds){
        modelIds = modelIds.substring(0, modelIds.length - 1);
      }
      signalVersionId = this.singleTable.selection[0].id;
      teleType = this.toAlarmDialog.alarmSettingDialog.alarmType;
      let result = {alarmLevel, modelIds, signalVersionId, teleType};
      return result;
    },
    setShakeInfoToAlarm(params, callback){
      signal.shakeInfoToAlarm(params).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: this.$t('poiMan.sucOpr')
          });
          this.toAlarmTable.selection = [];
          this.getToAlarmTableData(this.getToAlarmParam());
          callback && callback();
        }
      });
    },
    getAlarmToShakeInfoParam() {
      let modelIds = "", signalVersionId;
      this.toShakeInfoTable.selection.forEach(e => {
        modelIds += e.modelId + ",";
      });
      if (modelIds){
        modelIds = modelIds.substring(0, modelIds.length - 1);
      }
      signalVersionId = this.singleTable.selection[0].id;
      let result = {modelIds, signalVersionId};
      return result;
    },
    setAlarmToShakeInfo(params, callback) {
      signal.alarmToShakeInfo(params).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: this.$t('poiMan.sucOpr')
          });
          this.toShakeInfoTable.selection = [];
          this.getToShakeInfoTableData(this.getToShakeInfoParam());
          callback && callback();
        }
      });
    },
    setDevTypes() {
      signal.getDevTypes().then(resp => {
        if (resp.code === 1) {
          this.devTypes = resp.results
          // this.devTypes = [{
          //   label: "请选择",
          //   value: "",
          // }];
          // for (let i in resp.results) {
          //   this.devTypes.push({
          //     label: i,
          //     value: resp.results[i]
          //   })
          // }
        }
      });
    },
    setPointParamSettingTable(params, callback) {
      this.pointParamSettingTable.loading = true;
      signal.queryNormalizedByDevType(params).then(resp => {
        if (resp.code === 1) {
          this.pointParamSettingTable.list = [];
          resp.results.unificationModelList.forEach(e => {
            e.mappingParam = "";
            e.mappingParams = [];
            this.pointParamSettingTable.list.push(e);
          });
          this.pointParamSettingForm.modelVersion = "";
          this.modelVersions = [
            {
              label: this.$t('poiMan.choose'),
              value: "",
            }
          ];
          for (let i in resp.results.modelVersionMaps) {
            this.modelVersions.push({
              label: i,
              value: i,
            })
          }
          callback && callback();
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
        this.pointParamSettingTable.loading = false;
      }).catch(() => {
        this.$message({
          type: 'error',
          message: this.$t('poiMan.serExc')
        });
        this.pointParamSettingTable.loading = false;
      })
    },
    setMapingParams(params) {
      signal.getNormalizedInfo(params).then(resp => {
        if (resp.code === 1) {
          let mvcs = resp.results.mvc;
          let check = resp.results.check;
          let mappingParams = [
            {
              label: this.$t('poiMan.choose'),
              value: ""
            }
          ];
          for (let i in mvcs){
            mappingParams.push({
              label: mvcs[i],
              value: i
            })
          }
          this.pointParamSettingTable.list.forEach(e => {
            e.mappingParams = mappingParams;
            let isFind = false;
            for (let i = 0; i < check.length; i++){
              if (check[i].normalizedSignalId == e.id){
                e.mappingParam = check[i].siganlModelId + "";
                isFind = true;
                break;
              }
            }
            if (!isFind) {
              e.mappingParam = "";
            }
          });
        }
      });
    },
    pointParamSettingDevTypeChange(val) {
      this.setPointParamSettingTable({deviceTypeId: val})
    },
    pointParamSettingModelVersionChange(val) {
      this.setMapingParams({modelVersionCode: val})
    },
    getNormalizedAdapterParam() {
      let result = [];
      this.pointParamSettingTable.list.forEach(e => {
        if (e.mappingParam){
          result.push({
            unificationSignalId: e.id + "",
            signalModelId: e.mappingParam,
            signalUnit: "",
          })
        }
      })
      return result;
    },
    normalizedAdapter(params) {
      let modelVersionCode = this.pointParamSettingForm.modelVersion;
      signal.normalizedAdapter(params, modelVersionCode).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: this.$t('poiMan.sucOpr')
          });
          // 通知归一化配置的缓存清空，下一次取用的时候就会去重新查询数据库,即修改在缓存中计时生效
          deviceService.clearUnificationByModelVersion({signalVersion: modelVersionCode}).then(resp => {})
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
      });
    },
    clearNormalizedAdapter() {
      let modelVersionCode = this.pointParamSettingForm.modelVersion;
      signal.clearNormalizedAdapter({modelVersionCode}).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: this.$t('poiMan.sucOpr')
          });
          // 通知归一化配置的缓存清空，下一次取用的时候就会去重新查询数据库,即修改在缓存中计时生效
          deviceService.clearUnificationByModelVersion({signalVersion: modelVersionCode}).then(resp => {})
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
      });
    },
    formatterAlarmLevel(row, column, cellValue, index) {
      for (let i = 0; i < this.alarmLevels.length; i++){
        let e = this.alarmLevels[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    formatterAlarmType(row, column, cellValue, index) {
      for (let i = 0; i < this.alarmTypes.length; i++){
        let e = this.alarmTypes[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    rowClick (row, event, column, type) {
      let id = "id";
      if (type === "singleTable") {
        id = "id";
      } else if (type === "toAlarmTable") {
        id = "modelId";
      } else if (type === "toShakeInfoTable") {
        id = "modelId";
      }
      let isFind = false;
      for (let i = 0; i < this[type].selection.length; i++){
        let e = this[type].selection[i];
        if (e[id] === row[id]) {
          isFind = true;
          this[type].selection.splice(i, 1);
          break;
        }
      }
      if (!isFind) {
        this[type].selection.push(row);
      }
      this.$refs[type].toggleRowSelection(row, !isFind);
    },
    exportPoint () { // 点表导出
      let rows = this.singleTable.selection
      let len = rows.length
      if (len === 0 || len > 1) {
        this.$message(len === 0 ? this.$t('poiMan.poiExport') : this.$t('poiMan.onlyPointExport') + len)
        return;
      }
      let self = this
      self.$confirm(this.$t('poiMan.currentPointExport')).then(() => {
        let iframe = document.getElementById("myIframe_for_download_id");
        let lang = localStorage.getItem('lang') || 'zh'
        let tmpUrl = '/biz/signal/exportSignal' + '?id=' + rows[0].id + '&lang=' + lang
        if (iframe) {
          iframe.src = tmpUrl;
        } else {
          iframe = document.createElement("iframe");
          iframe.style.display = "none";
          iframe.src = tmpUrl;
          iframe.id = "myIframe_for_download_id";
          document.body.appendChild(iframe);
        }
      })
    }
  },
  watch: {},
  computed: {
    resizeWidth () {
      let parentWidth = this.parentWidth;
      let celWidthPreArr = this.celWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth;
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth
    },
    resizeDialogWidth () {
      let parentWidth = this.parentWidth * 0.8;
      let celWidthPreArr = this.popCelWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.popCelRealWidthArr[i] = celWidthPreArr[i] * parentWidth;
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth
    }
  },
  destroyed() {
    window.removeEventListener("resize", this.resize);
  }
}
