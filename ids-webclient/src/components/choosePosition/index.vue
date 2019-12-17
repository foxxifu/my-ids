<!-- 选择地图一个地方的位置的信息 -->
<template>
  <el-dialog
    title="选择位置"
    :visible="isDialogVisible"
    width="40%" append-to-body class="choose-position-dialog"
    :before-close="closeHandler">
    <div v-if="isShowLocationText" style="margin-bottom: 10px;">
      <el-row>
        <el-col :span="7">
          <el-input v-model="showObj.lng" :title="showObj.lng" @blur="inputBlur">
            <template slot="prepend">{{ $t('components.choosePosition.lon') }}</template>
          </el-input>
        </el-col>
        <el-col :span="7">
          <el-input v-model="showObj.lat" :title="showObj.lat" @blur="inputBlur">
            <template slot="prepend">{{ $t('components.choosePosition.lat') }}</template>
          </el-input>
        </el-col>
        <el-col :span="7" v-if="!isPosition">
          <el-input v-model="showObj.radius" :title="showObj.radius" @blur="inputBlur">
            <template slot="prepend">{{ $t('components.choosePosition.rad') }}</template>
          </el-input>
        </el-col>
      </el-row>
    </div>
    <!-- 是否选择位置：true弹出选择位置的地图信息 -->
    <div v-if="isPosition" class="position-main" :mapType="mapType" is="IDS_Map" @data-circle-change="dataCirleChange"
         :center="center" :zoom="zoom" :markers="markers" :events="events">
    </div>
    <!-- 选择区域 -->
    <edit-map v-else class="position-main" :circle="circle" :isShowMap="isShowMap" :mapType="mapType" :zoom="zoom" @data-circle-change="dataCirleChange"></edit-map>

    <span slot="footer" class="dialog-footer">
      <el-button @click="closeHandler">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="sureClick">{{ $t('sure') }}</el-button>
    </span>
  </el-dialog>

