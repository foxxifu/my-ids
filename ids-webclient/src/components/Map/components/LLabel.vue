<template>
  <div>
    <slot v-if="ready"></slot>
  </div>
</template>

<script>
  import Label from '../lib/leaflet-label/leaflet.label'

  import propsBinder from '../utils/propsBinder.js'
  import findRealParent from '../utils/findRealParent.js'

  const props = {
    content: {
      default: '',
    },
    options: {
      type: Object,
      default: () => ({}),
    }
  }

  export default {
    name: 'LLabel',
    props: props,
    data () {
      return {
        ready: false,
      }
    },
    mounted () {
      this.mapObject = new Label()
      this.mapObject.setContent(this.content || this.$el)
      propsBinder(this, this.mapObject, props)
      this.ready = true
      this.parentContainer = findRealParent(this.$parent)
      this.parentContainer.mapObject.bindLabel('<span>' + this.content + '</span>', this.options).showLabel(this.mapObject)
    },
    beforeDestroy () {
      if (this.parentContainer.mapObject.getLabel()) {
        this.parentContainer.mapObject.unbindLabel()
      }
    },
  }
</script>
<style src="../lib/leaflet-label/leaflet.label.css"></style>
