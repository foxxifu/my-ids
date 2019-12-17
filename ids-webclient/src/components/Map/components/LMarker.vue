<template>
  <div style="display: none;">
    <slot v-if="ready"></slot>
  </div>
</template>

<script>
  import L from 'leaflet'

  import propsBinder from '../utils/propsBinder.js'
  import findRealParent from '../utils/findRealParent.js'

  const props = {
    primaryKey: {
      type: String,
    },
    label: {
      type: String,
      custom: true,
      default: ''
    },
    badge: {
      type: [Number, String],
      custom: true,
      default: ''
    },
    draggable: {
      type: Boolean,
      custom: true,
      default: false,
    },
    visible: {
      type: Boolean,
      custom: true,
      default: true,
    },
    latLng: {
      type: [Object, Array],
      custom: true,
    },
    icon: {
      type: [Object, String],
      custom: true,
      default: ''
    },
    zIndexOffset: {
      type: Number,
      custom: false,
    },
    events: {
      type: Object,
      custom: true,
      default: () => ({}),
    },
    domEvents: {
      type: Object,
    },
    userData: {
      type: Object,
    },
    options: {
      type: Object,
      default: () => ({}),
    },
  }

  export default {
    name: 'LMarker',
    props: props,
    data () {
      return {
        ready: false,
      }
    },
    mounted () {
      const options = this.options

      options.icon = this.createIcon(this.icon, this.badge)

      options.draggable = this.draggable
      options.alt = this.primaryKey || ''

      let mapObject = this.mapObject = L.marker(this.latLng, options)

      mapObject.on('move', (ev) => {
        if (Array.isArray(this.latLng)) {
          this.latLng[0] = ev.latlng.lat
          this.latLng[1] = ev.latlng.lng
        } else {
          this.latLng.lat = ev.latlng.lat
          this.latLng.lng = ev.latlng.lng
        }
      })
      mapObject.on(this.events)
      L.DomEvent.on(this.mapObject, this.$listeners)

      propsBinder(this, this.mapObject, props)

      this.ready = true
      this.parentContainer = findRealParent(this.$parent)
      this.parentContainer.addLayer(this, !this.visible)
    },
    beforeDestroy () {
      this.parentContainer.removeLayer(this)
    },
    methods: {
      createIcon (iconOption, badgeNumber) {
        let icon = new L.Icon.Default()
        if (iconOption) {
          if (typeof iconOption === 'string') {
            icon = L.icon({
              iconUrl: iconOption,
              iconSize: [72, 98],
              iconAnchor: [36, 96],
              tooltipAnchor: [36, -65],
              popupAnchor: [-1, -65]
            })
          } else if (iconOption.type === 'custom') {
            icon = L.divIcon({
              iconSize: [55, 55],
              iconAnchor: [32, 32],
              className: 'leaflet-div-icon leaflet-custom-marker-icon'
            })
          } else if (badgeNumber) {
            let badge = ''
            if (typeof badgeNumber === 'number' && +badgeNumber > 99) {
              badge = '99+'
            } else {
              badge = badgeNumber
            }
            badge = '<sup class="el-badge__content is-fixed" style="top: 0; right: 0; transform: translateY(0%) translateX(50%);">' + badge + '</sup>'

            icon = L.divIcon(Object.assign({
              html: '<img src="' + iconOption.iconUrl + '" alt="' + this.primaryKey + '" tabindex="0" class="leaflet-marker-icon leaflet-zoom-animated leaflet-interactive"/>' + badge,
              iconSize: [55, 55],
              iconAnchor: [32, 32],
              className: 'leaflet-div-icon',
            }, iconOption))
          } else {
            icon = L.icon(iconOption)
          }
        }
        return icon
      },
      setDraggable (newVal, oldVal) {
        if (this.mapObject.dragging) {
          newVal ? this.mapObject.dragging.enable() : this.mapObject.dragging.disable()
        }
      },
      setVisible (newVal, oldVal) {
        if (newVal == oldVal) return
        if (this.mapObject) {
          if (newVal) {
            this.parentContainer.addLayer(this)
          } else {
            this.parentContainer.removeLayer(this)
          }
        }
      },
      setLatLng (newVal) {
        if (newVal == null) {
          return
        }

        if (this.mapObject) {
          let oldLatLng = this.mapObject.getLatLng()
          let newLatLng = {
            lat: newVal[0] || newVal.lat,
            lng: newVal[1] || newVal.lng,
          }
          if (newLatLng.lat != oldLatLng.lat || newLatLng.lng != oldLatLng.lng) {
            this.mapObject.setLatLng(newLatLng)
          }
        }
      },
      setIcon (newVal, oldVal) {
        this.mapObject.setIcon(this.createIcon(newVal, this.badge))
      },
      setEvents (newVal, oldVal) {
        if (newVal) {
          this.mapObject.on(newVal)
          L.DomEvent.on(this.mapObject, this.$listeners)
        }
      },
      setBadge (newVal, oldVal) {
        this.mapObject.setIcon(this.createIcon(this.icon, newVal))
      }
    }
  }
</script>
