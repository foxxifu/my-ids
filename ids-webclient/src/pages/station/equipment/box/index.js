import station from '@/service/singleStation';
import localStorage from '@/utils/localStorage';
import DevAlarm from '@/pages/station/alarm/devAlarm/index.vue';

export default {
  components: {
    DevAlarm
  },
  data(){
    return {
      inputData: [],
      outputData: [],
      devStatusTableData: [],
      devInfoTableData: [],
      elementSize: "",
      isShowRepairRecommendation: false,
      repairRecommendationForm: {},
      repairRecommendationTableData: [],
      devStatusTable: {
        name: "",
        model: "",
        inverterCapacity: "",
      },
      alarmManageForm: {
        alarmName: "",
        beginTime: "",
        endTime: "",
        alarmStatus: "",
        alarmLevel: "",
      },
      devAlarmTableData: [],
      task: null, // 定时任务
      inputOutputParam: null, // 定时任务查询参数
    }
  },
  props: ['transferedData'],
  mounted: function(){
    let params = {id: this.transferedData.devId};
    this.getDevDetail(params, _ => {
      this.getSingalData(params);
    });
    this.$nextTick(_ => {
      this.initTask();
    })
  },
  methods: {
    returnBack() {
      this.$emit("listenChildEvent");
    },

    getDevDetail(param, callback) {
      station.getDevDetail(param).then(resp => {
        if (resp.code === 1) {
          this.setDevInfo(resp.results);
        }
        callback && callback();
      });
    },

    getSingalData(param) {
      param.signalKeys = [];

      param.signalKeys.push('ab_u');
      param.signalKeys.push('bc_u');
      param.signalKeys.push('ca_u');
      param.signalKeys.push('a_u');
      param.signalKeys.push('b_u');
      param.signalKeys.push('c_u');
      param.signalKeys.push('a_i');
      param.signalKeys.push('b_i');
      param.signalKeys.push('c_i');
      param.signalKeys.push('active_power');
      param.signalKeys.push('power_factor');
      param.signalKeys.push('reactive_power');
      param.signalKeys.push('elec_freq');
      if (param.id) {
        this.inputOutputParam = param;
      } else {
        this.inputOutputParam = null;
      }

      station.getSingalData(param).then(resp => {
        if (resp.code === 1) {
          this.setDevStatus(resp.results);
        }
      })
    },
    initTask() {
      let finish = true;
      this.task = setInterval(_ => {
        if (this.inputOutputParam && finish && this.$refs.devStatusTableData && this.$refs.devStatusTableData.$el.offsetWidth) {
          finish = false;
          station.getSingalData(this.inputOutputParam).then(resp => {
            finish = true;
            if (resp.code === 1) {
              this.setDevStatus(resp.results);
            }
          })
        }
      }, 5 * 1000);
    },
    clearTask() {
      this.task && clearInterval(this.task)
    },

    setDevInfo(data) {
      this.devStatusTable.name = data.devAlias;
      this.devStatusTable.model = data.signalVersion;
      this.$data.devInfoTableData = [
        {
          kpiName1: this.$t("station.equipment.devName"), // "设备名称",
          kpiValue1: data.devAlias,
          kpiName2: this.$t("station.equipment.devFactory"), // "生产厂家",
          kpiValue2: data.venderName,
          kpiName3: this.$t("station.equipment.devSignal"), // "设备型号",
          kpiValue3: data.signalVersion,
        },
        {
          kpiName1: this.$t("station.equipment.devIP"), // "IP地址",
          kpiValue1: data.devIp,
          kpiName2: this.$t("station.equipment.devSN"), // "SN号",
          kpiValue2: data.snCode,
          kpiName3: this.$t("station.equipment.devRunStatus"), // "运行状态",
          kpiValue3: this.transferedData.devRunningStatus[data.devStatus],
        },
        {
          kpiName1: this.$t("station.equipment.devAddr"), // "安装地址",
          kpiValue1: data.devAddr,
          kpiName2: this.$t("station.equipment.devBelongStation"), // "所属电站",
          kpiValue2: data.stationName,
          kpiName3: "",
          kpiValue3: "",
        }
      ]
    },

    devStatusCellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },

    devInfoCellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },

    arraySpanMethod({row, column, rowIndex, columnIndex}) {
      if (rowIndex === 0 || rowIndex === 4 || rowIndex === 5 || rowIndex === 6) {
        if (columnIndex === 1) {
          return [1, 3];
        }
      }
    },

    viewRow(index, row) {
      this.$data.isShowRepairRecommendation = true;
      this.setRepairRecommendation(row);
    },

    repairRecommendationCellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },

    setStationAlarmData(sId) {
      this.$data.devManageTableData = this.$data.testData[sId].inverterData;
    },
    setStationFilter(sId) {
      this.$data.filter = this.$data.testData[sId].filter
    },

    setDevStatusTableData(inverterName) {
      let self = this;
      station.getDevRunningBySIdAndTime({inverterName: inverterName, sId: self.$data.stationCode}).then(resp => {
        this.setDevStatus(resp.results);
        this.setDevInfo(resp.results);
        this.setDevAlarm(resp.results.alarms);
      });
    },

    setDevStatus(data) {
      this.$data.devStatusTableData = [
        {
          kpiName1: this.$t('station.equipment.box.devStatusInfo')[0], // "A相电压(V)",
          kpiValue1: data['a_u'],
          kpiName2: this.$t('station.equipment.box.devStatusInfo')[1], // "电网A相电流(A)",
          kpiValue2: data['a_i'],
          kpiName3: this.$t('station.equipment.box.devStatusInfo')[2], // "电网AB线电压(V)",
          kpiValue3: data['ab_u']
        },
        {
          kpiName1: this.$t('station.equipment.box.devStatusInfo')[3], // "B相电压(V)",
          kpiValue1: data['b_u'],
          kpiName2: this.$t('station.equipment.box.devStatusInfo')[4], // "电网B相电流(A)",
          kpiValue2: data['b_i'],
          kpiName3: this.$t('station.equipment.box.devStatusInfo')[5], // "电网BC线电压(V)",
          kpiValue3: data['bc_u']
        },
        {
          kpiName1: this.$t('station.equipment.box.devStatusInfo')[6], // "C相电压(V)",
          kpiValue1: data['c_u'],
          kpiName2: this.$t('station.equipment.box.devStatusInfo')[7], // "电网C相电流(A)",
          kpiValue2: data['c_i'],
          kpiName3: this.$t('station.equipment.box.devStatusInfo')[8], // "电网CA线电压(V)",
          kpiValue3: data['ca_u']
        },
        {
          kpiName1: this.$t('station.equipment.box.devStatusInfo')[9], // "有功功率(kW)",
          kpiValue1: data['active_power'],
          kpiName2: this.$t('station.equipment.box.devStatusInfo')[10], // "无功功率(kVar)",
          kpiValue2: data['reactive_power'],
          kpiName3: this.$t('station.equipment.box.devStatusInfo')[11], // "功率因数",
          kpiValue3: data['power_factor']
        },
        {
          kpiName1: this.$t('station.equipment.box.devStatusInfo')[12], // "电网频率(Hz)",
          kpiValue1: data['elec_freq'],
          kpiName2: "",
          kpiValue2: "",
          kpiName3: "",
          kpiValue3: ""
        }
      ]
    },

    setDevAlarm(data) {
      this.$data.devAlarmTableData = data
    },

    setRepairRecommendation(data) {
      this.$data.repairRecommendationTableData = [
        {
          kpiName1: this.$t('station.equipment.devName'), // 设备名称
          kpiValue1: data.devName,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: this.$t('station.alarmName'), // 告警名称
          kpiValue1: data.alarmName,
          kpiName2: this.$t('station.alarmLevel'), // 告警级别
          kpiValue2: data.alarmLevel,
        },
        {
          kpiName1: this.$t('station.firstAlarmTime'), // 告警第一次发生时间
          kpiValue1: data.beginTime,
          kpiName2: this.$t('station.lastAlarmTime'), // 告警最后一次发生时间
          kpiValue2: data.endTime,
        },
        {
          kpiName1: this.$t('station.alarmStatus'), // 告警状态
          kpiValue1: data.alarmStatus,
          kpiName2: this.$t('station.devSn'), // 设备SN号
          kpiValue2: data.devSN,
        },
        {
          kpiName1: this.$t('station.devInsAdd'), // 设备安装地址
          kpiValue1: data.installedAddress,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: this.$t('station.alarmCause'), // 告警原因
          kpiValue1: data.alarmCause,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: this.$t('station.suggession'), // 修复建议
          kpiValue1: data.repairRecommendations,
          kpiName2: "",
          kpiValue2: "",
        },
      ]
    }

  },
  computed: {  },
  destroyed () {
    this.clearTask();
  }
}
