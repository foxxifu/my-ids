<template>
  <div class='enterprise'>
    <!-- 搜索栏 -->
    <div class="" style="float: left">
      <el-form :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item :label="$t('entPri.name')">
          <el-input style="width: 155px" v-model="searchData.name" :placeholder="$t('entPri.name')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('entPri.address')">
          <el-input style="width: 165px" v-model="searchData.address" :placeholder="$t('entPri.address')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('entPri.phone')">
          <el-input style="width: 140px" v-model="searchData.contactPhone" :placeholder="$t('entPri.phone')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('entPri.contatUser')">
          <el-input style="width: 160px" v-model="searchData.contactPeople" :placeholder="$t('entPri.contatUser')" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getEnterpriseDatas(true)">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮栏 -->
    <div class="btns" style="float: right;;margin-bottom: 10px;">
      <el-button @click="addEnterprise" type="primary">{{ $t('create') }}</el-button>
      <el-button @click="eidtEnterprise" type="primary">{{ $t('modify') }}</el-button>
      <el-button @click="delEnterprise" type="primary">{{ $t('delete') }}</el-button>
    </div>
    <!-- 数据列表栏 -->
    <div class="list-table">
      <el-table ref="singleTable" :data="list" @row-click="rowClick" highlight-current-row
                border :maxHeight="tableContentHeight">
        <el-table-column label="" width="60">
          <template slot-scope="scope">
            &nbsp;<el-radio v-model="getRadio" class="myRadioHoverCss" :label="scope.row.id" readonly disabled>&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column :label="$t('entPri.name')" prop="name" align="center">
          <template slot-scope="scope">
            <div class="text-ellipsis" :title="scope.row.name">{{ scope.row.name }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('entPri.address')" prop="address" align="center">
          <template slot-scope="scope">
            <div class="text-ellipsis" :title="scope.row.address">{{ scope.row.address }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('entPri.phone')" prop="contactPhone" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.email')" prop="email" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.contatUser')" prop="contactPeople" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.longitude')" prop="longitude" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.latitude')" prop="latitude" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.radius')" prop="radius" align="center">
        </el-table-column>
        <el-table-column :label="$t('entPri.op')" prop="description" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-info" class="table-skip-text-color"
                       @click="showDetail(scope.row, $event)">{{ $t('entPri.detail') }}
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
      <el-dialog :title="getDialogTitle" :visible.sync="dialogVisible" width="60%" :before-close="closeDialog">
        <el-form :model="enterModel" ref="enterpriseForm" :rules="rules" class="enterpriseForm" status-icon>
          <el-table :data="enterpriseTable" ref="enterpriseTable" border :show-header="false"
                    :cell-style="cellStyle" :span-method="megerCells" class="enterpriseTable">
            <!-- 第1列 -->
            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{ $t('entPri.name') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{ $t('entPri.description') }}</span></div>
                <div v-else-if="scope.$index === 2"><span>{{ $t('entPri.logo') }}</span></div>
                <div v-else-if="scope.$index === 3 && myDialogType(0)" class="self-is-required"><span>{{ $t('entPri.enterRole') }}</span></div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4" class="self-is-required"><span>{{ $t('entPri.contatUser') }}</span></div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 4 : scope.$index === 5" class="self-is-required"><span>{{ $t('entPri.geoSco') }}</span></div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 5 : scope.$index === 6" class="self-is-required"><span>{{ $t('entPri.address') }}</span></div>
              </template>
            </el-table-column>
            <!-- 第2列 -->
            <el-table-column align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="name">
                    <el-input v-model="enterModel.name"  :placeholder="$t('entPri.name')" :disabled="isDetal"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="description">
                    <el-input type="textarea" v-model="enterModel.description" name="description" :placeholder="$t('entPri.description')" :disabled="isDetal"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <el-form-item prop="logo">
                    <img-upload :isShowImgOnly="isDetal" style="background-color: #0A7EA5;" :fileId.sync="enterModel.logo" isValidWH></img-upload>
                  </el-form-item>
                </div>

                <div v-else-if="scope.$index === 3 && myDialogType(0)">
                  <el-form-item prop="loginName">
                    <el-input v-model="enterModel.loginName" :placeholder="$t('entPri.enterRole')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4">
                  <el-form-item prop="contactPeople">
                    <el-input v-model="enterModel.contactPeople" :placeholder="$t('entPri.contatUser')" :disabled="isDetal"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 4 : scope.$index === 5">
                  <input-for-position
                    :lat="enterModel.latitude"
                    :lng="enterModel.longitude"
                    :radius="enterModel.radius"
                    :isPosition="false"
                    :isCanChoose="!myDialogType(2)"
                    @myChange="dataCirleChange"
                  ></input-for-position>
                </div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 5 : scope.$index === 6">
                  <el-form-item prop="address">
                    <el-input type="textarea" :disabled="isDetal" v-model="enterModel.address" :placeholder="$t('entPri.address')"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>
            <!-- 第3列 -->
            <el-table-column align="center" :width="160">
              <template slot-scope="scope">
                <div v-if="scope.$index === 2"><span>{{ $t('entPri.smallImg') }}</span></div>
                <div v-else-if="scope.$index === 3 && myDialogType(0)" class="self-is-required"><span>{{ $t('entPri.password') }}</span></div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4" class="self-is-required"><span>{{ $t('entPri.phone') }}</span></div>
              </template>
            </el-table-column>
            <!-- 第4列 -->
            <el-table-column>
              <template slot-scope="scope">
                <div v-if="scope.$index === 2">
                  <img-upload :isShowImgOnly="isDetal" :fileId.sync="enterModel.avatarPath"></img-upload>
                </div>
                <div v-else-if="scope.$index === 3 && myDialogType(0)">
                  <el-form-item prop="password">
                    <el-input type="password" v-model="enterModel.password" :placeholder="$t('entPri.password')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4">
                  <el-form-item prop="contactPhone">
                    <el-input v-model="enterModel.contactPhone" maxLength="11" :placeholder="$t('entPri.phone')" :disabled="isDetal"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>
            <!-- 第5列 -->
            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 3 && myDialogType(0)" class="self-is-required"><span>{{ $t('entPri.repPas') }}</span></div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4" class="self-is-required"><span>{{ $t('entPri.email') }}</span></div>
              </template>
            </el-table-column>
            <!-- 第6列 -->
            <el-table-column align="center" >
              <template slot-scope="scope">
                <div v-if="scope.$index === 3 && myDialogType(0)">
                  <el-form-item prop="rePassword">
                    <el-input type="password" v-model="enterModel.rePassword" :placeholder="$t('entPri.repPas')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="!myDialogType(0) ? scope.$index === 3 : scope.$index === 4">
                  <el-form-item prop="email">
                    <el-input v-model="enterModel.email" :placeholder="$t('entPri.email')" :disabled="isDetal"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

          </el-table>
          <el-form-item class="tr-border" align="center" style="margin-top: 10px">
            <el-button  v-if="!isDetal" type="primary" @click="submitForm">{{ $t('sure')}}</el-button>
            <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
