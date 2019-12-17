import ComAna from './componentAnalysis/zh'
import ScaAna from './scatterAnalysis/zh'
import DCAna from './scatterDCAnalysis/zh'
// 智慧应用的国际化 中文
export default {
  wisdom: {
    inventerLsl: '逆变器组串离散率',
    zlhlxZcLsl: '直流汇流箱组串离散率',
    pvZd: '组串诊断',
  },
  ...ComAna,
  ...ScaAna,
  ...DCAna
}
