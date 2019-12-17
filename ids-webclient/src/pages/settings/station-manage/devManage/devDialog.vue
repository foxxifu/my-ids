<!-- 设备修改的弹出框 -->
<template>
  <div class="dev-update-dialog-parent">
    <el-dialog :title="$t('devMan.modDev')" :visible="isShowDialog" width="35%" :before-close="handleClose">
      <el-form ref="devForm" :model="formData" label-width="170px" :rules="rules" class="demo-ruleForm">
        <el-form-item :label="$t('devMan.devName')" prop="devAlias">
          <el-input v-model="formData.devAlias"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.devVersion')" required prop="signalVersion">
          <!-- 如果设备devId存在就不能够修改，否则是新增就可以修改，选择需要创建的版本信息 -->
          <el-select v-model="formData.signalVersion" :placeholder="$t('devMan.chooseVersion')" disabled>
            <!-- 版本信息是从后台获取的 -->
            <el-option v-for="item in modelVersionArr" :key="item.key" :label="item.key" :value="item.val"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('devMan.plantName')">
          <div>{{formData.stationName}}</div>
        </el-form-item>
        <el-form-item :label="$t('devMan.devType')">
          <div>{{ formData.devTypeId && $t('devTypeId.' + formData.devTypeId) || ''}}</div>
        </el-form-item>
        <el-form-item :label="$t('devMan.devSNNum')" prop="esnCode">
          <!-- SN号 -->
          <el-input v-model="formData.snCode" @blur="validateSn"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.devPassword')" v-if="formData.protocolCode === 'MQTT'" prop="mqttPassword">
          <!-- mqtt密码 -->
          <el-input v-model="formData.mqttPassword"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.equCorData')" v-if="formData.protocolCode === 'SNMODBUS'" prop="parentId" :required="formData.devTypeId !== 2">
          <!-- 有了SN号就不能修改了 -->
          <el-select v-model="formData.parentId" :placeholder="$t('devMan.choose')" :disabled="formData.devTypeId !== 2 && isNotUse">
            <!-- 数采数据是从后台获取的 -->
            <el-option v-for="item in scDevList" :key="item.id" :label="item.devAlias" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('devMan.secAddress')" prop="secondAddress">
          <!-- 有了SN号就不能修改了 -->
          <el-input v-model="formData.secondAddress" placeholder="1~255" :disabled="isNotUse"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.lonAndLet')">
          <!--<el-col :span="12">-->
            <!--<el-form-item label="纬度" label-width="60px" prop="longitude" required>-->
              <!--<el-input v-model="formData.longitude"></el-input>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <!--<el-col :span="12">-->
            <!--<el-form-item label="纬度" label-width="60px" prop="latitude" required>-->
              <!--<el-input v-model="formData.latitude"></el-input>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <input-for-position
            :lat="formData.latitude"
            :lng="formData.longitude"
            @myChange="dataCirleChange"
          ></input-for-position>
        </el-form-item>
        <!--<el-form-item label="地图定位">-->
          <!--<el-input suffix-icon="el-icon-location" @focus="isShowPostion = true"></el-input>-->
        <!--</el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('devMan.preservation') }}</el-button>
        <el-button @click="handleClose">{{ $t('cancel') }}</el-button>
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
  import devService from '@/service/device'
  import devAccessService from '@/service/devAccess'
  import myValidate from './myValidate'

  const props = {
    devId: { // 设备id, 如果传入了id就是修改，没有传入id就是添加
      type: Number,
      default: null
    },
    isShowDialog: { // 是否显示对话框
      type: Boolean,
      default: false
    }
  }
  export default {
    props: props,
    created () {
      this.queryDevVersionInfo() // 查询版本信息
      if (this.devId) { // 查询设备的信息
        this.queryDevByDevId()
      }
    },
    components: {
      InputForPosition
    },
    data () {
      return {
        formData: {},
        rules: {
          devAlias: [
            {required: true, message: this.$t('devMan.devNameReq')}
          ],
          latitude: [
            {required: true, message: this.$t('devMan.latReq')},
            {validator: myValidate.validateWdRange}
          ],
          longitude: [
            {required: true, message: this.$t('devMan.letReq')},
            {validator: myValidate.validateJdRange}
          ],
        },
        isNotUse: false, // 是否关联的数采和二级地址是否可用
        modelVersionArr: [], // 版本信息
        modelCodeToProtocalCodeObj: {}, // 版本信息到协议的转换
        modelCodeToDevTypeObj: {}, // 版本信息到
        scDevList: [] // 数采信息
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
        console.log(this.formData.stationCode)
        devAccessService.getSNDevList({stationCode: this.formData.stationCode}).then(resp => {
          let scDevList = (resp.code === 1 && resp.results) || []
          this.scDevList = scDevList
        })
      },
      queryDevByDevId () { // 根据设备id查询设备信息
        let self = this
        devService.getDevById(self.devId).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          self.$set(self, 'formData', datas)
          // self.$set(self.formData, 'devId', self.devId) // 表单设置id
          // self.validateSn() // 查询成功后默认去执行以下验证是否可以行
          self.querySCDevs()
        })
      },
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      dataCirleChange (lat, lng) { // 选择位置改变后的事件
        this.$set(this.formData, 'latitude', lat)
        this.$set(this.formData, 'longitude', lng)
      },
      submitForm () { // 提交表单
        let self = this
        this.$refs.devForm.validate((valid) => {
          if (valid) {
            // TODO 提交表单 this.formData 的数据给后台
            devService.updateDeviceById(self.formData).then(resp => {
              if (resp.code === 1) {
                self.$message(this.$t('devMan.modSuc'));
                self.handleClose()
                self.$emit('on-update-success')
              } else {
                self.$message(this.$t('devMan.modFail'));
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      validateSn() { // 验证SN号
        let self = this
        let snCode = this.formData.snCode
        let singVersion = this.formData.signalVersion
        if (snCode && singVersion && this.formData.protocolCode === 'SNMODBUS') { // 有sn号和选择了版本号就需要后台的验证
          // 假设后台根据snCode去获取数据验证数据
          devAccessService.checkAndQuerySnInfo({signalVersion: singVersion, snCode: snCode, id: this.formData.id}).then(resp => {
            if (resp.code === 1) { // 请求成功
              let results = resp.results
              let type = results.type
              // 1、验证通过；2、设备已存在；3、第三方设备；4、父设备不存在；
              if (type === 1) { // 验证成功并且获取了
                // 获取的数据
                // self.isNotUse = true // 设置为不可用
                // 假设验证通过，获取的数采id =2， 二级地址=3
                let data = results.data || {} // TODO 后台去获取对应的数采和二级地址的数据是data = {scId: 2, portNumber: 3}
                self.$set(self.formData, 'parentId', data.parentId)
                self.$set(self.formData, 'secondAddress', data.unitId)
              } else if (type === 2) { // SN号对应的设备已接入
                this.$message(this.$t('devMan.notModify'))
                // self.$set(self.formData, 'parentId', null)
                // self.$set(self.formData, 'secondAddress', null)
                // self.isNotUse = true // 设置为不可用
              } else if (type === 3) { // 接入的是第三方设备（非上能协议的设备）
                let data = results.data || {}
                if (data && data.unitId) {
                  self.$set(self.formData, 'parentId', data.parentId)
                  self.$set(self.formData, 'secondAddress', data.unitId)
                } else {
                  // self.$set(self.formData, 'parentId', null)
                  // self.$set(self.formData, 'secondAddress', null)
                }
                self.isNotUse = false // 可以任意的修改
              } else if (type === 4) { // 父设备不存在
                this.$message(this.$t('devMan.devNot'))
                self.$set(self.formData, 'parentId', null)
                self.$set(self.formData, 'secondAddress', null)
                // self.isNotUse = true // 设置为不可用
              }
            } else { // 请求失败
              // this.isNotUse = true // 设置为不可用
              // this.$set(this.formData, 'parentSn', null)
              // this.$set(this.formData, 'secondAddress', null)
            }
          })
        } else { // 没有sn号或者不是上能协议类型的
          this.isNotUse = false
        }
      }
    },
    watch: {
      'formData.signalVersion' (newVal) {
        if (this.formData.isMonitorDev !== '1') {
          if (newVal && this.modelCodeToDevTypeObj[newVal]) {
            this.formData.devTypeId = this.modelCodeToDevTypeObj[newVal].devTypeId
            this.formData.devTypeName = this.modelCodeToDevTypeObj[newVal].devTypeName
            this.validateSn()
          } else {
            // this.formData.devTypeId = null
            // this.formData.devTypeName = null
          }
        }
      }
    },
    computed: {
    }
  }
</script>

<style lang="less" scoped>
</style>

