<template>
<TreeNav
           @node-click="getStationParamDatas"
           :lazy="false"
           :data="treeData"
           :props="props"
           isLeafClick
           isFilter
           :titleText="$t('staPar.paramName')">
       <div id='stationParamConfig' class="stationParamConfig">
        <div style="float: left">
          <el-form :inline="true" :model="searchData" class="demo-form-inline">
            <el-form-item :label="$t('staPar.orgStr')">
              <el-input :span="1" v-model="searchData.paramName" :placeholder="$t('staPar.paramName')" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="getStationConfigDatas(true)">{{ $t('search') }}</el-button>
            </el-form-item>
          </el-form>
        </div>
        <!-- 按钮栏 -->
        <div class="btns" style="float: right;margin-bottom: 10px;">
          <el-button @click="editParamConfig" type="primary">{{ $t('staPar.edit') }}</el-button>
        </div>
        <!-- 数据列表栏 -->
        <div class="list-table">
          <el-table ref="singleTable" :data="list" @row-click="rowClick" row-key="id" highlight-current-row :size="resizeWidth +'px'"
                    border :maxHeight="tableContentHeight">
            <el-table-column label="" width="60">
              <template slot-scope="scope">
                &nbsp;<el-radio v-model="getRadio" :label="scope.row.paramKey">&nbsp;</el-radio>
              </template>
            </el-table-column>
            <el-table-column :label="$t('staPar.paramName')" prop="paramName" align="center">
              <template slot-scope="scope">
                <span v-text="$t('dataConverter.stationParam.' + scope.row.paramKey)"></span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('staPar.paramValue')" prop="paramValue" align="center">
            </el-table-column>
            <el-table-column :label="$t('staPar.paramUnit')" prop="paramUnit" align="center">
            </el-table-column>
            <el-table-column :label="$t('staPar.updateUser')" prop="modifyUserName" align="center">
            </el-table-column>
            <el-table-column :label="$t('staPar.uodateTime')" prop="modifyDate" align="center">
              <template slot-scope="scope">
                <div>{{ getDateTime(scope.row.modifyDate) }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('staPar.paramDesc')" prop="description" :width="celRealWidthArr[5]" align="left">
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
          <el-dialog :title="$t('staPar.edit')" :visible.sync="dialogVisible" width="40%" :before-close="closeDialog">
            <!--<el-form :model="enterModel" ref="stationParamConfigForm" :rules="rules" status-icon>
              <el-form-item class="tr-border">
                <el-col :span="4" class="label-right">
                  {{ $t('modules.main.menu.settings.paramConfig.stationParam.paramName') }}
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="paramName">
                    <el-input :disabled="true" v-model="enterModel.paramName" :placeholder="$t('modules.main.menu.settings.paramConfig.stationParam.paramName')"></el-input>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item class="tr-border">
                <el-col :span="4" class="label-right">
                  {{ $t('modules.main.menu.settings.paramConfig.stationParam.paramValue') }}
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="paramValue">
                    <el-input v-model="enterModel.paramValue" :placeholder="$t('modules.main.menu.settings.paramConfig.stationParam.paramValue')"></el-input>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item class="bottom-border tr-border">
                <el-col :span="4" class="label-right">{{ $t('modules.main.menu.settings.paramConfig.stationParam.paramUnit') }}</el-col>
                <el-col :span="8">
                  <el-form-item prop="paramUnit">
                    <el-input :disabled="true" type="textarea" v-model="enterModel.paramUnit" :placeholder="$t('modules.main.menu.settings.paramConfig.stationParam.paramUnit')"></el-input>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item class="bottom-border tr-border">
                <el-col :span="4" class="label-right">{{ $t('modules.main.menu.settings.paramConfig.stationParam.paramDesc') }}</el-col>
                <el-col :span="16">
                  <el-form-item prop="description">
                    <el-input type="textarea" v-model="enterModel.description" :placeholder="$t('modules.main.menu.settings.paramConfig.stationParam.paramDesc')"></el-input>
                  </el-form-item>
                </el-col>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
              <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
            </span>-->

            <el-form :model="enterModel" ref="stationParamConfigForm" :rules="rules" class="stationParamConfigForm">
              <el-table :data="[{},{},{},{}]" ref="stationParamConfigTable" border :show-header="false"
                        :cell-style="cellStyle">
                <el-table-column align="center" :width="165">
                  <template slot-scope="scope">
                    <div v-if="scope.$index === 0"><span>{{ $t('staPar.paramName') }}</span></div>
                    <div v-else-if="scope.$index === 1"><span>{{ $t('staPar.paramValue') }}</span></div>
                    <div v-else-if="scope.$index === 2"><span>{{ $t('staPar.paramUnit') }}</span></div>
                    <div v-else-if="scope.$index === 3"><span>{{ $t('staPar.paramDesc') }}</span></div>
                  </template>
                </el-table-column>

                <el-table-column align="left">
                  <template slot-scope="scope">
                    <div v-if="scope.$index === 0">
                      <el-form-item prop="paramName">
                        <el-input :disabled="true" v-model="enterModel.paramName" :placeholder="$t('staPar.paramName')"></el-input>
                      </el-form-item>
                    </div>
                    <div v-else-if="scope.$index === 1">
                      <el-form-item prop="paramValue">
                        <el-input v-model="enterModel.paramValue" :placeholder="$t('staPar.paramValue')"></el-input>
                      </el-form-item>
                    </div>
                    <div v-else-if="scope.$index === 2">
                      <el-form-item prop="paramUnit">
                        <el-input :disabled="true" type="textarea" v-model="enterModel.paramUnit" :placeholder="$t('staPar.paramUnit')"></el-input>
                      </el-form-item>
                    </div>
                    <div v-else-if="scope.$index === 3">
                      <el-form-item prop="description">
                        <el-input type="textarea" v-model="enterModel.description" :placeholder="$t('staPar.paramDesc')"></el-input>
                      </el-form-item>
                    </div>
                  </template>
                </el-table-column>
              </el-table>

              <el-form-item class="tr-border" align="center" style="margin-top: 10px">
                <el-button type="primary" @click="submitForm" :disabled="isSubmitting">{{ $t('sure') }}</el-button>
                <el-button @click="closeDialog" :disabled="isSubmitting">{{ $t('cancel') }}</el-button>
              </el-form-item>

            </el-form>

          </el-dialog>
        </div>
      </div>
 </TreeNav>
</template>

<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
