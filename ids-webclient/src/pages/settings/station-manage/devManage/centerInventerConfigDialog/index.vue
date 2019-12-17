<template>
  <el-dialog class="centerInveterDialog"
    :title="$t('cent.centInv')"
    :visible.sync="isShowDialog"
    width="70%"
    :before-close="handleClose" append-to-body>
    <div class="search-bar">
      {{ $t('cent.conBoxNum1') }}<el-input-number v-model="boxNum" @change="changeNum" controls-position="right" :min="1" :max="100" :precision="0" :label="$t('cent.conBoxNum')"></el-input-number>
      <el-button type="primary" @click="openBatchooseDevs" style="float: right;">{{ $t('cent.batchSelection') }}</el-button>
    </div>
    <div class="table-list">
      <el-form status-icon :model="selectDevForm" ref="centerInventerForm">
        <el-table :data="tableData" style="width: 100%;" border :max-height="maxHeight">
          <el-table-column
            prop="devId"
            align="center"
            :label="$t('cent.devName')">
            <template slot-scope="{row, $index}">
              <el-form-item :prop="'validateDevId' + $index" :rules="[{required:true, message: $t('cent.equSelect'), trigger: ['blur', 'change']}, {validator: valiDateRepeatDevId}]" style="margin: 0;">
                <el-select v-model="selectDevForm['validateDevId' + $index]" :placeholder="$t('cent.chooseDev')" clearable
                           filterable @change="selectChange($index)">
                  <el-option
                    v-for="item in hlxList"
                    :key="item.id"
                    :label="item.devAlias"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="venderName"
            align="center"
            :label="$t('cent.manuFac')"
            width="140">
          </el-table-column>
          <el-table-column
            prop="devTypeName"
            align="center"
            :label="$t('cent.devNum')">
          </el-table-column>
          <el-table-column
            prop="num"
            align="center"
            width="155"
            :label="$t('cent.strNum')">
          </el-table-column>
          <el-table-column
            prop="totalPvCap"
            align="center"
            :label="$t('cent.cap')">
          </el-table-column>
          <el-table-column
            prop="r2"
            align="center"
            width="120"
            :label="$t('cent.op')">
            <template slot-scope="{row, $index}">
              <el-button type="primary" :title="$t('delete')" @click="deletRow($index)" icon="el-icon-error"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer" style="text-align: center;">
    <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
    <el-button type="primary" @click="handleClose">{{ $t('cancel') }}</el-button>
    </div>
    <batch-select-dialog @on-select-finish="onSelectFinish" v-if="isShowBatchDialog"
                         :isShowDialog.sync="isShowBatchDialog"
                         :hlxDevs="hlxDevInfos" :stationCode="stationCode"></batch-select-dialog>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import DeviceInfoService from '@/service/device'
  import BatchSelectDialog from './batchConfluenceBoxDialog/index.vue'

  const props = {
    isShowDialog: { // 是否显示对话框
      type: Boolean,
      default: false
    },
    devId: { // 设备id
      type: Number,
      default: null
    },
    stationCode: { // 电站编号
      type: String,
      default: null
    }
  }
  export default {
    props: props,
    components: {
      BatchSelectDialog
    },
    created () {
      this.getZLHLXList()
      this.calcMaxHeight()
    },
    data() {
      return {
        isShowBatchDialog: false, // 是否显示批量配置的弹出框
        boxNum: 1, // 汇流箱的数量, 默认配置一个，可以输入修改
        tableData: [], // 表格数据, 进来的时候需要去查询集中式逆变器是否已经配置了
        hlxList: [ // 直流汇流箱设备的集合,首先需要先查询出来
        ],
        selectDevForm: { // 提交的选择的设备的表格数据
        },
        maxHeight: 120,
        hlxDevInfos: [] // 当前页已经选择了的设备
      }
    },
    filters: {},
    mounted: function () {
      // TODO 在查询了集中式逆变器设备之后组装表格数据
      // this.getTableData()
      this.$nextTick(function () {
      })
      window.onresize = this.calcMaxHeight
    },
    methods: {
      calcMaxHeight () { // 计算最大的高度
        let height = document.documentElement.clientHeight * 0.8 - 180
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      },
      getZLHLXList () { // 查询直流汇流箱的设备信息
        let self = this
        new Promise(function(resolve, reject) {
          DeviceInfoService.getDCJSIdAndName({id: self.devId}).then(resp => {
            let datas = (resp.code === 1 && resp.results) || []
            self.hlxList = datas
            resolve()
          })
        }).then(function () { // 请求成功后再请求已经配置好了的数据
          self.getDCJSByShip()
        })
      },
      getDCJSByShip () {
        let self = this
        DeviceInfoService.getDCJSByShip({id: this.devId}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          let idsStr = datas.ids
          if (idsStr) {
            let ids = idsStr.split(',')
            let len = ids.length
            let tmpForm = {}
            for (let i = 0; i < len; i++) {
              tmpForm['validateDevId' + i] = +ids[i]
            }
            self.selectDevForm = tmpForm
          }
          self.getTableData()
        })
      },
      openBatchooseDevs () { // 打开批量选择设备的对话框
        this.hlxDevInfos = this.getBathDevInfos()
        this.isShowBatchDialog = true
      },
      getBathDevInfos () { // 批量选择获取已经选择了的设备信息
        let tmpList = []
        let tmpFormObject = this.selectDevForm
        let tmpDataList = this.tableData
        for (let key in tmpFormObject) {
          if (tmpFormObject.hasOwnProperty(key) && tmpFormObject[key]) {
            let tmpIndex = +key.split('validateDevId')[1]
            tmpList.push({
              id: tmpFormObject[key],
              order: tmpIndex, // 排序使用
              devAlias: (tmpDataList[tmpIndex] && tmpDataList[tmpIndex].devAlias) || null
            })
          }
        }
        tmpList.sort((a, b) => {
          return a.order - b.order
        })
        return tmpList
      },
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      getTableData () { // 第一次进入的时候的加载数据
        // if (this.tableData.length === 0) { // 没有配置
        //   let len = this.boxNum
        //   for (let i = 0; i < len; i++) {
        //     this.tableData.push({}) // 添加一条空数据
        //   }
        // } else { // 配置了的
        // TODO 1.通过集中式逆变器去查询设备配置的数据,如果没有就添加默认的空行数据，如果有就添加对应的数据
        // TODO 2.去获取后台的配置的直流汇流箱的厂家等信息
        this.onSelectFinish(this.getCurrentSelectHLXDevId())
        // }
      },
      getCurrentSelectHLXDevId () { // 获取当前页面上的汇流箱id
        let ids = []
        let tmpFormObject = this.selectDevForm
        for (let key in tmpFormObject) {
          if (tmpFormObject.hasOwnProperty(key) && tmpFormObject[key]) {
            let tmpIndex = key.split('validateDevId')[1]
            ids.push(tmpIndex + '-' + tmpFormObject[key])
          }
        }
        ids.sort(function (a, b) { // 有一个不存在
          if (!a && !b) {
            return 0
          }
          if (a && !b) {
            return 1
          }
          if (!a && b) {
            return -1
          }
          let index1 = +a.split('-')[0]
          let index2 = +b.split('-')[0]
          return index1 - index2
        })
        return ids.map(item => {
          return +item.split('-')[1]
        })
      },
      submitForm () {
        let self = this
        this.$refs.centerInventerForm.validate((valid) => {
          if (valid) {
            // 组织表单数据
            let params = {}
            params.id = self.devId
            params.ids = self.getCurrentSelectHLXDevId().join(',')
            DeviceInfoService.saveDCJSShip(params).then(resp => {
              if (resp.code === 1) {
                self.$message(this.$t('cent.saveSuccess'))
                self.handleClose()
              } else {
                self.$message(resp.message || this.$t('cent.saveFailed'))
              }
            })
          } else {
            self.$message(this.$t('cent.valFail'))
            return false;
          }
        })
      },
      selectChange (index, row) { // 选择修改的事件
        let data
        // 判断是否重复
        let key = 'validateDevId' + index
        let val = this.selectDevForm[key]
        if (!val) {
          data = {}
          this.tableData[index] = data
          return
        }
        let self = this
        DeviceInfoService.getDCJSDetail({ids: this.selectDevForm['validateDevId' + index] + ''}).then(resp => {
          data = (resp.code === 1 && resp.results && resp.results[0]) || {}
          data.devTypeName = this.$t('cent.conBox')
          self.$set(this.tableData, index, data)
        })
      },
      onSelectFinish (deviceIds) { // 选择之后的设备信息,或者初始到页面查询到有配置了直流汇流箱的时候
        let len = deviceIds.length
        if (len > this.boxNum) {
          this.boxNum = len
        }
        let maxLen = Math.max(this.boxNum, len) // 取最大的
        let formDatas = {}
        let tabDatas = []
        for (let i = 0; i < maxLen; i++) {
          if (i < len) { // 保证配置的项
            formDatas['validateDevId' + i] = deviceIds[i]
          }
          tabDatas.push({}) // 添加一条空数据
        }
        this.$set(this, 'tableData', tabDatas)
        this.$set(this, 'selectDevForm', formDatas)
        // TODO 这里去获取后台的数据
        this.getDevConfigByDevIds(deviceIds)
      },
      deletRow (index) { // 删除一行记录
        let self = this
        this.$confirm(this.$t('cent.sureDelNum'), this.$t('cent.tip'), {
          confirmButtonText: this.$t('sure'),
        }).then(() => {
          let newTabls = [] // 表格的新数据
          // form表单的数据调整
          let tmpMap = {}
          let tmpForm = self.selectDevForm
          let tables = self.tableData
          for (let i = 0; i < index; i++) { // 删除位置之前的顺序是一样的
            let key = 'validateDevId' + i
            tmpMap[key] = tmpForm[key]
            newTabls[i] = tables[i]
          }
          let len2 = self.boxNum - 1
          for (let i = index; i < len2; i++) {
            tmpMap['validateDevId' + i] = tmpForm['validateDevId' + (i + 1)]
            newTabls[i] = tables[i + 1]
          }
          self.$set(self, 'selectDevForm', tmpMap)
          self.$set(self, 'tableData', newTabls)
          self.boxNum--
        }).catch(() => {
        })
      },
      changeNum (val) { // 输入框的值改变的事情
        let tableLen = this.tableData.length
        if (tableLen > val) { // 减少了
          let formData = this.selectDevForm
          for (let i = val; i < tableLen; i++) { // 删除多余的值
            let key = 'validateDevId' + i
            if (formData.hasOwnProperty(key)) {
              delete formData[key]
            }
          }
          let datas = []
          for (let i = 0; i < val; i++) {
            datas.push(this.tableData[i])
          }
          this.$set(this, 'tableData', datas)
        } else {
          for (let i = tableLen; i < val; i++) {
            this.tableData.push({})
          }
        }
      },
      valiDateRepeatDevId (rule, value, callback) { // 验证选择的设备是否重复
        if (!value) {
          callback()
        } else {
          let devForm = this.selectDevForm
          let sameCount = 0
          for (let key in devForm) {
            if (devForm[key] === value) {
              sameCount++
            }
          }
          if (sameCount > 1) {
            callback(new Error(this.$t('cent.depEqu')))
          } else {
            callback()
          }
        }
      },
      /**
       *  根据ids获取一行的汇流箱的厂家和容量等信息数据
       * @param ids： Array 查询的汇流箱的id数组
       */
      getDevConfigByDevIds (ids) { // 根据ids获取一批行的汇流箱的厂家和容量等信息数据
        let len
        if (!ids || (len = ids.length) === 0) {
          return
        }
        let self = this
        DeviceInfoService.getDCJSDetail({ids: ids.join(',')}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          let datasMap = {}
          let len1 = datas.length
          debugger
          for (let i = 0; i < len1; i++) {
            let tmp = datas[i]
            tmp.devTypeName = this.$t('cent.conBox')
            datasMap[tmp.id] = tmp
          }
          // self.$set(this.tableData, index, data)
          // 修改表数据
          let selectDevForm = this.selectDevForm
          for (let i = 0; i < len; i++) { // 将获取的数据修改为表格的信息
            let devId = ids[i]
            // id = > 行号的转换，只有通过selectDevForm的转换
            for (let key in selectDevForm) {
              if (selectDevForm.hasOwnProperty(key) && selectDevForm[key] === devId) { // 找到了就退出循环
                let index = +(key.split('validateDevId')[1])
                let tmpRow = datasMap[devId] || {devTypeName: this.$t('cent.conBox')} // 没有获取到就是显示为空
                self.$set(this.tableData, index, tmpRow)
                break
              }
            }
          }
        })
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
  .centerInveterDialog{
    /deep/ .el-form-item {
      margin-bottom: 0
    }
    /deep/ .el-form-item__error{
      top: 0;
    }
  }
</style>
