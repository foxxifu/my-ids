// 设备管理的接口
import fetch from '@/utils/fetch'

// 获取设备对比的数据(设备id之间使用逗号隔开)
const deviceComparisonTable = (data) => fetch('/dev/devManager/deviceComparisonTable', data, 'POST')
// 查询设备信号点信息
const getDeviceSignal = (data) => fetch('/dev/devManager/getDeviceSignal', data, 'POST')
// 查询设备信号点信息 查询图表的信息
const deviceComparisonChart = (data) => fetch('/dev/devManager/deviceComparisonChart', data, 'POST')
// 获取所有设备类型
const getAllDevType = (data) => fetch('/dev/devManager/getAllDevType', data, 'POST')

export default {
  deviceComparisonTable,
  getDeviceSignal,
  deviceComparisonChart,
  getAllDevType
}
