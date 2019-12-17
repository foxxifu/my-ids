const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')
// 电站日月年报表
const StationReport = resolve => require.ensure([], () => resolve(require('@/pages/pm/report-manage/index.vue')), 'stationReport')

export default [
  {
    path: '/production',
    component: Layout,
    name: 'menu.pm',
    redirect: '/production/reportManage',
    icon: 'icon-pm',
    type: 'menu',
    children: [
      {
        path: 'reportManage',
        component: StationReport,
        icon: 'icon-reportManage',
        meta: {role: ['pm']}
      },
    ]
  },
]