</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 组件使用于获取经纬度或圆形区域
   * 1. 属性(Attributes)
   *      1) lat：Number 地理位置纬度
   *      2) lng: Number 地理位置经度
   *      3) radius: Number 如果是圆形区域的半径
   *      4) isPosition: Boolean 默认true ，选择的类型 true: 只选择经纬度 false: 选择圆形区域
   *      5）isDialogVisible: Boolean 是否显示弹出框 true:显示 false:不显示
   *      6) mapType: Number 只支持 0 1 默认 1 ，含义： 0: 二维  1：卫星地图
   *      7) zoom: Number 地图的范围大小
   *      8) isShowLocationText: Boolean 默认true ，是否展示限制位置的信息 true: 显示 false: 不显示
   * 2. 事件(Events)
   *      1) data-circle-change: 改变地图位置信息给的回调函数，参数：(lat, lng, r) => (纬度, 经度, 半径)
   *  例子：
   *      1) 选择圆形区域的使用方法 (具体例子在 系统设置/权限人员管理/区域管理 的新增或者修改功能的选择区域的调用)
   *       <choose-position v-if="isPostionChoose" :isDialogVisible.sync="isPostionChoose"
   *       :lat="domainForm.latitude" :lng="domainForm.longitude" :radius="domainForm.radius"
   *       :isPosition="false"  // 这个重要表示选择位置信息
   *       @data-circle-change="dataCirleChange"></choose-position>
   *      2) 选择位置信息的方法
   *      <choose-position :isDialogVisible.sync="isShow" :lat.sync="lngs.lat" :lng.sync="lngs.lng"
   *      @data-circle-change="dataCirleChange"></choose-position>
   *      对应的事件例子：
   *      dataCirleChange (lat, lng) {
            this.$set(this.lngs, 'lat', lat)
            this.$set(this.lngs, 'lng', lng)
          }
   *
   * */
  import Map from '../Map/index.vue'
  import editMap from './editMap.vue'

  const props = {
    lat: { // 纬度的model
      type: Number,
      default: 30.413220,
      // validator: function (value) { // 验证范围
      //   if (value === undefined || value === null) {
      //     return true;
      //   }
      //   return value >= -90 && value <= 90;
      // }
    },
    lng: { // 经度的model
      type: Number,
      default: 104.219482,
      // validator: function (value) { // 验证范围
      //   if (value === undefined || value === null) {
      //     return true;
      //   }
      //   return value >= -180 && value <= 180;
      // },
    },
    radius: { // 半径
      type: Number,
      default: 10000,
      validator: function (value) { // 验证范围
        return value >= 0
      },
    },
    isPosition: { // 是否是选择位置 true:选择位置 false:选择圆形区域
      type: Boolean,
      default: true
    },
    isDialogVisible: {
      type: Boolean,
      default: false
    },
    mapType: {
      type: Number,
      custom: true,
      default: 1, // 0 为 2D地图，1 为卫星地图
    },
    zoom: {
      type: Number,
      custom: true,
      default: 11,
    },
    isShowLocationText: { // 是否展示位置的地理经纬度或者范围的输入框
      type: Boolean,
      default: true
    },
    maxRadius: { // 如果是选择区域最大的范围是300万
      type: Number,
      default: 3000000
    }
  }

  export default {
    props: props,
    components: {
      editMap,
      IDS_Map: Map
    },
    created () {
      let center = this.center = []
      let lng = this.lng !== undefined && this.lng !== null ? this.lng : 104.219482
      let lat = this.lat !== undefined && this.lat !== null ? this.lat : 30.413220
      center.push(lat)
      center.push(lng)
      this.showObj.lat = lat
      this.showObj.lng = lng
      this.circle.lng = lng
      this.circle.lat = lat
      // let radius
      if (!this.isPosition) {
        this.showObj.radius = this.circle.radius = this.radius ? this.radius : 10000
        this.circle.radius = this.showObj.radius
        this.circle.maxRadius = this.maxRadius
      } else {
        this.markers[0].position = [lat, lng]
      }
      // this.$emit('data-circle-change', lat, lng, radius)
      this.events = {
        'click': this.dragend
      }
    },
    data () {
      return {
        center: null,
        markers: [
          {
            id: 'mark' + Date.parse() + '' + (Math.random() * 100).toFixed(),
            position: null,
            tooltip: this.$t('components.choosePosition.moveModifyAdr'),
            draggable: true, // 可以拖动
            events: { // 这里修改为拖动结束之后再调用这个事件，避免组件之间的交互过多
              dragend: this.dragend // 在地图上拖动的事件
            }
          },
        ],
        circle: {
          lat: 0,
          lng: 0,
          isEditor: true,
          radius: 0
        },
        showObj: {
          lat: 0,
          lng: 0,
          radius: 0
        },
        events: {},
        isShowMap: true
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      dragend (e) { // 监听拖动位置的标志点的拖动结束条用的事件
        let latlng = e.latlng || e.target.getLatLng()
        this.$set(this.markers[0].position, '0', latlng.lat)
        this.$set(this.markers[0].position, '1', latlng.lng)
        // this.$emit('update:lat', latlng.lat)
        // this.$emit('update:lng', latlng.lng)
        this.dataCirleChange(latlng.lat, latlng.lng)
        // 改变中心位置为当前拖动到的位置
        this.$set(this.center, '0', latlng.lat)
        this.$set(this.center, '1', latlng.lng)
      },
      closeHandler () {
        this.$emit('update:isDialogVisible', false)
      },
      dataCirleChange (lat, lng, r) { // 圆改变半径和位置change事件
        lat = +lat.toFixed(6)
        lng = +lng.toFixed(6)
        this.$set(this.showObj, 'lat', lat)
        this.$set(this.showObj, 'lng', lng)
        if (!this.isPosition) {
          r = +r.toFixed(6)
          this.$set(this.showObj, 'radius', r)
        }
      },
      sureClick() {
        let lat, lng, r
        if (this.isPosition) {
          lat = this.showObj.lat
          lng = this.showObj.lng
        } else {
          lat = this.showObj.lat
          lng = this.showObj.lng
          r = this.showObj.radius
        }
        this.$emit('data-circle-change', lat, lng, r)
        this.closeHandler()
      },
      inputBlur () { // 输入框输入框失去焦点
        let lng = this.showObj.lng
        let lat = this.showObj.lat
        if (isNaN(lng)) {
          this.showObj.lng = this.circle.lng
          return
        } else {
          lng = +lng
          if (lng > 180 || lng < -180) {
            this.showObj.lng = this.circle.lng
            return
          }
        }
        if (isNaN(lat)) {
          this.showObj.lat = this.circle.lat
          return
        } else {
          lat = +lat
          if (lat > 85 || lat < -85) {
            this.showObj.lat = this.circle.lat
            return
          }
        }
        let r = this.showObj.radius
        if (!this.isPosition) {
          if (isNaN(r)) {
            this.showObj.radius = this.circle.radius
            return
          } else {
            r = +r
            if (r < 0 || r > this.maxRadius) { // 暂时定义最大300万
              this.showObj.radius = this.circle.radius
              return
            }
          }
        }
        if (this.circle.lng === lng && this.circle.lat === lat) {
          if (this.isPosition) {
            return
          } else if (this.circle.radius === r){
            return
          }
        }
        if (this.isPosition) {
          this.$set(this.center, '0', lat)
          this.$set(this.center, '1', lng)
          this.markers[0].position = [lat, lng]
        } else {
          this.isShowMap = false
          let self = this
          setTimeout(function () {
            self.circle.lng = lng
            self.circle.lat = lat
            self.center[0] = lat
            self.center[1] = lng
            self.circle.radius = r
            self.isShowMap = true
          }, 2)
        }
      }
    },
    watch: {
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
  .choose-position-dialog {
    /deep/ .el-dialog {
      height: 80%;
      min-width: 200px;
      min-height: 200px;
      /deep/ .el-dialog__body {
        width: 100%;
        height: calc(100% - 95px);
        .position-main {
          width: 100%;
          height: calc(100% - 40px);
          overflow: hidden;
        }
      }
      .el-col {
        &:not(:first-child) {
          margin-left: -2px;
        }
        .el-input-group {
          /deep/ .el-input-group__prepend {
            border-radius: 0;
          }
          /deep/ .el-input__inner {
            border-radius: 0;
          }
        }
        &:first-child {
          /deep/ .el-input-group__prepend {
            border-radius: 10px 0 0 10px;
          }
        }
        &:last-child {
          /deep/ .el-input__inner {
            border-radius: 0 10px 10px 0;
          }
        }
      }
    }
  }
</style>

