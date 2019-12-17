reportManage = {}
reportManage.D = module.exports.D = {
  testData: {
    'stationSIds': ['1001'],
    'stationRunning': {
      '1001': {
        'echart': {
          'year': {
            'inverters': ['河南侯寨分布式电站', '深圳机场分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站B', '江阴户用光伏站'],
            'electricityGeneration': [145478.270, 2740884.811, 1303455.223, 3799741.998, 552837.968, 2337937.057, 10522.581, 8704.176, 6845.719],
            'income': [138204.35, 2877929.05, 1368627.98, 3989729.10, 580479.87, 2454833.91, 11048.71, 9139.39, 7188.00],
          },
          'month': {
            'inverters': ['河南侯寨分布式电站', '深圳机场分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站B', '江阴户用光伏站'],
            'electricityGeneration': [72864.361, 539003.717, 402535.690, 911850.678, 116396.543, 605226.467, 2130.960, 2446.620, 2083.060],
            'income': [69221.14, 603630.11, 269181.11, 1019050.96, 77579.42, 533549.20, 2057.31, 2168.27, 1549.45],
          },
          'day': {
            'inverters': ['河南侯寨分布式电站', '深圳机场分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站B', '江阴户用光伏站'],
            'electricityGeneration': [1803.260, 23032.645, 10953.405, 31930.605, 4645.697, 19646.530, 88.425, 73.144, 57.527],
            'income': [1713.09, 24184.28, 11501.08, 33527.14, 4877.98, 20628.86, 92.85, 76.80, 60.40],
          }
        },
        'report': {
          'year': [
            {
              stationName: '河南侯寨分布式电站', // 电站名称
              address: '郑州市侯寨乡', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '600', // 装机容量(kw)
              productPower: '145478.270', // 当月发电量(kwh)
              income: '138204.35', // 当月收益(元)
              equivalentUtilizationHour: '242.463', // 等效利用小时数
            },
            {
              stationName: '深圳机场分布式电站', // 电站名称
              address: '广东深圳市宝安区', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '5000', // 装机容量(kw)
              productPower: '2740884.811', // 当月发电量(kwh)
              income: '2877929.05', // 当月收益(元)
              equivalentUtilizationHour: '548.176', // 等效利用小时数
            },
            {
              stationName: '广州大学城分布式电站', // 电站名称
              address: '广州大学城', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '3000', // 装机容量(kw)
              productPower: '1303455.223', // 当月发电量(kwh)
              income: '1368627.98', // 当月收益(元)
              equivalentUtilizationHour: '434.485', // 等效利用小时数
            },
            {
              stationName: '广州龙穴岛分布式电站', // 电站名称
              address: '广州龙穴岛', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '8000', // 装机容量(kw)
              productPower: '3799741.998', // 当月发电量(kwh)
              income: '3989729.10', // 当月收益(元)
              equivalentUtilizationHour: '474.967', // 等效利用小时数
            },
            {
              stationName: '江苏镇江光伏电站', // 电站名称
              address: '江苏镇江', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '1000', // 装机容量(kw)
              productPower: '552837.968', // 当月发电量(kwh)
              income: '580479.87', // 当月收益(元)
              equivalentUtilizationHour: '552.837', // 等效利用小时数
            },
            {
              stationName: '无锡分布式光伏电站', // 电站名称
              address: '江苏无锡某镇', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '6000', // 装机容量(kw)
              productPower: '2337937.057', // 当月发电量(kwh)
              income: '2454833.91', // 当月收益(元)
              equivalentUtilizationHour: '389.656', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站A\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '19', // 装机容量(kw)
              productPower: '10522.581', // 当月发电量(kwh)
              income: '11048.71', // 当月收益(元)
              equivalentUtilizationHour: '553.82', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站B\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '20', // 装机容量(kw)
              productPower: '8704.176', // 当月发电量(kwh)
              income: '9139.39', // 当月收益(元)
              equivalentUtilizationHour: '435.208', // 等效利用小时数
            },
            {
              stationName: '江阴户用光伏站\n', // 电站名称
              address: '江苏江阴某镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '21', // 装机容量(kw)
              productPower: '6845.719', // 当月发电量(kwh)
              income: '7188.00', // 当月收益(元)
              equivalentUtilizationHour: '325.98', // 等效利用小时数
            },
          ],
          'month': [
            {
              stationName: '河南侯寨分布式电站', // 电站名称
              address: '郑州市侯寨乡', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '600', // 装机容量(kw)
              productPower: '72864.361', // 当月发电量(kwh)
              income: '69221.14', // 当月收益(元)
              equivalentUtilizationHour: '121.44', // 等效利用小时数
            },
            {
              stationName: '深圳机场分布式电站\n', // 电站名称
              address: '广东深圳市宝安区', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '5000', // 装机容量(kw)
              productPower: '539003.717 \n', // 当月发电量(kwh)
              income: '603630.11 \n', // 当月收益(元)
              equivalentUtilizationHour: '107.80 \n', // 等效利用小时数
            },
            {
              stationName: '广州大学城分布式电站\n', // 电站名称
              address: '广州大学城', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '3000', // 装机容量(kw)
              productPower: '402535.690', // 当月发电量(kwh)
              income: '269181.11', // 当月收益(元)
              equivalentUtilizationHour: '134.18', // 等效利用小时数
            },
            {
              stationName: '广州龙穴岛分布式电站\n', // 电站名称
              address: '广州龙穴岛', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '8000', // 装机容量(kw)
              productPower: '911850.677', // 当月发电量(kwh)
              income: '1019050.96', // 当月收益(元)
              equivalentUtilizationHour: '113.98', // 等效利用小时数
            },
            {
              stationName: '江苏镇江光伏电站\n', // 电站名称
              address: '江苏镇江\n', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '1000', // 装机容量(kw)
              productPower: '116396.543 \n', // 当月发电量(kwh)
              income: '77579.42 \n', // 当月收益(元)
              equivalentUtilizationHour: '116.40 \n', // 等效利用小时数
            },
            {
              stationName: '无锡分布式光伏电站\n', // 电站名称
              address: '江苏无锡某镇\n', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '6000', // 装机容量(kw)
              productPower: '605226.467', // 当月发电量(kwh)
              income: '533549.20', // 当月收益(元)
              equivalentUtilizationHour: '100.87', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站A\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '19', // 装机容量(kw)
              productPower: '2130.960', // 当月发电量(kwh)
              income: '2057.31', // 当月收益(元)
              equivalentUtilizationHour: '112.16', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站B\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '20', // 装机容量(kw)
              productPower: '2446.620', // 当月发电量(kwh)
              income: '2168.27', // 当月收益(元)
              equivalentUtilizationHour: '122.33', // 等效利用小时数
            },
            {
              stationName: '江阴户用光伏站\n', // 电站名称
              address: '江苏江阴某镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '21', // 装机容量(kw)
              productPower: '2083.060', // 当月发电量(kwh)
              income: '1549.45 ', // 当月收益(元)
              equivalentUtilizationHour: '99.19', // 等效利用小时数
            },
          ],
          'day': [
            {
              stationName: '河南侯寨分布式电站', // 电站名称
              address: '郑州市侯寨乡', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '600', // 装机容量(kw)
              productPower: '1803.260', // 当月发电量(kwh)
              income: '1713.09', // 当月收益(元)
              equivalentUtilizationHour: '3.05', // 等效利用小时数
            },
            {
              stationName: '深圳机场分布式电站\n', // 电站名称
              address: '广东深圳市宝安区', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '5000', // 装机容量(kw)
              productPower: '23032.645', // 当月发电量(kwh)
              income: '24184.28', // 当月收益(元)
              equivalentUtilizationHour: '4.61', // 等效利用小时数
            },
            {
              stationName: '广州大学城分布式电站\n', // 电站名称
              address: '广州大学城', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '3000', // 装机容量(kw)
              productPower: '10953.405', // 当月发电量(kwh)
              income: '11501.08', // 当月收益(元)
              equivalentUtilizationHour: '3.65', // 等效利用小时数
            },
            {
              stationName: '广州龙穴岛分布式电站\n', // 电站名称
              address: '广州龙穴岛', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '8000', // 装机容量(kw)
              productPower: '31930.605', // 当月发电量(kwh)
              income: '33527.14', // 当月收益(元)
              equivalentUtilizationHour: '3.99', // 等效利用小时数
            },
            {
              stationName: '江苏镇江光伏电站\n', // 电站名称
              address: '江苏镇江\n', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '1000', // 装机容量(kw)
              productPower: '4645.697', // 当月发电量(kwh)
              income: '4877.98', // 当月收益(元)
              equivalentUtilizationHour: '4.65', // 等效利用小时数
            },
            {
              stationName: '无锡分布式光伏电站\n', // 电站名称
              address: '江苏无锡某镇\n', // 电站地址
              gridType: '分布式', // 并网类型
              installedCapacity: '6000', // 装机容量(kw)
              productPower: '19646.530', // 当月发电量(kwh)
              income: '20628.86', // 当月收益(元)
              equivalentUtilizationHour: '3.27', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站A\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '19', // 装机容量(kw)
              productPower: '88.425', // 当月发电量(kwh)
              income: '92.85', // 当月收益(元)
              equivalentUtilizationHour: '4.65', // 等效利用小时数
            },
            {
              stationName: '无锡户用光伏站B\n', // 电站名称
              address: '江苏无锡荷塘镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '20', // 装机容量(kw)
              productPower: '73.144', // 当月发电量(kwh)
              income: '76.80', // 当月收益(元)
              equivalentUtilizationHour: '3.66', // 等效利用小时数
            },
            {
              stationName: '江阴户用光伏站\n', // 电站名称
              address: '江苏江阴某镇\n', // 电站地址
              gridType: '户用', // 并网类型
              installedCapacity: '21', // 装机容量(kw)
              productPower: '57.527', // 当月发电量(kwh)
              income: '60.40', // 当月收益(元)
              equivalentUtilizationHour: '2.74', // 等效利用小时数
            },
          ],
        },
      }
    },
    'inverterRunning': {
      '1001': {
        'echart': {
          'year': {
            'inverters': ['河南侯寨分布式电站_1#逆变器', '河南侯寨分布式电站_2#逆变器', '河南侯寨分布式电站_3#逆变器', '河南侯寨分布式电站_4#逆变器', '河南侯寨分布式电站_5#逆变器', '河南侯寨分布式电站_6#逆变器', '河南侯寨分布式电站_7#逆变器', '河南侯寨分布式电站_8#逆变器', '河南侯寨分布式电站_9#逆变器', '河南侯寨分布式电站_10#逆变器', '河南侯寨分布式电站_11#逆变器', '河南侯寨分布式电站_12#逆变器'],
            'electricityGeneration': [15548.268, 15537.113, 14900.737, 14530.709, 15146.286, 15222.258, 14455.031, 15672.570, 14584.117, 15214.062, 14400.012, 15200.725],
          },
          'month': {
            'inverters': ['河南侯寨分布式电站_1#逆变器', '河南侯寨分布式电站_2#逆变器', '河南侯寨分布式电站_3#逆变器', '河南侯寨分布式电站_4#逆变器', '河南侯寨分布式电站_5#逆变器', '河南侯寨分布式电站_6#逆变器', '河南侯寨分布式电站_7#逆变器', '河南侯寨分布式电站_8#逆变器', '河南侯寨分布式电站_9#逆变器', '河南侯寨分布式电站_10#逆变器', '河南侯寨分布式电站_11#逆变器', '河南侯寨分布式电站_12#逆变器'],
            'electricityGeneration': [7666.360, 8093.568, 8020.676, 7494.136, 7711.351, 8141.494, 7979.523, 7563.814, 7826.665, 7844.573, 7520.240, 7448.202],
          },
          'day': {
            'inverters': ['河南侯寨分布式电站_1#逆变器', '河南侯寨分布式电站_2#逆变器', '河南侯寨分布式电站_3#逆变器', '河南侯寨分布式电站_4#逆变器', '河南侯寨分布式电站_5#逆变器', '河南侯寨分布式电站_6#逆变器', '河南侯寨分布式电站_7#逆变器', '河南侯寨分布式电站_8#逆变器', '河南侯寨分布式电站_9#逆变器', '河南侯寨分布式电站_10#逆变器', '河南侯寨分布式电站_11#逆变器', '河南侯寨分布式电站_12#逆变器'],
            'electricityGeneration': [310.324, 303.699, 303.835, 298.872, 307.220, 298.447, 295.376, 315.399, 307.978, 304.673, 303.532, 297.753],
          },
        },
        'report': {
          'year': [
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '1#逆变器\n', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15548.268', // 发电量(kwh)
              efficiency: '97.30%', // 转换效率(%)
              maxPower: '46.452', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '02#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15537.113', // 发电量(kwh)
              efficiency: '97.93%', // 转换效率(%)
              maxPower: '45.705', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '03#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '14900.737', // 发电量(kwh)
              efficiency: '97.62%', // 转换效率(%)
              maxPower: '42.976', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '04#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '14530.709', // 发电量(kwh)
              efficiency: '97.53%\n', // 转换效率(%)
              maxPower: '45.640', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '05#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15146.286', // 发电量(kwh)
              efficiency: '97.34%', // 转换效率(%)
              maxPower: '46.673', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '06#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15222.258', // 发电量(kwh)
              efficiency: '97.98%', // 转换效率(%)
              maxPower: '46.963', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '07#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '14455.031', // 发电量(kwh)
              efficiency: '97.94%', // 转换效率(%)
              maxPower: '47.268', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '08#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15672.570', // 发电量(kwh)
              efficiency: '97.72%', // 转换效率(%)
              maxPower: '46.709', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '09#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '14584.117', // 发电量(kwh)
              efficiency: '97.41%', // 转换效率(%)
              maxPower: '45.406', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '10#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15214.062', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '42.938', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '11#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '14400.012', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '43.858', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '12#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '15200.725', // 发电量(kwh)
              efficiency: '97.81%', // 转换效率(%)
              maxPower: '45.736', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
          ],
          'month': [
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '1#逆变器\n', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7666.360', // 发电量(kwh)
              efficiency: '97.30%', // 转换效率(%)
              maxPower: '46.452', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '02#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '8093.568', // 发电量(kwh)
              efficiency: '97.93%', // 转换效率(%)
              maxPower: '45.705', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '03#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '8020.676', // 发电量(kwh)
              efficiency: '97.62%', // 转换效率(%)
              maxPower: '42.976', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '04#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7494.136', // 发电量(kwh)
              efficiency: '97.53%', // 转换效率(%)
              maxPower: '45.640', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '05#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7711.351', // 发电量(kwh)
              efficiency: '97.34%', // 转换效率(%)
              maxPower: '46.673', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '06#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '8141.494', // 发电量(kwh)
              efficiency: '97.98%', // 转换效率(%)
              maxPower: '46.963', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '07#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7979.523', // 发电量(kwh)
              efficiency: '97.94%', // 转换效率(%)
              maxPower: '47.268', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '08#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7563.814', // 发电量(kwh)
              efficiency: '97.72%', // 转换效率(%)
              maxPower: '46.709', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '09#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7826.665', // 发电量(kwh)
              efficiency: '97.41%', // 转换效率(%)
              maxPower: '45.406', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '10#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7844.573', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '42.938', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '11#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7520.240', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '43.858', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '12#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '7448.202', // 发电量(kwh)
              efficiency: '97.81%', // 转换效率(%)
              maxPower: '45.736', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
          ],
          'day': [
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '1#逆变器\n', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '310.324', // 发电量(kwh)
              efficiency: '97.30%', // 转换效率(%)
              maxPower: '46.452', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '02#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '303.699', // 发电量(kwh)
              efficiency: '97.93%', // 转换效率(%)
              maxPower: '45.705', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '03#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '303.835', // 发电量(kwh)
              efficiency: '97.62%', // 转换效率(%)
              maxPower: '42.976', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '04#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '298.872', // 发电量(kwh)
              efficiency: '97.53%\n', // 转换效率(%)
              maxPower: '45.640', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '05#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '307.220', // 发电量(kwh)
              efficiency: '97.34%', // 转换效率(%)
              maxPower: '46.673', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '06#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '298.447', // 发电量(kwh)
              efficiency: '97.98%', // 转换效率(%)
              maxPower: '46.963', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '07#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '295.376', // 发电量(kwh)
              efficiency: '97.94%', // 转换效率(%)
              maxPower: '47.268', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '08#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '315.399', // 发电量(kwh)
              efficiency: '97.72%', // 转换效率(%)
              maxPower: '46.709', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '09#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '307.978', // 发电量(kwh)
              efficiency: '97.41%', // 转换效率(%)
              maxPower: '45.406', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '10#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '304.673', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '42.938', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '11#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '303.532', // 发电量(kwh)
              efficiency: '97.67%', // 转换效率(%)
              maxPower: '43.858', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
            {
              stationName: '河南侯寨分布式电站\n', // 电站名称
              devName: '12#逆变器', // 设备名称
              devType: '组串式逆变器', // 设备类型
              electricityGeneration: '297.753', // 发电量(kwh)
              efficiency: '97.81%', // 转换效率(%)
              maxPower: '45.736', // 峰值功率(kw)
              installedCapacity: '50.00', // 装机容量(kw)
            },
          ],
        }
      },
    }
  },
}

reportManage.M = module.exports.M = {
  checkSId: function (sId) {
    for (let i = 0; i < reportManage.D.testData.stationSIds.length; i++) {
      if (reportManage.D.testData.stationSIds[i] === sId) {
        return reportManage.D.testData.stationSIds[i]
      }
    }
    return reportManage.D.testData.stationSIds[0]
  },
}

module.exports.R = {
  getStationRunningReport: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = reportManage.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: reportManage.D.testData.stationRunning[stationCode].echart[body.timeType],
        report: reportManage.D.testData.stationRunning[stationCode].report[body.timeType]
      },
    }
  },
  getInverterRunningReport: function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = reportManage.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: reportManage.D.testData.inverterRunning[stationCode].echart[body.timeType],
        report: reportManage.D.testData.inverterRunning[stationCode].report[body.timeType]
      },
    }
  },
}
