<template>
  <el-container>
    <el-header>
      <el-form ref="inverterDetailSearchForm" :model="inverterDetailSearchForm" :inline="true"
               style="" :size="elementSize">
        <el-form-item :label="$t('station.report.selectTime')">
          <el-date-picker v-model="inverterDetailSearchForm.dayTime" type="date" placeholder=""
                          :format="$t('dateFormat.yyyymmdd')"
                          style="width:140px"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('station.dev')">
          <el-select v-model="inverterDetailSearchForm.deviceIds" multiple collapse-tags style="width: 300px"
                     :placeholder="$t('station.choose')">
            <el-option v-for="item in devicesIds" :key="item.devId" :label="item.devName" :value="item.devId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('station.index')">
          <el-select v-model="inverterDetailSearchForm.kpis" multiple collapse-tags style="width: 300px"
                     :placeholder="$t('station.choose')">
            <el-option v-for="item in kpis" :key="item.v" :label="item.l" :value="item.v">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :size="elementSize" icon="el-icon-search" @click="searchInverterDetail">
            {{$t('search')}}
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-back" @click="getReturn">{{$t('station.back')}}</el-button>
        </el-form-item>

      </el-form>
    </el-header>
    <el-main v-loading="loading" style="position: relative">
      <el-button type="primary" style="position: absolute; z-index: 999; right: 20px; top: 220px;" @click="exportData">{{$t('station.report.export')}}</el-button>
      <div ref="inverterDetailChart" style="width:100%;height: 200px;"></div>
      <el-tabs v-model="inverterDetailTabName" @tab-click="" tab-position="top" style="height: 100%; width: 100%;">
        <el-tab-pane v-for="item in inverterDetails" :key="item.value" :label="item.label" :name="item.value">
          <el-table :data="item.list" :size="elementSize" border :max-height="maxHeight">

            <el-table-column prop="devName" :label="$t('station.equipment.devName')" align="center"></el-table-column>
            <el-table-column prop="collectTime" :label="$t('station.acpTime')" align="center" :formatter="collectTimeFormatter"></el-table-column>
            <el-table-column prop="dayCapacity" :label="$t('station.dayCapability')" align="center"></el-table-column>
            <el-table-column prop="totalCapacity" :label="$t('station.totalCapability')" align="center"></el-table-column>
            <el-table-column prop="activePower" :label="$t('station.actPow')" align="center"></el-table-column>
            <el-table-column prop="temperature" :label="$t('station.temperature')" align="center"></el-table-column>
            <el-table-column prop="gridFrequency" :label="$t('station.gridFre')" align="center"></el-table-column>


            <el-table-column prop="au" v-if="containsKpi('au')" :label="$t('station.aVoltageV')" align="center"></el-table-column>
            <el-table-column prop="bu" v-if="containsKpi('bu')" :label="$t('station.bVoltageV')" align="center"></el-table-column>
            <el-table-column prop="cu" v-if="containsKpi('cu')" :label="$t('station.cVoltageV')" align="center"></el-table-column>
            <el-table-column prop="ai" v-if="containsKpi('ai')" :label="$t('station.aEleA')" align="center"></el-table-column>
            <el-table-column prop="bi" v-if="containsKpi('bi')" :label="$t('station.bEleA')" align="center"></el-table-column>
            <el-table-column prop="ci" v-if="containsKpi('ci')" :label="$t('station.cEleA')" align="center"></el-table-column>

            <el-table-column prop="pv1u" v-if="containsKpi('pv1u')" :label="$t('station.voltage1V')" align="center"></el-table-column>
            <el-table-column prop="pv2u" v-if="containsKpi('pv2u')" :label="$t('station.voltage2V')" align="center"></el-table-column>
            <el-table-column prop="pv3u" v-if="containsKpi('pv3u')" :label="$t('station.voltage3V')" align="center"></el-table-column>
            <el-table-column prop="pv4u" v-if="containsKpi('pv4u')" :label="$t('station.voltage4V')" align="center"></el-table-column>
            <el-table-column prop="pv5u" v-if="containsKpi('pv5u')" :label="$t('station.voltage5V')" align="center"></el-table-column>
            <el-table-column prop="pv6u" v-if="containsKpi('pv6u')" :label="$t('station.voltage6V')" align="center"></el-table-column>
            <el-table-column prop="pv7u" v-if="containsKpi('pv7u')" :label="$t('station.voltage7V')" align="center"></el-table-column>
            <el-table-column prop="pv8u" v-if="containsKpi('pv8u')" :label="$t('station.voltage8V')" align="center"></el-table-column>
            <el-table-column prop="pv9u" v-if="containsKpi('pv9u')" :label="$t('station.voltage9V')" align="center"></el-table-column>
            <el-table-column prop="pv10u" v-if="containsKpi('pv10u')" :label="$t('station.voltage10V')" align="center"></el-table-column>
            <el-table-column prop="pv11u" v-if="containsKpi('pv11u')" :label="$t('station.voltage11V')" align="center"></el-table-column>
            <el-table-column prop="pv12u" v-if="containsKpi('pv12u')" :label="$t('station.voltage12V')" align="center"></el-table-column>
            <el-table-column prop="pv13u" v-if="containsKpi('pv13u')" :label="$t('station.voltage13V')" align="center"></el-table-column>
            <el-table-column prop="pv14u" v-if="containsKpi('pv14u')" :label="$t('station.voltage14V')" align="center"></el-table-column>

            <el-table-column prop="pv1i" v-if="containsKpi('pv1i')" :label="$t('station.ele1A')" align="center"></el-table-column>
            <el-table-column prop="pv2i" v-if="containsKpi('pv2i')" :label="$t('station.ele2A')" align="center"></el-table-column>
            <el-table-column prop="pv3i" v-if="containsKpi('pv3i')" :label="$t('station.ele3A')" align="center"></el-table-column>
            <el-table-column prop="pv4i" v-if="containsKpi('pv4i')" :label="$t('station.ele4A')" align="center"></el-table-column>
            <el-table-column prop="pv5i" v-if="containsKpi('pv5i')" :label="$t('station.ele5A')" align="center"></el-table-column>
            <el-table-column prop="pv6i" v-if="containsKpi('pv6i')" :label="$t('station.ele6A')" align="center"></el-table-column>
            <el-table-column prop="pv7i" v-if="containsKpi('pv7i')" :label="$t('station.ele7A')" align="center"></el-table-column>
            <el-table-column prop="pv8i" v-if="containsKpi('pv8i')" :label="$t('station.ele8A')" align="center"></el-table-column>
            <el-table-column prop="pv9i" v-if="containsKpi('pv9i')" :label="$t('station.ele9A')" align="center"></el-table-column>
            <el-table-column prop="pv10i" v-if="containsKpi('pv10i')" :label="$t('station.ele10A')" align="center"></el-table-column>
            <el-table-column prop="pv11i" v-if="containsKpi('pv11i')" :label="$t('station.ele11A')" align="center"></el-table-column>
            <el-table-column prop="pv12i" v-if="containsKpi('pv12i')" :label="$t('station.ele12A')" align="center"></el-table-column>
            <el-table-column prop="pv13i" v-if="containsKpi('pv13i')" :label="$t('station.ele13A')" align="center"></el-table-column>
            <el-table-column prop="pv14i" v-if="containsKpi('pv14i')" :label="$t('station.ele14A')" align="center"></el-table-column>

          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>

<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
