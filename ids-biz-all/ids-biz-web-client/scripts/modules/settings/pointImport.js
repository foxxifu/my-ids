'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'pointImport',
    description: '模块功能：数据补采',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'ajaxFileUpload', 'Progress'
    ]
});
App.Module('pointImport', function ($) {
    return {
        Render: function (params) {
            //1.初始化tab页面
            //this.initForm();
            //2.初始化表格list数据
            this.initTable();
            //添加事件
            this.initEvent();
        },
        /**
         * 初始化表单信息
         */
        initForm: function () {
            //1.获取预制的点表名称,这里是从后台获取的
            var dbNames = [
                'PID2.0.xlsx',
                'SUN_2000_2.0.xlsx',
                'SUN_2000_3.0.xlsx',
            ];
            //预制的点表栏的下拉框
            var prefabSelect = $('#setting_point_import_submit #prefabricated select');
            var len = dbNames.length;
            for (var i = 0; i < len; i++) {
                prefabSelect.append($('<option/>').text(dbNames[i]));
            }
        },
        /*获取导入的点表信息*/
        initTable: function (params) {
            var self = this;
            params = params || {};
            $('#setting_point_import_list').GridTable({
                url: main.serverUrl.biz + '/settings/signal/import/list',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 510,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 510,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {name: 'id', hide: true},
                    {
                        display: '点表名称',
                        name: 'signalDataName',
                        //align: "center",
                        width: 0.3
                    },
                    {
                        display: '版本号',
                        name: 'version',
                        //align: "center",
                        width: 0.23,
                        //fnInit:getBlueColor
                    },
                    // {
                    //     display:'文本版本',
                    //     name: 'venderName',
                    //     //align: "center",
                    //     width: 0.23,
                    //     //fnInit:getBlueColor
                    // },
                    {
                        display: '导入日期',
                        name: 'createDate',
                        //align: "center",
                        width: 0.24,
                        fnInit: function dateFormte(element, value, data) {
                            element.empty();
                            var date = '';
                            if (value) {
                                date = new Date(value).format('yyyy-MM-dd HH:mm:ss');
                            }
                            element.text(date);
                            element.attr('title', date);
                            element.parent().attr('title', date);
                        }
                    },
                    {
                        display: '详情',
                        name: 't',
                        //align: "center",
                        width: 0.23,
                        fnInit: function (element, value, data) {
                            element.empty();
                            var devTypeId = data.devTypeId;
							/*
                            if (devTypeId == 13) {//根据设备类型来区别是否可以查看设备实时数据
                                var $runData = $('<a/>').text('设备实时数据').addClass('text-link');
                                element.append($runData);
                                $runData.unbind('click').bind('click', function (ev) {
                                    ev.stopPropagation();
                                    //TODO 通过ajax请求去查询数据，显示对应的信息
                                    self.showTongguanjiDialog(data.id);
                                });
                            }
							*/
                            var $signalDetail = $('<a/>').text('详情').addClass('text-link').css({'margin-left': '20px'});
                            element.append($signalDetail);
                            $signalDetail.unbind('click').bind('click', function (ev) {
                                ev.stopPropagation();
                                self.showDevSignalDialog(data.id);
                            });

                        }
                    },
                ]
            });
        },
        initEvent: function () {
            function shoTextFileChange() {//输入框的chang事件
                var value = this.value;
                var myShow = $('#point_show_import_text');
                if (!value || value == "" || value == null) {
                    myShow.val('');
                } else {
                    var index = value.lastIndexOf('\\');
                    index = index == -1 ? 0 : index + 1;
                    myShow.val(value.substring(index, value.length));
                }
            }

            $('#myShowTextFile').unbind('change').bind('change', shoTextFileChange);
            //对预置表单的事件
            $('#setting_point_import_submit #prefabricated .btn').unbind('click').bind('click', function () {
                var _this = $(this);
                _this.attr('disabled', true);
                var value = $('#setting_point_import_submit #prefabricated select').val();
                if (!value || value == null || value == "") {
                    App.alert('没有需要预置的表单信息');
                    _this.attr('disabled', false);
                    return;
                }
                var form = $('#setting_point_import_submit #prefabricated');
                $.http.post(form.attr('action'), form.serialize(), function (data) {
                    if (data.success) {
                        App.alert('预置成功！');
                        _this.attr('disabled', false);
                        //重新加载数据
                        $('#setting_point_import_list').GridTableReload();
                    } else {
                        App.alert('预置失败！');
                        _this.attr('disabled', false);
                    }
                })
            });
            var maxsize = 10 * 1024 * 1024;//上传文件最大10M
            //对非预置表单的事件
            $('#setting_point_import_submit #noPrefabricated .btn').unbind('click').bind('click', function () {
                var _this = $(this);
                _this.attr('disabled', true);
                var value = $('#setting_point_import_submit #noPrefabricated input[name="file"]').val();
                if (!value || value == null || value == "") {
                    App.alert('未选中上传文件');
                    _this.attr('disabled', false);
                    return;
                }
                //验证选上传的格式 是否是excel文件
                if (!/\.xl(s[xmb]|t[xm]|am)$/.test(value)) {
                    App.alert('请选择excel文件上传');
                    _this.attr('disabled', false);
                    return;
                }
                //验证文件的大小
                var fileSize = document.getElementById('myShowTextFile').files[0].size;
                if (fileSize > maxsize) {
                    App.alert('上传文件不超过10M');
                    _this.attr('disabled', false);
                    return;
                }
                $('#main_view').myProgress({
                    width: 100,//默认进度条的整体的宽,可以支持百分比的字符串如'100%'，数字(如200)就是px像素值，后面就不能使用px了
                    height: 100,//默认进度条的整体的高,可以支持百分比的字符串如'100%'，数字(如30)就是px像素值，后面就不能使用px了
                    //其他的默认设置
                    pType: 'b',//展示的样式 s：长方形  c:圆形,b:小球 （目前主要使用s 、c两种，b暂时不要使用) 默认长方形
                    hasProgress: false,//是否有进度 true：有  false：无 默认有
                    hasText: true,//是否有文字显示 true:显示文字 false：不显示文字  默认true显示文字
                    showText: '请等待',//显示文字
                    show: 'h',//展示方式 是垂直还是水平 h：水平 v：垂直 默认水平
                    isDialog: true,//是否是弹出框的形式
                });
                var form = $('#setting_point_import_submit #noPrefabricated');
                $.ajaxFileUpload({
                    url: form.attr('action'),
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'myShowTextFile', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json
                    success: function (data, status)  //服务器成功响应处理函数
                    {
                        $('#main_view').closeProgressDialog();
                        if (data.code == 1) {
                            App.alert('导入成功！');
                            _this.attr('disabled', false);
                            //重新加载数据
                            $('#setting_point_import_list').GridTableReload();
                        } else {
                            App.alert(data.results);
                            _this.attr('disabled', false);
                        }
                        $('#myShowTextFile').unbind('change').bind('change', shoTextFileChange);
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                        $('#main_view').closeProgressDialog();
                        App.alert('导入失败！出现异常');
                        _this.attr('disabled', false);
                        $('#myShowTextFile').unbind('change').bind('change', shoTextFileChange)
                    }
                })
            });

            //监听按钮的点击事件 适配 删除 导出
            $('#setting_point_import_btns input').unbind('click').bind('click', function () {
                var _this = $(this);
                _this.attr('disabled', true);
                var recodes = $('#setting_point_import_list').GridTableSelectedRecords();
                if (recodes.length == 0) {
                    App.alert('未选中任何记录');
                    _this.attr('disabled', false);
                    return;
                }
                //其他的控制后设置是否可以使用的样式
                var type = $(this).attr('myType')
                if (type == 'del') {
                    App.confirm('确定删除选中的数据？', function () {
                        $.http.post(main.serverUrl.biz + '/settings/signal/delete?ids=' + recodes[0].id, {}, function (res) {
                            if (res.success) {
                                App.alert('删除成功!');
                                $('#setting_point_import_list').GridTableReload();
                            } else {
                                App.alert('删除失败!');
                            }
                            _this.attr('disabled', false);
                        })
                    }, function () {
                        _this.attr('disabled', false);
                    })

                } else {
                    _this.attr('disabled', false);
                }

            })
        },
        /**
         * 根据通管机的版本id查询通管机下挂的设备信息
         * @param modelVersionId
         */
        showTongguanjiDialog: function (modelVersionId) {
            if (!modelVersionId) {
                return;
            }
            var content = $('<div/>');
            App.dialog({
                title: '设备实时数据',
                id: 'show_tgj_children_dialog',
                width: "40%",
                height: 'auto',//弹窗内容高度,支持%  <br>
                maxHeight: 700,//弹窗内容最大高度, 不支持% <br>
                content: content
            });
            //搜索栏
            var $searchDiv = $('<div/>').attr('id', 'show_tgj_children_search').addClass('toolbar');
            $searchDiv.ValidateForm('show_tgj_children_search', {
                    show: 'horizontal',
                    fnSubmit: function (searchData) {
                        for (var key in searchData) {
                            if (key == 'params') {
                                continue;
                            }
                            searchData.params[key] = searchData[key];
                        }
                        searchData.params['modelVersionId'] = modelVersionId;
                        $listTable.GridTableReload(searchData);
                    },
                    model: [[
                        {
                            input: 'input',
                            type: 'text',
                            show: '信号点名称',//信号点名称
                            name: 'signalName'
                        }
                    ]]
                }
            );
            content.append($searchDiv);
            //表格
            var $listTable = $('<div/>');
            content.append($listTable);
            $listTable.GridTable({
                url: main.serverUrl.biz + '/settings/devSigDatas',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 348,
                theme: 'ThemeA',
                height: 'auto',
                max_height: 348,
                params: {modelVersionId: modelVersionId},
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '信号点名称',
                        name: 'signalName',
                        //align: "center",
                        width: 0.5,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '信号点值',
                        name: 'signalValue',
                        //align: "center",
                        width: 0.5,
                    },
                ]
            })
        },
        /**
         * 展示设备和信号点的信息
         * @param modelVersionId
         */
        showDevSignalDialog: function (modelVersionId) {
            if (!modelVersionId) {
                return;
            }
            var content = $('<div/>');
            App.dialog({
                title: '设备信息',
                id: 'show_dev_children_dialog',
                width: "80%",
                height: 'auto',//弹窗内容高度,支持%  <br>
                maxHeight: 800,//弹窗内容最大高度, 不支持% <br>
                content: content
            });
            //搜索栏
            var $searchDiv = $('<div/>').attr('id', 'show_dev_children_search').addClass('toolbar');
            $searchDiv.ValidateForm('show_dev_children_search', {
                    show: 'horizontal',
                    fnSubmit: function (searchData) {
                        for (var key in searchData) {
                            if (key == 'params') {
                                continue;
                            }
                            searchData.params[key] = searchData[key];
                        }
                        searchData.params['versionId'] = modelVersionId;
                        $listTable.GridTableReload(searchData);
                    },
                    model: [[
                        {
                            input: 'input',
                            type: 'text',
                            show: '设备名称',//设备名称
                            name: 'deviceName'
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '信号点名称',//信号点名称
                            name: 'signalName'
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '版本号',//版本号
                            name: 'version'
                        },
                        {
                            input: 'select',
                            type: 'select',
                            options: [// 1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
                                {value: '-1', text: Msg.all},
                                {value: '1', text: '遥测'},
                                {value: '2', text: '遥信'},
                                {value: '3', text: '事件'},
                                {value: '4', text: '告警'},
                            ],
                            show: '信号点类型',//信号点名称
                            name: 'signalType'
                        },
                    ]]
                }
            );
            content.append($searchDiv);
            //按钮
            var btnDiv = $('<div/>').addClass('layout-bar right');
            var yxToAlarmBtn = $('<input/>').val('遥信转告警').attr('type', 'button').addClass('btn').attr('myType', true);
            btnDiv.append(yxToAlarmBtn);
            var alarmToYxBtn = $('<input/>').val('告警转遥信').attr('type', 'button').addClass('btn').attr('myType', false);
            btnDiv.append(alarmToYxBtn);

            //告警和遥信的转换
            function alarmTransform() {
                var recods = $listTable.GridTableSelectedRecords();
                var len = recods.length;
                if (len == 0) {
                    App.alert('未选择数据');
                    return;
                }
                var ids = [];
                for (var i = 0; i < len; i++) {debugger
                    var tmp = recods[i];
                    //1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
                    if (tmp.sigType == 2 || tmp.sigType == 3 || tmp.sigType == 8 || tmp.sigType == 9) {
                        ids.push(tmp.id);
                    } else {
                        App.alert('选择数据中有非遥信数据,信号点名称:' + tmp.sigName);
                        return;
                    }
                }
                var type = $(this).attr('myType');
                App.alert('确认转换？', function () {
                    $.http.post(main.serverUrl.biz + '/settings/signal/convert/' + type, ids, function (res) {
                        if (res.success) {
                            App.alert('转换成功');
                        } else {
                            App.alert('转换失败');
                        }
                    });
                });
            }

            yxToAlarmBtn.unbind('click').bind('click', alarmTransform);
            alarmToYxBtn.unbind('click').bind('click', alarmTransform);
            var saveBtn = $('<input/>').val('保存').attr('type', 'button').addClass('btn');
            btnDiv.append(saveBtn);
            //保存事件
            saveBtn.unbind('click').bind('click', function () {
                saveBtn.attr('disabled', true);
                var saveDatas = [];//需要保存的数据
                for (var key in modfyData) {
                    var tmpOldArr = searchData[key];
                    var tmpNewArr = modfyData[key];
                    if (tmpOldArr && tmpNewArr && tmpOldArr.join(',') != tmpNewArr.join(',')) {
                        if (tmpNewArr[0] === '' || tmpNewArr[1] === '') {
                            App.alert(tmpNewArr[0] === '' && '增益必填' || '偏移量必填');
                            saveBtn.attr('disabled', false);
                            return;
                        }
                        // var tempObj = {id:key,sigGain:tmpNewArr[0],sigOffset:tmpNewArr[1]};
                        // //tempObj.signalName = tmpNewArr[0];
                        // tempObj.sigGain = tmpNewArr[0];
                        // tempObj.sigOffset = tmpNewArr[1];
                        // //tempObj.signalType = tmpNewArr[3];
                        // tempObj.id = key;
                        saveDatas.push({id: key, sigGain: tmpNewArr[0], sigOffset: tmpNewArr[1]});
                    }
                }
                if (saveDatas.length == 0) {
                    App.alert('当前没有任何数据修改');
                    saveBtn.attr('disabled', false);
                    return;
                }
                //TODO 做ajax请求 保存修改了的数据
                console.log(saveDatas);
                App.confirm('确认修改当前修改的数据？', function () {
                    $.http.post(main.serverUrl.biz + '/settings/signal/update', saveDatas, function (res) {
                        if (res.success) {
                            App.alert('修改成功');
                            saveBtn.attr('disabled', false);
                            //更新当前的数据
                            $listTable.GridTableRefreshPage();//更新当前也的数据
                        } else {
                            App.alert('修改失败');
                        }
                    }, function () {
                        saveBtn.attr('disabled', false);
                        App.alert('服务错误');
                    })
                }, function () {
                    saveBtn.attr('disabled', false);
                });
            });
            content.append(btnDiv);

            //设置一条数据,返回数组
            function setOneData(temp) {
                var tmpArr = [];
                //tmpArr.push(temp.signalName);//信号点别名 [0]
                tmpArr.push(temp.sigGain);//信号点增益    [0]
                tmpArr.push(temp.sigOffset);//信号点偏移量 [1]
                //tmpArr.push(temp.signalType);//信号点类型
                return tmpArr;
            }

            /**
             * 修改的时候的事件
             * @param data 当前的原始值
             * @param editVal 修改后的值
             * @param index modfyData对应信号点id的数据的位置  [信号点别名,信号点增益,信号点偏移量]即[0-2]
             */
            function changeEditData(data, editVal, index) {
                if (!data) {
                    return;
                }
                var tmpArr = modfyData[data.id];
                if (!tmpArr) {
                    tmpArr = setOneData(data);
                    modfyData[data.id] = tmpArr;
                }
                tmpArr[index] = editVal;//修改值 信号点名称
                console.log(modfyData);
            }

            //表格
            var $listTable = $('<div/>').addClass('content-box');
            content.append($listTable);
            var searchData = {};//查询出来的原始数据 {信号点id:[信号点别名,信号点增益,信号点偏移量],....}
            var modfyData = {};//当前修改了的数据 {信号点id:[信号点别名,信号点增益,信号点偏移量],....},准备提交给后台的数据
            $listTable.GridTable({
                url: main.serverUrl.biz + '/settings/signal/list',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 560,
                theme: 'ThemeA',
                height: 'auto',
                max_height: 560,
                params: {versionId: modelVersionId},
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                onLoadReady: function (data) {
                    searchData = {};
                    modfyData = {};
                    var len = data && data.length || 0;
                    for (var i = 0; i < len; i++) {
                        var temp = data[i];
                        searchData[temp.id] = setOneData(temp);
                    }
                },
                colModel: [
                    {
                        display: '设备名称',
                        name: 'deviceName',
                        //align: "center",
                        width: 0.2,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '信号点名称',
                        name: 'sigName',
                        //align: "center",
                        width: 0.18,
                        // fnInit:function (element,value,data) {
                        //     element.empty().removeAttr('title').parent().removeAttr('title');
                        //     var $input = $('<input/>').attr('name','signalName').val(value);
                        //     element.unbind('click').bind('click',function (ev) {
                        //         ev.stopPropagation();
                        //     });
                        //     element.append($input);
                        //     $input.unbind('change').bind('change',function (ev) {
                        //         ev.stopPropagation();
                        //         changeEditData(data,this.value.trim(),0);
                        //     })
                        // }
                    },
                    {
                        display: '增益',
                        name: 'sigGain',
                        //align: "center",
                        width: 0.18,
                        fnInit: function (element, value, data) {
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            element.unbind('click').bind('click', function (ev) {
                                ev.stopPropagation();
                            });
                            var $input = $('<input/>').attr('name', 'sigGain').val(value);
                            element.append($input);
                            $input.unbind('change').bind('change', function (ev) {
                                ev.stopPropagation();
                                changeEditData(data, this.value.trim(), 0);
                            })
                        }
                    },
                    {
                        display: '偏移量',
                        name: 'sigOffset',
                        //align: "center",
                        width: 0.18,
                        fnInit: function (element, value, data) {
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            element.unbind('click').bind('click', function (ev) {
                                ev.stopPropagation();
                            });
                            var $input = $('<input/>').attr('name', 'sigOffset').val(value);
                            element.append($input);
                            $input.unbind('change').bind('change', function (ev) {
                                ev.stopPropagation();
                                changeEditData(data, this.value.trim(), 1);
                            });
                        }
                    },
                    {
                        display: '信号点类型',
                        name: 'sigType',
                        //align: "center",
                        width: 0.1,
                        fnInit: function (element, value, data) {
                            //1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
                            var list = ['遥测', '遥信', '遥信', '遥控', '遥控', '遥测', '遥调', '事件', '告警'];
                            value = value || 1;
                            var tmpText = list[value - 1];
                            element.empty().text(tmpText).attr('title', tmpText).parent().attr('title', tmpText);
                        }
                    },
                    {
                        display: '版本号',
                        name: 'version',
                        //align: "center",
                        width: 0.1,
                    },
                    // {
                    //     display: '告警转换',
                    //     name: 't1',
                    //     //align: "center",
                    //     width: 0.1,
                    //     fnInit:function (element,value,data) {
                    //         //1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
                    //         var $select = $('<select/>').attr('name','signalType');
                    //         element.append($select);
                    //         var signalType = data.signalType || 1;
                    //         var signalOptions = [{text:'遥测',value:1},{text:'单点遥信',value:2},{text:'双点遥信',value:3},{text:'单点遥控',value:4}
                    //         ,{text:'双点遥控',value:5},{text:'遥脉',value:6},{text:'遥调',value:7},{text:'事件',value:8},{text:'告警',value:9}];
                    //         for(var i=0;i<signalOptions.length;i++){
                    //             var tmpOpt = signalOptions[i];
                    //             var $opt = $('<option/>').text(tmpOpt.text).val(tmpOpt.value);
                    //             if(signalType == tmpOpt.value){
                    //                 $opt.attr('selected','selected');
                    //             }
                    //             $select.append($opt);
                    //         }
                    //         $select.unbind('change').bind('change',function (ev) {//改变事件
                    //             var tmpValue = this.value;
                    //             var ycList = [1,6,7];//遥测的信息
                    //             var ykList = [4,5];//遥控遥脉的转换
                    //             var yxList = [2,3,8,9];//遥信和告警的转换
                    //             if(ycList.indexOf(signalType)!=-1){//
                    //                 if(ycList.indexOf(tmpValue)==-1){
                    //                     $(this).val(signalType);
                    //                     App.alert('不能转换');
                    //                     return;
                    //                 }
                    //             }else if(ykList.indexOf(signalType)!=-1){
                    //                 if(ykList.indexOf(tmpValue)==-1){
                    //                     $(this).val(signalType);
                    //                     App.alert('不能转换');
                    //                     return;
                    //                 }
                    //             }else if(yxList.indexOf(signalType)!=-1){
                    //                 if(yxList.indexOf(tmpValue)==-1){
                    //                     $(this).val(signalType);
                    //                     App.alert('不能转换');
                    //                     return;
                    //                 }
                    //             }else{
                    //                 console.log('get data is error');
                    //                 return;
                    //             }
                    //             changeEditData(data,tmpValue,3);
                    //         });
                    //     }
                    // }
                ]
            })
        }
    }
});