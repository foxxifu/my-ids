<!-- 选择设备的弹出框 -->
<template>
  <el-dialog :title="$t('components.chooseDevice.chooseDev')" :visible="isShowDev" width="60%" :before-close="handleClose"
             class="choose-device-dialog" append-to-body :close-on-click-modal="false">
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('components.chooseDevice.plantName')">
          <el-input v-model="searchData.stationName" :placeholder="$t('components.chooseDevice.plantName')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('components.chooseDevice.devType')">
          <el-select v-model="searchData.devTypeId" filterable :placeholder="$t('components.chooseDevice.choosePlantStatus')" clearable>
            <el-option v-for="item in devTypeList" :key="item.devTypeId" :label="item.devTypeName"
                       :value="item.devTypeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('components.chooseDevice.devName')">
          <el-input v-model="searchData.devAlias" :placeholder="$t('components.chooseDevice.devName')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryDevices(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-list">
      <el-table
        ref="deviceType"
        :data="list"
        border
        :highlight-current-row="!isMutiSelect"
        @current-change="handleCurrentChange"
        @selection-change="handleSelectionChange"
        @row-click="mutiRowClick"
        :maxHeight="maxHeight"
        style="width: 100%">
        <el-table-column v-if="tableType === 'index'" :type="tableType" width="35">
          <template v-if="tableType === 'index'" slot-scope="scope">
            <el-radio v-model="radioModel" :label="scope.row.id" disabled></el-radio>
          </template>
        </el-table-column>
        <el-table-column v-else :type="tableType" width="35">
        </el-table-column>
        <el-table-column :label="$t('components.chooseDevice.plantName')" prop="stationName" align="center">
        </el-table-column>
        <el-table-column :label="$t('components.chooseDevice.devName')" prop="devAlias" align="center">
        </el-table-column>
        <el-table-column :label="$t('components.chooseDevice.devType')" width="130" prop="devTypeId" :formatter="devTypeFormat" align="center">
        </el-table-column>
        <!--<el-table-column :label="$t('components.chooseDevice.devType')" width="130" prop="devTypeId" :formatter="devTypeFormat" align="center">-->
        <!--</el-table-column>-->
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
      <el-button type="primary" @click="sureClick">{{$t('sure')}}</el-button>
      <el-button @click="handleClose">{{$t('cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 输入框组件的居右获取焦点后弹出选择设备的对话框
   * 其基本还是一个el-input输入框，只是添加了一个弹出选择电站的对话框的功能
   * 1. 属性(Attributes)
   *      1) isShowDev：Boolean 默认 false ， 参数意义 ---> true: 显示弹出框 false:不显示弹出框
   *      2) myValues：String 默认是空，设备的id值的字符串，如果是选择的多选的，设备id之间要使用mysplitStr的值隔开
   *      3) myTexts: String 默认是空，设备名称，如果是选择的多选的，设备名称之间要使用mysplitStr的值隔开
   *      4) isMutiSelect: Boolean 默认 true ，是否是多选 参数意义-->true： 多选， false：单选
   *      5) searchUrl: String 默认/biz/device/getDeviceByCondition,查询设备的url地址
   *      6）isAutoSelect: Boolean 默认 true， 参数的意义--> true:弹出框自动选择输入框的具体的值的列
   *      7) maxSelectNum: Number 默认 10，弹出框可以选择的最大数量的记录如果不限制使用-1
   *      8) mysplitStr: String 默认英文的逗号即','
   * 2. 事件(Events)
   *      1) on-sure-callback: 参数：row/rows 如果是单选（isMutiSelect===false）：就是当前的选中的哪一行，或者为空；
   *        如果是多选（isMutiSelect ==== true，就是当前选中的记录的数组,未选择的情况小时长度为0的数组
   *      2) on-cancel-callback 取消后的回调函数，无参数
   * 3. 使用例子
   *    1)
   *    关键代码
   *     <ChooseDevDialog
   *      v-if="isShowDev"
   *      :isShowDev.sync="isShowDev"
   *      :searchUrl="searchUrl"
   *      :isMutiSelect="isMutiSelect"
   *      @on-sure-callback="selectDev"
   *      :myValues="myValues"
   *      :myTexts="myTexts"
   *      :isAutoSelect="isAutoSelect"
   *      :maxSelectNum="maxSelectNum"
   *      :mysplitStr="mysplitStr"
   *     ></ChooseDevDialog>
   * */

  import fetch from '@/utils/fetch'

  const props = {
    isShowDev: { // 是否显示对话框
      type: Boolean,
      default: false
    },
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
    }
  }
  export default {
    props: props,
    created () {
      if (!this.searchUrl) {
        this.mySearchUrl = '/dev/device/getDeviceByCondition'
      } else {
        this.mySearchUrl = this.searchUrl
      }
      this.calcMaxHeight()
    },
    components: {},
    data () {
      return {
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
        oneSelectTimer: null, // 单加载器的一个定时器
        maxHeight: 120,
        devTypeList: [ // 设备类型的列表,这些数据应该是后台获取的先固定值
          {
            devTypeId: 1,
            devTypeName: this.$t('components.chooseDevice.seriesInv')
          },
          {
            devTypeId: 2,
            devTypeName: this.$t('components.chooseDevice.dataCollector')
          },
          {
            devTypeId: 8,
            devTypeName: this.$t('components.chooseDevice.boxTransformel')
          },
          {
            devTypeId: 15,
            devTypeName: this.$t('components.chooseDevice.confluenceBox')
          },
        ],
        devTypIdToNameMap: { // 设备类型到字符串的转换
          1: this.$t('components.chooseDevice.seriesInv'),
          2: this.$t('components.chooseDevice.dataCollector'),
          8: this.$t('components.chooseDevice.boxTransformel'),
          15: this.$t('components.chooseDevice.confluenceBox'),
        }
      }
    },
    filters: {},
    mounted: function () {
      this.queryDevices(true)
      this.$nextTick(function () {
      })
      window.onresize = () => this.calMaxHeight
    },
    methods: {
      queryDevices (isToFirst) { // 查询分页的数据
        if (isToFirst) {
          this.searchData.page = 1
        }
        this.searchData.index = this.searchData.page
        let self = this
        fetch(this.mySearchUrl, this.searchData, 'POST').then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          self.list = datas.list || []
          self.total = datas.count || 0
          // 查询到之后去判断哪些是被选中了的
          self.autoSelect()
        })
      },
      autoSelect () { // 自动选中已经查询到的值
        if (this.isAutoSelect && this.list.length > 0 && this.myValues) {
          let stationCodeArr = new String(this.myValues).split(this.mysplitStr)
          let len = this.list.length
          let arr = []
          for (let i = 0; i < len; i++) {
            let row = this.list[i]
            if (stationCodeArr.indexOf(row.stationCode) === -1) {
              continue
            }
            arr.push(row)
          }
          if (this.isMutiSelect) { // 多选的
            let self = this
            setTimeout(() => {
              if (arr.length > 0) {
                arr.forEach(row => {
                  self.$refs['deviceType'] && self.$refs.deviceType.toggleRowSelection(row)
                })
              } else {
                self.$refs['deviceType'] && self.$refs.deviceType.clearSelection()
              }
            }, 30)
          } else { // 单选的的
            let row
            if (arr.length > 0) {
              row = arr[0]
            }
            this.$refs['deviceType'] && this.$refs.deviceType.setCurrentRow(row)
          }
        }
      },
      handleCurrentChange (val) { // 选中了的行
        if (this.currentRow && val.id === this.currentRow.id) {
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
        this.$refs.deviceType.toggleRowSelection(row)
      },
      handleClose (isSure) {
        this.$emit('update:isShowDev', false)
        if (isSure !== true) {
          this.$emit('on-cancel-callback') // 取消的回调方法
        }
      },
      sureClick () { // 点击确定的方法
        if (this.isMutiSelect) { // 如果是多选是数组
          if (this.maxSelectNum !== -1 && this.multipleSelection.length > this.maxSelectNum) {
            this.$message(this.$t('components.chooseDevice.beyondThat') + this.maxSelectNum + this.$t('components.chooseDevice.chooseNumIs') + this.multipleSelection.length)
            return
          }
          this.$emit('on-sure-callback', this.multipleSelection)
        } else { // 单选是一行的数据
          if (!this.currentRow) {
            this.$message(this.$t('components.chooseDevice.pleaseChooseDev'))
            return
          }
          this.$emit('on-sure-callback', this.currentRow)
        }
        this.handleClose(true) // 关闭对话框
      },
      pageSizeChange (val) { // 每页显示的数量改变的事件
        this.searchData.pageSize = val
        this.queryDevices(true)
      },
      pageChange (val) { // 页数改变后的事件
        this.searchData.page = val
        this.queryDevices()
      },
      calcMaxHeight () { // 计算表格的最大高度
        let height = document.documentElement.clientHeight * 0.85 - 60 - 68 - 42 - 70 - 100
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      },
      devTypeFormat (row, column, cellValue, index) {
        if (cellValue) {
          return this.devTypIdToNameMap[cellValue] || '-'
        }
        return '-'
      }
    },
    watch: {
      myValues (newVal, oldVal) {
        if (newVal) {
          if (this.isAutoSelect && this.list.length > 0) {
            let stationCodeArr = new String(newVal).split(this.mysplitStr)
            let len = this.list.length
            let arr = []
            for (let i = 0; i < len; i++) {
              let row = this.list[i]
              if (stationCodeArr.indexOf(row.stationCode) === -1) {
                continue
              }
              arr.push(row)
            }
            if (this.isMutiSelect) { // 多选的
              let self = this
              setTimeout(() => {
                if (arr.length > 0) {
                  arr.forEach(row => {
                    self.$refs['deviceType'] && self.$refs.deviceType.toggleRowSelection(row)
                  })
                } else {
                  self.$refs['deviceType'] && self.$refs.deviceType.clearSelection()
                }
              }, 30)
            } else { // 单选的的
              let row
              if (arr.length > 0) {
                row = arr[0]
              }
              this.$refs['deviceType'] && this.$refs.deviceType.setCurrentRow(row)
            }
          }
        } else {

        }
      },
      currentRow (newVal) {
        if (newVal) {
          this.radioModel = newVal.id
        } else {
          this.radioModel = null
        }
      }
    },
    computed: {
      tableType () {
        return this.isMutiSelect ? 'selection' : 'index'
      }
    },
    updated () {
      if (!this.isMutiSelect) {
        // let self = this
        this.oneSelectTimer = setTimeout(() => {
          let dom = document.querySelector('.choose-device-dialog tr th div.cell')
          if (dom) { // 清除#号
            // self.oneSelectTimer && clearInterval(self.oneSelectTimer)
            dom.innerHTML = ''
          }
        }, 100)
      }
    },
    destroyed () {
      // this.oneSelectTimer && clearInterval(this.oneSelectTimer)
    }
  }
</script>

<style lang="less" scoped>
  .choose-device-dialog {
    /deep/ .el-radio__input.is-disabled .el-radio__inner {
      cursor: auto;
    }
    /deep/ .el-radio__input.is-disabled + span.el-radio__label {
      cursor: auto;
    }
  }

</style>

