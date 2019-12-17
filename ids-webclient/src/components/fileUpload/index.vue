<template>
    <div class="file-upload-parent">
      <el-upload
        class="upload-demo"
        :action="getAction"
        :before-upload="beforeUpload"
        :on-success="handleUploadSuccess"
        :on-error="handlUploadFailed"
        :data="otherData"
        :show-file-list="false">
        <el-button slot="trigger" size="small" type="primary">{{ $t('components.fileUpload.chooseFile') }}</el-button>
        <div v-if="isShowTip" slot="tip" class="el-upload__tip">{{ $t('components.fileUpload.onlyUpload') }}</div>
        <span v-if="isShowLoading && isLoading"><i class="el-icon-loading"></i>{{ $t('components.fileUpload.upload') }}</span>
      </el-upload>
    </div>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * 上传图片的插件说明
   * 1. 属性(Attributes)
   *      1) fileId：String 图片的上传路径，这里如果要同步到调用的父节点需要在这个属性后添加sync(:fileId.sync="xxxxx")
   *      2) uploadUrl: String 图片上传的url地址 可以不填使用默认的 '/biz/fileManager/fileUpload'
   *      3) userValidate: Array 删除文件之前的验证文件格式的数组，默认为空 可以传入当前想要传递的文件格式 例如：['image/jpeg','image/png']
   *      4）fileTypeError：String 上传文件之前验证不通过的提示 默认为'请选择正确的文件',可以自行根据修改
   *      5) fileMax: Number 文件上传的最大值单位M，默认3M
   *      6) isShowTip: Boolean 是否显示文件上传的提示信息，默认true； 参数意义 true：显示 ，false：不显示
   *      7) isShowLoading: Boolean 是否显示上传时候的等待图片,默认true; 参数意义 true:显示，false:不显示
   * 2. 事件(Events)
   *      1) on-upload-success 上传成功的回调函数 参数(fileId, file)
   *      2) on-upload-error 上传失败的回调函数 参数(message)
   *      3) on-validate-success 当验证通过的时候的回调函数 参数(file)
   * 3. 使用例子
   *      1) 在转工单的地方使用了这个例子关键代码
   *      关键代码：注意fileId后面需要添加sync，这样才能更新调用这个组件的元素的值 addDefect/index.vue中使用了
   *      <file-upload :fileId.sync="formData.fileId"></file-upload>
   *      2) 在导入点表中使用了这个组件，关键代码
   *      <fileUpload
   *        style="padding-top: 5px;"
   *        uploadUrl="/biz/settings/signal/import"
   *        fileTypeError="请选择excel的文件上传"
   *        @on-validate-success="setInputText"
   *        :isShowTip="false"
   *        :userValidate="myFileType"
   *        ></fileUpload>
   * */
  const props = {
    fileId: { // 上传文件的字段,主要是显示的作用
      type: String,
      default: null
    },
    uploadUrl: { // 上传文件的路径
      type: String,
      default: null
    },
    userValidate: { // 文件类型的数组,那些文件格式是需要上传 file.type的内容：如图片的一种格式'image/jpeg'等字符串的数组
      type: [String, Array],
      default: null
    },
    fileTypeError: { // 如果用户传递过来的文件格式验证错误给的提示信息
      type: String,
      default: '请上选择正确的文件'
    },
    fileMax: { // 上传文件的最大值 单位M 默认最大3M
      type: Number,
      default: 3
    },
    isShowTip: { // 是否显示提示信息
      type: Boolean,
      default: true
    },
    isShowLoading: { // 是否在上传的过程中添加等待的图标
      type: Boolean,
      default: true
    },
    otherData: { // 其他的数据
      type: Object,
      default () {
        return {}
      }
    }
  }
  export default {
    props: props,
    components: {},
    created() {
      if (this.uploadUrl) { // 修改上传的路径
        this.action = this.uploadUrl
      }
    },
    data () {
      return {
        action: '/biz/fileManager/fileUpload', // 上传文件的路径 默认的上传的图片
        isLoading: false, // 是否等待上传,当在等待的时候加载
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      beforeUpload (file) { // 上传文件之前的验证, 目前值验证大小
        console.log(file.type)
        let isExeFile
        if (this.userValidate && this.userValidate.length > 0) {
          isExeFile = this.userValidate.indexOf(file.type) === -1
        }
        const isLt5M = file.size / 1024 / 1024 <= this.fileMax // 文件大小不超过5M
        if (isExeFile) {
          this.$message.error(this.fileTypeError || '')
        }
        if (!isLt5M) {
          this.$message.error(this.$t('components.fileUpload.noExceed') + this.fileMax + 'MB!')
        }
        let isSuccess = !isExeFile && isLt5M
        if (isSuccess) {
          this.$emit('on-validate-success', file, this.otherData) // 当验证通过的时候的回调函数
          this.isLoading = true // 验证通过后加载等待的框
        }
        return isSuccess
      },
      handleUploadSuccess (res, file) { // 文件上传成功后的事件
        if (res.code === 1) { // 上传成功
          this.$emit('update:fileId', res.results) // 通知父节点的属性改变
          this.$emit('on-upload-success', res.results, file)
          this.$message(this.$t('components.fileUpload.uploadSuc'))
          // TODO 是否显示列表
        } else {
          this.$message.error(res.results || this.$t('components.fileUpload.uploadFail'))
          this.$emit('on-upload-error', res.message || this.$t('components.fileUpload.uploadFail'))
        }
        this.isLoading = false // 完成后就不使用等待框了
      },
      handlUploadFailed (err1, file, fileList) { // 失败后就不显示对话框
        this.isLoading = false // 完成后就不使用等待框了
        this.$message.error(this.$t('components.fileUpload.uploadFail'))
        console.warn(this.$t('components.fileUpload.delErr'), err1)
      },
    },
    watch: {},
    computed: {
      getAction () { // 获取提交的路径，如果有fileId就需要给fileId给后台，做更新的事情
        if (this.fileId) {
          return this.action + '?fileId=' + this.fileId
        }
        return this.action
      }
    }
  }
</script>

<style lang="less" scoped>
  .file-upload-parent {
    .el-upload__tip {
      margin-top: -4px;
    }
  }
</style>

