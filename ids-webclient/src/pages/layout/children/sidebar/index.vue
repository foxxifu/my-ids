<template>
  <el-menu mode="vertical" router unique-opened :default-active="$route.path"
           :collapse="!this.$store.state.toggleSidebar"
           background-color="#196d9a" text-color="#FFFFFF" active-text-color="#00F6FF">
    <template v-if="subMenuState">
      <template v-for="item in menus"
                v-if="!item.hidden && item.children.length">
        <el-submenu v-if='!item.noDropdown' :index="item.name">
          <template slot="title">
            <i class="el-icon-nav" :class='item.icon'></i>
            <span slot="title">{{$t(item.name)}}</span>
          </template>
          <template v-for="child in item.children" v-if='!child.hidden'>
            <el-menu-item :index="item.path + '/' + child.path">
              <span slot="title">{{$t(child.name)}}</span>
            </el-menu-item>
          </template>
        </el-submenu>
        <el-menu-item v-else
                      :route="{path: item.path !== '/' ? (item.path + '/' + item.children[0].path) : (item.path + item.children[0].path)}"
                      :index="item.path !== '/' ? (item.path + '/' + item.children[0].path) : (item.path + item.children[0].path)">
          <i class='el-icon-nav' :class='item.icon'></i>
          <span slot="title">{{$t(item.name)}}</span>
        </el-menu-item>
      </template>
    </template>
    <template v-else>
      <template v-for="item in menus" v-if="!item.hidden">
        <el-menu-item :route="{path: item.path}" :index="item.path">
          <i class='el-icon-nav' :class='item.icon'></i>
          <span slot="title">{{$t('modules.main.menu.' + item.name)}}</span>
        </el-menu-item>
      </template>
    </template>
  </el-menu>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
<style lang="less">
  .el-menu--popup {
    min-width: 120px;
    padding: 0;
    margin: 0;
    background-color: #00a0e9 !important;
    border: 1px solid #095a7d;
    border-radius: 0;

    .el-menu-item {
      height: 50px;
      line-height: 50px;

      &:hover, &:focus {
        border-left: 3px solid transparent;
        background-color: transparent !important;
      }

      &.is-active {
        color: #00F6FF;
        background-color: #116096 !important;
        border-left: none !important;
        i {
          background-position-x: -30px;
        }

        &:hover, &:focus {
          background-color: #116096 !important;
        }
      }
    }
  }
</style>
