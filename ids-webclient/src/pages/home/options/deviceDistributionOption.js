// 设备分布的option选项信息

const dataStyle = {
  normal: {
    label: {show: false},
    labelLine: {show: false}
  }
}

const legendDataTextStyle = (color) => {
  return {
    fontSize: 12,
    lineHeight: 14,
    color: color
  }
}

const placeHolderStyle = {
  normal: {
    color: 'rgba(182,225,249,0.9)',
    label: {show: false},
    labelLine: {show: false}
  },
  emphasis: {
    color: 'rgba(182,225,249,0.9)',
  }
}
const placeHolderStyle2 = {
  normal: {
    color: 'rgba(0,0,0,0)',
    label: {show: false},
    labelLine: {show: false}
  },
  emphasis: {
    color: 'rgba(0,0,0,0)'
  }
}
// 获取数据分布的具体的数据
const getSerDatas = (data = {}) => {
  let total;
  let zj = data.zj || 0 // 组件
  let nbq1 = data.nbq1 || 0 // 逆变器
  let nbq2 = data.nbq2 || 0 // 逆变器的第二种
  let zlhlx = data.zlhlx || 0 // 直流汇流箱
  total = zj + nbq1 + nbq2 + zlhlx
  if (total === 0) {
    total = 100
  }
  return {total: total, zj: zj, nbq1: nbq1, nbq2: nbq2, zlhlx: zlhlx}
}
const getOneData = (value, total) => {
  if (!value) {
    return 0
  }
  return (value * 0.75 / total).toFixed(4) - 0
}

