/**
 * 树的扩展
 * @author PY02039
 */
define(['jquery', 'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function ($) {

    if ($.fn.idsZtree) {
        return $;
    }

    $.fn.idsZtree = function (zOption, _p) {
        var $this = $(this);

        //参数配置
        var $cfg = $.extend({
            url: '',
            param: {},
            treeSearch: true, //树的搜索
            treeSearchInput: false, //可配置的树的搜索框
            searchDataLevel: false, //可搜索的的层数
            treeSearchClass: '_idsTreeSearch', //搜索tree的class
            treeNodeCreate: false, //渲染每个树节点时的回调
            treeDataLoadFiled: false, //ztree 数据加载失败调用
            //treeLoadBefore : false, //ztree 加载前执行的回调, 暂无扩展
            treeNodeFormat: false, //调整node格式回调函数
            treeLoadAfter: false //ztree 加载之后执行的回调
        }, _p);

        //树的参数
        var $zOption = $.extend({
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
                    name: 'name'
                }
            },
            check: {
                enable: true,
                chkStyle: 'checkbox',
                radioType: "all"
            }
        }, zOption);

        //内部操作对象
        var r = {

            //树的初始化
            createView: function () {
                $this.empty();
                $this.append("<div class='_idsZtreeLoading'>" + Msg.loading + "</div>"); //数据加载中

                var _param = $.extend(true, {}, $cfg.param); //深拷贝参数对象，避免原对象被污染

                $.http.post($cfg.url, _param, function (data) {
                    var ro = data;
                    if (ro.success) {
                        var zNodes = ro.data;
                        if (zNodes && zNodes.length > 0) {
                            if ($.isFunction($cfg.treeNodeFormat)) {
                                zNodes = $cfg.treeNodeFormat(zNodes);
                            }
                            if ($.isFunction($cfg.treeNodeCreate)) {
                                for (var i = 0; i < zNodes.length; i++) {
                                    $cfg.treeNodeCreate(zNodes[i]);
                                }
                            }
                            //初始化ztree
                            zTreeObj = $.fn.zTree.init($this, $zOption, zNodes);
                        }
                        $('._idsZtreeLoading', $this).remove();

                        //加入搜索
                        if ($cfg.treeSearch) {
                            r.addSearchText(zTreeObj);
                        }

                        //如果配置了后处理函数
                        if (zTreeObj && $.isFunction($cfg.treeLoadAfter)) {
                            $cfg.treeLoadAfter(zTreeObj);
                        }

                    } else {
                        if ($.isFunction($cfg.treeDataLoadFiled)) {
                            $cfg.treeDataLoadFiled();
                        } else {
                            $('._idsZtreeLoading', $this).remove();
                            $this.html("<div style='color:red;'>" + Msg.loadDataFaild + "</div>"); //数据加载失败
                        }
                    }
                });
            },

            //树的搜索 - 回调函数： 筛选最后一层的ztree节点
            filterLastSecond: function (node) {
                if ($cfg.searchDataLevel && $.isNumeric($cfg.searchDataLevel)) {
                    return node.level == $cfg.searchDataLevel;
                } else {
                    return !node.isParent;
                }
            },

            /**
             */
            SearchResult: function (node) {
                if ($cfg.searchDataLevel && $.isNumeric($cfg.searchDataLevel)) {
                    return node.level == $cfg.searchDataLevel && node.checked;
                } else {
                    return !node.isParent && node.checked;
                }
            },

            addSearchText: function (zTreeObj) {
                //显示搜索
                var searchInput;
                if ($cfg.treeSearchInput) {
                    searchInput = $($cfg.treeSearchInput);
                } else {
                    $('.' + $cfg.treeSearchClass, $this.parent()).remove();//先干掉已经存在的
                    searchInput = $('<input type="text" class="' + $cfg.treeSearchClass + '" />');
                    searchInput.attr('placeholder', Msg.inputTheKey); //请输入关键字
                    searchInput.placeholderSupport();
                    $this.before(searchInput);
                }

                //添加回调
                searchInput.unbind('input propertychange').bind('input propertychange', function () {
                    //searchInput.unbind('input keydown').bind('input keydown', function () {
                    var self = $(this);

                    var updateNodes = function (nodeList) {
                        var dfd = $.Deferred();

                        var process = function (nodes) {
                            var l = nodes.length;
                            for (var i = 0; i < l; i++) {
                                var node = nodes[i];
                                var f = false;
                                for (var j = 0, t = nodeList.length; j < t; j++) {
                                    if (node.isParent || node.tId == nodeList[j].tId) {
                                        f = true;
                                        break;
                                    }
                                }
                                if (f) {
                                    zTreeObj.expandNode(node.getParentNode(), true, false);
                                    zTreeObj.showNode(node);
                                } else {
                                    zTreeObj.hideNode(node);
                                }
                            }
                            zTreeObj.refresh();
                        };

                        var nodes = zTreeObj.getNodesByFilter(r.filterLastSecond);
                        var l = nodes.length;
                        if (l > 2000) {
                            setTimeout(function () {
                                process(nodes);
                                dfd.resolve();
                            }, 2000);
                        }
                        else {
                            process(nodes);
                            dfd.resolve();
                        }

                        return dfd.promise();
                    };

                    var nodeList = [];
                    var value = self.val().trim();
                    if (value != "" && value != " ") {
                        nodeList = zTreeObj.getNodesByParamFuzzy($zOption.data.key.name || "name", value, false);
                    } else {
                        nodeList = zTreeObj.getNodesByFilter(function (node) {
                            return !node.isParent;
                        });
                    }

                    $.when(updateNodes(nodeList || []))
                        .always(function () {
                            //重新勾选勾中节点的父节点
                            var SearchResultNode = zTreeObj.getNodesByFilter(r.SearchResult);
                            SearchResultNode &&
                            $.each(SearchResultNode, function (i, node) {
                                zTreeObj.checkNode(node, true, true);  //第一个参数是：要勾选的节点，第二个参数是否勾选，  第三个参数是否勾选关联父节点
                            });
                        });
                });
            }
        };

        r.createView();
    };

    return $;
});
