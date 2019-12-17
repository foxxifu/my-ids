/**
 * 功能：设备选择对话框，显示内容和样式可通过属性配置 日期：2017.02.10
 * P00102
 */
define(['jquery', 'ValidateForm', 'GridTable', 'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function ($) {
    (function ($) {
        $.fn.deviceDialog = function (options) {
            var $this = this;
            var p = $.extend({
                pageSize: 20,
                id: "initialDeviceDialog",
                width: 1000,					                // 选择框宽度
                height: 635,				                        // 选择框高度
                url: '/devManager/listDev',	                    // 选择设备后台请求地址
                param: {},					                    // 想后台请求的参数
                selector: true,				                    // 选择输入控件id
                toolbarID: '',				                    // 选择框中筛选控件的id
                tableID: '',					                // 选择框中设备表格的id
                submit: false,				                    // 点击确定后的回调函数
                refresh: false,				                    // 点击筛选控件是否执行刷新表格操作
                dialog: null,					                // 选择框结点
                singleSelect: false,			                // 是否只选择一个设备  false:多选 true:单选
                onDoubleClick: false,			                // 设备信息列表中的双击回调函数
                loaded: false,				                    // 设备信息列表加载完成的变量标示
                noButtons: false,				                // 不显示查询按钮
                title: Msg.chooseDevice,		                // 选择对话框的标题
                fnSelected: false,								// 选择设备后回调函数
                columns:						                //
                    ['icon', 'stationName', 'installCapacity', 'combineType', 'inverterType'],
                input: $this
            }, options);
            var serachParam = {};
            var hasChecked = false;
            /**
             * Gridtable加载完成后的执行函数
             */
            var onLoadReady = function (data, btrs, htrs) {
                if (!p.loaded) {
                    /* p.loaded = true;
                     $('#device_dialog_devTable').GridTableInitSelectedRecords(p.input.data('selectedRecords'));*/
                    p.loaded = true;
                    var selectedRecords = p.input.data('selectedRecords');
                    var arr = [];
                    if (!selectedRecords || selectedRecords.length == 0) {
                        var values = (p.input.attr('value') && p.input.attr('value').split(','));
                        var names = (p.input.val() && p.input.val().split(','));
                        $(values).each(function (i) {
                            var o = {};
                            o.devId = values[i];
                            o.devName = names[i];
                            arr.push(o);
                        });
                        $('#device_dialog_devTable').GridTableInitSelectedRecords(arr);
                    }
                    else {
                        $('#device_dialog_devTable').GridTableInitSelectedRecords(selectedRecords);

                    }
                    if (!p.input.data('selectedRecords') && arr.length == 0 && p.singleSelect) {
                        $(btrs[0]).click();
                    }
                }
                //单选时，如果未选中电站，默认选中第一个

            }
            App.dialog({
                id: p.id,
                title: p.title,
                width: p.width,
                height: p.height
            }).loadPage({url: "js/plugins/deviceDialog/deviceDialog.html"}, {}, function () {
                var param = p;

                /**
                 * 查询条件栏配置
                 */
                var initSearch = function () {
                    $("#device_dialog_index_search").ValidateForm('device_dialog_index_search', {
                        show: 'horizontal',
                        fnSubmit: serach,
                        model: [[
                            {
                                input: 'input',
                                type: 'text',
                                show: Msg.devSupplement.devName,
                                name: 'devName',
                                extend: {id: 'device_dialog_search_devName'}
                            },
                            {
                                input: 'select',
                                type: 'select',
                                show: Msg.dataLimit.devType,
                                name: 'devType',
                                options: [{value: '0', text: Msg.all}],
                                extend: {id: 'device_dialog_search_devType'},
                                fnInit: searchTypeInit
                            }
                        ]]
                    });
                };
                initSearch();

                var initTable = function (stationCodes) {
                    serachParam = {stationIds: stationCodes};
                    //设备表格
                    var devTable = $("#device_dialog_devTable").GridTable({
                        url: param.url,
                        title: false,
                        max_height: 410,
                        params: serachParam,
                        rp: 10,
                        clickSelect: true,
                        isRecordSelected: true,
                        showSelectedName: false,
                        singleSelect: param.singleSelect,
                        isSearchRecordSelected: true,
                        onLoadReady: onLoadReady,
                        idProperty: 'devId',
                        colModel: [{
                            display: Msg.deviceDialog.stationName,
                            name: "stationName",
                            width: 0.15,
                            align: 'center'
                        },
                            {display: Msg.devSupplement.devName, name: 'devName', width: 0.25, align: 'center'},
                            {
                                display: Msg.dataLimit.devType,
                                name: 'devTypeId',
                                width: 0.15,
                                align: 'center',
                                fnInit: showDevType
                            },
                            {display: Msg.deviceDialog.deviceBb, name: 'devVersion', width: 0.15, align: 'center'},
                            {display: Msg.deviceDialog.deviceEsn, name: 'devEsn', width: 0.15, align: 'center'},
                            /*{display: Msg.deviceDialog.runstatus,name: 'devRuningState',width: 0.12,align: 'center',fnInit:showRunStatus},*/
                            {
                                display: Msg.deviceDialog.latidu,
                                name: 'Latlong',
                                width: 0.15,
                                align: 'center',
                                fnInit: showLatlong
                            }
                        ]
                    });
                };

                //显示经纬度 
                function showLatlong(dom, value, datas) {
                    var longitude = LatlongFormart(datas.longitude); //经度
                    var latitude = LatlongFormart(datas.latitude);  //纬度
                    dom.html(longitude + "    " + latitude);
                    dom.parent()[0].title = longitude + "    " + latitude;
                }

                //将经纬度转换成为度分秒
                function LatlongFormart(value) {
                    if (value == null || value === "") {
                        return "";
                    }
                    value = Math.abs(value);
                    var v1 = Math.floor(value);//度  
                    var v2 = Math.floor((value - v1) * 60);//分  
                    var v3 = Math.round((value - v1) * 3600 % 60);//秒  
                    return v1 + '°' + v2 + '\'' + v3 + '"';
                }

                //显示运行状态
                function showRunStatus(dom, value, datas) {
                    var stationCode = datas.stationCode;
                    if (!stationCode) {
                        dom.html(Msg.deviceDialog.hasNotConnected);
                        dom.parent()[0].title = Msg.deviceDialog.hasNotConnected;
                    } else {
                        dom.html(Msg.deviceDialog.hasConnected);
                        dom.parent()[0].title = Msg.deviceDialog.hasConnected;
                    }
                }

                //显示设备类型
                function showDevType(dom, value, datas) {
                    dom.html(devTypeIfoCache[datas.devTypeId]);
                    dom.parent()[0].title = devTypeIfoCache[datas.devTypeId];
                }

                var devTypeIfoCache = {};//设备类型前端临时缓存，用来匹配设备列表中的设备类型id
                function searchTypeInit(dom) {
                    $.http.post('/signalconf/getDevTypeInfo', {}, function (data) {
                        if (data && data.success) {
                            $.each(data.data.list, function (i, t) {
                                // 查询到的每一个设备类型的选项添加
                                t.name = main.eval(t.languageKey);
                                var opt = $("<option value=" + t.id + ">" + t.name + "</option>");
                                devTypeIfoCache[t.id] = t.name;
                                dom.append(opt);
                            })

                        } else {
                            App.alert(Msg.partials.main.hp.poverty.getFail);
                        }
                    });
                }

                /**
                 * 查询按钮
                 */
                function serach() {
                    serachParam.devName = $("#device_dialog_search_devName").val();
                    serachParam.devTypeId = $("#device_dialog_search_devType").val();
                    $('#device_dialog_devTable').GridTableSearch({
                        param: serachParam
                    })
                }

                /**
                 * 确定和取消按钮
                 */
                var addClick = function () {
                    $("#device_dialog_index").find("#deviceselectHhCancel").click(function () {
                        $("#" + p.id).modal("hide");
                    }).end().find("#deviceselectHhOk").click(function () {
                        var selectedValues = getdevIds();
                        param.input.val(selectedValues.names).attr("value", selectedValues.devIds).focusout();
                        param.input.data('selectedRecords', selectedValues.selectedRecords);

                        if (param.singleSelect) {
                            var sData = selectedValues.selectedRecords[0];
                            sData = $.extend({
                                deviceType: devTypeIfoCache[sData.devTypeId]
                            }, sData);
                            param.fnSelected && param.fnSelected(sData);
                        }
                        $("#" + p.id).modal("hide");

                    })
                };
                /**
                 * 获取已经选择的设备Id和名称
                 */
                var getdevIds = function () {
                    var selectedRecords = $('#device_dialog_devTable').GridTableSelectedRecords();
                    var values = [];
                    var names = [];
                    $.each(selectedRecords, function (i, e) {
                        values.push(e.devId);
                        names.push(e.devName);
                    });
                    return {
                        devIds: values.toString(),
                        names: names,
                        selectedRecords: selectedRecords
                    };
                };

                /**
                 * 左侧域结构
                 */
                var initTree = function () {
                    var userId = (param.param && param.param.userId) || Cookies.get("userid");
                    $.http.post('/domain/queryUserDomainStaRes', {
                        "mdfUserId": userId
                    }, function (data) {
                        if (data && data.success) {
                            var zTreeObj;
                            var zNodes = main.getZnodes2(data.data);
                            var zTreeSetting = {
                                treeId: "devLocations",
                                callback: {
                                    onCheck: zTreeOnCheck,
                                    onClick: zTreeOnClick
                                },
                                check: {
                                    chkStyle: "checkbox",
                                    enable: true,
                                    autoCheckTrigger: false,
                                    chkboxType: {"Y": "ps", "N": "ps"}
                                },
                                view: {
                                    dblClickExpand: false,
                                    showLine: false,
                                    selectedMulti: false,
                                    showIcon: function (treeid, treeNode) {
                                        return treeNode.model == 'STATION';
                                    }
                                },
                                data: {
                                    simpleData: {
                                        enable: true
                                    }
                                }
                            };

                            $.fn.zTree.init($("#device_dialog_domainsTree"), zTreeSetting, zNodes);
                            zTreeObj = $.fn.zTree.getZTreeObj("device_dialog_domainsTree");

                            var stationIds = [];
                            for (var i = 0; i < zNodes.length; i++) {
                                if (zNodes[i].model == 'STATION') {
                                    stationIds.push(zNodes[i].id);
                                }
                            }
                            if (zNodes.length > 0 && stationIds.length === 0) {
                                stationIds = "NOSTATION";
                            } else {
                                stationIds = stationIds.join();
                            }
                            initTable(stationIds);

                            //点击域结构查询设备
                            function zTreeOnClick(event, treeId, treeNode) {
                                // serachParam.domainId = zTreeObj.getSelectedNodes()[0].id;
                                if (treeNode.isParent) {
                                    zTreeObj.expandNode(treeNode);
                                } else {
                                    zTreeObj.checkNode(treeNode, !treeNode.checked, true, true);
                                }
                            }

                            function zTreeOnCheck() {
                                var checkedNodes = zTreeObj.getCheckedNodes();
                                var stationIds = [];
                                hasChecked = checkedNodes.length > 0;
                                checkedNodes = hasChecked ? checkedNodes : zTreeObj.transformToArray(zTreeObj.getNodes());
                                for (var i = 0; i < checkedNodes.length; i++) {
                                    if (checkedNodes[i].model == 'STATION') {
                                        stationIds.push(checkedNodes[i].id);
                                    }
                                }
                                if (hasChecked && stationIds.length === 0) {
                                    serachParam.stationIds = "NOSTATION";
                                } else {
                                    serachParam.stationIds = stationIds.toString();
                                }
                                serach();
                            }
                        } else {
                            App.alert(Msg.partials.main.hp.poverty.getFail);
                        }
                    });
                };
                initTree();
                addClick();
            });
        }
    })(jQuery);
});