<template>
  <div style="height: 100%">
    <i class="el-icon-back" style="cursor: pointer;margin-bottom: 5px;" @click="returnBack">{{$t("station.equipment.return")}}</i> <!-- 返回 -->
    <el-tabs type="border-card" tab-position="right" style="height: calc(100% - 35px); width: 100%;" id="equipments">
      <el-tab-pane :label="$t('station.equipment.devMonitor')" style=""> <!-- 设备监控 -->
        <div style="display: flex;position: relative;margin-bottom: 20px;">
          <div style="flex: 0;">
            <img src="/assets/images/station/modules.png"/>
            <div style="text-align: center;">{{$t('station.equipment.module')}}</div><!-- 光伏板 -->
          </div>
          <div style="flex: 1;padding-top: 24px;">
            <el-row style="text-align: center">
              <el-col v-for="(item, index) in inputData[0]" :span="item.span" :key="index">
                <span v-html="item.text"></span>
              </el-col>
            </el-row>

            <el-row style="text-align: center">
              <el-col v-for="(item, index) in inputData[1]" :span="item.span" :key="index">
                <span>{{item.text}}</span>
              </el-col>
            </el-row>

            <el-row style="text-align: center;margin-top: 25px;">
              <el-col v-for="(item, index) in inputData[2]" :span="item.span" :key="index">
                <span>{{item.text}}</span>
              </el-col>
            </el-row>
          </div>
          <div style="flex: 0; z-index: 7">
            <img src="/assets/images/station/concinverter.png"/>
            <div style="text-align: center;">{{$t('station.equipment.devConcinverter')}}</div><!-- 集中式逆变器 -->
          </div>
          <div style="flex: 1.5;padding-top: 24px;">
            <el-row style="text-align: center">
              <el-col v-for="(item, index) in outputData[0]" :span="item.span" :key="index">
                <span v-html="item.text"></span>
              </el-col>
            </el-row>

            <el-row style="text-align: center">
              <el-col v-for="(item, index) in outputData[1]" :span="item.span" :key="index">
                <span>{{item.text}}</span>
              </el-col>
            </el-row>

            <el-row style="text-align: center;margin-top: 25px;">
              <el-col v-for="(item, index) in outputData[2]" :span="item.span" :key="index">
                <span>{{item.text}}</span>
              </el-col>
            </el-row>
          </div>
          <div style="flex: 0; z-index: 7;">
            <img src="/assets/images/station/grid.png" style="width: 122px;">
            <div style="text-align: center;">{{$t("station.equipment.grid")}}</div><!-- 电网 -->
          </div>
          <i class="dev-power-arraw arraw-running"></i>
        </div>

        <el-row>
          <el-col :span="20">
            <el-table :data="devStatusTableData" :size="elementSize" ref="devStatusTableData" border
                      :show-header="false" :highlight-current-row="false"
                      :cell-style="devStatusCellStyle">

              <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center" class-name="kpiName">
              </el-table-column>

              <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
              </el-table-column>

              <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center" class-name="kpiName">
              </el-table-column>

              <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
              </el-table-column>

            </el-table>
          </el-col>
          <el-col :span="4">
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane :label="$t('station.equipment.devInfo')"> <!-- 设备信息 -->
        <el-table :data="devInfoTableData" :size="elementSize" ref="devInfoTableData" border :show-header="false"
                  :highlight-current-row="false"
                  :cell-style="devInfoCellStyle">

          <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center" class-name="kpiName">
          </el-table-column>

          <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
          </el-table-column>

          <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center" class-name="kpiName">
          </el-table-column>

          <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
          </el-table-column>

          <el-table-column prop="kpiName3" :label="$t('station.kpiName')" align="center" class-name="kpiName">
          </el-table-column>

          <el-table-column prop="kpiValue3" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
          </el-table-column>

        </el-table>
        <br/>
        <h3>逆变器所接直流汇流箱</h3>
        <br/>
        <el-table :data="dcCombinerTableData" ref="dcCombinerTableData" :size="elementSize" border>
          <el-table-column prop="devAlias" :label="$t('station.equipment.concinverter.headerName')[0]" align="center"></el-table-column>
          <el-table-column prop="installedCapacity" :label="$t('station.equipment.concinverter.headerName')[1]" align="center"></el-table-column>
          <el-table-column prop="pvNum" :label="$t('station.equipment.concinverter.headerName')[2]" align="center"></el-table-column>
          <el-table-column prop="venderName" :label="$t('station.equipment.concinverter.headerName')[3]" align="center"></el-table-column>
          <el-table-column prop="signalVersion" :label="$t('station.equipment.concinverter.headerName')[4]" align="center"></el-table-column>
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

          <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center">
          </el-table-column>

          <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="left">
          </el-table-column>

          <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center">
          </el-table-column>

          <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="left">
          </el-table-column>
        </el-table>

        <div class="dialog-footer" style="text-align: center;margin-top: 10px">
          <el-button type="primary" @click="isShowRepairRecommendation = false">{{$t('close')}}</el-button>
        </div>

      </el-form>
    </el-dialog>
  </div>
</template>
<style lang='less' src='../index.less'></style>
<script src='./index.js'></script>

