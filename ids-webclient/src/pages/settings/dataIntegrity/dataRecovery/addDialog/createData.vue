<template>
  <!-- 创建数据补采的数据 -->
  <el-dialog
    :title="$t('dataRecovery.createDialog.add')"
    :visible="isShow"
    width="40%"
    :before-close="handleClose">
    <el-form ref="form" :model="formData" label-width="140px">
      <!-- 补采设备 -->
      <el-form-item :label="$t('dataRecovery.createDialog.minigDev')"
                    prop="devId" :rules="[{required: true, message: $t('dataRecovery.createDialog.chooseMinigDev')}]">
        <!--<choose-dev-dialog-input :myValues="formData.devId" :myTexts="formData.devName"
                                 :isMutiSelect="false" isCheckDevType
                      @device-change="devChange"></choose-dev-dialog-input>-->
        <el-select v-model="formData.devId" placeholder="请选择设备" clearable filterable>
          <el-option v-for="dev in scDevList" :label="dev.devAlias" :value="dev.id" :key="dev.id"></el-option>
        </el-select>
      </el-form-item>
      <!-- 补采时间 -->
      <el-form-item :label="$t('dataRecovery.createDialog.miningTime')"
                    prop="timeRange" :rules="[{required: true, message: $t('dataRecovery.createDialog.chooseMiningTime')}]">
        <el-date-picker
          v-model="formData.timeRange"
          type="datetimerange"
          :format="$t('dateFormat.yyyymmddhhmmss')"
          value-format="timestamp"
          range-separator="~"
          :picker-options="pkOptions"
          :start-placeholder="$t('dataRecovery.createDialog.bcStartTime')"
          :end-placeholder="$t('dataRecovery.createDialog.bcEndTime')">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <div style="margin-right: 140px;text-align: center;">
          <!-- 新增 -->
          <el-button type="primary" @click="onSubmit" :disabled="isSend">{{$t('create')}}</el-button>
          <!-- 取消 -->
          <el-button @click="handleClose" :disabled="isSend">{{$t('cancel')}}</el-button>
        </div>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  // import ChooseDevDialogInput from '@/components/chooseDevice/index.vue'
  import DataMinigService from '@/service/dataMining'
  import DevService from '@/service/device'
  // 选择范围超过提醒的时间
  const RANG_TIME = 7 * 24 * 60 * 60 * 1000
  // 最大的可选择的时间,因为当天的数据做补采，可能数据还没有来，并且kpi重计算的时候会导致重复的请求，就只有选择已经做了kpi重计算的时间
  const MAX_DATE = new Date(new Date().format('yyyy-MM-dd') + ' 00:00:00').getTime()
  const props = {
    isShow: { // 是否显示弹出框
      type: Boolean,
      default () {
        return false
      },
      required: true
    }
  }
  export default {
    props,
    components: {
      // ChooseDevDialogInput
    },
    data () {
      return {
        formData: { // 表单数据
          // 时间范围
          timeRange: '',
          devId: null,
          devName: null
        },
        pkOptions: {
          disabledDate: function (date) { // 配置最大可以选择的时间
            return date.getTime() >= MAX_DATE
          }
        },
        // 查询数采Dev列表
        scDevList: [],
        // 是否正在保存数据
        isSend: false
      }
    },
    filters: {},
    created() {
      this.findAllScDevs()
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      handleClose () { // 关闭弹出框
        if (this.isSend) { // 数据提交中
          this.$message(this.$t('dataRecovery.createDialog.dataIsSubmit')) // 数据正在提交，请稍后操作...
          return
        }
        this.$emit('update:isShow', false)
      },
      // 查询所有的Modbus协议的数采设备
      findAllScDevs() {
        DevService.findAllBindScDevs().then(resp => {
          this.scDevList = (resp.code === 1 && resp.results) || []
        })
      },
      onSubmit () { // 表单提交
        this.$refs.form.validate(valid => {
          if (valid) {
            this.isSend = true
            let {devId, devName} = this.formData
            let data = {devId, devName}
            let timeArr = this.formData.timeRange
            data.startTime = timeArr[0]
            data.endTime = timeArr[1]
            if (timeArr[1] - timeArr[0] > RANG_TIME) { // 超过一个星期
              // 选择时间请尽量保持在一个星期以内，当前选择的时间大于一个星期，确认提交?
              this.$confirm(this.$t('dataRecovery.createDialog.largRangeTip'))
                .then(() => { // 提交
                  this.sendData(data)
                }).catch(() => {
                  this.isSend = false
                })
            } else { // 直接提交
              this.sendData(data)
            }
          }
        })
      },
      devChange (vals, texts, rows) { // 选择设备改变的事件
        this.$set(this.formData, 'devId', vals)
        this.$set(this.formData, 'devName', texts)
      },
      sendData (data) {
        DataMinigService.saveData(data).then(resp => {
          this.isSend = false
          if (resp.code === 1) {
            this.$emit('save-success')
            this.handleClose()
            this.$message(this.$t('operatSuccess')) // 操作成功
          } else {
            this.$message(this.$t('operatFailed')) // 操作失败
          }
        }).catch(() => {
          this.isSend = false
          this.$message(this.$t('serviceError')) // 服务异常
        })
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
