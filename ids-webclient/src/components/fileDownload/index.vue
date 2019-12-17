<template>
  <!-- 文件下载的组件 -->
  <div class="file-download">
      <!-- 如果是显示图片的方式 -->
      <div v-for="(item, index) in list" :key="index" v-if="isShowImg" style="position: relative;" class="file-item">
        <div class="file-item-img">
          <img :src="getFileSmallImg(item[myProps.fileExt], item)" width="40" height="60"/>
        </div>
        <i v-if="isCanDel" @click="deletFileByFileId(item)" :title="$t('delete')" class="el-icon-circle-close-outline mySelfBtn"></i>
        <div class="file-item-txt-btn">
          <div class="text-ellipsis" :title="item[myProps.name]">{{item[myProps.name]}}</div>
          <el-button @click="download(item)" type="info" icon="el-icon-download" :title="$t('components.fileDownload.downLoad')" circle></el-button>
          <!--<a :href="downloadFile(item)" :download="item.name"><el-button type="info" icon="el-icon-download" title="下载" circle></el-button></a>-->
        </div>
      </div>
      <!-- TODO isShowImg===false 这里考虑使用列表形式 -->
  </div>
</template>

<script type="text/ecmascript-6">
  import fetch from '@/utils/fetch'

  const ImageFileExtArr = ['jpg', 'png', 'gif'] // 文件的后缀
  // word文档的后缀
  const docFileExtArr = ['doc', 'docx', 'docm', 'dotx', 'dotm', 'csv', 'xls', 'xlsx', 'xlsm',
    'xltx', 'xltm', 'xlsb', 'xlam', 'pptx', 'pptm', 'ppsx', 'ppsx', 'potx', 'potm', 'ppam', 'txt']
  // 压缩包的后缀
  const tarFileExtArr = ['arg', 'cab', 'lzh', 'ace', 'tar', 'gz', 'uue', 'bz2', 'jar', 'iso']
  // 默认的属性
  const defaultProps = {
    fileId: 'fileId', // 文件的id
    name: 'name', // 文件名称
    fileExt: 'fileExt' // 文件类型，根据文件类型来确定显示什么图片的前缀
  }

  const props = {
    list: { // 传递过来的下载文件的集合
      type: Array,
      default: () => ([])
    },
    isShowImg: {
      type: Boolean,
      custom: true,
      default: true
    },
    downloadFileCallback: { // 点击下载按钮的回调函数
      type: Function,
      default: null
    },
    downloadUrl: {
      type: String,
      default: '/biz/fileManager/downloadFile'
    },
    isCanDel: { // 是否可以删除
      type: Boolean,
      default: false
    },
    delUrl: { // 如果可以删除文件删除文件的url
      type: String,
      default: '/biz/fileManager/fileDelete'
    },
    props: { // 默认的文件以及属性信息
      type: Object,
      default () {
        return Object.assign({}, defaultProps)
      }
    }
  }
  export default {
    created () {
      this.download = this.downloadFileCallback || this.downloadFile
      // this.myList = (this.list && this.list.length > 0 && this.list.slice(0, this.list.length)) || []
      this.myProps = Object.assign({}, defaultProps, this.props)
    },
    props: props,
    components: {
    },
    data () {
      return {
        download: null,
        // myList: null,
        myProps: null
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      /**
       * 下载文件按钮的方法， 主要是拼接url地址
       * @param item 下载的项
       */
      downloadFile (item) { // 点击下载按钮的方法,拼接url地址
        // return this.downloadUrl + '?fileId=' + item.fileId + '&time=' + Date.parse()
        let iframe = document.getElementById("myIframe_for_download_id");
        if (iframe) {
          iframe.src = this.downloadUrl + '?fileId=' + item[this.myProps.fileId] + '&time=' + Date.parse();
        } else {
          iframe = document.createElement("iframe");
          iframe.style.display = "none";
          iframe.src = this.downloadUrl + '?fileId=' + item[this.myProps.fileId] + '&time=' + Date.parse();
          iframe.id = "myIframe_for_download_id";
          document.body.appendChild(iframe);
        }
      },
      deletFileByFileId (item) { // 删除文件
        let self = this
        this.$confirm(this.$t('components.fileDownload.sureDel') + item[this.myProps.name] + this.$t('components.fileDownload.filters'), {
          confirmButtonText: this.$t('sure'),
          cancelButtonText: this.$t('cancel')}
        ).then(() => {
          fetch(self.delUrl, {fileIds: item[this.myProps.fileId]}, 'POST').then(resp => {
            if (resp.code === 1) {
              self.$message(this.$t('components.fileDownload.delSuc'))
              self.list.splice(self.list.indexOf(item), 1)
              self.$emit('delSuccess', item, this.myProps) // 删除成功调用的方法
            } else {
              self.$message(resp.message || this.$t('components.fileDownload.delFail'))
            }
          })
        })
      },
      getFileSmallImg (fileExt, item) { // 获取文件的类型显示的默认图标
        if (!fileExt) { // 返回默认的图片
          // 如果不存在，就去取文档的后缀
          let fileName = item[this.myProps.name]
          if (!fileName) {
            return 'assets/images/fileExt/mydefault.png' // 否则使用默认的
          }
          let index = fileName.lastIndexOf('.') // 找最后一个点结尾的后缀
          if (index === -1 || index === fileName.length) {
            return 'assets/images/fileExt/mydefault.png' // 否则使用默认的
          }
          fileExt = fileName.substring(index + 1)
        }
        fileExt = fileExt.toLowerCase()
        if (ImageFileExtArr.indexOf(fileExt) !== -1) { // 图片的图片
          return 'assets/images/fileExt/imgs.png'
        }
        if (docFileExtArr.indexOf(fileExt) !== -1) { // word等文档的图片
          return 'assets/images/fileExt/docs.png'
        }
        if (tarFileExtArr.indexOf(fileExt) !== -1) { // 压缩包的图片
          return 'assets/images/fileExt/tars.png'
        }
        return 'assets/images/fileExt/mydefault.png' // 否则使用默认的
      }
    },
    watch: {
    },
    computed: {},
    beforeDestroy () {
      let dom = document.getElementById('myIframe_for_download_id')
      if (dom) {
        dom.parentNode.removeChild(dom)
      }
    }
  }
</script>

<style lang="less" scoped>
  .file-download {
    .file-item {
      display: inline-block;
      width: 105px;
      background: rgba(180,180,180,0.4);
      &:not(:first-child){
        margin-left: 10px;
      }
      & > div {
        float: left;
        &.file-item-img{
          width: 40%;
          margin-top: 5px;
        }
        &.file-item-txt-btn{
          width: 55%;
          margin-left: 5%;
          margin-top: 3px;
        }
      }
    }
    .mySelfBtn{
      color: red;
      position: absolute;
      top: 1px;
      right: 2px;
      font-size: 10px;
      cursor: pointer;
    }
  }
</style>

