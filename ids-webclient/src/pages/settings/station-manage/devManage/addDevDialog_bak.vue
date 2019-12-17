<!-- 新增设备，目前只支持新增上能协议类型的设备 -->
<template>
  <div class="dev-update-dialog-parent">
    <el-dialog title="设备接入" :visible="isShowDialog" width="30%" :before-close="handleClose">
      <el-form ref="addDevForm" :model="formData" label-width="120px" :rules="rules" class="demo-ruleForm">
        <el-form-item label="设备名称" prop="devName" :rules="[{required:true, message: '设备名称必填'}]">
          <el-input v-model="formData.devName"></el-input>
        </el-form-item>
        <el-form-item label="设备版本" :rules="[{required:true, message: '设备版本必选'}]" prop="signalVersion">
          <!-- 如果设备devId存在就不能够修改，否则是新增就可以修改，选择需要创建的版本信息 -->
          <el-select v-model="formData.signalVersion" placeholder="请选择版本" @change="modelVersionChange">
            <!-- 版本信息是从后台获取的 -->
            <el-option v-for="item in modelVersionArr" :key="item.key" :label="item.val" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电站名称">
          <div>{{formData.stationName}}</div>
        </el-form-item>
        <el-form-item label="设备类型">
          <div>{{formData.devTypeName}}</div>
        </el-form-item>
        <el-form-item label="设备SN号" prop="snCode">
          <!-- SN号 -->
          <el-input v-model="formData.snCode" @blur="validateSn"></el-input>
        </el-form-item>
        <el-form-item label="设备关联数采" prop="parentId" :rules="[{required:formData.devTypeId !== 2, message: '设备关联数采必选'}]" >
          <el-select v-model="formData.parentId" placeholder="请选择" :disabled="formData.devTypeId !== 2 && isNotUse">
            <!-- 数采数据是从后台获取的 -->
            <el-option v-for="item in scDevList" :key="item.id" :label="item.devAlias" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="二级地址" prop="secondAddress">
          <!-- 有了SN号就不能修改了 -->
          <el-input v-model="formData.secondAddress" placeholder="1~255" :disabled="isNotUse"></el-input>
        </el-form-item>
        <el-form-item label="经纬度" required>
          <input-for-position
            :lat="formData.latitude"
            :lng="formData.longitude"
            @myChange="dataCirleChange"
          ></input-for-position>
          <!--<el-col :span="12">-->
            <!--<el-form-item label="经度" label-width="60px" prop="longitude">-->
              <!--<el-input v-model="formData.longitude" maxLength="9"></el-input>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <!--<el-col :span="12">-->
            <!--<el-form-item label="纬度" label-width="60px" prop="latitude">-->
              <!--<el-input v-model="formData.latitude"  maxLength="8"></el-input>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
        </el-form-item>
        <!--<el-form-item label="地图定位">-->
          <!--<el-input suffix-icon="el-icon-location" @focus="isShowPostion = true"></el-input>-->
        <!--</el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="handleClose">取消</el-button>
      </div>
    </el-dialog>
    <!--<choose-position v-if="isShowPostion"-->
                     <!--:isDialogVisible.sync="isShowPostion" :lat.sync="formData.latitude" :lng.sync="formData.longitude"-->
                     <!--@data-circle-change="dataCirleChange"-->
    <!--&gt;</choose-position>-->
  </div>
</template>

