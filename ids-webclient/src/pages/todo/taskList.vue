<!-- 任务处理列表的插件 目前支持3种 待处理：0， 处理中：1,2， 已完成：3 -->
<template>
    <div class="task-list-parent">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchBarData" class="demo-form-inline">
          <el-form-item :label="$t('todo.stationName')">
            <el-input v-model="searchBarData.stationName" :placeholder="$t('todo.stationName')" clearable></el-input>
          </el-form-item>
          <el-form-item :label="$t('todo.occuringTime')">
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
            <el-button type="primary" @click="searchListData(true)">{{$t('search')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 表格数据 -->
      <div class="list-table">
        <el-table :data="list" width="100%"
                  border :maxHeight="maxHeight">
          <el-table-column :label="$t('todo.taskNum')" prop="defectCode" width="130" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.taskName')" prop="defectName" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.stationName')" prop="stationName" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.alarmDev')" prop="devAlias" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.alarmName')" prop="alarmNames" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.startTime')" prop="startTime" width="160" align="center">
            <template slot-scope="scope">
              {{ scope.row.startTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('todo.endTime')" prop="endTime" width="160" align="center">
            <template slot-scope="scope">
              {{ scope.row.endTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('todo.currentPersion')" prop="currentUserName" width="90" align="center">
          </el-table-column>
          <el-table-column :label="$t('todo.procStatus')" prop="procState" width="80" align="center">
            <template slot-scope="scope">
              <label :class="['proc-state', 'proc-state' + scope.row.procState]">{{ scope.row.procState | procStateFliter(vm) }}</label>
            </template>
          </el-table-column>
          <el-table-column :label="$t('io.operate')" prop="currentUserId" width="140" align="center">
            <template slot-scope="scope">
              <div style="color: #169BD5;">
                <a style="cursor: pointer;" @click.prevent="showDefectDetalClick(scope.row)">{{$t('detail')}}</a>
                <!-- 待分配的 -->
                <a v-if="isPending && isHasExcute(scope.row)"
                   @click.prevent="executeDefectDetalClick(scope.row)"
                   style="margin-left: 10px;cursor: pointer;">{{$t('io.defect.accept')}}</a>
                <a v-if="isPending && isHasExcute(scope.row)"
                   @click.prevent="showTaskTransfer(scope.row)"
                   style="margin-left: 10px;cursor: pointer;">{{$t('io.defect.toOtherUser')}}</a>
                <!-- 待处理的 -->
                <a v-if="isProcessing && isHasExcute(scope.row)"
                   @click.prevent="executeDefectDetalClick(scope.row)"
                   style="margin-left: 10px;cursor: pointer;">{{$t('io.doExcut')}}</a>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页数据 -->
      <!-- 分页栏 -->
      <div style="clear: both;overflow: hidden;margin-top: 10px;">
        <div style="float: right">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchBarData.page"
                         :page-sizes="[10, 20, 30, 50]" :page-size="searchBarData.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
      <DefectDetalDailog v-if="isShowDefectDetail" :isShowDefectDetail.sync="isShowDefectDetail" :defectId="defectId" :isDoExect="isDoExect"
       @on-success-cb="searchListData()"></DefectDetalDailog>
      <task-transfer v-if="isTaskTransfer" :dialogVisible.sync="isTaskTransfer" :defectId="defectId"></task-transfer>
    </div>
</template>

<script type="text/ecmascript-6">
  import {cookie} from 'cookie_js'
  import DefectDetalDailog from '@/pages/io/taskManage/defectDetail/index.vue'
  import taskTransfer from '@/pages/todo/taskTransfer.vue'
  import workFlowService from '@/service/workFlow'

  const taskNameArr = ['0', 'dealing', '3'];
  const props = {
    taskName: { // 当前列表的名称
      type: String,
      default: taskNameArr[0] // 目前支持3种 待处理：0， 处理中：1,2， 已完成：3
    },
    activeNameArr: {
      type: Array,
      default: () => taskNameArr
    }
  }
  export default {
    props: props,
    components: {
      DefectDetalDailog,
      taskTransfer
    },
    created () {
      this.calMaxHeight();
      let userStr = cookie.get('userInfo')
      if (userStr) { // 获取登录用户
        this.loginUserId = JSON.parse(userStr).id
      }
    },
    data () {
      return {
        maxHeight: 120,
        isShowDefectDetail: false, // 是否显示详情的对话框
        defectId: null, // 处理或者查看的缺陷的id
        isDoExect: false, // 是否是处理流程
        searchBarData: { // 查询list的查询数据
          page: 1,
          pageSize: 10
        },
        list: [], // 列表数据
        total: 0, // 列表显示的总记录数据
        isTaskTransfer: false, // 是否显示任务转发的弹出框
        loginUserId: null, // 当前登录的用户
        vm: this
      }
    },
    filters: {
      procStateFliter (val, vm) {
        if (val === null || val === undefined || isNaN(val)) {
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
      }
    },
    mounted: function () {
      let self = this;
      this.$nextTick(function () {
        self.searchListData();
      });
      window.onresize = () => self.calMaxHeight();
    },
    methods: {
      searchListData (isToFirst) { // 查询列表数据
        let self = this;
        if (isToFirst) { // 如果是到第一页
          self.searchBarData.page = 1;
        }
        let params = Object.assign({procState: self.taskName}, self.searchBarData);
        params.index = self.searchBarData.page
        workFlowService.getWorkFlowDefectsByCondition(params).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {};
          self.total = datas.count || 0;
          self.list = datas.list;
        });
      },
      showTaskTransfer (row) { // 显示人转发的弹出框
        this.defectId = row.defectId;
        this.isTaskTransfer = true;
      },
      showDefectDetalClick (row) { // 查看任务详情的页面
        this.defectId = row.defectId;
        this.isDoExect = false;
        this.isShowDefectDetail = true;
      },
      executeDefectDetalClick (row) { // 执行任务详情的页面
        this.defectId = row.defectId;
        this.isDoExect = true;
        this.isShowDefectDetail = true;
      },
      pageSizeChange (val) { // 修改分页的每页显示的数量
        this.searchBarData.pageSize = val;
        this.searchListData(true);
      },
      pageChange (val) { // 当分页的数据的页数改变的时候执行的事情
        this.searchBarData.page = val;
        this.searchListData();
      },
      calMaxHeight () {
        let height = document.documentElement.clientHeight - 340;
        if (height < 120) {
          height = 120;
        }
        this.maxHeight = height;
      },
      isHasExcute (item) { // 是否可以执行
        return this.loginUserId === 1 || this.loginUserId === item.currentUserId
      }
    },
    watch: {},
    computed: {
      isPending () { // 是否是待分配的标签
        return this.activeNameArr.indexOf(this.taskName) === 0;
      },
      isProcessing () { // 是否是处理中的标签
        return this.activeNameArr.indexOf(this.taskName) === 1;
      },
      isCompleted () { // 是否是已完成的标签
        return this.activeNameArr.indexOf(this.taskName) === 2;
      }
    }
  }
</script>

<style lang="less" scoped>
  .task-list-parent {
    .proc-state {
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
  }
</style>

