<template>
  <div class='home'>

    <el-row>
      <el-col :span="6">
        <e-chart :options="gaugeChartDatas.powerChart.option"
                 style="width: 100%;height: 135px;min-width: 240px;"></e-chart>
        <div style="margin-left:15%;width: 70%;font-size: 16px;color: #4f6271">
          <div style="float: left;width: 50%;">
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.powerChart.value}}
            </div>
            <div style="margin-top:10px;text-align: center;width: 100%;">
              <!-- retPow:实施功率的国际化-->
              {{$t('home.retPow')}}({{gaugeChartDatas.powerChart.unit}})
            </div>
          </div>
          <div style="float: left;width: 50%;text-align: center;">
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.powerChart.cap}}
            </div>
            <div style="margin-top:10px;text-align: center;width: 100%;">
              <!-- insCap:装机容量的国际化-->
              {{$t('home.insCap')}}({{gaugeChartDatas.powerChart.capUnit}})
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="7">
        <div style="width: 70%; margin: 0 auto;min-width: 240px;">
          <!-- 日发电量和年发电量 -->
          <div style="float: left;width: 50%;">
            <!--日发电量的图片 -->
            <div class="dayPowerChart"></div>
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.dayAndYearPowerChart.day}}
            </div>
            <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;">
              {{$t('home.daiYie')}}({{gaugeChartDatas.dayAndYearPowerChart.unitDay}})
            </div>
          </div>
          <div style="float: left;width: 50%;">
            <!-- 年发电量的图片 -->
            <div class="yearPowerChart"></div>
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.dayAndYearPowerChart.year}}
            </div>
            <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;">
              {{$t('home.annYie')}}({{gaugeChartDatas.dayAndYearPowerChart.unitYear}})
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="7">
        <!-- 日收益和年收益 -->
        <div style="width: 70%; margin: 0 auto;min-width: 240px;">
          <div style="float:left;width:50%;">
            <!--日收益的图片 -->
            <div class="dayIncomeChart"></div>
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.incommonDayAndYearChart.day}}
            </div>
            <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;color: #4f6271">
              {{$t('home.daiInc')}}({{gaugeChartDatas.incommonDayAndYearChart.dayUnit}})
            </div>
          </div>
          <div style="float:left;width:50%;">
            <!-- 年收益的图片 -->
            <div class="yearIncomeChart"></div>
            <div style="width: 100%;text-align: center;" class="show-text-style">
              {{gaugeChartDatas.incommonDayAndYearChart.year}}
            </div>
            <div style="margin-top: 10px;width: 100%;text-align: center;font-size: 16px;color: #4f6271">
              {{$t('home.annInc')}}({{gaugeChartDatas.incommonDayAndYearChart.yearUnit}})
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <!-- 累计发电量 -->
        <div ref="totalPowerChart" class="totalPowerChart">
        </div>
        <div style="width: 100%;text-align: center;" class="show-text-style">
          {{gaugeChartDatas.totalPowerChart.value}}
        </div>
        <div style="margin-top: 10px;width: 100%;text-align: center;" class="show-title-style">
          {{$t('home.totYie')}}({{gaugeChartDatas.totalPowerChart.unit}})
        </div>
      </el-col>
    </el-row>

    <div class="content-body">
      <!--第一行的数据-->
      <div class="content-row-first">
        <div style="width: 100%;height: 100%;overflow: hidden;min-width: 600px;">

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <span>{{$t('home.plantStatus')}}</span>
            </div>
            <div class="content-chart">
              <e-chart :options="contentCharts.stationStatusChart.option" style="height: 64%;"
                       id="stationStatusChart"></e-chart>
              <div style="height:36%;width: 100%;padding: 8px 0;">
                <div style="float: left;width: 15%;color: #aaa;line-height: 20px;">
                  <div style="margin-left: 20%;color: #f33;">
                    <div style="line-height: 30px;font-size: 16px;text-align: center;">{{$t('home.fault')}}</div>
                    <div style="line-height: 50px;font-size: 24px;text-align: center;">
                      {{contentCharts.stationStatusChart.trouble}}
                    </div>
                  </div>
                </div>
                <div style="float: left;width: 45%;color: #aaa;line-height: 20px;">
                  <div style="margin-left: 17.5%;color: #A7A7A7;">
                    <div style="line-height: 30px;font-size: 16px;text-align: center;">{{$t('home.disconnect')}}</div>
                    <div style="line-height: 50px;font-size: 24px;text-align: center;">
                      {{contentCharts.stationStatusChart.disconnected}}
                    </div>
                  </div>
                </div>
                <div style="float: left;width: 25%;color: #aaa;font-size: 16px;line-height: 20px;">
                  <div style="margin-left: 20%;color: #39BE71;">
                    <div style="line-height: 30px;font-size: 16px;text-align: center;">{{$t('home.normal')}}</div>
                    <div style="line-height: 50px;font-size: 24px;text-align: center;">
                      {{contentCharts.stationStatusChart.health}}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <span>{{$t('home.alarmStatistics')}}</span>
            </div>
            <div class="content-chart">
              <div style="width: 100%;height: 100%;overflow: hidden;clear: both;">
                <e-chart :options="contentCharts.alarmCountChart.option"
                         style="float: left;width: 60%;height: 95%;"></e-chart>
                <div style="float: left;width: 39%;height: 100%;">
                  <div style="margin: 15% 0 0 15%;">
                    <!-- 严重 -->
                    <div style="line-height: 50px;color:#f00;font-size: 24px;">
                      <i style="display: inline-block;background-color: #f00;width: 16px;height: 12px;"></i>
                      {{contentCharts.alarmCountChart.serious}}
                    </div>
                    <!-- 一般 -->
                    <div style="line-height: 50px;color:#ff9933;font-size: 24px;">
                      <i style="display: inline-block;background-color: #ff9933;width: 16px;height: 12px;"></i>
                      {{contentCharts.alarmCountChart.minor}}
                    </div>
                    <!-- 提示 -->
                    <div style="line-height: 50px;color:#09A8D9;font-size: 24px;">
                      <i style="display: inline-block;background-color: #09A8D9;width: 16px;height: 12px;"></i>
                      {{contentCharts.alarmCountChart.prompt}}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <!-- 电站排名的国际化 -->
              <span>{{$t('home.topplant')}}</span>
            </div>
            <div class="content-chart">
              <div style="height: 100%;position: relative;">
                <div class="stationOrder" @click="changeOrder">
                  <i :class="{ des: contentCharts.stationSort.des }"></i>
                  <span>{{ showDesText }}</span>
                </div>
                <el-tabs v-model="contentCharts.stationSort.activeName" style="height: 100%"
                         @tab-click="stationPRAndHourChange">
                  <el-tab-pane :label="$t('home.plantPR')" name="stationPr">
                    <div style="position: relative;width: 100%;height: 100%;">
                      <e-chart :options="contentCharts.stationSort.prOption" style="width: 100%;height:100%;"></e-chart>
                      <div style="position: absolute;top:0;left: 0;height:84%;width:20%;margin-top: 1.6%;">
                        <table width="100%" height="100%">
                          <tr v-for="stationName in contentCharts.stationSort.showYlableNameArr">
                            <td style="vertical-align: middle;color:#888;">
                              <div class="text-ellipsis" :title="stationName">{{stationName}}</div>
                            </td>
                          </tr>
                        </table>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('home.hour')" name="eqHours">
                    <div style="position: relative;width: 100%;height: 100%;">
                      <e-chart :options="contentCharts.stationSort.hourOption"
                               style="width: 100%;height:100%;"></e-chart>
                      <div style="position: absolute;top:0;left: 0;height:84%;width:20%;margin-top: 1.6%;">
                        <table width="100%" height="100%">
                          <tr v-for="stationName in contentCharts.stationSort.showYlableNameArr">
                            <td style="vertical-align: middle;color:#888;">
                              <div class="text-ellipsis" :title="stationName">{{stationName}}</div>
                            </td>
                          </tr>
                        </table>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>
          </el-card>

        </div>
      </div>

      <div class="content-row-secend">
        <div style="width: 100%;height: 100%;overflow: hidden;min-width: 600px;">

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <span>{{$t('home.yieldsAndIncome')}}</span>
            </div>
            <div class="content-chart">
              <el-tabs v-model="contentCharts.powerAndIncome.activeName" @tab-click="powerAndIncomeChange"
                       style="width: 88%; height: 100%;">
                <el-tab-pane :label="$t('home.daily')" name="month">
                  <e-chart :options="contentCharts.powerAndIncome.monthOption" style="width: 100%; height: 100%;"></e-chart>
                </el-tab-pane>
                <el-tab-pane :label="$t('home.month')" name="year">
                  <e-chart :options="contentCharts.powerAndIncome.yearOption" style="width: 100%; height: 100%;"></e-chart>
                </el-tab-pane>
                <el-tab-pane :label="$t('home.year')" name="allYear">
                  <e-chart :options="contentCharts.powerAndIncome.allYearOption" style="width: 100%; height: 100%;"></e-chart>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-card>

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <span>{{$t('home.equipmentStatistical')}}</span>
            </div>
            <div class="content-chart">
              <e-chart id="home_dev_dis_chart_div" :options="contentCharts.deviceDistribution.option"
                       style="width: 100%; height: 100%;min-width:179px;"></e-chart>
              <div style="width: 40%; position: absolute; top: 0;right: 10%;height: 100%;font-size: 18px">
                <div style="float:left;width:49%;height: 30%;margin-top: 20%;text-align: center;color: #2CA560;">
                  {{contentCharts.deviceDistribution.zj}}
                  <i class="my-dev-dis zj"></i>
                </div>
                <div style="float:left;width:49%;height: 30%;margin-top: 20%;text-align: center;color: #D69842;">
                  {{contentCharts.deviceDistribution.nbq1}}
                  <i class="my-dev-dis nbq"></i>
                </div>
                <div style="float:left;width:49%;height: 30%;margin-top: 10%;text-align: center;color: #1795E5;">
                  {{contentCharts.deviceDistribution.nbq2}}
                  <i class="my-dev-dis jzsnbq"></i>
                </div>
                <div style="float:left;width:49%;height: 30%;margin-top: 10%;text-align: center;color: #8FD7F2;">
                  {{contentCharts.deviceDistribution.zlhlx}}
                  <i class="my-dev-dis zlhlx"></i>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="box-card" shadow="never">
            <div slot="header" class="content-head clearfix">
              <span>{{$t('home.environmentalContribution')}}</span>
            </div>
            <div class="content-chart" style="padding: 8% 0;">
              <el-row style="font-size: 1rem;">
                <el-col :span="3">
                  <div style="height: 100%;">&nbsp;&nbsp;</div>
                </el-col>
                <el-col :span="7">
                  <div style="height: 100%;">
                    <div style="text-align: center;">
                      <i class="my-contribution coal"></i>
                      {{$t('home.savedStandardCoal')}}(t)
                    </div>
                  </div>
                </el-col>
                <el-col :span="7">
                  <div style="height: 100%;">
                    <div style="text-align: center;">
                      <i class="my-contribution co2"></i>
                      CO<sub>2</sub>{{$t('home.reduction')}}(t)
                    </div>
                  </div>
                </el-col>
                <el-col :span="7">
                  <div style="height: 100%;">
                    <div style="text-align: center;">
                      <i class="my-contribution tree-planting"></i>
                      {{$t('home.equivalentTreePlanting')}}
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-row style="margin-top: 5%;font-size: 18px">
                <el-col :span="3">
                  <div style="text-align: center;">{{$t('home.anuual')}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center">{{contribution.coal}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center">{{contribution.co2}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center">{{contribution.tree}}</div>
                </el-col>
              </el-row>
              <el-row style="margin-top: 5%;font-size: 18px">
                <el-col :span="3">
                  <div style="text-align: center;">{{$t('home.total')}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center;">{{contribution.coalTotal}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center;">{{contribution.co2Total}}</div>
                </el-col>
                <el-col :span="7">
                  <div style="text-align: center;">{{contribution.treeTotal}}</div>
                </el-col>
              </el-row>
            </div>
          </el-card>

        </div>
      </div>

    </div>

  </div>
</template>
<script src='./index.js'></script>
<style lang='less' src='./index.less' scoped></style>
