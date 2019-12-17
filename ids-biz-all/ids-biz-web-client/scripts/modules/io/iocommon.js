var IoUtil = {
    //获取时间的信息
    mininToData: function (value) {
        if (!value) {
            return "";
        }
        var date = new Date(value);
        //return this.getNowFormatDate(date);
        return date.format(Msg.dateFormat.yyyymmddhhss);
    },
    //获取当前时间
    // getNowFormatDate: function (date) {
    //     var seperator1 = "-";
    //     var seperator2 = ":";
    //     var month = this.getFirstIsZero(date.getMonth() + 1);
    //     var strDate = this.getFirstIsZero(date.getDate());
    //     var hours = this.getFirstIsZero(date.getHours());
    //     var minutes = this.getFirstIsZero(date.getMinutes());
    //     var seconds = this.getFirstIsZero(date.getSeconds());
    //     var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
    //         + " " + hours + seperator2 + minutes
    //         + seperator2 + seconds;
    //     return currentdate;
    // },
    // getFirstIsZero: function (data) {
    //     if (data <= 9) {
    //         data = '0' + data;
    //     }
    //     return data;
    // },
    showAlarmDetailContext: function (data) {//获取告警信息的详细信息的方法
        var content = '<table id="io_alarm_suggest" border="1" bordercolor="#125a85" cellspacing="0" cellpadding="0" width="100%">' +
            '<tr><td width="15%" align="center" height="50">告警名称</td>' +
            '<td width="35%" align="center">' + data.alarmName + '</td>' +
            '<td width="15%" align="center">告警级别</td>' +
            '<td width="35%" align="center">' + this.alarmLevelTransform(data.alarmLevel).showText + '</td></tr>' +

            '<tr><td align="center" height="50">第一次发生时间</td>' +
            '<td align="center">' + this.mininToData(data.firstAlarmTime) + '</td>' +
            '<td align="center">最后一次发生时间</td>' +
            '<td align="center">' + this.mininToData(data.lastAlarmTime) + '</td></tr>' +

            '<tr><td align="center" height="50">告警次数</td>' +
            '<td align="center">' + data.alarmTime + '</td>' +
            '<td align="center">版本名称</td>' +
            '<td align="center">' + data.signalVersion + '</td></tr>' +

            '<tr><td align="center" height="50">告警定位</td>' +
            '<td colspan="3"><label class="io_alarm_left_margin">' + data.alarmAddr + '</label></td>' +
            '</td></tr>' +

            '<tr><td align="center" height="80">设备名称</td>' +
            '<td colspan="3"><label class="io_alarm_left_margin">' + data.devAlias + '</label></td>' +
            '</td></tr>' +

            '<tr><td align="center" height="80">告警原因</td>' +
            '<td  colspan="3"><label class="io_alarm_left_margin">' + data.alarmCause + '</label></td>' +
            '</td></tr>' +

            '<tr><td align="center" height="80">修复建议</td>' +
            '<td  colspan="3"><label class="io_alarm_left_margin">' + data.sugession + '</label></td>' +
            '</td></tr>' +
            '</table>';
        return content;
    },
    alarmLevelTransform: function (value) {//告警转换
        var showText = "";
        var titleMsg = "";
        if (value == 1) {
            titleMsg = "严重";
            showText = '<span style="color: #f00;">' + "严重" + '</span>';
        }
        else if (value == 2) {
            titleMsg = "重要";
            showText = '<span style="color: #d48949">' + "重要" + '</span>';
        }
        else if (value == 3) {
            titleMsg = "次要";
            showText = '<span style="color: #af9b3d;">' + "次要" + '</span>';
        }
        else if (value == 4) {
            titleMsg = "提示";
            showText = '<span style="color: #7067ac;">' + "提示" + '</span>';
        } else {
            titleMsg = "未知";
            showText = '<span style="color: #7067ac;">' + "未知" + '</span>';
        }
        var obj = {};
        obj.titleMsg = titleMsg;
        obj.showText = showText;
        return obj;
    },
    /**
     * 获取告警状态的样式 1 未处理 2 已确认 3 处理中 4 已处理 5 已清除 6 已恢复
     * @param value
     */
    alarmStatusFormart: function (value) {
        var text = '';
        if (value == 1) {
            text = '未处理';
        }
        else if (value == 2) {
            text = '已确认';
        }
        else if (value == 3) {
            text = '处理中';
        }
        else if (value == 4) {
            text = '已处理';
        } else if (value == 5) {
            text = '已清除';
        } else if (value == 6) {
            text = '已恢复';
        } else {
            text = '未知';
        }
        return text;
    },
    /**
     * 获取告警类型
     * @param value 1 告警 2 事件 3 自检
     * @returns {string}
     */
    getAlarmType: function (value) {
        var result = '';
        if (!value) {
            result = '';
        }
        else if (value == 1) {
            result = '告警';
        }
        else if (value == 2) {
            result = '事件';
        }
        else if (value == 3) {
            result = '自检';
        }
        return result;
    },
    /**
     * 获取流程状态
     * @param value  流程状态 0.待审核 1.已退回 2. 消缺中 3 待确认 4. 已完成
     */
    flowState: function (value) {
        var msgText,msgTitle;
        if (value == 0) {
            msgTitle = '待审核';
            msgText = '<span style="color: #b8c3cf;">'+msgTitle+'</span>';
        }
        else if (value == 1) {
            msgTitle = '已回退';
            msgText = '<span style="color: #dd2a13;">'+msgTitle+'</span>';
        }
        else if (value == 2) {
            msgTitle = '消缺中';
            msgText = '<span style="color: #a0946f;">'+msgTitle+'</span>';
        } else if (value == 3) {
            msgTitle = '待确认';
            msgText = '<span style="color: #5581a8;">'+msgTitle+'</span>';
        } else if (value == 4) {
            msgTitle = '已完成';
            msgText = '<span style="color: #a0946f;">'+msgTitle+'</span>';
        }else{
            msgTitle = '未知';
            msgText = '<span style="color: #7067ac;">'+msgTitle+'</span>';
        }

        var obj = {};
        obj.msgText = msgText;
        obj.msgTitle = msgTitle;
        return obj;
    },
    /**
     * 告警处理结果0:'完成',1:'处理中',2:'未处理'
     * @param value
     */
    alarmOperatResult: function (value) {
        if (value == 0) {
            return '完成';
        } else if (value == 1) {
            return '处理中';
        }
        return '未处理';
    },
    /**
     * 缺陷出来结果  1:未完全消除 2:已完全消除 3:不处理
     * @param value
     */
    defectOperatResult: function (value) {
        if (value == 2) {
            return '已完全消除';
        } else if (value == 3) {
            return '不处理';
        }
        return '未完全消除';
    },
    // 获取性别 0： 男； 1：女
    getSex: function (value) {
        if (value == 0) {
            return "男";
        }
        return "女"
    },
    //获取完成状态 0 否 1：是
    getFinishStatus: function (value) {
        if (value == 1) {
            return "是";
        }
        return "否";
    },
    /**
     * 根据时间的维度获取当天所在的年、月、日的时间的long型数据
     * @param timeDim day:天 month：月 year：年
     */
    getCurrentTime: function (timeDim) {
        var dateFormat = this.getFormartOfTimeDiv(timeDim);
        return Date.parseTime(new Date().format(dateFormat), dateFormat);
    },
    /**
     * 根据时间维度确认年月日的时间格式化的内容
     * @param timeDim
     * @returns {*}
     */
    getFormartOfTimeDiv:function (timeDim) {
        var timeFormat ;
        if (timeDim == 'month') {
            timeFormat = Msg.dateFormat.yyyymm;//yyyy-MM
        } else if (timeDim == 'year') {
            timeFormat = Msg.dateFormat.yyyy;//yyyy
        }else{
            timeFormat = Msg.dateFormat.yyyymmdd;//yyyy-MM-dd
        }
        return timeFormat;
    },
    /**
     * 根据时间维度获取当前时间的时间戳
     * @param timeStr
     * @param timeDim day:天 month：月 year：年
     * @returns {number}
     */
    getCurentTimeOfStr: function (timeStr, timeDim) {
        return Date.parseTime(timeStr, this.getFormartOfTimeDiv(timeDim));
    },
    /**
     * 查询当前的时间纬度的时间
     * @param time
     * @param timeDiv
     * @returns {*|String}
     */
    getCurrentDateForm: function (time, timeDiv) {
        return Date.parse(time).format(this.getFormartOfTimeDiv(timeDiv));
    },
    /**
     * 根据时间纬度获取当前的中文的年/月/日
     * @param time
     * @param timeDiv
     * @returns {*|String}
     */
    getChinessStr: function (time, timeDiv) {
        if (timeDiv == 'month') {
            return new Date(time).format('yyyy年MM月');
        }
        if (timeDiv == 'year') {//返回当前的年
            return new Date(time).format('yyyy年');
        }
        return new Date(time).format('yyyy年MM月dd日');
    },
    /**
     *  获取当前时间纬度的下一个纬度
     * @param time
     * @param timeDiv 年 月 日
     * @returns {*|String}
     */
    getCurrentDateVimAfterForm: function (time, timeDiv) {
        if (timeDiv == 'month') {//返回月的时间 就是日的格式化
            return Date.parse(time).format(Msg.dateFormat.yyyymmdd);
        }
        if (timeDiv == 'year') {//返回当前的年的下一个纬度是月
            return Date.parse(time).format(Msg.dateFormat.yyyymm);
        }
        return Date.parse(time).format(Msg.dateFormat.yyyymmddhhss);//获取当前的小时
    },
    /**
     * 弹出选择电站的选择框  先不考虑回显的情况
     * @param fnCallback 选择了电站后的回调函数
     * @param isSig   true:单选  false：多选
     * @param width 宽 如果不填默认是 60% 支持百分比和像素的数字
     * @param height 高 目前只支持像素的数字值，如果没有默认是600
     */
    showStationDialog: function (fnCallback, selectRecodes, isSig, width, height) {
        isSig = !!isSig;
        width = width || '60%';
        height = height || 600;
        var $content = $('<div/>').attr('id', 'my_show_choose_station_content');
        App.dialog({
            id: 'my_show_choose_station_dialog',
            width: width,
            height: 'auto',
            maxHeight: height, //弹窗内容最大高度, 不支持%
            content: $content,
            buttons: [
                {
                    id: 'okId',
                    type: 'submit',
                    text: Msg.sure || 'OK',
                    click: function (e, dialog) {
                        var recodes = $('#my_show_choose_station_content__list').GridTableSelectedRecords();
                        fnCallback && $.isFunction(fnCallback) && fnCallback(recodes);
                        dialog.modal('hide');//关闭窗口
                    }
                },
                {
                    id: 'cancelId',
                    type: 'cancel',
                    text: Msg.cancel || 'Cancel',
                    clickToClose: true
                }
            ]
        });
        //搜索栏
        var barSearch = $('<div/>').attr('id', 'my_show_choose_station_content_search_bar').addClass('toolbar');
        $content.append(barSearch);

        function search(data) {//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
            for (var key in data) {
                if (key == 'params') {
                    continue;
                }
                data.params[key] = data[key];
            }
            //TODO 组装数据
            $('#my_show_choose_station_content__list').GridTableReload(data);
        }

        barSearch.ValidateForm('my_show_choose_station_content_search_bar', {
            show: 'horizontal',
            fnSubmit: search,//执行查询的事件
            model: [[
                {
                    input: 'input',
                    type: 'text',
                    show: '电站名称',//账号名称
                    name: 'stationName',
                }
            ]]
        });
        var tableDiv = $('<div/>').attr('id', 'my_show_choose_station_content__list');
        $content.append(tableDiv);
        tableDiv.GridTable({//TODO 修改请求的路径
            url: main.serverUrl.biz + '/station/getStationInfo',
            title: false,
            width: '0.06',
            // ellipsis: true,
            maxHeight: height - 193,
            theme: 'ThemeA',
            height: 'auto',
            max_height: height - 193,
            params: {},
            singleSelect: isSig,
            clickSelect: true,//有全选按钮
            isRecordSelected: true,
            isSearchRecordSelected: false,
            showSelectedName: false,
            resizable: true,
            objectsModel: true,
            onLoadReady: function () {//选中的数据
                if (selectRecodes && selectRecodes.length > 0) {
                    tableDiv.GridTableInitSelectedRecords(selectRecodes);
                }
            },
            idProperty: 'stationCode',
            colModel: [
                {
                    display: '电站名称',
                    name: 'stationName',
                    //align: "center",
                    width: 0.4,
                    //fnInit:getBlueColor
                },
                {
                    display: '电站地址',
                    name: 'stationAddr',
                    //align: "center",
                    width: 0.3,
                    //fnInit:getBlueColor
                },
                {
                    display: '装机容量',
                    name: 'capacity',
                    //align: "center",
                    width: 0.3,
                },
            ]
        })
    },
    /**
     * 弹出选择设备的弹出框
     * @param fnCallback 选择确定的回调函数
     * @param isMuti 是否是多选 默认是单选
     * @param url:查询设备信息的url
     */
    showDevSelectDialog: function (fnCallback, isMuti, url) {
        isMuti = !isMuti;
        url = url || (main.serverUrl.dev + '/device/getDeviceByCondition');
        var $content = $('<div/>').attr('id', 'my_choose_dev_dialog_content');
        App.dialog({
            title: '选择设备',
            id: 'my_choose_dev_dialog',
            width: '60%',
            height: 'auto',
            maxHeight: 600, //弹窗内容最大高度, 不支持%
            content: $content,
            buttons: [
                {
                    id: 'okId',
                    type: 'submit',
                    text: Msg.sure || 'OK',
                    click: function (e, dialog) {
                        var recodes = $('#my_choose_dev_dialog_content_list').GridTableSelectedRecords();
                        // if (recodes.length == 0) {
                        //     App.alert('请选择设备');
                        //     return;
                        // }
                        fnCallback && $.isFunction(fnCallback) && (isMuti ? fnCallback(recodes[0]) : fnCallback(recodes));
                        dialog.modal('hide');//关闭窗口
                    }
                },
                {
                    id: 'cancelId',
                    type: 'cancel',
                    text: Msg.cancel || 'Cancel',
                    clickToClose: true
                }
            ]
        });
        //搜索栏
        var barSearch = $('<div/>').attr('id', 'my_choose_dev_dialog_content_search_bar').addClass('toolbar');
        $content.append(barSearch);

        //调用搜索栏的方法,查询设备的列表
        function search(data) {
            for (var key in data) {
                if (key == 'params') {
                    continue;
                }
                data.params[key] = data[key];
            }
            $('#my_choose_dev_dialog_content_list').GridTableReload(data);
        }

        barSearch.ValidateForm('my_choose_dev_dialog_content_search_bar', {
            show: 'horizontal',
            fnSubmit: search,
            model: [[
                {
                    input: 'input',
                    type: 'text',
                    show: '电站名称',//电站名称
                    name: 'stationName',
                },
                {
                    input: 'select',
                    type: 'select',
                    show: '设备类型',//设备类型
                    name: 'devTypeId',
                    width: 150,
                    options: [{value: -1, text: '全部'}],
                    fnInit: function (element, value, data) {//查询后台的数据
                        //通过ajax请求去获取数据
                        //查询所有的设备型号 ajax请求，这里就使用一个固定的值
                        element.empty();
                        element.append($('<option value="-1">全部</option>'));
                        $.http.get(main.serverUrl.dev + '/settings/query', function (res) {
                            if (res.success) {
                                var devtypes = res.results;
                                for (var key in devtypes) {
                                    if (devtypes.hasOwnProperty(key)) {
                                        element.append($('<option value=' + devtypes[key] + '>' + key + '</option>'));
                                    }
                                }

                            }
                        });
                    }
                },
                {
                    input: 'input',
                    type: 'text',
                    show: '设备名称',//设备名称
                    name: 'devAlias',
                },
            ]]
        });

        //表格
        var myTableDiv = $('<div/>').attr('id', 'my_choose_dev_dialog_content_list');
        $content.append(myTableDiv);
        myTableDiv.GridTable({
            url: url,
            title: false,
            width: '0.08',
            // ellipsis: true,
            maxHeight: 380,
            theme: 'ThemeA',
            height: 'auto',
            max_height: 380,
            params: {},
            clickSelect: true,//有全选按钮
            singleSelect: isMuti,
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
                    width: 0.3,
                    //fnInit:getBlueColor
                },
                {
                    display: '设备名称',
                    name: 'devAlias',
                    //align: "center",
                    width: 0.3,
                },
                {
                    display: '设备类型',
                    name: 'devTypeName',
                    //align: "center",
                    width: 0.2,
                    //fnInit:getBlueColor
                },
                {
                    display: '设备版本号',
                    name: 'signalVersion',
                    //align: "center",
                    width: 0.2,
                    //fnInit:getBlueColor
                }
            ]
        })
    },
    /**
     * 任务的弹出框，里面有任务的详细信息，和任务执行了那些操作：如果是查看就不能处理，如果是处理，就可以去执行
     * @param showData 展示的数据
     * @param type 类型：1：详情 2：执行任务
     * @param subUrl 提交后台的数据
     * @param fnCallback 提交之后的callback事件,在执行的后的回调函数，如果是查询可以不给
     */
    showTaskDialog: function (showData, type, subUrl, fnCallback) {
        var self = this;
        var $content = $('<div/>').attr('id', 'my_task_workflow_content');
        var dialogOpts = {
            title: type == 1 ? '任务详情' : '执行任务',
            id: 'my_task_workflow_dialog',
            width: '60%',
            height: 'auto',
            maxHeight: 755, //弹窗内容最大高度, 不支持%
            content: $content
        };
        var subType = '';//1:同意 2:驳回
        if (subUrl) {
            dialogOpts.buttons = [
                {
                    id: 'okId',
                    type: 'submit',
                    text: '同意',
                    click: function (e, dialog) {
                        subType = 1;//同意
                        $(dialog).find('form').submit();
                    }
                },
                {
                    id: 'cancelId',
                    type: 'cancel',
                    text: '驳回',
                    //clickToClose: true
                    click: function (e, dialog) {
                        subType = 2;//驳回
                        $(dialog).find('form').submit();
                    }
                }
            ]
        } else {
            dialogOpts.buttons = [
                {
                    id: 'cancelId',
                    type: 'cancel',
                    text: '关闭',
                    clickToClose: true
                }
            ]
        }
        App.dialog(dialogOpts);

        var validateOpts = {
            noButtons: true,
            data: showData,//回显的数据
            type: type == 1 ? 'view' : 'modify',
            model: [
                [{
                    input: 'input',
                    type: 'text',
                    show: '电站名称',
                    name: 'stationName',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }, {
                    input: 'input',
                    type: 'text',
                    show: '设备名称',
                    name: 'devAlias',
                    fnInit: function ($ele, value, data) {
                        $ele.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '设备类型',
                    name: 'deviceType',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }, {
                    input: 'input',
                    type: 'text',
                    show: '设备型号',
                    name: 'neVersion',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '发现时间',
                    name: 'findTime',
                    extend: {class: 'Wdate'},
                    fnInit: function ($ele, value) {
                        if (value) {
                            $ele.val(new Date(value - 0).format(Msg.dateFormat.yyyymmddhhss));
                        }
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '发现人',
                    name: 'finder',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '附件',
                    name: 'fileId',
                    fnInit: function (element, value, data) {
                        element.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'textarea',
                    type: 'textarea',
                    show: '缺陷描述',
                    name: 'defectDesc',
                    fnInit: function (element, value, data) {
                        element.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'div',
                    type: 'div',
                    show: '流程状态',
                    name: 'procState',
                    fnInit: function (element, value, data) {
                        element.empty();
                        element.attr('readonly', 'readonly');
                        var obj = self.flowState(value);
                        element.append(obj.msgText).attr('title',obj.msgTitle).parent().attr('title',obj.msgTitle);
                    }
                }],
                [{
                    input: 'group',
                    type: 'group',
                    name: 't1',
                    fnInit: function (element, value, data) {
                        //element.attr('id','my_show_task_list_infos');
                        element.empty();
                        var $aoutDiv = $('<div/>').text('+ 任务详情').css({'text-indent': '15px'});
                        element.append($aoutDiv);
                        var $tmpGridDiv = $('<div/>').css({
                            width: '100%'
                        });
                        element.append($tmpGridDiv);
                        $tmpGridDiv.hide();

                        function dateFormate(element, value, data) {
                            if (value) {
                                var str = new Date(value).format(Msg.dateFormat.yyyymmddhhss);
                                element.text(str).attr('title', str).parent().attr('title', str);
                            }
                        }

                        $tmpGridDiv.GridTable({
                            title: false,
                            width: '0.1',
                            // ellipsis: true,
                            maxHeight: 199,
                            theme: 'ThemeA',
                            height: 'auto',
                            line_height: "30",
                            max_height: 199,
                            data: data.tasks,
                            pageBar: false,
                            isRecordSelected: false,
                            isSearchRecordSelected: false,
                            showSelectedName: false,
                            resizable: true,
                            objectsModel: true,
                            idProperty: 'id',
                            colModel: [
                                {
                                    display: '任务内容',
                                    name: 'taskContent',
                                    width: 0.2,
                                },
                                {
                                    display: '任务状态',
                                    name: 'taskState',
                                    width: 0.08,
                                    fnInit: function (element, value) {
                                        element.empty();
                                        var str = '未完成';
                                        if(value==1){
                                            str = '已完成';
                                        }
                                        element.append(str).attr('title', str).parent().attr('title', str);
                                    }
                                },
                                {
                                    display: '执行人用户姓名',
                                    name: 'assigneeName',
                                    width: 0.1,
                                },
                                {
                                    display: '接收人姓名',
                                    name: 'recipient',
                                    width: 0.1,
                                },
                                {
                                    display: '任务开始时间',
                                    name: 'taskStartTime',
                                    width: 0.15,
                                    fnInit: dateFormate
                                },
                                {
                                    display: '任务结束时间',
                                    name: 'taskEndTime',
                                    width: 0.15,
                                    fnInit: dateFormate
                                },
                                {
                                    display: '操作描述',
                                    name: 'operationDesc',
                                    width: 0.15,
                                }
                            ]
                        });
                        $aoutDiv.unbind('click').bind('click', function () {
                            $tmpGridDiv.toggle();
                            if ($tmpGridDiv.is(':hidden')) {
                                $aoutDiv.text('+ 任务详情');
                            } else {
                                $aoutDiv.text('- 任务详情');
                            }
                        })
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '任务简介',
                    name: 'taskContent',
                    fnInit: function (element, value, data) {
                        type != 2 && element.attr('readonly', 'readonly');
                        data.tasks && data.tasks.length > 0 && element.val(data.tasks[0].taskContent);
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '流转操作',
                    name: 'userId',
                    fnInit: function (element, value, data) {
                        if (type == 2) {
                            element.idsInputTree({
                                url: main.serverUrl.biz + '/user/getEnterpriseUser',
                                checkStyle: 'radio',
                                clickPIsChecked: true,
                                textFiled: "loginName",
                                //selectNodes: tmpVal && [tmpVal] || [],//被选中的记录
                                dataLevel: true,
                                treeSearch: true,
                                click2hidden: true,
                                rootCheckable: true
                            });
                        } else {
                            element.attr('readonly', 'readonly');
                        }
                    }
                }]

            ]
        };
        if (subUrl) {
            validateOpts.model.push([{
                input: 'textarea',
                type: 'textarea',
                show: '流转意见',
                name: 'operationDesc',
            }]);
            validateOpts.submitURL = subUrl;
            //TODO 提交之前和提交之后的处理
            validateOpts.fnModifyData = function (data) {
                var temp = {};
                temp.defectId = showData.defectId;
                temp.operationDesc = data.operationDesc;//流转意见
                temp.optType = subType;//1:同意 2：不同意
                temp.userId = $('#my_task_workflow_content input[name=userId]').attr('treeselid');
                temp.reviceUserName = $('#my_task_workflow_content input[name=userId]').val();
                temp.procState = showData.procState;
                temp.procId = showData.procId;
                temp.taskContent = data.taskContent;
                return temp;
            }
            validateOpts.fnSubmitSuccess = fnCallback;
            validateOpts.fnSubmitError = function () {
                App.alert('操作失败');
            }
        }
        $content.ValidateForm('my_task_workflow_content', validateOpts);
    },
    /**
     * 弹出缺陷对话框
     * @param url 保存缺陷的url
     * @param myType 类型 add 和 modyfiy
     * @param showData 如果是修改，在界面回显的数据
     * @param successCallBack 修改成功的回调函数
     */
    initShowDefautDialog: function (url, myType, showData, successCallBack) {
        var type = myType == 'add' ? 'add' : 'modify';//只有新增和修改（复制）
        var $content = $('<div/>').attr('id', 'io_defect_add_content');
        if ($.isFunction(showData)) {//如果第三个参数(showData)是方法，就是新增成功的回调函数，所以showData==null
            successCallBack = showData;
            showData = null;
        }
        App.dialog({
            title: '新增缺陷',
            id: 'io_defect_add_dialog',
            width: '60%',
            height: 'auto',
            maxHeight: 600, //弹窗内容最大高度, 不支持%
            content: $content,
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
        });
        $content.ValidateForm('io_defect_add_content', {
            noButtons: true,
            submitURL: url,//表单提交的路径
            data: showData,//回显的数据
            type: type,
            model: [
                [{
                    input: 'input',
                    type: 'text',
                    show: '电站名称',
                    name: 'stationName',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }, {
                    input: 'input',
                    type: 'text',
                    show: '设备名称',
                    rule: {
                        required: true
                    },
                    name: 'devAlias',
                    fnInit: function ($ele, value, data) {
                        value && $ele.val(data.devAlias);
                        if (myType == 'add' || myType == 'modify') {//如果是新增才会去做选中设备信息
                            $ele.unbind('focus').bind('focus', function () {
                                IoUtil.showDevSelectDialog(function (recode) {
                                    $('#io_defect_add_content input[name=stationName]').val(recode.stationName);
                                    $('#io_defect_add_content input[name=deviceType]').val(recode.devTypeName);
                                    $ele.val(recode.devAlias);
                                    $('#io_defect_add_content input[name=deviceVersion]').val(recode.signalVersion);
                                    $ele.data('select_dev_info_data', recode);
                                });
                            });
                        } else {
                            $ele.attr('readonly', 'readonly');
                        }
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '设备类型',
                    name: 'deviceType',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }, {
                    input: 'input',
                    type: 'text',
                    show: '设备型号',
                    name: 'deviceVersion',
                    fnInit: function (element) {
                        element.attr('readonly', 'readonly');
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '发现时间',
                    name: 'firstHappenTime',
                    extend: {class: 'Wdate'},
                    fnInit: function ($ele, value) {
                        if (value) {
                            $ele.val(new Date(value - 0).format(Msg.dateFormat.yyyymmddhhss));
                        }
                        $ele.off('focus').on('focus', function () {
                            DatePicker({dateFmt: Msg.dateFormat.yyyymmddhhss, el: this, isShowClear: true});
                        })
                    }
                }, {
                    input: 'input',
                    type: 'text',
                    show: '结束时间',
                    name: 'endTime',
                    extend: {class: 'Wdate'},
                    fnInit: function ($ele, value) {
                        if (value) {
                            $ele.val(new Date(value - 0).format(Msg.dateFormat.yyyymmddhhss));
                        }
                        $ele.off('focus').on('focus', function () {
                            DatePicker({dateFmt: Msg.dateFormat.yyyymmddhhss, el: this, isShowClear: true});
                        })
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '发现人',
                    name: 'finder',
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '附件',
                    name: 'fileId',
                    fnInit: function (element, value, data) {
                        if (value) {
                            element.data('defect_upload_file_id', value);
                        }
                        element.addClass('upload-url');
                        var parenmDiv = $('<div/>').addClass('upload');
                        element.wrap(parenmDiv);//使用div来包裹这个元素
                        var upload = $('<input type="button" class="upload-btn" value="浏览">' +
                            '<input type="file" name="mypic" id="defect_busi_file_img" class="file upload-input-file">');
                        element.after(upload);
                        $('#defect_busi_file_img').unbind('change').bind('change', function () {
                            var val = this.value;
                            var tmpStr = '';
                            if (val && val != null && val != '') {
                                tmpStr = val.substring(val.lastIndexOf('\\') + 1, val.length);
                            }
                            element.val(tmpStr);
                        });
                        var btn = $('<input type="button"/>').addClass('btn').val('上传');
                        $('#defect_busi_file_img').parent().after(btn);
                        btn.unbind('click').bind('click', function () {
                            var filePath = $('#defect_busi_file_img').val();
                            if (filePath == '') {
                                App.alert('未选择文件');
                                return;
                            }
                            $.ajaxFileUpload({
                                url: main.serverUrl.biz + '/fileManager/fileUpload',
                                secureuri: false, //是否需要安全协议，一般设置为false
                                fileElementId: 'defect_busi_file_img', //文件上传域的ID
                                data: data.fileId && {fileId: data.fileId} || {},
                                dataType: 'json', //返回值类型 一般设置为json
                                success: function (data, status)  //服务器成功响应处理函数
                                {
                                    if (data.code == 1 && data.results) {
                                        App.alert('上传成功！');
                                        element.data('defect_upload_file_id', data.results);
                                    } else {
                                        App.alert('上传失败！');
                                    }
                                },
                                error: function (data, status, e)//服务器响应失败处理函数
                                {
                                    App.alert('上传失败！出现异常');
                                }
                            })
                        })
                    }
                }],
                [{
                    input: 'textarea',
                    type: 'textarea',
                    show: '缺陷描述',
                    name: 'description',
                    rule: {
                        required: true
                    }
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '任务简介',
                    name: 'taskContent',
                    rule: {
                        maxlength: 20,
                        required: true
                    },
                }],
                [{
                    input: 'input',
                    type: 'text',
                    show: '流转操作',
                    rule: {
                        required: true
                    },
                    name: 'userId',
                    fnInit: function (element, value, data) {
                        var tmpVal = data && data.auditor && data.auditor.id;
                        if (tmpVal) {
                            element.val(data.auditor && data.auditor.loginName || '');
                            element.attr('treeselid', data.auditor && data.auditor.id || '');
                        }
                        element.idsInputTree({
                            url: main.serverUrl.biz + '/user/getEnterpriseUser',
                            checkStyle: 'radio',
                            clickPIsChecked: true,
                            textFiled: "loginName",
                            selectNodes: [tmpVal],//被选中的记录
                            dataLevel: true,
                            treeSearch: true,
                            click2hidden: true,
                        });
                    }
                }],
                [{
                    input: 'textarea',
                    type: 'textarea',
                    show: '流转意见',
                    name: 'operationDesc',
                    rule: {
                        required: true
                    }
                }]
            ],
            fnModifyData: function (data) {
				debugger
                var recd = $('#io_defect_add_content input[name=devAlias]').data('select_dev_info_data');
                if (recd) {//如果是选择了
                    data.devId = recd.id;
                    data.stationCode = recd.stationCode;
                    data.stationName = recd.stationName;
                } else {//否则判断是否是修改
                    if (type == 'modify' && showData) {
                        data.devId = showData.devId;
                        data.stationCode = showData.stationCode;
                        data.stationName = showData.stationName;
                    }
                }

                if (data.fileId && data.fileId != null && data.fileId != '') {
                    data.fileId = $('#io_defect_add_content input[name=fileId]').data('defect_upload_file_id');
                }
                if (data.findTime && data.findTime != null && data.findTime != '') {
                    data.findTime = new Date(data.findTime).getTime();
                }
                if (data.endTime && data.endTime != null && data.endTime != '') {
                    data.endTime = new Date(data.endTime).getTime();
                }
                data.userId = $('#io_defect_add_content input[name=userId]').attr('treeselid');
                data.reviceUserName = $('#io_defect_add_content input[name=userId]').val();
                return data;
            }, fnSubmitSuccess: function (res) {// 表单内容提交成功回调函数
                if (successCallBack && $.isFunction(successCallBack)) {
                    successCallBack(res);
                } else {
                    if (res.success) {
                        App.alert('保存成功！');
                    } else {
                        App.alert('保存失败！');
                    }
                }
            },
            fnSubmitError: function () {// 表单内容提交失败回调函数
                App.alert('保存失败！');
            },
        });
    },
    //获取文字
    getInfoOfDim: function (dim) {
        if (dim == 'month') {
            return Msg.unit.timeDem[1];
        } else if (dim == 'year') {
            return Msg.unit.timeDem[0];
        } else {
            return Msg.unit.timeDem[2];
        }
    },
    /**
     * TODO 后面来完善对应的信息 初始化界面上的信息 传递的界面的元素有lang的属性,根据lang属性来转换内容
     * @param langs 选择出来的居右lang属性的元素
     */

    initPageLauge:function (langs) {
        var len = langs.length;
        var htmlElemStr = ['DIV','LABEL','SPAN','H1','H2','H3','H4','H5','H6'];
        for(var i=0;i<len;i++){
            var el = langs.eq(i);
            var tmMsg = el.attr && el.attr('lang') || '';
            if(tmMsg){
                try{
                    var targName = el[0].tagName.toUpperCase();
                    if('INPUT'== targName || 'TEXTAREA'== targName){//输入框 和 区域的
                        el.val(eval(tmMsg));
                    }else if(htmlElemStr.indexOf(targName)!=-1){//div、label、span等元素的国际化
                        el.html(eval(tmMsg));
                    }
                }catch(e){
                    console.warn('can not get info::'+tmMsg);
                }
            }
        }
    }
}