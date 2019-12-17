'use strict';
(function (factory, window) {
    if (typeof define === 'function' && define.amd) {
        define(['leaflet'], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory(require('leaflet'));
    }
    if (typeof window !== 'undefined' && window.L) {
        factory(window.L);
    }
}(function (L) {

    L.MassMarkers = L.Layer.extend({
        options: {
            opacity: 0.8,
            zIndex: 111,
            cursor: 'pointer',
            style: []
        },

        initialize: function (data, options) {
            L.setOptions(this, options);

            this._markers = [];
            this.onMap = false;
        },

        onAdd: function(map) {
            var pane = map.getPane(this.options.pane);
            this._container = L.DomUtil.create('canvas', 'leaflet-zoom-animated');

            pane.appendChild(this._container);
            debugger

            // Calculate initial position of container with `L.Map.latLngToLayerPoint()`, `getPixelOrigin()` and/or `getPixelBounds()`
            //var point = map.latLngToLayerPoint();
            //var point = map.getPixelOrigin();
            var point = map.getPixelBounds();

            L.DomUtil.setPosition(this._container, point);

            // Add and position children elements if needed

            map.on('zoomend viewreset', this._update, this);
        },

        onRemove: function(map) {
            L.DomUtil.remove(this._container);
            map.off('zoomend viewreset', this._update, this);
        },

        _update: function() {
            // Recalculate position of container

            L.DomUtil.setPosition(this._container, point);

            // Add/remove/reposition children elements if needed
        }
    });

    L.massMarkers = function (markers, options) {
        var layer = new L.MassMarkers(null, options), i;
        for (i in markers) {
            layer._markers[i] = markers[i];
        }
        return layer;
    };
}, window));
