'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'home',
    description: '模块功能：首页',
    importList: [
        'jquery', 'core/main', 'easyTabs', 'ECharts', 'DatePicker', 'NumberRoll', 'Timer', 'converter', 'GridTable'
    ]
});
App.Module('home', function () {
    var isMuti = true;//判断是否是单电站 true:多电站  false：单电站
    var showStationCode = false;//保存展示的电站编号
    var currentJWD = '0:0';//当前电站的经纬度
    var URLs = {
        powerMsg: main.serverUrl.biz + '/stationOverview/getRealtimeKPI',
        powerRank_pr: main.serverUrl.biz + '/stationOverview/getPRList',
        powerRank_ph: main.serverUrl.biz + '/stationOverview/getPPRList',
        powerState: main.serverUrl.biz + '/stationOverview/getStationStatus',
        equipmentAlarm: main.serverUrl.biz + '/stationOverview/getAlarmStatistics',
        generatingIncome: main.serverUrl.biz + '/stationOverview/getPowerAndIncome',
        socialContribution: main.serverUrl.biz + '/largeScreen/getSocialContribution',
        powerDistribution: main.serverUrl.biz + '/stationOverview/getStationDistribution',
        stationList: main.serverUrl.biz + '/stationOverview/getPowerStationList'
    };
    //单电站的url
    var oneStationURLs = {
        //获取功率情况,滚动数据的url
        runDataKpi: main.serverUrl.biz + '/singleStation/getSingleStationCommonData',
        //电站概况
        stationInfo: main.serverUrl.biz + '/singleStation/getSingleStationInfo',
        //功率曲线
        powerKpi: main.serverUrl.biz + '/singleStation/getSingleStationActivePower',
        //收益
        pwoerAndIncomeKpi: main.serverUrl.biz + '/singleStation/getSingleStationPowerAndIncome',
        //告警信息
        alarmKpi: main.serverUrl.biz + '/singleStation/getSingleStationAlarmStatistics',
        //天气的情况 当日和之后的3天的数据
        weathInfo: main.serverUrl.biz + '/weather/getWeatherDaily',
        //当前的实时温度
        currentWeather: main.serverUrl.biz + '/weather/getWeatherNow'
    }

    function myGetDayOfStr(str) {
        var d = new Date(str);
        return Msg.modules.home.weekday[d.getDay()] + ' ' + d.format('yyyy-MM-dd');
    }

    /**
     * 获取当前时间的下几天的数据
     * @param num 如1获取当前时间的下一天的时间
     * @returns {{wek: *, day: (*|String)}}
     */
    function getNextDayObj(str, num) {
        var d = new Date(str);
        d.setDate(d.getDate() + (num || 0));
        return {wek: Msg.modules.home.weekday[d.getDay()], 'day': d.format('MM-dd')};
    }

    var home = {
        Render: function (params) {
            if (params.stationCode) {//单电站,如果传递了电站编号
                showStationCode = params.stationCode;
                currentJWD = params.jwd || currentJWD;
                isMuti = false;
            } else {//多电站
                isMuti = true;
                showStationCode = false;
                currentJWD = '0:0';
            }
            home.powerMsg();
            home.kpiModules();
            home.initOneStation();//添加跳转到单电站的入口
        },
        //跳转到单电站相关数据的初始化化
        initOneStation: function () {//初始化化单电站

            function goToOneStationFomart(data) {//跳转到单电站的方法
                $('#home').parent().loadPage({
                    url: '/modules/home/index.html',
                    scripts: ['modules/home/index'],
                }, {stationCode: data.stationCode, jwd: (data.latitude || 0) + ':' + (data.longitude || 0)});
            }

            setTimeout(function () {//避免印象影响其他的模块执行，这里就执行一个定时器
                var stationDatas = [];//获取电站信息
                $.http.post(URLs.stationList, {}, function (res) {
                    if (res.success && res.data) {
                        stationDatas = res.data;
                    }
                }, function () {

                }, false);
                //请求电站数据,获取电站列表的信息
                var showListDiv = $('#page_main_station_list', '#home');
                var $seaLabel = $('<label/>').addClass('glyphicon glyphicon-search').addClass('station-search-label');
                var $search = $('<input placeholder="请输入电站名称" maxlength="32"/>').addClass('station-search-input');
                showListDiv.append($seaLabel.append($search));
                var $showStationDiv = $('<div/>');
                showListDiv.append($showStationDiv);
                //搜索功能
                $search.unbind('keyup').bind('keyup',function () {
                    var tmpShowData = [];
                    var tmVal = this.value;
                    if(!tmVal || !(tmVal = tmVal.trim())){//展示全部
                        tmpShowData = stationDatas;
                    }else{//根据关键字查询
                        for(var i=0;i<stationDatas.length;i++){
                            var tmpSt = stationDatas[i];
                            if(tmpSt.stationName && tmpSt.stationName.indexOf(tmVal)!=-1){
                                tmpShowData.push(tmpSt);
                            }
                        }
                    }
                    $showStationDiv.GridTableReload({data:tmpShowData})
                });
                $showStationDiv.GridTable({
                    title: false,
                    width: '0.06',
                    pageBar: false,
                    maxHeight: 600,
                    theme: 'ThemeA',
                    height: 'auto',
                    max_height: 600,
                    data: stationDatas,
                    isRecordSelected: true,
                    isSearchRecordSelected: false,
                    showSelectedName: false,
                    resizable: true,
                    objectsModel: true,
                    idProperty: 'id',
                    colModel: [
                        {
                            display: '电站状态',
                            name: 'stationCurrentState',
                            align: "center",
                            width: 0.3,
                            fnInit: function (element, value, datas) {
                                var dom = $("<img/>").css({'width': '26px', 'height': '26px'});
                                if (value == 3) {
                                    dom.attr('src', '../images/main/io/table/blue.png')
                                        .attr('title', '健康');
                                    element.html(dom);
                                    dom.parents('td').attr('title', '健康');
                                } else if (value == 2) {
                                    dom.attr('src', '../images/main/io/table/gray.png')
                                        .attr('title', '断连');
                                    element.html(dom);
                                    dom.parents('td').attr('title', '断连');
                                } else if (value == 1) {
                                    dom.attr('src', '../images/main/io/table/red.png')
                                        .attr('title', '故障');
                                    element.html(dom);
                                    dom.parents('td').attr('title', '故障');
                                }
                                //添加单击事件,他的祖先辈的tr添加点击事件
                                element.closest('tr').unbind('click').bind('click', function () {
                                    goToOneStationFomart(datas);
                                })
                            }
                        },
                        {
                            display: '电站名称',
                            name: 'stationName',
                            align: "center",
                            width: 0.7

                        }
                    ]
                });
            }, 1);
            //电站列表的点击事件
            $('#page_main_station_nav', '#home').unbind('click').bind('click', function (ev) {
                $('#page_main_station_list', '#home').toggleClass('station-list-show');
            });

        },
        /**
         * 统计数据展示
         */
        powerMsg: function () {
            //实时功率
            var activePower = $('#activePower').NumberRoll({
                dts: 2,
                value: 0
            });
            //当日发电量
            var dayCap = $('#dayCapacity').NumberRoll({
                dts: 2,
                value: 0
            });
            //月收益
            var dayIncome = $('#dayIncome').NumberRoll({
                dts: 2,
                value: 0
            });
            //年发电量
            var yearCap = $('#yearCap').NumberRoll({
                dts: 2,
                value: 0
            });
            //累计发电量
            var totalCap = $('#totalCapacity').NumberRoll({
                dts: 2,
                value: 0
            });
            var tmpPowerMsgUrl = isMuti && URLs.powerMsg || oneStationURLs.runDataKpi;//请求滚动数据的url
            var tmpParams = isMuti && {} || {stationCode: showStationCode};//请求参数
            $('.powerMsg').everyTimer('5s', function () {
                $.http.post(tmpPowerMsgUrl, tmpParams, function (resp) {
                    if (resp && resp.success) {
                        if (resp.data) {
                            var activePowerNum = resp.data.activePower || 0, dayCapNum = resp.data.dayCapacity || 0,
                                dayIncomeNum = resp.data.dayIncome || 0,
                                yearCapNum = resp.data.yearCap || 0,
                                totalCapNum = resp.data.totalCapacity || 0;
                            //实时功率
                            activePower.NumberRollRefresh({value: activePowerNum});
                            //当月发电量
                            dayCap.NumberRollRefresh({value: dayCapNum});
                            //月收益
                            dayIncome.NumberRollRefresh({value: dayIncomeNum});
                            //年发电量
                            yearCap.NumberRollRefresh({value: yearCapNum});
                            //累计发电量
                            totalCap.NumberRollRefresh({value: totalCapNum});
                        }
                    }
                });
            });
        },
        /**
         * kpi模块展示
         */
        kpiModules: function () {
            var kpiModules = {
                powerRank: {
                    id: 'kpi_power_rank',
                    title: Msg.modules.home.kpiModules.powerRank,
                    more: false,
                    content: 'powerRank'
                },
                powerState: {
                    id: 'kpi_power_state',
                    title: Msg.modules.home.kpiModules.powerState,
                    more: false,
                    content: 'powerState'
                },
                equipmentAlarm: {
                    id: 'kpi_equipment_alarm',
                    title: Msg.modules.home.kpiModules.equipmentAlarm,
                    more: false,
                    content: 'equipmentAlarm'
                },
                generatingIncome: {
                    id: 'kpi_generating_income',
                    title: Msg.modules.home.kpiModules.generatingIncome,
                    more: false,
                    content: 'generatingIncome'
                },
                socialContribution: {
                    id: 'kpi_social_contribution',
                    title: Msg.modules.home.kpiModules.socialContribution,
                    more: false,
                    content: 'socialContribution'
                },
                powerDistribution: {
                    id: 'kpi_power_distribution',
                    title: Msg.modules.home.kpiModules.powerDistribution,
                    more: false,
                    content: 'powerDistribution'
                },

                //单电站的信息
                getSingleStationInfo: {//电站概况
                    id: 'kpi_single_station',
                    title: '电站概况',
                    more: false,
                    content: 'kpiSingleStation'
                },
                getWeatherInfo: {
                    id: 'kpi_single_weather',
                    title: '天气预报',
                    more: false,
                    content: 'kpiSingleWeather'
                },
                getSingleStationActivePower: {
                    id: 'kpi_single_active_power',
                    title: '功率曲线',
                    more: false,
                    content: 'kpiSingleActivePower'
                }

            };

            var container = home.$get('.kpi');
            var showModules = ['powerRank', 'powerState', 'equipmentAlarm', 'generatingIncome', 'powerDistribution'];
            if (!isMuti) {
                showModules = ['getSingleStationInfo', 'getWeatherInfo', 'equipmentAlarm', 'generatingIncome', 'getSingleStationActivePower'];
            }
            for (var i = 0; i < showModules.length; i++) {
                var moduleName = showModules[i];
                var moduleSetting = kpiModules[moduleName];

                var module = $('<div>').attr('id', moduleSetting.id).addClass('kpiList clearfix');
                i % 3 !== 0 && module.addClass('kpi-item');
                moduleName === 'generatingIncome' && module.addClass('big');

                var title = $('<h2>').append($('<span>').addClass('title').text(moduleSetting.title));
                module.append(title);
                if (moduleSetting.more) {
                    var more = $('<a>').addClass('more').text('MORE');
                    module.append(more);
                }
                var content = $('<div>').attr('id', moduleSetting.id + '_content').addClass('content');
                module.append(content);
                home[moduleName](content);

                container.append(module);
            }
        },
        /**
         * 获取单电站的电站概况
         * @param content
         */
        getSingleStationInfo: function (content) {
            function createStationStations(value) {
                var msg = '';
                var dom = $("<img/>").css({'width': '26px', 'height': '26px'});
                if (value == 3) {
                    msg = '健康';
                    dom.attr('src', '../images/main/io/table/blue.png');
                } else if (value == 1) {
                    msg = '断连';
                    dom.attr('src', '../images/main/io/table/gray.png');
                } else if (value == 2) {
                    msg = '故障';
                    dom.attr('src', '../images/main/io/table/red.png');
                }
                return dom.attr('title', msg);
            }

            //加载数据
            function loadPageDatas(showData) {
                var $topDiv = $('<div/>').addClass('sigleStationInfo');
                content.append($topDiv);
                //图片的div
                var $leftDivForImg = $('<div/>').addClass('sigleStationInfo-img').append($('<img/>').attr('src', showData.stationFileId && (main.serverUrl.biz + "/fileManager/downloadFile?fileId=" + showData.stationFileId + "&time=" + new Date().getTime())
                    || '/images/main/home/stationImg.png'));
                $topDiv.append($leftDivForImg);
                //加载右边的电站详细介绍信息
                var $rightDivForDetail = $('<div/>').addClass('sigleStationInfo-detail');
                $topDiv.append($rightDivForDetail);
                var $h4 = $('<h4/>').html(showData.stationName).addClass('name-title');
                $rightDivForDetail.append($h4);
                var $ul = $('<ul/>');
                $ul.append($('<li/>').html('<span>电站地址：</span>' + (showData.stationAddr || '')));
                var tmpStatusDom = createStationStations(showData.stationStatus);
                $ul.append($('<li/>').html('<span>电站状态：</span>').append(tmpStatusDom).append(tmpStatusDom.attr('title')));
                $ul.append($('<li/>').html('<span>并网时间：</span>' + (showData.onlineTime && new Date(showData.onlineTime).format('yyyy-MM-dd') || '')));
                $ul.append($('<li/>').html('<span>运行天数：</span>' + (showData.runDays || 0)));
                $ul.append($('<li/>').html('<span>联系人员：</span>' + (showData.contactPeople || '')));
                $rightDivForDetail.append($ul);
                //底部显示的信息
                var $bottomDiv = $('<div/>').addClass('sigleStationCaptInfo');
                content.append($bottomDiv);
                $bottomDiv.append($('<div/>').html('发电量：').css('font-weight', 600));
                var $captDiv = $('<div/>').addClass('showCapty');
                $bottomDiv.append($captDiv);
                $captDiv.append($('<span/>').html('当日&nbsp;').append('<label>' + (showData.dayCapacity || '-') + '</label>').append(' kWh'))
                    .append($('<span/>').html('当月&nbsp;').append('<label>' + (showData.monthCap || '-') + '</label>').append(' kWh'))
                    .append($('<span/>').html('当年&nbsp;').append('<label>' + (showData.yearCap || '-') + '</label>').append(' kWh'));
            }

            //获取单电站的电站概况的数据
            $.http.post(oneStationURLs.stationInfo, {stationCode: showStationCode}, function (res) {
                if (res.success && res.data) {
                    loadPageDatas(res.data);
                }
            })

        },
        /**
         * 单电站的功率曲线的echart图形
         * @param content
         */
        getSingleStationActivePower: function (content) {
            // TODO 添加发电趋势数据请求
            $.http.post(oneStationURLs.powerKpi, {stationCode: showStationCode}, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    var yUnit = 'kW';
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
                    var dayActivePowerList = {};
                    var len = data && data.length || 0;
                    for (var i = 0; i < len; i++) {
                        var oneData = data[i];
                        dayActivePowerList[oneData.collectTime] = oneData.activePower;
                    }
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

                    ECharts.Render(content[0], {
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
                                height: 8,
                                fillerColor: 'rgba(30, 176, 91, 0.5)',
                                borderColor: '#2EA6F6',
                                handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                                handleSize: 20,
                                handleStyle: {
                                    color: '#1EB05B'
                                },
                                shadowBlur: 3,
                                shadowColor: 'rgba(0, 0, 0, 0.6)',
                                shadowOffsetX: 2,
                                shadowOffsetY: 2,
                                textStyle: {
                                    color: '#EEEEEE'
                                },
                                filterMode: 'empty',
                                realtime: false,
                                left: 35,
                                right: 20,
                                bottom: 10,
                                xAxisIndex: [0],
                                start: 25,
                                end: 75
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
         * 天气预报的信息
         * @param content
         */
        getWeatherInfo: function (content) {
            //添加表头
            var dayStr = new Date().format('yyyy-MM-dd');
            content.append($('<div/>').append('<div>' + myGetDayOfStr(dayStr) + ' (实时 <label>-</label>℃)</div>').addClass('weather-title'));
            var ycDivInfo = $('<div/>').addClass('weather-content');
            content.append(ycDivInfo);
            var todayDiv = $('<div/>').addClass('weather-today').append($('<div/>').html('-℃')).append($('<div/>').html('-')).append($('<div/>').html('-'));
            ycDivInfo.append(todayDiv);
            var nextDay = getNextDayObj(dayStr, 1);
            var nextDiv = $('<div/>').addClass('weather-next').append($('<div/>').html(nextDay.wek)).append($('<div/>').html(nextDay.day))
                .append($('<div/>').html('-℃')).append($('<div/>').html('-')).append($('<div/>').html('-'));
            ycDivInfo.append(nextDiv);
            var nnextDay = getNextDayObj(dayStr, 2);
            var nnextDiv = $('<div/>').addClass('weather-nnext').append($('<div/>').html(nnextDay.wek)).append($('<div/>').html(nnextDay.day))
                .append($('<div/>').html('-℃')).append($('<div/>').html('-')).append($('<div/>').html('-'));
            ycDivInfo.append(nnextDiv);
            var tmpParam = {'location': currentJWD};
            //获取实时温度值
            $.http.post(oneStationURLs.currentWeather, tmpParam, function (res) {
                if (res.success && res.data && res.data.results && res.data.results[0] && res.data.results[0].now) {
                    var tmData = res.data.results[0].now;
                    var lastUpdateStr = res.data.results[0].last_update &&
                        Date.parse(res.data.results[0].last_update, Msg.dateFormat.yyyymmdd)
                        //res.data.results[0].last_update.substring(0, 10)
                        || dayStr;
                    content.find('.weather-title').html('<div>' + myGetDayOfStr(lastUpdateStr) + ' (实时 <label>' + tmData.temperature + '</label>℃)</div>');
                }
            });
            $.http.post(oneStationURLs.weathInfo, tmpParam, function (res) {
                if (res.success && res.data && res.data.results && res.data.results[0] && res.data.results[0].daily) {
                    var weDatas = res.data.results[0].daily;
                    weDatas[0] && resetWeatherInof(todayDiv, weDatas[0]);
                    weDatas[1] && resetWeatherInof(nextDiv, weDatas[1]);
                    weDatas[2] && resetWeatherInof(nnextDiv, weDatas[2]);
                }
            });

            function resetWeatherInof(parentDiv, data) {
                var imgCode = data.code_day || '99';//默认是无数据
                if (parentDiv.hasClass('weather-today')) {//当天的数据
                    imgCode = 'url(/images/weather/weather180/' + imgCode + '.png';
                    var divs = parentDiv.children();
                    divs.eq(0).html((data.low || 0) + '~' + (data.high || 0) + '℃');
                    divs.eq(1).html(data.text_day || '-');
                    divs.eq(2).html(data.wind_direction || '-');
                } else {
                    imgCode = 'url(/images/weather/weather60/' + imgCode + '.png';
                    var tmpDayStr = getNextDayObj(data.date || dayStr);
                    var divs = parentDiv.children();
                    divs.eq(0).html(tmpDayStr.wek);
                    divs.eq(1).html(tmpDayStr.day);
                    divs.eq(2).html((data.low || 0) + '~' + (data.high || 0) + '℃');
                    divs.eq(3).html(data.text_day || '-');
                    divs.eq(4).html(data.wind_direction || '-');
                }
                parentDiv.css('background-image', imgCode);
            }

        },
        /**
         * 电站排名展示
         */
        powerRank: function (content) {
            var tabIds = ['kpi_powerRank_pr', 'kpi_powerRank_ph'];
            $.easyTabs(content, {
                tabIds: tabIds,
                tabNames: ['电站PR', '等效利用小时数'],
                change: function (body, tabId) {
                    var type = tabIds.indexOf(tabId);
                    if (type !== -1) {
                        body.append($('<div/>').attr('id', 'kpi_powerRank_chart').css({
                            width: '100%',
                            height: 230
                        }));
                        var url = URLs.powerRank_pr, mark = 'pr';
                        var tmpParams = {};//请求参数
                        if (type == 1) {
                            url = URLs.powerRank_ph;
                            mark = 'ppr';
                        }
                        $.http.post(url, tmpParams, function (resp) {
                            var data;
                            if (resp.success) {
                                data = resp.data;
                            }
                            powerRankChart(mark, data || {});
                        });
                    }
                }
            });

            /**
             * 电站排名chart
             */
            var powerRankChart = function (mark, data) {
                if (!data || data.length <= 0) {
                    return;
                }
                var arrName = [];
                var arrValue = [];
                var stackValue = [];
                var length;
                if (data && data.length > 0) {
                    length = data.length - 1;
                } else {
                    length = -1;
                }

                for (var i = length; i >= 0; i--) {
                    var item = data[i];
                    var stationName = '-', val = '-';
                    if (item) {
                        stationName = item.stationName;
                        val = item[mark];
                    }
                    arrName.push(stationName);
                    arrValue.push(parseFloat(val).fixed(3).toFixed(3));
                }
                for (i = 0; i <= length; i++) {
                    stackValue.push(mark == 'pr' ? 100 : arrValue.max());
                }
                var getId = document.getElementById('kpi_powerRank_chart');
                if (!getId) {
                    return;
                }
                var _option = {
                    noDataLoadingOption: {
                        text: Msg.noData,
                        effect: 'bubble',
                        effectOption: {
                            effect: {
                                n: 0 //气泡个数为0
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        formatter: function (p) {
                            var val = p[0].value;
                            return val + (mark == 'pr' ? '%' : ' h');
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
                    grid: {
                        show: true,
                        left: '28%',
                        top: '5%',
                        right: '15%',
                        borderWidth: 0,
                        height: '95%'
                    },
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
                    series: [
                        // {
                        //     type: 'bar',
                        //     silent: true,
                        //     barWidth: 14,
                        //     itemStyle: {
                        //         normal: {
                        //             color: '#232323',
                        //             barBorderRadius: 7
                        //         }
                        //     },
                        //     data: stackValue
                        // },
                        {
                            type: 'bar',
                            barWidth: 14,
                            barGap: '-100%',
                            itemStyle: {
                                normal: {
                                    color: '#33CCB8',
                                    barBorderRadius: 7,
                                    label: {
                                        show: true,
                                        position: 'right',
                                        formatter: function (p) {
                                            var val = p.value;
                                            return val + (mark == 'pr' ? '%' : ' h');
                                        },
                                        textStyle: {
                                            fontFamily: 'Microsoft Yahei',
                                            fontSize: 14,
                                            color: '#37D08F'
                                        }
                                    }
                                }
                            },
                            data: arrValue
                        }
                    ]
                };

                if ($(window).width() <= 1280) {
                    _option.yAxis[0].axisLabel.textStyle.fontSize = 10;
                    _option.series[0].itemStyle.normal.label.textStyle.fontSize = 10;
                    _option.grid.x = '34%';
                } else if ($(window).width() <= 1440) {
                    _option.yAxis[0].axisLabel.textStyle.fontSize = 12;
                    _option.series[0].itemStyle.normal.label.textStyle.fontSize = 12;
                }
                // 使用刚指定的配置项和数据显示图表。
                var _edzpm = ECharts.Render(getId, _option, false);
                _edzpm && _edzpm.on('mouseover', function (params) {
                    if (params.componentType == 'yAxis' && params.targetType == 'axisLabel') {
                        $(getId).attr('title', params.value);
                    } else {
                        $(getId).attr('title', "");
                    }
                });
                _edzpm && _edzpm.on('mouseout', function () {
                    $(getId).attr('title', "");
                });
            };
        },
        /**
         * 电站状态展示
         */
        powerState: function (content) {
            content.append($('<div/>').attr('id', 'kpi_powerState_chart').css({
                width: '100%',
                height: 280
            }));

            $.http.post(URLs.powerState, {}, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    powerStateChart(data);
                }
            });

            /**
             * 电站状态chart
             */
            var powerStateChart = function (data) {
                var getId = document.getElementById('kpi_powerState_chart');
                if (!getId) {
                    return;
                }
                var _normal, _trouble, _disconnected, _totalStation;
                if (data) {
                    _normal = data.health ? data.health : 0;
                    _trouble = data.trouble ? data.trouble : 0;
                    _disconnected = data.disconnected ? data.disconnected : 0;
                    _totalStation = parseInt(_normal) + parseInt(_trouble) + parseInt(_disconnected);
                } else {
                    _normal = 0;
                    _trouble = 0;
                    _disconnected = 0;
                    _totalStation = 0;
                }
                var _option = {
                    tooltip: {
                        show: true
                    },
                    title: {
                        text: _totalStation,
                        subtext: '电站总数',
                        left: '29.5%',
                        top: '36.5%',
                        containLabel: true,
                        textAlign: 'center',
                        textStyle: {
                            fontSize: 22,
                            color: '#0EA2CF',
                            fontWeight: 500
                        },
                        subtextStyle: {
                            fontSize: 16,
                            color: '#f6f6f6',
                            fontWeight: 300
                        }
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        left: '60%',
                        top: '28%',
                        itemHeight: 22,
                        formatter: function (name) {
                            var data = [
                                Msg.modules.home.powerState.normal,
                                Msg.modules.home.powerState.trouble,
                                Msg.modules.home.powerState.disconnected
                            ];
                            var i = data.indexOf(name);
                            if (i !== -1) {
                                var p = 0;
                                i === 0 && (p = (_normal / _totalStation));
                                i === 1 && (p = (_trouble / _totalStation));
                                i === 2 && (p = (_disconnected / _totalStation));
                                return name + ' (' + (p * 100).fixed(2) + ' %)'
                            }
                            return name;
                        },
                        textStyle: {
                            fontSize: 14,
                            color: '#F6F6F6',
                            fontWeight: 500
                        },
                        data: [
                            Msg.modules.home.powerState.normal,
                            Msg.modules.home.powerState.trouble,
                            Msg.modules.home.powerState.disconnected
                        ]
                    },
                    series: [
                        {
                            type: 'pie',
                            center: ['30%', '50%'],
                            radius: ['50%', '65%'],
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center',
                                    formatter: function (param) {
                                        if (param.dataIndex === 1)
                                            return Msg.modules.home.kpiModules.powerState;
                                        else return '';
                                    }
                                }
                            },
                            hoverAnimation: false,
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: false
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            },
                            data: [
                                {
                                    name: Msg.modules.home.powerState.normal,
                                    value: _normal,
                                    itemStyle: {
                                        normal: {
                                            color: '#34CC67'
                                        }
                                    }
                                },
                                {
                                    name: Msg.modules.home.powerState.trouble,
                                    value: _trouble,
                                    itemStyle: {
                                        normal: {
                                            color: '#FF3334'
                                        }
                                    }
                                },
                                {
                                    name: Msg.modules.home.powerState.disconnected,
                                    value: _disconnected,
                                    itemStyle: {
                                        normal: {
                                            color: '#999999'
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                };
                ECharts.Render(getId, _option, false);
            };
        },
        /**
         * 设备告警展示
         */
        equipmentAlarm: function (content) {
            content.append($('<div/>').attr('id', 'kpi_equipmentAlarm_chart').css({
                width: '100%',
                height: 280
            }));
            var tmpParms = {};
            var tmpAlramUrl = URLs.equipmentAlarm;
            if (!isMuti) {//单电站的告警统计
                tmpParms.stationCode = showStationCode;
                tmpAlramUrl = oneStationURLs.alarmKpi;
            }
            $.http.post(tmpAlramUrl, tmpParms, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    equipmentAlarmChart(data);
                }
            });

            /**
             * 设备告警chart
             */
            var equipmentAlarmChart = function (param) {
                var getId = document.getElementById('kpi_equipmentAlarm_chart');
                if (!getId) {
                    return;
                }
                var _serious, _major, _minor, _prompt, _total;
                if (param) {
                    _serious = param.serious ? param.serious : 0;
                    _major = param.major ? param.major : 0;
                    _minor = param.minor ? param.minor : 0;
                    _prompt = param.prompt ? param.prompt : 0;
                    _total = parseInt(_serious) + parseInt(_major) + parseInt(_minor) + parseInt(_prompt);
                } else {
                    _serious = 0;
                    _major = 0;
                    _minor = 0;
                    _prompt = 0;
                    _total = 0;
                }
                var _option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        left: '60%',
                        top: '28%',
                        itemHeight: 22,
                        formatter: function (name) {
                            var data = [
                                Msg.modules.home.alarmType.serious,
                                Msg.modules.home.alarmType.major,
                                Msg.modules.home.alarmType.minor,
                                Msg.modules.home.alarmType.prompt
                            ];
                            var i = data.indexOf(name);
                            if (i !== -1) {
                                var p = 0;
                                i === 0 && (p = _serious);
                                i === 1 && (p = _major);
                                i === 2 && (p = _minor);
                                i === 3 && (p = _prompt);

                                return name + ' (' + p + Msg.unit.alarmUnit + ')'
                            }
                            return name;
                        },
                        textStyle: {
                            fontSize: 14,
                            color: '#F6F6F6',
                            fontWeight: 500
                        },
                        data: [
                            Msg.modules.home.alarmType.serious,
                            Msg.modules.home.alarmType.major,
                            Msg.modules.home.alarmType.minor,
                            Msg.modules.home.alarmType.prompt
                        ]
                    },
                    series: [
                        {
                            type: 'pie',
                            radius: '22%',
                            silent: true,
                            z: 2,
                            label: {
                                normal: {
                                    show: false
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: '#ffffff',
                                    opacity: 0.9
                                }
                            },
                            data: ['']
                        },
                        {
                            type: 'pie',
                            radius: '90%',
                            silent: true,
                            itemStyle: {
                                normal: {
                                    color: new ECharts.echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0, color: '#8fe5fa' // 0% 处的颜色
                                    }, {
                                        offset: 0.5, color: '#fff' // 100% 处的颜色
                                    }, {
                                        offset: 1, color: '#8fe5fa' // 0% 处的颜色
                                    }], false)
                                }
                            },
                            data: ['']
                        },
                        {
                            type: 'pie',
                            roseType: "radius",
                            color: ['#ff0000', '#ff9933', '#ffcc33', '#cc99ff'],
                            stillShowZeroSum: false,
                            radius: '78%',
                            center: ['25%', '50%'],
                            z: 1,
                            label: {
                                normal: {
                                    show: _total !== 0,
                                    position: 'inner',
                                    formatter: function (a) {
                                        if (a.percent < 6) {
                                            return '';
                                        } else {
                                            return a.percent + "%";
                                        }
                                    },
                                    textStyle: {
                                        color: "#f6f6f6"
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data: [
                                {
                                    name: Msg.modules.home.alarmType.serious,
                                    value: _serious
                                },
                                {
                                    name: Msg.modules.home.alarmType.major,
                                    value: _major
                                },
                                {
                                    name: Msg.modules.home.alarmType.minor,
                                    value: _minor
                                },
                                {
                                    name: Msg.modules.home.alarmType.prompt,
                                    value: _prompt
                                }
                            ]
                        }
                    ]
                };
                ECharts.Render(getId, _option, false);
            };
        },
        /**
         * 发电量收益展示
         */
        generatingIncome: function (content) {
            var tabIds = ['kpi_generatingIncome_day', 'kpi_generatingIncome_month', 'kpi_generatingIncome_year', 'kpi_generatingIncome_lifecycle'];
            $.easyTabs(content, {
                tabIds: tabIds,
                tabNames: ['月', '年', '寿命期'],
                change: function (body, tabId) {
                    var type = tabIds.indexOf(tabId);
                    if (type !== -1) {
                        body.append($('<div/>').attr('id', 'kpi_generatingIncome_chart').css({
                            width: '100%',
                            height: 230
                        }));
                        var queryType = 'month';
                        type == 0 && (queryType = 'month');
                        type == 1 && (queryType = 'year');
                        type == 2 && (queryType = 'allYear');
                        var tmpParams = {queryType: queryType};
                        var incomUrl = URLs.generatingIncome;
                        if (!isMuti) {
                            tmpParams.stationCode = showStationCode;
                            incomUrl = oneStationURLs.pwoerAndIncomeKpi;
                        }
                        $.http.post(incomUrl, tmpParams, function (resp) {
                            var data;
                            if (resp.success) {
                                data = resp.data;
                            }
                            generatingIncomeChart(type, data || {});
                        });
                    }
                }
            });

            /**
             * 发电量收益chart
             */
            var generatingIncomeChart = function (type, data) {
                var getId = document.getElementById('kpi_generatingIncome_chart');
                if (!getId) {
                    return;
                }
                var incomeList = {},
                    powerList = {};
                var len = data && data.length || 0;
                for (var i = 0; i < len; i++) {
                    var oneData = data[i];
                    incomeList[oneData.collectTime] = oneData.powerProfit;
                    powerList[oneData.collectTime] = oneData.producePower;
                }
                var tooltips;
                var yUnit = 'kWh', y2Unit = Msg.unit.RMBUnit;
                var xData = [], yData = [], y2Data = [];

                if (type == 0) {//月
                    var monthDays = Date.parse().getDaysInMonth();
                    for (var i = 0; i < monthDays; i++) {
                        var x = i + 1 < 10 ? "0" + (i + 1) : "" + (i + 1);
                        xData.push(x);
                        var xTime = Date.parseTime(Date.parse().format('yyyy-MM') + '-' + x);
                        yData.push((powerList && powerList[xTime]) || '-');
                        y2Data.push((incomeList && incomeList[xTime]) || '-');
                    }
                } else if (type == 1) {//年
                    for (var i = 0; i < 12; i++) {
                        var x = i + 1 < 10 ? "0" + (i + 1) : "" + (i + 1);
                        xData.push(x);
                        var xTime = Date.parseTime(Date.parse().format('yyyy') + '-' + x);
                        yData.push((powerList && powerList[xTime]) || '-');
                        y2Data.push((incomeList && incomeList[xTime]) || '-');
                    }
                } else if (type == 2) {//寿命期
                    var lifeList = [];
                    for (var year in powerList) {
                        lifeList.push(year);
                    }
                    for (var year in incomeList) {
                        lifeList.push(year);
                    }
                    var startYear = +(Date.parse(lifeList.min()).format('yyyy'));//开始的年
                    var endYear = +(Date.parse().format('yyyy'));//结束的年当前时间所在的年
                    for (var i = startYear; i <= endYear; i++) {
                        xData.push(i);
                        var xTime = Date.parseTime(i, 'yyyy');
                        yData.push((powerList && powerList[xTime]) || '-');
                        y2Data.push((incomeList && incomeList[xTime]) || '-');
                    }
                }

                var yMax = yData.removeAll('-').max();
                var curLang = (main.Lang + "_" + (main.region == 'GB' ? 'UK' : main.region));
                var splitNum = 1000;
                if ('zh_CN' == curLang) {
                    splitNum = 10000;
                }
                if (yMax > splitNum) {
                    if ('zh_CN' == curLang) {
                        yUnit = Msg.unit.WKWh;
                    } else {
                        yUnit = 'MWh';
                    }

                    for (var j = 0; j < yData.length; j++) {
                        if (!isNaN(yData[j])) {
                            yData[j] = (parseFloat(yData[j]) / splitNum).fixed(3);
                        }
                    }
                }
                var value;
                var length = yData.length;
                for (var k = 0; k < length; k++) {
                    value = yData[k];
                    if (!isNaN(value)) {
                        yData[k] = parseFloat(value).fixed(3);
                    }
                }

                for (var n = 0; n < y2Data.length; n++) {
                    if (!isNaN(y2Data[n])) {
                        y2Data[n] = parseFloat(y2Data[n]).fixed(2);
                    }
                }
                y2Unit = App.getCurrencyUnit();

                length = y2Data.length;
                for (var m = 0; m < length; m++) {
                    value = y2Data[m];
                    if (!isNaN(value)) {
                        y2Data[m] = parseFloat(value).fixed(3);
                    }
                }

                if (type == 0) {
                    tooltips = function (p) {
                        var x = p[0].name;
                        var result = Date.parse().format('yyyy-MM') + '-' + x + '<br>';

                        result += p[0].seriesName + ' : ' + (+p[0].value).fixed(3).format() + ' ' + yUnit + '<br>';
                        result += p[1].seriesName + ' : ' + y2Unit + ' ' + (+p[1].value).fixed(3).format();

                        return result;
                    }
                } else if (type == 1) {
                    tooltips = function (p) {
                        var x = p[0].name;
                        var result = Date.parse().format('yyyy') + '-' + x + '<br>';

                        result += p[0].seriesName + ' : ' + (+p[0].value).fixed(3).format() + ' ' + yUnit + '<br>';
                        result += p[1].seriesName + ' : ' + y2Unit + ' ' + (+p[1].value).fixed(3).format();

                        return result;
                    }
                } else if (type == 2) {
                    tooltips = function (p) {
                        var x = p[0].name;
                        var result = Date.parse().format('yyyy-MM') + '-' + x + '<br>';

                        result += p[0].seriesName + ' : ' + (+p[0].value).fixed(3).format() + ' ' + yUnit + '<br>';
                        result += p[1].seriesName + ' : ' + y2Unit + ' ' + (+p[1].value).fixed(3).format();

                        return result;
                    }
                }

                var _option = {
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
                        },
                        formatter: tooltips
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
                        bottom: 20,
                        top: '15%',
                        containLabel: true,
                        borderColor: '#EEEEEE'
                    },
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
                            barWidth: 18,
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
                };

                ECharts.Render(getId, _option, true);
            };
        },
        /**
         * 电站分布展示
         */
        powerDistribution: function (content) {
            content.append($('<div/>').attr('id', 'kpi_powerDistribution_chart').css({
                width: '100%',
                height: 280
            }));

            var params = {};
            $.http.post(URLs.powerDistribution, params, function (resp) {
                var data = resp.data;
                powerDistributionChart(data);
            });

            /**
             * 电站分布chart
             */
            var powerDistributionChart = function (data) {
                var getId = document.getElementById('kpi_powerDistribution_chart');
                if (!getId) {
                    return;
                }

                var _seriesData = [], _categories = [], _total = 0, _len = 0;
                if (data) {
                    _total = data.stationCount;
                    if (_total > 0 && data.stationDist && data.stationDist.length > 0) {
                        _len = data.stationDist.length;
                        _total = 0;
                        for (var i in data.stationDist) {
                            if (data.stationDist.hasOwnProperty(i)) {
                                _total += +data.stationDist[i].value;
                            }
                        }
                        for (var i in data.stationDist) {
                            if (data.stationDist.hasOwnProperty(i)) {
                                var item = data.stationDist[i];
                                _seriesData.push({
                                    "name": item.name,
                                    "value": item.value,
                                    "symbolSize": item.value * 45 * _len / _total,
                                    "category": item.name,
                                    "draggable": "true"
                                });
                                _categories.push({name: item.name});
                            }
                        }
                    }
                }

                var _option = {
                    color: [
                        '#ff9933', '#04a09a', '#649cd5', '#ffcc33', '#ea4e59', '#6d799c',
                        '#1790cf', '#1bb2d8', '#99d2dd', '#88b0bb', '#1c7099', '#038cc4', '#75abd0', '#afd6dd'
                    ],
                    tooltip: {
                        formatter: function (params) {
                            return params.seriesName + '</br>' + params.marker
                                + params.name + ' : ' + params.value + ' ' + Msg.unit.installCapacityUnit;
                        }
                    },
                    animationDuration: 3000,
                    animationEasingUpdate: 'quinticInOut',
                    series: [{
                        name: '电站分布',
                        type: 'graph',
                        // type: 'wordcloud',
                        layout: 'force',
                        force: {
                            initLayout: 'circular',
                            repulsion: 100, //_len && _total / _len,
                            gravity: 0.1,
                            edgeLength: [2, 10]
                        },
                        focusNodeAdjacency: true,
                        roam: true,
                        // roam: 'move',
                        // roam: 'scale',
                        label: {
                            normal: {
                                show: true
                            }
                        },
                        lineStyle: {
                            normal: {
                                show: false
                            }
                        },
                        data: _seriesData,
                        categories: _categories
                    }]
                };
                ECharts.Render(getId, _option, false);
            };
        },
        /**
         * 社会贡献展示
         */
        socialContribution: function (content) {
            var container = $('<div/>').addClass('socialContribution').css({
                width: '100%',
                height: 280
            });

            container.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title'))
                .append($('<li>').addClass('t_savedCoal')
                    .append('<img src="/images/main/home/home_envi_mei.png" alt="">')
                    .append($('<span>').html(Msg.modules.home.socialContribution.saveStandardCoal + Msg.unit.brackets[0] + "<i>" + Msg.unit.coalUnit + "</i>" + Msg.unit.brackets[1])))
                .append($('<li>').addClass('t_carbonEmissions')
                    .append('<img src="/images/main/home/home_envi_co2.png" alt="">')
                    .append($('<span>').html(Msg.modules.home.socialContribution.carbonDioxideReduction + Msg.unit.brackets[0] + "<i>" + Msg.unit.co2Unit + "</i>" + Msg.unit.brackets[1])))
                .append($('<li>').addClass('t_deforestation')
                    .append('<img src="/images/main/home/home_envi_senlin.png" alt="">')
                    .append($('<span>').html(Msg.modules.home.socialContribution.reduceDeforestation + Msg.unit.brackets[0] + "<i>" + Msg.unit.tree_unit + "</i>" + Msg.unit.brackets[1])))
            );

            container.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title').text(Msg.modules.home.socialContribution.year))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_savedCoal').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_carbonEmissions').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('year_deforestation').text(0)))
            );
            container.append($('<ul>')
                .append($('<li>').addClass('socialContribution-title').text(Msg.modules.home.socialContribution.cumulative))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_savedCoal').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_carbonEmissions').text(0)))
                .append($('<li>').addClass('socialContribution-value').append($('<span/>').addClass('accumulativeTotal_deforestation').text(0)))
            );

            content.append(container);

            $.http.post(URLs.socialContribution, {}, function (resp) {
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

        $get: function (selector) {
            var ele = $('#home');
            return selector ? $(selector, ele) : ele;
        }
    };

    return home;
});