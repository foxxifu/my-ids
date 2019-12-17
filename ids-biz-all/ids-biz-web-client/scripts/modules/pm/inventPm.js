'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'inventPm',
    description: '模块功能：逆变器运行报表',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'Timer', 'ECharts', 'modules/io/iocommon'
    ]
});

App.Module('inventPm', function ($) {
    //暂时都使用的是设备名称作为x轴
    var timeDim = 'day';//时间的纬度  day:日,month：月, year：年  默认是日，也是给echart图的多电站/但电站的时候使用
    var quereObjects = {};
    var serviceLastTime; // 获取的服务器的前一天的时间
    function reInitParams() {//每次render的时候重新初始化参数,这个是由于每次加载之后如果重新进入这些参数不会重新初始化，可能导致传递的参数错误
        timeDim = 'day';
        serviceLastTime = null;
        // 请求当前的上一天的时间
        $.http.post(main.serverUrl.biz + '/produceMng/getYestodayTime', {}, function (res) {
            if (res.code == 1) {
                serviceLastTime = res.results;
            }
        }, null, false);//同步获取时间
        quereObjects = {timeDim: timeDim, time: serviceLastTime};
    }

    function getMaxDate() { // 获取最大的时间
        if (timeDim === 'day') {
            return new Date(serviceLastTime).format('yyyy-MM-dd');
        }
        if (timeDim === 'month') {
            return new Date(serviceLastTime).format('yyyy-MM');
        }
        return new Date(serviceLastTime).format('yyyy');
    }

    function getShowDate() { // 获取时间
        if (timeDim === 'day') {
            return new Date(quereObjects.time || serviceLastTime).format('yyyy年MM月dd日');
        }
        if (timeDim === 'month') {
            return new Date(quereObjects.time || serviceLastTime).format('yyyy年MM月');
        }
        return new Date(quereObjects.time || serviceLastTime).format('yyyy年');
    }

    return {
        Render: function (params) {
            reInitParams();
            //1.初始查询框的组件
            this.initSearchBar();
            //2.加载发电量和收益的echart图表
            this.initPowersOfEchart();
            //3.加载表格信息
            this.initResult();
        },
        /**
         * 初始搜索栏的组件
         */
        initSearchBar: function () {
            var self = this;

            //查询数据
            function search(data) {
                var stationCodes = $('#pm_invent_search_bar input[name=stationCodes]').data('pm_staion_codes');
                data.params.stationCodes = stationCodes;
                timeDim = data.timeDim;
                data.params.timeDim = timeDim;
                if (data.time && data.time != '') {
                    data.params.time = IoUtil.getCurentTimeOfStr(data.time, timeDim);
                } else {//解决问题单 16 选择统计维度切换后传入后台时间为原时间维度的时间
                    data.params.time = null;
                }
                // $('#pm_invent_list_head div.pm_title_head').html('逆变器运行报表-{0}'.replace('{0}', getShowDate()));
                quereObjects = data.params;
                data.params['index'] = 1;
                data.params['pageSize'] = $('#pm_invent_search_bar select.pselect').val();
                // $('#pm_invent_list').GridTableReload(data);
                self.initResult();
            }

            //初始事件控件
            function dateInit(element, value, data) {
                element.val(new Date(serviceLastTime).format('yyyy-MM-dd'));
                element.focus(function () {
                    var tmpDim = $('#pm_invent_search_bar select[name=timeDim]').val();
                    var dateFmt = 'yyyy-MM-dd';
                    if (tmpDim == 'month') {
                        dateFmt = 'yyyy-MM';
                    } else if (tmpDim == 'year') {
                        dateFmt = 'yyyy';
                    }
                    DatePicker({dateFmt: dateFmt, el: this, isShowOK: true, isShowClear: true, maxDate: getMaxDate()});
                });
            }

            //获取选中的对象
            function getSelectsStations(element) {
                var stationCode = element.data('pm_staion_codes');
                if (!stationCode) {
                    return false;
                }
                //var tmpArr = stationCode.split('@');
                if (stationCode.length > 0) {
                    var tmpStationNameArr = element.val().split(',');
                    var resluts = [];
                    for (var i = 0; i < stationCode.length; i++) {
                        resluts.push({stationCode: stationCode[i], stationName: tmpStationNameArr[i]});
                    }
                    return resluts;
                } else {
                    return false;
                }
            }

            $('#pm_invent_search_bar').ValidateForm('pm_invent_search_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//设备名称
                        name: 'stationCodes',
                        fnInit: function (element) {
                            element.off('focus').on('focus', function () {
                                IoUtil.showStationDialog(function (records) {
                                    var len = records.length;
                                    if (len == 0) {
                                        element.val('');
                                        element.removeAttr('title');
                                        element.removeData('pm_staion_codes')
                                    } else {
                                        var scArr = [];
                                        scArr.push(records[0].stationCode);
                                        var showText = records[0].stationName;
                                        for (var i = 1; i < len; i++) {
                                            scArr.push(records[i].stationCode);
                                            showText += ',' + records[i].stationName;
                                        }
                                        element.val(showText).attr('title', showText).data('pm_staion_codes', scArr);//存放信息
                                    }
                                }, getSelectsStations(element));
                            });
                        }
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '统计维度',//处理结果
                        name: 'timeDim',
                        options: [
                            {value: 'day', text: '日'},
                            {value: 'month', text: '月'},
                            {value: 'year', text: '年'},
                        ],
                        fnInit: function (element) {
                            element.unbind('change').bind('change', function () {
                                var $this = $(this);
                                setTimeout(function () {
                                    var fmt;
                                    if ($this.val() === 'day') {
                                        fmt = 'yyyy-MM-dd';
                                    } else if ($this.val() === 'month') {
                                        fmt = 'yyyy-MM'
                                    } else {
                                        fmt = 'yyyy'
                                    }
                                    $('#pm_invent_search_bar input[name=time]').val(new Date(serviceLastTime).format(fmt));//清空数据
                                }, 0)
                            });
                        }
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '发生时间',//处理结果
                        name: 'time',
                        width: 181,
                        extend: {class: 'Wdate'},
                        fnInit: dateInit,
                    }
                ]],
                extraButtons: [
                    {
                        input: 'button',
                        type: 'button',
                        show: '',
                        name: '',
                        fnClick: function (e) {
                            console.log(222);
                        },//点击回调事件 type=button
                        align: 'right',
                        extend: {'class': 'btn btn-filter'}
                    }
                ]
            });
        },
        /**
         * 显示发电量和收益的echart图表
         */
        initPowersOfEchart: function () {
            var self = this;
            var dataAxis = [''
            ];
            var xLen = dataAxis.length;
            var tmpXdatas = [];
            var xTextStyle = {//设置x轴的字体为白色
                color: '#aaa',
                fontSize: 16
            }
            // 这里做遍历是为了给x轴的文字设置颜色为灰色
            for (var i = 0; i < xLen; i++) {
                var tmObj = {};
                tmObj.value = dataAxis[i];
                tmObj.textStyle = xTextStyle;
                tmpXdatas.push(tmObj);
            }
            var data1 = [''
            ]
            self.myEachOpions.xAxis[0].data = tmpXdatas;
            self.myEachOpions.series[0].data = data1;
            ECharts.Render('pm_invent_echart', self.myEachOpions);
        },
        initListHeard: function () {//初始集合的表头
            var $head = $('#pm_invent_list_head');
            $head.empty();
            var $title = $('<div class="pm_title_head">' + '逆变器运行报表-{0}'.replace('{0}', getShowDate()) + '</div>');
            $head.append($title);
            var $btn = $('<div class="pm_title_btn"><input type="button" class="btn" value="导出"></div>');
            $head.append($btn);
            $btn.unbind('click').bind('click', function () {
                App.confirm('确认导出数据？', function () {
                    var tmpStr = 'timeDim=' + quereObjects.timeDim;
                    if (quereObjects.time) {
                        tmpStr += '&time=' + quereObjects.time;
                    }
                    if (quereObjects.deviceIds) {
                        tmpStr += '&deviceIds=' + quereObjects.deviceIds;
                    }
                    window.location.href = (main.serverUrl.biz + '/produceMng/exportInverterRpt?' + tmpStr);
                });
            });
        },
        /**
         * 获取表格的数据
         * @param params
         */
        initResult: function () {
            var self = this;
            this.initListHeard();

            // 设置绿色字体
            function getBlueColor(element, value, data) {
                element.addClass('io_green_color');
            }

            var colModel;
            if (timeDim === 'day') { // 天纬度的数据
                colModel = [
                    {
                        name: 'id',
                        hide: true
                    },
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.2,
                        fnInit: getBlueColor
                    },
                    {
                        display: '设备名称',
                        name: 'deviceName',
                        //align: "center",
                        width: 0.2,
                        fnInit: getBlueColor
                    },
                    {
                        display: '发电量(kWh)',
                        name: 'inverterPower',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '转换效率(%)',
                        name: 'efficiency',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '峰值功率(kW)',
                        name: 'peakPower',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display: '生产可靠度',
                        name: 'aopRatio',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getMyTime
                    },
                    {
                        display: '生产偏差',
                        name: 'powerDeviation',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getMyTime
                    },
                    {
                        display: '通讯可靠度',
                        name: 'aocRatio',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getMyTime
                    }
                ]
            } else { // 月 年的数据
                colModel = [
                    {
                        name: 'id',
                        hide: true
                    },
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.3,
                        fnInit: getBlueColor
                    },
                    {
                        display: '设备名称',
                        name: 'deviceName',
                        //align: "center",
                        width: 0.25,
                        fnInit: getBlueColor
                    },
                    {
                        display: '发电量(kWh)',
                        name: 'inverterPower',
                        //align: "center",
                        width: 0.15,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '转换效率(%)',
                        name: 'efficiency',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display: '峰值功率(kW)',
                        name: 'peakPower',
                        //align: "center",
                        width: 0.15,
                        //fnInit: getAlarmStatus
                    }
                ]
            }
            ;
            $('#pm_invent_list').GridTable({
                url: main.serverUrl.biz + '/produceMng/listInverterRpt',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 280,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                //               line_height:"48",
                max_height: 280,
                params: quereObjects,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                loadReady: function (results) {//获取到数据的时候
                    var list = results.list;
                    var len = list.length;
                    var xDatas = [];//x轴的数据
                    var yDatas = [];//y轴的数据
                    var maxY = 0;
                    for (var i = 0; i < len; i++) {
                        var tmpData = list[i];
                        xDatas.push(tmpData.deviceName);
                        yDatas.push(tmpData.inverterPower || '-');
                        if (tmpData.inverterPower && tmpData.inverterPower * 1.2 > maxY) {
                            maxY = (tmpData.inverterPower * 1.2).fixed(2);
                        }

                    }
                    var opts = $.extend(true, {}, self.myEachOpions);//深度复制 递归地复制找到的任何对象
                    opts.xAxis[0].data = xDatas;
                    opts.yAxis[0].max = maxY || 10;
                    opts.series[0].data = yDatas;
                    if (xDatas.length > 10) {
                        opts.xAxis[0].axisLabel.rotate = 8;
                    }
                    ECharts.Render('pm_invent_echart', opts, false);
                },
                loadError: function () {
                    ECharts.Render('pm_invent_echart', self.myEachOpions, false);
                },
                colModel: colModel
            });
        },
        /**
         * eachart 的初始数据
         */
        myEachOpions: {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['发电量(kWh)'],
                textStyle: {//设置显示文字的颜色
                    color: '#fff'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            // calculable : true,
            xAxis: [
                {
                    type: 'category',
                    data: [],//加载x轴的数据
                    splitLine: {show: false},//去除网格线
                    lineStyle: {
                        show: false,
                    },
                    nameTextStyle: {
                        color: '#fff'
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#288297',
                            width: 2,//这里是为了突出显示加上的
                        }
                    },
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLabel: {
                        interval: 0,
                        rotate: 0,
                        align: 'center',
                        margin: 20,
                        formatter: function (value, index) {
                            var showText = value;
                            if (value.length > 10) {
                                showText = value.substring(0, 8) + "....";
                            }
                            return showText;
                        }
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '发电量(kWh)',
                    position: 'left',
                    splitLine: {show: false},//去除网格线
                    min: 0,
                    max: 500,
                    axisLabel: {
                        formatter: '{value}'
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#288297',
                            width: 2,//这里是为了突出显示加上的
                        }
                    }
                }
            ],
            series: [
                {
                    name: '发电量(kWh)',
                    type: 'bar',
                    barWidth: 10,
                    yAxis: 1,
                    itemStyle: {
                        normal: {
                            color: '#1eb05b',
                            barBorderRadius: [5, 5, 0, 0]//设置圆角
                        }
                    },
                    data: []
                }
            ]
        }
    }
});

