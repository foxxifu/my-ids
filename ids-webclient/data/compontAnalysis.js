module.exports.R = {
  // 电站月度分析
  getStationMouthAnalysis: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'hidPv': {
          'hidPvNum|0-100': 1,
          'hidPvCount|10-500.3': 1
        },
        'ineffPv': {
          'ineffPvNum|0-100': 1,
          'ineffPvCount|10-500.3': 1
        },
        'troublePv': {
          'troublePvNum|0-100': 1,
          'troublePvCount|10-500.3': 1
        }
      },
      'tableData|10': [
        {
          'analysisTime': '@date("T")',
          'hidLostPower|100-5000.3': 1,
          'hidPvNum|10-500': 1,
          'ineffLostPower|100-5000.3': 1,
          'ineffPvNum|10-500': 1,
          'installedCapacity|100-5000.3': 1,
          'matrixId|+1': 1,
          'matrixName|1': '测试:@ctitle(3, 5)',
          'productPower|100-5000.3': 1,
          'pvNum|10-500': 1,
          'troubleLostPower|100-5000.3': 1,
          'troublePvNum|10-500': 1
        }
      ],
      'index': 1,
      'pageSize': 10,
      'total|10-100': 1
    }
  },
  // 电站子阵级月度分析
  getCompontAnalysisMouth: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'hidPv': {
          'hidPvNum|0-100': 1,
          'hidPvCount|10-500.3': 1
        },
        'ineffPv': {
          'ineffPvNum|0-100': 1,
          'ineffPvCount|10-500.3': 1
        },
        'troublePv': {
          'troublePvNum|0-100': 1,
          'troublePvCount|10-500.3': 1
        }
      },
      'tableData|10': [
        {
          'analysisTime': '@date("T")',
          'matrixId|+1': 1,
          'matrixName': '测试:@ctitle(3, 5)',
          'devAlias': '测试:@ctitle(3, 5)',
          'devId|+1': 1,
          'pv1': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv2': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv3': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv4': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv5': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv6': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv7': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv8': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv9': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv10': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv11': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv13': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv14': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv15': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv16': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv17': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv18': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv12': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv19': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv20': '@float(10, 200, 3, 3)|@natural(0,3)',
        }
      ],
      'index': 1,
      'pageSize': 10,
      'total|10-100': 1,
      'pvCode|6-20': 1,
    }
  },
  // // 电站年度分析
  getStationYearAnalysis: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData|3-12': [
        {
          'analysisTime': new Date('2018-01').getTime(),
          'devAlias|1': '测试:@cword(3, 5)',
          'devId|+1': 1,
          'hidLastTime': '@date("T")',
          'hidLostPower|10-500.3': 1,
          'ineffLastTime': '@date("T")',
          'ineffLostPower|10-500.3': 1,
          'matrixId|+1': 1,
          'matrixName|1': '测试:@cword(3, 5)',
          'pvCode': 43765,
          'pv_capacity': 77547,
          'stationCode': '@word(3, 5)',
          'troubleLastTime': '@date("T")',
          'troubleLostPower|10-500.3': 1
        }
      ],
      'tableData|10': [
        {
          'analysisTime': '@date("T")',
          'hidLostPower|100-5000.3': 1,
          'hidPvNum|10-500': 1,
          'ineffLostPower|100-5000.3': 1,
          'ineffPvNum|10-500': 1,
          'installedCapacity|100-5000.3': 1,
          'matrixId|+1': 1,
          'matrixName|1': '测试:@ctitle(3, 5)',
          'productPower|100-5000.3': 1,
          'pvNum|10-500': 1,
          'troubleLostPower|100-5000.3': 1,
          'troublePvNum|10-500': 1
        }
      ],
      'total|10-100': 1
    }
  },
  // 电站子阵级年度分析
  getCompontAnalysisYear: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': [{
        'devId': 2,
        'stationCode': '1f663b9b70dc4151aea383547777c244',
        'analysisTime': 1532275200011,
        'matrixName': '测试子阵',
        'hidLastTime': 1532070669010,
        'hidLostPower': 50.000,
        'ineffLostPower': 40.000,
        'pvCode': '6',
        'troubleLastTime': 1532070669010,
        'ineffLastTime': 30,
        'troubleLostPower': 20.000,
        'devAlias': '南瑞继保通管机B-1-xm',
        'matrixId': 123456,
        'pvCapacity': 20.00000
      }, {
        'devId': 3,
        'stationCode': '1f663b9b70dc4151aea383547777c244',
        'analysisTime': 1532275200022,
        'matrixName': '测试子阵1',
        'hidLastTime': 1532070669010,
        'hidLostPower': 100.000,
        'ineffLostPower': 80.000,
        'pvCode': '8',
        'troubleLastTime': 1532070669010,
        'ineffLastTime': 70,
        'troubleLostPower': 60.000,
        'devAlias': '1#方阵箱变-xm',
        'matrixId': 5678,
        'pvCapacity': 30.00000
      }],
      'tableData': [{
        'stationCode': '1f663b9b70dc4151aea383547777c244',
        'pvNum': 8,
        'analysisTime': 1532070669010,
        'matrixName': '测试子阵1',
        'ineffPvNum': 15,
        'troublePvNum': 10,
        'hidLostPower': 18.000,
        'ineffLostPower': 16.000,
        'installedCapacity': 50.00000,
        'troubleLostPower': 14.000,
        'productPower': 9.000,
        'hidPvNum': 17,
        'matrixId': 5678
      }, {
        'stationCode': '1f663b9b70dc4151aea383547777c244',
        'pvNum': 6,
        'analysisTime': 1532070669010,
        'matrixName': '测试子阵',
        'ineffPvNum': 12,
        'troublePvNum': 10,
        'hidLostPower': 15.000,
        'ineffLostPower': 13.000,
        'installedCapacity': 30.00000,
        'troubleLostPower': 11.000,
        'productPower': 50.000,
        'hidPvNum': 14,
        'matrixId': 123456
      }],
      'total': 2,
      'index': 1,
      'pageSize': 10
    }
  },
  // 电站日度分析
  getStationAnalysisDay: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'hidPv': {
          'hidPvNum|0-100': 1,
          'hidPvCount|10-500.3': 1
        },
        'ineffPv': {
          'ineffPvNum|0-100': 1,
          'ineffPvCount|10-500.3': 1
        },
        'troublePv': {
          'troublePvNum|0-100': 1,
          'troublePvCount|10-500.3': 1
        }
      },
      'tableData|10': [
        {
          'analysisTime': '@date("T")',
          'hidLostPower|100-5000.3': 1,
          'hidPvNum|10-500': 1,
          'ineffLostPower|100-5000.3': 1,
          'ineffPvNum|10-500': 1,
          'installedCapacity|100-5000.3': 1,
          'matrixId|+1': 1,
          'matrixName|1': '测试:@ctitle(3, 5)',
          'productPower|100-5000.3': 1,
          'pvNum|10-500': 1,
          'troubleLostPower|100-5000.3': 1,
          'troublePvNum|10-500': 1
        }
      ],
      'index': 1,
      'pageSize': 10,
      'total|10-100': 1
    }
  },
  // 电站子阵级日度分析
  getCompontAnalysisDay: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'hidPv': {
          'hidPvNum|0-100': 1,
          'hidPvCount|10-500.3': 1
        },
        'ineffPv': {
          'ineffPvNum|0-100': 1,
          'ineffPvCount|10-500.3': 1
        },
        'troublePv': {
          'troublePvNum|0-100': 1,
          'troublePvCount|10-500.3': 1
        }
      },
      'tableData|10': [
        {
          'analysisTime': '@date("T")',
          'matrixId|+1': 1,
          'matrixName': '测试:@ctitle(3, 5)',
          'devAlias': '测试:@ctitle(3, 5)',
          'devId|+1': 1,
          'pv1': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv2': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv3': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv4': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv5': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv6': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv7': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv8': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv9': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv10': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv11': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv13': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv14': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv15': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv16': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv17': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv18': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv12': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv19': '@float(10, 200, 3, 3)|@natural(0,3)',
          'pv20': '@float(10, 200, 3, 3)|@natural(0,3)',
        }
      ],
      'index': 1,
      'pageSize': 10,
      'total|10-100': 1,
      'pvCode|6-20': 1,
    }
  },

  // 导出分析报表
  exportData: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {}
  },

  // 逆变器组串离散率分析
  getInverterDiscreteRate: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'error|0-100': 1,
        'ge20|0-200': 1,
        'ge10lt20|0-200': 1,
        'ge5lt10|0-200': 1,
        'ge0lt5|0-200': 1,
        'noAnalysis|0-100': 1
      },
      'tableData|10': [
        {
          'devAlias': '测试:@ctitle(3, 5)',
          'discreteRate|1-99.2': 1,
          'efficiency|1-99.2': 1,
          'equivalentHour|1-100': 1,
          'peakPower|100-5000.3': 1,
          'productPower|100-5000.3': 1,
          'isAnalysis|0-2': 1,
          'pv1': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv2': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv3': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv4': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv5': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv6': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv7': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv8': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv9': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv10': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv11': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv12': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv13': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv14': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv15': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv16': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv17': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv18': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv19': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv20': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
        }
      ],
      'total|10-100': 1,
      'pvCode|6-20': 1,
    }
  },

  // 直流汇流箱组串离散率分析
  getCombinerdcDiscreteRate: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'chartData': {
        'error|0-100': 1,
        'ge20|0-200': 1,
        'ge10lt20|0-200': 1,
        'ge5lt10|0-200': 1,
        'ge0lt5|0-200': 1,
        'noAnalysis|0-100': 1
      },
      'tableData|10': [
        {
          'devAlias': '测试:@ctitle(3, 5)',
          'discreteRate|1-99.2': 1,
          'equivalentHour|1-100': 1,
          'avgU|100-5000.3': 1,
          'isAnalysis|0-2': 1,
          'pv1': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv2': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv3': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv4': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv5': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv6': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv7': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv8': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv9': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv10': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv11': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv12': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv13': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv14': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv15': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv16': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv17': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv18': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv19': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
          'pv20': '@float(10, 200, 3, 3)/@float(10, 200, 3, 3)',
        }
      ],
      'total|10-100': 1,
      'pvCode|6-20': 1,
    }
  },
}
