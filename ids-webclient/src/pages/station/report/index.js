import echarts from 'echarts'
import reportManage from "@/service/reportManage";
import localStorage from '@/utils/localStorage';
import inverterDetail from '@/pages/station/report/inverterDetail/index.vue'

export default {
  components: {
    inverterDetail,
  },
  data() {
    return {
      isShowInverterDetail: false,
      stationTableName: this.$t('station.report.station.name'),
      inverterTableName: this.$t('station.report.inverter.name'),
      subarrayTableName: this.$t('station.report.subarray.name'),
      elementSize: "",
      reportTabName: "stationRunning",
      stationRunningChart: null,
      inverterChart: null,
      subarrayChart: null,
      stationReportSearchForm: {
        stationCodes: "",
        timeType: "1",
        dayTime: "",
        monthTime: "",
        yearTime: "",
      },
      inverterReportSearchForm: {
        stationCodes: "",
        timeType: "1",
        dayTime: "",
        monthTime: "",
        yearTime: "",
        devType: "",
      },
      subarrayReportSearchForm: {
        stationCode: "",
        timeType: "1",
        dayTime: "",
        monthTime: "",
        yearTime: "",
        devType: "",
      },
      // 设备类型
      devTypes: [
        {
          label: this.$t('station.report.devTypes')[0],
          value: "1", // 组串式逆变器
        },
        {
          label: this.$t('station.report.devTypes')[1],
          value: "10", // 环境监测仪
        },
        {
          label: this.$t('station.report.devTypes')[2],
          value: "8", // 箱变
        },
        {
          label: this.$t('station.report.devTypes')[3],
          value: "13", // 通管机
        },
        {
          label: this.$t('station.report.devTypes')[4],
          value: "17", // 电表
        },
        {
          label: this.$t('station.report.devTypes')[5],
          value: "14", // 集中式逆变器
        },
        {
          label: this.$t('station.report.devTypes')[6],
          value: "15", // 直流汇流箱
        },
      ],
      // 设备类型
      timeTypes: [
        {
          label: this.$t("station.report.day"),
          value: "1", // 日
        },
        {
          label: this.$t("station.report.month"),
          value: "2", // 月
        },
        {
          label: this.$t("station.report.year"),
          value: "3", // 年
        },
      ],
      stationRunningEchartData: {
        stations: [],
        electricityGeneration: [],
        income: [],
      },
      inverterRunningEchartData: {
        inverters: [],
        electricityGeneration: []
      },
      subarrayRunningEchartData: {
        inverters: [],
        electricityGeneration: []
      },
      inverterReport: {
        isDayTime: true,
        timeType: "day",
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        exportParams: {},
        maxHeight: 0,
        list: [],
      },
      stationReport: {
        timeType: "day",
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        exportParams: {},
        maxHeight: 0,
        message: null,
        isDayAndSingleStation: false,
        list: [],
      },
      subarrayReport: {
        isDayTime: true,
        timeType: "day",
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        exportParams: {},
        maxHeight: 0,
        list: [],
      },
      inverterDetailDevId: "",
      inverterDetailDayTime: "",
    }
  },
  created() {
    let today = new Date();
    let yesterday = new Date(new Date(today.getTime() - 24 * 3600 * 1000).format(this.$t('dateFormat.yyyymmdd')) + " 00:00:00").getTime();
    let month = today.format('yyyy-MM') + "-01 00:00:00"; // 时间的格式化,这里写死，因为是不参与限定的，主要是给后面创建的使用，就使用固定的格式
    let year = today.format(this.$t('dateFormat.yyyy')) + "-01-01 00:00:00";
    this.$data.stationReportSearchForm.dayTime = yesterday;
    this.$data.stationReportSearchForm.monthTime = new Date(month).getTime();
    this.$data.stationReportSearchForm.yearTime = new Date(year).getTime();

    this.$data.inverterReportSearchForm.dayTime = yesterday;
    this.$data.inverterReportSearchForm.monthTime = new Date(month).getTime();
    this.$data.inverterReportSearchForm.yearTime = new Date(year).getTime();

    this.$data.subarrayReportSearchForm.dayTime = yesterday;
    this.$data.subarrayReportSearchForm.monthTime = new Date(month).getTime();
    this.$data.subarrayReportSearchForm.yearTime = new Date(year).getTime();

    this.$data.stationReport.exportParams.stationCodes = "";
    this.$data.stationReport.exportParams.time = "day";
    this.$data.stationReport.exportParams.timeDim = yesterday;

    this.$data.inverterReport.exportParams.stationCodes = "";
    this.$data.inverterReport.exportParams.time = "day";
    this.$data.inverterReport.exportParams.timeDim = yesterday;

    this.$data.subarrayReport.exportParams.stationCodes = "";
    this.$data.subarrayReport.exportParams.time = "day";
    this.$data.subarrayReport.exportParams.timeDim = yesterday;

    this.stationReport.maxHeight = window.innerHeight - 515;
    this.inverterReport.maxHeight = window.innerHeight - 515;
    this.subarrayReport.maxHeight = window.innerHeight - 515;
  },
  mounted() {
    // dom更新完毕后执行的回调
    this.$nextTick(_ => {
      let code = localStorage.getStore('stationParams').stationCode;
      this.stationReportSearchForm.stationCodes = code;
      this.inverterReportSearchForm.stationCodes = code;
      this.subarrayReportSearchForm.stationCodes = code;
      this.setStationRunningData(this.getStationSearchParams(), _ => this.initStationRunningChart());
      this.setInverterRunningData(this.getInverterSearchParams(), _ => this.initInverterChart());
      this.setSubarrayRunningData(this.getSubarraySearchParams(true), _ => this.initSubarrayChart());
    });
    window.addEventListener("resize", this.resize);
  },
  methods: {
    showInverterDetail(param) {
      this.inverterDetailDevId = param.deviceId;
      this.inverterDetailDayTime = this.inverterReportSearchForm.dayTime;
      this.isShowInverterDetail = true;
    },
    resize() {
      this.stationRunningChart.resize();
      this.inverterChart.resize();
      this.stationReport.maxHeight = window.innerHeight - 515;
      this.inverterReport.maxHeight = window.innerHeight - 515;
      this.subarrayReport.maxHeight = window.innerHeight - 515;
    },
    stationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.stationReportSearchForm.stationCodes = vals;
      this.stationReportSearchForm.stationName = texts;
    },
    inverterStationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.inverterReportSearchForm.stationCodes = vals;
      this.inverterReportSearchForm.stationName = texts;
    },
    // 电站运行报表
    initStationRunningChart() {
      let self = this;
      let stationRunningChart = this.$refs.stationRunningChart;
      if (!self.$data.stationRunningChart) {
        self.$data.stationRunningChart = echarts.init(stationRunningChart);
      }
      let option = {
        noDataLoadingOption: {
          text: this.$t("station.report.noData"),
          effect: 'bubble',
          effectOption: {
            effect: {
              n: 0 // 气泡个数为0
            }
          }
        },
        legend: {
          data: [this.$t('station.report.station.powerCap'), this.$t('station.report.station.income')], // 发电量(kWh)，收益(元)
          textStyle: {
            fontSize: 14,
            color: '#000',
            fontWeight: 500
          },
          selectedMode: false
        },
        color: ['#1EB05B', '#0F95EF'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            lineStyle: {
              color: '#0D96F0'
            }
          },
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 20,
          top: '15%',
          containLabel: true,
          // borderColor: '#EEEEEE'
          borderColor: '#ee0800'
        },
        xAxis: [
          {
            type: 'category',
            data: self.$data.stationRunningEchartData.stations,
            axisTick: {
              alignWithLabel: true
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          }
        ],
        yAxis: [
          {
            name: this.$t('station.report.station.powerCap'), // 发电量(kWh)
            type: 'value',
            nameTextStyle: {
              color: '#000'
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          },
          {
            name: this.$t('station.report.station.income'), // '收益(元)',
            type: 'value',
            nameTextStyle: {
              color: '#000'
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            },
          }
        ],
        series: [
          {
            name: this.$t('station.report.station.powerCap'), // '发电量(kWh)',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            data: self.$data.stationRunningEchartData.electricityGeneration,
            yAxisIndex: 0,
            itemStyle: {
              normal: {
                barBorderRadius: 5,
                z: 0
              }
            }
          },
          {
            name: this.$t('station.report.station.income'), // '收益(元)',
            type: 'line',
            barWidth: '30%',
            data: self.$data.stationRunningEchartData.income,
            symbol: 'circle',
            symbolSize: 8,
            yAxisIndex: 1,
          }
        ]
      }
      this.$data.stationRunningChart.setOption(option, true);
    },
    // 逆变器报表
    initInverterChart() {
      let self = this;
      let inverterChart = this.$refs.inverterChart;
      if (!self.$data.inverterChart) {
        self.$data.inverterChart = echarts.init(inverterChart);
      }
      let option = {
        noDataLoadingOption: {
          text: this.$t("station.report.noData"), // '无数据',
          effect: 'bubble',
          effectOption: {
            effect: {
              n: 0 // 气泡个数为0
            }
          }
        },
        // legend: {
        //   data: ['发电量(kwh)', '收益(万元)'],
        //   textStyle: {
        //     fontSize: 14,
        //     color: '#000',
        //     fontWeight: 500
        //   },
        //   selectedMode: false
        // },
        color: ['#1EB05B', '#0F95EF'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            lineStyle: {
              color: '#0D96F0'
            }
          },
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 20,
          top: '15%',
          containLabel: true,
          borderColor: '#EEEEEE'
        },
        xAxis: [
          {
            type: 'category',
            data: self.$data.inverterRunningEchartData.inverters,
            axisTick: {
              alignWithLabel: true
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          }
        ],
        yAxis: [
          {
            name: this.$t("station.report.inverter.powerCap"), // '发电量(kWh)',
            type: 'value',
            nameTextStyle: {
              color: '#000'
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          }
        ],
        series: [
          {
            name: this.$t("station.report.inverter.powerCap"), // '发电量(kWh)',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            data: self.$data.inverterRunningEchartData.electricityGeneration,
            yAxisIndex: 0,
            itemStyle: {
              normal: {
                barBorderRadius: 5,
                z: 0
              }
            }
          }
        ]
      }
      this.$data.inverterChart.setOption(option, true);
    },
    // 子阵报表
    initSubarrayChart() {
      let self = this;
      let subarrayChart = this.$refs.subarrayChart;
      if (!self.$data.subarrayChart) {
        self.$data.subarrayChart = echarts.init(subarrayChart);
      }
      let option = {
        noDataLoadingOption: {
          text: this.$t("station.report.noData"), //  无数据',
          effect: 'bubble',
          effectOption: {
            effect: {
              n: 0 // 气泡个数为0
            }
          }
        },
        color: ['#1EB05B', '#0F95EF'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            lineStyle: {
              color: '#0D96F0'
            }
          },
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 20,
          top: '15%',
          containLabel: true,
          borderColor: '#EEEEEE'
        },
        xAxis: [
          {
            type: 'category',
            data: self.$data.subarrayRunningEchartData.inverters,
            axisTick: {
              alignWithLabel: true
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          }
        ],
        yAxis: [
          {
            name: this.$t("station.report.subarray.ehu"), // '等效利用小时(h)',
            type: 'value',
            nameTextStyle: {
              color: '#000'
            },
            axisLine: {
              lineStyle: {
                color: '#3BABC4'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#000',
                type: 'dashed'
              }
            }
          }
        ],
        series: [
          {
            name: this.$t("station.report.subarray.ehu"), // '等效利用小时(h)',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            data: self.$data.subarrayRunningEchartData.electricityGeneration,
            yAxisIndex: 0,
            itemStyle: {
              normal: {
                barBorderRadius: 5,
                z: 0
              }
            }
          }
        ]
      }
      this.$data.subarrayChart.setOption(option, true);
    },
    // echart重画
    chartResize(chartName) {
      if (chartName === "0") {
        setTimeout(_ => this.$data.stationRunningChart.resize(), 0);
      } else if (chartName === "1") {
        setTimeout(_ => this.$data.inverterChart.resize(), 0);
      } else if (chartName === "2") {
        setTimeout(_ => this.$data.subarrayChart.resize(), 0);
      }
    },
    // tab切换
    tabClick(tab, event) {
      this.chartResize(tab.index);
    },
    setStationRunningData(params, callback) {
      let self = this;
      reportManage.getStationRunningReport(params).then(resp => {
        if (resp.code === 1) {
          self.$data.stationReport.list = resp.results.list;
          self.$data.stationReport.count = resp.results.count;
          self.$data.stationReport.message = resp.message;
          if (resp.message === '1') {
            if (!this.stationReportSearchForm.stationName) {
              if (resp.results.list.length && resp.results.list[0].stationName) {
                this.stationTableName = resp.results.list[0].stationName + this.$t("station.report.station.name") // " 电站运行报表";
              }
            } else {
              this.stationTableName = this.stationReportSearchForm.stationName + this.$t("station.report.station.name") // " 电站运行报表";
            }
          } else {
            let dateFormat = "";
            if (this.stationReport.exportParams.timeDim === "day"){
              dateFormat = this.$t('dateFormat.yyyymmdd');
            } else if (this.stationReport.exportParams.timeDim === "month"){
              dateFormat = this.$t('dateFormat.yyyymm');
            } else if (this.stationReport.exportParams.timeDim === "year"){
              dateFormat = this.$t('dateFormat.yyyy');
            }
            this.stationTableName = this.$t("station.report.station.name") + new Date(this.stationReport.exportParams.time).format(dateFormat);
          }
          self.$data.stationRunningEchartData.stations = [];
          self.$data.stationRunningEchartData.electricityGeneration = [];
          self.$data.stationRunningEchartData.income = [];
          if (resp.message === '1') {
            let dateFormat = "";
            if (this.stationReport.exportParams.timeDim === "day"){
              dateFormat = this.$t('dateFormat.yyyymmddhhmmss');
            } else if (this.stationReport.exportParams.timeDim === "month"){
              dateFormat = this.$t('dateFormat.yyyymmdd');
            } else if (this.stationReport.exportParams.timeDim === "year"){
              dateFormat = this.$t('dateFormat.yyyymm');
            }
            resp.results.list.forEach(function(e){
              self.$data.stationRunningEchartData.stations.push(new Date(e.collectTime).format(dateFormat));
              self.$data.stationRunningEchartData.electricityGeneration.push(e.productPower || 0);
              self.$data.stationRunningEchartData.income.push(e.inCome || 0);
            })
          } else {
            resp.results.list.forEach(function(e){
              self.$data.stationRunningEchartData.stations.push(e.stationName);
              self.$data.stationRunningEchartData.electricityGeneration.push(e.productPower || 0);
              self.$data.stationRunningEchartData.income.push(e.inCome || 0);
            })
          }
          if (self.$data.stationRunningEchartData.stations.length === 0) {
            self.$data.stationRunningEchartData.stations.push("-");
          }
          if (self.$data.stationRunningEchartData.electricityGeneration.length === 0) {
            self.$data.stationRunningEchartData.electricityGeneration.push(0);
          }
          if (self.$data.stationRunningEchartData.income.length === 0) {
            self.$data.stationRunningEchartData.income.push(0);
          }
        }
        callback && callback();
      });
    },
    setInverterRunningData(params, callback) {
      let self = this;
      reportManage.getInverterRunningReport(params).then(resp => {
        if (resp.code === 1) {
          self.$data.inverterReport.list = resp.results.list;
          self.$data.inverterReport.count = resp.results.count;
          let dateFormat = "";
          if (this.inverterReport.exportParams.timeDim === "day"){
            dateFormat = this.$t('dateFormat.yyyymmdd');
          } else if (this.inverterReport.exportParams.timeDim === "month"){
            dateFormat = this.$t('dateFormat.yyyymm');
          } else if (this.inverterReport.exportParams.timeDim === "year"){
            dateFormat = this.$t('dateFormat.yyyy');
          }
          this.inverterTableName = this.$t("station.report.inverter.name") + new Date(this.inverterReport.exportParams.time).format(dateFormat);
          self.$data.inverterRunningEchartData.inverters = [];
          self.$data.inverterRunningEchartData.electricityGeneration = [];
          resp.results.list.forEach(function(e){
            self.$data.inverterRunningEchartData.inverters.push(e.stationName + "_" + e.devName);
            self.$data.inverterRunningEchartData.electricityGeneration.push(e.inverterPower);
          })
        }
        callback && callback();
      });
    },
    setSubarrayRunningData(params, callback) {
      reportManage.getSubarrayRunningReport(params).then(resp => {
        if (resp.code === 1) {
          this.subarrayReport.list = resp.results.list;
          this.subarrayReport.count = resp.results.count;
          let dateFormat = "";
          if (this.subarrayReport.exportParams.timeDim === "day"){
            dateFormat = this.$t('dateFormat.yyyymmdd');
          } else if (this.subarrayReport.exportParams.timeDim === "month"){
            dateFormat = this.$t('dateFormat.yyyymm');
          } else if (this.subarrayReport.exportParams.timeDim === "year"){
            dateFormat = this.$t('dateFormat.yyyy');
          }
          this.subarrayTableName = this.$t("station.report.subarray.name") + new Date(this.subarrayReport.exportParams.time).format(dateFormat);
          this.subarrayRunningEchartData.inverters = [];
          this.subarrayRunningEchartData.electricityGeneration = [];
          if (resp.results.list.length) {
            resp.results.list.forEach(e => {
              this.subarrayRunningEchartData.inverters.push(e.stationName + "_" + e.subarrayName);
              this.subarrayRunningEchartData.electricityGeneration.push(e.perPowerRatio);
            })
          } else {
            this.subarrayRunningEchartData.inverters.push("-");
            this.subarrayRunningEchartData.electricityGeneration.push(0);
          }

        }
        callback && callback();
      });
    },
    searchInverterReport() {
      this.setInverterRunningData(this.getInverterSearchParams(), _ => this.initInverterChart());
    },
    searchSubarrayReport() {
      let isClickBtn = arguments.length ? true : false;
      this.setSubarrayRunningData(this.getSubarraySearchParams(isClickBtn), _ => this.initSubarrayChart());
    },
    exportInverterReport() {
      let lang = window.localStorage.getItem('lang') || 'zh'
      window.open("/biz/produceMng/exportInverterRpt?stationCodes=" + this.inverterReport.exportParams.stationCodes + "&time=" + this.inverterReport.exportParams.time + "&timeDim=" + this.inverterReport.exportParams.timeDim + '&lang=' + lang);
    },
    exportSubarrayReport() {
      let lang = window.localStorage.getItem('lang') || 'zh'
      window.open("/biz/produceMng/exportSubarrayRpt?stationCode=" + this.subarrayReport.exportParams.stationCode + "&time=" + this.subarrayReport.exportParams.time + "&timeDim=" + this.subarrayReport.exportParams.timeDim + '&lang=' + lang);
    },
    searchStationReport() {
      let self = this;
      this.setStationRunningData(this.getStationSearchParams(), _ => self.initStationRunningChart());
    },
    exportStationReport() {
      let stationCodes = this.stationReport.exportParams.stationCodes;
      let time = this.stationReport.exportParams.time;
      let timeDim = this.stationReport.exportParams.timeDim;
      let lang = window.localStorage.getItem('lang') || 'zh'
      window.open("/biz/produceMng/report/download/StationRuning?stationCodes=" + stationCodes + "&time=" + time + "&timeDim=" + timeDim + '&lang=' + lang);
    },
    getInverterSearchParams() {
      let self = this;
      let index, pageSize, stationCodes, time, timeDim;
      index = self.$data.inverterReport.index;
      pageSize = self.$data.inverterReport.pageSize;
      stationCodes = self.$data.inverterReportSearchForm.stationCodes;
      if (self.$data.inverterReportSearchForm.timeType === '1'){
        self.$data.inverterReport.isDayTime = true;
        timeDim = 'day';
        time = self.$data.inverterReportSearchForm.dayTime;
      } else if (self.$data.inverterReportSearchForm.timeType === '2'){
        self.$data.inverterReport.isDayTime = false;
        timeDim = 'month';
        time = self.$data.inverterReportSearchForm.monthTime;
      } else if (self.$data.inverterReportSearchForm.timeType === '3'){
        self.$data.inverterReport.isDayTime = false;
        timeDim = 'year';
        time = self.$data.inverterReportSearchForm.yearTime;
      }
      if (stationCodes) {
        stationCodes = stationCodes.split(",")
      } else {
        stationCodes = [];
      }
      let result = {
        index,
        pageSize,
        stationCodes,
        time,
        timeDim
      };
      self.$data.inverterReport.exportParams = {stationCodes, time, timeDim};
      return result;
    },
    getSubarraySearchParams(isClickBtn) {
      let index, pageSize, stationCode, time, timeDim;
      index = this.subarrayReport.index;
      pageSize = this.subarrayReport.pageSize;
      stationCode = this.subarrayReportSearchForm.stationCodes;
      if (this.subarrayReportSearchForm.timeType === '1'){
        this.subarrayReport.isDayTime = true;
        timeDim = 'day';
        time = this.subarrayReportSearchForm.dayTime;
      } else if (this.subarrayReportSearchForm.timeType === '2'){
        this.subarrayReport.isDayTime = false;
        timeDim = 'month';
        time = this.subarrayReportSearchForm.monthTime;
      } else if (this.subarrayReportSearchForm.timeType === '3'){
        this.subarrayReport.isDayTime = false;
        timeDim = 'year';
        time = this.subarrayReportSearchForm.yearTime;
      }
      let result = {
        index,
        pageSize,
        stationCode,
        time,
        timeDim
      };
      isClickBtn && (this.subarrayReport.exportParams = {stationCode, time, timeDim})
      return result;
    },
    getStationSearchParams() {
      let self = this;
      let index, pageSize, stationCodes, time, timeDim;
      index = self.$data.stationReport.index;
      pageSize = self.$data.stationReport.pageSize;
      stationCodes = self.$data.stationReportSearchForm.stationCodes;
      if (self.$data.stationReportSearchForm.timeType === '1'){
        timeDim = 'day';
        time = self.$data.stationReportSearchForm.dayTime;
      } else if (self.$data.stationReportSearchForm.timeType === '2'){
        timeDim = 'month';
        time = self.$data.stationReportSearchForm.monthTime;
      } else if (self.$data.stationReportSearchForm.timeType === '3'){
        timeDim = 'year';
        time = self.$data.stationReportSearchForm.yearTime;
      }
      if (stationCodes) {
        stationCodes = stationCodes.split(",")
      } else {
        stationCodes = [];
      }
      if (timeDim === "day" && stationCodes.length === 1) {
        this.stationReport.isDayAndSingleStation = true;
      } else {
        this.stationReport.isDayAndSingleStation = false;
      }
      let result = {
        index,
        pageSize,
        stationCodes,
        time,
        timeDim
      };
      self.$data.stationReport.exportParams = {stationCodes, time, timeDim};
      return result;
    },
    collectTimeFormatter(row, column, cellValue, index) {
      let formatStr = "";
      if (this.stationReport.message === '1') {
        if (this.stationReport.exportParams.timeDim === "day") {
          formatStr = this.$t('dateFormat.yyyymmddhhmmss');
        } else if (this.stationReport.exportParams.timeDim === "month") {
          formatStr = this.$t('dateFormat.yyyymmdd');
        } else if (this.stationReport.exportParams.timeDim === "year") {
          formatStr = this.$t('dateFormat.yyyy');
        }
      } else {
        if (this.stationReport.exportParams.timeDim === "day") {
          formatStr = this.$t('dateFormat.yyyymmdd');
        } else if (this.stationReport.exportParams.timeDim === "month") {
          formatStr = this.$t('dateFormat.yyyymm');
        } else if (this.stationReport.exportParams.timeDim === "year") {
          formatStr = this.$t('dateFormat.yyyy');
        }
      }
      let result = new Date(cellValue).format(formatStr);
      return result;
    },
    collectSubarrayTimeFormatter(row, column, cellValue, index) {
      let formatStr = "";
      if (this.subarrayReport.message === '1') {
        if (this.subarrayReport.exportParams.timeDim === "day") {
          formatStr = this.$t('dateFormat.yyyymmddhhmmss');
        } else if (this.subarrayReport.exportParams.timeDim === "month") {
          formatStr = this.$t('dateFormat.yyyymmdd');
        } else if (this.subarrayReport.exportParams.timeDim === "year") {
          formatStr = this.$t('dateFormat.yyyymm');
        }
      } else {
        if (this.subarrayReport.exportParams.timeDim === "day") {
          formatStr = this.$t('dateFormat.yyyymmdd');
        } else if (this.subarrayReport.exportParams.timeDim === "month") {
          formatStr = this.$t('dateFormat.yyyymm');
        } else if (this.subarrayReport.exportParams.timeDim === "year") {
          formatStr = this.$t('dateFormat.yyyy');
        }
      }
      let result = "";
      if (cellValue) {
        result = new Date(cellValue).format(formatStr);
      }
      return result;
    },
    formatterDevType(row, column, cellValue, index) {
      for (let i = 0; i < this.devTypes.length; i++){
        let e = this.devTypes[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    changeShowInverterDetail() {
      this.isShowInverterDetail = !this.isShowInverterDetail;
      setTimeout(_ => {this.$data.inverterChart.resize();}, 0)
    }
  },
  computed: {
  },
  watch: {
  },
  destroyed() {
    window.removeEventListener("resize", this.resize);
  }
}
