// 运维工作台的请求
import fetch from '@/utils/fetch'

// 获取运维工作台的电站列表
const getSimpInfo = (data) => fetch('/biz/operation/worksite/getStationProfile', data, 'POST')
// 获取电站状态的数据
const getStationInfo = (data) => fetch('/biz/operation/worksite/userStationProf', data, 'POST')
// 获取电站列表的数据
const getStationList = (data) => fetch('/biz/operation/getStationList', data, 'POST')
// 获取电站列表的数据
const getStationDetailList = (data) => fetch('/biz/operation/worksite/getStationDetail', data, 'POST')
// 获取电站的告警列表
const getAlarmProfileList = (data) => fetch('/biz/operation/worksite/getAlarmProfile', data, 'POST')
// 根据设备id获取设备信息
const getDevById = (devId) => fetch('/biz/operation/getDevById', {devId: devId}, 'POST')
// 获取地图信息 用户，电站，企业，区域等的数据 data=> nodeId :企业编号/区域编号/电站编号 （admin: 为空）nodeType: 1：系统级 2：企业级 3：区域级
const getMapData = (data) => fetch('/biz/operation/worksite/getMapData', data, 'POST')
// 运维工作台获取消缺的简单信息
const getDefectTasks = (data) => fetch('/biz/operation/worksite/getDefectTasks', data, 'POST');
// 获取运维人员的缺陷信息
const getOperatorTasks = (data) => fetch(' /biz/operation/worksite/getOperatorTasks', data, 'POST');
// 获取告警详情
const getAlarmDetail = (data) => fetch(' /biz/operation/worksite/getAlarmDetail', data, 'POST');

export default {
  getSimpInfo,
  getStationInfo,
  getStationList,
  getStationDetailList,
  getAlarmProfileList,
  getDevById,
  getMapData,
  getDefectTasks,
  getOperatorTasks,
  getAlarmDetail
}
