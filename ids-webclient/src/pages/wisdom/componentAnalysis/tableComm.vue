<!-- 分析页面的表格内容 -->
<template>
  <div>
    <!-- 电站月的表格 -->
    <el-table :data="myData" border style="width: 100%;"v-if="myTableType === 0" :max-height="maxHeight">
      <!-- 子阵名称 -->
      <el-table-column
        prop="matrixName"
        :label="$t('comAna.subarrayName')"
        fixed="left"
        width="180">
        <template slot-scope="scope">
          <el-button @click="handleMatrixNameClick(scope.row)" type="text">
            {{scope.row && scope.row.matrixName}}
          </el-button>
        </template>
      </el-table-column>
      <!-- 子阵装机容量(KW) -->
      <el-table-column
        prop="installedCapacity"
        :label="$t('comAna.subarrayCap') + '(kW)'"
        width="190">
      </el-table-column>
      <!-- 组串总数(个) -->
      <el-table-column
        prop="pvNum"
        :label="$t('comAna.stringTotalNum') + '(' + $t('comAna.ge') + ')'"
        width="160">
      </el-table-column>
      <!-- 当月发电量(kWh) -->
      <el-table-column
        prop="productPower"
        :label="$t('comAna.dyfdl') + '(kWh)'">
      </el-table-column>
      <!-- 遮挡组串数(个) -->
      <el-table-column
        prop="hidPvNum"
        :label="$t('comAna.zdzcs') + '(' + $t('comAna.ge') + ')'"
        width="190">
      </el-table-column>
      <!-- 遮挡损失电量(kWh) -->
      <el-table-column
        prop="hidLostPower"
        :label="$t('comAna.lossTypeArr')[0] + '(kWh)'"
        width="185">
      </el-table-column>
      <!-- 低效组串数(个) -->
      <el-table-column
        prop="ineffPvNum"
        :label="$t('comAna.dxzcs') + '(' + $t('comAna.ge') + ')'"
        width="205">
      </el-table-column>
      <!-- 低效损失电量(kWh) -->
      <el-table-column
        prop="ineffLostPower"
        :label="$t('comAna.lossTypeArr')[1] + '(kWh)'"
        width="180">
      </el-table-column>
      <!-- 故障组串数(个) -->
      <el-table-column
        prop="troublePvNum"
        :label="$t('comAna.gzzcs') + '(' + $t('comAna.ge') + ')'"
        width="170">
      </el-table-column>
      <!-- 故障损失电量(kWh) -->
      <el-table-column
        prop="troubleLostPower"
        :label="$t('comAna.lossTypeArr')[2] + '(kWh)'"
        width="180">
      </el-table-column>
    </el-table>
    <!-- 电站年的表格 -->
    <el-table :data="myData" border style="width: 100%;" v-else-if="myTableType === 1" :max-height="maxHeight">
      <el-table-column
        prop="matrixName"
        :label="$t('comAna.subarrayName')"
        width="180">
        <template slot-scope="scope">
          <el-button @click="handleMatrixNameClick(scope.row)" type="text">
            {{scope.row && scope.row.matrixName}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="installedCapacity"
        :label="$t('comAna.subarrayCap') + '(kW)'"
        width="190">
      </el-table-column>
      <el-table-column
        prop="pvNum"
        :label="$t('comAna.stringTotalNum') + '(' + $t('comAna.ge') + ')'"
        width="200">
      </el-table-column>
      <!-- 当年发电量(kWh) -->
      <el-table-column
        prop="productPower"
        :label="$t('comAna.dnfdl') + '(kWh)'">
      </el-table-column>
      <el-table-column
        prop="hidLostPower"
        :label="$t('comAna.lossTypeArr')[0] + '(kWh)'"
        width="330">
      </el-table-column>
      <el-table-column
        prop="ineffLostPower"
        :label="$t('comAna.lossTypeArr')[1] + '(kWh)'"
        width="340">
      </el-table-column>
      <el-table-column
        prop="troubleLostPower"
        :label="$t('comAna.lossTypeArr')[2] + '(kWh)'"
        width="330">
      </el-table-column>
    </el-table>
    <!-- 子阵年的列表 -->
    <el-table :data="myData" border style="width: 100%;" v-else-if="myTableType === 2" :max-height="maxHeight">
      <el-table-column
        prop="analysisTime"
        :label="$t('comAna.statiTime')"
        width="180">
        <template slot-scope="scope">
          {{scope.row.analysisTime | formatDate}}
        </template>
      </el-table-column>
      <el-table-column
        prop="installedCapacity"
        :label="$t('comAna.subarrayCap') + '(kW)'"
        width="180">
      </el-table-column>
      <el-table-column
        prop="pvNum"
        :label="$t('comAna.stringTotalNum') + '(' + $t('comAna.ge') + ')'"
        width="180">
      </el-table-column>
      <el-table-column
        prop="productPower"
        :label="$t('comAna.dnfdl') + '(kWh)'">
      </el-table-column>
      <el-table-column
        prop="hidLostPower"
        :label="$t('comAna.lossTypeArr')[0] + '(kWh)'">
      </el-table-column>
      <el-table-column
        prop="ineffLostPower"
        :label="$t('comAna.lossTypeArr')[1] + '(kWh)'">
      </el-table-column>
      <el-table-column
        prop="troubleLostPower"
        :label="$t('comAna.lossTypeArr')[2] + '(kWh)'">
      </el-table-column>
    </el-table>
    <!-- 电站日的列表 -->
    <el-table :data="myData" border style="width: 100%;" v-else-if="myTableType === 3" :max-height="maxHeight">
      <el-table-column
        prop="matrixName"
        :label="$t('comAna.subarrayName')"
        fixed="left">
        <template slot-scope="scope">
          <el-button @click="handleMatrixNameClick(scope.row)" type="text">
            {{scope.row && scope.row.matrixName}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="installedCapacity"
        :label="$t('comAna.subarrayCap') + '(kW)'"
        width="190">
      </el-table-column>
      <el-table-column
        prop="pvNum"
        :label="$t('comAna.stringTotalNum') + '(' + $t('comAna.ge') + ')'"
        width="190">
      </el-table-column>
      <el-table-column
        prop="productPower"
        :label="$t('comAna.dyfdl') + '(kWh)'"
        width="130">
      </el-table-column>
      <el-table-column
        prop="hidPvNum"
        :label="$t('comAna.zdzcs') + '(' + $t('comAna.ge') + ')'"
        width="190">
      </el-table-column>
      <el-table-column
        prop="hidLostPower"
        :label="$t('comAna.lossTypeArr')[0] + '(kWh)'"
        width="180">
      </el-table-column>
      <el-table-column
        prop="ineffPvNum"
        :label="$t('comAna.dxzcs') + '(' + $t('comAna.ge') + ')'"
        width="205">
      </el-table-column>
      <el-table-column
        prop="ineffLostPower"
        :label="$t('comAna.lossTypeArr')[1] + '(kWh)'"
        width="180">
      </el-table-column>
      <el-table-column
        prop="troublePvNum"
        :label="$t('comAna.gzzcs') + '(' + $t('comAna.ge') + ')'"
        width="220">
      </el-table-column>
      <el-table-column
        prop="troubleLostPower"
        :label="$t('comAna.lossTypeArr')[2] + '(kWh)'"
        width="160">
      </el-table-column>
    </el-table>
    <el-table :data="myData" border style="width: 100%;" v-else-if="myTableType === 4" :max-height="maxHeight">
      <!--设备名称-->
      <el-table-column prop="devAlias" :label="$t('comAna.devName')"
                       width="200"
                        align="center">
        <template slot-scope="scope">
          {{scope.row && scope.row.devAlias}}
        </template>
        <!--<template v-else-if="item.pvList && item.pvList.length > 0">
          <el-table-column v-for="(pv, i) in item.pvList" :key="i" :prop="pv.prop" :label="pv.label" align="center">
            <template slot-scope="scope">
                  <span
                    :style="'display: block; background-color: ' + ['transparent', '#FE3230', '#efc54d', '#2eb1e2'][scope.row[pv.prop].split(/\|/)[1] || 0] +';'">
                    {{(scope.row[pv.prop].split(/\|/)[1] || 0) == 0 ? '正常' : scope.row[pv.prop].split(/\|/)[0]}}
                  </span>
            </template>
          </el-table-column>
        </template>-->
      </el-table-column>
      <!--组串损失电量(kWh)-->
      <el-table-column :label="$t('comAna.stringLossPower') + '(kWh)'" align="center">
        <el-table-column
                         v-for="(pv, i) in pvList" :key="i" :prop="pv.prop" :label="pv.label" align="center">
          <template slot-scope="{row, $index}">
             {{row[pv.prop] || '-'}}
          </template>
        </el-table-column>
      </el-table-column>
    </el-table>
    <!-- 子阵月报表的表格 -->
    <el-table :data="myData" border style="width: 100%;" v-if="myTableType === 5" :max-height="maxHeight">
      <el-table-column
        prop="devAlias"
        :label="$t('comAna.devName')"
        fixed="left"
        width="180">
        <template slot-scope="{row, $index}">
          {{row.devAlias}}
        </template>
      </el-table-column>
      <el-table-column
        prop="pvCapacity"
        :label="$t('comAna.stringCap')"
        width="180">
      </el-table-column>
      <el-table-column
        prop="matrixName"
        :label="$t('comAna.subarrayName')"
        width="180">
      </el-table-column>
      <el-table-column
        prop="hidPvNum"
        :label="$t('comAna.zdzcs') + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
      <el-table-column
        prop="hidLostPower"
        :label="$t('comAna.lossTypeArr')[0] + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
      <el-table-column
        prop="ineffPvNum"
        :label="$t('comAna.dxzcs') + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
      <el-table-column
        prop="ineffLostPower"
        :label="$t('comAna.lossTypeArr')[1] + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
      <el-table-column
        prop="troublePvNum"
        :label="$t('comAna.gzzcs') + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
      <el-table-column
        prop="troubleLostPower"
        :label="$t('comAna.lossTypeArr')[2] + '(' + $t('comAna.ge') + ')'">
      </el-table-column>
    </el-table>
  </div>
</template>

<script type="text/ecmascript-6">
  const props = {
    myData: [],
    // 0: 电站月报表的表格， 1：电站年报表的表格， 2：子阵年报表的表格， 3：电站日报表的表格，4：子阵日报表的表格， 5：子阵月表报的表格
    myTableType: {
      type: Number,
      default: 0,
    },
    pvList: [],
  }

  export default {
    props: props,
    components: {},
    data () {
      return {
        maxHeight: 120
      }
    },
    filters: {},
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
      handleMatrixNameClick (data) {
        this.$emit('handleMatrixNameClick', data)
      },
      calcMaxHeight () {
        let height = Math.floor(document.documentElement.clientHeight - 280 - 320)
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      }
    },
    watch: {
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
