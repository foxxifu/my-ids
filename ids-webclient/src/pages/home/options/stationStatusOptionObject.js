// 电站状态的对象的option信息
const dataStyle = {
  normal: {
    label: {
      show: false
    },
    labelLine: {
      show: false
    },
    shadowBlur: 40,
    // shadowColor: 'rgba(200, 200, 200, 0.5)',
    shadowColor: '#aaa',
  }
}
const placeHolderStyle = {
  normal: {
    color: '#aaa', // 未完成的圆环的颜色
    label: {
      show: false
    },
    labelLine: {
      show: false
    }
  },
  emphasis: {
    color: '#aaa' // 未完成的圆环的颜色
  }
}
const placeHolderStyleBig = {
  normal: {
    color: 'rgba(44,59,70,0)', // 未完成的圆环的颜色
    label: {
      show: false
    },
    labelLine: {
      show: false
    }
  },
  emphasis: {
    color: 'rgba(44,59,70,0)' // 未完成的圆环的颜色
  }
}

/**
 * 获取电站状态的数据
 * @param tmColor 颜色
 * @param total 总数必须，数字
 * @param value 具体的值
 * @param center 绘制的位置
 * @returns {*,*,*} series的一个饼图的数据
 */
const getStatusSeDatas = (tmColor, total, value, center) => {
  let baseVal = 60
  let prowWidth = 6 // 显示进度的宽度
  let angle = 2 * Math.PI * value / total // 计算与向上的y轴的角度
  // 这里计算有一个弧度的位置，因为长宽不相同，还必须获取到对应的长宽来做比较
  let echarEl = document.getElementById('stationStatusChart')
  let width = echarEl.offsetWidth
  let height = echarEl.offsetHeight
  let anglePos1 = center[0].replace('%', '') - 0 + Math.sin(angle) * baseVal * height / (2.0 * width) + '%'
  let anglePos2 = center[1].replace('%', '') - Math.cos(angle) * baseVal / 2.0 + '%'
  let results = [
    // 无进度的一个圆
    {
      name: 'Pie1',
      type: 'pie',
      clockWise: false,
      radius: [baseVal + '%', baseVal + 2 + '%'],
      itemStyle: dataStyle,
      hoverAnimation: false,
      center: center,
      data: [
        {
          value: 1,
          name: 'invisible',
          itemStyle: placeHolderStyle,
        }],
      zlevel: 0,
    },
    // 进度的数据
    {
      name: 'Pie2',
      type: 'pie',
      clockWise: false,
      radius: [baseVal - prowWidth + '%', baseVal + 6 + '%'],
      itemStyle: dataStyle,
      hoverAnimation: false,
      center: center,
      data: [
        {
          value: total - value,
          name: 'invisible',
          itemStyle: placeHolderStyleBig,
        },
        {
          value: value,
          label: {
            normal: {
              formatter: '{d}%',
              position: 'center',
              color: tmColor,
              show: true,
              textStyle: {
                fontSize: '20', // 字体大小
                fontWeight: 'normal',
                color: tmColor // 圆环中间文字的颜色
              }
            }
          },
          itemStyle: {
            normal: {
              color: tmColor,
              shadowColor: tmColor,
              shadowBlur: 10
            }
          }
        }],
      zlevel: 2,
    },
    // 起始点的数据
    {
      name: 'Pie3',
      type: 'pie',
      clockWise: false,
      radius: [baseVal - 8 + '%', baseVal + 8 + '%'],
      itemStyle: dataStyle,
      hoverAnimation: false,
      center: center,
      data: [
        {
          value: 99,
          name: 'invisible',
          itemStyle: placeHolderStyleBig,
        },
        {
          value: 1,
          itemStyle: {
            normal: {
              color: tmColor,
              shadowColor: '#5fb682',
              shadowBlur: 10
            }
          }
        }],
      zlevel: 3,
    }
  ]
  if (value * 1.0 / total > 0.02) { // 只要大于2%的都才显示弧度的信息
    results.push(// 进度结尾的圆角的弧度
      {
        name: 'Pie4',
        // color: [tmColor],
        type: 'pie',
        radius: [0, prowWidth + '%'],
        center: [anglePos1, anglePos2],
        avoidLabelOverlap: false,
        animation: false,
        labelLine: {
          normal: {
            show: false
          }
        },
        data: [
          {
            value: 10,
            name: ' ',
            itemStyle: {
              normal: {
                // color: '#3dd4de',
                color: tmColor,
                shadowColor: tmColor,
                shadowBlur: 10
              }
            }
          },
        ],
        zlevel: 1,
        silent: true
      })
  }
  return results
}
/**
 * 获取电站状态的所有数据的series数组的数据
 * @param obj1 正常的数据
 * @param obj2 异常的数据
 * @param obj3 通讯中断的数据
 * @returns {Array.<*>}
 */
const getAllSeData = (obj1 = {total: 1, value: 0}, obj2 = {total: 1, value: 0}, obj3 = {total: 1, value: 0}) => {
  let vCenter = '65%'
  return [].concat(getStatusSeDatas('#f33', obj2.total, obj2.value, ['10%', vCenter]))
    .concat(getStatusSeDatas('#A7A7A7', obj3.total, obj3.value, ['42.5%', vCenter]))
    .concat(getStatusSeDatas('#39BE71', obj1.total, obj1.value, ['75%', vCenter]))
}

export default {
  getAllSeData
}
