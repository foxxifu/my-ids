<!-- 输入框选择电站 -->
<template>
  <div class="choose-div-input-parent">
    <el-input @focus="isShowDev = true" :title="showText" v-model="showText" :placeholder="placeholder"></el-input>
    <!-- 弹出对话框 -->
    <ChooseDeviceDialog
      :isShowDev.sync="isShowDev"
      :searchUrl="searchUrl"
      :isMutiSelect="isMutiSelect"
      @on-sure-callback="selectDevice"
      :myValues="myValues"
      :myTexts="myTexts"
      :isAutoSelect="isAutoSelect"
      :maxSelectNum="maxSelectNum"
      :mysplitStr="mysplitStr"
    ></ChooseDeviceDialog>
  </div>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 输入框组件的居右获取焦点后弹出选择电站的对话框
   * 其基本还是一个el-input输入框，只是添加了一个弹出选择电站的对话框的功能
   * 1. 属性(Attributes)
   *      1) myValues：String 默认是空，电站的编号，如果是选择的多选的，电站编号之间要使用mysplitStr的值隔开
   *      2) myTexts: String 默认是空，电站名称，如果是选择的多选的，电站名称之间要使用mysplitStr的值隔开
   *      3) isMutiSelect: Boolean 默认 true ，是否是多选 参数意义-->true： 多选， false：单选
   *      4) searchUrl: String 默认空，这时在弹出种使用的是/dev/station/getStationInfo，可以修改这个查询的url
   *      5) placeholder: String 默认'请选择电站'，输入框的提示信息
   *      6）isAutoSelect: Boolean 默认 true， 参数的意义--> true:弹出框自动选择输入框的具体的值的列
   *      7) maxSelectNum: Number 默认 10，弹出框可以选择的最大数量的记录如果不限制使用-1
   *      8) mysplitStr: String 默认英文的逗号即','
   *      9) props: Object 取value 和 name的真正字段，默认是id取id;devName取devName
   *      10) isCheckDevType: Boolean是否验证设备类型，默认false 参数意义-》true:验证 false：不验证
   *      11) checkDevTypeArr: [Object, Array] 如果isCheckDevType === true的情况下验证的设备类型，默认值[1]
   *      12) checkErrorMsg: String 当开启验证设备类型的时候验证错误的提示信息，默认'请选择组串式逆变器，当前选择的非组串式逆变器设备名称为：'
   * 2. 事件(Events)
   *      1) device-change: 参数：(vals, texts, rows) => 即(devIds, devNames, rows), 如果有验证在验证通过之后才会调用
   * 3. 使用例子
   *    1) 设备对比中有相应的使用
   *    关键代码
   *     <StationInput
   *     :myValues="calcTaskModel.stationCode"
   *     :myTexts="calcTaskModel.stationName"
   *     @device-change="deviceChange"
   *     :isMutiSelect="false"></StationInput>
   *     // 回调处理
   *     deviceChange (vals, texts) { // 选择电站输入框之后的回调函数
   *      this.$set(this.calcTaskModel, 'stationCode', vals)
   *      this.$set(this.calcTaskModel, 'stationName', texts)
   *    }
   * */
  import ChooseDeviceDialog from './chooseDeviceDialog.vue'

  const defaultProps = {
    id: 'id',
    devName: 'devAlias',
    devTypeId: 'devTypeId'
  }

  const props = {
    myValues: { // 选择的值
      type: String,
      default: null
    },
    myTexts: { // 显示的文本
      type: String,
      default: null
    },
    isMutiSelect: { // 是否多选
      type: Boolean,
      default: true
    },
    searchUrl: { // 搜索的url
      type: String,
      default: null
    },
    placeholder: {
      type: String,
      default: '请选择设备'
    },
    isAutoSelect: { // 是否默认选中已经选中了的数据
      type: Boolean,
      default: true
    },
    maxSelectNum: { // 最大选择数 默认10个如果设置为 -1就不限制, 除了-1是全选，必须是大于0的整数，否则就不能选择了
      type: Number,
      default: 10
    },
    mysplitStr: { // 默认分割的字符串
      type: String,
      default: ','
    },
    props: { // 行数据的属性 ，主要有id和名称
      type: Object,
      default (){
        return defaultProps
      }
    },
    isCheckDevType: { // 是否验证设备类型
      type: Boolean,
      default: false
    },
    checkDevTypeArr: { // 验证正确的设备类型的集合, 在isCheckDevType为true时这个数组中的值生效
      type: [Number, Array],
      default () { // 默认只验证组串式逆变器
        return [1]
      }
    },
    checkErrorMsg: {
      type: String,
      default: '请选择组串式逆变器，当前选择的非组串式逆变器设备名称为：'
    }
  }
  export default {
    props: props,
    created () {
      this.showText = this.myTexts
      this.realValue = this.myValues
      this.myProps = Object.assign({}, defaultProps, this.props)
    },
    components: {
      ChooseDeviceDialog
    },
    data () {
      return {
        showText: null, // 显示的文字
        realValue: null,
        isShowDev: false,
        myProps: null
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      // 当弹出框弹出确定的时候在父组件上的回调函数
      selectDevice (rows) { // 判断是单电站还是多电站
        let vals, texts
        let errorDevNames = '' // 如果需要验证，错误的设备名称
        let checkDevTypeArr = (this.isCheckDevType && this.checkDevTypeArr) || null
        if (this.isMutiSelect) { // 如果是多电站
          let len = (rows && rows.length) || 0
          if (len > 0) {
            vals = rows[0][this.myProps.id] + ''
            texts = rows[0][this.myProps.devName]
            if (checkDevTypeArr && checkDevTypeArr.indexOf(rows[0][this.myProps.devTypeId]) === -1) {
              errorDevNames = texts
            }
            for (let i = 1; i < len; i++) {
              let devName = rows[i][this.myProps.devName]
              vals += this.mysplitStr + rows[i][this.myProps.id]
              texts += this.mysplitStr + devName
              if (checkDevTypeArr && checkDevTypeArr.indexOf(rows[i][this.myProps.devTypeId]) === -1) {
                errorDevNames += ',' + devName
              }
            }
          }
        } else if (rows){
          vals = rows[this.myProps.id] + ''
          texts = rows[this.myProps.devName] + ''
          if (checkDevTypeArr && checkDevTypeArr.indexOf(rows.devTypeId) === -1) {
            errorDevNames = texts
          }
        }
        if (errorDevNames) { // 选中的设备类型不符合
          this.$message(this.checkErrorMsg + errorDevNames)
          return
        }
        // this.showText = texts
        // this.realValue = vals
        this.$emit('device-change', vals, texts, rows)
      }
    },
    watch: {
      myValues (newVal) {
        this.$set(this, 'realValue', newVal)
      },
      myTexts (newVal) {
        this.$set(this, 'showText', newVal)
      }
    },
    computed: {},
    updated () {
    },
    destroyed () {
    }
  }
</script>

<style lang="less" scoped>
  .choose-div-input-parent {
    width: 100%;
    height: 100%;
  }
</style>

