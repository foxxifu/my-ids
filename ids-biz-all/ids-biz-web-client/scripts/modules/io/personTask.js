/**
 ** 个人任务处理的脚本
 **/

'use strict';
App.Module.config({
    package: '/io',
    moduleName: 'personTask',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Timer','idsInputTree','modules/io/iocommon'
    ]
});

App.Module('personTask',function($){
    return {
        Render:function(params){
            //1.初始查询框的组件
            this.initSearchBar();
            //2.加载表格信息
            this.initResult();
        },
        /**
         * 初始搜索栏的组件
         */
        initSearchBar:function(){
            var self = this;
            //查询数据
            function search(data){
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                if(data.params['startFindTime']){
                    data.params['startFindTime'] = new Date(data.params['startFindTime']).getTime();
                }
                if(data.params['endFindTime']){
                    data.params['endFindTime'] = new Date(data.params['endFindTime']).getTime();
                }
                data.params['index'] = 1;
                data.params['pageSize'] = $('#io_task_persion_list select.pselect').val();
                $('#io_task_persion_list').GridTableReload(data);
            }
            //初始事件控件
            function dateInit(element,value,data){
                if(element.attr('name')=='endFindTime'){//设置与之前的事件间隔
                    element.parents('.clsRow').css('margin-left',-10);
                }
                element.focus(function(){
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',el:this,isShowClear:true});
                });
            }

            $('#io_task_persion_search_bar').ValidateForm('io_task_persion_search_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//电站名称
                        name: 'stationName',
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '流程状态',//设备名称
                        name: 'procState',
                        options:[
                            // 流程状态 0.待审核 1.已退回 2. 消缺中 3 待确认 4. 已完成
                            {value:-1, text:Msg.all},
                            {value:0, text:'待审核'},
                            {value:1, text:'已退回'},
                            {value:2, text:'消缺中'},
                            {value:3, text:'待确认'},
                            {value:4, text:'已完成'}
                        ],
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '处理结果',//处理结果
                        name: 'dealResult',
                        options:[
                            //1:未完全消除 2:已完全消除 3:不处理
                            {value:-1, text:Msg.all},
                            {value:1, text:'未完全消除'},
                            {value:2, text:'已完全消除'},
                            {value:3, text:'不处理'},
                        ],
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '发生时间',//处理结果
                        name: 'startFindTime',
                        width:181,
                        extend: {class:'Wdate'},
                        fnInit:dateInit,
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '~',//处理结果
                        name: 'endFindTime',
                        width:181,
                        extend: {class:'Wdate'},
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
                element.html(obj.msgText);
                element.addClass('io_bloder');
            }
            function getMyTime(element,value,data){//获取时间
                element.empty();
                var date = IoUtil.mininToData(value);
                //$tma.click(suggestClick);//添加点击事件
                element.attr('title',date);
                element.html(date);
            }
            //处理结果的格式化
            function getOpeatReslt(element,value,data){
                element.empty();
                var str = '未处理'
                if(value==1){
                    str = '已处理';
                }
                element.attr('title',str);
                element.html(str);
            }

            function initOperate(element,value,data){
                element.empty();
                var $detail = $('<a href="#" title="详情" class="io_green_color">详情</a>');
                element.append($detail);
                function getDetails(ev,type,subUrl,fnCallback) {
                    ev = ev || event;
                    ev.stopPropagation();
                    //查询数据
                    $.http.post(main.serverUrl.biz+'/workFlow/getWorkFlowDefectDetails',{defectId:data.defectId},function (res) {
                        if(res.success && res.results){
                            IoUtil.showTaskDialog(res.results,type,subUrl,fnCallback);
                        }else{
                            App.alert('获取详情数据失败');
                        }
                    },function () {
                        App.alert('获取详情数据失败');
                    });
                }
                $detail.unbind('click').bind('click',function (ev) {
                    getDetails(ev,1);
                });
                //未处理的，并且当前处理人是当前的登录用户  data.dealResult==0表示未处理
                if(data.dealResult!=1 && data.currentUserId==Cookies.get('userId')){
                    var $exe = $('<a href="#" title="执行" style="margin-left: 5px;" class="io_green_color">执行</a>');
                    element.append($exe);
                    $exe.unbind('click').bind('click',function (ev) {
                        getDetails(ev,2,main.serverUrl.biz+'/workFlow/executeWorkFlowDefect',function (data) {
                            App.alert('操作成功!');
                            $('#io_task_persion_list').GridTableReload();//从新加载数据
                        });
                    })
                }
            }
            $('#io_task_persion_list').GridTable({
                url: main.serverUrl.biz+'/workFlow/getWorkFlowDefectsByCondition',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 560,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:560,
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
                        name: 'deviceName',
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
                        display:'任务内容',
                        name: 'finder',
                        //align: "center",
                        width: 0.16,
                        //fnInit: getMyTime
                    },
                    {
                        display:'开始时间',
                        name: 'startTime',
                        //align: "center",
                        width: 0.14,
                        fnInit: getMyTime
                    },
                    {
                        display:'结束时间',
                        name: 'endTime',
                        //align: "endDate",
                        width: 0.14,
                        fnInit: getMyTime
                    },
                    {
                        display:'处理结果',
                        name:'dealResult',
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
        }
    }
});