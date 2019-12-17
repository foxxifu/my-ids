import UnitConverter from '@/utils/unitConverter'

import * as ExhibitionService from '@/service/largeScreen'
import SingleStationService from '@/service/singleStation'
import WeatherService from '@/service/weather'

import Cesium from '@/components/Cesium/index.vue'
import Map from '@/components/Map/index.vue'

import * as options from './options'

const kpiModules = ['powerGeneration', 'powerTrend', 'powerScale', 'equipmentDistribution', 'powerSupply', 'socialContribution']

export default {
  name: 'exhibition',
  components: {
    IDS_Cesium: Cesium,
    IDS_Map: Map,
  },
  data () {
    return {
      totalPower: 0.0,
      safeRunningDays: 0,
      currentTime: new Date(),

      kpi_powerGeneration_chart: {},
      kpi_powerTrend_chart: {},
      kpi_powerScale_chart: {},
      kpi_taskStatistics_chart: {},
      kpi_equipmentDistribution_chart: {},
      kpi_powerSupply_chart: {},

      kpi_powerScale_total: 0,
      kpi_powerSupply_current: 0,
      kpi_equipmentDistribution: {},
      kpi_taskStatistics: {},
      kpi_taskStatistics_total: 0,
      contribution: {},

      totalYearPower: {
        value: 0.0,
        unit: this.$t('unit.KWh')
      },
      totalInstalledCapacity: {
        value: 0.0,
        unit: this.$t('unit.KW')
      },
      totalIncome: {
        value: 0.0,
        unit: this.$t('unit.RMBUnit')
      },
      equipmentQuantity: {
        value: 0,
        unit: '',
      },
      totalTask: {
        value: 0,
        unit: '',
      },
      untreatedTask: {
        value: 0,
        unit: '',
      },

      showEarth: true,
      earthPoints: [],

      mapPathArea: [],
      mapCenter: [30.89, 104.7878],
      mapZoom: 3,
      mapClusterMarkers: [],
      mapAreaBounds: null,

      isStationView: false, // 是否为电站视图
    }
  },
  computed: {
    imgPath() { // 图片
      return this.$store.state.logoImgPath
    }
  },
  methods: {
    /**
     * 返回普屏【总览】
     */
    goToHome: function () {
      this.$router.push({path: '/home'})
    },

    goEarth: function () {
      this.showEarth = true
      this.mapPathReview()
    },

    goMap: function (target, latlng) {
      this.showEarth = false
      this.mapCenter = latlng || [30.78545, 104.89786]
      this.mapZoom = 4
      this.mapPathReview(target.id, '1')
    },

    renderCenter (params) {
      const _this = this
      const id = params && params.id
      const type = params && params.type
      const render = () => {
        if (_this.mapTimer) {
          clearTimeout(_this.mapTimer)
        }

        let params = {id: id, type: type}
        ExhibitionService.regionPowerStation(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results || {}
            const currentShowData = data.currentShowData || []

            if (_this.showEarth) {
              let earthPoints = []

              currentShowData.forEach(item => {
                earthPoints.push({
                  id: item.id,
                  avatar: '',
                  name: item.name,
                  position: [item.latitude, item.longitude],
                  radius: item.radius,
                  icon: 'enterprise'
                })
              })
              _this.earthPoints = earthPoints
            } else {
              _this.mapAreaBounds = {
                latitude: data.latitude,
                longitude: data.longitude,
                radius: data.radius,
              }

              _this.mapPathArea = data.domainTree || []

              const iconStyles = [
                {
                  iconSize: [72, 98],
                  iconAnchor: [36, 96],
                  tooltipAnchor: [36, -65],
                  popupAnchor: [-1, -65],
                  labelAnchor: [-1, 3]
                },
                {
                  iconSize: [66, 98],
                  iconAnchor: [33, 96],
                  tooltipAnchor: [33, -65],
                  popupAnchor: [-1, -65],
                  labelAnchor: [-1, 3]
                },
                {
                  iconSize: [56, 70],
                  iconAnchor: [28, 68],
                  tooltipAnchor: [28, -40],
                  popupAnchor: [-1, -40],
                  labelAnchor: [-1, 3]
                }
              ]

              let regionPoints = []
              let stationPoints = []
              currentShowData.forEach(item => {
                if (+item.nodeType === 2) {
                  let iconSettings = {
                    iconUrl: '/assets/images/exhibition/mapPoints/domain-icon.gif',
                  }
                  Object.assign(iconSettings, {
                    iconSize: [100, 100],
                    iconAnchor: [50, 50],
                    tooltipAnchor: [45, 0],
                    labelAnchor: [-1, 50]
                  })
                  regionPoints.push({
                    id: item.id + '',
                    icon: iconSettings,
                    position: [item.latitude, item.longitude],
                    tooltip: item.name,
                    label: item.name,
                    events: {
                      click: function (e) {
                        _this.regionParams = {id: item.id, type: item.nodeType}
                        _this.mapPathReview(item.id, item.nodeType)
                      }
                    }
                  })
                } else if (+item.nodeType === 3) {
                  let state = item.combinedType + '_' + item.stationType
                  let iconSettings = {
                    iconUrl: '/assets/images/exhibition/mapPoints/' + state + '.png',
                  }
                  Object.assign(iconSettings, iconStyles[+item.combinedType - 1])
                  stationPoints.push({
                    id: item.id + '',
                    icon: iconSettings,
                    position: [item.latitude, item.longitude],
                    tooltip: item.name,
                    label: item.name,
                    popup: {
                      content: {
                        type: 'exhibition',
                        name: item.name,
                        installedCapacity: item.capacity,
                        weathers: []
                      },
                      options: {
                        className: 'ids-popupInfoWindow'
                      }
                    },
                    events: {
                      popupopen: function (e) {
                        _this.isStationView = true
                        _this.mapClusterMarkers[0].data.forEach(m => {
                          if (m.id === item.id) {
                            // TODO 获取电站信息
                            SingleStationService.getSingleStationInfo({
                              stationCode: item.id
                            }).then(resp => {
                              if (resp.code === 1) {
                                const data = resp.results || {}
                                m.popup.content.pic = data.stationFileId || ''
                                m.popup.content.name = data.stationName || ''
                                m.popup.content.address = data.stationAddr || ''
                                m.popup.content.installedCapacity = data.installed_capacity || 0
                                m.popup.content.runDate = data.onlineTime
                                m.popup.content.safeRunningDays = data.runDays || 0
                                m.popup.content.description = data.stationDesc || ''
                                m.popup.content.gridPowerDay = data.dayCapacity || 0
                                m.popup.content.gridPowerMonth = data.monthCap || 0
                                m.popup.content.gridPowerYear = data.yearCap || 0
                              }
                            })

                            // 获取电站未来3天天气信息
                            let longitudeLatitude = item.latitude + ':' + item.longitude
                            WeatherService.getWeatherNow({location: longitudeLatitude}).then(resp => {
                              if (resp.code === 1 && !!!resp.results['status_code']) {
                                m.popup.content.weather = resp.results.results[0].now || {}
                              }
                            })
                            WeatherService.getWeatherDaily({location: longitudeLatitude}).then(resp => {
                              if (resp.code === 1 && !!!resp.results['status_code']) {
                                m.popup.content.weathers = resp.results.results[0].daily || []
                              }
                            })
                          }
                        })
                        setTimeout(function () {
                          _this.renderLeftAndRight({id: item.id, type: item.nodeType})
                          _this.renderCommonData({id: item.id, type: item.nodeType})
                        }, 100)
                      },
                      popupclose: function (e) {
                        _this.isStationView = false
                        setTimeout(function () {
                          _this.renderLeftAndRight(_this.regionParams)
                          _this.renderCommonData(_this.regionParams)
                        }, 100)
                      }
                    }
                  })
                }
              })
              _this.mapClusterMarkers = [
                {
                  type: 'stations',
                  data: stationPoints
                },
                {
                  type: 'regions',
                  data: regionPoints
                },
              ]
            }
          }
        })

        _this.mapTimer = setTimeout(render, 5 * 60 * 1000)
      }
      render()
    },
    /**
     * 地图路径点击事件处理
     */
    mapPathReview (id, type) {
      let params = {id: id, type: type}
      this.renderCenter(params)
      this.renderLeftAndRight(params)
      this.renderCommonData(params)
    },

    renderLeftAndRight (params) {
      let _this = this
      for (let i in kpiModules) {
        if (kpiModules.hasOwnProperty(i)) {
          if (i === '2' && _this.isStationView) {
            if (_this.powerScaleTimer) {
              clearTimeout(_this.powerScaleTimer)
            }
            _this.taskStatistics(params)
          } else {
            if (_this.taskStatisticsTimer) {
              clearTimeout(_this.taskStatisticsTimer)
            }
            _this[kpiModules[i]](params)
          }
        }
      }
    },

    /**
     * 发电状态（实时）
     */
    powerGeneration (params) {
      const _this = this

      const render = () => {
        if (_this.powerGenerationTimer) {
          clearTimeout(_this.powerGenerationTimer)
        }

        ExhibitionService.powerGeneration(Object.assign({}, params)).then(resp => {
          if (resp.code === 1) {
            const data = resp.results || {}
            let totalPower = data.activePower || {}
            let dailyCharge = data.dayCap || {}
            let todayIncome = data.dayIncome || {}
            let totalYearPower = data.ongridPower || {}
            let totalIncome = data.totalIncome || {}

            _this.totalPower = data.totalPower ? data.totalPower.value : 0

            let TYP = totalYearPower ? UnitConverter(totalYearPower.value, _this.$t('unit.KWh')) : {
              value: 0.0,
              unit: _this.$t('unit.KWh')
            }
            let lang = (localStorage.getItem('lang') || 'zh') === 'en' ? 'en_US' : 'zh_CN'
            let TOI = totalIncome ? UnitConverter(totalIncome.value, 'Y', lang) : {
              value: 0.0,
              unit: _this.$t('unit.RMBUnit')
            }

            _this.totalYearPower = TYP
            _this.totalIncome = TOI
            let TP = totalPower ? UnitConverter(totalPower.value, _this.$t('unit.KW'), lang) : {
              value: 0.0,
              unit: _this.$t('unit.KW')
            }
            let DC = dailyCharge ? UnitConverter(dailyCharge.value, _this.$t('unit.KWh'), lang) : {
              value: 0.0,
              unit: _this.$t('unit.KWh')
            }
            let TI = todayIncome ? UnitConverter(todayIncome.value, 'Y', lang) : {
              value: 0.0,
              unit: _this.$t('unit.RMBUnit')
            }

            _this.kpi_powerGeneration_chart = options.powerGeneration(
              {
                total: Math.max(totalPower.max / TP.anchor, TP.value || 0),
                value: (TP.value || 0).fixed(3),
                unit: TP.unit
              },
              {
                total: Math.max(dailyCharge.max / DC.anchor, DC.value || 0),
                value: (DC.value || 0).fixed(3),
                unit: DC.unit
              },
              {
                total: Math.max(todayIncome.max / TI.anchor, TI.value || 0),
                value: (TI.value || 0).fixed(3),
                unit: TI.unit
              },
            )
          }
        })

        _this.powerGenerationTimer = setTimeout(render, 5 * 1000)
      }
      render()
    },
    /**
     * 发电趋势（本月）
     */
    powerTrend (params) {
      const _this = this

      const render = () => {
        if (_this.powerTrendTimer) {
          clearTimeout(_this.powerTrendTimer)
        }

        ExhibitionService.powerTrend(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results
            const powerTrends = data.powerTrends

            let yUnit = this.$t('unit.KWh')
            let y2Unit = this.$t('unit.RMBUnit')

            let xData = []
            let yData = []
            let y2Data = []

            for (let i = 0; i < Date.parse().getDaysInMonth(); i++) {
              let isFoundData = false
              let xv = i + 1 < 10 ? '0' + (i + 1) : '' + (i + 1)
              xData.push(xv)
              for (let j = 0; j < powerTrends.length; j++) {
                if (Date.parse(powerTrends[j].collectTime).format('dd') === xv) {
                  isFoundData = true
                  yData.push((powerTrends && powerTrends[j].productPower) || '-')
                  y2Data.push((powerTrends && powerTrends[j].powerProfit) || '-')
                  break
                }
              }
              if (!isFoundData) {
                yData.push('-')
                y2Data.push('-')
              }
            }
            _this.kpi_powerTrend_chart = options.powerTrend(xData, {
              name: this.$t('exhibition.kpiModules.powerTrend.generatingCapacity'), unit: yUnit, data: yData
            }, {
              name: this.$t('exhibition.kpiModules.powerTrend.income'), unit: y2Unit, data: y2Data
            })
          }
        })

        _this.powerTrendTimer = setTimeout(render, 60 * 60 * 1000)
      }
      render()
    },
    /**
     * 发电规模统计
     */
    powerScale (params) {
      const _this = this

      const render = () => {
        if (_this.powerScaleTimer) {
          clearTimeout(_this.powerScaleTimer)
        }

        ExhibitionService.powerScale(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results.stationInfoList

            let _0to5, _5to10, _10to100, _100to500, _500up, _totalStation
            if (data) {
              _0to5 = data.s0to5 ? data.s0to5 : 0
              _5to10 = data.s5to10 ? data.s5to10 : 0
              _10to100 = data.s10to100 ? data.s10to100 : 0
              _100to500 = data.s100to500 ? data.s100to500 : 0
              _500up = data.s500up ? data.s500up : 0
            } else {
              _0to5 = 0
              _5to10 = 0
              _100to500 = 0
              _10to100 = 0
              _500up = 0
            }
            _totalStation = parseInt(_0to5) + parseInt(_5to10) + parseInt(_100to500) + parseInt(_10to100) + parseInt(_500up)
            _this.kpi_powerScale_total = _totalStation

            _this.kpi_powerScale_chart = options.powerScale(this.$t('exhibition.kpiModules.powerScale.installedCapacity'), [
              {value: _0to5, name: this.$t('exhibition.kpiModules.powerScale.scale[0]')},
              {value: _5to10, name: this.$t('exhibition.kpiModules.powerScale.scale[1]')},
              {value: _100to500, name: this.$t('exhibition.kpiModules.powerScale.scale[2]')},
              {value: _10to100, name: this.$t('exhibition.kpiModules.powerScale.scale[3]')},
              {value: _500up, name: this.$t('exhibition.kpiModules.powerScale.scale[4]')}
            ], _totalStation, this)
          }
        })

        _this.powerScaleTimer = setTimeout(render, 60 * 60 * 1000)
      }
      render()
    },
    /**
     * 任务工单统计
     */
    taskStatistics (params) {
      const _this = this

      const render = () => {
        if (_this.taskStatisticsTimer) {
          clearTimeout(_this.taskStatisticsTimer)
        }

        ExhibitionService.taskStatistics(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results

            let done, doing, todo, total
            if (data) {
              done = data.done ? data.done : 0
              doing = data.doing ? data.doing : 0
              todo = data.todo ? data.todo : 0
            } else {
              done = 0
              doing = 0
              todo = 0
            }
            total = parseInt(done) + parseInt(doing) + parseInt(todo)

            _this.kpi_taskStatistics = Object.entries(data).sort((i1, i2) => {
              const sort = ['todo', 'doing', 'done']
              const index1 = sort.indexOf(i1[0])
              const index2 = sort.indexOf(i2[0])
              return index1 > index2
            })
            _this.kpi_taskStatistics_total = total

            _this.kpi_taskStatistics_chart = options.taskStatistics(
              this.$t('exhibition.kpiModules.taskStatistics.status'),
              [parseInt(todo), parseInt(doing), parseInt(done)],
              total
            )
          }
        })

        _this.taskStatisticsTimer = setTimeout(render, 5 * 1000)
      }
      render()
    },
    /**
     * 设备分布统计
     */
    equipmentDistribution (params) {
      const _this = this

      const render = () => {
        if (_this.equipmentDistributionTimer) {
          clearTimeout(_this.equipmentDistributionTimer)
        }

        ExhibitionService.equipmentDistribution(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results

            let arrName = []
            let arrValue = []

            arrName.push(_this.$t('exhibition.kpiModules.equipmentDistribution["combinerBoxCount"]'))
            arrValue.push(Number(data['combinerBoxCount'] || 0))
            arrName.push(_this.$t('exhibition.kpiModules.equipmentDistribution["inverterConcCount"]'))
            arrValue.push(Number(data['inverterConcCount'] || 0))
            arrName.push(_this.$t('exhibition.kpiModules.equipmentDistribution["inverterStringCount"]'))
            arrValue.push(Number(data['inverterStringCount'] || 0))
            arrName.push(_this.$t('exhibition.kpiModules.equipmentDistribution["pvCount"]'))
            arrValue.push(Number(data['pvCount'] || 0))

            _this.kpi_equipmentDistribution = data
            _this.equipmentQuantity.value = +data.deviceTotalCount || 0

            _this.kpi_equipmentDistribution_chart = options.equipmentDistribution(arrName, arrValue)
          }
        })

        _this.equipmentDistributionTimer = setTimeout(render, 60 * 60 * 1000)
      }
      render()
    },
    /**
     * 供电负荷统计
     */
    powerSupply (params) {
      const _this = this

      const render = () => {
        if (_this.powerSupplyTimer) {
          clearTimeout(_this.powerSupplyTimer)
        }

        ExhibitionService.powerSupply(params).then(resp => {
          if (resp.code === 1) {
            const data = resp.results
            _this.kpi_powerSupply_current = data && Number(data.currentPower || 0)

            const yUnit = _this.$t('unit.KW')

            let xData = []
            let yData = []

            for (let i = 0; i < 24 * 60 / 5; i++) {
              let t = 5 * i
              let h = parseInt(t / 60)
              let m = t % 60
              let hour = h < 10 ? '0' + h : '' + h
              let min = m < 10 ? '0' + m : '' + m
              xData.push(hour + ':' + min)
              yData.push('-')
            }

            const dayActivePowerList = data.dayActivePowerList

            for (let key in dayActivePowerList) {
              if (dayActivePowerList.hasOwnProperty(key)) {
                const value = dayActivePowerList[key]
                const date = Date.parse(key).format('HH:mm')
                const index = xData.indexOf(date)
                if (index !== -1) {
                  yData[index] = value
                }
              }
            }
            _this.kpi_powerSupply_chart = options.powerSupply(xData, {
              name: _this.$t('exhibition.kpiModules.powerSupply.supplyKey'),
              unit: yUnit,
              data: yData
            })
          }
        })

        _this.powerSupplyTimer = setTimeout(render, 5 * 60 * 1000)
      }
      render()
    },
    /**
     * 环保贡献统计
     */
    socialContribution (params) {
      const _this = this

      const render = () => {
        if (_this.socialContributionTimer) {
          clearTimeout(_this.socialContributionTimer)
        }

        ExhibitionService.socialContribution(params).then(resp => {
          let data = (resp.code === 1 && resp.results) || {}
          if (data.year) {
            _this.contribution.coal = data.year.savedCoal || '-'
            _this.contribution.co2 = data.year.carbonEmissions || '-'
            _this.contribution.tree = data.year.deforestation || '-'
          } else {
            _this.contribution.coal = '-'
            _this.contribution.co2 = '-'
            _this.contribution.tree = '-'
          }
          if (data.accumulativeTotal) {
            _this.contribution.coalTotal = data.accumulativeTotal.savedCoal || '-'
            _this.contribution.co2Total = data.accumulativeTotal.carbonEmissions || '-'
            _this.contribution.treeTotal = data.accumulativeTotal.deforestation || '-'
          } else {
            _this.contribution.coalTotal = '-'
            _this.contribution.co2Total = '-'
            _this.contribution.treeTotal = '-'
          }
        })

        _this.socialContributionTimer = setTimeout(render, 60 * 60 * 1000)
      }
      render()
    },

    /**
     * 公共数据渲染
     */
    renderCommonData (params) {
      const _this = this

      const render = () => {
        if (_this.commonDataTimer) {
          clearTimeout(_this.commonDataTimer)
        }

        ExhibitionService.commonData(Object.assign({}, params)).then(resp => {
          if (resp.code === 1) {
            const data = resp.results

            _this.currentTime = data.currentTime || new Date()
            _this.safeRunningDays = data.safeRunDays || 0

            let totalInstalledCapacity = data ? UnitConverter(data.capacity || 0, _this.$t('unit.KW')) : {
              value: 0.0,
              unit: this.$t('unit.KW')
            }

            // let equipmentQuantity = data.equipmentQuantity || 0
            let totalTask = data.allTaskCount || 0
            let untreatedTask = data.notFinishTaskCount || 0

            _this.totalInstalledCapacity = totalInstalledCapacity
            // _this.equipmentQuantity.value = equipmentQuantity
            _this.totalTask.value = totalTask
            _this.untreatedTask.value = untreatedTask
          }
        })

        _this.commonDataTimer = setTimeout(render, 60 * 60 * 1000)
      }
      render()
    },
  },
  created () {
  },
  mounted () {
    const _this = this
    _this.$nextTick(function () {
      document.title = _this.$t('exhibition_systemName')
      // Timer 当前时间定时刷新
      _this.timer = setInterval(function () {
        _this.currentTime = new Date()
      }, 1000)
      _this.goEarth()
      _this.renderLeftAndRight()
      _this.renderCommonData()
    })
  },
  beforeDestroy () {
    this.timer && clearInterval(this.timer)
    this.mapTimer && clearTimeout(this.mapTimer)
    this.powerGenerationTimer && clearTimeout(this.powerGenerationTimer)
    this.powerTrendTimer && clearTimeout(this.powerTrendTimer)
    this.powerScaleTimer && clearTimeout(this.powerScaleTimer)
    this.equipmentDistributionTimer && clearTimeout(this.equipmentDistributionTimer)
    this.powerSupplyTimer && clearTimeout(this.powerSupplyTimer)
    this.socialContributionTimer && clearTimeout(this.socialContributionTimer)
  },

}
