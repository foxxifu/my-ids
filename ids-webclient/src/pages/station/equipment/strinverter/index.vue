<template>
  <div style="height: 100%">
    <i class="el-icon-back" style="cursor: pointer;margin-bottom: 5px;" @click="returnBack">{{$t('station.equipment.return')}}</i>
      <el-tabs @tab-click="tbClickHandle" type="border-card" tab-position="right" style="height: calc(100% - 35px); width: 100%;" id="equipments">
        <el-tab-pane :label="$t('station.equipment.devMonitor')" style="">
          <div style="display: flex;position: relative;margin-bottom: 20px;">
            <div style="flex: 0;">
              <img src="/assets/images/station/modules.png"/>
              <div style="text-align: center;">{{$t('station.equipment.module')}}</div><!-- 光伏板 -->
            </div>
            <div style="flex: 2.6;padding-top: 24px;">
              <div>
                <div style="display: inline-block;width: 11%">
                  <span v-html="inputData[0][0].text"></span>
                </div>
                <div v-for="(item, index) in inputData[0].slice(1)" v-if="capLength > 0" :style="{display: 'inline-block', width: 88/capLength + '%', 'text-align': 'center'}">
                  <span v-html="item.text"></span>
                </div>
                <div v-if="capLength === 0" style="display: inline-block;width: 88%; text-align: center">
                  <span>PV1</span>
                </div>
              </div>
              <div style="margin-top: 10px;">
                <div style="display: inline-block;width: 11%">
                  <span v-html="inputData[1][0].text"></span>
                </div>
                <div v-for="(item, index) in inputData[1].slice(1)" v-if="capLength > 0" :style="{display: 'inline-block', width: 88/capLength + '%', 'text-align': 'center'}">
                  <span v-html="item.text"></span>
                </div>
                <div v-if="capLength === 0" style="display: inline-block;width: 88%; text-align: center">
                  <span>{{ inputData && inputData[1] &&  inputData[1][1] && inputData[1][1].text || '-' }}</span>
                </div>
              </div>
              <div style="margin-top: 20px;">
                <div style="display: inline-block;width: 11%">
                  <span v-html="inputData[2][0].text"></span>
                </div>
                <div v-for="(item, index) in inputData[2].slice(1)" v-if="capLength > 0" :style="{display: 'inline-block', width: 88/capLength + '%', 'text-align': 'center'}">
                  <span v-html="item.text"></span>
                </div>
                <div v-if="capLength === 0" style="display: inline-block;width: 88%; text-align: center">
                  <span>{{ inputData && inputData[2] && inputData[2][1] && inputData[2][1].text || '-' }}</span>
                </div>
              </div>
              <!--<el-row style="text-align: center">
                <el-col v-for="(item, index) in inputData[0]" v-if="inputData[0].length <= 21 || index !== 0" :span="item.span" :key="index">
                  <span v-html="item.text"></span>
                </el-col>
              </el-row>-->

              <!--<el-row style="text-align: center" :title="$t('station.equipment.strinverter.inputU')">
                <el-col v-for="(item, index) in inputData[1]" v-if="inputData[0].length <= 21 || index !== 0" :span="item.span" :key="index">
                  <span>{{item.text}}</span>
                </el-col>
              </el-row>

              <el-row style="text-align: center;margin-top: 25px;" :title="$t('station.equipment.strinverter.inputI')">
                <el-col v-for="(item, index) in inputData[2]" v-if="inputData[0].length <= 21 || index !== 0" :span="item.span" :key="index">
                  <span>{{item.text}}</span>
                </el-col>
              </el-row>-->
            </div>
            <div style="flex: 0; z-index: 7">
              <img src="/assets/images/station/inverter.png"/>
              <div style="text-align: center;">{{$t('station.equipment.inverter')}}</div><!-- 逆变器 -->
            </div>
            <div style="flex: 1;padding-top: 24px;">
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
              <div style="text-align: center;">{{$t('station.equipment.grid')}}</div><!-- 电网 -->
            </div>
            <i class="dev-power-arraw arraw-running"></i>
          </div>

          <el-row>
            <el-col :span="20">
              <el-table :data="devStatusTableData" :size="elementSize" ref="" border
                        :show-header="false" :highlight-current-row="false"
                        :cell-style="devStatusCellStyle">

                <el-table-column prop="kpiName1" label="kpi名称" align="center" class-name="kpiName"></el-table-column>
                <el-table-column prop="kpiValue1" label="kpi值" align="right" class-name="kpiName"></el-table-column>
                <el-table-column prop="kpiName2" label="kpi名称" align="center" class-name="kpiName"></el-table-column>
                <el-table-column prop="kpiValue2" label="kpi值" align="right" class-name="kpiName"></el-table-column>
                <!-- <el-table-column prop="kpiName3" label="kpi名称" align="center" class-name="kpiName"></el-table-column>
                <el-table-column prop="kpiValue3" label="kpi值" align="right" class-name="kpiName"></el-table-column> -->

              </el-table>
            </el-col>
            <el-col :span="4">
            </el-col>
          </el-row>

        </el-tab-pane>

        <el-tab-pane :label="$t('station.equipment.devInfo')"><!-- 设备信息 -->
          <el-table :data="devInfoTableData" :size="elementSize" ref="devInfoTableData" border :show-header="false"
                    :highlight-current-row="false"
                    :cell-style="devInfoCellStyle">

            <el-table-column prop="kpiName1" label="kpi名称" align="center" class-name="kpiName">
            </el-table-column>

            <el-table-column prop="kpiValue1" label="kpi值" align="right" class-name="kpiName">
            </el-table-column>

            <el-table-column prop="kpiName2" label="kpi名称" align="center" class-name="kpiName">
            </el-table-column>

            <el-table-column prop="kpiValue2" label="kpi值" align="right" class-name="kpiName">
            </el-table-column>

            <el-table-column prop="kpiName3" label="kpi名称" align="center" class-name="kpiName">
            </el-table-column>

            <el-table-column prop="kpiValue3" label="kpi值" align="right" class-name="kpiName">
            </el-table-column>

          </el-table>
          <br/>
          <h3>{{$t('station.equipment.strinverter.detailInfo')}}</h3><!-- 逆变器所接组串容量 -->
          <br/>
          <el-table :data="capacityOfStringTableData" :size="elementSize" ref="capacityOfStringTable" border>

            <el-table-column prop="pvCode" :label="$t('station.equipment.strinverter.headerName')[0]" align="center">
            </el-table-column><!-- 组串编号 -->

            <el-table-column prop="pvCapacity" :label="$t('station.equipment.strinverter.headerName')[1]" align="center">
              <template slot-scope="scope">
                <span>{{scope.row.pvCapacity && scope.row.pvCapacity * 1000}}</span>
              </template>
            </el-table-column><!-- 容量(w) -->

            <el-table-column prop="unitNum" :label="$t('station.equipment.strinverter.headerName')[2]" align="center">
            </el-table-column><!-- 组件数(块) -->

            <el-table-column prop="manufacturer" :label="$t('station.equipment.strinverter.headerName')[3]" align="center">
            </el-table-column><!-- 组件厂家 -->

            <el-table-column prop="moduleVersion" :label="$t('station.equipment.strinverter.headerName')[4]" align="center">
            </el-table-column><!-- 组件型号 -->

            <el-table-column prop="moduleType" :label="$t('station.equipment.strinverter.headerName')[5]" align="center" :formatter="formatterModuleType">
            </el-table-column><!-- 组件类型 -->
          </el-table>
        </el-tab-pane>

        <el-tab-pane :label="$t('station.equipment.devAlarm')"><!-- 设备关联告警 -->
          <DevAlarm v-bind:devId="transferedData.devId"></DevAlarm>
        </el-tab-pane>
        <el-tab-pane :label="$t('station.equipment.command')"><!-- 设备命令下发 -->
          <el-form ref="devCommand" :model="devCommand" label-width="300px" label-position="center">
            <el-form-item :label="$t('station.equipment.strinverter.presentDev')">
              <div>{{devStatusTable.name}}</div>
            </el-form-item>
            <el-form-item :label="$t('station.equipment.strinverter.devCmd')">
              <el-input v-model="devCommand.orderCommand" style="width: 500px" :disabled="sentable"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"  @click="sendDevCmd" :disabled="sentable">{{$t('station.equipment.strinverter.sendCmd')}}</el-button>
            </el-form-item>
            <el-form-item>
              <el-input type="textarea" style="width: 800px" :autosize="{minRows: 2,maxRows:10}" v-model="hisOrder" :disabled="true"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <el-dialog title="告警修复建议" :visible.sync="isShowRepairRecommendation">

        <el-form :model="repairRecommendationForm" ref="repairRecommendationForm" :size="elementSize">

          <el-table :data="repairRecommendationTableData" :size="elementSize" ref="repairRecommendationTableData" border
                    :show-header="false"
                    :highlight-current-row="false" :span-method="arraySpanMethod"
                    :cell-style="repairRecommendationCellStyle">

            <el-table-column prop="kpiName1" label="kpi名称" align="center">
            </el-table-column>

            <el-table-column prop="kpiValue1" label="kpi值" align="left">
            </el-table-column>

            <el-table-column prop="kpiName2" label="kpi名称" align="center">
            </el-table-column>

            <el-table-column prop="kpiValue2" label="kpi值" align="left">
            </el-table-column>
          </el-table>

          <div class="dialog-footer" style="text-align: center;margin-top: 10px">
            <el-button type="primary" @click="isShowRepairRecommendation = false">关闭</el-button>
          </div>
        </el-form>
      </el-dialog>
  </div>
</template>
<style lang='less' src='../index.less'></style>
<script src='./index.js'></script>

