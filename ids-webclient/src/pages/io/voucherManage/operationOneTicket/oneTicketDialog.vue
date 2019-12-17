<template>
  <el-dialog :title="$t(dialogType)"
    :visible="isShowDialog"
    width="100%" top="0"
    append-to-body :close-on-click-modal="false"
    :before-close="handleClose">
    <div class="detail-main-box" :style="{height: currentHieght + 'px', overflow: 'auto'}">
      <el-form :model="ticketCreateForm" :rules="ticketCreateFormRules" ref="ticketCreateForm" label-position="right" :disabled="isDetail">
      <el-input v-model="ticketCreateForm.createDate" type="hidden" :value="Date.now()"></el-input>
      <div class="form-gzp">
        <p align="center" style="color:#113C6A; margin:0 0 20px; font-size: 24px;">
          <b><span class="text">{{$t('io.workTick.dqITick')}}</span></b>
        </p>
        <table ref="ticketData" class="ticketData" width="100%" cellpadding="0" cellspacing="0">
          <tr>
            <td height="25" align="right" style="width: 10%;">
              <label class="table-serialNum">1.&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item prop="alarmIds" :label="$t('io.defect.affiliateAlarm')">
                <div style="width: 72%; display: inline-block;">
                  <el-input v-model="ticketCreateForm.alarmNames" @focus="chooseAlarm" :placeholder="$t('io.workTick.alarmNeedSameDev')"></el-input>
                </div>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="25" align="right" style="width: 10%;">
              <label class="table-serialNum">2.&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item prop="chargeId" :label="$t('io.workTick.workFzr')">
                <div style="display: inline-block;">
                  <choose-user :treeData="allUsers" :label="$t('io.workTick.needChargeId')"
                               :valueMode.sync="ticketCreateForm.chargeId"></choose-user>
                </div>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="25" align="right">
              <label class="table-serialNum">&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item prop="groupId" :label="$t('io.workTick.workCy')" style="width: 68%;display: inline-block;">
                <div style="display: inline-block; width: 605px;">
                  <choose-user :treeData="allUsers" :isMutiues="true" :label="$t('io.workTick.needWorkCy')"
                               :valueMode.sync="ticketCreateForm.groupId" v-if="isShowDialog"></choose-user>
                </div>
              </el-form-item>
              <el-form-item prop="ticketCreateFormPeopleCount" :label="$t('io.workTick.totalPersion')"
                            style="width: 20%; display: inline-block;">
                <input type="text" readonly v-model="ticketCreateFormPeopleCount" value="0" :min="0"
                       style="width:120px"/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="25" align="right" valign="top">
              <label class="table-serialNum">3.&nbsp;</label>
            </td>
            <td align="left" colspan="2" style="vertical-align: middle;">
              <el-form-item prop="workContent" :label="$t('io.workTick.workTask')">
                <div style="width: 90%;">
                  <!-- 请输入内容-->
                  <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                            v-model="ticketCreateForm.workContent"></el-input>
                </div>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="25" align="right">
              <label class="table-serialNum">4.&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item prop="workAddress" :label="$t('io.workTick.workPlace')">
                <el-input type="text" v-model="ticketCreateForm.workAddress" style="width: 810px;"></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="25" align="right">
              <label class="table-serialNum">5.&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item prop="workTime" :label="$t('io.workTick.plantWorkTime')">
                <el-date-picker v-model="ticketCreateForm.workTime" type="datetimerange"
                                :range-separator="$t('io.workTick.to')" :start-placeholder="$t('io.defect.startTime')" :end-placeholder="$t('io.defect.endTime')"
                                value-format="timestamp">
                </el-date-picker>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="36" align="right" valign="top">
              <label class="table-serialNum">6.&nbsp;</label>
            </td>
            <td align="left" colspan="2">
              <el-form-item :label="$t('io.workTick.skileYq')">
                <div id="wtm_wtm1_safe_todo_create">
                  <table width="100%">
                    <tr>
                      <td align="left">
                        <el-form-item prop="safetyTechnical" :label="$t('io.workTick.cqCs')">
                          <div style="width: 90%; display: inline-block">
                            <!-- 需采取安全技术措施 -->
                            <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                                      v-model="ticketCreateForm.safetyTechnical"></el-input>
                          </div>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td align="left">
                        <!-- 需采取个人安全措施 -->
                        <el-form-item prop="personalSafetyTechnical" :label="$t('io.workTick.cqGrCs')">
                          <div style="width: 90%; display: inline-block">
                            <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                                      v-model="ticketCreateForm.personalSafetyTechnical"></el-input>
                          </div>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td align="left">
                        <!-- 需装设接地线／投入接地刀闸／设置短路线（含编号、地点和数量） -->
                        <el-form-item prop="groundWire" :label="$t('io.workTick.xqJx')">
                          <div style="width: 90%; display: inline-block">
                            <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                                      v-model="ticketCreateForm.groundWire"></el-input>
                          </div>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td align="left">
                        <!-- 需设置安全警示标志牌／安全遮栏／安全绳（含编号、地点和数量） -->
                        <el-form-item prop="safetyWarningSigns" :label="$t('io.workTick.safetyWarningSigns')">
                          <div style="width: 90%; display: inline-block">
                            <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                                      v-model="ticketCreateForm.safetyWarningSigns"></el-input>
                          </div>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td align="left">
                        <!-- 工作地点需保留带电设备／带压设备／转动设备／注意事项 -->
                        <el-form-item prop="liveElectrifiedEquipment" :label="$t('io.workTick.liveElectrifiedEquipment')">
                          <div style="width: 90%; display: inline-block">
                            <el-input type="textarea" :rows="5" :cols="120" :placeholder="$t('io.workTick.enterContant')"
                                      v-model="ticketCreateForm.liveElectrifiedEquipment"></el-input>
                          </div>
                        </el-form-item>
                      </td>
                    </tr>
                  </table>
                </div>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td height="36" align="right" valign="top">
              <label class="table-serialNum">&nbsp;</label>
            </td>
            <td colspan="2" align="left">
              <!-- 工作票签发人 -->
              <el-form-item prop="issueId" :label="$t('io.workTick.issueId')">
                <div style="width: 265px; display: inline-block;">
                  <choose-user :treeData="allUsers" :placeholder="$t('io.workTick.needIssueId')"
                               :valueMode.sync="ticketCreateForm.issueId"></choose-user>
                </div>
              </el-form-item>
            </td>
          </tr>
        </table>
      </div>
    </el-form>
    </div>

    <div slot="footer" class="dialog-footer" style="text-align: center;">
      <el-button type="primary" :size="elementSize" v-if="!isDetail" @click="saveTicket" :disabled="isSubmitting">{{$t('io.workTick.submit')}}</el-button>
      <el-button type="info" :size="elementSize"
                 @click="handleClose">
        {{$t('cancel')}}
      </el-button>
    </div>
    <SelectAlarmDialog v-if="isShowChooseAlarm" @choose-alarm="chooseAlarms"
                       :is-show-choose-alarm.sync="isShowChooseAlarm"></SelectAlarmDialog>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import TicketService from '@/service/workTicket'
  import UserService from '@/service/user'

  import ChooseUser from '@/components/chooseTreeSelect/index.vue'
  import SelectAlarmDialog from '@/pages/io/alarm/chooseAlarmDialog/index.vue'
  const props = {
    dialogType: { // 对话框类型
      type: String,
      default: 'detail'
    },
    ticketCreateForm: {
      type: Object,
      default () {
        return {
          // 计划工作时间
          workTime: []
        }
      }
    },
    isShowDialog: { // 是否可见
      type: Boolean,
      default: false
    },
    allUsers: { // 所有的用户信息
      type: Array,
      default: []
    }
  }

  export default {
    props: props,
    components: {
      ChooseUser,
      SelectAlarmDialog
    },
    data () {
      return {
        currentHieght: window.innerHeight - 124,
        ticketCreateFormRules: {
          alarmIds: [
            {required: true, message: this.$t('io.workTick.needDevAlarm'), trigger: ['blur', 'change']}
          ],
          chargeId: [ // 请选择工作负责人
            {required: true, message: this.$t('io.workTick.needChargeId'), trigger: ['blur', 'change']}
          ],
          workContent: [ // 请填写工作任务
            {required: true, message: this.$t('io.workTick.needWorkContent'), trigger: 'blur'}
          ],
          workTime: [ // 请选择计划工作时间
            {required: true, message: this.$t('io.workTick.needWorkTime'), trigger: 'blur'}
          ],
          issueId: [ // 请选择工作票签发人
            {required: true, message: this.$t('io.workTick.needIssueId'), trigger: ['blur', 'change']}
          ],
        },
        isShowChooseAlarm: false, // 弹出选择告警的弹出框
        isSubmitting: false, // 是否真正提交
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      // 关闭对话框
      handleClose () {
        this.$emit('update:isShowDialog', false)
      },
      chooseAlarm (ev) { // 关联告警获取告警的框
        this.isShowChooseAlarm = true
      },
      // 选中告警的数据后的告警回显函数
      chooseAlarms (chooseRows, devId, stationCode) {
        let alarmIds = []
        let alarmNames = []
        let len = (chooseRows && chooseRows.length) || 0
        this.$set(this.ticketCreateForm, 'stationCode', stationCode) // 改变后会去自动查询用户信息
        this.$set(this.ticketCreateForm, 'devId', devId)
        if (len === 0) {
          this.allUsers = []
          this.ticketCreateForm.alarmIds = null;
          this.ticketCreateForm.alarmNames = null;
          return;
        }
        // this.selectAllUsers(stationCode)
        UserService.getEnterpriseUser({stationCode: stationCode}).then(resp => {
          const data = (resp.code === 1 && resp.results) || []
          let userLen = data.length
          let userArr = []
          if (userLen > 0) {
            for (let i = 0; i < userLen; i++) { // 电站添加人员
              userArr.push({
                label: data[i].loginName,
                id: data[i].id,
              })
            }
          }
          this.$set(this, 'allUsers', userArr)
        })
        this.$set(this.ticketCreateForm, 'devName', chooseRows[0].devAlias)
        this.$set(this.ticketCreateForm, 'alarmType', chooseRows[0].alarmType)
        for (let i = 0; i < len; i++) {
          let tempAlarm = chooseRows[i]
          alarmIds.push(tempAlarm.id)
          alarmNames.push(tempAlarm.alarmName)
        }
        this.ticketCreateForm.alarmIds = alarmIds.join(',')
        this.ticketCreateForm.alarmNames = alarmNames.join(',') + ';'
      },
      /**
       * 保存票据信息
       */
      saveTicket () {
        this.isSubmitting = true
        this.$refs['ticketCreateForm'].validate((valid) => {
          if (valid) {
            let params = this.ticketCreateForm
            params.startWorkTime = params.workTime && params.workTime[0]
            params.endWorkTime = params.workTime && params.workTime[1]
            if (this.dialogType === 'modify') {
              TicketService.updateOneWorkTicket(params).then(resp => {
                if (resp.code === 1) {
                  this.$msgbox({message: this.$t('operatSuccess'), type: 'success', title: this.$t('confirm')}, () => {
                    this.ticketCreateDialog.isVisible = false
                  })
                  this.$emit('onSaveOrUpdateSuccess')
                  this.handleClose()
                } else {
                  this.$msgbox({message: this.$t('operatFailed'), type: 'error', title: this.$t('confirm')})
                }
                this.isSubmitting = false
              }).catch(() => {
                this.isSubmitting = false
              })
            } else {
              TicketService.saveOneWorkTicket(params).then(resp => {
                if (resp.code === 1) {
                  this.$msgbox({message: this.$t('operatSuccess'), type: 'success', title: this.$t('confirm')}, () => {
                    this.ticketCreateDialog.isVisible = false
                  })
                  this.$emit('onSaveOrUpdateSuccess')
                  this.handleClose()
                } else {
                  this.$msgbox({message: this.$t('operatFailed'), type: 'error', title: this.$t('confirm')})
                }
                this.isSubmitting = false
              }).catch(() => {
                this.isSubmitting = false
              })
            }
          } else {
            this.isSubmitting = false
            return false
          }
        })
      },
    },
    watch: {},
    computed: {
      // 是否是查看详情
      isDetail () {
        return this.dialogType === 'detail'
      },
      ticketCreateFormPeopleCount () {
        let count = 0
        this.ticketCreateForm.chargeId && (count += 1)
        if (this.ticketCreateForm.groupId) {
          let groupIdArr = this.ticketCreateForm.groupId.split(',')
          let len = groupIdArr.length
          if (count > 0) {
            if (groupIdArr.indexOf(this.ticketCreateForm.chargeId + '') === -1) { // 工作组人员中不包含负责人
              count += len
            } else {
              count = len
            }
          } else {
            count = len
          }
        }
        // this.ticketCreateForm.groupId && (count += this.ticketCreateForm.groupId.split(',').length)
        return count
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "~@/assets/css/variables";
  .detail-main-box {
    width: 1240px;
    margin: 0 auto;
    padding: 24px 50px;
    overflow-y: auto;
    background-color: #fff;
    border: 1px solid #ccc;
    box-shadow: 6px 6px 15px #ccc;

    .table-serialNum {
      height: 36px;
      line-height: 36px;
      display: inline-block;
    }

    .el-form-item {
      margin-bottom: 5px;

      /deep/ .el-form-item__label {
        font-weight: bold;
      }
    }

    input:not([type]), input[type='text'], textarea {
      padding: 6px 10px;
      color: #606266;
      border-color: #5D7C92;
      border-bottom: 1px solid #5D7C92;

      &:hover, &:focus {
        border-color: @linkHoverColor;
      }

      &[readonly] {
        border-color: #a6a9ad;
        &:hover, &:focus {
          border-color: #5D7C92;
        }
      }
    }

    /deep/ .el-input__inner {
      height: 30px;
      line-height: 30px;
      color: #606266;
      border: none;
      border-color: #5D7C92;
      border-bottom: 1px solid #5D7C92;
      border-radius: 0;
    }
    /deep/ .el-textarea__inner {
      color: #606266;
      border-color: #5D7C92;
      border-radius: 0;
    }
  }
</style>
