
const getSer = (max, center = ['50%', '50%'], radius = '100%') => {
  let middelRadius, smallRadius;
  middelRadius = (radius.replace('%', '') - 0) / 5;
  smallRadius = middelRadius / 2 + '%';
  middelRadius += '%';
  return [
    {
      name: name,
      type: 'gauge',
      min: 0,
      max: max,
      center: center,
      splitNumber: 10,
      startAngle: 215,
      endAngle: -35,
      radius: radius,
      // 坐标轴线
      axisLine: {
        show: true,
        // 属性lineStyle控制线条样式
        lineStyle: {
          color: [[0, '#9A9A9A'], [1, '#9A9A9A']],
          width: 2,
        }
      },
      // 坐标轴小标记
      axisLabel: {
        show: false,
      },
      // 坐标轴小标记, 小刻度
      axisTick: {
        show: false,
      },
      // 分隔线,大刻度
      splitLine: {
        // 属性length控制线长
        length: 10,
        // 属性lineStyle（详见lineStyle）控制线条样式
        lineStyle: {
          width: 2,
          color: 'auto'// 颜色制动配置
        }
      },
      // 分隔线
      pointer: {
        // 默认透明
        width: 2,
      },
      title: {
        offsetCenter: [0, '100%'],
        // 其余属性默认使用全局文本样式，详见TEXTSTYLE
        textStyle: {
          fontWeight: 'bolder',
          fontSize: 14,
          color: '#7dffd2',
          shadowColor: '#fff', // 默认透明
          shadowBlur: 10
        }
      },
      detail: {
        show: false,
      },
      data: [0],
      zlevel: 1,
    },
    {
      name: '',
      color: ['#1092B990'],
      type: 'pie',
      radius: [0, middelRadius],
      center: center,
      avoidLabelOverlap: false,
      labelLine: {
        normal: {
          show: false
        }
      },
      data: [
        {value: 1}
      ],
      zlevel: 0,
      silent: true
    },
    {
      name: '2',
      color: ['#666'],
      type: 'pie',
      radius: [0, smallRadius],
      center: center,
      avoidLabelOverlap: false,
      labelLine: {
        normal: {
          show: false
        }
      },
      data: [
        {value: 10, name: ' '}
      ],
      zlevel: 2,
      silent: true
    }
  ];
}

/**
 * kpi的echart的option选项
 * @param max 最大值
 * @param center 位置
 * @param radius 仪盘的半径
 * @returns {{tooltip: {formatter: string}, series: [null,null,null]}}
 */
const getOption = (dayMax, yearMax) => {
  let serArr = [].concat(getSer(dayMax, ['32%', '61%'])).concat(getSer(yearMax, ['68%', '71%'], '60%'));
  return {
    tooltip: {
      formatter: '{a} {b} : {c}'
    },
    series: serArr
  }
}
/**
 * 设置指定gauge图表的option的值
 * @param option 选项
 * @param value 值
 */
const setOption = (option, value, value2) => {
  if (option && option.series) {
    option.series[0].data = [value];
    option.series[3].data = [value2];
  }
}

export default {
  getOption,
  setOption
}
