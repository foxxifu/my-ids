<template>
  <el-container direction="vertical" class="app-wrapper">
    <el-header height="75px">
      <HeaderTop/>
    </el-header>

    <div class="body-container">
      <!-- 判断是否展示单电站列表的div -->
      <div v-if="$route.meta.showNav && $store.state.resources.station" class="body-side-nav">
        <ul>
          <li v-if="$route.fullPath.indexOf('/station/') >= 0">
            <router-link tag="a" to="/home">
              <i class="side-nav-icon icon-home"></i>
              <span>{{$t('layout.home')}}</span>
            </router-link>
          </li>
          <li @mouseover.stop="showStationList" @mouseout.stop="isShowStationList = false">
            <i class="side-nav-icon icon-stationList"></i>
            <span>{{$t('layout.title')}}</span>
            <transition name="el-list">
              <!-- 显示的电站列表 -->
              <div v-show="isShowStationList" class="station-list-content">
                <div class="station-search">
                  <el-input :placeholder="$t('layout.enterCon')" v-model="stationName">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                  </el-input>
                </div>
                <div class="list-station">
                  <ul>
                    <li v-for="station in stationFilter" :key="station.id" @click.stop="toStation(station)">
                        <i :class="['station-status', 'station-status-' + station.status]"></i>
                        <div style="display:inline-block;width: 80%;height: 100%;">
                          <div class="text-ellipsis" :title="station.stationName">
                            {{station.stationName}}
                          </div>
                        </div>
                    </li>
                  </ul>
                </div>
              </div>
            </transition>
          </li>
        </ul>
        <div id="page_main_station_list"></div>
      </div>

      <el-aside v-if="showSidebar" :width="$store.state.toggleSidebar ? '228px' : '52px'">
        <div
          :class="['toggle-side-btn', sideBarOpen === false ? 'collapse' : 'expand']"
          @click="toggleSideBar">
        </div>
        <SideBar class="sidebar-wrapper" :isSubMenu="isSubMenuShow" :menuList="settingsMenuList"/>
      </el-aside>

      <el-main class="main-container">
        <NavBar/>
        <ContentArea/>
      </el-main>
    </div>
  </el-container>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
