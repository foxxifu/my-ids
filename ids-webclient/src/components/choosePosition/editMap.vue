<template>
  <div class="map-edit-continer">
    <div is="IDS_Map" @data-circle-change="dataCirleChange" v-if="isShowMap"  :mapType="mapType" :center="getCenter"
         :editable="true" :zoom="zoom" :circleMarkers="circleMarkers" :circle="circle" :initViewBounds="mapAreaBounds">
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Map from '../Map/index.vue'

  const props = { // 属性
    circle: {
      type: Object,
      require: true
    },
    circleMarkers: {
      type: [Object, Array],
      custom: true,
      default: () => []
    },
    zoom: {
      type: Number,
      default: undefined,
      require: true,
    },
    mapType: {
      type: Number,
      custom: true,
      default: 1, // 0 为 2D地图，1 为卫星地图
    },
    isShowMap: {
      type: Boolean,
      default: true
    }
  }
  export default {
    components: {
      IDS_Map: Map
    },
    props: props,
    created () {
      if (this.circle) {
        this.center = [this.circle.lat, this.circle.lng]
        this.mapAreaBounds = {
          latitude: this.circle.lat,
          longitude: this.circle.lng,
          radius: this.circle.radius * 2
        }
      } else { // 如果没有中心位置
        this.center = [30.413220, 104.219482]
        this.mapAreaBounds = {
          latitude: 30.413220,
          longitude: 104.219482,
          radius: 10000 * 2
        }
      }
    },
    data () {
      return {
        center: null,
        mapAreaBounds: null,
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      dataCirleChange (lat, lng, r) { // 圆改变半径和位置change事件
        this.$emit('data-circle-change', lat, lng, r)
        if (r && r > 0) {
          this.mapAreaBounds = {
            latitude: lat,
            longitude: lng,
            radius: r * 2
          }
        } else {
          this.center = [lat, lng] // 地图的中心位置
        }
      },
      doBoundArea (lat, lng, r2) {
        let self = this
        setTimeout(function () {
          self.$set(self, 'mapAreaBounds', {
            latitude: lat,
            longitude: lng,
            radius: r2
          })
        }, 2)
      }
    },
    watch: {
      'circle.radius' (val) { // 这个是监听输入框的值变化后导致这个内容改变的事件
        if (val && !isNaN(val)) {
          let self = this
          let tmp = self.mapAreaBounds
          this.doBoundArea(tmp.latitude, tmp.longitude, val * 2)
        }
      },
      'circle.lat' (val) { // 这个是监听输入框的值变化后导致这个内容改变的事件
        if (val && !isNaN(val)) {
          let self = this
          let tmp = self.mapAreaBounds
          this.doBoundArea(val, tmp.longitude, tmp.radius)
        }
      },
      'circle.lng' (val) { // 这个是监听输入框的值变化后导致这个内容改变的事件
        if (val && !isNaN(val)) {
          let self = this
          let tmp = self.mapAreaBounds
          this.doBoundArea(tmp.latitude, val, tmp.radius)
        }
      },
    },
    computed: {
      getCenter () {
        let tmp
        if (this.circle && this.circle.lat !== null && this.circle.lat !== undefined &&
          this.circle.lng !== null && this.circle.lng !== undefined) {
          tmp = [this.circle.lat, this.circle.lng]
        } else { // 如果没有中心位置
          tmp = [30.413220, 104.219482]
        }
        return tmp
      }
    },
    destroyed () {
    },
  }
</script>

<style lang="less" scoped>
  .map-edit-continer {
    // 修改圆形选择区域可以拖动区域大小和中心位置的样式
    /deep/ div.leaflet-marker-pane /deep/ div.leaflet-vertex-icon { // 设置在选择位置的信息的编辑内容的样式
      &:nth-child(2n) {
        width: 10px !important;
        height: 10px !important;
        margin-left: -5px !important;
        margin-top: -5px !important;
        border-radius: 50%;
        background: red;
      }
      &:nth-child(2n + 1) {
        width: 34px !important;
        height: 34px !important;
        margin-left: -17px !important;
        margin-top: -17px !important;
        opacity: 0.3; // 透明
      }
    }
  }
</style>

