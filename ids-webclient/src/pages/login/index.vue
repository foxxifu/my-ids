<template>
  <div class='login'>
    <div class="login-top"></div>
    <div class="login-main">
      <div class="logo">
        <!--<img src="/assets/images/login/logo.png" alt="logo"/>-->
        <span v-text="$t('systemName')"></span>
      </div>
      <el-form class='login-form' :model="loginForm" :rules="rules" ref="loginForm">
        <div class="form-item-content">
          <el-form-item prop="username">
            <label class="userIcon" for="username">{{$t('login.userName')}}</label>
            <el-input id="username" type="text" v-model="loginForm.username" auto-focus auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <label class="pwdIcon" for="password">{{$t('login.password')}}</label>
            <el-input id="password" type="password" v-model="loginForm.password" auto-complete="off"
                      @keyup.enter.native="submitLogin('loginForm')"></el-input>
          </el-form-item>
        </div>

        <div class="login-buttons">
          <el-button type="primary" class="login-btn" @click="submitLogin('loginForm')">
            {{ $t('login.doLogin') }}
          </el-button>
        </div>

        <div class="login-form-bottom"></div>
      </el-form>
    </div>
    <div class="login-bottom">
      <p class="inner">
        <span v-text="$t('copyright')"></span>
        <router-link :to="{path: '/about'}">{{$t('about')}}</router-link>
      </p>
    </div>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
