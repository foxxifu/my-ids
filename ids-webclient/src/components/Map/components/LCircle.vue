<template>
  <div style="display: none;">
    <slot v-if="ready"></slot>
  </div>
</template>

<script>
  import L from 'leaflet'
  import propsBinder from '../utils/propsBinder.js'
  import findRealParent from '../utils/findRealParent.js'
  import 'leaflet-editable'

  const props = {
    latLng: {
      type: [Object, Array],
    },
    radius: {
      type: Number,
    },
    isEditor: { // 是否可以编辑
      type: Boolean,
      custom: true,
      default: false
    },
    lStyle: {
      type: Object,
      custom: true,
    },
    visible: {
      type: Boolean,
      custom: true,
      default: true
    },
    stroke: {
      type: Boolean,
      custom: true,
      default: true
    },
    color: {
      type: String,
      custom: true,
      default: '#3388ff'
    },
    weight: {
      type: Number,
      custom: true,
      default: 3
    },
    opacity: {
      type: Number,
      custom: true,
      default: 1.0
    },
    lineCap: {
      type: String,
      custom: true,
      default: 'round'
    },
    lineJoin: {
      type: String,
      custom: true,
      default: 'round'
    },
    dashArray: {
      type: String,
      custom: true,
      default: null
    },
    dashOffset: {
      type: String,
      custom: true,
      default: null
    },
    fill: {
      type: Boolean,
      custom: true,
      default: true
    },
    fillColor: {
      type: String,
      custom: true,
      default: '#3388ff'
    },
    fillOpacity: {
      type: Number,
      custom: true,
      default: 0.2
    },
    fillRule: {
      type: String,
      custom: true,
      default: 'evenodd'
    },
    className: {
      type: String,
      custom: true,
      default: null
    },
    maxRadius: { // 如果是选择区域最大的范围是300万
      type: Number,
      default: 3000000
    }
  }

  export default {
    name: 'LCircle',
    props: props,
    data () {
      return {
        ready: false,
      }
    },
    mounted () {
      const options = {}
      if (this.color) {
        options.color = this.color
      }
      if (this.radius) {
        options.radius = this.radius
      }
      if (this.lStyle) {
        for (var style in this.lStyle) {
          options[style] = this.lStyle[style]
        }
      }
      const otherPropertytoInitialize = ['smoothFactor', 'noClip', 'stroke', 'color',
        'weight', 'opacity', 'lineCap', 'lineJoin', 'dashArray', 'dashOffset', 'fill',
        'fillColor', 'fillOpacity', 'fillRule', 'className'
      ]
      for (var i = 0; i < otherPropertytoInitialize.length; i++) {
        const propName = otherPropertytoInitialize[i]
        if (this[propName] !== undefined) {
          options[propName] = this[propName]
        }
      }
      this.parentContainer = findRealParent(this.$parent)
      if (!this.isEditor || !this.parentContainer.mapObject.editTools) { // 如果不是编辑状态
        this.mapObject = L.circle(this.latLng, options)
        L.DomEvent.on(this.mapObject, this.$listeners)
        propsBinder(this, this.mapObject, props)
        this.ready = true
        this.parentContainer.addLayer(this, !this.visible)
      } else { // 如果是编辑状态，并且当前的map支持编辑才可以
        // this.parentContainer.mapObject 获取当前的map容器对象
        this.circlePathCreate(this.parentContainer.mapObject) // 修改圆形的图标
        this.mapObject = L.createCircleIocn(this.latLng, {radius: this.radius}).addTo(this.parentContainer.mapObject)
        L.DomEvent.on(this.mapObject, this.$listeners)
        propsBinder(this, this.mapObject, props)
        this.ready = true
        // 添加可以编辑和编辑的事件
        let self = this
        this.mapObject.enableEdit()
        let tmpParentMap = this.mapObject._map
        tmpParentMap.addControl(L.control.zoom({position: 'topleft'}))
          .on('editable:dragstart', function (e) {
            tmpParentMap.dragging.disable()
          })
          .on('editable:dragend', function (e) {
            tmpParentMap.dragging.enable()
          })
          .on('editable:vertex:dragend', function (e) {
            let latlng = e.layer.getLatLng()
            tmpParentMap.dragging.disable()
            tmpParentMap.dragging.enable()
            if (e.layer.getRadius() > self.maxRadius) { // 确定范围应该在最大的radius中
              e.layer.setRadius(self.maxRadius)
            }
            self.dataCirleChange(latlng.lat, latlng.lng, e.layer.getRadius()) // 更新中心
          })
        // let flag = false;
        // let oldObjLatLng = {lat: 0, lng: 0}
        // let newObjLatLng = null;
        // self.mapObject.on('mousedown', function (e) {
        //   flag = true;
        //   tmpParentMap.on('move', function (e) {
        //     if (flag) {
        //       newObjLatLng = e.target.getCenter()
        //     }
        //   });
        // })
        // self.mapObject.on('mouseup', function (e) {
        //   flag = false;
        //   if (newObjLatLng && (newObjLatLng.lat !== oldObjLatLng.lat || newObjLatLng.lng !== oldObjLatLng.lng)) {
        //     self.dataCirleChange(newObjLatLng.lat, newObjLatLng.lng, self.radius)
        //     oldObjLatLng = newObjLatLng
        //     self.mapObject.setLatLng(newObjLatLng);
        //     self.mapObject.disableEdit();
        //     self.mapObject.enableEdit();
        //   }
        // })
        // tmpParentMap.on('move', function (e) {
        //   let latlng = e.target.getCenter()
        //   self.mapObject.setLatLng(latlng);
        //   // self.mapObject.disableEdit();
        //   self.mapObject.enableEdit(tmpParentMap);
        //   self.dataCirleChange(latlng.lat, latlng.lng, self.radius);
        // });
        // this.parentContainer.mapObject.panel
      }
    },
    beforeDestroy () {
      this.parentContainer.removeLayer(this)
    },
    methods: {
      /**
       * 圆形的中心和半径改变的事件
       * @param lat 纬度值
       * @param lng 经度值
       * @param r 半径
       */
      dataCirleChange (lat, lng, r) { // 主要是告诉上级当前的位置和半径发生了变化了
        this.$emit('data-circle-change', lat, lng, r)
      },
      setVisible (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.parentContainer.addLayer(this)
        } else {
          this.parentContainer.removeLayer(this)
        }
      },
      setLStyle (newVal, oldVal) {
        if (newVal == oldVal) return
        this.mapObject.setStyle(newVal)
      },
      setStroke (newVal, oldVal) {
        if (newVal == oldVal) return
        this.mapObject.setStyle({stroke: newVal})
      },
      setColor (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({color: newVal})
        }
      },
      setWeight (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({weight: newVal})
        }
      },
      setOpacity (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({opacity: newVal})
        }
      },
      setLineCap (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({lineCap: newVal})
        }
      },
      setLineJoin (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({lineJoin: newVal})
        }
      },
      setDashArray (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({dashArray: newVal})
        }
      },
      setDashOffset (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({dashOffset: newVal})
        }
      },
      setFill (newVal, oldVal) {
        if (newVal == oldVal) return
        this.mapObject.setStyle({fill: newVal})
      },
      setFillColor (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({fillColor: newVal})
        }
      },
      setFillOpacity (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({fillOpacity: newVal})
        }
      },
      setFillRule (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({fillRule: newVal})
        }
      },
      setClassName (newVal, oldVal) {
        if (newVal == oldVal) return
        if (newVal) {
          this.mapObject.setStyle({className: newVal})
        }
      },
      circlePathCreate (map) { // 绘制圆的图形
        L.SVG.include({
          _updateFocusIcon: function (layer) {
            var p = map.latLngToLayerPoint(layer._latlng)
            var r = layer._radius
            var r2 = layer._radiusY || r
            var arc = 'a' + r + ',' + r2 + ' 0 1,0 '
            var circlePath = layer._empty() ? 'M0 0' : 'M' + (p.x - r) + ',' + p.y +
              arc + (r * 2) + ',0 ' +
              arc + (-r * 2) + ',0 '
            var focusPath = 'M' + (p.x + 15) + ',' + (p.y) +
              'L' + (p.x - 15) + ',' + (p.y) +
              'M' + (p.x) + ',' + (p.y + 15) +
              'L' + (p.x) + ',' + (p.y - 15)
            this._setPath(layer, [circlePath, focusPath].join(' '))
          }
        })
        L.CircleFocus = L.Circle.extend({
          _update: function () {
            if (this._map) {
              this._renderer._updateFocusIcon(this)
            }
          }
        })
        // 创建圆的自定义svg图表的圆形信息
        L.createCircleIocn = function (latlng, options, legacyOptions) {
          return new L.CircleFocus(latlng, options, legacyOptions)
        }
      }
    },
    watch: {}
  }
</script>
