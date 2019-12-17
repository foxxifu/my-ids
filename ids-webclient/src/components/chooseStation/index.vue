<!-- 输入框选择电站 -->
<template>
  <div class="choose-div-input-parent">
    <el-input @focus="showStation" :title="showText" v-model="showText" :placeholder="myPlaceholder"></el-input>
    <!-- 弹出对话框 -->
    <ChooseStationDialog ref="chooseStationDialog"
      :searchUrl="searchUrl"
      :isMutiSelect="isMutiSelect"
      @on-sure-callback="selectStation"
      :myValues="realValue"
      :myTexts="showText"
      :isAutoSelect="isAutoSelect"
      :maxSelectNum="maxSelectNum"
      :mysplitStr="mysplitStr"
      :enterpriseId="enterpriseId"
    ></ChooseStationDialog>
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
   *      7）isDefaultSelectFirst: Boolean 默认 false， 参数的意义--> true:初始选择searchUrl查询的第一条记录并回显
   *      8) maxSelectNum: Number 默认 10，弹出框可以选择的最大数量的记录如果不限制使用-1
   *      9) mysplitStr: String 默认英文的逗号即','
   *     10) enterpriseId: Number 查询指定企业的电站信息
   * 2. 事件(Events)
   *      1) station-change: 参数：(vals, texts) => 即(stationCodes, stationNames)
   * 3. 使用例子
   *    1) 数据完整性的kpi重计算中新建任务的时候有这个的例子
   *    关键代码
   *     <StationInput
   *     :myValues="calcTaskModel.stationCode"
   *     :myTexts="calcTaskModel.stationName"
   *     @station-change="stationChange"
   *     :isMutiSelect="false"></StationInput>
   *     // 回调处理
   *     stationChange (vals, texts) { // 选择电站输入框之后的回调函数
   *      this.$set(this.calcTaskModel, 'stationCode', vals)
   *      this.$set(this.calcTaskModel, 'stationName', texts)
   *    }
   * */
  import ChooseStationDialog from './chooseStationDialog.vue'

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
      default: 'components.chooseStation.choosePlant'
    },
    isAutoSelect: { // 是否默认选中已经选中了的数据
      type: Boolean,
      default: true
    },
    isDefaultSelectFirst: { // 是否默认选择searchUrl查询的第一条记录并回显
      type: Boolean,
      default: false
    },
    maxSelectNum: { // 最大选择数 默认10个如果设置为 -1就不限制, 除了-1是全选，必须是大于0的整数，否则就不能选择了
      type: Number,
      default: 10
    },
    mysplitStr: { // 默认分割的字符串
      type: String,
      default: ','
    },
    enterpriseId: { // 查询电站的企业
      type: Number,
      default: null
    }
  }
  export default {
    props: props,
    created () {
      this.showText = this.myTexts
      this.realValue = this.myValues
    },
    components: {
      ChooseStationDialog
    },
    data () {
      return {
        showText: null, // 显示的文字
        realValue: null,
        isShowStation: false
      }
    },
    filters: {},
    mounted: function () {
      const _this = this
      _this.$nextTick(function () {
        if (this.isDefaultSelectFirst) {
          let dialogEl = _this.$refs['chooseStationDialog']
          dialogEl.queryStations(true, function () {
            if (dialogEl && dialogEl.list[0]) {
              let vals = dialogEl.list[0].stationCode
              let texts = dialogEl.list[0].stationName
              _this.$set(_this, 'realValue', vals)
              _this.$set(_this, 'showText', texts)
              dialogEl.setRealValue(vals)
              _this.$emit('station-change', vals, texts)
            }
          })
        }
      })
    },
    methods: {
      showStation () {
        const _this = this
        let dialogEl = _this.$refs['chooseStationDialog']
        dialogEl.showDialog()
      },
      // 当弹出框弹出确定的时候在父组件上的回调函数
      selectStation (rows) { // 判断是单电站还是多电站
        let vals, texts
        if (this.isMutiSelect) { // 如果是多电站
          let len = (rows && rows.length) || 0
          if (len > 0) {
            vals = rows[0].stationCode
            texts = rows[0].stationName
            for (let i = 1; i < len; i++) {
              vals += this.mysplitStr + rows[i].stationCode
              texts += this.mysplitStr + rows[i].stationName
            }
          }
        } else if (rows) {
          vals = rows.stationCode
          texts = rows.stationName
        }
        this.$emit('station-change', vals, texts)
      }
    },
    watch: {
      myTexts (val) {
        this.$set(this, 'showText', val)
      },
      myValues (val) {
        this.$set(this, 'realValue', val)
      }
    },
    computed: {
      myPlaceholder () {
        if (this.placeholder && this.placeholder.indexOf('.') > 0) {
          return this.$t(this.placeholder)
        }
        if (this.placeholder) {
          return this.placeholder
        }
        return this.$t('components.chooseStation.choosePlant')
      }
    },
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

