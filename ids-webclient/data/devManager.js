module.exports.R = {
// 设备对比，根据设备获取数据的模拟数据
  deviceComparisonTable: function (params) {
    function getDatas (params) {
      let param = (params && params.body && JSON.parse(params.body)) || {}
      let idsStr = param.devIds
      let idArr = (idsStr && idsStr.split(',')) || null
      let results = []
      for (let i = 0; i < idArr.length; i++) {
        results.push({
          num: 8,
          'dev_id': (idArr && +idArr[i]) || 1, // 设备id
          'vender_name': '华为', // 厂家名称
          'manufacturer': '协鑫', // 组件所属厂家
          'module_production_date': 1523269327744, // 组件投产日期
          'module_type': '1', // 组件类型
          'module_ratio': 15.7 + +(Math.random()).toFixed(1), // 标称组件转换效率(%)
          'max_power_temp_coef': -0.82 + +(Math.random()).toFixed(1), // 峰值功率温度系数(%/℃)
          // 组件1-8的容量
          pv1_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv2_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv3_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv4_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv5_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv6_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv7_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv8_capacity: 8000 + +(Math.random() * 10).toFixed(),
          'total_capacity': 12080.55, // 发电量
          'mppt_power': 122.9375, // 输出功率(kw)
          // 1-8串的电流电压
          'pv1_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv1_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv2_u': 8.2 + +(Math.random() * 4).toFixed(),
          'pv2_i': 6.5 + +(Math.random() * 4).toFixed(),
          'pv3_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv3_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv4_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv4_i': 6 + +(Math.random() * 4).toFixed(),
          'pv5_u': 8 + +(Math.random() * 4).toFixed(),
          'pv5_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv6_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv6_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv7_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv7_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv8_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv8_i': 5.5 + +(Math.random() * 4).toFixed(),
        })
        return results
      }
    }
    let resutls = getDatas(params)
    return {
      code: 1,
      results: resutls
    }
  },
  getDeviceSignal: function (params) { // 获取设备的信号点
    return {
      code: 1,
      results: [
        {
          "column_name": "inverter_state",
          "display_name": "逆变器状态"
        },
        {
          "column_name": "ab_u",
          "display_name": "电网AB电压"
        },
        {
          "column_name": "bc_u",
          "display_name": "电网BC电压"
        },
        {
          "column_name": "ca_u",
          "display_name": "电网CA电压"
        },
        {
          "column_name": "a_u",
          "display_name": "A相电压"
        },
        {
          "column_name": "b_u",
          "display_name": "B相电压"
        },
        {
          "column_name": "c_u",
          "display_name": "C相电压"
        },
        {
          "column_name": "a_i",
          "display_name": "电网A相电流"
        },
        {
          "column_name": "b_i",
          "display_name": "电网B相电流"
        },
        {
          "column_name": "c_i",
          "display_name": "电网C相电流"
        },
        {
          "column_name": "conversion_efficiency",
          "display_name": "逆变器转换效率(厂家)"
        },
        {
          "column_name": "temperature",
          "display_name": "机内温度"
        },
        {
          "column_name": "power_factor",
          "display_name": "功率因数"
        },
        {
          "column_name": "grid_frequency",
          "display_name": "电网频率"
        },
        {
          "column_name": "active_power",
          "display_name": "有功功率"
        },
        {
          "column_name": "reactive_power",
          "display_name": "输出无功功率"
        },
        {
          "column_name": "day_capacity",
          "display_name": "当日发电量"
        },
        {
          "column_name": "mppt_power",
          "display_name": "MPPT输入总功率"
        },
        {
          "column_name": "pv1_u",
          "display_name": "PV1输入电压"
        },
        {
          "column_name": "pv2_u",
          "display_name": "PV2输入电压"
        },
        {
          "column_name": "pv3_u",
          "display_name": "PV3输入电压"
        },
        {
          "column_name": "pv4_u",
          "display_name": "PV4输入电压"
        },
        {
          "column_name": "pv5_u",
          "display_name": "PV5输入电压"
        },
        {
          "column_name": "pv6_u",
          "display_name": "PV6输入电压"
        },
        {
          "column_name": "pv7_u",
          "display_name": "PV7输入电压"
        },
        {
          "column_name": "pv8_u",
          "display_name": "PV8输入电压"
        },
        {
          "column_name": "pv9_u",
          "display_name": "PV9输入电压"
        },
        {
          "column_name": "pv10_u",
          "display_name": "PV10输入电压"
        },
        {
          "column_name": "pv11_u",
          "display_name": "PV11输入电压"
        },
        {
          "column_name": "pv12_u",
          "display_name": "PV12输入电压"
        },
        {
          "column_name": "pv13_u",
          "display_name": "PV13输入电压"
        },
        {
          "column_name": "pv14_u",
          "display_name": "PV14输入电压"
        },
        {
          "column_name": "pv1_i",
          "display_name": "PV1输入电流"
        },
        {
          "column_name": "pv2_i",
          "display_name": "PV2输入电流"
        },
        {
          "column_name": "pv3_i",
          "display_name": "PV3输入电流"
        },
        {
          "column_name": "pv4_i",
          "display_name": "PV4输入电流"
        },
        {
          "column_name": "pv5_i",
          "display_name": "PV5输入电流"
        },
        {
          "column_name": "pv6_i",
          "display_name": "PV6输入电流"
        },
        {
          "column_name": "pv7_i",
          "display_name": "PV7输入电流"
        },
        {
          "column_name": "pv8_i",
          "display_name": "PV8输入电流"
        },
        {
          "column_name": "pv9_i",
          "display_name": "PV9输入电流"
        },
        {
          "column_name": "pv10_i",
          "display_name": "PV10输入电流"
        },
        {
          "column_name": "pv11_i",
          "display_name": "PV11输入电流"
        },
        {
          "column_name": "pv12_i",
          "display_name": "PV12输入电流"
        },
        {
          "column_name": "pv13_i",
          "display_name": "PV13输入电流"
        },
        {
          "column_name": "pv14_i",
          "display_name": "PV14输入电流"
        },
        {
          "column_name": "total_capacity",
          "display_name": "累计发电量"
        },
        {
          "column_name": "on_time",
          "display_name": "逆变器开机时间"
        },
        {
          "column_name": "off_time",
          "display_name": "逆变器关机时间"
        },
        {
          "column_name": "mppt_total_cap",
          "display_name": "直流输入总电量"
        },
        {
          "column_name": "mppt_1_cap",
          "display_name": "MPPT1直流累计发电量"
        },
        {
          "column_name": "mppt_2_cap",
          "display_name": "MPPT2直流累计发电量"
        },
        {
          "column_name": "mppt_3_cap",
          "display_name": "MPPT3直流累计发电量"
        },
        {
          "column_name": "mppt_4_cap",
          "display_name": "MPPT4直流累计发电量"
        }
      ]
    }
  },
  // 获取echart的对比数据
  deviceComparisonChart: function (params) {
    function getPWXData (x, max, xs) { // 获取一个曲线的数据
      return (max - Math.pow(x - 156, 2)) * xs
    }
    function getDatas (params) {
      let param = (params && params.body && JSON.parse(params.body)) || {}
      // 设备名称是不会发送到后台，这里就使用设备名称来给数据
      let devNames = (param.devNames && param.devNames.split(',')) || []
      let queryColumn = param.queryColumn // 查询的信号点名称
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      let begin = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day)
      // 一天每5分钟的点有288个即
      let timeCount = 24 * 60 / 5
      let results = {}
      for (let k = 0; k < devNames.length; k++) {
        let arr = []
        let max = 9140.77 + +(Math.random() * 1200).toFixed(2) * (+Math.random().toFixed() > 0 ? 1 : -1)
        for (let i = 0; i < timeCount; i++) {
          let minTime = i * 5
          let tmpHour = Math.floor(minTime / 60) // 小时，一天小时的分钟数对60取下整数就是小时的数
          tmpHour = tmpHour > 10 ? tmpHour : '0' + tmpHour
          let min = minTime % 60 // 分钟
          min = min > 10 ? min : '0' + min
          let time = new Date(begin + ' ' + tmpHour + ':' + min + ':' + '00').getTime()
          let tmpOne = {
            "collect_time": time
          }
          tmpOne[queryColumn] = i <= 71 || i >= 239 ? 0 : getPWXData(i, max, 0.2)
          arr.push(tmpOne)
        }
        results[devNames[k]] = arr
      }
      return results
    }
    let results = getDatas(params)
    return {
      "code": 1,
      "results": results
    }
  },
  getAllDevType: function (params) {
    return {
      "code": 1,
      "message": "string",
      "results": {
        "环境监测仪": 10,
        "电表": 17,
        "直流汇流箱": 15,
        "箱变": 8,
        "组串式逆变器": 1,
        "集中式逆变器": 14
      }
    }
  }
}
