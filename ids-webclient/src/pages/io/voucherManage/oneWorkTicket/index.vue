<style lang="less" src="./index.less" scoped></style>

<template>
  <div style="height: 100%">
    <div class="search-bar clear">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <!--<el-form-item label="编号">-->
          <!--<el-input v-model="searchData.id" placeholder="编号" clearable style="width: 120px"></el-input>-->
        <!--</el-form-item>-->
        <el-form-item :label="$t('io.workTick.workFzr')">
          <el-input v-model="searchData.chargeName" :placeholder="$t('io.workTick.workFzr')" clearable style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item :label="$t('io.workTick.plantStartTime')">
          <el-date-picker
            v-model="searchData.startWorkTime" :placeholder="$t('io.workTick.plantStartTime')" type="datetime" :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
          ~
          <el-date-picker
            v-model="searchData.endWorkTime" :placeholder="$t('io.workTick.plantEndTime')" type="datetime" :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('io.processStatus')">
          <el-select style="width: 180px" v-model="searchData.processStatus" :placeholder="$t('io.workTick.all')" clearable>
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

        <el-table-column prop="serialNum" :label="$t('io.workTick.num')" width="80" align="center">
        </el-table-column>

        <el-table-column prop="chargeName" :label="$t('io.workTick.workFzr')" width="120" align="center">
        </el-table-column>

        <el-table-column prop="workContent" :label="$t('io.workTick.workTask')" align="center" :show-overflow-tooltip="true">
        </el-table-column>

        <el-table-column prop="startWorkTime" :label="$t('io.workTick.plantStartTime')" width="180" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.startWorkTime && Date.parse(scope.row.startWorkTime).format($t('dateFormat.yyyymmddhhmmss'))
              }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="endWorkTime" :label="$t('io.workTick.plantEndTime')" width="180" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.endWorkTime && Date.parse(scope.row.endWorkTime).format($t('dateFormat.yyyymmddhhmmss')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processTime" :label="$t('io.workTick.dealUserTime') + '(h)'" width="140" align="center">
          <template slot-scope="{row, $index}">
            <span>{{row.startWorkTime | dealHourOfTimeFilter(row.endWorkTime)}}</span>
          </template>
        </el-table-column>

        <el-table-column prop="createDate" :label="$t('io.workTick.createTime')" width="135" align="center" :sortable="true">
          <template slot-scope="scope">
            <span>{{ scope.row.createDate && Date.parse(scope.row.createDate).format($t('dateFormat.yyyymmdd')) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processStatus" :label="$t('io.processStatus')" width="135" align="center">
          <template slot-scope="scope">
            <span>{{ $t('io.workTick.processStatus')[scope.row.processStatus]
              }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="processPerson" :label="$t('io.currentUser')" width="135" align="center">
          <template slot-scope="{row}">
            <el-button type="text">{{ row.processPerson | processPersonFilter}}</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="alarmId" :label="$t('io.defect.affiliateAlarm')" align="center">
          <template slot-scope="scope">
            <el-button type="text">{{ scope.row.alarmNames }}</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="operator" :label="$t('io.operate')" width="200" align="center">
          <template slot-scope="{row, $index}">
            <el-button type="text" @click.stop="viewHandler(row)"><span>{{$t('io.seeView')}}</span></el-button>
            <template
              v-if="+row.processUserId === +loginUserId && row.processStatus && row.processStatus !== 'processEnd' && row.processStatus !== 'giveup'">
              <span> | </span>
              <el-button type="text" @click.stop="processHandler(row)">
                <span v-if="row.processStatus === 'createFirstWork'">{{$t('io.workTick.submit')}}</span>
                <span v-else>{{$t('io.workTick.sh')}}</span>
              </el-button>
            </template>
            <template v-if="+row.processUserId === +loginUserId && row.processStatus && row.processStatus === 'issueFirstWork'">
              <span> | </span>
              <el-button type="text" @click.stop="endWorkFlow(row)">
                <span>{{$t('io.workTick.zf')}}</span>
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <div style="overflow: hidden;margin-top: 10px;">
        <div style="float: right;right: 10px;">
          <el-pagination :current-page="searchData.index" :page-sizes="[10, 20, 30, 50]"
                         :page-size="searchData.pageSize"
                         @size-change="pageSizeChange" @current-change="pageChange"
                         layout="total, sizes, prev, pager, next, jumper" :total="ticketTableData.total"
                         style="float: right; margin-top: 5px;">
          </el-pagination>
        </div>
      </div>
    </div>
    <OneTicketDialog :dialogType="ticketCreateDialog.type" :ticketCreateForm="ticketCreateForm" @onSaveOrUpdateSuccess="searchTicket()"
                     v-if="ticketCreateDialog.isVisible" :isShowDialog.sync="ticketCreateDialog.isVisible" :allUsers="allUsers"></OneTicketDialog>

    <OperationOneTickDialog v-if="operatTransDatas.isShowOperationDialog" :isShowDialog.sync="operatTransDatas.isShowOperationDialog"
                            :definitionId="operatTransDatas.operationId" :isSelectNextUser="operatTransDatas.isSelectNextUser"
                            :title="operatTransDatas.operatTitle" :isReback="operatTransDatas.isReback"  @dealSuccess="dealSuccess"></OperationOneTickDialog>
  </div>
</template>

<script type="text/ecmascript-6" src="./index.js"></script>

