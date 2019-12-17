<!--告警的页面-->
<template>
  <div class='alarm'>
    <!-- 设备告警 -->
    <el-tabs v-model="activeName">
      <el-tab-pane :label="$t('io.alarm.devAlarm')" name="DevAlarm">
        <DevAlarm v-if="activeName === 'DevAlarm'"></DevAlarm>
      </el-tab-pane>
      <!-- 智能告警 -->
      <el-tab-pane :label="$t('io.alarm.smartAlarm')" name="SmartAlarm">
        <SmartAlarm v-if="activeName === 'SmartAlarm'"></SmartAlarm>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
