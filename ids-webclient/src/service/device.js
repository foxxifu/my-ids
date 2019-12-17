import fetch from '@/utils/fetch'

// 获取设备列表
const getDeviceByCondition = (data) => fetch('/dev/device/getDeviceByCondition', data, 'POST')
// 根据id获取设备
const getDevById = (devId) => fetch('/dev/device/getDeviceById', {id: devId}, 'POST')
// 查询设备版本信息
const getModelVersionInfos = () => fetch('/dev/device/getModelVersionInfos', {}, 'POST')
// 查询组串的详情信息
const getStationPvModuleDetail = (data) => fetch('/dev/device/getStationPvModuleDetail', data, 'POST')
// 查询组件厂家的简单信息列表
const findFactorys = (data) => fetch('/dev/device/findFactorys', data, 'POST')
// 保存组串详情的信息
const saveStationPvModule = (data) => fetch('/dev/device/saveStationPvModule', data, 'POST')
// 保存设备信息
const updateDeviceById = (data) => fetch('/dev/device/updateDeviceById', data, 'POST')
// 获取当前未绑定的直流汇流箱和当前集中式逆变器绑定的直流汇流箱
const getDCJSIdAndName = (data) => fetch('/dev/device/getDCJSIdAndName', data, 'POST')
// 获取直流汇流箱配置的信息
const getDCJSDetail = (data) => fetch('/dev/device/getDCJSDetail', data, 'POST')
// 获取直流汇流箱设备的接口
const getDCJSByPage = (data) => fetch('/dev/device/getDCJSByPage', data, 'POST')
// 保存集中式逆变器与组串式逆变器的关系
const saveDCJSShip = (data) => fetch('/dev/device/saveDCJSShip', data, 'POST')
// 获取保存的关系信息
const getDCJSByShip = (data) => fetch('/dev/device/getDCJSByShip', data, 'POST')
// 通知铁牛数采修改设备的与客户端的连接状态
const notifyTnDevUpdateChange = (data) => fetch('/dev/device/notifyTnDevUpdateChange', data, 'POST')
// 通过特牛数采的版本信息获取铁牛数采设备的id字符串
const getTnDevIdByVersionId = (data) => fetch('/dev/device/getTnDevIdByVersionId', data, 'POST')
// 清除版本的归一化缓存,主要是给biz模块修改了缓存之后调用的
const clearUnificationByModelVersion = (data) => fetch('/dev/device/clearCacheOfUnificationByModelVersion', data, 'POST')
// 当告警转遥信或者遥信转告警的修改信号点的信息
const resetMasterServiceSignals = (versionId) => fetch('/dev/device/resetMasterServiceSignals', {versonId: versionId}, 'POST')
// 通知清除mqtt的缓存，清除了缓存之后会去数据库里面查询，便于数据准确性
const clearMqqtToDbCache = () => fetch('/dev/device/clearMqqtToDbCache', {}, 'GET')
// 设备调试下发指令
const sendDevCmd = (data) => fetch('/dev/device/orderDev', data, 'POST')
// 获取所有的未绑定电站的MODBUS数采设备
const findAllUnBindSCdevs = () => fetch('/dev/device/findAllUnBindSCdevs', {}, 'GET')
// 将数采设备绑定到指定的电站上
const bindScToStation = (data) => fetch('/dev/device/bindScToStation', data, 'POST')
// 获取所有的MODBUS协议的数采设备
const findAllBindScDevs = () => fetch('/dev/device/findAllBindScDevs', {}, 'GET')
// 查询设备升级的信息
const findUpgradeDevInfos = (params) => fetch('/dev/device/findUpgradeDevInfos', params, 'GET')
// 删除modbus设备之后信号点缓存
const delModbusSignalLocalCatchBySns = (params) => fetch('/dev/device/delModbusSignalLocalCatchBySns', params, 'POST')

/**
 * 执行设备升级
 * @param devIds
 */
const devUpgrade = (devIds) => fetch('/dev/device/devUpgrade', {devIds: devIds}, 'POST')

export default {
  getDeviceByCondition,
  getDevById,
  getModelVersionInfos,
  getStationPvModuleDetail,
  findFactorys,
  saveStationPvModule,
  updateDeviceById,
  getDCJSIdAndName,
  getDCJSDetail,
  getDCJSByPage,
  saveDCJSShip,
  getDCJSByShip,
  notifyTnDevUpdateChange,
  getTnDevIdByVersionId,
  clearUnificationByModelVersion,
  resetMasterServiceSignals,
  clearMqqtToDbCache,
  sendDevCmd,
  findAllUnBindSCdevs,
  bindScToStation,
  findAllBindScDevs,
  findUpgradeDevInfos,
  devUpgrade,
  delModbusSignalLocalCatchBySns,
}
