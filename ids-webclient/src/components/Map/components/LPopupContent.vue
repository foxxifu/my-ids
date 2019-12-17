<template>
  <div>
    <template v-if="type === 'stations'">
      <div class="popup-custom-station-container">
        <h2 class="popup-title">{{data.name}}</h2>
        <div class="popup-info">
          <div class="popup-info-left">
            <img ref="stationImage" :src="stationImagePath" alt="station-image" class="popup-station-image"
                 onerror="this.src = '/assets/images/exhibition/infoWin/defaultPic.png';this.onerror = null;"/>
          </div>
          <div class="popup-info-right">
            <ul>
              <li>
                <label>{{$t('components.map.address')}}</label>
                <span :title="data.address">{{data.address}}</span>
              </li>
              <li>
                <label>{{$t('components.map.installedCapacity')}}</label>
                <span><span
                  class="val">{{(data.installedCapacity || 0.00) | fixedNumber(2) | formatNumber}}</span>{{dataUnit.installedCapacity}}</span>
              </li>
              <li>
                <label>{{$t('components.map.gridTime')}}</label>
                <span><span v-if="data.runDate">{{data.runDate | moment('YYYY-MM-DD')}}</span></span>
              </li>
              <li v-if="data.type !== 'exhibition'">
                <label>{{$t('components.map.safeRunningDays')}}</label>
                <span><span
                  class="val">{{(data.safeRunningDays || 0) | formatNumber}}</span>{{dataUnit.safeRunningDays}}</span>
              </li>
              <li>
                <label>{{$t('components.map.generatingCapacity')}}</label>
              </li>
              <li class="generating-capacity">
            <span class="gc-item">{{$t('components.map.dayCapacity')}} <span
              class="val">{{(data.gridPowerDay || 0) | fixedNumber(2) | formatNumber}}</span>{{dataUnit.generatingCapacity.day}}</span>
                <span class="gc-item">{{$t('components.map.monthCapacity')}} <span
                  class="val">{{(data.gridPowerMonth || 0) | fixedNumber(2) | formatNumber}}</span>{{dataUnit.generatingCapacity.month}}</span>
                <span class="gc-item">{{$t('components.map.yearCapacity')}} <span
                  class="val">{{(data.gridPowerYear || 0) | fixedNumber(2) | formatNumber}}</span>{{dataUnit.generatingCapacity.year}}</span>
              </li>
            </ul>
          </div>
        </div>
        <div class="popup-box">
          <div v-if="data.weathers && data.weathers.length > 0" class="popup-weather">
            <p class="popup-box-title">
              {{data.weathers[0].date | moment('ddd')}} {{data.weathers[0].date | moment('YYYY-MM-DD')}}
              <span v-if="data.weather">(实时 {{+data.weather.temperature}}{{$t('unit.temperatureUnit')}})</span>
            </p>
            <div class="popup-box-content">
              <ul>
                <li v-for="(weather, index) in data.weathers" :key="weather.date">
                  <div v-if="index !== 0">
                    <p>{{weather.date | moment('ddd')}}</p>
                    <p>{{weather.date | moment('YYYY-MM-DD')}}</p>
                  </div>
                  <div>
                    <img :src="'/assets/images/weather/weather60/' + (weather.code_day || '99') + '.png'"
                         :width="index === 0 ? 75 : 40"
                         :height="index === 0 ? 75 : 40"/>
                    <div style="line-height: 20px;">
                      <p>{{weather.low}}~{{weather.high}}{{$t('unit.temperatureUnit')}}</p>
                      <p>{{weather.text_day}}</p>
                      <p>{{weather.wind_direction}}</p>
                    </div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
          <div class="popup-station-description">
            <p class="popup-box-title">{{$t('components.map.description')}}</p>
            <div class="popup-box-content box-description">{{data.description}}</div>
          </div>
        </div>
      </div>
    </template>
    <template v-else-if="type === 'users'">
      <div class="popup-custom-user-container">
        <div class="popup-user-title">{{data.userName}}{{$t('components.map.defectList')}}({{$t('components.map.common')}}{{data.list && data.list.length}}{{$t('components.map.ge')}})</div>
        <!-- 缺陷列表的查询 -->
        <el-table :data="data.list" style="width: 100%;margin-top: 10px;" border maxHeight="374">
          <el-table-column prop="stationName" :label="$t('components.chooseDevice.plantName')" align="center">
          </el-table-column>
          <el-table-column prop="taskDesc" :label="$t('components.map.taskDesc')" align="center">
            <template slot-scope="scope">
              <div class="text-ellipsis" :title="scope.row.taskDesc">
                {{scope.row.taskDesc}}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="taskStatus" :label="$t('components.map.taskStatus')" align="center">
            <template slot-scope="scope">
              <div class="s" :title="scope.row.taskStatus | taskStatusFormater(vm)">
                <label :class="['defect-color-state' + scope.row.taskStatus]">{{scope.row.taskStatus | taskStatusFormater(vm)}}</label>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </div>
