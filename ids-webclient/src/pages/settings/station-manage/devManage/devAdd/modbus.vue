<template>
    <!-- modbus协议类型的设备新增 -->
    <div>
      <el-form ref="addDevForm" :model="formData" label-width="170px" :rules="rules" class="demo-ruleForm">
        <el-form-item :label="$t('devMan.devName')" prop="devName" :rules="[{required:true, message: $t('devMan.devNameReq')}]">
          <el-input v-model="formData.devName"></el-input>
        </el-form-item>
        <!-- 数采SN号 -->
        <el-form-item :label="$t('devMan.addDev.parentSn')" :rules="[{required:true, message: $t('devMan.addDev.needParentSn')}]" prop="parentSn">
          <el-select v-model="formData.parentSn" :placeholder="$t('devMan.addDev.needParentSn')">
            <!-- 版本信息是从后台获取的 -->
            <el-option v-for="item in scSnArr" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('devMan.devVersion')" :rules="[{required:true, message: $t('devMan.devVersionSelect')}]" prop="signalVersion">
          <!-- 如果设备devId存在就不能够修改，否则是新增就可以修改，选择需要创建的版本信息 -->
          <el-select v-model="formData.signalVersion" :placeholder="$t('devMan.chooseVersion')" @change="modelVersionChange">
            <!-- 版本信息是从后台获取的 -->
            <el-option v-for="item in modelVersionArr" :key="item.id" :label="item.name" :value="item.signalVersion"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('devMan.plantName')">
          <div>{{formData.stationName}}</div>
        </el-form-item>
        <el-form-item :label="$t('devMan.devType')">
          <div>{{ formData.devTypeId && $t('devTypeId.' + formData.devTypeId) || ''}}</div>
        </el-form-item>
        <!-- 设备SN号 -->
        <el-form-item :label="$t('devMan.addDev.devSn')">
          <el-input v-model="formData.snCode" :placeholder="$t('devMan.addDev.needDevSn')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.secAddress')" prop="secondAddress" :rules="[{required:true, message: $t('devMan.secAddressReq')}]">
          <!-- 设备的二级地址 -->
          <el-input v-model="formData.secondAddress" placeholder="1~255" min="1" max="255" maxlength="3"></el-input>
        </el-form-item>
        <el-form-item :label="$t('devMan.lonAndLet')" required>
          <input-for-position
            :lat="formData.latitude"
            :lng="formData.longitude"
            @myChange="dataCirleChange"
          ></input-for-position>
        </el-form-item>
      </el-form>
      <div style="text-align: center;">
        <el-button type="primary" @click="submitForm">{{ $t('devMan.preservation') }}</el-button>
        <el-button @click="handleClose">{{ $t('cancel') }}</el-button>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import InputForPosition from '@/components/choosePosition/inputForPosition.vue'
  import deviceService from '@/service/device'
  import devAccessService from '@/service/devAccess'
  import myValidate from '..//myValidate'

  const props = {
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
    components: {
      InputForPosition
    },
    created () {
      this.$set(this.formData, 'stationCode', this.stationCode)
      this.$set(this.formData, 'stationName', this.stationName)
      this.queryDevVersionInfo()
      this.getModbusParentSnList()
    },
    data () {
      return {
        formData: {
          protocolCode: 'MODBUS'
        }, // 表单数据
        // 表单验证规则
        rules: {
          latitude: [
            {required: true, message: this.$t('devMan.latReq')},
            {validator: myValidate.validateWdRange}
          ],
          longitude: [
            {required: true, message: this.$t('devMan.letReq')},
            {validator: myValidate.validateJdRange}
          ],
          secondAddress: [
            {required: true, message: this.$t('devMan.secAddressReq')},
            {validator: myValidate.secendAdressRange}
          ]
        },
        selectedModel: {}, // 当前选中的版本号
        isNotUse: false, // 是否启用
        modelVersionArr: [], // sn协议下的设备版本信息的集合
        modelCodeToProtocalCodeObj: {}, // 通过版本号获取协议了想
        modelCodeToDevTypeObj: {}, // 通过版本号获取设备类型
        scDevList: [], // 数采设备信息
        parentSnCodeList: [], // 获取的sn号
        orgParentSnCodeList: [], // 原始的sn号
        inputParentSn: '', // 手动输入添加的sn
        scSnArr: ['PSN1']
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
        // 获取modbus协议的点表
        devAccessService.getMqqtVersionList({protocal: 'MODBUS'}).then(resp => { // 获取协议的版本信息
          let datas = (resp.code === 1 && resp.results) || []
          self.modelVersionArr = datas;
        })
      },
      querySCDevs () { // 查询数采设备
        devAccessService.getSNDevList({stationCode: this.stationCode}).then(resp => {
          let scDevList = (resp.code === 1 && resp.results) || []
          this.scDevList = scDevList
        })
      },
      modelVersionChange (val) { // 版本信息修改的时候的事件
        let selectedModels = this.modelVersionArr.filter(item => val === item.signalVersion)
        if (selectedModels.length > 0) {
          this.selectedModel = selectedModels[0]
        } else {
          this.selectedModel = {}
        }
        this.formData.devTypeId = this.selectedModel.devTypeId
        this.formData.devTypeName = this.selectedModel.devTypeName
      },
      handleClose () { // 关闭对话框
        this.$emit('closeDialog')
      },
      dataCirleChange (lat, lng) { // 选择位置改变后的事件
        this.$set(this.formData, 'latitude', lat)
        this.$set(this.formData, 'longitude', lng)
      },
      submitForm () { // 表单提交的信息
        let self = this
        this.$refs.addDevForm.validate((valid) => {
          if (valid) {
            // 提交表单 this.formData 的数据给后台
            devAccessService.insertModbusDevInfo(self.formData).then(resp => {
              if (resp.code === 1) {
                self.$message(this.$t('operatSuccess'))
                // 2.通知后台去处理数据，因为不知道当前的协议是什么，就在所有的设备改变，清除缓存信息
                deviceService.clearMqqtToDbCache().then(resp => {
                  if (resp.code === 1) {
                    console.log(this.$t('devMan.clearMQTTSuc'))
                  } else {
                    console.log(this.$t('devMan.clearMQTTFail'))
                  }
                })
                self.$emit('on-insert-success')
                self.handleClose()
              } else {
                self.$message(resp.message || this.$t('devMan.addFail'))
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      getModbusParentSnList() { // 获取modbus父节点的sn号
        devAccessService.getModbusParentSnList({stationCode: this.stationCode}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          // this.orgParentSnCodeList = datas;
          this.scSnArr = [].concat(datas);
        })
      }

    },
    watch: {
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
