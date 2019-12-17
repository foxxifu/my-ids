import echarts from 'echarts'

/**
 * 实时发电状态图表配置
 * @param obj1 {{total: Number, value: Number, unit: String}} 实时功率
 * @param obj2 {{total: Number, value: Number, unit: String}} 日发电量
 * @param obj3 {{total: Number, value: Number, unit: String}} 今日收益
 * @returns {*, *, *}
 */
const powerGeneration = (obj1 = {total: 1, value: 0, unit: ''}, obj2 = {total: 1, value: 0, unit: ''}, obj3 = {
  total: 1,
  value: 0,
  unit: ''
}) => {
  let option = {}

  const dataStyle = {
    normal: {
      label: {
        show: false
      },
      labelLine: {
        show: false
      },
      shadowBlur: 40,
      shadowColor: 'rgba(40, 40, 40, 0.5)',
    }
  }

  const placeHolderStyle = {
    normal: {
      color: 'rgba(100, 100, 100, 0.8)', // 未完成的圆环的颜色
      label: {
        show: false
      },
      labelLine: {
        show: false
      }
    },
    emphasis: {
      color: 'rgba(100, 100, 100, 0.8)' // 未完成的圆环的颜色
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
   * @param unit 单位
   * @returns {*,*,*} series的一个饼图的数据
   */
  const getSeriesData = function (tmColor, total, value, center, unit) {
    let baseVal = 60
    let prowWidth = 6 // 显示进度的宽度
    let angle = 2 * Math.PI * value / total // 计算与向上的y轴的角度
    // 这里计算有一个弧度的位置，因为长宽不相同，还必须获取到对应的长宽来做比较
    let echarEl = document.getElementById('kpi_powerGeneration')
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
        animation: false,
        center: center,
        data: [
          {
            value: 1,
            name: 'invisible',
            itemStyle: placeHolderStyle,
          }
        ],
        zlevel: 0,
        silent: true,
      },
      // 进度的数据
      {
        name: 'Pie2',
        type: 'pie',
        clockWise: false,
        radius: [baseVal - prowWidth + '%', baseVal + 6 + '%'],
        startAngle: 90,
        itemStyle: dataStyle,
        hoverAnimation: false,
        animation: false,
        animationType: 'scale',
        center: center,
        data: [
          {
            value: total - value,
            name: 'invisible',
            itemStyle: placeHolderStyleBig,
          },
          {
            name: unit || '',
            value: value,
            label: {
              normal: {
                formatter: function (p) {
                  return p.value + '\n{white|' + p.name + '}'
                },
                position: 'center',
                show: true,
                textStyle: {
                  fontSize: 20,
                  fontWeight: 'normal',
                  color: '#0C83CF',
                  lineHeight: 40,
                },
                rich: {
                  white: {
                    color: '#b6b6b6',
                    align: 'center',
                    lineHeight: 10,
                  }
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
          }
        ],
        zlevel: 2,
      },
      // 起始点的数据
      {
        name: 'Pie3',
        type: 'pie',
        clockWise: false,
        radius: [baseVal - 7 + '%', baseVal + 7 + '%'],
        itemStyle: dataStyle,
        hoverAnimation: false,
        animation: false,
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
                color: '#3dd4de',
                shadowColor: '#5fb682',
                shadowBlur: 10
              }
            }
          }
        ],
        zlevel: 3,
        silent: true,
      }
    ]
    if (value * 1.0 / total > 0.02) { // 只要大于2%的都才显示弧度的信息
      results.push(// 进度结尾的圆角的弧度
        {
          name: 'Pie4',
          // color: [tmColor],
          type: 'pie',
          clockWise: false,
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

  option.series = [].concat(getSeriesData('#5fb682', obj1.total, obj1.value, ['17%', '50%'], obj1.unit))
    .concat(getSeriesData('#efc54d', obj2.total, obj2.value, ['50%', '50%'], obj2.unit))
    .concat(getSeriesData('#FE3230', obj3.total, obj3.value, ['83%', '50%'], obj3.unit))

  return option
}

/**
 * 发电趋势
 * @param xData
 * @param yData
 * @param y2Data
 * @returns {{color: [string,string], tooltip: {trigger: string, axisPointer: {type: string, lineStyle: {color: string}}}, legend: {itemWidth: number, itemHeight: number, right: number, data: [null,null], textStyle: {fontSize: number, color: string, fontWeight: number}, selectedMode: boolean}, grid: {left: number, right: number, top: number, bottom: number, containLabel: boolean, borderColor: string}, dataZoom: [null,null], xAxis: [null], yAxis: [null,null], series: [null,null]}}
 */
const powerTrend = (xData = [], yData = {name: '', unit: '', data: []}, y2Data = {name: '', unit: '', data: []}) => {
  let option = {
    color: ['#1EB05B', '#0F95EF'],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        lineStyle: {
          color: '#0D96F0'
        }
      }
    },
    legend: {
      itemWidth: 14,
      itemHeight: 11,
      right: 100,
      data: [yData.name, y2Data.name],
      textStyle: {
        fontSize: 12,
        color: '#ddd',
        fontWeight: 500
      },
      selectedMode: false
    },
    grid: {
      left: 20,
      right: 20,
      top: 30,
      bottom: 30,
      containLabel: true,
      borderColor: '#666'
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
    xAxis: [
      {
        type: 'category',
        data: xData,
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          lineStyle: {
            color: '#666666'
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
        name: yData.unit,
        type: 'value',
        nameTextStyle: {
          color: '#666666'
        },
        axisLine: {
          lineStyle: {
            color: '#666666'
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
        name: y2Data.unit,
        type: 'value',
        nameTextStyle: {
          color: '#666666'
        },
        axisLine: {
          lineStyle: {
            color: '#666666'
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
        name: yData.name,
        type: 'bar',
        barCategoryGap: '50%',
        barWidth: '78%',
        data: yData.data,
        yAxisIndex: 0,
        itemStyle: {
          normal: {
            barBorderRadius: 5,
            z: 0
          }
        }
      },
      {
        name: y2Data.name,
        type: 'line',
        barWidth: '13',
        data: y2Data.data,
        symbol: 'circle',
        symbolSize: 8,
        yAxisIndex: 1
      }
    ]
  }
  return option
}

/**
 * 发电规模统计
 * @param name
 * @param data
 * @param total
 * @returns {{color: [string,string,string,string,string], tooltip: {show: boolean, formatter: tooltip.formatter}, legend: {show: boolean}, toolbox: {show: boolean}, series: [null]}}
 */
const powerScale = (name = '', data = [], total = 0, vm) => {
  let scaleData = []

  const placeHolderStyle = {
    normal: {
      label: {
        show: false
      },
      labelLine: {
        show: false
      },
      color: 'rgba(0, 0, 0, 0)',
      borderColor: 'rgba(0, 0, 0, 0)',
      borderWidth: 0
    }
  }
  let Rata = vm.$t('exhibition.ratio')
  for (let i = 0; i < data.length; i++) {
    scaleData.push({
      value: +data[i].value,
      name: data[i].name,
      labelLine: {
        normal: {
          lineStyle: {
            color: 'rgba(255, 255, 255, 0.3)'
          },
          smooth: 0.4,
          length: 10,
          length2: 20
        }
      },
    })
    if (total !== 0) {
      scaleData.push({
        value: data.map(t => +t.value).max() / 100,
        name: '',
        itemStyle: placeHolderStyle
      })
    }
  }
  return {
    color: ['rgba(30, 176, 91, 1)', 'rgba(134, 222, 252, 1)', 'rgba(10, 159, 254, 1)', 'rgba(232, 155, 45, 1)', 'rgba(167, 28, 32, 1)'],
    tooltip: {
      show: true,
      formatter: function (params) {
        let percent = 0
        if (total !== 0) {
          percent = ((params.value / total) * 100).toFixed(0)
        }
        if (params.name !== '') {
          return params.seriesName + '<br>' + params.name + '(' + Rata + ' ' + percent + '%)'
        } else {
          return ''
        }
      }
    },
    legend: {
      show: false
    },
    toolbox: {
      show: false
    },
    series: [
      {
        name: name,
        type: 'pie',
        center: [225, 125],
        radius: [50, 86],
        itemStyle: {
          normal: {
            shadowBlur: 5,
            shadowColor: 'rgba(30, 176, 91, 0.6)',
            label: {
              show: true,
              position: 'outside',
              color: '#ddd',
              formatter: function (params) {
                let percent = 0
                if (total !== 0) {
                  percent = ((params.value / total) * 100).toFixed(0)
                }
                if (params.name !== '') {
                  return params.name + '\n' + Rata + ': ' + percent + '%'
                } else {
                  return ''
                }
              },
              textStyle: {
                fontSize: 14
              }
            }
          }
        },
        data: scaleData,
        clockwise: false,
        zlevel: 2
      }
    ]
  }
}

/**
 * 任务工单统计
 * @param names
 * @param data
 * @param total
 */
const taskStatistics = (names = [], data = [], total = 0) => {
  // 路径的svg坐标
  const M_PATH = 'path://M 0 5 C 0 2.5 2.5 0 5 0 L 40 0 C 42.5 0 45 2.5 45 5 L 45 10 C 45 12.5 42.5 15 40 15 L 5 15 C 2.5 15 0 12.5 0 10 ZM 0 35 C 0 32.5 2.5 30 5 30 L 40 30 C 42.5 30 45 32.5 45 35 L 45 40 C 45 42.5 42.5 45 40 45 L 5 45 C 2.5 45 0 42.5 0 40 ZM 0 65 C 0 62.5 2.5 60 5 60 L 40 60 C 42.5 60 45 62.5 45 65 L 45 70 C 45 72.5 42.5 75 40 75 L 5 75 C 2.5 75 0 72.5 0 70 ZM 0 95 C 0 92.5 2.5 90 5 90 L 40 90 C 42.5 90 45 92.5 45 95 L 45 100 C 45 102.5 42.5 105 40 105 L 5 105 C 2.5 105 0 102.5 0 100 ZM 0 125 C 0 122.5 2.5 120 5 120 L 40 120 C 42.5 120 45 122.5 45 125 L 45 130 C 45 132.5 42.5 135 40 135 L 5 135 C 2.5 135 0 132.5 0 130 ZM 0 155 C 0 152.5 2.5 150 5 150 L 40 150 C 42.5 150 45 152.5 45 155 L 45 160 C 45 162.5 42.5 165 40 165 L 5 165 C 2.5 165 0 162.5 0 160 ZM 0 185 C 0 182.5 2.5 180 5 180 L 40 180 C 42.5 180 45 182.5 45 185 L 45 190 C 45 192.5 42.5 195 40 195 L 5 195 C 2.5 195 0 192.5 0 190 ZM 0 215 C 0 212.5 2.5 210 5 210 L 40 210 C 42.5 210 45 212.5 45 215 L 45 220 C 45 222.5 42.5 225 40 225 L 5 225 C 2.5 225 0 222.5 0 220 ZM 0 245 C 0 242.5 2.5 240 5 240 L 40 240 C 42.5 240 45 242.5 45 245 L 45 250 C 45 252.5 42.5 255 40 255 L 5 255 C 2.5 255 0 252.5 0 250 ZM 0 275 C 0 272.5 2.5 270 5 270 L 40 270 C 42.5 270 45 272.5 45 275 L 45 280 C 45 282.5 42.5 285 40 285 L 5 285 C 2.5 285 0 282.5 0 280 Z'
  const symbols = [M_PATH, M_PATH, M_PATH]
  const pathSize = ['60%', '100%']
  const bodyMax = total

  const labelSetting = (name, color) => {
    return {
      normal: {
        show: true,
        position: 'bottom',
        offset: [0, 0],
        formatter: name,
        textStyle: {
          fontSize: 16,
          fontFamily: 'Arial',
          color: color
        }
      }
    }
  }
  const colors = ['#ff3333', '#ff9933', '#09A8D9']

  return {
    tooltip: {
      show: false
    },
    grid: {
      top: '5%',
      left: '5%',
      right: '35%',
      bottom: '20%'
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
    series: [
      {
        name: 'typeA',
        type: 'pictorialBar',
        symbolClip: true,
        symbolBoundingData: bodyMax,
        data: [
          {
            value: data[0],
            symbolSize: pathSize,
            label: labelSetting(names[0], colors[0]),
            symbol: symbols[0],
            itemStyle: {
              normal: {
                color: colors[0]
              }
            }
          },
          {
            value: data[1],
            symbolSize: pathSize,
            label: labelSetting(names[1], colors[1]),
            symbol: symbols[1],
            itemStyle: {
              normal: {
                color: colors[1]
              }
            }
          },
          {
            value: data[2],
            symbolSize: pathSize,
            label: labelSetting(names[2], colors[2]),
            symbol: symbols[2],
            itemStyle: {
              normal: {
                color: colors[2]
              }
            }
          }
        ],
        z: 10
      },
      {
        name: 'full',
        type: 'pictorialBar',
        symbolBoundingData: bodyMax,
        animationDuration: 0,
        silent: true,
        itemStyle: {
          normal: {
            color: 'rgba(55, 67, 86, 0.5)'
          }
        },
        data: [
          {
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
          }
        ]
      }
    ]
  }
}

/**
 * 设备分布统计
 * @param names
 * @param values
 * @returns {{legend: [null], series: Array}}
 */
const equipmentDistribution = (names = [], values = []) => {
  const colors = ['rgba(134, 222, 252, 1)', 'rgba(10, 159, 254, 1)', 'rgba(232, 155, 45, 1)', 'rgba(30, 176, 91, 1)']

  const placeHolderStyle = {
    normal: {
      label: {
        show: false,
        position: 'center'
      },
      labelLine: {
        show: false
      },
      color: 'rgb(17, 60, 88)',
    },
  }
  const placeHolderStyle2 = {
    normal: {
      label: {
        show: false,
        position: 'center'
      },
      labelLine: {
        show: false
      },
      color: 'transparent',
    },
    emphasis: {
      color: 'transparent',
    }
  }

  let total = 0
  let maxV = Math.max(1, values.max())
  let data = []
  let legendData = []
  let seriesData = []

  for (let i = 0; i < names.length; i++) {
    total += +values[i]
  }
  for (let i = 0; i < names.length; i++) {
    let name = ' ' + names[i] + '   ' + (+values[i] * 100.0 / total).fixed(2) + '%'
    data.push({value: '', name: name})
    legendData.push(name)
    seriesData.push({
      name: '',
      type: 'pie',
      clockWise: false, // 顺时加载
      hoverAnimation: false, // 鼠标移入变大
      center: [110, 128],
      radius: [21 + i * 22, 26 + i * 22],
      itemStyle: {
        normal: {
          color: colors[i],
          label: {
            show: false,
          },
          labelLine: {
            show: false,
          }
        }
      },
      data: [
        {
          value: +values[i],
          name: names[i],
        },
        {
          value: maxV - +values[i],
          name: names[i],
          itemStyle: placeHolderStyle,
        },
        {value: maxV / 3, name: names[i], itemStyle: placeHolderStyle2, tooltip: {show: false}},
      ]
    })
  }

  seriesData.push({
    type: 'pie',
    data: data
  })

  return {
    tooltip: {
      show: true,
      trigger: 'item',
      formatter: function (params) {
        return params.name + '<br/> ' + +values[params.seriesIndex] + ' (' + (+values[params.seriesIndex] * 100.0 / total).fixed(2) + '%)'
      }
    },
    legend: [
      {
        orient: 'vertical',
        top: 28,
        left: 110,
        itemWidth: 7,
        itemHeight: 7,
        itemGap: 10,
        icon: 'circle',
        textStyle: {
          fontSize: 12,
          color: '#A8A7A9'
        },
        selectedMode: false,
        data: legendData.reverse()
      }
    ],
    series: seriesData
  }
}

/**
 * 供电负荷统计
 * @param xData
 * @param yData
 * @returns {{color: [string], tooltip: {trigger: string, axisPointer: {type: string, lineStyle: {color: string}}}, legend: {show: boolean}, grid: {left: number, right: number, top: string, bottom: number, containLabel: boolean, borderColor: string}, dataZoom: [null,null], xAxis: [null], yAxis: [null], series: [null]}}
 */
const powerSupply = (xData = [], yData = {name: '', unit: '', data: []}) => {
  return {
    color: ['#0F95EF'],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        lineStyle: {
          color: '#0D96F0'
        }
      }
    },
    legend: {show: false},
    grid: {
      left: 20,
      right: 40,
      top: '15%',
      bottom: 30,
      containLabel: true,
      borderColor: '#EEEEEE'
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
        borderRadius: 5,
        handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
        handleSize: 20,
        handleStyle: {
          color: '#0a9ffe'
        },
        shadowBlur: 3,
        shadowColor: 'rgba(0, 0, 0, 0.6)',
        shadowOffsetX: 2,
        shadowOffsetY: 2,
        textStyle: {
          color: '#EEEEEE'
        },
        left: 58,
        right: 50,
        bottom: 10,
        xAxisIndex: [0],
        start: 20,
        end: 80
      },
      {
        type: 'inside'
      }
    ],
    xAxis: [
      {
        type: 'category',
        data: xData,
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          lineStyle: {
            color: '#666666'
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
        name: yData.unit,
        type: 'value',
        nameTextStyle: {
          color: '#666666'
        },
        axisLine: {
          lineStyle: {
            color: '#666666'
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
        name: yData.name,
        type: 'line',
        smooth: true,
        smoothMonotone: 'x',
        symbol: 'circle',
        symbolSize: 4,
        data: yData.data,
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: '#0a9ffe'
            }, {
              offset: 1,
              color: '#3BABC4'
            }])
          }
        }
      }
    ]
  }
}

export {
  powerGeneration,
  powerTrend,
  powerScale,
  taskStatistics,
  equipmentDistribution,
  powerSupply,
}
