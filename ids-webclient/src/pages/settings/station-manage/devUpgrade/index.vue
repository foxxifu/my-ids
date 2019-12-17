<template>
  <div>
    <el-row>
      <el-col :span="18">
        <el-form :inline="true" :model="searchForm">
          <el-form-item :label="$t('devUpgrade.devName')">
            <el-input v-model="searchForm.devName" :placeholder="$t('devUpgrade.devName')" @change="searchDevs(true)" clearable></el-input>
          </el-form-item>
          <el-form-item :label="$t('devUpgrade.devType')">
            <el-select v-model="searchForm.devTypeId" :placeholder="$t('devUpgrade.devType')" @change="searchDevs(true)" clearable>
              <el-option :label="$t('devTypeId.2')" :value="2"></el-option>
              <el-option :label="$t('devTypeId.1')" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('devUpgrade.upgradeStatus')">
            <!-- 0：未上传  1:文件上传 2:升级中  3：升级成功  -1：升级失败 -->
            <el-select v-model="searchForm.upgradeStatus" :placeholder="$t('devUpgrade.upgradeStatus')" @change="searchDevs(true)" clearable>
              <el-option :label="$t('devUpgrade.upgradeStatusArr')[0]" :value="0"></el-option>
              <el-option :label="$t('devUpgrade.upgradeStatusArr')[1]" :value="1"></el-option>
              <el-option :label="$t('devUpgrade.upgradeStatusArr')[2]" :value="2"></el-option>
              <el-option :label="$t('devUpgrade.upgradeStatusArr')[3]" :value="3"></el-option>
              <el-option :label="$t('devUpgrade.upgradeError')" :value="-1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('devUpgrade.chooseSCdev')">
            <el-select v-model="searchForm.scDevId" :placeholder="$t('devUpgrade.chooseSCdev')" @change="searchDevs(true)" clearable filterable>
              <el-option v-for="dev in scDevList" :label="dev.devAlias" :value="dev.id" :key="dev.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchDevs(true)">{{ $t('search') }}</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="6">
        <div style="text-align: right;margin-right: 10px;">
          <el-button type="primary" @click="showFileUpload">{{ $t('devUpgrade.uploadFile') }}</el-button>
          <el-button type="primary" @click="devUpgrade">{{ $t('devUpgrade.exeUpgrade') }}</el-button>
        </div>
      </el-col>
    </el-row>
    <el-table :data="list" style="width: 100%" :maxHeight="maxTableHeight" @selection-change="handleSelectionChange" border>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <!-- 设备名称 -->
      <el-table-column prop="devAlias" :label="$t('devUpgrade.devName')" align="center">
      </el-table-column>
      <!-- 设备类型 -->
      <el-table-column prop="devTypeId" :label="$t('devUpgrade.devType')" align="center">
        <template slot-scope="{row}">
          {{ row.devTypeId | devTypeIdFormater(vm) }}
        </template>
      </el-table-column>
      <!-- 升级状态 -->
      <el-table-column prop="upgradeStatus" :label="$t('devUpgrade.upgradeStatus')" align="center">
        <template slot-scope="{row}">
          {{ row.upgradeStatus | upgradeStatusFormater(vm) }}
        </template>
      </el-table-column>
      <!-- 升级进度 -->
      <el-table-column prop="upgradeProcess" :label="$t('devUpgrade.upgradeProgress')" align="center">
        <template slot-scope="{row}">
          <el-progress v-if="row.upgradeStatus === 2" :percentage="row.upgradeProcess" :stroke-width="20" status="success"
                       :text-inside="true"></el-progress>
          <span v-else>{{ row.upgradeStatus | upgradeStatusFormater(vm) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <div style="overflow: hidden;margin-top: 10px;">
      <div style="float: right;right: 10px;">
        <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchForm.page"
                       :page-sizes="[10, 20, 30, 50]" :page-size="searchForm.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>
      </div>
    </div>

    <upload-dev-file v-if="isShow" :isShow.sync="isShow" :devIds="selectDevIds" @uploadSuccess="searchDevs()"></upload-dev-file>
  </div>
</template>

<script type="text/ecmascript-6">
    import UploadDevFile from './upload/index.vue'
    import DevService from '@/service/device'
    import TableHightHandler from '@/pages/plugins/heightMixin/TableHightHandler'
    export default {
      name: 'DevUpgrade',
      components: {
        UploadDevFile
      },
      data() {
        return {
          searchForm: {
            devName: '',
            devTypeId: '',
            scDevId: '',
            upgradeStatus: '',
            page: 1,
            pageSize: 10
          },
          scDevList: [],
          total: 0,
          // 升级设备列表信息
          list: [],
          isShow: false,
          deductNum: 200,
          multipleSelection: [],
          selectDevIds: [],
          vm: this,
          selectTimer: null
        }
      },
      mixins: [TableHightHandler],
      created() {
        this.searchScDevs()
        this.searchDevs()
        // 每15s定时刷新一下，就不使用websocket
        this.doIntegerSearch()
      },
      filters: {
        upgradeStatusFormater(val, vm) {
          if (isNaN(val)) {
            return ''
          }
          if (val === -1) {
            return vm.$t('devUpgrade.upgradeError')
          }
          if (val >= 0) {
            return vm.$t('devUpgrade.upgradeStatusArr')[val]
          }
        },
        devTypeIdFormater(val, vm) {
          if (isNaN(val) || val < 1) {
            return ''
          }
          return vm.$t('devTypeId.' + val)
        }
      },
      mounted: function () {
        this.$nextTick(function () {
        })
      },
      methods: {
        searchScDevs() {
          DevService.findAllBindScDevs().then(resp => {
            this.scDevList = (resp.code === 1 && resp.results) || []
          })
        },
        showFileUpload() {
          if (this.multipleSelection.length === 0) {
            this.$message(this.$t('devUpgrade.pleaseChooseDev'))
            return
          }
          let flag = this.multipleSelection.some(item => item.upgradeStatus === 2)
          if (flag) {
            this.$message(this.$t('devUpgrade.hasCannotUpload'))
            return;
          }
          // 当前的设备信息
          this.selectDevIds = this.multipleSelection.map(item => item.id)
          this.isShow = true
        },
        searchDevs(isToFrist) {
          if (isToFrist) {
            this.searchForm.page = 1
          }
          DevService.findUpgradeDevInfos(this.searchForm).then(resp => {
            let datas = (resp.code === 1 && resp.results) || {}
            this.list = datas.list || [];
            this.total = datas.total || 0
          }).catch(() => {
            this.list = [];
            this.total = 0
          })
        },
        pageSizeChange(pageSize) {
          this.searchForm.pageSize = pageSize
          this.searchDevs(true)
        },
        pageChange(page) {
          this.searchForm.page = page
          this.searchDevs()
        },
        handleSelectionChange(val) {
          this.multipleSelection = val
        },
        clearTimer() {
          if (this.selectTimer) {
            clearTimeout(this.selectTimer)
          }
        },
        // 每隔15s中定时调用一次
        doIntegerSearch() {
          this.selectTimer = setTimeout(() => {
            this.clearTimer()
            this.searchDevs()
            this.doIntegerSearch()
          }, 15000)
        },
        // 执行设备升级的点击事件
        devUpgrade() {
          let selectDatas = [...this.multipleSelection]
          if (selectDatas.length === 0) {
            this.$message(this.$t('devUpgrade.pleaseChooseUpgradeDev'))
            return
          }
          if (selectDatas.some(item => item.upgradeStatus !== 1 && item.upgradeStatus !== -1)) {
            this.$message(this.$t('devUpgrade.chooseCanUpgradeDev'))
            return
          }
          let devIds = selectDatas.map(item => item.id)
          this.$confirm(this.$t('devUpgrade.sureUpgradeChooseDev')).then(() => {
            DevService.devUpgrade(devIds).then(resp => {
              if (resp.code === 1) {
                this.$message.success(this.$t('operatSuccess'))
                this.searchDevs()
              } else {
                this.$message.error(this.$t('operatFailed'))
              }
            }).catch(() => {
              this.$message.error(this.$t('operatFailed'))
            })
          })
        }
      },
      watch: {},
      computed: {
      },
      beforeDestroy () {
        this.clearTimer()
      }
    }
</script>

<style lang="less" scoped>
</style>