const setLegendItemGap = (option) => {
  if (option) {
    let height = document.getElementById('home_dev_dis_chart_div').offsetHeight;
    option.legend.itemGap = height * 0.095 - 10;
  }
}
// 获取设备分布的option
const getOption = (vm) => {
  let datas = getSerDatas();
  const serNameArr = vm.$t('home.serNameArr') // ['组件', '组串式逆变器', '集中式逆变器', '直流汇流箱']
  const colors = ['#2CA560', '#D69842', '#1795E5', '#8FD7F2'];
  let total = datas.total
  let zj = getOneData(datas.zj, total) * 100
  let nbq1 = getOneData(datas.nbq1, total) * 100
  let nbq2 = getOneData(datas.nbq2, total) * 100
  let zlhlx = getOneData(datas.zlhlx, total) * 100
  let myNameArr = [serNameArr[0] + ' ' + (datas.zj * 100.0 / total).toFixed(2) + '%', serNameArr[1] + ' ' + (datas.nbq1 * 100.0 / total).toFixed(2) + '%',
    serNameArr[2] + ' ' + (datas.nbq2 * 100.0 / total).toFixed(2) + '%', serNameArr[3] + ' ' + (datas.zlhlx * 100.0 / total).toFixed(2) + '%']
  let data75 = 75;
  let data25 = 25;
  let maxR = 80; // 圆环开始的半径 百分比
  let jg = 20; // 圆环之间的间隔 百分比
  let yhWidth = 3; // 圆环的宽度 百分比
  let center = ['24%', '56%']; // 中心位置
  let option = {
    tooltip: {
      show: true,
      // formatter: "{a} <br/>{b} : {c} ({d}%)"
      formatter: function (a) {
        return a.name
      }
    },
    legend: {
      orient: 'vertical',
      top: '12%',
      left: '24%',
      icon: 'circle',
      itemGap: 24,
      itemWeight: 5,
      itemHeight: 5,
      data: [
        {name: myNameArr[0], textStyle: legendDataTextStyle(colors[0])},
        {name: myNameArr[1], textStyle: legendDataTextStyle(colors[1])},
        {name: myNameArr[2], textStyle: legendDataTextStyle(colors[2])},
        {name: myNameArr[3], textStyle: legendDataTextStyle(colors[3])}
      ]
    },
    series: [
      {
        name: serNameArr[0],
        type: 'pie',
        clockWise: false,
        center: center,
        radius: [maxR - yhWidth + '%', maxR + '%'],
        itemStyle: dataStyle,
        data: [
          {
            value: zj,
            name: myNameArr[0],
            itemStyle: { color: colors[0] }
          },
          {
            value: data75 - zj,
            name: myNameArr[0],
            itemStyle: placeHolderStyle
          },
          {
            value: data25,
            name: myNameArr[0],
            itemStyle: placeHolderStyle2
          }
        ],
        animation: false // 无动画效果
      },
      {
        name: serNameArr[1],
        type: 'pie',
        center: center,
        clockWise: false,
        radius: [maxR - jg - yhWidth + '%', maxR - jg + '%'],
        itemStyle: dataStyle,
        data: [
          {
            value: nbq1,
            name: myNameArr[1],
            itemStyle: { color: colors[1] }
          },
          {
            value: data75 - nbq1,
            name: myNameArr[1],
            itemStyle: placeHolderStyle
          },
          {
            value: data25,
            name: myNameArr[1],
            itemStyle: placeHolderStyle2
          }
        ],
        animation: false // 无动画效果
      },
      {
        name: serNameArr[2],
        type: 'pie',
        center: center,
        clockWise: false,
        radius: [maxR - jg * 2 - yhWidth + '%', maxR - jg * 2 + '%'],
        itemStyle: dataStyle,
        data: [
          {
            value: nbq2,
            name: myNameArr[2],
            itemStyle: { color: colors[2] }
          },
          {
            value: data75 - nbq2,
            name: myNameArr[2],
            itemStyle: placeHolderStyle
          },
          {
            value: data25,
            name: myNameArr[2],
            itemStyle: placeHolderStyle2
          }
        ],
        animation: false // 无动画效果
      },
      {
        name: serNameArr[3],
        type: 'pie',
        center: center,
        clockWise: false,
        radius: [maxR - jg * 3 - yhWidth + '%', maxR - jg * 3 + '%'],
        itemStyle: dataStyle,
        data: [
          {
            value: zlhlx,
            name: myNameArr[3],
            itemStyle: { color: colors[3] }
          },
          {
            value: data75 - zlhlx,
            name: myNameArr[3],
            itemStyle: placeHolderStyle
          },
          {
            value: data25,
            name: myNameArr[3],
            itemStyle: placeHolderStyle2
          }
        ],
        animation: false // 无动画效果
      }
    ]
  }
  setLegendItemGap(option);
  return option
}
/**
 * 设置选项内容
 * @param option 需要设置的option
 * @param data 对应的数据
 */
const setOption = (option, data, vm) => {
  if (option) {
    let datas = getSerDatas(data);
    const serNameArr = vm.$t('home.serNameArr') // ['组件', '组串式逆变器', '集中式逆变器', '直流汇流箱']
    let total = datas.total
    // 按次序是 组件 nbq1 nbq2 zlhlx
    let valueArr = [getOneData(datas.zj, total) * 100, getOneData(datas.nbq1, total) * 100, getOneData(datas.nbq2, total) * 100, getOneData(datas.zlhlx, total) * 100];
    let myNameArr = [serNameArr[0] + ' ' + (datas.zj * 100.0 / total).toFixed(2) + '%', serNameArr[1] + ' ' + (datas.nbq1 * 100.0 / total).toFixed(2) + '%',
      serNameArr[2] + ' ' + (datas.nbq2 * 100.0 / total).toFixed(2) + '%', serNameArr[3] + ' ' + (datas.zlhlx * 100.0 / total).toFixed(2) + '%']
    for (let i = 0; i < 4; i++) {
      let tempName = myNameArr[i];
      option.legend.data[i].name = tempName;
      for (let j = 0; j < 3; j++) {
        let tmpData = option.series[i].data[j];
        tmpData.name = tempName;
        j === 0 && (tmpData.value = valueArr[i]);
        j === 1 && (tmpData.value = 75 - valueArr[i]);
      }
    }
  }
}
export default {
  getOption,
  setOption,
  setLegendItemGap
}
