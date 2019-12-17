import fetch from '@/utils/fetch'

const platformList = (data) => fetch('/api/platform/list', data, 'POST')
const platformDevList = (data) => fetch('/api/platform/devList', data, 'POST')
const getRealtimeKPI = (data) => fetch('/api/platform/getRealtimeKPI', data, 'POST') // 电站指标，仪表盘的数据
const stationStatus = (data) => fetch('/api/platform/stationStatus', data, 'POST') // 电站状态的数据
const getAlarmStatistics = (data) => fetch('/api/platform/getAlarmStatistics', data, 'POST') // 设备告警数量的数据

export default {
  platformList,
  platformDevList,
  getRealtimeKPI,
  stationStatus,
  getAlarmStatistics
}
