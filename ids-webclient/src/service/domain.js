import fetch from '@/utils/fetch'
// 插入区域
const insertDomain = (data) => fetch('/biz/domain/insertDomain', data, 'POST');

// 根据id更新区域
const updateDomain = (data) => fetch('/biz/domain/updateDomain', data, 'POST');

// 根据id查询区域
const getDomainById = (data) => fetch('/biz/domain/getDomainById', data, 'POST');

// 根据id删除区域
const deleteDomainById = (data) => fetch('/biz/domain/deleteDomainById', data, 'POST');

// 全部加载区域形成树形节点
const getDomainTree1 = (data) => fetch('/biz/domain/getDomainTree1', data, 'POST');

export default {
  insertDomain,
  updateDomain,
  getDomainById,
  deleteDomainById,
  getDomainTree1,
}
