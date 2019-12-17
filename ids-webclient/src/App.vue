<template>
  <div id="app">
    <div class="transitionBackground">{{ $t('systemName') }}</div>
    <transition name="router-fade" mode="out-in">
      <keep-alive include="exhibition">
        <router-view v-if="isRouterAlive"></router-view>
      </keep-alive>
    </transition>
  </div>
</template>

<script>
  export default {
    name: 'app',
    provide () {
      return {
        reload: this.reload,
      }
    },
    data () {
      return {
        isRouterAlive: true,
      }
    },
    methods: {
      reload () {
        this.isRouterAlive = false
        this.$nextTick(function () {
          this.isRouterAlive = true
        })
      }
    }
  }
</script>

<style lang="less">
  #app {
    width: 100%;
    height: 100%;
    .transitionBackground {
      display: block;
      position: absolute;
      text-align: center;
      top: 50%;
      width: 100%;
      font-size: 1.7em;
      color: #e0dfe3;
      font-weight: normal !important;
      /* &::after {
        content: url("~/assets/images/login/login_title_top.png");
        position: relative;
        top: -16px;
        left: -373px;
        width: 40px;
      } */
    }
  }

  .router-fade-enter-active, .router-fade-leave-active {
    transition: opacity .3s;
  }

  .router-fade-enter, .router-fade-leave-active {
    opacity: 0;
  }
</style>
