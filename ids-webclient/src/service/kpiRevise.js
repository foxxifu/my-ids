import fetch from '@/utils/fetch'

// 获取KPI修正数据
const getKpiReviseTByCondtion = (data) => fetch('/biz/kpiRevise/getKpiReviseTByCondtion', data, 'POST');

// 新增KPI修正
const saveKpiReviseT = (data) => fetch('/biz/kpiRevise/saveKpiReviseT', data, 'POST');

// 修改KPI修正
const updateKpiReviseT = (data) => fetch('/biz/kpiRevise/updateKpiReviseT', data, 'POST');

// 同步KPI修正
const kpiSyncronize = (data) => fetch('/biz/kpiRevise/kpiSyncronize', data, 'POST');

export default {
  getKpiReviseTByCondtion,
  saveKpiReviseT,
  updateKpiReviseT,
  kpiSyncronize,
}
