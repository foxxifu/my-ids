import fetch from '@/utils/fetch'

// 电站月度分析
const getStationMonthAnalysis = (data) => fetch('/biz/compontAnalysis/getStationMouthAnalysis', data, 'POST')

// 电站子阵级月度分析
const getSubarrayMonthAnalysis = (data) => fetch('/biz/compontAnalysis/getCompontAnalysisMouth', data, 'POST')

// 电站年度分析
const getStationYearAnalysis = (data) => fetch('/biz/compontAnalysis/getStationYearAnalysis', data, 'POST')

// 电站子阵级年度分析
const getSubarrayYearAnalysis = (data) => fetch('/biz/compontAnalysis/getCompontAnalysisYear', data, 'POST')

// 电站日度分析
const getStationDateAnalysis = (data) => fetch('/biz/compontAnalysis/getStationAnalysisDay', data, 'POST')

// 电站子阵级日度分析
const getSubarrayDateAnalysis = (data) => fetch('/biz/compontAnalysis/getCompontAnalysisDay', data, 'POST')

// 分析报表导出
const exportData = (data) => fetch('/biz/compontAnalysis/exportData', data, 'POST')

// 逆变器组串离散率分析
const getScatterAnalysis = (data) => fetch('/biz/compontAnalysis/getInverterDiscreteRate', data, 'POST')

// 直流汇流箱组串离散率分析
const getScatterDCAnalysis = (data) => fetch('/biz/compontAnalysis/getCombinerdcDiscreteRate', data, 'POST')

export default {
  getStationMonthAnalysis,
  getSubarrayMonthAnalysis,
  getStationYearAnalysis,
  getSubarrayYearAnalysis,
  getStationDateAnalysis,
  getSubarrayDateAnalysis,
  exportData,

  getScatterAnalysis,
  getScatterDCAnalysis,
}
