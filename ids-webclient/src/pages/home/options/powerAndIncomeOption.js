// 发电量和收益的页面的option等的数据
const getDaysInMonth = () => {
  let nowDate = new Date()
  let year = nowDate.getFullYear()
  let month = nowDate.getMonth() + 1 // 获取当前月的下一个月
  let date = new Date(year, month, 0) // 获取（nowDate）这个月的最后一天
  return date.getDate()
}
// 获取当前时间的月份的字符串
const getDateMouthOfDate = () => {
  let date = new Date()
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  if (month < 10) {
    month = '0' + month
  }
  return year + '-' + month
}
// 获取当前时间的00：00 :00 的时间戳
const getTimeOfZero = (dateStr) => {
  let date = new Date(dateStr)
  date.setHours(0, 0, 0, 0)
  return date.getTime()
}

// 获取当前时间年的字符串
const getYearOfStr = (dateStr) => {
  let date
  if (dateStr) {
    date = new Date(dateStr)
  } else {
    date = new Date()
  }
  return date.getFullYear() + ''
}
// 保留3位有效数字
const getValueOfStr = (value) => {
  if (value === '-' || isNaN(value)) {
    return '0'
  }
  return (value - 0).toFixed(3)
}
// 获取数组 中的最小值
const getArrMin = (arr) => {
  return Math.min.apply(Math, arr)
}
// 移除数组中的指定的值 返回一个新的数组
const removeArrVals = (arr, val) => {
  let result = arr.slice(0)
  let len = (arr && arr.length) || 0
  while (len--) {
    if (result[len] === val) {
      result.splice(len, 1)
    }
  }
  return result
}

const getDatas = (type, data) => {
  let results = {}
  let xData = []
  let yData = []
  let y2Data = []
  let incomeList = {}
  let powerList = {}
  var len = (data && data.length) || 0
  for (var i = 0; i < len; i++) {
    var oneData = data[i]
    incomeList[oneData.collectTime] = oneData.powerProfit
    powerList[oneData.collectTime] = oneData.producePower
  }
  if (type === 'month') { // 月
    let monthDays = getDaysInMonth() // 获取这个月的天数
    let tmpMonthStr = getDateMouthOfDate() // 当前的yyyy-MM 的字符串
    for (let i = 0; i < monthDays; i++) {
      let x = i + 1 < 10 ? '0' + (i + 1) : '' + (i + 1)
      xData.push(x)
      let xTime = getTimeOfZero(tmpMonthStr + '-' + x)
      yData.push((powerList && powerList[xTime]) || '-')
      y2Data.push((incomeList && incomeList[xTime]) || '-')
    }
  } else if (type === 'year') { // 年
    let yearStr = getYearOfStr()
    for (let i = 1; i <= 12; i++) {
      let x = i < 10 ? '0' + i : '' + i
      xData.push(x)
      var xTime = getTimeOfZero(yearStr + '-' + x)
      yData.push((powerList && powerList[xTime]) || '-')
      y2Data.push((incomeList && incomeList[xTime]) || '-')
    }
  } else { // 寿命期
    let allYearArr = []
    for (let year in powerList) {
      allYearArr.push(year)
    }
    for (let year in incomeList) {
      allYearArr.push(year)
    }
    let minYear = (allYearArr.length > 0 && getArrMin(allYearArr) && new Date(getArrMin(allYearArr)).getFullYear()) || new Date().getFullYear()
    let startYear = getYearOfStr(minYear + '') - 0// 开始的年
    let endYear = getYearOfStr() - 0// 结束的年当前时间所在的年
    for (let k = startYear; k <= endYear; k++) {
      xData.push(k)
      let k2 = getTimeOfZero(k + '')
      yData.push((powerList && powerList[k2]) || '-')
      y2Data.push((incomeList && incomeList[k2]) || '-')
    }
  }
  results.xData = xData
  results.yData = yData
  results.y2Data = y2Data
  return results
}

