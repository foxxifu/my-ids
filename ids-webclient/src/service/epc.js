// 单电站功能请求
import fetch from '@/utils/fetch'

// 获取厂家结构树
const getManufacturerTree = (data) => fetch('/api/epc/getManufacturerTree', data, 'POST');

// 获取资产数据
const getAssetsOfManufacturer = (data) => fetch('/api/epc/getAssetsOfManufacturer', data, 'POST');

// 获取厂家结构树
const getExchangeOfGoodsTree = (data) => fetch('/api/epc/getExchangeOfGoodsTree', data, 'POST');

// 获取退换货数据
const getExchangeOfGoodsOfManufacturer = (data) => fetch('/api/epc/getExchangeOfGoodsOfManufacturer', data, 'POST');

// 获取统计数据
const getStatisticsData = (data) => fetch('/api/epc/getStatisticsData', data, 'POST');

// 获取项目数据
const getProjectTableData = (data) => fetch('/api/epc/getProjectTableData', data, 'POST');

// 获取项目选择数据
const getProjectSelectData = (data) => fetch('/api/epc/getProjectSelectData', data, 'POST');

export default {
  getManufacturerTree,
  getAssetsOfManufacturer,
  getExchangeOfGoodsTree,
  getExchangeOfGoodsOfManufacturer,
  getStatisticsData,
  getProjectTableData,
  getProjectSelectData
}
