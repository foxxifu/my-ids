'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'stationPm',
    description: '模块功能：智能运维——智能告警',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'Timer', 'ECharts', 'modules/io/iocommon'
    ]
});

App.Module('stationPm', function ($) {
    var timeDim = 'day';//时间的纬度  day:日,month：月, year：年  默认是日，也是给echart图的多电站/但电站的时候使用
    var quereObjects = {};//查询数据的对象，主要是给导出功能使用
    var isMutiStation = true; // 是否是多电站
    var serviceLastTime; // 获取的服务器的前一天的时间
    var oneStationName = ''; // 单电站的时候的名称
    function reInitParams() {//每次render的时候重新出山参数,这个是由于每次加载之后如果重新进入这些参数不会重新初始化，可能导致传递的参数错误
        timeDim = 'day';
        quereObjects = {};
        oneStationName = '';
        isMutiStation = true;
        // 请求当前的上一天的时间
        $.http.post( main.serverUrl.biz + '/produceMng/getYestodayTime', {}, function (res) {
            if(res.code == 1){
                serviceLastTime = res.results;
            }
        },null,false);//同步获取时间
    }
    function getMaxDate() { // 获取最大的时间
        if(timeDim === 'day'){
            return new Date(serviceLastTime).format('yyyy-MM-dd');
        }
        if(timeDim === 'month'){
            return new Date(serviceLastTime).format('yyyy-MM');
        }
        return new Date(serviceLastTime).format('yyyy');
    }
    function getShowDate() { // 获取时间
        if(timeDim === 'day'){
            return new Date(quereObjects.time || serviceLastTime).format('yyyy年MM月dd日');
        }
        if(timeDim === 'month'){
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
            //4.初始化按钮事件
            //this.initEvent();
        },
        /**
         * 初始搜索栏的组件
         */
        initSearchBar: function () {
            var self = this;

            //查询数据
            function search(data) {
                var stationCodes = $('#pm_station_search_bar input[name=stationCodes]').data('pm_staion_codes');
                data.params.stationCodes = stationCodes;
                timeDim = $('#pm_station_search_bar select[name=timeDim]').val();// 时间纬度
                data.params.timeDim = timeDim;
                var tmpTime = $('#pm_station_search_bar input[name=time]').val();
                if (tmpTime && tmpTime != '') {
                    data.params.time = IoUtil.getCurentTimeOfStr(tmpTime, timeDim);
                } else {//解决问题单 16 选择统计维度切换后传入后台时间为原时间维度的时间
                    data.params.time = null;
                }
                quereObjects = data.params;
                data.params['index'] = 1;
                data.params['pageSize'] = $('#io_task_defect_list select.pselect').val();
                // $('#pm_station_list').GridTableReload(data);
                self.initResult();
            }

            //初始事件控件
            function dateInit(element, value, data) {
                element.val(new Date(serviceLastTime).format('yyyy-MM-dd'));
                element.focus(function () {//根据选择的纬度确定可以选择的时间值
                    var tmpDim = $('#pm_station_search_bar select[name=timeDim]').val()
                    var dateFmt = 'yyyy-MM-dd';
                    if (tmpDim == 'month') {
                        dateFmt = 'yyyy-MM';
                    } else if (tmpDim == 'year') {
                        dateFmt = 'yyyy';
                    }
                    DatePicker({dateFmt: dateFmt, el: this, isShowOK: true, isShowClear: true,maxDate:getMaxDate()});
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

            $('#pm_station_search_bar').ValidateForm('pm_station_search_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//电站名称
                        name: 'stationCodes',
                        fnInit: function (element, value) {//TODO 将选择的电站的stationCode显示出来
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
                            element.off('change').on('change', function () {
                                var $this = $(this);
                                setTimeout(function () {
                                    var fmt;
                                    if($this.val()==='day'){
                                        fmt = 'yyyy-MM-dd';
                                    } else if($this.val()==='month'){
                                        fmt = 'yyyy-MM'
                                    }else{
                                        fmt = 'yyyy'
                                    }
                                    $('#pm_station_search_bar input[name=time]').val(new Date(serviceLastTime).format(fmt));
                                },0);
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
            var dataAxis = [
                "00"
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
            var data1 = [
                0
                // 250,300,250,300,240,230,250,260,300,300,
                // 250,300,250,300,240,230,250,260,300,300,
                // 250,300,250,300,240,230,250,260,300,300,
                // 280
            ]
            var data2 = [
                0
                // 310,350,300,320,400,360,330,340,320,280,
                // 310,350,300,320,400,360,330,340,320,280,
                // 310,350,300,320,400,360,330,340,320,380,
                // 420
            ]

            self.myEachOpions.xAxis[0].data = tmpXdatas;
            self.myEachOpions.series[0].data = data1;
            self.myEachOpions.series[1].data = data2;
            ECharts.Render('pm_station_echart', self.myEachOpions);
        },
        initListHeard: function () {//初始集合的表头
            var $head = $('#pm_station_list_head');
            $head.empty();
            var str ;
            if(isMutiStation){
                str = getShowDate()+'发电收益报表';
            } else {
                str = oneStationName + '发电收益报表-' + getShowDate();
            }
            var $title = $('<div class="pm_title_head">' + str + '</div>');
            $head.append($title);
            var $btn = $('<div class="pm_title_btn"><input type="button" class="btn" value="导出"></div>');
            $head.append($btn);
            $btn.unbind('click').bind('click', function () {
                App.confirm('确认导出当前数据？', function () {
                    var tmpStr = 'timeDim=' + quereObjects.timeDim;
                    if (quereObjects.time) {
                        tmpStr += '&time=' + quereObjects.time;
                    }
                    if (quereObjects.stationCodes) {
                        tmpStr += '&stationCodes=' + quereObjects.stationCodes;
                    }
                    window.location.href = (main.serverUrl.biz + '/produceMng/report/download/StationRuning?' + tmpStr);
                });
            });
        },
        myTimeDim: function(element,value){
            if(value && !isNaN(value)) {
                var date = new Date(+value);
                var fmt;
                if(timeDim === 'day'){
                    if(!isMutiStation){
                        fmt = 'yyyy-MM-dd hh:mm';
                    } else {
                        fmt = 'yyyy-MM-dd';
                    }
                }else if(timeDim === 'month'){
                    if(!isMutiStation){
                        fmt = 'yyyy-MM-dd';
                    } else {
                        fmt = 'yyyy-MM';
                    }
                } else{
                    if(!isMutiStation){
                        fmt = 'yyyy-MM';
                    } else {
                        fmt = 'yyyy';
                    }
                }
                var str = date.format(fmt);
                element.text(str).attr('title',str).parent().attr('title',str);
            }
        },
        /**
         * 获取表格的数据
         * @param params
         */
        initResult: function () {
            if(!quereObjects.timeDim) {
                quereObjects.timeDim = timeDim;
                quereObjects.time = serviceLastTime;
            }
            var self = this;
            // 设置绿色字体
            function getBlueColor(element, value, data) {
                element.addClass('io_green_color');
            }
            // 请求数据，确定是单电站 还是多电站
            var url = main.serverUrl.biz + '/produceMng/listStationRpt';
            $.http.post(url, quereObjects, function (res) {
                var message = res.message;
                if(message){
                    isMutiStation = message != 1;
                }
                if(!isMutiStation){
                    var datas = res.results && res.results.list || [];
                    if(datas.length>0){
                        oneStationName = datas[0].stationName;
                    }
                }else {
                    oneStationName = '';
                }
            }, null, false);
            this.initListHeard();
            var colModel;
            if (!isMutiStation && timeDim === 'day') { // 单电站 并且纬度是日
                colModel = [
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.25,
                        fnInit: getBlueColor
                    },
                    {
                        display: '采集时间',
                        name: 'collectTime',
                        width: 0.15,
                        fnInit: self.myTimeDim
                    },
                    {
                        display: '辐照量(MJ/㎡)',
                        name: 'radiationIntensity',
                        //align: "center",
                        width: 0.15,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display: '理论发电量(kW·h)',
                        name: 'theoryPower',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display: '发电量(kW·h)',
                        name: 'productPower',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display: '收益(元)',
                        name: 'inCome',
                        // width: 0.15,
                    },

                ]
            } else if(isMutiStation){
                colModel = [
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.22,
                        fnInit: getBlueColor
                    },
                    {
                        display: '辐照量(MJ/㎡)',
                        name: 'radiationIntensity',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display: '等效利用小时数(h)',
                        name: 'equivalentHour',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: 'PR(%)',
                        name: 'pr',
                        //align: "center",
                        width: 0.06,
                    },
                    {
                        display: '发电量(kW·h)',
                        name: 'productPower',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '上网电量(kW·h)',
                        name: 'ongridPower',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '自用电量(kW·h)',
                        name: 'selfUsePower',
                        width: 0.08,
                        // fnInit: getMyTime
                    },
                    {
                        display: '自发自用率(%)',
                        name: 'selfUseRatio',
                        width: 0.08,
                        // fnInit: getMyTime
                    },
                    {
                        display: '收益(元)',
                        name: 'inCome'
                    }
                ]
            } else { // 单电站的月 和 年
                colModel = [
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.2,
                        fnInit: getBlueColor
                    },
                    {
                        display: '采集时间',
                        name: 'collectTime',
                        width: 0.1,
                        fnInit: self.myTimeDim
                    },
                    {
                        display: '辐照量(MJ/㎡)',
                        name: 'radiationIntensity',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display: '等效利用小时数(h)',
                        name: 'equivalentHour',
                        //align: "center",
                        width: 0.08,
                    },
                    {
                        display: 'PR(%)',
                        name: 'pr',
                        //align: "center",
                        width: 0.06,
                    },
                    {
                        display: '发电量(kW·h)',
                        name: 'productPower',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '上网电量(kW·h)',
                        name: 'ongridPower',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '自用电量(kW·h)',
                        name: 'selfUsePower',
                        width: 0.08,
                        // fnInit: getMyTime
                    },
                    {
                        display: '自发自用率(%)',
                        name: 'selfUseRatio',
                        width: 0.08,
                        // fnInit: getMyTime
                    },
                    {
                        display: '收益(元)',
                        name: 'inCome',
                    }

                ]
            }
            $('#pm_station_list').GridTable({
                url: url,
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 280,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:280,
                params: quereObjects,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                //idProperty: 'id',
                onLoadReady:function (crentPageData) {//数据加载好之后去加载echart的数据,data是查询到了的数据
                    //x轴的数据
                    var tmpXdatas = [];//x轴的数据
                    var data1 = [];//y1轴的数据
                    var data2 = [];//y2轴的数据
                    var powerMData = {min:0,max:0};
                    var incomMData = {min:0,max:0};
                    if(crentPageData && crentPageData.length>0){
                        var len = crentPageData.length;
                        for(var i=0;i<len;i++){
                            var tmData = crentPageData[i];
                            if(isMutiStation){//如果是多电站的就添加电站名称
                                tmpXdatas.push(tmData.stationName)
                            }else{//如果是单电站的就添加时间
                                tmpXdatas.push(IoUtil.getCurrentDateVimAfterForm(tmData.collectTime,timeDim));
                            }
                            if(tmData.productPower){
                                powerMData.min===undefined && (powerMData.min = tmData.productPower*0.8);
                                if(powerMData.min>tmData.productPower*0.8){
                                    powerMData.min = (tmData.productPower*0.8).fixed(2);
                                }
                                if(powerMData.max<tmData.productPower*1.2){
                                    powerMData.max = (tmData.productPower*1.2).fixed(2);
                                }
                            }
                            if(tmData.inCome){
                                incomMData.min===undefined && (incomMData.min = tmData.inCome*0.8);
                                if(incomMData.min>tmData.inCome*0.8){
                                    incomMData.min = (tmData.inCome*0.8).fixed(2);
                                }
                                if(incomMData.max<tmData.inCome*1.2){
                                    incomMData.max = (tmData.inCome*1.2).fixed(2);
                                }
                            }

                            data1.push(tmData.productPower || '-');//发电量
                            data2.push(tmData.inCome || '-');//收益
                        }
                    }
                    //设置最值
                    self.myEachOpions.yAxis[0].min = powerMData.min || 0;
                    self.myEachOpions.yAxis[0].max = powerMData.max || 10;
                    self.myEachOpions.yAxis[1].min = incomMData.min || 0;
                    self.myEachOpions.yAxis[1].max = incomMData.max || 10;
                    //设置坐标轴的数据
                    self.myEachOpions.xAxis[0].data = tmpXdatas;
                    self.myEachOpions.series[0].data = data1;
                    self.myEachOpions.series[1].data = data2;
                    ECharts.Render('pm_station_echart', self.myEachOpions,false);
                },
                loadError:function () {
                    ECharts.Render('pm_station_echart', self.myEachOpions,false);
                },
                colModel:colModel
            });
        },
        /**
         * 初始化按钮事件
         */
        // initEvent:function () {
        //     //导出按钮的事件
        //     $('#pm_station_btns input[type=button]').unbind('click').bind('click',function () {
        //         App.confirm('确认导出当前数据？',function () {
        //             var tmpStr = 'timeDim='+quereObjects.timeDim;
        //             if(quereObjects.time){
        //                 tmpStr += '&time='+quereObjects.time;
        //             }
        //             if(quereObjects.stationCodes){
        //                 tmpStr += '&stationCodes='+quereObjects.stationCodes;
        //             }
        //             window.location.href = (main.serverUrl.biz + '/produceMng/report/download/StationRuning?'+tmpStr);
        //         });
        //     });
        // },
        /**
         * eachart 的初始数据
         */
        myEachOpions: {
            color: ['red'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'cross'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['发电量(kWh)', '收益(万元)'],
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
                },
                {
                    type: 'value',
                    name: '收益(万元)',
                    splitLine: {show: false},//去除网格线
                    min: 0,
                    max: 500,
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}'
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#288297',//设置坐标轴的颜色
                            width: 2,//这里是为了突出显示加上的
                        }
                    }
                },
            ],
            series: [
                {
                    name: '发电量(kWh)',
                    type: 'bar',
                    barWidth: 15,
                    yAxis: 1,
                    itemStyle: {
                        normal: {
                            color: '#1eb05b',
                            barBorderRadius: [5, 5, 0, 0]//设置圆角
                        }
                    },
                    data: []
                },
                {
                    name: '收益(万元)',
                    type: 'line',
                    symbol: 'circle',
                    symbolSize: 8,
                    yAxisIndex: 1,
                    itemStyle: {normal: {color: '#1191f2'}},
                    data: []
                }

            ]
        }
    }
});

