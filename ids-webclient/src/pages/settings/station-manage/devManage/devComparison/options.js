
// 获取一天每5分钟的288个点
function getXData() {
  let xData = [];
  let len = 24 * 60 / 5 // 一天每隔5分钟的一个点有288个
  for (let i = 0; i < len; i++) {
    let t = 5 * i
    let h = Math.floor(t / 60)
    let m = t % 60;
    let hour = h < 10 ? "0" + h : "" + h;
    let min = m < 10 ? "0" + m : "" + m;
    xData.push(hour + ":" + min);
  }
  return xData;
}
// 一个基本选项
let options = {
  noDataLoadingOption: {
    text: 'NoData',
    effect: 'bubble',
    effectOption: {
      effect: {
        n: 0 // 气泡个数为0
      }
    }
  },
  color: ['#1EB05B', '#0F95EF', '#1d17ff', '#863e6c', '#d995f0', '#0F1545'],
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      lineStyle: {
        color: '#0D96F0'
      }
    }
  },
  legend: {},
  grid: {
    left: 20,
    right: 20,
    top: '15%',
    bottom: 30,
    containLabel: true,
    borderColor: '#EEEEEE'
  },
  dataZoom: [
    {
      type: 'slider',
      show: true,
      height: 8,
      fillerColor: 'rgba(30, 176, 91, 0.5)',
      borderColor: '#2EA6F6',
      handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
      handleSize: 20,
      handleStyle: {
        color: '#1EB05B'
      },
      shadowBlur: 3,
      shadowColor: 'rgba(0, 0, 0, 0.6)',
      shadowOffsetX: 2,
      shadowOffsetY: 2,
      textStyle: {
        color: '#EEEEEE'
      },
      filterMode: 'empty',
      realtime: false,
      left: 35,
      right: 20,
      bottom: 10,
      xAxisIndex: [0],
      start: 0,
      end: 100
    },
    {
      type: 'inside'
    }
  ],
  xAxis: [
    {
      type: 'category',
      data: getXData(),
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
  yAxis: [],
  series: []
}
// 设置一个y轴的数据
const getOneSerie = (name, data) => {
  return {
    name: name,
    type: 'line',
    smooth: true,
    smoothMonotone: 'x',
    symbol: 'circle',
    symbolSize: 4,
    data: data,
  }
}
// 设置一个数据的单位
const setOneYUnit = (name) => {
  return {
    name: name,
    type: 'value',
    nameTextStyle: {
      color: '#aaa'
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
}
// 将数据格式的方式
const dataToFiexed = (value, count) => {
  if (value !== null && value !== undefined && value !== '') {
    return +(value - 0).toFixed(count)
  }
  return '-'
}
// 显示没有数据的echart信息
const getNoDataOptions = () => {
  var yData = [];
  var xLen = options.xAxis[0].data.length;
  for (var i = 0; i < xLen; i++) {
    yData.push('-');
  }
  var series = options.series = [];
  var yAxis = options.yAxis = [];
  series.push(getOneSerie('-', yData));
  yAxis.push(setOneYUnit(''));
  return options
}
// 获取所有值都是-的y的所有数据，根据选择的设备名字来组装
const initYDataArr = (devNameArr, xData) => {
  let yDataArr = []
  for (let i = 0; i < devNameArr.length; i++) {
    let tmpObj = {};
    tmpObj.name = devNameArr[i];
    tmpObj.data = [];
    tmpObj.type = 'line'
    yDataArr.push(tmpObj);
  }
  var xLen = xData.length;
  for (let i = 0; i < xLen; i++) {
    for (let j = 0; j < yDataArr.length; j++) {
      yDataArr[j].data.push('-');
    }
  }
  return yDataArr
}

const getYDataByDevName = (yDataArr, devName) => {
  for (var i = 0; i < yDataArr.length; i++) {
    if (yDataArr[i] && yDataArr[i].name === devName) {
      return yDataArr[i].data;
    }
  }
  return null;
}
/**
 * 设置y的所有项的数据
 * @param allYDatas
 * @param yDataArr
 * @param devNameArr
 * @param xData
 * @param queryColumn
 * @param count
 */
const getYSerDatas = (allYDatas, yDataArr, devNameArr, xData, queryColumn, count) => {
  let devLen = devNameArr.length
  for (let i = 0; i < devLen; i++) {
    let devName = devNameArr[i]
    let tmpOneData = getYDataByDevName(yDataArr, devName)
    if (tmpOneData !== null) {
      let searchData = allYDatas[devName] || [] // 查询到的数据
      let searchLen = searchData.length
      for (let j = 0; j < searchLen; j++) {
        let oneTmp = searchData[j]
        let cTime = oneTmp.collect_time
        if (oneTmp.collect_time) {
          let dateStr = Date.parse(cTime).format('HH:mm');
          let index = xData.indexOf(dateStr)
          if (index !== -1) {
            tmpOneData[index] = dataToFiexed(oneTmp[queryColumn], count)
          }
        }
      }
    }
  }
}

export default {
  setOneYUnit,
  getNoDataOptions,
  initYDataArr,
  getYSerDatas
}
