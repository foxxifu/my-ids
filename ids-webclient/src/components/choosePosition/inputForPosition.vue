<template>
  <div class="input-for-postion-parent">
    <el-row>
      <!-- 经度 -->
      <el-col :span="spanLen">
        <el-form-item :prop="myProps.lng">
          <el-input v-model="position.lng" readonly @focus="showDialog" :title="position.lng" :disabled="!isCanChoose">
            <template slot="prepend">{{ $t('components.choosePosition.lon') }}</template>
          </el-input>
        </el-form-item>
      </el-col>
      <!-- 纬度 -->
      <el-col :span="spanLen">
        <el-form-item :prop="myProps.lat">
          <el-input v-model="position.lat" readonly @focus="showDialog"  :title="position.lat" :disabled="!isCanChoose">
            <template slot="prepend">{{ $t('components.choosePosition.lat') }}</template>
          </el-input>
        </el-form-item>
      </el-col>
      <!-- 如果不是位置，就是选择区域，就有半径 -->
      <el-col :span="spanLen" v-if="!isPosition">
        <el-form-item :prop="myProps.r">
          <el-input v-model="position.radius" readonly @focus="showDialog"  :title="position.radius" :disabled="!isCanChoose">
            <template slot="prepend">{{ $t('components.choosePosition.rad') }}</template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <choose-position v-if="isDialogVisible" :isPosition="isPosition"
                     :isDialogVisible.sync="isDialogVisible" :lat="position.lat" :lng="position.lng"
                     :radius="position.radius"
                     :isShowLocationText="isShowLocationText"
                     :mapType="mapType"
                     :zoom="zoom"
                     @data-circle-change="dataCirleChange"
    ></choose-position>
  </div>
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
   *      5) mapType: Number 只支持 0 1 默认 1 ，含义： 0: 二维  1：卫星地图
   *      6) zoom: Number 地图的范围大小
   *      7) isShowLocationText: Boolean 默认true ，是否展示限制位置的信息 true: 显示 false: 不显示
   *      8) props: 如果有验证规则，验证的经纬度以及半径的验证属性名称 默认{lat: 'latitude',lng: 'longitude',r: 'radius'}
   *      9) isCanChoose: Boolean 默认true，是否可以修改内容 参数意义：true：可以修改内容  false：不能修改内容
   * 2. 事件(Events)
   *      1) myChange: 改变地图位置信息给的回调函数，参数：(lat, lng, r) => (纬度, 经度, 半径)
   *  例子：
   *      1) 企业选择区域的例子 (具体例子在 企业管理/企业信息 的新增或者修改功能的选择区域的调用)
   *      <input-for-position
           :lat="enterModel.latitude"
           :lng="enterModel.longitude"
           :radius="enterModel.radius" // 如果 isPosition === true 就不需要这个参数
           :isPosition="false" // 判断是选择区域还是选择范围
           :isCanChoose="!myDialogType(2)"
           @myChange="dataCirleChange"
          ></input-for-position>
   *
   * */
  import ChoosePosition from './index.vue'
  const defaultProps = {
    lat: 'latitude', // 经度的属性取值key，用于验证等等
    lng: 'longitude', // 纬度的属性取值key，用于验证等等
    r: 'radius' // 半径的属性取值key，用于验证等等
  }
  const props = {
    lat: { // 纬度的model
      type: Number,
      default: null,
    },
    lng: { // 经度的model
      type: Number,
      default: null,
    },
    radius: { // 半径
      type: Number,
      default: null,
      validator: function (value) { // 验证范围
        return value >= 0
      },
    },
    isPosition: { // 是否是选择位置 true:选择位置 false:选择圆形区域
      type: Boolean,
      default: true
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
    isShowLocationText: { // 是否展示位置的文字信息
      type: Boolean,
      default: true
    },
    props: { // 默认验证的属性
      type: Object,
      default () {
        return defaultProps
      }
    },
    isCanChoose: { // 是否弹出显示框
      type: Boolean,
      default: true
    }
  }
  export default {
    props: props,
    created () {
      this.spanLen = this.isPosition ? 10 : 8
      this.position = {
        lat: this.lat,
        lng: this.lng,
        radius: this.radius,
      }
      this.myProps = Object.assign({}, defaultProps, this.props)
    },
    components: {
      ChoosePosition
    },
    data () {
      return {
        spanLen: 0, // 每一个col的宽度
        isDialogVisible: false,
        myProps: {},
        position: {
        }
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      showDialog () { // 展示对话框
        if (this.isCanChoose) {
          this.isDialogVisible = true
        }
      },
      dataCirleChange (lat, lng, r) { // 选择改变的事件
        this.$emit('myChange', lat, lng, r) // 通知调用的地方使用myChange事件
      }
    },
    watch: {
      radius (val) {
        this.position.radius = val
      },
      lng (val) {
        this.position.lng = val
      },
      lat (val) {
        this.position.lat = val
      },
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
  .input-for-postion-parent{
    width: 100%;
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
</style>

