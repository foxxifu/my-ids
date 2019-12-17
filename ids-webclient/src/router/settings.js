const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')

// 系统设置
const SettingsLayout = resolve => require.ensure([], () => resolve(require('@/pages/layout/settings.vue')), 'settingsLayout')

// 企业管理
const EnterpriseManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/enterprise/index.vue')), 'enterprise')

// 人员权限管理 -> 区域管理
const RegionManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/personRightsManager/region/index.vue')), 'region')

// 人员权限管理 -> 角色管理
const RoleManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/personRightsManager/role/index.vue')), 'role')

// 人员权限管理 -> 账号管理
const AccountManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/personRightsManager/account/index.vue')), 'account')

// 数据完整性 -> 数据补采
const DataRecovery = resolve => require.ensure([], () => resolve(require('@/pages/settings/dataIntegrity/dataRecovery/index.vue')), 'dataRecovery')

// 数据完整性 -> KPI修正
const KPICorrection = resolve => require.ensure([], () => resolve(require('@/pages/settings/dataIntegrity/kpiCorrection/index.vue')), 'kpiCorrection')

// 数据完整性 -> KPI重计算
const KPIRecalculation = resolve => require.ensure([], () => resolve(require('@/pages/settings/dataIntegrity/kpiRecalculation/index.vue')), 'kpiRecalculation')

// 参数配置 -> 点表管理
const PointManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/paramConfig/pointManage/index.vue')), 'pointManage')

// 参数配置 -> 告警设置
const AlarmConfig = resolve => require.ensure([], () => resolve(require('@/pages/settings/paramConfig/alarmConfig/index.vue')), 'alarmConfig')

// 参数配置 -> 电站参数
const StationParam = resolve => require.ensure([], () => resolve(require('@/pages/settings/paramConfig/stationParam/index.vue')), 'stationParam')

// 电站管理 -> 电站信息
const StationManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/station-manage/station/index.vue')), 'stationManage')

// 电站管理 -> 设备管理
const DevManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/station-manage/devManage/index.vue')), 'devManage')
// 电站管理 -> 设备升级
const DevUpgrade = resolve => require.ensure([], () => resolve(require('@/pages/settings/station-manage/devUpgrade/index.vue')), 'devUpgrade')

// 电站管理 -> 设备通讯
const DevConfig = resolve => require.ensure([], () => resolve(require('@/pages/settings/station-manage/devConfig/index.vue')), 'devConfig')

// 票据管理 -> 电气一种工作票
// const OneWorkTicket = resolve => require.ensure([], () => resolve(require('@/pages/settings/voucherManage/oneWorkTicket/index.vue')), 'oneWorkTicket')
// // 票据管理 -> 电气一种工作票
// const TwoWorkTicket = resolve => require.ensure([], () => resolve(require('@/pages/settings/voucherManage/twoWorkTicket/index.vue')), 'twoWorkTicket')
// // 票据管理 -> 操作票
// const OperationTicket = resolve => require.ensure([], () => resolve(require('@/pages/settings/voucherManage/operationTicket/index.vue')), 'operationTicket')
// // 票据管理 -> 操作票模板
// const OperationTicketTemplate = resolve => require.ensure([], () => resolve(require('@/pages/settings/voucherManage/operationTicketTemplate/index.vue')), 'operationTicketTemplate')

// 系统参数
const SystemParam = resolve => require.ensure([], () => resolve(require('@/pages/settings/systemParam/index.vue')), 'systemParam')

// 组织架构
const Organization = resolve => require.ensure([], () => resolve(require('@/pages/settings/organization/index.vue')), 'organization')

// Licence管理
const LicenceManage = resolve => require.ensure([], () => resolve(require('@/pages/settings/licenceManage/index.vue')), 'licenceManage')

// 电站绑定
const stationBinding = resolve => require.ensure([], () => resolve(require('@/pages/settings/station-manage/stationBinding/index.vue')), 'stationBinding')

