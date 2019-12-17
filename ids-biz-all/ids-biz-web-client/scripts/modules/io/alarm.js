'use strict';

App.Module.config({
    package: '/io',
    moduleName: 'alarm',
    description: '模块功能：智能运维——告警管理',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'idsInputTree', 'modules/io/iocommon'
    ]
});
App.Module('alarm', function ($) {
    var msgBegin = Msg.modules.io.alarm;//国际化语言的前缀io->alarm模块的国际化
    return {
        Render: function (params) {
            IoUtil.initPageLauge($('[lang]','#io_alarm'));//初始化界面上的信息
            this.searchBar();//初始查询菜单
            this.resultTable();//初始化表格信息
            this.initEvent();//初始事件
        },
        initEvent: function () {//初始化事件
            function getContent() {
                var $tb = $('<table border="1" bordercolor="#125a85" cellpadding="0" cellspacing="0" width="100%">' +
                    '<tr><td width="30%" align="center" height="50">电站名称</td><td ><input name="stationName" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">电站状态</td><td ><input name="stationState" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">缺陷编码</td><td ><input name="code" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">电站地址</td><td ><input name="stationAddr" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">其他字段</td><td ><input name="other" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">其他字段</td><td ><input name="otherValue" class="io_alarm_gj_input"/></td></tr>' +
                    '<tr><td width="30%" align="center" height="50">时间选择</td><td >' +
                    '<input name="startTime" onfocus="DatePicker({dateFmt: \'yyyy-MM-dd HH:mm:ss\'})">~' +
                    '<input name="endTime"onfocus="DatePicker({dateFmt: \'yyyy-MM-dd HH:mm:ss\'})">' +
                    '</td></tr>' +
                    '</table>');
                return $tb;
            }

            $('#io_alarm_search_bar  button').eq(1).click(function () {
                App.dialog({
                    title: "高级查询", content: getContent(), width: 800, height: 400,
                    buttons: [
                        {
                            id: 'gj_search', text: '查询', click: function () {

                        }, clickToClose: true
                        },
                        {id: 'gj_search_cancel', text: '取消', clickToClose: true}
                    ]
                });
            });////或者.append() 或者loadPage的方式加载一个页面;
            //App.dialog('close'); 关闭所有
            //$('#dialogid').dialog('close');// 关闭指定的对话框
            //监听操作按钮的点击事件
            $('#io_alarm #io_alarm_btns input[type=button]').unbind('click').bind('click', function () {
                var $this = $(this);
                var myType = $this.attr('myType');
                var records = $('#io_alarm_list').GridTableSelectedRecords();
                var len = records.length;
                if (myType == 'toDefect') {
                    if (len == 0 || len > 1) {
                        App.alert(len == 0 && msgBegin.toDefault.noSelect || msgBegin.toDefault.selectMutiRecod + len);
                        return;
                    }
                    //ajax请求去获取数据 type=1由告警转换为缺陷  type=2由智能告警转换为缺陷
                    $.http.post(main.serverUrl.biz + '/workFlow/alarmToDefect', {
                        alarmId: records[0].id,
                        type: 1
                    }, function (res) {
                        if (res.success && res.results && res.results != null) {
                            IoUtil.initShowDefautDialog(main.serverUrl.biz + '/workFlow/saveDefect', 'alarmToDefect', res.results, function (res) {
                                if (res.success) {
                                    $('#io_alarm_list').GridTableReload();//重新加载数据
                                    App.alert(msgBegin.toDefault.tranSuccess);//转换成功！
                                } else {
                                    App.alert(msgBegin.toDefault.tranFailed);//转换失败！
                                }
                            });
                        } else {
                            App.alert(msgBegin.toDefault.getFailed);//获取失败
                        }
                    }, function () {
                        App.alert(msgBegin.toDefault.serverError);//获取失败，请检测服务
                    });
                } else if (myType == 'sure' || myType == 'clearAlarm') {//确认 和清除 告警的按钮的事件
                    if (len == 0) {
                        App.alert(msgBegin.operate.noAlarm);//未选择记录
                        return;
                    }
                    var showText = msgBegin.operate.sure;//确定将选中的记录设置为确认状态？
                    var tmSubUrl = main.serverUrl.biz + '/alarm/confirm/ack';
                    var opterPre = msgBegin.operate.opterPre[0];//确认
                    if (myType == 'clearAlarm') {
                        showText = msgBegin.operate.doClear;//确定将选中的记录设置为清除状态？
                        tmSubUrl = main.serverUrl.biz + '/alarm/confirm/cleared';
                        opterPre = msgBegin.operate.opterPre[1];//清除
                    }
                    App.confirm(showText, function () {
                        var ids = [];
                        for (var i = 0; i < len; i++) {
                            ids.push(records[i].id);
                        }
                        $.http.post(tmSubUrl, ids, function (res) {
                            if (res.success) {
                                $('#io_alarm_list').GridTableReload();//重新加载数据
                                App.alert(opterPre + msgBegin.operate.operResult[0]);//确认(清除)成功
                            } else {
                                App.alert(opterPre + msgBegin.operate.operResult[1]);//确认(清除)失败
                            }
                        })
                    });

                }
            });
        },
        searchBar: function () {
            var _this = this;

            function search(data) {//submit提交的时候的方法
                var params = {};
                params.stationName = $("#io_alarm_search_bar #station_name").val().trim();// 电站名称
                params.alarmName = $("#io_alarm_search_bar #alarm_name").val().trim();//告警名称
                var alarmLevel = $("#io_alarm_search_bar select").eq(0).val();//告警级别
                params.alarmLevel = alarmLevel;
                var devAlias = $("#io_alarm_search_bar input[name=devAlias]").val();//设备名称
                params.devAlias = devAlias;
                data.params = params;
                $('#io_alarm_list').GridTableReload(data);
            }

            //调用form表单插件
            $("#io_alarm_search_bar").ValidateForm('io_alarm_search_bar', {
                show: 'horizontal',
                fnSubmit: search,
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: msgBegin.search.stationName,//电站名称
                        name: 'stationName',
                        extend: {id: 'station_name'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: msgBegin.search.alarmName,//告警名称
                        name: 'alarmName',
                        extend: {id: 'alarm_name'}
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: msgBegin.search.alarmLevel,//告警级别
                        name: 'alarmLevel',
                        options: [
                            {value: -1, text: Msg.all},
                            {value: 1, text: msgBegin.search.alarmLevelList[0]},//严重
                            {value: 2, text: msgBegin.search.alarmLevelList[1]},//重要
                            {value: 3, text: msgBegin.search.alarmLevelList[2]},//次要
                            {value: 4, text: msgBegin.search.alarmLevelList[3]}//提示
                        ],
                        extend: {id: 'alarm_level'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: msgBegin.search.devAlias,//设备名称
                        name: 'devAlias'
                    }
                ]],
                extraButtons: [
                    {
                        input: 'button',
                        type: 'close',
                        show: '',
                        name: '',
                        align: 'right',
                        width: '100%',
                        extend: {'class': 'btn btn-filter'}
                    }
                ]
            });
        },
        mininToData: function (value) {
            if (!value) {
                return "";
            }
            var date = new Date(value - 0);
            return this.getNowFormatDate(date);
        },
        //获取当前时间
        getNowFormatDate: function (date) {
            var seperator1 = "-";
            var seperator2 = ":";
            var month = this.getFirstIsZero(date.getMonth() + 1);
            var strDate = this.getFirstIsZero(date.getDate());
            var hours = this.getFirstIsZero(date.getHours());
            var minutes = this.getFirstIsZero(date.getMinutes());
            var seconds = this.getFirstIsZero(date.getSeconds());
            var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + hours + seperator2 + minutes
                + seperator2 + seconds;
            return currentdate;
        },
        getFirstIsZero: function (data) {
            if (data <= 9) {
                data = '0' + data;
            }
            return data;
        },
        showAlarmDetailContext: function (data) {//获取告警信息的详细信息的方法
            var content = '<table id="io_alarm_suggest" border="1" bordercolor="#125a85" cellspacing="0" cellpadding="0" width="100%">' +
                '<tr><td width="15%" align="center" height="50">' + msgBegin.search.alarmName + '</td>' +//告警名称
                '<td width="35%" align="center">' + data.alarmName + '</td>' +
                '<td width="15%" align="center">' + msgBegin.search.alarmLevel + '</td>' +//告警级别
                '<td width="35%" align="center">' + IoUtil.alarmLevelTransform(data.alarmLeve).showText + '</td></tr>' +

                '<tr><td align="center" height="50">' + msgBegin.alarmDetail.firstTime + '</td>' +//第一次发生时间
                '<td align="center">' + this.mininToData(data.firstDate) + '</td>' +
                '<td align="center">' + msgBegin.alarmDetail.lastTime + '</td>' +//最后一次发生时间
                '<td align="center">' + this.mininToData(data.lastDate) + '</td></tr>' +

                '<tr><td align="center" height="50">' + msgBegin.alarmDetail.alarmNum + '</td>' +//告警次数
                '<td align="center">' + (data.alarmNumber || '') + '</td>' +
                '<td align="center">' + msgBegin.alarmDetail.modelVersionName + '</td>' +//版本名称
                '<td align="center">' + (data.version || '') + '</td></tr>' +

                '<tr><td align="center" height="50">' + msgBegin.alarmDetail.alarmLocation + '</td>' +//告警定位
                '<td colspan="3"><label class="io_alarm_left_margin">' + (data.alarmPosition || '') + '</label></td>' +
                '</td></tr>' +

                '<tr><td align="center" height="80">' + msgBegin.search.devAlias + '</td>' +//设备名称
                '<td colspan="3"><label class="io_alarm_left_margin">' + (data.devAlias || '') + '</label></td>' +
                '</td></tr>' +

                '<tr><td align="center" height="80">' + msgBegin.alarmDetail.alarmCause + '</td>' +//告警原因
                '<td  colspan="3"><label class="io_alarm_left_margin">' + (data.alarmCause || '') + '</label></td>' +
                '</td></tr>' +

                '<tr><td align="center" height="80">' + msgBegin.alarmDetail.repearSuggestion + '</td>' +//修复建议
                '<td  colspan="3"><label class="io_alarm_left_margin">' + (data.repairSuggestion || '') + '</label></td>' +
                '</td></tr>' +
                '</table>';
            return content;
        },
        resultTable: function (params) {
            var self = this;
            //对checkbox做初始
            //显示详情的点击事件
            function showDetails(e, showData) {
                var e = e || event;
                e.stopPropagation();
                //1.根据id查询数据
                var id = showData.id;
                $.http.get(main.serverUrl.biz + '/alarm/repair?id=' + id, function (res) {
                    if (!res.success) {
                        App.alert({title: "提示", message: "获取告警修复建议失败"});
                        return;
                    }
                    App.dialog({
                        //id:'',
                        title: "告警修复建议",
                        width: 1000,
                        height: 500,
                        content: self.showAlarmDetailContext(res.data),
                        buttons: [{text: '关闭', clickToClose: true}]
                    });
                    //或者.append();
                    //App.dialog('close'); 关闭所有
                    //$('#dialogid').dialog('close');// 关闭指定的对话框
                });
            }

            function alarmDetail(element, vale, data) {
                element.empty();
                var $tma = $('<a tmpName="' + vale + '">详情信息</a>');
                $tma.unbind('cllick').bind('click', function (ev) {
                    showDetails(ev, data)
                });
                element.attr('title', '详情信息');
                element.append($tma);
            }

            //告警级别的格式化
            function alarmLevel(element, value, data) {
                element.empty();
                var obj = IoUtil.alarmLevelTransform(value);
                element.attr('title', obj.titleMsg);
                var span = $(obj.showText);
                element.append(span);
            }

            //告警状态：1 未处理 2 已确认 3 处理中 4 已处理 5 已清除 6 已恢复
            function alarmStatusFormart(element, value, data) {
                element.empty();
                var text = IoUtil.alarmStatusFormart(value);
                element.html(text);
                element.attr('title', text);
            }

            function dataFormart(element, value, data) {//转换为时间
                var dateStr = self.mininToData(value);
                element.empty();
                element.html(dateStr);
                element.attr('title', dateStr);
            }

            $('#io_alarm_list').GridTable({
                url: main.serverUrl.biz + '/alarm/list',
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
                        display: msgBegin.search.stationName,//电站名称
                        name: 'stationName',
                        //align: "center",
                        width: 0.2,
                    },
                    {
                        display: msgBegin.search.alarmName,//告警名称
                        name: 'alarmName',
                        //align: "center",
                        width: 0.14,
                    },
                    {
                        display: msgBegin.search.alarmLevel,//告警级别
                        name: 'alarmLevel',
                        align: "center",
                        width: 0.08,
                        filter: {
                            type: 'enum',
                            options: [
                                {text: msgBegin.search.alarmLevelList[0], value: "1"},//严重
                                {text: msgBegin.search.alarmLevelList[1], value: "2"},//重要
                                {text: msgBegin.search.alarmLevelList[2], value: "3"},//次要
                                {text: msgBegin.search.alarmLevelList[3], value: "4"}//提示
                            ]
                        },
                        fnInit: alarmLevel
                    },
                    {
                        display: msgBegin.search.devAlias,//设备名称
                        name: 'devAlias',
                        //align: "center",
                        width: 0.14,
                        // fnInit: initCheckBox
                    },
                    // {
                    //     display:'设备类型',
                    //     name: 'deviceType',
                    //     //align: "center",
                    //     width: 0.1,
                    //     // fnInit: initCheckBox
                    // },
                    {
                        display: msgBegin.tableList.alarmState,//告警状态
                        name: 'alarmState',
                        //align: "center",
                        width: 0.08,
                        fnInit: alarmStatusFormart
                    },
                    {
                        display: msgBegin.tableList.createDate,//发生时间
                        name: 'createDate',
                        //align: "center",
                        width: 0.15,
                        fnInit: dataFormart
                    },
                    {
                        display: msgBegin.alarmDetail.repearSuggestion,//修复建议
                        name: 'id',
                        //align: "center",
                        //width: 0.12,
                        fnInit: alarmDetail
                    }
                ]
            });
        }
    }
});