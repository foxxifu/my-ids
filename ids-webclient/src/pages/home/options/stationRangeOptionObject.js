//  电站PR和等效利用小时的echart图表信息的基本信息

/**
 * 获取基本的option选项
 * @param mark 标记是 pr 还是电站排名
 * @returns {}
 */
const getOption = (mark) => {
  let arrName = []
  let arrValue = []
  let option = {
    noDataLoadingOption: {
      text: '无数据',
      effect: 'bubble',
      effectOption: {
        effect: {
          n: 0 // 气泡个数为0
        }
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { // 坐标轴指示器，坐标轴触发有效
        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      },
      formatter: function (p) {
        var val = p[0].value
        return val + (mark === 'pr' ? '%' : ' h')
      }
    },
    xAxis: [{
      type: 'value',
      show: false,
      // 线的颜色
      axisLine: {
        show: false
      }
    }],
    grid: {
      show: true,
      left: '20%',
      top: '5%',
      right: '15%',
      borderWidth: 0,
      height: '84%'
    },
    yAxis: [{
      // 线的颜色
      axisLine: {
        show: false
      },
      inverse: true,
      // 字的颜色
      axisLabel: {
        textStyle: {
          color: '#999',
          fontFamily: 'Microsoft Yahei',
          fontSize: 14
        },
        formatter: function (val) {
          return ''
        }
      },
      axisTick: {
        inside: {
          default: true
        },
        show: false
      },
      type: 'category',
      show: true,
      position: 'left',
      nameLocation: 'start',
      nameTextStyle: {
        align: 'left'
      },
      triggerEvent: true,
      data: arrName
    }],
    series: [
      {
        type: 'bar',
        barWidth: 12,
        // barGap: '-100%',
        itemStyle: {
          normal: {
            color: '#39BE71',
            barBorderRadius: 7,
            label: {
              show: true,
              position: 'right',
              formatter: function (p) {
                var val = p.value
                return val + (mark === 'pr' ? '%' : ' h')
              },
              textStyle: {
                fontFamily: 'Microsoft Yahei',
                fontSize: 14,
                color: '#39BE71'
              }
            }
          }
        },
        data: arrValue
      }
    ]
  }
  return option
}

const setOption = (option, nameArr, valueArr) => {
  if (option) {
    nameArr && (option.yAxis[0].data = nameArr)
    valueArr && (option.series[0].data = valueArr)
    // option.yAxis[0].inverse = !!inverse
  }
}

export default {
  getOption,
  setOption
}
