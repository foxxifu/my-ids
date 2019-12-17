function getSeriesVal () {
  let begin = new Date('2018-01-01 00:00:00')
  let end = new Date('2018-01-01 23:59:59')
  let result = [[], []]
  while (begin.getTime() < end.getTime()) {
    result[0].push(begin.format('hh:mm'))
    result[1].push(Math.ceil(Math.random() * 2000))
    begin = new Date(begin.getTime() + 3600 * 1000)
  }
  return result
}

let seriesVal = getSeriesVal()
let xData = seriesVal[0]

let option = {
  noDataLoadingOption: {
    text: 'No Data',
    effect: 'bubble',
    effectOption: {
      effect: {
        n: 0 // 气泡个数为0
      }
    }
  },
  dataZoom: [
    {
      type: 'slider',
      show: true,
      height: 6,
      backgroundColor: 'rgba(10, 159, 254, 0.2)',
      filterMode: 'empty',
      fillerColor: 'rgba(10, 159, 254, 0.5)',
      borderColor: 'rgba(10, 159, 254, 0.2)',
      borderRadius: 10,
      handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
      handleSize: 20,
      handleStyle: {
        color: '#0a9ffe'
      },
      shadowBlur: 30,
      shadowColor: 'rgba(10, 159, 254, 0.5)',
      shadowOffsetX: 2,
      shadowOffsetY: 2,
      textStyle: {
        color: '#666'
      },
      left: 48,
      right: 48,
      bottom: 10,
      xAxisIndex: [0],
      start: 0,
      end: 100
    },
    {
      type: 'inside'
    }
  ],
  // legend: {
  //   data: ['发电量(kwh)', '收益(万元)'],
  //   textStyle: {
  //     fontSize: 14,
  //     color: '#000',
  //     fontWeight: 500
  //   },
  //   selectedMode: false
  // },
  color: ['#1EB05B', '#0F95EF'],
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      lineStyle: {
        color: '#0D96F0'
      }
    },
  },
  grid: {
    left: 20,
    right: 20,
    bottom: 20,
    top: '15%',
    containLabel: true,
    borderColor: '#EEEEEE'
  },
  xAxis: [
    {
      type: 'category',
      boundaryGap: false,
      data: xData,
      axisTick: {
        alignWithLabel: true
      },
      axisLine: {
        lineStyle: {
          color: '#3BABC4'
        }
      },
      splitLine: {
        show: false,
        lineStyle: {
          color: '#000',
          type: 'dashed'
        }
      },
    }
  ],
  yAxis: [
    {
      name: 'kW',
      type: 'value',
      nameTextStyle: {
        color: '#000'
      },
      axisLine: {
        lineStyle: {
          color: '#3BABC4'
        }
      },
      splitLine: {
        show: false,
        lineStyle: {
          color: '#000',
          type: 'dashed'
        }
      }
    }
  ],
  series: [
    {
      name: 'kW',
      type: 'line',
      barCategoryGap: '30%',
      barWidth: '30%',
      data: [0, 0, 0, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2050, 2100, 2050, 1800, 1700, 1600, 1500, 1300, 1200, 0, 0, 0],
      yAxisIndex: 0,
      itemStyle: {
        normal: {
          barBorderRadius: 5,
          z: 0
        }
      },
      areaStyle: {}
    }
  ]
}

let getOption = function () {
  return option
}

export default {
  getOption
}
