<template>
  <div>
    <el-row class="header" :gutter="15">
      <el-col :span="8" :xs="20" :sm="9" :md="6" :lg="7" :xl="8">
        <div class="grid-content">
          <figure id="header_logo" class="header-logo">
            <!--<img class="logo" src="/assets/images/logo.png" :alt="$t('systemName')" :title="$t('systemName')"/>-->
            <div class="logoBox" :title="$t('systemName')">
              <img :src="imgPath" class="logoImg"/>
              <span class="logoTxt logoTopTxt">{{ $t('systemName') }}</span>
            </div>
          </figure>
        </div>
      </el-col>
      <el-col :span="8" :xs="0" :sm="2" :md="10" :lg="10" :xl="10">
        <div class="grid-content">
          <!-- 导航栏的信息 -->
          <el-menu v-if="!isStationMenuShow" mode="horizontal" :router="true" :default-active="$route.path"
                   :unique-opened="true"
                   background-color="transparent" text-color="#FFFFFF" active-text-color="#00F6FF">
            <template v-for="item in permissionRoutes"
                      v-if="!item.hidden && item.type === 'menu' && item.children.length">
              <el-menu-item :route="{path: item.path}"
                            :index="item.path === '/production' ? item.path + '/reportManage' : item.path + '/index'">
                <i class='el-icon-nav' :class='item.icon'></i>
                <!--<span slot="title">{{$t('modules.main.menu.' + item.name)}}</span>-->
                <span slot="title">{{$t(item.name)}}</span>
              </el-menu-item>
            </template>
          </el-menu>
          <!-- 单站的导航信息 -->
          <el-menu v-else mode="horizontal" :router="true" :default-active="$route.path" :unique-opened="true"
                   background-color="transparent" text-color="#FFFFFF" active-text-color="#00F6FF">
            <template v-for="i in permissionRoutes"
                      v-if="!i.hidden && i.type === 'stationMenu' && i.children.length">
              <template v-for="item in i.children">
                <el-menu-item
                  :route="{path: item.path}"
                  :index="item.path">
                  <i class='el-icon-nav' :class='item.icon'></i>
                  <!--<span slot="title">{{$t('modules.main.menu.' + item.name)}}</span>-->
                  <span slot="title">{{$t(item.name)}}</span>
                </el-menu-item>
              </template>
            </template>
          </el-menu>
        </div>
      </el-col>
      <el-col :span="8" :xs="4" :sm="13" :md="9" :lg="9" :xl="6">
        <div class="header-right grid-content">
          <!-- 个人任务 -->
          <router-link v-if="!isStationMenuShow && $store.state.resources.todo" class="item inlineBlock" to="/todo">
            <el-badge :value="todoNumber" :max="99" :hidden="!todoNumber">
              <i class="header-icon header-icon-todo"></i>
            </el-badge>
            {{ $t('menu.todo') }}
          </router-link>
          <!-- 大屏 -->
          <router-link v-if="!isStationMenuShow && $store.state.resources.exhibition" class="item inlineBlock"
                       to="/exhibition">
            <i class="header-icon header-icon-switch"></i>
            <span>{{ $t('menu.exhibition') }}</span>
          </router-link>
          <!-- 登录用户信息 -->
          <el-dropdown :class="['avatar-container', 'item', 'inlineBlock', !isStationMenuShow && 'noSingle']"
                       trigger="click">
            <div class="dropdown-wrapper">
              <!--<img class="dropdown-handler" :src="user ? user.avatar : '/assets/images/layout/user_avatar.png'">-->
              <i class="header-icon header-icon-user"></i>
              <!--<span class="dropdown-text">{{user ? user.loginName : '-'}}</span>-->
              <span>{{user ? user.loginName : 'admin'}}</span>
              <i class="el-icon-caret-bottom"></i>
            </div>
            <el-dropdown-menu class="user-dropdown" slot="dropdown">
              <el-dropdown-item>
                <ul class="box-content">
                  <li>
                    <span class="item-title">{{$t('hea.username')}}</span>
                    <span class="item-value cm-strong">{{user && user.loginName ? user.loginName : '-'}}</span>
                  </li>
                  <li>
                    <span class="item-title">{{$t('hea.userRole')}}</span>
                    <span class="item-value">
                      <ul v-if="user && user.roles && user.roles.length > 0">
                        <li v-for="role in user.roles" :key="role.id">
                           {{role.name || '-'}}
                        </li>
                      </ul>
                    </span>
                  </li>
                  <li>
                    <span class="item-title">{{$t('hea.company')}}</span>
                    <span
                      class="item-value">{{user && user.company ? user.company : '-'}}</span>
                  </li>
                  <li>
                    <span class="item-title">{{$t('hea.address')}}</span>
                    <span class="item-value">{{user && user.address ? user.address : '-'}}</span>
                  </li>
                </ul>
              </el-dropdown-item>
              <el-dropdown-item divided>
                <button class="left" @click="modifyPassword">{{$t('hea.modifyPassword')}}</button>
                <button class="right" @click="logout">{{$t('hea.cancellation')}}</button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!-- 设置菜单信息 -->
          <el-dropdown v-if="!isStationMenuShow && $store.state.resources.settings"
                       class="settings-container item inlineBlock" :hide-on-click="false"
                       trigger="hover">
            <div class="dropdown-wrapper">
              <i class="dropdown-handler icon-more"></i>
            </div>
            <el-dropdown-menu class="settings-dropdown" slot="dropdown">
              <router-link class="inlineBlock" :to="settingRouter">
                <el-dropdown-item class="settings-menu">{{$t('menu.settings.title')}}</el-dropdown-item>
              </router-link>
              <el-dropdown-item>
                <!-- 语言切换的下拉框 -->
                <el-select v-model="langeVal" style="width: 100px;background: #196d9a;height: 30px;" :placeholder="$t('hea.choose')" @change="changeLange">
                  <el-option :label="$t('hea.zh')" value="zh">
                  </el-option>
                  <el-option label="English" value="en">
                  </el-option>
                </el-select>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-col>
    </el-row>

    <!-- 修改密码弹出框 -->
    <el-dialog width="30vw" :title="$t('hea.modifyPassword')"
               :visible.sync="modifyPasswordDialogVisible" :before-close="modifyPasswordClose"
               append-to-body>
      <el-form :model="modifyPasswordRuleForm" status-icon :rules="modifyPasswordRules" ref="modifyPasswordRuleForm"
               label-width="130px" class="demo-ruleForm">
        <el-form-item :label="$t('hea.currentPas')" prop="oldPassword">
          <el-input type="password" v-model="modifyPasswordRuleForm.oldPassword" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item :label="$t('hea.newPassword')" prop="password">
          <el-input type="password" v-model="modifyPasswordRuleForm.password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item :label="$t('hea.confirmPas')" prop="checkPassword">
          <el-input type="password" v-model="modifyPasswordRuleForm.checkPassword" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button type="info" @click="modifyPasswordDialogVisible = false">{{$t('cancel')}}</el-button>
        <el-button type="warning" @click="modifyPasswordResetForm">{{$t('hea.reset')}}</el-button>
        <el-button type="primary" @click="modifyPasswordSubmitForm">{{$t('hea.sub')}}</el-button>
      </span>
    </el-dialog>

  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
