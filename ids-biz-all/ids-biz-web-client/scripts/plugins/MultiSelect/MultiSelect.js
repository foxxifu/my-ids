define([
    'jquery',
    'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'
], function ($) {
    $.addMultiSelect = function (t, options) {
        var p = $.extend({
            popupID: '',
            zTreeID: '',
            width: 150,
            height: 150,
            selector: '',
            parent: $(t).parent(),
            refresh: false,
            param: {},
            wholeStation: 'ALL',
            setting: {
                check: {
                    enable: true,
                    chkboxType: {
                        "Y": "ps",
                        "N": "ps"
                    }
                },
                view: {
                    dblClickExpand: false,
                    showLine: false,
                    showIcon: false,
                    txtSelectedEnable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    beforeClick: beforeClick,
                    onCheck: onCheck
                }
            },
            wholeNode: {
                id: 'ALL',
                pId: 0,
                name: Msg.all
            },
            zNodes: [{
                id: 1,
                pId: 0,
                name: "地面式"
            }, {
                id: 2,
                pId: 0,
                name: "分布式"
            }]
        }, options);

        var r = {
            work: function () {
                r.init();
                if ($('body').find('#' + p.popupID).length > 0) {
                    return;
                }
                r.searchData();
                r.createPopup();
                p.zNodes.unshift(p.wholeNode);
                $.fn.zTree.init($("#" + p.zTreeID), p.setting, p.zNodes);
                $("#" + p.zTreeID).find(".switch").remove();
                r.initZTree();
            },
            init: function () {
                t.p = p;
                p.popupID = $(t).attr('id') + "Popup";
                p.zTreeID = $(t).attr('id') + "ZTree";
                p.selector = $(t).attr('id');
                p.width = parseInt($("#" + p.selector).css('width')) + 2;
            },
            searchData: function () {
            },
            createPopup: function () {
                var div = $("<div/>").attr('id', p.popupID).css({
                    'position': 'absolute',
                    'z-index': 1,
                    'background': '#ffffff'
                });
                var ul = $("<div/>").attr('id', p.zTreeID).addClass('ztree MultiSelect').css({
                    'overflow': 'auto',
                    'margin-top': '0',
                    'width': p.width,
                    'max-height': p.height,
                    'border': '1px solid #999'
                });
                $(p.parent).append(div);
                div.append(ul);
                r.showPopupMenu();
            },
            initZTree: function () {
                var sids = $("#" + p.selector).attr('value');
                if (sids) {
                    var treeObj = $.fn.zTree.getZTreeObj(p.zTreeID);
                    var nodes = treeObj.getNodes();
                    if (sids == 'ALL') {
                        for (var i = 1, l = nodes.length; i < l; i++) {
                            treeObj.checkNode(nodes[i], true, true, true);
                        }
                    } else {
                        var aSids = sids.split(',');
                        for (var i = 0, l = aSids.length; i < l; i++) {
                            var node = treeObj.getNodeByParam("id", aSids[i], null);
                            if (node)
                                treeObj.checkNode(node, true, true, true);
                        }
                    }
                }
            },
            beforeClick: function (treeId, treeNode) {
                var zTree = $.fn.zTree.getZTreeObj(p.zTreeID);

                zTree.checkNode(treeNode, !treeNode.checked, null, true);
                return false;
            },

            onCheck: function (e, treeId, treeNode) {
                var treeObj = $.fn.zTree.getZTreeObj(p.zTreeID);
                var nodes = treeObj.getNodes();
                var wholeLength = nodes.length;
                var textObj = $("#" + p.selector);
                if (treeNode.id == p.wholeStation) {
                    if (treeNode.checked) {
                        for (var i = 1, l = nodes.length; i < l; i++) {
                            treeObj.checkNode(nodes[i], true, true, false);
                        }
                        textObj.attr('value', 'ALL');
                    } else {
                        for (var i = 1, l = nodes.length; i < l; i++) {
                            treeObj.checkNode(nodes[i], false, true, false);
                        }
                        textObj.attr('value', '');
                    }
                    textObj.val('');

                } else {
                    if (!treeNode.checked) {
                        treeObj.checkNode(nodes[0], false, false, false);
                    }
                    nodes = treeObj.getCheckedNodes(true), v = "", sids = "";

                    for (var i = 0, l = nodes.length; i < l; i++) {
                        v += nodes[i].name + ",";
                        sids += nodes[i].id + ",";
                    }
                    if (nodes.length && wholeLength - 1 == nodes.length && sids.indexOf(p.wholeStation) < 0) {
                        treeObj.checkNode(treeObj.getNodes()[0], true, true, true);
                        return;
                    }
                    if (v.length > 0)
                        v = v.substring(0, v.length - 1);
                    if (sids.length > 0)
                        sids = sids.substring(0, sids.length - 1);
                    var textObj = $("#" + p.selector);
                    if (sids.indexOf(p.wholeStation) > -1) {
                        textObj.val(Msg.all);
                        // sids=sids.substring(p.wholeStation.length+1,sids.length);
                        sids = "";
                    } else {
                        textObj.val(v);
                    }
                    textObj.attr('value', sids);
                }
                if (p.refresh) {
                    p.refresh();
                }
            },

            showPopupMenu: function () {
                var textOffset = null;

                if (p.parent == 'body') {
                    textOffset = $("#" + p.selector).offset();
                } else {
                    textOffset = $("#" + p.selector).position();
                }
                var textObj = $("#" + p.selector);
                $("#" + p.popupID).css({
                    left: textOffset.left + "px",
                    top: textOffset.top + textObj.outerHeight() + "px"
                }).slideDown("fast");
                $("body").bind("mousedown", r.onDestroy);
            },
            hidePopupMenu: function () {
                $("#" + p.popupID).fadeOut("fast");
                $("#" + p.popupID).remove();
                $("body").unbind("mousedown", r.onDestroy);
            },
            onDestroy: function (event) {
                if (!(event.target.id == p.selector || event.target.id == p.zTreeID || event.target.id == p.popupID || $(event.target).parents(
                        '#' + p.zTreeID).length > 0)) {
                    r.hidePopupMenu();
                }
            }
        };

        function beforeClick(treeId, treeNode) {
            r.beforeClick(treeId, treeNode);
        }

        function onCheck(e, treeId, treeNode) {
            r.onCheck(e, treeId, treeNode);
        }

        $(document).ready(function () {
            r.work();
        });
        return true;
    };

    var docLoaded = false;
    $(document).ready(function () {
        docLoaded = true;
    });

    $.fn.MultiSelect = function (p) {
        return this.each(function () {
            if (!docLoaded) {
                $(this).hide();
                var t = this;
                $(document).ready(function () {
                    $.addMultiSelect(t, p);
                });
            } else {
                $.addMultiSelect(this, p);
            }
        });
    };

    return $;
});
