<style lang="less" src="./index.less" scoped></style>

<template>
  <div style="height: 100%">
    <div class="search-bar clear">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('io.workTick.num')">
          <el-input v-model="searchData.id" :placeholder="$t('io.workTick.num')" clearable style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item :label="$t('io.workTick.actualStartTime')">
          <el-date-picker v-model="searchData.startTime" type="datetimerange"
                          :range-separator="$t('io.workTick.to')" :start-placeholder="$t('io.workTick.startTime')" :end-placeholder="$t('io.workTick.endTime')"
                          value-format="timestamp">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('io.processStatus')">
          <el-select style="width: 110px" v-model="searchData.processStatus" :placeholder="$t('io.all')">
            <el-option v-for="item in processStatus" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTicket">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="button-bar right">
      <el-button type="primary" @click="addTicket">{{$t('create')}}</el-button>
      <el-button type="primary" @click="copyTicket">{{$t('copy')}}</el-button>
      <el-button type="primary" @click="modifyTicket">{{$t('modify')}}</el-button>
      <el-button type="primary" @click="startWorkFlow">{{$t('io.workTick.processStart')}}</el-button>
      <el-button type="primary" @click="setWorkFlow">{{$t('io.workTick.nodeSetup')}}</el-button>
      <el-button type="primary" @click="exportTicket">{{$t('export')}}</el-button>
      <el-button type="primary" @click="printTicket">{{$t('print')}}</el-button>
    </div>

    <div class="content-box">
      <el-table :data="ticketTableData.list" ref="ticketTable" row-key="id" @row-click="rowClick" border
                max-height="600" :fit="true" @select="ticketSelected" @select-all="ticketSelected"
                :default-sort="{prop: 'createDate', order: 'descending'}"
                :sort="{prop: 'createDate', order: 'descending'}" class="ticketTable">
        <el-table-column type="selection" width="40" align="center">
        </el-table-column>

        <el-table-column prop="id" :label="$t('io.workTick.num')" width="100" align="center">
        </el-table-column>

        <el-table-column prop="task" :label="$t('io.workTick.opTask')" width="250" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-button @click="view(scope.row)" type="text">
              {{scope.row.ticketName}}
            </el-button>
          </template>
        </el-table-column>

        <el-table-column prop="startTime" :label="$t('io.workTick.actualStartTime')" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.startTime && new Date(scope.row.startTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="endTime" :label="$t('io.workTick.actualEndTime')" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.endTime && new Date(scope.row.endTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processTime" :label="$t('io.workTick.dealUserTime')" align="center">
        </el-table-column>

        <el-table-column prop="createDate" :label="$t('io.workTick.createTime')" align="center" :sortable="true">
          <template slot-scope="scope">
            <span>{{ scope.row.createDate && new Date(scope.row.createDate).format($t('dateFormat.yyyymmdd')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processStatus" :label="$t('io.processStatus')" width="100" align="center">
        </el-table-column>

        <el-table-column prop="processPerson" :label="$t('io.currentUser')" align="center">
        </el-table-column>

        <el-table-column prop="check" :label="$t('io.workTick.view')" align="center">
        </el-table-column>

        <el-table-column prop="auditing" :label="$t('io.workTick.sh')" align="center">
        </el-table-column>
      </el-table>

      <el-pagination :current-page="ticketTableData.index" :page-sizes="ticketTableData.pageSizes"
                     :page-size="ticketTableData.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="ticketTableData.count"
                     style="float: right; margin-top: 5px;">
      </el-pagination>
    </div>

    <el-dialog :title="ticketDialog.title" :visible.sync="ticketDialog.isVisible" top="0" width="100%">
      <div class="detail-main-box" :style="{height: ticketDialog.height + 'px', overflow: 'auto'}">
        <el-form :model="ticketForm" ref="ticketForm" :size="elementSize" :rules="ticketFormRules">
          <input id="createTime" name="createTime" type="hidden">
          <div class="form-gzp">
            <p align="center" style="color:#113C6A; margin:0 0 20px; font-size: 24px;">
              <b><span class="text">{{$t('io.workTick.dqITick')}}</span></b>
            </p>
            <table ref="ticketData" class="ticketData" width="100%" cellpadding="0" cellspacing="0">
              <tr>
                <td width="50px" height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <div style="float: left;">
                      <span class="text">{{$t('io.workTick.dw')}}</span>
                      <input type="text" id="wtUnit" name="wtUnit" maxlength="20" style="width:150px"
                             class="input_new">
                      <font color="red">*</font>
                    </div>
                    <div style="float: left;">
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <span class="text">{{$t('io.workTick.workGroup')}}</span>
                      <input type="text" id="teams" name="teams" maxlength="15" style="width:150px" class="input_new">
                      <font color="red">*</font>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">1.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.workFzr')}}</span></b>
                    <input type="text" id="chargeMan" name="chargeMan" style="width:10em" maxlength="10"
                           class="input_new" value="">
                    <font color="red">*</font>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <div style="float: left;">
                      <b><span class="text">{{$t('io.workTick.workCy')}}</span></b>
                      <input type="text" class="input_new" id="wtMember" name="wtMember" style="width:150px"
                             maxlength="30" value="">
                      <font color="red">*</font>
                    </div>
                    <div style="float: left;">
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <span class="text">{{$t('io.workTick.totalPersion')}}</span>&nbsp;
                      <input type="text" maxlength="4" id="peopleCount" name="peopleCount" class="input_new"
                             style="width:5em" value="" onchange="">
                      <font color="red">*</font>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">2.&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="step4">
                    <b>
                      <span class="text">{{$t('io.workTick.workTask')}}</span>
                    </b>
                    <textarea rows="3" cols="94" id="wtTask" name="wtTask" class="input_false_area"></textarea>
                    <font color="red">*</font>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">3.&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="step5">
                    <div style="float: left;">
                      <b><span class="text">{{$t('io.workTick.workPlace')}}</span></b>
                      <input type="hidden" class="input_new" id="wtAddr" name="wtAddr">
                      <input type="hidden" id="deviceId" name="deviceId" value="">
                      <span>
                                  <input id="deviceName" name="deviceName" value="" class="input_new"
                                         style="width: 700px">
                                  <input type="button" class="button-select" id="wtm_wtm1_device_selectButton"
                                         value="···">
                                  <font color="red">*</font>
                                </span>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">4.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <div style="float: left;">
                      <b><span class="text">{{$t('io.workTick.plantWorkTime')}}</span></b>&nbsp;<span class="text">{{$t('io.workTick.from')}}</span>
                      <input type="text" id="wtm_onec_planStime" name="planStime" class="input_new" value="" size="20"
                             readonly="readonly">
                      <font color="red">*</font><span class="text"></span>
                    </div>
                    <div style="float: left;">
                      &nbsp;<span class="text">{{$t('io.workTick.to')}}</span>
                      <input type="text" id="wtm_onec_planEtime" name="planEtime" class="input_new" value="" size="20"
                             readonly="readonly">
                      <font color="red">*</font><span class="text"></span>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">5.&nbsp;</td>
                <td align="left" colspan="2">
                  <b><span class="text">{{$t('io.workTick.skile')}}</span></b>
                </td>
              </tr>
              <tr>
                <td>
                </td>
                <td>
                  <div id="wtm_wtm1_safe_todo_create">
                  </div>
                </td>
                <td>
                  <div id="wtm_wtm1_safe_done_create">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td colspan="2" align="left" style="padding-right:150px">
                  <div>
                    <span class="text">{{$t('io.workTick.issueId')}}</span>
                    <input type="text" id="signMan" name="signMan" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.issueTime')}}</span>
                    <input type="text" id="signTime" name="signTime" class="input_new" value="" onfocus="" size="20"
                           readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">6.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.ticketTipTime')}}</span></b>
                    <input type="text" id="submitTime" name="submitTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.workTicketFzr')}}</span>
                    <input type="text" id="wtCharger" name="wtCharger" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.recTime')}}</span></b>
                    <input type="text" id="wtm_onec_recvTime" name="recvTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.runFzr')}}</span>
                    <input type="text" id="runCharger" name="runCharger" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                    &nbsp;&nbsp;
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">7.&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="one4">
                    <b><span class="text">{{$t('io.workTick.workOpTime')}}</span></b>
                    <input type="text" id="wtm_onec_approveStime" name="approveStime" class="input_new" value=""
                           onfocus="" size="20" readonly="readonly">
                    &nbsp;<span class="text">{{$t('io.workTick.to')}}</span><span class="text"></span>
                    <input type="text" id="wtm_onec_approveEtime" name="approveEtime" class="input_new" value=""
                           onfocus="" size="20" readonly="readonly"><span class="text"></span>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">{{$t('io.workTick.valueLen')}}</span>
                    <input type="text" id="approveClassman" name="approveClassman" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">8.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.contantConfirm')}}</span></b>
                    <input type="text" id="permitTime" name="permitTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">{{$t('io.workTick.workPer')}}</span>
                    <input type="text" id="permitMan" name="permitMan" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.workFzr')}}</span>
                    <input type="text" id="permitCharger" name="permitCharger" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">9.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.workFzrMove')}}</span></b>
                    <input type="text" id="changeTime" name="changeTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    <span class="text"> {{$t('io.workTick.workFzrLea')}}</span>
                    <input type="text" id="changeCharger" name="changeCharger" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                    <span class="text">{{$t('io.workTick.drWorkFzr')}} </span>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">{{$t('io.workTick.issueId')}}</span>
                    <input type="text" id="changeSigner" name="changeSigner" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.workPer')}}</span>
                    <input type="text" id="changePermiter" name="changePermiter" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25" align="right">10.&nbsp;</td>
                <td align="left" colspan="2">
                  <b>
                    <span class="text">{{$t('io.workTick.workTicketStop')}}</span>
                  </b>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td colspan="3">
                  <div>
                    <table bgcolor="#C0D2E9" cellpadding="0" cellspacing="0">
                      <tbody>
                      <tr>
                        <td bgcolor="#ffffff" align="left">
                          &nbsp;<span class="text">{{$t('io.workTick.overWork')}}</span>
                          <input type="text" id="suspendTime" name="suspendTime" class="input_new" size="20"
                                 readonly="readonly">
                          <span class="text">{{$t('io.workTick.workSite')}}  </span>
                          <br>

                          &nbsp;<span class="text">{{$t('io.workTick.workFzr')}}</span>
                          <input type="text" id="suspendCharger1" name="suspendCharger1" style="width:10em"
                                 maxlength="10" value="" class="input_false" readonly="readonly">
                          &nbsp;&nbsp;
                          <span class="text">{{$t('io.workTick.workPer')}}</span>
                          <input type="text" id="suspendPermiter1" name="suspendPermiter1" style="width:10em"
                                 maxlength="10" value="" class="input_new" readonly="readonly">
                          <br>

                          &nbsp;<span class="text">{{$t('io.workTick.sureTime')}}</span>
                          <input type="text" id="suspendCtime" name="suspendCtime" class="input_new" value=""
                                 onfocus="" size="20" readonly="readonly">
                          <br>

                          &nbsp;<span class="text">{{$t('io.workTick.workPer')}}</span>
                          <input type="text" id="suspendPermiter2" name="suspendPermiter2" style="width:10em"
                                 maxlength="10" value="" class="input_false" readonly="readonly">
                          &nbsp;&nbsp;
                          <span class="text">{{$t('io.workTick.workFzr')}}</span>
                          <input type="text" id="suspendCharger2" name="suspendCharger2" style="width:10em"
                                 maxlength="10" value="" class="input_false" readonly="readonly">
                        </td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">11.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.workTicketExt')}}</span></b>
                    &nbsp;&nbsp;&nbsp;&nbsp;<span class="text">{{$t('io.workTick.valPerTo')}}</span>
                    <input type="text" id="delayTime" name="delayTime" class="input_new" value="" onfocus="" size="20"
                           readonly="readonly">
                    <span class="text"></span>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">{{$t('io.workTick.valueLen')}}</span>
                    <input type="text" id="delayClassman" name="delayClassman" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.workFzr')}}</span>
                    <input type="text" id="delayCharger" name="delayCharger" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25" align="right">12.&nbsp;</td>
                <td align="left" colspan="2">
                  <b><span class="text">{{$t('io.workTick.workTicketEnd')}} </span></b>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td colspan="3" align="left">
                  <div>
                    &nbsp;<span class="text"> {{$t('io.workTick.groundWire')}}</span>
                    <input type="text" id="doWirecount" name="doWirecount" maxlength="7" style="width:5em"
                           class="input_false" value="" onblur="" readonly="readonly">
                    <span class="text">{{$t('io.workTick.groupDis')}}</span>
                    <span class="text">{{$t('io.workTick.haveSetting')}}</span>
                    <input type="text" id="doSigncount" name="doSigncount" value="" size="20" class="input_false"
                           readonly="readonly">
                    <br>
                    &nbsp;<span class="text">{{$t('io.workTick.haveWithdrawn')}}</span>
                    <span class="text">{{$t('io.workTick.workCompleted')}}</span>
                    <input type="text" id="finalTime" name="finalTime" class="input_new" value="" onfocus="" size="20"
                           readonly="readonly">
                    &nbsp;<span class="text">{{$t('io.workTick.workEnd')}}</span>
                    <br>
                    &nbsp;<span class="text">{{$t('io.workTick.workPer')}}</span>
                    <input type="text" id="finalPermiter" name="finalPermiter" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">{{$t('io.workTick.workFzr')}}</span>
                    <input type="text" id="finalCharger" name="finalCharger" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td class="one_remark_td" height="25" align="right">13.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">{{$t('io.workTick.remarks')}}</span></b>
                    <span class="text">{{$t('io.workTick.undismantled')}}</span>
                    <input type="text" id="undoWirecount" name="undoWirecount" maxlength="7" style="width:5em"
                           class="input_false" value="" readonly="readonly">
                    <span class="text">{{$t('io.workTick.group')}}</span>
                    <span class="text">{{$t('io.workTick.unrecoveredSafety')}}</span>
                    <input type="text" id="undoSigncount" name="undoSigncount" maxlength="7" style="width:5em"
                           class="input_false" value="" readonly="readonly">
                    <span class="text">{{$t('io.workTick.block')}}</span>
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="one15">
                    <span class="text">{{$t('io.workTick.settingNum')}}</span>
                    <input type="text" id="setInfo" name="setInfo" maxlength="50" style="width:400px;"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="one16">
                    <span class="text">{{$t('io.workTick.reason')}}</span>
                    <input type="text" id="cause" name="cause" maxlength="100" style="width:790px;"
                           class="input_false" value="" readonly="readonly">
                    <br><br>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button type="primary" :size="elementSize" @click="saveTicket">{{ticketDialog.addOrModify}}</el-button>
        <el-button v-if="!ticketForm.isView" type="info" :size="elementSize" @click="ticketDialog.isVisible = false">
          {{$t('cancel')}}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6" src="./index.js"></script>

