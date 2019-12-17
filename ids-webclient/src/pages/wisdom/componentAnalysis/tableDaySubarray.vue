<!-- 组串诊断 按日统计的子阵表格信息 -->
<template>
  <el-table :data="myData" border style="width: 100%;" v-else-if="myTableType === 4" :max-height="maxHeight">
    <el-table-column prop="devAlias" :label="$t('comAna.devName')"
                     width="200"
                     align="center">
      <template slot-scope="scope">
        {{scope.row && scope.row.devAlias}}
      </template>
    </el-table-column>
    <el-table-column :label="$t('comAna.stringLossPower')" align="center">
      <el-table-column
        v-for="(pv, i) in pvList" :key="i" :prop="pv.prop" :label="pv.label" align="center">
        <template slot-scope="{row, $index}">
         <!-- <span
            :style="'background-color: ' + (row[pv.prop] | colorFilter)">
                    {{row[pv.prop] | pvDisplayFilter(vm)}}
          </span>-->
          <PvCom :myData="row[pv.prop] ? row[pv.prop] : '-'"></PvCom>
        </template>
      </el-table-column>
    </el-table-column>
  </el-table>
</template>

<script type="text/ecmascript-6">
  import PvCom from './pvComponent.vue'

  // 组串诊断子阵日的表格数据
  const props = {
    myData: [], // 表格里面的数据
    pvList: [], // 表头中组串的串数
  }
  // 渲染出来的颜色
  const colorArr = ['transparent', '#FE3230', '#efc54d', '#2eb1e2']

  export default {
    props: props,
    components: {
      PvCom
    },
    data () {
      return {
        maxHeight: 120,
        vm: this
      }
    },
    filters: {
      pvDisplayFilter (val, vm) {
        if (!val) {
          return '-'
        }
        let tmpArr = val.split(/\|/)
        if (tmpArr.length === 2) {
          if (tmpArr[1] && tmpArr[1].indexOf(',') === -1) {
            if (isNaN(tmpArr[1]) || tmpArr[1] === '0') { // 如果是0就代表的是正常
              return vm.$t('comAna.normal')
            } else if (isNaN(tmpArr[0])) {
              return '-'
            } else {
              return (+tmpArr[0]).toFixed(2)
            }
          } else { // 带有逗号就有几个状态 1:故障 2：遮挡损失 3：低效
            if (isNaN(tmpArr[0])) {
              return '-'
            } else {
              return (+tmpArr[0]).toFixed(2)
            }
          }
        } else {
          return vm.$t('comAna.normal')
        }
      },
      colorFilter (val) { // 这些逻辑目前有问题，没有对应的接口说明
        if (!val) {
          return colorArr[3]
        }
        let tmpArr = val.split(/\|/)
        if (tmpArr.length === 2) {
          if (tmpArr[1] && tmpArr[1].indexOf(",") === -1) {
            if (isNaN(tmpArr[1]) || tmpArr[1] === '0') { // 如果是0就代表的是正常
              return colorArr[0]
            } else {
              return colorArr[+tmpArr[1]]
            }
          } else { // 有多种状态,取最小的 带有逗号就有几个状态 1:故障 2：遮挡损失 3：低效
            let tmpStrArrs = tmpArr[1].split(',').map(item => {
              if (isNaN(item)) { // 不可能取的
                return 100
              }
              return +item;
            })
            debugger
            let tmp = Math.min.apply(Math, tmpStrArrs)
            if (tmp === 100) {
              return colorArr[3]
            } else {
              return colorArr[tmp]
            }
          }
        } else {
          return colorArr[0]
        }
      }
    },
    mounted: function () {
      let self = this
      this.$nextTick(function () {
        self.calcMaxHeight()
      })
      window.onresize = function () {
        self.calcMaxHeight()
      }
    },
    methods: {
      calcMaxHeight () {
        let height = Math.floor(document.documentElement.clientHeight - 280 - 320)
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
