<template>
  <l-map ref="map" :zoom="zoom" :center="center" :options="options" :min-zoom="minZoom" :max-zoom="maxZoom" :bounds="bounds"
         :editable="editable" :events="events">
    <l-tile-layer v-for="tileProvider in tileProviders" layerType="base" :key="tileProvider.name"
                  :name="tileProvider.name" :visible="tileProvider.visible" :url="tileProvider.url"
                  :attribution="tileProvider.attribution"></l-tile-layer>
    <l-control-layers v-if="options.mapTypeControl && options.mapTypeControl.show"
                      :position="options.mapTypeControl.position"></l-control-layers>
    <l-control-zoom v-if="options.zoomControl && options.zoomControl.show"
                    :position="options.zoomControl.position"></l-control-zoom>
    <l-control-attribution v-if="options.attributionControl && options.attributionControl.show"
                           :position="options.attributionControl.position"
                           :prefix="options.attributionControl.attributionPrefix"></l-control-attribution>
    <l-control-scale v-if="options.scaleControl && options.scaleControl.show"
                     :position="options.scaleControl.position"
                     :imperial="options.scaleControl.imperial"></l-control-scale>

    <l-marker v-for="marker in markers" :key="marker.id" :visible="marker.visible" :draggable="marker.draggable"
              :primary-key="marker.id" :lat-lng="marker.position" :icon="marker.icon" :badge="marker.badge"
              :events="marker.events" :domEvents="marker.domEvents">
      <l-popup v-if="marker.popup" :options="typeof marker.popup === 'object' ? marker.popup.options : {}">
        <l-popup-content
          :data="typeof marker.popup === 'object' ? marker.popup.content : marker.popup"></l-popup-content>
      </l-popup>
      <l-label v-if="marker.label" :content="marker.label" :badge="marker.badge" :options="{noHide: true}"></l-label>
      <l-tooltip v-if="marker.tooltip" :content="marker.tooltip"></l-tooltip>
    </l-marker>

    <l-marker-cluster :options="clusterOptions" v-for="markers in clusterMarkers" :key="markers.type">
      <l-marker v-for="marker in markers.data" :key="marker.id" :visible="marker.visible"
                :draggable="marker.draggable" :primary-key="marker.id" :lat-lng="marker.position" :icon="marker.icon"
                :badge="marker.badge" :events="marker.events" :domEvents="marker.domEvents">
        <l-popup v-if="marker.popup" :options="typeof marker.popup === 'object' ? marker.popup.options : {}">
          <l-popup-content :type="markers.type"
                           :data="typeof marker.popup === 'object' ? marker.popup.content : marker.popup"></l-popup-content>
        </l-popup>
        <l-label v-if="marker.label" :content="marker.label" :badge="marker.badge" :options="{noHide: true}"></l-label>
        <l-tooltip v-if="marker.tooltip" :content="marker.tooltip"></l-tooltip>
      </l-marker>
    </l-marker-cluster>

    <l-circle v-if="circle" @data-circle-change="updateLatLngRadius" :latLng="circleCenter" :radius="circle.radius"
              :isEditor="circle.isEditor" :maxRadius="circle.maxRadius"></l-circle>
  </l-map>
</template>

