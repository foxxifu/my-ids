<template>
  <div style="height: 100%">
    <i class="el-icon-back" style="cursor: pointer;margin-bottom: 5px;" @click="returnBack">{{$t('station.equipment.return')}}</i><!-- 返回 -->
    <el-tabs type="border-card" tab-position="right" style="height: calc(100% - 35px); width: 100%;" id="equipments">
      <el-tab-pane :label="$t('station.equipment.devMonitor')"><!-- 设备监控 -->
        <el-row style="margin: 30px 0px 60px 0px;">
          <el-col :span="4">
            <!--<div class="dev-power-electricMeter"></div>
            <div class="dev_name">环境监测仪</div>-->

            <div style="text-align: center">
              <img src="/assets/images/station/emi.png">
              <div>{{$t('station.equipment.devEmi')}}</div><!-- 环境监测仪 -->
            </div>
          </el-col>
          <el-col :span="20">
            <el-table :data="devStatusTableData" ref="devStatusTableData" border :show-header="false" :highlight-current-row="false"
                      :cell-style="devStatusCellStyle" >
              <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
              <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
              <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
              <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane :label="$t('station.equipment.devInfo')"><!-- 设备信息 -->
        <el-table :data="devInfoTableData" :size="elementSize" ref="devInfoTableData" border :show-header="false"
                  :highlight-current-row="false"
                  :cell-style="devInfoCellStyle">
          <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiName3" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue3" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane :label="$t('station.equipment.devAlarm')"><!-- 设备关联告警 -->
        <DevAlarm v-bind:devId="transferedData.devId"></DevAlarm>
      </el-tab-pane>

    </el-tabs>

    <el-dialog :title="$t('station.alarmDetailTitle')" :visible.sync="isShowRepairRecommendation">

      <el-form :model="repairRecommendationForm" ref="repairRecommendationForm" :size="elementSize">
        <el-table :data="repairRecommendationTableData" :size="elementSize" ref="repairRecommendationTableData" border
                  :show-header="false"
                  :highlight-current-row="false" :span-method="arraySpanMethod"
                  :cell-style="repairRecommendationCellStyle">
          <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center"></el-table-column>
          <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="left"></el-table-column>
          <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center"></el-table-column>
          <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="left"></el-table-column>
        </el-table>

        <div class="dialog-footer" style="text-align: center;margin-top: 10px">
          <el-button type="primary" @click="isShowRepairRecommendation = false">{{$t('close')}}</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<style lang='less' src='./index.less'></style>
<script src='./index.js'></script>

