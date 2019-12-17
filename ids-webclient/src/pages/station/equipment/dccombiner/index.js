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
      capacityOfStringTableData: [],
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
      moduleTypes: [
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[0], // "多晶",
          value: "1",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[1], // "单晶",
          value: "2",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[2], // "多晶",
          value: "3",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[3], // "N型单晶",
          value: "4",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[4], // "PERC单晶(单晶PERC)",
          value: "5",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[5], // "多晶双玻",
          value: "6",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[6], // "单晶四栅60片",
          value: "7",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[7], // "单晶四栅60片",
          value: "8",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[8], // "多晶四栅60片",
          value: "9",
        },
        {
          label: this.$t('station.equipment.dccombiner.moduleTypes')[9], // "多晶四栅72片",
          value: "10",
        },
      ],
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
          this.setCapacityOfString(resp.results.pvInfo);
        }
        callback && callback();
      });
    },

    getSingalData(param) {
      /*let pvLength = this.capacityOfStringTableData ? this.capacityOfStringTableData.length : 0;
      param.signalKeys = [];
      for (let i = 1; i <= pvLength; i++) {
        param.signalKeys.push('dc_i' + i);
      }

      param.signalKeys.push('photc_u');
      param.signalKeys.push('photc_i');
      param.signalKeys.push('temperature');
      if (param.id) {
        this.inputOutputParam = param;
      } else {
        this.inputOutputParam = null;
      }

      station.getSingalData(param).then(resp => {
        this.parseInputOutputResult(resp);
      })*/
      param.signalKeys = [];
      param.signalKeys.push('dc_i1');
      param.signalKeys.push('dc_i2');
      param.signalKeys.push('dc_i3');
      param.signalKeys.push('dc_i4');
      param.signalKeys.push('dc_i5');
      param.signalKeys.push('dc_i6');
      param.signalKeys.push('dc_i7');
      param.signalKeys.push('dc_i8');
      param.signalKeys.push('dc_i9');
      param.signalKeys.push('dc_i10');
      param.signalKeys.push('dc_i11');
      param.signalKeys.push('dc_i12');
      param.signalKeys.push('dc_i13');
      param.signalKeys.push('dc_i14');
      param.signalKeys.push('dc_i15');
      param.signalKeys.push('dc_i16');
      if (param.devId) {
        this.inputOutputParam = param;
      } else {
        this.inputOutputParam = null;
      }
      station.getSingalData(param).then(resp => {
        this.parseInputOutputResult(resp);
      });
    },
    parseInputOutputResult(resp) {
      if (resp.code === 1) {
        /* this.inputData = [];
          this.outputData = [];
          let outputName = [];
          let outputU = [];
          let outputI = [];
          let outputSpan = 14;
          outputName.push({text: "&nbsp;", span: 10});
          outputU.push({text: "输出直流电压(V)", span: 10});
          outputU.push({text: resp.results['photc_u'] || '-', span: outputSpan});
          outputI.push({text: "输出直流电流(A)", span: 10});
          outputI.push({text: resp.results['photc_i'] || '-', span: outputSpan});
          this.outputData.push(outputName);
          this.outputData.push(outputU);
          this.outputData.push(outputI);
        */
        this.setDevStatus(resp.results);
        // this.devStatusTableData = resp.results;
      }
    },
    initTask() {
      let finish = true;
      this.task = setInterval(_ => {
        if (this.inputOutputParam && finish && this.$refs.devStatusTableData && this.$refs.devStatusTableData.$el.offsetWidth) {
          finish = false;
          station.getSingalData(this.inputOutputParam).then(resp => {
            finish = true;
            this.parseInputOutputResult(resp.results);
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
      if (data.pvInfo.length) {
        this.devStatusTable.inverterCapacity = 0;
        data.pvInfo.forEach(e => {
          this.devStatusTable.inverterCapacity += e.pvCapacity;
        });
        this.devStatusTable.inverterCapacity = this.devStatusTable.inverterCapacity / 1000;
      }
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
        this.setCapacityOfString(resp.results.pvInfos);
        this.setDevAlarm(resp.results.alarms);
        self.$data.inputData = resp.results.inputData;
        self.$data.outputData = resp.results.outputData;
      });
    },

    setDevStatus(data) {
      this.devStatusTableData = data;
      /* this.$data.devStatusTableData = [
        {
          kpiName1: "当日发电量(kwh)",
          kpiValue1: data['day_capacity'],
          kpiName2: "逆变器名称",
          kpiValue2: this.devStatusTable.name,
        },
        {
          kpiName1: "累计发电量(kwh)",
          kpiValue1: data['total_capacity'],
          kpiName2: "逆变器型号",
          kpiValue2: this.devStatusTable.model,
        },
        {
          kpiName1: "有功功率(kw)",
          kpiValue1: data['active_power'],
          kpiName2: "功率因素",
          kpiValue2: data['power_factor'],
        },
        {
          kpiName1: "无功功率(kw)",
          kpiValue1: data['reactive_power'],
          kpiName2: "今日逆变器开机时间",
          kpiValue2: data['open_time'] && new Date(data['open_time']).format("yyyy-MM-dd hh:mm:ss"),
        },
        {
          kpiName1: "输入总功率(kw)",
          kpiValue1: data['mppt_power'],
          kpiName2: "今日逆变器关机时间",
          kpiValue2: data['off_time'] && new Date(data['off_time']).format("yyyy-MM-dd hh:mm:ss"),
        },
        {
          kpiName1: "转换效率(%)",
          kpiValue1: data['efficiency'],
          kpiName2: "机内温度(℃)",
          kpiValue2: data['temperature'],
        },
        {
          kpiName1: "逆变器装机容量(kw)",
          kpiValue1: this.devStatusTable.inverterCapacity,
          kpiName2: "",
          kpiValue2: "",
        },
      ] */
    },

    formatterModuleType(row, column, cellValue, index) {
      for (let i = 0; i < this.moduleTypes.length; i++){
        let e = this.moduleTypes[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },

    setCapacityOfString(data) {
      this.$data.capacityOfStringTableData = data;
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
