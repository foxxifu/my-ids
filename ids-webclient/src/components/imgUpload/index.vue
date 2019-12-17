<!-- 图片上传的组件 -->
<template>
  <div class="img-upload-parent">
    <!-- 如果不是指展示图片就可以做上传的事情 -->
    <el-upload v-if="!isShowImgOnly"
               class="avatar-uploader"
               :action="pathByFileIdObj.action"
               :show-file-list="false"
               :on-success="handleAvatarSuccess"
               :before-upload="beforeAvatarUpload" :title="$t('components.imgUpload.chooseImgUpload')">
      <img v-if="fileId" v-lazy="pathByFileIdObj.download" :onerror="errorImage" class="avatar">
      <i v-else  :class="['avatar-uploader-icon', 'el-icon-plus']"></i>
    </el-upload>
    <!-- 如果只展示图片 -->
    <img v-else v-lazy="pathByFileIdObj.download" :onerror="errorImage" class="avatar">
    <span v-if="isShowLoading && isLoading"><i class="el-icon-loading"></i>{{ $t('components.fileUpload.upload') }}</span>
  </div>
</template>

<script type="text/ecmascript-6">
  /**
   * 使用组件的api说明
   * @Author wq
   * TODO 考虑可以自己定义图片的宽和高
   * 上传图片的插件说明
   * 1. 属性(Attributes)
   *      1) fileId：String 图片的上传路径
   *      2) isShowImgOnly: Boolean 默认 false，是否值展示图片 true: 只展示图片， false：展示可以上传的形式
   *      3) defaultErrorImageUrl: String 当获取图片失败的时候显示的默认图片 ，默认'this.src="/assets/images/defaultPic.png"'
   *      4) uploadUrl: String 图片上传的url地址 可以不填使用默认的 '/biz/fileManager/fileUpload'
   *      5) downloadUrl: String 图片上传的url地址 可以不填使用默认的 '/biz/fileManager/downloadFile'
   *      6）isShowLoading：String 是否在上传的时候有等待框，默认true  , true: 有 ，false：无
   * 2. 事件(Events)
   *      1) on-upload-success 上传成功的回调函数 参数(fileId, file)
   *      2) on-upload-error 上传失败的回调函数 参数(message)
   * 3. 使用例子
   *      1) 在企业管理中企业缩略图有使用的例子
   *      关键代码：注意fileId后面需要添加sync，这样才能更新调用这个组件的元素的值
   *      <img-upload :isShowImgOnly="isDetal" :fileId.sync="enterModel.avatarPath"></img-upload>
   * */
  let props = {
    fileId: { // 当前文件的值,这个需要做同步的
      type: String,
      default: null
    },
    isShowImgOnly: { // 是否值展示图片 true: 只展示图片， false：展示可以上传的形式
      type: Boolean,
      default: false
    },
    defaultErrorImageUrl: { // 图片默认错误的地址
      type: String,
      default: null
    },
    uploadUrl: { // 图片上传的url地址
      type: String,
      default: null
    },
    downloadUrl: { // 下载的路径
      type: String,
      default: null
    },
    isShowLoading: { // 是否在上传的时候有等待框，默认true  , true: 有 ，false：无
      type: Boolean,
      default: true
    },
    isValidWH: { // 是否验证宽高 true:验证  false：不验证
      type: Boolean,
      default: false,
    },
    defaultWH: { // 如果需要验证宽高的宽和高
      type: Array,
      default: [40, 40, 0, 0] // 第一个是宽  第二个是高, 第三个参数：宽的误差，第四个参数：高的误差
    }
  }
  export default {
    props: props,
    created () {
      if (this.defaultErrorImageUrl) { // 错误图片的默认替换图片
        this.errorImage = 'this.src="' + this.defaultErrorImageUrl + '"'
      } else {
        this.errorImage = 'this.src="/assets/images/defaultPic.png"' // 错误的时候的图片信息
      }
      if (this.uploadUrl) { // 上传的url路径
        this.action = this.uploadUrl
      } else {
        this.action = '/biz/fileManager/fileUpload'
      }
      if (this.downloadUrl) { // 回显图片获取图片信息的url路径
        this.fileDownPath = this.downloadUrl
      } else {
        this.fileDownPath = '/biz/fileManager/downloadFile'
      }
    },
    components: {
    },
    data () {
      return {
        action: null, // 文件上传的路径, 默认值为： '/biz/fileManager/fileUpload'
        fileDownPath: null, // 显示图片的时候获取图片数据流的路径，默认值为：'/biz/fileManager/downloadFile'
        errorImage: null, // 错误的时候的图片信息，默认值为：'this.src="/assets/images/defaultPic.png"'
        time: Date.parse(), // 时间，下载时候的事件
        isLoading: false, // 是否等待上传,当在等待的时候加载
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      // 图片上传之前
      beforeAvatarUpload (file) { // 上传之前的验证
        const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif')
        const isLt2M = file.size / 1024 / 1024 < 2 // 图片大小
        if (!isJPG) {
          this.$message.error(this.$t('components.imgUpload.uploadOnlyFormat'))
        }
        if (!isLt2M) {
          this.$message.error(this.$t('components.imgUpload.uploadImgOnlySize'))
        }
        let isSuccess = isJPG && isLt2M
        if (this.isValidWH) { // 如果还需要验证宽高
          // this.$confirm("上传企业LOGO请上传40*40的图片，确定上传吗？")
          if (!isSuccess) {
            return false;
          }
          if (confirm(this.$t('components.imgUpload.logoSureUpload'))) {
            return true
          } else {
            return false
          }
          // let self = this
          // return new Promise(function(resolve, reject) {
          //   let defaultWH = self.defaultWH || []
          //   let width = defaultWH[0] || 40
          //   let height = defaultWH[1] || 40
          //   let wi = defaultWH[2] || 0
          //   let hi = defaultWH[3] || 0
          //   let _URL = window.URL || window.webkitURL;
          //   let img = new Image();
          //   img.onload = function() {
          //     let valid = Math.abs(img.width - width) <= wi && Math.abs(img.height - height) <= hi;
          //     if (valid) {
          //       resolve()
          //     } else {
          //       reject()
          //     }
          //   }
          //   img.src = _URL.createObjectURL(file);
          // }).then(() => {
          //   self.isLoading = true
          //   return true;
          // }, () => {
          //   self.$alert({message: '上传的icon必须是等于141*54!', btn: false})
          //   return false;
          // });
        } else {
          if (isSuccess) {
            this.isLoading = true
          }
          return isSuccess
        }
      },
      // 图片上传成功之后
      handleAvatarSuccess (res, file) {
        // 通知上级修改fileId
        if (res.code === 1 && res.results) {
          this.$emit('update:fileId', res.results)
          this.$emit('on-upload-success', res.results, file) // 上传成功的回调函数
          this.time = Date.parse() // 更新时间
        } else {
          this.$message.error(this.$t('components.fileUpload.imgUploadFail'))
          this.$emit('on-upload-error', res.message) // 上传失败的回调函数
        }
        this.isLoading = false
      },
    },
    watch: {},
    computed: {
      pathByFileIdObj () { // 图片回显获取文件的路径
        let obj = {}
        if (this.fileId) {
          obj.action = this.action + '?fileId=' + this.fileId // 文件上传的路径
          obj.download = this.fileDownPath + '?fileId=' + this.fileId + '&time=' + this.time // 文件下载的路径，避免不更新的情况
          return obj
        }
        obj.action = this.action
        obj.download = ''
        return obj
      },
    }
  }
</script>

<style lang="less" scoped>
  .img-upload-parent{ // 文件上传的样式修改
    .avatar-uploader /deep/ .el-upload {
      border: 2px dashed #409EFF;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
      border-color: #409EFF;
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      line-height: 100px;
      text-align: center;
    }
    .avatar {
      width: 100px;
      height: 100px;
      display: block;
    }
  }
</style>

