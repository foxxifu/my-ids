import station from '@/service/singleStation';
import device from '@/service/device';
import DevAlarm from '@/pages/station/alarm/devAlarm/index.vue';

export default {
  components: {
    DevAlarm
  },
  data(){
    return {
      devTypeCode: '',
      inputData: [],
      outputData: [],
      devStatusTableData: [],
      devInfoTableData: [],
      capacityOfStringTableData: [],
      elementSize: "",
      isShowRepairRecommendation: false,
      repairRecommendationForm: {},
      repairRecommendationTableData: [],
      devCommand: {
        devId: '',
        orderCommand: '',
      },
      hisOrder: '',
      sentable: '',
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
          label: this.$t('station.equipment.strinverter.moduleTypes')[0], // "多晶",
          value: "1",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[1], // "单晶",
          value: "2",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[2], // "多晶",
          value: "3",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[3], // "N型单晶",
          value: "4",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[4], // "PERC单晶(单晶PERC)",
          value: "5",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[5], // "多晶双玻",
          value: "6",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[6], // "单晶四栅60片",
          value: "7",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[7], // "单晶四栅60片",
          value: "8",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[8], // "多晶四栅60片",
          value: "9",
        },
        {
          label: this.$t('station.equipment.strinverter.moduleTypes')[9], // "多晶四栅72片",
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
    if (2 !== this.transferedData.devStatus){
      this.sentable = false;
    }
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
      let pvLength = this.capacityOfStringTableData ? this.capacityOfStringTableData.length : 0;
      param.signalKeys = [];
      for (let i = 1; i <= pvLength; i++) {
        param.signalKeys.push('pv' + i + '_u');
        param.signalKeys.push('pv' + i + '_i');
      }
      param.signalKeys.push('a_u');
      param.signalKeys.push('b_u');
      param.signalKeys.push('c_u');
      param.signalKeys.push('a_i');
      param.signalKeys.push('b_i');
      param.signalKeys.push('c_i');

      param.signalKeys.push('day_capacity');
      param.signalKeys.push('total_capacity');
      param.signalKeys.push('active_power');
      param.signalKeys.push('power_factor');
      param.signalKeys.push('reactive_power');
      param.signalKeys.push('open_time');
      param.signalKeys.push('mppt_power');
      param.signalKeys.push('off_time');
      param.signalKeys.push('efficiency');
      param.signalKeys.push('temperature');
      param.signalKeys.push('dev_serial_code');
      param.signalKeys.push('dev_type_code');
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
      let pvLength = this.capacityOfStringTableData ? this.capacityOfStringTableData.length : 0;
      if (resp.code === 1) {
        this.inputData = [];
        let name = [];
        let inputU = [];
        let inputI = [];
        let span = 1;
        name.push({text: "&nbsp;", span: 3});
        inputU.push({text: this.$t('station.equipment.strinverter.inputU'), span: 3});
        inputI.push({text: this.$t('station.equipment.strinverter.inputI'), span: 3});
        if (pvLength) {
          // span = Math.floor(span);
          if (pvLength < 10) {
            span = 2
          }
          for (let i = 1; i <= pvLength; i++) {
            name.push({text: "PV" + i, span: span});
            inputU.push({text: resp.results['pv' + i + '_u'] || '-', span: span});
            inputI.push({text: resp.results['pv' + i + '_i'] || '-', span: span});
          }
        } else {
          name.push({text: "PV1", span: 12});
          inputU.push({text: "-", span: 12});
          inputI.push({text: "-", span: 12});
        }
        this.inputData.push(name);
        this.inputData.push(inputU);
        this.inputData.push(inputI);
        this.outputData = [];
        let outputName = [];
        let outputU = [];
        let outputI = [];
        let outputSpan = 5;
        outputName.push({text: "&nbsp;", span: 11});
        outputName.push({text: "A", span: outputSpan});
        outputName.push({text: "B", span: outputSpan - 1});
        outputName.push({text: "C", span: outputSpan - 1});
        outputU.push({text: this.$t('station.equipment.strinverter.outputU'), span: 11}); // 输出交流电压(V)
        outputU.push({text: resp.results['a_u'] || '-', span: outputSpan});
        outputU.push({text: resp.results['b_u'] || '-', span: outputSpan - 1});
        outputU.push({text: resp.results['c_u'] || '-', span: outputSpan - 1});
        outputI.push({text: this.$t('station.equipment.strinverter.outputI'), span: 11}); // "输出交流电流(A)"
        outputI.push({text: resp.results['a_i'] || '-', span: outputSpan});
        outputI.push({text: resp.results['b_i'] || '-', span: outputSpan - 1});
        outputI.push({text: resp.results['c_i'] || '-', span: outputSpan - 1});
        this.outputData.push(outputName);
        this.outputData.push(outputU);
        this.outputData.push(outputI);
        this.devTypeCode = resp.results['dev_type_code'];
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
            this.parseInputOutputResult(resp);
          })
        }
      }, 5 * 1000);
    },
    clearTask() {
      this.task && clearInterval(this.task)
    },

    setDevInfo(data) {
      this.devCommand.devId = data.id;
      this.devStatusTable.name = data.devAlias;
      this.devStatusTable.model = data.signalVersion;
      if (data.pvInfo.length) {
        this.devStatusTable.inverterCapacity = 0;
        data.pvInfo.forEach(e => {
          this.devStatusTable.inverterCapacity += e.pvCapacity;
        });
        this.devStatusTable.inverterCapacity = this.round(this.devStatusTable.inverterCapacity);
      }
      this.$data.devInfoTableData = [
        {
          kpiName1: this.$t('station.equipment.devName'), // "设备名称",
          kpiValue1: data.devAlias,
          kpiName2: this.$t('station.equipment.devFactory'), // "生产厂家",
          kpiValue2: data.venderName,
          kpiName3: this.$t('station.equipment.devSignal'), // "设备型号",
          kpiValue3: this.devTypeCode,
        },
        {
          kpiName1: this.$t('station.equipment.devIP'), // "IP地址",
          kpiValue1: data.devIp,
          // kpiName2: this.$t('station.equipment.devSN'), // "SN号",
          // kpiValue2: data.snCode,
          kpiName2: this.$t('station.equipment.devBelongStation'), // "所属电站",
          kpiValue2: data.stationName,
          kpiName3: this.$t('station.equipment.devRunStatus'), // "运行状态",
          kpiValue3: this.transferedData.devRunningStatus[data.devStatus],
        },
        {
          kpiName1: this.$t('station.equipment.devAddr'), // "安装地址",
          kpiValue1: data.devAddr,
          // kpiName2: this.$t('station.equipment.devBelongStation'), // "所属电站",
          // kpiValue2: data.stationName,
          kpiName2: "",
          kpiValue2: "",
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
      this.$data.devStatusTableData = [
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[0], // "当日发电量(kwh)",
          kpiValue1: data['day_capacity'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[1], // "逆变器名称",
          kpiValue2: this.devStatusTable.name,
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[2], // "累计发电量(kwh)",
          kpiValue1: data['total_capacity'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[3], // "逆变器型号",
          kpiValue2: data['dev_type_code'],
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[4], // "有功功率(kw)",
          kpiValue1: data['active_power'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[5], // "功率因数",
          kpiValue2: data['power_factor'],
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[6], // "无功功率(kw)",
          kpiValue1: data['reactive_power'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[7], // "今日逆变器开机时间",
          kpiValue2: data['open_time'] && new Date(data['open_time']).format(this.$t('dateFormat.yyyymmddhhmmss')),
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[8], // "输入总功率(kw)",
          kpiValue1: data['mppt_power'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[9], // "今日逆变器关机时间",
          kpiValue2: data['off_time'] && new Date(data['off_time']).format(this.$t('dateFormat.yyyymmddhhmmss')),
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[10], // "转换效率(%)",
          kpiValue1: data['efficiency'],
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[11], // "机内温度(℃)",
          kpiValue2: data['temperature'],
        },
        {
          kpiName1: this.$t('station.equipment.strinverter.devStatusInfo')[12], // "逆变器装机容量(kw)",
          kpiValue1: this.devStatusTable.inverterCapacity,
          kpiName2: this.$t('station.equipment.strinverter.devStatusInfo')[13], // "产品序列号(SN)"
          kpiValue2: data['dev_serial_code'],
        },
      ]
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
    sendDevCmd(){
      let self = this;
      device.sendDevCmd(self.devCommand).then(resp => {
        if (resp.code === 1){
          self.$message(this.$t('station.equipment.strinverter.sendCmdSuccess'));
        } else {
          self.$message(this.$t('station.equipment.strinverter.sendCmdFail'));
        }
        self.hisOrder = new Date().format(this.$t('dateFormat.yyyymmddhhmmss')) + "   Order: " + self.devCommand.orderCommand + "  Reply: " + resp.results + '\n' + self.hisOrder
      })
    },
    setRepairRecommendation(data) {
      this.$data.repairRecommendationTableData = [
        {
          kpiName1: "设备名称",
          kpiValue1: data.devName,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "告警名称",
          kpiValue1: data.alarmName,
          kpiName2: "告警级别",
          kpiValue2: data.alarmLevel,
        },
        {
          kpiName1: "告警第一次发生时间",
          kpiValue1: data.beginTime,
          kpiName2: "告警最后一次发生时间",
          kpiValue2: data.endTime,
        },
        {
          kpiName1: "告警状态",
          kpiValue1: data.alarmStatus,
          kpiName2: "设备SN号",
          kpiValue2: data.devSN,
        },
        {
          kpiName1: "设备安装地址",
          kpiValue1: data.installedAddress,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "告警原因",
          kpiValue1: data.alarmCause,
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "修复建议",
          kpiValue1: data.repairRecommendations,
          kpiName2: "",
          kpiValue2: "",
        },
      ]
    },
    round(num) { // 保留三位小数
      if (isFinite(num)) {
        return Math.round(num * 1000) / 1000;
      } else {
        return num;
      }
    },
    tbClickHandle (param) {
      if (param.index === '1') {
        var devInfoTableData = this.devInfoTableData;
        devInfoTableData[0].kpiValue3 = this.devTypeCode
      }
    }

  },
  computed: {
    capLength () { // 总的长度
      return this.capacityOfStringTableData ? this.capacityOfStringTableData.length : 0;
    }
  },
  destroyed () {
    this.clearTask();
  }
}
