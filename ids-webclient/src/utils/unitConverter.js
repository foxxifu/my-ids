require('./prototype')

const measures = {
  // 功率
  power: {
    zh_CN: {
      'kW': {
        name: 'kW',
        to_anchor: 1
      },
      'MW': {
        name: 'MW',
        to_anchor: 1000
      },
      'GW': {
        name: 'GW',
        to_anchor: 1000 * 1000
      }
    },
    en_US: {
      'kW': {
        name: 'kW',
        to_anchor: 1
      },
      'MW': {
        name: 'MW',
        to_anchor: 1000
      },
      'GW': {
        name: 'GW',
        to_anchor: 1000 * 1000
      }
    },
    _anchors: {
      zh_CN: {
        unit: 'kW',
        ratio: 1
      },
      en_US: {
        unit: 'kW',
        ratio: 1
      },
    }
  },
  // 电量
  energy: {
    zh_CN: {
      'kWh': {
        name: 'kWh',
        to_anchor: 1
      },
      'WkWh': {
        name: '万kWh',
        to_anchor: 10 * 1000
      },
    },
    en_US: {
      'kWh': {
        name: 'kWh',
        to_anchor: 1
      },
      'MWh': {
        name: 'MWh',
        to_anchor: 1000
      },
      'GWh': {
        name: 'GWh',
        to_anchor: 1000 * 1000
      }
    },
    _anchors: {
      zh_CN: {
        unit: 'kWh',
        ratio: 1
      },
      en_US: {
        unit: 'kWh',
        ratio: 1
      },
    }
  },
  // 货币
  currency: {
    zh_CN: {
      'Y': {
        name: '元',
        to_anchor: 1
      },
      'WY': {
        name: '万元',
        to_anchor: 10 * 1000
      },
    },
    en_US: {
      'Y': {
        name: 'dollars',
        to_anchor: 1
      },
      'KY': {
        name: 'k dollars',
        to_anchor: 1000
      },
    },
    _anchors: {
      zh_CN: {
        unit: 'Y',
        ratio: 1
      },
      en_US: {
        unit: 'Y',
        ratio: 1
      },
    }
  },
}

/**
 * 单位转换
 * @param value
 * @param unit
 * @param lang
 * @returns {{value: number, unit: string}}
 */
const converter = function (value = 0.0, unit = '', lang = 'zh_CN') {
  let units
  let _anchor
  try {
    Object.keys(measures).forEach(item => {
      let measure = measures[item]
      Object.keys(measure).forEach(system => {
        if (system !== lang || system === '_anchors') {
          return true
        }
        units = measure[system]
        _anchor = measure['_anchors'][lang]
      })
      if (Object.keys(units).contains(unit)) {
        throw new Error('StopIteration')
      }
    })
  } catch (e) {}

  let best = {
    value: value,
    anchor: 1,
    unit: unit
  }
  if (units && !Object.isEmptyObject(units) && !Object.isEmptyObject(_anchor)) {
    let anchor = units[unit]
    if (anchor) {
      value *= anchor.to_anchor
      best = {
        value: value,
        anchor: _anchor.ratio,
        unit: units[_anchor.unit].name
      }
      Object.keys(units).forEach(u => {
        const r = units[u].to_anchor
        const result = value / r
        if (result >= 1 && result < best.value) {
          best = {
            value: result,
            anchor: r,
            unit: units[u].name
          }
        }
      })
    }
  }
  return best
}

export default converter
