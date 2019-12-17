/* eslint-disable no-multi-spaces */
module.exports.R = {
  // 获取系统时间之后3天的数据
  getWeatherDaily: {
    code: 1,
    results: {
      'results': [{
        'location': {
          'id': 'WX4FBXXFKE4F',
          'name': '北京',
          'country': 'CN',
          'path': '北京,北京,中国',
          'timezone': 'Asia/Shanghai',
          'timezone_offset': '+08:00'
        },
        'daily': [
          {                             // 返回指定days天数的结果
            'date': '2018-05-28',             // 日期
            'text_day': '多云',               // 白天天气现象文字
            'code_day': '4',                  // 白天天气现象代码
            'text_night': '晴',               // 晚间天气现象文字
            'code_night': '0',                // 晚间天气现象代码
            'high': '26',                     // 当天最高温度
            'low': '17',                      // 当天最低温度
            'precip': '0',                    // 降水概率，范围0~100，单位百分比
            'wind_direction': '东风4级',       // 风向文字
            'wind_direction_degree': '255',   // 风向角度，范围0~360
            'wind_speed': '9.66',             // 风速，单位km/h（当unit=c时）、mph（当unit=f时）
            'wind_scale': ''                  // 风力等级
          },
          {
            'date': '2018-05-29',
            'text_day': '晴',
            'code_day': '0',
            'text_night': '晴',
            'code_night': '0',
            'high': '27',
            'low': '17',
            'precip': '0',
            'wind_direction': '东风4级',
            'wind_direction_degree': '157',
            'wind_speed': '17.7',
            'wind_scale': '3'
          },
          {
            'date': '2018-05-30',
            'text_day': '晴',
            'code_day': '0',
            'text_night': '晴',
            'code_night': '0',
            'high': '27',
            'low': '17',
            'precip': '0',
            'wind_direction': '东风4级',
            'wind_direction_degree': '157',
            'wind_speed': '17.7',
            'wind_scale': '3'
          },
        ],
        'last_update': '2018-01-20T18:00:00+08:00' // 数据更新时间（该城市的本地时间）
      }]
    }
  },
  // 获取实时的温度值
  getWeatherNow: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'results': [
        {
          'last_update': '2018-05-31T10:10:00+08:00',
          'location': {
            'country': 'CN',
            'id': 'WX4FBXXFKE4F',
            'name': '北京',
            'path': '北京,北京,中国',
            'timezone': 'Asia/Shanghai',
            'timezone_offset': '+08:00'
          },
          'now': {
            'code': '0',
            'temperature': '25',
            'text': '晴'
          }
        }
      ]
    }
  }
}
