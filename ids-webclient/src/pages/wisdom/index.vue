<!-- 智慧应用菜单 -->
<style lang="less" src="./index.less" scoped></style>

<template>
  <el-tabs v-model="activeName">
    <!-- 逆变器组串离散率 -->
    <el-tab-pane :label="$t('wisdom.inventerLsl')" name="first">
      <scatter-analysis/>
    </el-tab-pane>
    <!-- 直流汇流箱组串离散率 -->
    <el-tab-pane :label="$t('wisdom.zlhlxZcLsl')" name="second">
      <scatter-dc-analysis/>
    </el-tab-pane>
    <!-- 组串诊断 -->
    <el-tab-pane :label="$t('wisdom.pvZd')" name="third">
      <component-analysis/>
    </el-tab-pane>
  </el-tabs>
</template>

<script type="text/ecmascript-6" src="./index.js"></script>
