<template>
  <el-menu class="navbar" mode="horizontal">
    <div class="navbar-icon el-icon-fa fa-home"></div>
    <el-breadcrumb class="app-levelbar" separator=">">
      <el-breadcrumb-item v-for="(item,index)  in levelList" :key="index">
        <router-link v-if='item.redirect === "noredirect" || index == levelList.length-1' to="" class="no-redirect">
          {{$t(item.name)}}
        </router-link>
        <router-link v-else :to="item.redirect || item.path">{{$t(item.name)}}</router-link>
      </el-breadcrumb-item>
    </el-breadcrumb>
    <div class="navbar-info">
      <div class="company-info">{{ currentCompany }}</div>
      <!--<div class="time">{{ currentDate | formatDate }}</div>-->
      <div class="time">{{ new Date() | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</div>
    </div>
  </el-menu>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
