<!--告警的页面-->
<template>
  <div class='dev-alarm'>
    <div>
      <div style="float: left;">
        <el-form :inline="true" :model="searchData" class="demo-form-inline">
          <!--<el-form-item label="电站名称">
            <el-input v-model="searchData.stationName" placeholder="电站名称" clearable></el-input>
          </el-form-item>-->
          <el-form-item :label="$t('io.alarmName')">
            <el-input v-model="searchData.alarmName" :placeholder="$t('io.alarmName')" clearable style="width: 140px"></el-input>
          </el-form-item>
          <el-form-item :label="$t('io.alarm.alarmLevel')">
            <el-select v-model="searchData.alarmLevel" :placeholder="$t('io.alarm.alarmLevel')" clearable style="width: 140px">
              <el-option :label="$t('io.alarmLevelArr')[0]" value="1"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[1]"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[2]" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('io.alarm.devName')">
            <el-input v-model="searchData.devAlias" :placeholder="$t('io.alarm.devName')" clearable style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchAlarmList(true)">{{$t('search')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div style="float: right">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item><el-button @click="clearOrSureAlarm('clear')" type="primary">{{$t('io.alarm.clear')}}</el-button></el-form-item>
          <el-form-item><el-button @click="clearOrSureAlarm('sure')" type="primary">{{$t('io.alarm.ack')}}</el-button></el-form-item>
          <el-form-item><el-button @click="clickToDefect" type="primary">{{$t('io.alarm.createOrder')}}</el-button></el-form-item>
        </el-form>
      </div>
    </div>
    <div class="list-bar">
      <div class="list-table">
        <el-table :data="list" ref="devAlarmTable" :maxHeight="maxHeight" border width="100%" :fit="fit" row-key="id"
                  @selection-change="tableSelectionChange" @row-click="toggleSelection">
          <el-table-column type="selection" width="55" align="center">
          </el-table-column>
          <el-table-column prop="stationName" :label="$t('io.alarm.plantName')" align="center">
          </el-table-column>
          <el-table-column prop="alarmName" :label="$t('io.alarmName')" align="center">
          </el-table-column>
          <el-table-column prop="alarmLevel" :label="$t('io.alarm.alarmLevel')" align="center">
            <template slot-scope="scope">
              <label :class="{'text-color-red':scope.row.alarmLevel === 1,
                  'text-color-blue':scope.row.alarmLevel === 2,
                  'text-color-green':scope.row.alarmLevel === 3}">{{ scope.row.alarmLevel | alarmLeveFilter(vm)}}</label>
            </template>
          </el-table-column>
          <el-table-column prop="devAlias" :label="$t('io.alarm.devName')" align="center">
          </el-table-column>
          <el-table-column prop="alarmState" :label="$t('io.alarmStatus')" align="center">
            <template slot-scope="scope">
              <label :class="{'text-color-red':scope.row.alarmState === 0}">
                {{ scope.row.alarmState | alarmStatusFilter(vm)}}
              </label>
            </template>
          </el-table-column>
          <el-table-column prop="createDate" :label="$t('io.alarm.happendTime')" align="center">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ dateFormat(scope.row.createDate,$t('dateFormat.yyyymmddhhmmss')) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createDate" :label="$t('io.alarm.recoverTime')" align="center">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ dateFormat(scope.row.recoverDate,$t('dateFormat.yyyymmddhhmmss')) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="devInterval" :label="$t('io.alarm.devInverl')" align="center">
            <template slot-scope="{row}">
              <span style="margin-left: 10px">{{ row.devInterval || row.stationName }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" :label="$t('io.suggession')" align="center">
            <template slot-scope="scope">
              <a @click="repeatDetailClick(scope.row)" style="cursor: pointer;color: #54B8D1;" class="text-color-green" width="100">{{$t('detail')}}</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="overflow: hidden;margin-top: 10px;">
        <div style="float: right;right: 10px;">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchData.page" :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
    <el-dialog
      :title="$t('io.alarmDetailTitle')"
      :visible.sync="isShowDetailDialog"
      width="80%"
      :close-on-click-modal="false"
      append-to-body>
      <AlarmDetailDialgContent v-if="isShowDetailDialog" :needSearch="[true, {id:rowData.id}, 1,{stationName: rowData.stationName}]"></AlarmDetailDialgContent>
    </el-dialog>
    <!-- 转工单的的对话框 -->
    <AddDefectDialog v-bind:devId="devId" v-if="isShowAddDefect" :is-show-add-defect.sync="isShowAddDefect" :rowDatas="addDefectFormData" :alarmType="1"></AddDefectDialog>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
