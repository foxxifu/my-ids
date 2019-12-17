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
      dcCombinerTableData: [],
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
          label: this.$t('station.equipment.concinverter.stringTypes')[0], // "多晶",
          value: "1",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[1], // "单晶",
          value: "2",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[2], // "多晶",
          value: "3",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[3], // "N型单晶",
          value: "4",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[4], // "PERC单晶(单晶PERC)",
          value: "5",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[5], // "多晶双玻",
          value: "6",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[6], // "单晶四栅60片",
          value: "7",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[7], // "单晶四栅60片",
          value: "8",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[8], // "多晶四栅60片",
          value: "9",
        },
        {
          label: this.$t('station.equipment.concinverter.stringTypes')[9], // "多晶四栅72片",
          value: "10",
        },
      ],
      task: null, // 定时任务
      inputOutputParam: null, // 定时任务查询参数
    }
  },
  props: ['transferedData'],
  mounted: function(){
    let params = {id: this.transferedData.devId, devTypeId: this.transferedData.devTypeId};
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
          this.dcCombinerTableData = resp.results && resp.results.childDevices || [];
        }
        callback && callback();
      });
    },

    getSingalData(param) {
      param.signalKeys = [];

      param.signalKeys.push('center_u');
      param.signalKeys.push('center_i');

      param.signalKeys.push('a_u');
      param.signalKeys.push('b_u');
      param.signalKeys.push('c_u');
      param.signalKeys.push('a_i');
      param.signalKeys.push('b_i');
      param.signalKeys.push('c_i');

      param.signalKeys.push('day_capacity');
      param.signalKeys.push('total_capacity');
      param.signalKeys.push('grid_frequency');
      param.signalKeys.push('active_power');
      param.signalKeys.push('power_factor');
      param.signalKeys.push('reactive_power');
      param.signalKeys.push('open_time');
      param.signalKeys.push('mppt_power');
      param.signalKeys.push('off_time');
      param.signalKeys.push('efficiency');
      param.signalKeys.push('temperature');
      if (param.id) {
        this.inputOutputParam = param;
      } else {
        this.inputOutputParam = null;
      }

      station.getSingalData(param).then(resp => {
        this.parseInputOutputResult(resp);
      })
    },
    parseInputOutputResult(resp) {
      if (resp.code === 1) {
        this.inputData = [];
        let name = [];
        let inputU = [];
        let inputI = [];
        let span = 2;
        name.push({text: "&nbsp;", span: 12});
        inputU.push({text: this.$t('station.equipment.concinverter.inputU'), span: 12});
        inputI.push({text: this.$t('station.equipment.concinverter.inputI'), span: 12});
        inputU.push({text: resp.results['center_u'] || '-', span: 12});
        inputI.push({text: resp.results['center_i'] || '-', span: 12});
        this.inputData.push(name);
        this.inputData.push(inputU);
        this.inputData.push(inputI);
        this.outputData = [];
        let outputName = [];
        let outputU = [];
        let outputI = [];
        let outputSpan = 4;
        outputName.push({text: "&nbsp;", span: 10});
        outputName.push({text: "A", span: outputSpan});
        outputName.push({text: "B", span: outputSpan});
        outputName.push({text: "C", span: outputSpan});
        outputU.push({text: this.$t('station.equipment.concinverter.outputU'), span: 10});
        outputU.push({text: resp.results['a_u'] || '-', span: outputSpan});
        outputU.push({text: resp.results['b_u'] || '-', span: outputSpan});
        outputU.push({text: resp.results['c_u'] || '-', span: outputSpan});
        outputI.push({text: this.$t('station.equipment.concinverter.outputI'), span: 10});
        outputI.push({text: resp.results['a_i'] || '-', span: outputSpan});
        outputI.push({text: resp.results['b_i'] || '-', span: outputSpan});
        outputI.push({text: resp.results['c_i'] || '-', span: outputSpan});
        this.outputData.push(outputName);
        this.outputData.push(outputU);
        this.outputData.push(outputI);

        this.setDevStatus(resp.results);
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
        this.setDevAlarm(resp.results.alarms);
        self.$data.inputData = resp.results.inputData;
        self.$data.outputData = resp.results.outputData;
      });
    },

    setDevStatus(data) {
      this.$data.devStatusTableData = [
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[0], // "当日发电量(kwh)",
          kpiValue1: data['day_capacity'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[1], // "逆变器名称",
          kpiValue2: this.devStatusTable.name
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[2], // "累计发电量(kwh)",
          kpiValue1: data['total_capacity'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[3], // "逆变器型号",
          kpiValue2: this.devStatusTable.model
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[4], // "直流输入电压(V)",
          kpiValue1: data['center_u'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[5], // "功率因数",
          kpiValue2: data['power_factor']
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[6], // "直流输入电流(A)",
          kpiValue1: data['center_i'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[7], // "并网频率",
          kpiValue2: data['grid_frequency']
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[8], // "直流输入功率",
          kpiValue1: data['mppt_power'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[9], // "交流输出功率",
          kpiValue2: data['active_power']
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[10], // "转换效率(%)",
          kpiValue1: data['efficiency'],
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[11], // "机内温度(℃)",
          kpiValue2: data['temperature'],
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[12], // "逆变器装机容量(kw)",
          kpiValue1: this.devStatusTable.inverterCapacity,
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[13], // "输出无功功率(Var)",
          kpiValue2: data['reactive_power']
        },
        {
          kpiName1: this.$t('station.equipment.concinverter.devStatusInfo')[14], // "今日逆变器开机时间",
          kpiValue1: data['open_time'] && new Date(data['open_time']).format(this.$t('dateFormat.yyyymmddhhmmss')),
          kpiName2: this.$t('station.equipment.concinverter.devStatusInfo')[15], // "今日逆变器关机时间",
          kpiValue2: data['off_time'] && (new Date(data['off_time']).format(this.$t('dateFormat.yyyymmddhhmmss')))
        }
      ]
    },

    formatterModuleType(row, column, cellValue, index) {
      for (let i = 0; i < this.moduleTypes.length; i++){
        let e = this.moduleTypes[i];
        if (e.value === cellValue) {
          return e.label;
        }
      }
      return cellValue;
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
