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

      param.signalKeys.push('temperature');
      param.signalKeys.push('pv_temperature');
      param.signalKeys.push('wind_speed');
      param.signalKeys.push('wind_direction');
      param.signalKeys.push('total_irradiation');
      param.signalKeys.push('irradiation_intensity');
      param.signalKeys.push('horiz_irradiation_intensity');
      param.signalKeys.push('horiz_total_irradiation');
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
        // self.$data.inputData = resp.results.inputData;
        // self.$data.outputData = resp.results.outputData;
      });
    },

    setDevStatus(data) {
      this.$data.devStatusTableData = [
        {
          kpiName1: this.$t('station.equipment.emi.devStatusInfo')[0], // "辐照强度(W/㎡)",
          kpiValue1: data['irradiation_intensity'],
          kpiName2: this.$t('station.equipment.emi.devStatusInfo')[1], // "总辐射量(WJ/㎡)",
          kpiValue2: data['total_irradiation']
        },
        {
          kpiName1: this.$t('station.equipment.emi.devStatusInfo')[2], // "水平辐照强度(W/㎡)",
          kpiValue1: data['horiz_irradiation_intensity'],
          kpiName2: this.$t('station.equipment.emi.devStatusInfo')[3], // "水平辐照量(WJ/㎡)",
          kpiValue2: data['horiz_total_irradiation']
        },
        {
          kpiName1: this.$t('station.equipment.emi.devStatusInfo')[4], // "风速(m/s)",
          kpiValue1: data['wind_speed'],
          kpiName2: this.$t('station.equipment.emi.devStatusInfo')[5], // "风向",
          kpiValue2: data['wind_direction']
        },
        {
          kpiName1: this.$t('station.equipment.emi.devStatusInfo')[6], // "温度(°)",
          kpiValue1: data['temperature'],
          kpiName2: this.$t('station.equipment.emi.devStatusInfo')[7], // "PV温度",
          kpiValue2: data['pv_temperature']
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
