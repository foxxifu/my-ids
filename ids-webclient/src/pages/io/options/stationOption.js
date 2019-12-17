// 获取电站状态初始选项
const getOption = (vm) => {
  let radius = ['50%', '70%']
  let stationStatusArr = vm.$t('io.stationStatusArr')
  let option = {
    title: {
      text: '0' + vm.$t('io.plantUnit'),
      x: 'center',
      y: '35%',
      itemGap: 20,
      textStyle: {
        color: '#000',
        fontFamily: '微软雅黑',
        fontSize: 16,
        fontWeight: 'bolder'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}'
    },
    legend: {
      x: 'center',
      y: '85%',
      data: stationStatusArr
    },
    series: [
      {
        name: vm.$t('io.plantform.plantStatus'),
        type: 'pie',
        radius: radius,
        center: ['50%', '40%'],
        avoidLabelOverlap: false,
        label: {
          normal: {
            show: false,
            position: 'center'
          },
          emphasis: {
            show: false,
            textStyle: {
              fontSize: '30',
              fontWeight: 'bold'
            }
          }
        },
        labelLine: {
          normal: {
            show: false
          }
        },
        data: [
          {value: 0, name: stationStatusArr[0], itemStyle: { color: '#F00' }},
          {value: 0, name: stationStatusArr[1], itemStyle: { color: '#AAA' }},
          {value: 0, name: stationStatusArr[2], itemStyle: { color: '#1DC556' }}
        ],
        zlevel: 1,
      }
    ],
    animation: false // 无动画效果
  }
  return option;
}

const setOption = (option, data, vm) => {
  if (option) {
    let total = +data[0] + (+data[1]) + (+data[2]);
    let stationStatusArr = vm.$t('io.stationStatusArr')
    option.title.text = total + vm.$t('io.plantUnit');
    let serData = option.series[0].data;
    let names = [stationStatusArr[0] + ' ' + data[0], stationStatusArr[1] + ' ' + data[1], stationStatusArr[2] + ' ' + data[2]]
    serData[0].value = +data[0];
    serData[0].name = names[0];
    serData[1].value = +data[1];
    serData[1].name = names[1];
    serData[2].value = +data[2];
    serData[2].name = names[2];
    option.legend.data = names; // 设置显示的名称
  }
}

export default {
  getOption,
  setOption
}
