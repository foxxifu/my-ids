/**
 * 数据补采界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'kpiRecalculation',
    description: '模块功能：数据补采',
    importList: [
        'jquery','IDSWebSocket', 'ValidateForm', 'GridTable', 'DatePicker', 'Progress', 'modules/io/iocommon'
    ]
});
App.Module('kpiRecalculation', function ($,IDSWebSocket) {
    var MsePre = Msg.modules.settings.kpiRecalculation;
    var webSocketTaskIdToProgressElement = {};//{taskId,'进度条的jquery元素'} 任务id和任务元素的的,websocket的时候获取更新的元素对象
    return {
        Render: function (params) {
            //1.初始查询框的组件
            this.initSearchBar();
            //2.按钮栏的初始化

            //3.加载表格信息
            this.initResult();
            //4.初始化事件
            this.initEvent();
        },
        initSearchBar: function () {
            function search(data) {
                webSocketTaskIdToProgressElement = {};//清空数据
                for (var key in data) {
                    if (key == 'params') {
                        continue;
                    }
                    data.params[key] = data[key];
                }
                $('#setting_kr_list').GridTableReload(data);

            }

            //初始事件控件
            // function dateInitFormart(element, value, data) {
            //     if (element.attr('name') == 'endDate') {//设置与之前的事件间隔
            //         element.parents('.clsRow').css('margin-left', -10);
            //     }
            //     element.focus(function () {
            //         DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', el: this});
            //     });
            // }

            $('#setting_kr_search_bar').ValidateForm('setting_kr_search_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model: [[
                        {
                            input: 'input',
                            type: 'text',
                            show: '任务名称',//任务名称
                            name: 'taskName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '电站名称',//电站名称
                            name: 'stationName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '任务状态',//任务状态
                            name: 'taskStatus',
                            options: [
                                {'value': -1, 'text': Msg.all},
                                {'value': 1, 'text': MsePre.taskStateList[0]},//未完成
                                {'value': 2, 'text': MsePre.taskStateList[1]},//正在执行
                                {'value': 3, 'text': MsePre.taskStateList[2]},//已完成
                                {'value': 4, 'text': MsePre.taskStateList[3]},//执行失败
                            ]
                            //extend: {id: 'station_name'}
                        }
                        // ,
                        // {
                        //     input: 'input',
                        //     type: 'text',
                        //     show: ' 发生时间',//设备名称
                        //     name: 'startTime',
                        //     width: 181,
                        //     fnInit: dateInitFormart,
                        //     extend: {class: 'Wdate'}
                        // },
                        // {
                        //     input: 'input',
                        //     type: 'text',
                        //     show: ' ~',//
                        //     name: 'endDate',
                        //     width: 181,
                        //     fnInit: dateInitFormart,
                        //     extend: {class: 'Wdate'}
                        // },
                    ]],
                    extraButtons: [
                        {
                            input: 'button',
                            type: 'button',
                            show: '',
                            name: '',
                            align: 'right',
                            fnClick: function (e) {//点击回调函数
                                App.alert('点击了按钮');
                            },
                            extend: {'class': 'btn btn-filter'}
                        }
                    ]
                })
        },
        /**
         * 初始化表格数据
         * @param params
         */
        initResult: function (params) {
            var self = this;
            params = params || {};
            //时间的格式化
            function dateFormator(element, value, data) {
                element.empty();
                var str = '';
                if (value && value != null && value != '') {
                    str = new Date(value).format('yyyy-MM-dd HH:mm:ss');
                    element.attr('title', str);
                    element.parent().attr('title', str);
                } else {
                    element.removeAttr('title');
                    element.parent().removeAttr('title');
                }
                element.html(str);
            }

            //进度条的格式化
            function progressFormator(element, value, data) {
                element.empty();
                var status = data.taskStatus;
                if (status == 2) {//执行中
                    element.removeAttr('title').parent().removeAttr('title');
                    element.myProgress({width: '99%', height: 10, r: 5,hasProgress:false});//调用进度插件
                    webSocketTaskIdToProgressElement[data.id] = element;
                    //element.setProgressValue(0);
                } else if (status == 1) {//未完成
                    element.text(MsePre.taskStateList[0])
                } else if (status == 3) {
                    element.text(MsePre.taskStateList[2]);
                } else if (status == 4) {
                    element.text(MsePre.taskStateList[3]);
                }
                if(status != 2){//不是执行中的就删除对应的信息
                    if(webSocketTaskIdToProgressElement.hasOwnProperty(data.id)){
                        delete webSocketTaskIdToProgressElement[data.id];
                    }
                }
                //TODO 通过进度条查询进度的信息,这里还是做一个模拟的线程池来执行，避免发送太多的信息到后台(根据value的值不是某一个值得时候就执行一个任务)
            }

            $('#setting_kr_list').GridTable({
                url: main.serverUrl.biz + '/kpiRevise/queryCalcTask',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 600,
                params: params,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                onLoadReady:function (data) {
                    var tmpStat = IDSWebSocket.getWebSocktState();
                    if(tmpStat && tmpStat!=1 ){//注册信息 ajax请求去获取url地址
                        $.http.get(main.serverUrl.biz +'/getWebsocketUrl',function (res) {
                            if(res.success && res.data){
                                IDSWebSocket.initWebSocket(res.data);
                            }
                        },false);
                        //IDSWebSocket.initWebSocket();
                    }
                    IDSWebSocket.startRegister('kpirevise',{},function (res) {
                       debugger
                        var taskId = res.taskId;
                        var tmpElement;
                        if(!(tmpElement=webSocketTaskIdToProgressElement[taskId])){//如果当期页每页对应的元素就不更新
                            return
                        }
                        var taskStatus = res.taskStatus;
                        if(taskStatus==3){
                            tmpElement.closeProgressDialog();
                            tmpElement.html('已完成');
                            delete webSocketTaskIdToProgressElement[taskId];
                        }else if(taskStatus==4){
                            tmpElement.closeProgressDialog();
                            tmpElement.html('执行失败');
                            delete webSocketTaskIdToProgressElement[taskId];
                        }
                    });
                },
                colModel: [
                    {
                        display: '任务名称',
                        name: 'taskName',
                        //align: "center",
                        width: 0.15,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '电站',
                        name: 'stationName',
                        //align: "center",
                        width: 0.18,
                    },
                    {
                        display: '任务状态',
                        name: 'taskStatus',
                        //align: "center",
                        width: 0.07,
                        fnInit: function (element, value) {
                            var showText = value && MsePre.taskStateList[value - 1] || MsePre.taskStateList[3];
                            element.empty().text(showText).attr('title', showText).parent().attr('title', showText);
                        }
                    },
                    {
                        display: '开始时间',
                        name: 'startTime',
                        //align: "center",
                        width: 0.12,
                        fnInit: dateFormator
                    },
                    {
                        display: '完成时间',
                        name: 'endTime',
                        //align: "center",
                        width: 0.12,
                        fnInit: dateFormator
                    },
                    {
                        display: '进度',
                        name: 'p1',
                        //align: "center",
                        width: 0.22,
                        fnInit: progressFormator
                    },
                    {
                        display: '操作',
                        name: 'opt1',
                        //align: "center",
                        //width: 0.2,
                        fnInit: function (element, value, data) {
                            element.empty();
                            if (data.taskStatus == 1) {
                                var $updateA = $('<a/>').text('修改').attr('title', '修改');
                                element.append($updateA);
                                $updateA.unbind('click').bind('click', function (ev) {
                                    ev.stopPropagation();
                                    self.showTaskDialog(data);
                                });
                                var $delA = $('<a/>').text('删除').attr('title', '删除');
                                $delA.css('margin-left', '1%');
                                element.append($delA);
                                $delA.unbind('click').bind('click', function (ev) {
                                    ev.stopPropagation();
                                    App.confirm('确认删除记录当前记录?', function () {
                                        $.http.post(main.serverUrl.biz + '/kpiRevise/removeCalcTask', {id: data.id}, function (res) {
                                            if (res.success) {
                                                App.alert('删除成功');
                                                $('#setting_kr_list').GridTableRefreshPage();
                                            }else{
                                                App.alert('删除失败');
                                            }
                                        });
                                    });
                                });
                            }
                            if (data.taskStatus == 1 || data.taskStatus == 4) {//可以执行
                                var $exeA = $('<a/>').text('执行').attr('title', '执行');
                                if (data.taskStatus == 1) {
                                    $exeA.css('margin-left', '1%');
                                }
                                element.append($exeA);
                                $exeA.unbind('click').bind('click',function (ev) {
                                    ev.stopPropagation();
                                    App.confirm('确定执行当前任务？',function () {
                                        $.http.post('/biz/kpiRevise/executeCalcTask',{id:data.id},function (res) {
                                            if(res.success){
                                                // App.alert('执行成功');
                                                $('#setting_kr_list').GridTableRefreshPage();
                                            }else{
                                                App.alert('执行失败');
                                            }
                                        })
                                    });
                                })
                            }
                        }
                    },

                ]
            });
        },
        /**
         * 初始化事件
         */
        initEvent: function () {
            var self = this;
            var $create = $('#setting_kr_btn').find('input[type=button]').eq(0);

            $create.off('click').on('click', function () {
                self.showTaskDialog();
            })
        },
        showTaskDialog: function (recod) {
            //recod = recod || null;
            var $content = $('<div id="setting_dr_create_content"></div>');//內容元素
            App.dialog({
                id: 'setting_dr_dialog_id',
                title: "KPI重计算",//弹窗标题 <br>
                width: '40%',//弹窗内容宽度，不支持% <br>
                height: 'auto',//弹窗内容高度,不支持%  <br>
                maxHeight: '400',//弹窗内容最大高度, 不支持% <br>
                buttons: [
                    {
                        id: 'okId',
                        type: 'submit',
                        text: recod && '修改任务' || '创建任务',
                        click: function (e, dialog) {
                            dialog.find('form').submit();
                        }
                    },
                    {
                        id: 'cancelId',
                        type: 'cancel',
                        text: Msg.cancel || 'Cancel',
                        clickToClose: true
                    }
                ]
            }).append($content);
            var timeNameArr = ['startDate', 'endDate'];//选择时间的名称数组
            function dateFormart(element, value, data) {
                var tempName = element.attr('name');
                var tmpIndex = timeNameArr.indexOf(tempName);
                var otherName = timeNameArr[1 - tmpIndex];
                var otherEl = element.closest('form').find('input[name=' + otherName + ']');
                var maxDate = 6;//最多选择一周，最少选择同一天，限制时间
                if (value) {
                    element.val(Date.parse(value).format(Msg.dateFormat.yyyymmddhhss));
                }
                element.unbind('focus').bind('focus', function () {
                    var tmpMaxTime = '2099-12-31 23:59:59';
                    var tmpMinTime = '1900-01-01 00:00:00';
                    if (tmpIndex == 0) {
                        var tmpOtherTime = otherEl.val();
                        if (tmpOtherTime) {
                            tmpMaxTime = tmpOtherTime;
                            var tmpDate = new Date(tmpOtherTime);
                            tmpDate.setDate(tmpDate.getDate() - maxDate);
                            tmpMinTime = tmpDate.format(Msg.dateFormat.yyyymmddhhss);
                        }
                    } else if (tmpIndex == 1) {
                        var tmpOtherTime = otherEl.val();
                        if (tmpOtherTime) {
                            tmpMinTime = tmpOtherTime;
                            var tmpDate = new Date(tmpOtherTime);
                            tmpDate.setDate(tmpDate.getDate() + maxDate);
                            tmpMaxTime = tmpDate.format(Msg.dateFormat.yyyymmddhhss);
                        }
                    }
                    DatePicker({
                        dateFmt: Msg.dateFormat.yyyymmddhhss,
                        el: this,
                        isShowClear: true,
                        minDate: tmpMinTime,
                        maxDate: tmpMaxTime
                    });
                });
            }

            $content.ValidateForm('setting_dr_create_content', {
                //show: 'horizontal',
                noButtons: true,
                data: recod,
                type: recod && 'modify' || 'add',
                submitURL: recod && (main.serverUrl.biz + '/kpiRevise/modifyCalcTask') || (main.serverUrl.biz + '/kpiRevise/createCalcTask'),
                //fnSubmit: createDr,//执行查询的事件
                model: [
                    [{
                        input: 'input',
                        type: 'text',
                        //width:'100%',
                        show: '任务名称',//任务名称
                        rule: {
                            required: true
                        },
                        name: 'taskName',
                        //extend: {id: 'station_name'}
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        //width:'100%',
                        show: '选择电站',
                        rule: {
                            required: true
                        },
                        name: 'stationName',
                        fnInit: function (element, value, data) {
                            element.unbind('focus').bind('focus', function () {
                                IoUtil.showStationDialog(function (recods) {
                                    if (recods.length > 0) {
                                        debugger
                                        element.val(recods[0].stationName);
                                        element.data('station-code', recods[0]);
                                    } else {
                                        element.val('');
                                        element.removeData('station-code');
                                    }
                                }, element.data('station-code') && [element.data('station-code')] || null, true, '40%');
                            });
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        //width:'100%',
                        show: '开始时间',
                        name: 'startTime',
                        rule: {
                            required: true
                        },
                        extend: {class: 'Wdate'},
                        fnInit: dateFormart
                    }, {
                        input: 'input',
                        type: 'text',
                        rule: {
                            required: true
                        },
                        //width:'100%',
                        show: '结束时间',
                        name: 'endTime',
                        extend: {class: 'Wdate'},
                        fnInit: dateFormart
                    }]
                ],
                fnModifyData: function (data) {
                    for (var key in data) {
                        if (key == 'startTime' || key == 'endTime') {
                            data[key] = Date.parseTime(data[key], Msg.dateFormat.yyyymmddhhss);
                        }
                    }
                    var stationInfo = $('input[name=stationName]', '#setting_dr_dialog_id').data('station-code');
                    if (stationInfo) {
                        data.stationCode = stationInfo.stationCode;
                    } else {
                        data.stationCode = recod && recod.stationCode;
                    }
                    delete data['stationName'];
                    if (recod) {
                        data.id = recod.id;
                    }
                    return data;
                },
                fnSubmitSuccess: function () {
                    App.alert('成功');
                    //$content.closest('dialog').modal('hide');//关闭窗口
                    $('#setting_kr_list').GridTableRefreshPage();
                },
                fnSubmitError: function () {
                    App.alert('失败');
                }
            })
        }
    }
});