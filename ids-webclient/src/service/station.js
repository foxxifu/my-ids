/**
 * 电站相关接口请求
 */
import fetch from '@/utils/fetch'

// 修改电站信息接口
const updateStationInfoMById = (data) => fetch('/biz/station//updateStationInfoMById', data, 'POST')

// 删除电站信息接口
const deleteStationInfosByIds = (data) => fetch('/biz/station/deleteStationInfosByIds', data, 'POST')

// 单电站查询接口
const getStationByCode = (data) => fetch('/biz/station/getStationByCode', data, 'POST')

// 新增电站信息接口
const insertStation = (data) => fetch('/biz/station/insertStation', data, 'POST')

// 查询环境检测仪电站接口
const getStationInfoByEmiId = (data) => fetch('/biz/station/getStationInfoByEmiId', data, 'POST')

// 环境检测仪信息查询接口
const getEmiInfoByStationCode = (data) => fetch('/biz/station/getEmiInfoByStationCode', data, 'POST')

// 电价查询接口
const getPowerPriceById = (data) => fetch('/biz/station/getPowerPriceById', data, 'POST')

// 电站信息列表查询接口
const getStationInfo = (data) => fetch('/biz/station/getStationInfo', data, 'POST')

// 电站名称验证接口
const checkNameIsExists = (data) => fetch('/biz/station/checkNameIsExists', data, 'POST')

// 组织结构查询接口
const getUserDomainTree = (data) => fetch('/biz/station/getUserDomainTree', data, 'POST')

// 设备列表查询接口
const getDevicesByStationCode = (data) => fetch('/biz/station/getDevicesByStationCode', data, 'POST')

// 设备检索接口
const getDeviceInfoBySN = (data) => fetch('/biz/station/getDeviceInfoBySN', data, 'POST')

// 设备版本号查询接口
const getModelVersionCodeList = (data) => fetch('/biz/station/getModelVersionCodeList', data, 'POST')

// 置共享环境检测仪接口
const saveShareEmi = (data) => fetch('/biz/station/saveShareEmi', data, 'POST')

// 获取电站电价
const getPricesByStationCode = (data) => fetch('/biz/station/getPricesByStationCode', data, 'POST')

// 获取未被绑定的电站数据
const getNoBindingStationInfoByPage = (data) => fetch('/biz/station/getNoBindingStationInfoByPage', data, 'POST')

export default {
  updateStationInfoMById,
  deleteStationInfosByIds,
  getStationByCode,
  insertStation,
  getStationInfoByEmiId,
  getEmiInfoByStationCode,
  getPowerPriceById,
  getStationInfo,
  checkNameIsExists,
  getUserDomainTree,
  getDevicesByStationCode,
  getDeviceInfoBySN,
  getModelVersionCodeList,
  saveShareEmi,
  getPricesByStationCode,
  getNoBindingStationInfoByPage,
}
