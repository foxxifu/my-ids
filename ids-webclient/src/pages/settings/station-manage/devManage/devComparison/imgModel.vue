<!-- 图表模式 -->
<template>
  <div class="dev-comparison-img-parent">
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline" ref="searchDevEchartForm">
        <el-form-item :label="$t('devCom.chooseDev')" prop="devIds" :rules="[{required: true, message: $t('devCom.reqDev'), trigger: ['blur', 'change']}]">
          <choose-device-input
            :myValues="searchData.devIds"
            :myTexts="searchData.devNames"
            :maxSelectNum="5"
            isCheckDevType
            @device-change="devChange"
            ></choose-device-input>
        </el-form-item>
        <el-form-item :label="$t('devCom.timePoint')" prop="startTime" :rules="[{required: true, message: $t('devCom.req'), trigger: ['blur', 'change']}]">
          <el-date-picker
            v-model="searchData.startTime"
            type="date"
            :format="$t('dateFormat.yyyymmdd')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('devCom.sigPointName')" prop="queryColumn" :rules="[{required: true, message: $t('devCom.req'), trigger: ['blur', 'change']}]">
          <el-select v-model="searchData.queryColumn" style="width: 240px" filterable :placeholder="$t('devCom.chooseSigPoint')" clearable>
            <el-option v-for="item in signalList" :key="item.column_name" :label="item.display_name" :value="item.column_name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDeviceComparisonChart">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="showSearchDataText">{{showInfoText}}</div>
    <div class="echart-parent" :style="{'height': echartHeight + 'px'}" ref="imgChartContent" >
    </div>

  </div>
</template>

<script type="text/ecmascript-6">
  import echarts from 'echarts'
  import ChooseDeviceInput from '@/components/chooseDevice/index.vue'
  import devManagerService from '@/service/devManager'
  import ImageOptions from './options'

  export default {
    components: {
      ChooseDeviceInput
    },
    created () {
      this.getDeviceSignal()
      this.calcEchartHeight()
    },
    data () {
      return {
        searchData: {
          devIds: null,
          devNames: null,
          queryColumn: null,
          startTime: null,
        },
        signalList: [], // 选择的信号点
        echartHeight: 400,
        options: null,
        chart: null,
        chartWidth: 0,
        ySelectSingName: null,
        showInfoText: null
      }
    },
    filters: {},
    mounted: function () {
      let self = this
      self.initChart()
      this.$nextTick(function () {
        self.resetEchart()
      })
      window.onresize = () => {
        self.calcEchartHeight()
        self.resetEchart()
      }
    },
    methods: {
      devChange (vals, texts) {
        this.$set(this.searchData, 'devIds', vals)
        this.$set(this.searchData, 'devNames', texts)
      },
      getDeviceSignal () { // 查询信号的信息
        let self = this
        devManagerService.getDeviceSignal({devTypeId: 1}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          self.$set(self, 'signalList', datas)
        })
      },
      getDeviceComparisonChart () { // 获取vue的数据
        let self = this
        this.$refs.searchDevEchartForm.validate(valid => {
          if (valid) {
            setTimeout(function () { // 获取单位
              self.getUitName()
            }, 0)
            devManagerService.deviceComparisonChart(self.searchData).then(resp => {
              let queryDatas = (resp.code === 1 && resp.results) || {} // 查询到的数据,应该有一个单位的，这里接口文档里面这个就只是数据
              // 组装option的数据
              // 1.标题的内容
              // legendData
              let devNameArr = (self.searchData.devNames && self.searchData.devNames.split(',')) || []
              self.options.legend = {show: true, data: devNameArr, textStyle: {color: '#aaa'}}; // 设置对应的线对应的颜色的提示
              let xData = self.options.xAxis[0].data
              let yDataArr = ImageOptions.initYDataArr(devNameArr, xData) // 先获取y的空数据所有设备的
              ImageOptions.getYSerDatas(queryDatas, yDataArr, devNameArr, xData, self.searchData.queryColumn, 3) // 对yDataArr设置查询到的值
              var series = self.options.series = [];
              var yAxis = self.options.yAxis = []; // y轴坐标轴显示的信号点和单位的信息，只有一个坐标轴，所以这个里面只需要添加一个
              for (let i = 0; i < yDataArr.length; i++) {
                series.push(yDataArr[i])
              }
              console.log(self.ySelectSingName)
              yAxis.push(ImageOptions.setOneYUnit(self.ySelectSingName)); // 设置y轴坐标轴的文字显示
              self.chart.setOption(self.options, true)
              self.showInfoText = '[' + Date.parse(self.searchData.startTime).format(this.$t('dateFormat.yyyymmdd')) +
                '] ' + devNameArr.join(';') + ' ' + self.ySelectSingName
            })
          } else {
            this.$message(this.$t('devMan.infInc'))
          }
        })
      },
      calcEchartHeight () {
        let height = document.documentElement.clientHeight * 0.95 - 360
        if (height < 150) {
          height = 150
        }
        this.echartHeight = height
      },
      initChart () {
        let container = this.$refs.imgChartContent
        let options = this.options = ImageOptions.getNoDataOptions()
        let chart = this.chart = echarts.init(container)
        chart.setOption(options)
      },
      resetEchart () {
        this.chart.resize()
        this.chart.setOption(this.options, true)
      },
      getUitName () { // 获取单位的信息(即Y的坐标轴)
        let len = this.signalList.length
        let queryColumn = this.searchData.queryColumn
        for (let i = 0; i < len; i++) {
          let tmp = this.signalList[i]
          if (tmp.column_name === queryColumn) {
            this.ySelectSingName = tmp.display_name
            return
          }
        }
        this.ySelectSingName = ''
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
  .dev-comparison-img-parent {
    width: 100%;
    .echart-parent{
      width: 100%;
    }
    .showSearchDataText{
      font-size: 14px;
      font-weight: 600;
      color: #68BEE4;
    }
  }
</style>

