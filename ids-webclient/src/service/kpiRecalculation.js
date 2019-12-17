// KPI重新计算功能相关请求
import fetch from '@/utils/fetch'

// 获取重算任务分页数据
const getCalcTask = (data) => fetch('/biz/kpiRevise/queryCalcTask', data, 'POST')
// 创建任务
const createCalcTask = (data) => fetch('/biz/kpiRevise/createCalcTask', data, 'POST')
// 修改企业
const modifyCalcTask = (data) => fetch('/biz/kpiRevise/modifyCalcTask', data, 'POST')
// 删除企业
const removeCalcTask = (data) => fetch('/biz/kpiRevise/removeCalcTask', data, 'POST')
// 执行任务
const executeCalcTask = (data) => fetch('/biz/kpiRevise/executeCalcTask', data, 'POST')
// 获得服务端context
const getWebsocketUrl = () => fetch('/biz/getWebsocketUrl', {}, 'GET')

export default {
  getCalcTask,
  createCalcTask,
  modifyCalcTask,
  removeCalcTask,
  executeCalcTask,
  getWebsocketUrl
}
