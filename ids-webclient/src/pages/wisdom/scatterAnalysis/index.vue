<template>
  <section>
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <div class="button-bar inline left">
          <el-form-item :label="$t('scaAna.statiTime')">
            <el-date-picker v-model="searchData.statisticalTime" :type="date"
                            :format="$t('dateFormat.yyyymmdd')"
                            value-format="timestamp" :editable="false" clearable></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('scaAna.chooseStation')">
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
          <el-table-column prop="devAlias" :label="$t('scaAna.devName')" width="200">
            <template slot-scope="{row, $index}">
          <span>
          {{row.devAlias}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="productPower" :label="$t('scaAna.dayYields') + '(kWh)'" width="150">
            <template slot-scope="{row, $index}">
          <span>
          {{row.productPower | numDataFilter | fixedNumber(3)}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="efficiency" :label="$t('scaAna.conPr') + '(%)'" width="110">
            <template slot-scope="{row, $index}">
          <span>
          {{row.efficiency | numDataFilter | fixedNumber(3)}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="equivalentHour" :label="$t('scaAna.eqHours') + '(h)'" width="100">
            <template slot-scope="{row, $index}">
          <span>
          {{row.equivalentHour | numDataFilter | fixedNumber(3)}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="peakPower" :label="$t('scaAna.maxAcPower') + '(kW)'" width="160">
            <template slot-scope="{row, $index}">
          <span>
          {{row.peakPower | numDataFilter | fixedNumber(3)}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="discreteRate" :label="$t('scaAna.stringRate') + '(%)'" width="140">
            <template slot-scope="{row, $index}">
          <span :class="getRateClass(row.discreteRate)">
          {{row.discreteRate | rateFilter | fixedNumber(3)}}
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="isAnalysis" :label="$t('scaAna.isAnalysis')" width="95">
            <template slot-scope="{row, $index}">
              <!--分析情况-->
              <span>
              {{row.isAnalysis == 1 ? $t('scaAna.analysisTypeArr')[0] : (row.isAnalysis == 2 ? $t('scaAna.analysisTypeArr')[1] : $t('scaAna.analysisTypeArr')[2])}}
              </span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('scaAna.pvAndpi').replace('{0}', '(A)').replace('{1}', '(V)')" align="center">
            <el-table-column v-for="(pv, i) in pvList" :key="i" :prop="pv.prop" :label="pv.label" align="center">
              <template slot-scope="{row, $index}">
                  <span>
                    {{row[pv.prop] | numDataFilter}}
                  </span>
              </template>
            </el-table-column>
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
        pvList: []
      }
    },
    filters: {
      numDataFilter (val) {
        if (val === null || val === undefined) {
          return '-'
        }
        return val
      },
      rateFilter (val) {
        if (val === null || val === undefined || val === -1) {
          return '-'
        }
        return val
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
        Service.getScatterAnalysis(params).then(resp => {
          let results = (resp.code === 1 && resp.results) || {}
          if (results) {
            _this.showChart(results.chartData)
            _this.showTable(results.tableData, results.total || 0, Math.max(results.pvCode || 0, 6))
          }
        })
      },

      showChart (data) {
        const _this = this
        // let formatTime = _this.$moment(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymmdd'))
        let formatTime = new Date(this.searchData.statisticalTime).format(this.$t('dateFormat.yyyymmdd'));
        let title = formatTime + ' ' + (this.searchData.stationName || '') + this.$t('scaAna.plantStringRate')
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
              data: this.$t('scaAna.scattEchartXArr')
            }
          ],
          yAxis: [
            {
              name: this.$t('scaAna.quantity'),
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
        let pvList = []
        for (let i = 1; i <= pvCode; i++) {
          pvList.push({prop: 'pv' + i, label: this.$t('scaAna.zc') + i})
        }
        this.$set(this, 'pvList', pvList)
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
          this.searchData.statisticalRange = 'subarray'
          this.search()
        }
      },
      /**
       * 导出报表
       */
      exportData () {
        let type = 'scatter'
        let time = this.searchData.statisticalTime
        let id = this.searchData.stationCode
        let lang = localStorage.getItem('lang') || 'zh'
        window.open('/biz/compontAnalysis/exportData?type=' + type + '&time=' + time + '&stationName=' + this.searchData.stationName + '&stationCode=' + id + '&_=' + Date.parseTime() + '&lang=' + lang, '_self')
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

