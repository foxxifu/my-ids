/**
 * 树的扩展 - 搜索框点击之后出现的树
 * @author PY02039
 */
define(['jquery', 'plugins/inputTree/idsZtree'], function ($) {
    if ($.fn.idsInputTree) {
        return $;
    }

    var scrollTop = -1; // 记录当前滚动条的高度
    $('#main_view').mousemove(function () {
        if (!$('._ztreeInputDiv').get(0) || $('._ztreeInputDiv').is(':hidden')) {
            scrollTop = $(this).scrollTop();
        }
    });
    $('#main_view').scroll(function () {
        if (scrollTop != -1 && $('._ztreeInputDiv').is(':visible')) {
            $(this).scrollTop(scrollTop);
        }
    });
    $(window).resize(function () {
        $('._ztreeInputDiv').hide();
    });

    $.fn.idsInputTree = function (_p) {
        var $this = $(this);

        var $cfg = $.extend({
            url: '',
            param: {},
            parent: false,
            $id: false, //传递反填id的jquery实例对象，不传递默认绑定到this元素的treeSelId属性上
            checkStyle: "checkbox", //zTree树格式："checkbox"、"radio"
            nodeEvents: false, // 树节点操作事件回调定义，可定义 onCheck（节点选中）和 onClick（节点点击）事件
            dataLevel: false, //数据层次， 默认是最底层， 但是可能层次不同的也会被统计
            treeSearch: true, //树的搜索
            treeNodeCreate: false, //树节点构建时调用， 默认为替换为电站各种图标
            treeNodeFormat: false, //树节点格式化
            ajaxBefore: false,
            clickPIsChecked: false, // 父节点是否可选
            textFiled: "name", //自定义展示字段
            selectNodes: [], //默认选中的值 id
            isPromptlyInit: false, //是否立即初始化
            click2hidden: true, //点击是否隐藏
            isBind: false, //是否绑定change事件
            rootCheckable: false, //root节点是否可选择,
            success: null //成功后的回调函数
        }, _p);

        var r = {
            //数据
            _data: {
                _treeId: $this.attr('id') + "_inputTree",
                _searchDiv: $this.attr('id') + "_searchDiv",
                _inputId: $this.attr('id')
            },

            init: function () {
                if ($cfg.isPromptlyInit) {
                    r.createView();
                }

                $this.unbind("click").bind('click', function (event) {
                    r.createView(event);
                });

                //绑定输入框内容变化事件
                if ($cfg.isBind) {
                    $this.unbind("input propertychange").bind("input propertychange", function (event) {
                        r.createView(event);
                    });
                }

                return $this;
            },

            /**
             * 创建视图结构
             */
            createView: function () {
                var _selfData = r._data;
                r._dialogs = $('.modal[role=dialog]').length || 0;

                if ($('#' + _selfData._searchDiv).length > 0 && $('#' + _selfData._searchDiv).is(':visible')) {
                    return;
                }

                $this.removeAttr('readonly').removeAttr('title').focusin().val('');

                //创建必要div
                $("#" + _selfData._searchDiv).remove();

                //添加到input后
                var seaDiv = $("<div id='" + _selfData._searchDiv + "' class='_ztreeInputDiv' style='overflow-y: auto; display: none;'><div>");
                var treeDiv = $("<ul id='" + _selfData._treeId + "' class='ztree'></ul>");
                seaDiv.append(treeDiv);
                $($cfg.parent || 'body').append(seaDiv);

                var inputPosition = $this.offset();
                seaDiv.css({
                    top: inputPosition.top + $this.height() + 5 + "px",
                    left: inputPosition.left + "px",
                    width: $this.outerWidth() + 5
                });

                //显示树
                r.show(seaDiv);

                //树的加载
                r.initTree(treeDiv);
            },

            /**
             * 树的搜索： 如果没有指定就搜索最后一层
             */
            selectedTreeDataLevel: function (node) {
                if ($cfg.dataLevel && $.isNumeric($cfg.dataLevel)) {
                    return !node.isParent && node.level == $cfg.dataLevel && node.checked;
                } else {
                    return ($cfg.clickPIsChecked || !node.isParent) && node.checked;
                }
            },

            /**
             * 树的初始化
             */
            initTree: function ($tree) {
                if ($cfg.ajaxBefore && $.isFunction($cfg.ajaxBefore)) {
                    $cfg.ajaxBefore($cfg);
                }

                var $zOption = {
                    view: {
                        dblClickExpand: false,
                        showLine: false,
                        showIcon: false,
                        selectedMulti: false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pid",
                            rootPId: ""
                        },
                        key: {
                            name: $cfg.textFiled
                        }
                    },
                    check: {
                        enable: true,
                        chkStyle: $cfg.checkStyle,
                        radioType: "all"
                    },
                    callback: {
                        onClick: function (e, treeId, treeNode, clickFlag) {
                            var _tree = $.fn.zTree.getZTreeObj(treeId);
                            if (!$cfg.clickPIsChecked && treeNode.isParent) {
                                _tree.expandNode(treeNode, !treeNode.open, false, true);
                            } else {
                                var checked = treeNode.checked;
                                if ('radio' == _tree.setting.check.chkStyle) {
                                    _tree.expandNode(treeNode, !treeNode.open, false, true);
                                } else {
                                    _tree.checkNode(treeNode, !checked, true, true);
                                }
                            }

                            if ($cfg.nodeEvents && $cfg.nodeEvents.onClick && $.isFunction($cfg.nodeEvents.onClick)) {
                                $cfg.nodeEvents.onClick(e, treeId, treeNode, clickFlag);
                            }

                            return false;
                        },
                        onCheck: function (e, treeId, treeNode) {
                            var _tree = $.fn.zTree.getZTreeObj(treeId);

                            if ($cfg.nodeEvents && $cfg.nodeEvents.onCheck && $.isFunction($cfg.nodeEvents.onCheck)) {
                                if ($cfg.nodeEvents.onCheck(e, treeId, treeNode)) {
                                    return;
                                }
                            }

                            //if (!$cfg.clickPIsChecked && treeNode.isParent) {
                            //    _tree.checkNode(treeNode, false, true);
                            //}

                            if ('radio' == _tree.setting.check.chkStyle) {
                                if ($cfg.click2hidden) {
                                    r.onDestroy();
                                }
                            }
                        }
                    }
                };

                //构建树
                $tree.idsZtree($zOption, {
                    url: $cfg.url,
                    param: $cfg.param,
                    treeSearch: $cfg.treeSearch,
                    treeSearchInput: $this,
                    searchDataLevel: $cfg.dataLevel,
                    treeNodeCreate: $cfg.treeNodeCreate,
                    treeNodeFormat: $cfg.treeNodeFormat,
                    treeLoadAfter: function (tree) {
                        if ($cfg.rootCheckable) {
                            var rootNode = tree.getNodesByFilter(function (node) {
                                return node.level == 0
                            }, true);
                            if (rootNode && rootNode.id == 1) {
                                tree.setChkDisabled(rootNode, true);
                            }
                        }
                        var checkNodeId = $this.attr('treeSelId');//获取之前选中节点
                        if (checkNodeId) {
                            checkNodeId = checkNodeId.split(',');
                            $cfg.selectNodes = null;
                            var tCheckNodes = tree.getNodesByFilter(function (node) {
                                return -1 != $.inArray(node.id.toString(), checkNodeId);
                            });
                            $.each(tCheckNodes, function (i, tnode) {
                                tree.expandNode(tnode.getParentNode(), true, false);   //展开区域
                                if (tnode.getParentNode()) {
                                    tree.expandNode(tnode.getParentNode().getParentNode(), true, false);   //展开顶层节点
                                }
                                tree.checkNode(tnode, true, true, false);  //第一个参数是：要勾选的节点，第二个参数是否勾选，  第三个参数是否勾选关联父节点
                            });
                            $cfg.success && $cfg.success(null,tree);
                        } else if ($cfg.selectNodes && $cfg.selectNodes.length > 0) {
                            var tCheckNodes = tree.getNodesByFilter(function (node) {
                                return -1 != $.inArray(node.id.toString(), $cfg.selectNodes);
                            });
                            var datas = [];
                            if (tCheckNodes && tCheckNodes.length > 0) {
                                var seletedIds = [];
                                var seletedNames = [];
                                $.each(tCheckNodes, function (i, tnode) {
                                    datas.push(tnode);
                                    seletedIds.push(tnode.id);
                                    seletedNames.push(tnode[$cfg.textFiled]);
                                    tree.expandNode(tnode.getParentNode(), true, false);   //展开区域
                                    if (tnode.getParentNode()) {
                                        tree.expandNode(tnode.getParentNode().getParentNode(), true, false);   //展开顶层节点
                                    }
                                    tree.checkNode(tnode, true, true, false);  //第一个参数是：要勾选的节点，第二个参数是否勾选，  第三个参数是否勾选关联父节点
                                });
                                $this.attr('treeSelId', seletedIds.join(',')).attr('title', seletedNames.join(','));
                                $cfg.$id && $cfg.$id.val && $cfg.$id.val(seletedIds.join(','));
                                $this.val(seletedNames.join(',')).focusout();
                            }
                            $cfg.success && $cfg.success(datas,tree);
                        }
                    },
                    treeDataLoadFiled: function () {
                        App.alert({
                            title: Msg.info,
                            message: Msg.loadDataFaild
                        });
                        $tree.parent(r._data._searchDiv).html("<span color='red'>" + Msg.loadDataFaild + "</span>");
                    }
                });
            },

            /**
             * 显示
             */
            show: function (dom) {
                $(dom).css({
                    'z-index': +(App.dialogZIndex || 940)
                }).show();
                $("body").bind('mousedown', function (event) {
                    if (!(event.target.id == r._data._searchDiv
                        || event.target.id == r._data._treeId
                        || event.target.id == r._data._inputId
                        || $(event.target).parents("._ztreeInputDiv").length > 0
                        || $('.modal-overlay[role=dialog]').length > (r._dialogs || 0))) {
                        r.onDestroy();
                    }
                });
            },

            /**
             * 树消失，并且输入框变成只读，回写数据
             */
            onDestroy: function () {
                var _selfData = r._data;
                var _tree = $.fn.zTree.getZTreeObj(_selfData._treeId);

                var seletedIds = [];
                var seletedNames = [];
                var poorsupport = [];
                var seletedUnits = [];    //勾选节点单位

                //输入框失去焦点， 并且变为只读
                $this.focusout().attr('readonly', 'readonly');

                //取得树勾选的id, 并将id填入影藏域， name填入输入框
                if (_tree) {
                    var selectedNodes = _tree.getNodesByFilter(r.selectedTreeDataLevel);
                    $.each(selectedNodes, function (i, node) {
                        seletedIds.push(node.id);
                        seletedNames.push(node[$cfg.textFiled]);
                        seletedUnits.push(node.unit);
                        if (node.supportPoor) {
                            poorsupport.push(node.supportPoor);
                        }
                    });

                    $this.attr('treeSelId', seletedIds.join(',')).attr('title', seletedNames.join(','));
                    $this.attr('unit', seletedUnits.join(',')); //勾选节点单位
                    if (poorsupport.length > 0) {
                        $this.attr('poorsupport', poorsupport.join(','));
                    } else {
                        $this.attr('poorsupport', "");
                    }
                    $cfg.$id && $cfg.$id.val && $cfg.$id.val(seletedIds.join(','));
                    $this.val(seletedNames.join(',')).focusout();
                    $this.attr('value', seletedIds.join(','));

                    //销毁树
                    _tree.destroy();
                }

                //清空树，隐藏div
                var seaDiv = $('#' + _selfData._searchDiv);
                if (seaDiv.is(':visible')) {
                    seaDiv.fadeIn().remove();
                    $("body").unbind('mousedown');
                }
                $this.trigger('change');
            }
        };

        return r.init();
    };
});

