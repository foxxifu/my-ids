import echarts from 'echarts'
import epcService from "@/service/epc";

export default {
  data() {
    return {
      shipmentsChart: null,
      gridConnectedChart: null,
      returnMachineChart: null,
      consignmentOfInverterChart: null,
      elementSize: "",
      statisticsSearchForm: {},
      inverterInfo: "shipments",
      shipmentsTableData: [
        {
          num: "1",
          epc: "EPC庚",
          location: "青海省",
          shipmentsNum: "1700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "2",
          epc: "EPC甲",
          location: "甘肃省",
          shipmentsNum: "2600",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "3",
          epc: "EPC乙",
          location: "宁夏自治区",
          shipmentsNum: "2600",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
      ],
      gridConnectedTableData: [
        {
          num: "1",
          epc: "EPC庚",
          location: "青海省",
          shipmentsNum: "1700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "2",
          epc: "EPC甲",
          location: "甘肃省",
          shipmentsNum: "2600",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "3",
          epc: "EPC乙",
          location: "宁夏自治区",
          shipmentsNum: "2600",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
      ],
      returnMachineTableData: [
        {
          num: "1",
          epc: "EPC庚",
          location: "青海省",
          shipmentsNum: "12",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "2",
          epc: "EPC甲",
          location: "甘肃省",
          shipmentsNum: "32",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
        {
          num: "3",
          epc: "EPC乙",
          location: "宁夏自治区",
          shipmentsNum: "44",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
        },
      ],
      consignmentOfInverterTableData: [
        {
          date: "1月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "2月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "3月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "4月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "5月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "6月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "7月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "8月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "9月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        },
        {
          date: "10月",
          shipmentsNum: "2700",
          zcsInverterNum: "7",
          jzsInverterNum: "10",
          zssInverterNum: "10",
          destination: "青海省",
        }
      ],
      shipmentsChartData: {
        xData: [],
        seriesData: [],
      },
      gridConnectedChartData: {
        xData: [],
        seriesData: [],
      },
      returnMachineChartData: {
        xData: [],
        seriesData: [],
      },
      consignmentOfInverterChartData: {
        xData: [],
        seriesData: [],
      },
    }
  },
  filters: {},
  computed: {},
  created() {
    // this.initShipmentsChart();
  },
  mounted() {
    let self = this
    // dom更新完毕后执行的回调
    self.$nextTick(function () {
      self.setShipmentsData(_ => self.initShipmentsChart());
      self.setGridConnectedData(_ => self.initGridConnectedChart());
      self.setReturnMachineData(_ => self.initReturnMachineChart());
      self.setConsignmentOfInverterData(_ => self.initConsignmentOfInverterChart());
    })
  },
  methods: {
    // 逆变器发货量排行
    initShipmentsChart() {
      let self = this;
      let shipments = this.$refs.shipments;
      this.$data.shipmentsChart = echarts.init(shipments);
      let option = {
        noDataLoadingOption: {
          text: '无数据',
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
            // data: ["EPC甲", "EPC乙", "EPC丙", "EPC丁", "EPC戊", "EPC己", "EPC庚", "EPC辛", "EPC壬", "EPC癸", "EPC子"],
            data: self.$data.shipmentsChartData.xData,
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
            name: '台',
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
            name: '台',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            // data: [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
            data: self.$data.shipmentsChartData.seriesData,
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
      this.$data.shipmentsChart.setOption(option, true);
    },
    // 逆变器并网量排行
    initGridConnectedChart() {
      let self = this;
      let gridConnected = this.$refs.gridConnected;
      this.$data.gridConnectedChart = echarts.init(gridConnected);
      let option = {
        noDataLoadingOption: {
          text: '无数据',
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
            // data: ["EPC甲", "EPC乙", "EPC丙", "EPC丁", "EPC戊", "EPC己", "EPC庚", "EPC辛", "EPC壬", "EPC癸", "EPC子"],
            data: self.$data.gridConnectedChartData.xData,
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
            name: '台',
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
            name: '台',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            // data: [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
            data: self.$data.gridConnectedChartData.seriesData,
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
      this.$data.gridConnectedChart.setOption(option, true);
    },
    // 逆变器换机量排行
    initReturnMachineChart() {
      let self = this;
      let returnMachine = this.$refs.returnMachine;
      this.$data.returnMachineChart = echarts.init(returnMachine);
      let option = {
        noDataLoadingOption: {
          text: '无数据',
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
            // data: ["EPC甲", "EPC乙", "EPC丙", "EPC丁", "EPC戊", "EPC己", "EPC庚", "EPC辛", "EPC壬", "EPC癸", "EPC子"],
            data: self.$data.returnMachineChartData.xData,
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
            name: '台',
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
            name: '台',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            // data: [12, 32, 44, 55, 22, 56, 34, 23, 45, 67, 99, 12],
            data: self.$data.returnMachineChartData.seriesData,
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
      this.$data.returnMachineChart.setOption(option, true);
    },
    // 发货量统计
    initConsignmentOfInverterChart() {
      let self = this;
      let consignmentOfInverter = this.$refs.consignmentOfInverter;
      this.$data.consignmentOfInverterChart = echarts.init(consignmentOfInverter);
      let option = {
        noDataLoadingOption: {
          text: '无数据',
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
            // data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            data: self.$data.consignmentOfInverterChartData.xData,
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
            name: '台',
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
            name: '台',
            type: 'bar',
            barCategoryGap: '30%',
            barWidth: '30%',
            // data: [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
            data: self.$data.consignmentOfInverterChartData.seriesData,
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
      this.$data.consignmentOfInverterChart.setOption(option, true);
    },
    // echart重画
    chartResize(chartName) {
      let self = this;
      if (chartName === "0") {
        setTimeout(_ => self.$data.shipmentsChart.resize(), 0);
      } else if (chartName === "1") {
        setTimeout(_ => self.$data.gridConnectedChart.resize(), 0);
      } else if (chartName === "2") {
        setTimeout(_ => self.$data.returnMachineChart.resize(), 0);
      } else if (chartName === "3") {
        setTimeout(_ => self.$data.consignmentOfInverterChart.resize(), 0);
      }
    },
    // tab切换
    tabClick(tab, event) {
      this.chartResize(tab.index);
    },
    setShipmentsData(callback) {
      let self = this;
      epcService.getStatisticsData({'type': 'manufacturer', 'model': "shipments"}).then(resp => {
        self.$data.shipmentsChartData = resp.results.chart;
        self.$data.shipmentsTableData = resp.results.report;
        callback && callback();
      });
    },
    setGridConnectedData(callback) {
      let self = this;
      epcService.getStatisticsData({'type': 'manufacturer', 'model': "gridConnected"}).then(resp => {
        self.$data.gridConnectedChartData = resp.results.chart;
        self.$data.gridConnectedTableData = resp.results.report;
        callback && callback();
      });
    },
    setReturnMachineData(callback) {
      let self = this;
      epcService.getStatisticsData({'type': 'manufacturer', 'model': "returnMachine"}).then(resp => {
        self.$data.returnMachineChartData = resp.results.chart;
        self.$data.returnMachineTableData = resp.results.report;
        callback && callback();
      });
    },
    setConsignmentOfInverterData(callback) {
      let self = this;
      epcService.getStatisticsData({'type': 'manufacturer', 'model': "consignmentOfInverter"}).then(resp => {
        self.$data.consignmentOfInverterChartData = resp.results.chart;
        self.$data.consignmentOfInverterTableData = resp.results.report;
        callback && callback();
      });
    }
  },
  watch: {}
}
