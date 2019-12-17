const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')
const Index = resolve => require.ensure([], () => resolve(require('@/pages/io/index.vue')), 'index')

export default [
  {
    path: '/operation',
    component: Layout,
    name: 'menu.io',
    redirect: '/operation/index',
    icon: 'icon-io',
    noDropdown: true,
    type: 'menu',
    children: [
      {
        path: 'index',
        component: Index,
        type: 'menu',
        meta: {role: ['io']}
      }
    ]
  }
]
