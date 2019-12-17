import fetch from '@/utils/fetch'

// 获取数采信息
const getSNDevList = (data) => fetch('/biz/devAccess/getSNDevList', data, 'POST')
// 获取上能协议类型的版本信息
const getSNSignalVersionList = (data) => fetch('/biz/devAccess/getSNSignalVersionList', data, 'POST')
// 获取MQQT的版本信息
const getMqqtVersionList = (data) => fetch('/biz/devAccess/getMqqtVersionList', data, 'POST')
// 验证设备输入的sn号的正确性
const checkAndQuerySnInfo = (data) => fetch('/biz/devAccess/checkAndQuerySnInfo', data, 'POST')
// 新增上能设备
const insertDevInfo = (data) => fetch(' /biz/devAccess/insertDevInfo', data, 'POST')
// 获取当前电站下所有的mqqt的父sn号
const getMqqtParentSnList = (data) => fetch('/biz/devAccess/getMqqtParentSnList', data, 'GET')
// 新增mqqt下挂设备
const insertMqqtDevInfo = (data) => fetch('/biz/devAccess/insertMqqtDevInfo', data, 'POST')
// 新增modbus协议的设备
const insertModbusDevInfo = (data) => fetch('/biz/devAccess/insertModbusDevInfo', data, 'POST')
// 获取modbus协议的父设备信息SN号的信息
const getModbusParentSnList = (data) => fetch('/biz/devAccess/getModbusParentSnList', data, 'POST')

export default {
  getSNDevList,
  getSNSignalVersionList,
  checkAndQuerySnInfo,
  insertDevInfo,
  getMqqtVersionList,
  insertMqqtDevInfo,
  getMqqtParentSnList,
  insertModbusDevInfo,
  getModbusParentSnList
}
