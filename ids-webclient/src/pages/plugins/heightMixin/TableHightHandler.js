/**
 * 混入计算高度的组件,减少代码量
 */
export default {
  data() {
    return {
      // 最大的表格高度,初始化值尽量大一点，避免出现滚动条
      maxTableHeight: 1300,
      // 减去的数据
      deductNum: 200
    }
  },
  watch: {
  },
  beforeMount() {
    window.addEventListener('resize', this.$_resizeTableHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.$_resizeTableHandler)
  },
  mounted() {
    this.$_resizeTableHandler()
  },
  methods: {
    // 表格高度的计算
    $_resizeTableHandler() {
      const height = window.innerHeight
      let relHeight = height - this.deductNum
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.maxTableHeight = relHeight
    }
  }
}
