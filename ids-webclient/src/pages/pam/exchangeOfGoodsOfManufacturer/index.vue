<template>

  <el-container v-if="isShowMain" style="height: 100%;" id="exchangeOfGoodsOfManufacturer">
    <el-aside style="width: 236px;border-right: 1px solid #8080804d;padding-right: 5px;">

      <el-form ref="exchangeOfGoodsForm" :model="exchangeOfGoodsForm" :inline="true" :size="elementSize"
               style="border-bottom: 1px solid #8080804d;margin-bottom: 5px">

        <!--<el-form-item style="margin-bottom: 5px;margin-right: 0px;">
          <el-input placeholder="输入EPC名称" v-model="exchangeOfGoodsForm.epcName" type="primary">
          </el-input>

        </el-form-item>

        <el-form-item style="margin-bottom: 5px;margin-right: 0px;">
          <el-button icon="el-icon-search" type="primary"></el-button>
        </el-form-item>-->

        <el-form-item style="margin-bottom: 5px;margin-right: 0px;">
          <el-input placeholder="输入EPC名称" v-model="exchangeOfGoodsForm.epcName" class="input-with-select">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-form-item>

      </el-form>

      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"
               style="background: transparent;" :default-expanded-keys="['1', '2']" node-key="id"></el-tree>

    </el-aside>

    <el-container>
      <el-header style="height: auto">
        <el-form ref="inverterSearchForm" :model="inverterSearchForm" :inline="true" label-width="50px"
                 style="height: 40px;float: left" :size="elementSize">
          <el-form-item>
            <el-input v-model="inverterSearchForm.esnNum" placeholder="输入设备名称或ESN号" width="100px"
            ></el-input>
          </el-form-item>

          <el-form-item>
            <el-select v-model="inverterSearchForm.inverterType" placeholder="逆变器类型" style="width: 130px">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :size="elementSize" icon="el-icon-search">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-form ref="btnsForm" :inline="true" label-width="50px" style="float:right;height: 40px" :size="elementSize">

          <el-form-item>
            <el-button type="primary">导出</el-button>
          </el-form-item>

        </el-form>

      </el-header>

      <el-main style="padding-top: 5px">
        <el-tabs v-model="returnState" type="border-card" style="height: 500px">
          <el-tab-pane label="申请中" name="applying">

            <el-table :data="applyingTableData" :size="elementSize" ref="applyingTable" border>

              <el-table-column prop="num" label="编号" width="60" align="center">
              </el-table-column>

              <el-table-column prop="applicationNum" label="申请编号" align="center">
                <template slot-scope="scope">
                  <el-button @click="viewRow(scope.$index, applyingTableData)" type="text">
                    {{scope.row['applicationNum']}}
                  </el-button>
                </template>
              </el-table-column>

              <el-table-column prop="applicationDate" label="申请日期" align="center">
              </el-table-column>

              <el-table-column prop="devVersion" label="设备型号" align="center">
              </el-table-column>

              <el-table-column prop="devType" label="设备类型" align="center">
              </el-table-column>

              <el-table-column prop="productionDate" label="生产日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="installationDate" label="安装日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="esn" label="ESN" align="center">
              </el-table-column>

              <el-table-column prop="epc_agent" label="EPC、代理商" align="center">
              </el-table-column>

              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click="isShowAgreementDialog = true" type="text" style="color: green">同意</el-button>
                  <el-button @click="isShowRejectionDialog = true" type="text" style="color: red">拒绝</el-button>
                </template>
              </el-table-column>

            </el-table>

            <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                           layout="total, sizes, prev, pager, next, jumper" :total="3" style="float: right; margin-top: 5px;">
            </el-pagination>

          </el-tab-pane>
          <el-tab-pane label="审批通过" name="approved">

            <el-table :data="approvedTableData" :size="elementSize" ref="approvedTable" border>

              <el-table-column prop="num" label="编号" width="60" align="center">
              </el-table-column>

              <el-table-column prop="applicationNum" label="申请编号" align="center">
                <template slot-scope="scope">
                  <el-button @click="approvedViewRow(scope.$index, approvedTableData)" type="text">
                    {{scope.row['applicationNum']}}
                  </el-button>
                </template>
              </el-table-column>

              <el-table-column prop="applicationDate" label="申请日期" align="center">
              </el-table-column>

              <el-table-column prop="devVersion" label="设备型号" align="center">
              </el-table-column>

              <el-table-column prop="devType" label="设备类型" align="center">
              </el-table-column>

              <el-table-column prop="productionDate" label="生产日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="installationDate" label="安装日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="esn" label="ESN" align="center">
              </el-table-column>

              <el-table-column prop="epc_agent" label="EPC、代理商" align="center">
              </el-table-column>

              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click="approvedViewRow(scope.$index, approvedTableData)" type="text" style="color: green">查看
                  </el-button>
                </template>
              </el-table-column>

            </el-table>

            <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                           layout="total, sizes, prev, pager, next, jumper" :total="1" style="float: right; margin-top: 5px;">
            </el-pagination>

          </el-tab-pane>
          <el-tab-pane label="已拒绝" name="refused">

            <el-table :data="refusedTableData" :size="elementSize" ref="refusedTable" border>

              <el-table-column prop="num" label="编号" width="60" align="center">
              </el-table-column>

              <el-table-column prop="applicationNum" label="申请编号" align="center">
                <template slot-scope="scope">
                  <el-button @click="rejectionViewRow(scope.$index, refusedTableData)" type="text" style="color: red">
                    {{scope.row['applicationNum']}}
                  </el-button>
                </template>
              </el-table-column>

              <el-table-column prop="applicationDate" label="申请日期" align="center">
              </el-table-column>

              <el-table-column prop="devVersion" label="设备型号" align="center">
              </el-table-column>

              <el-table-column prop="devType" label="设备类型" align="center">
              </el-table-column>

              <el-table-column prop="productionDate" label="生产日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="installationDate" label="安装日期" align="center" :width="160">
              </el-table-column>

              <el-table-column prop="esn" label="ESN" align="center">
              </el-table-column>

              <el-table-column prop="epc_agent" label="EPC、代理商" align="center">
              </el-table-column>

              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button @click="rejectionViewRow(scope.$index, refusedTableData)" type="text" style="color: green">查看
                  </el-button>
                </template>
              </el-table-column>

            </el-table>

            <el-pagination :current-page="1" :page-sizes="[10, 20, 30, 50]" :page-size="10"
                           layout="total, sizes, prev, pager, next, jumper" :total="2" style="float: right; margin-top: 5px;">
            </el-pagination>

          </el-tab-pane>
        </el-tabs>


      </el-main>
    </el-container>

    <el-dialog title="同意" :visible.sync="isShowAgreementDialog">
      <el-form :model="agreementForm" ref="agreementForm" :size="elementSize"
               label-position="left" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批意见">
              <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="agreementForm.approvalOpinion">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
              <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" multiple :limit="3"
                         :auto-upload="false">
                <el-button size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="primary" :size="elementSize" @click="isShowAgreementDialog = false">同意</el-button>
        <el-button :size="elementSize" @click="isShowAgreementDialog = false">返回</el-button>
      </div>

    </el-dialog>

    <el-dialog title="拒绝" :visible.sync="isShowRejectionDialog" class="rejectionForm">

      <el-form :model="rejectionForm" ref="rejectionForm" :size="elementSize"
               label-position="left" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批意见">
              <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="rejectionForm.approvalOpinion">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
              <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" multiple :limit="3"
                         :auto-upload="false">
                <el-button size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="danger" :size="elementSize" @click="isShowRejectionDialog = false">拒绝</el-button>
        <el-button :size="elementSize" @click="isShowRejectionDialog = false">返回</el-button>
      </div>

    </el-dialog>

  </el-container>

  <el-container v-else>
    <el-main style="padding-top: 5px">
      <el-row >
        <el-col :span="12" style="float: left">
          <i class="el-icon-view" v-if="exchangeForm.titleType == 1">换机申请详情</i>
          <i class="el-icon-view" v-if="exchangeForm.titleType == 2">查看已通过换机申请详情</i>
          <i class="el-icon-view" v-if="exchangeForm.titleType == 3">查看已拒绝换机申请详情</i>
        </el-col>
        <el-col :span="12">
          <i class="el-icon-back" style="float: right;cursor: pointer;" @click="isShowMain = true">返回</i>
        </el-col>
      </el-row>
      <div style="width: 100%;border-bottom: 1px solid #e1e1e1;margin: 15px 0px 20px 0px"></div>
      <el-form :model="exchangeForm" ref="exchangeForm" :size="elementSize" style="width: 100%;margin-top: 10px" :rules="inputFormRules">

        <el-table :data="exchangeTableData" :size="elementSize" ref="exchangeTableData" border :show-header="false"
                  :highlight-current-row="false" :span-method="arraySpanMethod"
                  :cell-style="exchangeTableDataCellStyle">

          <el-table-column prop="kpiName1" label="kpi名称" align="center">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" :class="exchangeFormDisabledFlag ? '' : 'self-is-required'"><span>申请编号</span></div>
              <div v-else-if="scope.$index === 1"><span>生产日期</span></div>
              <div v-else-if="scope.$index === 2" :class="exchangeFormDisabledFlag ? '' : 'self-is-required'"><span>设备状态</span></div>
              <div v-else-if="scope.$index === 3" :class="exchangeFormDisabledFlag ? '' : 'self-is-required'"><span>EPC</span></div>
              <div v-else-if="scope.$index === 4"><span>安装日期</span></div>
              <div v-else-if="scope.$index === 5"><span>安装地点</span></div>
              <div v-else-if="scope.$index === 6"><span>所属电站</span></div>
              <div v-else-if="scope.$index === 7"><span>换机原因</span></div>
              <div v-else-if="scope.$index === 8"><span>申请附件</span></div>
              <div v-else-if="scope.$index === 9"><span>申请人</span></div>
              <div v-else-if="scope.$index === 10"><span>审批意见</span></div>
              <div v-else-if="scope.$index === 11"><span>审批人</span></div>
            </template>
          </el-table-column>

          <el-table-column prop="kpiValue1" label="kpi值" align="left">
            <template slot-scope="scope">
              <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="num">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.num"></el-input>
                <span v-else type="text">{{exchangeForm.num}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 1">
                <el-date-picker v-if="!exchangeFormDisabledFlag" style="width: 180px" v-model="exchangeForm.productionDate" type="date"
                                placeholder="选择日期"></el-date-picker>
                <span v-else type="text">{{exchangeForm.productionDate}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 2" prop="devStatus">
                <el-select v-if="!exchangeFormDisabledFlag" style="width: 180px" v-model="exchangeForm.devStatus" placeholder="请选择">
                  <el-option v-for="item in useOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
                <span v-else type="text">{{exchangeForm.devStatus}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 3" prop="epc">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.epc"></el-input>
                <span v-else type="text">{{exchangeForm.epc}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 4" prop="installationDate">
                <el-date-picker v-if="!exchangeFormDisabledFlag" style="width: 180px" v-model="exchangeForm.installationDate" type="date"
                                placeholder="选择日期"></el-date-picker>
                <span v-else type="text">{{exchangeForm.installationDate}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 5" prop="installationSite">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.installationSite"></el-input>
                <span v-else type="text">{{exchangeForm.installationSite}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 6" prop="powerStation">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.powerStation"></el-input>
                <span v-else type="text">{{exchangeForm.powerStation}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 7" prop="exchangeReason">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.exchangeReason"></el-input>
                <span v-else type="text">{{exchangeForm.exchangeReason}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 8" prop="attachment">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.attachment"></el-input>
                <span v-else type="text">{{exchangeForm.attachment}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 9" prop="applicant">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.applicant"></el-input>
                <span v-else type="text">{{exchangeForm.applicant}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 10" prop="applicant">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.approvalOpinion"></el-input>
                <span v-else type="text">{{exchangeForm.approvalOpinion}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 11" prop="applicant">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.approver"></el-input>
                <span v-else type="text">{{exchangeForm.approver}}</span>
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column prop="kpiName2" label="kpi名称" align="center">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" :class="exchangeFormDisabledFlag ? '' : 'self-is-required'"><span>设备类型</span></div>
              <div v-else-if="scope.$index === 1"><span>发货日期</span></div>
              <div v-else-if="scope.$index === 2" :class="exchangeFormDisabledFlag ? '' : 'self-is-required'"><span>ESN号</span></div>
              <div v-else-if="scope.$index === 3"><span>代理商</span></div>
              <div v-else-if="scope.$index === 4"><span>设备类型</span></div>
              <div v-else-if="scope.$index === 5"><span>额定输出功率(kw)</span></div>
              <div v-else-if="scope.$index === 6"><span>质保时间</span></div>
              <div v-else-if="scope.$index === 7"><span></span></div>
              <div v-else-if="scope.$index === 8"><span></span></div>
              <div v-else-if="scope.$index === 9"><span>申请时间</span></div>
              <div v-else-if="scope.$index === 10"><span></span></div>
              <div v-else-if="scope.$index === 11"><span>审批时间</span></div>
            </template>
          </el-table-column>

          <el-table-column prop="kpiValue2" label="kpi值" align="left">
            <template slot-scope="scope">
              <el-form-item v-if="scope.$index === 0" prop="devType">
                <el-select v-if="!exchangeFormDisabledFlag" style="width: 180px" v-model="exchangeForm.devType" placeholder="请选择">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
                <span v-else type="text">{{exchangeForm.devType}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 1">
                <el-date-picker v-if="!exchangeFormDisabledFlag" style="width: 180px" v-model="exchangeForm.deliveryDate" type="date"
                                placeholder="选择日期"></el-date-picker>
                <span v-else type="text">{{exchangeForm.deliveryDate}}</span>
              </el-form-item>

              <el-form-item  v-else-if="scope.$index === 2" prop="esnNum">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.esnNum"></el-input>
                <span v-else type="text">{{exchangeForm.esnNum}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 3" prop="agent">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.agent"></el-input>
                <span v-else type="text">{{exchangeForm.agent}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 4" prop="devType">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.devType"></el-input>
                <span v-else type="text">{{exchangeForm.devType}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 5" prop="ratedOutputPower">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.ratedOutputPower"></el-input>
                <span v-else type="text">{{exchangeForm.ratedOutputPower}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 6" prop="warrantyTime">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.warrantyTime"></el-input>
                <el-button v-else type="text">{{exchangeForm.warrantyTime}}</el-button>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 7">
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 8">
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 9" prop="warrantyTime">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.applicationDate"></el-input>
                <span v-else type="text">{{exchangeForm.applicationDate}}</span>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 10">
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 11" prop="warrantyTime">
                <el-input v-if="!exchangeFormDisabledFlag" v-model="exchangeForm.approvalTime"></el-input>
                <span v-else type="text">{{exchangeForm.approvalTime}}</span>
              </el-form-item>

            </template>
          </el-table-column>

        </el-table>
        <el-row v-if="!exchangeFormDisabledFlag" style="margin: 5px 0px">
          <el-col :span="12">
            <div><span>带"<span style="color:red">*</span>"为必填项</span></div>
          </el-col>
        </el-row>

        <div style="text-align: center;margin-top: 10px">
          <el-button type="primary" :size="elementSize" @click="isShowAgreementDialog = true" v-if="exchangeForm.showAgreementBtn">同意</el-button>
          <el-button type="danger" :size="elementSize" @click="isShowRejectionDialog = true" v-if="exchangeForm.showRejectionBtn">拒绝</el-button>
          <el-button type="primary" :size="elementSize" @click="isShowMain = true" v-if="exchangeForm.showConfirmBtn">确定</el-button>
          <el-button :size="elementSize" @click="isShowMain = true" v-if="exchangeForm.showReturnBtn">返回</el-button>
        </div>

      </el-form>
    </el-main>
    <el-dialog title="同意" :visible.sync="isShowAgreementDialog">
      <el-form :model="agreementForm" ref="agreementForm" :size="elementSize"
               label-position="left" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批意见">
              <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="agreementForm.approvalOpinion">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
              <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" multiple :limit="3"
                         :auto-upload="false">
                <el-button size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="primary" :size="elementSize" @click="isShowAgreementDialog = false">同意</el-button>
        <el-button :size="elementSize" @click="isShowAgreementDialog = false">返回</el-button>
      </div>

    </el-dialog>

    <el-dialog title="拒绝" :visible.sync="isShowRejectionDialog" class="rejectionForm">

      <el-form :model="rejectionForm" ref="rejectionForm" :size="elementSize"
               label-position="left" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批意见">
              <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="rejectionForm.approvalOpinion">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
              <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" multiple :limit="3"
                         :auto-upload="false">
                <el-button size="small" type="primary">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="danger" :size="elementSize" @click="isShowRejectionDialog = false">拒绝</el-button>
        <el-button :size="elementSize" @click="isShowRejectionDialog = false">返回</el-button>
      </div>

    </el-dialog>
  </el-container>


</template>

<script src='./index.js'></script>
<style src="./index.less" lang="less" scoped></style>
