<template>
  <el-tabs v-model="reportTabName" @tab-click="tabClick" id="reportManage" type="border-card" tab-position="right" style="height: 100%; width: 100%;">
    <el-tab-pane :label="$t('pm.stationRunPm')" name="stationRunning">
      <el-form ref="stationReportSearchForm" :model="stationReportSearchForm" :inline="true"
               style="" :size="elementSize">

        <el-form-item :label="$t('pm.chooseStation')">
          <StationInput
            :myValues="stationReportSearchForm.stationCodes"
            :myTexts="stationReportSearchForm.stationName"
            @station-change="stationChange"
            :isMutiSelect="true"></StationInput>
        </el-form-item>

        <el-form-item :label="$t('pm.timeDim')">
          <el-select v-model="stationReportSearchForm.timeType" :placeholder="$t('pm.pSelect')" style="width:100px">
            <el-option v-for="item in timeTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="stationReportSearchForm.timeType == 1">
          <el-date-picker v-model="stationReportSearchForm.dayTime" type="date" value-format="timestamp"
                          :format="$t('dateFormat.yyyymmdd')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="stationReportSearchForm.timeType == 2">
          <el-date-picker v-model="stationReportSearchForm.monthTime" type="month" value-format="timestamp"
                          :format="$t('dateFormat.yyyymm')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="stationReportSearchForm.timeType == 3">
          <el-date-picker v-model="stationReportSearchForm.yearTime" type="year" value-format="timestamp"
                          :format="$t('dateFormat.yyyy')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchStationReport">{{$t('search')}}</el-button>
        </el-form-item>

      </el-form>

      <div ref="stationRunningChart" style="width:100%;height: 200px;"></div>

      <el-row style="line-height: 32px;margin-bottom: 5px;">
        <el-col :span="24">
          <div style="float: left"> {{ stationTableName }}</div>
          <div style="float: right">
            <el-button type="primary" :size="elementSize" @click="exportStationReport">{{$t('export')}}</el-button>
          </div>
        </el-col>
      </el-row>

      <el-table :data="stationReport.list" :size="elementSize" ref="stationRunningTableData" border :max-height="stationReport.maxHeight">

        <el-table-column prop="stationName" :label="$t('pm.plantName')" align="center">
        </el-table-column>

        <el-table-column prop="collectTime" :label="$t('pm.colTime')" :formatter="collectTimeFormatter">
        </el-table-column>

        <el-table-column width="210px" prop="radiationIntensity" :label="$t('pm.radia') + '(MJ/㎡)'" align="center">
        </el-table-column>

        <el-table-column  v-if="stationReport.isDayAndSingleStation" prop="theoryPower" :label="$t('pm.theoryPower') + '(kWh)'" align="center">
        </el-table-column>

        <el-table-column width="250px" v-if="!stationReport.isDayAndSingleStation" prop="equivalentHour" :label="$t('pm.equivalentHour') + '(h)'" align="center">
        </el-table-column>

        <el-table-column v-if="!stationReport.isDayAndSingleStation" prop="pr" :label="$t('pm.pr') + '(%)'" align="center">
        </el-table-column>

        <el-table-column prop="productPower" :label="$t('pm.productPower') + '(kWh)'" align="center">
        </el-table-column>

        <el-table-column v-if="!stationReport.isDayAndSingleStation" prop="ongridPower" :label="$t('pm.ongridPower') + '(kWh)'" align="center">
        </el-table-column>

        <el-table-column width="220px" v-if="!stationReport.isDayAndSingleStation" prop="selfUsePower" :label="$t('pm.selfUsePower') + '(kWh)'" align="center">
        </el-table-column>

        <el-table-column prop="inCome" :label="$t('pm.inCome') + $t('unit.brackets')[0] + $t('unit.RMBUnit') + $t('unit.brackets')[1]" align="center">
        </el-table-column>

      </el-table>

      <el-pagination :current-page="stationReport.index" :page-sizes="stationReport.pageSizes" :page-size="stationReport.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="stationReport.count" style="float: right; margin-top: 5px;"
                     @size-change="val => {stationReport.pageSize = val; searchStationReport()}" @current-change="val => {stationReport.index = val; searchStationReport()}">
      </el-pagination>

    </el-tab-pane>

    <el-tab-pane :label="$t('pm.inventerPm')" name="inverter">

      <el-form ref="inverterReportSearchForm" :model="inverterReportSearchForm" :inline="true"
               style="" :size="elementSize">

        <el-form-item :label="$t('pm.chooseStation')">
          <StationInput
            :myValues="inverterReportSearchForm.stationCodes"
            :myTexts="inverterReportSearchForm.stationName"
            @station-change="inverterStationChange"
            :isMutiSelect="true"></StationInput>
        </el-form-item>

        <el-form-item :label="$t('pm.timeDim')">
          <el-select v-model="inverterReportSearchForm.timeType" placeholder="请选择" style="width:100px">
            <el-option v-for="item in timeTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="inverterReportSearchForm.timeType == 1">
          <el-date-picker v-model="inverterReportSearchForm.dayTime" type="date" value-format="timestamp"
                          :format="$t('dateFormat.yyyymmdd')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="inverterReportSearchForm.timeType == 2">
          <el-date-picker v-model="inverterReportSearchForm.monthTime" type="month" value-format="timestamp"
                          :format="$t('dateFormat.yyyymm')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('pm.selectTime')" v-if="inverterReportSearchForm.timeType == 3">
          <el-date-picker v-model="inverterReportSearchForm.yearTime" type="year" value-format="timestamp"
                          :format="$t('dateFormat.yyyy')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchInverterReport">{{$t('search')}}</el-button>
        </el-form-item>

      </el-form>

      <div ref="inverterChart" style="width:100%;height: 200px;"></div>

      <el-row style="line-height: 32px;margin-bottom: 5px;">
        <el-col :span="24">
          <div style="float: left"> {{ inverterTableName }}</div>
          <div style="float: right">
            <el-button type="primary" :size="elementSize" @click="exportInverterReport">{{$t('export')}}</el-button>
          </div>
        </el-col>
      </el-row>

      <el-table :data="inverterReport.list" :size="elementSize" ref="inverterTableData" border :max-height="inverterReport.maxHeight">

        <el-table-column prop="stationName" :label="$t('pm.plantName')" align="center">
        </el-table-column>

        <el-table-column prop="devName" :label="$t('pm.devName')" align="center">
        </el-table-column>

        <el-table-column prop="inverterType" :label="$t('pm.devType')" align="center" :formatter="formatterDevType">
          <template slot-scope="{row}">
            <span v-text="$t('devTypeId.' + row.inverterType)"></span>
          </template>
        </el-table-column>

        <el-table-column prop="inverterPower" :label="$t('pm.productPower') + '(kWh)'" align="center">
        </el-table-column>

        <el-table-column prop="efficiency" :label="$t('pm.efficiency') + '(%)'" align="center">
        </el-table-column>

        <el-table-column prop="peakPower" :label="$t('pm.peakPower') + '(kW)'" align="center">
        </el-table-column>

        <el-table-column v-if="!inverterReport.isDayTime" prop="aopRatio" :label="$t('pm.aopRatio')" align="center">
        </el-table-column>

        <el-table-column v-if="!inverterReport.isDayTime" prop="powerDeviation" :label="$t('pm.powerDeviation')" align="center">
        </el-table-column>

        <el-table-column width="210px" v-if="!inverterReport.isDayTime" prop="aocRatio" :label="$t('pm.aocRatio')" align="center">
        </el-table-column>

      </el-table>
      <el-pagination :current-page="inverterReport.index" :page-sizes="inverterReport.pageSizes" :page-size="inverterReport.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="inverterReport.count" style="float: right; margin-top: 5px;"
                     @size-change="val => {inverterReport.pageSize = val; searchInverterReport()}" @current-change="val => {inverterReport.index = val; searchInverterReport()}">
      </el-pagination>

    </el-tab-pane>
  </el-tabs>
</template>

<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
