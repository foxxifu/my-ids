<template>
  <el-tabs v-model="reportTabName" @tab-click="tabClick" id="reportManage" type="border-card" tab-position="right" style="height: 100%; width: 100%;">
    <el-tab-pane :label="$t('station.report.station.name')" name="stationRunning">
      <el-form ref="stationReportSearchForm" :model="stationReportSearchForm" :inline="true"
               style="" :size="elementSize">

        <el-form-item :label="$t('station.report.timeType')">
          <el-select v-model="stationReportSearchForm.timeType" :placeholder="$t('station.choose')" style="width:100px">
            <el-option v-for="item in timeTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="stationReportSearchForm.timeType == 1">
          <el-date-picker v-model="stationReportSearchForm.dayTime" type="date" value-format="timestamp"
                          :format="$t('dateFormat.yyyymmdd')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="stationReportSearchForm.timeType == 2">
          <el-date-picker v-model="stationReportSearchForm.monthTime" type="month" value-format="timestamp"
                          :format="$t('dateFormat.yyyymm')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="stationReportSearchForm.timeType == 3">
          <el-date-picker v-model="stationReportSearchForm.yearTime" type="year" value-format="timestamp"
                          :format="$t('dateFormat.yyyy')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchStationReport">{{$t('station.report.search')}}</el-button>
        </el-form-item>

      </el-form>

      <div ref="stationRunningChart" style="width:100%;height: 200px;"></div>

      <el-row style="line-height: 32px;margin-bottom: 5px;">
        <el-col :span="24">
          <div style="float: left"> {{ stationTableName }}</div>
          <div style="float: right">
            <el-button type="primary" :size="elementSize" @click="exportStationReport">{{$t('station.report.export')}}</el-button>
          </div>
        </el-col>
      </el-row>

      <el-table :data="stationReport.list" :size="elementSize" ref="stationRunningTableData" border :max-height="stationReport.maxHeight">

        <el-table-column prop="stationName" :label="$t('station.report.station.headerName')[0]" align="center">
        </el-table-column>

        <el-table-column prop="collectTime" :label="$t('station.report.station.headerName')[1]" align="center" :formatter="collectTimeFormatter">
        </el-table-column>

        <el-table-column width="210px" prop="radiationIntensity" :label="$t('station.report.station.headerName')[2]" align="center">
        </el-table-column>

        <el-table-column v-if="stationReport.isDayAndSingleStation" prop="theoryPower" :label="$t('station.report.station.headerName')[3]" align="center">
        </el-table-column>

        <el-table-column width="180px" v-if="!stationReport.isDayAndSingleStation" prop="equivalentHour" :label="$t('station.report.station.headerName')[4]" align="center">
        </el-table-column>

        <el-table-column v-if="!stationReport.isDayAndSingleStation" prop="pr" label="PR(%)" align="center">
        </el-table-column>

        <el-table-column prop="productPower" :label="$t('station.report.station.headerName')[5]" align="center">
        </el-table-column>

        <el-table-column v-if="!stationReport.isDayAndSingleStation" prop="ongridPower" :label="$t('station.report.station.headerName')[6]" align="center">
        </el-table-column>

        <el-table-column width="180px" v-if="!stationReport.isDayAndSingleStation" prop="selfUsePower" :label="$t('station.report.station.headerName')[7]" align="center">
        </el-table-column>

        <el-table-column prop="inCome" :label="$t('station.report.station.headerName')[8]" align="center">
        </el-table-column>

      </el-table>

      <el-pagination :current-page="stationReport.index" :page-sizes="stationReport.pageSizes" :page-size="stationReport.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="stationReport.count" style="float: right; margin-top: 5px;"
                     @size-change="val => {stationReport.pageSize = val; searchStationReport()}" @current-change="val => {stationReport.index = val; searchStationReport()}">
      </el-pagination>

    </el-tab-pane>

    <el-tab-pane :label="$t('station.report.inverter.name')" name="inverter">

      <inverterDetail v-if="isShowInverterDetail" @changeShowInverter="changeShowInverterDetail" :devId="inverterDetailDevId" :dayTime="inverterDetailDayTime"></inverterDetail>
      <div v-show="!isShowInverterDetail">
          <el-form ref="inverterReportSearchForm" :model="inverterReportSearchForm" :inline="true"
                   style="" :size="elementSize">

            <el-form-item :label="$t('station.report.timeType')">
              <el-select v-model="inverterReportSearchForm.timeType" placeholder="请选择" style="width:100px">
                <el-option v-for="item in timeTypes" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item :label="$t('station.report.selectTime')" v-if="inverterReportSearchForm.timeType == 1">
              <el-date-picker v-model="inverterReportSearchForm.dayTime" type="date" value-format="timestamp"
                              :format="$t('dateFormat.yyyymmdd')"
                              placeholder="" style="width:140px"></el-date-picker>
            </el-form-item>

            <el-form-item :label="$t('station.report.selectTime')" v-if="inverterReportSearchForm.timeType == 2">
              <el-date-picker v-model="inverterReportSearchForm.monthTime" type="month" value-format="timestamp"
                              :format="$t('dateFormat.yyyymm')"
                              placeholder="" style="width:140px"></el-date-picker>
            </el-form-item>

            <el-form-item :label="$t('station.report.selectTime')" v-if="inverterReportSearchForm.timeType == 3">
              <el-date-picker v-model="inverterReportSearchForm.yearTime" type="year" value-format="timestamp"
                              :format="$t('dateFormat.yyyy')"
                              placeholder="" style="width:140px"></el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchInverterReport">{{$t('station.report.search')}}</el-button>
            </el-form-item>

          </el-form>

          <div ref="inverterChart" style="width:100%;height: 200px;"></div>

          <el-row style="line-height: 32px;margin-bottom: 5px;">
            <el-col :span="24">
              <div style="float: left"> {{ inverterTableName }}</div>
              <div style="float: right">
                <el-button type="primary" :size="elementSize" @click="exportInverterReport">{{$t('station.report.export')}}</el-button>
              </div>
            </el-col>
          </el-row>

          <el-table :data="inverterReport.list" :size="elementSize" ref="inverterTableData" border :max-height="inverterReport.maxHeight">

            <el-table-column prop="stationName" :label="$t('station.report.inverter.headerName')[0]" align="center">
            </el-table-column>

            <el-table-column prop="devName" :label="$t('station.report.inverter.headerName')[1]" align="center" @click="showInverterDetail">
              <template slot-scope="scope">
                <a v-if="inverterReportSearchForm.timeType == 1" @click="showInverterDetail(scope.row)" style="cursor: pointer;color: #54B8D1;" class="text-color-green">{{scope.row.devName}}</a>
                <div v-else>{{scope.row.devName}}</div>
              </template>
            </el-table-column>

            <el-table-column prop="inverterType" :label="$t('station.report.inverter.headerName')[2]" align="center" :formatter="formatterDevType">
            </el-table-column>

            <el-table-column prop="inverterPower" :label="$t('station.report.inverter.headerName')[3]" align="center">
            </el-table-column>

            <el-table-column prop="efficiency" :label="$t('station.report.inverter.headerName')[4]" align="center">
            </el-table-column>

            <el-table-column prop="peakPower" :label="$t('station.report.inverter.headerName')[5]" align="center">
            </el-table-column>

            <el-table-column v-if="!inverterReport.isDayTime" prop="aopRatio" :label="$t('station.report.inverter.headerName')[6]" align="center">
            </el-table-column>

            <el-table-column v-if="!inverterReport.isDayTime" prop="powerDeviation" :label="$t('station.report.inverter.headerName')[7]" align="center">
            </el-table-column>

            <el-table-column width="210px" v-if="!inverterReport.isDayTime" prop="aocRatio" :label="$t('station.report.inverter.headerName')[8]" align="center">
            </el-table-column>

          </el-table>
          <el-pagination :current-page="inverterReport.index" :page-sizes="inverterReport.pageSizes" :page-size="inverterReport.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="inverterReport.count" style="float: right; margin-top: 5px;"
                         @size-change="val => {inverterReport.pageSize = val; searchInverterReport()}" @current-change="val => {inverterReport.index = val; searchInverterReport()}">
          </el-pagination>
        </div>

    </el-tab-pane>

    <el-tab-pane :label="$t('station.report.subarray.name')" name="subarray">

      <el-form ref="subarrayReportSearchForm" :model="subarrayReportSearchForm" :inline="true"
               style="" :size="elementSize">

        <el-form-item :label="$t('station.report.timeType')">
          <el-select v-model="subarrayReportSearchForm.timeType" placeholder="请选择" style="width:100px">
            <el-option v-for="item in timeTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="subarrayReportSearchForm.timeType == 1">
          <el-date-picker v-model="subarrayReportSearchForm.dayTime" type="date" value-format="timestamp"
                          :format="$t('dateFormat.yyyymmdd')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="subarrayReportSearchForm.timeType == 2">
          <el-date-picker v-model="subarrayReportSearchForm.monthTime" type="month" value-format="timestamp"
                          :format="$t('dateFormat.yyyymm')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('station.report.selectTime')" v-if="subarrayReportSearchForm.timeType == 3">
          <el-date-picker v-model="subarrayReportSearchForm.yearTime" type="year" value-format="timestamp"
                          :format="$t('dateFormat.yyyy')"
                          placeholder="" style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchSubarrayReport">{{$t('station.report.search')}}</el-button>
        </el-form-item>

      </el-form>

      <div ref="subarrayChart" style="width:100%;height: 200px;"></div>

      <el-row style="line-height: 32px;margin-bottom: 5px;">
        <el-col :span="24">
          <div style="float: left"> {{ subarrayTableName }}</div>
          <div style="float: right">
            <el-button type="primary" :size="elementSize" @click="exportSubarrayReport">{{$t('station.report.export')}}</el-button>
          </div>
        </el-col>
      </el-row>

      <el-table :data="subarrayReport.list" :size="elementSize" ref="subarrayTableData" border :max-height="subarrayReport.maxHeight">

        <el-table-column prop="stationName" :label="$t('station.report.subarray.headerName')[0]" align="center">
        </el-table-column>

        <el-table-column prop="collectTime" :label="$t('station.report.subarray.headerName')[1]" align="center" :formatter="collectSubarrayTimeFormatter">
        </el-table-column>

        <el-table-column prop="subarrayName" :label="$t('station.report.subarray.headerName')[2]" align="center">
        </el-table-column>

        <el-table-column prop="realCapacity" :label="$t('station.report.subarray.headerName')[3]" align="center">
        </el-table-column>

        <el-table-column prop="productPower" :label="$t('station.report.subarray.headerName')[4]" align="center">
        </el-table-column>

        <el-table-column prop="perPowerRatio" :label="$t('station.report.subarray.headerName')[5]" align="center">
        </el-table-column>

        <el-table-column prop="peakPower" :label="$t('station.report.subarray.headerName')[6]" align="center">
        </el-table-column>

      </el-table>
      <el-pagination :current-page="subarrayReport.index" :page-sizes="subarrayReport.pageSizes" :page-size="subarrayReport.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="subarrayReport.count" style="float: right; margin-top: 5px;"
                     @size-change="val => {subarrayReport.pageSize = val; searchSubarrayReport()}" @current-change="val => {subarrayReport.index = val; searchSubarrayReport()}">
      </el-pagination>

    </el-tab-pane>
  </el-tabs>
</template>

<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
