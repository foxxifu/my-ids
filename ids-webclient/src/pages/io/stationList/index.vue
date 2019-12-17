<template>
  <div class="station-list">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchBarData" class="demo-form-inline">
        <el-form-item :label="$t('io.plantform.plantName')">
          <el-input v-model="searchBarData.stationName" :placeholder="$t('io.plantform.plantName')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('io.plantform.plantStatus')">
          <el-select v-model="searchBarData.stationStatus" :placeholder="$t('io.plantform.selectPlantStatus')" clearable>
            <el-option :label="$t('io.stationStatusArr')[0]" value="1"></el-option>
            <el-option :label="$t('io.stationStatusArr')[1]" value="2"></el-option>
            <el-option :label="$t('io.stationStatusArr')[2]" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('io.plantform.plantType')">
          <el-select v-model="searchBarData.onlineType" :placeholder="$t('io.plantform.selectPlantType')" clearable>
            <el-option :label="$t('io.plantform.plantTypeArr')[0]" value="1"></el-option>
            <el-option :label="$t('io.plantform.plantTypeArr')[1]" value="2"></el-option>
            <el-option :label="$t('io.plantform.plantTypeArr')[2]" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchDatas(true)">{{$t('search')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- table数据 -->
    <div class="list-bar">
      <!-- 表格数据 -->
      <el-table ref="stationList" :data="list" border style="width: 100%" @sort-change="sortChange" :maxHeight="maxHeight">
        <el-table-column prop="stationStatus" align="center" :label="$t('io.status')" width="60">
          <template slot-scope="scope">
            <i class="status-cirle" :title="scope.row.stationStatus | statusFilter(vm)"
               :class="{'status-red': (scope.row.stationStatus === 1),'status-green':(scope.row.stationStatus === 3)}"></i>
          </template>
        </el-table-column>
        <el-table-column prop="stationName" align="center" :label="$t('io.plantform.plantName')">
          <template slot-scope="scope">
            <div class="text-ellipsis" :title="scope.row.stationName"
              style="cursor: pointer;color: #3f3;" @click.prevent="toOneStation(scope.row)"> {{ scope.row.stationName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="stationAddr" align="center" :label="$t('io.plantform.plantAddr')">
        </el-table-column>
        <el-table-column prop="onlineType" align="center" :label="$t('io.plantform.plantType')" width="130">
          <template slot-scope="scope">
            {{ scope.row.onlineType | stationTypeFilter(vm) }}
          </template>
        </el-table-column>
        <el-table-column width="170px" prop="installedCapacity" sortable align="center" :label="$t('io.plantform.capacity')">
        </el-table-column>
        <el-table-column width="150px" prop="activePower" align="center" sortable :label="$t('io.plantform.runTimePower')">
        </el-table-column>
        <el-table-column prop="deviceStatus" align="center" :label="$t('io.plantform.devStatus')" width="200">
          <template slot-scope="scope">
            <div :title="scope.row.deviceStatus | devStatusFilter(vm)">
              <i v-if="isShowStatusImage(scope.row.deviceStatus,1)" :class="getImageClass(scope.row.deviceStatus,1)"></i>
              <i v-if="isShowStatusImage(scope.row.deviceStatus,8)" :class="getImageClass(scope.row.deviceStatus,8)"></i>
              <i v-if="isShowStatusImage(scope.row.deviceStatus,15)" :class="getImageClass(scope.row.deviceStatus,15)"></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column width="105px" prop="undoTask" align="center" :label="$t('io.plantform.todayNoFinisth')">
        </el-table-column>
        <el-table-column width="105px" prop="totalTask" align="center" :label="$t('io.plantform.todayTotalTask')">
        </el-table-column>
        <el-table-column width="115px" prop="totalAlarm" align="center" :label="$t('io.plantform.todayActiveAlarm')">
        </el-table-column>
      </el-table>
      <!-- 分页栏 -->
      <div style="clear: both;overflow: hidden;margin-top: 10px;">
        <div style="float: right">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchBarData.page" :page-sizes="[10, 20, 30, 50]" :page-size="searchBarData.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="./index.js"></script>
<style lang="less" src="./index.less" scoped></style>

