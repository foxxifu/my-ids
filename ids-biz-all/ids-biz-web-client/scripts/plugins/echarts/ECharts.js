/**
 * 图表工具，基于 echarts 3.8.4
 */
(function () {
    "use strict";
    define(['jquery', 'plugins/echarts/echarts-3.8.4', 'plugins/zrender-2.1.0/src/zrender'], function ($, echarts, zrender) {
        
        window.ECharts = {
            echarts: echarts,
            // 图表对象缓存
            Cache: {},
            // 图表主题
            Theme: "macarons",
            // 图表配置参数
            _option: {},
            // 是否显示Loading
            IsShowLoading: false,
            //右键保存图片
            saveImage: function (id, option, isMerge, chart) {
                option = $.extend({}, ECharts.OptionTemplate.CommonOption, option);
                $('#' + id).bind("contextmenu", function (event) {
                    if ($('#imgSave') && $('#imgSave').length) {
                        $('#imgSave').remove();
                    }
                    if (option.imageSave) {
                        var imgSrc;
                        try {
                            imgSrc = chart.getDataURL("png");
                        } catch (e) {
                        }
                        if (imgSrc) {
                            var downloadLink = document.createElement('a');
                            //downloadLink.onclick = _saveImageForIE;
                            downloadLink.href = imgSrc;
                            downloadLink.setAttribute(
                                'download', option.imageName + '.png'
                            );
                            downloadLink.innerHTML = Msg.op.exportPNG;
                            var $ul = $('<ul id="imgSave"/>');
                            var $li = $('<li/>');

                            var left = event.pageX + "px";
                            var top = event.pageY + "px";

                            $ul.css({
                                'top': top,
                                'left': left
                            });
                            $ul.append($li);
                            $('body').append($ul);
                            $li.append($(downloadLink));
                        }
                    }
                    return false;
                });
                $("body").click(function (event) {
                    if ($("#imgSave") && $("#imgSave").length) {
                        $("#imgSave").remove();
                    }
                });
            },

            /**
             * 渲染图表
             * @param id 容器ID
             * @param option 图表配置
             * @param isMerge 是否合并
             */
            Render: function (id, option, isMerge) {
                this._option = option;
                var self = this;
                var chart, container;
                if (id instanceof HTMLElement) {
                    container = id;
                    id = id.id;
                } else {
                    container = document.getElementById(id);
                }
                this._option.domId = container;
                if (!isMerge) {
                    self.Dispose(id);
                }

                if (!container) return;

                chart = echarts.init(container, self.Theme);
                if (self.IsShowLoading) {
                    //图表显示提示信息
                    chart.showLoading({
                        text: Msg.ajax.loading
                    });
                }
                self.Cache[id] = chart;

                try {
                    $(container).resize(function () {
                        setTimeout(function () {
                            ECharts.Resize(id);
                        }, 200);
                    });

                    option = $.extend({}, ECharts.OptionTemplate.CommonOption, option);

                    //数据检查
                    if (this._noDataCheck.check(option)) {
                        ECharts.saveImage(id, option, isMerge, chart);
                        return;
                    }

                    //无数据时的默认样式，当前实现的是饼图样式，其他图可继续扩展
                    var qualifyOpt = option;
                    var checkSeries = qualifyOpt.series;
                    $.each(checkSeries, function (index, item) {
                        switch (item.type) {
                            case "pie":
                                var itamdatas = [];
                                var colorDataFlag = true;
                                item.data && $.each(item.data, function (index, item) {
                                    if (isNaN(item)) {
                                        if (Number(item.value) === 0) {
                                            colorDataFlag = colorDataFlag && true;
                                            itamdatas.push(item);
                                        } else {
                                            colorDataFlag = false;
                                        }
                                    } else {
                                        colorDataFlag = false
                                    }

                                });
                                if (colorDataFlag) { //数据全为0
                                    var roseType = item.roseType;
                                    qualifyOpt.tooltip.show = false;
                                    qualifyOpt.tooltip.showContent = false;
                                    item.data && $.each(item.data, function (index, item) {
                                        item.value = ''; //数据全置空
                                        if (!!roseType) return true; //如果是展示成南丁格尔图，则不用下面的样式
                                        item.itemStyle.normal.color = ECharts.Graphic.LinearGradient({
                                            offsetColors: [{
                                                offset: 0, color: '#fff' // 0% 处的颜色
                                            }, {
                                                offset: 0.5, color: '#eee' // 50% 处的颜色
                                            }, {
                                                offset: 1, color: '#fff' // 100% 处的颜色
                                            }]
                                        })
                                    })
                                }
                                break;
                        }
                    });
                    $.extend(option, qualifyOpt);


                    if (option.yAxis && option.yAxis[0].type == 'value') {
                        var max = 0, min = 0;
                        for (var i = 0; i < option.series.length; i++) {
                            if (option.series[i].type == 'line' || option.series[i].type == 'bar') {
                                //var index = option.series[i].yAxisIndex || 0;
                                var data = option.series[i].data;
                                max = Math.max(
                                    data.removeAll('-').removeAll('').removeAll('_').max(),
                                    (max || 0)
                                );
                                min = Math.min(
                                    data.removeAll('-').removeAll('').removeAll('_').min(),
                                    (min || 0)
                                );
                                //option.yAxis[index].boundaryGap = [0, 0];
                                //option.yAxis[index].precision = 3; // TODO 小数位限制
                                //if ( (max == 1 && min == 1) || (max == -1 && min == -1)
                                //    //|| data.removeAll('-').removeAll('').removeAll('_').min() == max
                                //    //|| data.removeAll('-').removeAll('').removeAll('_').max() == min
                                //    ) {
                                //    option.yAxis[index].splitNumber = 2;
                                //}
                                //if (max == 1 && min != -1) {
                                //    option.yAxis[index].max = max;
                                //}
                                //else if (max > 0 && max < 1) {
                                //    option.yAxis[index].max = max;
                                //    option.yAxis[index].boundaryGap = [0, 0];
                                //}
                                //if (min == -1 && max != 1) {
                                //    option.yAxis[index].min = min;
                                //    //option.yAxis[index].boundaryGap = [-0.01, 0.01];
                                //}
                                //else if (min < 0 && min > -1) {
                                //    option.yAxis[index].min = min;
                                //    //option.yAxis[index].boundaryGap = [-0.0001, 0];
                                //}
                            }
                        }
                        if (max == 1 && min >= 0) {
                            for (i = 0; i < option.yAxis.length; i++) {
                                option.yAxis[i].boundaryGap = [0, 0.00001];
                                option.yAxis[i].splitNumber = 2;
                            }
                        }
                        if (min == -1 && max <= 0) {
                            for (i = 0; i < option.yAxis.length; i++) {
                                option.yAxis[i].boundaryGap = [-0.0001, 0];
                                option.yAxis[i].splitNumber = 2;
                            }
                        }
                    }
                    chart.setOption(option, isMerge);

                    ECharts.saveImage(id, option, isMerge, chart);
                    $(container).bind('mouseup mouseout', function () {
                        self.Resize(id);
                    });
                } catch (e) {
                    console.error(e);
                } finally {
                    if (self.IsShowLoading) {
                        chart.hideLoading();
                    }
                }

                return chart;
            },

            Refresh: function (id, newOption) {
                var self = this;
                self.Cache[id] && self.Cache[id].setOption(newOption, true);
                return self.Cache[id];
            },

            /**
             * 视图区域大小变化更新，默认绑定节点的resize事件
             */
            Resize: function (id) {
                var self = this;
                return (function (id) {
                    try {
                        if (id) {
                            self.Cache[id] && self.Cache[id].resize && self.Cache[id].resize();
                        } else {
                            for (var key in self.Cache) {
                                var c = self.Cache[key];
                                c && c.dom && $(c.dom).is(':visible') && c.resize && c.resize();
                            }
                        }
                    } catch (e) {
                    }

                    return self;
                })(id);
            },

            /**
             * 释放，释放后echarts实例不可用
             */
            Dispose: function (id) {
                var self = this;
                return (function (id) {
                    try {
                        if (id) {
                            self.Cache[id] && self.Cache[id].dispose && self.Cache[id].dispose();
                        } else {
                            for (var key in self.Cache) {
                                var c = self.Cache[key];
                                c && c.dispose && c.dispose();
                            }
                        }
                    } catch (e) {
                    }

                    return self;
                })(id);
            },

            /**
             * 事件处理
             */
            EVENT: {
                // -----------------------基础事件-----------------------
                REFRESH: 'refresh', // 刷新
                RESTORE: 'restore', // 还原
                RESIZE: 'resize', // 显示容器大小变化
                CLICK: 'click', // 点击
                DBLCLICK: 'dblclick', // 双击
                HOVER: 'hover', // 悬浮
                MOUSEOUT: 'mouseout', // 鼠标离开图表

                // ---------------------交互逻辑事件--------------------
                DATA_CHANGED: 'dataChanged',  // 数据修改，如拖拽重计算
                DATA_ZOOM: 'dataZoom', // 数据区域缩放
                DATA_RANGE: 'dataRange', // 值域漫游
                DATA_RANGE_SELECTED: 'dataRangeSelected', //值域开关选择
                DATA_RANGE_HOVERLINK: 'dataRangeHoverLink', // 值域漫游hover
                LEGEND_SELECTED: 'legendSelected', // 图例开关选择
                LEGEND_HOVERLINK: 'legendHoverLink', // 图例hover
                MAP_SELECTED: 'mapSelected', // 地图选择
                PIE_SELECTED: 'pieSelected', // 饼图选择
                MAGIC_TYPE_CHANGED: 'magicTypeChanged', // 动态类型切换
                DATA_VIEW_CHANGED: 'dataViewChanged', // 数据视图修改
                TIMELINE_CHANGED: 'timelineChanged', //时间轴变化
                MAP_ROAM: 'mapRoam' //地图漫游
            },

            /**
             * 数据格式化
             */
            DataFormate: {

                /**
                 * 无分组数据初始化，这种数据格式多用于饼图、单一的柱形图的数据源
                 * @param data {Object} 格式如：[{name:XXX,value:XXX},{name:XXX,value:XXX},……]
                 * @return {Object} {category:Array, data:Array}
                 */
                NoGroupData: function (data) {
                    var categories = [];
                    var datas = [];
                    for (var i = 0; i < data.length; i++) {
                        categories.push(data[i].name || "");
                        datas.push({name: data[i].name, value: data[i].value || 0});
                    }
                    return {category: categories, data: datas};
                },

                /**
                 * 有分组数据初始化，这种格式的数据多用于展示多条折线图、分组的柱形图等
                 * @param data {Object} 格式如：[{name:XXX,group:XXX,value:XXX},{name:XXX,group:XXX,value:XXX},……]
                 * @param type {string} 渲染的图表类型：可以是‘line’,‘bar’
                 * @param isStack {boolean} 是否为堆积图
                 * @return {Object} {category:Array, xAxis:Array, series:Array}
                 */
                GroupData: function (data, type, isStack) {
                    var chartType = "line";
                    if (type) {
                        chartType = type || "line";
                        var xAxis = [];
                        var group = [];
                        var series = [];
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 0; j < xAxis.length && xAxis[j] != data[i].name; j++) ;

                            if (j == xAxis.length) {
                                xAxis.push(data[i].name);
                            }

                            for (var k = 0; k < group.length && group[k] != data[i].group; k++) ;

                            if (k == group.length) {
                                group.push(data[i].group);
                            }
                        }

                        for (var i = 0; i < group.length; i++) {
                            var temp = [];
                            for (var j = 0; j < data.length; j++) {
                                if (group[i] == data[j].group) {
                                    if (type == "map") {
                                        temp.push({name: data[j].name, value: data[i].value});
                                    } else {
                                        temp.push(data[j].value);
                                    }
                                }
                            }
                            var seriesTemp;
                            switch (type) {
                                case 'line':
                                    seriesTemp = {name: group[i], data: temp, type: chartType};
                                    if (isStack)
                                        seriesTemp = $.extend({}, {stack: 'stack'}, seriesTemp);
                                    break;
                                case 'bar' :
                                    seriesTemp = {name: group[i], data: temp, type: chartType};
                                    if (isStack)
                                        seriesTemp = $.extend({}, {stack: 'stack'}, seriesTemp);
                                    break;
                                case 'map':
                                    seriesTemp = {
                                        name: group[i],
                                        type: chartType,
                                        mapType: 'china',
                                        selectedMode: 'single',
                                        itemStyle: {
                                            normal: {
                                                label: {show: true}
                                            },
                                            emphasis: {
                                                label: {show: true}
                                            }
                                        },
                                        data: temp
                                    };
                                    break;
                                default :
                                    seriesTemp = {name: group[i], data: temp, type: chartType};
                            }
                            series.push(seriesTemp);
                        }
                    }
                    return {category: group, xAxis: xAxis, series: series};
                }
            },

            /**
             * 无数据时，显示默认样式，目前只实现了气泡显示
             *
             */
            _noDataCheck: {
                _option: {},
                /*
                 * 检查数据
                 * @param magicOption:获取到的echarts的参数配置
                 * */
                check: function (magicOption) {
                    this._option = magicOption;
                    this._option.flag = true; //有data值
                    var series = magicOption.series;
                    for (var i = 0, l = series.length; i < l; i++) {
                        if (series[i].data && series[i].data.length > 0
                            || series[i].markPoint && series[i].markPoint.data && series[i].markPoint.data.length > 0
                            || series[i].markLine && series[i].markLine.data && series[i].markLine.data.length > 0
                            || series[i].nodes && series[i].nodes.length > 0
                            || series[i].links && series[i].links.length > 0
                            || series[i].matrix && series[i].matrix.length > 0
                            || series[i].eventList && series[i].eventList.length > 0) {
                            this._option.flag = false;
                        }
                    }
                    if (!this._option.flag) {
                        return;
                    }
                    var loadOption = this._option && this._option.noDataLoadingOption || {
                        text: this._option && this._option.noDataText,
                        effect: this._option && this._option.noDataEffect
                    };
                    this.showLoading(loadOption);
                    return true;

                },
                /*
                 * 检查数据
                 * @param loadingOption:参数封装，后面可扩展
                 * */
                showLoading: function (loadingOption) {
                    loadingOption = loadingOption || {};
                    var textStyle = loadingOption.textStyle || {};
                    loadingOption.textStyle = textStyle;
                    textStyle.text = loadingOption.text || this._option && this._option.loadingText;
                    if (loadingOption.x != null) {
                        textStyle.x = loadingOption.x;
                    }
                    if (loadingOption.y != null) {
                        textStyle.y = loadingOption.y;
                    }
                    loadingOption.effectOption = loadingOption.effectOption || {};
                    loadingOption.effectOption.textStyle = textStyle;

                    this.zrShowLoading(loadingOption, this._option.domId);
                    return this;
                },
                /*
                 * 绘制显示动画
                 * @param loadingOption：参数集合（主要有：显示文字，效果，气泡个数）
                 * @param id：需要绘制的容器id <br><br>
                 * example：
                 * <pre>
                 *  noDataLoadingOption: {<br>
                 *      text: '暂无数据',<br>
                 *      effect: 'bubble',<br>
                 *      effectOption: {<br>
                 *          effect: {<br>
                 *              n: 0<br>
                 *          }<br>
                 *      }<br>
                 *  }<br>
                 * </pre>
                 * */
                zrShowLoading: function (loadingOption, id) {
                    var n = 0; //默认0个气泡
                    if (loadingOption.effect === 'bubble') {
                        n = loadingOption.effectOption.effect || 0;
                    }
                    var zr = zrender.init(id);
                    var animationTicket;
                    clearInterval(animationTicket);
                    zr.clear();
                    var color = require(['plugins/zrender-2.1.0/src/tool/color']);
                    var colorIdx = 0;
                    var width = Math.ceil(zr.getWidth());
                    var height = Math.ceil(zr.getHeight());

                    var i;
                    var shapeList = [];
                    require(['plugins/zrender-2.1.0/src/shape/Circle'], function (CircleShape) {
                        // 动画元素
                        for (i = 0; i < n; i++) {
                            shapeList[i] = new CircleShape({
                                style: {
                                    x: Math.ceil(Math.random() * width),
                                    y: Math.ceil(Math.random() * height),
                                    r: Math.ceil(Math.random() * 40),
                                    brushType: Math.ceil(Math.random() * 100) % 3 >= 1 ? 'both' : 'stroke',
                                    color: 'rgba('
                                    + Math.round(Math.random() * 256) + ','
                                    + Math.round(Math.random() * 256) + ','
                                    + Math.round(Math.random() * 256) + ', 0.3)',
                                    strokeColor: 'rgba('
                                    + Math.round(Math.random() * 256) + ','
                                    + Math.round(Math.random() * 256) + ','
                                    + Math.round(Math.random() * 256) + ', 0.3)',
                                    lineWidth: 3
                                },
                                _animationX: Math.ceil(Math.random() * 20),
                                _animationY: Math.ceil(Math.random() * 20),
                                hoverable: false
                            });
                            if (shapeList[i].style.x < 100 || shapeList[i].style.x > (width - 100)) {
                                shapeList[i].style.x = width / 2;
                            }
                            if (shapeList[i].style.y < 100 || shapeList[i].style.y > (height - 100)) {
                                shapeList[i].style.y = height / 2;
                            }
                            zr.addShape(shapeList[i]);
                        }
                        zr.addShape(new CircleShape({
                            position: [100, 100],
                            rotation: [0, 0, 0],
                            scale: [1, 1],
                            style: {
                                x: (width / 2 - 50),
                                y: (height / 2 - 100),
                                r: 50,
                                color: 'transparent',
                                textColor: '#666',
                                strokeColor: "transparent",
                                lineWidth: 0,
                                text: loadingOption.effectOption.textStyle.text,
                                textPosition: 'inside'
                            },
                            hoverable: false
                        }));
                        // 绘画，利用render的callback可以在绘画完成后马上开始动画
                        zr.render(function () {
                            animationTicket = setInterval(function () {
                                var style;
                                for (i = 0; i < n; i++) {
                                    // 可以跳过
                                    style = shapeList[i].style;
                                    if (style.brushType == 'both') {
                                        if (style.x + style.r + shapeList[i]._animationX >= width
                                            || style.x - style.r + shapeList[i]._animationX <= 0
                                        ) {
                                            shapeList[i]._animationX = -shapeList[i]._animationX;
                                        }
                                        shapeList[i].style.x += shapeList[i]._animationX;
                                    }

                                    if (style.brushType == 'both') {
                                        if (style.y + style.r + shapeList[i]._animationY >= height ||
                                            style.y - style.r + shapeList[i]._animationY <= 0) {
                                            shapeList[i]._animationY = -shapeList[i]._animationY;
                                        }
                                        shapeList[i].style.y += shapeList[i]._animationY;
                                    }
                                    else {
                                        if (style.y - shapeList[i]._animationY + style.r <= 0) {
                                            shapeList[i].style.y = height + style.r;
                                            shapeList[i].style.x = Math.ceil(Math.random() * width);
                                        }
                                        shapeList[i].style.y -= shapeList[i]._animationY;
                                    }


                                    // 就看这句就行
                                    zr.modShape(shapeList[i].id, shapeList[i]);
                                }
                                zr.refresh();
                            }, 150);
                        });

                    });
                }
            },

            /**
             * 初始化常用的图表类型
             */
            OptionTemplate: {
                /**
                 * 通用的图表基本配置
                 */
                CommonOption: {
                    grid: {
                        borderWidth: 0
                    },
                    tooltip: {
                        trigger: 'axis', // tooltip触发方式：axis以X轴线触发，item以每一个数据项触发
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    toolbox: {
                        show: false
                    },
                    //yAxis: [{
                    //    boundaryGap: [0, 0]
                    //}],
                    legend: {
                        show: false, // 默认不显示图例
                        selectedMode: false, // 选择模式，默认开启图例开关，可选single，multiple
                        data: []
                    },
                    //calculable: false, // 是否拖拽重计算
                    //animation: false,  // 是否启用动画
                    imageSave: false, // 是否可以右键保存图片
                    imageName: 'image' // 默认保存的图片名称
                },

                /**
                 * 饼图 配置
                 * @param data 数据 格式：[{name:XXX,value:XXX},……]
                 * @param name 显示标题
                 * @return option
                 */
                Pie: function (data, name) {
                    var pieDatas = ECharts.DataFormate.NoGroupData(data);
                    var option = {
                        title: {
                            text: name || ''
                        },
                        tooltip: {
                            tigger: 'item'
                        },
                        legend: {
                            selectedMode: 'multiple',
//                    orient: 'vertical',
                            data: pieDatas.category
                        },
                        series: [
                            {
                                name: name || "",
                                type: 'pie',
                                radius: '65%',
                                center: ['50%', '50%'],
                                data: pieDatas.data
                            }
                        ]
                    };
                    return $.extend({}, ECharts.OptionTemplate.CommonOption, option);
                },

                /**
                 * 折线图 配置
                 * @param data 数据 格式：[{name:XXX,value:XXX,group:XXX},……]
                 * @param name 显示标题
                 * @param xName x轴显示名称
                 * @param yName y轴显示名称
                 * @param isStack 是否为堆积图
                 * @return option
                 */
                Lines: function (data, name, xName, yName, isStack) {
                    var stackLineDatas = ECharts.DataFormate.GroupData(data, 'line', isStack);
                    var option = {
                        title: {
                            text: name || ''
                        },
                        legend: {
                            data: stackLineDatas.category
                        },
                        xAxis: [
                            {
                                name: xName || '',
                                type: 'category',
                                boundaryGap: false,
                                data: stackLineDatas.xAxis
                            }
                        ],
                        yAxis: [
                            {
                                name: yName || '',
                                type: 'value',
                                splitArea: {show: true}
                            }
                        ],
                        series: stackLineDatas.series
                    };
                    return $.extend({}, ECharts.OptionTemplate.CommonOption, option);
                },

                /**
                 * 柱状图 配置
                 * @param data 数据 格式：[{name:XXX,group:XXX,value:XXX},……]
                 * @param name 显示标题
                 * @param xName x轴显示名称
                 * @param yName y轴显示名称
                 * @param isStack 是否为堆积图
                 * @return option
                 */
                Bars: function (data, name, xName, yName, isStack) {
                    var stackBarDatas = ECharts.DataFormate.GroupData(data, 'bar', isStack);
                    var option = {
                        title: {
                            text: name || ''
                        },
                        legend: {
                            data: stackBarDatas.category
                        },
                        xAxis: [
                            {
                                name: xName || '',
                                type: 'category',
                                axisLabel: {
                                    show: true,
                                    interval: 'auto',
                                    rotate: 0,
                                    margin: 8
                                },
                                data: stackBarDatas.xAxis
                            }
                        ],
                        yAxis: [
                            {
                                name: yName || '',
                                type: 'value',
                                splitArea: {show: true}
                            }
                        ],
                        series: stackBarDatas.series
                    };
                    return $.extend({}, ECharts.OptionTemplate.CommonOption, option);
                }

            },

            /**
             *配置渐变和纹理
             */
            Graphic: {
                /** 线性渐变，前四个参数分别是 x0, y0, x2, y2, 范围从 0 - 1
                 *   相当于在图形包围盒中的百分比，如果最后一个参数传 true，则该四个值是绝对的像素位置
                 * eg： ECharts.Graphic.LinearGradient({
                    x0:0,
                    y0:0,
                    x2:0,
                    y2:1,
                    isAbsoluteOffset:false,
                    offsetColors: [{
                        offset: 0, color: '#fff' // 0% 处的颜色
                    }, {
                        offset: 0.5, color: '#eee' // 50% 处的颜色
                    }, {
                        offset: 1, color: '#fff' // 100% 处的颜色
                    }]
                */
                LinearGradient: function (params) {
                    var params = params || {};
                    //配置默认样式
                    var _params = {
                        x0: params.x0 || 0,
                        y0: params.y0 || 0,
                        x2: params.x2 || 0,
                        y2: params.y2 || 1,
                        offsetColors: params.offsetColors || [{
                            offset: 0, color: '#8fe5fa' // 0% 处的颜色
                        }, {
                            offset: 0.5, color: '#fff' // 100% 处的颜色
                        }, {
                            offset: 1, color: '#8fe5fa' // 0% 处的颜色
                        }],
                        isAbsoluteOffset: params.isAbsoluteOffset || false
                    };
                    return new echarts.graphic.LinearGradient(_params.x0 || 0, _params.y0 || 0, _params.x2 || 0, _params.y2 || 1, _params.offsetColors, _params.isAbsoluteOffset || false)

                }
            }
        };

        return ECharts
    });

})();