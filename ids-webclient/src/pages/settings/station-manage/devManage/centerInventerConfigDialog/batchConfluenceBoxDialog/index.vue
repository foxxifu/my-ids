<!-- 批量选择汇流箱设备 -->
<template>
  <el-dialog
    class="batchBoxConfigDialog"
    :title="$t('batchCon.batchSelectCon')"
    :visible.sync="isShowDialog"
    width="90%"
    :before-close="handleClose" append-to-body>
    <div class="search-bar">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('batchCon.devName')">
          <el-input v-model="searchData.devAlias" :placeholder="$t('batchCon.devName')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('batchCon.numSN')">
          <el-input v-model="searchData.snCode" :placeholder="$t('batchCon.numSN')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDevList(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-row :gutter="20">
        <el-col :span="16">
          <el-table
            :data="devList" :max-height="maxHeight"
            style="width: 100%" border>
            <el-table-column
              prop="devAlias"
              align="center"
              :label="$t('batchCon.devName')">
            </el-table-column>
            <el-table-column
              prop="devTypeName"
              align="center"
              :label="$t('batchCon.devType')">
              <template slot-scope="scope">
                {{ $t('batchCon.conBox') }}
              </template>
            </el-table-column>
            <el-table-column
              prop="signalVersion"
              align="center"
              :label="$t('batchCon.devVersion')">
            </el-table-column>
            <el-table-column
              prop="sn"
              align="center"
              :label="$t('batchCon.devSN')">
            </el-table-column>
            <el-table-column
              prop="all"
              :render-header="cellHeadRender"
              align="center"
              :label="$t('batchCon.allSelect')">
              <template slot-scope="{row, $index}">
                <el-button type="primary" icon="el-icon-d-arrow-right" @click="addOneDev(row)" :title="$t('batchCon.add')"></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="float: right;margin-top: 10px;"
            @size-change="pageSizeChange"
            @current-change="pageChange"
            :current-page="searchData.index"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="searchData.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-col>
        <el-col :span="8">
          <div style="border: 1px solid rgb(33, 177, 220);margin-bottom: 10px;">
            <div style="background: rgb(33, 177, 220); height: 100%;">
              <div style="display: inline-block;line-height: 40px;">
                {{ $t('batchCon.selectEqc') }}<label>{{selectList.length}}</label>
              </div>
              <el-button type="primary" style="float: right;margin-top: 3px;" @click="delAllDev">{{ $t('batchCon.allDel') }}</el-button>
            </div>
            <div style="overflow-y: auto;overflow-x: hidden;" :style="{height: maxHeight -20 + 'px', 'max-height': maxHeight - 20 + 'px',}">
              <ul>
                <li :title="$t('batchCon.dragToMove')" class="li-for-drag" v-for="(item, index) in selectList" :key="item.id" v-dragging="{ item: item, list: selectList, group: 'devId' }">
                  <el-row :gutter="10">
                    <el-col :span="20">
                      <el-row style="border: 1px solid #aaa;padding-left: 10px;margin-left: 10px;">
                        <el-col :span="16">
                          {{item.devName}}
                        </el-col>
                        <el-col :span="7">
                          {{item.devTypeName}}
                        </el-col>
                      </el-row>
                    </el-col>
                    <el-col :span="4">
                      <i class="el-icon-remove" :title="$t('batchCon.remove')" style="color: red;cursor: pointer;font-size: 24px;" @click="delOneDev(index, item)"></i>
                    </el-col>
                  </el-row>
                </li>
              </ul>
            </div>
          </div>
          <div style="text-align: center;">
            <el-button type="primary" @click="sureSelectData">{{ $t('batchCon.sureChoose') }}</el-button>
            <el-button type="primary" @click="handleClose">{{ $t('cancel') }}</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import deviceInfoService from '@/service/device'

  const props = {
    isShowDialog: { // 是否显示对话框
      type: Boolean,
      default: false
    },
    hlxDevs: { // 已经选择了的汇流箱
      type: [Object, Array],
      default: null
    },
    stationCode: { // 电站编号，查询可以绑定的直流汇流箱必须是这个电站下的
      type: String,
      default: null
    }
  }
  export default {
    props: props,
    components: {},
    created () {
      this.calcMaxHeight()
      if (this.hlxDevs && this.hlxDevs.length > 0) {
        this.selectList = this.hlxDevs.map(item => {
          return {
            id: item.id,
            devName: item.devAlias,
            devTypeName: this.$t('batchCon.conBox')
          }
        })
      }
    },
    data() {
      return {
        selectList: [], // 已经选择了的记录
        searchData: { // 查询信息
          index: 1,
          pageSize: 10
        },
        // 表格设备列表
        devList: [],
        maxHeight: 120,
        total: 0
      }
    },
    filters: {},
    mounted: function () {
      this.getDevList()
      this.$nextTick(function () {
      })
      window.onresize = this.calcMaxHeight
    },
    methods: {
      calcMaxHeight () { // 计算表格的高度
        let height = document.documentElement.clientHeight * 0.8 - 210
        if (height < 120) {
          height = 120
        }
        this.maxHeight = height
      },
      handleClose () { // 关闭对话框
        this.$emit('update:isShowDialog', false)
      },
      getDevList (isToFirst) { // 获取设备的信息
        if (isToFirst) {
          this.searchData.index = 1
        }
        let params = Object.assign({}, this.searchData)
        // 查询参数添加电站编号
        params.stationCode = this.stationCode
        let self = this
        deviceInfoService.getDCJSByPage(params).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          self.total = datas.count || 0
          let data = datas.list || []
          self.$set(self, 'devList', data)
        })
      },
      delOneDev (index, item) { // 删除一个设备
        this.selectList.splice(index, 1)
      },
      delAllDev () { // 移除所有选择的设备
        if (this.selectList.length === 0) {
          this.$message(this.$t('batchCon.noDevMove'))
          return
        }
        let self = this
        this.$confirm(this.$t('batchCon.sureMoveChooseDev'), this.$t('batchCon.tip'), {
          confirmButtonText: this.$t('sure'),
          // cancelButtonText: '取消',
          center: true
        }).then(() => { // 就直接复制了,也可以使用slice(0)
          self.$set(self, 'selectList', [])
        }).catch(() => {
          self.$message(this.$t('batchCon.cancelMove'))
        })
      },
      addOneDev (row) { // 添加一个设备
        // 1.判断是否已经存在
        let id = row.id
        if (this.selectList.filter(item => {
          return item.id === id
        }).length > 0) {
          this.$message(this.$t('batchCon.addDev'))
          return
        }
        // 2.如果不存在则添加
        this.selectList.push({
          id: id,
          devName: row.devAlias,
          devTypeName: this.$t('batchCon.conBox')
        })
        console.log(this.selectList)
      },
      addAllDev () { // 添加所有设备,如果有已经存在来的就不重复添加
        let len = this.devList.length
        if (len === 0) {
          this.$message(this.$t('batchCon.noDev'))
          return
        }
        let selectList = this.selectList
        let isAdd = false
        for (let i = 0; i < len; i++) {
          let tmpRow = this.devList[i]
          let tmpId = tmpRow.id
          if (selectList.filter(item => {
            return item.id === tmpId
          }).length === 0) { // 没有这个设备的情况
            isAdd = true
            selectList.push({
              id: tmpId,
              devName: tmpRow.devAlias,
              devTypeName: this.$t('batchCon.conBox')
            })
          }
        }
        if (!isAdd) {
          this.$message(this.$t('batchCon.noNeedAdd'))
        }
      },
      cellHeadRender (h, { column, _self }) {
        return h(
          'div',
          {
            'class': {
              'header-center': true,
              'dev-name-is-head': true,
              'text-ellipsis': true
            },
            domProps: {
              title: this.$t('batchCon.allSelect')
            },
          },
          [ // 子元素
            h('el-button', {
              props: {
                type: 'primary'
              },
              domProps: {
                innerHTML: this.$t('batchCon.allSelect')
              },
              on: {
                click: this.addAllDev
              }
            })
          ]
        );
      },
      pageChange (val) { // 页数改变的事件
        this.searchData.index = val
        this.getDevList()
      },
      pageSizeChange (val) { // 页数改变的事件
        this.searchData.pageSize = val
        this.getDevList(true)
      },
      sureSelectData () { // 确认选择设备之后
        let len = this.selectList.length
        if (len === 0) {
          this.$message(this.$t('batchCon.noChooseDev'))
          return
        }
        let self = this
        this.$confirm(this.$t('batchCon.sureChooseNum'), this.$t('batchCon.tip'), {
          confirmButtonText: this.$t('sure'),
        }).then(() => {
          self.$emit('on-select-finish', this.selectList.map(item => {
            return item.id
          }))
          self.handleClose()
        }).catch(() => {
        })
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
  .batchBoxConfigDialog{
    .li-for-drag{
      line-height: 30px;
      margin-top: 10px;
    }
  }
</style>