<script type="text/ecmascript-6">
  import InputForPosition from '@/components/choosePosition/inputForPosition.vue'
  // import devService from '@/service/device'
  import devAccessService from '@/service/devAccess'
  import myValidate from './myValidate'

  const props = {
    isShowDialog: {
      type: Boolean,
      default: false
    },
    stationName: { // 电站名称
      type: String,
      default: null
    },
    stationCode: { // 电站编号
      type: String,
      default: null
    }
  }
  export default {
    props: props,
    created () {
      this.$set(this.formData, 'stationCode', this.stationCode)
      this.$set(this.formData, 'stationName', this.stationName)
      this.queryDevVersionInfo()
      this.querySCDevs()
    },
    components: {
      InputForPosition
    },
    data () {
      return {
        formData: {}, // 表单数据
        // 表单验证规则
        rules: {
          latitude: [
            {required: true, message: '纬度必填'},
            {validator: myValidate.validateWdRange}
          ],
          longitude: [
            {required: true, message: '经度必填'},
            {validator: myValidate.validateJdRange}
          ],
          secondAddress: [
            {required: true, message: '二级地址必填'},
            {validator: myValidate.secendAdressRange}
          ]
        },
        isNotUse: false, // 是否启用
        modelVersionArr: [], // sn协议下的设备版本信息的集合
        modelCodeToProtocalCodeObj: {}, // 通过版本号获取协议了想
        modelCodeToDevTypeObj: {}, // 通过版本号获取设备类型
        scDevList: [] // 数采设备信息
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      queryDevVersionInfo () { // 查询设备版本信息,每次进入的时候查询, 查询到设备的版本和协议和设备类型等信息
        let self = this
        self.modelVersionArr = []
        self.modelCodeToProtocalCodeObj = {}
        self.modelCodeToDevTypeObj = {}
        devAccessService.getSNSignalVersionList().then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          let len = datas.length
          for (let i = 0; i < len; i++) {
            let one = datas[i]
            self.$set(self.modelVersionArr, i, {key: one.signalVersion, val: one.name})
            self.$set(self.modelCodeToProtocalCodeObj, one.signalVersion, one.protocolCode)
            self.$set(self.modelCodeToDevTypeObj, one.signalVersion, {
              devTypeName: one.devTypeName,
              devTypeId: one.devTypeId,
            })
          }
        })
      },
      querySCDevs () { // 查询数采设备
        devAccessService.getSNDevList({stationCode: this.stationCode}).then(resp => {
          let scDevList = (resp.code === 1 && resp.results) || []
          this.scDevList = scDevList
        })
      },
      modelVersionChange (val) { // 版本信息修改的时候的事件
        this.formData.devTypeId = this.modelCodeToDevTypeObj[val].devTypeId
        this.formData.devTypeName = this.modelCodeToDevTypeObj[val].devTypeName
        this.validateSn()
      },
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      dataCirleChange (lat, lng) { // 选择位置改变后的事件
        this.$set(this.formData, 'latitude', lat)
        this.$set(this.formData, 'longitude', lng)
      },
      submitForm () { // 表单提交的信息
        let self = this
        this.$refs.addDevForm.validate((valid) => {
          if (valid) {
            // TODO 提交表单 this.formData 的数据给后台
            console.log(this.formData)
            devAccessService.insertDevInfo(self.formData).then(resp => {
              if (resp.code === 1) {
                self.$message('添加成功')
                self.$emit('on-insert-success')
                self.handleClose()
              } else {
                self.$message(resp.message || '新增失败')
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      validateSn () { // sn号的验证,对于新增界面的设备都是snmodbus协议的设备，这里就不做协议类型的验证了
        let self = this
        let snCode = this.formData.snCode
        let singVersion = this.formData.signalVersion
        if (snCode && singVersion) { // 有sn号和选择了版本号就需要后台的验证
          // 假设后台根据snCode去获取数据验证数据
          devAccessService.checkAndQuerySnInfo({signalVersion: singVersion, snCode: snCode}).then(resp => {
            if (resp.code === 1) { // 请求成功
              let results = resp.results
              let type = results.type
              // 1、验证通过；2、设备已存在；3、第三方设备；4、父设备不存在；
              if (type === 1) { // 验证成功并且获取了
                // 获取的数据
                // self.isNotUse = true // 设置为不可用
                // 假设验证通过，获取的数采id =2， 二级地址=3
                let data = results.data // TODO 后台去获取对应的数采和二级地址的数据是data = {scId: 2, portNumber: 3}
                self.$set(self.formData, 'parentId', data.parentId)
                self.$set(self.formData, 'secondAddress', data.unitId)
              } else if (type === 2) { // SN号对应的设备已接入
                this.$message('设备已接入,不能继续修改，请修改对应的sn或者删除对应sn的设备，否则无法继续修改')
                self.$set(self.formData, 'parentId', null)
                self.$set(self.formData, 'secondAddress', null)
                // self.isNotUse = true // 设置为不可用
              } else if (type === 3) { // 接入的是第三方设备（非上能协议的设备）
                self.$set(self.formData, 'parentId', null)
                self.$set(self.formData, 'secondAddress', null)
                self.isNotUse = false // 可以任意的修改
              } else if (type === 4) { // 父设备不存在
                this.$message('父设备不存在')
                self.$set(self.formData, 'parentId', null)
                self.$set(self.formData, 'secondAddress', null)
                // self.isNotUse = true // 设置为不可用
              }
            } else { // 请求失败
              // this.isNotUse = true // 设置为不可用
              this.$set(this.formData, 'parentSn', null)
              this.$set(this.formData, 'secondAddress', null)
            }
          })
        } else { // 没有sn号的就需要选择内容
          this.isNotUse = false
          this.$set(this.formData, 'parentSn', null)
          this.$set(this.formData, 'secondAddress', null)
        }
      },
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>

