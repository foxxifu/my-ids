<template>
  <el-dialog
    :title="$t('devUpgrade.dialog.title')"
    :visible="isShow"
    :close-on-click-modal="false"
    width="30%"
    :before-close="handleClose">

   <!-- <file-upload uploadUrl="/dev/device/uploadUpgradeFile" :otherData="{devIds: devIdsStr}">
    </file-upload>-->
   <el-upload
      ref="upload"
      action="/dev/device/uploadUpgradeFile"
      name="file"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-error="handlUploadFailed"
      :before-upload="beforeUpload"
      :multiple="false"
      :limit="1"
      :data="{devIds: devIdsStr}"
      :auto-upload="false"
    >
      <el-button slot="trigger" size="small" type="primary">{{ $t('devUpgrade.dialog.chooseFile') }}</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">{{ $t('devUpgrade.dialog.uploadService') }}</el-button>
    </el-upload>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
    import FileUpload from '@/components/fileUpload/index.vue'
    const props = {
      isShow: {
        type: Boolean,
        default: false
      },
      // 升级的设备ID集合
      devIds: {
        type: Array,
        default() {
          return []
        }
      }
    }
    export default {
      props,
      name: 'DevUpgradeUploadFile',
      components: {
        FileUpload
      },
      data() {
        return {
        }
      },
      created() {
      },
      filters: {},
      mounted: function () {
        this.$nextTick(function () {
        })
      },
      methods: {
        handleClose() {
          this.$emit('update:isShow', false)
        },
        submitUpload() {
          if (this.devIdsStr.length === 0) {
            this.$message.error(this.$t('devUpgrade.dialog.noDevInfo'))
            return
          }
          this.$confirm(this.$t('devUpgrade.dialog.sureUpload')).then(() => {
            this.$refs.upload.submit()
          })
        },
        handleRemove(file, fileList) {
          console.log(file, fileList)
        },
        handlePreview(file) {
          console.log(file)
        },
        handleUploadSuccess (res, file) { // 文件上传成功后的事件
          if (res.code === 1) { // 上传成功
            this.$emit('uploadSuccess')
            this.$message(this.$t('components.fileUpload.uploadSuc'))
            this.handleClose()
          } else {
            this.$message.error(res.results || this.$t('components.fileUpload.uploadFail'))
            this.$emit('on-upload-error', res.message || this.$t('components.fileUpload.uploadFail'))
          }
        },
        handlUploadFailed (err1, file, fileList) { // 失败后就不显示对话框
          this.$message.error(this.$t('components.fileUpload.uploadFail'))
          console.warn(this.$t('components.fileUpload.delErr'), err1)
        },
        beforeUpload(file) {
          // TODO 验证文件的后缀
          console.log(file)
        }
      },
      watch: {
      },
      computed: {
        devIdsStr() {
          if (this.devIds) {
            return this.devIds.join(',')
          } else {
            return ''
          }
        }
      }
    }
</script>

<style lang="less" scoped>
</style>
