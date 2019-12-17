<template>
  <div style="height: 100%">
    <i class="el-icon-back" style="cursor: pointer;margin-bottom: 5px;" @click="returnBack">{{$t("station.equipment.return")}}</i> <!-- 返回 -->
    <el-tabs type="border-card" tab-position="right" style="height: calc(100% - 35px); width: 100%;" id="equipments">
      <el-tab-pane :label="$t('station.equipment.devMonitor')"> <!-- 设备监控 -->
        <!--<el-row style="margin: 30px 0px 60px 0px;">
          <el-col :span="24">
            <div style="position: relative;width: 100%;height:130px;">
              <div>
                <i class="dev-power-state dev-power-inverter" style="left: 5px">
                  <div class="dev-power-inner-text">逆变器</div>
                </i>
                <i class="dev-power-state dev-power-box">
                  <div class="dev-power-inner-text" style="left: 30px">箱变</div>
                </i>
                <i class="dev-power-state dev-power-grid">
                  <div class="dev-power-inner-text">电网</div>
                </i>
                <i class="dev-power-arraw arraw-running"></i>
              </div>
              <div>
                <div class="dev-power-div " style="width: 55%;margin-left: 186px;line-height: 25px;top: 7px;">
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

                <div class="dev-power-div " style="width: 17%;margin-left: 72%;line-height: 25px;top: 4px;">
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
              </div>
            </div>
          </el-col>
        </el-row>-->

        <!-- <div style="display: flex;position: relative;margin-bottom: 20px;">
          <div style="flex: 0;">
            <img src="/assets/images/station/inverter.png"/>
            <div style="text-align: center;">逆变器</div>
          </div>
          <div style="flex: 2.6;padding-top: 24px;">
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
            <img src="/assets/images/station/box.png"/>
            <div style="text-align: center;">箱变</div>
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
            <div style="text-align: center;">电网</div>
          </div>
          <i class="dev-power-arraw arraw-running"></i>
        </div> -->

		<div style="padding: 20px 0px;margin-bottom: 20px">
		  <div style="display: flex;position: relative;">
			<div style="flex: 0;padding-left: 20px;">
			  <img src="/assets/images/station/inverter.png"/>
			  <div style="text-align: center;">{{$t('station.equipment.inverter')}}</div> <!-- 逆变器 -->
			</div>
			<div style="flex: 1;padding-top: 24px;">
			</div>
			<div style="flex: 0; z-index: 7">
			  <img src="/assets/images/station/box.png"/>
			  <div style="text-align: center;">{{$t('station.equipment.devBox')}}</div> <!-- 箱变 -->
			</div>
			<div style="flex: 1;padding-top: 24px;">
			</div>
			<div style="flex: 0; z-index: 7;">
			  <img src="/assets/images/station/grid.png" style="width: 122px;">
			  <div style="text-align: center;">{{$t('station.equipment.grid')}}</div> <!-- 电网 -->
			</div>
			<i class="dev-power-arraw arraw-running"></i>
		  </div>
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

			   <el-table-column prop="kpiName3" :label="$t('station.kpiName')" align="center" class-name="kpiName">
              </el-table-column>

              <el-table-column prop="kpiValue3" :label="$t('station.kpiValue')" align="right" class-name="kpiName">
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
          <el-table-column prop="kpiName1" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue1" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiName2" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue2" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiName3" :label="$t('station.kpiName')" align="center" class-name="kpiName"></el-table-column>
          <el-table-column prop="kpiValue3" :label="$t('station.kpiValue')" align="right" class-name="kpiName"></el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane :label="$t('station.equipment.devAlarm')"> <!-- 设备关联告警 -->
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

