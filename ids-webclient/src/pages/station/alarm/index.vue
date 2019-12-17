<!--告警的页面-->
<template>
  <div class='alarm' style="height: 100%; width: 100%;">
    <el-tabs v-model="activeName" type="border-card" tab-position="right" style="height: 100%; width: 100%;">
      <el-tab-pane :label="$t('io.alarm.devAlarm')" name="DevAlarm">
        <DevAlarm v-if="activeName === 'DevAlarm'"></DevAlarm>
      </el-tab-pane>
      <el-tab-pane :label="$t('io.alarm.smartAlarm')" name="SmartAlarm">
        <SmartAlarm v-if="activeName === 'SmartAlarm'"></SmartAlarm>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
