<template>
  <!-- 新增消缺的界面 -->
  <el-dialog
    :title="$t('io.defect.addDefect')"
    :visible="isShowAddDefect"
    :close-on-click-modal="false"
    :before-close="beforeClose"
    width="80%" append-to-body>
    <div class="add-defect">
      <el-form ref="addDefectForm" :rules="rules" :model="formData" status-icon>
        <table cellpadding="0" cellspacing="0" width="100%">
          <tr class="table-row">
            <td class="name-label" width="20%" align="center"><label style="color: red;">*</label>{{$t('io.defect.taskName')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
              <el-input v-model="formData.defectName"></el-input>
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center"><label style="color: red;">*</label>{{$t('io.defect.affiliateAlarm')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
              <el-input v-model="formData.alarmNames" @focus="chooseAlarm" :placeholder="$t('io.alarm.selectSameDevToDefect')"></el-input>
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center">{{$t('io.plantName')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
                {{ formData.stationName }}
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center">{{$t('io.plantform.plantAddr')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
              {{ formData.stationAddr }}
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center">{{$t('io.alarm.devName')}}</td>
            <td class="val-label" width="30%" align="center">{{ formData.devAlias }}</td>
            <td class="name-label" width="20%" align="center">{{$t('io.devType')}}</td>
            <td class="val-label last-label" align="center" width="30%">
              {{ formData.devType }}
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center">{{$t('io.defect.devModel')}}</td>
            <td class="val-label" width="30%" align="center">{{ formData.devVersion }}</td>
            <td class="name-label" width="20%" align="center"><label style="color: red;">*</label>{{$t('io.defect.taskProcPersion')}}</td>
            <td class="val-label last-label" align="center" width="30%">
              <choose-user :treeData="allUsers" :placeholder="$t('io.defect.firstSelectAlarm')" :valueMode.sync="formData.userId"
                           @item-click="itemClick"></choose-user>
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center"><label style="color: red;">*</label>{{$t('io.defect.startTime')}}</td>
            <td class="val-label" width="30%" align="center">
              <el-date-picker
                v-model="formData.findTime"
                type="datetime"
                :format="$t('dateFormat.yyyymmddhhmmss')"
                value-format="timestamp" :editable="false"
                :placeholder="$t('io.defect.startTime')">
              </el-date-picker>
            </td>
            <td class="name-label" width="20%" align="center">{{$t('io.defect.dealTime')}}</td>
            <td class="val-label last-label" align="center" width="30%">-</td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center"><label style="color: red;">*</label>{{$t('io.defectDesc')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
              <el-input type="textarea" v-model="formData.description"></el-input>
            </td>
          </tr>
          <tr class="table-row">
            <td class="name-label" width="20%" align="center">{{$t('io.defect.uploadFile')}}</td>
            <td class="val-label last-label" colspan="3" width="80%">
              <file-upload style="display: inline-block;margin-right: 10px;" :fileId.sync="formData.fileId" @on-upload-success="onUploadSuccess"></file-upload>
              <file-download style="display: inline-block" v-if="formData.fileId" :list="fileInfo" @delSuccess="delFile" isCanDel></file-download>
            </td>
          </tr>
        </table>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button @click="beforeClose">{{$t('cancel')}}</el-button>
        <el-button type="primary" @click="submitForm">{{$t('sure')}}</el-button>
      </span>
    <SelectAlarmDialog v-if="isShowChooseAlarm" @choose-alarm="chooseAlarms"
                       :is-show-choose-alarm.sync="isShowChooseAlarm"></SelectAlarmDialog>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import SelectAlarmDialog from '@/pages/io/alarm/chooseAlarmDialog/index.vue'
  import UserService from '@/service/user'
  import DeviceService from '@/service/device'
  // import OperationService from '@/service/operation'
  import WorkFlowService from '@/service/workFlow'
  import chooseUser from '@/components/chooseTreeSelect/index.vue'
  import FileUpload from '@/components/fileUpload/index.vue'
  import fileDownload from '@/components/fileDownload/index.vue'

  const props = {
    rowDatas: {
      type: Object,
      custom: true,
      default: () => ({})
    },
    //    formData: {
    //      type: Object,
    //      custom: true,
    //      default: () => ({})
    //    },
    isShowAddDefect: {
      type: Boolean,
      custom: true,
      default: false
    },
    alarmType: {
      type: Number,
      default: 1 // 1：设备告警的类型  2：智能告警的类型
    }
  }
  export default {
    created () {
      this.formData = Object.assign({}, this.rowDatas)
      if (this.formData.stationCode) { // 如果有电站信息，就查询电站
        this.getUserByStationCode(this.formData.stationCode)
      }
    },
    props: props,
    components: {
      SelectAlarmDialog,
      chooseUser,
      FileUpload,
      fileDownload
    },
    data () {
      return {
        isShowChooseAlarm: false,
        formData: {
          alarmIds: null, // 告警id
          alarmType: 1, // 设备类型的告警 1.设备告警 2：智能告警
          reviceUserName: '', // 任务处理人
          userId: null, // 任务处理人id
        },
        allUsers: [],
        rules: {
          defectName: [
            {required: true, message: this.$t('io.defect.inputDefectName'), trigger: 'blur'},
          ]
        },
        fileInfo: []
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      /**
       * 告警选择后的回调函数
       * @param chooseRows 选择的告警的行 数组array
       * @param devId 选择告警的设备id
       * @param stationCode 选择告警的电站编号
       */
      chooseAlarms (chooseRows, devId, stationCode) { // 选择告警的事件
        let alarmIds = []
        let alarmNames = []
        let len = (chooseRows && chooseRows.length) || 0
        this.allUsers = []
        if (len === 0) {
          this.formData.alarmIds = null;
          this.formData.alarmNames = null;
          this.formData.reviceUserName = '';
          this.formData.userId = null;
          this.setFormAttrs({})
          return;
        }
        for (let i = 0; i < len; i++) {
          let tempAlarm = chooseRows[i]
          alarmIds.push(tempAlarm.id)
          alarmNames.push(tempAlarm.alarmName)
        }
        this.formData.alarmIds = alarmIds.join(',')
        this.formData.alarmNames = alarmNames.join(',') + ';'
        this.formData.stationName = chooseRows[0].stationName
        // 1.获取用户,是根据选择的告警所在的电站确定处理人员
        this.getUserByStationCode(stationCode)
        // 获取设备信息
        this.getDevById(devId)
      },
      getUserByStationCode (stationCode) { // 获取电站下的用户信息
        let allUsers = this.allUsers = []
        if (!stationCode) { // 没有电站编号就不查询用户信息
          return
        }
        let self = this
        // 1.获取用户,是根据选择的告警所在的电站确定处理人员
        UserService.getEnterpriseUser({stationCode: stationCode}).then(resp => {
          var datas = (resp.code === 1 && resp.results) || []
          let userLen = datas.length
          if (userLen === 0) {
            return
          }
          let tempStation = {} // 当前的电站
          tempStation.label = self.formData.stationName
          tempStation.stationCode = stationCode
          allUsers.push(tempStation)
          let clds = tempStation.children = []
          for (let i = 0; i < userLen; i++) { // 电站添加人员
            clds.push({
              label: datas[i].loginName,
              id: datas[i].id,
            })
          }
        })
      },
      getDevById (devId) { // 查询设备信息
        let self = this
        // 查询设备的相关信息
        DeviceService.getDevById(devId).then(resp => {
          var datas = (resp.code === 1 && resp.results) || {};
          self.setFormAttrs(datas)
        })
      },
      setFormAttrs (datas) {
        this.$set(this.formData, 'stationName', datas.stationName || '')
        this.$set(this.formData, 'stationCode', datas.stationCode || '')
        // 设备地址
        this.$set(this.formData, 'stationAddr', datas.stationAddr || '')
        this.$set(this.formData, 'devAlias', datas.devAlias || '')
        this.$set(this.formData, 'devId', datas.id || null)
        this.$set(this.formData, 'devType', datas.devTypeName || '')
        this.$set(this.formData, 'devVersion', datas.signalVersion || '')
      },
      querySearchAsync (queryString, cb) { // 过滤，当获取焦点的时候就给值
        let allUsers = this.allUsers
        // 过来出需要的据结果
        var results = queryString ? allUsers.filter(this.createStateFilter(queryString)) : allUsers
        cb(results)
      },
      createStateFilter (queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0)
        }
      },
      chooseAlarm (ev) { // 关联告警获取告警的框
        this.isShowChooseAlarm = true
      },
      itemClick (data) { // 选择用户后执行的事件
        if (data) {
          this.formData.reviceUserName = data.label
        }
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
      submitForm () { // 提交form表单
        // 验证信息
        if (!this.formData.defectName) { // '缺陷任务必填'
          this.$message(this.$t('io.defect.inputDefectName'))
          return
        }
        if (!this.formData.alarmIds) {
          this.$message(this.$t('io.defect.noAlarm'))
          return
        }
        if (!this.formData.userId || this.allUsers.length === 0) {
          this.$message(this.$t('io.defect.chooseUser'))
          return
        }
        // TODO 保存信息
        let self = this
        this.formData.alarmType = this.alarmType;
        WorkFlowService.saveDefect(this.formData).then(resp => {
          if (resp.code === 1) {
            self.$message(this.$t('operatSuccess'))
            self.$emit('saveSuccess')
            self.beforeClose()
          } else {
            self.$message(this.$t('operatFailed'))
          }
        })
      },
      beforeClose () { // 关闭弹出框的调用的方法
        // this.isShow = false;
        // this.$emit('setIsShowAddDefect'); // 调用父节点的方法
        this.$emit('update:isShowAddDefect', false) // 调用父节点调用组件的时候isShowAddDefect.sync同步修改
        // this.$emit('child-say', false); // 调用父节点的方法 父节点需要做绑定 on-bind:child-say的事件
      }
    },
    watch: {},
    computed: {},
    beforeDestroy () {
      console.log('desroy add defect')
    }
  }
</script>

<style lang="less" scoped>
  .add-defect {
    .table-row {
      td.name-label, td.val-label {
        color: #000;
        padding: 12px 5px;
        border-left: 1px solid #dedede;
        border-top: 1px solid #dedede;
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
  }
</style>

