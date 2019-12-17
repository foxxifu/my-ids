<template>
  <div class="role">
    <!-- 搜索栏 -->
    <div>
      <el-form :inline="true" :model="searchData" class="demo-form-inline" style="float: left;">
        <el-form-item :label="$t('rol.ower')">
          <el-select v-model="id" :placeholder="$t('rol.choose')" :props="enterpriseProps" @change = "changeEnterprise">
            <el-option
              v-for="item in enterprises"
              :key="item.name"
              :label="item.name"
              :value="item.id">
            </el-option>

          </el-select>
        </el-form-item>
        <el-form-item :label="$t('rol.roleName')">
          <el-input v-model="searchData.name" :placeholder="$t('rol.roleName')" clearable></el-input>
        </el-form-item>
        <el-form-item :label="$t('rol.createDate')">
          <el-date-picker
            v-model="searchData.startTime"
            type="date"
            :format="$t('dateFormat.yyyymmdd')"
            value-format="timestamp" :editable="false"
            :placeholder="$t('rol.startTime')">
          </el-date-picker> ~
          <el-date-picker
            v-model="searchData.endTime"
            type="date"
            :format="$t('dateFormat.yyyymmdd')"
            value-format="timestamp" :editable="false"
            :placeholder="$t('rol.endTime')">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getData">{{ $t('search') }}</el-button>
        </el-form-item>
      </el-form>
      <div style="float: right;">
        <el-button @click="addRole" type="primary">{{ $t('rol.add') }}</el-button>
      </div>
    </div>
    <!-- 表格数据 -->
    <div class="list-table">
      <el-table :data="list" border>
        <el-table-column
          prop="name"
          :label="$t('rol.roleName')" align="center">
        </el-table-column>
        <el-table-column prop="description" :label="$t('rol.description')"  align="center">
        </el-table-column>
        <el-table-column :label="$t('rol.isUse')" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" @change="statusChange(scope.row)" active-color="#13ce66" inactive-color="#aaa" :active-value="0" :inactive-value="1">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column :label="$t('rol.createDate')" align="center">
          <template slot-scope="scope">
            <div>{{ scope.row.createDate | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('rol.operate')" align="center">
          <template slot-scope="scope" v-if="scope.row.id !== 1 && scope.row.id !== 2">
            <el-button type="text" @click.prevent="editRole(scope.row.id)">{{ $t('rol.edit') }}
            </el-button>
            <el-button type="text" @click="deleteRoleById(scope.row.id)">{{ $t('rol.del') }}
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
    </div>
    <!-- 弹出框 -->
    <el-dialog :title="getDialogTitle" :visible.sync="dialogVisible" class="dialog" width="700px" :before-close="closeDialog" :close-on-click-modal="false">
      <!--<el-form :model="roleModel" ref="roleForm" :rules="rules" label-width="100px" style="max-height:580px;overflow-y: auto;" status-icon>
        <el-form-item :label="$t('modules.main.menu.settings.personRightsManager.role.roleName')" prop="name">
          <el-col :span="18">
            <el-input v-model="roleModel.name" clearable :disabled="isDetal"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item :label="$t('modules.main.menu.settings.personRightsManager.role.isUse')" prop="status">
          <el-switch v-model="roleModel.status" active-color="#13ce66" inactive-color="#aaa" :active-value="0" :inactive-value="1"></el-switch>
        </el-form-item>
        <el-form-item :label="$t('modules.main.menu.settings.personRightsManager.role.description')" prop="description">
          <el-col :span="18">
            <el-input type="textarea" v-model="roleModel.description" :rows="5" clearable :disabled="isDetal"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item :label="$t('modules.main.menu.settings.personRightsManager.role.roleAuth')" prop="authIds">
        <el-checkbox-group v-model="authCheckArr">
          <el-checkbox v-for="auth in userAuths" :label="auth.id+''" :key="auth.id" type="authIds" :disabled="isDetal">
            {{ auth.auth_name }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
        <el-form-item :label="$t('modules.main.menu.settings.personRightsManager.role.appAuth')" prop="authIds">
          <el-checkbox-group v-model="authCheckArr">
            <el-checkbox v-for="auth in appAuths" :label="auth.id+''" :key="auth.id" type="authIds" :disabled="isDetal">
              {{ auth.auth_name }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>-->

      <el-form :model="roleModel" ref="roleForm" :rules="rules" style="max-height:590px;overflow-y: auto;" status-icon>
        <el-table :data="[{},{},{},{},{}]" ref="roleModelTableData" border :show-header="false"
                  :highlight-current-row="false" class="roleModelTableData"
                  :cell-style="cellStyle">

          <el-table-column prop="kpiName1" :label="$t('rol.kpiName')" align="center" :width="110">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" class="self-is-required"><span>{{ $t('rol.roleName') }}</span></div>
              <div v-else-if="scope.$index === 1"><span>{{ $t('rol.isUse') }}</span></div>
              <div v-else-if="scope.$index === 2"><span>{{ $t('rol.description') }}</span></div>
              <div v-else-if="scope.$index === 3"><span>{{ $t('rol.roleAuth') }}</span></div>
              <div v-else-if="scope.$index === 4"><span>{{ $t('rol.appAuth') }}</span></div>
            </template>
          </el-table-column>

          <el-table-column prop="kpiName1" :label="$t('rol.kpiName')" align="left">
            <template slot-scope="scope">
              <div v-if="scope.$index === 0" >
                <el-form-item prop="name">
                  <el-input v-model="roleModel.name" clearable :disabled="isDetal"></el-input>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 1">
                <el-form-item prop="status">
                  <el-switch v-model="roleModel.status" active-color="#13ce66" inactive-color="#aaa" :active-value="0" :inactive-value="1"></el-switch>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 2">
                <el-form-item prop="description">
                  <el-input type="textarea" v-model="roleModel.description" :rows="5" clearable :disabled="isDetal"></el-input>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 3">
                <el-form-item prop="authIds">
                  <el-checkbox-group v-model="authCheckArr">
                    <el-checkbox v-for="auth in userAuths" :label="auth.id+''" :key="auth.id" type="authIds" :disabled="isDetal">
                      {{ $t('pageRole.' + auth.id) }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </div>
              <div v-else-if="scope.$index === 4">
                <el-form-item prop="authIds">
                  <el-checkbox-group v-model="authCheckArr">
                    <el-checkbox v-for="auth in appAuths" :label="auth.id+''" :key="auth.id" type="authIds" :disabled="isDetal">
                      {{ $t('pageRole.' + auth.id) }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </div>
            </template>
          </el-table-column>

        </el-table>
      </el-form>
      <div class="dialog-footer" style="text-align: center;padding-top: 10px;">
          <el-button  v-if="!isDetal" type="primary" @click="submitForm">{{ $t('sure') }}</el-button>
          <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
        </div>
    </el-dialog>
  </div>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>

