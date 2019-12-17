import DataRecovery from './dataIntegrity/dataRecovery/en'
import KpiCorrection from './dataIntegrity/kpiCorrection/en'
import KpiRecalculation from './dataIntegrity/kpiRecalculation/en'
import EnterPrise from './enterprise/en'
import LicenceMannage from './licenceManage/en'
import Organization from './organization/en'
import AlarmConfig from './paramConfig/alrmConfig/en'
import PointMannage from './paramConfig/pointManage/en'
import StationParam from './paramConfig/stationParam/en'
import Account from './personRightsManager/account/en'
import Region from './personRightsManager/region/en'
import Role from './personRightsManager/role/en'
import DevConfig from './station-manage/devConfig/en'
import DevManage from './station-manage/devManage/en'
import Station from './station-manage/station/en'
import StationBinding from './station-manage/stationBinding/en'
import SystemParam from './systemParam/en'
import DevUpgrade from './station-manage/devUpgrade/en'
// 设置界面的国际化 英文
export default {
  settings: {
  },
  ...DataRecovery,
  ...KpiRecalculation,
  ...KpiCorrection,
  ...EnterPrise,
  ...LicenceMannage,
  ...Organization,
  ...AlarmConfig,
  ...PointMannage,
  ...StationParam,
  ...Account,
  ...Role,
  ...Region,
  ...DevConfig,
  ...DevManage,
  ...Station,
  ...StationBinding,
  ...SystemParam,
  ...DevUpgrade
}
