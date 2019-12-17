<template>
  <el-container v-if="isShowMain" style="height: 100%;" id="assetsOfManufacturer">
    <el-aside style="width: 236px;border-right: 1px solid #8080804d;padding-right: 5px;">

      <el-form ref="agentSearchForm" :model="agentSearchForm" :inline="true" :size="elementSize" style="border-bottom: 1px solid #8080804d;margin-bottom: 5px">

        <el-form-item style="margin-bottom: 5px;margin-right: 0px;">
          <el-input placeholder="输入EPC名称" v-model="agentSearchForm.epcName" class="input-with-select">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-form-item>

      </el-form>

      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" accordion
               style="background: transparent;" :default-expanded-keys="['1', '2']" node-key="id"></el-tree>

    </el-aside>

    <el-container>
      <el-header style="height: auto">
        <el-form ref="inverterSearchForm" :model="inverterSearchForm" :inline="true"
                 style="height: 40px;float: left" :size="elementSize">
          <el-form-item>
            <el-input v-model="inverterSearchForm.esnNum" placeholder="输入设备名称或ESN号"></el-input>
          </el-form-item>

          <el-form-item>
            <el-select v-model="inverterSearchForm.inverterType" placeholder="逆变器类型" style="width: 130px">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-select v-model="inverterSearchForm.useVal" placeholder="用途" style="width: 120px">
              <el-option v-for="item in useOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :size="elementSize" icon="el-icon-search">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-form ref="btnsForm" :inline="true" label-width="50px" style="float:right;height: 40px" :size="elementSize">

          <el-form-item>
            <el-button type="primary" icon="el-icon-download">下载模板</el-button>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-upload2">批量导入</el-button>
          </el-form-item>

          <el-form-item>
            <el-button type="primary">导出</el-button>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="assetsInput">录入</el-button>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-delete" @click="deleteRow()">批量删除</el-button>
          </el-form-item>

        </el-form>

      </el-header>

      <el-main style="padding-top: 5px">
        <el-table :data="tableData" border :size="elementSize" @select="select" max-height="600" :fit="true">

          <el-table-column type="selection" width="40" align="center">
          </el-table-column>

          <el-table-column prop="num" label="编号" width="60" align="center">
            <template slot-scope="scope">
              <el-button @click="viewRow(scope.$index, tableData)" type="text"> {{scope.row.num}}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column prop="devVersion" label="设备型号" align="center">
          </el-table-column>

          <el-table-column prop="devType" label="设备类型" align="center">
          </el-table-column>

          <el-table-column prop="esnNum" label="ESN号" align="center">
          </el-table-column>

          <el-table-column prop="devStatus" label="设备状态" align="center">
          </el-table-column>

          <el-table-column prop="productionDate" label="生产日期" align="center" :width="160">
          </el-table-column>

          <el-table-column prop="epc_agent" label="EPC、代理商" align="center">
          </el-table-column>

          <el-table-column prop="deliveryDate" label="发货日期" align="center" :width="160">
          </el-table-column>

          <el-table-column prop="warrantyTime" label="保质期" align="center">
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button @click="editRow(scope.$index, tableData)" type="text">编辑</el-button>
              <el-button @click="deleteRow(scope.$index, tableData)" type="text" style="color: red">删除</el-button>
            </template>
          </el-table-column>

        </el-table>

        <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                       layout="total, sizes, prev, pager, next, jumper" :total="5" style="float: right; margin-top: 5px;">
        </el-pagination>

      </el-main>
    </el-container>

    <el-dialog :title="inputForm.title" :visible.sync="assetsInputDialogVisible">
      <el-form :model="inputForm" :rules="inputFormRules" ref="inputForm" :inline="true" :size="elementSize"
               label-position="left" label-width="100px" :disabled="inputFormDisabledFlag">

        <el-row>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="devVersion">
              <el-input v-model="inputForm.devVersion"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="devType">
              <el-select style="width: 180px" v-model="inputForm.devType" placeholder="请选择">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="生产日期">
              <el-date-picker style="width: 180px" v-model="inputForm.productionDate" type="date"
                              placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货日期">
              <el-date-picker style="width: 180px" v-model="inputForm.deliveryDate" type="date"
                              placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态" prop="devStatus">
              <el-select style="width: 180px" v-model="inputForm.devStatus" placeholder="请选择">
                <el-option v-for="item in useOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ESN号" prop="esnNum">
              <el-input v-model="inputForm.esnNum"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="EPC" prop="epc">
              <el-input v-model="inputForm.epc"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代理商" prop="agent">
              <el-input v-model="inputForm.agent"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="安装日期" prop="installationDate">
              <el-date-picker style="width: 180px" v-model="inputForm.installationDate" type="date"
                              placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="devModel">
              <el-input v-model="inputForm.devModel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="安装点" prop="installationSite">
              <el-input v-model="inputForm.installationSite"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额定输出功率" prop="ratedOutputPower">
              <el-input v-model="inputForm.ratedOutputPower"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="所属电站" prop="powerStation">
              <el-input v-model="inputForm.powerStation"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质保时间" prop="warrantyTime">
              <el-input v-model="inputForm.warrantyTime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="primary" :size="elementSize" v-if="inputForm.showInputBtn">录入</el-button>
        <el-button type="primary" :size="elementSize" v-if="inputForm.showResetBtn">重置</el-button>
        <el-button type="primary" :size="elementSize" v-if="inputForm.showSaveBtn">保存</el-button>
        <el-button type="primary" :size="elementSize" v-if="inputForm.showConfirmBtn"
                   @click="assetsInputDialogVisible = false">确定
        </el-button>
        <el-button :size="elementSize" @click="assetsInputDialogVisible = false" v-if="inputForm.showCancelBtn">取消
        </el-button>
      </div>
    </el-dialog>

  </el-container>
  <el-container v-else>
    <el-main style="padding: 0px">
      <el-row >
        <el-col :span="12" style="float: left">
          <i class="el-icon-view" v-if="inputForm.titleType == 1">设备详情</i>
          <i class="el-icon-edit" v-if="inputForm.titleType == 2">编辑设备</i>
          <i class="el-icon-plus" v-if="inputForm.titleType == 3">录入设备</i>
        </el-col>
        <el-col :span="12">
          <i class="el-icon-back" style="float: right;cursor: pointer;" @click="isShowMain = true">返回资产列表</i>
        </el-col>
      </el-row>
      <div style="width: 100%;border-bottom: 1px solid #e1e1e1;margin: 15px 0px 20px 0px"></div>
      <el-form :model="devDetailForm" ref="devDetailForm" :size="elementSize" style="width: 100%;margin-top: 10px" :rules="inputFormRules">

          <el-table :data="devDetailTableData" :size="elementSize" ref="devDetailTableData" border :show-header="false"
                    :highlight-current-row="false"
                    :cell-style="devDetailTableDataCellStyle">

            <el-table-column prop="kpiName1" label="kpi名称" align="center">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" :class="inputFormDisabledFlag ? '' : 'self-is-required'"><span>设备型号</span></div>
                <div v-else-if="scope.$index === 1"><span>生产日期</span></div>
                <div v-else-if="scope.$index === 2" :class="inputFormDisabledFlag ? '' : 'self-is-required'"><span>设备状态</span></div>
                <div v-else-if="scope.$index === 3" :class="inputFormDisabledFlag ? '' : 'self-is-required'"><span>EPC</span></div>
                <div v-else-if="scope.$index === 4"><span>安装日期</span></div>
                <div v-else-if="scope.$index === 5"><span>安装地点</span></div>
                <div v-else-if="scope.$index === 6"><span>所属电站</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiValue1" label="kpi值" align="left">
              <template slot-scope="scope">
                <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="devVersion">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.devVersion"></el-input>
                  <span v-else type="text">{{inputForm.devVersion}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 1">
                  <el-date-picker v-if="!inputFormDisabledFlag" style="width: 180px" v-model="inputForm.productionDate" type="date"
                                  placeholder="选择日期"></el-date-picker>
                  <span v-else type="text">{{inputForm.productionDate}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 2" prop="devStatus">
                  <el-select v-if="!inputFormDisabledFlag" style="width: 180px" v-model="inputForm.devStatus" placeholder="请选择">
                    <el-option v-for="item in useOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <span v-else type="text">{{inputForm.devStatus}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 3" prop="epc">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.epc"></el-input>
                  <span v-else type="text">{{inputForm.epc}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 4" prop="installationDate">
                  <el-date-picker v-if="!inputFormDisabledFlag" style="width: 180px" v-model="inputForm.installationDate" type="date"
                                  placeholder="选择日期"></el-date-picker>
                  <span v-else type="text">{{inputForm.installationDate}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 5" prop="installationSite">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.installationSite"></el-input>
                  <span v-else type="text">{{inputForm.installationSite}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 6" prop="powerStation">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.powerStation"></el-input>
                  <span v-else type="text">{{inputForm.powerStation}}</span>
                </el-form-item>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName2" label="kpi名称" align="center">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" :class="inputFormDisabledFlag ? '' : 'self-is-required'"><span>设备类型</span></div>
                <div v-else-if="scope.$index === 1"><span>发货日期</span></div>
                <div v-else-if="scope.$index === 2" :class="inputFormDisabledFlag ? '' : 'self-is-required'"><span>ESN号</span></div>
                <div v-else-if="scope.$index === 3"><span>代理商</span></div>
                <div v-else-if="scope.$index === 4"><span>设备型号</span></div>
                <div v-else-if="scope.$index === 5"><span>额定输出功率(kw)</span></div>
                <div v-else-if="scope.$index === 6"><span>质保时间</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiValue2" label="kpi值" align="left">
              <template slot-scope="scope">
                <el-form-item v-if="scope.$index === 0" prop="devType">
                  <el-select v-if="!inputFormDisabledFlag" style="width: 180px" v-model="inputForm.devType" placeholder="请选择">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <span v-else type="text">{{inputForm.devType}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 1">
                  <el-date-picker v-if="!inputFormDisabledFlag" style="width: 180px" v-model="inputForm.deliveryDate" type="date"
                                  placeholder="选择日期"></el-date-picker>
                  <span v-else type="text">{{inputForm.deliveryDate}}</span>
                </el-form-item>

                <el-form-item  v-else-if="scope.$index === 2" prop="esnNum">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.esnNum"></el-input>
                  <span v-else type="text">{{inputForm.esnNum}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 3" prop="agent">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.agent"></el-input>
                  <span v-else type="text">{{inputForm.agent}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 4" prop="devModel">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.devModel"></el-input>
                  <span v-else type="text">{{inputForm.devModel}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 5" prop="ratedOutputPower">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.ratedOutputPower"></el-input>
                  <span v-else type="text">{{inputForm.ratedOutputPower}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 6" prop="warrantyTime">
                  <el-input v-if="!inputFormDisabledFlag" v-model="inputForm.warrantyTime"></el-input>
                  <span v-else type="text">{{inputForm.warrantyTime}}</span>
                </el-form-item>
              </template>
            </el-table-column>

          </el-table>
        <el-row v-if="!inputFormDisabledFlag" style="margin: 5px 0px">
          <el-col :span="12">
            <div><span>带"<span style="color:red">*</span>"为必填项</span></div>
          </el-col>
        </el-row>

          <div class="dialog-footer" style="text-align: center;margin-top: 10px">
            <el-button type="primary" :size="elementSize" v-if="inputForm.showInputBtn">录入</el-button>
            <el-button type="primary" :size="elementSize" v-if="inputForm.showResetBtn">重置</el-button>
            <el-button type="primary" :size="elementSize" v-if="inputForm.showSaveBtn">保存</el-button>
            <el-button type="primary" :size="elementSize" v-if="inputForm.showConfirmBtn"
                       @click="isShowMain = true">确定
            </el-button>
            <el-button :size="elementSize" @click="isShowMain = true" v-if="inputForm.showCancelBtn">取消
            </el-button>
          </div>

        </el-form>
    </el-main>
  </el-container>
</template>

<style>
  #assetsOfManufacturer .el-dialog__header{
    background: rgb(0, 57, 122);
  }
  #assetsOfManufacturer .el-dialog__title{
    color: white;
  }

  .self-is-required:after{
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
  }
</style>
<script src='./index.js'></script>
