<template>
  <div style="width: 100%;" class="task-manage">
    <!-- 展示所有的任务数量 -->
    <el-row style="width: 100%;margin-bottom: 10px;" class="number-task">
      <el-col :span="4">
        <div class="number-task-middle">
          <div class="number-task-val">{{taskNumObj.total}}</div>
          {{$t('io.defect.totalTask')}}
        </div>
      </el-col>
      <el-col :span="5">
        <div class="number-task-middle proc-state1">
          <div class="number-task-val">{{taskNumObj.waiting}}</div>
          {{$t('io.defect.toBeAssigned')}}
        </div>
      </el-col>
      <el-col :span="5">
        <div class="number-task-middle proc-state2">
          <div class="number-task-val">{{taskNumObj.dealing}}</div>
          {{$t('io.defect.defectClean')}}
        </div>
      </el-col>
      <el-col :span="5">
        <div class="number-task-middle proc-state3">
          <div class="number-task-val">{{taskNumObj.notsure}}</div>
          {{$t('io.defect.pendApproval')}}
        </div>
      </el-col>
      <el-col :span="5">
        <div class="number-task-middle proc-state4" style="border: none;">
          <div class="number-task-val">{{taskNumObj.today}}</div>
          {{$t('io.defect.todayClean')}}
        </div>
      </el-col>
    </el-row>
    <!-- 搜索栏 -->
    <div>
      <el-form :inline="true" :model="searchBarData" class="demo-form-inline" style="float: left;">
        <el-form-item :label="$t('io.plantName')">
          <el-input v-model="searchBarData.stationName" :placeholder="$t('io.plantName')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('io.processStatus')">
          <el-select v-model="searchBarData.procState" :placeholder="$t('io.processStatus')" clearable>
            <el-option :label="$t('io.defectStatusArr')[0]" value="0"></el-option>
            <el-option :label="$t('io.defectStatusArr')[1]" value="1"></el-option>
            <el-option :label="$t('io.defectStatusArr')[2]" value="2"></el-option>
            <el-option :label="$t('io.defectStatusArr')[3]" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('io.alarm.happendTime')">
          <el-date-picker
            v-model="searchBarData.startTime"
            type="datetime"
            :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
          ~
          <el-date-picker
            v-model="searchBarData.endTime"
            type="datetime"
            :format="$t('dateFormat.yyyymmddhhmmss')"
            value-format="timestamp" :editable="false">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getWorkDatas(true)">{{$t('search')}}</el-button>
        </el-form-item>
      </el-form>
      <div style="float: right">
        <el-button @click="addDefectClick" type="primary">{{$t('create')}}</el-button>
        <el-button @click="copyDefectClick" type="primary">{{$t('copy')}}</el-button>
      </div>
    </div>
    <div class="list-table">
      <el-table :data="list" width="100%" ref="multipleDefectTable"
                border :maxHeight="maxHeight"
                @row-click="toggleSelection"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.defect.taskSerial')" prop="defectCode" width="120" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.defect.taskName')" prop="defectName" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.plantName')" prop="stationName" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.defectDesc')" prop="description" align="center">
          <template slot-scope="scope">
            <div class="text-ellipsis" :title="scope.row.description"> {{ scope.row.description }} </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.defect.affiliateAlarmNum')" prop="alarmNum" width="102" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.defect.startTime')" prop="startTime" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.defect.endTime')" prop="endTime" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.endTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.currentUser')" prop="currentUserName" width="136" align="center">
        </el-table-column>
        <el-table-column :label="$t('io.processStatus')" prop="procState" width="160" align="center">
          <template slot-scope="scope">
            <label :class="'proc-state' + scope.row.procState">{{ scope.row.procState | procStateFliter(vm) }}</label>
          </template>
        </el-table-column>
        <el-table-column :label="$t('io.operate')" prop="currentUserId" width="120" align="center">
          <template slot-scope="scope">
            <div style="color: #169BD5;">
              <a style="cursor: pointer;" @click.prevent="showDefectDetalClick(scope.row)">{{$t('detail')}}</a>
              <a v-if="scope.row.currentUserId === loginUserId && scope.row.procState && scope.row.procState !== '3'"
                 @click.prevent="executeDefectDetalClick(scope.row)"
                 style="margin-left: 10px;cursor: pointer;">{{$t('io.doExcut')}}</a>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页栏 -->
      <div style="clear: both;overflow: hidden;margin-top: 10px;">
        <div style="float: right">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchBarData.page"
                         :page-sizes="[10, 20, 30, 50]" :page-size="searchBarData.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
    <AddDefectDialog v-if="isShowAddDefect" @saveSuccess="saveSuccess" :is-show-add-defect.sync="isShowAddDefect" :rowDatas="addDefectFormData"></AddDefectDialog>
    <DefectDetalDailog v-if="isShowDefectDetail" :isShowDefectDetail.sync="isShowDefectDetail" @on-success-cb="saveSuccess" :defectId="defectId" :isDoExect="isDoExect"></DefectDetalDailog>
  </div>
</template>

<script src="./index.js"></script>
<style lang="less" src="./index.less" scoped></style>

