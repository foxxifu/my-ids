<template>
  <el-container style="height: 100%">
    <el-aside style="height: 100%; width: 240px">
      <el-input :placeholder="$t('sta.enterKeyWords')" v-model="stationName" prefix-icon="el-icon-search">
      </el-input>

      <div style="width: 240px;height: calc(100% - 60px);margin-top: 15px;border: 1px solid #CAE7EF;overflow: auto">
        <el-tree class="filter-tree" :data="stationTreeData" :props="defaultProps" default-expand-all
                 @node-click="stationTreeClick" highlight-current
                 :filter-node-method="filterNode" ref="stationTree" style="width: max-content;min-width: 298px; height: 100%"
                 :expand-on-click-node="false">
        </el-tree>
      </div>

    </el-aside>
    <el-container>
      <el-header style="height: auto">
        <el-form :inline="true" :model="searchData" class="demo-form-inline" style="float: left;">
          <el-form-item :label="$t('sta.plantName')">
            <el-input v-model="searchData.name" :placeholder="$t('sta.plantName')" clearable style="width: 120px"></el-input>
          </el-form-item>

          <el-form-item :label="$t('sta.gridConTime')">
            <el-date-picker v-model="searchData.onlineTime" type="date" style="width: 140px" :format="$t('dateFormat.yyyymmdd')" value-format="timestamp">
            </el-date-picker>
          </el-form-item>

          <el-form-item :label="$t('sta.gridConType')" >
            <el-select style="width: 125px" v-model="searchData.onlineType" :placeholder="$t('sta.choose')">
              <el-option v-for="item in gridTypes" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('sta.plantCondition')">
            <el-select style="width: 165px" v-model="searchData.stationStatus" :placeholder="$t('sta.choose')">
              <el-option v-for="item in stationStatus" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="searchStationInfo">{{ $t('search') }}</el-button>
          </el-form-item>
        </el-form>
        <el-form :model="btnsForm" ref="btnsForm" :inline="true" style="float: right;">
          <el-form-item label="">
            <el-button type="primary" @click="showBindDevDialog">{{ $t('bindDevDialog.bindDev') }}</el-button>
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary" @click="addStationInfo">{{ $t('create') }}
            </el-button>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" @click="modifyStationInfo">{{ $t('modify') }}</el-button>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" @click="delStation">{{ $t('delete') }}</el-button>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" @click="setEmi">{{ $t('sta.setEnvMon') }}</el-button>
          </el-form-item>
        </el-form>
      </el-header>
      <el-main style="padding-top: 5px">
        <el-table :data="stationInfoTableData.list" ref="stationInfoTable" row-key="id" @row-click="rowClick" border max-height="600" :fit="true" @select="stationInfoSelected" @select-all="stationInfoSelected" class="stationInfoTable">

          <el-table-column type="selection" width="40" align="center">
          </el-table-column>

          <el-table-column prop="stationName" :label="$t('sta.plantName')" align="center">
            <template slot-scope="scope">
              <el-button @click="view(scope.row)" type="text">
                {{scope.row.stationName}}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column prop="installedCapacity" :label="$t('sta.insCap')" align="center">
          </el-table-column>

          <el-table-column prop="stationAddr" :label="$t('sta.plantAddress')" align="center" :show-overflow-tooltip="true">
          </el-table-column>

          <el-table-column prop="contactPeople" :label="$t('sta.contacts')" align="center">
          </el-table-column>

          <el-table-column prop="phone" :label="$t('sta.contactInf')" align="center">
          </el-table-column>

          <el-table-column prop="produceDate" :label="$t('sta.gridConTime')" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.produceDate && new Date(scope.row.produceDate).format($t('dateFormat.yyyymmdd')) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="emiInfoNames" :label="$t('sta.envMon')" align="center">
          </el-table-column>

        </el-table>

        <el-pagination :current-page="stationInfoTableData.index" :page-sizes="stationInfoTableData.pageSizes"
                       :page-size="stationInfoTableData.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" :total="stationInfoTableData.count"
                       style="float: right; margin-top: 5px;">
        </el-pagination>
      </el-main>
    </el-container>

    <el-dialog :title="stationDialog.title" :visible.sync="stationDialog.isVisible" top="50px" width="50%">

      <div :style="{maxHeight: stationDialog.maxHeight + 'px', overflow: 'auto'}">
        <el-row>
          <el-col :span="12"><span style="font-weight: 700;">{{ $t('sta.essInf') }}</span></el-col>
          <el-col :span="12" style="text-align: right">
            <el-checkbox :disabled="stationForm.isView" v-model="isPovertyRelief">{{ $t('sta.povAllPowStation') }}</el-checkbox>
          </el-col>
        </el-row>

        <el-form :model="stationForm" ref="stationForm" :size="elementSize" style="width: 100%;margin-top: 10px"
                 :rules="stationFormRules">

          <el-table :data="[{},{},{},{},{},{},{}]" ref="stationTableData" border :show-header="false"
                    :highlight-current-row="false" class="stationTableData"
                    :span-method="megerCells"
                    :cell-style="cellStyle">

            <el-table-column prop="kpiName1" :label="$t('sta.kpiValue')" align="center" :width="180">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{ $t('sta.plantAss') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{ $t('sta.insCap') }}</span></div>
                <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{ $t('sta.plantType') }}</span></div>
                <div v-else-if="scope.$index === 3" class="self-is-required"><span>{{ $t('sta.contacts') }}</span></div>
                <div v-else-if="scope.$index === 4" class="self-is-required"><span>{{ $t('sta.lonAndLat') }}</span></div>
                <div v-else-if="scope.$index === 5"><span>{{ $t('sta.location') }}</span></div>
                <div v-else-if="scope.$index === 6"><span>{{ $t('sta.plantPicture') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiValue1" :label="$t('sta.kpiValue')" align="left">
              <template slot-scope="scope">
                <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="name">
                  <!--<el-input v-model="stationForm.domainId"></el-input>-->
                  <el-dropdown trigger="click" placement="bottom-start" ref="selectTree" v-if="!stationForm.isView">
                    <el-input v-model="stationForm.name" ref="stationFormName" :placeholder="$t('sta.enterCon')"></el-input>
                    <el-dropdown-menu slot="dropdown">
                      <template>
                        <el-tree
                          :data="stationTreeData"
                          :expand-on-click-node="false"
                          node-key="id"
                          @node-click="selectDomainTreeClick"
                          highlight-current ref="selectTree"
                          :props="defaultProps" style="width:190px;overflow: auto;">
                        </el-tree>
                      </template>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <span v-else="stationForm.isView" type="text">{{stationForm.name}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 1" style="margin-bottom: 0px" prop="installedCapacity">
                  <el-input v-if="!stationForm.isView" v-model="stationForm.installedCapacity"></el-input>
                  <span v-else="stationForm.isView" type="text">{{stationForm.installedCapacity}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 2" prop="gridType" style="margin-bottom: 0px; width: 120px">
                  <el-select v-if="!stationForm.isView" style="width: 180px" v-model="stationForm.gridType" :placeholder="$t('sta.choose')">
                    <el-option v-for="item in (gridTypes.slice(1,gridTypes.length))" :key="item.value"
                               :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <span v-else="stationForm.isView" type="text">{{formatterGridType(stationForm.gridType)}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 3" prop="contactPeople" style="margin-bottom: 0px">
                  <el-input v-if="!stationForm.isView" v-model="stationForm.contactPeople"></el-input>
                  <span v-else="stationForm.isView" type="text">{{stationForm.contactPeople}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 4" prop="" style="margin-bottom: 0px">
                  <!--<el-input v-model="stationForm.longitude" style="width: 100px"></el-input>-->
                  <!--经度-->
                  <!--<el-input v-model="stationForm.latitude" style="width: 100px"></el-input>-->
                  <!--纬度-->
                  <input-for-position v-if="!stationForm.isView"
                    :lat="stationForm.latitude"
                    :lng="stationForm.longitude"
                    @myChange="dataCirleChange"
                    :isCanChoose="!stationForm.isView"
                  ></input-for-position>
                  <span v-else="stationForm.isView">{{ $t('sta.log') }}{{stationForm.longitude}},{{ $t('sta.lat') }}{{stationForm.latitude}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 5" prop="prov" style="margin-bottom: 0px">
                  <el-select v-if="!stationForm.isView" style="width: 135px" v-model="stationForm.prov" :placeholder="$t('sta.choose')" @change="provChange" filterable>
                    <el-option v-for="(item, index) in cityInfo.prov" :key="item.code" :label="item.name"
                               :value="item.code + '_' + index">
                    </el-option>
                  </el-select>
                  <el-select v-if="!stationForm.isView" style="width: 135px" v-model="stationForm.city" :placeholder="$t('sta.choose')" @change="cityChange" filterable>
                    <el-option v-for="(item, index) in citys" :key="item.code" :label="item.name"
                               :value="item.code + '_' + index">
                    </el-option>
                  </el-select>
                  <el-select style="width: 100px" v-if="stationForm.isShowDist && !stationForm.isView" v-model="stationForm.dist"
                             :placeholder="$t('sta.choose')" filterable>
                    <el-option v-for="(item, index) in dists" :key="item.code" :label="item.name"
                               :value="item.code + '_' + index">
                    </el-option>
                  </el-select>
                  <div v-if="stationForm.isView">
                    <span>{{provName + "," + cityName}}{{distName ? "," + distName : ""}}</span>
                  </div>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 6" prop="pic" style="margin-bottom: 0px">
                  <el-upload v-if="!stationForm.isView"
                    action="/biz/fileManager/fileUpload"
                    list-type="picture"
                    :file-list="fileList"
                    :on-remove="removeFile"
                    :on-exceed="handleExceed"
                    :limit="1"
                    multiple
                    :on-error="fileUploadError"
                    :on-success="fileUploadSuccess">
                    <el-button size="small" type="primary">{{ $t('sta.clickUpload') }}</el-button>
                  </el-upload>
                  <img style="max-width:262px;max-height:143px;width:100%;" v-if="stationForm.isView" :src="'/biz/fileManager/downloadFile?fileId=' + stationForm.stationFileId" />
                </el-form-item>

              </template>
            </el-table-column>

            <el-table-column align="center" :width="135">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{ $t('sta.plantName') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{ $t('sta.gridConTime') }}</span></div>
                <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{ $t('sta.plantCondition') }}</span></div>
                <div v-else-if="scope.$index === 3" class="self-is-required"><span>{{ $t('sta.contactPhone') }}</span></div>
                  <!--<div v-else-if="scope.$index === 4"><span>地图定位</span></div>-->
                <div v-else-if="scope.$index === 5" class="self-is-required"><span>{{ $t('sta.detailAddress') }}</span></div>
                <div v-else-if="scope.$index === 6"><span>{{ $t('sta.plantBriefInf') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiValue1" :label="$t('sta.kpiValue')" align="left">
              <template slot-scope="scope">
                <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="stationName">
                  <el-input v-if="!stationForm.isView" v-model="stationForm.stationName"></el-input>
                  <span v-else="stationForm.isView" type="text">{{stationForm.stationName}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 1" style="margin-bottom: 0px" prop="produceDate">
                  <!--<el-input v-model="stationForm.produceDate"></el-input>-->
                  <el-date-picker v-if="!stationForm.isView" v-model="stationForm.produceDate" :format="$t('dateFormat.yyyymmdd')" type="date" style="width: 140px"
                                  value-format="timestamp">
                  </el-date-picker>
                  <span v-else="stationForm.isView" type="text">{{new Date(stationForm.produceDate).format($t('dateFormat.yyyymmdd'))}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 2" prop="stationStatus" style="margin-bottom: 0px;">
                  <el-select v-if="!stationForm.isView" style="width: 180px" v-model="stationForm.stationStatus" :placeholder="$t('sta.choose')">
                    <el-option v-for="item in (stationStatus.slice(1,stationStatus.length))" :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                  <span v-else="stationForm.isView" type="text">{{formatterStationStatus(stationForm.stationStatus)}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 3" prop="phone" style="margin-bottom: 0px">
                  <el-input v-if="!stationForm.isView"  maxLength="11" v-model="stationForm.phone"></el-input>
                  <span v-else="stationForm.isView" type="text">{{stationForm.phone}}</span>
                </el-form-item>

                <!--<el-form-item v-else-if="scope.$index === 4" prop="location" style="margin-bottom: 0px">-->
                  <!--<el-input v-model="stationForm.location" suffix-icon="el-icon-location" @focus="isShowPostion = true"></el-input>-->
                <!--</el-form-item>-->

                <el-form-item v-else-if="scope.$index === 5" prop="stationAddr" style="margin-bottom: 0px">
                  <el-input v-if="!stationForm.isView" v-model="stationForm.stationAddr"></el-input>
                  <span v-else="stationForm.isView" type="text">{{stationForm.stationAddr}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 6" prop="stationDesc" style="margin-bottom: 0px">
                  <el-input v-if="!stationForm.isView" v-model="stationForm.stationDesc" type="textarea" :rows="2"></el-input>
                  <span v-else="stationForm.isView">{{stationForm.stationDesc}}</span>
                </el-form-item>

              </template>
            </el-table-column>

          </el-table>

          <el-row style="margin-top: 20px">
            <el-col :span="12"><span style="font-weight: 700;">{{ $t('sta.elePriceSetting') }}</span></el-col>
          </el-row>

          <!--<el-form :model="elePriceSettingForm" ref="elePriceSettingForm" :size="elementSize"
                   style="width: 100%;margin-top: 10px">-->

          <el-table :data="[{},{}]" ref="elePriceSettingTableData" border :show-header="false"
                    :highlight-current-row="false"
                    :cell-style="cellStyle" class="elePriceSettingTable">

            <el-table-column prop="kpiName1" :label="$t('sta.kpiName')" align="center" :width="200">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0"><span>{{ $t('sta.intPrice') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{ $t('sta.timeSharingSetting') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiValue1" :label="$t('sta.kpiValue')" align="left">
              <template slot-scope="scope">
                <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="price">
                  <el-input-number v-if="!stationForm.isView" v-model="stationForm.price" controls-position="right"></el-input-number>
                  <span v-else="stationForm.isView" type="text">{{stationForm.price}}</span>
                </el-form-item>

                <el-form-item v-else-if="scope.$index === 1" style="margin-bottom: 0px" prop="">
                  <el-button v-if="!stationForm.isView" type="primary" icon="el-icon-plus" style="float: right;margin-bottom: 5px"
                             @click="addTimeSetting"></el-button>
                  <div style="clear: both"></div>
                  <el-table v-for="(item, index) in priceSettings" :key="item.key" :data="item.data"
                            :size="elementSize"
                            border :show-header="false"
                            :highlight-current-row="false" style="margin-bottom: 10px" :span-method="arraySpanMethod">
                    <el-table-column align="left">
                      <template slot-scope="scope">
                        <div v-if="scope.$index === 0">
                          <el-form :model="item" :inline="true" :size="elementSize" :ref="'dateTimeForm_' + index">
                            <el-form-item prop="begin" :rules="[{ required: true, message: $t('sta.req'), trigger: 'blur' }]">
                              <el-date-picker v-if="!stationForm.isView" v-model="item.begin" type="date" :placeholder="$t('sta.startData')" style="width: 130px;" value-format="timestamp">
                              </el-date-picker>
                              <span v-else="stationForm.isView" type="text">{{new Date(item.begin).format($t('dateFormat.yyyymmdd'))}}</span>
                            </el-form-item>
                            <el-form-item>~</el-form-item>
                            <el-form-item prop="end" :rules="[{ required: true, message: $t('sta.req'), trigger: 'blur' }]">
                              <el-date-picker v-if="!stationForm.isView" v-model="item.end" type="date" :placeholder="$t('sta.endData')" style="width: 130px;" value-format="timestamp">
                              </el-date-picker>
                              <span v-else="stationForm.isView" type="text">{{new Date(item.end).format($t('dateFormat.yyyymmdd'))}}</span>
                            </el-form-item>
                          </el-form>
                        </div>
                        <div v-else-if="scope.$index === 1">
                          <el-form :inline="true" v-for="(it, i) in scope.row.times" style="margin-bottom: 5px" :model="it"
                                   :key="it.key" :size="elementSize" :ref="'timeForm_' + index + '_' + i">
                            <el-form-item prop="begin" :rules="[{ required: true, message: $t('sta.req'), trigger: 'blur' }]">
                              <el-time-select v-if="!stationForm.isView" v-model="it.begin" style="width: 120px;"
                                              :picker-options="{start: '00:00', step: '01:00', end: '24:00'}"></el-time-select>
                              <span v-else="stationForm.isView" type="text">{{it.begin}}</span>
                            </el-form-item>
                            <el-form-item>~</el-form-item>
                            <el-form-item prop="end" :rules="[{ required: true, message: $t('sta.req'), trigger: 'blur' }]">
                              <el-time-select v-if="!stationForm.isView" v-model="it.end" style="width: 120px;"
                                              :picker-options="{start: '00:00', step: '01:00', end: '24:00'}"></el-time-select>
                              <span v-else="stationForm.isView" type="text">{{it.end}}</span>
                            </el-form-item>
                            <el-form-item :label="$t('sta.elePrice')" prop="price" :rules="[{ required: true, message: $t('sta.req'), trigger: 'blur' }]">
                              <el-input-number v-if="!stationForm.isView" v-model="it.price" controls-position="right"
                                               style="width: 120px;"></el-input-number>
                              <span v-else="stationForm.isView" type="text">{{it.price}}</span>
                            </el-form-item>
                            <el-form-item>
                              <el-button v-if="!stationForm.isView" type="primary" icon="el-icon-plus" circle
                                         @click="addTimes(scope.row.times)"></el-button>
                              <el-button v-if="!stationForm.isView" type="danger" icon="el-icon-close" circle
                                         @click="del(i, scope.row.times)"></el-button>
                            </el-form-item>
                          </el-form>
                        </div>
                      </template>
                    </el-table-column>

                    <el-table-column align="center" :width="60">
                      <template slot-scope="scope">
                        <div v-if="scope.$index === 0">
                          <el-button v-if="!stationForm.isView" type="danger" icon="el-icon-close" circle
                                     @click="del(index, priceSettings, true)"></el-button>
                        </div>
                      </template>
                    </el-table-column>

                  </el-table>

                </el-form-item>
              </template>
            </el-table-column>

          </el-table>

        </el-form>
      </div>

      <div class="dialog-footer" style="text-align: center;margin-top: 10px">
        <el-button type="primary" :size="elementSize" @click="saveStationInfo">{{stationDialog.addOrModify}}</el-button>
        <el-button v-if="!stationForm.isView" type="info" :size="elementSize" @click="stationDialog.isVisible = false">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="environmentDialog.title" :visible.sync="environmentDialog.isVisible" width="40%">
      <el-form :model="environmentForm" ref="environmentForm" :size="elementSize" style="width: 100%;margin-top: 10px"
               :rules="environmentFormRules">

        <el-table :data="[{},{},{}]" ref="environmentTableData" border :show-header="false"
                  :highlight-current-row="false"
                  :cell-style="cellStyle">

          <el-table-column align="center" :width="150">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0"><span>{{ $t('sta.choosePlant') }}</span></div>
              <div v-else-if="scope.$index === 1"><span>{{ $t('sta.sourcePlant') }}</span></div>
              <div v-else-if="scope.$index === 2"><span>{{ $t('sta.plantType') }}</span></div>
            </template>
          </el-table-column>

          <el-table-column prop="kpiValue1" :label="$t('sta.kpiValue')" align="left">
            <template slot-scope="scope">
              <el-form-item v-if="scope.$index === 0" style="margin-bottom: 0px" prop="stations">
                <el-tag
                  :key="tag.stationCode"
                  v-for="tag in stationInfoTableData.selectionClone"
                  closable
                  :disable-transitions="false"
                  @close="stationClose(tag)">
                  {{tag.stationName}}
                </el-tag>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 1" style="margin-bottom: 0px" prop="fromStation">
                <el-select style="width: 180px" v-model="environmentTable.fromStation" :placeholder="$t('sta.choose')" @change="envSettingStationChange">
                  <el-option v-for="item in stationInfoTableData.hasEnvStations" :key="item.stationCode" :label="item.stationName" :value="item.stationCode">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item v-else-if="scope.$index === 2" style="margin-bottom: 0px" prop="devices">
                <el-select style="width: 180px" v-model="environmentTable.device" :placeholder="$t('sta.choose')">
                  <el-option v-for="item in devices" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>

            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div class="dialog-footer" style="text-align: center;margin-top: 10px">
        <el-button type="primary" :size="elementSize" @click="envSettingStationConfirm">{{ $t('sure') }}</el-button>
        <el-button type="info" :size="elementSize" @click="environmentDialog.isVisible = false">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>
    <bind-dev-dialog v-if="isShowBindDev" :isShowBindDev.sync="isShowBindDev" :stationCode="bindStationCode" />
  </el-container>
</template>
<style lang='less' src='./index.less' scoped></style>
<script src='./index.js'></script>
