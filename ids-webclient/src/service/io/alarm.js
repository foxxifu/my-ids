import fetch from '@/utils/fetch'
// 设备告警的列表
const alarmList = (data) => fetch('/biz/alarm/list', data, 'POST')
// 设备实时告警的列表
const alarmOnlineList = (data) => fetch('/biz/alarm/alarmOnlineList', data, 'POST')
// 智能告警的列表
const analysisList = (data) => fetch('/biz/alarm/list/analysis', data, 'POST')
// 获取简单的告警信息
const getAlarmSimple = (data) => fetch('/biz/alarm/getAlarmSimple', data, 'POST');
// 查询设备告警数据
const listSpecifiedInfo = (data) => fetch('/biz/alarm/list/specified', data, 'POST')
// 查询设备告警修复建议
const infoRepair = (data) => fetch('/biz/alarm/repair', data, 'POST')
// 设备告警清除
const devAlarmClear = (data) => fetch('/biz/alarm/confirm/cleared', data, 'POST')
// 设备告警确认
const devAlarmAck = (data) => fetch('/biz/alarm/confirm/ack', data, 'POST')
// 智能告警清除
const smartAlarmClear = (data) => fetch('/biz/alarm/confirm/clearedzl', data, 'POST')
// 智能告警确认
const smartAlarmAck = (data) => fetch('/biz/alarm/confirm/ackzl', data, 'POST')

export default {
  alarmList,
  analysisList,
  getAlarmSimple,
  listSpecifiedInfo,
  infoRepair,
  devAlarmClear,
  devAlarmAck,
  smartAlarmClear,
  smartAlarmAck,
  alarmOnlineList
}