// 获取选项 type：month月 year年 allYear寿命期
const getOption = (type, vm) => {
  // let tooltips = function (p) {
  //   return p
  // }
  let yUnit = 'kWh'
  let y2Unit = vm.$t('unit.RMBUnit2')
  let datas = getDatas(type)
  let xData = datas.xData
  let yData = datas.yData
  let y2Data = datas.y2Data
  const powerAndIncomeArr = vm.$t('home.powerAndIncomeArr') // ['发电量', '收益']
  let option = {
    noDataLoadingOption: {
      text: vm.$t('home.noData'),
      effect: 'bubble',
      effectOption: {
        effect: {
          n: 0 // 气泡个数为0
        }
      }
    },
    color: ['#1EB05B', '#0F95EF'],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        lineStyle: {
          color: '#0D96F0'
        }
      },
      // formatter: tooltips
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
        left: 30,
        right: 56,
        bottom: 12,
        xAxisIndex: [0],
        start: 0,
        end: 100
      },
      {
        type: 'inside'
      }
    ],
    legend: {
      data: powerAndIncomeArr, // ['发电量', '收益']
      textStyle: {
        fontSize: 14,
        color: '#000',
        fontWeight: 500
      },
      selectedMode: false
    },
    grid: {
      left: 10,
      right: 10,
      bottom: 24,
      top: '30%',
      containLabel: true,
      borderColor: '#EEEEEE'
    },
    xAxis: [
      {
        type: 'category',
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
        }
      }
    ],
    yAxis: [
      {
        name: yUnit,
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
      }, {
        name: y2Unit,
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
        name: powerAndIncomeArr[0],
        type: 'bar',
        barCategoryGap: '50%',
        // barWidth: '30%',
        barWidth: 4,
        data: yData,
        yAxisIndex: 0,
        itemStyle: {
          normal: {
            barBorderRadius: 5,
            z: 0
          }
        }
      },
      {
        name: powerAndIncomeArr[1],
        type: 'line',
        barWidth: '13',
        data: y2Data,
        symbol: 'circle',
        symbolSize: 8,
        yAxisIndex: 1
      }
    ]
  }
  let showText = '';
  if (type === 'month') { // 月
    let tmpMonthStr = getDateMouthOfDate()
    showText += tmpMonthStr + '-';
  } else if (type === 'year') { // 年
    showText += getYearOfStr() + '-';
  }
  option.tooltip.formatter = function (p) {
    let x = p[0].name
    let result = showText + x + '<br>'
    result += p[0].seriesName + ' : ' + getValueOfStr(p[0].value) + ' ' + option.yAxis[0].name + '<br>'
    result += p[1].seriesName + ' : ' + y2Unit + ' ' + getValueOfStr(p[1].value)
    return result
  }
  return option
}
/**
 * 设置选项的内容
 * @param option 需要设置的option信息
 * @param type 内容 month:月 year:年 allYear: 寿命期
 * @param data 获取的数据
 */
const setOption = (option, type, data, vm) => {
  if (option) {
    let datas = getDatas(type, data)
    let xData = datas.xData
    let yData = datas.yData
    let y2Data = datas.y2Data
    let tmpYArr = removeArrVals(yData, '-')
    let yUnit = 'kWh'
    if (tmpYArr.length > 0) {
      let maxY = Math.max.apply(Math, tmpYArr)
      let splitNum = 10000
      if (maxY > splitNum) { // 单位转换
        yUnit = vm.$t('unit.WKWh')
        for (let i = 0; i < yData.length; i++) {
          !isNaN(yData[i]) && (yData[i] = ((yData[i] - 0) / splitNum).toFixed(3))
        }
      }
    }
    option.xAxis[0].data = xData
    option.series[0].data = yData
    option.yAxis[0].name = yUnit
    option.series[1].data = y2Data
  }
}

export default {
  getOption,
  setOption
}
