<template>
  <el-tabs id="pointManage" type="border-card" tab-position="right">
    <el-tab-pane :label="$t('poiMan.importPoint')">
      <div id='importPointTable'>
        <!-- 搜索栏 -->
        <div>
          <el-form :inline="true" :size="elementSize" style="float: left;">
            <el-form-item>
              <el-input disabled :value="myFileName" :placeholder="$t('poiMan.selFile')"></el-input>
            </el-form-item>
            <el-form-item>
              <fileUpload
                :uploadUrl="importUrl"
                :fileTypeError="$t('poiMan.excelFile')"
                @on-validate-success="setInputText"
                :isShowTip="false"
                :userValidate="myFileType"
                @on-upload-success="onUploadSuccess"
              ></fileUpload>
            </el-form-item>
          </el-form>

          <div style="float: right;">
            <el-button @click="shakeInfoConvertToAlarm" type="primary">{{ $t('poiMan.telAle') }}</el-button>
            <el-button @click="alarmConvertToShakeInfo" type="primary">{{ $t('poiMan.warToTel') }}</el-button>
            <el-button @click="delPointManage" type="primary">{{ $t('poiMan.del') }}</el-button>
            <el-button @click="exportPoint" type="primary">{{ $t('poiMan.tabExport') }}</el-button>
          </div>
        </div>

        <!-- 数据列表栏 -->
        <div class="list-table">
          <el-table ref="singleTable" :data="list" row-key="id"
                    @row-click="(row, event, column) => {rowClick(row, event, column, 'singleTable')}"
                    border @selection-change="handleSelectionChange" class="singleTable" :max-height="singleTable.maxHeight">
            <el-table-column type="selection" width="60" align="center">
            </el-table-column>
            <el-table-column :label="$t('poiMan.pointName')"
                             prop="signalDataName" :width="celRealWidthArr[0]" align="center">
            </el-table-column>
            <el-table-column :label="$t('poiMan.devType')" prop="devTypeId"
                             :width="celRealWidthArr[1]" align="center">
              <template slot-scope="scope">
                 <div>{{ scope.row.devTypeId && $t('devTypeId.' + scope.row.devTypeId) }}</div>
               </template>
            </el-table-column>
            <el-table-column :label="$t('poiMan.devVersion')" prop="version"
                             :width="celRealWidthArr[2]" align="center">
            </el-table-column>
            <el-table-column :label="$t('poiMan.importDate')"
                             prop="createDate" :width="celRealWidthArr[3]" align="center">
              <template slot-scope="scope">
                <div>{{ getDateTime(scope.row.createDate) }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('poiMan.detail')" prop="description"
                             :width="celRealWidthArr[4]" align="center">
              <template slot-scope="scope">
                <el-button type="text" icon="el-icon-info" class="table-skip-text-color" size="medium"
                           @click="showDetail(scope.row, $event, true)">{{
                  $t('poiMan.detail') }}
                </el-button>
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
          <!-- 弹出框 -->
          <el-dialog :title="$t('poiMan.devInfo')"
                     :visible.sync="dialogVisible" width="70%" :before-close="closeDialog">
            <!-- 搜索栏 -->
            <div style="float: left" class="search-bar">
              <el-form :inline="true" :model="popSearchData" class="demo-form-inline">
                <el-form-item :label="$t('poiMan.devName')">
                  <el-input v-model="popSearchData.deviceName"
                            :placeholder="$t('poiMan.devName')"
                            clearable style="width: 155px"></el-input>
                </el-form-item>
                <el-form-item :label="$t('poiMan.signalName')">
                  <el-input v-model="popSearchData.signalName"
                            :placeholder="$t('poiMan.signalName')"
                            clearable style="width: 135px"></el-input>
                </el-form-item>
                <el-form-item :label="$t('poiMan.devVersion')">
                  <el-input v-model="popSearchData.version"
                            :placeholder="$t('poiMan.devVersion')"
                            clearable style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item :label="$t('poiMan.signalType')">
                  <el-select style="width: 235px" v-model="popSearchData.signalType"
                             :placeholder="$t('poiMan.allSelect')">
                    <el-option v-for="item in signalTypes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="getDevSignalInfo(true)">{{ $t('search') }}</el-button>
                </el-form-item>
              </el-form>
            </div>
            <!-- 按钮栏 -->
            <div class="btns" style="float: right; overflow: hidden;margin-bottom: 10px;">
              <div style="float: right">
                <el-button @click="modifySigGainAndOffset" type="primary">{{ $t('poiMan.preservation') }}</el-button>
              </div>
            </div>
            <div style="clear: both"></div>
            <!-- 数据列表栏 -->
            <div class="list-table">
              <el-form :model="popTable" ref="popTableForm">
              <el-table ref="popTable" :data="popTable.popList" class="popTable"
                        border :maxHeight="popTable.maxHeight" row-key="id"
                        @row-click="(row, event, column) => {rowClick(row, event, column, 'popTable')}"
                        @selection-change="popHandleSelectionChange" :cell-class-name="popTableCellClass">
                <!--<el-table-column type="selection" width="60" align="center">
                </el-table-column>-->
                <el-table-column :label="$t('poiMan.devName')"
                                 prop="deviceName" :width="popCelRealWidthArr[0]" align="center">
                </el-table-column>
                <el-table-column :label="$t('poiMan.signalName')"
                                 prop="sigName" :width="popCelRealWidthArr[1]" align="center">
                </el-table-column>
                <el-table-column :label="$t('poiMan.sigGain')"
                                 prop="sigGain" :width="popCelRealWidthArr[2]" align="center">
                  <template slot-scope="scope">
                    <el-form-item :prop="'popList.'+scope.$index+'.sigGain'" :rules="[{ required: true, message: $t('poiMan.rep'), trigger: 'blur' },{type: 'number', message: $t('poiMan.musNum'), trigger: 'blur'}]">
                    <span class="cell-edit-input"><el-input v-model.number="scope.row.sigGain"></el-input></span>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('poiMan.sigOffset')"
                                 prop="sigOffset" :width="popCelRealWidthArr[3]" align="center">
                  <template slot-scope="scope">
                    <el-form-item :prop="'popList.'+scope.$index+'.sigOffset'" :rules="[{ required: true, message: $t('poiMan.rep'), trigger: 'blur' },{type: 'number', message: $t('poiMan.musNum'), trigger: 'blur'}]">
                    <span class="cell-edit-input"><el-input v-model.number="scope.row.sigOffset"></el-input></span>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('poiMan.signalType')"
                                 prop="sigType" :width="popCelRealWidthArr[4]" align="center">
                  <template slot-scope="scope">
                    <div>{{ getSigTypeType(scope.row.sigType) }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('poiMan.devVersion')"
                                 prop="version" :width="popCelRealWidthArr[5]" align="center">
                </el-table-column>
              </el-table>
              </el-form>
              <div style="overflow: hidden;margin-top: 10px;">
                <div style="float: right;right: 10px;">
                  <el-pagination @size-change="popPageSizeChange" @current-change="popPageChange"
                                 :current-page="popSearchData.page"
                                 :page-sizes="[10, 20, 30, 50]" :page-size="popSearchData.pageSize"
                                 layout="total, sizes, prev, pager, next, jumper" :total="popTotal">
                  </el-pagination>
                </div>
              </div>
            </div>
          </el-dialog>

        </div>
      </div>
    </el-tab-pane>

    <el-tab-pane :label="$t('poiMan.uniformizationConfig')"
                 name="uniformizationConfig">

      <el-form :inline="true" v-model="pointParamSettingForm" :size="elementSize" style="float: left">

        <el-form-item :label="$t('poiMan.devType')">
          <el-select style="width: 300px" v-model="pointParamSettingForm.devType" :placeholder="$t('poiMan.choose')"
                     @change="pointParamSettingDevTypeChange">
            <el-option v-for="item in devTypes" :key="item" :label="$t('devTypeId.' + item)" :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('poiMan.version')">
          <el-select style="width: 180px" v-model="pointParamSettingForm.modelVersion" :placeholder="$t('poiMan.choose')"
                     @change="pointParamSettingModelVersionChange">
            <el-option v-for="item in modelVersions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-form :inline="true" :size="elementSize" style="float: right">
        <el-form-item>
          <el-button type="primary" :size="elementSize" @click="savePointParamSetting">{{ $t('poiMan.preservation') }}</el-button>
          <el-button type="primary" :size="elementSize" @click="resetPointParamSetting">{{ $t('poiMan.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <div style="clear: both"></div>

      <el-table :data="pointParamSettingTable.list"
                @row-click="(row, event, column) => {rowClick(row, event, column, 'pointParamSettingTable')}"
                class="pointParamSettingTable" v-loading="pointParamSettingTable.loading" :size="elementSize"
                ref="pointParamSettingTable" border :max-height="pointParamSettingTable.maxHeight">

       <!-- <el-table-column type="selection" width="55" align="center">
        </el-table-column>-->

        <!--<el-table-column prop="id" label="序号" align="center">
        </el-table-column>-->

        <el-table-column type="index" :label="$t('poiMan.serNum')" width="100" align="center">
        </el-table-column>

        <el-table-column prop="signalName" :label="$t('poiMan.parName')" align="center">
          <template slot-scope="scope">
            <span v-text="$t('dataConverter.normalized.' + scope.row.id)"></span>
          </template>
        </el-table-column>

        <el-table-column prop="params" :label="$t('poiMan.mapPar')" align="center">
          <template slot-scope="scope">
            <el-select style="width: 180px" v-model="scope.row.mappingParam" :placeholder="$t('poiMan.choose')" filterable>
              <el-option v-for="item in scope.row.mappingParams" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>

      </el-table>

      <div style="clear: both"></div>

    </el-tab-pane>
    <!-- 遥信转告警 -->
    <el-dialog :title="$t('poiMan.telAle')" :visible.sync="toAlarmDialog.isShow" width="70%">
      <el-form :inline="true" v-model="toAlarmForm" :size="elementSize" style="float: left">
        <el-form-item :label="$t('poiMan.devType')">
          <!--<el-button type="primary" @click="getDevSignalInfo(true)">{{ $t('modules.search') }}</el-button>-->
          <el-select style="width: 180px" v-model="toAlarmForm.devType" :placeholder="$t('poiMan.choose')" clearable>
            <el-option v-for="item in devTypes" :key="item" :label="$t('devTypeId.' + item)" :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('poiMan.signalName')">
          <el-input type="primary" v-model="toAlarmForm.pointName"></el-input>
        </el-form-item>

        <el-form-item :label="$t('poiMan.version')">
          <el-input v-model="toAlarmForm.modelVersion" type="primary"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchToAlarm">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" :size="elementSize" style="float: right">
        <el-form-item>
          <el-button type="primary" :size="elementSize" @click="toAlarm">{{ $t('poiMan.telAle') }}</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="toAlarmTable.list" class="toAlarmTable"
                @row-click="(row, event, column) => {rowClick(row, event, column, 'toAlarmTable')}"
                v-loading="toAlarmTable.loading" :size="elementSize" ref="toAlarmTable" border
                @select="toAlarmTableSelect" @select-all="toAlarmTableSelect" :max-height="toAlarmTable.maxHeight">

        <el-table-column type="selection" width="55" align="center">
        </el-table-column>

        <el-table-column prop="devTypeId" :label="$t('poiMan.devType')" align="center">
          <template slot-scope="scope">
            {{$t('devTypeId.' + scope.row.devTypeId)}}
          </template>
        </el-table-column>

        <el-table-column prop="signalName" :label="$t('poiMan.signalName')" align="center">
        </el-table-column>

        <el-table-column prop="signalType" :label="$t('poiMan.sigType')" align="center"
                         :formatter="(a,b,c,d) => {return getSigTypeType(c)}">
        </el-table-column>

        <el-table-column prop="signalVersion" :label="$t('poiMan.version')" align="center">
        </el-table-column>

      </el-table>

      <el-pagination :current-page="toAlarmTable.index" :page-sizes="toAlarmTable.pageSizes"
                     :page-size="toAlarmTable.pageSize"
                     @size-change="val => {toAlarmTable.pageSize = val; searchToAlarm()}"
                     @current-change="val => {toAlarmTable.index = val; searchToAlarm()}"
                     layout="total, sizes, prev, pager, next, jumper" :total="toAlarmTable.count"
                     style="float: right; margin-top: 5px;">
      </el-pagination>

      <div style="clear: both"></div>

    </el-dialog>

    <el-dialog :title="$t('poiMan.telAlarm')" :visible.sync="toAlarmDialog.alarmSettingDialog.isShow" width="30%">
      <el-table :data="[{},{}]" :size="elementSize" ref="alarmSettingTable" border :show-header="false"
                :cell-style="cellStyle">
        <el-table-column align="center" :width="150">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0"><span>{{ $t('poiMan.alarmType') }}</span></div>
            <div v-else-if="scope.$index === 1"><span>{{ $t('poiMan.alarmLevel') }}</span></div>
          </template>
        </el-table-column>

        <el-table-column align="center">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0">
              <el-select style="width: 180px" v-model="toAlarmDialog.alarmSettingDialog.alarmType" :placeholder="$t('poiMan.choose')">
                <el-option v-for="item in alarmTypes" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
            <div v-else-if="scope.$index === 1">
              <el-select style="width: 180px" v-model="toAlarmDialog.alarmSettingDialog.alarmLevel" :placeholder="$t('poiMan.choose')">
                <el-option v-for="item in alarmLevels" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="dialog-footer" style="text-align: center;margin-top: 10px">
        <el-button type="primary" :size="elementSize" @click="confirmAlarmSettig">{{ $t('poiMan.sure') }}</el-button>
        <el-button type="info" :size="elementSize" @click="toAlarmDialog.alarmSettingDialog.isShow = false">{{ $t('poiMan.cancel') }}
        </el-button>
      </div>
    </el-dialog>
    <!-- 告警转遥信弹出框 -->
    <el-dialog :title="$t('poiMan.warToTel')" :visible.sync="toShakeInfoDialog.isShow" width="70%">
      <el-form :inline="true" v-model="toShakeInfoForm" :size="elementSize" style="float:left">
        <el-form-item :title="$t('poiMan.devType')" :label="$t('poiMan.devType')">
          <!--<el-button type="primary" @click="getDevSignalInfo(true)">{{ $t('modules.search') }}</el-button>-->
          <el-select style="width: 180px" v-model="toShakeInfoForm.devType" :placeholder="$t('poiMan.choose')" clearable>
            <el-option v-for="item in devTypes" :key="item" :label="$t('devTypeId.' + item)" :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('poiMan.sigPoi')">
          <el-input type="primary" v-model="toShakeInfoForm.pointName"></el-input>
        </el-form-item>

        <el-form-item :label="$t('poiMan.version')">
          <el-input v-model="toShakeInfoForm.modelVersion" type="primary"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchToShakeInfo">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" :size="elementSize" style="float: right">
        <el-form-item>
          <el-button type="primary" :size="elementSize" @click="toShakeInfo">{{ $t('poiMan.warToTel') }}</el-button>
        </el-form-item>
      </el-form>
      <div style="clear: both"></div>

      <el-table :data="toShakeInfoTable.list" class="toShakeInfoTable"
                @row-click="(row, event, column) => {rowClick(row, event, column, 'toShakeInfoTable')}"
                :size="elementSize" ref="toShakeInfoTable" border @select="toShakeInfoTableSelect"
                @select-all="toShakeInfoTableSelect" :max-height="toShakeInfoTable.maxHeight">

        <el-table-column type="selection" width="55" align="center">
        </el-table-column>

        <el-table-column prop="devTypeId" :label="$t('poiMan.devType')" align="center">
          <template slot-scope="scope">
            {{$t('devTypeId.' + scope.row.devTypeId)}}
          </template>
        </el-table-column>

        <el-table-column prop="signalName" :label="$t('poiMan.sigPoi')" align="center">
        </el-table-column>

        <el-table-column prop="signalType" :label="$t('poiMan.sigType')" align="center"
                         :formatter="(a,b,c,d) => {return getSigTypeType(c)}">
        </el-table-column>

        <el-table-column prop="signalVersion" :label="$t('poiMan.version')" align="center">
        </el-table-column>

        <el-table-column prop="alarmType" :label="$t('poiMan.alarmType')" align="center" :formatter="formatterAlarmType">
        </el-table-column>

        <el-table-column prop="levelId" :label="$t('poiMan.alarmLevel')" align="center" :formatter="formatterAlarmLevel">
        </el-table-column>

      </el-table>

      <el-pagination :current-page="toShakeInfoTable.index" :page-sizes="toShakeInfoTable.pageSizes"
                     :page-size="toShakeInfoTable.pageSize"
                     @size-change="val => {toShakeInfoTable.pageSize = val; searchToShakeInfo();}"
                     @current-change="val => {toShakeInfoTable.index = val; searchToShakeInfo()}"
                     layout="total, sizes, prev, pager, next, jumper" :total="toShakeInfoTable.count"
                     style="float: right; margin-top: 5px;">
      </el-pagination>

      <div style="clear: both"></div>

    </el-dialog>

    <el-dialog :title="$t('poiMan.warToTel')" :visible.sync="toShakeInfoDialog.alarmSettingDialog.isShow" width="30%">
      <!--<el-table :data="[{},{},{}]" :size="elementSize" ref="" border :show-header="false" :cell-style="cellStyle">
        <el-table-column align="center" :width="150">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0"><span>信号点名称</span></div>
            <div v-else-if="scope.$index === 1"><span>告警类型</span></div>
            <div v-else-if="scope.$index === 2"><span>告警级别</span></div>
          </template>
        </el-table-column>

        <el-table-column align="center">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0">
              {{toShakeInfoDialog.alarmSettingDialog.pointName}}
            </div>
            <div v-else-if="scope.$index === 1">
              {{toShakeInfoDialog.alarmSettingDialog.alarmType}}
            </div>
            <div v-else-if="scope.$index === 2">
              {{toShakeInfoDialog.alarmSettingDialog.alarmLevel}}
            </div>
          </template>
        </el-table-column>
      </el-table>-->
      <h1 style="color: red;margin: 20px auto;">{{ $t('poiMan.conToTel') }}</h1>
      <div class="dialog-footer" style="text-align: center;margin-top: 10px">
        <el-button type="primary" :size="elementSize" @click="confirmToShakeInfo">{{ $t('poiMan.sure') }}</el-button>
        <el-button type="info" :size="elementSize" @click="toShakeInfoDialog.alarmSettingDialog.isShow = false">{{ $t('poiMan.cancel') }}
        </el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
