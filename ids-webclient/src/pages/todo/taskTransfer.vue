<template>
  <!-- 任务转派的框 -->
  <el-dialog
    :title="$t('todo.tranTask')"
    :visible="dialogVisible"
    width="50%"
    :close-on-click-modal="false"
    :before-close="colseHandler"
    class="task-transfer-dialog"
    append-to-body>
    <el-form ref="form" :model="formData" label-width="160px">
      <el-form-item :label="$t('todo.taskNum')">
        <div>
          {{formData.defectId}}
        </div>
      </el-form-item>
      <el-form-item :label="$t('todo.taskName')">
        <div>
        {{formData.defectName}}
        </div>
      </el-form-item>
      <el-form-item :label="$t('todo.alarmDev')">
        <div>
        {{formData.devAlias}}
        </div>
      </el-form-item>
      <el-form-item :label="$t('io.defectDesc')">
        <div>
        {{formData.description}}
        </div>
      </el-form-item>
      <el-form-item :label="$t('io.defect.createUser')">
        <div>
        {{formData.createUserName}}
        </div>
      </el-form-item>
      <el-form-item :label="$t('io.defect.toOtherDealUser')">
        <div>
          <choose-user :treeData="userList" :value-mode.sync="formData.userId" @item-click="selectUser"></choose-user>
        </div>
      </el-form-item>
      <el-form-item :label="$t('todo.tranYj')">
        <div class="my-text-area-div">
          <el-input type="textarea" v-model="formData.operationDesc"></el-input>
        </div>
      </el-form-item>
      <el-form-item :label="$t('todo.fj')">
        <div>
          <!--<file-upload :fileId.sync="formData.fileId"></file-upload>-->
          <file-upload style="display: inline-block;margin-right: 10px;" :fileId.sync="formData.fileId" @on-upload-success="onUploadSuccess"></file-upload>
          <file-download style="display: inline-block" v-if="formData.fileId" :list="fileInfo" @delSuccess="delFile" isCanDel></file-download>
        </div>
      </el-form-item>
    </el-form>
    <div style="width: 200px; margin: 10px auto;">
      <el-button type="primary" @click="submitHandler">{{$t('todo.tranTask')}}</el-button>
      <el-button @click="colseHandler">{{$t('cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import workFlowService from '@/service/workFlow'
  import chooseUser from '@/components/chooseTreeSelect/index.vue'
  import FileUpload from '@/components/fileUpload/index.vue'
  import FileDownload from '@/components/fileDownload/index.vue'

  const props = {
    dialogVisible: { // 对话框是否可见
      type: Boolean,
      custom: true,
      default: false
    },
    defectId: { // 工单号id
      type: Number,
      default: 0
    }
  }
  export default {
    props: props,
    components: {
      chooseUser,
      FileUpload,
      FileDownload
    },
    created () {
    },
    data () {
      return {
        formData: { // 表单的数据
        },
        userList: [],
        fileInfo: [] // 上传了的文件可以下载
      }
    },
    filters: {
    },
    mounted: function () {
      this.getTaskFormData();
      this.$nextTick(function () {
      });
    },
    methods: {
      submitHandler() {
        let self = this;
        self.formData.optType = '2';
        workFlowService.executeWorkFlowDefect(self.formData).then(resp => {
          if (resp && resp.code === 1) {
            self.$message(this.$t('operatSuccess'));
            this.$emit('update:dialogVisible', false);
            this.$emit('on-success-cb') // 转派成功后的回调函数
          } else {
            self.$message(resp.message);
          }
        });
      },
      colseHandler () { // 关闭对话框
        this.$emit('update:dialogVisible', false);
      },
      getTaskFormData () { // 获取表单数据
        // TODO 这里需要从后台查询出来，先固定写死
        let self = this;
        workFlowService.getWorkFlowDefectDetails(self.defectId).then(resp => {
          if (resp && resp.code === 1) {
            let datas = resp.results
            datas.fileId = null
            self.$set(self, 'formData', datas);
            workFlowService.getStationUser().then(resp => {
              if (resp && resp.code === 1) {
                self.$set(self, 'userList', resp.results);
              } else {
                self.$message(resp.message);
              }
            });
          } else {
            self.$message(resp.message);
          }
        });
      },
      selectUser (data) {
        console.log(data)
        this.formData.transferorName = data.label;
        this.formData.enterpriseId = data.enterpriseId;
      },
      onUploadSuccess (fileId, file) { // 上传文件成功的事件
        let result = [
          {
            fileId: fileId,
            name: file.name
          }
        ]
        this.fileInfo = result
      },
      delFile ({fileId, name}) { // 删除文件成功
        this.formData.fileId = null
        this.fileInfo = []
      },
      // getUserList () { // 获取当前的处理人用户信息
      //   // TODO 这里需要从后台查询出来，先固定写死
      //   this.userList = [ ] // tree的树节点数据
      // }
    },
    watch: {},
    computed: {
    }
  }
</script>

<style lang="less" scoped>
  .task-transfer-dialog {
    .el-form > .el-form-item {
      margin: 0;
      border-left: 1px solid #dedede;
      border-top: 1px solid #dedede;
      /deep/ label.el-form-item__label{
        text-align: center;
        padding: 5px 0;
      }
      /deep/ div.el-form-item__content{
        border-left: 1px solid #dedede;
        border-right: 1px solid #dedede;
        text-align: left;
        & > div { // 为了与左边有空隙，添加的一个div
          margin-left: 10px;
          padding: 5px 0;
          width: 100%;
          height: 100%;
          vertical-align: middle;
          &.my-text-area-div {
            width: 80%;
          }
        }
      }
      &:last-child{
        border-bottom: 1px solid #dedede;
      }
    }
  }
</style>