export default [
  {
    path: '/settings',
    name: 'menu.settings.title',
    redirect: '/settings/enterprise/organization',
    icon: 'icon-settings',
    type: 'settings',
    component: Layout,
    children: [
      { // 企业管理
        path: '/settings/enterprise',
        name: 'menu.settings.enterprise.title',
        icon: 'icon-enterpriseManage',
        redirect: '/settings/enterprise/index',
        component: SettingsLayout,
        children: [
          {
            path: 'index',
            name: 'menu.settings.enterprise.qy',
            component: EnterpriseManage,
            meta: {role: ['enterprise'], showSidebar: true},
          },
          {
            path: 'organization',
            name: 'menu.settings.enterprise.zzjg',
            component: Organization,
            meta: {role: ['organization'], showSidebar: true},
          },
        ],
      },
      { // 人员权限管理
        path: '/settings/personRightsManager',
        name: 'menu.settings.qxry.title',
        redirect: '/settings/personRightsManager/region',
        icon: 'icon-personRightsManager',
        component: SettingsLayout,
        children: [
          { // 区域管理
            path: 'region',
            name: 'menu.settings.qxry.domain',
            component: RegionManage,
            meta: {
              role: ['regionManager'],
              showSidebar: true // 显示左边菜单栏
            },
          },
          { // 角色管理
            path: 'role',
            name: 'menu.settings.qxry.role',
            component: RoleManage,
            meta: {
              role: ['roleManager'],
              showSidebar: true
            },
          },
          { // 账号管理
            path: 'account',
            name: 'menu.settings.qxry.account',
            component: AccountManage,
            meta: {
              role: ['accountManager'],
              showSidebar: true
            },
          },
        ]
      },
      { // 数据完整性
        path: '/settings/dataIntegrity',
        name: 'menu.settings.sjwzx.title',
        redirect: '/settings/dataIntegrity/dataRecovery',
        icon: 'icon-dataIntegrity',
        component: SettingsLayout,
        children: [
          { // 数据补采
            path: 'dataRecovery',
            name: 'menu.settings.sjwzx.sjbc',
            component: DataRecovery,
            meta: {
              role: ['dataRecovery'],
              showSidebar: true // 显示左边菜单栏
            },
          },
          { // KPI修正
            path: 'kpiCorrection',
            name: 'menu.settings.sjwzx.xz',
            component: KPICorrection,
            meta: {
              role: ['kpiCorrection'],
              showSidebar: true // 显示左边菜单栏
            },
          },
          { // KPI重计算
            path: 'kpiRecalculation',
            name: 'menu.settings.sjwzx.cjs',
            component: KPIRecalculation,
            meta: {
              role: ['kpiRecalculation'],
              showSidebar: true // 显示左边菜单栏
            },
          }
        ]
      },
      { // 参数配置
        path: '/settings/paramConfig',
        name: 'menu.settings.params.title',
        redirect: '/settings/paramConfig/pointManage',
        icon: 'icon-paramConfig',
        component: SettingsLayout,
        children: [
          { // 点表管理
            path: 'pointManage',
            name: 'menu.settings.params.pointManage',
            component: PointManage,
            meta: {
              role: ['pointManage'],
              showSidebar: true // 显示左边菜单栏
            },
          },
          { // 告警设置
            path: 'alarmConfig',
            name: 'menu.settings.params.alarmSetting',
            component: AlarmConfig,
            meta: {
              role: ['alarmConfig'],
              showSidebar: true // 显示左边菜单栏
            },
          },
          { // 电站参数
            path: 'stationParam',
            name: 'menu.settings.params.stationParam',
            component: StationParam,
            meta: {
              role: ['stationParam'],
              showSidebar: true // 显示左边菜单栏
            },
          }
        ]
      },
      {
        path: '/settings/stationManage',
        name: 'menu.settings.station.title',
        redirect: '/settings/stationManage/station',
        icon: 'icon-stationManage',
        component: SettingsLayout,
        children: [
          { // 电站信息
            path: 'station',
            name: 'menu.settings.station.info',
            component: StationManage,
            meta: {
              role: ['stationManage'],
              showSidebar: true
            },
          },
          { // 电站绑定
            path: 'stationBinding',
            name: 'menu.settings.station.bind',
            component: stationBinding,
            meta: {
              role: ['stationManage'],
              showSidebar: true
            },
          },
          { // 设备管理
            path: 'devManage',
            name: 'menu.settings.station.dev',
            component: DevManage,
            meta: {
              role: ['devManage'],
              showSidebar: true
            },
          },
          { // 设备升级
            path: 'devUpgrade',
            name: 'menu.settings.station.devUpgrade',
            component: DevUpgrade,
            meta: {
              role: ['devUpgrade'],
              showSidebar: true
            },
          },
          { // 设备通讯
            path: 'devConfig',
            name: 'menu.settings.station.sbtx',
            component: DevConfig,
            meta: {
              role: ['devConfig'],
              showSidebar: true
            },
          },
        ]
      },
      // { // 票据管理
      //   path: '/settings/voucherManage',
      //   name: 'settings.voucherManage.title',
      //   icon: 'icon-voucherManage',
      //   redirect: '/settings/voucherManage/oneWorkTicket',
      //   component: SettingsLayout,
      //   children: [
      //     {
      //       path: 'oneWorkTicket',
      //       name: 'settings.voucherManage.oneWorkTicket.title',
      //       component: OneWorkTicket,
      //       meta: {role: ['oneWorkTicket'], showSidebar: true},
      //     },
      //     {
      //       path: 'twoWorkTicket',
      //       name: 'settings.voucherManage.twoWorkTicket.title',
      //       component: TwoWorkTicket,
      //       meta: {role: ['twoWorkTicket'], showSidebar: true},
      //     },
      //     {
      //       path: 'operationTicket',
      //       name: 'settings.voucherManage.operationTicket.title',
      //       component: OperationTicket,
      //       meta: {role: ['operationTicket'], showSidebar: true},
      //     },
      //     {
      //       path: 'operationTicketTemplate',
      //       name: 'settings.voucherManage.operationTicketTemplate.title',
      //       component: OperationTicketTemplate,
      //       meta: {role: ['operationTicketTemplate'], showSidebar: true},
      //     },
      //   ],
      // },
      { // 系统参数
        path: '/settings/systemParam',
        name: 'menu.settings.systemParam',
        icon: 'icon-systemParam',
        redirect: '/settings/systemParam/index',
        component: SettingsLayout,
        noDropdown: true, // 没有二级菜单
        children: [
          {
            path: 'index',
            component: SystemParam,
            meta: {role: ['systemParam'], showSidebar: true},
          },
        ],
      },
      { // Licence管理
        path: '/settings/licenceManage',
        name: 'menu.settings.licence',
        icon: 'icon-licenceManage',
        redirect: '/settings/licenceManage/index',
        component: SettingsLayout,
        noDropdown: true, // 没有二级菜单
        children: [
          {
            path: 'index',
            component: LicenceManage,
            meta: {role: ['licenceManage'], showSidebar: true},
          },
        ],
      }
    ]
  }
]
