'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'exhibition',
    description: '模块功能：大屏展示界面',
    importList: [
        'jquery', 'core/main', 'ECharts', 'NumberRoll', 'Timer', 'MapUtil', 'converter', 'Cesium'
    ]
});
App.Module('exhibition', function ($, main) {
    var theme = '1';

    var URLs = {
        // top KPI 数据请求
        topData: main.serverUrl.biz + '/largeScreen/getCommonData',
        // center 地图数据请求
        mapData: '/biz/largeScreen/getMapShowData',

        /*
         * left KPI 数据请求
         */
        // 系统简介
        resumeData: main.serverUrl.biz + '/largeScreen/getCompdisc',
        // 发电趋势
        powerTrendData: main.serverUrl.biz + '/largeScreen/getPowerTrends',
        // 电站规模
        powerScaleData: main.serverUrl.biz + '/largeScreen/getListStationInfo',
        /*
         * right KPI 数据请求
         */
        // 设备分布
        equipmentDistributionData: main.serverUrl.biz + '/largeScreen/getDeviceCount',
        // 供电负荷
        powerSupplyData: main.serverUrl.biz + '/largeScreen/getActivePower',
        // 社会贡献
        socialContributionData: main.serverUrl.biz + '/largeScreen/getSocialContribution',

        // 获取电站信息
        getStationInfo: main.serverUrl.biz + '/station/getStationByCode',

        downloadFile: main.serverUrl.biz + '/fileManager/downloadFile',

        // 获取天气
        getWeatherNow: main.serverUrl.biz + '/weather/getWeatherNow',
        getWeatherDaily: main.serverUrl.biz + '/weather/getWeatherDaily'
    };

    var domainHierarchy = {domainId: '1', domainName: '', type: '2'};
    var kpiModules = {
        resume: {
            id: 'kpi_resume',
            title: Msg.modules.exhibition.kpiModules.resume,
            content: 'resume'
        },
        powerTrend: {
            id: 'kpi_power_trend',
            title: Msg.modules.exhibition.kpiModules.powerTrend,
            content: 'powerTrend'
        },
        powerScale: {
            id: 'kpi_power_scale',
            title: Msg.modules.exhibition.kpiModules.powerScale,
            content: 'powerScale'
        },
        equipmentDistribution: {
            id: 'kpi_equipment_distribution',
            title: Msg.modules.exhibition.kpiModules.equipmentDistribution,
            content: 'equipmentDistribution'
        },
        powerSupply: {
            id: 'kpi_power_supply',
            title: Msg.modules.exhibition.kpiModules.powerSupply,
            content: 'powerSupply'
        },
        socialContribution: {
            id: 'kpi_social_contribution',
            title: Msg.modules.exhibition.kpiModules.socialContribution,
            content: 'socialContribution'
        }
    };
    var map, stationMarkerGroup, domainMarkerGroup;
    var viewer;
    var center = [107.884661, 32.296748]; // [116.403972, 39.915118];

    return {
        Render: function () {
            var _this = this;

            _this.$get('.wrapper').removeClass('type-0').removeClass('type-1').addClass('type-' + theme);

            _this.renderTop();
            _this.renderLeftAndRight();
            _this.renderCenter();
            _this.renderBottom()
        },

        renderTop: function (params) {
            var _this = this;

            _this.$get('#exhibition_totalPower').NumberRoll({value: 0, length: 10, dts: 0});
            _this.$get('#exhibition_safeRunningDays').NumberRoll({value: 0, length: 5, dts: 0});

            _this.$get('.top .top-center h1').click(function () {
                Cookies.clear('isExhibition');
                main.loadSystem();
            });

            _this.$get('.top').everyTimer('5min', function () {
                $.http.post(URLs.topData, params || {}, function (reps) {
                    if (reps.success) {
                        var data = reps.data;
                        var safeRunningDays = Number(data.safeRunDays || 0), totalPower = Number(data.totalCap || 0),
                            currentTime = Number(data.currentTime || 0);

                        if (currentTime) {
                            _this.$get('#exhibition_currentTime').text(Date.parse(currentTime).format('yyyy-MM-dd HH:mm:ss'));
                            _this.$get('#exhibition_currentTime').everyTimer('1s', function () {
                                currentTime += 1000;
                                $(this).text(Date.parse(currentTime).format('yyyy-MM-dd HH:mm:ss'));
                            }, 5 * 60);
                        } else {
                            _this.$get('#exhibition_currentTime').everyTimer('1s', function () {
                                $(this).text(Date.parse().format('yyyy-MM-dd HH:mm:ss'));
                            }, 5 * 60);
                        }

                        _this.$get('#exhibition_safeRunningDays').NumberRollRefresh({value: safeRunningDays});
                        _this.$get('#exhibition_totalPower').NumberRollRefresh({value: totalPower});
                    }
                }, function () {
                    _this.$get('#exhibition_currentTime').everyTimer('1s', function () {
                        $(this).text(Date.parse().format('yyyy-MM-dd HH:mm:ss'));
                    }, 5 * 60);
                });
            });
        },

        renderCenter: function () {
            var _this = this;

            // 初始化地图
            if (theme === '0') {
                map = MapUtil.Instance('exhibition_center_map', $.extend(MapUtil.OptionTemplates.exhibition, {
                    zoomLevel: 4,
                    center: center,
                    activeArea: {
                        position: 'absolute',
                        top: '80px',
                        left: '0',
                        right: '0',
                        bottom: '100px'
                    }
                })).option.map;

                _this.showMapData(null, null, true);
            }
            // 初始化地球
            else if (theme === '1') {
                // var geeMetadata = new GoogleEarthEnterpriseMetadata({
                //     url: 'http://www.earthenterprise.org/3d'
                // });
                Cesium.BingMapsApi.defaultKey = 'Ar9n20kTp-N8tEg3Dpx-Pgocmx3W0-GUnD_Bgt3h8g6pSeDL8yxByTVGHyMyjI2p';
                viewer = new Cesium.Viewer('exhibition_center_map', {
                    geocoder: false,
                    animation: false,
                    timeline: false,
                    homeButton: false,
                    vrButton: false,
                    infoBox: false,
                    fullscreenButton: false,
                    baseLayerPicker : false,
                    scene3DOnly: true,
                    selectionIndicator: false,
                    navigationHelpButton: false,
                    // imageryProvider : new Cesium.GoogleEarthEnterpriseImageryProvider({
                    //     metadata : geeMetadata
                    // }),
                    // terrainProvider : new Cesium.GoogleEarthEnterpriseTerrainProvider({
                    //     metadata : geeMetadata
                    // }),
                    navigationInstructionsInitiallyVisible: false
                });

                viewer.imageryLayers.addImageryProvider(new Cesium.BingMapsImageryProvider({
                    url : 'https://dev.virtualearth.net',
                    // mapStyle: Cesium.BingMapsStyle.AERIAL // Can also use Cesium.BingMapsStyle.ROAD
                    // mapStyle: Cesium.BingMapsStyle.AERIAL_WITH_LABELS
                    mapStyle: Cesium.BingMapsStyle.COLLINS_BART
                }));

                viewer.scene.globe.enableLighting = true;

                var initialPosition = new Cesium.Cartesian3.fromDegrees(104.678114, 30.894512, 20000000);
                var initialOrientation = new Cesium.HeadingPitchRoll.fromDegrees(0, -90, 0);
                var homeCameraView = {
                    destination : initialPosition,
                    orientation : {
                        heading : initialOrientation.heading,
                        pitch : initialOrientation.pitch,
                        roll : initialOrientation.roll
                    }
                };
                // // Set the initial view
                viewer.scene.camera.setView(homeCameraView);
            }
        },

        /**
         * 显示地图数据
         */
        showMapData: function (domainId, type, fitView) {
            var _this = this;
            var params = {};
            if (domainId) {
                params.queryId = domainId;
                params.queryType = type || '2';
            }
            domainHierarchy.domainId = domainId;
            domainHierarchy.domainName = type || '2';
            domainId && _this.refreshLeftAndRight();
            // TODO 地图数据查询
            $.http.post(URLs.mapData, params, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    var areaList = data.domainTree;
                    var showData = data.currentShowData;

                    stationMarkerGroup && map.removeLayer(stationMarkerGroup);
                    stationMarkerGroup = null;
                    domainMarkerGroup && map.removeLayer(domainMarkerGroup);
                    domainMarkerGroup = MapUtil.createMarkerGroup(map);

                    // 域标记
                    MapUtil.circle(domainMarkerGroup, [data.latitude || center[1], data.longitude || center[0]], data.radius || 2800000, {
                        color: '#679AE3',
                        opacity: 0.02,
                        fillColor: '#67C4E3',
                        fillOpacity: 0.02,
                        events: {
                            'click': function (e) {
                                _this.showMapData(e.target.options.domainId, '2', true);
                            }
                        }
                    });
                    fitView && MapUtil.fitView(map);

                    var stationMarkers = [];
                    // 电站标记
                    $.each(showData, function (i, p) {
                        //经度或者纬度为空, 则不展示在地图上
                        if (p.longitude == null || $.trim(p.longitude) == ""
                            || p.latitude == null || $.trim(p.latitude) == "") {
                            return true;
                        }
                        if (p.nodeType) {
                            if (p.nodeType == 1) {
                            }
                            else if (p.nodeType == 2) {
                                MapUtil.addMarker(domainMarkerGroup, MapUtil.createDomainMarker(map, [p.latitude || 0, p.longitude || 0], p.radius, {
                                    domainId: p.id,
                                    strokeOpacity: 0.00001,
                                    fillOpacity: 0.00001,
                                    title: p.name,
                                    events: {
                                        'click': function (e) {
                                            _this.showMapData(e.target.options.domainId, '2', true)
                                        }
                                    }
                                }));
                            }
                            else if (p.nodeType == 3) {
                                var content = "<div style=\"width:800px;\"></div>";
                                var icons = ['centralized', 'distributed', 'household'];
                                var markerColors = ['running', 'building', 'planning'];
                                var options = {
                                    icon: MapUtil.createAwesomeMarkersIcon({
                                        prefix: 'station',
                                        markerColor: icons[p.combinedType - 1] + '-' + markerColors[p.stationType - 1]
                                    }),
                                    popup: content,
                                    tooltip: p.name,
                                    stationCode: p.id,
                                    events: {
                                        'popupopen': function (e) {
                                            $.get("/modules/exhibition/stationInfoWin.html", {}, function (data) {
                                                if (data) {
                                                    e.popup.setContent(data);
                                                    i18n.setLanguage();
                                                    $.http.post(URLs.getStationInfo, {
                                                        domainId: domainHierarchy.id,
                                                        stationCode: e.target.options.stationCode
                                                    }, function (resp) {
                                                        $(".info-station-msg .i18n").show();
                                                        if (!resp.success) {
                                                            return;
                                                        }
                                                        setTimeout(function () {
                                                            var data = resp.data;

                                                            $("#station_name").html(data.stationName);
                                                            $("#station_address").html(data.stationAddr);

                                                            if ($.trim(data.stationFileId) != "") {
                                                                $("#station_pic").attr("src", URLs.downloadFile + "?fileId=" + data.stationFileId);
                                                            } else {
                                                                $("#station_pic").attr("src", "/images/main/exhibition/infoWin/defaultPic.png");
                                                            }

                                                            if ($.trim(data.latitude) != "" && $.trim(data.longitude) != "") {
                                                                $("#station_lonlat").html(data.longitude + "°, " + data.latitude + "°");
                                                            }

                                                            var capacityDisplay = convert(data.capacity, main.Lang + "_" + main.region).from('MW').toBest();
                                                            $("#station_capacity").html(parseFloat(capacityDisplay.val).fixed(3).format() + capacityDisplay.unit);
                                                            $("#station_devoteDate").html($.isNumeric(data.safeRunDatetime)
                                                                ? Date.parse(data.safeRunDatetime, data.timeZone).format('yyyy-MM-dd')
                                                                : Msg.modules.exhibition.map.stationInfoWin.notGrid);
                                                            $("#station_runningDays").html(data.lifeCycle + ' ' + Msg.unit.days);

                                                            var energyDayOutput = convert(data.gridPowerDay, main.Lang + "_" + main.region).from('kWh').toBest();
                                                            $("#station_gridPowerDay").html(parseFloat(energyDayOutput.val).fixed(3).format());
                                                            $("#station_gridPowerDay").next().html(energyDayOutput.unit);

                                                            var energyMonthOutput = convert(data.gridPowerMonth, main.Lang + "_" + main.region).from('kWh').toBest();
                                                            $("#station_gridPowerMonth").html(parseFloat(energyMonthOutput.val).fixed(3).format());
                                                            $("#station_gridPowerMonth").next().html(energyMonthOutput.unit);

                                                            var energyYearOutput = convert(data.gridPowerYear, main.Lang + "_" + main.region).from('kWh').toBest();
                                                            $("#station_gridPowerYear").html(parseFloat(energyYearOutput.val).fixed(3).format());
                                                            $("#station_gridPowerYear").next().html(energyYearOutput.unit);

                                                            $("#station_briefing").html(data.stationBrief);

                                                            var longitudeLatitude = data.latitude + ":" + data.longitude;
                                                            /**
                                                             * 获取天气信息
                                                             */
                                                            //当天的天气状况
                                                            $.http.post(URLs.getWeatherNow, {location: longitudeLatitude}, function (res) {
                                                                if (res.success && res.data.results) {
                                                                    var results = res.data.results[0];
                                                                    console.log('getWeatherNow', results);
                                                                    try {
                                                                        var now_weather = results.now;
                                                                        var now = Date.parse();
                                                                        $('.weather-top').text(Msg.unit.weekday[now.getDay()] + ' ' + now.format(Msg.dateFormat.yyyymmdd) +
                                                                            " (" + Msg.modules.exhibition.map.stationInfoWin.weather.realtime + now_weather.temperature + " " + Msg.unit.temperatureUnit + ")");

                                                                        //未来四天的天气状况
                                                                        $.http.post(URLs.getWeatherDaily, {
                                                                            location: longitudeLatitude,
                                                                            days: 5
                                                                        }, function (res) {
                                                                            if (res.success && res.data.results) {
                                                                                var results = res.data.results[0];
                                                                                console.log('getWeatherDaily', results);
                                                                                try {
                                                                                    var data = results.daily;
                                                                                    var weather_left_div = $(".weather-left").find('div');
                                                                                    $(weather_left_div[0]).find('img').attr("src", "/images/weather/weather180/" + (now_weather.code || '99') + ".png");
                                                                                    $(weather_left_div[1]).text(data[0].low + " ~ " + data[0].high + " " + Msg.unit.temperatureUnit);
                                                                                    $(weather_left_div[2]).text(data[0].text_day);
                                                                                    $(weather_left_div[3]).text(data[0].wind_direction);

                                                                                    var weather_right_div = $(".weather-right").find('div[class="weather-right-day"]');
                                                                                    for (var i = 1; i < data.length; i++) {
                                                                                        var contentDiv = weather_right_div.eq(i - 1).find('div');
                                                                                        var date = Date.parse(data[i].date, Msg.dateFormat.yyyymmdd);
                                                                                        $(contentDiv[0]).text(Msg.unit.weekday[date.getDay()]);
                                                                                        $(contentDiv[1]).text(date.format(Msg.dateFormat.yyyymmdd));
                                                                                        $(contentDiv[2]).find('img').attr("src", "/images/weather/weather60/" + (data[i].code_day || '99') + ".png");
                                                                                        data[i].low && data[i].high && $(contentDiv[3]).text(data[i].low + " ~ " + data[i].high + " " + Msg.unit.temperatureUnit);
                                                                                        $(contentDiv[4]).text(data[i].text_day || '');
                                                                                        $(contentDiv[5]).text(data[i].wind_direction || '');
                                                                                    }
                                                                                } catch (e) {
                                                                                    console.debug(e);
                                                                                }
                                                                            }
                                                                        });
                                                                    } catch (e) {
                                                                        console.debug(e);
                                                                    }
                                                                }
                                                            });
                                                        }, 500);
                                                    });
                                                }
                                            }, 'html');
                                        }
                                    }
                                };
                                stationMarkers.push(MapUtil.createMarker([p.latitude || 0, p.longitude || 0], options));
                            }
                        }
                    });
                    stationMarkerGroup = MapUtil.addCluster(map, stationMarkerGroup, stationMarkers);

                    _this.areaPath(areaList);
                }
            });
        },

        areaPath: function (mapPath) {
            var _this = this;
            var content = _this.$get(".map-path > ul");

            content.empty();
            $.each(mapPath, function (i, path) {
                var item = $('<li/>');
                item.attr("pathId", path.id).attr("pathType", path.nodeType).addClass("mp_item");
                item.append($('<span>').text(path.name));
                item.off('click').on('click', function (e) {
                    if ($(this).hasClass("mp_selected")) {
                        MapUtil.fitView(map);
                    } else {
                        // 地图重绘
                        _this.showMapData($(this).attr("pathId"), $(this).attr("pathType"), true);
                    }
                });
                content.append(item);
            });
            $('.mp_item', content).last().addClass('mp_selected');
        },

        renderLeftAndRight: function () {
            var _this = this;

            var leftBox = _this.$get('.left .box').empty(), rightBox = _this.$get('.right .box').empty();
            var i = 0;
            for (var moduleName in kpiModules) {
                var moduleSetting = kpiModules[moduleName];

                var moduleBox = $('<div>').attr('id', moduleSetting.id).addClass('box-content clearfix');
                var title = $('<h2>').append($('<span>').addClass('title').text(moduleSetting.title));
                moduleBox.append(title);
                var content = $('<div>').addClass('content');
                moduleBox.append(content);

                i < 3 ? leftBox.eq(i).append(moduleBox) : rightBox.eq(i - 3).append(moduleBox);

                _this[moduleName](content);

                i++;
            }
        },
        refreshLeftAndRight: function () {
            var _this = this;
            var params = domainHierarchy && domainHierarchy.domainId && {queryId: domainHierarchy.domainId, queryType: domainHierarchy.type} || {};
            _this.renderTop(params);
            for (var moduleName in kpiModules) {debugger
                var moduleSetting = kpiModules[moduleName];
                var moduleBox = $('#' + moduleSetting.id);

                _this[moduleName](moduleBox.find('.content'), params);
            }
        },

        /**
         * 系统简介
         */
        resume: function (content, params) {
            $(content).empty();

            var description = $('<div/>').addClass('resume-description');
            var img = $('<img/>').addClass('resume-img');
            $(content).append(img).append(description);

            // TODO 添加系统简介数据请求
            $.http.post(URLs.resumeData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    main.DownloadFile({target: img, fileId: data.compPic});
                    description.html(data.compDisc);
                }
            });
        },
        /**
         * 发电趋势
         */
        powerTrend: function (content, params) {
            $(content).empty();

            var chart = $('<div/>').attr('id', 'power_trend_chart').addClass('power-trend-chart');
            var tip = $('<div/>').addClass('power-trend-tip');
            var tipValue = $('<span>').addClass('power-trend-tip-val').text(0);
            tip.append(Msg.modules.exhibition.powerTrend.todayPower).append(tipValue).append(Msg.unit.powerUnit);

            $(content).append(chart).append(tip);

            // TODO 添加发电趋势数据请求
            $.http.post(URLs.powerTrendData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;

                    var yUnit = 'kWh', y2Unit = "万元";
                    var todayPower = Number(data.dayCap || 0),
                        powerTrends = data.powerTrends;

                    tipValue.text(todayPower.fixed(3).format());

                    var xData = [], yData = [], y2Data = [];

                    for (var i = 0; i < Date.parse().getDaysInMonth(); i++) {
                        var isFoundData = false;
                        var xdata = i + 1 < 10 ? "0" + (i + 1) : "" + (i + 1);
                        xData.push(xdata);
                        for (var j = 0; j < powerTrends.length; j++) {
                            if (Date.parse(powerTrends[j].collectTime).format("dd") == xdata) {
                                isFoundData = true;
                                yData.push((powerTrends && powerTrends[j].productPower) || '-');
                                y2Data.push((powerTrends && powerTrends[j].powerProfit) || '-');
                                break;
                            }
                        }
                        if (!isFoundData) {
                            yData.push('-');
                            y2Data.push('-');
                        }
                    }

                    ECharts.Render(document.getElementById('power_trend_chart'), {
                        noDataLoadingOption: {
                            text: Msg.noData,
                            effect: 'bubble',
                            effectOption: {
                                effect: {
                                    n: 0 //气泡个数为0
                                }
                            }
                        },
                        color: ['#1EB05B', '#0F95EF'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                lineStyle: {
                                    color: '#0D96F0'
                                }
                            }
                        },
                        legend: {
                            data: ['发电量', '收益'],
                            textStyle: {
                                fontSize: 14,
                                color: '#F6F6F6',
                                fontWeight: 500
                            },
                            selectedMode: false
                        },
                        grid: {
                            left: 20,
                            right: 20,
                            top: '15%',
                            bottom: 30,
                            containLabel: true,
                            borderColor: '#EEEEEE'
                        },
                        dataZoom: [
                            {
                                type: 'slider',
                                show: true,
                                height: 6,
                                backgroundColor: 'rgba(10, 159, 254, 0.2)',
                                // filterMode: 'empty',
                                fillerColor: 'rgba(10, 159, 254, 0.5)',
                                borderColor: 'rgba(10, 159, 254, 0.2)',
                                borderRadius: 5,
                                handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                                handleSize: 20,
                                handleStyle: {
                                    color: '#0a9ffe'
                                },
                                shadowBlur: 3,
                                shadowColor: 'rgba(0, 0, 0, 0.6)',
                                shadowOffsetX: 2,
                                shadowOffsetY: 2,
                                textStyle: {
                                    color: '#EEEEEE'
                                },
                                left: 58,
                                right: 58,
                                bottom: 10,
                                xAxisIndex: [0],
                                start: 0,
                                end: 100
                            },
                            {
                                type: 'inside'
                            }
                        ],
                        xAxis: [
                            {
                                type: 'category',
                                data: xData,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3BABC4'
                                    }
                                },
                                splitLine: {
                                    show: false,
                                    lineStyle: {
                                        color: '#000',
                                        type: 'dashed'
                                    }
                                }
                            }
                        ],
                        yAxis: [
                            {
                                name: yUnit,
                                type: 'value',
                                nameTextStyle: {
                                    color: "#f0f0f0"
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3BABC4'
                                    }
                                },
                                splitLine: {
                                    show: false,
                                    lineStyle: {
                                        color: '#000',
                                        type: 'dashed'
                                    }
                                }
                            }, {
                                name: y2Unit,
                                type: 'value',
                                nameTextStyle: {
                                    color: "#f0f0f0"
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3BABC4'
                                    }
                                },
                                splitLine: {
                                    show: false,
                                    lineStyle: {
                                        color: '#000',
                                        type: 'dashed'
                                    }
                                }
                            }
                        ],
                        series: [
                            {
                                name: '发电量',
                                type: 'bar',
                                barCategoryGap: '50%',
                                barWidth: '78%',
                                data: yData,
                                yAxisIndex: 0,
                                itemStyle: {
                                    normal: {
                                        barBorderRadius: 5,
                                        z: 0
                                    }
                                }
                            },
                            {
                                name: '收益',
                                type: 'line',
                                barWidth: '13',
                                data: y2Data,
                                symbol: 'circle',
                                symbolSize: 8,
                                yAxisIndex: 1
                            }
                        ]
                    }, false);
                }
            });
        },
        /**
         * 电站规模
         */
        powerScale: function (content, params) {
            $(content).empty();

            var chart = $('<div/>').attr('id', 'power_scale_chart').addClass('power-scale-chart');
            var tip = $('<div/>').addClass('power-scale-tip');
            var tipValue = $('<span>').addClass('power-scale-tip-val').text(0);
            tip.append(Msg.modules.exhibition.powerScale.totalStation).append(tipValue).append(Msg.unit.stationUnit);

            $(content).append(chart).append(tip);

            // TODO 添加电站规模数据请求

            $.http.post(URLs.powerScaleData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.results.stationInfoList;

                    var _0to10, _10to100, _100to500, _500up, _totalStation;
                    if (data) {
                        _0to10 = data.s0to10 ? data.s0to10 : 0;
                        _10to100 = data.s10to100 ? data.s10to100 : 0;
                        _100to500 = data.s100to500 ? data.s100to500 : 0;
                        _500up = data.s500up ? data.s500up : 0;
                        _totalStation = parseInt(_0to10) + parseInt(_100to500) + parseInt(_10to100) + parseInt(_500up);
                    } else {
                        _0to10 = 0;
                        _100to500 = 0;
                        _10to100 = 0;
                        _500up = 0;
                        _totalStation = 0;
                    }

                    tipValue.text(_totalStation.fixed(3).format());

                    ECharts.Render(document.getElementById('power_scale_chart'), {
                        color: ['#39E071', '#FFE038', '#A8A8A8', '#FF3839'],
                        grid: {
                            top: 10,
                            left: 10,
                            right: 10,
                            bottom: 10
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            show: false,
                            left: 'center',
                            data: ['<10kW', '10~100kW', '100~500kW', '>500kW']
                        },
                        series: [
                            {
                                name: '电站装机容量',
                                type: 'pie',
                                radius: ['30%', '60%'],
                                center: ['50%', '50%'],
                                data: [
                                    {value: _0to10, name: '<10kW'},
                                    {value: _10to100, name: '10~100kW'},
                                    {value: _100to500, name: '100~500kW'},
                                    {value: _500up, name: '>500kW'}
                                ],
                                label: {
                                    normal: {
                                        show: true,
                                        align: 'center',
                                        formatter: '{b}\n{c}({d}%)'
                                    }
                                },
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    }, false);
                }
            });
        },
        /**
         * 设备分布
         */
        equipmentDistribution: function (content, params) {
            $(content).empty();

            var chart = $('<div/>').attr('id', 'equipment_distribution_chart').addClass('equipment-distribution-chart');
            var tip = $('<div/>').addClass('equipment-distribution-tip');
            var tipValue = $('<span>').addClass('equipment-distribution-tip-val').text(0);
            tip.append(Msg.modules.exhibition.equipmentDistribution.deviceTotalCount).append(tipValue).append(Msg.unit.stringUnit);

            $(content).append(chart).append(tip);

            // TODO 添加设备分布数据请求
            $.http.post(URLs.equipmentDistributionData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;

                    var arrName = [];
                    var arrValue = [];

                    arrName.push(Msg.modules.exhibition.equipmentDistribution['combinerBoxCount']);
                    arrValue.push(Number(data['combinerBoxCount']).format());
                    arrName.push(Msg.modules.exhibition.equipmentDistribution['inverterStringCount']);
                    arrValue.push(Number(data['inverterStringCount']).format());
                    arrName.push(Msg.modules.exhibition.equipmentDistribution['inverterConcCount']);
                    arrValue.push(Number(data['inverterConcCount']).format());
                    arrName.push(Msg.modules.exhibition.equipmentDistribution['pvCount']);
                    arrValue.push(Number(data['pvCount']).format());

                    tipValue.text(Number(data.deviceTotalCount).format());

                    ECharts.Render(document.getElementById('equipment_distribution_chart'), {
                        noDataLoadingOption: {
                            text: Msg.noData,
                            effect: 'bubble',
                            effectOption: {
                                effect: {
                                    n: 0 //气泡个数为0
                                }
                            }
                        },
                        grid: {
                            show: true,
                            left: '28%',
                            top: '5%',
                            right: '15%',
                            borderWidth: 0,
                            height: '95%'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow'
                            }
                        },
                        xAxis: [{
                            type: 'value',
                            show: false,
                            //线的颜色
                            axisLine: {
                                show: false
                            }
                        }],
                        yAxis: [{
                            //线的颜色
                            axisLine: {
                                show: false
                            },
                            //字的颜色
                            axisLabel: {
                                textStyle: {
                                    color: "#FFFFFF",
                                    fontFamily: 'Microsoft Yahei',
                                    fontSize: 14
                                },
                                formatter: function (val) {
                                    var result;
                                    if (val && val.length > 13) {
                                        if (13 <= val.length - 1) {
                                            result = val.substring(0, 14) + '...';//'\n' + val.substring(position + 1, val.length);
                                        } else {
                                            result = val;
                                        }
                                    } else {
                                        result = val;
                                    }
                                    return result;
                                }
                            },
                            axisTick: {
                                inside: {
                                    default: true
                                },
                                show: false
                            },
                            type: 'category',
                            show: true,
                            triggerEvent: true,
                            data: arrName
                        }],
                        series: [{
                            type: 'bar',
                            stack: '总量',
                            barWidth: 14,
                            barGap: 20,
                            itemStyle: {
                                normal: {
                                    color: '#33CCB8',
                                    barBorderRadius: 7,
                                    label: {
                                        show: true,
                                        position: 'right',
                                        textStyle: {
                                            fontFamily: 'Microsoft Yahei',
                                            fontSize: 14,
                                            color: '#33CCB8'
                                        }
                                    }
                                }
                            },
                            data: arrValue
                        }]
                    }, false);
                }
            });
        },
        /**
         * 供电负荷
         */
        powerSupply: function (content, params) {
            $(content).empty();

            var chart = $('<div/>').attr('id', 'power_supply_chart').addClass('power-supply-chart');
            var tip = $('<div/>').addClass('power-supply-tip');
            var tipValue = $('<span>').addClass('power-supply-tip-val').text(0);
            tip.append(Msg.modules.exhibition.powerSupply.currentSupply).append(tipValue).append(Msg.unit.installCapacityUnit);

            $(content).append(chart).append(tip);

            // TODO 添加发电趋势数据请求
            $.http.post(URLs.powerSupplyData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    tipValue.text(data && Number(data.currentPower || 0).fixed(3).format());

                    var yUnit = Msg.unit.installCapacityUnit;
                    var xData = [];
                    var yData = [];

                    for (var i = 0; i < 24 * 60 / 5; i++) {
                        var t = 5 * i,
                            h = parseInt(t / 60), m = t % 60;
                        var hour = h < 10 ? "0" + h : "" + h;
                        var min = m < 10 ? "0" + m : "" + m;
                        xData.push(hour + ":" + min);
                        yData.push('-');
                    }

                    var dayActivePowerList = data.dayActivePowerList;
                    for (var key in dayActivePowerList) {
                        if (dayActivePowerList.hasOwnProperty(key)) {
                            var value = dayActivePowerList[key];
                            var date = Date.parse(key).format('HH:mm');
                            var index = xData.indexOf(date);
                            if (index !== -1) {
                                yData[index] = value;
                            }
                        }
                    }

                    ECharts.Render(document.getElementById('power_supply_chart'), {
                        noDataLoadingOption: {
                            text: Msg.noData,
                            effect: 'bubble',
                            effectOption: {
                                effect: {
                                    n: 0 //气泡个数为0
                                }
                            }
                        },
                        color: ['#1EB05B', '#0F95EF'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                lineStyle: {
                                    color: '#0D96F0'
                                }
                            }
                        },
                        legend: {show: false},
                        grid: {
                            left: 20,
                            right: 20,
                            top: '15%',
                            bottom: 30,
                            containLabel: true,
                            borderColor: '#EEEEEE'
                        },
                        dataZoom: [
                            {
                                type: 'slider',
                                show: true,
                                height: 6,
                                backgroundColor: 'rgba(10, 159, 254, 0.2)',
                                // filterMode: 'empty',
                                fillerColor: 'rgba(10, 159, 254, 0.5)',
                                borderColor: 'rgba(10, 159, 254, 0.2)',
                                borderRadius: 5,
                                handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                                handleSize: 20,
                                handleStyle: {
                                    color: '#0a9ffe'
                                },
                                shadowBlur: 3,
                                shadowColor: 'rgba(0, 0, 0, 0.6)',
                                shadowOffsetX: 2,
                                shadowOffsetY: 2,
                                textStyle: {
                                    color: '#EEEEEE'
                                },
                                left: 58,
                                right: 10,
                                bottom: 10,
                                xAxisIndex: [0],
                                start: 20,
                                end: 80
                            },
                            {
                                type: 'inside'
                            }
                        ],
                        xAxis: [
                            {
                                type: 'category',
                                data: xData,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3BABC4'
                                    }
                                },
                                splitLine: {
                                    show: false,
                                    lineStyle: {
                                        color: '#000',
                                        type: 'dashed'
                                    }
                                }
                            }
                        ],
                        yAxis: [
                            {
                                name: yUnit,
                                type: 'value',
                                nameTextStyle: {
                                    color: "#f0f0f0"
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3BABC4'
                                    }
                                },
                                splitLine: {
                                    show: false,
                                    lineStyle: {
                                        color: '#000',
                                        type: 'dashed'
                                    }
                                }
                            }
                        ],
                        series: [
                            {
                                name: '功率',
                                type: 'line',
                                smooth: true,
                                smoothMonotone: 'x',
                                symbol: 'circle',
                                symbolSize: 4,
                                data: yData,
                                areaStyle: {
                                    normal: {
                                        color: new ECharts.echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                            offset: 0,
                                            color: '#1EB05B'
                                        }, {
                                            offset: 1,
                                            color: '#3BABC4'
                                        }])
                                    }
                                }
                            }
                        ]
                    }, false);
                }
            });
        },
        /**
         * 社会贡献
         */
        socialContribution: function (content, params) {
            $(content).empty();

            content.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title'))
                .append($('<li>').addClass('t_savedCoal')
                    .append('<img src="/images/main/home/home_envi_mei.png" alt="">')
                    .append($('<span>').html(Msg.modules.exhibition.socialContribution.saveStandardCoal + Msg.unit.brackets[0] + "<i>" + Msg.unit.coalUnit + "</i>" + Msg.unit.brackets[1])))
                .append($('<li>').addClass('t_carbonEmissions')
                    .append('<img src="/images/main/home/home_envi_co2.png" alt="">')
                    .append($('<span>').html(Msg.modules.exhibition.socialContribution.carbonDioxideReduction + Msg.unit.brackets[0] + "<i>" + Msg.unit.co2Unit + "</i>" + Msg.unit.brackets[1])))
                .append($('<li>').addClass('t_deforestation')
                    .append('<img src="/images/main/home/home_envi_senlin.png" alt="">')
                    .append($('<span>').html(Msg.modules.exhibition.socialContribution.reduceDeforestation + Msg.unit.brackets[0] + "<i>" + Msg.unit.tree_unit + "</i>" + Msg.unit.brackets[1])))
            );

            content.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title').text(Msg.modules.exhibition.socialContribution.year))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_savedCoal').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_carbonEmissions').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_deforestation').text(0)))
            );
            content.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title').text(Msg.modules.exhibition.socialContribution.cumulative))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_savedCoal').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_carbonEmissions').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_deforestation').text(0)))
            );

            $.http.post(URLs.socialContributionData, params || {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    for (var type in data) {
                        for (var item in data[type]) {
                            var value = data[type][item];
                            var splitNumber = 1000;
                            var curLang = (main.Lang + "_" + (main.region == 'GB' ? 'UK' : main.region));
                            var prepend;
                            if ('zh_CN' === curLang) {
                                splitNumber = 10000;
                                prepend = "万";
                            } else {
                                prepend = "k";
                            }

                            if (value > splitNumber) {
                                value = value / splitNumber;
                                $('ul', content).eq(0).find('li.t_' + item + ' i').prepend(prepend);
                                $('.' + type + '_' + item, content).text(parseFloat(value).fixed(3));
                            } else {
                                $('.' + type + '_' + item, content).text(parseFloat(value).fixed(3));
                            }
                        }
                    }
                }
            });
        },

        renderBottom: function () {
            var _this = this;
        },

        $get: function (selector) {
            var ele = $('#exhibition');
            return selector ? $(selector, ele) : ele;
        }
    };
});