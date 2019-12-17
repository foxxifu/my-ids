<template>
  <section>
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <div class="button-bar inline left">
          <el-form-item :label="$t('dCAna.statiTime')">
            <el-date-picker v-model="searchData.statisticalTime" :type="date"
                            :format="$t('dateFormat.yyyymmdd')"
                            value-format="timestamp" :editable="false" clearable></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('dCAna.chooseStation')">
            <choose-station :myValues="searchData.stationCode"
                            :myTexts="searchData.stationName" :isMutiSelect="false"
                            :isDefaultSelectFirst="true" @station-change="stationChange"></choose-station>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search(true)">{{ $t('search') }}</el-button>
          </el-form-item>
        </div>

        <div class="button-bar inline right">
          <el-button type="primary" @click="exportData">{{$t('export')}}</el-button>
        </div>
      </el-form>
    </div>
    <div class="content-box">
      <e-chart :options="chartOption" style="width: 100%; height: 20rem; min-width: 240px;"></e-chart>
      <section>
        <!-- 离散率分析报告: -->
        <el-table :data="tableData" border style="width: 100%;">
          <el-table-column v-for="(item, index) in tableHeaders" :key="index" :prop="item.prop" :label="item.label"
                           :width="item.width || 180" :fixed="!item.pvList"
                           header-align="center">
            <template slot-scope="scope">
              <!-- scope.row[item.prop]=> 1:未分析 2：分析异常 3：已分析 -->
              <span v-if="item.prop === 'isAnalysis'">
                {{scope.row[item.prop] == 1 ? $t('dCAna.analysisTypeArr')[0] : (scope.row[item.prop] == 2 ? $t('dCAna.analysisTypeArr')[1] : $t('dCAna.analysisTypeArr')[2])}}
              </span>
              <span v-else-if="item.prop === 'discreteRate'" :class="getRateClass(scope.row.discreteRate)">
                {{scope.row[item.prop]}}
              </span>
              <span v-else>
                {{scope.row[item.prop]}}
              </span>
            </template>
            <template v-if="item.pvList && item.pvList.length > 0">
              <el-table-column v-for="(pv, i) in item.pvList" :key="i" :prop="pv.prop" :label="pv.label" align="center">
              </el-table-column>
            </template>
          </el-table-column>
        </el-table>
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

  import Service from '@/service/compontAnalysis'

  export default {
    components: {
      ChooseStation,
    },
    data () {
      return {
        searchData: {
          statisticalTime: (Date.now() - 24 * 60 * 60 * 1000),
          page: 1,
          pageSize: 10,
        },
        chartOption: {},
        tableData: [],
        tableHeaders: [],
        pvCode: 6,
        total: 0,
      }
    },
    filters: {
      analysisFilter (val) {

      }
    },
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
        const _this = this

        if (isToFirst) {
          this.searchData.page = 1
        }

        let params = {}
        params.countTime = this.searchData.statisticalTime
        params.index = this.searchData.page
        params.pageSize = this.searchData.pageSize
        params.stationCode = this.searchData.stationCode
        Service.getScatterDCAnalysis(params).then(resp => {
          let results = (resp.code === 1 && resp.results) || {}
          if (results) {
            _this.showChart(results.chartData)
            _this.showTable(results.tableData, results.total || 0, Math.max(results.pvCode || 0, 6))
          }
        })
      },
      showChart (data) {
        const _this = this
        // let formatTime = _this.$moment(this.searchData.statisticalTime).format('LL')
        let formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymmdd'));
        let title = formatTime + ' ' + (this.searchData.stationName || '') + ' ' + this.$t('dCAna.dcStrRote') // 电站和直流汇流箱组串离散率
        let results = []
        results.push(data['error'] || 0)
        results.push(data['ge20'] || 0)
        results.push(data['ge10lt20'] || 0)
        results.push(data['ge5lt10'] || 0)
        results.push(data['ge0lt5'] || 0)
        results.push(data['noAnalysis'] || 0)

        _this.chartOption = {
          color: '#2eb1e2',
          title: {
            text: title,
            left: 'center',
            textStyle: {
              fontSize: 18,
              fontWeight: 'normal',
            },
          },
          grid: {
            top: 48,
            left: 30,
            right: 0,
            bottom: 20,
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            confine: true,
            axisPointer: {
              type: 'shadow'
            },
            formatter: function (params) {
              let result = ''
              params.forEach(function (item) {
                result += item.seriesName + '<br/>' + item.marker + ' ' + item.name + ' : ' + (item.value || 0) + _this.$t('unit.inverterUnit') + '</br>'
              })
              return result
            }
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
              data: this.$t('dCAna.scattEchartXArr') // ['(异常)', '(20%以上)', '(10~20%)', '(5~10%)', '(0~5%)', '(未分析)']
            }
          ],
          yAxis: [
            { // 数量(台)
              name: this.$t('dCAna.num') + this.$t('dCAna.brackets')[0] + this.$t('dCAna.inverterUnit') + this.$t('dCAna.brackets')[1],
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
          series: [
            {
              name: title,
              type: 'bar',
              barGap: 0,
              barWidth: '20%',
              data: results
            },
          ]
        }
      },
      showTable (data, total, pvCode) {
        this.$set(this, 'tableData', data || [])
        this.$set(this, 'total', total)

        let pvList = []
        for (let i = 1; i <= pvCode; i++) {
          pvList.push({prop: 'pv' + i, label: this.$t('dCAna.zc') + i})
        }
        let tableHeaders = [
          {
            prop: 'devAlias',
            label: this.$t('dCAna.devName'), // '设备名称',
            width: 200
          },
          {
            prop: 'discreteRate',
            label: this.$t('dCAna.stringRate') + '(%)', // 组串离散率(%)
            width: 140
          },
          {
            prop: 'isAnalysis',
            label: this.$t('dCAna.isAnalysis'), // 是否分析
            width: 90
          },
          {
            prop: 'avgU',
            label: this.$t('dCAna.avgPV') + '(V)', // 平均电压(V)
            width: 150
          },
          {
            label: this.$t('dCAna.pvAndpi').replace('{0}', '(A)').replace('{1}', '(V)'), // 组串平均电流(A)/电压(V)
            pvList: pvList
          }
        ]
        this.$set(this, 'tableHeaders', tableHeaders)
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
          this.searchData.statisticalRange = 'subarray'
          this.search()
        }
      },
      /**
       * 导出报表
       */
      exportData () {
        let type = 'scatterDC'
        let time = this.searchData.statisticalTime
        let id = this.searchData.stationCode
        let lang = localStorage.getItem('lang') || 'zh'
        window.open('/biz/compontAnalysis/exportData?type=' + type + '&time=' + time +
          '&stationName=' + this.searchData.stationName + '&stationCode=' + id + '&_=' + Date.parseTime() + '&lang=' + lang, '_self')
      },
      getRateClass (val) {
        if (val === null || val === undefined || isNaN(val) || (val = +val) < 0) {
          return ''
        }
        if (val < 5) { // 绿色
          return 'lower5'
        }
        if (val < 10) { // 黄色
          return 'lower10'
        }
        if (val < 20) { // 粉红色
          return 'lower20'
        }
        // >= 20的样式 红色
        return 'lowerMore'
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
  span.lowerMore{
    background: red;
    color: white;
  }
  span.lower20{
    background: #ff4cef;
    color: white;
  }
  span.lower10{
    background: yellow;
  }
  span.lower5{
    background: green;
    color: white;
  }
</style>

