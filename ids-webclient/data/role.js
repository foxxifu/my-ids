module.exports.R = {
  getRoleMByPage: {
    code: 1,
    results: {
      count: 100,
      list: [
        {id: 1, name: '超级管理员', description: '描述信息', roleType: 'system', status: '0', createDate: new Date().getTime()},
        {id: 2, name: '企业管理员', description: '描述信息', roleType: 'enterprise', status: '0', createDate: new Date().getTime()},
        {id: 3, name: '管理员', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
        {id: 4, name: '管理员2', description: '描述信息', roleType: 'normal', status: '1', createDate: new Date().getTime()},
        {id: 5, name: '管理员3', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
        {id: 6, name: '管理员4', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
        {id: 7, name: '管理员5', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
        {id: 8, name: '管理员6', description: '描述信息', roleType: 'normal', status: '1', createDate: new Date().getTime()},
        {id: 9, name: '管理员7', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
        {id: 10, name: '管理员8', description: '描述信息', roleType: 'normal', status: '1', createDate: new Date().getTime()},
        {id: 11, name: '管理员9', description: '描述信息', roleType: 'normal', status: '0', createDate: new Date().getTime()},
      ]
    }
  },
  getRoleMById: { // 根据角色id获取角色信息
    code: 1,
    results: {
      "createDate": 1512375367000,
      "createUserId": 1,
      "description": "22233333",
      "enterpriseId": 2,
      "id": 2,
      "modifyDate": 1512375367000,
      "modifyUserId": 2,
      "name": "管理员1",
      "roleType": "normal",
      "status": 0,
      authIds: '1001,1002,1003' // 当前角色具有的权限
    }
  },
  insertRole: {
    code: 1
  },
  updateRoleMById: {
    code: 1
  },
  updateRoleMStatus: {
    code: 1
  }
}
