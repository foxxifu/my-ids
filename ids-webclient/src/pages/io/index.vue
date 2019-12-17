<template>
  <!-- 运维工作台的界面 -->
  <el-container class="io-index">
    <div class="map-path">
      <ul>
        <li v-for="path in navMapArr" :key="path.nodeId + '_' + path.nodeType" class="map-path-item"
            @click="navLiClick(path)">
          <span>{{path.nodeName}}</span>
        </li>
      </ul>
    </div>
    <div class="io-main" :initViewBounds="mapAreaBounds" :delegateEvent="delegateEvent" is="IDS_Map" :center="center"  :zoom="6" :clusterMarkers="mapClusterMarkers" style="overflow: hidden;">
    </div>
    <!-- 显示电站统计 -->
    <div class="pos-abs io-total" style="z-index: 2">
      <div class="io-total-title"><label>{{$t('io.stationNum')}}{{ stationTotalStation }}</label><label style="padding:0 20px;">{{$t('io.capacity') + ': '}}{{ captory | fixedNumber(3) }}</label>
        <i class="io-total-hand" @click="stationInfoClick"
           :class="{'el-icon-caret-bottom': !isIoTitleOpen,'el-icon-caret-top': isIoTitleOpen}"></i>
      </div>
      <!--<div class="show-title" :class="{'is-show-title':isIoTitleOpen}" ref="stationChartContainer">-->
      <!--</div>-->
      <div class="show-title" :class="{'is-show-title':isIoTitleOpen}">
        <e-chart :options="stationInfo.option" style="width: 100%;height: 100%;"></e-chart>
      </div>
    </div>
    <!-- 按钮栏 -->
    <div class="pos-abs io-btns">
      <!-- 电站管理 按钮 -->
      <el-button type="primary" :class="{isActive:isPlatform,'normal-btn-color':true}" @click="platformClick">{{$t('io.stationList')}}
      </el-button>
      <!-- 告警管理 按钮 -->
      <el-button type="primary" :class="{isActive:isAlarm,'normal-btn-color':true}" @click="alarmClick"
                 >{{$t('io.alarmList')}}
      </el-button>
      <!-- 消缺任务 按钮 -->
      <el-button type="primary" :class="{isActive:isDefect,'normal-btn-color':true}" @click="defectClick"
                 >{{$t('io.defectList')}}
      </el-button>
      <!-- 一种工作票 按钮 -->
      <el-button type="primary" :class="{isActive:isOneWorkTicket,'normal-btn-color':true}" @click="oneWorkTicketClick">
        {{$t('io.workList')}}
      </el-button>
    </div>
    <!-- 左边底部搜索栏 -->
    <!--<div class="pos-abs io-search">-->
      <!--<el-form :inline="true" class="demo-form-inline">-->
        <!--<el-form-item>-->
          <!--<el-input v-model="searchValue" placeholder="搜索运维人员或者电站名"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item>-->
          <!--<el-button type="primary" @click="">查询</el-button>-->
        <!--</el-form-item>-->
      <!--</el-form>-->
    <!--</div>-->
    <!-- 显示列表 -->
    <div class="pos-abs io-showList" :class="{'is-io-show-list':showListTable}">
      <div style="width: 100%;">
        <div class="io-showList-head" style="overflow: hidden;width: 100%;text-align: center;padding: 10px 0;
          border-bottom:1px solid #00ffff;">
          <label style="font-size: 18px;">{{ getListTitle }}</label>
          <div style="position: absolute;right: 10px;top:10px;">
            <i class="el-icon-menu" :title="$t('detail')" style="font-size: 16px;cursor: pointer;"
            @click="isShowListDetal=true"></i>
            <i class="el-icon-close" :title="$t('close')" style="margin-left: 15px;font-size: 16px;cursor: pointer;"
               @click="showListTable = false;currentComp=null"></i>
          </div>
        </div>
        <div class="list">
          <!-- 表头 -->
          <div style="width: 100%;background: #D1F2FB;color: #54B8D1;font-weight: 700">
            <div class="my-li-dev my-li-col1">{{ getHeadArr[0] }}</div>
            <div class="my-li-dev my-li-col2">{{ getHeadArr[1] }}</div>
            <div class="my-li-dev my-li-col3">{{ getHeadArr[2] }}</div>
            <div class="my-li-dev my-li-col4">{{ getHeadArr[3] }}</div>
            <div class="my-li-dev my-li-col5">{{ getHeadArr[4] }}</div>
          </div>
          <!-- 表内容 -->
          <div style="width:100%;" class="my-table-content-div">
            <ul style="width: 100%;" class="tab-content-ul">
              <!-- 运维工作台的界面，不可拖动li -->
              <li style="width: 100%;" v-if="isPlatform" v-for="item in list" :key="item.id">
                <div class="my-li-dev my-li-col1">
                  <div class="text-ellipsis" :title="item.stationName">{{ item.stationName }}</div>
                </div>
                <div class="my-li-dev my-li-col2">{{ item.installedCapacity }}</div>
                <div class="my-li-dev my-li-col3">{{ item.activePower }}
                </div>
                <div class="my-li-dev my-li-col4">{{ item.dayCapacity }}
                </div>
                <div class="my-li-dev my-li-col6">
                  <i class="status-cirle" :class="{'status-blue':item.stationStatus === 3, 'status-red':item.stationStatus === 1}"
                  :title="item.stationStatus | stationStatusFilter(vm)"></i>
                  {{ item.stationStatus | stationStatusFilter(vm) }}
                </div>
              </li>
              <!-- 告警管理的界面 -->
              <li style="width: 100%;" v-if="isAlarm" v-for="item in list" :key="item.alarmId" draggable="true"
                  @dragstart="drag"
                  :title="$t('io.dragAlarm')"
                  :data-alarm-id="item.alarmId" :data-alarm-type="item.alarmType">
                <div class="my-li-dev my-li-col1">
                  <div class="text-ellipsis" :title="item.stationName">
                    {{ item.stationName }}
                  </div>
                </div>
                <div class="my-li-dev my-li-col2">
                  <div class="text-ellipsis" :title="alarmNameFormat(item.alarmName, item.alarmType, item.alarmModelId, 'alarmName')">{{ alarmNameFormat(item.alarmName, item.alarmType, item.alarmModelId, 'alarmName') }}</div>
                </div>
                <div class="my-li-dev my-li-col3">
                  <label :class="{'text-color-red':item.alarmState === 0}">{{ item.alarmState | alarmStatusFilter(vm) }}</label>
                </div>
                <div class="my-li-dev my-li-col4">
                  <label :class="{'text-color-red':item.alarmLevel === 1,
                  'text-color-blue':item.alarmLevel === 2,
                  'text-color-green':item.alarmLevel === 3}">{{ item.alarmLevel | alarmLeveFilter(vm)}}</label>
                </div>
                <div class="my-li-dev my-li-col6">
                  <a style="cursor: pointer;color: #54B8D1" @click="showAlarmDetail(item)">{{$t('detail')}}</a>
                </div>
              </li>
              <!-- 消缺任务的界面 不可拖动 -->
              <li style="width: 100%;" v-if="isDefect" v-for="item in list" :key="item.taskId">
                <div class="my-li-dev my-li-col1">
                  <div class="text-ellipsis" :title="item.stationName">{{ item.stationName}}</div>
                </div>
                <div class="my-li-dev my-li-col2">
                  <div class="text-ellipsis" :title="item.taskDesc">{{ item.taskDesc }}</div>
                </div>
                <div class="my-li-dev my-li-col3">
                  <label :class="'defect-color-state' + item.taskStatus">{{ item.taskStatus | defectStateFilter(vm) }}</label>
                </div>
                <div class="my-li-dev my-li-col4">{{ item.currDealUser }}
                </div>
                <div class="my-li-dev my-li-col6">
                  <a style="cursor: pointer;color: #090;" @click="showDefectDetail(item)">{{$t('io.seeView')}}</a>&nbsp;
                  <!-- admin用户的id是1，或者当前的处理人是登录用户id -->
                  <a style="cursor: pointer;color: #090;" @click="executeDefectDetalClick(item)" v-if="loginUserId === 1 || item.userId === loginUserId">{{$t('io.doExcut')}}</a></div>
              </li>
              <!-- 一种工作票 不可拖动 -->
              <li style="width: 100%;" v-if="isOneWorkTicket" v-for="item in list" :key="item.taskId">
                <div class="my-li-dev my-li-col1">
                  <div class="text-ellipsis" :title="item.workContent">{{ item.workContent}}</div>
                </div>
                <div class="my-li-dev my-li-col2">
                  <div class="text-ellipsis" :title="item.startWorkTime | timestampFomat">{{ item.startWorkTime | timestampFomat}}</div>
                </div>
                <div class="my-li-dev my-li-col3">
                  <label>
                    {{ $t('io.workTick.processStatus')[item.processStatus]
                    }}</label>
                </div>
                <div class="my-li-dev my-li-col4">{{ item.processPerson }}
                </div>
                <div class="my-li-dev my-li-col6">
                  <a style="cursor: pointer;color: #090;" @click="showTickDialog(item)">{{$t('io.seeView')}}</a>
                </div>
              </li>
            </ul>
          </div>
          <!-- 分页的内容 -->
          <div style="margin-top: 10px;overflow-x: hidden;float: right">
            <el-pagination small @size-change="pageSizeChange" @current-change="pageChange"
                           :current-page="pageDatas.page" :page-sizes="[10, 20, 30, 50]" :page-size="pageDatas.pageSize"
                           layout="total, sizes, prev, pager, next" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
      <!-- 弹出详细的dialog -->
      <el-dialog
        :title="getListTitle"
        :visible.sync="isShowListDetal"
        width="96%"
        :close-on-click-modal="false"
        :before-close="closeListDetail">
        <Platform v-if="isShowListDetal && isPlatform"></Platform>
        <Alarm v-if="isShowListDetal && isAlarm"></Alarm>
        <Defect v-if="isShowListDetal && isDefect"></Defect>
        <OneWorkTicket v-if="isShowListDetal && isOneWorkTicket"></OneWorkTicket>
      </el-dialog>
      <!-- 告警详情的弹出框  告警修复建议-->
      <el-dialog
        :title="$t('io.alarmDetailTitle')"
        :visible.sync="isShowDetailDialog"
        width="80%"
        :close-on-click-modal="false"
        append-to-body>
        <!-- 修改为查询告警数据 -->
        <AlarmDetailDialgContent v-if="isShowDetailDialog" :needSearch="[true, {id:rowData.alarmId}, rowData.alarmType, {stationName: rowData.stationName}]"></AlarmDetailDialgContent>
      </el-dialog>
      <AddDefectDialog v-if="isShowAddDefect" :is-show-add-defect.sync="isShowAddDefect" :rowDatas="addDefectFormData"></AddDefectDialog>
      <DefectDetalDailog v-if="isShowDefectDetail" @on-success-cb="exeTaskSuccess" :isShowDefectDetail.sync="isShowDefectDetail" :defectId="defectId" :isDoExect="isDoExect"></DefectDetalDailog>
      <OneWorkTickDialog  dialogType="detail" :ticketCreateForm="ticketForm"
                          v-if="isShowOneWorkTickDialog" :isShowDialog.sync="isShowOneWorkTickDialog" :allUsers="tickUsers"></OneWorkTickDialog>
    </div>
  </el-container>
</template>

<script src="./index.js"></script>
<style lang="less" src="./index.less" scoped></style>

