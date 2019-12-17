<template>
  <div class="echarts"/>
</template>

<style>
  .echarts {
    width: 100%;
    min-height: 100px;
  }
</style>

<script>
  import Vue from 'vue'
  import echarts from 'echarts'
  // import echarts from 'echarts/lib/echarts'
  import debounce from 'lodash/debounce'
  import elementResizeDetectorMaker from 'element-resize-detector'

  // 当前支持的所有事件
  const EVENTS = [
    'legendselectchanged',
    'legendselected',
    'legendunselected',
    'datazoom',
    'datarangeselected',
    'timelinechanged',
    'timelineplaychanged',
    'restore',
    'dataviewchanged',
    'magictypechanged',
    'geoselectchanged',
    'geoselected',
    'geounselected',
    'pieselectchanged',
    'pieselected',
    'pieunselected',
    'mapselectchanged',
    'mapselected',
    'mapunselected',
    'axisareaselected',
    'focusnodeadjacency',
    'unfocusnodeadjacency',
    'brush',
    'brushselected',
    'click',
    'dblclick',
    'mouseover',
    'mouseout',
    'mousedown',
    'mouseup',
    'globalout'
  ]

  export default {
    props: {
      options: Object,
      theme: [String, Object],
      initOptions: Object,
      group: String,
      autoResize: {
        type: Boolean,
        default: true
      },
      watchShallow: Boolean
    },
    data () {
      return {
        chart: null,
        lastArea: 0
      }
    },
    computed: {
      width: {
        cache: false,
        get () {
          return this.delegateGet('width', 'getWidth')
        }
      },
      height: {
        cache: false,
        get () {
          return this.delegateGet('height', 'getHeight')
        }
      },
      isDisposed: {
        cache: false,
        get () {
          return !!this.delegateGet('isDisposed', 'isDisposed')
        }
      },
      computedOptions: {
        cache: false,
        get () {
          return this.delegateGet('computedOptions', 'getOption')
        }
      }
    },
    watch: {
      group (group) {
        this.chart.group = group
      }
    },
    methods: {
      mergeOptions (options, notMerge, lazyUpdate) {
        this.delegateMethod('setOption', options, notMerge, lazyUpdate)
      },
      appendData (params) {
        this.delegateMethod('appendData', params)
      },
      resize (options) {
        this.delegateMethod('resize', options)
      },
      dispatchAction (payload) {
        this.delegateMethod('dispatchAction', payload)
      },
      convertToPixel (finder, value) {
        return this.delegateMethod('convertToPixel', finder, value)
      },
      convertFromPixel (finder, value) {
        return this.delegateMethod('convertFromPixel', finder, value)
      },
      containPixel (finder, value) {
        return this.delegateMethod('containPixel', finder, value)
      },
      showLoading (type, options) {
        this.delegateMethod('showLoading', type, options)
      },
      hideLoading () {
        this.delegateMethod('hideLoading')
      },
      getDataURL (options) {
        return this.delegateMethod('getDataURL', options)
      },
      getConnectedDataURL (options) {
        return this.delegateMethod('getConnectedDataURL', options)
      },
      clear () {
        this.delegateMethod('clear')
      },
      dispose () {
        this.delegateMethod('dispose')
      },
      delegateMethod (name, ...args) {
        if (!this.chart) {
          Vue.util.warn(`Cannot call [${name}] before the chart is initialized. Set prop [options] first.`, this)
          return
        }
        return this.chart[name](...args)
      },
      delegateGet (name, method) {
        if (!this.chart) {
          Vue.util.warn(`Cannot get [${name}] before the chart is initialized. Set prop [options] first.`, this)
        }
        return this.chart[method]()
      },
      getArea () {
        return this.$el.offsetWidth * this.$el.offsetHeight
      },
      init () {
        if (this.chart) {
          return
        }

        let chart = echarts.init(this.$el, this.theme, this.initOptions)

        if (this.group) {
          chart.group = this.group
        }

        chart.setOption(this.options, true)

        // 事件处理
        EVENTS.forEach(event => {
          chart.on(event, params => {
            this.$emit(event, params)
          })
        })

        if (this.autoResize) {
          this.lastArea = this.getArea()
          this.__resizeHandler = debounce(() => {
            if (this.lastArea === 0) {
              // 初始化初始不可见的图表
              this.mergeOptions({}, true)
              this.resize()
              this.mergeOptions(this.options, true)
            } else {
              this.resize()
            }
            this.lastArea = this.getArea()
          }, 100, {leading: true})
          this.elementResizeDetector.listenTo(this.$el, this.__resizeHandler)
        }

        this.chart = chart
      },
      destroy () {
        if (this.autoResize) {
          this.elementResizeDetector.removeListener(this.$el, this.__resizeHandler)
        }
        this.dispose()
        this.chart = null
      },
      refresh () {
        this.destroy()
        this.init()
      }
    },
    created () {
      this.$watch('options', options => {
        if (!this.chart && options) {
          this.init()
        } else if (this.chart && options) {
          this.chart.setOption(options, true)
        }
      }, {deep: !this.watchShallow})

      let watched = ['theme', 'initOptions', 'autoResize', 'watchShallow']
      watched.forEach(prop => {
        this.$watch(prop, () => {
          this.refresh()
        }, {deep: true})
      })
      const elementResizeDetectorMaker = require('element-resize-detector')
      this.elementResizeDetector = elementResizeDetectorMaker()
    },
    mounted () {
      if (this.options) {
        this.init()
      }
    },
    activated () {
      if (this.autoResize) {
        this.chart && this.chart.resize()
      }
    },
    beforeDestroy () {
      if (!this.chart) {
        return
      }
      this.destroy()
    },
    connect (group) {
      if (typeof group !== 'string') {
        group = group.map(chart => chart.chart)
      }
      echarts.connect(group)
    },
    disconnect (group) {
      echarts.disConnect(group)
    },
    registerMap (mapName, geoJSON, specialAreas) {
      echarts.registerMap(mapName, geoJSON, specialAreas)
    },
    registerTheme (name, theme) {
      echarts.registerTheme(name, theme)
    },
    graphic: echarts.graphic
  }
</script>
