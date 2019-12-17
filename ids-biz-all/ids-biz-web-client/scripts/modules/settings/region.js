'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'region',
    description: '模块功能：区域管理',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'idsInputTree', 'locationPicker'
    ]
});
App.Module('region', function ($) {
    var URLs = {
        getEnterpriseByUserId: main.serverUrl.biz + '/enterprise/getEnterpriseByUserId',
        getDomainTree: main.serverUrl.biz + '/domain/getDomainsByParentId',
		getDomainTrees: main.serverUrl.biz + '/domain/getDomainsByParentIdForTree',
        getDomainById: main.serverUrl.biz + '/domain/getDomainById',
        getAllDomainTree: main.serverUrl.biz + '/domain/getDomainTree',

        addRegion: main.serverUrl.biz + '/domain/insertDomain',
        modifyRegion: main.serverUrl.biz + '/domain/updateDomain',
        deleteRegion: main.serverUrl.biz + '/domain/deleteDomainById'
    };

    var query = {}, currentEnterprise, currentRegion;

    var region = {
        /**
         * 初始化查询条
         */
        initSearchBar: function () {
            $.http.post(URLs.getEnterpriseByUserId, {}, function (resp) {
                if (resp.success && resp.data) {
                    var data = resp.data;
                    var searchBar = region.$get('#settings_region_search_bar');
                    if (data.length > 0) {
                        query = {
                            id: data[0].id,
                            model: 'E'
                        };
                        region.initResult(query);

                        if (data.length > 1) {
                            searchBar.removeClass('hidden');
                            searchBar.ValidateForm('settings_region_search_bar', {
                                show: 'horizontal',
                                fnSubmit: function (data) {
                                    query = {
                                        id: data.enterpriseId,
                                        model: 'E'
                                    };
                                    region.initResult(query);
                                },
                                model: [[
                                    {
                                        input: 'select',
                                        type: 'select',
                                        show: '所属企业',
                                        name: 'enterpriseId',
                                        options: [
                                            {text: '请选择', value: ''}
                                        ],
                                        fnInit: function (dom) {
                                            dom.empty();
                                            $.each(data, function (i, t) {
                                                dom.append('<option value="' + t.id + '">' + t.name + '</option>');
                                            });
                                        }
                                    }
                                ]]
                            });
                        }
                        else if (data.length == 1) {
                            searchBar.remove();
                        }
                    } else {
                        searchBar.remove();
                    }
                }
            });
        },

        /**
         * 初始化树表
         * @param params
         */
        initResult: function (params) {
            currentEnterprise = params || currentEnterprise;
            currentRegion = null;

            function setNodes(parentNode, data) {
                var zNodes = [];
                if (!data || data.length <= 0) return zNodes;

                for (var i = 0; i < data.length; i++) {
                    var node = {
                        "id": data[i].id, "pId": data[i].pid,
                        "name": data[i].name, "model": "D",
                        "open": false, "isParent": true,
                        "isHidden": data[i].isHidden || false,
                        "path": data[i].path,
                        "text": data[i].text
                    };
                    var path = "";
                    if (parentNode && parentNode.path) {
                        path = parentNode.path;
                    }
                    if (node.pId && node.pId != '0') {
                        path = path ? (path + '@' + node.pId) : node.pId;
                    }
                    !node.path && (node.path = path);
                    path = node.path;

                    zNodes.push(node);
                }
                return zNodes;
            }

            $.http.post(URLs.getDomainTree, currentEnterprise || {}, function (resp) {
                if (resp.success && resp.data) {
                    var nodes = setNodes(null, resp.data);
                    var settings = {
                        async: {
                            enable: true,
                            url: URLs.getDomainTrees,
                            type: "post",
                            dataType: "json",
                            autoParam: ['id', 'model'],
                            // otherParam: query || {},
                            dataFilter: function (treeId, parentNode, responseData) {
                                var nodes = null;
                                if (responseData && responseData.code == 1) {
                                    nodes = setNodes(parentNode, responseData.results);
                                }
                                return nodes;
                            }
                        },
                        callback: {
                            onClick: function (event, treeId, treeNode) {
                                currentRegion = {
                                    id: treeNode.id,
                                    name: treeNode.name,
                                    model: treeNode.model
                                };
                                var tree = $.fn.zTree.getZTreeObj(treeId);
                                tree.expandNode(treeNode, true, true, true);
                            }
                        },
                        view: {
                            dblClickExpand: false,
                            showLine: false,
                            selectedMulti: false,
                            showIcon: false,
                            addDiyDom: function (treeId, treeNode) {
                                var aObj = $("#" + treeNode.tId + "_a");

                                if ($("#diyBtn_" + treeNode.id).length > 0) return;

                                var cell = $("<span class='diy-cell'></span>");
                                var desc = $("<div class='diy-cell-item settings-region-data-description'></div>");

                                desc.text(treeNode.text || '');

                                var btns = $("<div class='diy-cell-item diyBtns' data-content-id='" + treeNode.id + "'></div>");
                                var addBtn = $("<k class='diyBtn add' id='diyBtn_" + treeNode.id + "'></k>").text('新增');
                                var editBtn = $("<k class='diyBtn edit' id='diyBtn_" + treeNode.id + "'></k>").text('修改');
                                var delBtn = $("<k class='diyBtn del' id='diyBtn_" + treeNode.id + "'></k>").text('删除');

                                addBtn.bind("click", function (e) {
                                    region.showDialog('add', treeNode);
                                    e.stopPropagation();
                                });
                                editBtn.bind("click", function (e) {
                                    region.showDialog('modify', treeNode);
                                    e.stopPropagation();
                                });
                                delBtn.bind("click", function (e) {
                                    region.deleteRegion(treeNode.id);
                                    e.stopPropagation();
                                });

                                btns.append(addBtn).append(editBtn).append(delBtn);
                                cell.append(desc).append(btns);
                                aObj.append(cell);
                            }
                        },
                        data: {
                            simpleData: {
                                enable: true,
                                idKey: "id",
                                pIdKey: "pid",
                                rootPId: ""
                            },
                            key: {
                                name: 'name'
                            }
                        }
                    };
                    var zTree = $.fn.zTree.init(region.$get('#settings_region_list'), settings, nodes);

                    // var n = zTree.getNodes();
                    // zTree.selectNode(n[0]);
                    // settings.callback.onClick(null, zTree.setting.treeId, n[0]);//调用事件
                }
            });
        },

        /**
         * 工具条事件绑定
         */
        bindEvents: function () {
            var btns = region.$get('.settings-region-buttons .btn');
            btns.unbind('click').bind('click', function (e) {
                var type = this.dataset.type;
                if (type == 'add') {
                    if (currentEnterprise && currentEnterprise.id) {
                        region.showDialog(type);
                    } else {
                        App.alert(Msg.modules.settings.region.message.noEnterprise);
                    }
                }
            });
        },

        showDialog: function (type, node) {
            var contentId = 'settings_region_dialog_content_' + type,
                dialogId = 'settings_region_dialog_' + type,
                subUrl = type == 'modify' ? URLs.modifyRegion : URLs.addRegion,
                title = type == 'modify' && '修改区域';
            var params = node ? {id: node.id} : {};
            var $content = $('<div></div>').attr('id', contentId);

            var model = [
                [
                    [
                        {
                            input: 'input',
                            type: 'hidden',
                            name: 'id',
                            hide: true
                        },
                        {
                            input: 'input',
                            type: 'hidden',
                            name: 'enterpriseId',
                            hide: true,
                            fnInit: function (dom, value) {
                                dom.val(value || (currentEnterprise && currentEnterprise.id));
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '区域归属',
                            name: 'parentId',
                            fnInit: function (dom, value, record) {
                                var id = (node && node.id) || (currentRegion && currentRegion.id);
                                type == 'modify' && (id = value);
                                var name = (node && node.name) || (currentRegion && currentRegion.name);
                                type == 'modify' && (name = record && record.parentName);
                                dom.val(name);
                                dom.attr('value', id);
                                dom.attr('treeSelId', id);
                                $(dom).idsInputTree({
                                    url: URLs.getAllDomainTree,
                                    param: currentEnterprise,
                                    parent: '#' + dialogId,
                                    selectNodes: [id],
                                    checkStyle: 'radio',
                                    clickPIsChecked: true,
                                    textFiled: "name",
                                    isPromptlyInit: false,
                                    dataLevel: true,
                                    treeSearch: true,
                                    click2hidden: true,
                                    rootCheckable: false
                                });
                            },
                            extend: {id: 'settings_region_parent', readonly: true}
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '区域名称',
                            name: 'name',
                            rule: {
                                required: true,
                                nullCheck: true,
                                PSNameCheck: true
                            },
                            extend: {id: 'settings_region_name'}
                        }
                    ],
                    [
                        {
                            input: 'select',
                            type: 'select',
                            show: '货币设置',
                            name: 'currency',
                            extend: {id: 'settings_region_currency'},
                            options: [
                                {text: Msg.unit.currencyType.CNY, value: 'CNY'},
                                {text: Msg.unit.currencyType.USD, value: 'USD'},
                                {text: Msg.unit.currencyType.EUR, value: 'EUR'},
                                {text: Msg.unit.currencyType.GBP, value: 'GBP'}
                            ]
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'number',
                            show: '区域电价',
                            name: 'domainPrice',
                            rule: {
                                number: true
                            },
                            extend: {id: 'settings_region_domainPrice', class: 'modelPrice', step: '0.01'}
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
                            extend: {id: 'settings_region_longitude'},
                            fnInit: function (dom, value, record) {
                                region.selectLocation(dom, dialogId, record);
                            }
                        }
                    ],
                    [
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
                            extend: {id: 'settings_region_latitude'},
                            fnInit: function (dom, value, record) {
                                region.selectLocation(dom, dialogId, record);
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '区域范围',
                            name: 'radius',
                            rule: {
                                required: true,
                                number: true,
                                range: [0, 1000000000000]
                            },
                            unit: Msg.unit.meter,
                            extend: {id: "settings_region_radius"},
                            fnInit: function (dom, value, record) {
                                region.selectLocation(dom, dialogId, record);
                            }
                        }
                    ],
                    [
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: '区域描述',
                            name: 'description',
                            rule: {
                                maxlength: 500
                            },
                            height: 150,
                            extend: {id: 'settings_region_description'}
                        }
                    ]
                ]
            ];

            App.dialog({
                id: dialogId,
                title: title || "新增区域",
                content: $content,
                width: 500,
                height: 600
            }).ValidateForm(contentId, {
                submitURL: subUrl,//表单提交的路径
                fnModifyData: function (data) {
                    var pid = $('#settings_region_parent').attr('value');
                    data.parentId = pid || (currentRegion && currentRegion.id);
                    data.parentType = pid ? 'D' : (currentRegion && currentRegion.model);
                    return data;
                },
                fnSubmitSuccess: function (data) {
                    $('#setting_station_manager_dialog').modal('hide');
                    region.initResult();
                },
                fnSubmitError: function (data) {
                    App.alert(data.message || Msg.operateFail);
                },
                listURL: URLs.getDomainById,
                fnListSuccess: function (data) {
                },
                fnListError: function (data) {
                },
                type: type,  //add:新增 modify：修改 view：查看
                params: params,
                model: model
            });
        },

        deleteRegion: function (id) {
            App.confirm(Msg.modules.settings.region.message.deleteConfirm, function () {
                $.http.post(URLs.deleteRegion, {id: id}, function (resp) {
                    if (resp.success) {
                        App.alert(Msg.deleteSucceed);
                    } else {
                        App.alert(Msg.deleteFailed);
                    }
                    region.initResult(query);
                });
            });
        },

        selectLocation: function (dom, contentId, data) {
            $(dom).leafletLocationPicker({
                attachTo: '#' + contentId,
                onlyLocation: false,
                binding: {
                    latitude: '#settings_region_latitude',
                    longitude: '#settings_region_longitude',
                    radius: '#settings_region_radius'
                },
                location: [
                    data.latitude || data.latitude == 0 ? data.latitude : 30.657517,
                    data.longitude || data.longitude == 0 ? data.longitude : 104.065804
                ],
                radius: data.radius || 2000000,
                map: {
                    editable: true
                }
            });
        },

        $get: function (selector) {
            var ele = $('#settings_region');
            return selector ? $(selector, ele) : ele;
        }
    };
    return {
        Render: function (params) {
            $('#location-picker').remove();
            region.initSearchBar();
            region.bindEvents();
        }
    };
});