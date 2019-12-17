<template>
  <!-- 选择告警的弹出框 -->
  <el-dialog
    :title="$t('io.alarm.chooseAlarm')"
    :visible="isShowChooseAlarm"
    :close-on-click-modal="false"
    :before-close="beforeClose"
    width="75%" append-to-body>
    <div class="choose-alarm">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchData" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="searchData.alarmName" :placeholder="$t('io.alarmName')" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchData.alarmType" :placeholder="$t('io.alarm.alarmType')">
              <el-option :label="$t('io.alarm.devAlarm')" :value="1"></el-option>
              <el-option :label="$t('io.alarm.smartAlarm')" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchData.alarmLevel" :placeholder="$t('io.alarm.alarmLevel')" clearable>
              <el-option :label="$t('io.alarmLevelArr')[0]" value="1"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[1]" value="2"></el-option>
              <el-option :label="$t('io.alarmLevelArr')[2]" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('io.alarm.happendTime')">
            <el-date-picker
              v-model="searchData.startTime"
              type="datetime"
              :format="$t('dateFormat.yyyymmddhhmmss')"
              value-format="timestamp" :editable="false"
              :placeholder="$t('io.alarm.happendTime')">
            </el-date-picker> ~
            <el-date-picker
              v-model="searchData.endTime"
              type="datetime"
              :format="$t('dateFormat.yyyymmddhhmmss')"
              value-format="timestamp" :editable="false"
              :placeholder="$t('io.alarm.happendTime')">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchDataList(true)">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 显示数据的列表 -->
      <div class="table-bar">
        <el-table
          :data="list"
          tooltip-effect="dark"
          ref="multipleAlarmTable"
          style="width: 100%"
          :maxHeight="maxHeight"
          border
          @row-click="toggleSelection"
          @selection-change="handleSelectionChange">
          <el-table-column
            type="selection"
            width="55" align="center">
          </el-table-column>
          <el-table-column :label="$t('io.plantName')" prop="stationName" align="center">
          </el-table-column>
          <el-table-column prop="devAlias" :label="$t('io.alarm.devName')" align="center">
          </el-table-column>
          <el-table-column prop="alarmName" :label="$t('io.alarmName')" align="center">
          </el-table-column>
          <el-table-column prop="alarmLevel" :label="$t('io.alarm.alarmLevel')" show-overflow-tooltip align="center" width="100">
            <template slot-scope="scope">{{ scope.row.alarmLevel | alarmLevelFilter(vm) }}</template>
          </el-table-column>
          <el-table-column prop="createDate" :label="$t('io.alarm.happendTime')" width="160" align="center">
            <template slot-scope="scope">{{ scope.row.createDate | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</template>
          </el-table-column>
          <el-table-column prop="devTypeName" :label="$t('io.devType')" align="center" width="120">
          </el-table-column>
          <el-table-column prop="alarmState" :label="$t('io.alarmStatus')" align="center" width="100">
            <template slot-scope="scope">{{ scope.row.alarmState | alarmStatusFilter(vm) }}</template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button @click="beforeClose">{{$t('cancel')}}</el-button>
        <el-button type="primary" @click="chooseHandler">{{$t('sure')}}</el-button>
      </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import alarmService from '@/service/io/alarm'
  import localStorage from '@/utils/localStorage'

  const props = {
    isShowChooseAlarm: {
      type: Boolean,
      custom: true,
      default: false
    },
    devId: {
      type: Number,
    },
  }
  export default {
    created () {
      this.calMaxHeight();
    },
    props: props,
    components: {},
    data () {
      return {
        maxHeight: 120, // 表格的最大高度
        searchData: {
          alarmType: 1, // 告警类型
          page: 1,
          pageSize: 10
        },
        total: 0,
        list: [],
        multipleSelection: null, // 多选的数据
        stationCode: "",
        vm: this
      }
    },
    filters: {
      alarmLevelFilter (val, vm) {
        if (!val || isNaN(val)) {
          return '';
        }
        if (val === 1) {
          // 严重
          return vm.$t('io.alarmLevelArr')[0]
        }
        if (val === 2) {
          // 一般
          return vm.$t('io.alarmLevelArr')[1]
        }
        if (val === 3) {
          // 提示
          return vm.$t('io.alarmLevelArr')[2]
        }
        // 未知
        return vm.$t('io.unKnow')
      },
      // 1 未处理 2 已确认 3 已恢复
      alarmStatusFilter (val, vm) { // 告警处理状态的过滤器
        if (isNaN(val)) {
          return '';
        }
        if (val === 1) {
          // 未处理
          return vm.$t('io.alarmStatusArr')[0]
        }
        if (val === 2) {
          // 已确认
          return vm.$t('io.alarmStatusArr')[1]
        }
        if (val === 3) {
          // 处理中
          return vm.$t('io.alarmStatusArr')[2]
        }
        if (val === 4) {
          // 已处理
          return vm.$t('io.alarmStatusArr')[3]
        }
        if (val === 5) {
          // 已清除
          return vm.$t('io.alarmStatusArr')[4]
        }
        if (val === 6) {
          // 已恢复
          return vm.$t('io.alarmStatusArr')[5]
        }
        // '未知'
        return vm.$t('io.unKnow')
      },
    },
    mounted: function () {
      let self = this;
      this.stationCode = localStorage.getStore('stationParams').stationCode;
      self.searchDataList();
      self.$nextTick(function () {
      })
      // window.onresize = () => self.calMaxHeight
      window.addEventListener("resize", self.calcMaxHeight);
    },
    methods: {
      calMaxHeight () { // 计算表格的最大高度
        let height = document.documentElement.clientHeight - 400;
        if (height < 120) {
          height = 120;
        }
        this.maxHeight = height;
      },
      searchDataList (isToFirst) { // 查询数据
        let urlName;
        if (this.searchData.alarmType === 1) { // 查询设备告警
          urlName = 'alarmOnlineList';
        } else {
          urlName = 'analysisList'; // 查询智能告警
        }
        if (isToFirst) {
          this.searchData.page = 1;
        }
        let self = this;
        this.searchData.index = this.searchData.page;
        this.searchData.stationCode = this.stationCode;
        if (this.devId) {
          this.searchData.devId = this.devId;
        }
        // 查询数据
        alarmService[urlName](this.searchData).then(resp => {
          var datas = (resp.code === 1 && resp.results) || {};
          self.list = datas.list || [];
          self.total = datas.total || 0;
        })
      },
      handleSelectionChange(val) { // 选择的数据改变的事件
        this.multipleSelection = val;
      },
      toggleSelection (row, event, column) {
        this.$refs.multipleAlarmTable.toggleRowSelection(row);
      },
      beforeClose () { // 关闭弹出框的调用的方法
        this.$emit('update:isShowChooseAlarm', false); // 调用父节点调用组件的时候isShowChooseAlarm.sync同步修改
      },
      chooseHandler () { // 选择确认的事件
        let multipleSelection = this.multipleSelection;
        let len = (multipleSelection && multipleSelection.length) || 0;
        if (len === 0) { // 没有选择的时候的情况
          // 前面的回调函数
          this.$emit('choose-alarm');
          this.beforeClose(); // 关闭对话框
          return;
        }
        // 判断选择的是不是相同的设备类型
        let fistDevType = multipleSelection[0].devName;
        for (let i = 1; i < len; i++) {
          if (fistDevType !== multipleSelection[i].devName) {
            this.$message(this.$t('io.alarm.selectSameDevToDefect'));
            return;
          }
        }
        // 前面的回调函数
        this.$emit('choose-alarm', multipleSelection, multipleSelection[0].devId, multipleSelection[0].stationCode);
        this.beforeClose(); // 关闭对话框
      }
    },
    watch: {},
    computed: {},
    beforeDestroy () {
    },
    destroyed() {
      window.removeEventListener("resize", this.calMaxHeight);
    }
  }
</script>

<style lang="less" scoped>
</style>

