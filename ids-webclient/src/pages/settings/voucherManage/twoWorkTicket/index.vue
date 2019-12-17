<style lang="less" src="./index.less" scoped></style>

<template>
  <div style="height: 100%">
    <div class="search-bar clear">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item label="编号">
          <el-input v-model="searchData.id" placeholder="编号" clearable style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item label="工作责任人">
          <el-input v-model="searchData.personLiable" placeholder="工作责任人" clearable style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item label="实际开始时间">
          <el-date-picker v-model="searchData.startTime" type="datetimerange"
                          range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"
                          value-format="timestamp">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="流程状态">
          <el-select style="width: 110px" v-model="searchData.processStatus" placeholder="全部">
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
      <el-button type="primary" @click="startWorkFlow">启动流程</el-button>
      <el-button type="primary" @click="setWorkFlow">节点设置</el-button>
      <el-button type="primary" @click="exportTicket">{{$t('export')}}</el-button>
      <el-button type="primary" @click="printTicket">打印</el-button>
    </div>

    <div class="content-box">
      <el-table :data="ticketTableData.list" ref="ticketTable" row-key="id" @row-click="rowClick" border
                max-height="600" :fit="true" @select="ticketSelected" @select-all="ticketSelected"
                :default-sort="{prop: 'createDate', order: 'descending'}"
                :sort="{prop: 'createDate', order: 'descending'}" class="ticketTable">
        <el-table-column type="selection" width="40" align="center">
        </el-table-column>

        <el-table-column prop="id" label="编号" width="100" align="center">
        </el-table-column>

        <el-table-column prop="personLiable" label="工作负责人" align="center">
          <template slot-scope="scope">
            <el-button @click="view(scope.row)" type="text">
              {{scope.row.personLiable}}
            </el-button>
          </template>
        </el-table-column>

        <el-table-column prop="task" label="工作任务" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-button @click="view(scope.row)" type="text">
              {{scope.row.ticketName}}
            </el-button>
          </template>
        </el-table-column>

        <el-table-column prop="startTime" label="实际开始时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.startTime && new Date(scope.row.startTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="endTime" label="实际结束时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.endTime && new Date(scope.row.endTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processTime" label="处理时长" align="center">
        </el-table-column>

        <el-table-column prop="createDate" label="创建时间" align="center" :sortable="true">
          <template slot-scope="scope">
            <span>{{ scope.row.createDate && new Date(scope.row.createDate).format($t('dateFormat.yyyymmdd')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processStatus" label="流程状态" width="100" align="center">
        </el-table-column>

        <el-table-column prop="processPerson" label="当前处理人" align="center">
        </el-table-column>

        <el-table-column prop="alarmName" label="告警" align="center">
        </el-table-column>

        <el-table-column prop="check" label="查看" align="center">
        </el-table-column>

        <el-table-column prop="overhaul" label="检修交代" align="center" :show-overflow-tooltip="true">
        </el-table-column>

        <el-table-column prop="auditing" label="审核" align="center">
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
              <b><span class="text">电气二种工作票</span></b>
            </p>
            <table ref="ticketData" class="ticketData" width="100%" cellpadding="0" cellspacing="0">
              <tr>
                <td width="10%" height="25" align="right">1.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">工作负责人</span></b>
                    <div style="display: inline-block;">
                      <choose-user :treeData="allUsers" placeholder="请选择工作负责人"
                                   :valueMode.sync="ticketForm.chargeId"></choose-user>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div style="float: left;">
                    <b><span class="text">工作成员</span></b>
                    <div style="display: inline-block; width: 565px;">
                      <choose-user :treeData="allUsers" :isMutiues="true" placeholder="请选择工作成员"
                                   :valueMode.sync="ticketForm.groupId"></choose-user>
                    </div>
                  </div>
                  <div style="float: left;">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span class="text">共（人）</span>&nbsp;
                    <el-input-number v-model="ticketForm.peopleCount" value="0" :min="0" clearable
                                     style="width:150px"></el-input-number>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">2.&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="step4">
                    <b>
                      <span class="text">工作任务</span>
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
                      <b><span class="text">工作地点</span></b>
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
                      <b><span class="text">计划工作时间</span></b>&nbsp;<span class="text">从</span>
                      <input type="text" id="wtm_onec_planStime" name="planStime" class="input_new" value="" size="20"
                             readonly="readonly">
                      <font color="red">*</font><span class="text"></span>
                    </div>
                    <div style="float: left;">
                      &nbsp;<span class="text">至</span>
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
                  <b><span class="text">安全技术措施</span></b>
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
                    <span class="text">工作票签发人</span>
                    <input type="text" id="signMan" name="signMan" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">签发时间</span>
                    <input type="text" id="signTime" name="signTime" class="input_new" value="" onfocus="" size="20"
                           readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">6.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">工作票提交时间</span></b>
                    <input type="text" id="submitTime" name="submitTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">工作票负责人</span>
                    <input type="text" id="wtCharger" name="wtCharger" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">接收时间</span></b>
                    <input type="text" id="wtm_onec_recvTime" name="recvTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span class="text">运行值班负责人</span>
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
                    <b><span class="text">检修作业批准时间</span></b>
                    <input type="text" id="wtm_onec_approveStime" name="approveStime" class="input_new" value=""
                           onfocus="" size="20" readonly="readonly">
                    &nbsp;<span class="text">至</span><span class="text"></span>
                    <input type="text" id="wtm_onec_approveEtime" name="approveEtime" class="input_new" value=""
                           onfocus="" size="20" readonly="readonly"><span class="text"></span>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">值长</span>
                    <input type="text" id="approveClassman" name="approveClassman" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">8.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">上述内容已确认，具备开工条件。工作许可开工时间</span></b>
                    <input type="text" id="permitTime" name="permitTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">工作许可人</span>
                    <input type="text" id="permitMan" name="permitMan" style="width:10em" maxlength="10" value=""
                           class="input_false" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">工作负责人</span>
                    <input type="text" id="permitCharger" name="permitCharger" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">9.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">工作负责人变动自</span></b>
                    <input type="text" id="changeTime" name="changeTime" class="input_new" value="" onfocus=""
                           size="20" readonly="readonly">
                    <span class="text"> 原工作负责人离去，变更为</span>
                    <input type="text" id="changeCharger" name="changeCharger" style="width:10em" maxlength="10"
                           value="" class="input_false" readonly="readonly">
                    <span class="text">担任工作负责人 </span>
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <span class="text">工作票签发人</span>
                    <input type="text" id="changeSigner" name="changeSigner" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">工作许可人</span>
                    <input type="text" id="changePermiter" name="changePermiter" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25" align="right">10.&nbsp;</td>
                <td align="left" colspan="2">
                  <b>
                    <span class="text">工作票中止与复工</span>
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
                          &nbsp;<span class="text">检修作业于</span>
                          <input type="text" id="suspendTime" name="suspendTime" class="input_new" size="20"
                                 readonly="readonly">
                          <span class="text">中止，检修作业人员已全部撤离作业现场。  </span>
                          <br>

                          &nbsp;<span class="text">工作负责人</span>
                          <input type="text" id="suspendCharger1" name="suspendCharger1" style="width:10em"
                                 maxlength="10" value="" class="input_false" readonly="readonly">
                          &nbsp;&nbsp;
                          <span class="text">工作许可人</span>
                          <input type="text" id="suspendPermiter1" name="suspendPermiter1" style="width:10em"
                                 maxlength="10" value="" class="input_new" readonly="readonly">
                          <br>

                          &nbsp;<span class="text">检修作业已采取安全技术措施已复查、确认，满足工作复工要求。确认时间</span>
                          <input type="text" id="suspendCtime" name="suspendCtime" class="input_new" value=""
                                 onfocus="" size="20" readonly="readonly">
                          <br>

                          &nbsp;<span class="text">工作许可人</span>
                          <input type="text" id="suspendPermiter2" name="suspendPermiter2" style="width:10em"
                                 maxlength="10" value="" class="input_false" readonly="readonly">
                          &nbsp;&nbsp;
                          <span class="text">工作负责人</span>
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
                    <b><span class="text">工作票延期</span></b>
                    &nbsp;&nbsp;&nbsp;&nbsp;<span class="text">有效期延长到</span>
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
                    <span class="text">值长</span>
                    <input type="text" id="delayClassman" name="delayClassman" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">工作负责人</span>
                    <input type="text" id="delayCharger" name="delayCharger" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25" align="right">12.&nbsp;</td>
                <td align="left" colspan="2">
                  <b><span class="text">工作票终结 </span></b>
                </td>
              </tr>
              <tr>
                <td height="25" align="right">&nbsp;</td>
                <td colspan="3" align="left">
                  <div>
                    &nbsp;<span class="text"> 检修作业已装设接地线／投入接地刀闸／设置短路线共</span>
                    <input type="text" id="doWirecount" name="doWirecount" maxlength="7" style="width:5em"
                           class="input_false" value="" onblur="" readonly="readonly">
                    <span class="text">组已拆除／拉开，</span>
                    <span class="text">已设置安全警示标志牌／遮栏／绳共</span>
                    <input type="text" id="doSigncount" name="doSigncount" value="" size="20" class="input_false"
                           readonly="readonly">
                    <br>
                    &nbsp;<span class="text">块／个／条已收回。</span>
                    <span class="text">检修作业已结束，检修人员已全部撤离作业现场，作业交代符合规定要求，作业现场已清理。此检修作业工作票于</span>
                    <input type="text" id="finalTime" name="finalTime" class="input_new" value="" onfocus="" size="20"
                           readonly="readonly">
                    &nbsp;<span class="text">办理作业结束手续。</span>
                    <br>
                    &nbsp;<span class="text">工作许可人</span>
                    <input type="text" id="finalPermiter" name="finalPermiter" maxlength="10" style="width:10em"
                           class="input_false" value="" readonly="readonly">
                    &nbsp;&nbsp;
                    <span class="text">工作负责人</span>
                    <input type="text" id="finalCharger" name="finalCharger" style="width:10em" maxlength="10"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>

              <tr>
                <td class="one_remark_td" height="25" align="right">13.&nbsp;</td>
                <td align="left" colspan="2">
                  <div>
                    <b><span class="text">备注：</span></b>
                    <span class="text">未拆除／拉开的接地线／投入接地刀闸／设置短路线共</span>
                    <input type="text" id="undoWirecount" name="undoWirecount" maxlength="7" style="width:5em"
                           class="input_false" value="" readonly="readonly">
                    <span class="text">组。</span>
                    <span class="text">未收回的安全警示标志牌／遮栏／绳共</span>
                    <input type="text" id="undoSigncount" name="undoSigncount" maxlength="7" style="width:5em"
                           class="input_false" value="" readonly="readonly">
                    <span class="text">块／个／条。</span>
                  </div>
                </td>
              </tr>

              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="one15">
                    <span class="text">设置地点及编号</span>
                    <input type="text" id="setInfo" name="setInfo" maxlength="50" style="width:400px;"
                           class="input_false" value="" readonly="readonly">
                  </div>
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;</td>
                <td align="left" colspan="2">
                  <div id="one16">
                    <span class="text">原因</span>
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
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6" src="./index.js"></script>

