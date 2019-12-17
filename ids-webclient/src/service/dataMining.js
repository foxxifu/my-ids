import fetch from '@/utils/fetch'
// 获取补采数据的分页信息
const findPage = (data) => fetch('/dev/dataMining/findPage', data, 'POST')
// 保存补采的数据
const saveData = (data) => fetch('/dev/dataMining/saveData', data, 'POST')
// 执行补采任务 发送执行补采任务的操作
const reloadData = (taskId) => fetch('/dev/dataMining/reloadData', {id: taskId}, 'POST')
// 删除补采任务
const deleteTask = (taskId) => fetch('/dev/dataMining/deleteTask', {id: taskId}, 'POST')

export default {
  findPage,
  saveData,
  reloadData,
  deleteTask
}
