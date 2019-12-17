<template>
  <div class="contact-alarm-table">
    <div style="overflow: hidden;clear: both;margin: 10px;">
      <div style="float: left;font-size: 18px;font-weight: bold;">
        {{alarmData.alarmName}}
      </div>
      <div style="float: right;">
        <label style="padding-right: 10px;font-size: 18px;color: #009900;">{{alarmData.statusId | alarmStatusFilter(vm)}}</label>
        <i class="expan-btn" :class="btnClass" @click="expandClick"></i>
      </div>
    </div>
    <div style="margin: 10px;">
      <table style="width: 100%;" cellspacing="0" cellpadding="0">
        <tr class="table-alarm-row">
          <td width="20%" align="center" class="label-name">{{$t('io.alarmName')}}</td>
          <td width="30%" align="center" class="label-val">{{alarmData.alarmName}}</td>
          <td width="20%" align="center" class="label-name">{{$t('io.alarm.alarmLevel')}}</td>
          <td width="30%" align="center" class="label-val last-td"><label :class="alarmLevelClass">{{alarmData.levelId | alarmLeveFilter(vm)}}</label></td>
        </tr>
        <tr class="table-alarm-row" :class="{'hidden-last':isHidden}">
          <td width="20%" align="center" class="label-name">{{$t('io.alarm.alarmType')}}</td>
          <td width="30%" align="center" class="label-val">{{alarmData.teleType | alarmTypeFilter(vm)}}</td>
          <td width="20%" align="center" class="label-name">{{$t('io.defect.startTime')}}</td>
          <td width="30%" align="center" class="label-val last-td">{{alarmData.firstHappenTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</td>
        </tr>
        <tr class="table-alarm-row" :class="{'is-hidden':isHidden}">
          <td width="20%" align="center" class="label-name">{{$t('io.alarm.devName')}}</td>
          <td width="30%" align="center" class="label-val">{{alarmData.devAlias}}</td>
          <td width="20%" align="center" class="label-name">{{$t('io.alarm.recoverTime')}}</td>
          <td width="30%" align="center" class="label-val last-td">{{alarmData.recoverTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</td>
        </tr>
        <tr class="table-alarm-row" :class="{'is-hidden':isHidden}">
          <td width="20%" align="center" class="label-name">{{$t('io.devType')}}</td>
          <td width="30%" align="center" class="label-val">{{alarmData.devType}}</td>
          <td width="20%" align="center" class="label-name">{{$t('io.alarm.devAddr')}}</td>
          <td width="30%" align="center" class="label-val last-td">{{alarmData.longitude === null ? '-' : (alarmData.longitude + ' : '+ alarmData.latitude)}}</td>
        </tr>
        <tr class="table-alarm-row" :class="{'is-hidden':isHidden}">
          <td width="20%" align="center" class="label-name">{{$t('io.defect.devSn')}}</td>
          <td width="30%" align="center" class="label-val">{{alarmData.snCode}}</td>
          <td width="20%" align="center" class="label-name">{{$t('io.plantName')}}</td>
          <td width="30%" align="center" class="label-val last-td">{{stationName}}</td>
        </tr>
      </table>
    </div>
    <!-- 修复建议的信息 -->
    <div style="margin: 10px;" :class="{'is-hidden':isHidden}">
      <div style="font-size: 14px;font-weight: 700;">
        {{$t('io.suggession')}}
      </div>
      <div>
        <table style="width: 100%;" cellspacing="0" cellpadding="0">
          <tr class="table-alarm-row">
            <td width="20%" align="center" class="label-name">{{$t('io.alarm.alarmCause')}}</td>
            <td width="80%" align="center" class="label-val last-td">
              <div v-html="textHtmlFilter(alarmData.alarmCuase)" style="text-align: left;padding-left: 10px;">
              </div>
            </td>
          </tr>
          <tr class="table-alarm-row">
            <td width="20%" align="center" class="label-name">{{$t('io.suggession')}}</td>
            <td width="80%" align="center" class="label-val last-td">
              <div v-html="textHtmlFilter(alarmData.repairSuggestion)" style="text-align: left;padding-left: 10px;"></div>
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  const btnClassLen = 2; // 对应下面class的数组的个数
  const btnClassArr = ['el-icon-arrow-up', 'el-icon-arrow-down']; // 切换的class的数组
  const props = {
    alarmData: { // 告警数据
      type: Object,
      default: () => ({
        // 测试告警
        alarmName: '测试告警',
        alarmLevel: 1,
        alarmState: 1,
        alarmType: 1, // 1.设备告警 2.智能告警
        startTime: new Date().getTime() - 30 * 60 * 1000,
        devName: '01#组串式逆变器',
        endTime: new Date().getTime(),
        devType: '组串式逆变器',
        devAddress: '经度 32.32°    纬度 60.00°',
        devSn: 'SN1365494949',
        alarmCuase: '1、逆变器工作电压过低；\n2、逆变器接线故障;\n3、光伏组件受到严重遮挡;',
        suggession: '1、逆变器实时监测外部工作条件，故障消失后自动恢复正常工作，不需要人工干预；\n2、检查接线端子及接线是否正常；\n3、如果反复出现，请联系运维热线：400-888-8888;\n'
      })
    },
    stationName: {
      type: String,
      default: () => ('智慧光伏电站A')
    },
    pBtnClass: { // 当前展开的样式
      type: String,
      default: () => (btnClassArr[1]) // 默认不展开
    },
  }
  export default {
    props: props,
    created () {
      if (btnClassArr.indexOf(this.pBtnClass) >= 0) {
        this.btnClass = this.pBtnClass;
      } else {
        this.btnClass = btnClassArr[1];
      }
    },
    data () {
      return {
        btnClass: null,
        vm: this
      }
    },
    filters: {
      alarmTypeFilter (val, vm) {
        if (!val) {
          return '-'
        }
        if (val === 1) {
          // 设备告警
          return vm.$t('io.alarm.devAlarm')
        }
        if (val === 2) {
          // 智能告警
          return vm.$t('io.alarm.smartAlarm')
        }
        return '-';
      },
      alarmLeveFilter (val, vm) { // 告警级别的过滤器
        if (!val || isNaN(val)) {
          return '-';
        }
        if (val === 1){
          // 严重
          return vm.$t('io.alarmLevelArr')[0]
        }
        if (val === 2){
          // 一般
          return vm.$t('io.alarmLevelArr')[1]
        }
        if (val === 3){
          // 提示
          return vm.$t('io.alarmLevelArr')[2]
        }
        return '-';
      },
      // '1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）；'
      alarmStatusFilter (val, vm) { // 告警处理状态的过滤器
        if (isNaN(val)) {
          return '';
        }
        if (val === 1) {
          // 未处理
          return vm.$t('io.alarmStatusArr')[0]
        }
        if (val === 2) {
          // 已确认
          return vm.$t('io.alarmStatusArr')[1]
        }
        if (val === 3) {
          // 处理中
          return vm.$t('io.alarmStatusArr')[2]
        }
        if (val === 4) {
          // 已处理
          return vm.$t('io.alarmStatusArr')[3]
        }
        if (val === 5) {
          // 已清除
          return vm.$t('io.alarmStatusArr')[4]
        }
        if (val === 6) {
          // 已恢复
          return vm.$t('io.alarmStatusArr')[5]
        }
        // '未知'
        return vm.$t('io.unKnow')
      }
    },
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      expandClick (ev) { // 展开折叠的按钮的点击事件 通过切换具有的class来判断是显示还是隐藏
        this.btnClass = btnClassArr[(btnClassArr.indexOf(this.btnClass) + 1) % btnClassLen];
      },
      textHtmlFilter (val) {
        if (!val) {
          return '-'
        }
        return val.replace(/\n|\r\n/g, "<br/>")
      }
    },
    watch: {},
    computed: {
      isHidden () {
        return btnClassArr.indexOf(this.btnClass) === 1;
      },
      alarmLevelClass() {
        let alarmLevel = this.alarmData.alarmLevel;
        if (!alarmLevel || isNaN(alarmLevel)) {
          return '';
        }
        if (alarmLevel >= 1 && alarmLevel <= 3) {
          return 'alarm-level-' + alarmLevel;
        }
        return '';
      }
    }
  }
</script>

<style lang="less" scoped>
  .contact-alarm-table {
    margin: 10px 0;
    border: 1px solid #dedede;
    i.expan-btn {
      color: #fff;
      font-size: 20px;
      line-height: 28px;
      text-align: center;
      width: 28px;
      height: 28px;
      background: #666;
      border-radius: 50%;
      cursor: pointer;
    }
    tr.table-alarm-row > td {
      border-left: 1px solid #dedede;
      border-top: 1px solid #dedede;
      font-size: 14px;
      padding: 8px;
      vertical-align: middle;
      &.label-name{
        background: rgba(200,200,200,0.5);
      }
      &.last-td{
        border-right: 1px solid #dedede;
      }
    }
    tr.table-alarm-row:last-child > td,tr.hidden-last > td {
        border-bottom: 1px solid #dedede;
    }
    .is-hidden {
      display: none;
    }
    .alarm-level-1 {
      color: red;
    }
    .alarm-level-2 {
      color: #09f;
    }
    .alarm-level-3 {
      color: #af4;
    }
  }
</style>

