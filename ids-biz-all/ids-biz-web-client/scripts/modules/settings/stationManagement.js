'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'stationManagement',
    description: '模块功能：电站管理',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'easyTabs', 'idsInputTree', 'CitySelect',
        'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'
    ]
});
App.Module('stationManagement', function ($) {
    var URLs = {
        // 获取企业区域树
        getUserDomainTree: main.serverUrl.biz + '/station/getUserDomainTree',
        // 获取电站信息
        getStationInfo: main.serverUrl.biz + '/station/getStationInfo',

        // 检测电站名称是否已经存在
        checkNameIsExists: main.serverUrl.biz + '/station/checkNameIsExists',

        // 根据电站ID查询电站信息
        getStationById: main.serverUrl.biz + '/station/getStationByCode',
        // 获取指定区域或者电站的电价设置
        getPowerPriceById: main.serverUrl.biz + '/station/getPowerPriceById',
        // TODO 获取电站的分时电价设置
        getPriceDayPartingByStationCode: main.serverUrl.biz + '/station/getPricesByStationCode',

        addStation: main.serverUrl.biz + '/station/insertStation',
        modifyStation: main.serverUrl.biz + '/station/updateStationInfoMById',
        deleteStation: main.serverUrl.biz + '/station/deleteStationInfosByIds',

        // 设备接入（绑定设备）
        getModelVersionCodeList: main.serverUrl.biz + '/station/getModelVersionCodeList',
        getDevBySN: main.serverUrl.biz + '/station/getDeviceInfoBySN',
        getDevByStationCode: main.serverUrl.biz + '/station/getDevicesByStationCode',

        // 文件操作
        fileUpload: main.serverUrl.biz + '/fileManager/fileUpload',
        downloadFile: main.serverUrl.biz + '/fileManager/downloadFile',

        // 共享环境监测仪设置
        getEmiByStationCode: main.serverUrl.biz + '/station/getEmiInfoByStationCode',
        getStationListWithPersonal: main.serverUrl.biz + '/station/getStationInfoByEmiId',
        saveShareEmi: main.serverUrl.biz + '/station/saveShareEmi'
    };

    var query = {};

    var stationManager = {
        modelVersionCodeList: [],

        /**
         * 创建\修改\查看电站信息的弹出框
         */
        showStationDialog: function (type, id) {
            var contentId = 'setting_station_content_' + type,
                subUrl = type == 'modify' ? URLs.modifyStation : URLs.addStation,
                title = type == 'modify' && '修改电站';
            var params = id ? {stationCode: id} : {};
            var $content = $('<div></div>').attr('id', contentId);//內容元素

            App.dialog({
                id: 'setting_station_manager_dialog',
                title: title || "创建电站",//弹窗标题
                content: $content,
                width: '50%', //弹窗内容宽度，支持%
                height: 680, //弹窗内容高度, 支持%
                minWidth: 720, //弹窗内容最小宽度, 不支持%
                maxHeight: 900 //弹窗内容最大高度, 不支持%
            });

            var tabIds = ['baseInfo', 'importDev', 'priceSet', 'other'];
            var model = [
                [
                    [
                        {
                            input: 'input',
                            type: 'hidden',
                            hide: true,
                            name: 'id'
                        },
                        {
                            input: 'input',
                            type: 'hidden',
                            hide: true,
                            name: 'stationCode',
                            extend: {id: 'settings_station_stationCode'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '电站归属',
                            name: 'domainId',
                            fnInit: function (dom, value, record) {
                                if (type == 'add') {
                                    dom.val(query.queryName);
                                    dom.attr('value', query.queryId)
                                } else {
                                    dom.val(record.domain || query.queryName);
                                    dom.attr('value', value || query.queryId);
                                    dom.attr('treeSelId', value || query.queryId);
                                    $(dom).idsInputTree({
                                        url: URLs.getUserDomainTree,
                                        parent: '#setting_station_manager_dialog',
                                        selectNodes: [value || query.queryId],
                                        checkStyle: 'radio',
                                        clickPIsChecked: true,
                                        textFiled: "name",
                                        isPromptlyInit: false,
                                        dataLevel: true,
                                        treeSearch: true,
                                        click2hidden: true,
                                        rootCheckable: false
                                    });
                                }
                            },
                            extend: {id: 'stationDomain', readonly: true}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '电站名称',
                            name: 'stationName',
                            rule: {
                                required: true,
                                nullCheck: true,
                                PSNameCheck: true,
                                stringCheck: true,
                                remote: {
                                    type: "POST",
                                    url: URLs.checkNameIsExists, //请求地址
                                    contentType: 'application/x-www-form-urlencoded',
                                    data: {
                                        stationName: function () {
                                            return $("#stationName").val();
                                        },
                                        stationCode: function () {
                                            return $('#settings_station_stationCode').val();
                                        }
                                    },
                                    dataType: 'json'
                                }
                            },
                            extend: {id: 'stationName'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '装机容量',
                            name: 'installedCapacity',
                            rule: {
                                required: true,
                                number: true
                            },
                            unit: 'kW',
                            extend: {id: 'stationCapacity'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '并网时间',
                            name: 'produceDate',
                            rule: {
                                required: true,
                                date: true
                            },
                            extend: {id: 'station_safeRunDatetime', class: 'Wdate'},
                            fnInit: function (dom, value, record) {
                                $(dom).val(Date.parse(value).format('yyyy-MM-dd'));
                            },
                            fnClick: function () {
                                DatePicker({
                                    dateFmt: 'yyyy-MM-dd',
                                    el: this,
                                    isShowClear: true
                                });
                            }
                        }
                    ],
                    // [
                    //     {
                    //         input: 'select',
                    //         type: 'select',
                    //         show: '逆变器类型',
                    //         name: 'inverterType',
                    //         rule: {
                    //             required: true
                    //         },
                    //         extend: {id: 'station_inverterType'},
                    //         options: [
                    //             {text: '请选择', value: ''},
                    //             {text: '集中式', value: '1'},
                    //             {text: '组串式', value: '2'},
                    //             {text: '混合式', value: '3'}
                    //         ]
                    //     }
                    // ],
                    [
                        {
                            input: 'select',
                            type: 'select',
                            show: '并网类型',
                            name: 'onlineType',
                            rule: {
                                required: true
                            },
                            extend: {id: 'station_combinedType'},
                            options: [
                                {text: '请选择', value: ''},
                                {text: '地面式', value: '1'},
                                {text: '分布式', value: '2'},
                                {text: '户用', value: '3'}
                            ]
                        }
                    ],
                    // [
                    //     {
                    //         input: 'select',
                    //         type: 'select',
                    //         show: '电站类型',
                    //         name: 'stationType',
                    //         rule: {
                    //             required: true
                    //         },
                    //         extend: {id: 'station_stationType'},
                    //         options: [
                    //             {text: '请选择', value: ''},
                    //             {text: '大型地面站', value: '1'},
                    //             {text: '分布式电站', value: '2'},
                    //             {text: '户用电站', value: '3'}
                    //         ]
                    //     }
                    // ],
                    [
                        {
                            input: 'select',
                            type: 'select',
                            show: '电站状态',
                            name: 'stationBuildStatus',
                            rule: {
                                required: true
                            },
                            extend: {id: 'station_status'},
                            options: [
                                {text: '请选择', value: ''},
                                {text: '规划中', value: '3'},
                                {text: '在建', value: '2'},
                                {text: '并网', value: '1'}
                            ]
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '联系人',
                            name: 'contactPeople',
                            extend: {id: 'stationContact'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '联系电话',
                            name: 'phone',
                            rule: {
                                vacTel: true
                            },
                            extend: {id: "stationContactPhone"}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'checkbox',
                            after: $('<span/>').text('是否扶贫电站'),
                            name: 'isPovertyRelief',
                            fnInit: function (dom, value, record) {
                                dom.prop('checked', !!value);
                            }
                        }
                    ]
                ],
                [
                    [
                        {
                            input: 'div',
                            type: 'group',
                            fnInit: function (dom, value, record) {
                                $(dom).empty();
                                var buttons = $('<div/>').addClass('layout-bar right devButtons');
                                var addBtn = $('<button/>').attr('type', 'button').addClass('btn').text(Msg.create);
                                var content = $('<div/>').addClass('content-box clearfix');
                                var oldContent = $('<div/>').addClass('oldContent content-box clearfix');

                                addBtn.off('click').on('click', function () {
                                    stationManager.addDevice(content);
                                });
                                buttons.append(addBtn);

                                var params = record ? {stationCode: record.stationCode} : {};
                                $.http.post(URLs.getDevByStationCode, params, function (resp) {
                                    if (resp.success) {
                                        $.each(resp.data, function () {
                                            stationManager.addDevice(oldContent, this);
                                        });
                                    }
                                });

                                $(dom).append(buttons).append(content).append(oldContent);
                            },
                            extend: {id: 'station_importDev'}
                        }
                    ]
                ],
                [
                    // [
                    //     {
                    //         input: 'select',
                    //         type: 'select',
                    //         show: '货币设置',
                    //         name: 'stationPriceCurrencySet',
                    //         options: [
                    //             {text: Msg.unit.currencyType.CNY, value: 'CNY'},
                    //             {text: Msg.unit.currencyType.USD, value: 'USD'},
                    //             {text: Msg.unit.currencyType.EUR, value: 'EUR'},
                    //             {text: Msg.unit.currencyType.GBP, value: 'GBP'}
                    //         ],
                    //         extend: {id: 'station_price_currency'}
                    //     }
                    // ],
                    [
                        {
                            input: 'input',
                            type: 'number',
                            show: Msg.modules.settings.stationManagement.priceSettingList.gridPrice,
                            name: 'stationPrice',
                            unit: Msg.unit.currencyType.CNY,
                            extend: {id: 'station_price_grid', class: 'modelPrice', step: '0.01'},
                            fnInit: function (dom) {
                                $.http.post(URLs.getPowerPriceById, query, function (resp) {
                                    if (resp.success) {
                                        var data = resp.data;
                                        if (data && data.price != undefined) {
                                            dom.val(data.price);
                                        }
                                    }
                                });
                            }
                        }
                    ],
                    [
                        {
                            input: 'group',
                            type: 'group',
                            show: '分时设置',
                            name: 'stationPriceDaypartingSet',
                            css: {maxHeight: 390},
                            fnInit: function (dom, value, record) {
                                $(dom).empty();

                                var box = $('<div/>').addClass('dayparting-box');
                                var buttons = $('<div/>').addClass('dayparting-buttons');

                                var addBtn = $('<button type="button"/>').addClass('btn icon-btn').html('+');
                                addBtn.off('click').on('click', function () {
                                    stationManager.addPriceDayparting(box);
                                });
                                buttons.css({
                                    'text-align': 'right'
                                }).append(addBtn);

                                // 获取电价分时设置
                                var params = record ? {stationCode: record.stationCode} : {};
                                $.http.post(URLs.getPriceDayPartingByStationCode, params, function (resp) {
                                    if (resp.success) {
                                        $.each(resp.data, function () {
                                            stationManager.addPriceDayparting(box, this);
                                        });
                                    }
                                });

                                $(dom).append(buttons).append(box);
                            },
                            extend: {id: 'station_price_dayparting'}
                        }
                    ]
                ],
                [
                    [
                        {
                            input: 'input',
                            type: 'image-form',
                            show: '电站图片',
                            name: 'stationFileId',
                            fileDisDir: URLs.downloadFile + '?fileId=',
                            extend: {id: 'station_image'},
                            process: function (dom) {
                                if ($(dom).val().trim() == '') {
                                    App.alert('未选择文件');
                                    return;
                                }
                                var img = $(dom).parent().find('img');
                                main.UploadFile($(dom).attr('id'), {
                                    fileId: $(dom).data('fileId')
                                }, function (data) {
                                    main.DownloadFile({target: img, fileId: data.results});
                                    $('input[name="stationFileId"]', '#settings_station_manager_dialog_content')
                                        .data('fileId', data.results).val(data.results);
                                });
                            },
                            fnInit: function (dom, value) {
                                $(dom).data('fileId', value);
                                var img = $(dom).parent().find('img');
                                main.DownloadFile({target: img, fileId: value});
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '经度',
                            name: 'longitude',
                            rule: {
                                required: true,
                                number: true,
                                range: [-180, 180]
                            },
                            extend: {id: 'stationLongitude'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '纬度',
                            name: 'latitude',
                            rule: {
                                required: true,
                                number: true,
                                range: [-90, 90]
                            },
                            extend: {id: 'stationLatitude'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '所在地区',
                            name: 'areaCode',
                            rule: {
                                required: true
                            },
                            fnInit: function (dom, value) {
                                var prov, city, dist;
                                if (value) {
                                    var vs = value.split('@');
                                    if (vs.length > 0) {
                                        prov = vs[0];
                                        vs.length > 1 && (city = vs[1]);
                                        vs.length > 2 && (dist = vs[2]);
                                    }
                                }
                                var content = $(dom).parent();
                                content.empty();
                                content.citySelect({
                                    prov: prov || '110000',
                                    city: city,
                                    dist: dist,
                                    nodata: 'hidden',
                                    required: true
                                });
                            },
                            extend: {id: 'stationArea'}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '详细地址',
                            name: 'stationAddr',
                            rule: {
                                maxlength: 200
                            },
                            css: {width: '100%'},
                            extend: {id: 'stationAddress'}
                        }
                    ],
                    [
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: '电站简介',
                            name: 'stationDesc',
                            rule: {
                                maxlength: 1000
                            },
                            css: {width: '100%', height: 120},
                            extend: {id: 'stationDesc'}
                        }
                    ]
                ]
            ];

            // 获取区域电价
            $.http.post(URLs.getPowerPriceById, query, function (resp) {
                if (resp.success) {
                    var data = resp.data;
                    if (data && data.price != undefined) {
                        model[2].unshift(
                            [
                                {
                                    input: 'input',
                                    type: 'checkbox',
                                    name: 'useDefaultPrice',
                                    after: $('<span/>').text(Msg.modules.settings.stationManagement.priceSettingList.useDefaultPrice + "（" + data.price + " " + Msg.unit.currencyType[data.unit] + "）"),
                                    extend: {id: 'useDefaultPrice'},
                                    fnInit: function (dom, value) {
                                        dom.off('click').on('click', function () {
                                            // $('#station_price_dayparting').attr('disabled', this.checked);
                                            $('#station_price_grid').val(data.price).attr('readonly', this.checked);
                                        });
                                        dom.get(0).checked = !!value;
                                        dom.trigger('click');
                                    }
                                }
                            ],
                            [
                                {
                                    input: 'input',
                                    type: 'hidden',
                                    name: 'defaultGridPrice',
                                    hide: true,
                                    fnInit: function (dom) {
                                        dom.val(data.price);
                                        $('#station_price_grid').val(data.price);
                                    }
                                }
                            ]
                        );
                    }
                }
            }, null, false);
            // 获取设备版本列表
            $.http.post(URLs.getModelVersionCodeList, {}, function (resp) {
                if (resp.success) {
                    stationManager.modelVersionCodeList = resp.data;
                }
            }, null, false);

            $.easyTabs($content, {
                tabIds: tabIds,
                tabNames: ['基础信息', '设备接入', '电价设置', '其他信息'],                  // 标签按钮显示内容
                keepPage: true,
                procedure: true, // 所有标签必须按照顺序切换
                bodys: $('<div/>').attr('id', 'settings_station_manager_dialog_content')
                //表单提交的信息 先将所有的内容在一个标签中信息出来以后修改了easyTabs插件后再修改对应的单独显示
                    .ValidateForm('settings_station_manager_dialog_content', {
                        submitURL: subUrl,//表单提交的路径
                        fnModifyData: function (data) {
                            // 提交表单前修改数据
                            data.domainId = $('#stationDomain').attr('value');
                            data.domainName = $('#stationDomain').val();
                            data.isPovertyRelief = data.isPovertyRelief ? 1 : 0;
                            data.useDefaultPrice = data.useDefaultPrice ? 1 : 0;
                            data.useDefaultPrice && (data.stationPrice = data.defaultGridPrice);
                            data.areaCode = data.prov + '@' + data.city + (data.dist ? '@' + data.dist : '');

                            data.priceList = [];
                            $(".dayparting-box > p", "#station_price_dayparting").each(function () {
                                var k = $(this).attr('index') || '0';
                                var startDate = $('#rangeStart_' + k, $(this)).val(),
                                    endDate = $('#rangeEnd_' + k, $(this)).val();

                                $('.dayparting-hours-content > p', $(this)).each(function () {
                                    var hoursSelect = $('select.hoursSelect', $(this));
                                    var times = {
                                        startDate: Date.parseTime(startDate),
                                        endDate: Date.parseTime(endDate),
                                        startTime: $(hoursSelect[0]).val(),
                                        endTime: $(hoursSelect[1]).val(),
                                        price: $('input.hoursVal', $(this)).val()
                                    };
                                    data.priceList.push(times);
                                });
                            });

                            data.devInfoList = [];
                            var devTags = $(".addDevTag", "#station_importDev");
                            for (var i = 1; i <= devTags.length; i++) {
                                data.devInfoList.push({
                                    snCode: data['addDevESN' + i],
                                    devAlias: data['addDevName' + i],
                                    devTypeId: data['addDevType' + i],
                                    signalVersion: data['addVersionCode' + i]
                                });
                            }

                            return data;
                        },
                        fnSubmitSuccess: function (data) {
                            $('#setting_station_manager_dialog').modal('hide');
                            $('#setting_station_manage_list').GridTableReload();
                        },
                        fnSubmitError: function (data) {
                            App.alert(data.message || Msg.operateFail);
                        },
                        header: [
                            {
                                title: '基础信息',
                                id: 'tb_baseInfo',
                                show: false,
                                extend: {
                                    class: 'easy-tabs-bodys'
                                }
                            },
                            {
                                title: '设备接入',
                                id: 'tb_importDev',
                                show: false,
                                extend: {
                                    class: 'easy-tabs-bodys'
                                }
                            },
                            {
                                title: '电价设置',
                                id: 'tb_priceSet',
                                show: false,
                                extend: {
                                    class: 'easy-tabs-bodys'
                                }
                            },
                            {
                                title: '其他信息',
                                id: 'tb_other',
                                show: false,
                                extend: {
                                    class: 'easy-tabs-bodys'
                                }
                            }
                        ],
                        listURL: URLs.getStationById,	//表单内容获取的请求url，根据id获取信息的url
                        type: type,  //add:新增 modify：修改 view：查看
                        params: params,
                        messages: {
                            stationName: {
                                remote: '该电站名称已经存在'
                            }
                        },
                        model: model,
                        extraButtons: {
                            align: 'center',
                            model: [
                                {
                                    input: 'button',
                                    type: 'button',
                                    show: '下一步',
                                    name: '',
                                    align: 'center',
                                    extend: {class: 'btn nextBtn'},
                                    fnClick: function (e) {
                                        var m = $content.data('methods');
                                        m.select(+$content.data('selectedIndex') + 1);
                                    }
                                },
                                {
                                    input: 'button',
                                    type: 'button',
                                    show: '上一步',
                                    name: '',
                                    align: 'center',
                                    extend: {class: 'btn prevBtn'},
                                    fnClick: function (e) {
                                        var m = $content.data('methods');
                                        m.select(+$content.data('selectedIndex') - 1);
                                    }
                                }
                            ]
                        }
                    }),
                change: function (body, tabId, tabIndex, isInit) {
                    // tabs和表单按钮绑定关系
                    // 上一步、下一步、确认、取消
                    var buttons = body.parent().find('.button-pane .btn');
                    if (tabIndex == 0) {
                        buttons.eq(0).hide();
                        buttons.eq(1).show();
                        buttons.eq(2).hide();
                    }
                    if (tabIndex == 1) {
                        buttons.eq(0).show();
                        buttons.eq(1).show();
                        buttons.eq(2).hide();
                    }
                    if (tabIndex == 2) {
                        buttons.eq(0).show();
                        buttons.eq(1).show();
                        buttons.eq(2).hide();
                    }
                    if (tabIndex == 3) {
                        buttons.eq(0).show();
                        buttons.eq(1).hide();
                        buttons.eq(2).show();
                    }
                    !isInit && $('#settings_station_manager_dialog_content').ValidateInit(tabIndex);
                },
                changeBefore: function (index) {
                    if (index == 0) return true;

                    var flag = true;

                    for (var i = 1; i <= index; i++) {
                        flag = flag && stationManager.validatePage(i);
                    }

                    return flag;
                }
            });
        },

        /**
         * 验证指定标签页
         * @param index 标签页序列号，从1开始
         * @returns {boolean} 是否验证通过
         */
        validatePage: function (index) {

            /*表单校验*/
            var valid = function (domId) {
                return $('#settings_station_manager_dialog_contentValidate').validate().element('#' + domId);
            };

            var totalFlag = true;
            //第一页基础信息
            if (index == 1) {
                // var domain = valid("stationDomain");
                var stationName = valid("stationName");                      //电站名称
                var stationCapacity = valid("stationCapacity");              //装机容量
                var gridTime = valid("station_safeRunDatetime");             //并网时间
                var contact = valid("stationContact");                       //联系人
                var phone = valid("stationContactPhone");                    //联系电话
                // var inverterType = valid("station_inverterType");            //逆变器类型
                var onlineType = valid("station_combinedType"); 		     //并网类型
                // var stationType = valid("station_stationType");              //电站类型
                var stationStatus = valid("station_status");                 //电站状态

                totalFlag = stationName && stationCapacity && gridTime && contact && phone
                    && onlineType && stationStatus;
            }
            //第二页设备接入
            else if (index == 2) {
                var esnFlag = true;
                $(".addDevTag", '#settings_station_manager_dialog_contentValidate').each(function () {
                    $(this).find("input.esnCode").each(function () {
                        var esnT = $(this).val();
                        if (!esnT) {
                            $(this).addClass('error');
                            $(this).tooltip({
                                title: Msg.modules.settings.stationManagement.deviceInfo.noESN,
                                placement: "auto right",
                                container: $(this).closest('dialog') || '#main_view'
                            });
                            esnFlag = false;
                            return false;
                        } else {
                            $(this).removeClass('error');
                        }
                    });
                    $(this).find("input.devName").each(function () {
                        var nameT = $(this).val();
                        if (!nameT) {
                            $(this).addClass('error');
                            $(this).tooltip({
                                title: Msg.modules.settings.stationManagement.deviceInfo.noDevName,
                                placement: "auto right",
                                container: $(this).closest('dialog') || '#main_view'
                            });
                            esnFlag = false;
                            return false;
                        } else {
                            $(this).removeClass('error');
                        }
                    });
                    $(this).find(".deviceTypeId").each(function () {
                        var nameT = $(this).val();
                        if (!nameT) {
                            $(this).addClass('error');
                            $(this).tooltip({
                                title: Msg.modules.settings.stationManagement.deviceInfo.noVersionCode,
                                placement: "auto right",
                                container: $(this).closest('dialog') || '#main_view'
                            });
                            esnFlag = false;
                            return false;
                        } else {
                            $(this).removeClass('error');
                        }
                    });
                });

                return esnFlag;
            }
            //第三页电价设置
            else if (index == 3) {
                if ($("#useDefaultPrice").length > 0) {
                    var isDefault = $("#useDefaultPrice")[0].checked;
                    if (isDefault && $('.dayparting-box > p', '#station_price_dayparting').length <= 0) {
                        return true;
                    }
                }

                var flag = true;

                $(".easy-tabs-bodys .modelPrice").each(function () {
                    var priceT = $(this).val();
                    if (!priceT && flag) {
                        $(this).addClass('error');
                        $(this).tooltip({
                            title: Msg.modules.settings.stationManagement.priceSettingList.plsFillinPriceinfo,
                            placement: "auto right",
                            container: $(this).closest('dialog') || '#main_view'
                        });
                        flag = false;
                        return false;
                    }
                    var reg = /(^[1-9]\d*(\.\d{1,2})?$)|(^[0]{1}(\.\d{1,2})?$)/;
                    if (reg.test(priceT) != true && flag) {
                        $(this).addClass('error');
                        $(this).tooltip({
                            title: Msg.modules.settings.stationManagement.priceSettingList.plsFillinPriceCorrectly,
                            placement: "auto right",
                            container: $(this).closest('dialog') || '#main_view'
                        });
                        flag = false;
                        return false;
                    }
                    if (isNaN(priceT) || 0 > priceT || priceT > 10) {
                        $(this).addClass('error');
                        $(this).tooltip({
                            title: Msg.modules.settings.stationManagement.priceSettingList.plsFillinPriceCorrectly,
                            placement: "auto right",
                            container: $(this).closest('dialog') || '#main_view'
                        });
                        flag = false;
                        return false;
                    }
                });

                if ($('.dayparting-box > p', '#station_price_dayparting').length > 0) {
                    var crossFlag = false;
                    var daypartDates = [], daypartTimes = [];
                    $('.dayparting-box > p', '#station_price_dayparting').each(function () {
                        var index = $(this).attr('index') || '0';
                        var dateObj = {
                            startDate: $('#rangeStart_' + index).val(),
                            endDate: $('#rangeEnd_' + index).val()
                        };
                        daypartDates.push(dateObj);
                        if (!(dateObj.startDate && dateObj.endDate)) {
                            $('#rangeStart_' + index).addClass('error');
                            $('#rangeEnd_' + index).addClass('error');
                            $('#rangeStart_' + index).tooltip({
                                title: Msg.modules.settings.stationManagement.priceSettingList.plInputCorrectDate,
                                placement: "auto right",
                                container: $(this).closest('dialog') || '#main_view'
                            });
                            $('#rangeEnd_' + index).tooltip({
                                title: Msg.modules.settings.stationManagement.priceSettingList.plInputCorrectDate,
                                placement: "auto right",
                                container: $(this).closest('dialog') || '#main_view'
                            });
                            flag = false;
                            return false;
                        } else {
                            $('#rangeStart_' + index).removeClass('error');
                            $('#rangeStart_' + index).tooltip('destroy');
                            $('#rangeEnd_' + index).removeClass('error');
                            $('#rangeEnd_' + index).tooltip('destroy');
                        }

                        var startTemp = dateObj.startDate;
                        var endTemp = dateObj.endDate;
                        $(this).siblings().each(function() {
                            var index = $(this).attr('index') || '0';
                            var minT = $('#rangeStart_' + index).val();
                            var maxT = $('#rangeEnd_' + index).val();
                            if (endTemp > minT && startTemp < maxT) {
                                crossFlag = true;
                                return true;
                            }
                        });

                        if (crossFlag) {
                            App.alert(Msg.modules.settings.stationManagement.priceSettingList.dateRangeCross);
                            flag = false;
                            return false;
                        }

                        $('.dayparting-hours-content > p', this).each(function () {
                            var hoursSelect = $('select.hoursSelect', this);
                            var times = {
                                startTime: $(hoursSelect[0]).val(),
                                endTime: $(hoursSelect[1]).val()
                            };
                            daypartTimes.push(times);
                            var startTemp = parseInt(times.startTime);
                            var endTemp = parseInt(times.endTime);
                            $(this).siblings().each(function() {
                                var sel = $('select.hoursSelect', this);
                                var minT = parseInt($(sel[0]).val());
                                var maxT = parseInt($(sel[1]).val());
                                if (endTemp > minT && startTemp < maxT) {
                                    crossFlag = true;
                                    return true;
                                }
                            });
                        });

                        if (crossFlag) {
                            App.alert(Msg.modules.settings.stationManagement.priceSettingList._24hoursCross);
                            flag = false;
                            return false;
                        }
                    });
                }

                return flag;
            }
            else if (index == 4) {
                var longitude = valid("stationLongitude");                   //经度
                var latitude = valid("stationLatitude");                     //纬度
                var address = valid("stationAddress");                       //电站地址
                var area = valid("stationArea");                             //电站所属省市县地区
                var brief = valid("stationDesc");                           //电站简介

                totalFlag = longitude && latitude && address && area && brief;
            }

            return totalFlag;
        },

        /**
         * 添加设备项
         * @param content
         * @param data
         */
        addDevice: function (content, data) {
            var index = $(".addDevTag").length + 1;
            var snId = 'addDevESN' + index;
            var nameId = 'addDevName' + index;
            var typeId = 'addDevType' + index;
            var versionCode = "addVersionCode" + index;
            var newDevRecord = $("<div/>").attr('index', index);
            newDevRecord.append($("<p/>")).addClass("addDevTag");
            if (data) {
                var snVal = data.snCode, nameVal = data.devAlias, typeVal = data.devTypeId,
                    devIdVal = data.id, modelVersionCodeVal = data.signalVersion;
                newDevRecord.find("p").addClass("devTag").append(
                    [
                        $('<b class="dc" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.devESN + "</span>"),
                        $('<input type="text" readonly title="' + snVal + '" value="' + snVal + '" class="esnCode" name="' + snId + '" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.devName + "</span>"),
                        $('<input type="text" readonly title="' + nameVal + '" value="' + nameVal + '" class="busiName" name="' + nameId + '" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.versionCode + "</span>"),
                        $('<select class="deviceTypeId" title="' + modelVersionCodeVal + '" name="' + typeId + '" />'),
                        $('<input type="hidden" value="' + modelVersionCodeVal + '" class="modelVersionCode" name="' + versionCode + '" />'),
                        $('<b class="delDevBtn"></b>')
                    ]
                ).attr("add", "add").attr("devId", devIdVal);
            }
            else {
                newDevRecord.find("p").addClass("devTag").append(
                    [
                        $('<b class="dc" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.devESN + "</span>"),
                        $('<input type="text" class="esnCode" name="' + snId + '" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.devName + "</span>"),
                        $('<input type="text" class="busiName" name="' + nameId + '" />'),
                        $("<span>" + Msg.modules.settings.stationManagement.deviceInfo.versionCode + "</span>"),
                        $('<select class="deviceTypeId" name="' + typeId + '" />'),
                        $('<input type="hidden" class="modelVersionCode" name="' + versionCode + '" />'),
                        $('<b class="delDevBtn"></b>')
                    ]
                ).attr("add", "add");
            }
            newDevRecord.find("b.dc").hide();
            // esn号绑定事件
            newDevRecord.find(".esnCode").off("change keydown").on("change keydown", function (e) {
                if (e.type == "change") {
                    var that = $(this);
                    if (that.parent().parent().find("div.kids").length > 0) {
                        that.parent().parent().find("div.kids").remove();
                    }
                    that.siblings("b.dc").removeClass("dcOpen");

                    that.siblings("b.delDevBtn").show();
                    var esnTemp = that.val().trim();

                    if (esnTemp) {
                        // 判断重复
                        var repeatFlag = false, repeatCode = [];
                        var esnInputList = $("input.esnCode");
                        if (esnInputList.length > 1) {
                            $(esnInputList).each(function () {
                                var v = $(this).val();
                                if (repeatCode.contains(v)) {
                                    repeatFlag = true;
                                    return false;
                                } else {
                                    repeatCode.push(v);
                                }
                            });
                        }
                        if (repeatFlag) {
                            that.siblings("b.dc").hide();
                            that.parent().css('border-color', 'red');
                            that.tooltip({
                                title: Msg.modules.settings.stationManagement.deviceInfo.devHasBeenBind,
                                placement: "auto right",
                                container: that.closest('dialog') || '#main_view'
                            });
                            return;
                        }
                        // 后台查询设备并接入，同时填入名称和类型
                        $.http.post(URLs.getDevBySN, {snCode: that.val().trim()}, function (data) {
                            if (data && data.success) {
                                var dev = data.data;
                                that.siblings(".esnCode").val(dev.snCode).attr("title", dev.snCode);
                                that.siblings(".busiName").val(dev.devAlias);
                                that.siblings(".modelVersionCode").val(dev.signalVersion);
                                var version = that.siblings(".deviceTypeId");
                                version.val(dev.devTypeId);
                                that.parent().attr("devId", dev.id);

                                // 判断是否已被绑定
                                if (dev.stationCode) {
                                    that.siblings("b.dc").hide();
                                    that.parent().css('border-color', 'red');
                                    that.tooltip({
                                        title: Msg.modules.settings.stationManagement.deviceInfo.devHasBeenBind,
                                        placement: "auto right",
                                        container: that.closest('dialog') || '#main_view'
                                    });
                                    that.attr("isBind", "yes");
                                    return;
                                } else {
                                    that.attr("isBind", "no");
                                    var subInverterList = data.data.subDevList;
                                    // 判断是否是数采且下挂有设备
                                    if (subInverterList && subInverterList.length > 0) {
                                        that.siblings("b.dc").show();
                                        that.siblings("b.dc").css("margin-left", "0");
                                        var childrenDiv = $("<div/>");
                                        var length = subInverterList.length;
                                        if (length > 0) {
                                            stationManager.addChildDevice(subInverterList, childrenDiv, that.parent().parent(), false, 1);
                                        }
                                        that.siblings("b.dc").off("click").on("click", function () {
                                            $(this).parent().siblings(".kids").toggleClass("hideKids");
                                            $(this).toggleClass("dcOpen");
                                        });
                                        $(".kids").find("b.delDevBtn").off("click").on("click", function () {
                                            $(this).parent().remove();
                                        });
                                        if (subInverterList.length > 8) { //如果下联设备数量大于8个则需要将宽度置为99%
                                            $(newDevRecord).find(".kids").find("p").css('width', "99%");
                                        }
                                    }
                                    $(".kids").find("b.delDevBtn").off("click").on("click", function () {
                                        if ($(this).parents("div.kids").children().length == 1) {
                                            $(this).parents('div.kids').siblings('.devTag').find("b.dc").remove();
                                        }
                                        $(this).parent().remove();

                                    });
                                    childrenDiv = null;
                                }
                            } else {
                                that.siblings("b.dc").hide();
                                that.parent().css('border-color', 'red');
                                that.tooltip({
                                    title: Msg.modules.settings.stationManagement.deviceInfo.noDevData,
                                    placement: "auto right",
                                    container: that.closest('dialog') || '#main_view'
                                });
                                that.attr("isBind", "yes");
                                return;
                            }
                        });
                    }
                }
                else if (e.type == "keydown") {
                    $(this).parent().css('border-color', '');
                    $(this).tooltip("destroy");
                }
            });

            // 构造signalVersion下拉列表项
            var versionList = stationManager.modelVersionCodeList;
            $(versionList).each(function (i, t) {
                newDevRecord.find(".deviceTypeId").append($('<option/>').attr('value', t.devTypeId).text(t.signalVersion));
            });
            newDevRecord.find(".deviceTypeId").off("change").on("change", function () {
                var title = $(this).find('option:selected').text();
                $(this).siblings('.modelVersionCode').val(title);
                $(this).attr('title', title);
            });
            data && newDevRecord.find(".deviceTypeId").val(data.devTypeId);
            newDevRecord.find("b.delDevBtn").off("click").on("click", function () {
                $(this).parent().parent().remove();
            });
            content.append(newDevRecord);
        },

        /**
         * 添加设备子项
         * @param subInverterList
         * @param childrenDiv
         * @param parentDiv
         * @param isBind
         * @param subIndex
         */
        addChildDevice: function (subInverterList, childrenDiv, parentDiv, isBind, subIndex) {
            for (var i = 0; i < subInverterList.length; i++) {
                var dev = subInverterList[i].dev ? subInverterList[i].dev : subInverterList[i];
                var subList = subInverterList[i].subDevList;
                var esnValidate = $(childrenDiv).attr("esnValidate");
                if ((2 != dev.devTypeId || dev.parentTypeId == 13) && 37 != dev.devTypeId && (isBind || !dev.stationCode)) {
                    var ele = $(parentDiv).find("p:first").length > 0 ? $(parentDiv).find("p:first") : $(parentDiv).siblings("p:first");
                    var newChild = $(ele).clone(false);
                    newChild.attr("esnValidate", esnValidate);
                    newChild.find("input.devName").val(dev.devAlias).attr("title", dev.devAlias);
                    newChild.find("input.devESN").val(dev.snCode).attr("disabled", "disabled").attr("esnValidate", esnValidate).attr("title", dev.snCode);
                    newChild.find("input.devType").val(dev.signalVersion).attr("disabled", "disabled").attr("title", dev.signalVersion);
                    newChild.find("input.pType").val(dev.devTypeId);
                    newChild.find("input.devId").val(dev.id);
                    newChild.find("input.busiCode").val(dev.busiCode);
                    if (!subList || subList.length <= 0) {
                        newChild.find("b.dc").remove();
                        newChild.removeClass("devTag");
                    }
                    if (isBind) {
                        newChild.attr("add", "add");
                    }
                    if (2 == subIndex) {
                        newChild.css("margin-left", "0%").css("width", "97%").attr("devId", dev.id);
                        childrenDiv.css("border", "1px dotted green");
                    } else {
                        newChild.attr("devId", dev.id).css("width", "97%");
                    }
                    //$(childrenDiv).css("margin-left","-1%");
                    childrenDiv.append(newChild).addClass("kids").addClass("hideKids");
                    if (subList && subList.length > 0) {
                        var div = $("<div/>");
                        var height = (40 * subList.length) + 40 + "px";
                        stationManager.addChildDevice(subList, div, newChild, isBind, 2);
                        $(newChild.find("b.dc")).css("margin-left", "-4%");
                        //newChild.css("height","100px");
                        $(newChild.find("b.dc")).off("click").on("click", function () {
                            $(this).parent().find(".kids").toggleClass("hideKids");
                            $(this).toggleClass("dcOpen");
                            if ('30px' == $(this).parent().css("height")) {
                                $(this).parent().css("height", height);
                            } else {
                                $(this).parent().css("height", "30px");
                            }
                        });
                    }
                }
            }
            parentDiv.append(childrenDiv);
        },

        /**
         * 添加电价分时设置项
         * @param box 添加到指定容器
         * @param data 数据
         */
        addPriceDayparting: function (box, data) {
            var index = box.find('>p').length;
            var valBox = $('<p/>').attr('index', index);

            var valBar = $('<div>').addClass('dayparting-bar'),
                valContent = $('<div>').addClass('dayparting-content'),
                closeBtn = $('<a>');

            var from = $('<input type="text" id="rangeStart_' + index + '" value=""/>'),
                to = $('<input type="text" id="rangeEnd_' + index + '" value=""/>');

            from.attr('placeholder', Msg.modules.settings.stationManagement.priceSettingList.plsSelectStartDate).placeholderSupport();
            to.attr('placeholder', Msg.modules.settings.stationManagement.priceSettingList.plsSelectEndDate).placeholderSupport();
            from.addClass('Wdate').css({
                width: 160
            }).click(function () {
                DatePicker({dateFmt: 'yyyy-MM-dd', el: this, maxDate: '#F{$dp.$D(\'rangeEnd_' + index + '\')}'});
            });
            to.addClass('Wdate').css({
                width: 160
            }).click(function () {
                DatePicker({dateFmt: 'yyyy-MM-dd', el: this, minDate: '#F{$dp.$D(\'rangeStart_' + index + '\')}'});
            });
            valBar.append(from).append('~ ').append(to);

            /**
             * 在容器中追加小时设置项
             * @param content 容器
             * @param data 数据
             * @param f 是否重新初始化
             */
            function addHours(content, data, f) {
                var p = $('<p>');
                var hoursSelect = $('<select/>').addClass('hoursSelect');
                for(var i = 0; i <= 24; i++) {
                    hoursSelect.append('<option value="' + i + '">' + (i < 10 ? ('0' + i + ':00') : (i + ':00')) + '</option>');
                }
                var val = $('<input type="number">').addClass('modelPrice hoursVal');
                val.attr('step', '0.01').css({
                    width: 100
                });
                var closeBtn = closeBtn = $('<a>').addClass('btn btn-close dayparting-hours-close').html('-')
                    .off('click').on('click', function () {
                        p.remove();
                    });

                f && content.empty();
                var startTimeSel = hoursSelect.attr('placeholder', Msg.modules.settings.stationManagement.priceSettingList.plsSelectStartTime);
                var endTimeSel = hoursSelect.clone().attr('placeholder', Msg.modules.settings.stationManagement.priceSettingList.plsSelectEndTime);
                startTimeSel.placeholderSupport();
                endTimeSel.placeholderSupport();

                if (data) {
                    startTimeSel.val(data.startTime);
                    endTimeSel.val(data.endTime);
                    val.val(data.price);
                }

                p.append(startTimeSel).append('~ ').append(endTimeSel)
                    .append('<span> ' + Msg.modules.settings.stationManagement.priceSettingList.plsFillinPrice + ': </span>')
                    .append(val);
                !f && p.append(closeBtn);
                content.append(p);
            }

            // 24小时分时设置
            var hoursBox = $('<div>').addClass('dayparting-hours-content');
            var addBtn = $('<a>').addClass('btn icon-btn dayparting-hours-addBtn').html('+')
                .off('click').on('click', function () {
                    addHours(hoursBox);
                });
            if (data) {
                var hoursPrices = data.hoursPrices;
                from.val(Date.parse(data.startDate).format('yyyy-MM-dd'));
                to.val(Date.parse(data.endDate).format('yyyy-MM-dd'));

                if (hoursPrices && hoursPrices.length > 0) {
                    $.each(hoursPrices, function (i, t) {
                        addHours(hoursBox, this, i === 0);
                    });
                }
            } else {
                addHours(hoursBox, null, true);
            }
            valContent.append(hoursBox).append(addBtn);

            closeBtn.addClass('btn btn-close dayparting-close').html('×')
                .off('click').on('click', function () {
                    valBox.remove();
                });

            valBox.append(valBar).append(valContent).append(closeBtn);
            box.append(valBox);
        },

        /**
         * 显示共享环境检测仪设置界面
         * @param stationList 所选电站列表
         */
        showStationDetector: function (stationList) {
            var $content = $('<div></div>').attr('id', 'setting_station_detector_set');//內容元素
            App.dialog({
                id: 'setting_station_detector_set_dialog',
                title: "设置共享环境监测仪",
                content: $content,
                width: 680,
                height: 320
            }).ValidateForm('setting_station_detector_set', {
                submitURL: URLs.saveShareEmi,
                fnSubmitSuccess: function () {
                    $('#setting_station_manage_list').GridTableReload();
                },
                fnSubmitError: function () {
                    $('#setting_station_manage_list').GridTableReload();
                },
                model: [
                    [
                        [
                            {
                                input: 'div',
                                type: 'group',
                                show: '选择的电站',
                                fnInit: function (dom) {
                                    var stationCodeInput = $('<input/>').attr('type', 'hidden').attr('name', 'stationCode');
                                    var stationCodes = [];
                                    for (var i = 0; i < stationList.length; i++) {
                                        var id = stationList[i]['stationCode'];
                                        var name = stationList[i]['stationName'];

                                        if (name && id) {
                                            var item = $('<div>').addClass('c').attr('title', name).attr('stationCode', id);
                                            item.append($('<div>').addClass('t').text(name));
                                            item.append($('<div>x</div>').addClass('close').click((function (item, id) {
                                                return (function () {
                                                    var v = stationCodeInput.val();
                                                    var vs = v.split(',');
                                                    vs.remove(id);
                                                    item.remove();
                                                    stationCodeInput.val(vs.join(','));
                                                });
                                            })(item, id)));
                                            $(dom).append(item);
                                            stationCodes.push(id);
                                        }
                                    }
                                    stationCodeInput.val(stationCodes.join(','));
                                    $(dom).append(stationCodeInput);
                                },
                                css: {'min-height': 100},
                                extend: {class: 'selectedShowBox'}
                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '电站',
                                name: 'shareStationCode',
                                rule: {
                                    required: true
                                },
                                options: [
                                    {text: '无', value: ''}
                                ],
                                extend: {id: 'setting_station_detector_set_station'},
                                fnInit: function (dom) {
                                    dom.off('change').on('change', function () {
                                        var deviceSelect = $('#setting_station_detector_set_device');
                                        var stationCode = $(this).val();
                                        $.http.post(URLs.getEmiByStationCode, {stationCode: stationCode}, function (resp) {
                                            if (resp.success && resp.data) {
                                                deviceSelect.empty();
                                                deviceSelect.append('<option value="">请选择共享环境监测仪</option>');
                                                var deviceList = resp.data;
                                                $.each(deviceList, function () {
                                                    deviceSelect.append('<option value="' + this.id + '">' + this.devAlias + '</option>');
                                                });
                                            }
                                        });
                                    });
                                    $.http.post(URLs.getStationListWithPersonal, {}, function (resp) {
                                        if (resp.success && resp.data) {
                                            dom.empty();
                                            dom.append('<option value="">请选择共享数据来源电站</option>');
                                            var stationList = resp.data;
                                            $.each(stationList, function () {
                                                dom.append('<option value="' + this.stationCode + '">' + this.stationName + '</option>');
                                            });
                                        }
                                    });
                                }
                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '设备',
                                name: 'shareDeviceId',
                                rule: {
                                    required: true
                                },
                                options: [
                                    {text: '无', value: ''}
                                ],
                                extend: {id: 'setting_station_detector_set_device'}
                            }
                        ]
                    ]
                ],
                extraButtons: {
                    align: 'center',
                    model: [
                        {
                            input: 'button',
                            type: 'button',
                            show: '重置',
                            name: '',
                            align: 'center',
                            extend: {class: 'btn btn-reset'},
                            fnClick: function (e) {
                            }
                        }
                    ]
                }
            });
        }
    };

    return {
        Render: function (params) {
            this.initTree();
            this.initEvent();
        },

        initTree: function () {
            var self = this;
            var settings = {
                check: {
                    enable: false
                },
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        self.initSearchBar();

                        var type = 'area';
                        if (treeNode.model == 'E') type = 'enterprise';
                        else if (treeNode.model == 'D') type = 'area';
                        query = {
                            queryId: treeNode.id.replace(/^[DE]/g, ''),
                            queryType: type,
                            queryName: treeNode.name
                        };
                        self.initResult(query);//初始化表格

                        var treeObj = $.fn.zTree.getZTreeObj(treeId);
                        treeObj.expandNode(treeNode, true, false, true);
                    }
                }
            };
            $("#station_manager_tree").idsZtree(settings, {
                url: URLs.getUserDomainTree,
                param: {},
                treeSearch: false,
                treeLoadAfter: function (zTree) {
                    var nodes = zTree.getNodes();
                    zTree.selectNode(nodes[0]);
                    zTree.expandNode(nodes[0], true, false, true);
                    settings.callback.onClick(null, zTree.setting.treeId, nodes[0]);//调用事件
                },
                treeDataLoadFiled: function () {
                    self.initSearchBar();
                    self.initResult();
                }
            });
        },

        initSearchBar: function () {
            function search(data) {//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
                for (var key in data) {
                    if (key == 'params') {
                        continue;
                    }
                    data.params[key] = data[key];
                }
                $.extend(data.params, query);
                $('#setting_station_manage_list').GridTableReload(data);
            }

            function timeFormat(element, value, data) {
                element.focus(function () {
                    DatePicker({dateFmt: 'yyyy-MM-dd', el: this});
                });
            }

            $('#setting_station_manage_bar').ValidateForm('setting_station_manage_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//账号名称
                        name: 'stationName'
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '并网时间',//并网时间
                        name: 'onlineTime',
                        width: 181,
                        fnInit: timeFormat,
                        extend: {class: 'Wdate'}
                    }
                ]],
                extraButtons: [
                    {
                        input: 'button',
                        type: 'button',
                        show: '',
                        name: '',
                        extend: {'class': 'btn btn-filter'},
                        fnClick: function (e) {//点击回调函数
                            App.dialog({
                                id: 'settings_station_manage_filter_dialog',
                                title: '高级查询',
                                width: 800,
                                height: 'auto',
                                content: $('<div/>').attr('id', 'setting_station_manage_filter'),
                                buttons: [
                                    {
                                        id: 'okId',
                                        type: 'submit',
                                        text: Msg.sure || 'OK',
                                        clickToClose: true,
                                        click: function (e, d) {
                                            $('#setting_station_manage_filterValidate').submit();
                                        }
                                    },
                                    {
                                        id: 'cancelId',
                                        type: 'cancel',
                                        text: Msg.cancel || 'Cancel',
                                        clickToClose: true
                                    }
                                ]
                            }).ValidateForm('setting_station_manage_filter', {
                                fnSubmit: search,
                                noButtons: true,
                                model: [
                                    [
                                        {
                                            input: 'input',
                                            type: 'text',
                                            show: '电站名称',//账号名称
                                            name: 'stationName'
                                        },
                                        {
                                            input: 'input',
                                            type: 'text',
                                            show: '并网时间',//并网时间
                                            name: 'onlineTime',
                                            width: 181,
                                            fnInit: timeFormat,
                                            extend: {class: 'Wdate'}
                                        }
                                    ],
                                    // [
                                    //     {
                                    //         input: 'select',
                                    //         type: 'select',
                                    //         show: '逆变器类型',
                                    //         name: 'inverterType',
                                    //         options: [
                                    //             {text: '请选择', value: ''},
                                    //             {text: '集中式', value: '1'},
                                    //             {text: '组串式', value: '2'},
                                    //             {text: '混合式', value: '3'}
                                    //         ]
                                    //     },
                                    //     {
                                    //         input: 'select',
                                    //         type: 'select',
                                    //         show: '电站类型',
                                    //         name: 'stationType',
                                    //         options: [
                                    //             {text: '请选择', value: ''},
                                    //             {text: '大型地面站', value: '1'},
                                    //             {text: '分布式电站', value: '2'},
                                    //             {text: '户用电站', value: '3'}
                                    //         ]
                                    //     }
                                    // ],
                                    [
                                        {
                                            input: 'select',
                                            type: 'select',
                                            show: '并网类型',
                                            name: 'onlineType',
                                            options: [
                                                {text: '请选择', value: ''},
                                                {text: '地面式', value: '1'},
                                                {text: '分布式', value: '2'},
                                                {text: '户用', value: '3'}
                                            ]
                                        },
                                        {
                                            input: 'select',
                                            type: 'select',
                                            show: '电站状态',
                                            name: 'stationStatus',
                                            options: [
                                                {text: '请选择', value: ''},
                                                {text: '规划中', value: '3'},
                                                {text: '在建', value: '2'},
                                                {text: '并网', value: '1'}
                                            ]
                                        }
                                    ]
                                ]
                            });
                        }
                    }
                ]
            });
        },

        /**
         * 初始化表格数据
         * @param params
         */
        initResult: function (params) {
            $('#setting_station_manage_list').GridTable({
                url: URLs.getStationInfo,
                title: false,
                maxHeight: 585,
                height: 'auto',
                max_height: 585,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                showSelectedName: false,
                idProperty: 'id',
                colModel: [
                    {name: 'stationCode', hide: true},
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.15,
                        fnInit: function (dom) {
                            dom.addClass('setting_blue_color');
                        }
                    },
                    {
                        display: '装机容量',
                        name: 'installedCapacity',
                        //align: "center",
                        width: 0.1,
                        unit: ' kW'
                    },
                    {
                        display: '电站地址',
                        name: 'stationAddr',
                        //align: "center",
                        width: 0.2
                    },
                    {
                        display: '联系人',
                        name: 'contactPeople',
                        //align: "center",
                        width: 0.1
                    },
                    {
                        display: '联系方式',
                        name: 'phone',
                        //align: "center",
                        width: 0.15
                    },
                    {
                        display: '并网时间',
                        name: 'produceDate',
                        //align: "center",
                        width: 0.18,
                        fnInit: function (dom, value, record) {
                            var date = value ? Date.parse(value).format('yyyy-MM-dd') : '';
                            dom.html(date);
                            dom.parent().attr('title', date);
                        }
                    },
                    {
                        display: '环境监测仪',
                        name: 'deviceShare',
                        //align: "center",
                        width: 0.12,
                        fnInit: function (dom, value, record) {
                            var stationCode = record.stationCode;
                            $.http.post(URLs.getEmiByStationCode, {stationCode: stationCode}, function (resp) {
                                if (resp.success && resp.data) {
                                    var emiList = resp.data;
                                    var names = [];
                                    dom.empty();
                                    $.each(emiList, function (i, emi) {
                                        names.push(emi.devAlias);
                                    });
                                    dom.html(names.join());
                                    dom.parent().attr('title', names.join());
                                    $('#setting_station_manage_list').GridTableUpdateCell({
                                        id: record.id,
                                        name: 'deviceShare'
                                    }, emiList, null, false);
                                }
                            });
                        }
                    }
                ]
            });
        },

        /**
         * 初始化事件
         */
        initEvent: function () {
            var _this = this;
            var btns = $('#setting_station_manage_btns').find('input[type=button]');
            btns.unbind('click').bind('click', function (e) {
                var myType = $(this).attr('myType');//获取类型 分别是 add modify del set
                if (myType == 'del') {
                    var records = $('#setting_station_manage_list').GridTableSelectedRecords();
                    var length = records ? records.length : 0;
                    if (records && length > 0) {
                        var ids = [];
                        var sids = "";
                        for (var i = 0; i < length; i++) {
                            var station = {
                                id: records[i].id,
                                stationCode: records[i].stationCode
                            };
                            ids.push(station);

                            if (i != 0) {
                                sids += ",";
                            }
                            sids += records[i].stationCode;
                        }
                        App.confirm(Msg.modules.settings.stationManagement.messages.confirmDelete, function () {
                            $.http.post(URLs.deleteStation, {"ids": sids}, function (res) {
                                if (res.success) {
                                    App.alert(Msg.deleteSucceed, function () {
                                        $('#setting_station_manage_list').GridTableReload();
                                    });
                                }
                                else {
                                    var _data = data.data;
                                    var errorMsg = Msg.deleteFailed;
                                    if (_data && _data.errorMsg) {
                                        errorMsg = _data.errorMsg;
                                    }
                                    App.alert(errorMsg);
                                }
                            });
                        });
                    }
                    else {
                        App.alert(Msg.modules.settings.stationManagement.messages.atLeastOne);
                    }
                }
                else if (myType == 'set') {
                    // 设置共享环境监测仪
                    var selectedRecords = $('#setting_station_manage_list').GridTableSelectedRecords();
                    if (selectedRecords.length == 0) {
                        App.alert(Msg.modules.settings.stationManagement.messages.atLeastOne);
                        return;
                    }
                    // 验证是否存在自身拥有环境监测仪的电站
                    var personalStations = [];
                    for (var i = 0; i < selectedRecords.length; i++) {
                        var r = selectedRecords[i];
                        var d = r.deviceShare;
                        if (d && d.length > 0) {
                            var f = false;
                            $.each(d, function () {
                                if (this.emiType == 'personal') {
                                    f = true;
                                    return false;
                                }
                            });
                            f && personalStations.push(r.stationName);
                        }
                    }
                    if (personalStations.length > 0) {
                        App.alert(String.format(Msg.modules.settings.stationManagement.messages.personalStations, personalStations.join()));
                        return;
                    }

                    stationManager.showStationDetector(selectedRecords);
                }
                else if (myType == 'modify') {
                    var selectedRecord = $('#setting_station_manage_list').GridTableSelectedRecords();
                    if (selectedRecord.length == 0) {
                        App.alert(Msg.modules.settings.stationManagement.messages.atLeastOne);
                        return;
                    }
                    if (selectedRecord.length > 1) {
                        App.alert(Msg.modules.settings.stationManagement.messages.onlyOneSelected);
                        return;
                    }
                    stationManager.showStationDialog(myType, selectedRecord[0].stationCode);
                }
                else {
                    if (query && query.queryId) {
                        stationManager.showStationDialog(myType);
                    } else {
                        App.alert(Msg.modules.settings.stationManagement.messages.selectDomain);
                    }
                }
            });
        }

    };
});