<script>
  import L from 'leaflet'

  import 'leaflet-active-area'

  import LMap from './components/LMap.vue'
  import LTileLayer from './components/LTileLayer.vue'
  import LMarker from './components/LMarker.vue'
  import LMarkerCluster from './components/LMarkerCluster.vue'
  import LPolyline from './components/LPolyline.vue'
  import LLayerGroup from './components/LLayerGroup.vue'
  import LTooltip from './components/LTooltip.vue'
  import LLabel from './components/LLabel.vue'
  import LPopup from './components/LPopup.vue'
  import LPopupContent from './components/LPopupContent.vue'
  import LControlZoom from './components/LControlZoom.vue'
  import LControlAttribution from './components/LControlAttribution.vue'
  import LControlScale from './components/LControlScale.vue'
  import LControlLayers from './components/LControlLayers.vue'
  import LCircleMarker from './components/LCircleMarker.vue'
  import LCircle from './components/LCircle.vue'

  delete L.Icon.Default.prototype._getIconUrl

  L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('./images/marker-icon-2x.png'),
    iconUrl: require('./images/marker-icon.png'),
    shadowUrl: require('./images/marker-shadow.png'),
  })

  const props = {
    mapType: {
      type: Number,
      default: 0, // 0 为 2D地图，1 为卫星地图
    },
    markers: {
      type: [Object, Array],
      custom: true,
      default: () => []
    },
    clusterMarkers: {
      type: [Object, Array],
      default: () => []
    },
    clusterOptions: {
      type: Object,
      default: () => {}
    },
    editable: { // 是否可以编辑地图
      type: Boolean,
      default: false
    },
    circleMarkers: {
      type: [Object, Array],
      default: () => []
    },
    circle: { // 圆形
      type: Object,
      default: () => {}
    },
    center: {
      type: [Object, Array],
      default: () => [30.413220, 104.219482],
    },
    zoom: {
      type: Number,
      default: 4,
    },
    minZoom: {
      type: Number,
      default: 3,
    },
    maxZoom: {
      type: Number,
      default: 18,
    },
    options: {
      type: Object,
      default: () => ({mapTypeControl: false, zoomControl: false, attributionControl: false}),
    },
    delegateEvent: { // 委托事件 {{ event: '', selector: '', fn: function () {} }}
      type: [Object, Array],
      default: () => [],
    },
    events: {
      type: Object,
    },
    initViewBounds: {
      type: Object,
    },
  }

  export default {
    name: 'Map',
    components: {
      LMap,
      LTileLayer,
      LMarker,
      LMarkerCluster,
      LPolyline,
      LLayerGroup,
      LTooltip,
      LLabel,
      LPopup,
      LPopupContent,
      LControlZoom,
      LControlAttribution,
      LControlScale,
      LControlLayers,
      LCircleMarker,
      LCircle
    },
    props: props,
    data: function () {
      let lang = localStorage.getItem('lang') || 'zh'
      lang = lang === 'zh' ? 'zh_cn' : 'en'
      return {
        tileProviders: [
          {
            name: 'Google-Normal',
            url: 'http://www.google.cn/maps/vt?lyrs=p,m&hl=' + lang + '&x={x}&y={y}&z={z}',
            visible: this.mapType === 0,
            attribution: ''
          },
          {
            name: 'Google-Satellite',
            url: 'http://www.google.cn/maps/vt?lyrs=s,m&hl=' + lang + '&x={x}&y={y}&z={z}',
            visible: this.mapType === 1,
            attribution: ''
          },
        ],
        Positions: ['topleft', 'topright', 'bottomleft', 'bottomright'],
        circleCenter: null,
        bounds: null
      }
    },
    filters: {},
    created () {
      if (this.circle && this.circle.lat && this.circle.lng) {
        this.circleCenter = [this.circle.lat, this.circle.lng]
      } else {
        this.circleCenter = [30.413220, 104.219482]
      }
    },
    mounted: function () {
      const _this = this
      _this.$nextTick(function () {
        _this.$watch('initViewBounds', (newVal, oldVal) => {
          _this['setInitViewBounds'](newVal, oldVal)
        }, {
          deep: true
        })
        _this.bindDelegateEvent(this.delegateEvent)
      })
    },
    methods: {
      bindDelegateEvent (events) {
        if (events) {
          const _this = this
          const element = _this.$el

          if (events instanceof Array) {
            events.forEach(item => {
              _this.bindEvent(element, item.event, item.selector, item.fn)
            })
          } else {
            _this.bindEvent(element, events.event, events.selector, events.fn)
          }
        }
      },
      bindEvent (element, event, selector, fn) {
        const containsNode = function (nodeList, node) {
          let result = false
          Array.prototype.every.call(nodeList, d => {
            if (d.isEqualNode(node)) {
              result = true
              return false
            }
            return true
          })
          return result
        }

        const bind = function (_this, e, args) {
          e = e || window.event
          let target = e.target || e.srcElement

          // 判断target元素是否在NodeList中
          const nodeList = document.querySelectorAll(selector)
          if (containsNode(nodeList, target)) {
            typeof fn === 'function' && fn.apply(_this, args)
          }
        }

        if (element.addEventListener) {
          element.addEventListener(event, function (e) {
            const _this = this
            bind(_this, e, arguments)
          }, false)
        } else {
          element.attachEvent('on' + event, function (e) {
            const _this = this
            bind(_this, e, arguments)
          }, false)
        }
      },
      updateLatLngRadius (lat, lng, r) { // 圆改变半径和位置change事件
        this.$emit('data-circle-change', lat, lng, r)
      },
      setInitViewBounds (newVal, oldVal) {
        if (newVal) {
          this.bounds = new L.LatLng(+this.initViewBounds.latitude, +this.initViewBounds.longitude).toBounds(+this.initViewBounds.radius)
        }
      },
      /**
       * 使地图自适应显示到合适的范围
       *
       * @param bounds {LatLngBounds} 给定的地理边界视图
       * @param options fitBounds options
       *
       * @return {LatLng} 新的中心点
       */
      fitView: function (bounds, options) {
        let map = this.$refs.map
        if (!(map && map.getCenter)) return null;
        if (!bounds) {
          let b = new L.LatLngBounds();
          map.eachLayer(function (t) {
            try {
              if (typeof t.getBounds === "function") {
                t.getBounds() && b.extend(t.getBounds());
              } else if (typeof t.getLatLng === "function") {
                t.getLatLng() && b.extend(t.getLatLng());
              }
            } catch (e) {
            }
          });
          // 当地图上无任何域或者电站显示时, 不做fitBounds操作
          return b.isValid() && map.fitBounds(b, options);
        }

        return map.fitBounds(bounds, options);
      },
    },
    watch: {},
    computed: {},
    beforeDestroy () {
    }
  }
</script>
<style lang="less" src="./style/leaflet.less"></style>
<style lang="less" scoped>
  .leaflet-container {
    background: #81A3C4;

    & /deep/ .activeArea {
      position: absolute;
      top: 30px;
      left: 15px;
      right: 15px;
      bottom: 30px;
    }
  }
</style>

