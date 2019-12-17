import fetch from '@/utils/fetch'

const PRE = '/biz/workTicket/'

// 条件查询电气一种工作票
const getOneTicketList = (data) => fetch(PRE + 'getWorkTicketList', data, 'POST')
// 条件查询电气一种工作票
const getOneTicket = (data) => fetch(PRE + 'getWorkTicketByDefinitionId', data, 'POST')
// 新增电气一种工作票
const saveOneWorkTicket = (data) => fetch(PRE + 'save', data, 'POST')
// 修改电气一种工作票
const updateOneWorkTicket = (data) => fetch(PRE + 'updateWorkTicket', data, 'POST')

// 新增电气二种工作票
const saveTwoWorkTicket = (data) => fetch(PRE + 'save', data, 'POST')
// 新增电气二种工作票
const executeWorkTicket = (data) => fetch(PRE + 'executeWorkTicket', data, 'POST')

export default {
  getOneTicketList,
  getOneTicket,
  saveOneWorkTicket,
  saveTwoWorkTicket,
  updateOneWorkTicket,
  executeWorkTicket
}
