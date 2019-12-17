import fetch from '@/utils/fetch'

// 根据企业ID或者区域ID查询所有的直接子区域
const getDomainsByParentId = (data) => fetch('/biz/domain/getDomainsByParentId', data, 'POST');

// 插入区域
const insertDomain = (data) => fetch('/biz/domain/insertDomain', data, 'POST');

// 根据id更新区域
const updateDomain = (data) => fetch('/biz/domain/updateDomain', data, 'POST');

// 根据id查询区域
const getDomainById = (data) => fetch('/biz/domain/getDomainById', data, 'POST');

// 根据id删除区域
const deleteDomainById = (data) => fetch('/biz/domain/deleteDomainById', data, 'POST');

export default {
  getDomainsByParentId,
  insertDomain,
  updateDomain,
  getDomainById,
  deleteDomainById,
}
