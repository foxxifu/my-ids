<!-- 新增设备，目前只支持新增上能协议类型的设备 -->
<template>
  <div class="dev-update-dialog-parent">
    <el-dialog :title="$t('devMan.devAcc')" :visible="isShowDialog" width="35%" :before-close="handleClose">
      <el-row style="margin-bottom: 10px;">
        <el-col :span="6"><div style="text-align: right" v-text="$t('devMan.addDev.colType')"></div></el-col>
        <el-col :span="18">
          <el-select v-model="collctType" placeholder="请选择" style="padding-left: 10px;">
            <el-option
              v-for="item in collTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <!-- 添加mqtt设备 -->
      <mqtt v-if="collctType === 'MQTT'" :stationName="stationName" :stationCode="stationCode" @closeDialog="handleClose" @on-insert-success="onInsertSuccess"></mqtt>
      <!-- 添加modbus设备 -->
      <modbus v-else-if="collctType === 'MODBUS'" :stationName="stationName" :stationCode="stationCode" @closeDialog="handleClose" @on-insert-success="onInsertSuccess"></modbus>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import mqtt from './mqtt.vue'
  import modbus from './modbus.vue'

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
      this.getMqqtParentSnList()
    },
    components: {
      mqtt,
      modbus
    },
    data () {
      return {
        collTypes: [{value: 'MQTT', label: 'MQTT'}, {value: 'MODBUS', label: 'MODBUS'}],
        collctType: 'MQTT', // 采集类型,默认选中的是MQTT
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      handleClose () {
        this.$emit('update:isShowDialog', false)
      },
      onInsertSuccess () {
        this.$emit('on-insert-success')
      }
    },
    watch: {
    },
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>

