// 单电站功能请求
import fetch from '@/utils/fetch'

// // 获取电站运行报表
// const getStationRunningReport = (data) => fetch('/api/reportManage/getStationRunningReport', data, 'POST');
//
// // 获取逆变器运行报表
// const getInverterRunningReport = (data) => fetch('/api/reportManage/getInverterRunningReport', data, 'POST');

// 获取电站运行报表
const getStationRunningReport = (data) => fetch('/biz/produceMng/listStationRpt', data, 'POST');

// 获取逆变器运行报表
const getInverterRunningReport = (data) => fetch('/biz/produceMng/listInverterRpt', data, 'POST');

// 获取子阵运行报表
const getSubarrayRunningReport = (data) => fetch('/biz/produceMng/listSubarrayRpt', data, 'POST');

// 导出电站运行报表
const exportStationRunningReport = (data) => fetch('/biz/produceMng/report/download/StationRuning', data, 'GET');

// 导出逆变器运行报表
const exportInverterRunningReport = (data) => fetch('/biz/produceMng/exportInverterRpt', data, 'GET');

// 导出子阵运行报表
const exportSubarrayRunningReport = (data) => fetch('/biz/produceMng/exportSubarrayRpt', data, 'GET');

// 获取逆变器详细运行报表
const getInverterDetailReport = (data) => fetch('/biz/produceMng/listInverterDetailRpt', data, 'POST');

// 获取逆变器详细运行报表，设备数据
const getDevices = (data) => fetch('/biz/produceMng/listInverterDevices', data, 'POST');

// 导出逆变器详细报表
const exportInverterDetail = (data) => fetch('/biz/produceMng/exportInverterDetailRpt', data, 'GET');

export default {
  getStationRunningReport,
  getInverterRunningReport,
  getSubarrayRunningReport,
  exportStationRunningReport,
  exportInverterRunningReport,
  exportSubarrayRunningReport,
  getInverterDetailReport,
  getDevices,
  exportInverterDetail
}
