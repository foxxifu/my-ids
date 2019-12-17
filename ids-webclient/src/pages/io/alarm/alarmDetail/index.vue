<template>
  <div class="alarm-detail">
    <table cellpadding="0" cellspacing="0" width="100%">
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.plantName')}}</td>
        <td class="val-label last-label" colspan="3" width="80%">
          {{ currentData.stationName }}
        </td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.devName')}}</td>
        <td class="val-label last-label" colspan="3" width="80%">
          {{ currentData.devName }}
        </td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarmName')}}</td>
        <td class="val-label" width="30%" align="center">{{ getWithReplace(currentData.alarmName, currentData.alarmModelId, 'alarmName') }}</td>
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.alarmLevel')}}</td>
        <td class="val-label last-label" align="center" width="30%">
          <label :class="{'text-color-red':currentData.alarmLevel === 1,
          'text-color-blue':currentData.alarmLevel === 2,
          'text-color-green':currentData.alarmLevel === 3}">{{ currentData.alarmLevel | alarmLeveFilter(vm) }}</label>
        </td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.firstAlarmTime')}}</td>
        <td class="val-label" width="30%" align="center">{{ currentData.firstHappenTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</td>
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.lastAlarmTime')}}</td>
        <td class="val-label last-label" align="center" width="30%">{{ currentData.recoverTime | timestampFomat($t('dateFormat.yyyymmddhhmmss')) }}</td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.devAddr')}}</td>
        <td class="val-label last-label" width="30%">
          {{ currentData.devAddr }}
        </td>
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.devInverl')}}</td>
        <td class="val-label last-label" width="30%">
          {{ currentData.devInterval }}
        </td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.alarm.alarmCause')}}</td>
        <td class="val-label last-label" colspan="3" width="80%">
          <div v-html="textHtmlFilter(getValueNoReplace(currentData.alarmCause, currentData.alarmModelId, 'cause'))"></div>
        </td>
      </tr>
      <tr class="table-row">
        <td class="name-label" width="20%" align="center">{{$t('io.suggession')}}</td>
        <td class="val-label last-label" colspan="3" width="80%">
          <div v-html="textHtmlFilter(getValueNoReplace(currentData.repairSuggestion, currentData.alarmModelId, 'suggestion'))"></div>
        </td>
      </tr>
    </table>
  </div>
</template>
<script src="./index.js"></script>
<style lang="less" src="./index.less" scoped></style>

