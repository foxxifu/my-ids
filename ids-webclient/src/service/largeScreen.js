import fetch from '@/utils/fetch'

// 头尾数据
const commonData = (e) => fetch('/biz/largeScreen/getCommonData', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 发电状态（实时）
const powerGeneration = (e) => fetch('/biz/largeScreen/getPowerGeneration', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 发电趋势（本月）
const powerTrend = (e) => fetch('/biz/largeScreen/getPowerTrends', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 发电规模统计
const powerScale = (e) => fetch('/biz/largeScreen/getListStationInfo', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 任务工单统计
const taskStatistics = (e) => fetch('/biz/largeScreen/getTaskStatistics', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 设备分布统计
const equipmentDistribution = (e) => fetch('/biz/largeScreen/getDeviceCount', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 供电负荷统计
const powerSupply = (e) => fetch('/biz/largeScreen/getActivePower', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 环保贡献统计
const socialContribution = (e) => fetch('/biz/largeScreen/getSocialContribution', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')
// 获取区域和直属电站
const regionPowerStation = (e) => fetch('/biz/largeScreen/getMapShowData', e ? {
  queryId: e.id,
  queryType: e.type
} : {}, 'POST')

export {
  commonData,
  powerGeneration,
  powerTrend,
  powerScale,
  taskStatistics,
  equipmentDistribution,
  powerSupply,
  socialContribution,
  regionPowerStation,
}
