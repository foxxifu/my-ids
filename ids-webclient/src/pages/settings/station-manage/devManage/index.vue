<template>
  <!--lazySearchUrl="/biz/domain/getDomainsByParentId"-->
  <TreeNav
           @node-click="treeNodeClick"
           :lazy="false"
           :data="treeData"
           :props="props"
           isFilter
           :titleText="$t('devMan.strPowStation')">
    <div slot-scope="selectNode" class="dev-manager-parent">
      <!-- 调用了树节点的组织结果右边的内容都通过这个方式来写入，考虑了左右结构的 -->
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchData" class="demo-form-inline">
          <el-form-item :label="$t('devMan.plantName')">
            <el-input v-model="searchData.stationName" :placeholder="$t('devMan.plantName')" clearable></el-input>
          </el-form-item>
          <el-form-item :label="$t('devMan.devType')">
            <el-select style="width: 300px" v-model="searchData.devTypeId" :placeholder="$t('devMan.chooseDevType')" clearable>
              <el-option v-for="item in devTypes" :key="item" :value="item" :label="$t('devTypeId.' + item)"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('devMan.devName')">
            <el-input v-model="searchData.devAlias" :placeholder="$t('devMan.devName')" clearable></el-input>
          </el-form-item>
          <el-form-item :label="$t('devMan.numSN')">
            <el-input v-model="searchData.snCode" :placeholder="$t('devMan.numSN')" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="queryInfoOfDevs(true)">{{ $t('search') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 按钮栏 -->
      <div class="btns" style="margin-bottom: 20px">
          <!--<el-button @click="" type="primary">设备模板下载</el-button>-->
          <!--<el-button @click="" type="primary">设备批量导入</el-button>-->
          <el-button @click="addDevClick" type="primary">{{ $t('devMan.devAcc') }}</el-button>
          <el-button @click="devCompClick" type="primary">{{ $t('devMan.devComparison') }}</el-button>
          <el-button @click="groupDetailConfigClilck" type="primary">{{ $t('devMan.groupDet') }}</el-button>
          <el-button type="primary" @click="openCenterInventerDialog">{{ $t('devMan.centInv') }}</el-button>
          <el-button @click="updateDevClick" type="primary">{{ $t('devMan.edit') }}</el-button>
          <el-button @click="delDevsClick" type="primary">{{ $t('devMan.del') }}</el-button>
          <el-button @click="exportMqttUser" type="primary">{{ $t('devMan.mqttExport') }}</el-button>
      </div>
      <!-- 数据列表栏 -->
      <div class="list-table">
        <el-table :data="list" width="100%" ref="multipleDevManageTable"
                  border :maxHeight="maxHeight"
                  @row-click="toggleSelection"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center">
          </el-table-column>
          <el-table-column :label="$t('devMan.plantName')" prop="stationName"align="center">
          </el-table-column>
          <el-table-column :label="$t('devMan.devName')" prop="devAlias"align="center">
          </el-table-column>
          <el-table-column :label="$t('devMan.devType')" prop="devTypeId"align="center">
            <template slot-scope="scope">
              {{ scope.row.devTypeId && $t('devTypeId.' + scope.row.devTypeId) || ''}}
            </template>
          </el-table-column>
          <el-table-column :label="$t('devMan.version')" prop="signalVersion"align="center">
          </el-table-column>
          <el-table-column :label="$t('devMan.numSN')" prop="snCode"align="center">
          </el-table-column>
          <el-table-column :label="$t('devMan.devStatus')" prop="devStatus"align="center">
            <template slot-scope="scope">
              <div style="height: 100%;position: relative;">
                <i :class="['dev-status', 'status-' + scope.row.devStatus ]">
                </i>
                {{ scope.row.devStatus | devStatusFilter(vm) }}
              </div>
            </template>
          </el-table-column>
          <!--<el-table-column label="功率(kW)" prop="power"align="center">
          </el-table-column>-->
          <el-table-column :label="$t('devMan.equSource')" prop="isMonitorDev"align="center">
            <template slot-scope="{row}">
              {{row.isMonitorDev | devSourceFilter(vm)}}
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页栏 -->
        <div style="clear: both;overflow: hidden;margin-top: 10px;">
          <div style="float: right">
            <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchData.page"
                           :page-sizes="[10, 20, 30, 50]" :page-size="searchData.pageSize"
                           layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
      <!-- 设备修改的弹出框 -->
      <dev-dialog v-if="devDialogData.isShow" :devId="devDialogData.devId" :isShowDialog.sync="devDialogData.isShow"></dev-dialog>
     <!-- 新增设备的弹出框 -->
      <add-dev-dialog v-if="isShowAddDevDialog" :stationName="selectTreeNode && selectTreeNode.name" :stationCode="selectTreeNode && (selectTreeNode.id + '')" :isShowDialog.sync="isShowAddDevDialog" @on-insert-success="addDevSuccess"></add-dev-dialog>
      <group-details-dialog v-if="isShowGroupDialog" :isShowDialog.sync="isShowGroupDialog" :devIds="confGroupDevIds"></group-details-dialog>
      <dev-comparision-dialog v-if="isShowDevComparision" :isShowDialog.sync="isShowDevComparision"></dev-comparision-dialog>
      <!-- 集中式逆变器的弹出框 -->
      <center-inventer-dialog v-if="isShowInventerDialog" :isShowDialog.sync="isShowInventerDialog" :devId="confGroupDevIds" :stationCode="stationCode"></center-inventer-dialog>
    </div>
  </TreeNav>
</template>

<script type="text/ecmascript-6">
  import TreeNav from '@/components/treeNav/index.vue'
  import deviceService from '@/service/device'
  import devManageService from '@/service/devManager'
  import districtService from '@/service/district'
  import DevDialog from './devDialog.vue'
  import AddDevDialog from './devAdd/addDevDialog.vue'
  import GroupDetailsDialog from './groupDetailsDialog.vue'
  import DevComparisionDialog from './devComparison/index.vue'
  import CenterInventerDialog from './centerInventerConfigDialog/index.vue'
  import DevService from '@/service/dev'

  export default {
    components: {
      TreeNav,
      DevDialog,
      AddDevDialog,
      GroupDetailsDialog,
      DevComparisionDialog,
      CenterInventerDialog
    },
    created () {
      this.calcMaxHeight()
      // 查询树节点的数据
      this.queryTreeData()
    },
    data () {
      return {
        vm: this,
        maxHeight: 400,
        selectTreeNode: null,
        searchData: {
          page: 1,
          pageSize: 10
        },
        list: [], // 当前页的数据
        total: 0,
        devTypes: [], // 设备类型
        multipleSelection: [], // 当前表格选择的数据
        devDialogData: {
          isShow: false,
          devId: null
        },
        isShowAddDevDialog: false, // 是否显示新增设备的对话框
        isShowGroupDialog: false, // 是否显示组件配置的弹出框
        isShowDevComparision: false, // 是否显示设备对比的弹出框
        isShowInventerDialog: false, // 显示集中式逆变器的配置弹出框
        confGroupDevIds: null, // 配置组串的设备id的字符串，id之间使用逗号隔开
        treeData: [], // 列表数据
        props: {
          label: 'name',
          children: 'children', // 树节点中的下一级使用的字段名称
          isLeaf: 'isStation' // 叶子节点取数据的属性名称
        },
        stationCode: null, // 批量绑定的需要传递给查询当前电站下的直流汇流箱的电站编号
      }
    },
    filters: {
      devStatusFilter (val, vm) {
        if (!val) {
          return '-'
        }
        if (val === 1) {
          return vm.$t('devMan.fault')
        }
        if (val === 2) {
          return vm.$t('devMan.disconnect')
        }
        if (val === 3) {
          return vm.$t('devMan.normal')
        }
        return vm.$t('devMan.unKnow')
      },
      devSourceFilter (val, vm) { // 数据来源
        if (val === '1') {
          return vm.$t('devMan.monitor')
        }
        return vm.$t('devMan.setDem')
      }
    },
    mounted: function () {
      this.queryDevTypes() // 查询设备类型
      this.$nextTick(function () {
      })
      window.onresize = this.calcMaxHeight
    },
    methods: {
      treeNodeClick (data, node) { // 树节点的调用方式,点击树节点的回调函数
        this.selectTreeNode = data
        // 去查询数据
        this.searchData = {
          page: 1,
          pageSize: this.searchData.pageSize,
          areaCode: data.code // TODO 确定传递给后台的areacode是什么
        }
        if (data.isStation) { // 如果是点击的电站，需要查询当前电站的编号为点击的电站编号的信息
          this.searchData.stationCode = data.id
        }
        this.queryInfoOfDevs(true)
      },
      clearSelectData () { // 清空选中的数据
        this.$refs.multipleDevManageTable.clearSelection(); // 清空选中的数据
      },
      queryTreeData () { // 获取树节点的信息
        let self = this
        districtService.getAllDistrict({}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          getDevTreeData(datas)
          self.treeData = datas
        })
      },
      queryInfoOfDevs (isToFirst) { // 查询设备信息
        if (!this.selectTreeNode) {
          this.$message(this.$t('devMan.selectOrg'))
          return
        }
        if (isToFirst) {
          this.searchData.page = 1
        }
        this.clearSelectData() // 清空选中的数据
        // TODO 查询设备信息
        let self = this
        this.searchData.index = this.searchData.page
        deviceService.getDeviceByCondition(this.searchData).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          self.list = datas.list || []
          self.total = datas.count || 0
        })
      },
      queryDevTypes () {
        // 需要通过ajax请求查询
        let self = this
        devManageService.getAllDevType({}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || []
          // let arr = []
          // for (let key in datas) {
          //   if (datas.hasOwnProperty(key)) {
          //     arr.push({
          //       devTypeId: datas[key],
          //       name: key
          //     })
          //   }
          // }
          // arr.sort(function(a, b) {
          //   return a.devTypeId - b.devTypeId
          // })
          self.$set(this, 'devTypes', datas)
        })
      },
      toggleSelection (row, event, column) { // 点击行的事件
        this.$refs.multipleDevManageTable.toggleRowSelection(row);
      },
      handleSelectionChange(val) { // 选择的数据改变的事件
        this.multipleSelection = val;
      },
      pageSizeChange (val) { // 每页显示记录数改变后的事件
        this.searchData.pageSize = val
        this.queryInfoOfDevs(true)
      },
      pageChange (val) { // 页数改变后的事件
        this.searchData.page = val
        this.queryInfoOfDevs()
      },
      calcMaxHeight () { // 计算最大高度
        let height = document.documentElement.clientHeight - 75 - 44 - 100 - 100
        this.maxHeight = height < 120 ? 120 : height
      },
      updateDevClick () { // 点击修改按钮的事件
        let len = (this.multipleSelection && this.multipleSelection.length) || 0
        if (len === 0 || len > 1) {
          this.$message(len === 0 ? this.$t('devMan.selectEqu') : this.$t('devMan.onlyDeviceMod') + len)
          return
        }
        this.addBaseData = {}
        let devInfo = this.multipleSelection[0]
        this.devDialogData.devId = devInfo.id
        this.devDialogData.isShow = true
      },
      addDevClick () { // 点击设备接入的按钮，目前只支持上能协议类型的设备新增
        if (!this.selectTreeNode || !this.selectTreeNode.isStation) { // 只有是电站的
          this.$message(this.$t('devMan.selectPow'))
          return
        }
        this.isShowAddDevDialog = true
      },
      delDevsClick () { // 删除设备
        let len = (this.multipleSelection && this.multipleSelection.length) || 0
        if (len === 0) {
          this.$message(this.$t('devMan.chooseEquDel'))
          return
        }
        let ids = this.multipleSelection.map(item => {
          return item.id
        })
        let self = this
        this.$confirm(this.$t('devMan.sureDelSleEqu'), this.$t('devMan.tip'), {
          confirmButtonText: this.$t('devMan.sure'),
          // cancelButtonText: '取消',
          // center: true
        }).then(() => {
          DevService.removeDev(ids).then(resp => {
            if (resp.code === 1) {
              self.$message(this.$t('devMan.delSuc'))
              self.queryInfoOfDevs()
              let devIds = []
              for (let tmRow in self.multipleSelection) {
                if (tmRow.devTypeId === 37) { // 如果是铁牛数采的删除
                  devIds.push(tmRow.id)
                }
              }
              // 删除设备缓存
              let modbusSns = resp.results;
              if (modbusSns.length > 0){
                deviceService.delModbusSignalLocalCatchBySns({snList: modbusSns}).then(resp => {
                  console.log("删除缓存成功")
                })
              }

              if (devIds.length > 0) { // 如果是铁牛数采的通知连接状态的更新和断连告警的清除
                // type 1：新增 2：删除 3：修改
                // delType 1: 传递的ids代表设备  其他的代表是版本信息
                deviceService.notifyTnDevUpdateChange({ids: devIds.join(','), type: 2, delType: 1}).then(resp => {
                  console.log('send change success')
                })
              }
            } else {
              self.$message(this.$t('devMan.delFail'))
            }
          })
        }).catch(() => {
          self.$message(this.$t('devMan.cancelDel'))
        })
      },
      groupDetailConfigClilck () { // 组串详情配置的页面
        // TODO 判断是否是选择的是同一种设备类型的设备在做配置
        let selectDatas = this.multipleSelection
        let len = selectDatas.length
        if (len === 0) {
          this.$message(this.$t('devMan.selectSerOrShu'))
          return
        }
        let errorNames = ''
        let ids = []
        for (let i = 0; i < len; i++) {
          let tmp = selectDatas[i]
          if (tmp.devTypeId !== 1 && tmp.devTypeId !== 15) {
            errorNames += tmp.devName + ','
          }
          ids.push(tmp.id)
        }
        if (errorNames) {
          this.$message(this.$t('devMan.notMeetPre') + errorNames.substring(0, errorNames.length - 1))
          return
        }
        this.confGroupDevIds = ids.join(',')
        this.isShowGroupDialog = true
      },
      devCompClick () { // 设备对比的点击事件
        this.isShowDevComparision = true
      },
      openCenterInventerDialog () { // 弹出集中式逆变器的弹出框
        let len = this.multipleSelection.length
        if (len === 0 || len > 1) {
          this.$message(len === 0 ? this.$t('devMan.selectCentMod') : this.$t('devMan.onlyOneConfig') + len)
          return
        }
        if (this.multipleSelection[0].devTypeId !== 14) {
          this.$message(this.$t('devMan.onlyOneConfig'))
          return
        }
        this.confGroupDevIds = this.multipleSelection[0].id
        this.stationCode = this.multipleSelection[0].stationCode
        this.isShowInventerDialog = true
      },
      addDevSuccess() { // 新增设备成功之后的回调函数
        this.queryInfoOfDevs() // 刷新界面
      },
      exportMqttUser() { // 导出mqtt的用户信息
        let iframe = document.getElementById("myIframe_for_download_id");
        let lang = localStorage.getItem('lang') || 'zh'
        let url = '/biz/signal/exportMqqtUser?time=' + Date.parse() + '&lang=' + lang
        if (iframe) {
          iframe.src = url;
        } else {
          iframe = document.createElement("iframe");
          iframe.style.display = "none";
          iframe.src = url;
          iframe.id = "myIframe_for_download_id";
          document.body.appendChild(iframe);
        }
      }
    },
    watch: {},
    computed: {}
  }
  // 获取行政区域的树节点
  const getDevTreeData = (datas, parentCode = '0') => {
    let len
    if (!datas || (len = datas.length) === 0) {
      return
    }
    for (let i = 0; i < len; i++) {
      let tmp = datas[i]
      if (tmp.isStation) { // 电站就不用去在做处理了
        continue
      }
      let code = tmp.id + ''
      if (parentCode) { // 修改code的值 上级与下级之间使用@分割，因为后台需要的是parentcode@xxxxxxx@xxx.....
        tmp.code = code = parentCode + '@' + code
      }
      let stations = tmp.stations
      let sLen
      let sChildren = [] // 行政区域的电站区域
      if (stations && (sLen = stations.length) > 0) {
        for (let j = 0; j < sLen; j++) { // 将电站设置为区域的子节点
          let sTmp = stations[j]
          let one = {
            id: sTmp.stationCode, // 电站编号
            name: sTmp.stationName,
            sId: sTmp.id,
            code: code,
            isStation: true // 是否是电站
          }
          sChildren.push(one)
        }
      }
      let children = tmp.children
      if (children) { // 不考虑有children的有电站，即只有最低一级的行政区域才有电站
        sChildren.length > 0 && children.push(...sChildren) // 有children的也可能有电站
        getDevTreeData(children, code) // 递归调用,判断叶子节将最低一层的电站放出来
      } else {
        sChildren.length > 0 && (tmp.children = sChildren) // 电站子节点
      }
    }
  }
</script>

<style lang="less" scoped>
  .dev-manager-parent {
    i.dev-status {
      display: inline-block;
      position: relative;
      width: 20px;
      height: 20px;
      background: #aaa;
      border-radius: 50%;
      top: 4px;
      /*&.status-DISCONNECTED{*/
        /*background: #f00;*/
      /*}*/
      &.status-3{
        background: #0f0;
      }
      &.status-1{
        background: #f00;
      }
    }
  }
</style>

