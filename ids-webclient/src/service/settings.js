import fetch from '@/utils/fetch'

/** 系统设置设备通讯管理配置----start */
// 获取设备通讯的分页数据
const getDevConfigByCondition = (data) => fetch('/dev/devManager/listDcConfig', data, 'POST');
// 修改设备通讯
const updateDevConfig = (data) => fetch('/dev/devManager/communicationConfig', data, 'POST');
/** 系统设置设备通讯管理配置----end */

/** 系统设置点表管理配置----start */
// 获取点表的分页数据
const getPointManageByCondition = (data) => fetch('/biz/signal/listSignalVersionInfo', data, 'POST');
// 删除点表
const delPointManage = (data) => fetch('/biz/signal/deleteSignalVersionById', data, 'POST');
// 获取点表详细信息
const getDevSignalInfo = (data) => fetch('biz/signal/listSignalInfo', data, 'POST');
const updatePointManage = (data) => fetch('/biz/signal/updateSignalInfo', data, 'POST');
/** 系统设置点表管理配置----start */

/** 系统设置设备通讯管理配置----start */
// 获取告警配置信息
const getAlarmConfigByCondition = (data) => fetch('/biz/settings/alarm/list', data, 'POST');
// 修改告警配置信息
const updateAlarmConfig = (data) => fetch('/biz/settings/alarm/update', data, 'POST');
/** 系统设置设备通讯管理配置----end */
/** 设备升级-------------start */
const getDevUpgradeDatasByCondition = (data) => fetch('/dev/device/getDevUpgradeDatas', data, 'POST');
// 执行升级
const upgradeDevByFile = (data) => fetch('/dev/device/doUpgrade', data, 'POST');
// 上传升级文件
const upgradeLoadFile = (data) => fetch('/dev/device/doUpgradeLoadFile', data, 'POST');
// 根据父设备id查询子设备
const getChildDevList = (data) => fetch('/dev/device/getChildDevList', data, 'POST');
/** 设备升级-------------end */

export default {
  getDevConfigByCondition,
  updateDevConfig,
  getPointManageByCondition,
  delPointManage,
  getDevSignalInfo,
  updatePointManage,
  getAlarmConfigByCondition,
  updateAlarmConfig,
  getDevUpgradeDatasByCondition,
  upgradeDevByFile,
  upgradeLoadFile,
  getChildDevList,
}
