/**
 * 处理缺陷管理页面的脚本
 */
'use strict';
App.Module.config({
    package: '/io',
    moduleName: 'defect',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Timer','ajaxFileUpload','idsInputTree','modules/io/iocommon'
    ]
});

App.Module('defect',function($){
    return {
        Render:function(params){
            //1.初始查询框的组件
            this.initSearchBar();
            //2.初始缺陷信息的显示内容
            this.initDefectInfo();
            //3.加载表格信息
            this.initResult();
            //4.初始化事件
            this.initEvent();
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
                data.params.queryType = '1';
                data.params['index'] = 1;
                data.params['pageSize'] = $('#io_task_defect_list select.pselect').val();
                $('#io_task_defect_list').GridTableReload(data);
                self.initDefectInfo(data.params);//查询缺陷的数据
                self.tmpParams = data.params;
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

            $('#io_task_defect_search_bar').ValidateForm('io_task_defect_search_bar',{
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
         * 初始消缺的信息
         */
        initDefectInfo:function(param){
            var self = this;
            function myCreateElements(className,showText){
                var $tmDiv = $('<div class="'+className+'"></div>');
                $tmDiv.append('<div class="io_task_defect_top">'+showText+'</div>');
                $tmDiv.append('<div class="io_task_defect_middel"></div>');
                $tmDiv.append('<div class="io_task_defect_bottom">总：<span></span></div>');
                return $tmDiv;

            }
            //创建元素
            var $cout = $('#io_task_defect_cont');
            $cout.empty();
            $cout.append(myCreateElements('io_task_defect_finish','待审核'));
            $cout.append(myCreateElements('io_task_defect_back','已回退'));
            $cout.append(myCreateElements('io_task_defect_cancel','消缺中'));
            $cout.append(myCreateElements('io_task_defect_sure','待确认'));
            // 获取消缺信息
            function getDefects(){
                $.http.post(main.serverUrl.biz+'/workFlow/getWorkFlowCount',
                    param||{},
                    function(res){
                        if(res.success){
                            var data = res.data;
                            $cout.find('div span').html(data.count);
                            $cout.find('.io_task_defect_back .io_task_defect_middel').html(data.reback||0);//已回退
                            $cout.find('.io_task_defect_cancel .io_task_defect_middel').html(data.dealing||0);//消缺中
                            $cout.find('.io_task_defect_sure .io_task_defect_middel').html(data.notsure||0);//待确认
                            $cout.find('.io_task_defect_finish .io_task_defect_middel').html(data.waiting||0);//待审核
                        }else{
                            //获取失败的信息
                            $cout.find('div span').html(0);
                            $cout.find('.io_task_defect_back .io_task_defect_middel').html(0);
                            $cout.find('.io_task_defect_cancel .io_task_defect_middel').html(0);
                            $cout.find('.io_task_defect_sure .io_task_defect_middel').html(0);
                            $cout.find('.io_task_defect_finish .io_task_defect_middel').html(0);
                        }
                    });
            }
            getDefects();
            //$cout.everyTimer('5s','query_defect_count',getDefects,0,false);// 执行定时任务
        },
        /**
         * 获取表格的数据
         * @param params
         */
        initResult:function(params){
            var self = this;
            params = params || {};
            params.queryType = '1';
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
                var re = IoUtil.defectOperatResult(value);
                element.attr('title',re);
                element.html(re);
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
                //未处理的并且当前处理人是当前的登录用户  data.dealResult==1表示未处理
                if(data.dealResult==1 && data.currentUserId==Cookies.get('userId')){//TODO 未处理 当前的登录用户等于当前流程用户的id就可以执行流程的操作
                    var $exe = $('<a href="#" title="执行" style="margin-left: 5px;" class="io_green_color">执行</a>');
                    element.append($exe);
                    $exe.unbind('click').bind('click',function (ev) {
                        getDetails(ev,2,main.serverUrl.biz+'/workFlow/executeWorkFlowDefect',function (data) {
                            App.alert('操作成功!');
                            $('#io_task_defect_list').GridTableReload();//从新加载数据
                            self.initDefectInfo(self.tmpParams);//查询缺陷的数据
                        });
                    })
                }
            }
            $('#io_task_defect_list').GridTable({
                url: main.serverUrl.biz+'/workFlow/getWorkFlowDefectsByCondition',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 450,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                //line_height:"48",
                max_height:450,
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
                        name: 'devAlias',
                        //align: "center",
                        width: 0.08,
                    },
                    {
                        display:'缺陷描述',
                        name: 'description',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display:'流程状态',
                        name: 'procState',
                        //align: "center",
                        width: 0.08,
                        fnInit: getAlarmStatus
                    },
                    {
                        display:'当前处理人',
                        name: 'currentUserName',
                        //align: "center",
                        width: 0.08,
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
        },
        /**
         * 初始化事件
         */
        initEvent:function () {
            var self = this;
            $('#io_alarm_btns input[type=button]').unbind('click').bind('click',function (ev) {
                var _this = $(this);
                var myType = _this.attr('myType');
                if(myType == 'add'){//新增
                    IoUtil.initShowDefautDialog(main.serverUrl.biz+'/workFlow/saveDefect','add',function (res) {
                        if(res.success){debugger
                            $('#io_task_defect_list').GridTableReload();
                            self.initDefectInfo(self.tmpParams);//查询缺陷的数据
                            App.alert('新增成功');
                        }else{
                            App.alert('新增失败');
                        }
                    });
                }else if(myType == 'copy'){//复制
                    //根据选择的记录查询一条缺陷数据，然后回显出来
                    var records = $('#io_task_defect_list').GridTableSelectedRecords();
                    var len = records.length;
                    if(len==0 || len>1){
                        App.alert(len==0 &&'请选择需要复制的内容' || '最多复制一条缺陷，当前选中记录数：'+len);
                        return;
                    }
                    //ajax请求去获取数据
                    $.http.post(main.serverUrl.biz+'/workFlow/getWorkFlowDefectDetails',{defectId:records[0].defectId},function (res) {
                        if(res.success && res.results && res.results!=null){
                            IoUtil.initShowDefautDialog(main.serverUrl.biz+'/workFlow/saveDefect','modify',res.results,function (res) {
                                if(res.success){
                                    $('#io_task_defect_list').GridTableReload();//重新加载数据
                                    self.initDefectInfo(self.tmpParams);//查询缺陷的数据
                                    App.alert('保存成功！');
                                }else{
                                    App.alert('保存失败！');
                                }
                            });
                        }else{
                            App.alert('获取失败');
                        }
                    },function () {
                        App.alert('获取失败，请检测服务');
                    });

                }
            });
        }

    }
});