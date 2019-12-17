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

      param.signalKeys.push('a_u');
      param.signalKeys.push('b_u');
      param.signalKeys.push('c_u');
      param.signalKeys.push('a_i');
      param.signalKeys.push('b_i');
      param.signalKeys.push('c_i');

      param.signalKeys.push('active_power');
      param.signalKeys.push('power_factor');
      param.signalKeys.push('active_capacity');
      param.signalKeys.push('reactive_power');
      param.signalKeys.push('reverse_active_cap');
      param.signalKeys.push('forward_reactive_cap');
      param.signalKeys.push('reverse_reactive_cap');
      param.signalKeys.push('active_power_a');
      param.signalKeys.push('active_power_b');
      param.signalKeys.push('active_power_c');
      param.signalKeys.push('reactive_power_a');
      param.signalKeys.push('reactive_power_b');
      param.signalKeys.push('reactive_power_c');
      param.signalKeys.push('total_apparent_power');
      param.signalKeys.push('grid_frequency');
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
          kpiName1: this.$t('station.equipment.devName'), // "设备名称",
          kpiValue1: data.devAlias,
          kpiName2: this.$t('station.equipment.devFactory'), // "生产厂家",
          kpiValue2: data.venderName,
          kpiName3: this.$t('station.equipment.devSignal'), // "设备型号",
          kpiValue3: data.signalVersion,
        },
        {
          kpiName1: this.$t('station.equipment.devIP'), // "IP地址",
          kpiValue1: data.devIp,
          kpiName2: this.$t('station.equipment.devSN'), // "SN号",
          kpiValue2: data.snCode,
          kpiName3: this.$t('station.equipment.devRunStatus'), // "运行状态",
          kpiValue3: this.transferedData.devRunningStatus[data.devStatus],
        },
        {
          kpiName1: this.$t('station.equipment.devAddr'), // "安装地址",
          kpiValue1: data.devAddr,
          kpiName2: this.$t('station.equipment.devBelongStation'), // "所属电站",
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
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[0], // "A相电压(V)",
          kpiValue1: data['a_u'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[1], // "A相电流(A)",
          kpiValue2: data['a_i'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[2], // "正向有功电度(kWh)",
          kpiValue3: data['active_capacity'],
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[3], // "B相电压(V)",
          kpiValue1: data['b_u'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[4], // "B相电流(A)",
          kpiValue2: data['b_i'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[5], // "反向有功电度(kWh)",
          kpiValue3: data['reverse_active_cap']
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[6], // "C相电压(V)",
          kpiValue1: data['c_u'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[7], // "C相电流(A)",
          kpiValue2: data['c_i'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[8], // "总视在功率(kW)",
          kpiValue3: data['total_apparent_power']
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[9], // "电网频率(Hz)",
          kpiValue1: data['grid_frequency'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[10], // "功率因数",
          kpiValue2: data['power_factor'],
          kpiName3: "",
          kpiValue3: ""
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[11], // "有功功率(kW)",
          kpiValue1: data['active_power'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[12], // "无功功率(kVar)",
          kpiValue2: data['reactive_power'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[13], // "功率因数",
          kpiValue3: data['power_factor']
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[14], // "有功功率PA(kW)",
          kpiValue1: data['active_power_a'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[15], // "有功功率PB(kW)",
          kpiValue2: data['active_power_b'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[16], // "有功功率PC(kW)",
          kpiValue3: data['active_power_c']
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[17], // "无功功率QA(kVar)",
          kpiValue1: data['reactive_power_a'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[18], // "无功功率QB(kVar)",
          kpiValue2: data['reactive_power_b'],
          kpiName3: this.$t('station.equipment.meter.devStatusInfo')[19], // "无功功率QC(kVar)",
          kpiValue3: data['reactive_power_c']
        },
        {
          kpiName1: this.$t('station.equipment.meter.devStatusInfo')[20], // "正向无功电度(kVarh)",
          kpiValue1: data['forward_reactive_cap'],
          kpiName2: this.$t('station.equipment.meter.devStatusInfo')[21], // "反向无功电度(kVarh)",
          kpiValue2: data['reverse_reactive_cap'],
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
