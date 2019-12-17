<!-- 选择电站的弹出框 -->
<template>
  <el-dialog :title="$t('components.chooseStation.choosePlant')" :visible="isShowStation" width="50%" :before-close="handleClose"
             class="choose-station-dialog" append-to-body :close-on-click-modal="false">
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('components.chooseDevice.plantName')">
          <el-input v-model="searchData.stationName" :placeholder="$t('components.chooseDevice.plantName')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryStations(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-list">
      <el-table
        ref="stationTable"
        :data="list"
        border
        :highlight-current-row="!isMutiSelect"
        @current-change="handleCurrentChange"
        @selection-change="handleSelectionChange"
        @row-click="mutiRowClick"
        :maxHeight="maxHeight"
        style="width: 100%">
        <el-table-column v-if="tableType === 'index'"
                         :type="tableType"
                         width="35">
          <template v-if="tableType === 'index'" slot-scope="scope">
            <el-radio v-model="radioModel" :label="scope.row.stationCode"></el-radio>
          </template>
        </el-table-column>
        <el-table-column v-else
                         :type="tableType"
                         width="35">
        </el-table-column>
        <el-table-column :label="$t('components.chooseDevice.plantName')" prop="stationName" align="center">
        </el-table-column>
        <el-table-column :label="$t('components.chooseStation.plantAdr')" prop="stationAddr" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column :label="$t('components.chooseStation.insCap')" width="180" prop="installedCapacity" align="center">
        </el-table-column>
      </el-table>
      <!-- 分页栏 -->
      <div style="clear: both;overflow: hidden;margin-top: 10px;">
        <div style="float: right;right: 10px;">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchData.page"
                         :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="sureClick">{{ $t('sure') }}</el-button>
      <el-button @click="handleClose">{{ $t('cancel') }}</el-button>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 输入框组件的居右获取焦点后弹出选择电站的对话框
   * 其基本还是一个el-input输入框，只是添加了一个弹出选择电站的对话框的功能
   * 1. 属性(Attributes)
   *      1) isShowStation：Boolean 默认 false ， 参数意义 ---> true: 显示弹出框 false:不显示弹出框
   *      2) myValues：String 默认是空，电站的编号，如果是选择的多选的，电站编号之间要使用mysplitStr的值隔开
   *      3) myTexts: String 默认是空，电站名称，如果是选择的多选的，电站名称之间要使用mysplitStr的值隔开
   *      4) isMutiSelect: Boolean 默认 true ，是否是多选 参数意义-->true： 多选， false：单选
   *      5) searchUrl: String 默认/dev/station/getStationInfo,查询电站的url地址
   *      6）isAutoSelect: Boolean 默认 true， 参数的意义--> true:弹出框自动选择输入框的具体的值的列
   *      7) maxSelectNum: Number 默认 10，弹出框可以选择的最大数量的记录如果不限制使用-1
   *      8) mysplitStr: String 默认英文的逗号即','
   *      9) enterpriseId: Number 查询指定企业的电站信息
   * 2. 事件(Events)
   *      1) on-sure-callback: 参数：row/rows 如果是单选（isMutiSelect===false）：就是当前的选中的哪一行，或者为空；
   *        如果是多选（isMutiSelect ==== true，就是当前选中的记录的数组,未选择的情况小时长度为0的数组
   * 3. 使用例子
   *    1) chooseStation/index.vue
   *    关键代码
   *     <ChooseStationDialog
   *      v-if="isShowStation"
   *      :isShowStation.sync="isShowStation"
   *      :searchUrl="searchUrl"
   *      :isMutiSelect="isMutiSelect"
   *      @on-sure-callback="selectStation"
   *      :myValues="myValues"
   *      :myTexts="myTexts"
   *      :isAutoSelect="isAutoSelect"
   *      :maxSelectNum="maxSelectNum"
   *      :mysplitStr="mysplitStr"
   *     ></ChooseStationDialog>
   * */

  import fetch from '@/utils/fetch'

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
    searchUrl: {
      type: String,
      default: null
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
    enterpriseId: { // 查询电站的企业
      type: Number,
      default: null
    }
  }
  export default {
    props: props,
    created () {
      if (!this.searchUrl) {
        this.mySearchUrl = '/biz/station/getStationInfo'
      } else {
        this.mySearchUrl = this.searchUrl
      }
      if (this.enterpriseId) {
        this.searchData.enterpriseId = this.enterpriseId
      }
      this.calcMaxHeight()
    },
    components: {},
    data () {
      return {
        isShowStation: false,
        mySearchUrl: null,
        searchData: {
          index: 1,
          pageSize: 10
        },
        list: [],
        total: 0,
        currentRow: null, // 单选的行
        radioModel: null, // 单选的按钮选中的项
        multipleSelection: [], // 多选的数据
        // oneSelectTimer: null, // 单加载器的一个定时器
        maxHeight: 120,
      }
    },
    filters: {},
    mounted: function () {
      let _this = this
      _this.$nextTick(function () {
        _this.queryStations(true)
        _this.setRealValue(_this.myValues)
      })
      if (window.addEventListener) { // 避免window.onresize使用其他的不生效的情况
        window.addEventListener('resize', _this.calcMaxHeight, false)
      } else if (window.attachEvent) {
        window.attachEvent('onresize', _this.calcMaxHeight)
      }
    },
    methods: {
      showDialog () {
        this.isShowStation = true
        this.queryStations(true)
      },
      queryStations (isToFirst, callback) { // 查询分页的数据
        if (isToFirst) {
          this.searchData.page = 1
        }
        let self = this
        fetch(this.mySearchUrl, this.searchData, 'POST').then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          self.list = datas.list || []
          self.total = datas.count || 0

          Object.isFunction(callback) && callback()

          // 查询到之后去判断哪些是被选中了的
          self.autoSelect()
        })
      },
      autoSelect () { // 自动选中已经查询到的值
        let self = this
        if (self.isAutoSelect && self.list.length > 0 && self.realValue) {
          let stationCodeArr = self.realValue
          let len = self.list.length
          let arr = []
          for (let i = 0; i < len; i++) {
            let r = self.list[i]
            if (stationCodeArr.indexOf(r.stationCode) === -1) {
              continue
            }
            arr.push(r)
          }
          if (this.isMutiSelect) { // 多选的
            setTimeout(() => {
              if (arr.length > 0) {
                arr.forEach(row => {
                  self.$refs['stationTable'] && self.$refs['stationTable'].toggleRowSelection(row)
                })
              } else {
                self.$refs['stationTable'] && self.$refs['stationTable'].clearSelection()
              }
            }, 30)
          } else { // 单选的的
            let row
            if (arr.length > 0) {
              row = arr[0]
            }
            self.$refs['stationTable'] && self.$refs['stationTable'].setCurrentRow(row)
          }
        }
      },
      setRealValue (val) {
        this.realValue = val && val.split(this.mysplitStr)
      },
      handleCurrentChange (val) { // 选中了的行
        if (this.currentRow && val.stationCode === this.currentRow.stationCode) {
          this.currentRow = null
        } else {
          this.currentRow = val
        }
      },
      handleSelectionChange (val) { // 多选的时候出发的事件
        this.multipleSelection = val
      },
      mutiRowClick (row, event, column) { // 对于多选的rowclick的事件
        if (!this.isMutiSelect) {
          return
        }
        this.$refs['stationTable'] && this.$refs['stationTable'].toggleRowSelection(row)
      },
      handleClose () {
        this.isShowStation = false
      },
      sureClick () { // 点击确定的方法
        if (this.isMutiSelect) { // 如果是多选是数组
          if (this.maxSelectNum !== -1 && this.multipleSelection.length > this.maxSelectNum) {
            this.$message(this.$t('components.chooseDevice.beyondThat') + this.maxSelectNum + this.$t('components.chooseDevice.chooseNumIs') + this.multipleSelection.length)
            return
          }
          this.$emit('on-sure-callback', this.multipleSelection)
        } else { // 单选是一行的数据
          this.$emit('on-sure-callback', this.currentRow)
        }
        this.handleClose() // 关闭对话框
      },
      pageSizeChange (val) { // 每页显示的数量改变的事件
        this.searchData.pageSize = val
        this.queryStations(true)
      },
      pageChange (val) { // 页数改变后的事件
        this.searchData.page = val
        this.queryStations()
      },
      calcMaxHeight () { // 计算表格的最大高度
        let height = document.documentElement.clientHeight * 0.85 - 60 - 68 - 42 - 70 - 100
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      }
    },
    watch: {
      currentRow (newVal) {
        if (newVal) {
          this.radioModel = newVal.stationCode
        } else {
          this.radioModel = null
        }
      },
      enterpriseId (newVal) {
        this.$set(this.searchData, 'enterpriseId', newVal)
      }
    },
    computed: {
      tableType () {
        return this.isMutiSelect ? 'selection' : 'index'
      }
    },
    updated () {
      if (!this.isMutiSelect) {
        setTimeout(() => {
          let dom = document.querySelector('.choose-station-dialog tr th div.cell')
          if (dom) { // 清除#号
            dom.innerHTML = ''
          }
        }, 100)
      }
    },
    destroyed () {
      if (window.addEventListener) {
        window.removeEventListener('resize', this.calcMaxHeight, false)
      } else if (window.attachEvent) {
        window.detachEvent('onresize', this.calcMaxHeight)
      }
      // this.oneSelectTimer && clearInterval(this.oneSelectTimer)
    }
  }
</script>

<style lang="less" scoped>
  .choose-station-dialog {
    /deep/ .el-radio__input.is-disabled .el-radio__inner {
      cursor: auto;
    }
    /deep/ .el-radio__input.is-disabled + span.el-radio__label {
      cursor: auto;
    }
  }
</style>

