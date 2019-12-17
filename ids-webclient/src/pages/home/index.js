// import echarts from 'echarts'
import stationOverviewService from '@/service/stationOverview'
// 告警统计的option项
import { getAlarmCountInitOption, setAlarmCountOption } from '@/pages/home/options/alarmCalOption'
// kpi实时数据的option的对象
import kpiChartObj from '@/pages/home/options/kpiChartOption'
import stationStatusObj from '@/pages/home/options/stationStatusOptionObject'
import stationRangeOptionObject from '@/pages/home/options/stationRangeOptionObject'
import powerAndIncomeOptionObject from '@/pages/home/options/powerAndIncomeOption'
import deviceDistributionOptionObject from '@/pages/home/options/deviceDistributionOption'

// 对数字做每千位添加逗号的正则表达式
const MY_REGEX = /(\d{1,2})(?=(\d{3})+\.)/g
let count = 0 // 为了模拟数据改变统计请求的次数
const tempRandom = +(Math.random() * 100).toFixed(2) // 为了模拟数据改变统计请求的改变的值

export default {
  name: 'Home',
  components: {},
  filters: {},
  data () {
    return {
      data_time: null, // 获取实时数据的定时任务
      // 仪表盘的echart的选项数据
      gaugeChartDatas: {
        powerChart: {
          option: {},
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
          dayUnit: this.$t('unit.RMBUnit'),
          year: 0,
          yearUnit: this.$t('unit.RMBUnit')
        },
        yearPowerChart: {
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
          option: {},
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
          option: {},
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
          prOption: {},
          hourOption: {},
          showYlableNameArr: [], // 展示坐标名称的数据
        },
        powerAndIncome: { // 发电量和收益的相关的数据
          activeName: 'month',
          monthOption: {},
          yearOption: {},
          allYearOption: {}
        },
        deviceDistribution: { // 设备分布的echart和数据
          option: {},
          zj: 0,
          nbq1: 0,
          nbq2: 0,
          zlhlx: 0
        }
      },
      contribution: { // 贡献信息
        coal: '-', // 煤
        co2: '-', // 二氧化碳
        tree: '-', // 植树面积
        coalTotal: '-', // 累计煤
        co2Total: '-', // 累计二氧化碳
        treeTotal: '-', // 累计植树面积
      }
    }
  },
  created () {
  },
  mounted () {
    let self = this
    // dom更新完毕后执行的回调
    self.$nextTick(function () {
      // 仪表盘的数据
      self.initKpiChart()// 加载kpi的数据
      self.getKpiDatas()
      // 电站状态
      self.initStationStatusChart()
      self.getStationStatus()

      // 告警统计
      self.initAlarmChart()
      self.getAlarmCont()
      // 电站排名的数据
      self.stationRange()
      self.stationHoursRange()
      self.getStationRangeData()
      // 发电量和收益的信息
      self.initPowerAndIncom()
      self.getPowerAndIncomeData()
      // 设备分布的图表信息
      self.initDeviceDistribution()
      self.getDevDistributionData()
      // 环保贡献的数据
      self.getContributionDatas()

      // 请求时间 每5分钟请求一次
      let reqTime = 5 * 60 * 1000 // 5 * 60 * 1000
      // 定时任务请求 获取实时数据
      this.data_time = setInterval(function () {
        count++
        self.getKpiDatas()
        self.getStationStatus()
        self.getAlarmCont()
        self.getStationRangeData()
        self.getPowerAndIncomeData()
        self.getDevDistributionData()
        self.getContributionDatas()
      }, reqTime)
    })
    window.onresize = () => {
      self.size()
    }
  },
  computed: {
    showDesText () { // 获取升序和降序的文字的内容
      return (this.contentCharts.stationSort.des && this.$t('home.descend')) || this.$t('home.ascend')
    }
  },
  methods: {
    // 获取kpi的实时数据的请求
    getKpiDatas () {
      let self = this
      stationOverviewService.getRealtimeKPI({testData: count * tempRandom}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}// 数据
        // 1.更新实时功率数据
        let currentPower = datas.activePower || 0
        // if (currentPower > 10000) {
        //   currentPower = +(currentPower / 1000.0).toFixed(3)
        //   self.gaugeChartDatas.powerChart.unit === 'kW' && (self.gaugeChartDatas.powerChart.unit = 'MW')
        // } else {
        //   // 单位不一致就修改
        //   self.gaugeChartDatas.powerChart.unit === 'MW' && (self.gaugeChartDatas.powerChart.unit = 'kW')
        // }
        let cap = datas.capacity || 0
        kpiChartObj.setOption(self.gaugeChartDatas.powerChart.option, (currentPower - 0).toFixed(2) - 0, (cap - 0).toFixed(2) - 0)
        self.gaugeChartDatas.powerChart.value = (currentPower - 0).toFixed(2).replace(MY_REGEX, '$1,')
        self.gaugeChartDatas.powerChart.cap = (cap - 0).toFixed(2).replace(MY_REGEX, '$1,')
        // 2.更新日发电量数据 和年发电量的数据
        self.gaugeChartDatas.dayAndYearPowerChart.day = ((datas.dayCapacity || 0)).toFixed(2).replace(MY_REGEX, '$1,')
        self.gaugeChartDatas.dayAndYearPowerChart.year = ((datas.yearCap || 0)).toFixed(2).replace(MY_REGEX, '$1,')
        // 3.更新日收益数据 和年收益
        self.gaugeChartDatas.incommonDayAndYearChart.day = ((datas.dayIncome || 0)).toFixed(2).replace(MY_REGEX, '$1,')
        self.gaugeChartDatas.incommonDayAndYearChart.year = ((datas.yearIncome || 0)).toFixed(2).replace(MY_REGEX, '$1,')
        // 4.更新累计发电量 设置3位整数之间使用逗号分隔
        self.gaugeChartDatas.totalPowerChart.value = ((datas.totalCapacity || 0)).toFixed(2).replace(MY_REGEX, '$1,')
      })
    },
    // 获取电站状态的数据
    getStationStatus () {
      let self = this
      stationOverviewService.getStationStatus({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        let health = self.contentCharts.stationStatusChart.health = (datas.health || 0) - 0
        let trouble = self.contentCharts.stationStatusChart.trouble = (datas.trouble || 0) - 0
        let disconnected = self.contentCharts.stationStatusChart.disconnected = (datas.disconnected || 0) - 0
        let total = health + trouble + disconnected
        if (total === 0) {
          total = 1
        }
        self.contentCharts.stationStatusChart.total = total
        let option = self.contentCharts.stationStatusChart.option
        option.series = stationStatusObj.getAllSeData({total: total, value: health}, {
          total: total,
          value: trouble
        }, {total: total, value: disconnected})
      })
    },
    // 获取设备告警的数量
    getAlarmCont () {
      let self = this
      stationOverviewService.getAlarmStatistics({}).then(reps => {
        var datas = (reps.code === 1 && reps.results) || {}
        let prompt = self.contentCharts.alarmCountChart.prompt = (datas.prompt || 0) - 0
        let minor = self.contentCharts.alarmCountChart.minor = (datas.minor || 0) - 0
        let serious = self.contentCharts.alarmCountChart.serious = (datas.serious || 0) - 0
        let total = prompt + minor + serious
        self.contentCharts.alarmCountChart.total = total
        if (total === 0) {
          total = 1
        }
        let option = self.contentCharts.alarmCountChart.option
        if (!option) {
          return
        }
        setAlarmCountOption(option, total, prompt, minor, serious)
      })
    },
    size: function () {
      if (this.contentCharts.stationStatusChart.option) {
        let option = this.contentCharts.stationStatusChart.option
        let total = this.contentCharts.stationStatusChart.total
        // 主要目的是修改进度尾部的小圆点的位置
        option.series = stationStatusObj.getAllSeData({
          total: total,
          value: this.contentCharts.stationStatusChart.health
        }, {
          total: total,
          value: this.contentCharts.stationStatusChart.trouble
        }, {total: total, value: this.contentCharts.stationStatusChart.disconnected})
      }
    },
    // 电站排名PR的初始化
    stationRange () {
      this.contentCharts.stationSort.prOption = stationRangeOptionObject.getOption('pr', this.contentCharts.stationSort.des)
    },
    // 电站排名PR的初始化
    stationHoursRange () {
      this.contentCharts.stationSort.hourOption = stationRangeOptionObject.getOption('h', this.contentCharts.stationSort.des)
    },
    // 获取后台的RP或者等效利用小时数的数据
    getStationRangeData () {
      let self = this
      let action = 'getPRList' // 默认是获取PR数据的方法名称
      if (this.contentCharts.stationSort.activeName !== 'stationPr') { // 获取等效利用小时数的请求方法
        action = 'getPPRList'
      }
      stationOverviewService[action]({orderBy: this.contentCharts.stationSort.des ? 'DESC' : 'ASC'}).then(resp => {
        let option
        let nameArr = []
        let valueArr = []
        let datas = (resp.code === 1 && resp.results) || []
        let prName = 'pr'
        if (this.contentCharts.stationSort.activeName === 'stationPr') { // 如果是pr的数据
          option = self.contentCharts.stationSort.prOption
        } else { // 等效利用小时数
          option = self.contentCharts.stationSort.hourOption
          prName = 'ppr'
        }
        for (let i = 0; i < datas.length; i++) {
          let tmp = datas[i]
          nameArr.push(tmp.stationName)
          let tmpVal = tmp[prName]
          tmpVal = (tmpVal === undefined || tmpVal === null) ? '-' : tmpVal
          valueArr.push(tmpVal)
        }
        self.contentCharts.stationSort.showYlableNameArr = nameArr
        stationRangeOptionObject.setOption(option, nameArr, valueArr)
      })
    },
    // 获取发电量和收益的数据
    getPowerAndIncomeData () {
      let self = this
      let activeName = this.contentCharts.powerAndIncome.activeName
      let tempParam = {queryType: activeName}
      stationOverviewService.getPowerAndIncome(tempParam).then(resp => {
        let option
        let data = (resp.code === 1 && resp.results) || []
        if (activeName === 'month') {
          option = self.contentCharts.powerAndIncome.monthOption
        } else if (activeName === 'year') {
          option = self.contentCharts.powerAndIncome.yearOption
        } else {
          option = self.contentCharts.powerAndIncome.allYearOption
        }
        powerAndIncomeOptionObject.setOption(option, activeName, data, this)
      })
    },
    // 获取设备分布的数据
    getDevDistributionData () {
      let self = this
      stationOverviewService.getDevDistrition({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.contentCharts.deviceDistribution.zj = (datas.zj || 0)
        self.contentCharts.deviceDistribution.nbq1 = (datas.nbq1 || 0)
        self.contentCharts.deviceDistribution.nbq2 = (datas.nbq2 || 0)
        self.contentCharts.deviceDistribution.zlhlx = (datas.zlhlx || 0)
        let option = self.contentCharts.deviceDistribution.option
        deviceDistributionOptionObject.setOption(option, datas, this)
      })
    },
    // 获取环保贡献的数据
    getContributionDatas () {
      let self = this
      stationOverviewService.getContribution({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.contribution.coal = self.getTheValue(datas.coalYear)
        self.contribution.co2 = self.getTheValue(datas.co2Year)
        self.contribution.tree = self.getTheValue(datas.treeYear)
        self.contribution.coalTotal = self.getTheValue(datas.coalTotal)
        self.contribution.co2Total = self.getTheValue(datas.co2Total)
        self.contribution.treeTotal = self.getTheValue(datas.treeTotal)
      })
    },
    // 初始化设备分布的信息
    initDeviceDistribution () {
      this.contentCharts.deviceDistribution.option = deviceDistributionOptionObject.getOption(this)
    },
    // 初始发电量和收益的图表信息
    initPowerAndIncom () {
      // 月
      this.contentCharts.powerAndIncome.monthOption = powerAndIncomeOptionObject.getOption('month', this)
      // 年
      this.contentCharts.powerAndIncome.yearOption = powerAndIncomeOptionObject.getOption('year', this)
      // 寿命期
      this.contentCharts.powerAndIncome.allYearOption = powerAndIncomeOptionObject.getOption('allYear', this)
    },
    // 仪表盘的样式
    initKpiChart () {
      // 实时功率
      this.gaugeChartDatas.powerChart.option = kpiChartObj.getOption(10000, 4000)
    },
    // 电站状态的初始化
    initStationStatusChart () {
      this.contentCharts.stationStatusChart.option = {
        tooltip: {
          show: false,
        },
        toolbox: {
          show: false,
        },
        // 背景色透明
        backgroundColor: 'rgba(0,0,0,0)',
        series: stationStatusObj.getAllSeData()
      }
    },
    // 初始化告警统计的数据
    initAlarmChart () {
      this.contentCharts.alarmCountChart.option = getAlarmCountInitOption(this)// 不传递参数就使用默认的
    },
    // 点击切换排序的方法
    changeOrder () {
      this.contentCharts.stationSort.des = !this.contentCharts.stationSort.des
      this.getStationRangeData() // 查询pr或者等效利用小时数的数据
    },
    // 电站排名的tab改变事件
    stationPRAndHourChange (tab, event) {
      let self = this
      self.getStationRangeData() // 查询pr或者等效利用小时数的数据
    },
    // 发电量和收益改变事件
    powerAndIncomeChange (tab, event) {
      this.getPowerAndIncomeData() // 查询数据
    },
    // 保留两位小数的值,每隔千位使用逗号隔开
    getTheValue (value) {
      if (value === undefined || value === '-' || isNaN(value)) {
        return '-'
      }
      value += ''
      if (value.indexOf('.') > 0) {
        value = (value - 0).toFixed(2)
      }
      return value.replace(MY_REGEX, '$1,')
    }
  },
  beforeDestroy () {
    // 清除定时任务
    this.data_time && clearInterval(this.data_time)
  }
}
