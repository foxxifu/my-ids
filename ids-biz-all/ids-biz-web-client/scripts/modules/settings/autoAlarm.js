/**
 * 智能告警界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'autoAlarm',
    description: '模块功能：告警设置',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','modules/io/iocommon'
    ]
});
App.Module('autoAlarm', function ($) {
    return {
        Render:function(params){
            //1.初始化搜索框
            this.initSearchBar();

            //初始化表格数据
            this.initResult();
            //初始事件
            this.initEvent();
        },
        /*初始搜索框*/
        initSearchBar:function(){
            function search(data){//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                $('#setting_auto_alarm_list').GridTableReload(data);

            }
            $('#setting_auto_alarm_bar').ValidateForm('setting_auto_alarm_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model:[[
                        {
                            input: 'select',
                            type: 'select',
                            show: '告警级别',//告警级别
                            name: 'alarmLevel',
                            options:[
                                {value:-1, text:Msg.all},
                                {value:0, text:'严重'},
                                {value:1, text:'重要'},
                                {value:2, text:'一般'},
                                {value:3, text:'次要'},
                                {value:4, text:'提示'},
                            ]
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '告警名称',//告警名称
                            name: 'alarmName',
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '修复建议',//修复建议
                            name: 'suggestion',
                        },
                    ]]
                });
        },
        /*获取初始数据*/
        initResult:function(params){
            params = params || {};
            $('#setting_auto_alarm_list').GridTable({
                url: 'alarmOfDevModels/getAutoAlarms',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 510,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:510,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {name: 'id', hide: true},
                    {
                        display:'告警名称',
                        name: 'alarmName',
                        //align: "center",
                        width: 0.3,
                        //fnInit:dateFormator
                    },
                    {
                        display:'告警级别',
                        name: 'alarmLevel',
                        //align: "center",
                        width: 0.1,
                        fnInit:function (element,value,data) {
                            element.empty();
                            var obj = IoUtil.alarmLevelTransform(value);
                            element.append(obj.showText);
                            element.attr('title',obj.titleMsg);
                            element.parent().attr('title',obj.titleMsg);
                        }
                    },
                    {
                        display:'修复建议',
                        name: 'suggestion',
                        align: "left",
                        width: 0.4,
                    },

                ]
            });
        },
        /*初始事件*/
        initEvent:function () {
            $('#setting_dev_alarm_btns input').unbind('click').bind('click',function () {
                var _this = $(this);
                _this.attr('disabled',true);
                var chbs = $('#setting_dev_alarm_list input[type="checkbox"].BodyCheckBox:checked');
                if(chbs.length==0){
                    App.alert("请选择需要修改的记录");
                    _this.attr('disabled',false);
                    return;
                }
                if(chbs.length>1){
                    App.alert("最多只能选择一条记录做修改");
                    _this.attr('disabled',false);
                    return;
                }
                //TODO 修改内容弹出修改的对话框
                console.log('edit')
                _this.attr('disabled',false);
            })
        }
    }
});