<template>
  <section>
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <div class="button-bar inline left">
          <el-form-item>
            <el-radio-group v-model="searchData.statisticalType"
                            @change="searchData.statisticalRange = 'station'; search();">
              <!-- 按年统计 -->
              <el-radio-button label="year">{{$t('comAna.deepYear')}}</el-radio-button>
              <!-- 按月统计 -->
              <el-radio-button label="month">{{$t('comAna.deepMonth')}}</el-radio-button>
              <!-- 按日统计 -->
              <el-radio-button label="date">{{$t('comAna.deepDay')}}</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <span class="button-separate"></span>
          <el-form-item>
            <el-radio-group v-model="searchData.statisticalRange" @change="search">
              <!-- 电站 -->
              <el-radio-button label="station">{{$t('comAna.station')}}</el-radio-button>
              <!-- 子阵 -->
              <el-radio-button label="subarray" style="opacity: 0.5; pointer-events: none;">{{$t('comAna.subarray')}}</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </div>

        <div class="button-bar inline left">
          <span class="button-separate"></span>
          <!-- 统计时间 -->
          <el-form-item :label="$t('comAna.statiTime')">
            <el-date-picker v-model="searchData.statisticalTime"
                            :type="searchData.statisticalType"
                            :format="timeForamtComputed"
                            value-format="timestamp" :editable="false" clearable @change="search"></el-date-picker>
          </el-form-item>
          <!-- 选择电站 -->
          <el-form-item :label="$t('comAna.chooseStation')">
            <choose-station :myValues="searchData.stationCode"
                            :myTexts="searchData.stationName" :isMutiSelect="false"
                            :isDefaultSelectFirst="true" @station-change="stationChange"></choose-station>
          </el-form-item>
        </div>

        <div class="button-bar inline right">
          <!-- 导出 -->
          <el-button type="primary" @click="exportData">{{$t('export')}}</el-button>
        </div>
      </el-form>
    </div>
    <div class="content-box">
      <e-chart :options="chartOption" style="width: 100%; height: 20rem; min-width: 240px;"></e-chart>
      <section>
        <!-- 月报电站级分析报告: -->
        <MonthStationComm :myData="tableData" :myTableType="0" @handleMatrixNameClick="handleMatrixNameClick" v-if="searchData.statisticalType === 'month' && searchData.statisticalRange === 'station'"></MonthStationComm>
        <!-- 月报电站子阵级分析报告: -->
        <MonthStationComm :myData="tableData" :myTableType="5" v-else-if="searchData.statisticalType === 'month' && searchData.statisticalRange === 'subarray'"></MonthStationComm>
        <!-- 年报电站级分析报告: -->
        <MonthStationComm :myData="tableData" :myTableType="1" @handleMatrixNameClick="handleMatrixNameClick" v-else-if="searchData.statisticalType === 'year' && searchData.statisticalRange === 'station'"></MonthStationComm>
        <!-- 年报子阵级分析报告: -->
        <MonthStationComm :myData="tableData" :myTableType="2" v-else-if="searchData.statisticalType === 'year' && searchData.statisticalRange === 'subarray'"></MonthStationComm>
        <!-- 日分析：电站级得表格 -->
        <MonthStationComm :myData="tableData" :myTableType="3" @handleMatrixNameClick="handleMatrixNameClick" v-else-if="searchData.statisticalType === 'date' && searchData.statisticalRange === 'station'"></MonthStationComm>
        <!-- 日分析：电站子阵级 -->
        <!--<MonthStationComm :myData="tableData" :myTableType="4" :pvList="pvList" v-else-if="searchData.statisticalType === 'date' && searchData.statisticalRange === 'subarray'"></MonthStationComm>-->
        <DaySubarrayTable :myData="tableData" :myTableType="4" :pvList="pvList" v-else-if="searchData.statisticalType === 'date' && searchData.statisticalRange === 'subarray'"></DaySubarrayTable>
      </section>
      <el-pagination @size-change="pageSizeChange" @current-change="pageChange"
                     :current-page="searchData.index"
                     :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </section>
</template>

