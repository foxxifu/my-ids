<template>
  <el-tabs v-if="isEpc" v-model="reportTabName" @tab-click="tabClick" type="border-card" id="pam" tab-position="right" style="height: calc(100% - 35px); width: 100%;">
    <el-tab-pane label="资产" name="assets" style="height: 100%">
      <assetsOfepc style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="退换货" name="exchange" style="height: 100%">
      <exchangeOfepc style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="统计" name="statistics" style="height: 100%">
      <statisticsOfepc style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="项目" name="project" style="height: 100%">
      <projectOfepc style="height: 100%"/>
    </el-tab-pane>
  </el-tabs>

  <el-tabs v-else v-model="reportTabName" @tab-click="tabClick" type="border-card" id="pam" tab-position="right" style="height: calc(100% - 35px); width: 100%;">
    <el-tab-pane label="资产" name="assets" style="height: 100%">
      <assetsOfManufacturer style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="退换货" name="exchange" style="height: 100%">
      <exchangeOfManufacturer style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="统计" name="statistics" style="height: 100%">
      <statisticsOfManufacturer style="height: 100%"/>
    </el-tab-pane>
    <el-tab-pane label="项目" name="project" style="height: 100%">
      <projectOfManufacturer style="height: 100%"/>
    </el-tab-pane>
  </el-tabs>
</template>
<style lang='less' src='./index.less' scoped></style>
<script src='./index.js'></script>
