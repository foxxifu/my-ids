<template>
  <el-container class="systemParam">
    <el-tabs v-model="activeName" style="width: 100%">
      <el-tab-pane :label="$t('sysPar.systemParam')" name="first">
        <!--<el-form :model="systemForm" ref="systemForm" :rules="rules" label-width="100px" style="max-height:600px;overflow-y: auto;" status-icon>
          <el-form-item :label="$t('modules.main.menu.settings.systemManager.systemName')" prop="systemName">
            <el-col :span="24">
              <el-input v-model="systemForm.systemName" clearable></el-input>
            </el-col>
          </el-form-item>

          <el-form-item :label="$t('modules.main.menu.settings.systemManager.description')" prop="description">
            <el-col :span="24">
              <el-input v-model="systemForm.description" clearable></el-input>
            </el-col>
          </el-form-item>
          &lt;!&ndash;<el-form-item :label="$t('modules.main.menu.settings.systemManager.logo')" prop="logo">
            <el-col :span="24">
              &lt;!&ndash;<el-input v-model="systemForm.logo" clearable type="file"></el-input>&ndash;&gt;
              <div id="app">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :on-preview="handlePictureCardPreview" >
                  <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog :visible.sync="dialogVisible">
                  <img width="100%" v-lazy="dialogImageUrl" alt>
                </el-dialog>
              </div>
            </el-col>
          </el-form-item>&ndash;&gt;

          <el-form-item class="tr-border" align="center">
            <el-col :span="20">
              <el-form-item prop="tags">
                <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
              </el-form-item>
            </el-col>
          </el-form-item>
        </el-form>-->

        <el-form :model="systemForm" ref="systemForm" :rules="rules" class="systemForm">
          <el-table :data="[{},{},{},{}]" ref="systemTable" border :show-header="false" class="systemTable"
                    :cell-style="cellStyle">
            <el-table-column align="center" :width="150">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span v-text="$t('sysPar.systemName')"></span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span v-text="$t('sysPar.description')"></span></div>
                <div v-else-if="scope.$index === 2" class="self-is-required"><span v-text="$t('sysPar.version')"></span></div>
                <div v-else-if="scope.$index === 3" class="self-is-required"><span v-text="$t('sysPar.installDate')"></span></div>
              </template>
            </el-table-column>

            <el-table-column align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="systemName">
                    <el-input v-model="systemForm.systemName" clearable></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="description">
                    <el-input v-model="systemForm.description" clearable></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <span v-text="systemForm.version"></span>
                </div>
                <div v-else-if="scope.$index === 3">
                  {{convertDateFromString(systemForm.installDate)}}
                </div>
              </template>
            </el-table-column>
          </el-table>

          <el-form-item class="tr-border" align="center" style="margin-top: 10px">
            <el-button type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
          </el-form-item>

        </el-form>

      </el-tab-pane>
      <!--<el-tab-pane :label="$t('modules.main.menu.settings.systemManager.clearParam')" name="second">
        <el-button type="primary" @click="submitForm" style="float:right;margin-bottom: 5px;">{{ $t('modules.edit') }}</el-button>
        <el-table :data="tableData3" height="100%" border style="width: 100%;">
          <el-table-column prop="paramName" :label="$t('modules.main.menu.settings.systemManager.paramName')" width="100px;" align="center"></el-table-column>
          <el-table-column prop="paramValue" :label="$t('modules.main.menu.settings.systemManager.paramValue')" align="center"></el-table-column>
          <el-table-column prop="unit" :label="$t('modules.main.menu.settings.systemManager.unit')" align="center"></el-table-column>
          <el-table-column prop="updateName" :label="$t('modules.main.menu.settings.systemManager.updateName')" align="center"></el-table-column>
          <el-table-column prop="updateDate" :label="$t('modules.main.menu.settings.systemManager.updateDate')" align="center"></el-table-column>
          <el-table-column prop="paramDescription" :label="$t('modules.main.menu.settings.systemManager.paramDescription')" align="center"></el-table-column>
        </el-table>
      </el-tab-pane>-->
    </el-tabs>
  </el-container>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>
