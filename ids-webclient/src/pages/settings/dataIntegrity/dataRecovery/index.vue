<template>
  <el-container class="dataRecovery">
    <!-- 查询栏 -->
    <el-header>
      <div class="" style="float: left">
        <el-form :inline="true" :model="searchData" class="demo-form-inline">
          <!-- 设备名称 -->
          <el-form-item :label="$t('dataRecovery.devName')">
            <el-input v-model="searchData.devName" :placeholder="$t('dataRecovery.devName')" clearable></el-input>
          </el-form-item>
          <!-- 执行结果 -->
          <el-form-item :label="$t('dataRecovery.exeResult')">
            <el-select v-model="searchData.exeStatus" :placeholder="$t('dataRecovery.exeResult')" clearable>
              <el-option
                v-for="(item, index) in exeResultArr"
                :key="index"
                :label="$t('dataRecovery.exeStatus.' + item)"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="doSearch(true)">{{ $t('search') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 按钮栏 -->
      <div class="btns" style="float: right;margin-bottom: 10px;">
        <el-button @click="showDialog" type="primary">{{ $t('create') }}</el-button>
      </div>
    </el-header>
    <el-main>
      <el-table :data="list" style="width: 100%" :maxHeight="maxTableHeight" border>
        <!-- 电站名称 -->
        <el-table-column prop="stationName" :label="$t('dataRecovery.stationName')" align="center">
        </el-table-column>
        <!-- 设备名称 -->
        <el-table-column prop="devName" :label="$t('dataRecovery.devName')" align="center">
        </el-table-column>
        <!-- 执行结果 -->
        <el-table-column prop="exeStatus" :label="$t('dataRecovery.exeResult')" align="center">
          <template slot-scope="{row}">
            {{ $t('dataRecovery.exeStatus.' + row.exeStatus) }}
          </template>
        </el-table-column>
        <!-- 开始时间 -->
        <el-table-column prop="startTime" :label="$t('dataRecovery.startTime')" width="180" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime | timestampFomat }}
          </template>
        </el-table-column>
        <!-- 结束时间 -->
        <el-table-column prop="endTime" :label="$t('dataRecovery.endTime')" width="180" align="center">
          <template slot-scope="scope">
            {{ scope.row.endTime | timestampFomat }}
          </template>
        </el-table-column>
        <!-- 补采进度 -->
        <el-table-column prop="progress" :label="$t('dataRecovery.miningProgress')" align="center">
          <template slot-scope="{ row }" >
            <el-progress v-if="row.exeStatus === 1" :percentage="row.progressNum" :stroke-width="20" status="success"
                         :text-inside="true"></el-progress>
            <span v-else>{{ $t('dataRecovery.exeStatus.' + row.exeStatus) }}</span>
          </template>
        </el-table-column>
        <!-- 补采状态 -->
        <!--<el-table-column prop="kpiReCompStatus" :label="$t('dataRecovery.kpiRecalcResult')" align="center">
          <template slot-scope="{row}">
            {{ $t('dataRecovery.kpiReCompStatus.' + row.kpiReCompStatus) }}
          </template>
        </el-table-column>-->
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-button @click="exeReloadData(row.id)" type="primary" v-if="row.exeStatus === 0">执行</el-button>
            <el-button @click="deleteData(row.id)" type="primary" v-if="row.exeStatus !== 1">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="overflow: hidden;margin-top: 10px;">
        <div style="float: right;right: 10px;">
          <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchData.page"
                         :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize"
                         layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>

      <!-- 弹出框新增数据的补采 -->
      <add-data-replace :isShow.sync="isAddDialog" v-if="isAddDialog" @save-success="queryPage"/>
    </el-main>
  </el-container>
</template>
<script type="text/ecmascript-6">
  import DataMiningService from '@/service/dataMining'
  import AddDataReplace from './addDialog/createData.vue'
  import TableHeightHandler from '@/pages/plugins/heightMixin/TableHightHandler'

  export default {
    components: {
      AddDataReplace
    },
    mixins: [TableHeightHandler],
    data () {
      return {
        list: [],
        // 执行结果的选择框 0:未补采 1：补采中 2：补采成功  -1：补采失败，
        exeResultArr: [-1, 0, 1, 2],
        searchData: {
          page: 1,
          pageSize: 10,
          exeStatus: null
        },
        // 真正的查询的数据
        realSearchData: {},
        // 总记录数
        total: 0,
        timer: null,
        // 新增记录
        isAddDialog: false,
        deductNum: 300
      }
    },
    filters: {
      timestampFomat (val, fmt = 'yyyy-MM-dd HH:mm:ss') { // 时间的格式化
        if (!val) {
          return ''
        }
        return new Date(val).format(fmt)
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    created () {
      this.doSearch()
      // 每30s刷新一次
      this.timer = setInterval(() => this.queryPage(), 30000)
    },
    methods: {
      doSearch (isToFirst) {
        if (isToFirst) {
          this.searchData.page = 1
        }
        this.realSearchData = Object.assign({}, this.searchData)
        this.queryPage()
      },
      queryPage () { // 查询数据
        DataMiningService.findPage(this.realSearchData).then(resp => {
          if (resp.code === 1){
            let data = resp.results
            this.list = data.list
            this.total = data.count
          } else {
            this.list = []
            this.total = 0
          }
        }).catch(() => {
          this.list = []
          this.total = 0
        })
      },
      pageSizeChange (val) { // 每页显示的记录数改变的事件
        this.searchData.pageSize = val
        this.doSearch(true)
      },
      pageChange (val) { // 页数改变的事件
        this.searchData.page = val
        this.doSearch()
      },
      showDialog () {
        this.isAddDialog = true
      },
      exeReloadData(taskId) {
        this.$confirm('确认执行当前的补采任务?').then(resp => {
          DataMiningService.reloadData(taskId).then(resp => {
            if (resp.code === 1) {
              this.$message.success('操作成功')
            } else {
              this.$message.error('操作失败')
            }
          }).catch(() => {
            this.$message.error('操作失败')
          })
        })
      },
      deleteData(taskId) {
        this.$confirm('确认删除当前的补采任务?').then(resp => {
          DataMiningService.deleteTask(taskId).then(resp => {
            if (resp.code === 1) {
              this.$message.success('删除成功')
              this.pageChange(this.searchData.page !== 1 && this.list.length === 1
                ? this.searchData.page - 1 : this.searchData.page)
            } else {
              this.$message.error('删除失败')
            }
          }).catch(() => {
            this.$message.error('删除失败')
          })
        })
      }
    },
    watch: {},
    computed: {},
    beforeDestroy () {
      if (this.timer) {
        clearInterval(this.timer)
      }
    }
  }
</script>
<style lang="less" src="./index.less" scoped></style>

