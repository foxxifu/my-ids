<style lang="less" src="./index.less" scoped></style>

<template>
  <div style="height: 100%">
    <div class="search-bar clear">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <!--<el-form-item label="编号">-->
          <!--<el-input v-model="searchData.id" placeholder="编号" clearable style="width: 120px"></el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="工作责任人">
          <el-input v-model="searchData.chargeName" placeholder="工作负责人" clearable style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item label="实际开始时间">
          <el-date-picker
            v-model="searchData.startWorkTime" placeholder="开始时间" type="datetime" :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
          ~
          <el-date-picker
            v-model="searchData.endWorkTime" placeholder="结束时间" type="datetime" :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="流程状态">
          <el-select style="width: 160px" v-model="searchData.processStatus" placeholder="全部" clearable>
            <el-option v-for="item in processStatus" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTicket(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="button-bar right">
      <el-button type="primary" @click="addTicket">{{$t('create')}}</el-button>
      <el-button type="primary" @click="copyTicket">{{$t('copy')}}</el-button>
      <el-button type="primary" @click="modifyTicket">{{$t('modify')}}</el-button>
      <el-button type="primary" @click="exportTicket">{{$t('export')}}</el-button>
      <!--<el-button type="primary" @click="printTicket">{{$t('print')}}</el-button>-->
    </div>

    <div class="content-box">
      <el-table :data="ticketTableData.list" ref="ticketTable" row-key="id" @selection-change="handleSelectionChange" @row-click="rowClick" border
                max-height="640" :fit="true"
                :default-sort="{prop: 'createDate', order: 'descending'}"
                :sort="{prop: 'createDate', order: 'descending'}" class="ticketTable">
        <el-table-column type="selection" width="40" align="center">
        </el-table-column>

        <el-table-column prop="serialNum" label="编号" width="80" align="center">
        </el-table-column>

        <el-table-column prop="chargeName" label="工作负责人" width="120" align="center">
        </el-table-column>

        <el-table-column prop="workContent" label="工作任务" align="center" :show-overflow-tooltip="true">
        </el-table-column>

        <el-table-column prop="startWorkTime" label="计划开始时间" width="180" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.startWorkTime && Date.parse(scope.row.startWorkTime).format($t('dateFormat.yyyymmddhhmmss'))
              }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="endWorkTime" label="计划结束时间" width="180" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.endWorkTime && Date.parse(scope.row.endWorkTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processTime" label="处理时长(h)" width="120" align="center">
          <template slot-scope="{row, $index}">
            <span>{{row.startWorkTime | dealHourOfTimeFilter(row.endWorkTime)}}</span>
          </template>
        </el-table-column>

        <el-table-column prop="createDate" label="创建时间" width="135" align="center" :sortable="true">
          <template slot-scope="scope">
            <span>{{ scope.row.createDate && Date.parse(scope.row.createDate).format($t('dateFormat.yyyymmdd')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processStatus" label="流程状态" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ $t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus')[scope.row.processStatus]
              }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processPerson" label="当前处理人" width="120" align="center">
          <template slot-scope="{row}">
            <el-button type="text">{{ row.processPerson | processPersonFilter}}</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="alarmId" label="告警" align="center">
          <template slot-scope="scope">
            <el-button type="text">{{ scope.row.alarmNames }}</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="operator" label="操作" width="200" align="center">
          <template slot-scope="{row, $index}">
            <el-button type="text" @click.stop="viewHandler(row)"><span>查看</span></el-button>
            <template
              v-if="+row.processUserId === +loginUserId && row.processStatus && row.processStatus !== 'processEnd'">
              <span> | </span>
              <el-button type="text" @click.stop="processHandler(row)">
                <span>{{$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus')[row.processStatus]}}</span>
              </el-button>
            </template>
            <template v-if="+row.processUserId === +loginUserId && row.processStatus && row.processStatus === 'issueFirstWork'">
              <span> | </span>
              <el-button type="text" @click.stop="endWorkFlow(row)">
                <span>作废</span>
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="searchData.index" :page-sizes="[10, 20, 30, 50]"
                     :page-size="searchData.pageSize"
                     @size-change="pageSizeChange" @current-change="pageChange"
                     layout="total, sizes, prev, pager, next, jumper" :total="ticketTableData.total"
                     style="float: right; margin-top: 5px;">
      </el-pagination>
    </div>

    <!-- 第一步：创建并指定签发人 -->
    <el-dialog :title="$t(ticketCreateDialog.type)" :visible.sync="ticketCreateDialog.isVisible" top="0" width="100%">
      <div class="detail-main-box" :style="{height: ticketCreateDialog.height + 'px', overflow: 'auto'}">
        <el-form :model="ticketCreateForm" :rules="ticketCreateFormRules" ref="ticketCreateForm" label-position="right" :disabled="isDetail">
          <el-input v-model="ticketCreateForm.createDate" type="hidden" :value="Date.now()"></el-input>
          <div class="form-gzp">
            <p align="center" style="color:#113C6A; margin:0 0 20px; font-size: 24px;">
              <b><span class="text">电气一种工作票</span></b>
            </p>
            <table ref="ticketData" class="ticketData" width="100%" cellpadding="0" cellspacing="0">
              <tr>
                <td height="25" align="right" style="width: 10%;">
                  <label class="table-serialNum">1.&nbsp;</label>
                </td>
                <td align="left" colspan="2">
                  <el-form-item prop="alarmIds" label="设备告警">
                    <div style="width: 72%; display: inline-block;">
                      <!--<choose-device :myValues.sync="ticketCreateForm.devId" :myTexts="ticketCreateForm.devName"
                                     @device-change="deviceChange" :isMutiSelect="false"></choose-device>-->
                      <el-input v-model="ticketCreateForm.alarmNames" @focus="chooseAlarm" placeholder="请选择同一个设备的告警"></el-input>
                      <!--<input v-model="ticketCreateForm.alarmIds" :value="ticketCreateForm.alarmIds"/>-->
                      <!--<el-input v-model="ticketCreateForm.alarmIds"></el-input>-->
                    </div>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td height="25" align="right" style="width: 10%;">
                  <label class="table-serialNum">2.&nbsp;</label>
                </td>
                <td align="left" colspan="2">
                  <el-form-item prop="chargeId" label="工作负责人">
                    <div style="display: inline-block;">
                      <choose-user :treeData="allUsers" placeholder="请选择工作负责人"
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
                  <el-form-item prop="groupId" label="工作成员" style="width: 68%;display: inline-block;">
                    <div style="display: inline-block; width: 605px;">
                      <choose-user :treeData="allUsers" :isMutiues="true" placeholder="请选择工作成员"
                                   :valueMode.sync="ticketCreateForm.groupId" v-if="ticketCreateDialog.isVisible"></choose-user>
                    </div>
                  </el-form-item>
                  <el-form-item prop="ticketCreateFormPeopleCount" label="共（人）"
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
                  <el-form-item prop="workContent" label="工作任务：">
                    <div style="width: 90%;">
                      <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
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
                  <el-form-item prop="workAddress" label="工作地点">
                    <el-input type="text" v-model="ticketCreateForm.workAddress" style="width: 810px;"></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">
                  <label class="table-serialNum">5.&nbsp;</label>
                </td>
                <td align="left" colspan="2">
                  <el-form-item prop="workTime" label="计划工作时间">
                    <el-date-picker v-model="ticketCreateForm.workTime" type="datetimerange"
                                    range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"
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
                  <el-form-item label="安全技术措施要求">
                    <div id="wtm_wtm1_safe_todo_create">
                      <table width="100%">
                        <tr>
                          <td align="left">
                            <el-form-item prop="safetyTechnical" label="需采取安全技术措施">
                              <div style="width: 90%; display: inline-block">
                                <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
                                          v-model="ticketCreateForm.safetyTechnical"></el-input>
                              </div>
                            </el-form-item>
                          </td>
                        </tr>
                        <tr>
                          <td align="left">
                            <el-form-item prop="personalSafetyTechnical" label="需采取个人安全措施">
                              <div style="width: 90%; display: inline-block">
                                <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
                                          v-model="ticketCreateForm.personalSafetyTechnical"></el-input>
                              </div>
                            </el-form-item>
                          </td>
                        </tr>
                        <tr>
                          <td align="left">
                            <el-form-item prop="groundWire" label="需装设接地线／投入接地刀闸／设置短路线（含编号、地点和数量）">
                              <div style="width: 90%; display: inline-block">
                                <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
                                          v-model="ticketCreateForm.groundWire"></el-input>
                              </div>
                            </el-form-item>
                          </td>
                        </tr>
                        <tr>
                          <td align="left">
                            <el-form-item prop="safetyWarningSigns" label="需设置安全警示标志牌／安全遮栏／安全绳（含编号、地点和数量）">
                              <div style="width: 90%; display: inline-block">
                                <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
                                          v-model="ticketCreateForm.safetyWarningSigns"></el-input>
                              </div>
                            </el-form-item>
                          </td>
                        </tr>
                        <tr>
                          <td align="left">
                            <el-form-item prop="liveElectrifiedEquipment" label="工作地点需保留带电设备／带压设备／转动设备／注意事项">
                              <div style="width: 90%; display: inline-block">
                                <el-input type="textarea" :rows="5" :cols="120" placeholder="请输入内容"
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
                  <el-form-item prop="issueId" label="工作票签发人">
                    <div style="width: 265px; display: inline-block;">
                      <choose-user :treeData="allUsers" placeholder="请选择工作票签发人"
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
        <el-button type="primary" :size="elementSize" v-if="!isDetail" @click="saveTicket">{{ticketCreateDialog.addOrModify}}</el-button>
        <el-button v-if="!ticketCreateForm.isView" type="info" :size="elementSize"
                   @click="ticketCreateDialog.isVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
    <!-- 第二步：签发 -->
    <el-dialog title="签发" :visible.sync="ticketIssueDialog.isVisible" top="0" width="600">
      <div class="detail-main-box" :style="{height: ticketIssueDialog.height + 'px', overflow: 'auto'}">
        <el-form :model="ticketIssueForm" :rules="ticketIssueFormRules" ref="ticketIssueForm" label-position="right">
          <div class="form-gzp">
            <p align="center" style="color:#113C6A; margin:0 0 20px; font-size: 24px;">
              <b><span class="text">电气一种工作票</span></b>
            </p>
            <el-form-item prop="issuserId" label="工作票签发人">
              <div style="width: 265px; display: inline-block;">
                <choose-user :treeData="allUsers" placeholder="请选择工作票签发人"
                             :valueMode.sync="ticketIssueForm.issuserId"></choose-user>
              </div>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button type="primary" :size="elementSize" @click="saveTicket">{{ticketIssueDialog.addOrModify}}</el-button>
        <el-button v-if="!ticketIssueForm.isView" type="info" :size="elementSize"
                   @click="ticketIssueDialog.isVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
    <SelectAlarmDialog v-if="isShowChooseAlarm" @choose-alarm="chooseAlarms"
                       :is-show-choose-alarm.sync="isShowChooseAlarm"></SelectAlarmDialog>

    <OperationOneTickDialog v-if="operatTransDatas.isShowOperationDialog" :isShowDialog.sync="operatTransDatas.isShowOperationDialog"
                            :definitionId="operatTransDatas.operationId" :isSelectNextUser="operatTransDatas.isSelectNextUser"
                            :title="operatTransDatas.operatTitle" :isReback="operatTransDatas.isReback"  @dealSuccess="dealSuccess"></OperationOneTickDialog>
  </div>
</template>

<script type="text/ecmascript-6" src="./index.js"></script>

