'use strict';
/**
 * 处理缺陷管理页面的脚本
 */
'use strict';
App.Module.config({
    package: '/io',
    moduleName: 'inventerAnalysis',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Timer','ECharts','modules/io/iocommon'
    ]
});

App.Module('inventerAnalysis',function($){
    return {
        Render:function(params){
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
            debugger
            var self = this;
            //查询数据
            function search(){
                var params = {};
                self.initResult(params);
            }
            //初始事件控件
            function dateInit(element,value,data){
                if(element.attr('name')=='endDate'){//设置与之前的时间控件间隔
                    element.parents('.clsRow').css('margin-left',-10);
                }
                element.focus(function(){
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',el:this});
                });
            }

            $('#io_diagnosis_inventer_analysis_search_bar').ValidateForm('io_task_defect_search_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//电站名称
                        name: 'stationName',
                        extend: {id: 'station_name'}
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '流程状态',//设备名称
                        name: 'state',
                        options:[
                            {value:-1, text:Msg.all},
                            {value:0, text:'待确认'},
                            {value:1, text:'待分配'},
                            {value:2, text:'消缺中'},
                            {value:3, text:'已终结'},
                            {value:4, text:'已回退'}
                        ],
                        extend: {id: 'defect_state'}
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '处理结果',//处理结果
                        name: 'result',
                        options:[
                            {value:-1, text:Msg.all},
                            {value:0, text:'完成'},
                            {value:1, text:'处理中'},
                            {value:2, text:'未处理'},
                        ],
                        extend: {id: 'defect_result'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '发生时间',//处理结果
                        name: 'startDate',
                        width:181,
                        extend: {id: 'defect_start_date',class:'Wdate'},
                        fnInit:dateInit,
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '~',//处理结果
                        name: 'endDate',
                        width:181,
                        extend: {id: 'defect_end_date',class:'Wdate'},
                        fnInit:dateInit,//监听对象的事件
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
            var dataAxis = [
                '01日','02日','03日','04日','05日','06日','07日','08日','09日','10日',
                '11日','12日','13日','14日','15日','16日','17日','18日','19日','20日',
                '21日','22日','23日','24日','25日','26日','27日','28日','29日','30日',
                '31日'
            ];
            var xLen = dataAxis.length;
            var tmpXdatas = [];
            var xTextStyle = {//设置x轴的字体为白色
                color:'#aaa',
                fontSize:16
            }
            // 这里做遍历是为了给x轴的文字设置颜色为灰色
            for(var i=0;i<xLen;i++){
                var tmObj = {};
                tmObj.value = dataAxis[i];
                tmObj.textStyle = xTextStyle;
                tmpXdatas.push(tmObj);
            }
            debugger
            var data1 = [
                250,300,250,300,240,230,250,260,300,300,
                250,300,250,300,240,230,250,260,300,300,
                250,300,250,300,240,230,250,260,300,300,
                280
            ]
            var data2 = [
                310,350,300,320,400,360,330,340,320,280,
                310,350,300,320,400,360,330,340,320,280,
                310,350,300,320,400,360,330,340,320,380,
                420
            ]

            self.myEachOpions.xAxis[0].data = tmpXdatas;
            self.myEachOpions.series[0].data = data1;
            self.myEachOpions.series[1].data = data2;
            ECharts.Render('io_diagnosis_inventer_analysis_cont', self.myEachOpions);
        },
        /**
         * 获取表格的数据
         * @param params
         */
        initResult:function(params){
            // 设置绿色字体
            function getBlueColor(element,value,data){
                element.addClass('io_green_color');
            }
            function  getAlarmStatus(element,value,data) {//获取告警状态
                element.empty();
                var obj = IoUtil.flowState(value);
                element.attr('title',obj.msgTitle);
                element.parent().attr('title',obj.msgTitle);
                element.html(obj.msgText);
                element.addClass('io_bloder');
            }
            function getMyTime(element,value,data){//获取时间
                element.empty();
                var date = IoUtil.mininToData(value);
                //$tma.click(suggestClick);//添加点击事件
                element.attr('title',date);
                element.parent().attr('title',date);
                element.html(date);
            }
            //处理结果的格式化
            function getOpeatReslt(element,value,data){
                element.empty();
                var re = IoUtil.alarmOperatResult(value);
                element.attr('title',re);
                element.parent().attr('title',re);
                element.html(re);
            }

            function initOperate(element,value,data){
                element.empty();

                var $detail = $('<a href="#" title="详情" class="io_green_color">详情</a>');
                element.attr('title','详情');
                element.parent().attr('title','详情');
                element.append($detail);
                if(data.result==2){//未处理
                    var $exe = $('<a href="#" title="执行" style="margin-left: 5px;" class="io_green_color">执行</a>');
                    element.append($exe);
                }
            }
            $('#io_diagnosis_inventer_analysis_list').GridTable({
                url: 'alarmOfDevModels/getDefectList',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 280,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:280,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[{
                    display:'电站名称',
                    name: 'stationName',
                    //align: "center",
                    width: 0.2,
                    fnInit:getBlueColor
                },
                    {
                        display:'设备名称',
                        name: 'devName',
                        //align: "center",
                        width: 0.08,
                    },
                    {
                        display:'缺陷描述',
                        name: 'defectDesc',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display:'流程状态',
                        name: 'status',
                        //align: "center",
                        width: 0.08,
                        fnInit: getAlarmStatus
                    },
                    {
                        display:'当前处理人',
                        name: 'userName',
                        //align: "center",
                        width: 0.08,
                        //fnInit: getMyTime
                    },
                    {
                        display:'开始时间',
                        name: 'startDate',
                        //align: "center",
                        width: 0.14,
                        fnInit: getMyTime
                    },
                    {
                        display:'结束时间',
                        name: 'endDate',
                        //align: "endDate",
                        width: 0.14,
                        fnInit: getMyTime
                    },
                    {
                        display:'处理结果',
                        name:'result',
                        width:0.08,
                        fnInit:getOpeatReslt

                    },
                    {
                        display:'操作',
                        name: 'id',
                        //align: "center",
                        width: 0.1,
                        fnInit: initOperate
                    },]
            });
        },
        /**
         * eachart 的初始数据
         */
        myEachOpions:{
            color: ['red'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data:['发电量(kWh)','收益(万元)'],
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
            // calculable : true,
            xAxis : [
                {
                    type : 'category',
                    data : [],//加载x轴的数据
                    splitLine:{show: false},//去除网格线
                    lineStyle:{
                        show:false,
                    },
                    nameTextStyle:{
                        color:'#fff'
                    },
                    axisLine:{
                        lineStyle:{
                            color:'#288297',
                            width:2,//这里是为了突出显示加上的
                        }
                    },
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type: 'value',
                    name: '发电量(kWh)',
                    position: 'left',
                    splitLine:{show: false},//去除网格线
                    min:0,
                    max:500,
                    axisLabel: {
                        formatter: '{value}'
                    },
                    axisLine:{
                        lineStyle:{
                            color:'#288297',
                            width:2,//这里是为了突出显示加上的
                        }
                    }
                },
                {
                    type: 'value',
                    name: '收益(万元)',
                    splitLine:{show: false},//去除网格线
                    min: 0,
                    max: 500,
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}'
                    },
                    axisLine:{
                        lineStyle:{
                            color:'#288297',//设置坐标轴的颜色
                            width:2,//这里是为了突出显示加上的
                        }
                    }
                },
            ],
            series : [
                {
                    name:'发电量(kWh)',
                    type:'bar',
                    barWidth: '60%',
                    yAxis: 1,
                    itemStyle:{
                        normal:{
                            color:'#1eb05b',
                            barBorderRadius:[5,5,0,0]//设置圆角
                        }
                    },
                    data:[]
                },
                {
                    name:'收益(万元)',
                    type:'line',
                    symbol:'circle',
                    symbolSize:8,
                    yAxisIndex: 1,
                    itemStyle:{normal:{color:'#1191f2'}},
                    data:[]
                }

            ]
        }
    }
});