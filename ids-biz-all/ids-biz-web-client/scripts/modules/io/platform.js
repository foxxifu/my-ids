'use strict';

App.Module.config({
    package: '/main/io',
    moduleName: 'platform',
    description: '模块功能：智能运维——运维工作台',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'easyTabs'
    ]
});
App.Module('platform', function ($) {

    var URLs = {
        // 分页查询电站状态数据统计列表
        queryStationList: main.serverUrl.biz + '/operation/worksite/getInfo',
        // 获取指定设备的统计列表
        getDeviceInfo: main.serverUrl.biz + '/operation/worksite/getDeviceInfo'
    };

    var platform = {
        searchBar: function () {
            platform.$get("#io_platform_search_bar").ValidateForm('io_platform_search_bar', {
                show: 'horizontal',
                fnSubmit: function (data) {
                    var params = {
                        "stationName": data.stationName,
                        "stationStatus": data.stationStatus
                    };
                    platform.$get('#io_platform_station_list').GridTableReload({params: params});
                },
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: Msg.modules.io.platform.tableList.name,
                        name: 'stationName'
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: Msg.modules.io.platform.tableList.status,
                        name: 'stationStatus',
                        options: [
                            {value: "", text: Msg.all},
                            {value: "1", text: Msg.modules.io.platform.stationStatus[0]},
                            {value: "2", text: Msg.modules.io.platform.stationStatus[1]},
                            {value: "3", text: Msg.modules.io.platform.stationStatus[2]}
                        ]
                    }
                ]]
            });
        },

        resultTable: function (params) {
            function statusFormatter(element, value, datas) {
                var dom = $("<img/>").css({'width': '26px', 'height': '26px'});
                if (value == 3) {
                    dom.attr('src', '../images/main/io/table/blue.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.healthy);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.healthy);
                } else if (value == 2) {
                    dom.attr('src', '../images/main/io/table/gray.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.disconnect);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.disconnect);
                } else if (value == 1) {
                    dom.attr('src', '../images/main/io/table/red.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.error);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.error);
                }
            }

            function picFormatter(element, value, datas) {
                var dom = $("<img/>").css({'width': '100%', 'max-width': 105, 'display': 'inline-block', 'margin': '5px auto'});
                element.html(dom);
                main.DownloadFile({target: dom, fileId: value});
                element.removeAttr("title");
                element.parent().removeAttr("title");
            }

            platform.$get('#io_platform_station_list').GridTable({
                url: URLs.queryStationList,
                width: 'auto',
                height: 'auto',
                params: params,
                clickSelect: false,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                idProperty: 'stationCode',
                colModel: [
                    {name:'stationCode', hide: true},
                    {
                        display: Msg.modules.io.platform.tableList.status,
                        name: 'stationStatus',
                        align: "center",
                        width: 0.08,
                        fnInit: statusFormatter
                    },
                    {
                        display: Msg.modules.io.platform.tableList.pic,
                        name: 'stationFileId',
                        width: 0.1,
                        align: "center",
                        fnInit: picFormatter
                    },
                    {
                        display: Msg.modules.io.platform.tableList.name,
                        name: 'stationName',
                        width: 0.15,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.tableList.address,
                        name: "stationAddr",
                        width: 0.25,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.tableList.deviceStatus,
                        name: "deviceStatus",
                        width: 0.15,
                        align: "center",
                        fnInit: function (dom, value, record) {
                            var content = $('<div/>').addClass('deviceStatus');
                            var items = [], title = [];
                            $.each(value, function (k, v) {
                                // TODO 设置设备状态显示（图标未定义）
                                items.push('<i class="s s' + k + '_' + v + '"></i>');
                                title.push(Msg.modules.io.platform.deviceType[+k - 1]
                                    + "[" + Msg.modules.io.platform.deviceStatus[+v - 1] + ']');
                            });
                            content.append(items.join(' '));
                            dom.parent().attr('title', title.join(' | '));
                            dom.html(content);
                        }
                    },
                    {
                        display: Msg.modules.io.platform.tableList.pr,
                        name: 'stationPr',
                        width: 0.1,
                        align: "center",
                        unit: '%'
                    },
                    {
                        display: Msg.modules.io.platform.tableList.alarmCount,
                        name: 'alarmCount',
                        width: 0.1,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.tableList.defectCount,
                        name: 'defectCount',
                        width: 0.1,
                        align: "center"
                    }
                ],
                expandFlag: function (record) {
                    return !!(record && record.deviceStatus && !$.isEmptyObject(record.deviceStatus));
                },
                expand: function (record, expandBox, index) {
                    platform.showDeviceTabs(expandBox, record, true);
                }
            });
        },

        showStationInfo: function (content, station) {
            if (!station) return;
            $(content).addClass('noBorder').empty();

            var status = station.stationStatus, statusClass = '', statusName = '';
            var pic = station.stationFileId;
            var name = station.stationName;
            if (status == 1) {
                statusClass = 'disconnect';
                statusName = Msg.modules.io.platform.plantSurvey.disconnect;
            }
            else if (status == 2) {
                statusClass = 'error';
                statusName = Msg.modules.io.platform.plantSurvey.error;
            }
            else if (status == 3) {
                statusClass = 'healthy';
                statusName = Msg.modules.io.platform.plantSurvey.healthy;
            }

            var t_top = $('<div/>').addClass('toolbar_top'),
                t_left = $('<div/>').addClass('toolbar_left'),
                t_right = $('<div/>').addClass('toolbar_right'),
                s_content = $('<div/>').attr('id', 'io_platform_station_right');
            t_right.append(s_content.css('margin-left', '15px'));
            $(content).append(t_top).append(t_left).append(t_right);

            t_top.text(name);

            var s_pic = $('<img/>').attr('alt', name);
            main.DownloadFile({target: s_pic, fileId: pic});

            var s_status = $('<i/>').addClass('station-status' + ' ' + statusClass)
                .attr('title', Msg.modules.io.platform.tableList.status + '[' + statusName + ']');

            t_left.append(s_pic).append(s_status);

            t_right.ValidateForm('io_platform_station_right', {
                type: 'view',
                data: station,
                model: [
                    [
                        {input: 'input', type: 'hidden', name:'stationCode', hide: true}
                    ],
                    [
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: Msg.modules.io.platform.tableList.address,
                            name: "stationAddr",
                            align: "center",
                            css: {'height': 64}
                        }
                    ],
                    [
                        {
                            input: 'div',
                            type: 'group',
                            show: Msg.modules.io.platform.tableList.deviceStatus,
                            name: "deviceStatus",
                            align: "center",
                            fnInit: function (dom, value, record) {
                                var content = $('<div/>').addClass('deviceStatus');
                                var items = [], title = [];
                                if (value) {
                                    $.each(value, function (k, v) {
                                        items.push('<i class="s s' + k + '_' + v + '"></i>');
                                        title.push(Msg.modules.io.platform.deviceType[+k - 1]
                                            + "[" + Msg.modules.io.platform.deviceStatus[+v - 1] + ']');
                                    });
                                }
                                content.append(items.join(' '));
                                dom.parent().attr('title', title.join(' | '));
                                dom.html(content);
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: Msg.modules.io.platform.tableList.pr,
                            name: 'stationPr',
                            align: "center",
                            fnInit: function (dom, value) {
                                +value && dom.val(+value + '%');
                            }
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: Msg.modules.io.platform.tableList.alarmCount,
                            name: 'alarmCount',
                            align: "center"
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: Msg.modules.io.platform.tableList.defectCount,
                            name: 'defectCount',
                            align: "center"
                        }
                    ]
                ],
                noButtons: true
            });
        },

        showDeviceTabs: function (content, station, isExpandBox) {
            if (!station) return;
            $(content).empty();

            var stationCode = station.stationCode;
            var deviceStatus = station.deviceStatus;

            var statisticsBox = $('<div/>').addClass('device-content-statistics');
            var tabsBox = $('<div/>').addClass('device-content-tabs');
            $(content).append(statisticsBox);
            $(content).append(tabsBox);

            var tabIds = [], tabNames = [];
            $.each(deviceStatus, function (k, v) {
                tabIds.push('device_type_' + k);
                tabNames.push(Msg.modules.io.platform.deviceType[+k - 1]);
            });
            $.easyTabs(tabsBox, {
                tabIds: tabIds,
                tabNames: tabNames,
                change: function (body, tabId) {
                    var table = $('<div/>').attr('id', 'io_platform_device_table');
                    body.append(table);
                    platform.deviceTable('io_platform_device_table', {
                        stationCode: stationCode
                    }, tabId.split('_').pop(), isExpandBox);
                }
            });
        },

        deviceTable: function (contentId, params, devTypeId, isExpandBox) {

            function statusFormatter(element, value, datas) {
                var dom = $("<img/>").css({'width': '26px', 'height': '26px'});
                if (value == 3) {
                    dom.attr('src', '../images/main/io/table/blue.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.healthy);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.healthy);
                } else if (value == 1) {
                    dom.attr('src', '../images/main/io/table/gray.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.disconnect);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.disconnect);
                } else if (value == 2) {
                    dom.attr('src', '../images/main/io/table/red.png')
                        .attr('title', Msg.modules.io.platform.plantSurvey.error);
                    element.html(dom);
                    dom.parents('td').attr('title', Msg.modules.io.platform.plantSurvey.error);
                }
            }

            function dealStatusFormatter(element, value, record) {
                var dom = $('<f/>').data('deviceId', record.deviceId);
                if (value) {
                    dom.text(Msg.modules.io.platform.dealStatus[value]);
                    dom.parents('td').attr('title', Msg.modules.io.platform.dealStatus[value]);
                } else {
                    dom.text(Msg.modules.io.platform.dealStatus.normal);
                    dom.parents('td').attr('title', Msg.modules.io.platform.dealStatus.normal);
                }
                element.html(dom);
            }

            function detailFormatter(element, value, record) {
                if (record.deviceTypeId == 1) {
                    var link = $('<a/>').data('deviceId', record.deviceId);
                    link.text(Msg.modules.io.platform.deviceInfo.detail);
                    link.on('click', function () {
                        App.dialog({
                            title: Msg.modules.io.platform.deviceInfo.detail,
                            width: '98%',
                            height: '98%'
                        }).loadPage({
                            url: '/modules/pm/devManager.html',
                            scripts: ['modules/pm/devManager'],
                            styles: ['css!/css/main/pm.css']
                        }, {devId: record.deviceId, devTypeId: record.deviceTypeId});
                    });
                    element.html(link);
                } else {
                    element.html('');
                }
            }

            var colModel = [
                {name: 'deviceId', hide: true},
                {
                    display: Msg.modules.io.platform.deviceInfo.status,
                    name: 'status',
                    align: "center",
                    width: 0.1,
                    fnInit: statusFormatter
                },
                {
                    display: Msg.modules.io.platform.deviceInfo.deviceName,
                    name: 'deviceName',
                    width: 0.3,
                    align: "center"
                },
                {
                    display: Msg.modules.io.platform.deviceInfo.dealStatus,
                    name: 'dealStatus',
                    width: 0.15,
                    align: "center",
                    fnInit: dealStatusFormatter
                },
                {
                    display: Msg.modules.io.platform.deviceInfo.detail,
                    name: 'detail',
                    width: 0.15,
                    align: "center",
                    fnInit: detailFormatter
                }
            ];
            var colModels = {
                "1": [
                    {
                        display: Msg.modules.io.platform.deviceInfo.activePower,
                        name: 'activePower',
                        width: 0.15,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.deviceInfo.reactivePower,
                        name: 'reactivePower',
                        width: 0.15,
                        align: "center"
                    }
                ],
                "8": [
                    {
                        display: Msg.modules.io.platform.deviceInfo.activePower,
                        name: 'activePower',
                        width: 0.15,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.deviceInfo.reactivePower,
                        name: 'reactivePower',
                        width: 0.15,
                        align: "center"
                    }
                ],
                "15": [
                    {
                        display: Msg.modules.io.platform.deviceInfo.photcI,
                        name: 'photcI',
                        width: 0.15,
                        align: "center"
                    },
                    {
                        display: Msg.modules.io.platform.deviceInfo.photcU,
                        name: 'photcU',
                        width: 0.15,
                        align: "center"
                    }
                ]
            };

            for (var i = 0; i < colModels[devTypeId].length; i++) {
                colModel.splice(3 + i, 0, colModels[devTypeId][i]);
            }
            !params && (params = {});
            params.deviceTypeId = devTypeId;

            var option = {
                url: URLs.getDeviceInfo,
                width: 'auto',
                maxHeight: 600,
                height: 'auto',
                params: params,
                clickSelect: false,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                idProperty: 'deviceId',
                colModel: colModel
            };
            isExpandBox && (option.theme = 'ThemeA');
            $('#' + contentId).GridTable(option);
        },

        $get: function (selector) {
            if (selector) {
                return $(selector, $('#io_platform'));
            }
            return $('#io_platform');
        }
    };

    return {
        Render: function (params) {
            $.http.post(URLs.queryStationList, {index: 1, pageSize: 1}, function (resp) {
                if (resp.success && resp.data) {
                    if (resp.data.count > 1) {
                        platform.searchBar();
                        platform.resultTable();
                    } else {
                        if (resp.data.list.length > 0) {
                            var record = resp.data.list[0];
                            platform.showStationInfo(platform.$get('.toolbar'), record);
                            if (!!(record && record.deviceStatus && !$.isEmptyObject(record.deviceStatus))) {
                                platform.showDeviceTabs(platform.$get('.content-box'), record, false);
                            }
                        }
                    }
                }
            }, function () {
                platform.searchBar();
                platform.resultTable();
            });
        }
    };
});