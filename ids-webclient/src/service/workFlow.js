import fetch from '@/utils/fetch'

const PRE = '/biz'

// 获取缺陷的简单数据
const getDefectSimple = (data) => fetch(PRE + '/workFlow/getDefectSimple', data, 'POST')
// 获取消缺的数量
const getWorkFlowCount = (data) => fetch(PRE + '/workFlow/getWorkFlowCount', data, 'POST')
// 获取消缺详情的数据
const getWorkFlowDefectsByCondition = (data) => fetch(PRE + '/workFlow/getWorkFlowDefectsByCondition', data, 'POST')
// 新增缺陷
const saveDefect = (data) => fetch(PRE + '/workFlow/saveDefect', data, 'POST')
// 根据缺陷id获取缺陷信息
const getWorkFlowDefectDetails = (defectId) => fetch(PRE + '/workFlow/getWorkFlowDefectDetails', {defectId: defectId}, 'POST')
// 获取告警转缺陷的信息
const alarmToDefect = (data) => fetch(PRE + '/workFlow/alarmToDefect', data, 'POST')

// 统计个人任务总条数
const getTaskCount = (taskState) => fetch(PRE + '/workFlow/getWorkFlowTaskCountByUserId', {taskState: taskState || '0'}, 'POST')

// 获取缺陷关联的告警
const getDefectAlarm = (data) => fetch(PRE + '/workFlow/getDefectAlarm', data, 'POST');

// 查询电站和用户数据
const getStationUser = (data) => fetch(PRE + '/workFlow/getStationUser', data, 'POST');

// 执行
const executeWorkFlowDefect = (data) => fetch(PRE + '/workFlow/executeWorkFlowDefect', data, 'POST');

export default {
  getDefectSimple,
  getWorkFlowCount,
  getWorkFlowDefectsByCondition,
  saveDefect,
  getWorkFlowDefectDetails,
  alarmToDefect,
  getTaskCount,
  getDefectAlarm,
  getStationUser,
  executeWorkFlowDefect,
}
