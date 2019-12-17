import ComAna from './componentAnalysis/en'
import ScaAna from './scatterAnalysis/en'
import DCAna from './scatterDCAnalysis/en'
// 智慧应用的国际化 英文
export default {
  wisdom: {
    inventerLsl: 'Inverter String Discrete Rate',
    zlhlxZcLsl: 'DC String Discrete Rate',
    pvZd: 'String Diagnosis',
  },
  ...ComAna,
  ...ScaAna,
  ...DCAna
}
