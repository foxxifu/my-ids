<template>
  <div>
    <div>
      <el-row :gutter="10">
        <el-col :span="5">
          <el-input disabled :value="fileName" :placeholder="$t('licMan.choose_file')"></el-input>
        </el-col>
        <el-col :span="2">
          <fileUpload style="padding-top: 5px;"
            uploadUrl="/biz/license/licenseImport"
            @on-validate-success="setInputText"
            :isShowTip="false"
            @on-upload-success="importSuccess()"
            @on-upload-error="importFailed()"></fileUpload>
        </el-col>
      </el-row>
    </div>
    <!-- License 信息 -->
    <div style="margin-top: 20px">
      <!--<el-form :model="licenseInfo" label-width="150px" label-position="left">
        <el-form-item class="bottom-border tr-border" label="导入时间">
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.importDate" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="导入用户" >
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.importUser" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="可接入容量">
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.totalCapacity" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="已接入容量">
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.installedCapacity" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="可接入逆变器数" >
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.totalDevNum" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="已接入逆变器数" >
          <el-col :span="8">
            <el-input type="text" v-model="licenseInfo.installedDevNum" disabled></el-input>
          </el-col>
        </el-form-item>
      </el-form>-->

      <el-table :data="[{},{},{},{},{},{}]" border :show-header="false"
                :cell-style="cellStyle">
        <el-table-column prop="kpiName1" :label="$t('licMan.kpiName')" align="center" :width="300">
          <template slot-scope="scope">
            <!--// 导入时间 -->
            <div v-if="scope.$index === 0"><span>{{$t('licMan.impTime')}}</span></div>
            <!--导入用户 -->
            <div v-else-if="scope.$index === 1"><span>{{$t('licMan.impUser')}}</span></div>
            <!--可接入容量(kW) -->
            <div v-else-if="scope.$index === 2"><span>{{$t('licMan.accCap')}}</span></div>
            <!--已接入容量(kW)) -->
            <div v-else-if="scope.$index === 3"><span>{{$t('licMan.acceCap')}}</span></div>
            <!--可接入逆变器数(台) -->
            <div v-else-if="scope.$index === 4"><span>{{$t('licMan.accInv')}}</span></div>
            <!--已接入逆变器数(台) -->
            <div v-else-if="scope.$index === 5"><span>{{$t('licMan.invCom')}}</span></div>
          </template>
        </el-table-column>

        <el-table-column prop="kpiValue1" :label="$t('licMan.kpiVal')" align="left">
          <template slot-scope="scope">
            <div v-if="scope.$index === 0">
              <span>{{licenseInfo.importDate}}</span>
            </div>
            <div v-if="scope.$index === 1">
              <span>{{licenseInfo.importUser}}</span>
            </div>
            <div v-if="scope.$index === 2">
              <span>{{licenseInfo.totalCapacity}}</span>
            </div>
            <div v-if="scope.$index === 3">
              <span>{{licenseInfo.installedCapacity}}</span>
            </div>
            <div v-if="scope.$index === 4">
              <span>{{licenseInfo.totalDevNum}}</span>
            </div>
            <div v-if="scope.$index === 5">
              <span>{{licenseInfo.installedDevNum}}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script src="./index.js"></script>
