<template>
  <el-container v-if="isShowDev">
    <el-aside width="70px">
      <!--<div class="legends_1">
        <div class="legend1 one">
          <div class="status"></div>
          <span>汇流箱</span>
        </div>
        <div class="legend1 two">
          <div class="status"></div>
          <span>逆变器</span>
        </div>
        <div class="legend1 three">
          <div class="status"></div>
          <span>箱变</span>
        </div>
      </div>-->

      <div class="legends_2">
        <div class="legend2">
          <div class="normal"></div>
          <span class="desc">{{$t("station.equipment.devStatus")[2]}}</span></div>
        <div class="legend2">
          <div class="fault"></div>
          <span class="desc">{{$t("station.equipment.devStatus")[0]}}</span></div>
        <div class="legend2">
          <div class="disconnection"></div>
          <span class="desc">{{$t("station.equipment.devStatus")[1]}}</span></div>
      </div>
    </el-aside>
    <el-main style="padding: 0px;">
      <el-form ref="devManageSearchForm" :model="devManageSearchForm" :inline="true"
               style="" :size="elementSize">
        <!--过滤项-->
        <el-form-item :label="$t('station.equipment.filterName')">
        </el-form-item>

        <template v-for="(item, index) in deviceTypes">
          <el-form-item label="">
            <el-radio v-model="searchType" :label="item.id" border @change="getDevRunStatus">{{item.name + ":" + item.num}}</el-radio>
          </el-form-item>
        </template>
      </el-form>
      <!-- 生产设备 -->
      <el-table :data="devManageTableData" :size="elementSize" ref="devManageTableData" border :show-header="false"
                :highlight-current-row="false"
                :cell-style="cellStyle" >

        <el-table-column prop="devName1" :label="$t('station.equipment.devName')" align="center" class-name="devName">
          <template slot-scope="scope">
            <div v-if="scope.row.devName1" @click="showDevDetail(scope.$index, scope.row, scope,'1')" style="cursor: pointer">
              <div :style="iconStyle(scope.$index, scope.row, '1')" class="devStatus"></div>
              {{scope.row.devName1}}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="devPower1" :label="$t('station.devPow')" align="right" class-name="devPower">
          <template slot-scope="scope" v-if="scope.row.devPower1">
            {{getSignaData(scope.row.devPower1)}}
          </template>
        </el-table-column>

        <el-table-column prop="devName2" :label="$t('station.equipment.devName')" align="center" class-name="devName">
          <template slot-scope="scope">
            <div v-if="scope.row.devName2" @click="showDevDetail(scope.$index, scope.row, scope,'2')" style="cursor: pointer">
              <div :style="iconStyle(scope.$index, scope.row, '2')" class="devStatus"></div>
              {{scope.row.devName2}}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="devPower2" :label="$t('station.devPow')" align="right" class-name="devPower">
          <template slot-scope="scope" v-if="scope.row.devPower2">
            {{getSignaData(scope.row.devPower2)}}
          </template>
        </el-table-column>

        <el-table-column prop="devName3" :label="$t('station.equipment.devName')" align="center" class-name="devName">
          <template slot-scope="scope">
            <div v-if="scope.row.devName3" @click="showDevDetail(scope.$index, scope.row, scope, '3')" style="cursor: pointer">
              <div :style="iconStyle(scope.$index, scope.row, '3')" class="devStatus"></div>
              {{scope.row.devName3}}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="devPower3" :label="$t('station.devPow')" align="right" class-name="devPower">
          <template slot-scope="scope" v-if="scope.row.devPower3">
            {{getSignaData(scope.row.devPower3)}}
          </template>
        </el-table-column>

        <el-table-column prop="devName4" :label="$t('station.equipment.devName')" align="center" class-name="devName">
          <template slot-scope="scope">
            <div v-if="scope.row.devName4" @click="showDevDetail(scope.$index, scope.row, scope, '4')" style="cursor: pointer">
              <div :style="iconStyle(scope.$index, scope.row, '4')" class="devStatus"></div>
              {{scope.row.devName4}}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="devPower4" :label="$t('station.devPow')" align="right" class-name="devPower">
          <template slot-scope="scope" v-if="scope.row.devPower4">
            {{getSignaData(scope.row.devPower4)}}
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>

  <!-- 设备详情（设备监控、设备信息、关联告警） -->
  <el-container v-else style="height: 100%;">
    <el-main>
      <strinverter v-if="searchType == 1" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <concinverter v-else-if="searchType == 14" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <meterdev v-else-if="[9,17,18,19,21].indexOf(searchType) >= 0" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <emidev v-else-if="searchType == 10" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <dccombiner v-else-if="searchType == 15" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <boxdev v-else-if="searchType == 8" v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"/>
      <emptydev v-else v-bind:transferedData="transferedData" v-on:listenChildEvent="showMain"></emptydev>
    </el-main>
  </el-container>
</template>
<style lang='less' src='./index.less'></style>
<script src='./index.js'></script>
