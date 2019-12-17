import DataRecovery from './dataIntegrity/dataRecovery/zh'
import KpiCorrection from './dataIntegrity/kpiCorrection/zh'
import KpiRecalculation from './dataIntegrity/kpiRecalculation/zh'
import EnterPrise from './enterprise/zh'
import LicenceMannage from './licenceManage/zh'
import Organization from './organization/zh'
import AlarmConfig from './paramConfig/alrmConfig/zh'
import PointMannage from './paramConfig/pointManage/zh'
import StationParam from './paramConfig/stationParam/zh'
import Account from './personRightsManager/account/zh'
import Region from './personRightsManager/region/zh'
import Role from './personRightsManager/role/zh'
import DevConfig from './station-manage/devConfig/zh'
import DevManage from './station-manage/devManage/zh'
import Station from './station-manage/station/zh'
import StationBinding from './station-manage/stationBinding/zh'
import SystemParam from './systemParam/zh'
import DevUpgrade from './station-manage/devUpgrade/zh'
// 设置界面的国际化 中文
export default {
  settings: {
  },
  ...DataRecovery,
  ...KpiCorrection,
  ...KpiRecalculation,
  ...EnterPrise,
  ...LicenceMannage,
  ...Organization,
  ...AlarmConfig,
  ...PointMannage,
  ...StationParam,
  ...Account,
  ...Region,
  ...Role,
  ...DevConfig,
  ...DevManage,
  ...Station,
  ...StationBinding,
  ...SystemParam,
  ...DevUpgrade
}
