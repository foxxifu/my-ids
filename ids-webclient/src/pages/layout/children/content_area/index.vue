<template>
  <section class="content-area">
    <transition name="router-fade" mode="out-in">
      <router-view :key="key"></router-view>
    </transition>
  </section>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
