<template>
  <el-container id="statisticsOfManufacturer">
    <el-header style="height: auto">
      <el-form ref="statisticsSearchForm" :model="statisticsSearchForm" :inline="true"
               style="height: 40px;" :size="elementSize">
        <el-form-item label="选择时间维度">
          <el-date-picker style="width: 180px" v-model="statisticsSearchForm.time" type="date"
                          placeholder=""></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search">查询</el-button>
        </el-form-item>

      </el-form>
    </el-header>
    <el-main style="margin-top: 10px;padding-top: 0;">

      <el-tabs v-model="inverterInfo" @tab-click="tabClick">
        <el-tab-pane label="逆变器发货量排行" name="shipments" ref="shipmentsTab">
          <div ref="shipments" style="width:100%;height: 200px;" id="shipments"></div>
          <el-row style="line-height: 32px;margin-bottom: 5px;">
            <el-col :span="24">
              <div style="float: left">逆变器发货量统计 - 2017年</div>
              <div style="float: right">
                <el-button type="primary" :size="elementSize">导出</el-button>
              </div>
            </el-col>
          </el-row>

          <el-table :data="shipmentsTableData" :size="elementSize" ref="shipmentsTable" border>

            <el-table-column prop="num" label="编号" align="center">
            </el-table-column>

            <el-table-column prop="epc" label="EPC" align="center">
            </el-table-column>

            <el-table-column prop="location" label="所在地" align="center">
            </el-table-column>

            <el-table-column prop="shipmentsNum" label="总发货量(台)" align="center">
            </el-table-column>

            <el-table-column prop="zcsInverterNum" label="组串式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="jzsInverterNum" label="集中式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="zssInverterNum" label="集散式逆变器(台)" align="center">
            </el-table-column>

          </el-table>

          <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                         layout="total, sizes, prev, pager, next, jumper" :total="3"
                         style="float: right; margin-top: 5px;">
          </el-pagination>

        </el-tab-pane>
        <el-tab-pane label="逆变器并网量排行" name="gridConnected">

          <div ref="gridConnected" style="width:100%;height: 200px;"></div>
          <el-row style="line-height: 32px;margin-bottom: 5px;">
            <el-col :span="24">
              <div style="float: left">逆变器发货量统计 - 2017年</div>
              <div style="float: right">
                <el-button type="primary" :size="elementSize">导出</el-button>
              </div>
            </el-col>
          </el-row>

          <el-table :data="gridConnectedTableData" :size="elementSize" ref="gridConnectedTable" border>

            <el-table-column prop="num" label="编号" align="center">
            </el-table-column>

            <el-table-column prop="epc" label="EPC" align="center">
            </el-table-column>

            <el-table-column prop="location" label="所在地" align="center">
            </el-table-column>

            <el-table-column prop="shipmentsNum" label="并网量(台)" align="center">
            </el-table-column>

            <el-table-column prop="zcsInverterNum" label="组串式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="jzsInverterNum" label="集中式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="zssInverterNum" label="集散式逆变器(台)" align="center">
            </el-table-column>

          </el-table>

          <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                         layout="total, sizes, prev, pager, next, jumper" :total="3"
                         style="float: right; margin-top: 5px;">
          </el-pagination>

        </el-tab-pane>
        <el-tab-pane label="逆变器换机量排行" name="returnMachine">

          <div ref="returnMachine" style="width:100%;height: 200px;"></div>
          <el-row style="line-height: 32px;margin-bottom: 5px;">
            <el-col :span="24">
              <div style="float: left">逆变器发货量统计 - 2017年</div>
              <div style="float: right">
                <el-button type="primary" :size="elementSize">导出</el-button>
              </div>
            </el-col>
          </el-row>

          <el-table :data="returnMachineTableData" :size="elementSize" ref="returnMachineTable" border>

            <el-table-column prop="num" label="编号" align="center">
            </el-table-column>

            <el-table-column prop="epc" label="EPC" align="center">
            </el-table-column>

            <el-table-column prop="location" label="所在地" align="center">
            </el-table-column>

            <el-table-column prop="shipmentsNum" label="换机量(台)" align="center">
            </el-table-column>

            <el-table-column prop="zcsInverterNum" label="组串式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="jzsInverterNum" label="集中式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="zssInverterNum" label="集散式逆变器(台)" align="center">
            </el-table-column>

          </el-table>

          <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                         layout="total, sizes, prev, pager, next, jumper" :total="3"
                         style="float: right; margin-top: 5px;">
          </el-pagination>

        </el-tab-pane>
        <el-tab-pane label="发货量统计" name="consignmentOfInverter">

          <div ref="consignmentOfInverter" style="width:100%;height: 200px;"></div>
          <el-row style="line-height: 32px;margin-bottom: 5px;">
            <el-col :span="24">
              <div style="float: left">逆变器发货量统计 - 2017年</div>
              <div style="float: right">
                <el-button type="primary" :size="elementSize">导出</el-button>
              </div>
            </el-col>
          </el-row>

          <el-table :data="consignmentOfInverterTableData" :size="elementSize" ref="consignmentOfInverterTable" border :max-height="340">

            <el-table-column prop="date" label="日期" width="60" align="center">
            </el-table-column>

            <el-table-column prop="shipmentsNum" label="发货量(台)" align="center">
            </el-table-column>

            <el-table-column prop="zcsInverterNum" label="组串式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="jzsInverterNum" label="集中式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="zssInverterNum" label="集散式逆变器(台)" align="center">
            </el-table-column>

            <el-table-column prop="destination" label="发往地" align="center">
            </el-table-column>
          </el-table>
          <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                         layout="total, sizes, prev, pager, next, jumper" :total="10"
                         style="float: right; margin-top: 5px;">
          </el-pagination>

        </el-tab-pane>
      </el-tabs>

    </el-main>
  </el-container>
</template>
<script src='./index.js'></script>
<style src="./index.less" lang="less" scoped></style>


