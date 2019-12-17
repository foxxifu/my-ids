<template>
  <div>
    <div v-if="showTabs.length>0">
      <el-tabs v-model="searchData.activeName" type="border-card" @tab-click="handleTabClick">
        <el-tab-pane v-if="showTabs.length>0" v-for="(item,index) in showTabs" :label="item.text" :name="item.name" :key="item.name">
          <el-table :data="list" border maxHeight="400">
            <el-table-column prop="status" :label="$t('components.platformComponent.status')" align="center">
            </el-table-column>
            <el-table-column prop="deviceName" :label="$t('components.chooseDevice.devName')" align="center">
            </el-table-column>
            <el-table-column :prop="showList[searchData.activeName][0].pro"
                             :label="showList[searchData.activeName][0].text"
                             align="center">
            </el-table-column>
            <el-table-column :prop="showList[searchData.activeName][1].pro"
                             :label="showList[searchData.activeName][1].text"
                             align="center">
            </el-table-column>
            <el-table-column prop="dealStatus" :label="$t('components.platformComponent.proState')" align="center">
            </el-table-column>
            <el-table-column prop="dealStatus" :label="$t('components.platformComponent.devDetail')" align="center">
              <template slot-scope="scope">
                <el-button @click="" type="text" size="small">{{ $t('components.platformComponent.devDetail') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <div style="overflow: hidden;margin-top: 10px;">
          <div style="float: right;right: 10px;">
            <el-pagination @size-change="searchDevList(true)" @current-change="searchDevList()"
                           :current-page="searchData.page" :page-sizes="[10, 20, 30, 50]"
                           :page-size="searchData.pageSize" layout="total, sizes, prev, pager, next, jumper"
                           :total="total">
            </el-pagination>
          </div>
        </div>

      </el-tabs>
    </div>
    <div v-else style="text-align: center;">
      {{ $t('components.platformComponent.noData') }}
    </div>
  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less'></style>
