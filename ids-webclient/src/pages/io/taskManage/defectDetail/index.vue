<template>
  <!-- 流程详情 -->
  <el-dialog
    title="消缺任务"
    :visible="isShowDefectDetail"
    :close-on-click-modal="false"
    :before-close="beforeClose"
    width="80%"
    append-to-body>
    <div class="defect-detail">
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('io.defect.taskDetail')" name="taskDetal">
          <!-- 任务详情的页面 -->
          <!-- 1.缺陷的任务表格 -->
          <div style="overflow-y: auto;" :style="{'max-height': maxHeight + 'px'}">
            <table cellpadding="0" cellspacing="0" width="100%">
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.taskSerial')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ tableDatas.defectId }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.taskName')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ tableDatas.defectName }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.plantName')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ stationName }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.plantform.plantAddr')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ tableDatas.stationAddr }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.alarm.devName')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ tableDatas.devAlias }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.devModel')}}</td>
                <td class="val-label" width="30%" align="center">{{ tableDatas.devTypeName }}</td>
                <td class="name-label" width="20%" align="center">{{$t('io.processStatus')}}</td>
                <td class="val-label last-label" align="center" width="30%">
                  <label :class="['proc-state', 'proc-state' + tableDatas.procState]">{{ tableDatas.procState | procStateFliter(vm) }}</label>
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.startTime')}}</td>
                <td class="val-label" width="30%" align="center">
                  {{ tableDatas.createTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
                </td>
                <td class="name-label" width="20%" align="center">{{$t('io.defect.dealTime')}}</td>
                <td class="val-label last-label" align="center" width="30%">
                  {{ tableDatas.updateTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.taskProcPersion')}}</td>
                <td class="val-label" width="30%" align="center">{{ tableDatas.currentUserName }}</td>
                <td class="name-label" width="20%" align="center">{{$t('io.defect.taskCreateUser')}}</td>
                <td class="val-label last-label" align="center" width="30%">
                  {{ tableDatas.createUserName }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defectDesc')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  {{ tableDatas.description }}
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.files')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  <fileDownload :list="dataList"></fileDownload>
                </td>
              </tr>
              <tr class="table-row">
                <td class="name-label" width="20%" align="center">{{$t('io.defect.defectNodes')}}</td>
                <td class="val-label last-label" colspan="3" width="80%">
                  <workflow-user :list="userList"></workflow-user>
                </td>
              </tr>
            </table>
            <!-- 如果是执行 -->
            <div v-if="isDoExect">
              <!-- 待分配 -->
              <div v-if="isPending">
                <div style="width: 280px;margin:10px auto;">
                  <el-button type="primary" @click="accept" :disabled="isSubmitting">{{$t('io.defect.accept')}}</el-button>
                  <el-button type="danger" @click="showTaskTransfer" :disabled="isSubmitting">{{$t('io.defect.toOtherUser')}}</el-button>
                  <el-button type="info" @click="beforeClose">{{$t('io.defect.back')}}</el-button>
                </div>
              </div>
              <!-- 消缺中 -->
              <div v-if="isProcessing">
                <div class="do-excute-title">{{$t('io.defect.taskHuiBao')}}</div>
                <table cellpadding="0" cellspacing="0" width="100%">
                  <tr class="table-row">
                    <td class="name-label" width="20%" align="center">{{$t('io.defect.suggestOfSH')}}</td>
                    <td class="val-label last-label" width="80%">
                      <el-input type="textarea" v-model="formData.taskContent"></el-input>
                    </td>
                  </tr>
                  <tr class="table-row">
                    <td class="name-label" width="20%" align="center">{{$t('io.defect.uploadFile')}}</td>
                    <td class="val-label last-label" width="80%">
                      <!--<file-upload :fileId.sync="tableDatas.fileId"></file-upload>-->
                      <file-upload style="display: inline-block;margin-right: 10px;" :fileId.sync="formData.fileId" @on-upload-success="onUploadSuccess"></file-upload>
                      <file-download style="display: inline-block" v-if="formData.fileId" :list="fileInfo" @delSuccess="delFile" isCanDel></file-download>
                    </td>
                  </tr>
                </table>
                <div style="width: 200px;margin:10px auto;">
                  <el-button type="primary" @click="dealSubmit" :disabled="isSubmitting">{{$t('io.defect.sendHuiBao')}}</el-button>
                  <el-button type="info" @click="beforeClose">{{$t('cancel')}}</el-button>
                </div>
              </div>
              <!-- 待审核 -->
              <div v-if="isWaiting">
                <div class="do-excute-title">{{$t('io.defect.taskSH')}}</div>
                <table cellpadding="0" cellspacing="0" width="100%">
                  <tr class="table-row">
                    <td class="name-label" width="20%" align="center">{{$t('io.defect.suggestOfSH')}}</td>
                    <td class="val-label last-label" width="80%">
                      <el-input type="textarea" v-model="formData.taskContent"></el-input>
                    </td>
                  </tr>
                  <tr class="table-row">
                    <td class="name-label" width="20%" align="center">{{$t('io.defect.uploadFile')}}</td>
                    <td class="val-label last-label" width="80%">
                      <!--<file-upload :fileId.sync="tableDatas.fileId"></file-upload>-->
                      <file-upload style="display: inline-block;margin-right: 10px;" :fileId.sync="formData.fileId" @on-upload-success="onUploadSuccess"></file-upload>
                      <file-download style="display: inline-block" v-if="formData.fileId" :list="fileInfo" @delSuccess="delFile" isCanDel></file-download>
                    </td>
                  </tr>
                </table>
                <div style="width: 280px;margin:10px auto;">
                  <el-button type="primary" @click="agreeSubmit(1)" :disabled="isSubmitting">{{$t('io.defect.agreeAndPass')}}</el-button>
                  <el-button type="danger" @click="agreeSubmit(2)" :disabled="isSubmitting">{{$t('io.defect.toBack')}}</el-button>
                  <el-button type="info" @click="beforeClose">{{$t('cancel')}}</el-button>
                </div>
              </div>
            </div>
            <div v-else style="width:100%;">
              <el-button type="primary" @click="beforeClose" style="margin-left: 49%;margin-top: 10px;">{{$t('close')}}</el-button>
            </div>
            <div>
              <el-steps direction="vertical" :active="(flowList && flowList.length) || 0">
                <el-step v-for="(item, index) in flowList" :key="index" :title="item.taskStartTime | timestampFomat" icon="el-icon-success">
                  <div slot="description">
                    <div v-if="item.taskState === '-1'">
                      <div>{{$t('io.defect.createUser')}}:{{createUserName}}</div>
                      <div>{{$t('io.defect.taskSerial')}}:{{defectNumber}}</div>
                      <div>{{$t('io.defectDesc')}}:
                        <div v-html="textHtmlFilter(description)"></div>
                      </div>
                    </div>
                    <div v-if="item.taskState === '0'">
                      {{$t('io.defect.taskAsses')}}：{{item.assigneeName}}
                    </div>
                    <div v-if="item.taskState === '-2'">
                      {{$t('io.defect.excuteUser')}}：{{item.transferorName}}<br/>
                      {{$t('io.defect.toOtherDealUser')}}: {{item.transferorName}}
                    </div>
                    <div v-if="item.taskState === '1'">
                      {{$t('io.defect.sendTask')}}：{{item.assigneeName}}
                      <div>{{$t('io.defectDesc')}}:
                        <div v-html="textHtmlFilter(item.taskContent)"></div>
                      </div>
                    </div>
                    <div v-if="item.taskState === '2'">
                      {{$t('io.defect.taskSP')}}：{{item.assigneeName}}
                      <div>
                        {{$t('io.defect.suggestOfSP')}}:{{item.taskContent}}
                      </div>
                    </div>
                    <div v-if="item.taskState === '3'">
                      {{$t('io.defect.taskFinish')}}
                    </div>
                    <div v-if="item.fileId">
                      <fileDownload :list="[{fileId: item.fileId, name: item.originalName, fileExt: item.fileExt}]"></fileDownload>
                    </div>
                  </div>
                </el-step>
              </el-steps>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('io.defect.affiliateAlarm')" name="second">
          <div>
            {{$t('io.defect.affiliateAlarmNum')}} <label style="color:#8CC756;">{{ alarmNum }}</label> {{$t('io.defect.ge')}}
            <div style="overflow-y: auto;" :style="{'max-height': (maxHeight - 45) + 'px'}">
              <alarm-table v-for="(item, index) in alarmList" :key="item.id" :pBtnClass="(index === 0 && 'el-icon-arrow-up') || 'el-icon-arrow-down'" :alarmData="item"></alarm-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <task-transfer v-if="isTaskTransfer" @on-success-cb="transferSuccess" :dialogVisible.sync="isTaskTransfer" :defectId="defectId"></task-transfer>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import fileDownload from '@/components/fileDownload/index.vue'
  import alarmTable from '@/pages/io/taskManage/defectDetail/alarmTable.vue';
  import taskTransfer from '@/pages/todo/taskTransfer.vue'
  import workflowService from '@/service/workFlow'
  import WorkflowUser from './workflowOfUser.vue'
  import FileUpload from '@/components/fileUpload/index.vue'

  const props = {
    defectId: {
      type: Number,
      default: 0
    },
    isShowDefectDetail: {
      type: Boolean,
      custom: true,
      default: false
    },
    isDoExect: { // 是否是执行
      type: Boolean,
      custom: true,
      default: false
    }
  }
  export default {
    created () {
      this.calMaxHeight();
      if (this.defectId) {
        this.searchDatas();
      }
    },
    props: props,
    components: {
      fileDownload,
      alarmTable,
      taskTransfer,
      WorkflowUser,
      FileUpload
    },
    data () {
      return {
        maxHeight: 120,
        activeName: 'taskDetal',
        isTaskTransfer: false, // 是否显示转派的任务
        tableDatas: {}, // 表格数据
        formData: {}, // 如果是执行的时候，就需要form表单提交数据
        taskList: [], // 任务列表
        stationName: '智慧光伏电站A', // 电站名称
        dataList: [],
        myContent: null, // 提交审核审核的信息
        userList: [], // 处理节点的用户流
        flowList: [], // 最后显示所有的节点任务
        defectNumber: {},
        description: {},
        createUserName: {},
        alarmNum: 0, // 告警的数量
        alarmList: {},
        fileInfo: [], // 文件信息
        isSubmitting: false, // 是否在执行
        vm: this
      }
    },
    filters: {
      procStateFliter (val, vm) {
        if (!val || isNaN(val)) {
          return ''
        }
        if (val === '0') {
          // 待分配
          return vm.$t('io.defectStatusArr')[0]
        }
        if (val === '1') {
          // 消缺中
          return vm.$t('io.defectStatusArr')[1]
        }
        if (val === '2') {
          // 待审核
          return vm.$t('io.defectStatusArr')[2]
        }
        if (val === '3') {
          // 已完成
          return vm.$t('io.defectStatusArr')[3]
        }
        // '未知'
        return vm.$t('io.unKnow')
      },
      formatFlow: function (value, vm) { // 对属性格式化的方法
        if (value === '-1') {
          // 任务创建人
          return vm.$t('io.defect.userTaskArr')[0]
        }
        if (value === '0') {
          // 任务接受人
          return vm.$t('io.defect.userTaskArr')[1]
        }
        if (value === '1') {
          // 任务消缺人
          return vm.$t('io.defect.userTaskArr')[2]
        }
        if (value === '2') {
          // '任务审核人'
          return vm.$t('io.defect.userTaskArr')[3]
        }
        if (value === '3') {
          // '任务已完成'
          return vm.$t('io.defect.userTaskArr')[4]
        }
        // '未知'
        return vm.$t('io.unKnow')
      }
    },
    mounted: function () {
      let self = this
      this.$nextTick(function () {
      })
      window.onresize = () => self.calMaxHeight
    },
    methods: {
      agreeSubmit (state) { // 同意的提交
        let self = this;
        if (undefined !== state && state === 1) {
          self.formData.optType = '1';
        } else if (undefined !== state && state === 2) {
          self.formData.optType = '2';
        }
        self.realExcuteWorkflow()
      },
      dealSubmit () { // 处理的提交
        let self = this;
        self.realExcuteWorkflow()
      },
      accept () { // 接受的提交
        let self = this;
        self.formData.optType = '1';
        self.realExcuteWorkflow()
      },
      realExcuteWorkflow () { // 真正执行流程的方法
        this.isSubmitting = true
        let self = this
        workflowService.executeWorkFlowDefect(self.formData).then(resp => {
          if (resp && resp.code === 1) {
            self.$message(self.$t('operatSuccess'));
            this.beforeClose();
            this.onSuccessCallback()
          } else {
            self.$message(resp.message);
          }
          this.isSubmitting = false
        }).catch(() => {
          this.isSubmitting = false
        });
      },
      searchDatas () { // 根据缺陷id查询缺陷的信息
        let self = this;
        workflowService.getWorkFlowDefectDetails(this.defectId).then(resp => {
          var datas = (resp.code === 1 && resp.results) || {};
          if (self.isDoExect) {
            self.formData = Object.assign({}, datas)
            self.formData.fileId = null // 不能使用相同的文件
            self.formData.tasks = null // 去掉任务的信息
          }
          self.tableDatas = datas;
          if (undefined !== datas && undefined !== datas.tasks && datas.tasks.length > 0) {
            self.defectNumber = datas.defectId;
            self.description = datas.description;
            self.createUserName = datas.createUserName;
            self.alarmNum = datas.alarmNum;
            workflowService.getDefectAlarm({'alarmIds': datas.alarmIds, 'alarmType': datas.alarmType}).then(resp => {
              if (resp && resp.code === 1) {
                self.$set(self, "alarmList", resp.results);
                console.log(self.alarmList)
              } else {
                self.$message(resp.message);
              }
            });
            var temp = [];
            var users = [];
            users.push({ 'id': -2, 'name': datas.createUserName, 'status': '-1' });
            if (datas.fileId) {
              temp.push({
                'name': datas.originalName, 'fileId': datas.fileId, fileExt: datas.fileExt
              })
            }
            for (var i = 0; i < datas.tasks.length; i++) {
              users.push({'id': i, 'name': datas.tasks[i].assigneeName, 'status': datas.tasks[i].taskState});
              let task = datas.tasks[i]
              let fileId = task.fileId
              if (!fileId) { // 只有有fileId的才有下载的附件
                continue
              }
              temp.push({'name': task.originalName, 'fileId': fileId, fileExt: task.fileExt})
            }
            if (datas.procState === 3) {
              users[users.length] = { 'id': users.length, 'name': datas.createUserName, 'status': datas.procState };
            }
            self.$set(self, "dataList", temp);
            self.$set(self, "userList", users);
            datas.tasks.unshift({'taskState': '-1', 'taskStartTime': datas.createTime, fileId: datas.fileId, 'originalName': datas.originalName, fileExt: datas.fileExt});
            self.$set(self, "flowList", datas.tasks);
          }
        });
      },
      calMaxHeight () {
        let height = document.documentElement.clientHeight - 230
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      },
      showTaskTransfer () {
        this.isTaskTransfer = true;
      },
      textHtmlFilter (val) {
        if (!val) {
          return '-'
        }
        return val.replace(/\n|\r\n/g, "<br/>")
      },
      onSuccessCallback () { // 操作成功后的回掉函数，通知上级
        this.$emit('on-success-cb')
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
      transferSuccess () { // 转派成功的事件
        this.beforeClose();
        this.onSuccessCallback()
      },
      beforeClose () { // 关闭弹出框的调用的方法
        // this.isShow = false;
        this.$emit('update:isShowDefectDetail', false) // 调用父节点调用组件的时候isShowAddDefect.sync同步修改
        // this.$emit('child-say', false); // 调用父节点的方法 父节点需要做绑定 on-bind:child-say的事件
      }
    },
    watch: {},
    computed: {
      isPending () { // 是否是待分配的内容
        return this.tableDatas.procState && this.tableDatas.procState === '0';
      },
      isProcessing () { // 是否是消缺中
        return this.tableDatas.procState && this.tableDatas.procState === '1';
      },
      isWaiting () { // 是否是待审核
        return this.tableDatas.procState && this.tableDatas.procState === '2';
      },
    },
    beforeDestroy () {
      console.log('desroy DefectDetail')
    }
  }
</script>

<style lang="less" scoped>
  .defect-detail {
    /deep/ .table-row {
      td.name-label, td.val-label {
        padding: 10px 5px;
        border-left: 1px solid #dedede;
        border-top: 1px solid #dedede;
        color: #000;
        &.last-label {
          border-right: 1px solid #dedede;
        }
        &.name-label {
          background-color: rgba(200, 200, 200, 0.3);
          vertical-align: middle; // 垂直居中
        }
      }
      &:last-child > td {
        border-bottom: 1px solid #dedede;
      }
    }
    .do-excute-title {
      font-size: 16px;
      font-weight: bold;
    }
    label.proc-state {
      // 缺陷的颜色
      &.proc-state1 { // 待审核
        color: #f33;
      }
      &.proc-state2 { // 消缺中
        color: #f90;
      }
      &.proc-state3 { // 待审核
        color: #3399FF;
      }
      &.proc-state4 { // 已完成
        color:#009900;
      }
    }
    /deep/ .el-step.is-vertical .el-step__icon.is-icon {
      width: 25px;
    }
  }

  /deep/ .el-dialog {
    margin-top: 10px !important;
    .el-step__head.is-finish {
      color: #009DD9;
      border-color: #009DD9;
    }
  }
</style>

