<!-- 组串详情的界面 -->
<template>
  <el-dialog
    :title="$t('devMan.groupDet')"
    :visible="isShowDialog"
    width="80%"
    class="group-details-dialog"
    :before-close="handleClose">
    <div class="search-bar">
      <el-select v-model="groupNum" filterable :placeholder="$t('devMan.choose')">
        <el-option
          v-for="item in groupSearchDatas"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <div style="float: right;line-height: 50px;">
        <el-checkbox v-model="isConfigAll">{{ $t('devMan.batchConfig') }}</el-checkbox>
      </div>
    </div>
    <div class="list-table">
      <el-form status-icon :model="dynamicValidateForm" ref="groupDetailsForm">
        <el-table border
          :data="list"
          style="width: 100%"
          :maxHeight="maxHeight"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <factory-detail :moduleId="props.row.moduleVersionId"></factory-detail>
            </template>
          </el-table-column>
          <el-table-column
            :label="$t('devMan.groupStr')"
            prop="name" align="center">
          </el-table-column>
          <el-table-column
            :label="$t('devMan.groupStrFac')"
            prop="manufacturer" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.manufacturer" filterable :placeholder="$t('devMan.chooseFac')" @change="getmoduleVersionArr(scope)" required>
                <el-option
                  v-for="item in factoryArr"
                  :key="item.manufacturer"
                  :label="item.manufacturer"
                  :value="item.manufacturer">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            :label="$t('devMan.comType')"
            prop="moduleVersionId" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="'validateList.' + scope.$index + '.moduleVersionId' + scope.$index" :rules="[{required:true, message: $t('devMan.comModelReq'), trigger: 'blur'}]">
                <el-select v-model="dynamicValidateForm.validateList[scope.$index]['moduleVersionId' + scope.$index]" :placeholder="$t('devMan.chooseComType')"
                           filterable @change="moduleVersionIdChange(scope)">
                  <el-option
                    v-for="item in scope.row.moduleVersions"
                    :key="item.id"
                    :label="item.moduleVersion"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            :label="$t('devMan.nomPow')"
            prop="standardPower" align="center">
          </el-table-column>
          <el-table-column
            :label="$t('devMan.comNum')"
            prop="num" align="center" width="200px">
            <template slot-scope="scope">
              <el-form-item :prop="'validateList.' + scope.$index + '.num' + scope.$index" :rules="[
                {required:true, message: $t('devMan.groupReq'), trigger: ['blur']},
                {pattern:/^([1-9]|[1-9]\d|100)$/, message: $t('devMan.entInteger'), trigger: ['blur', 'change']}
                ]">
                <el-input v-model="dynamicValidateForm.validateList[scope.$index]['num' + scope.$index]" @blur="updateOtherNum(scope)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            :label="$t('devMan.groupCap')"
            prop="caption" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="'validateList.' + scope.$index + '.caption' + scope.$index" :rules="[
                {required:true, message: $t('devMan.groupCapReq'), trigger: ['blur', 'change']},
                {pattern:/^([1-9]\d{0,4}(\.\d{1,3})?)$/, message: $t('devMan.range'), trigger: ['blur', 'change']}
                ]">
                <el-input v-model="dynamicValidateForm.validateList[scope.$index]['caption' + scope.$index]" @blur="updateOtherCaption(scope)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="formSubmit">{{ $t('sure') }}</el-button>
      <el-button @click="handleClose">{{ $t('cancel') }}</el-button>
    </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import FactoryDetail from './showGroupFactoryDetail.vue'
  import devService from '@/service/device'

  const props = {
    isShowDialog: { // 是否显示对话框
      type: Boolean,
      default: false
    },
    devIds: { // 提交设备的id，id之间使用逗号隔开,由父节点确定保存哪些设备的组串信息
      type: String,
      default: null
    }
  }

  export default {
    props: props,
    components: {
      FactoryDetail
    },
    created () {
      this.findFactorys() // 查询组串的信息
      this.calcMaxHeight()
      for (let i = 1; i <= 24; i++) {
        this.groupSearchDatas.push({
          value: i,
          label: i + this.$t('devMan.str')
        })
      }
    },
    data () {
      return {
        groupNum: 6, // 组串串数
        groupSearchDatas: [],
        list: [],
        factoryArr: [], // 工厂的数据
        maxHeight: 120,
        rules: {},
        dynamicValidateForm: { // 动态验证的表单数据
          validateList: [] // 需要验证的数据
        },
        isConfigAll: false, // 是否批量配置
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
      window.onresize = () => this.calcMaxHeight
    },
    methods: {
      findFactorys () { // 获取组件厂家的信息
        let self = this
        devService.findFactorys({}).then(resp => {
          self.factoryArr = (resp.code === 1 && resp.results) || []
          self.getGroupData(self.groupNum)
        })
      },
      getGroupData (num) { // 当组串数据改变的时候当前的表的数据
        this.$refs.groupDetailsForm.resetFields();
        if (!this.factoryArr || this.factoryArr.length === 0) {
          return
        }
        let moduleVersions = (this.factoryArr[0] && this.factoryArr[0].moduleVersions[0] && this.factoryArr[0].moduleVersions) || []
        let moduleVersions0 = moduleVersions[0] || {}
        let moduleVersionId = moduleVersions0.id
        let standardPower = moduleVersions0.standardPower
        let manufacturer = this.factoryArr[0].manufacturer
        let list = []
        let validateList = []
        let valiOne
        for (let i = 1; i <= num; i++) {
          list.push({name: ('pv' + i),
            id: 'pvGrid_' + i,
            moduleVersionId: moduleVersionId,
            manufacturer: manufacturer,
            standardPower: standardPower,
            moduleVersions: moduleVersions // 当前的数据
          })
          valiOne = {} // 组装动态验证的数据
          let num = i - 1
          valiOne.name = 'pv' + i
          valiOne['moduleVersionId' + num] = moduleVersionId
          valiOne['manufacturer' + num] = manufacturer
          valiOne['standardPower' + num] = standardPower
          valiOne['moduleVersions' + num] = moduleVersions
          validateList.push(valiOne)
        }
        this.list = list
        this.dynamicValidateForm.validateList = validateList
      },
      calcMaxHeight () {
        let height = document.documentElement.clientHeight * 0.85 - 60 - 60 - 60 - 60 - 60 - 10
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      },
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      getmoduleVersionArr (scope) { // 根据组件厂家名称获取组件型号
        let index = scope.$index
        let manufacturer = scope.row.manufacturer
        let changeToData
        if (!manufacturer) {
          this.list[index].moduleVersions = changeToData = []
        } else {
          let factoryArr = this.factoryArr
          let len = factoryArr.length
          let tmp
          let isHas = false
          for (let i = 0; i < len; i++) {
            tmp = factoryArr[i]
            if (tmp.manufacturer === manufacturer) {
              let tmpV = tmp.moduleVersions || []
              changeToData = tmpV
              isHas = true
              break
            }
          }
          if (!isHas) {
            changeToData = []
          }
        }
        if (this.isConfigAll) { // 是否是批量配置
          let len2 = this.list.length
          for (let i = 0; i < len2; i++) {
            this.$set(this.list[i], 'moduleVersions', changeToData)
            this.list[i].moduleVersions = changeToData
            this.list[i].manufacturer = manufacturer
            if (changeToData.length > 0) {
              this.$set(this.dynamicValidateForm.validateList[i], 'moduleVersionId' + i, changeToData[0].id) // 选中第一项
              this.$set(this.list[i], 'moduleVersionId', changeToData[0].id) // 选中第一项
              this.$set(this.list[i], 'standardPower', changeToData[0].standardPower)// 设置属性的值
            } else {
              this.$set(this.dynamicValidateForm.validateList[i], 'moduleVersionId' + i, null) // 选中第一项
              this.$set(this.list[i], 'moduleVersionId', null) // 选中第一项
              this.$set(this.list[i], 'standardPower', null)// 设置属性的值
            }
          }
        } else {
          this.$set(this.list[index], 'moduleVersions', changeToData)
          // this.list[index].moduleVersions = changeToData
          if (changeToData.length > 0) {
            this.$set(this.dynamicValidateForm.validateList[index], 'moduleVersionId' + index, changeToData[0].id) // 选中第一项
            this.$set(this.list[index], 'moduleVersionId', changeToData[0].id) // 选中第一项
            this.$set(this.list[index], 'standardPower', changeToData[0].standardPower)// 设置属性的值
          } else {
            this.$set(this.dynamicValidateForm.validateList[index], 'moduleVersionId' + index, null) // 选中第一项
            this.$set(this.list[index], 'moduleVersionId', null) // 选中第一项
            this.$set(this.list[index], 'standardPower', null)// 设置属性的值
          }
        }
      },
      moduleVersionIdChange (scope) { // 组件型号改变的事件
        let index = scope.$index
        let moduleVersionId = this.dynamicValidateForm.validateList[index]['moduleVersionId' + index]
        let moduleVersions = this.list[index].moduleVersions
        let len = (moduleVersions && moduleVersions.length) || 0
        let selectModule
        for (let i = 0; i < len; i++) {
          let tmp = moduleVersions[i]
          if (tmp.id === moduleVersionId) {
            selectModule = tmp
            break
          }
        }
        // 先不管选择的厂家不一致的bug （即先改变个别的厂家然后在勾选批量配置，导致这个情况的bug）
        if (this.isConfigAll) { // 批量配置
          if (selectModule) {
            let len2 = this.list.length
            for (let i = 0; i < len2; i++) {
              if (selectModule) {
                this.$set(this.dynamicValidateForm.validateList[i], 'moduleVersionId' + i, selectModule.id) // 选中第一项
                this.$set(this.list[i], 'moduleVersionId', selectModule.id) // 选中第一项
                this.$set(this.list[i], 'standardPower', selectModule.standardPower)// 设置属性的值
              } else {
                this.$set(this.dynamicValidateForm.validateList[i], 'moduleVersionId' + i, null) // 选中第一项
                this.$set(this.list[i], 'moduleVersionId', null) // 选中第一项
                this.$set(this.list[i], 'standardPower', null)// 设置属性的值
              }
            }
          }
        } else { // 单个配置
          if (selectModule) {
            this.$set(this.dynamicValidateForm.validateList[index], 'moduleVersionId' + index, selectModule.id) // 选中第一项
            this.$set(this.list[index], 'moduleVersionId', selectModule.id) // 选中第一项
            this.$set(this.list[index], 'standardPower', selectModule.standardPower)// 设置属性的值
          } else {
            this.$set(this.dynamicValidateForm.validateList[index], 'moduleVersionId' + index, null) // 选中第一项
            this.$set(this.list[index], 'moduleVersionId', null) // 选中第一项
            this.$set(this.list[index], 'standardPower', null)// 设置属性的值
          }
        }
      },
      updateOtherNum (scope) { // 修改组串的串数
        if (this.isConfigAll) { // 批量配置
          let val = this.dynamicValidateForm.validateList[scope.$index]['num' + scope.$index]
          let len = this.list.length
          for (let i = 0; i < len; i++) {
            this.$set(this.dynamicValidateForm.validateList[i], 'num' + i, val)
          }
        }
      },
      updateOtherCaption (scope) { // 修改组串的容量
        if (this.isConfigAll) { // 批量配置
          let val = this.dynamicValidateForm.validateList[scope.$index]['caption' + scope.$index]
          let len = this.list.length
          for (let i = 0; i < len; i++) {
            this.$set(this.dynamicValidateForm.validateList[i], 'caption' + i, val)
          }
        }
      },
      formSubmit () { // 表单提交
        let self = this
        this.$refs.groupDetailsForm.validate((valid) => {
          if (valid) {
            // TODO 提交表单 this.formData 的数据给后台
            // 组织后台的数据
            let subDatas = [] // 组件厂家和组串以及容量等信息
            let datas = self.dynamicValidateForm.validateList
            let len = datas.length

            for (let i = 0; i < len; i++) {
              let one = datas[i]
              let tmp = {
                fixedPower: one['caption' + i], // 容量
                modulesNumPerString: one['num' + i], // 组串串数
                pvModuleId: one['moduleVersionId' + i] // 选择的厂家组件型号id
              }
              subDatas.push(tmp)
            }
            // 提交给后台的数据
            let postData = {
              num: self.groupNum,
              ids: self.devIds,
              modules: subDatas // 组件厂家和装机容量和组串的串数
            }
            devService.saveStationPvModule(postData).then(resp => {
              if (resp.code === 1) {
                self.$message(this.$t('devMan.saveSuc'));
                self.handleClose()
              } else {
                self.$message(resp.message);
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
    watch: {
      groupNum (newVal) { // 组串数量改变的时候的数据
        this.getGroupData(newVal)
      }
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
  .group-details-dialog{
    .search-bar{
      margin-bottom: 10px;
    }
    /deep/ .el-form-item {
      margin-bottom: 0;
    }
    /deep/ .el-form-item__error {
      top: 8px;
      left: 98px
    }
  }
</style>

