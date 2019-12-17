// 大屏界面
const Exhibition = resolve => require.ensure([], () => resolve(require('@/pages/exhibition/index.vue')), 'exhibition')

export default [
  {
    path: '/exhibition',
    name: '大屏',
    type: 'link',
    component: Exhibition,
    // meta: {role: ['exhibition']}
  }
]
