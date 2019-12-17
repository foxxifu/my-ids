<template>
  <div style="display: none;">
    <slot v-if="ready"></slot>
  </div>
</template>

<script>
  import L from 'leaflet'
  import 'leaflet.markercluster'

  import propsBinder from '../utils/propsBinder.js'
  import findRealParent from '../utils/findRealParent.js'

  const props = {
    options: {
      type: Object,
      default () { return {} },
    },
  }
  export default {
    name: 'LMarkerCluster',
    props,
    data () {
      return {
        ready: false,
      }
    },
    mounted () {
      this.mapObject = L.markerClusterGroup(Object.assign(this.options, {
        iconCreateFunction: function (cluster) {
          const childCount = cluster.getChildCount()
          const type = Math.floor(Math.log10(childCount))

          return new L.DivIcon({
            html: '<div><span>' + childCount + '</span></div>',
            className: 'marker-cluster ids-marker-cluster marker_' + type,
            iconSize: new L.Point(72 + 23 * type, 72 + 23 * type)
          })
        },
        showCoverageOnHover: false
      }))
      L.DomEvent.on(this.mapObject, this.$listeners)
      propsBinder(this, this.mapObject, props)
      this.ready = true
      this.parentContainer = findRealParent(this.$parent)
      this.parentContainer.addLayer(this)
    },
    beforeDestroy () {
      this.parentContainer.removeLayer(this)
    },
    methods: {
      addLayer (layer, alreadyAdded) {
        if (!alreadyAdded) {
          this.mapObject.addLayer(layer.mapObject)
        }
      },
      removeLayer (layer, alreadyRemoved) {
        if (!alreadyRemoved) {
          this.mapObject.removeLayer(layer.mapObject)
        }
      },
    },
  }
</script>
<style>
  .marker-cluster.ids-marker-cluster {
    background: url("../images/marker-cluster-0-10.png") center no-repeat;
  }

  .marker-cluster.ids-marker-cluster div {
    width: 100%;
    height: 100%;
    font-size: 18px;
    line-height: 72px;
    margin: 0;
    text-align: center;
    color: #ffffff;
  }

  .marker-cluster.ids-marker-cluster.marker_0 {
    background-image: url("../images/marker-cluster-0-10.png");
  }

  .marker-cluster.ids-marker-cluster.marker_1 {
    background-image: url("../images/marker-cluster-10-99.png");
  }

  .marker-cluster.ids-marker-cluster.marker_2 {
    background-image: url("../images/marker-cluster-100+.png");
  }

  .marker-cluster.ids-marker-cluster.marker_0 div {
    line-height: 77px;
  }

  .marker-cluster.ids-marker-cluster.marker_1 div {
    line-height: 100px;
  }

  .marker-cluster.ids-marker-cluster.marker_2 div {
    line-height: 123px;
  }
</style>
