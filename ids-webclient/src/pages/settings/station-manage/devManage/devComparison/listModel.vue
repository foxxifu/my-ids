<!-- 列表模式 -->
<template>
  <div class="list-model-parent">
    <div style="margin-bottom: 20px;">
      <el-checkbox v-model="isHiddenSame">{{ $t('devCom.hideSameItem') }}</el-checkbox>
      <el-checkbox v-model="isShowBest">{{ $t('devCom.labelBestItem') }}</el-checkbox>
      <span v-show="isLoadingData" class="my-load-style"><i class="el-icon-loading"></i>{{ $t('devCom.dataLoading') }}</span>
      <div style="float: right;">
        {{ $t('devCom.selectComTime') }}
        <el-date-picker
          v-model="comparateTime"
          type="datetime"
          :format="$t('dateFormat.yyyymmddhhmmss')"
          value-format="timestamp" :editable="false" @change="myChangeVal">
        </el-date-picker>
      </div>
    </div>
    <div class="list-table" style="min-height: 250px;">
      <el-table
        :data="outerList"
        :span-method="mergMethod"
        border
        :maxHeight="maxHeight"
        :row-class-name="setRowClass"
        @header-click="headRowClick"
        style="width: 100%">
        <el-table-column
          prop="col1"
          :label="outerListHeader[0]" align="center">
          <template slot-scope="props">
            <div v-if="props.row.type === 'staticData'" class="my-for-padding row-header" @click="rowStaticClick">
              <i :class="[expendStaticClass]"></i>{{props.row.col1}}
            </div>
            <div v-else-if="props.row.type === 'runData'" class="my-for-padding row-header" @click="rowRunClick">
              <i :class="[expendRunClass]"></i>{{props.row.col1}}
            </div>
            <div class="my-for-padding" v-else style="width: 220px;">
              {{props.row.col1}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col2"
          :label="outerListHeader[1]" align="center" :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 1}">
              {{scope.row.col2}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col3"
          :label="outerListHeader[2]" align="center"  :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 2}">
              {{scope.row.col3}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col4"
          :label="outerListHeader[3]" align="center"  :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 3}">
              {{scope.row.col4}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col5"
          :label="outerListHeader[4]" align="center" :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 4}">
              {{scope.row.col5}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col6"
          :label="outerListHeader[5]" align="center"  :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 5}">
              {{scope.row.col6}}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="col7"
          :label="outerListHeader[6]" align="center"  :render-header="cellHeadRender">
          <template slot-scope="scope">
            <div :class="{'my-for-padding': true, 'is-best-cell': isShowBest && scope.row.bestIndex === 6}">
              {{scope.row.col7}}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <choose-device-dialog v-if="isShowDev" :isShowDev.sync="isShowDev" :isMutiSelect="false" :isAutoSelect="false"
        @on-sure-callback="selectDevice"
        @on-cancel-callback="noShowLoading"></choose-device-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import ChooseDeviceDialog from '@/components/chooseDevice/chooseDeviceDialog.vue'
  import devManagerService from '@/service/devManager'

  let chooseText // 显示没有设备的时候占据表头的名称

  const getTimeOfFive = (time) => {
    let date
    if (time) {
      date = new Date(time)
    } else {
      date = new Date()
    }
    let min = Math.floor(date.getMinutes() / 5) * 5 // 离他最近的5分钟的时间
    date.setMinutes(min, 0, 0)
    return date.getTime()
  }
  export default {
    components: {
      ChooseDeviceDialog
    },
    created () {
      chooseText = this.$t('devCom.chooseDev2')
      this.initOutterTableDatas()
    },
    data () {
      return {
        maxHeight: 600,
        // 对比时间
        comparateTime: getTimeOfFive(),
        oldTime: null, // 用于查询当前查询的时间点的时间
        isHiddenSame: false, // 是否隐藏相同项
        isShowBest: false, // 是否显示最优项
        outerList: [], // 最外边的列表数据
        outerListHeader: [], // 外表表格的表头数据
        staticList: [], // 静态数据的表格信息
        maxGroupNum: 6, // 最大的组串串数
        isShowStaticRow: true, // 是否显示静态的数据
        isShowRunRow: true, // 是否显示静态的数据
        isShowDev: false,
        selectDataList: [], // 查询到的数据
        // 设备id到设备数据的一个映射 <设备id, 设备数据> 需要确定一个顺序,唯一一个是确定设备顺序的是devIdArr: [设备id的数组]
        devDataMap: {
          devIdArr: [], // 设备id的数组
        },
        // 第一列静态数据的数据
        staticFirstCell: this.$t('devCom.staticNameList'),
        // 第一列动态数据的所有的数据
        runFirstCell: this.$t('devCom.runDataNameList'),
        // 组件类型的国际化
        componentTypeArr: this.$t('devCom.componentType'),
        isLoadingData: false, // 标志是否在查询数据
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      initOutterTableDatas () { // 获取初始的表格数据
        let list = []
        // 静态数据的数据
        list.push({
          col1: this.$t('devCom.staticInf'),
          type: 'staticData',
        })
        let colFistText = this.staticFirstCell
        let len = this.maxGroupNum + 6
        for (let i = 0; i < len; i++) {
          list.push({
            col1: colFistText[i],
            col2: '',
            col3: '',
            col4: '',
            col5: '',
            col6: '',
            col7: '',
            myRowClass: 'static-hidden-class'
          })
        }
        // 运行的数据
        list.push({
          col1: this.$t('devCom.runNum'),
          type: 'runData'
        })
        let colFistRunArr = this.runFirstCell
        len = this.maxGroupNum + 2
        let oneRunData // 一条运行数据
        for (let i = 0; i < len; i++) {
          oneRunData = {
            col1: colFistRunArr[i],
            col2: '',
            col3: '',
            col4: '',
            col5: '',
            col6: '',
            col7: '',
            myRowClass: 'run-hidden-class'
          }
          list.push(oneRunData)
        }
        this.outerListHeader = [this.$t('devCom.parameterConfig'), chooseText, chooseText, chooseText, chooseText, chooseText, chooseText]
        this.outerList = list
      },
      getOuterTableList () { // 获取表格所有行的数据
        let devIdArr = this.devDataMap.devIdArr
        let devLen = devIdArr.length
        // 1.判断是否有设备
        if (devLen === 0) { // 如果没有设备
          this.initOutterTableDatas()
          return
        }
        let devDatas = this.devDataMap
        // 有设备了
        let list = []
        let zcNum = this.maxGroupNum // 组串的最大串数
        // 1. 组装静态数据
        list.push({
          col1: '静态信息',
          type: 'staticData',
        })
        // 分别获取的名称,分别为厂家名称-组串容量的对应查询的数据的内容
        let listName = ['vender_name', 'manufacturer', 'module_production_date', 'module_type', 'module_ratio', // 标称组件转换效率(%)
          'max_power_temp_coef'// 峰值功率温度系数(%)
        ];
        for (var i = 1; i <= zcNum; i++) {
          listName.push('pv' + i + '_capacity');
        }
        let staticNum = zcNum + 6; // 静态数据的行数
        let staticFirstCell = this.staticFirstCell
        let oneData
        for (let i1 = 0; i1 < staticNum; i1++) {
          oneData = {
            col1: staticFirstCell[i1],
            // 至少有一个设备
            col2: this.getOneStaticDevData(0, devLen, devDatas[devIdArr[0]], listName[i1]),
            col3: this.getOneStaticDevData(1, devLen, devDatas[devIdArr[1]], listName[i1]),
            col4: this.getOneStaticDevData(2, devLen, devDatas[devIdArr[2]], listName[i1]),
            col5: this.getOneStaticDevData(3, devLen, devDatas[devIdArr[3]], listName[i1]),
            col6: this.getOneStaticDevData(4, devLen, devDatas[devIdArr[4]], listName[i1]),
            col7: this.getOneStaticDevData(5, devLen, devDatas[devIdArr[5]], listName[i1]),
            myRowClass: 'static-hidden-class'
          }
          // 判断是否是相同的
          let flag = true
          if (devLen > 1) {
            let tmpFirst = oneData.col2
            for (let j = 1; j < devLen; j++) {
              if (tmpFirst !== oneData['col' + (2 + j)]) {
                flag = false
              }
            }
          }
          oneData.isSame = flag
          list.push(oneData)
        }
        // 2.组装动态数据
        list.push({
          col1: this.$t('devCom.runNum'),
          type: 'runData'
        })
        listName = ['day_capacity', 'active_power'] // 发电量,输入功率
        for (let i3 = 1; i3 <= zcNum; i3++) {
          listName.push(i3);
        }
        let runDataNum = 2 + zcNum
        let runFirstCell = this.runFirstCell
        for (let i4 = 0; i4 < runDataNum; i4++) {
          oneData = {
            col1: runFirstCell[i4],
            // 至少有一个设备
            col2: this.getOneRunDevData(0, devLen, devDatas[devIdArr[0]], listName[i4]),
            col3: this.getOneRunDevData(1, devLen, devDatas[devIdArr[1]], listName[i4]),
            col4: this.getOneRunDevData(2, devLen, devDatas[devIdArr[2]], listName[i4]),
            col5: this.getOneRunDevData(3, devLen, devDatas[devIdArr[3]], listName[i4]),
            col6: this.getOneRunDevData(4, devLen, devDatas[devIdArr[4]], listName[i4]),
            col7: this.getOneRunDevData(5, devLen, devDatas[devIdArr[5]], listName[i4]),
            myRowClass: 'run-hidden-class'
          }
          // 判断是否相同，和是否是最优的
          let flag = true // 是否相同的
          let bestIndex = 0 // 最优的下标
          let tmpMax = 0
          for (let j = 0; j < devLen; j++) {
            let tmpData = oneData['col' + (2 + j)]
            if (j > 0) { // 判断是否相同
              if (oneData.col2 !== tmpData) {
                flag = false
              }
            }
            // 判断最优的
            if (tmpData && (tmpData + '').indexOf('/') !== -1) { // 说明是
              let tempArr = tmpData.split('/')
              if (tempArr[0] !== '-' && tempArr[1] !== '-') {
                let tmpMi = +tempArr[0] * +tempArr[1]
                if (tmpMi > tmpMax) {
                  tmpMax = tmpMi
                  bestIndex = j + 1
                }
              }
            } else { // 发电量或者功率
              if (tmpData !== '-' && +tmpData > tmpMax) {
                tmpMax = +tmpData
                bestIndex = j + 1
              }
            }
          }
          oneData.isSame = flag // 是否是相同项
          tmpMax && (oneData.bestIndex = bestIndex)
          list.push(oneData)
        }
        // 最后对表格数据赋值
        this.outerList = list
      },
      getOneStaticDevData (index, devLen, data, name) { // 获取一个cell中的静态数据的内容
        if (index < devLen) {
          if (name === 'module_production_date' && data && data[name]) { // 日期格式
            return Date.parse(data[name]).format(this.$t('dateFormat.yyyymmddhhmmss'));
          } else if (name === 'module_type' && data && data[name] && !isNaN(data[name])) { // 组件类型,需要确定一定是数字
            return this.componentTypeArr[(data[name] - 1)] || '-';
          } else if (name !== 'module_production_date' && name !== 'module_type') { // 非上面条件的数据
            return (data && data[name]) || '-'
          } else {
            return '-';
          }
        } else {
          return '';
        }
      },
      getOneRunDevData (index, devLen, data, name) { // 获取一个cell中的动态数据内容
        if (index < devLen) {
          if ((name === 'day_capacity' || name === 'active_power') && data && data[name]) { // 发电量或者输出功率
            return data[name];
          } else if (!(name === 'day_capacity' || name === 'active_power') && data) {
            var str = (data['pv' + name + '_u'] && data['pv' + name + '_u'].fixed(2)) || '-'
            str += '/' + ((data['pv' + name + '_i'] && data['pv' + name + '_i'].fixed(2)) || '-')
            return str;
          } else {
            return '-';
          }
        } else {
          return '';
        }
      },
      mergMethod ({ row, column, rowIndex, columnIndex }) { // 合并表格的方法
        if (row.type){
          if (columnIndex === 0) {
            return [1, 7];
          }
        }
        return [1, 1]
      },
      setRowClass ({row, rowIndex}) { // 设置行的class
        let rowClass = row.myRowClass
        if (!rowClass) { // 两个静态的行，负责展开和折叠的行
          return ''
        }
        if (rowClass === 'static-hidden-class') {
          return this.getMyClass(this.isShowStaticRow, rowClass, row.isSame)
        } else {
          return this.getMyClass(this.isShowRunRow, rowClass, row.isSame)
        }
      },
      getMyClass(flag, rowClass, isSame) { // 获取样式
        let tmpClass = !flag ? rowClass : ''
        if (!tmpClass) {
          tmpClass = this.isHiddenSame && isSame ? 'is-same-tr' : ''
        } else {
          tmpClass += this.isHiddenSame && isSame ? ' is-same-tr' : ''
        }
        return tmpClass
      },
      rowStaticClick () { // 展开隐藏静态的信息
        this.isShowStaticRow = !this.isShowStaticRow
      },
      rowRunClick () { // 展开隐藏运行数据的信息
        this.isShowRunRow = !this.isShowRunRow
      },
      selectDevice(dev) { // 选择设备后的回调函数
        if (dev.devTypeId !== 1) {
          this.$message(this.$t('devCom.selectGroupInv'))
          this.noShowLoading()
          return
        }
        let devIdStr = dev.id + ''
        if (this.devDataMap.devIdArr.indexOf(devIdStr) !== -1) {
          this.$message(this.$t('devCom.devHave'))
          this.noShowLoading()
          return
        }
        this.devDataMap.devIdArr.push(devIdStr) // 数组中添加了一个设备
        this.$set(this.outerListHeader, this.devDataMap.devIdArr.length, dev.devAlias)// 修改表头的名字
        this.queryDevDatas(devIdStr)
      },
      queryDevDatas (devIdStr) { // 查询静态数据和运行数据
        this.oldTime = this.comparateTime
        let self = this
        // 去查询设备数据
        devManagerService.deviceComparisonTable({devIds: devIdStr, queryTime: self.comparateTime}).then(resp => {
          let datas = (resp.code === 1 && resp.results && resp.results) || []
          let len = datas.length
          for (let i = 0; i < len; i++) {
            let data = datas[i]
            self.$set(self.devDataMap, data.dev_id + '', data)
            let num = data.num || 0
            if (self.devDataMap.devIdArr === 1 && num > 0) {
              this.maxGroupNum = num
            } else if (num > self.maxGroupNum) {
              this.maxGroupNum = num
            }
          }
          // 重新设置table的数据
          self.getOuterTableList()
          self.noShowLoading()
        }).catch(e => {
          self.getOuterTableList()
          self.noShowLoading()
        })
      },
      noShowLoading () { // 不显示加载数据的框
        this.isLoadingData = false
      },
      headRowClick (column, event) { // 点击表头的事件
        // 已经在查询的过程中就不能再点击了
        if (!this.isLoadingData && column.label === chooseText && (column.property === 'col' + (this.devDataMap.devIdArr.length + 2))) { // 点击的是选择设备的lable的时候就需要弹出对话框
          this.isShowDev = true
          this.isLoadingData = true
        }
      },
      myChangeVal (val) { // 选择时间值改变的内容
        let time = getTimeOfFive(val)
        this.$set(this, 'comparateTime', time)
        // 如果改变后的时间与查询时候的时间不一致就需要重新查询
        if (this.devDataMap.devIdArr.length > 0 && this.oldTime !== time) {
          this.isLoadingData = true
          let self = this
          setTimeout(function () {
            self.queryDevDatas(self.devDataMap.devIdArr.join(','))
          }, 5)
        }
      },
      cellHeadRender(h, { column, _self }) { // 表格头部的渲染
        let label = column.label;
        if (label !== chooseText) {
          return h(
            'div',
            {
              'class': {
                'header-center': true,
                'dev-name-is-head': true,
                'text-ellipsis': true
              },
              domProps: {
                title: label
              },
            },
            [ // 子元素
              label,
              h('i', {
                'class': {
                  'el-icon-error': true,
                  'my-close-info': true
                },
                attrs: {
                  'data-devname': label,
                  'data-col': column.property
                },
                domProps: {
                  title: this.$t('devCom.remove')
                },
                on: {
                  click: this.deleteDevCell
                },
              })
            ]
          );
        } else {
          return h(
            'div',
            {
              'class': {
                'header-center': true,
                'text-ellipsis': true
              },
              domProps: {
                innerHTML: label
              },
            }
          );
        }
      },
      deleteDevCell (column) { // 点击头部的删除的元素
        if (!this.isLoadingData) {
          this.isLoadingData = true
          let property = column.target.dataset.col
          let index = +property.replace('col', '')
          // 数组中的数据 当前的设备减一
          let devIdArr = this.devDataMap.devIdArr
          devIdArr.splice(index - 2, 1) // 删除是根据位置来删除的
          // 表头的数据
          let hearder = this.outerListHeader
          hearder.splice(index - 1, 1)
          hearder.push(chooseText) // 删除了一个还添加一个
          if (devIdArr.length > 0) {
            this.queryDevDatas(devIdArr.join(','))
          } else {
            this.initOutterTableDatas()
            this.noShowLoading()
          }
        }
      }
    },
    watch: {
    },
    computed: {
      expendStaticClass () {
        return (this.isShowStaticRow && 'el-icon-remove-outline') || 'el-icon-circle-plus-outline'
      },
      expendRunClass () {
        return (this.isShowRunRow && 'el-icon-remove-outline') || 'el-icon-circle-plus-outline'
      },
    }
  }
</script>

<style lang="less" scoped>
  .list-model-parent{
    div.row-header{
      font-size: 16px;
      font-weight: bold;
      float: left;
      margin-left: 20px;
    }
    .my-load-style{
      color: #00ffff;
      display: inline-block;
      margin-left: 30%;
    }
    /deep/ tr.static-hidden-class, /deep/ tr.run-hidden-class, /deep/ tr.is-same-tr {
      display: none;
    }
    /deep/ .el-table th, /deep/ .el-table td {
      padding: 0px 0;
    }
    /deep/ .el-table .cell {
      padding: 0;
      .my-for-padding{
        padding: 2px 10px;
        &.is-best-cell {
          background: #00CC33;
        }
      }
    }
   /deep/ div.dev-name-is-head {
      width: 100%;
      height: 100%;
      position: relative;
      /deep/ i.my-close-info{
        font-size: 14px;
        position: absolute;
        top: 2px;
        right: 5px;
        color: red;
        &:hover{
          color: #500;
          cursor: pointer;
        }
      }
    }
  }
</style>