<script type="text/ecmascript-6">
  import ChooseStation from '@/components/chooseStation/index.vue'
  import MonthStationComm from './tableComm.vue'
  import DaySubarrayTable from './tableDaySubarray.vue'

  import Service from '@/service/compontAnalysis'

  export default {
    components: {
      ChooseStation,
      MonthStationComm,
      DaySubarrayTable
    },
    data () {
      return {
        searchData: {
          statisticalType: 'month',
          statisticalRange: 'station',
          statisticalTime: (Date.now() - 24 * 60 * 60 * 1000),
          stationCode: null,
          page: 1,
          pageSize: 10,
        },
        chartOption: {},
        tableData: [],
        // tableHeaders: [],
        pvList: [],
        pvCode: 6,
        total: 0,
        subarrayName: null
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      stationChange (vals, texts) {
        this.$set(this.searchData, 'stationCode', vals)
        this.$set(this.searchData, 'stationName', texts)
        this.search()
      },
      /**
       * 查询
       * @param isToFirst {boolean} 是否跳转到第一页
       */
      search (isToFirst) {
        this.$set(this, 'tableData', []) // 查询之前需要先清空
        const _this = this

        if (isToFirst) {
          this.searchData.page = 1
        }

        let params = {}
        params.countTime = this.searchData.statisticalTime
        params.index = this.searchData.page
        params.pageSize = this.searchData.pageSize
        if (this.searchData.statisticalRange === 'subarray') {
          params.matrixId = this.searchData.matrixId
        } else {
          params.stationCode = this.searchData.stationCode
        }
        let requestUrl = 'Analysis'
        requestUrl = this.searchData.statisticalRange.firstUpperCase() + this.searchData.statisticalType.firstUpperCase() + requestUrl
        Service['get' + requestUrl](params).then(resp => {
          let results = (resp.code === 1 && resp.results) || {}
          if (results) {
            _this.showChart(results.chartData)
            _this.showTable(results.tableData, results.total || 0, Math.max(results.pvCode || 0, 6))
          }
        })
      },

      showChart (data) {
        let formatTime = ''
        switch (this.searchData.statisticalType) {
          case 'year':
            // formatTime = this.$moment(this.searchData.statisticalTime).format(this.$t('comAna.year'))
            formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyy'))
            break
          case 'month':
            // formatTime = this.$moment(this.searchData.statisticalTime).format(this.$t('comAna.yearAndMonth'))
            formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymm'))
            break
          case 'date':
            // formatTime = this.$moment(this.searchData.statisticalTime).format(this.$t('comAna.yearAndMonthAndDay'))
            formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymmdd'))
            break
          default:
            // formatTime = this.$moment(this.searchData.statisticalTime).format('LL')
            formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymmdd'))
        }
        let rangeName = ''
        switch (this.searchData.statisticalRange) {
          case 'station':
            rangeName = this.$t('comAna.station')
            break
          case 'subarray':
            rangeName = this.$t('comAna.subarray')
            break
          default:
            rangeName = ''
        }
        let title = formatTime + ' ' + (this.searchData.stationName || '') + ' ' + this.$t('comAna.zdResult') // 组串诊断结果
        let self = this
        let series = []
        let options = {
          'single': {
            title: {
              text: title,
              left: 'center',
              textStyle: {
                fontSize: 18,
                fontWeight: 'normal',
              },
            },
            color: ['#2eb1e2', '#efc54d', '#FE3230', '#81BF23'],
            grid: {
              top: 80,
              left: 10,
              right: 0,
              bottom: 24,
              containLabel: true
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'line'
              },
              formatter: function (params) {
                let result = self.$t('comAna.diaResult') + '：<br/>'
                params.forEach(function (item, index) {
                  if (item.value === '-') return true
                  // result += item.marker + ' ' + item.name + (index === 0 ? '数' : '损失电量') + ' : ' + item.value + '</br>'     （损失电量  数）
                  result += item.marker + ' ' + item.name + (item.seriesName === self.$t('comAna.lossPower') ? item.seriesName : self.$t('comAna.shu')) + ' : ' + item.value + '</br>'
                })
                return result
              }
            },
            legend: {
              top: 24,
              right: 80,
              data: this.$t('comAna.zdTypeArr') // ['遮挡组串', '低效组串', '故障组串', '损失电量']
            },
            xAxis: [
              {
                type: 'category',
                axisTick: {
                  show: false,
                  alignWithLabel: true
                },
                axisLine: {
                  lineStyle: {
                    color: '#08A8D8'
                  }
                },
                splitLine: {
                  lineStyle: {
                    color: '#CAE7EF'
                  }
                },
                data: this.$t('comAna.zdType')
              }
            ],
            yAxis: [
              {
                name: this.$t('comAna.ge'), // 个
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#08A8D8'
                  }
                },
                splitLine: {
                  show: false,
                  lineStyle: {
                    color: '#CAE7EF'
                  }
                },
              },
              {
                name: 'kWh',
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#08A8D8'
                  }
                },
                splitLine: {
                  show: false,
                  lineStyle: {
                    color: '#CAE7EF'
                  }
                },
              }
            ],
          },
          'multi': {
            color: ['#2eb1e2', '#efc54d', '#FE3230'],
            title: {
              text: title,
              left: 'center',
              textStyle: {
                fontSize: 18,
                fontWeight: 'normal',
              },
            },
            grid: {
              top: 80,
              left: 10,
              right: 0,
              bottom: 20,
              containLabel: true
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            legend: {
              top: 30,
              right: 40,
              data: this.$t('comAna.lossTypeArr') // ['遮挡损失电量', '低效损失电量', '故障损失电量']
            },
            xAxis: [
              {
                type: 'category',
                axisTick: {show: false},
                axisLine: {
                  lineStyle: {
                    color: '#08A8D8'
                  }
                },
                splitLine: {
                  lineStyle: {
                    color: '#CAE7EF'
                  }
                },
              }
            ],
            yAxis: [
              {
                name: 'kWh',
                type: 'value',
                axisLine: {
                  lineStyle: {
                    color: '#08A8D8'
                  }
                },
                splitLine: {
                  lineStyle: {
                    color: '#CAE7EF'
                  }
                },
              }
            ],
          }
        }
        if (Object.getTypeOf(data) === 'Array') {
          this.chartOption = options['multi']

          let hidePVs = []
          let ineffPVs = []
          let troublePVs = []
          let xAxisData = []
          xAxisData.push(...this.$t('comAna.mon'))
          for (let i = 1; i <= 12; i++) {
            let statisticalItem
            for (let index in data) {
              if (data.hasOwnProperty(index)) {
                let item = data[index]
                if (item && i === Number(Date.parse(item.analysisTime).format('M'))) {
                  statisticalItem = item
                  break
                }
              }
            }
            if (statisticalItem) {
              hidePVs.push((statisticalItem.hidLostPower || 0).toFixed(3))
              ineffPVs.push((statisticalItem.ineffLostPower || 0).toFixed(3))
              troublePVs.push((statisticalItem.troubleLostPower || 0).toFixed(3))
            } else {
              hidePVs.push('-')
              ineffPVs.push('-')
              troublePVs.push('-')
            }
          }
          this.chartOption.xAxis[0].data = xAxisData
          series = [
            {
              name: this.$t('comAna.lossTypeArr')[0], // '遮挡损失电量',
              type: 'bar',
              barGap: 0,
              data: hidePVs
            },
            {
              name: this.$t('comAna.lossTypeArr')[1], // '低效损失电量',
              type: 'bar',
              data: ineffPVs
            },
            {
              name: this.$t('comAna.lossTypeArr')[2], // '故障损失电量',
              type: 'bar',
              data: troublePVs
            },
          ]
        } else {
          this.chartOption = options['single']
          let hidePV = data.hidPv
          let ineffPV = data.ineffPv
          let troublePV = data.troublePv
          series = [
            {
              name: this.$t('comAna.zdTypeArr')[0], // '遮挡组串',
              data: [
                hidePV ? (hidePV.hidPvNum || 0) : '-',
                '-', '-'
              ],
              type: 'bar',
              stack: this.$t('comAna.stringNum'), // '组串数',
              barWidth: '20%',
              yAxisIndex: 0,
            },
            {
              name: this.$t('comAna.zdTypeArr')[1], // '低效组串',
              data: [
                '-',
                ineffPV ? (ineffPV.ineffPvNum || 0) : '-',
                '-'
              ],
              type: 'bar',
              barWidth: '20%',
              stack: this.$t('comAna.stringNum'), // '组串数',
              yAxisIndex: 0,
            },
            {
              name: this.$t('comAna.zdTypeArr')[2], // '故障组串',
              data: [
                '-', '-',
                troublePV ? (troublePV.troublePvNum || 0) : '-'
              ],
              type: 'bar',
              barWidth: '20%',
              stack: this.$t('comAna.stringNum'), // '组串数',
              yAxisIndex: 0,
            },
            {
              name: this.$t('comAna.zdTypeArr')[3], // '损失电量',
              data: [
                hidePV ? (hidePV.hidPvCount || 0).toFixed(3) : '-',
                ineffPV ? (ineffPV.ineffPvCount || 0).toFixed(3) : '-',
                troublePV ? (troublePV.troublePvCount || 0).toFixed(3) : '-'
              ],
              type: 'bar',
              barWidth: '20%',
              yAxisIndex: 1,
            },
          ]
        }
        this.chartOption.series = series
      },
      showTable (data, total, pvCode) {
        if (this.searchData.statisticalRange === 'subarray' && this.searchData.statisticalType !== 'year') {
          let pvList = []
          for (let i = 1; i <= pvCode; i++) { // 组串i
            pvList.push({prop: 'pv' + i, label: this.$t('comAna.zc') + i})
          }
          this.$set(this, 'pvList', pvList)
          // let tableHeaders = [
          //   {
          //     prop: 'devAlias',
          //     label: '设备名称'
          //   },
          //   {
          //     label: '组串损失电量(kWh)',
          //     pvList: pvList
          //   }
          // ]
          // this.$set(this, 'tableHeaders', tableHeaders)
        }
        this.$set(this, 'tableData', data || [])
        this.$set(this, 'total', total)
      },
      /**
       * 每页显示的数量改变的事件
       * @param val
       */
      pageSizeChange (val) {
        this.$set(this.searchData, 'pageSize', val)
        this.search(true)
      },
      /**
       * 页数改变后的事件
       * @param val
       */
      pageChange (val) {
        this.$set(this.searchData, 'page', val)
        this.search()
      },
      /**
       * 跳转到子阵视图
       * @param data
       */
      handleMatrixNameClick (data) {
        if (data && !Object.isEmptyObject(data)) {
          this.searchData.matrixId = data.matrixId
          this.subarrayName = data.matrixName
          this.searchData.statisticalRange = 'subarray'
          this.search()
        }
      },
      /**
       * 导出报表
       */
      exportData () {
        let type = this.searchData.statisticalRange + '_' + this.searchData.statisticalType
        let time = this.searchData.statisticalTime
        let tmpStr = '';
        if (this.searchData.statisticalRange === 'station') {
          tmpStr += "&stationName=" + this.searchData.stationName
        } else {
          tmpStr += "&stationName=" + this.subarrayName
        }
        let id = this.searchData.statisticalRange === 'station' ? this.searchData.stationCode : this.searchData.matrixId
        let lang = localStorage.getItem('lang') || 'zh'
        window.open('/biz/compontAnalysis/exportData?type=' + type + '&time=' + time + '&stationCode=' + id + '&_=' + Date.parseTime() +
          tmpStr + '&lang=' + lang, '_self')
      }
    },
    watch: {},
    computed: {
      timeForamtComputed () { // 显示时间的格式化的计算
        if (!this.searchData.statisticalType) { // 如果不存在，就直接任务是月的
          return this.$t('dateFormat.yyyymm')
        }
        switch (this.searchData.statisticalType) {
          case 'year':
            return this.$t('dateFormat.yyyy')
          case 'date':
            return this.$t('dateFormat.yyyymmdd')
          default:
            return this.$t('dateFormat.yyyymm')
        }
      }
    }
  }
</script>

<style lang="less" scoped>
</style>

