<!--智能运维的页面-->
<template>
  <div class='platform'>
      <div class="search-bar toolbar">
        <el-form :inline="true" :model="searchBarData" class="demo-form-inline">
          <el-form-item label="电站名称">
            <el-input v-model="searchBarData.stationName" placeholder="电站名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="电站状态">
            <el-select v-model="searchBarData.status" placeholder="请选择电站状态" clearable>
              <el-option label="断链" value="1"></el-option>
              <el-option label="故障" value="2"></el-option>
              <el-option label="正常" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBarClick">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="table-bar">
          <div class="list-table">
            <el-table :data="list" width="100%" border :maxHeight="maxHeight">
              <el-table-column type="expand" width="55">
                <template slot-scope="scope">
                  <!-- 调用自定义插件展开的插件 -->
                  <PlatCard :row="scope.row"></PlatCard>
                </template>
              </el-table-column>
              <el-table-column label="电站状态" prop="stationStatus" align="center">
                <template slot-scope="scope">
                  <img v-lazy="getStatusImage(scope.row.stationStatus)" style="width: 26px; height: 26px;">
                </template>
              </el-table-column>
              <el-table-column label="电站缩略图" prop="stationPic" align="center">
                <template slot-scope="scope">
                  <img v-lazy="getPicImgUrl(scope.row.stationPic)" :onerror="errorImg01" style="width: 100%;max-width: 105px; display: inline-block; margin: 5px auto;"/>
                </template>
              </el-table-column>
              <el-table-column label="电站名称" prop="stationName" align="center">
              </el-table-column>
              <el-table-column label="电站地址" prop="stationAddr" align="center">
              </el-table-column>
              <el-table-column label="设备状态" prop="deviceStatus" align="center">
                <template slot-scope="scope">
                  <i v-if="isShowStatusImage(scope.row.deviceStatus,1)" :class="getImageClass(scope.row.deviceStatus,1)"></i>
                  <i v-if="isShowStatusImage(scope.row.deviceStatus,8)" :class="getImageClass(scope.row.deviceStatus,8)"></i>
                  <i v-if="isShowStatusImage(scope.row.deviceStatus,15)" :class="getImageClass(scope.row.deviceStatus,15)"></i>

                </template>
              </el-table-column>
              <el-table-column label="电站RP" prop="stationPr" align="center">
              </el-table-column>
              <el-table-column label="活动告警数" prop="alarmCount" align="center">
              </el-table-column>
              <el-table-column label="未完结缺陷数" prop="defectCount" align="center">
              </el-table-column>
            </el-table>
          </div>
        <div style="overflow: hidden;margin-top: 10px;">
          <div style="float: right;right: 10px;">
            <el-pagination @size-change="searchPlatformList(true)" @current-change="searchPlatformList()" :current-page="searchData.page" :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' ></style>
