import echarts from 'echarts'
import reportManage from "@/service/reportManage";
import localStorage from '@/utils/localStorage';

export default {
  props: ['devId', 'dayTime'],
  data() {
    return {
      loading: true,
      stationCode: localStorage.getStore('stationParams').stationCode,
      stationName: localStorage.getStore('stationParams').stationName,
      elementSize: "",
      inverterDetailSearchForm: {
        dayTime: new Date(new Date().getTime() - 3600 * 24 * 1000).format(this.$t('dateFormat.yyyymmdd')),
        deviceIds: [],
        kpis: [],
        kpiFlags: [],
      },
      devicesIds: [],
      kpis: [
        {v: 'au', l: this.$t("station.aVoltage")},
        {v: 'bu', l: this.$t("station.bVoltage")},
        {v: 'cu', l: this.$t("station.cVoltage")},
        {v: 'ai', l: this.$t("station.aEle")},
        {v: 'bi', l: this.$t("station.bEle")},
        {v: 'ci', l: this.$t("station.cEle")},
        {v: 'pv1u', l: this.$t("station.voltage1")},
        {v: 'pv2u', l: this.$t("station.voltage2")},
        {v: 'pv3u', l: this.$t("station.voltage3")},
        {v: 'pv4u', l: this.$t("station.voltage4")},
        {v: 'pv5u', l: this.$t("station.voltage5")},
        {v: 'pv6u', l: this.$t("station.voltage6")},
        {v: 'pv7u', l: this.$t("station.voltage7")},
        {v: 'pv8u', l: this.$t("station.voltage8")},
        {v: 'pv9u', l: this.$t("station.voltage9")},
        {v: 'pv10u', l: this.$t("station.voltage10")},
        {v: 'pv11u', l: this.$t("station.voltage11")},
        {v: 'pv12u', l: this.$t("station.voltage12")},
        {v: 'pv13u', l: this.$t("station.voltage13")},
        {v: 'pv14u', l: this.$t("station.voltage14")},
        {v: 'pv1i', l: this.$t("station.ele1")},
        {v: 'pv2i', l: this.$t("station.ele2")},
        {v: 'pv3i', l: this.$t("station.ele3")},
        {v: 'pv4i', l: this.$t("station.ele4")},
        {v: 'pv5i', l: this.$t("station.ele5")},
        {v: 'pv6i', l: this.$t("station.ele6")},
        {v: 'pv7i', l: this.$t("station.ele7")},
        {v: 'pv8i', l: this.$t("station.ele8")},
        {v: 'pv9i', l: this.$t("station.ele9")},
        {v: 'pv10i', l: this.$t("station.ele10")},
        {v: 'pv11i', l: this.$t("station.ele11")},
        {v: 'pv12i', l: this.$t("station.ele12")},
        {v: 'pv13i', l: this.$t("station.ele13")},
        {v: 'pv14i', l: this.$t("station.ele14")},
      ],
      inverterDetailTabName: "",
      inverterDetails: [],
      devicesResponse: {}, // 获取设备数据
      inverterDetailResponse: {}, // 获取逆变器详细数据
      maxHeight: 300,
      inverterDetailChart: null,
      legendData: [],
      times: [],
      series: [],
    }
  },
  created() {
  },
  mounted() {
    // dom更新完毕后执行的回调
    this.$nextTick(_ => {
      this.init();
      this.inverterDetailSearchForm.deviceIds = [this.devId];
      this.inverterDetailSearchForm.dayTime = this.dayTime;
      this.getDevices();
      this.searchInverterDetail();
      this.resize();
    });
    window.addEventListener("resize", this.resize);
  },
  methods: {
    init() {
    },
    getReturn() {
      this.$emit('changeShowInverter');
    },
    resize() {
      this.inverterDetailChart.resize();
      this.maxHeight = window.innerHeight - 515;
    },
    getDevices() {
      reportManage.getDevices({stationCode: this.stationCode}).then(resp => {
        this.devicesResponse = resp;
      })
    },
    getInverterDetailReport(params) {
      this.loading = true;
      reportManage.getInverterDetailReport(params).then(resp => {
        this.inverterDetailResponse = resp;
        this.loading = false;
      })
    },
    searchInverterDetail() {
      let params = {
        stationCode: this.stationCode,
        beginTime: this.beginTime,
        endTime: this.endTime,
        deviceIds: this.inverterDetailSearchForm.deviceIds,
      }
      this.getInverterDetailReport(params);
    },
    collectTimeFormatter(row, column, cellValue, index) {
      return new Date(cellValue).format(this.$t('dateFormat.yyyymmddhhmmss'));
    },
    containsKpi(kpi) {
      let result = this.inverterDetailSearchForm.kpiFlags.contains(kpi);
      return result;
    },
    initInverterDetailChart() {
      let self = this;
      let inverterDetailChart = this.$refs.inverterDetailChart;
      if (!self.inverterDetailChart) {
        self.inverterDetailChart = echarts.init(inverterDetailChart);
      }
      let option = {
        tooltip: {
          trigger: 'axis',
          formatter: function(params, ticket, callback) {
            let result = params[0].axisValue;
            params.forEach(e => {
              result += "<br/>" + e.seriesName + this.$t("station.activePower") + e.value + "kW";
            })
            return result;
          }
        },
        legend: {
          data: self.legendData,
        },
        grid: {
          left: '1%',
          right: '1%',
          bottom: '1%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: self.times,
        },
        yAxis: {
          type: 'value'
        },
        series: self.series,
      };
      this.inverterDetailChart.setOption(option, true);
    },
    exportData() {
      let lang = window.localStorage.getItem('lang') || 'zh'
      window.open("/biz/produceMng/exportInverterDetailRpt?stationName="
        + this.stationName + "&stationCode=" + this.stationCode + "&beginTime=" + this.beginTime + "&endTime=" + this.endTime + "&deviceIds=" + this.inverterDetailSearchForm.deviceIds.join(',') + '&lang=' + lang)
    },
  },
  computed: {
    beginTime() {
      return new Date(new Date(this.inverterDetailSearchForm.dayTime).format(this.$t('dateFormat.yyyymmdd')) + " 00:00:00").getTime();
    },
    endTime() {
      return new Date(new Date(this.inverterDetailSearchForm.dayTime).format(this.$t('dateFormat.yyyymmdd')) + " 23:59:59").getTime();
    }
  },
  watch: {
    devicesResponse: {
      handler(newValue, oldValue) {
        if (newValue.code == 1) {
          this.devicesIds = newValue.results.list;
        }
      },
      deep: true,
    },
    inverterDetailResponse: {
      handler(newValue, oldValue) {
        if (newValue.code == 1) {
          let result = {};
          newValue.results.list.forEach(e => {
            if (!result[e.devId]) {
              result[e.devId] = {
                label: e.devName,
                value: e.devId,
                list: []
              }
            };
            result[e.devId].list.push(e);
          })
          this.inverterDetails = [];
          this.legendData = [];
          this.series = [];
          this.times = [];
          let init = false;
          for(let i in result) {
            this.legendData.push(result[i].label);
            this.inverterDetails.push(result[i]);
            let sData = {
              type: "line",
              name: result[i].label,
              data: [],
            }
            this.series.push(sData);
            result[i].list.forEach(e => {
              !init && this.times.push(new Date(e.collectTime).format("hh:mm"));
              sData.data.push(e.activePower);
            })
            init = true;
          }
          this.initInverterDetailChart();
          this.inverterDetailSearchForm.kpiFlags = this.inverterDetailSearchForm.kpis;
          this.inverterDetailTabName = this.inverterDetails[0] && this.inverterDetails[0].value;
        }
      },
      deep: true,
    },
  },
  destroyed() {
    window.removeEventListener("resize", this.resize);
  }
}
