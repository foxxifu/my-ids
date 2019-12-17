import echarts from 'echarts'
import kpiChartObj from '@/pages/home/options/kpiChartOption'
import powerAndIncomeOptionObject from '@/pages/home/options/powerAndIncomeOption'
import powerMonitoringObject from '@/pages/station/options/powerMonitoringOption'
import station from '@/service/singleStation'
import weather from '@/service/weather'
import localStorage from '@/utils/localStorage'
const MY_REGEX = /(\d{1,2})(?=(\d{3})+\.)/g
let weekArr = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];

export default {
  name: 'station',
  created () {
    weekArr = this.$t("station.weekArr")
  },
  data () {
    return {
      stationStatus: this.$t("station.stationStatus"),
      data_time: null, // 获取实时数据的定时任务
      // 仪表盘的echart的选项数据
      gaugeChartDatas: {
        powerChart: {
          chart: '',
          option: false,
          value: 0,
          unit: 'kW',
          cap: 0, // 装机容量
          capUnit: 'kW'
        },
        // 日发电量 和年发电量
        dayAndYearPowerChart: {
          // 日发电量的值
          day: 0,
          unitDay: 'kWh',
          // 年发电量的值
          year: 0,
          unitYear: 'kWh'
        },
        incommonDayAndYearChart: {
          day: 0,
          dayUnit: this.$t("station.RMBUnit"),
          year: 0,
          yearUnit: this.$t("station.RMBUnit"),
        },
        yearPowerChart: {
          // chart: '',
          // option: false,
          value: 0,
          unit: 'kWh'
        },
        totalPowerChart: {
          value: 0,
          unit: 'kWh'
        },
      },
      // 具体的内容的下面的echart图表
      contentCharts: {
        // 电站状态
        stationStatusChart: {
          chart: null,
          option: null,
          // 总数
          total: 1,
          // 正常
          health: 0,
          // 故障,
          trouble: 0,
          // 断链
          disconnected: 0
        },
        alarmCountChart: {
          chart: null,
          option: null,
          // 总数
          total: 1,
          // 提示
          prompt: 0,
          // 次要
          minor: 0,
          // 重要
          major: 0,
          // 严重
          serious: 0
        },
        stationSort: {
          activeName: 'stationPr',
          des: true,
          prChart: null,
          prOption: null,
          hourChart: null,
          hourOption: null
        },
        powerAndIncome: { // 发电量和收益的相关的数据
          activeName: 'month',
          monthChart: null,
          monthOption: null,
          yearChart: null,
          yearOption: null,
          allYearChart: null,
          allYearOption: null
        },
        deviceDistribution: { // 设备分布的echart和数据
          chart: null,
          option: null,
          zj: 0,
          nbq1: 0,
          nbq2: 0,
          zlhlx: 0
        },
        powerMonitoring: {
          chart: null,
          option: null
        }
      },
      contribution: { // 贡献信息
        coal: '-', // 煤
        co2: '-', // 二氧化碳
        tree: '-', // 植树面积
        coalTotal: '-', // 累计煤
        co2Total: '-', // 累计二氧化碳
        treeTotal: '-', // 累计植树面积
        month: [1, 2, 3],
        year: [1, 2, 3],
        total: [1, 2, 3]
      },
      stationInfo: {
        name: "",
        address: "",
        status: "",
        installedCapacity: "",
        runDate: "2018-01-01 00:00:00",
        ower: "",
        weathers: [
          {
            time: "",
            detail: "",
          }
        ]
      },
      devRunningStatus: {
        inputPower: "",
        outputPower: "",
        onGridPower: "",
      },
      stationCode: "",
      weathers: [
        {
          descr: "阴",
          temperature: "5~14℃",
          wind: "东风4级",
          img: "/assets/images/weather/weather60/4.png",
        },
        {
          descr: "晴",
          temperature: "22~31℃",
          wind: "东风4级",
          img: "/assets/images/weather/weather60/5.png",
        },
        {
          descr: "晴",
          temperature: "20~30℃",
          wind: "东风4级",
          img: "/assets/images/weather/weather60/5.png",
        },
      ],
      fiveMinutesTask: null,
      fiveSecondsTask: null,
      getKpiDatasFlag: true, // 获取KPI数据标记
      getDevRunningStatusFlag: true, // 获取电站功率输出数据标记
    }
  },
  filters: {
    dateFormater (dateTime, type = 'MM-dd') { // 获取当前时间的年月日 周
      if (!dateTime || isNaN(dateTime)) {
        return '-';
      }
      let date = new Date(dateTime);
      return date.format(type) + ' ' + weekArr[date.getDay()];
    }
  },
  mounted () {
    let self = this
    // dom更新完毕后执行的回调
    self.$nextTick(_ => {
      self.$data.stationCode = localStorage.getStore('stationParams').stationCode;
      // 仪表盘的数据
      self.initKpiChart(); // 加载kpi的数据
      self.getKpiDatas();
      self.getStationInfo();
      self.getDevRunningStatus();
      // 发电量和收益的信息
      self.initPowerAndIncom();
      self.getPowerAndIncomeData();
      self.initPowerMonitoring();
      // // 环保贡献的数据
      self.getContributionDatas();
      window.addEventListener("resize", self.size);
      this.initFiveMinutesTask();
      this.initFiveSecondsTask();
    })
  },
  methods: {
    initFiveMinutesTask() {
      this.fiveMinutesTask = setInterval(_ => {
        this.getStationInfo(); // 电站信息
        this.powerAndIncomeChange(); // 电站收益
        this.initPowerMonitoring(); // 电站功率曲线
        this.getContributionDatas(); // 电站社会贡献
      }, 5 * 60 * 1000); // 5分钟刷新
    }, // powerAndIncomeChange
    initFiveSecondsTask() {
      this.fiveSecondsTask = setInterval(_ => {
        this.initKpiChart();// 加载kpi的数据
        this.getKpiDatas();
        this.getDevRunningStatus();
      }, 5 * 1000); // 5秒钟刷新
    },
    // 删除定时任务
    clearTask() {
      this.fiveMinutesTask && clearInterval(this.fiveMinutesTask);
      this.fiveSecondsTask && clearInterval(this.fiveSecondsTask);
    },
    // 获取kpi的实时数据的请求
    getKpiDatas (callback) {
      let self = this;
      if (this.getKpiDatasFlag) {
        this.getKpiDatasFlag = false;
        station.getSingleStationCommonData({stationCode: self.$data.stationCode}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}// 数据
          // 1.更新实时功率数据
          let currentPower = datas.activePower || 0
          if (currentPower > 10000) {
            currentPower = currentPower / 1000.0
            self.gaugeChartDatas.powerChart.unit === 'kW' && (self.gaugeChartDatas.powerChart.unit = 'MW')
          } else {
            // 单位不一致就修改
            self.gaugeChartDatas.powerChart.unit === 'MW' && (self.gaugeChartDatas.powerChart.unit = 'kW')
          }
          let cap = datas.capacity || 0

          kpiChartObj.setOption(self.gaugeChartDatas.powerChart.option, (currentPower - 0).toFixed(2) - 0, (cap - 0).toFixed(2) - 0)
          self.gaugeChartDatas.powerChart.value = (currentPower - 0).toFixed(2).replace(MY_REGEX, '$1,')
          self.gaugeChartDatas.powerChart.cap = (cap - 0).toFixed(2).replace(MY_REGEX, '$1,')

          self.gaugeChartDatas.powerChart.chart.setOption(self.gaugeChartDatas.powerChart.option, true)
          // 2.更新日发电量数据 和年发电量的数据
          self.gaugeChartDatas.dayAndYearPowerChart.day = ((datas.dayCapacity || 0)).toFixed(2).replace(MY_REGEX, '$1,')
          self.gaugeChartDatas.dayAndYearPowerChart.year = ((datas.yearCap || 0)).toFixed(2).replace(MY_REGEX, '$1,')
          // 3.更新日收益数据 和年收益
          self.gaugeChartDatas.incommonDayAndYearChart.day = ((datas.dayIncome || 0)).toFixed(2).replace(MY_REGEX, '$1,')
          self.gaugeChartDatas.incommonDayAndYearChart.year = ((datas.yearIncome || 0)).toFixed(2).replace(MY_REGEX, '$1,')
          // 4.更新累计发电量 设置3位整数之间使用逗号分隔
          self.gaugeChartDatas.totalPowerChart.value = ((datas.totalCapacity || 0)).toFixed(2).replace(MY_REGEX, '$1,')
          callback && callback();
          this.getKpiDatasFlag = true;
        });
      }
    },
    getStationInfo(){
      let self = this;
      station.getSingleStationInfo({stationCode: self.$data.stationCode}).then(resp => {
        if (resp.code === 1){
          self.stationInfo.name = resp.results.stationName;
          self.stationInfo.address = resp.results.stationAddr;
          self.stationInfo.status = resp.results.stationStatus;
          self.stationInfo.installedCapacity = resp.results.installed_capacity;
          self.stationInfo.runDate = resp.results.onlineTime;
          self.stationInfo.ower = resp.results.contactPeople;
          self.runDays = resp.results.runDays;
          let latitude = resp.results.latitude;
          let longitude = resp.results.longitude;
          if (latitude !== undefined && longitude !== undefined) {
            self.getWeatherDaily({location: latitude + ":" + longitude});
          }
          document.title = self.stationInfo && self.stationInfo.name + '——' + self.$t('systemName');
          if (resp.results.stationFileId) {
            this.$refs.stationPic.src = "/biz/fileManager/downloadFile?fileId=" + resp.results.stationFileId;
          }
        }
      });
    },
    convertDateFromString(dateString) {
      if (dateString) {
        var arr1 = dateString.split(" ");
        var sdate = arr1[0].split('-');
        var date = new Date(sdate[0], sdate[1] - 1, sdate[2]);
        return date;
      }
    },
    onerror() {
      console.log(arguments)
      this.$refs.stationPic.src = "/assets/images/station/station.png";
      this.$refs.stationPic.onerror = null;
    },
    getDevRunningStatus(callback) {
      let self = this;
      if (this.getDevRunningStatusFlag) {
        this.getDevRunningStatusFlag = false;
        station.getDevPowerStatus({stationCode: self.$data.stationCode}).then(resp => {
          if (resp.code === 1){
            self.devRunningStatus.inputPower = this.round(resp.results.mpptPower);
            self.devRunningStatus.outputPower = this.round(resp.results.activePower);
            self.devRunningStatus.onGridPower = this.round(resp.results.meterActivePower);
            callback && callback();
          }
          this.getDevRunningStatusFlag = true;
        });
      }
    },
    getElectricityGenerationAndIncome() {
      station.getElectricityGenerationAndIncome({sId: self.$data.stationCode}).then(resp => {
      });
    },
    getRealTimePower() {
    },
    size: function () {
      if (this.gaugeChartDatas.powerChart.chart) {
        this.gaugeChartDatas.powerChart.chart.resize();
        this.gaugeChartDatas.powerChart.chart.setOption(this.gaugeChartDatas.powerChart.option, true)
      }
      this.contentCharts.powerAndIncome.monthChart && this.contentCharts.powerAndIncome.monthChart.resize()
      this.contentCharts.powerAndIncome.yearChart && this.contentCharts.powerAndIncome.yearChart.resize()
      this.contentCharts.powerAndIncome.allYearChart && this.contentCharts.powerAndIncome.allYearChart.resize()
      this.contentCharts.powerMonitoring.chart && this.contentCharts.powerMonitoring.chart.resize()
    },
    // 仪表盘的样式
    initKpiChart () {
      // 实时功率
      let container = this.$refs.powerChart
      let chart = this.gaugeChartDatas.powerChart.chart = echarts.init(container)
      chart.setOption((this.gaugeChartDatas.powerChart.option = kpiChartObj.getOption(10000, 4000)))
    },
    // 获取发电量和收益的数据
    getPowerAndIncomeData () {
      let self = this
      let activeName = this.contentCharts.powerAndIncome.activeName
      // let tempParam = {queryType: activeName}
      let chart, option, formatTimeType, timeData = [], powerData = [], profitData = [];
      // let data = (resp.code === 1 && resp.results) || []
      if (activeName === 'month') {
        chart = self.contentCharts.powerAndIncome.monthChart
        option = self.contentCharts.powerAndIncome.monthOption
        formatTimeType = "dd";
        let time = new Date();
        let days = new Date(time.getYear(), time.getMonth() + 1, 0).getDate();
        for (let i = 1; i <= days; i++) {
          let d = i < 10 ? "0" + i : "" + i;
          timeData.push(d);
          powerData.push("-");
          profitData.push("-");
        }
      } else if (activeName === 'year') {
        chart = self.contentCharts.powerAndIncome.yearChart
        option = self.contentCharts.powerAndIncome.yearOption
        formatTimeType = "MM";
        for (let i = 1; i <= 12; i++) {
          let d = i < 10 ? "0" + i : "" + i;
          timeData.push(d);
          powerData.push("-");
          profitData.push("-");
        }
      } else {
        activeName = "allYear";
        chart = self.contentCharts.powerAndIncome.allYearChart
        option = self.contentCharts.powerAndIncome.allYearOption
        formatTimeType = "yyyy";
      }
      powerAndIncomeOptionObject.setOption(option, activeName, [])
      station.getSingleStationPowerAndIncome({queryType: activeName, stationCode: self.$data.stationCode}).then(resp => {
        option.xAxis[0].data = [];
        option.series[0].data = [];
        option.series[1].data = [];
        resp.results.forEach(function(e){
          if (activeName === "allYear"){
            timeData.push(new Date(e.collectTime).format(formatTimeType));
            if (e.producePower == 0){
              powerData.push(e.producePower);
            } else {
              powerData.push(e.producePower || "-");
            }
            if (e.powerProfit == 0) {
              profitData.push(e.powerProfit);
            } else {
              profitData.push(e.powerProfit || "-");
            }
          } else {
            let time = new Date(e.collectTime).format(formatTimeType);
            let index = timeData.indexOf(time);
            powerData[index] = e.producePower;
            profitData[index] = e.powerProfit;
          }
        });
        option.xAxis[0].data = timeData;
        option.series[0].data = powerData;
        option.series[1].data = profitData;
        chart.setOption(option, true)
      });
    },
    // 初始发电量和收益的图表信息
    initPowerAndIncom () {
      // 月
      let container = this.$refs.month
      let chart = this.contentCharts.powerAndIncome.monthChart = echarts.init(container)
      chart.setOption((this.contentCharts.powerAndIncome.monthOption = powerAndIncomeOptionObject.getOption('month', this)))
      // 年
      container = this.$refs.year
      chart = this.contentCharts.powerAndIncome.yearChart = echarts.init(container)
      chart.setOption((this.contentCharts.powerAndIncome.yearOption = powerAndIncomeOptionObject.getOption('year', this)))
      // 寿命期
      container = this.$refs.allYear
      chart = this.contentCharts.powerAndIncome.allYearChart = echarts.init(container)
      chart.setOption((this.contentCharts.powerAndIncome.allYearOption = powerAndIncomeOptionObject.getOption('allYear', this)))
    },
    // 初始化功率监控图表信息
    initPowerMonitoring() {
      // 月
      let container = this.$refs.powerMonitoring
      let chart = this.contentCharts.powerMonitoring.chart = echarts.init(container);
      this.contentCharts.powerMonitoring.option = powerMonitoringObject.getOption();
      let self = this;
      // station.getRealTimePower({sId: self.$data.stationCode}).then(resp => {
      station.getSingleStationActivePower({stationCode: self.$data.stationCode}).then(resp => {
        self.contentCharts.powerMonitoring.option.xAxis[0].data = [];
        self.contentCharts.powerMonitoring.option.series[0].data = [];
        let beginTime = new Date("2018-01-01 00:00:00");
        let endTime = new Date("2018-01-01 23:59:59");
        let times = [];
        let activePowers = [];
        let begin = beginTime.getTime();
        while (begin < endTime.getTime()){
          times.push(new Date(begin).format("hh:mm"));
          begin = begin + 5 * 60 * 1000;
        }
        let resultTime = [];
        let resultActivePower = [];
        resp.results.forEach(function(e){
          let time = new Date(e.collectTime).format("hh:mm");
          resultTime.push(time);
          if (e.activePower == 0) {
            resultActivePower.push(e.activePower);
          } else {
            resultActivePower.push(e.activePower || "-");
          }
        });
        for (let i = 0; i < times.length; i++) {
          let t = times[i];
          if (resultTime.contains(t)) {
            activePowers.push(resultActivePower.shift());
          } else {
            activePowers.push("-");
          }
        }
        self.contentCharts.powerMonitoring.option.xAxis[0].data = times;
        self.contentCharts.powerMonitoring.option.series[0].data = activePowers;
        chart.setOption(this.contentCharts.powerMonitoring.option)
      });
    },
    // 发电量和收益改变事件
    powerAndIncomeChange (tab, event) {
      this.getPowerAndIncomeData() // 查询数据powerMonitoringObject
      let chart
      if (this.contentCharts.powerAndIncome.activeName === 'year') { // 年
        chart = this.contentCharts.powerAndIncome.yearChart;
      } else if (this.contentCharts.powerAndIncome.activeName === 'allYear') {
        chart = this.contentCharts.powerAndIncome.allYearChart;
      } else {
        chart = this.contentCharts.powerAndIncome.monthChart;
      }
      setTimeout(() => {
        chart && chart.resize();
      }, 10)
    },
    // 保留两位小数的值,每隔千位使用逗号隔开
    getTheValue(value) {
      if (value === undefined || value === '-' || isNaN(value)) {
        return '-';
      }
      value += '';
      if (value.indexOf('.') > 0) {
        value = (value - 0).toFixed(2);
      }
      return value.replace(MY_REGEX, '$1,');
    },
    // 获取环保贡献的数据
    getContributionDatas() {
      let self = this;
      station.getContribution({stationCode: self.$data.stationCode}).then(resp => {
        if (resp.code === 1){
          self.contribution.month = [this.round(resp.results.coalMonth), this.round(resp.results.co2Month), this.round(resp.results.treeMonth)];
          self.contribution.year = [this.round(resp.results.coalYear), this.round(resp.results.co2Year), this.round(resp.results.treeYear)];
          self.contribution.total = [this.round(resp.results.coalTotal), this.round(resp.results.co2Total), this.round(resp.results.treeTotal)];
        }
      });
    },
    getWeatherDaily(param){
      weather.getWeatherDaily(param).then(resp => {
        if (resp.code === 1){
          let results = resp.results.results;
          if (results && results.length) {
            let daily = results[0].daily;
            if (daily && daily.length){
              daily.forEach((e, i) => {
                this.weathers[i].descr = e.text_day;
                this.weathers[i].temperature = e.low + "~" + e.high + "℃";
                this.weathers[i].wind = e.wind_direction;
                this.weathers[i].img = '/assets/images/weather/weather60/' + (e.code_day || '99') + '.png';
              });
            }
          }
        }
      });
    },
    // 保留三位小数
    round(num) {
      if (isFinite(num)) {
        return Math.round(num * 1000) / 1000;
      } else {
        return num;
      }
    }
  },
  watch: {},
  computed: {
    /*runDays() {
      let d = new Date(this.$data.stationInfo.runDate);
      let n = new Date();
      let result = Math.floor((n.getTime() - d.getTime()) / (3600 * 24 * 1000));
      return result;
    }*/
  },
  destroyed () {
    window.removeEventListener("resize", this.size);
    this.clearTask();
  }
}
