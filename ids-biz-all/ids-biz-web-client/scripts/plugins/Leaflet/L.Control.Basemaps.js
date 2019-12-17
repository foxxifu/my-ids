(function (factory) {
    if(typeof define === 'function' && define.amd) {
        //AMD
        define(['leaflet'], factory);
    } else if(typeof module !== 'undefined') {
        // Node/CommonJS
        module.exports = factory(require('leaflet'));
    } else {
        // Browser globals
        if(typeof window.L === 'undefined')
            throw 'Leaflet must be loaded first';
        factory(window.L);
    }
})(function (L) {

    L.Control.Basemaps = L.Control.extend({
        _map: null,
        includes: L.Mixin.Events,
        options: {
            position: 'bottomright',
            tileX: 0,
            tileY: 0,
            tileZ: 0,
            layers: []  // list of basemap layer objects, first in list is default and added to map with this control
        },
        availableLayerGroup:[],
        currentLayerGroupIdx:0,
        onAdd: function (map) {
            this._map = map;
            var container = L.DomUtil.create('div', 'basemaps leaflet-control');
            var layerImgDom = L.DomUtil.create('img', 'animated rotateIn', container);
            var layerImgUrl = [];
            // disable events
            L.DomEvent.disableClickPropagation(container);
            if (!L.Browser.touch) {
                L.DomEvent.disableScrollPropagation(container);
            }
            this.options.tileLayers.forEach(function(group, groupIdx){
                var layers = [];
                //多个图层合并为layerGroup
                group.forEach(function(d, i){
                    layers.push(d);
                }, this);
                var layerGroup = L.layerGroup(layers);
                this.availableLayerGroup.push(layerGroup);

                //默认选中最末一个layerGroup做展示
                if (groupIdx == this.options.tileLayers.length -1) {
                    this._map.addLayer(layerGroup);
                    this.currentLayerGroupIdx = groupIdx;
                }
                var coords = {x: this.options.tileX, y: this.options.tileY};
                var url = L.Util.template(group[0]._url, L.extend({
                    s: group[0]._getSubdomain(coords),
                    x: coords.x,
                    y: group[0].options.tms ? group[0]._globalTileRange.max.y - coords.y : coords.y,
                    z: this.options.tileZ
                }, group[0].options));
                layerImgUrl.push(url);
            }, this);
            if(this.availableLayerGroup.length >= 2){
                layerImgDom.src = layerImgUrl[this.currentLayerGroupIdx];
                L.DomEvent.on(container, 'click', function(e) {
                    $(this).removeClass("rotateIn").addClass("rotateOut");
                    var self = this;
                    setTimeout(function(){
                        self._map.removeLayer(self.availableLayerGroup[self.currentLayerGroupIdx]);
                        self.currentLayerGroupIdx++;
                        if(self.currentLayerGroupIdx==layerImgUrl.length) self.currentLayerGroupIdx=0;
                        $(".basemaps img").remove();
                        $(".basemaps").append($("<img/>").attr("src",layerImgUrl[self.currentLayerGroupIdx]).addClass("animated rotateIn"))
                        self._map.addLayer(self.availableLayerGroup[self.currentLayerGroupIdx]);
                    },200);
                }, this);
            } else {
                L.DomUtil.remove(layerImgDom);
            }

            this._container = container;
            return this._container;
        }
    });

    L.control.basemaps = function (options) {
        return new L.Control.Basemaps(options);
    };

    return L.Control.Basemaps;
});









