/**
 * 智能告警的业务处理js
 **/
'use strict';
App.Module.config({
    package: '/io',
    moduleName: 'autoAlarm',
    description: '模块功能：智能运维——智能告警',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'modules/io/iocommon'
    ]
});

App.Module('autoAlarm', function ($) {
    return {
        Render: function (params) {
            //1.初始搜索表单
            this.initSearchBar();
            //2.加载数据
            this.resltTable();
            //3.事件监听
            this.initEvent();
        },
        //初始搜索框的内容
        initSearchBar: function () {
            var self = this;

            function search(data) {//点击提交按钮执行的事件
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                data.params['index'] = 1;
                data.params['pageSize'] = $('#io_auto_alarm_list select.pselect').val();//这个可以不用
                $('#io_auto_alarm_list').GridTableReload(data);
            }

            //调用表单控件
            $('#io_auto_alarm_search_bar').ValidateForm('io_auto_alarm_search_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                // noButtons:true,
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: Msg.modules.io.autoAlarm.search.sationName,//电站名称
                        name: 'stationName',
                        extend: {id: 'station_name'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: Msg.modules.io.autoAlarm.search.devAlias,//设备名称
                        name: 'devAlias',
                        extend: {id: 'dev_name'}
                    },
                    // {
                    //     input: 'select',
                    //     type: 'select',
                    //     show: Msg.modules.io.autoAlarm.search.devType,//设备类型
                    //     name: 'devType',
                    //     options: [
                    //         {value: -1, text: Msg.all},
                    //         {value: 0, text: Msg.modules.io.autoAlarm.search.devTypeList[0]},
                    //         {value: 1, text: Msg.modules.io.autoAlarm.search.devTypeList[1]},
                    //         {value: 2, text: Msg.modules.io.autoAlarm.search.devTypeList[2]},
                    //         {value: 3, text: Msg.modules.io.autoAlarm.search.devTypeList[3]},
                    //     ],
                    //     extend: {id: 'dev_type'}
                    // },
                    {
                        input: 'input',
                        type: 'text',
                        show: '告警名称',
                        name: 'alarmName'
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '告警级别',
                        name: 'alarmLevel',
                        options: [
                            {value: -1, text: Msg.all},
                            {value: 1, text: '严重'},
                            {value: 2, text: '重要'},
                            {value: 3, text: '次要'},
                            {value: 4, text: '提示'}
                        ],
                        extend: {id: 'alarm_level'}
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
        initEvent:function () {
            $('#io_atuo_alarm #io_auto_alarm_btns input[type=button]').unbind('click').bind('click',function () {
                debugger
                var $this = $(this);
                var myType = $this.attr('myType');
                var records = $('#io_auto_alarm_list').GridTableSelectedRecords();
                var len = records.length;
                if(myType=='toDefect'){
                    if(len==0 || len>1){
                        App.alert(len==0 && '请选择要转换的告警' || '请选择一条告警转换，当前选中的记录数：'+len);
                    }
                    //ajax请求去获取数据 type=1由告警转换为缺陷  type=2由智能告警转换为缺陷
                    $.http.post(main.serverUrl.biz+'/workFlow/alarmToDefect',{alarmId:records[0].id,type:2},function (res) {
                        if(res.success && res.results && res.results!=null){
                            IoUtil.initShowDefautDialog(main.serverUrl.biz+'/workFlow/saveDefect','alarmToDefect',res.results,function (res) {
                                if(res.success){
                                    $('#io_auto_alarm_list').GridTableReload();//重新加载数据
                                    App.alert('转换成功！');
                                }else{
                                    App.alert('转换失败！');
                                }
                            });
                        }else{
                            App.alert('获取失败');
                        }
                    },function () {
                        App.alert('获取失败，请检测服务');
                    });
                }else if(myType == 'sure' || myType == 'clearAlarm'){//确认 和清除 告警的按钮的事件
                    if(len==0){
                        App.alert('未选择记录');
                        return;
                    }
                    var showText = '确定将选中的记录设置为确认状态？';
                    var tmSubUrl = main.serverUrl.biz+'/alarm/confirm/ackzl';
                    var opterPre = '确认';
                    if(myType == 'clearAlarm'){
                        showText = '确定将选中的记录设置为清除状态？';
                        tmSubUrl = main.serverUrl.biz+'/alarm/confirm/clearedzl';
                        opterPre = '清除';
                    }
                    App.confirm(showText,function () {
                        var ids = [];
                        for(var i=0;i<len;i++){
                            ids.push(records[i].id);
                        }
                        $.http.post(tmSubUrl, ids, function (res) {
                            if(res.success){
                                $('#io_auto_alarm_list').GridTableReload();//重新加载数据
                                App.alert(opterPre+"成功");
                            }else{
                                App.alert(opterPre+'失败');
                            }
                        })
                    });

                }
            });
        },
        resltTable: function (params) {
            var self = this;

            /**
             * 展示具体信息内容
             * @param $box 表格的div
             * @param datas 表格显示的数据
             */
            function getContent($box,datas) {
                $box.GridTable({
                    //url: '/workFlow/getWorkFlowDefectsByCondition',
                    title: false,
                    width: '0.1',
                    // ellipsis: true,
                    maxHeight: 450,
                    data:datas,
                    theme: 'ThemeA',
                    height: 'auto',
                    //line_height:"48",
                    max_height:450,
                    //params: params,
                    //clickSelect: true,//有全选按钮
                    isRecordSelected: true,
                    isSearchRecordSelected: false,
                    showSelectedName: false,
                    resizable: true,
                    objectsModel: true,
                    pageBar:false,
                    idProperty: 'causeId',
                    colModel:[
                        {
                            display:'原因码',
                            name: 'causeId',
                            //align: "center",
                            width: 0.2,
                        },
                        {
                            display:'告警原因',
                            name: 'alarmCause',
                            align: "left",
                            width: 0.3,
                            fnInit:function (element,value) {
                                element.empty();
                                element.append($('<div/>').append(value).addClass('auto-alarm-td-indent'));
                            }
                        },
                        {
                            display:'修复建议',
                            name: 'repairSuggestion',
                            align: "left",
                            width: 0.5,
                            fnInit:function (element,value) {
                                element.empty();
                                element.append($('<div/>').append(value).addClass('auto-alarm-td-indent'));
                            }
                        },
                    ]

                });
                $box.append(('<a href="#">告警知识库</a>'));
            }

            //修复建议的点击事件
            function suggestClick(e) {
                e = e || event;
                e.stopPropagation();
                var id = $(this).attr('tmpName');
                $.http.get(main.serverUrl.biz+'/alarm/repair/analysis?id='+id, function (res) {
                    if (!res.success) {
                        App.alert({title: "提示", message: "获取告警修复建议失败"});
                        return;
                    }
                    var $content = $('<div/>').attr('id','io_auto_alarm_suggest_dialog');
                    App.dialog({
                        //id:'',
                        title: "告警修复建议",
                        width: 900,
                        height: 'auto',
                        maxHeight:600,
                        content:$content,
                        buttons: [{text: '关闭', clickToClose: true}]
                    })//.append(getContent(res.data));
                    //或者.append();
                    //App.dialog('close'); 关闭所有
                    //$('#dialogid').dialog('close');// 关闭指定的对话框
                    getContent($content,res.data);
                });
            }

            //修复建议的数据格式
            function repariSuggest(element, value, data) {
                element.empty();
                var $tma = $('<a tmpName="' + value + '">详情信息</a>');
                $tma.click(suggestClick);//添加点击事件
                element.attr('title', '详情信息');
                element.append($tma);
            }

            function getMyTime(element, value, data) {//获取时间
                element.empty();
                var date = IoUtil.mininToData(value);
                //$tma.click(suggestClick);//添加点击事件
                element.attr('title', date);
                element.html(date);
            }

            function getAlarmLevle(element, value, data) {//获取告警级别
                element.empty();
                var alarmInfo = IoUtil.alarmLevelTransform(value);
                element.attr('title', alarmInfo.titleMsg);
                element.append(alarmInfo.showText);
            }

            function getAlarmStatus(element, value, data) {//获取告警状态
                element.empty();
                var text = IoUtil.alarmStatusFormart(value);
                element.attr('title', text);
                element.html(text);
            }

            $('#io_auto_alarm_list').GridTable({
                url: main.serverUrl.biz+'/alarm/list/analysis',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                //line_height:"58",
                max_height: 600,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.2,
                    },
                    {
                        display: '告警名称',
                        name: 'alarmName',
                        //align: "center",
                        width: 0.1,
                    },
                    {
                        display: '发生时间',
                        name: 'firstDate',
                        //align: "center",
                        width: 0.1,
                        fnInit: getMyTime
                    },
                    {
                        display: '复位时间',
                        name: 'recoveDate',
                        //align: "center",
                        width: 0.1,
                        fnInit: getMyTime
                    },
                    {
                        display: '告警级别',
                        name: 'alarmLevel',
                        //align: "center",
                        width: 0.08,
                        fnInit: getAlarmLevle
                    },
                    {
                        display: '设备名称',
                        name: 'devAlias',
                        //align: "center",
                        width: 0.08,
                        // fnInit: initCheckBox
                    },
                    {
                        display: '告警状态',
                        name: 'alarmState',
                        //align: "center",
                        width: 0.08,
                        fnInit: getAlarmStatus
                    },
                    {
                        display: '告警类型',
                        name: 'alarmType',
                        //align: "center",
                        width: 0.1,
                        fnInit:function (element,value) {
                            debugger
                            element.empty();
                            var text = IoUtil.getAlarmType(value);
                            element.html(text).attr('title',text).parent().attr('title',text);
                        }
                        // fnInit: initCheckBox
                    },
                    {
                        display: '修复建议',
                        name: 'id',
                        //align: "center",
                        width: 0.1,
                        fnInit: repariSuggest
                    },
                ]
            })
        }
    }
});