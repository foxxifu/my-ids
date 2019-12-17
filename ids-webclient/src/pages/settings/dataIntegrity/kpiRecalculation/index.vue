<template>
  <div class='kpiRecalculation'>
    <!-- 搜索栏 -->
    <div class="" style="float: left">
      <el-form :inline="true" :model="searchParam" class="demo-form-inline" >
        <el-form-item :label="$t('kpiRec.taskName')">
          <el-input v-model="searchParam.taskName" :placeholder="$t('kpiRec.taskName')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('kpiRec.stationName')">
          <el-input v-model="searchParam.stationName" :placeholder="$t('kpiRec.stationName')" clearable></el-input>
        </el-form-item>
        <el-select v-model="searchParam.taskStatus">
          <el-option v-for="item in status" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <!-- 查询按钮 -->
        <el-form-item>
           <el-button type="primary" icon="el-icon-search" @click="queryCalcTask">{{$t('search')}}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 新增 -->
    <div class="btns" style="float: right;margin-bottom: 10px;">
      <el-button @click="createCalcTask" type="primary">{{ $t('kpiRec.add') }}</el-button>
    </div>

    <!-- 任务列表 -->
    <div class="list-table">
      <el-table ref="singleTable" :data="calcTaskData.list" border max-height="670">
        <el-table-column :label="$t('kpiRec.taskName')" align="center" prop="taskName"></el-table-column>
        <el-table-column :label="$t('kpiRec.stationName')" align="center" prop="stationName"></el-table-column>
        <el-table-column :label="$t('kpiRec.taskStatus')" align="center" prop="taskStatus">
          <template slot-scope="scope" v-model="exeProcess">
            {{scope.row.taskStatus | taskStatusFilter(vm)}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('kpiRec.startTime')" align="center" prop="startTime">
          <template slot-scope="scope" v-model="exeProcess">
            {{scope.row.startTime | taskTimeFilter($t('dateFormat.yyyymmddhhmmss'))}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('kpiRec.endTime')" align="center" prop="endTime">
          <template slot-scope="scope" v-model="exeProcess">
            {{scope.row.endTime | taskTimeFilter($t('dateFormat.yyyymmddhhmmss'))}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('kpiRec.process')" align="center">
          <template slot-scope="scope">
            <el-progress :text-inside="true" :stroke-width="25" :percentage="scope.row.percentage" :status="scope.row.procStatus"></el-progress>
          </template>
        </el-table-column>
        <el-table-column :label="$t('kpiRec.operation')" align="center" width="240">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-caret-right" :disabled="scope.row.taskStatus !== 1 && scope.row.taskStatus !==4" class="table-skip-text-color" @click="executeCalcTask(scope.row, $event)">
              {{ $t('kpiRec.execute')}}
            </el-button>
            <el-button type="text" icon="el-icon-edit" :disabled="scope.row.taskStatus !== 1" class="table-skip-text-color" @click="modifyCalcTask(scope.row, $event)">
              {{ $t('kpiRec.edit') }}
            </el-button>
            <el-button type="text" icon="el-icon-delete" :disabled="scope.row.taskStatus !== 1" class="table-skip-text-color" @click="removeCalcTask(scope.row, $event)">
              {{ $t('kpiRec.del') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页栏 -->
    <div style="overflow: hidden;margin-top: 10px;">
      <div style="float: right;right: 10px;">
        <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchParam.index"
                       :page-sizes="[10, 20, 30, 50]" :page-size="searchParam.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" :total="calcTaskData.total">
        </el-pagination>
      </div>
    </div>
    <!-- 弹出框 -->
    <el-dialog :title="getDialogTitle" :visible.sync="isShowDialog" width="40%" >
      <el-form :model="calcTaskModel" ref="kpiRecalcForm" :rules="formValidRules" class="kpiRecalcForm">
        <el-table :data="[{},{},{}]" ref="kpiRecalcTable" border :show-header="false"
                  :cell-style="cellStyle" class="kpiRecalcTable">
          <el-table-column align="center" :width="150">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('kpiRec.taskName')}}</span></div>
              <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{$t('kpiRec.stationName')}}</span></div>
              <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{$t('kpiRec.timeDuration')}}</span></div>
            </template>
          </el-table-column>

          <el-table-column align="left">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0">
                <el-form-item prop="taskName">
                  <el-input type="text" v-model="calcTaskModel.taskName" :placeholder="$t('kpiRec.taskName')"></el-input>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 1">
                <el-form-item prop="stationCode">
                  <!-- 选择电站的弹出框的输入文本框 -->
                  <StationInput
                    :myValues="calcTaskModel.stationCode"
                    :myTexts="calcTaskModel.stationName"
                    @station-change="stationChange"
                    :isMutiSelect="false"></StationInput>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 2">
                <el-form-item prop="taskTimeDuration">
                  <el-date-picker
                    v-model="calcTaskModel.taskTimeDuration"
                    type="datetimerange"
                    :format="$t('dateFormat.yyyymmddhhmmss')"
                    range-separator="-"
                    :start-placeholder="$t('kpiRec.startTime')"
                    :end-placeholder="$t('kpiRec.endTime')" style="width: 100%">
                  </el-date-picker>
                </el-form-item>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <el-form-item class="tr-border" align="center" style="margin-top: 20px">
          <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
          <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
        </el-form-item>

      </el-form>

    </el-dialog>
  </div>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
