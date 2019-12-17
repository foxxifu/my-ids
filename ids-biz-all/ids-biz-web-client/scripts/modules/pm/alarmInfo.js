/**
 * 设备实时数据的界面 脚本
 */
'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'runData',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','modules/io/iocommon'
    ]
});

App.Module('runData',function($){
    //获取清除或者确认的告警信息
    function getRecodsInfo(recods) {
        if(!recods || recods.length==0){
            return false;
        }
        var len = recods.length;
        var autoIds = [];//智能告警的id
        var ids = [];//设备告警的id
        for(var i=0;i<len;i++){debugger
            var recod = recods[i];
            if(recod.alarmType == '3'){
                autoIds.push(recod.id);
            }else{
                ids.push(recod.id);
            }
        }
        return {ids:ids,autoIds:autoIds}
    }
    return {
        Render:function(params){
            console.log(params);
            //TODO 根据params中的电站id和设备id查询对应的设备的告警信息
            this.initResult(params);
            //初始化事件
            this.initEvent();
        },
        //获取告警信息
        initResult:function(params){
            //var data = this.getAlarmInfos(params).results;
            var tmpParam = {devId:params.devId};
            function dateFomart(element,value,data){
                element.empty();
                var str = '-';
                if(value){
                    str = new Date(value).format('yyyy-MM-dd HH:mm:ss');
                }
                element.text(str);
                element.attr('title',str).parent().attr('title',str);
            }
            $('#pm_dev_alarm_list').GridTable({
                url: main.serverUrl.biz + '/alarm/list/specified',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 560,
                theme: 'ThemeA',
                height: 'auto',
                //line_height:"48",
                max_height:560,
                params: tmpParam,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                //data:data.list,
                colModel:[
                    {
                        display:'电站名称',
                        name: 'stationName',
                        width: 0.2,
                    },
                    {
                        display:'告警名称',
                        name: 'alarmName',
                        width: 0.1,
                    },
                    {
                        display:'发生时间',
                        name: 'createDate',
                        width: 0.1,
                        fnInit:dateFomart
                    },
                    {
                        display:'恢复时间',
                        name: 'recoveDate',
                        width: 0.1,
                        fnInit:dateFomart
                    },
                    {
                        display:'告警级别',
                        name: 'alarmLevel',
                        width: 0.06,
                        fnInit:function (element,value,data) {
                            element.empty();
                            var obj = IoUtil.alarmLevelTransform(value);
                            element.append(obj.showText);
                            element.attr('title',obj.titleMsg).parent().attr('title',obj.titleMsg);
                        }
                    },
                    {
                        display:'设备名称',
                        name: 'devAlias',
                        width: 0.1,
                    },
                    {
                        display:'设备类型',
                        name: 'devType',
                        width: 0.08,
                    },
                    {
                        display:'告警状态',
                        name: 'alarmState',
                        width: 0.06,
                        fnInit:function (element,value,data) {
                            var str = IoUtil.alarmStatusFormart(value);
                            element.empty();
                            element.text(str).removeAttr('title').parent().attr('title',str);
                        }
                    },
                    {
                        display:'告警类型',
                        name: 'alarmType',
                        width: 0.1,
                        fnInit:function (element,value,data) {
                            var str = '';
                            if(value==1){
                                str = '告警';
                            }else if(value==2){
                                str = '事件';
                            }else {
                                str = '系统自检';
                            }
                            element.text(str).attr('title',str).parent().attr('title',str);
                        }
                    },
                    {
                        display:'修复建议',
                        name: 'id',
                        width: 0.1,
                        fnInit:function (element,value,data) {
                            element.empty();
                            element.removeAttr('title').parent().removeAttr('title').attr('title','修复建议');
                            var $a = $('<a/>').text('修复建议');
                            element.append($a);
                            //TODO 确认是否根据告警类型来弹出不同的修复建议信息
                            $a.unbind('click').bind('click',function (ev) {
                                ev = ev || event;
                                ev.stopPropagation();
                                var $content = $('<div/>').append(data.repairSuggestion);
                                App.dialog({
                                    title:'修复建议',
                                    height: 'auto',
                                    maxHeight: '400', //弹窗内容最大高度, 不支持%
                                    content:$content,
                                    buttons: [
                                        {
                                            id: 'cancelId',
                                            type: 'cancel',
                                            text: Msg.cancel || 'Cancel',
                                            clickToClose: true
                                        }
                                    ]
                                })
                            })
                        }
                    },
                ]
            });
        },
        //初始化事件
        initEvent:function () {
            var btns = $('#pm_dev_alarm_btns input[type=button]');
            btns.unbind('click').bind('click',function () {
                var $this = $(this);
                var myType = $this.attr('myType');
                var recodes = $('#pm_dev_alarm_list').GridTableSelectedRecords();
                debugger
                if(recodes.length==0){
                    App.alert('未选择任何数据');
                    return;
                }
                var pData = getRecodsInfo(recodes);
                if(myType=='clear'){//清除
                    App.confirm('是否清除当前选中数据?',function () {
                        $.http.post(main.serverUrl.biz +'/alarm/more/cleared',pData,function (res) {
                            if(res.success){
                                App.alert('清除成功');
                                $('#pm_dev_alarm_list').GridTableReload();
                            }else{
                                App.alert('清除失败');
                            }
                        })
                    });
                }else if(myType=='sure'){//确认
                    App.confirm('是否确认当前选中数据?',function () {
                        $.http.post(main.serverUrl.biz +'/alarm/more/ack',pData,function (res) {
                            if(res.success){
                                App.alert('确认成功');
                                $('#pm_dev_alarm_list').GridTableReload();
                            }else{
                                App.alert('确认失败');
                            }
                        })
                    });
                }
            });
        }
    }
});