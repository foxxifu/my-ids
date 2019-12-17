const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')
const Index = resolve => require.ensure([], () => resolve(require('@/pages/station/index.vue')), 'index')
const Equipment = resolve => require.ensure([], () => resolve(require('@/pages/station/equipment/index.vue')), 'equipment')
const Alarm = resolve => require.ensure([], () => resolve(require('@/pages/station/alarm/index.vue')), 'alarm')
const Report = resolve => require.ensure([], () => resolve(require('@/pages/station/report/index.vue')), 'report')

export default [
  {
    path: '/station',
    type: 'stationMenu',
    component: Layout,
    redirect: '/station/index',
    children: [
      {
        path: '/station/index',
        icon: 'icon-index',
        noDropdown: true,
        type: 'stationMenu',
        component: Index,
        name: 'menu.station.index',
        meta: {role: ['station_home'], showNav: true}
      },
      {
        path: '/station/equipment',
        icon: 'icon-equipment',
        noDropdown: true,
        type: 'stationMenu',
        component: Equipment,
        name: 'menu.station.equipment',
        // meta: {role: ['station_equipment']}
        meta: {role: ['station_home']}
      },
      {
        path: '/station/alarm',
        icon: 'icon-alarm',
        noDropdown: true,
        type: 'stationMenu',
        component: Alarm,
        name: 'menu.station.alarm',
        // meta: {role: ['station_alarm']}
        meta: {role: ['station_home']}
      },
      {
        path: '/station/report',
        icon: 'icon-report',
        noDropdown: true,
        type: 'menu',
        component: Report,
        name: 'menu.station.report',
        // meta: {role: ['station_report']}
        meta: {role: ['station_home']}
      },
    ]
  }
]
