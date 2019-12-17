// 系统告警统计的界面option
// 路径的svg坐标
const M_PATH = 'path://M 0 5 C 0 2.5 2.5 0 5 0 L 40 0 C 42.5 0 45 2.5 45 5 L 45 10 C 45 12.5 42.5 15 40 15 L 5 15 C 2.5 15 0 12.5 0 10 ZM 0 35 C 0 32.5 2.5 30 5 30 L 40 30 C 42.5 30 45 32.5 45 35 L 45 40 C 45 42.5 42.5 45 40 45 L 5 45 C 2.5 45 0 42.5 0 40 ZM 0 65 C 0 62.5 2.5 60 5 60 L 40 60 C 42.5 60 45 62.5 45 65 L 45 70 C 45 72.5 42.5 75 40 75 L 5 75 C 2.5 75 0 72.5 0 70 ZM 0 95 C 0 92.5 2.5 90 5 90 L 40 90 C 42.5 90 45 92.5 45 95 L 45 100 C 45 102.5 42.5 105 40 105 L 5 105 C 2.5 105 0 102.5 0 100 ZM 0 125 C 0 122.5 2.5 120 5 120 L 40 120 C 42.5 120 45 122.5 45 125 L 45 130 C 45 132.5 42.5 135 40 135 L 5 135 C 2.5 135 0 132.5 0 130 ZM 0 155 C 0 152.5 2.5 150 5 150 L 40 150 C 42.5 150 45 152.5 45 155 L 45 160 C 45 162.5 42.5 165 40 165 L 5 165 C 2.5 165 0 162.5 0 160 ZM 0 185 C 0 182.5 2.5 180 5 180 L 40 180 C 42.5 180 45 182.5 45 185 L 45 190 C 45 192.5 42.5 195 40 195 L 5 195 C 2.5 195 0 192.5 0 190 ZM 0 215 C 0 212.5 2.5 210 5 210 L 40 210 C 42.5 210 45 212.5 45 215 L 45 220 C 45 222.5 42.5 225 40 225 L 5 225 C 2.5 225 0 222.5 0 220 ZM 0 245 C 0 242.5 2.5 240 5 240 L 40 240 C 42.5 240 45 242.5 45 245 L 45 250 C 45 252.5 42.5 255 40 255 L 5 255 C 2.5 255 0 252.5 0 250 ZM 0 275 C 0 272.5 2.5 270 5 270 L 40 270 C 42.5 270 45 272.5 45 275 L 45 280 C 45 282.5 42.5 285 40 285 L 5 285 C 2.5 285 0 282.5 0 280 Z'
const symbols = [M_PATH, M_PATH, M_PATH]
const pathSize = ['40%', '100%'] // 路径的大小

const getLableNorm = (showText, color) => {
  let label = {
    normal: {
      show: true,
      formatter: function (params) {
        return showText
      },
      position: 'bottom',
      offset: [0, 0],
      textStyle: {
        color: color,
        fontSize: 16
      }
    }
  }
  return label
}

/**
 * 获取告警初始化的信息
 */
const getAlarmCountInitOption = (vm) => {
  let bodyMax, prompt, secondary, serious
  bodyMax = 1
  prompt = 0
  secondary = 0
  serious = 0;
  const colorArr = ['#f33', '#ff9933', '#09A8D9']
  let alarmOption = {
    tooltip: {
      show: false
    },
    xAxis: {
      data: ['a', 'b', 'c'],
      axisTick: {show: false},
      axisLine: {show: false},
      axisLabel: {show: false}
    },
    yAxis: {
      show: false,
      max: bodyMax,
    },
    grid: {
      top: 'center',
      left: '0%',
      height: '70%',
      width: '100%'
    },
    markLine: {
      z: -100
    },
    series: [{
      name: 'typeA',
      type: 'pictorialBar',
      symbolClip: true,
      symbolBoundingData: bodyMax,
      data: [
        {
          value: serious,
          symbolSize: pathSize,
          symbol: symbols[2],
          label: getLableNorm(vm.$t('home.critical'), colorArr[0]),
          itemStyle: {
            normal: {
              color: colorArr[0]
            }
          }
        }, {
          value: secondary,
          symbolSize: pathSize,
          symbol: symbols[1],
          label: getLableNorm(vm.$t('home.minor'), colorArr[1]), // 一般告警
          itemStyle: {
            normal: {
              color: colorArr[1]
            }
          }
        }, {// 显示占的百分比的进度
          value: prompt,
          symbolSize: pathSize,
          symbol: symbols[0],
          label: getLableNorm(vm.$t('home.warning'), colorArr[2]),
          itemStyle: {
            normal: {
              color: colorArr[2]
            }
          },
        }],
      // markLine: markLineSetting,
      z: 10,
      silent: true
    }, {// 背景的灰色的
      name: 'full',
      type: 'pictorialBar',
      symbolBoundingData: bodyMax,
      animationDuration: 0,
      itemStyle: {
        normal: {
          color: '#CFE0EF'
        }
      },
      data: [{
        value: 0,
        symbolSize: pathSize,
        symbol: symbols[0]
      }, {
        value: 0,
        symbolSize: pathSize,
        symbol: symbols[1]
      }, {
        value: 0,
        symbolSize: pathSize,
        symbol: symbols[2]
      }],
      silent: true
    }]
  }
  return alarmOption
}

/**
 * 设置告警统计的选项
 * @param option 需要设置的选项
 * @param bodyMax 最大值（下面四个参数的和，除非后面的4个参数都是0）
 * @param prompt 提示
 * @param secondary 一般
 * @param serious 严重
 */
const setAlarmCountOption = (option, bodyMax = 1, prompt = 0, secondary = 0, serious = 0) => {
  if (!option) {
    return
  }
  option.yAxis && (option.yAxis.max = bodyMax)
  let s1
  if (option.series && option.series instanceof Array && (s1 = option.series[0])) {
    s1.symbolBoundingData = bodyMax
    if (s1.data && s1.data instanceof Array) {
      s1.data[0] && (s1.data[0].value = serious)
      s1.data[1] && (s1.data[1].value = secondary)
      s1.data[2] && (s1.data[2].value = prompt)
    }
  }
  let s2
  if (s1 && (s2 = option.series[1])) {
    s2.symbolBoundingData = bodyMax
  }
}

export {
  getAlarmCountInitOption,
  setAlarmCountOption
}
