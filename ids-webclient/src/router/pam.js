const Layout = resolve => require.ensure([], () => resolve(require('@/pages/layout/index.vue')), 'layout')

// // 资产(厂家)
// const AssetsOfManufacturer = resolve => require.ensure([], () => resolve(require('@/pages/pam/assetsOfManufacturer/index.vue')), 'assetsOfManufactor')
//
// // 换退货(厂家)
// const ExchangeOfGoodsOfManufacturer = resolve => require.ensure([], () => resolve(require('@/pages/pam/exchangeOfGoodsOfManufacturer/index.vue')), 'exchangeOfGoodsOfManufactor')
//
// // 统计(厂家)
// const StatisticsOfManufacturer = resolve => require.ensure([], () => resolve(require('@/pages/pam/statisticsOfManufacturer/index.vue')), 'statisticsOfManufactor')
//
// // 资产(EPC)
// const AssetsOfepc = resolve => require.ensure([], () => resolve(require('@/pages/pam/assetsOfepc/index.vue')), 'assetsOfepc')
//
// // 换退货(EPC)
// const ExchangeOfGoodsOfepc = resolve => require.ensure([], () => resolve(require('@/pages/pam/exchangeOfGoodsOfepc/index.vue')), 'exchangeOfGoodsOfepc')
//
// // 统计(EPC)
// const StatisticsOfepc = resolve => require.ensure([], () => resolve(require('@/pages/pam/statisticsOfepc/index.vue')), 'statisticsOfepc')

const epc = resolve => require.ensure([], () => resolve(require('@/pages/pam//index.vue')), 'epc')

// let isEpc = false; // 用户角色是否是EPC(根据角色的不同进入不同的界面)

// let Assets = null;
// let ExchangeOfGoods = null;
// let Statistics = null;

// if (!isEpc) {
//   Assets = {
//     path: '/poverty/assets',
//     name: 'pam.assets',
//     component: AssetsOfManufacturer,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
//   ExchangeOfGoods = {
//     path: '/poverty/exchangeOfGoods',
//     name: 'pam.exchangeOfGoods',
//     component: ExchangeOfGoodsOfManufacturer,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
//   Statistics = {
//     path: '/poverty/statistics',
//     name: 'pam.statistics',
//     component: StatisticsOfManufacturer,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
// } else {
//   Assets = {
//     path: '/poverty/assets',
//     name: 'pam.assets',
//     component: AssetsOfepc,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
//   ExchangeOfGoods = {
//     path: '/poverty/exchangeOfGoods',
//     name: 'pam.exchangeOfGoods',
//     component: ExchangeOfGoodsOfepc,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
//   Statistics = {
//     path: '/poverty/statistics',
//     name: 'pam.statistics',
//     component: StatisticsOfepc,
//     meta: {role: ['pam'], showSidebar: 'epc'}
//   };
// }

let epcC = {
  path: '/poverty/epc',
  name: '',
  component: epc,
  meta: {role: ['pam']}
};

export default [
  {
    path: '/poverty',
    component: Layout,
    name: 'pam.title',
    redirect: '/poverty/epc',
    icon: 'icon-pam',
    type: 'menu',
    filterType: 'epc',
    children: [
      epcC,
      // Assets,
      // ExchangeOfGoods,
      // Statistics,
    ]
  },
]
