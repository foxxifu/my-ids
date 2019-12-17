<template>
<div class='home'>
  <el-row>
    <el-col :span="7">
      <div ref="powerChart" style="width: 100%;height: 135px;min-width: 240px;"></div>
      <div style="margin-left:15%;width: 70%;font-size: 16px;color: #4f6271">
        <div style="float: left;width: 50%;">
          <div style="margin-top: -10px;width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.powerChart.value}}
          </div>
          <div style="margin-top:10px;text-align: center;width: 100%;">
            {{$t('station.realTimePower')}}({{gaugeChartDatas.powerChart.unit}})
          </div>
        </div>
        <div style="float: left;width: 50%;text-align: center;font-size: 16px;color: #4f6271">
          <div style="margin-top: -10px;width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.powerChart.cap}}
          </div>
          <div style="margin-top:10px;text-align: center;width: 100%;">
            {{$t("station.installedCapactiy")}}({{gaugeChartDatas.powerChart.capUnit}})
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="7">
      <div style="width: 70%; margin: 0 auto;min-width: 240px;">
        <!-- 日发电量和年发电量 -->
        <div style="float: left;width: 50%;font-size: 16px;color: #4f6271">
          <!--日发电量的图片 -->
          <div class="dayPowerChart"></div>
          <div style="width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.dayAndYearPowerChart.day}}
          </div>
          <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;">
            {{$t("station.dayCap")}}({{gaugeChartDatas.dayAndYearPowerChart.unitDay}})
          </div>
        </div>
        <div style="float: left;width: 50%;font-size: 16px;color: #4f6271">
          <!-- 年发电量的图片 -->
          <div class="yearPowerChart"></div>
          <div style="width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.dayAndYearPowerChart.year}}
          </div>
          <div style="margin-top: 10px;width: 100%;text-align: center;">
            {{$t("station.yearCap")}}({{gaugeChartDatas.dayAndYearPowerChart.unitYear}})
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="7">
      <!-- 日收益和年收益 -->
      <div style="width: 70%; margin: 0 auto;min-width: 240px;">
        <div style="float:left;width:50%;font-size: 16px;color: #4f6271">
          <!--日收益的图片 -->
          <div class="dayIncomeChart"></div>
          <div style="width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.incommonDayAndYearChart.day}}
          </div>
          <div style="margin-top: 10px;width: 100%;text-align: center;">
            {{$t("station.dayIncome")}}({{gaugeChartDatas.incommonDayAndYearChart.dayUnit}})
          </div>
        </div>
        <div style="float:left;width:50%;">
          <!-- 年收益的图片 -->
          <div class="yearIncomeChart"></div>
          <div style="width: 100%;text-align: center;" class="show-text-style">
            {{gaugeChartDatas.incommonDayAndYearChart.year}}
          </div>
          <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;color: #4f6271">
            {{$t("station.yearIncome")}}({{gaugeChartDatas.incommonDayAndYearChart.yearUnit}})
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="3">
      <!-- 累计发电量 -->
      <div ref="totalPowerChart" class="totalPowerChart">
      </div>
      <div style="width: 100%;text-align: center;" class="show-text-style">
        {{gaugeChartDatas.totalPowerChart.value}}
      </div>
      <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;color: #4f6271" class="show-title-style">
        {{$t('station.totalPower')}}({{gaugeChartDatas.totalPowerChart.unit}})
      </div>
    </el-col>
  </el-row>

  <el-row style="margin-top: 10px" class="content-show-row">
    <el-col :span="7" style="margin-top: 10px">
      <el-row class="content-show-row-title">
        <el-col :span="24">
          <div class="content-head">
            <div class="content-head-title">{{$t('station.introduction')}}</div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <!--<div class="powerStationPic"></div>-->
          <img src="/assets/images/station/station.png" onerror="(function(event){var img=event.srcElement;img.src='/assets/images/station/station.png';img.onerror=null;})(event)" width="100%" height="143" ref="stationPic">
        </el-col>
        <el-col :span="13">
          <div class="powerStationDesc" style="padding-left: 10px;">
            <div class="station-des-title-text">{{stationInfo.name}}</div>
            <table width="100%">
              <tr>
                <td class="station-des-text">{{$t("station.status")}}</td>
                <td class="station-des-text-val">{{ stationStatus[stationInfo.status - 1]}}</td>
              </tr>
              <tr>
                <td class="station-des-text">{{$t("station.capacity")}}</td>
                <td class="station-des-text-val"><div class="text-ellipsis">{{stationInfo.installedCapacity}}kW</div></td>
              </tr>
              <tr>
                <td class="station-des-text">{{$t("station.runDate")}}</td>
                <td class="station-des-text-val"><div class="text-ellipsis">{{convertDateFromString(stationInfo.runDate) | timestampFomat($t('dateFormat.yyyymmdd'))}}</div></td>
              </tr>
              <tr>
                <td class="station-des-text">{{$t("station.runDays")}}</td>
                <td class="station-des-text-val"><div class="text-ellipsis">{{runDays}}</div></td>
              </tr>
              <tr>
                <td class="station-des-text">{{$t("station.ower")}}</td>
                <td class="station-des-text-val"><div class="text-ellipsis">{{stationInfo.ower}}</div></td>
              </tr>
              <tr>
                <td class="station-des-text">{{$t("station.address")}}</td>
                <td class="station-des-text-val"><div class="text-ellipsis" :title="stationInfo.address">{{stationInfo.address}}</div></td>
              </tr>
            </table>
          </div>
        </el-col>
      </el-row>
      <el-row style="margin-top: 5px">
        <el-col :span="7">
          <el-row>
            <el-col :span="24" style="font-size:14px;color:#333">
              {{ new Date().getTime() | dateFormater($t('dateFormat.mmdd'))}}
            </el-col>
          </el-row>
          <el-row style="height:100%;">
            <el-col :span="12" style="line-height: 20px;">
              {{weathers[0].descr}}<br/>
              {{weathers[0].temperature}}<br/>
              {{weathers[0].wind}}
            </el-col>
            <el-col :span="12">
              <img :src="weathers[0].img" width="100%"/>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="7">
          <el-row>
            <el-col :span="24" style="font-size:14px;color:#333">
              {{ new Date().getTime() + 24 * 60 * 60 * 1000 | dateFormater($t('dateFormat.mmdd'))}}
            </el-col>
          </el-row>
          <el-row style="height:100%;">
            <el-col :span="12" style="line-height: 20px;">
              {{weathers[1].descr}}<br/>
              {{weathers[1].temperature}}<br/>
              {{weathers[1].wind}}
            </el-col>
            <el-col :span="12">
              <img :src="weathers[1].img" width="100%"/>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="7">
          <el-row>
            <el-col :span="24" style="font-size:14px;color:#333">
              {{ (new Date().getTime() + 2 * 24 * 60 * 60 * 1000) | dateFormater($t('dateFormat.mmdd'))}}
            </el-col>
          </el-row>
          <el-row style="height:100%;">
            <el-col :span="12" style="line-height: 20px;">
              {{weathers[2].descr}}<br/>
              {{weathers[2].temperature}}<br/>
              {{weathers[2].wind}}
            </el-col>
            <el-col :span="12">
              <img :src="weathers[2].img" width="100%"/>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="1">
      <div>&nbsp;</div>
    </el-col>
    <el-col :span="16" style="margin-top: 10px">
      <el-row class="content-show-row-title">
        <el-col :span="24">
          <div class="content-head">
            <div class="content-head-title">{{$t("station.devStatus")}}</div>
          </div>
        </el-col>
      </el-row>
      <el-row style="margin-top: 50px;">
        <el-col :span="24">
          <!-- <div style="position: relative;width: 100%;height:130px;">
              <div>
                <i class="dev-power-state dev-power-modules">
                  <div class="dev-power-inner-text">光伏组件</div>
                </i>
                <i class="dev-power-state dev-power-inverter">
                  <div class="dev-power-inner-text">逆变器</div>
                </i>
                <i class="dev-power-state dev-power-electricMeter">
                  <div class="dev-power-inner-text">电表</div>
                </i>
                <i class="dev-power-state dev-power-grid">
                  <div class="dev-power-inner-text">电网</div>
                </i>
                <i class="dev-power-arraw arraw-running"></i>
              </div>
              <div>
                <div class="dev-power-div dev-power-in">
                  <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.inputPower || 0}}</label><br/>
                  <label style="font-size: 14px;color:#333;">逆变器输入功率(kW)</label>
                </div>
                <div class="dev-power-div dev-power-out">
                  <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.outputPower || 0}}</label><br/>
                  <label style="font-size: 14px;color:#333;">逆变器输出功率(kW)</label>
                </div>
                <div class="dev-power-div dev-power-inline">
                  <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.onGridPower || 0}}</label><br/>
                  <label style="font-size: 14px;color:#333;">上网功率(kW)</label>
                </div>
              </div>
          </div> -->

          <div style="display: flex;position: relative;margin-bottom: 20px;">
            <div style="flex: 0;">
              <img src="/assets/images/station/modules.png" style="height: 110px;"/>
              <div style="text-align: center;">{{$t('station.strings')}}</div><!-- 光伏板 -->
            </div>
            <div style="flex: 1;padding-top: 24px;">
              <div class="dev-power-div dev-power-in">
                <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.inputPower || 0}}</label><br/>
                <label style="font-size: 14px;color:#333;">{{$t("station.inputPower")}}(kW)</label>
              </div>
            </div>
            <div style="flex: 0; z-index: 7">
              <img src="/assets/images/station/inverter.png" style="height: 110px;"/>
              <div style="text-align: center;">{{$t('station.inverter')}}</div><!-- 逆变器 -->
            </div>
            <div style="flex: 1;padding-top: 24px;">
              <div class="dev-power-div dev-power-out">
                <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.outputPower || 0}}</label><br/>
                <label style="font-size: 14px;color:#333;">{{$t("station.outputPower")}}(kW)</label>
              </div>
            </div>
            <div style="flex: 0; z-index: 7;">
              <img src="/assets/images/station/electricMeter.png" style="height: 110px;">
              <div style="text-align: center;">{{$t('station.electricMeter')}}</div><!-- 电网 -->
            </div>

            <div style="flex: 1;padding-top: 24px;">
              <div class="dev-power-div dev-power-inline">
                <label style="font-size: 24px;color:#39BE71;">{{devRunningStatus.onGridPower || 0}}</label><br/>
                <label style="font-size: 14px;color:#333;">{{$t("station.onGridPower")}}(kW)</label>
              </div>
            </div>

            <div style="flex: 0; z-index: 7;">
              <img src="/assets/images/station/grid.png" style="height: 110px;">
              <div style="text-align: center;">{{$t('station.grid')}}</div><!-- 电网 -->
            </div>
            <i class="dev-power-arraw arraw-running"></i>
          </div>
        </el-col>
      </el-row>
    </el-col>
  </el-row>

  <el-row style="margin-top:20px" class="content-show-row">
    <el-col :span="8">
      <el-row class="content-show-row-title">
        <el-col :span="24">
          <div class="content-head">
            <div class="content-head-title">
              <a class="powerAndIncomeIcon"></a><span>{{$t("station.environmentContribution")}}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <div class="content-chart">
        <el-row style="font-size: 14px;">
          <el-col :span="3">
            <div style="height: 100%;">&nbsp;&nbsp;</div>
          </el-col>
          <el-col :span="6">
            <div style="height: 100%;">
              <div style="text-align: center;">
                <i class="my-contribution coal"></i>
                {{$t("station.coalContribution")}}(t)
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div style="height: 100%;">
              <div style="text-align: center;">
                <i class="my-contribution co2"></i>
                CO<sub>2</sub>{{$t("station.reduction")}}(t)
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div style="height: 100%;">
              <div style="text-align: center;">
                <i class="my-contribution tree-planting"></i>
                {{$t("station.treePlanting")}}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 5%;font-size: 14px">
          <el-col :span="3">
            <div style="text-align: center;line-height: 16px">{{$t("station.monthly")}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.month[0]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.month[1]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.month[2]}}</div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 5%;font-size: 14px">
          <el-col :span="3">
            <div style="text-align: center;line-height: 16px" >{{$t("station.yearly")}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.year[0]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.year[1]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center" class="contributionDetail">{{contribution.year[2]}}</div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 5%;font-size: 14px">
          <el-col :span="3">
            <div style="text-align: center;line-height: 16px" class="">{{$t("station.total")}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center;" class="contributionDetail">{{contribution.total[0]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center;" class="contributionDetail">{{contribution.total[1]}}</div>
          </el-col>
          <el-col :span="6">
            <div style="text-align: center;" class="contributionDetail">{{contribution.total[2]}}</div>
          </el-col>
        </el-row>
      </div>

    </el-col>
    <el-col :span="8">
      <el-row class="content-show-row-title">
        <el-col :span="24">
          <div class="content-head">
            <div class="content-head-title">
              <a class="powerAndIncomeIcon"></a><span>{{$t("station.powerAndIncome")}}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row class="content-chart">
        <el-col :span="24">
        <el-tabs v-model="contentCharts.powerAndIncome.activeName" @tab-click="powerAndIncomeChange"
                 style="height: 100%">
          <el-tab-pane :label="$t('station.month')" name="month">
            <div ref="month" style="width: 100%; height: 210px"></div>
          </el-tab-pane>
          <el-tab-pane :label="$t('station.year')" name="year">
            <div ref="year" style="width: 100%; height: 210px"></div>
          </el-tab-pane>
          <el-tab-pane :label="$t('station.life')" name="allYear">
            <div ref="allYear" style="width: 100%; height: 210px"></div>
          </el-tab-pane>
        </el-tabs>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="1">
      <div>&nbsp;</div>
    </el-col>
    <el-col :span="7">
      <el-row class="content-show-row-title">
        <el-col :span="24">
          <div class="content-head">
            <div class="content-head-title">
              <a class="powerAndIncomeIcon"></a><span>{{$t('station.powerMonitoring')}}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="">
          <div class="content-chart">
            <div ref="powerMonitoring" style="width: 100%; height: 260px"></div>
          </div>
        </el-col>
      </el-row>
    </el-col>
  </el-row>
</div>
</template>
<style lang='less' src='./index.less' scoped></style>
<script src='./index.js'></script>
