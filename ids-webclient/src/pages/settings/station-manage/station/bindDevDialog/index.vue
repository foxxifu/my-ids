<template>
  <el-dialog
    :title="$t('bindDevDialog.title')"
    :visible="isShowBindDev"
    :close-on-click-modal="false"
    width="30%"
    :before-close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="180px">
    <el-form-item prop="scDevId" :label="$t('bindDevDialog.labelSelctSc')">
      <el-select v-model="form.scDevId" :placeholder="$t('bindDevDialog.pleaseChooseSc')">
        <el-option v-for="dev in list" :label="dev.devAlias" :value="dev.id" :key="dev.id"></el-option>
      </el-select>
    </el-form-item>
    </el-form>
    <div style="text-align: center;margin-top: 30px;">
      <el-button type="primary" @click="submitData">{{ $t('sure') }}</el-button>
      <el-button @click="handleClose">{{ $t('cancel') }}</el-button>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import DeviceService from '@/service/device'
  const props = {
    stationCode: {
      type: String,
      default: null
    },
    isShowBindDev: {
      type: Boolean,
      default: false
    }
  }
  export default {
    props,
    name: 'BindDevDialog',
    components: {},
    data () {
      return {
        list: [],
        form: {
          scDevId: null
        },
        rules: {
          scDevId: [
            {required: true, message: this.$t('bindDevDialog.labelSelctSc'), trigger: 'change'}
          ]
        }
      }
    },
    created() {
      this.searchScDevs()
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      searchScDevs() {
        DeviceService.findAllUnBindSCdevs().then(resp => {
          this.list = (resp.code === 1 && resp.results) || []
        })
      },
      handleClose() {
        this.$emit('update:isShowBindDev', false)
      },
      submitData() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            // 提交
            debugger
            const data = Object.assign({ stationCode: this.stationCode }, this.form)
            DeviceService.bindScToStation(data).then(resp => {
              if (resp.code === 1) {
                this.$message.success(this.$t('operatSuccess'))
                this.handleClose()
              } else {
                this.$message.error(this.$t('operatFailed'))
              }
            }).catch(() => {
              this.$message.error(this.$t('operatFailed'))
            })
          }
        })
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
