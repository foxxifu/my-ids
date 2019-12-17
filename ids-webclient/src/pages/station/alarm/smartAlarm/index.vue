<template>
  <div class="smart-alarm">
    <!-- 搜索栏 -->
    <div>
      <div style="float: left;">
        <el-form :inline="true" :model="searchData" class="demo-form-inline">
          <!--<el-form-item label="电站名称">
            <el-input v-model="searchData.stationName" placeholder="电站名称" clearable></el-input>
          </el-form-item>-->
          <el-form-item :label="$t('io.alarm.devName')">
            <el-input v-model="searchData.devAlias" :placeholder="$t('io.alarm.devName')" clearable style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item :label="$t('io.alarm.alarmLevel')">
            <el-select v-model="searchData.alarmLevel" :placeholder="$t('io.alarm.alarmLevel')" clearable style="width: 140px">
              <el-option :label="$t('io.alarmLevelArr')[0]" value="1"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[1]" value="2"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[2]" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('io.alarmStatus')">
            <el-select v-model="searchData.alarmState" :placeholder="$t('io.alarmStatus')" clearable style="width: 140px">
              <el-option :label="$t('io.alarmStatusArr')[0]" value="1"></el-option>
              <el-option :label="$t('io.alarmStatusArr')[1]" value="2"></el-option>
              <el-option :label="$t('io.alarmStatusArr')[2]" value="3"></el-option>
              <el-option :label="$t('io.alarmStatusArr')[3]" value="4"></el-option>
              <el-option :label="$t('io.alarmStatusArr')[4]" value="5"></el-option>
              <el-option :label="$t('io.alarmStatusArr')[5]" value="6"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchListData(true)">{{$t('search')}}</el-button>
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
      <!-- 表格数据 -->
      <el-table :data="list" :maxHeight="maxHeight" border width="100%" ref="smartAlarmTable" :fit="true" row-key="id"
                @selection-change="tableSelectionChange"  @row-click="toggleSelection">
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.alarm.plantName')" prop="stationName" align="center"></el-table-column>
        <el-table-column :label="$t('io.alarmName')" prop="alarmName" align="center">
          <template slot-scope="{row}">
            {{row.alarmId && ($t('dataConverter.analysis.' + row.alarmId + '.alarmName') + (row.alarmName.lastIndexOf('#') > 0 ? row.alarmName.substring(row.alarmName.lastIndexOf('#')) : ''))}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.alarm.alarmLevel')" prop="alarmLevel" align="center">
          <template slot-scope="scope">
            <label :class="{'text-color-red':scope.row.alarmLevel === 1,
                  'text-color-blue':scope.row.alarmLevel === 2,
                  'text-color-green':scope.row.alarmLevel === 3}"> {{ scope.row.alarmLevel | alarmLeveFilter(vm) }} </label>
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.alarm.devName')" prop="devAlias" align="center"></el-table-column>
        <el-table-column :label="$t('io.alarmStatus')" prop="alarmState" align="center">
          <template slot-scope="scope">
            <label :class="{'text-color-red':scope.row.alarmState === 0}">
              {{ scope.row.alarmState | alarmStatusFilter(vm) }}
            </label>
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.alarm.happendTime')" prop="createDate" align="center">
          <template slot-scope="scope">
            {{ scope.row.createDate | timestampFomat($t('dateFormat.yyyymmddhhmmss'))}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.alarm.recoverTime')" prop="endDate" align="center">
          <template slot-scope="scope">
            {{ scope.row.recoverDate | timestampFomat($t('dateFormat.yyyymmddhhmmss'))}}
          </template>
        </el-table-column>

        <!--<el-table-column label="告警定位" prop="alarmLocation" align="center"></el-table-column>-->
        <el-table-column prop="devInterval" :label="$t('io.alarm.devInverl')" align="center">
          <template slot-scope="{row}">
            <span style="margin-left: 10px">{{ row.devInterval || row.stationName }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.suggession')" align="center">
          <template slot-scope="scope">
            <a style="cursor: pointer;color: #54B8D1" @click.prevent="showReperDialog(scope.row)">{{$t('detail')}}</a>
          </template>
        </el-table-column>
      </el-table>
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
      <AlarmDetailDialgContent v-if="isShowDetailDialog" :needSearch="[true, {id:rowData.id}, 2, {stationName: rowData.stationName}]"></AlarmDetailDialgContent>
    </el-dialog>
    <!-- 转工单的的对话框 -->
    <AddDefectDialog v-if="isShowAddDefect" :is-show-add-defect.sync="isShowAddDefect" :rowDatas="addDefectFormData"
                     :alarmType="2"></AddDefectDialog>
  </div>
</template>

<script src="./index.js"></script>
<style lang="less" src="./index.less" scoped></style>

