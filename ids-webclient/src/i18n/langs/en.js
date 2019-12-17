import enLocale from 'element-ui/lib/locale/lang/en';
// 设备管理界面的URL到路由的那一层结束
import Home from './home/en'
import Io from './io/en'
import Login from './login/en'
import Pm from './pm/en'
import Settings from './settings/en'
import Station from './station/en'
import Todo from './todo/en'
import Exhibition from './exhibition/en'
import Layout from './layout/en'
import Wisdom from './wisdom/en'
import DevTypeId from './devTypeId/en'
import Menu from './menu/en'
import PageRole from './pageRole/en'
import Components from './components/en'
import DataConverter from './dataConverter/en'

export default {
  systemName: 'Smart Energy System',
  exhibition_systemName: 'Smart New Energy Cloud Center',
  // systemName: 'Sienergy 智享能源云平台',
  // exhibition_systemName: 'Sienergy 智享能源云中心',
  copyright: '@2019 All Rights Reserved',
  about: 'Smart New Energy Management System R&D Department',
  sure: 'Confirm',
  cancel: 'Cancel',
  close: 'Close',
  confirm: 'Warning',
  search: 'Search',
  create: 'New',
  copy: 'Copy',
  update: 'Modify',
  modify: 'Modify',
  delete: 'Delete',
  detail: 'Detail',
  export: 'Export',
  print: 'Print',
  operatSuccess: 'Successful operation',
  operatFailed: 'Operation failed',
  selectOneRecord: 'Please select a record that you want to work with.',
  deleteCancel: 'Undelete',
  deleteSuccess: 'Successfully deleted!',
  deleteMsg: 'This action will permanently delete the data. Do you want to continue?',
  serviceError: 'Service exception',
  unit: {
    colon: '：',
    brackets: ['(', ')'],
    KWh: 'kWh',
    MWh: 'MWh',
    WKWh: '*10kWh',
    GWh: 'GWh',
    KW: 'kW',
    MW: 'MW',
    GW: 'GW',
    RMBUnit: '$',
    RMBUnit2: '$',
    KRMBUnit: '$',
    WRMBUnit: '$',
    co2Unit: 't',
    coalUnit: 't',
    treeUnit: 'One',
    co2WUnit: '*10kt',
    coalWUnit: '*10kt',
    treeWUnit: '*10km³',
    days: 'day',
    door: 'Household',
    inverterUnit: 'Station',
    temperatureUnit: '℃',
  },
  dateFormat: {
    yyyymmddhhmmss: 'MM/dd/yyyy HH:mm:ss',
    yyyymmdd: 'MM/dd/yyyy',
    mmdd: 'MM/dd',
    yyyymm: 'MM/yyyy',
    yyyy: 'yyyy',
  },
  ...enLocale,
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