</template>

<script>
  export default {
    name: 'LPopupContent',
    props: ['type', 'data'],
    data () {
      return {
        dataUnit: {
          installedCapacity: 'kW',
          safeRunningDays: this.$t('unit.days'),
          generatingCapacity: {
            day: 'kWh',
            month: 'kWh',
            year: 'kWh',
          },
        },
        vm: this
      }
    },
    filters: {
      taskStatusFormater (cellValue, vm) {
        if (!cellValue) {
          return ''
        }
        if (cellValue === '0') {
          return vm.$t('components.map.defectStatusArr')[0]
        }
        if (cellValue === '1') {
          return vm.$t('components.map.defectStatusArr')[1]
        }
        if (cellValue === '2') {
          return vm.$t('components.map.defectStatusArr')[2]
        }
        if (cellValue === '3') {
          return vm.$t('components.map.defectStatusArr')[3]
        }
        return vm.$t('components.map.unKnow')
      }
    },
    computed: {
      stationImagePath (vm) {
        return vm.data.pic ? '/biz/fileManager/downloadFile?fileId=' + vm.data.pic : '/assets/images/exhibition/infoWin/defaultPic.png'
      }
    },
    methods: {
    },
    beforeDestroy () {
    },
  }
</script>

<style lang="less" scoped>
  .popup-custom-station-container {
    position: relative;
    width: 620px;
    min-width: 120px;
    padding: 10px 15px;
    .popup-title {
      position: relative;
      height: 28px;
      padding: 0 1.2em;
      color: #F7F7F7;
      font-size: 20px;
      font-weight: 500;
      &::before {
        content: "\E613";
        position: absolute;
        top: 0;
        left: 0;
        color: #37afff;
        font-family: element-icons !important;
        font-size: 20px;
        font-weight: bolder;
      }
    }
    .popup-info {
      width: 100%;
      height: 218px;
      display: table;
      .popup-info-left {
        width: 45%;
        height: 100%;
        margin-right: 10px;
        vertical-align: middle;
        display: table-cell;
        .popup-station-image {
          width: 270px;
          height: 200px;
          margin: 10px 4px;
          display: block;
        }
      }
      .popup-info-right {
        width: 315px;
        height: 100%;
        vertical-align: top;
        /*display: table-cell;*/
        ul {
          margin: 10px 15px;
          li {
            font-size: 14px;
            line-height: 24px;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
            color: #bfbfbf;
            label {
              color: #f7f7f7;
            }
            span {
              color: #bfbfbf;
            }

            .val {
              position: relative;
              top: 0;
              margin: 0 5px 0 0;
              color: #00feff;
              font-size: 20px;
              line-height: 20px;
            }

            &.generating-capacity {
              margin: 5px 0;
              .gc-item {
                line-height: 22px;
                display: block;
              }
            }
          }
        }
      }
    }
    .popup-box {
      width: 100%;
      height: 185px;
      margin: 5px auto;
      background: url("../images/popup-weather-bg.png") 0 0 no-repeat;
      display: flex;
      .popup-box-title {
        height: 30px;
        padding: 5px 8px;
        margin: 0;
        color: #00fcff;
        font-size: 14px;
        font-weight: 500;
      }
      .popup-box-content {
        width: 100%;
        height: 135px;
        color: #e4e4e4;
        overflow: hidden;

        &.box-description {
          padding: 5px 8px;
          text-indent: 2em;
          overflow-x: hidden;
          overflow-y: auto;
        }
      }
      .popup-weather {
        position: relative;
        width: 92%;
        height: 100%;
        padding: 10px;
        display: table-cell;
        ul {
          width: 100%;
          display: flex;
          li {
            width: 100% / 3;
            text-align: center;
            color: #e4e4e4;
            font-size: 11px;
            display: inline-block;
            p {
              margin: 0;
              line-height: 18px;
            }
            &:first-child {
              font-size: 14px;
            }
          }
        }
        &::after {
          content: '';
          position: absolute;
          top: 20%;
          right: 0;
          width: 1px;
          height: 111px;
          background-image: linear-gradient(transparent, rgba(0, 252, 255, 0.5) 50%, transparent);
        }
      }
      .popup-station-description {
        width: 100%;
        height: 100%;
        padding: 10px;
        display: table-cell;
      }
    }
  }

  .popup-custom-user-container {
    position: relative;
    width: 480px;
    min-width: 120px;
    padding: 15px;
    .popup-user-title{
      font-size: 16px;
      color: #fff;
    }
    // 缺陷的颜色
    .defect-color-state0 { // 待审核
      color: #444;
    }
    .defect-color-state1 { // 待分配
      color: #a00;
    }
    .defect-color-state2 { // 消缺中
      color: #f90;
    }
    .defect-color-state3 { // 已完成
      color: #009900;
    }
    /*.defect-color-state4 { // 已完成*/
      /*color: #3399FF;*/
    /*}*/
  }
</style>
