module.exports.R = {
  save: {
    code: 1
  },
  updateWorkTicket: {
    code: 1
  },

  getWorkTicketList: {
    'code': 1,
    'results': {
      'list|10': [
        {
          'alarmId|+1': 1,
          'alarmName': '@ctitle(3, 8)',
          'alarmType|1-2': 1,
          'chargeId|+1': 1,
          'chargeName': '@cname()',
          'devId|+1': 1,
          'createDate': '@now("T")',
          'endWorkTime': '@now("T")',
          'processTime|1-24': 1,
          'processUserId': 1,
          'processPerson': 'admin',
          'serialNum|+1': 1,
          'startWorkTime': '@now("T")',
          'processStatus': 'issueFirstWork',
          'workContent': '@csentence(10, 100)'
        }
      ],
      'index': 1,
      'pageSize': 10,
      'total|10-100': 10,
    }
  },

  getWorkTicketByDefinitionId: {
    'code': 1,
    'results': {
      'alarmId': 1,
      'alarmType': 1,
      'chargeId': '1',
      'groupId': '2,5,7,8',
      'chargeName': '@ctitle(3, 8)',
      'devId|+1': 1,
      'endWorkTime': '@now("T")',
      'groundWire': '@paragraph(5, 20)',
      'issuserId|+1': 1,
      'liveElectrifiedEquipment': '@paragraph(5, 20)',
      'personalSafetyTechnical': '@paragraph(5, 20)',
      'safetyTechnical': '@paragraph(5, 20)',
      'safetyWarningSigns': '@paragraph(5, 20)',
      'startWorkTime': '@now("T")',
      'workAddress': '@csentence(10, 100)',
      'workContent': '@csentence(10, 100)'
    }
  },
  executeWorkTicket: {
    code: 1
  }
}
