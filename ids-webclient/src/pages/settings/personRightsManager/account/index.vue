<template>
  <tree-nav @node-click="getUserDatas" :props="defaultProps" :lazySearchFun="getDepartmentData" :titleText="$t('account.stuEnter')">
    <div class="account">
      <div>
        <el-form ref="form" :model="searchBarData" :inline="true" style="float: left">
          <el-form-item :label="$t('account.accountName')">
            <el-input v-model="searchBarData.loginName"></el-input>
          </el-form-item>
          <el-form-item :label="$t('account.conInf')">
            <el-input v-model="searchBarData.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getUserDatas()">{{ $t('search') }}</el-button>
          </el-form-item>
        </el-form>
        <el-button @click="createUser()" type="primary" style="float: right;margin-right: 12px;">{{ $t('account.add') }}</el-button>
      </div>
      <el-table :data="list" border style="width: 100%;">
        <el-table-column prop="loginName" :label="$t('account.loginName')" width="100px;" align="center"></el-table-column>
        <el-table-column prop="userName" :label="$t('account.userName')" align="center"></el-table-column>
        <el-table-column prop="enterprise" :label="$t('account.enterprise')" align="center"></el-table-column>
        <el-table-column prop="department" :label="$t('account.department')" align="center"></el-table-column>
        <el-table-column prop="phone" :label="$t('account.phone')" align="center"></el-table-column>
        <el-table-column prop="roleName" :label="$t('account.roleName')" align="center">
          <template slot-scope="scope">
            <div style="color:#63cc90">{{scope.row.roleName}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('account.status')" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" @change="statusChange(scope.row,scope.row.type_)" active-color="#13ce66" inactive-color="#aaa" :active-value="0" :inactive-value="1">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="operation" :label="$t('account.op')" align="center">
          <template slot-scope="scope">
            <el-button @click="getDetail(scope.row)" type="text" size="small">{{$t('detail') }}</el-button>
            <el-button v-if="scope.row.type_ !== 'enterprise'" @click="getUserForupdate(scope.row)" type="text" size="small">{{$t('update') }}</el-button>
            <el-button type="text" size="small" @click="deleteUser(scope.row.id)">{{$t('delete') }}</el-button>
            <el-button type="text" size="small" v-if="isCanResetPassword" @click="showChangePassword(scope.row.id)">{{$t('account.pasReset') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="float: right;margin-top:5px" :page-sizes="[10, 20, 30, 50]" @size-change="pageSizeChange" @current-change="pageChange" :current-page="searchBarData.index" :page-size="searchBarData.pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="total"></el-pagination>
      <!-- 查看权限弹出框 -->
      <el-dialog :visible.sync="dialogVisible" class="dialog" width="500px" :before-close="closeDialogVisible" :close-on-click-modal="false">
        <el-form :model="roleModel" ref="roleForm" :rules="rules" label-width="100px" style="max-height:470px;overflow-y: auto;" status-icon>
          <el-form-item :label="$t('account.authorizeName')" prop="authIds">
            <el-checkbox-group>
              <el-checkbox v-for="auth in userAuths" :label="auth.id+''" :key="auth.id" type="authIds">
                {{ auth.auth_name }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ $t('close') }}</el-button>
        </span>
      </el-dialog>

      <!--新建和修改弹框 -->
      <el-dialog :title="title" v-if="createdialogVisible" :visible.sync="createdialogVisible" class="dialog" width="50%" :before-close="closeDialog" :close-on-click-modal="false">
        <el-form :model="userModel" ref="userForm" :rules="rules" status-icon :disabled="isDetail" >
          <div style="font-weight: bold">{{$t('account.essInf') }}</div>
          <el-table :data="[{},{},{},{}]" ref="baseInfoTableData" border :show-header="false"
                    :highlight-current-row="false" class="baseInfoTableData"
                    :cell-style="cellStyle">

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="center" :width="120">
              <template slot-scope="scope">
                <!--姓名-->
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('account.userName') }}</span></div>
                <!--企业-->
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{$t('account.enter') }}</span></div>
                <!--联系电话-->
                <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{$t('account.phone') }}</span></div>
                <!--帐号状态-->
                <div v-else-if="scope.$index === 3" class="self-is-required"><span>{{$t('account.status') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" >
                  <el-form-item prop="userName">
                    <el-input type="text" v-model="userModel.userName" :placeholder="$t('account.userName')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="enterpriseName">
                    <el-input type="text" @focus="showEnterprise" v-model="userModel.enterpriseName" :placeholder="$t('account.enterprise')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <el-form-item prop="phone">
                    <el-input type="text" v-model="userModel.phone" maxLength="11" :placeholder="$t('account.phone')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 3">
                  <el-form-item prop="status">
                    <el-switch v-model="userModel.status" active-color="#13ce66" inactive-color="#aaa" :active-value="0" :inactive-value="1">
                    </el-switch>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="center" :width="110">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('account.gender') }}</span></div>
                <div v-else-if="scope.$index === 1"><span>{{$t('account.department') }}</span></div>
                <div v-else-if="scope.$index === 2" class="self-is-required"><span>{{$t('account.email') }}</span></div>
                <div v-else-if="scope.$index === 3"><span>{{$t('account.userStation') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="gender">
                    <el-radio v-model="userModel.gender" label="1">{{$t('account.man') }}</el-radio>
                    <el-radio v-model="userModel.gender" label="2">{{$t('account.famale') }}</el-radio>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 1">
                  <el-form-item prop="department">
                    <el-input type="text" v-model="userModel.department" :placeholder="$t('account.department')" disabled></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 2">
                  <el-form-item prop="email">
                    <el-input type="text" v-model="userModel.email" :placeholder="$t('account.email')"></el-input>
                  </el-form-item>
                </div>
                <div v-else-if="scope.$index === 3" >
                  <el-form-item prop="stationCodes">
                    <StationInput
                      :myValues="userModel.stationCodes"
                      :myTexts="userModel.stationNames"
                      @station-change="stationChange"
                      :enterpriseId="!!userModel.enterpriseId ? userModel.enterpriseId : searchBarData.enterpriseId"
                      isMutiSelect></StationInput>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

          </el-table>
          <div style="font-weight: bold">{{$t('account.accountInf') }}</div>
          <el-table :data="userInfoArr" ref="countTableData" border :show-header="false"
                    :highlight-current-row="false" class="countTableData"
                    :cell-style="cellStyle">

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="center" :width="110">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('account.loginName') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{$t('account.password') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" >
                  <el-form-item prop="loginName" v-if="isShowPassword">
                    <el-input type="text" v-model="userModel.loginName" :placeholder="$t('account.loginName')"></el-input>
                  </el-form-item>
                  <div v-else>
                    {{userModel.loginName}}
                  </div>
                </div>
                <div v-else-if="isShowPassword && scope.$index === 1">
                  <el-form-item prop="password">
                    <el-input type="password" v-model="userModel.password" :placeholder="$t('account.password')"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="center" :width="110">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0" class="self-is-required"><span>{{$t('account.userType') }}</span></div>
                <div v-else-if="scope.$index === 1" class="self-is-required"><span>{{$t('account.repassword1') }}</span></div>
              </template>
            </el-table-column>

            <el-table-column prop="kpiName1" :label="$t('account.kpiName')" align="left">
              <template slot-scope="scope">
                <div v-if="scope.$index === 0">
                  <el-form-item prop="userType">
                    <el-select v-model="userModel.userType">
                      <el-option  v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div v-else-if="isShowPassword && scope.$index === 1">
                  <el-form-item prop="repassword">
                    <el-input type="password" v-model="userModel.repassword" :placeholder="$t('account.repassword')"></el-input>
                  </el-form-item>
                </div>
              </template>
            </el-table-column>

          </el-table>
          <div style="font-weight: bold">{{$t('account.roleInf') }}</div>
          <el-table ref="roleList" border :data="roleList" tooltip-effect="dark" @selection-change="changeRole" style="margin-bottom: 10px;">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column :label="$t('account.roleName')" width="300" prop="name"></el-table-column>
            <el-table-column prop="description" :label="$t('account.roleDes')"></el-table-column>
          </el-table>

          <el-form-item align="center">
            <el-col :span="24">
              <el-form-item prop="tags">
                <el-button type="primary" @click="submitUpdateForm" v-if="title === $t('account.edit')">{{ $t('sure') }}</el-button>
                <el-button type="primary" @click="submitForm" v-if="title === $t('account.newUser')">{{ $t('sure') }}</el-button>
                <el-button @click="closeDialog">{{ $t('cancel') }}</el-button>
              </el-form-item>
            </el-col>
          </el-form-item>
        </el-form>
      </el-dialog>

      <!-- 弹出企业和部门供用户选择 -->
      <el-dialog v-if="departmentdialogVisible" :title="$t('account.selectDep')" :visible.sync="departmentdialogVisible" class="dialog" width="500px" :before-close="closedepartmentDialogVisible" append-to-body :close-on-click-modal="false">
        <div style="margin-right: 15px;">
          <el-tree ref="tree" :data="departmentData" highlight-current :lazy="true" :load="getDepartmentData" :props="defaultProps" @node-click="getDepartmentData"></el-tree>
        </div>
        <span slot="footer" class="dialog-footer">
           <el-button type="primary" @click="showDepartment">{{ $t('sure') }}</el-button>
          <el-button @click="closedepartmentDialogVisible">{{ $t('close') }}</el-button>
        </span>
      </el-dialog>
      <!-- 修改密码弹出框 -->
      <el-dialog width="30vw" :title="$t('account.modifyPassword')"
                 :visible.sync="modifyPasswordDialogVisible" :before-close="modifyPasswordClose"
                 append-to-body>
        <el-form :model="modifyPasswordRuleForm" status-icon :rules="modifyPasswordRules" ref="modifyPasswordRuleForm"
                 label-width="120px" class="demo-ruleForm">
          <el-form-item :label="$t('account.loginPas')" prop="loginUserPassword">
            <el-input type="password" v-model="modifyPasswordRuleForm.loginUserPassword" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('account.newPas')" prop="password">
            <el-input type="password" v-model="modifyPasswordRuleForm.password" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('account.conPas')" prop="checkPassword">
            <el-input type="password" v-model="modifyPasswordRuleForm.checkPassword" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button type="info" @click="modifyPasswordDialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="modifyPasswordSubmitForm">{{ $t('account.sub') }}</el-button>
      </span>
      </el-dialog>
    </div>
  </tree-nav>
</template>
<script src="./index.js"></script>
<style lang='less' src='./index.less' scoped></style>

