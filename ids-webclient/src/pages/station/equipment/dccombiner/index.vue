<template>
  <div style="height: 100%">
    <i class="el-icon-back" style="cursor: pointer;margin-bottom: 5px;" @click="returnBack">{{$t('station.equipment.return')}}</i> <!-- 返回 -->
      <el-tabs type="border-card" tab-position="right" style="height: calc(100% - 35px); width: 100%;" id="equipments">
        <el-tab-pane :label="$t('station.equipment.devMonitor')" style=""> <!-- 设备监控 -->
          <!--<el-row style="margin: 30px 0px 60px 0px;">
            <el-col :span="24">
              <div style="position: relative;width: 100%;height:130px;">
                <div>
                  <i class="dev-power-state dev-power-modules">
                    <div class="dev-power-inner-text">光伏组件</div>
                  </i>
                  <i class="dev-power-state dev-power-dccombiner">
                    <div class="dev-power-inner-text">直流汇流箱</div>
                    &lt;!&ndash;<el-switch v-model="isTurnOn" active-color="#13ce66" inactive-color="#ff4949" style="top: 161px;left: 80px;">
                    </el-switch>&ndash;&gt;
                  </i>
                  <i class="dev-power-state dev-power-concinverter" style="left: 85%">
                    <div class="dev-power-inner-text">集中式逆变器</div>
                  </i>
                  <i class="dev-power-arraw arraw-running"></i>
                </div>
                <div>
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

          <div style="display: flex;position: relative;margin-bottom: 20px;">
            <div style="flex: 0;">
              <img src="/assets/images/station/modules.png"/>
              <div style="text-align: center;">{{$t('station.equipment.string')}}</div><!-- 光伏组件 -->
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
              <img src="/assets/images/station/dccombiner.png"/>
              <div style="text-align: center;">{{$t('station.equipment.devDccombiner')}}</div><!-- 汇流箱 -->
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
              <img src="/assets/images/station/concinverter.png" style="">
              <div style="text-align: center;">{{$t('station.equipment.devConcinverter')}}</div><!-- 集中式逆变器 -->
            </div>
            <i class="dev-power-arraw arraw-running"></i>
          </div>

          <el-row>
            <el-col :span="20">
              <!-- <el-table :data="devStatusTableData" :size="elementSize" ref="devStatusTableData" border
                        :show-header="false" :highlight-current-row="false"
                        :cell-style="devStatusCellStyle">

                <el-table-column prop="kpiName1" label="kpi名称" align="center" class-name="kpiName">
                </el-table-column>

                <el-table-column prop="kpiValue1" label="kpi值" align="right" class-name="kpiName">
                </el-table-column>

                <el-table-column prop="kpiName2" label="kpi名称" align="center" class-name="kpiName">
                </el-table-column>

                <el-table-column prop="kpiValue2" label="kpi值" align="right" class-name="kpiName">
                </el-table-column>

              </el-table> -->
			  <el-table :data="[{},{},{},{},{},{}]" :size="elementSize" ref="devStatusTableData" border
                          :show-header="false" :highlight-current-row="false"
                          :cell-style="devStatusCellStyle" :maxHeight="devDataMaxHeight">

                  <el-table-column prop="kpiName1" align="center" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span>汇流箱名称</span></div> -->
                      <div v-if="scope.$index === 0"><span></span>{{$t('station.equipment.dccombiner.devStatusInfo')[0]}}</div><!-- 第1路电流值(A) -->
                      <div v-else-if="scope.$index === 1"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[1]}}</span></div><!-- 第2路电流值(A) -->
                      <div v-else-if="scope.$index === 2"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[2]}}</span></div><!-- 第3路电流值(A) -->
                      <div v-else-if="scope.$index === 3"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[3]}}</span></div><!-- 第4路电流值(A) -->
                      <div v-else-if="scope.$index === 4"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[4]}}</span></div><!-- 第5路电流值(A) -->
                      <div v-else-if="scope.$index === 5"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[5]}}</span></div><!-- 第6路电流值(A) -->
                    </template>
                  </el-table-column>

                  <el-table-column prop="kpiValue1" label="kpi值" align="right" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span>{{devName}}</span></div> -->
                      <div v-if="scope.$index === 0"><span>{{devStatusTableData.dc_i1}}</span></div>
                      <div v-else-if="scope.$index === 1"><span>{{devStatusTableData.dc_i2}}</span></div>
                      <div v-else-if="scope.$index === 2"><span>{{devStatusTableData.dc_i3}}</span></div>
                      <div v-else-if="scope.$index === 3"><span>{{devStatusTableData.dc_i4}}</span></div>
                      <div v-else-if="scope.$index === 4"><span>{{devStatusTableData.dc_i5}}</span></div>
                      <div v-else-if="scope.$index === 5"><span>{{devStatusTableData.dc_i6}}</span></div>
                    </template>
                  </el-table-column>

                  <el-table-column prop="kpiName2" label="kpi名称" align="center" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span>&nbsp;</span></div> -->
                      <div v-if="scope.$index === 0"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[6]}}</span></div><!-- 第7路电流值(A) -->
                      <div v-else-if="scope.$index === 1"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[7]}}</span></div><!-- 第8路电流值(A) -->
                      <div v-else-if="scope.$index === 2"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[8]}}</span></div><!-- 第9路电流值(A) -->
                      <div v-else-if="scope.$index === 3"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[9]}}</span></div><!-- 第10路电流值(A) -->
                      <div v-else-if="scope.$index === 4"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[10]}}</span></div><!-- 第11路电流值(A) -->
                      <div v-else-if="scope.$index === 5"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[11]}}</span></div><!-- 第12路电流值(A) -->
                    </template>
                  </el-table-column>

                  <el-table-column prop="kpiValue2" label="kpi值" align="right" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span></span></div> -->
                      <div v-if="scope.$index === 0"><span>{{devStatusTableData.dc_i7}}</span></div>
                      <div v-else-if="scope.$index === 1"><span>{{devStatusTableData.dc_i8}}</span></div>
                      <div v-else-if="scope.$index === 2"><span>{{devStatusTableData.dc_i9}}</span></div>
                      <div v-else-if="scope.$index === 3"><span>{{devStatusTableData.dc_i10}}</span></div>
                      <div v-else-if="scope.$index === 4"><span>{{devStatusTableData.dc_i11}}</span></div>
                      <div v-else-if="scope.$index === 5"><span>{{devStatusTableData.dc_i12}}</span></div>
                    </template>
                  </el-table-column>

                  <el-table-column prop="kpiName3" label="kpi名称" align="center" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span>&nbsp;</span></div> -->
                      <div v-if="scope.$index === 0"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[12]}}</span></div><!-- 第13路电流值(A) -->
                      <div v-else-if="scope.$index === 1"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[13]}}</span></div><!-- 第14路电流值(A) -->
                      <div v-else-if="scope.$index === 2"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[14]}}</span></div><!-- 第15路电流值(A) -->
                      <div v-else-if="scope.$index === 3"><span>{{$t('station.equipment.dccombiner.devStatusInfo')[15]}}</span></div><!-- 第16路电流值(A) -->
                      <div v-else-if="scope.$index === 4"><span>&nbsp;</span></div>
                      <div v-else-if="scope.$index === 5"><span>&nbsp;</span></div>
                    </template>
                  </el-table-column>

                  <el-table-column prop="kpiValue3" label="kpi值" align="right" class-name="kpiName">
                    <template slot-scope="scope">
                      <!-- <div v-if="scope.$index === 0"><span></span></div> -->
                      <div v-if="scope.$index === 0"><span>{{devStatusTableData.dc_i13}}</span></div>
                      <div v-else-if="scope.$index === 1"><span>{{devStatusTableData.dc_i14}}</span></div>
                      <div v-else-if="scope.$index === 2"><span>{{devStatusTableData.dc_i15}}</span></div>
                      <div v-else-if="scope.$index === 3"><span>{{devStatusTableData.dc_i16}}</span></div>
                      <div v-else-if="scope.$index === 5"><span></span></div>
                      <div v-else-if="scope.$index === 6"><span></span></div>
                    </template>
                  </el-table-column>

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
          <h3>{{$t('station.equipment.dccombiner.detailInfo')}}</h3><!-- 接入组串信息 -->
          <br/>
          <el-table :data="capacityOfStringTableData" :size="elementSize" ref="capacityOfStringTable" border>

            <el-table-column prop="pvCode" :label="$t('station.equipment.dccombiner.headerName')[0]" align="center">
            </el-table-column><!-- 组串编号 -->

            <el-table-column prop="pvCapacity" :label="$t('station.equipment.dccombiner.headerName')[1]" align="center">
            </el-table-column><!-- 容量(w) -->

            <el-table-column prop="unitNum" :label="$t('station.equipment.dccombiner.headerName')[2]" align="center">
            </el-table-column><!-- 组件数(块) -->

            <el-table-column prop="manufacturer" :label="$t('station.equipment.dccombiner.headerName')[3]" align="center">
            </el-table-column><!-- 组件厂家 -->

            <el-table-column prop="moduleVersion" :label="$t('station.equipment.dccombiner.headerName')[4]" align="center">
            </el-table-column><!-- 组件型号 -->

            <el-table-column prop="moduleType" :label="$t('station.equipment.dccombiner.headerName')[5]" align="center" :formatter="formatterModuleType">
            </el-table-column><!-- 组件类型 -->
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

