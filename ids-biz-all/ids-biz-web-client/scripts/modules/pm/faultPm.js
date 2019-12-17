'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'faultPm',
    description: '模块功能：故障分析',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Timer','ECharts','modules/io/iocommon'
    ]
});

App.Module('faultPm',function($){
    var isMuttyStations = true;//默认是多电站
    var timeDim = 'day';//时间的纬度  day:日,month：月, year：年  默认是日，也是给echart图的多电站/但电站的时候使用
    var quereObjects = {};
    function reInitParams() {//每次render的时候重新初始化参数,这个是由于每次加载之后如果重新进入这些参数不会重新初始化，可能导致传递的参数错误
        isMuttyStations = true;
        timeDim = 'day';
        quereObjects = {};
    }
    return {
        Render:function(params){
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
        initSearchBar:function(){
            //查询数据
            function search(data){
                var stationCodes = $('#pm_fault_search_bar input[name=stationCodes]').data('pm_staion_codes');
                if(!stationCodes || stationCodes=='' || stationCodes.indexOf(',')>0){//多电站
                    isMuttyStations = true;
                    data.params.stationCodes = stationCodes;//list
                }else{//单电站
                    isMuttyStations = false;//修改为单电站的模式
                    data.params.stationCodes = stationCodes;
                }
                data.params.deviceTypeId = $('#pm_fault_search_bar select[name=deviceTypeId]').val();
                timeDim = $('#pm_fault_search_bar select[name=timeDim]').val();// 时间纬度
                data.params.timeDim = timeDim;
                var tmpTime = $('#pm_fault_search_bar input[name=time]').val();
                if(tmpTime && tmpTime!=''){
                    data.params.time = IoUtil.getCurentTimeOfStr(tmpTime,timeDim);
                }else{//解决问题单 16 选择统计维度切换后传入后台时间为原时间维度的时间
                    data.params.time = null;
                }
                $('#pm_fault_list_head div.pm_title_head').html('设备{0}故障报表'.replace('{0}',IoUtil.getInfoOfDim(timeDim)));
                quereObjects = data.params;
                data.params['index'] = 1;
                data.params['pageSize'] = $('#pm_fault_search_bar select.pselect').val();
                $('#pm_fault_list').GridTableReload(data);//重新加载数据
            }
            //初始事件控件
            function dateInit(element,value,data){
                element.focus(function(){
                    var tmpDim = $('#pm_fault_search_bar select[name=timeDim]').val();//根据选择的确认当前是什么时间维度
                    var dateFmt = 'yyyy-MM-dd';
                    if(tmpDim === 'month'){
                        dateFmt = 'yyyy-MM';
                    }else if(tmpDim === 'year'){
                        dateFmt = 'yyyy';
                    }
                    DatePicker({dateFmt: dateFmt,el:this,isShowOK:true,isShowClear:true});
                });
            }
            //获取选中的对象
            function getSelectsStations(element){
                var stationCode = element.data('pm_staion_codes');
                if(!stationCode){
                    return false;
                }
                //var tmpArr = stationCode.split('@');
                if(stationCode.length>0){
                    var tmpStationNameArr = element.val().split(',');
                    var resluts = [];
                    for(var i=0;i<stationCode.length;i++){
                        resluts.push({stationCode:stationCode[i] ,stationName:tmpStationNameArr[i]});
                    }
                    return resluts;
                }else{
                    return false;
                }
            }
            $('#pm_fault_search_bar').ValidateForm('pm_fault_search_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//电站名称
                        name: 'stationCodes',
                        fnInit:function (element) {
                            element.unbind('focus').bind('focus',function () {
                                IoUtil.showStationDialog(function (records) {
                                    var len = records.length;


                                    if(len == 0){
                                        element.val('');
                                        element.removeAttr('title');
                                        element.removeData('pm_staion_codes');
                                    }else{
                                        var scArr = [];
                                        scArr.push(records[0].stationCode);
                                        var showText = records[0].stationName;
                                        for(var i=1;i<len;i++){
                                            scArr.push(records[i].stationCode);
                                            showText += ','+records[i].stationName;
                                        }
                                        element.val(showText).attr('title',showText).data('pm_staion_codes',scArr);//存放信息
                                    }
                                },getSelectsStations(element));
                            });
                        }
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '设备类型',//设备名称
                        name: 'deviceTypeId',
                        options:[
                            {value:-1, text:Msg.all},
                        ],
                        fnInit:function (element,value,data) {
                            element.empty();
                            //查询设备类型
                            $.http.get(main.serverUrl.dev+'/settings/query',function (res) {
                                var devTypes2 = [];
                                devTypes2.push({value:-1,text:'请选择设备类型'});
                                element.append($('<option/>').val('-1').text(Msg.all))
                                if(res.success){
                                    var devtypes = res.results;
                                    for(var key in devtypes){
                                        element.append($('<option/>').val(devtypes[key]).text(key));
                                    }
                                }
                                //查询成功后去执行这个方法,不管是否有值都需要去执行这个
                            });
                        }
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '统计维度',//处理结果
                        name: 'timeDim',
                        options:[
                            {value:'day', text:'日'},
                            {value:'month', text:'月'},
                            {value:'year', text:'年'},
                        ],
                        fnInit:function (element) {
                            element.unbind('change').bind('change',function () {
                                $('#pm_fault_search_bar input[name=time]').val('');//清空时间数据，避免发出错误的时间查询
                            });
                        }
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '发生时间',//处理结果
                        name: 'time',
                        width:181,
                        extend: {class:'Wdate'},
                        fnInit:dateInit,
                    }
                ]],
                extraButtons:[
                    {
                        input: 'button',
                        type: 'button',
                        show: '',
                        name: '',
                        fnClick:function(e){
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
        initPowersOfEchart:function(){
            var self = this;
            ECharts.Render('pm_fault_echart', self.myEachOpions);
        },
        initListHeard:function(){//初始集合的表头
           var $head =  $('#pm_fault_list_head');
           $head.empty();
           var $title =$('<div class="pm_title_head">'+'设备{0}故障报表'.replace('{0}',IoUtil.getInfoOfDim(timeDim))+'</div>');
           $head.append($title);
           var $btn = $('<div class="pm_title_btn"><input type="button" class="btn" value="导出"></div>');
            $head.append($btn);
            //导出按钮的事件
            $btn.unbind('click').bind('click',function () {
                App.confirm('确认导出当前数据？',function () {
                    var tmpStr = 'timeDim='+quereObjects.timeDim;
                    if(quereObjects.time){
                        tmpStr += '&time='+quereObjects.time;
                    }
                    if(quereObjects.stationCodes){
                        tmpStr += '&stationCodes='+quereObjects.stationCodes;
                    }
                    if(quereObjects.deviceTypeId && quereObjects.deviceTypeId!='-1'){
                        tmpStr += 'deviceTypeId='+quereObjects.deviceTypeId;
                    }
                    window.location.href = (main.serverUrl.biz + '/produceMng/report/download/DevAlarm?'+tmpStr);
                });
            });
        },
        /**
         * 获取表格的数据
         * @param params
         */
        initResult:function(){
            var params = {};
            params.timeDim = timeDim;
            //params.time = IoUtil.getCurrentTime(timeDim);//当前的时间
            quereObjects = params;
            var self = this;
            this.initListHeard();
            // 设置绿色字体
            function getBlueColor(element,value,data){
                element.addClass('io_green_color');
            }

            $('#pm_fault_list').GridTable({
                url: main.serverUrl.biz+'/produceMng/listDeviceTroubleRpt',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 280,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:280,
                params: params,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                //请求成功后的回调函数
                loadReady:function (results) {
                    //获取告警级别的信息
                    function getShowByLevel(level) {
                        if(level==1){
                            return '严重';
                        }
                        if(level==2){
                            return '重要';
                        }
                        if(level==3){
                            return '一般';
                        }
                        if(level==4){
                            return '次要';
                        }
                        return '提示';
                    }
                    var list = results.list;
                    var len = list.length;
                    var obj = {};//<devTypename设备类型名称,<level告警级别,num频次>> 二重map
                    var xNames = [];//x轴的值
                    for(var i=0;i<len;i++){
                        var tmData = list[i];
                        var devTypeName = tmData.deviceTypeName;
                        if(xNames.indexOf(devTypeName)==-1){//避免重复添加
                            xNames.push(devTypeName);
                        }
                        if(!obj[devTypeName]){
                            obj[devTypeName] = {};
                        }
                        obj[devTypeName][getShowByLevel(tmData.levle)] = tmData.happenNum;
                    }
                    var legendData =  ['严重', '重要','一般','次要','提示'];
                    //根据设备类型分别统计严重，总要一般的数量
                    var obj2 = {};//<level,[number]);
                    var temMax = 0;//保存最大的值
                    for(var devTypeName in obj){//对设备类型做循环
                        var alarmDatas = obj[devTypeName];
                        var tempSum = 0;
                        for(var i=0;i<legendData.length;i++){
                            var tmpData = legendData[i];
                            if(!obj2[tmpData]){
                                obj2[tmpData] = [];
                            }
                            obj2[tmpData].push(alarmDatas[tmpData]||'-');
                            tempSum += (alarmDatas[tmpData]||0);
                        }
                        if(tempSum*1.2>temMax){//判断是否是大于最大值,大于就使用当前的值
                            temMax = (tempSum*1.2).fixed(2);
                        }
                    }
                    //给内容复制
                    var opts = $.extend(true,{},self.myEachOpions);//深度复制
                    opts.legend.data = legendData;
                    opts.yAxis[0].max = temMax;//使用计算的最大值
                    opts.xAxis.data = xNames;
                    for(var i=0;i<legendData.length;i++){
                        opts.series[i].data = obj2[legendData[i]];
                    }
                    ECharts.Render('pm_fault_echart', opts,false);
                },
                loadError:function () {
                    ECharts.Render('pm_fault_echart', self.myEachOpions,false);
                },
                colModel:[
                    {
                        name:'id',
                        hide: true
                    },
                    {
                        display:'设备类型',
                        name: 'deviceTypeName',
                        //align: "center",
                        width: 0.15,
                        fnInit:getBlueColor
                     },
                    {
                        display:'级别',
                        name: 'levle',
                        //align: "center",
                        width: 0.1,
                        fnInit:function (element,value) {
                            var obj = IoUtil.alarmLevelTransform(value);
                            element.html(obj.showText).attr('title',obj.titleMsg).parent().attr('title',obj.titleMsg);
                        }
                    },
                    {
                        display:'频次(次)',
                        name: 'happenNum',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display:'频率最多故障',
                        name: 'maxTroubleName',
                        //align: "center",
                        width: 0.23,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display:'故障时长(ms)',
                        name: 'troubleDuration',
                        //align: "center",
                        width: 0.17,
                        //fnInit: getMyTime
                    },
                    {
                        display:'故障持续最长故障',
                        name: 'maxTroubleDuration',
                        //align: "center",
                        width: 0.2,
                        //fnInit: getMyTime
                    }]
            });
        },
        /**
         * eachart 的初始数据
         */
         myEachOpions:{
            //几种图形的颜色
            color:['#f00','#d48949', '#ccc', '#af9b3d', '#7067ac','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {//假设显示的数值分别代表的告警级别 ，严重 重要 一般 次要 提示
                // data: ['1', '2','3','4','5'],
                data: ['严重', '重要','一般','次要','提示'],
                textStyle:{//设置显示文字的颜色
                    color:'#fff'
                }

            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            yAxis:  [{
                type: 'value',
                position: 'left',
                splitLine:{show: false},//去除网格线
                min:0,
                max:2400,
                axisLabel: {
                    formatter: '{value}'
                },
                axisLine:{
                    lineStyle:{
                        color:'#288297',
                        width:2,//这里是为了突出显示加上的
                    }
                }
            }],
            xAxis: {//设备类型
                type: 'category',
                data: [],
                axisLine:{
                    lineStyle:{
                        color:'#288297',
                        width:2,//这里是为了突出显示加上的
                    }
                },
            },
            series: [
                {
                    name: '严重',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    itemStyle:{
                        normal:{
                            barBorderRadius:[3,3,0,0]//设置圆角
                        }
                    },
                    data: ['-']//数据代表缺陷的数量
                },
                {
                    name: '重要',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    itemStyle:{
                        normal:{
                            barBorderRadius:[3,3,0,0]//设置圆角
                        }
                    },
                    data: ['-']
                },
                {
                    name: '一般',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    itemStyle:{
                        normal:{
                            barBorderRadius:[3,3,0,0]//设置圆角
                        }
                    },
                    data: ['-']
                },
                {
                    name: '次要',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    itemStyle:{
                        normal:{
                            barBorderRadius:[3,3,0,0]//设置圆角
                        }
                    },
                    data: ['-']
                },
                {
                    name: '提示',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    itemStyle:{
                        normal:{
                            barBorderRadius:[3,3,0,0]//设置圆角
                        }
                    },
                    data: ['-']
                }
            ]
         }
    }
});

