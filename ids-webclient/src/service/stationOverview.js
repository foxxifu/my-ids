// 电站总览页面的URL请求
import fetch from '@/utils/fetch';

// 电站RP
const getPRList = (data) => fetch('/biz/stationOverview/getPRList', data, 'POST');
// 等效利用小时数
const getPPRList = (data) => fetch('/biz/stationOverview/getPPRList', data, 'POST');
// 获取发电量和收益的数据
const getPowerAndIncome = (data) => fetch('/biz/stationOverview/getPowerAndIncome', data, 'POST');
// 获取设备分布的数据
const getDevDistrition = (data) => fetch('/biz/stationOverview/getDevDistrition', data, 'POST');
// 获取环保贡献的数据
const getContribution = (data) => fetch('/biz/stationOverview/getContribution', data, 'POST');
const getRealtimeKPI = (data) => fetch('/biz/stationOverview/getRealtimeKPI', data, 'POST');// 电站指标，仪表盘的数据
const getStationStatus = (data) => fetch('/biz/stationOverview/getStationStatus', data, 'POST'); // 电站状态的数据
const getAlarmStatistics = (data) => fetch('/biz/stationOverview/getAlarmStatistics', data, 'POST'); // 设备告警数量的数据
const getPowerStationList = (data) => fetch('/biz/stationOverview/getPowerStationList', data, 'POST'); // 电站列表数据
export default {
  getPRList,
  getPPRList,
  getPowerAndIncome,
  getDevDistrition,
  getContribution,
  getRealtimeKPI,
  getStationStatus,
  getAlarmStatistics,
  getPowerStationList
}
