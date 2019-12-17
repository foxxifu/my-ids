/**
 * Created by p00034 on 2017-01-10.
 */
"use strict";
define([
    'jquery',
    'css!plugins/Leaflet/leaflet.css',
    'css!plugins/Leaflet/leaflet.search.css',
    'css!plugins/Leaflet/L.Control.Basemaps.css',

    'leaflet',
    'plugins/Leaflet/leaflet.providers',
    'leaflet.awesomeMarkers',
    'leaflet.MarkerCluster',
    'plugins/Leaflet/leaflet.activearea',
    'plugins/Leaflet/Leaflet.Editable',
    'plugins/Leaflet/Leaflet.Editable.Drag',
    'plugins/Leaflet/leaflet.search',
    'plugins/Leaflet/L.Control.Basemaps',
    'plugins/Leaflet/leaflet.MassMarker'
], function () {
    var MapUtil = {
        ready: false,
        Cache: [], // 缓存数据
        defaultLanguage: 'zh',

        //theme: {
        //    'default': 'openstreet', // 默认主题 （openstreetmap地图）
        //    'streets': 'streets-v10', // 2D地图
        //    'satellite': 'satellite-v9', // 卫星地图（无街道等浮标信息）
        //    'outdoors': 'outdoors-v10', // 等高线地图
        //    'dark': 'dark-v9', // 暗色主题
        //    'light': 'light-v9', // 亮色主题
        //    'satellite-streets': 'satellite-streets-v10' // 卫星街道地图
        //},

        OptionTemplates: {
            "default": {
                zoomLevel: 3,
                minZoom: 3,
                maxZoom: 17,
                mapType: 0,
                zoomControl: {
                    show: true,
                    position: 'bottomright',
                    zoomInTitle: Msg.map.zoomsTitle[0],
                    zoomOutTitle: Msg.map.zoomsTitle[1]
                },
                scaleControl: {
                    show: true
                },
                mapTypeControl: {
                    show: true,
                    position: 'bottomright',
                    tileX: 0,
                    tileY: 0,
                    tileZ: 0,
                    layers: ['satellite', '2D']
                }
            },
            "exhibition": {
                zoomLevel: 3,
                minZoom: 3,
                maxZoom: 17,
                mapType: 1,
                zoomControl: {
                    show: false
                },
                scaleControl: {
                    show: false
                },
                mapTypeControl: {
                    show: false,
                    layers: ['satellite']
                }
            }
        },

        /**
         * 绘制并初始化地图
         * @param id 容器ID
         * @param option 配置参数
         * @returns {MapUtil}
         */
        Instance: function (id, option) {
            this.ready = !!(window.L);
            var self = this;

            try {
                var p = this.option = {
                    map: {},
                    option: $.extend(true, {}, self.OptionTemplates[option.theme || 'default'], option),
                    container: id
                };
                var lng = p.option.center[0];
                var lat = p.option.center[1];

                var tileLayers = [], initLayer = [];

                if (p.option.mapType) {
                    initLayer.push(L.tileLayer.provider('Google.Satellite', {language: (p.option.language || self.defaultLanguage)}));
                } else {
                    initLayer.push(L.tileLayer.provider('Google.Normal', {language: (p.option.language || self.defaultLanguage)}));
                }
                tileLayers.push(initLayer);

                // 创建地图实例
                if ($('#' + p.container).length > 0) {
                    if (self.Cache[id] && self.Cache[id].map) {
                        self.clearMap(self.Cache[id].map);
                    }
                    p.map = L.map(p.container, {
                        center: self.createPoint(lng, lat),
                        zoom: p.option.zoomLevel || 5,
                        minZoom: 3,
                        maxZoom: 15,
                        layers: initLayer,
                        editable: true,
                        //renderer: L.canvas(),
                        zoomControl: false,
                        attributionControl: false,
                        keyboard: false,
                        //maxBounds: [[90, 180], [-90, -180]],
                        inertia: true,
                        worldCopyJump: true
                    });
                    p.map.invalidateSize(true);
                    p.option.activeArea && p.map.setActiveArea(p.option.activeArea);

                    /* TODO 添加控件 */
                    // 缩放控件
                    if (p.option.zoomControl && p.option.zoomControl.show) {
                        p.map.addControl(L.control.zoom({
                            position: p.option.zoomControl.position || 'bottomright',
                            zoomInText: p.option.zoomControl.zoomInText || '+',
                            zoomInTitle: p.option.zoomControl.zoomInTitle || 'Zoom in',
                            zoomOutText: p.option.zoomControl.zoomOutText || '-',
                            zoomOutTitle: p.option.zoomControl.zoomOutTitle || 'Zoom out'
                        }));
                    }
                    // 地图类型切换控件
                    if (p.option.mapTypeControl && p.option.mapTypeControl.show) {
                        $.each(p.option.mapTypeControl.layers, function (idx, layerName) {
                            if ("2D" === layerName) {
                                if (p.option.mapType !== 0) {
                                    tileLayers.unshift([L.tileLayer.provider('Google.Normal', {language: (p.option.language || self.defaultLanguage)})]);
                                }
                            } else if ("satellite" === layerName) {
                                if (p.option.mapType !== 1) {
                                    tileLayers.unshift([L.tileLayer.provider('Google.Satellite', {language: (p.option.language || self.defaultLanguage)})]);
                                }
                            }
                        });
                        p.map.addControl(L.control.basemaps({
                            position: p.option.mapTypeControl.position || 'bottomright',
                            tileLayers: tileLayers,
                            tileX: p.option.mapTypeControl.tileX || 1,
                            tileY: p.option.mapTypeControl.tileY || 0,
                            tileZ: p.option.mapTypeControl.tileZ || 1
                        }));
                    }
                    // 比例尺
                    if (p.option.scaleControl && p.option.scaleControl.show) {
                        p.map.addControl(L.control.scale({
                            position: p.option.scaleControl.position || 'bottomleft',
                            maxWidth: p.option.scaleControl.maxWidth || 100,
                            metric: p.option.scaleControl.metric || true,
                            imperial: p.option.scaleControl.imperial || true
                        }));
                    }

                    self.Cache[id] = self.option;
                }
            } catch (e) {
                console.error(e);
            }

            return self;
        },

        /**
         * 创建坐标点（Point）
         * @param lng 经度
         * @param lat 纬度
         * @return {* || o.LatLng}
         */
        createPoint: function (lng, lat) {
            if (!this.ready) return null;
            return new L.LatLng(lat, lng);
        },

        /**
         * 创建像素位置
         * @param x
         * @param y
         * @return {* || o.point}
         */
        createPixel: function (x, y) {
            if (!this.ready) return null;
            return L.point(x, y);
        },

        /**
         * 创建图标（Icon）
         * @param options 图标样式参数
         * <pre>
         *     {
         *          iconUrl: './images/marker-icon.png', // 图标图片地址
         *          iconSize: [38, 95], // 图标大小[x, y]
         *          iconAnchor: [22, 94], // 图标标识中心位置[x, y]
         *          popupAnchor: [-3, -76], // 弹出层指向图标位置[x, y]
         *          shadowUrl: './images/marker-shadow.png', // 图标阴影图层
         *          shadowSize: [68, 95], // 阴影大小[x, y]
         *          shadowAnchor: [22, 94] // 阴影标识中心位置[x, y]
         *     }
         * </pre>
         * @returns {* || o.Icon || L.AwesomeMarkers.Icon}
         */
        createIcon: function (options) {
            if (!this.ready) return null;
            var icon = L.icon($.extend({
                iconUrl: './images/marker-icon.png',
                iconSize: [38, 95],
                iconAnchor: [22, 94],
                popupAnchor: [-3, -76],
                shadowUrl: './images/marker-shadow.png',
                shadowSize: [68, 95],
                shadowAnchor: [22, 94]
            }, options));
            return icon || L.Icon.Default();
        },

        /**
         * 创建自定义图标（DivIcon）
         * @param options 图标样式参数
         * <pre>
         *     {
         *          iconUrl: './images/marker-icon.png', // 图标图片地址
         *          iconSize: [38, 95], // 图标大小[x, y]
         *          iconAnchor: [22, 94], // 图标标识中心位置[x, y]
         *          popupAnchor: [-3, -76], // 弹出层指向图标位置[x, y]
         *          shadowUrl: './images/marker-shadow.png', // 图标阴影图层
         *          shadowSize: [68, 95], // 阴影大小[x, y]
         *          shadowAnchor: [22, 94] // 阴影标识中心位置[x, y]
         *     }
         * </pre>
         * @returns {* || o.DivIcon}
         */
        createCustomIcon: function (options) {
            if (!this.ready) return null;
            var icon = L.divIcon($.extend({
                iconUrl: './images/marker-icon.png',
                iconSize: [38, 95],
                iconAnchor: [22, 94],
                popupAnchor: [-3, -76],
                shadowUrl: './images/marker-shadow.png',
                shadowSize: [68, 95],
                shadowAnchor: [22, 94]
            }, options));
            return icon || L.Icon.Default();
        },

        /**
         * 创建预定义标注点图标（AwesomeMarkersIcon）
         * @param options 图标参数
         * <pre>
         *     {<br>
         *          icon: 'home', // 图标名称，如：（'home', 'glass', 'flag', 'star', 'bookmark', ....）* 所有在：http://fortawesome.github.io/Font-Awesome/icons/，http://getbootstrap.com/components/#glyphicons，http://ionicons.com中的图标可以直接使用<br>
         *          prefix: 'glyphicon', // 图标前缀，可选图标库'glyphicon'（bootstrap 3）、'fa'(FontAwesome)<br>
         *          markerColor: 'blue', // 标注点颜色（可选值：'red', 'darkred', 'orange', 'green', 'darkgreen', 'blue', 'purple', 'darkpuple', 'cadetblue'）<br>
         *          iconColor: 'white', // 图标颜色（可选值：'white', 'black' 或者 颜色值 (hex, rgba 等)<br>
         *          spin: false, // 是否转动，* FontAwesome图标必填！<br>
         *          extraClasses: '' // 为Icon生成标签添加指定自定义的 class 属性，如：'fa-rotate90 myclass'<br>
         *     }<br>
         * </pre>
         * @returns {* || L.AwesomeMarkers.Icon}
         */
        createAwesomeMarkersIcon: function (options) {
            if (!this.ready) return null;
            var icon = L.AwesomeMarkers.icon($.extend({
                icon: 'home',
                prefix: 'glyphicon',
                markerColor: 'blue',
                iconColor: 'white',
                spin: true
            }, options));
            return icon || L.Icon.Default();
        },

        /**
         * 创建标注点(Marker)
         *
         * @param point {L.point} 点对象
         * @param options {*} 参数
         *  <pre>
         *      {<br>
         *          icon: L.Icon.Default(), // {L.Icon || L.AwesomeMarkers.Icon} 默认图标<br>
         *          draggable: true, // 使图标可拖拽<br>
         *          title: 'Title', // 添加一个标题<br>
         *          opacity: 0.5 // 设置透明度<br>
         *      }<br>
         *  </pre>
         * @return {Marker}
         */
        createMarker: function (point, options) {
            if (!this.ready) return null;
            options = $.extend({
                draggable: false,
                opacity: 1
            }, options);

            var marker = L.marker(point, options);
            options.popup && marker.bindPopup(options.popup, {
                autoPan: true,
                keepInView: true,
                maxWidth: 950
            }).openPopup();
            options.tooltip && marker.bindTooltip(options.tooltip, {
                direction: 'right',
                permanent: true,
                opacity: 0.7,
                offset: [14, -18]
            }).openTooltip();
            // 添加事件
            if (options && options.events) {
                for (var event in options.events) {
                    if (options.events.hasOwnProperty(event)) {
                        options.events[event] && (typeof options.events[event] === 'function')
                        && marker.on(event, function (e) {
                            options.events[e.type](e);
                        });
                    }
                }
            }
            return marker;
        },

        /**
         * 创建域标注点
         * @param map
         * @param center
         * @param radius
         * @param options
         * @returns {L.marker}
         */
        createDomainMarker: function (map, center, radius, options) {
            if (!this.ready) return null;
            var cssIcon = L.divIcon({
                className: 'domain-icon',
                html: '<div class="domain-wrapper"> <div class="glow"></div> <div class="title"><span>' + options.title + '</span></div> </div>',
                iconSize: [60, 60]
            });
            var marker = L.marker(center, {
                icon: cssIcon,
                domainId: options.domainId
            });
            if (options && options.events) {
                for (var event in options.events) {
                    options.events.hasOwnProperty(event)
                    && options.events[event]
                    && (typeof options.events[event] === 'function')
                    && marker.on(event, function (e) {
                        options.events[e.type](e);
                    });
                }
            }
            return marker;
        },

        /**
         * 添加一个标注点到地图
         * @param map {L.map} 地图
         * @param marker {Marker} 标注点对象
         */
        addMarker: function (map, marker) {
            if (!this.ready) return this;
            if (map && map.addLayer) {
                marker && marker.addTo(map);
            }
            return this;
        },

        /**
         * 添加若干个注点到地图，并返回点聚合对象
         * @param map {Map} 地图对象
         * @param cluster {L.markerClusterGroup} 点聚合对象，如果不存在，会自动创建一个聚合对象
         * @param markers {Array<Marker>} 标注点数组
         * @return {* || L.markerClusterGroup} 返回添加点聚合对象
         */
        addCluster: function (map, cluster, markers) {
            if (!this.ready) return {};
            if (markers && markers.length > 0) {
                if (!cluster) {
                    cluster = L.markerClusterGroup({
                        spiderfyOnMaxZoom: true,
                        showCoverageOnHover: false,
                        zoomToBoundsOnClick: true,
                        spiderLegPolylineOptions: {
                            weight: 1.5,
                            color: '#222',
                            opacity: 0.5
                        }
                    });
                }

                for (var i = 0; i < markers.length; i++) {
                    cluster.addLayer(markers[i]);
                }

                if (map && map.addLayer) {
                    map.addLayer(cluster);
                }
            }
            return cluster;
        },

        /**
         * 添加地图搜索控件
         * @param map
         * @param layerGroup {L.layerGroup}
         * @param options
         * @returns {*}
         */
        addSearchControl: function (map, layerGroup, options) {
            if (!this.ready) return {};
            var searchControl = new L.Control.Search($.extend({
                position: 'topleft',
                layer: layerGroup,
                initial: false,
                zoom: 12,
                marker: false,
                propertyName: 'tooltip',
                collapsed: false,
                textErr: '无法找到该电站',	//error message
                textCancel: '取消',		    //title in cancel button
                textPlaceholder: '请输入电站名...'   //placeholder value
            }, options));
            map && map.addControl && map.addControl(searchControl);

            return searchControl;
        },

        /**
         * 使地图自适应显示到合适的范围
         *
         * @param map
         * @param bounds {LatLngBounds} 给定的地理边界视图
         * @param options fitBounds options
         *
         * @return {LatLng} 新的中心点
         */
        fitView: function (map, bounds, options) {
            if (!(map && map.getCenter)) return null;
            if (!this.ready) return map.getCenter();
            if (!bounds) {
                var b = new L.LatLngBounds();
                map.eachLayer(function (t) {
                    try {
                        if (typeof(t.getBounds) === "function") {
                            t.getBounds() && b.extend(t.getBounds());
                        } else if (typeof(t.getLatLng) === "function") {
                            t.getLatLng() && b.extend(t.getLatLng());
                        }
                    } catch (e) {
                    }
                });
                //当地图上无任何域或者电站显示时, 不做fitBounds操作
                return b.isValid() && map.fitBounds(b, options);
            }

            return map.fitBounds(bounds, options);
        },

        /**
         * 定位到指定点
         * @param map
         * @param lng 经度
         * @param lat 纬度
         * @param zoomLevel 定位缩放级别（默认最大）
         * @param success 定位成功回调方法
         * @param error 定位失败回调方法
         */
        panToPoint: function (map, lng, lat, zoomLevel, success, error) {
            if (!this.ready) error && error instanceof Function && error();
            var self = this;
            setTimeout(function () {
                map.flyTo(self.createPoint(lng, lat), zoomLevel);
            }, 200);
            success && success instanceof Function && success();
        },

        /**
         * 清除地图
         * @param map
         * @return {MapUtil}
         */
        clearMap: function (map) {
            if (!this.ready) return this;
            if (map) {
                map.off();
                map.remove();
            }
            return this;
        },

        /**
         * 创建layerGroup, 用于保存电子围栏, 电站域, 电站等的标注信息, 重绘前可以方便的直接删除整个layerGroup
         * @param map
         * @returns {*}
         */
        createMarkerGroup: function (map) {
            if (!this.ready) return null;
            if (map && map.addLayer) {
                return L.layerGroup().addTo(map);
            }
            return null;
        },

        /**
         * 绘制折线
         * @param map
         * @param lineArr {Array.<LatLng>} 折线各端点坐标
         * @param properties {Object}
         */
        polyline: function (map, lineArr, properties) {
            if (!this.ready) return null;
            var p = L.polyline(lineArr, {
                color: (properties && properties.color) || 'red',            // 线颜色
                opacity: (properties && properties.opacity) || 1,            // 线透明度
                weight: (properties && properties.weight) || 2              // 线宽
            });
            if (map && map.addLayer) {
                p.addTo(map);
            }

            return p;
        },

        /**
         * 绘制多边形
         * @param map
         * @param gonArr {Array.<LatLng>} 多边形各顶点坐标
         * @param properties {Object}
         */
        polygon: function (map, gonArr, properties) {
            if (!this.ready) return null;
            var p = L.polygon(gonArr, {
                strokeColor: (properties && properties.strokeColor) || "#0000ff",
                strokeOpacity: (properties && properties.strokeOpacity) || 1,
                strokeWeight: (properties && properties.strokeWeight) || 2,
                fillColor: (properties && properties.fillColor) || "#f5deb3",
                fillOpacity: (properties && properties.fillOpacity) || 0.35
            });
            if (map && map.addLayer) {
                p.addTo(map);
            }

            return p;
        },

        /**
         * 绘制矩形
         * @param map
         * @param gonArr {Array.<LatLng>} 矩形左上角和右下角顶点坐标
         * @param properties {Object}
         */
        rectangle: function (map, gonArr, properties) {
            if (!this.ready) return null;
            var rect = L.rectangle(gonArr, {
                strokeColor: (properties && properties.strokeColor) || "#0000ff",
                strokeOpacity: (properties && properties.strokeOpacity) || 1,
                strokeWeight: (properties && properties.strokeWeight) || 2,
                fillColor: (properties && properties.fillColor) || "#f5deb3",
                fillOpacity: (properties && properties.fillOpacity) || 0.35
            });
            if (map && map.addLayer) {
                rect.addTo(map);
            }

            return rect;
        },

        /**
         * 绘制圆
         * @param map
         * @param center {LatLng} 圆心经纬度坐标
         * @param radius {Number} 半径
         * @param properties {Object}
         */
        circle: function (map, center, radius, properties) {
            if (!this.ready) return null;
            var c = L.circle(center, {
                opacity: (properties && properties.opacity) || 0.2,
                color: (properties && properties.color) || "orange", //线颜色
                fillColor: (properties && properties.fillColor) || "#ff0", //填充颜色
                fillOpacity: (properties && properties.fillOpacity) || 0.6,//填充透明度
                radius: radius
            });
            if (map && map.addLayer) {
                c.addTo(map);
            }

            return c;
        }

    };

    window.MapUtil = MapUtil;
    return MapUtil;
});