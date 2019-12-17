/**
 * 信号点相关功能请求
 */
import fetch from '@/utils/fetch'

// 获取点表模型
const getSignalModel = (data) => fetch('/biz/signal/getSignalModel', data, 'POST');

// 告警转遥信
const alarmToShakeInfo = (data) => fetch('/biz/signal/convert/false', data, 'POST');

// 遥信转告警
const shakeInfoToAlarm = (data) => fetch('/biz/signal/convert/true', data, 'POST');

// 通过设备类型查询信号点信息
const queryNormalizedByDevType = (data) => fetch('/biz/signal/queryNormalizedByDevType', data, 'GET');

// 信号点归一化配置
const normalizedAdapter = (data, modeVersionCode) => fetch('/biz/signal/normalizedAdapter?modeVersionCode=' + modeVersionCode, data, 'POST');

// 归一化适配清除
const clearNormalizedAdapter = (data) => fetch('/biz/signal/clearNormalizedAdapter', data, 'POST');

// 查询设备类型
const getDevTypes = (data) => fetch('/dev/devManager/getAllDevType', data, 'POST');

// 查询已经配置的归一化信息
const getNormalizedInfo = (data) => fetch('/biz/signal/getNormalizedInfo', data, 'POST');

export default {
  getSignalModel,
  alarmToShakeInfo,
  shakeInfoToAlarm,
  queryNormalizedByDevType,
  normalizedAdapter,
  clearNormalizedAdapter,
  getDevTypes,
  getNormalizedInfo,
}
