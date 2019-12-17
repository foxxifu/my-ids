<!-- 组件厂家信息详情 -->
<template>
  <table width="100%" class="group-factory-detail-table">
    <tr>
      <td class="td-label">{{ $t('devMan.groupStrFac') }}</td>
      <td class="td-value">{{data.manufacturer}}</td>
      <td class="td-label">{{ $t('devMan.comType') }}</td>
      <td class="td-value">{{data.moduleVersion}}</td>
      <!--组件标称最大功率(Wpx)-->
      <td class="td-label">{{ $t('devMan.maxPow') }}</td>
      <td class="td-value">{{data.standardPower}}</td>
    </tr>
    <tr>
      <!--组件类型-->
      <td class="td-label">{{ $t('devMan.compType') }}</td>
      <td class="td-value">{{data.moduleType | getZjType(vm)}}</td>
      <!--组件标称开路电压Voc(V)-->
      <td class="td-label">{{ $t('devMan.volVoc') }}</td>
      <td class="td-value">{{data.componentsNominalVoltage}}</td>
      <!--组件标称最小功率(Wpm))-->
      <td class="td-label">{{ $t('devMan.midPow') }}</td>
      <td class="td-value">{{data.standardPower}}</td>
    </tr>
    <tr>
      <!--组件最大功率点电压Vm(V))-->
      <td class="td-label">{{ $t('devMan.volMax') }}</td>
      <td class="td-value">{{data.maxPowerPointVoltage}}</td>
      <!--组件最大功率点电流Im(A)-->
      <td class="td-label">{{ $t('devMan.curMax') }}</td>
      <td class="td-value">{{data.maxPowerPointCurrent}}</td>
      <!--填充因子 FF(%)-->
      <td class="td-label">{{ $t('devMan.filFax') }}</td>
      <td class="td-value">{{data.fillFactor}}</td>
    </tr>
    <tr>
      <!--峰值功率温度系数(%))-->
      <td class="td-label">{{ $t('devMan.temCoe') }}</td>
      <td class="td-value">{{data.maxPowerTempCoef}}</td>
      <!--组件电压温度系数 (%/oC)-->
      <td class="td-label">{{ $t('devMan.volCoe') }}</td>
      <td class="td-value">{{data.voltageTempCoef}}</td>
      <!--组件电流温度系数(%/oC)-->
      <td class="td-label">{{ $t('devMan.curCoe') }}</td>
      <td class="td-value">{{data.currentTempCoef}}</td>
    </tr>
    <tr>
      <!--组件首年衰减率(%)-->
      <td class="td-label">{{ $t('devMan.firstYearAtt') }}</td>
      <td class="td-value">{{data.firstDegradationDrate}}</td>
      <!--组件逐年衰减率(%)-->
      <td class="td-label">{{ $t('devMan.annDec') }}</td>
      <td class="td-value">{{data.secondDegradationDrate}}</td>
      <!--组件电池片数(片)-->
      <td class="td-label">{{ $t('devMan.batSli') }}</td>
      <td class="td-value">{{data.cellsNumPerModule}}</td>
    </tr>
    <tr>
      <!--工作温度(oC) (最小值)-->
      <td class="td-label">{{ $t('devMan.workTemMid') }}</td>
      <td class="td-value">{{data.minWorkTemp}}</td>
      <!--工作温度(oC) (最大值)-->
      <td class="td-label">{{ $t('devMan.workTemMax') }}</td>
      <td class="td-value">{{data.maxWorkTemp}}</td>
      <!--创建时间-->
      <td class="td-label">{{ $t('devMan.creationTime') }}</td>
      <td class="td-value">{{data.createTime | timestampFomat($t('dateFormat.yyyymmddhhmmss'))}}</td>
    </tr>
  </table>
</template>

<script type="text/ecmascript-6">
  import deviceService from '@/service/device'

  const props = {
    moduleId: { // 组件厂家的信息表的id
      type: Number,
      default: null
    }
  }
  export default {
    props: props,
    components: {},
    created () {
      this.getStationPvModuleDetail()
    },
    data () {
      return {
        vm: this,
        // 展示的数据
        data: {}
      }
    },
    filters: {
      getZjType (val, vm) {
        val = val || ''
        if (val === '1') {
          return vm.$t('devMan.componentType')[0] // 多晶
        } else if (val === '2') {
          return vm.$t('devMan.componentType')[1] // 单晶
        } else if (val === '3') {
          return vm.$t('devMan.componentType')[2] // N型单晶
        } else if (val === '4') {
          return vm.$t('devMan.componentType')[3] // PERC单晶
        } else if (val === '5') {
          return vm.$t('devMan.componentType')[4] // 单晶双玻
        } else if (val === '6') {
          return vm.$t('devMan.componentType')[5] // 多晶双玻
        } else if (val === '7') {
          return vm.$t('devMan.componentType')[6] // 单晶四栅60片
        } else if (val === '8') {
          return vm.$t('devMan.componentType')[7] // 单晶四栅72片
        } else if (val === '9') {
          return vm.$t('devMan.componentType')[8] // 多晶四栅60片
        } else if (val === '10') {
          return vm.$t('devMan.componentType')[9] // 多晶四栅72片
        }
        return ''
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      getStationPvModuleDetail () { // 获取组件信息
        if (this.moduleId) {
          // 请求去查询数据
          let self = this
          deviceService.getStationPvModuleDetail({id: this.moduleId}).then(resp => {
            let data = (resp.code === 1 && resp.results) || {}
            self.$set(self, 'data', data)
          })
        } else {
          this.data = {}
        }
      }
    },
    watch: {
      moduleId () { // 观察是否变化，如果变化就需要重新查询数据
        this.getStationPvModuleDetail()
      }
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
  .group-factory-detail-table {
    & > tr > td:first-child {
      border-left: 1px solid #E1E1E1;
    }
    & > tr:first-child > td {
      border-top: 1px solid #E1E1E1;
    }
    td.td-label {
      text-align: center;
      background: #ccc;
    }
    td.td-value {
      text-indent: 10px;
    }
  }
</style>

