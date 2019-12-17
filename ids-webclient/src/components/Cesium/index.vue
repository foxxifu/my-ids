<template>
  <div ref="container" class="ids-earth">
  </div>
</template>

<script type="text/ecmascript-6">
  import Earth from '@/components/earth'

  export default {
    name: 'Cesium',
    props: {
      jump: {
        type: Function,
        default: () => {}
      },
      locations: {
        type: Array,
        default: []
      }
    },
    data () {
      return {
        earth: null,
      }
    },
    filters: {},
    mounted: function () {
      const _this = this
      _this.$nextTick(function () {
        _this.init(_this.$refs.container)

        _this.$watch('locations', (newVal, oldVal) => {
          _this['setLocations'](newVal, oldVal);
        }, {
          deep: true
        });
      })
    },
    methods: {
      init (container) {
        let locations = this.locations.concat([])
        let option = {
          jump: this.jump,
          locations,
        }
        this.earth = new Earth(container, option)

        setTimeout(function () {
          container.className = 'ids-earth show'
        }, 100)
      },
      setLocations (newVal, oldVal) {
        if (newVal) {
          this.earth.setLocations(newVal)
        }
      }
    },
    watch: {},
    computed: {},
    beforeDestroy: function () {
      this.earth = null
    },
  }
</script>

<style lang="less" scoped>
  div.ids-earth {
    position: absolute;
    top: 80px;
    left: 500px;
    right: 500px;
    width: 925px;
    height: 925px;
    border-radius: 50%;
    transform: scale(0);
    transition: transform 1s ease;

    &.show {
      transform: scale(1);
    }
    & /deep/ .earth-backglow {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: url("./earth_glow.png") center no-repeat;
      background-size: 100% 100%;
      border-radius: 50%;
      cursor: -webkit-grab;
    }
    /deep/ .cesium-location-label {
      position: absolute;
      top: 0;
      left: 0;
      min-width: 120px;
      max-width: 180px;
      white-space: nowrap;
      color: #fff;
      padding: 10px;
      text-overflow: ellipsis;
      background: rgb(12, 131, 207, 0.8);
      border: 1px solid #39be71;
      border-radius: 4px;
      overflow: hidden;
      transform: translate3d(-50%, -125%, 0px);
      transition: all 0.4s ease;
      cursor: pointer;
    }
  }
</style>

