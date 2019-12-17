const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')
// 个人任务
const Todo = resolve => require.ensure([], () => resolve(require('@/pages/todo/index.vue')), 'todo')

export default [
  {
    path: '/todo',
    type: 'link',
    component: Layout,
    children: [
      {
        path: '',
        name: 'menu.todo',
        component: Todo,
        meta: {role: ['todo']},
      },
    ]
  }
]
