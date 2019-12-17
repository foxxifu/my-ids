<template>
  <div id="exhibition" ref="exhibition" class="ids-module">
    <div :class="['top', 'center-shadow', showEarth ? '' : 'active']">
      <div class="top-left">
          <span
            class="total-power-label">{{$t('exhibition.totalPower')}}</span><span
        class="unit total-power-unit"><span>(</span>{{$t('unit.KWh')}}<span>)</span></span>
        <div ref="exhibition_totalPower" class="total-power-value" draggable="true">
          {{totalPower | fixedNumber(3) | formatNumber}}
        </div>
      </div>
      <div class="top-center">
        <h1 :title="$t('exhibition_systemName')" @click="goToHome">
          <!--<img src="/assets/images/exhibition/logo.png" alt="logo"/>-->
          <img :src="imgPath" class="logoImg"/>
          {{$t('exhibition_systemName')}}
        </h1>
      </div>
      <div class="top-right">
          <span
            class="safe-running-days-label">{{$t('exhibition.safeRunningDays')}}</span><span
        class="unit safe-running-days-unit"><!-- <span>(</span>{{$t('unit.days')}}<span>)</span> --></span>
        <div ref="exhibition_safeRunningDays" class="safe-running-days-value" draggable="true">
          {{safeRunningDays | fixedNumber(0) | formatNumber}}
        </div>
        <p ref="exhibition_currentTime" class="top-current-time">{{currentTime | timestampFomat($t('dateFormat.yyyymmddhhmmss'))}}</p>
      </div>
    </div>

    <div class="left">
      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>{{$t('exhibition.kpiModules.powerGeneration.title')}}</h1>
        </div>
        <div class="box-content clearfix">
          <e-chart id="kpi_powerGeneration" :options="kpi_powerGeneration_chart" style="height: 170px;"></e-chart>
          <ul class="powerGeneration-label">
            <li>{{$t('exhibition.kpiModules.powerGeneration.realTimePower')}}</li>
            <li>{{$t('exhibition.kpiModules.powerGeneration.dailyCharge')}}</li>
            <li>{{$t('exhibition.kpiModules.powerGeneration.todayIncome')}}</li>
          </ul>
        </div>
      </div>

      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>{{$t('exhibition.kpiModules.powerTrend.title')}}</h1>
        </div>
        <div class="box-content clearfix">
          <e-chart id="kpi_powerTrend" :options="kpi_powerTrend_chart" class="charts-box"></e-chart>
        </div>
      </div>

      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>
            {{isStationView ? $t('exhibition.kpiModules.taskStatistics.title') : $t('exhibition.kpiModules.powerScale.title')}}
          </h1>
          <div v-if="isStationView" class="more">{{$t('exhibition.kpiModules.taskStatistics.total')}}
            <span class="val">{{kpi_taskStatistics_total | fixedNumber(0) | formatNumber}}</span>
          </div>
        </div>
        <div class="box-content clearfix">
          <e-chart v-if="isStationView" id="kpi_taskStatistics" :options="kpi_taskStatistics_chart" class="charts-box"></e-chart>
          <e-chart v-else id="kpi_powerScale" :options="kpi_powerScale_chart" class="charts-box"></e-chart>
          <div v-if="!isStationView" ref="kpi_powerScale_total"
               :class="['kpi-powerScale-shadow', showEarth ? '' : 'active']">
            <div class="kpi-powerScale-label">
              <p>{{kpi_powerScale_total}}</p>
              <label>{{$t('exhibition.kpiModules.powerScale.totalStation')}}</label>
            </div>
          </div>
          <ul v-else class="kpi-taskStatistics-status">
            <li v-for="state in kpi_taskStatistics" :key="state[0]" :class="state[0] + '_s'">
              {{state[1]}}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="right">
      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>{{$t('exhibition.kpiModules.equipmentDistribution.title')}}</h1>
        </div>
        <div class="box-content clearfix">
          <e-chart id="kpi_equipmentDistribution" :options="kpi_equipmentDistribution_chart" class="charts-box"></e-chart>
          <div class="kpi-equipmentDistribution-content">
            <ul>
              <li class="pvCount">
                <p>{{(kpi_equipmentDistribution.pvCount || 0) | fixedNumber(0) | formatNumber}}</p>
                <i></i>
              </li>
              <li class="inverterStringCount">
                <p>{{(kpi_equipmentDistribution.inverterStringCount || 0) | fixedNumber(0) | formatNumber}}</p>
                <i></i>
              </li>
              <li class="inverterConcCount">
                <p>{{(kpi_equipmentDistribution.inverterConcCount || 0) | fixedNumber(0) | formatNumber}}</p>
                <i></i>
              </li>
              <li class="combinerBoxCount">
                <p>{{(kpi_equipmentDistribution.combinerBoxCount || 0) | fixedNumber(0) | formatNumber}}</p>
                <i></i>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>{{$t('exhibition.kpiModules.powerSupply.title')}}</h1>
          <div class="more">{{$t('exhibition.kpiModules.powerSupply.currentSupply')}}
            <span class="val">{{(kpi_powerSupply_current || 0) | fixedNumber(3) | formatNumber}}</span>
            <span>{{$t('unit.KW')}}</span>
          </div>
        </div>
        <div class="box-content clearfix">
          <e-chart id="kpi_powerSupply" :options="kpi_powerSupply_chart" class="charts-box"></e-chart>
        </div>
      </div>

      <div :class="['box-card', 'center-shadow', showEarth ? '' : 'active']">
        <div class="box-header clearfix">
          <h1>{{$t('exhibition.kpiModules.socialContribution.title')}}</h1>
        </div>
        <div class="box-content clearfix">
          <div id="kpi_socialContribution" ref="kpi_socialContribution">
            <el-row>
              <el-col :span="3">
                <div class="grid-content grid-title"></div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content grid-title">
                  <i class="social-contribution coal"></i>
                  <span>{{$t('exhibition.kpiModules.socialContribution.saveStandardCoal') + '(' + $t('unit.coalUnit') + ')'}}</span>
                </div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content grid-title">
                  <i class="social-contribution co2"></i>
                  <span>{{$t('exhibition.kpiModules.socialContribution.carbonDioxideReduction') + '(' + $t('unit.co2Unit') + ')'}}</span>
                </div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content grid-title">
                  <i class="social-contribution tree-planting"></i>
                  <span>{{$t('exhibition.kpiModules.socialContribution.reduceDeforestation')}}</span>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="3">
                <div class="grid-content">
                  <span>{{$t('exhibition.kpiModules.socialContribution.year')}}</span>
                </div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.coal | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.co2 | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.tree | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="3">
                <div class="grid-content">
                  <span>{{$t('exhibition.kpiModules.socialContribution.cumulative')}}</span>
                </div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.coalTotal | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.co2Total | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
              <el-col :span="7">
                <div class="grid-content"><span>{{contribution.treeTotal | fixedNumber(2) | formatNumber}}</span></div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>

    <div v-if="!showEarth" class="map-path">
      <ul ref="mapPathArea">
        <li class="map-path-item" @click="goEarth"><span>{{$t('exhibition.center.toEarth')}}</span></li>
        <li v-for="path in mapPathArea" :key="path.id + '_' + path.nodeType" class="map-path-item"
            @click="mapPathReview(path.id, path.nodeType)">
          <span>{{path.name}}</span>
        </li>
      </ul>
    </div>

    <div class="center">
      <div ref="exhibition_center_map" class="center-map">
        <keep-alive>
          <div v-if="showEarth" is="IDS_Cesium" ref="cesium" :locations="earthPoints" :jump="goMap"></div>
          <div v-else is="IDS_Map" ref="map" :center="mapCenter" :zoom="mapZoom" :mapType=1
               :clusterMarkers="mapClusterMarkers" :initViewBounds="mapAreaBounds"></div>
        </keep-alive>

      </div>
    </div>

    <div :class="['bottom', 'center-shadow', showEarth ? '' : 'active']">
      <el-row :gutter="20" :class="['bottom-content', showEarth ? '' : 'no-border']">
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_totalYearPower">
            <span class="label">{{$t('exhibition.bottomKpi.totalYearPower')}}</span>
            <span class="value">
              {{totalYearPower.value | fixedNumber(2) | formatNumber}}
            </span>
            <span class="unit">{{totalYearPower.unit}}</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_totalInstalledCapacity">
            <span class="label">{{$t('exhibition.bottomKpi.totalInstalledCapacity')}}</span>
            <span class="value">
              {{totalInstalledCapacity.value | fixedNumber(2) | formatNumber}}
            </span>
            <span class="unit">{{totalInstalledCapacity.unit}}</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_totalIncome">
            <span class="label">{{$t('exhibition.bottomKpi.totalIncome')}}</span>
            <span class="value">
              {{totalIncome.value | fixedNumber(2) | formatNumber}}
            </span>
            <span class="unit">{{totalIncome.unit}}</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_equipmentQuantity">
            <span class="label">{{$t('exhibition.bottomKpi.equipmentQuantity')}}</span>
            <span class="value">
              {{equipmentQuantity.value | fixedNumber(0) | formatNumber}}
            </span>
            <span class="unit"></span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_povertyAlleviation">
            <span class="label">{{$t('exhibition.bottomKpi.totalTask')}}</span>
            <span class="value">
              {{totalTask.value | fixedNumber(0) | formatNumber}}
            </span>
            <span class="unit"></span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="bottom-grid-content" ref="exhibition_untreatedTask">
            <span class="label">{{$t('exhibition.bottomKpi.untreatedTask')}}</span>
            <span class="value">
              {{untreatedTask.value | fixedNumber(0) | formatNumber}}
            </span>
            <span class="unit"></span>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script src="./index.js"></script>
<style lang="less">
  body {
    overflow: hidden;
  }
</style>
<style lang="less" src="./index.less" scoped></style>
