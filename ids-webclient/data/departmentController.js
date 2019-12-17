module.exports.R = {
  getEnterprise: function (params) {
    return {
      'code': 1,
      'message': '查询成功',
      'results': [{'id': 1, 'name': 'wq企业1', 'leaf': false}, {
        'id': 2,
        'name': '四川能源投资集团有限责任公司',
        'leaf': false
      }, {'id': 3, 'name': 'aaaa', 'leaf': true}, {'id': 4, 'name': 'aaa', 'leaf': true}]
    }
  },
  getDepartmentByParentId: function (params) {
    return {
      'code': 1,
      'message': null,
      'results': [{'id': 9, 'name': '33', 'enterpriseId': 1, 'parentId': 7, 'order_': 8, 'leaf': true}, {
        'id': 8,
        'name': '22',
        'enterpriseId': 1,
        'parentId': 7,
        'order_': 9,
        'leaf': true
      }]
    }
  }
}
