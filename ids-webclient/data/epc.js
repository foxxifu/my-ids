epc = {}
epc.D = module.exports.D = {
  testData: {
    'assets': {
      'epc': {
        'treeData': [{
          label: '全部',
          id: '1',
          children: [{
            label: 'EPC乙',
            id: '2',
            children: [{
              label: '代理1'
            }, {
              label: '代理2'
            }, {
              label: '代理3'
            }]
          }]
        }],
        'assetsData': [
          {
            'num': '00001',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商A',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '5年',
            'installer': '厂商1',
          },
          {
            'num': '00002',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商b',
            'epc_agent': 'EPC甲代理商A',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '6年',
            'installer': '厂商2',
          },
          {
            'num': '00003',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商c',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '7年',
            'installer': '厂商3',
          },
        ],
      },
      'manufacturer': {
        'treeData': [{
          label: '全部',
          id: '1',
          children: [{
            label: 'EPC乙',
            id: '2',
            children: [{
              label: '代理12'
            }, {
              label: '代理23'
            }, {
              label: '代理34'
            }]
          }]
        }],
        'assetsData': [
          {
            'num': '00001',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商A',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '5年',
          },
          {
            'num': '00002',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商b',
            'epc_agent': 'EPC甲代理商A',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '6年',
          },
          {
            'num': '00003',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商c',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '7年',
          },
          {
            'num': '00003',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商c',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '7年',
          },
          {
            'num': '00003',
            'devVersion': 'EP-1260-A-OD',
            'devType': '集中式逆变器',
            'esnNum': 'SN36165550',
            'devStatus': '在途',
            'productionDate': '2018-04-16 10:00:00',
            'epc': 'EPC甲',
            'agent': '代理商A',
            'epc_agent': 'EPC甲代理商c',
            'deliveryDate': '2018-04-17 10:00:00',
            'warrantyTime': '7年',
          },
        ]
      }
    },
    'exchangeOfGoods': {
      'epc': {
        'treeData': [{
          label: '全部',
          id: '1',
          children: [{
            label: '代理A',
            id: '2',
            children: [{
              label: '分销商A',
              children: [
                {label: '安装商A'},
                {label: '安装商B'}
              ],
            }, {
              label: '分销商B'
            }, {
              label: '分销商C'
            }]
          }]
        }],
        'exchangeOfGoodsData': {
          'applying': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
            {
              'num': '0002', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
            {
              'num': '0003', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
          ],
          'approved': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            },
          ],
          'refused': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            },
            {
              'num': '0002', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            }
          ],
        },
      },
      'manufacturer': {
        'treeData': [{
          label: '全部',
          id: '1',
          children: [{
            label: 'EPC乙',
            id: '2',
            children: [{
              label: '代理1'
            }, {
              label: '代理2'
            }, {
              label: '代理3'
            }]
          }]
        }],
        'exchangeOfGoodsData': {
          'applying': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
            {
              'num': '0002', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
            {
              'num': '0003', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
            },
          ],
          'approved': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            },
          ],
          'refused': [
            {
              'num': '0001', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            },
            {
              'num': '0002', // 编号
              'applicationNum': 'S265687', // 申请编号
              'applicationDate': '2018-04-17 12:00:00', // 申请日期
              'devVersion': 'EP-1260-A-OD', // 设备型号
              'devType': '集中式逆变器', // 设备类型
              'productionDate': '2018-04-16 10:00:00', // 生产日期
              'installationDate': '2018-04-17 10:00:00', // 安装日期
              'esn': 'SN36165550', // esn
              'epc_agent': 'EPC甲 代理商A', // epc和代理商
              'epc': '', // epc
              'agent': '', // 代理商
              'deliveryDate': '2018-04-19 10:00:00', // 发货日期
              'devStatus': '0', // 设备状态
              'installationSite': '', // 安装地点
              'ratedOutputPower': '', // 额定输出功率
              'powerStation': '', // 所属电站
              'warrantyTime': '', // 质保时间(年)
              'exchangeReason': '', // 换机原因
              'attachment': '', // 附件
              'applicant': '', // 申请人
              'approvalOpinion': '不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题', // 审批意见
              'approver': 'admin', // 审批人
              'approvalTime': '2018-04-17 17:00:00', // 审批时间
            }
          ],
        }
      }
    },
    'statistics': {
      'epc': {
        'shipmentsChart': {
          'xData': ['代理商A', '代理商B', '代理商C', '代理商D', '代理商E', '代理商F', '代理商H', '代理商I', '代理商G', '代理商K', '代理商L'],
          'seriesData': [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
        },
        'gridConnectedChart': {
          'xData': ['代理商A', '代理商B', '代理商C', '代理商D', '代理商E', '代理商F', '代理商H', '代理商I', '代理商G', '代理商K', '代理商L'],
          'seriesData': [2100, 2200, 2800, 1200, 2400, 1500, 1300, 2200, 1200, 1000, 1600, 1500],
        },
        'returnMachineChart': {
          'xData': ['代理商A', '代理商B', '代理商C', '代理商D', '代理商E', '代理商F', '代理商H', '代理商I', '代理商G', '代理商K', '代理商L'],
          'seriesData': [12, 32, 44, 55, 22, 56, 34, 23, 45, 67, 99, 12],
        },
        'consignmentOfInverterChart': {
          'xData': ['代理商A', '代理商B', '代理商C', '代理商D', '代理商E', '代理商F', '代理商H', '代理商I', '代理商G', '代理商K', '代理商L'],
          'seriesData': [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
        },
        'shipmentsTableData': [
          {
            num: '1',
            agent: '代理商A',
            location: '青海省',
            shipmentsNum: '1700',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            agent: '代理商B',
            location: '甘肃省',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            agent: '代理商C',
            location: '宁夏自治区',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'gridConnectedTableData': [
          {
            num: '1',
            agent: '代理商D',
            location: '青海省',
            shipmentsNum: '1700',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            agent: '代理商E',
            location: '甘肃省',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            agent: '代理商A',
            location: '宁夏自治区',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'returnMachineTableData': [
          {
            num: '1',
            agent: '代理商B',
            location: '青海省',
            shipmentsNum: '12',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            agent: '代理商C',
            location: '甘肃省',
            shipmentsNum: '32',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            agent: '代理商D',
            location: '宁夏自治区',
            shipmentsNum: '44',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'consignmentOfInverterTableData': [
          {
            date: '1月',
            shipmentsNum: '2100',
            zcsInverterNum: '121',
            jzsInverterNum: '1244',
            zssInverterNum: '231',
            destination: '青海省',
          },
          {
            date: '2月',
            shipmentsNum: '1700',
            zcsInverterNum: '123',
            jzsInverterNum: '1022',
            zssInverterNum: '231',
            destination: '海南',
          },
          {
            date: '3月',
            shipmentsNum: '3560',
            zcsInverterNum: '245',
            jzsInverterNum: '342',
            zssInverterNum: '123',
            destination: '四川',
          },
          {
            date: '4月',
            shipmentsNum: '2342',
            zcsInverterNum: '123',
            jzsInverterNum: '121',
            zssInverterNum: '1230',
            destination: '新疆',
          },
          {
            date: '5月',
            shipmentsNum: '1231',
            zcsInverterNum: '13',
            jzsInverterNum: '1000',
            zssInverterNum: '120',
            destination: '陕西',
          },
          {
            date: '6月',
            shipmentsNum: '2213',
            zcsInverterNum: '123',
            jzsInverterNum: '564',
            zssInverterNum: '1230',
            destination: '江苏',
          },
          {
            date: '7月',
            shipmentsNum: '1324',
            zcsInverterNum: '134',
            jzsInverterNum: '23',
            zssInverterNum: '452',
            destination: '甘肃',
          },
          {
            date: '8月',
            shipmentsNum: '1812',
            zcsInverterNum: '213',
            jzsInverterNum: '231',
            zssInverterNum: '523',
            destination: '宁夏',
          },
          {
            date: '9月',
            shipmentsNum: '1200',
            zcsInverterNum: '12',
            jzsInverterNum: '123',
            zssInverterNum: '124',
            destination: '云南',
          },
          {
            date: '10月',
            shipmentsNum: '531',
            zcsInverterNum: '21',
            jzsInverterNum: '423',
            zssInverterNum: '24',
            destination: '广东',
          }
        ],
      },
      'manufacturer': {
        'shipmentsChart': {
          'xData': ['EPC甲', 'EPC乙', 'EPC丙', 'EPC丁', 'EPC戊', 'EPC己', 'EPC庚', 'EPC辛', 'EPC壬', 'EPC癸', 'EPC子'],
          'seriesData': [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
        },
        'gridConnectedChart': {
          'xData': ['EPC甲', 'EPC乙', 'EPC丙', 'EPC丁', 'EPC戊', 'EPC己', 'EPC庚', 'EPC辛', 'EPC壬', 'EPC癸', 'EPC子'],
          'seriesData': [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
        },
        'returnMachineChart': {
          'xData': ['EPC甲', 'EPC乙', 'EPC丙', 'EPC丁', 'EPC戊', 'EPC己', 'EPC庚', 'EPC辛', 'EPC壬', 'EPC癸', 'EPC子'],
          'seriesData': [12, 32, 44, 55, 22, 56, 34, 23, 45, 67, 99, 12],
        },
        'consignmentOfInverterChart': {
          'xData': ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          'seriesData': [2600, 2000, 1800, 2200, 2300, 1500, 1600, 2100, 1900, 2000, 1400, 1200],
        },
        'shipmentsTableData': [
          {
            num: '1',
            epc: 'EPC庚',
            location: '青海省',
            shipmentsNum: '1700',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            epc: 'EPC甲',
            location: '甘肃省',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            epc: 'EPC乙',
            location: '宁夏自治区',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'gridConnectedTableData': [
          {
            num: '1',
            epc: 'EPC庚',
            location: '青海省',
            shipmentsNum: '1700',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            epc: 'EPC甲',
            location: '甘肃省',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            epc: 'EPC乙',
            location: '宁夏自治区',
            shipmentsNum: '2600',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'returnMachineTableData': [
          {
            num: '1',
            epc: 'EPC庚',
            location: '青海省',
            shipmentsNum: '12',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '2',
            epc: 'EPC甲',
            location: '甘肃省',
            shipmentsNum: '32',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
          {
            num: '3',
            epc: 'EPC乙',
            location: '宁夏自治区',
            shipmentsNum: '44',
            zcsInverterNum: '7',
            jzsInverterNum: '10',
            zssInverterNum: '10',
          },
        ],
        'consignmentOfInverterTableData': [
          {
            date: '1月',
            shipmentsNum: '2100',
            zcsInverterNum: '121',
            jzsInverterNum: '1244',
            zssInverterNum: '231',
            destination: '青海省',
          },
          {
            date: '2月',
            shipmentsNum: '1700',
            zcsInverterNum: '123',
            jzsInverterNum: '1022',
            zssInverterNum: '231',
            destination: '海南',
          },
          {
            date: '3月',
            shipmentsNum: '3560',
            zcsInverterNum: '245',
            jzsInverterNum: '342',
            zssInverterNum: '123',
            destination: '四川',
          },
          {
            date: '4月',
            shipmentsNum: '2342',
            zcsInverterNum: '123',
            jzsInverterNum: '121',
            zssInverterNum: '1230',
            destination: '新疆',
          },
          {
            date: '5月',
            shipmentsNum: '1231',
            zcsInverterNum: '13',
            jzsInverterNum: '1000',
            zssInverterNum: '120',
            destination: '陕西',
          },
          {
            date: '6月',
            shipmentsNum: '2213',
            zcsInverterNum: '123',
            jzsInverterNum: '564',
            zssInverterNum: '1230',
            destination: '江苏',
          },
          {
            date: '7月',
            shipmentsNum: '1324',
            zcsInverterNum: '134',
            jzsInverterNum: '23',
            zssInverterNum: '452',
            destination: '甘肃',
          },
          {
            date: '8月',
            shipmentsNum: '1812',
            zcsInverterNum: '213',
            jzsInverterNum: '231',
            zssInverterNum: '523',
            destination: '宁夏',
          },
          {
            date: '9月',
            shipmentsNum: '1200',
            zcsInverterNum: '12',
            jzsInverterNum: '123',
            zssInverterNum: '124',
            destination: '云南',
          },
          {
            date: '10月',
            shipmentsNum: '531',
            zcsInverterNum: '21',
            jzsInverterNum: '423',
            zssInverterNum: '24',
            destination: '广东',
          }
        ],
      }
    },
    'project': {
      'epc': {
        tableData: [
          {
            num: 'XM0001',
            projectPic: '',
            name: '屋顶分布式并网光伏电站A一期工程',
            schedule: 48,
            beginTime: '2018-04-15 15:22:55',
            endTime: '2018-04-15 15:22:55',
            status: '待启动',
            charge: '任小华',
            tabName: 'earlyPeriod',
            earlyPeriod: [
              {
                num: '01',
                name: '属地项目意向书(框架协议书)',
                taskProgress: 100,
                involvingDepartment: '发改委或招商局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '已完成',
                taskLeader: '张小欧',
              },
              {
                num: '02',
                name: '同意开展前期工作的函',
                taskProgress: 20,
                involvingDepartment: '省发改委',
                beginTime: '2018-03-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '已超期',
                taskLeader: '张小欧',
              },
              {
                num: '03',
                name: '可行性研究报告',
                taskProgress: 63,
                involvingDepartment: '有资质的设计院',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '04',
                name: '可研报告评审意见',
                taskProgress: 76,
                involvingDepartment: '专家评审',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
            ],
            developmentPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            buidingPeriod: [
              {
                num: '1',
                name: '建设项目用地预审意见的函',
                taskProgress: 76,
                involvingDepartment: '省国土资源厅',
                beginTime: '2018-02-16 12:00:12',
                endTime: '2018-03-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '2',
                name: '环评批复函',
                taskProgress: 70,
                involvingDepartment: '省环境保护厅',
                beginTime: '2018-04-11 12:00:12',
                endTime: '2018-05-02 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '3',
                name: '能评批复函',
                taskProgress: 78,
                involvingDepartment: '发改委',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '4',
                name: '水土保持方案书批复函',
                taskProgress: 76,
                involvingDepartment: '省水利厅',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
            ],
            onGridPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '4',
                name: '项目公司合同清单',
                taskProgress: 70,
                involvingDepartment: '合同及清单',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ]
          },
          {
            num: 'XM0002',
            projectPic: '',
            name: '屋顶分布式并网光伏电站B二期工程',
            schedule: 69,
            beginTime: '2018-04-15 15:22:55',
            endTime: '2018-04-15 15:22:55',
            status: '待启动',
            charge: '任小华',
            tabName: 'earlyPeriod',
            earlyPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            developmentPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            buidingPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            onGridPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '4',
                name: '项目公司合同清单',
                taskProgress: 70,
                involvingDepartment: '合同及清单',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ]
          },
        ],
        data: [{
          label: '全部',
          id: '0',
          children: [{
            label: '华北',
            id: '1',
            children: [{
              label: '项目A'
            }, {
              label: '项目B'
            }]
          }, {
            label: '华西',
            id: '2',
            children: [{
              label: '项目I'
            }]
          }, {
            label: '华东',
            id: '3',
            children: [{
              label: '项目J'
            }]
          }, {
            label: '华中',
            id: '4',
            children: [{
              label: '项目C'
            }, {
              label: '项目D'
            }, {
              label: '项目E'
            }]
          }, {
            label: '华南',
            id: '5',
            children: [{
              label: '项目F'
            }]
          }, {
            label: '西南',
            id: '6',
            children: [{
              label: '项目G'
            }]
          }, {
            label: '西北',
            id: '7',
            children: [{
              label: '项目H'
            }]
          }
          ]
        }],
      },
      'manufacturer': {
        tableData: [
          {
            num: 'XM0001',
            projectPic: '',
            name: '屋顶分布式并网光伏电站A一期工程',
            schedule: 48,
            beginTime: '2018-04-15 15:22:55',
            endTime: '2018-04-15 15:22:55',
            status: '待启动',
            charge: '任小华',
            tabName: 'earlyPeriod',
            earlyPeriod: [
              {
                num: '01',
                name: '属地项目意向书(框架协议书)',
                taskProgress: 100,
                involvingDepartment: '发改委或招商局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '已完成',
                taskLeader: '张小欧',
              },
              {
                num: '02',
                name: '同意开展前期工作的函',
                taskProgress: 20,
                involvingDepartment: '省发改委',
                beginTime: '2018-03-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '已超期',
                taskLeader: '张小欧',
              },
              {
                num: '03',
                name: '可行性研究报告',
                taskProgress: 63,
                involvingDepartment: '有资质的设计院',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '04',
                name: '可研报告评审意见',
                taskProgress: 76,
                involvingDepartment: '专家评审',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
            ],
            developmentPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            buidingPeriod: [
              {
                num: '1',
                name: '建设项目用地预审意见的函',
                taskProgress: 76,
                involvingDepartment: '省国土资源厅',
                beginTime: '2018-02-16 12:00:12',
                endTime: '2018-03-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '2',
                name: '环评批复函',
                taskProgress: 70,
                involvingDepartment: '省环境保护厅',
                beginTime: '2018-04-11 12:00:12',
                endTime: '2018-05-02 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '3',
                name: '能评批复函',
                taskProgress: 78,
                involvingDepartment: '发改委',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
              {
                num: '4',
                name: '水土保持方案书批复函',
                taskProgress: 76,
                involvingDepartment: '省水利厅',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '张小欧',
              },
            ],
            onGridPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '4',
                name: '项目公司合同清单',
                taskProgress: 70,
                involvingDepartment: '合同及清单',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ]
          },
          {
            num: 'XM0002',
            projectPic: '',
            name: '屋顶分布式并网光伏电站B二期工程',
            schedule: 69,
            beginTime: '2018-04-15 15:22:55',
            endTime: '2018-04-15 15:22:55',
            status: '待启动',
            charge: '任小华',
            tabName: 'earlyPeriod',
            earlyPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            developmentPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            buidingPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ],
            onGridPeriod: [
              {
                num: '1',
                name: '竣工预验收报告',
                taskProgress: 80,
                involvingDepartment: '项目所在地建设局',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '2',
                name: '电力业务许可证',
                taskProgress: 60,
                involvingDepartment: '电监办资源管理中心',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '3',
                name: '项目公司基本情况',
                taskProgress: 30,
                involvingDepartment: '全套工商档案及三证',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
              {
                num: '4',
                name: '项目公司合同清单',
                taskProgress: 70,
                involvingDepartment: '合同及清单',
                beginTime: '2018-04-16 12:00:12',
                endTime: '2018-05-01 12:00:00',
                taskStatus: '进行中',
                taskLeader: '任小华',
              },
            ]
          },
        ],
        data: [{
          label: '全部',
          id: '0',
          children: [{
            label: '华北',
            id: '1',
            children: [{
              label: '项目A'
            }, {
              label: '项目B'
            }]
          }, {
            label: '华西',
            id: '2',
            children: [{
              label: '项目I'
            }]
          }, {
            label: '华东',
            id: '3',
            children: [{
              label: '项目J'
            }]
          }, {
            label: '华中',
            id: '4',
            children: [{
              label: '项目C'
            }, {
              label: '项目D'
            }, {
              label: '项目E'
            }]
          }, {
            label: '华南',
            id: '5',
            children: [{
              label: '项目F'
            }]
          }, {
            label: '西南',
            id: '6',
            children: [{
              label: '项目G'
            }]
          }, {
            label: '西北',
            id: '7',
            children: [{
              label: '项目H'
            }]
          }
          ]
        }],
      }
    }
  }
}
module.exports.R = {
  getManufacturerTree: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.assets[body.type].treeData,
    }
  },
  getAssetsOfManufacturer: function (params) {
    // {body: {sId = '1001', type = 'manufacturer'}}
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.assets[body.type].assetsData,
    }
  },
  getExchangeOfGoodsTree: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.exchangeOfGoods[body.type].treeData,
    }
  },
  getExchangeOfGoodsOfManufacturer: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    body.status = body.status || 'applying'
    return {
      code: 1,
      results: epc.D.testData.exchangeOfGoods[body.type].exchangeOfGoodsData[body.status],
    }
  },
  getStatisticsData: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    body.model = body.model || 'shipments'
    return {
      code: 1,
      results: {
        'chart': epc.D.testData.statistics[body.type][body.model + 'Chart'],
        'report': epc.D.testData.statistics[body.type][body.model + 'TableData'],
      },
    }
  },
  getProjectTableData: function (params) {
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.project[body.type].tableData,
    }
  },
  getProjectSelectData: function (params) {
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.project[body.type].data,
    }
  }
}
