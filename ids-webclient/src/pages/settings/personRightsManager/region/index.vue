<template>
  <div class="regionSetting">
    <div>
      <div style="margin-right: 5px;">
        <div style="float: left">
          <el-form :inline="true" :model="searchData" class="demo-form-inline">
            <el-form-item :label="$t('rol.ower')">
              <el-select v-model="id" :placeholder="$t('region.choose')" :props="enterpriseProps" @change="changeEnterprise">
                <el-option
                  v-for="item in enterprises"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <div style="float: right">
          <el-button type="primary" @click="createDomain">{{$t('create') }}</el-button>
        </div>
        </div>
      <div style="clear: both"></div>
     <!-- <div style="background: white;padding-bottom: 5px;padding-top: 5px;clear: both">
        <el-row :gutter="20">
          <el-col :span="8">{{$t('modules.main.menu.settings.domainManager.name')}}</el-col>
          <el-col :span="14">{{$t('modules.main.menu.settings.domainManager.description')}}</el-col>
          <el-col :span="2" align="center">{{$t('modules.main.menu.settings.domainManager.op')}}</el-col>
        </el-row>
      </div>-->
      <span class="custom-tree-node" style="height: 20px;display: none" >
        <span>{{$t('region.name')}}</span>
        <span>{{$t('region.description')}}</span>
        <span>{{$t('region.op')}}</span>
      </span>
    </div>
    <el-tree :data="list" :props="props"  show-checkbox node-key="id"  :expand-on-click-node="false" default-expand-all >
      <span class="custom-tree-node" slot-scope="{ node, data }" >
        <span style="flex: 0;">{{ data.name }}</span>
        <div style="flex: 1;padding: 0 10px"><div style="width: 100%;border: 1px dashed #80808052;"></div></div>
        <span style="flex: 0;">
          <el-button type="text" size="" @click="createDomain(data)">
            {{$t('create') }}
          </el-button>
          <el-button type="text" size="" @click="getDomainById(data, node)">
            {{$t('modify') }}
          </el-button>
          <el-button type="text" size="" @click="deleteDomain(data.id)">
            {{$t('delete') }}
          </el-button>
        </span>
      </span>
    </el-tree>

    <!-- 弹出框 -->
    <el-dialog :title="getDialogTitle" v-if="dialogVisible" :visible="dialogVisible" class="dialog" width="600px" :before-close="closeDialog" :close-on-click-modal="false">

      <el-form :model="domainForm" ref="domainForm" :rules="rules" class="domainForm">
      <el-table :data="[{},{},{},{},{},{},{},{}]" ref="domainTable" border :show-header="false"
                :cell-style="cellStyle">
        <el-table-column align="center" :width="150">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0"><span>{{$t('region.domainOwer')}}</span></div>
            <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{$t('region.name')}}</span></div>
            <div v-else-if="scope.$index === 2"><span>{{$t('region.currency')}}</span></div>
            <div v-else-if="scope.$index === 3"><span>{{$t('region.domainPrice')}}</span></div>
            <div v-else-if="scope.$index === 4" class="self-is-required"><span>{{$t('region.longitude')}}</span></div>
            <div v-else-if="scope.$index === 5" class="self-is-required"><span>{{$t('region.latitude')}}</span></div>
            <div v-else-if="scope.$index === 6" class="self-is-required"><span>{{$t('region.radius')}}</span></div>
            <div v-else-if="scope.$index === 7"><span>{{$t('region.description')}}</span></div>
          </template>
        </el-table-column>

        <el-table-column align="left">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0">
              <el-form-item prop="domainOwer">
                <chooseMyRegin :disabled="isEdit" :treeData="list" :props="props" accordion :isClickLeaf="false" :value-mode.sync="domainForm.parentId"></chooseMyRegin>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 1">
              <el-form-item prop="name">
                <el-input v-model="domainForm.name" clearable></el-input>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 2">
              <el-form-item prop="currency">
                <el-select v-model="domainForm.currency" :placeholder="$t('region.choose')">
                  <el-option
                    v-for="item in currencys"
                    :key="item.label"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 3">
              <el-form-item prop="domainPrice">
                <el-input v-model="domainForm.domainPrice" clearable></el-input>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 4">
              <el-form-item prop="longitude">
                <el-input v-model="domainForm.longitude" @focus="showPositionDialog" clearable></el-input>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 5">
              <el-form-item prop="latitude">
                <el-input v-model="domainForm.latitude" @focus="showPositionDialog" clearable></el-input>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 6">
              <el-form-item prop="radius">
                <el-input v-model="domainForm.radius" @focus="showPositionDialog" clearable></el-input>
              </el-form-item>
            </div>
            <div v-else-if="scope.$index === 7">
              <el-form-item prop="description">
                <el-input v-model="domainForm.description" clearable type="textarea" :rows="3"></el-input>
              </el-form-item>
            </div>
          </template>
        </el-table-column>
      </el-table>

        <el-form-item class="tr-border" align="center" style="margin-top: 10px">
          <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
          <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
        </el-form-item>

      </el-form>

    </el-dialog>
    <!-- 选择位置的弹出框 -->
    <choose-position v-if="isPostionChoose" :isDialogVisible.sync="isPostionChoose" :lat="domainForm.latitude"
                     :lng="domainForm.longitude"
                     :radius="domainForm.radius"
                     :isPosition="false"
                     @data-circle-change="dataCirleChange"></choose-position>
  </div>

</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>

