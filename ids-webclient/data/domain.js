module.exports.R = {
  getDomainsByParentId: function (params) {
    params = (params && params.body && JSON.parse(params.body)) || {body: {}}
    let parentId = params.id
    let results = []
    let len = 10
    if (!parentId) {
      len = 5
    } else {
      len = 2
    }
    let one
    for (let i = 0; i < len; i++) {
      one = {
        id: (i + 1),
        isLeaf: +Math.random().toFixed() === 0,
        label: '测试名称' + (Math.random() * 10000).toFixed()
      }
      results.push(one)
    }
    return {
      code: 1,
      results: results
    }
  },
  getDomainTree1: function (params) {
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': [{
        'subDomain': null,
        'parentName': null,
        'id': 1,
        'name': 'wq区域',
        'description': '区域的描述',
        'parentId': 0,
        'enterpriseId': 1,
        'longitude': 104.631729,
        'latitude': 30.133845,
        'radius': 9335.876,
        'domainPrice': 12.000,
        'currency': 'rmb',
        'createUserId': null,
        'createDate': null,
        'modifyUserId': null,
        'modifyDate': null,
        'path': '0',
        'children': []
      }, {
        'subDomain': null,
        'parentName': null,
        'id': 5,
        'name': '22',
        'description': null,
        'parentId': 0,
        'enterpriseId': 1,
        'longitude': 104.219482,
        'latitude': 30.41322,
        'radius': 10000.0,
        'domainPrice': 33.000,
        'currency': '$',
        'createUserId': null,
        'createDate': null,
        'modifyUserId': null,
        'modifyDate': null,
        'path': '0',
        'children': [{
          'subDomain': null,
          'parentName': null,
          'id': 6,
          'name': '22',
          'description': '44',
          'parentId': 5,
          'enterpriseId': 1,
          'longitude': 104.219482,
          'latitude': 30.41322,
          'radius': 10000.0,
          'domainPrice': 44.000,
          'currency': '$',
          'createUserId': null,
          'createDate': null,
          'modifyUserId': null,
          'modifyDate': null,
          'path': '0@5',
          'children': []
        }]
      }]
    }
  }
}
