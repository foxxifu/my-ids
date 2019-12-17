<template>
  <div id='devConfid' class="devConfid">
    <!-- 搜索栏 -->
    <div>
      <el-form :inline="true" :model="searchData" class="demo-form-inline" style="float: left;">
        <el-form-item :label="$t('devConfig.devName')">
          <el-input v-model="searchData.devAlias" :placeholder="$t('devConfig.devName')" clearable></el-input>
        </el-form-item>
        <!-- <el-form-item :label="$t('modules.main.menu.settings.stationManage.devConfig.devType')">
          <el-select v-model="searchData.devTypeId" :placeholder="$t('modules.allSelect')">
            <el-option v-for="item in devTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item :label="$t('devConfig.channelType')">
          <el-select v-model="searchData.channelType" :placeholder="$t('devConfig.allSelect')">
            <el-option v-for="item in channelTypes" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('devConfig.portNum')">
          <el-input v-model="searchData.port" :placeholder="$t('devConfig.portNum')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDevConfigDatas(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>

      <div style="float: right">
        <el-button @click="editDevConfig" type="primary">{{ $t('devConfig.edit') }}</el-button>
      </div>

    </div>
    <!-- 数据列表栏 -->
    <div class="list-table">
      <el-table ref="singleTable" :data="list" @row-click="rowClick" highlight-current-row :size="resizeWidth +'px'"
                border :maxHeight="tableContentHeight">
        <el-table-column label="" width="60" align="center">
          <template slot-scope="scope">
            &nbsp;<el-radio v-model="getRadio" :label="scope.row.devId">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column :label="$t('devConfig.devName')" prop="devAlias"  align="center">
        </el-table-column>
        <el-table-column :label="$t('devConfig.devVersion')" prop="devVersion" align="center">
        </el-table-column>
        <el-table-column :label="$t('devConfig.channelType')" prop="channelType" align="center">
          <template slot-scope="scope">
            <div>{{ getChannelType(scope.row.channelType) }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('devConfig.ipAddress')" prop="ip" align="center">
        </el-table-column>
        <el-table-column :label="$t('devConfig.portNum')" prop="port" align="center">
        </el-table-column>
        <el-table-column :label="$t('devConfig.logicAddress')" prop="logicalAddres" align="center">
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
      <el-dialog :title="getDialogTitle" :visible.sync="dialogVisible" width="30%" :before-close="closeDialog">
        <el-form :model="enterModel" ref="devConfigForm" :rules="rules" class="devConfigForm">
          <el-table :data="[{},{},{},{},{}]" ref="devConfigTable" border :show-header="false"
                    :cell-style="cellStyle" class="devConfigTable">
            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0"><span>{{ $t('devConfig.devName') }}</span></div>
                <div v-else-if="scope.$index === 1"><span>{{ $t('devConfig.channelType') }}</span></div>
                <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{ $t('devConfig.ipAddress') }}</span></div>
                <div v-else-if="scope.$index === 3" class="self-is-required"><span>{{ $t('devConfig.portNum') }}</span></div>
                <div v-else-if="scope.$index === 4" class="self-is-required"><span>{{ $t('devConfig.logicAddress') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="devAlias">
                    <el-input v-model="enterModel.devAlias" :placeholder="$t('devConfig.devName')" :disabled="true"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="channelType">
                    <el-select v-model="enterModel.channelType" :placeholder="$t('devConfig.channelType')">
                      <el-option v-for="item in channelType" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <el-form-item prop="ip">
                    <el-input v-model="enterModel.ip"  :placeholder="$t('devConfig.ipAddress')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 3">
                  <el-form-item prop="port">
                    <el-input v-model="enterModel.port"  :placeholder="$t('devConfig.portNum')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 4">
                  <el-form-item prop="logicalAddres">
                    <el-input v-model="enterModel.logicalAddres"  :placeholder="$t('devConfig.logicAddress')"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <el-form-item class="tr-border" align="center" style="margin-top: 10px">
            <el-button  type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
            <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>
    </div>
  </div>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
