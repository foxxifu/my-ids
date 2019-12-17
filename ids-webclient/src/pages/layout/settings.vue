<template>
  <transition name="router-fade" mode="out-in">
    <router-view :key="key"></router-view>
  </transition>
</template>

<script type="text/ecmascript-6">
  export default {
    computed: {
      key () {
        return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
      },
    }
  }
</script>

<style lang="less" scoped>
</style>
