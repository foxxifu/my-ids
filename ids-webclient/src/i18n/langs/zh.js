import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
// 设备管理界面的URL到路由的那一层结束
import Home from './home/zh'
import Io from './io/zh'
import Login from './login/zh'
import Pm from './pm/zh'
import Settings from './settings/zh'
import Station from './station/zh'
import Todo from './todo/zh'
import Exhibition from './exhibition/zh'
import Layout from './layout/zh'
import Wisdom from './wisdom/zh'
import DevTypeId from './devTypeId/zh'
import Menu from './menu/zh'
import PageRole from './pageRole/zh'
import Components from './components/zh'
import DataConverter from './dataConverter/zh'

export default {
  systemName: '智慧新能源管理系统',
  exhibition_systemName: '智慧新能源云中心',
  // systemName: 'Sienergy 智享能源云平台',
  // exhibition_systemName: 'Sienergy 智享能源云中心',
  copyright: '@2018 版权所有',
  about: '智慧新能源管理系统研发部',
  sure: '确定',
  cancel: '取消',
  close: '关闭',
  confirm: '提示',
  search: '查询',
  create: '新增',
  copy: '复制',
  update: '修改',
  modify: '修改',
  delete: '删除',
  detail: '详情',
  export: '导出',
  print: '打印',
  operatSuccess: '操作成功',
  operatFailed: '操作失败',
  selectOneRecord: '请选择一条您要操作的记录。',
  deleteCancel: '已取消删除',
  deleteSuccess: '删除成功!',
  deleteMsg: '此操作将永久删除该数据, 是否继续?',
  serviceError: '服务异常',
  unit: {
    colon: '：',
    brackets: ['（', '）'],
    KWh: 'kWh',
    MWh: 'MWh',
    WKWh: '万kWh',
    GWh: 'GWh',
    KW: 'kW',
    MW: 'MW',
    GW: 'GW',
    RMBUnit: '元',
    RMBUnit2: '¥',
    KRMBUnit: '千元',
    WRMBUnit: '万元',
    co2Unit: 't',
    coalUnit: 't',
    treeUnit: '颗',
    co2WUnit: '万t',
    coalWUnit: '万t',
    treeWUnit: '万m³',
    days: '天',
    door: '户',
    inverterUnit: '台',
    temperatureUnit: '℃',
  },
  dateFormat: {
    yyyymmddhhmmss: 'yyyy-MM-dd HH:mm:ss',
    yyyymmdd: 'yyyy-MM-dd',
    mmdd: 'MM-dd',
    yyyy: 'yyyy',
    yyyymm: 'yyyy-MM',
  },
  ...zhLocale,
  ...Home,
  ...Io,
  ...Login,
  ...Pm,
  ...Settings,
  ...Station,
  ...Todo,
  ...Exhibition,
  ...Layout,
  ...Wisdom,
  ...DevTypeId,
  ...Menu,
  ...PageRole,
  ...Components,
  ...DataConverter
}
