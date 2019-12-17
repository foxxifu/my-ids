/**
 * 设备告警界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'devAlarm',
    description: '模块功能：告警设置',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','modules/io/iocommon'
    ]
});
App.Module('devAlarm', function ($) {
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
                $('#setting_dev_alarm_list').GridTableReload(data);

            }
            $('#setting_dev_alarm_bar').ValidateForm('setting_dev_alarm_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model:[[
                        {
                            input: 'select',
                            type: 'select',
                            show: '设备类型',//设备类型
                            name: 'devTypeId',
                            options:[
                                {value:-1, text:Msg.all},
                                {value:1, text:'逆变器'},
                                {value:2, text:'集中式逆变器'},
                            ],
                            fnInit:function (element) {
                                //查询所有的设备型号 ajax请求，这里就使用一个固定的值
                                element.empty().append($('<option/>').val('-1').text(Msg.all));
                                $.http.get(main.serverUrl.dev+'/settings/query',function (res) {
                                    if(res.success){
                                        var devtypes = res.results;
                                        for(var key in devtypes){
                                            element.append($('<option/>').val(devtypes[key]).text(key));
                                        }
                                    }
                                });
                                element.unbind('change').bind('change',function(ev){
                                    var value = $(this).val();
                                    var modelSelect = $('#setting_dev_alarm_bar select[name=version]');
                                    modelSelect.empty();
                                    modelSelect.append($('<option/>').val('-1').text('请选择版本'));
                                    if(value!=-1){
                                        //TODO 做ajax请求获取版本信息
                                        $.http.get(main.serverUrl.biz+'/settings/signal/unificationAdapter/query?deviceTypeId='+value,function (res) {
                                            if(res.success){debugger
                                                var versions = res.results.modelVersionMaps;
                                                for(var key in versions){//加载版本信息
                                                    modelSelect.append($('<option/>').val(key).text(versions[key]));
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '版本型号',//版本型号
                            name: 'version',
                            options:[
                                {value:-1, text:Msg.all}
                            ]
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '告警级别',//告警级别
                            name: 'severityId',
                            options:[
                                {value:-1, text:Msg.all},
                                {value:1, text:'严重'},
                                {value:2, text:'重要'},
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
                            show: '告警原因',//告警原因
                            name: 'alarmCause',
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '修复建议',//修复建议
                            name: 'repairSuggestion',
                        },
                    ]]
                });
        },
        /*获取初始数据*/
        initResult:function(params){
            params = params || {};
            $('#setting_dev_alarm_list').GridTable({
                url: main.serverUrl.biz+'/settings/alarm/list',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 510,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:510,
                params: params,
                clickSelect: true,//有全选按钮
                singleSelect:true,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {name: 'id', hide: true},
                    {
                        display:'设备类型',
                        name: 'devTypeName',
                        //align: "center",
                        width: 0.14,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'版本号',
                        name: 'versionCode',
                        //align: "center",
                        width: 0.14,
                    },
                    {
                        display:'告警名称',
                        name: 'alarmName',
                        //align: "center",
                        width: 0.16,
                        //fnInit:dateFormator
                    },
                    {
                        display:'告警级别',
                        name: 'alarmLevel',
                        //align: "center",
                        width: 0.08,
                        fnInit:function (element,value,data) {
                            element.empty();
                            var obj = IoUtil.alarmLevelTransform(value);
                            element.append(obj.showText);
                            element.attr('title',obj.titleMsg);
                            element.parent().attr('title',obj.titleMsg);
                        }
                    },
                    {
                        display:'告警原因',
                        name: 'alarmCause',
                        //align: "center",
                        width: 0.2,
                    },
                    {
                        display:'原因码',
                        name: 'causeId',
                        //align: "center",
                        width: 0.06,
                    },
                    {
                        display:'修复建议',
                        name: 'repairSuggestion',
                        align: "left",
                        width: 0.22,
                    },

                ]
            });
        },
        /*初始事件*/
        initEvent:function () {
            $('#setting_dev_alarm_btns input').unbind('click').bind('click',function () {
                var _this = $(this);
                _this.attr('disabled',true);
                var recodes = $('#setting_dev_alarm_list').GridTableSelectedRecords();
                if(recodes.length==0){
                    App.alert("请选择需要修改的记录");
                    _this.attr('disabled',false);
                    return;
                }
                if(recodes.length>1){
                    App.alert("最多只能选择一条记录做修改");
                    _this.attr('disabled',false);
                    return;
                }
                //TODO 修改内容弹出修改的对话框
                var showData = recodes[0];
                var $content = $('<div/>').attr('id','setting_dev_alarm_update_content');
                App.dialog({
                    id:'setting_dev_alarm_update_dialog',
                    title:'修改告警信息',
                    content:$content,
                    width:'60%',
                    height: 'auto',
                    maxHeight: 600, //弹窗内容最大高度, 不支持%
                    buttons: [
                        {
                            id: 'okId',
                            type: 'submit',
                            text: Msg.sure || 'OK',
                            click: function (e, dialog) {
                                $(dialog).find('form').submit();
                            }
                        },
                        {
                            id: 'cancelId',
                            type: 'cancel',
                            text: Msg.cancel || 'Cancel',
                            clickToClose: true
                        }
                    ]
                }).ValidateForm('setting_dev_alarm_update_content',{
                    noButtons: true,
                    submitURL: main.serverUrl.biz+'/settings/alarm/update',//表单提交的路径
                    data:showData,//回显的数据
                    type:'modify',
                    model:[
                        [
                            {
                                input: 'input',
                                type: 'text',
                                show: '告警名称',
                                name: 'alarmName',
                                fnInit:function (element) {
                                    element.attr('readonly','readonly');
                                }
                            },
                            {
                                input: 'input',
                                type: 'text',
                                show: '原因码',
                                name: 'causeId',
                                fnInit:function (element) {
                                    element.attr('readonly','readonly');
                                }
                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '告警级别',
                                options:[//1.严重 2:重要 3:次要 4:提示
                                    {value:1,text:'严重'},
                                    {value:2,text:'重要'},
                                    {value:3,text:'次要'},
                                    {value:4,text:'提示'}
                                ],
                                name: 'alarmLevel',

                            },
                            {
                                input: 'input',
                                type: 'text',
                                show: '版本信息',
                                name: 'versionCode',
                                fnInit:function (element) {
                                    element.attr('readonly','readonly');
                                }
                            }
                        ],
                        [
                            {
                                input: 'textarea',
                                type: 'textarea',
                                show: '修复建议',
                                name: 'repairSuggestion',
                                rule:{
                                    required:true
                                }
                            }
                        ]
                    ],
                    fnModifyData:function (data) {
                        var datas = {};//目前只支持修改2个内容，其他的内容不允许修改
                        datas.id = showData.id;
                        datas.severityId = data.alarmLevel;
                        datas.repairSuggestion = data.repairSuggestion;
                        return datas;
                    },
                    fnSubmitSuccess:function () {
                        App.alert('修改成功');
                        $('#setting_dev_alarm_list').GridTableReload();
                    },
                    fnSubmitError:function () {
                        App.alert('修改失败');
                    }
                });
                _this.attr('disabled',false);
            })
        }
    }
});