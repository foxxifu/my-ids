/**
 * 功能：生成选择电站对话框，显示内容和样式可通过属性配置 日期：2017.01.19
 */
define(['jquery', 'ValidateForm', 'ExpandTable', 'MultiSelect'], function ($) {
    (function ($) {
        $.fn.stationDialog = function (options) {
            var $this = this;
            var p = $.extend({
                pageSize: 20,
                id: "initialStationDialog",
                width: 1000,					                    // 选择框宽度
                height: 560,				                    // 选择框高度
                url: '/station/page',	// 选择框电站列表后台请求地址
                param: {},					                    // 想后台请求的参数
                selector: true,				                    // 选择输入控件id
                toolbarID: '',				                    // 选择框中筛选控件的id
                tableID: '',					                // 选择框中电站表格的id
                submit: false,				                    // 点击确定后的回调函数
                refresh: false,				                    // 点击筛选控件是否执行刷新表格操作
                dialog: null,					                // 选择框结点
                status: [0],					                // 电站状态0：并网,1：在建，2：未建，3：并购中
                combineType: [1, 2],			                // 并网类型筛选条件，1：地面式电站，2：分布式电站
                inverterType: [1, 2, 3, 4],		                // 逆变器类型筛选条件，1：组串式电站，2：集中式电站，3：混合式电站
                singleSelect: false,			                // 是否只选择一个电站  false:多选 true:单选
                onDoubleClick: false,			                // 电站信息列表中的双击回调函数
                loaded: false,				                    // 电站信息列表加载完成的变量标示
                noButtons: false,				                // 不显示查询按钮
                // queryReset: true, // 查询重置选择项
                title: Msg.selectStation,		                // 选择对话框的标题
                groupId: [],                                  // 集团Id
                userId: [],                                   // 用户Id
                isAll: false,                                 // 是否获取全部电站false:
                // 集团下全部电站；true：集团下用户自己所属的电站
                columns:						               // 对话框里电站列表的行
                    ['icon', 'stationName', 'installCapacity', 'combineType', 'inverterType'],
                input: $this
            }, options);

            p.param.combineType = p.combineType.toString();
            p.param.inverterType = p.inverterType.toString();
            p.param.buildState = p.status.toString();

            App.dialog({
                id: p.id,
                title: p.title,
                width: p.width,
                height: p.height
            }).loadPage({url: "js/plugins/stationDialog/selectDialogAddGroup.html"}, {}, function () {
                var param = p;
                var $this = p.input[0];
                var sIds = ($this && $this.defaultValue && $this.defaultValue.split(",")) || [];
                var selectHhStations = $("#selectHhStations").expandTable({
                    maxHeight: 350,
//        			param: param.param,
                    url: param.url,
                    showTitle: true,
                    ellipsis: true,
                    pageSize: param.pageSize,
                    autoHeight: false,
                    noSelectPageSize: true,
                    autoText: true,
                    contentHei: 30,
                    // sortColumn: "stationName",
                    // sortType: "asc",
                    // sortIndexs:[0,1,2,3], //排序列index集合
                    // sortNames:["stationName","combineType","inverterType","installCapacity"],
                    // sortTypes:["asc","asc","asc","asc"],
                    thWidsIndex: [0, 4],
                    thWids: [170, 60],
                    noClickColor: true,
                    // columnName:
                    // ["sid","stationName","combineType","inverterType","installCapacity","no"],
                    columnName: ["stationCode", "stationName", "combineType", "inverterType", "capacity", "no"],
                    headers: [Msg.sm.psm.sname, Msg.sm.psm.combineType, Msg.sm.psm.inverterType, Msg.sm.psm.installCapacity, Msg.main.hhsrc.select],
                    hiddenIndexs: [0],
                    theme: {
                        extend: "a",
                        eDivL: "#C8D0DE",
                        eDivR: "#C8D0DE",
                        eHeaderBg: "#D7E9EF"
                    },
                    renderCell: function (row, col, value, record, nativeRecord) {
                        if (!record) return;
                        if (col == 4) {
                            var sId;
                            var dom = $("<a sid='" + record[0] + "'  class='choose' title='" + Msg.main.hhsrc.select + "'></a>");
                            dom.click(function () {
                                sId = $("#selectDialogAddGroup ul.selectedStations li").attr("sid");
                                if (param.singleSelect) {
                                    $("#selectDialogAddGroup ul.selectedStations li").remove();
                                    showStation(sId);
                                    addSelectNum();
                                }
                                addStation(record, nativeRecord, row, param.singleSelect);
                                var i = $("#selectDialogAddGroup ul.selectedStations li").length;
                                sId = $("#selectDialogAddGroup ul.selectedStations li:nth-child(" + i + ")").attr("sid");
                                sIds[i - 1] = sId;
                            });
                            if (sIds) {
                                for (var j = 0; j < sIds.length; j++) {
                                    if (sIds[j] == record[0]) {
                                        dom.css({'visibility': 'hidden'});
                                        return {dom: dom};
                                    }
                                }
                            }
                            dom.css({'visibility': 'visible'});
                            return {dom: dom};
                        } else if (col == 1) {
                            return Msg.sm.psm.station_type[value - 1];
                        } else if (col == 2) {
                            return Msg.sm.psm.inverter_type[value - 1];
                        } else if (col == 3 || col == 0) {
                            return {dom: $("<span/>").html(value)};
                        }
                    }
                });

                $("#selectDialogAddGroup span.selectedNum").attr("title", Msg.main.hhsrc.selectedStation);

                // 创建子公司树形结构
                var createTree = function () {
                    /*	$.http.post("/domain/queryDomainByUserId",{userid:"1"},function(d){
                     if(d.success && d.data && d.data.length>0){
                     initTree(d.data, getPidByUserGroupId(d.data), 0, $("#selectDialogAddGroup .hhTree"));
                     treeAddClick();
                     }
                     })*/
                    var userid = Cookies.get("userid");
                    $.http.post('/domain/queryDomainByUserId', {
                        "userid": userid
                    }, function (data) {
                        if (data && data.success) {
                            var zTreeObj;
                            var zNodes = main.getZnodes(data.data);
                            var zTreeSetting = {
                                treeId: "Locations",
                                callback: {
                                    onClick: zTreeOnClick
                                },
                                view: {
                                    dblClickExpand: false,
                                    showLine: false,
                                    selectedMulti: false
                                },
                                data: {
                                    simpleData: {
                                        enable: true
                                    }
                                }
                            };
                            $.fn.zTree.init($("#station_ztree"), zTreeSetting, zNodes);
                            zTreeObj = $.fn.zTree.getZTreeObj("station_ztree");
                            //点击域结构查询设备
                            function zTreeOnClick(event, treeId, treeNode) {
                                /* p.param.domainid = zTreeObj.getSelectedNodes()[0].id;
                                 s.search();*/
                            }
                        } else {
                            // App.alert(Msg.partials.main.hp.poverty.getFail || "获取数据失败!");
                        }
                    });

                };

                // 获取根节点的pid
                var getPidByUserGroupId = function (list) {
                    /*  	if(App.stationIds.groupType==1){
                     if(list.length==1){
                     return list[0].pid;
                     }
                     }
                     for(var i=0;i<list.length;i++){
                     if(list[i].orgNo == App.stationIds.groupId){
                     return list[i].pid;
                     }
                     }
                     return 0;*/

                    if (list && list.length > 0) {
                        return list[0].pid;
                    }
                }

                // 初始化树
                var initTree = function (list, pid, level, node) {
                    var ul = $("<div class='items'></div>");
                    var isHas = false;
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (item.pid == pid) {
                            isHas = true;
                            var itemNode = getItemNode(item, level, isLeaf(list, item.id));
                            ul.append(itemNode);
                            initTree(list, item.id, level + 1, itemNode);
                        }
                    }
                    //level!=0 && ul.hide();
                    isHas && node.append(ul);
                }

                // 检查是否叶子节点
                var isLeaf = function (list, id) {
                    var result = true;
                    for (var i = 0; i < list.length; i++) {
                        if (list[i].pid == id) {
                            result = false;
                            break;
                        }

                    }
                    return result;
                }

                // 将数据拼装成html
                var getItemNode = function (item, level, isLeaf) {
                    var paddingLeft = level == 0 ? 0 : 13;
                    var group = level == 0 ? "root" : "leaf";
                    var hide = "";
                    if (isLeaf && level > 0) {
                        hide = "display:none";
                        paddingLeft += 18;
                    }
                    paddingLeft = paddingLeft + "px";
                    var html = "<li class='item' style='padding-left:" + paddingLeft + "'>"
                        + "<div class='detail'>"
                        + "<span class='icon close' style='" + hide + "'></span>"
                        + "<p class='line' style='margin-left:0px'>"
                        + "<span class='son " + group + "'></span>"
                        + "<span class='name' orgNo='" + item.id + "' level='" + level + "' title='" + item.domainName + "' >" + item.domainName + "</span>"
                        + "</p>"
                        + "</div>"
                        + "</li>";
                    var node = $(html);
                    return node;
                };

                // 添加点击事件
                var treeAddClick = function () {
                    $("#selectDialogAddGroup .hhTree").delegate("span.icon", "click", function () {
                        var $this = $(this);
                        if ($this.hasClass("close")) {
                            $this.addClass("open");
                            $this.removeClass("close");
                        } else {
                            $this.addClass("close");
                            $this.removeClass("open");
                        }
                        $(this).parent().next().toggle("fast");
                    }).delegate("p.line", "click", function () {
                        $("#selectDialogAddGroup ul.hhTree").find("p.line").removeClass("selected");
                        $(this).addClass("selected");
                        var orgNo = $(this).find(".name").attr("orgNo");
                        var level = $(this).find(".name").attr("level");
                        var param = {};
                        if (level == 0) {
                            param.orgNo = "";
                        } else {
                            param.orgNo = orgNo;
                        }
                        selectHhStations.search({param: param});
                    })
                }

                var initSelect = function (input) {
                    var names = input.val();
                    var sids = input.attr("value");
                    if (names && names.length > 0) {
                        names = names.split(",");
                    }
                    if (sids && sids.length > 0) {
                        sids = sids.split(",");
                    }
                    if ($.isArray(names) && $.isArray(sids) && names.length == sids.length) {
                        for (var i = 0; i < sids.length; i++) {
                            var item = [];
                            item.push(sids[i]);
                            item.push(names[i]);
                            addStation(item);
                        }
                    }
                }

                var addStation = function (item, nativeRecord, row) {
                    if ($("#selectDialogAddGroup li[sId='" + item[0] + "']").length) {
                        return;
                    }
                    var li = "<li sId='" + item[0] + "' class='stationItem' >"
                        + "	<span class='station' title='" + item[1] + "' >" + item[1] + "</span>"
                            // +" <a class='del'
                            // title='"+Msg.main.hhsrc.delete+"'>"+Msg.main.hhsrc.delete+"</a>"
                        + "	<table style='height:100%'><tr><td><a class='del' title='" + Msg.main.hhsrc.delete + "'></a></td></tr></table>"

                        + "</li>";
                    li = $(li).data("nativeRecord", nativeRecord);
                    $("#selectDialogAddGroup .selectedStations").append(li);
                    if (isScroll()) {
                        li.prev().css("border-bottom", "")
                            .end().css("border-bottom", "none");
                    }
                    addSelectNum();
                    if (param.singleSelect) {
                        selectHhStations.find(".choose").css("visibility", "visible");
                    }
                    hiddenStation(row);
                }
                // 隐藏已经选择后的电站
                var hiddenStation = function (row) {
                    $("#selectHhStations .contentTable tbody tr:eq(" + row + ") td:last div").children().css("visibility", "hidden");
                }

                var delStation = function () {
                    $("#selectDialogAddGroup").delegate("a.del", "click", function () {
                        var sid = $(this).parent().parent().parent().parent().parent().attr("sid");
                        $(this).parent().parent().parent().parent().parent().remove();
                        var li = $("#selectDialogAddGroup .selectedStations").find("li.stationItem:last");
                        if (isScroll()) {
                            li.css("border-bottom", "none");
                        } else {
                            li.css("border-bottom", "");
                        }
                        addSelectNum();
                        showStation(sid);
                    })
                };
                // 显示不再选择的电站
                var showStation = function (sid) {
                    $("#selectHhStations .contentTable tbody tr").each(function () {
                        var sId = $(this).find("td:last").children().children().attr("sid");
                        if (sId == sid) {
                            $(this).find("td:last").children().children().css("visibility", "visible");
                        }
                    });
                    sIds.remove(sid);
                };

                var getSids = function () {
                    var sids = [];
                    var nativeRecords = [];
                    $("#selectDialogAddGroup .selectedStations .stationItem").each(function (i, e) {
                        var sid = $(e).attr("sId") || "";
                        sids.push(sid);
                        nativeRecords.push($(e).data("nativeRecord"));
                    });
                    return {
                        sids: sids.toString(),
                        selectedRecords: nativeRecords
                    };
                };

                var getNames = function () {
                    var names = [];
                    $("#selectDialogAddGroup .selectedStations .stationItem").each(function (i, e) {
                        var name = $.trim($(e).find(".station").text()) || "";
                        names.push(name);
                    });
                    return names.toString();
                };

                var addClick = function () {
                    $("#selectDialogAddGroup").find("#selectHhCancel").click(function () {
                        $("#" + p.id).modal("hide");
                    }).end().find("#selectHhOk").click(function () {
                        var selectedValues = getSids();
                        param.input.val(getNames()).attr("value", selectedValues.sids);
                        if ($.isFunction(param.submit)) {
                            if (param.submit(selectedValues.selectedRecords) != "notClose") {
                                $("#" + p.id).modal("hide");
                            }
                        } else {
                            $("#" + p.id).modal("hide");
                        }

                    });
                };

                // 全选事件
                var selectAll = function () {
                    if (param.singleSelect) {
                        $("#selectDialogAddGroup").find(".selectAll").remove();
                        return;
                    }
                    $("#selectDialogAddGroup").find(".selectAll").attr("title", Msg.household.selectAll).click(function () {
                        selectHhStations.find(".choose").click();
                    });
                };

                var delAll = function () {
                    $("#selectDialogAddGroup").find(".delAll").attr("title", Msg.household.deleteAll).click(function () {
                        $("#selectDialogAddGroup ul.selectedStations li").remove();
                        addSelectNum();
                        selectHhStations.find(".choose").parent().show();
                        selectHhStations.find(".choose").css("visibility", "visible");
                        sIds = [];
                    });
                };

                // 计算选中的电站数量
                var addSelectNum = function () {
                    var size = $("#selectDialogAddGroup").find("a.del").size();
                    $("#selectDialogAddGroup span.selectedNum").html(size);
                };

                // 检查已选中的电站区是否出现竖向滚动条
                var isScroll = function () {
                    var scrollFlag = false;
                    if ($("#selectDialogAddGroup .selectedStations").find("li:first").length) {
                        var top = $("#selectDialogAddGroup .selectedStations").find("li:first").position().top;
                        var scrollTop = $("#selectDialogAddGroup .selectedStations").scrollTop();

                        $("#selectDialogAddGroup .selectedStations").scrollTop(scrollTop + 1);
                        var topAdd = $("#selectDialogAddGroup .selectedStations").find("li:first").position().top;

                        $("#selectDialogAddGroup .selectedStations").scrollTop(scrollTop - 1);
                        var topReduce = $("#selectDialogAddGroup .selectedStations").find("li:first").position().top;

                        if (top != topAdd || top != topReduce) {
                            scrollFlag = true;
                        }
                        $("#selectDialogAddGroup .selectedStations").scrollTop(scrollTop);
                    }
                    return scrollFlag;
                };

                // 电站类型选择框的添加
                var addStationTypeEvent = function () {
                    var zNodes = [];
                    for (var i = 0; i < param.combineType.length; i++) {
                        zNodes.push({
                            id: param.combineType[i],
                            pId: 0,
                            name: Msg.sm.psm.station_type[param.combineType[i] - 1]
                        });
                    }
                    // 使用多选下拉框插件
                    $("#selectDialogAddGroup_combineType").click(function () {
                        $(this).MultiSelect({
                            zNodes: zNodes.concat()
                        });
                    });
                };

                // 添加电站的逆变器类型方法
                var addInverterTypeEvent = function () {
                    var zNodes = [];
                    for (var i = 0; i < param.inverterType.length; i++) {
                        zNodes.push({
                            id: param.inverterType[i],
                            pId: 0,
                            name: Msg.sm.psm.inverter_type[param.inverterType[i] - 1],
                        });
                    }

                    $("#selectDialogAddGroup_inverterType").click(function () {
                        $(this).MultiSelect({
                            zNodes: zNodes.concat()
                        });
                    });
                };

                // 添加查询事件
                var addSearchEvent = function () {
                    $("#selectDialogAddGroup_sbtn").click(function () {
                        var p = getSearchParam();
                        selectHhStations.search({param: p});
                    });
                    $("#selectDialogAddGroup_sname").keyup(function () {
                        if (event.keyCode == 13) {
                            $("#selectDialogAddGroup_sbtn").click();
                        }
                    });
                };

                // 获取查询参数
                var getSearchParam = function () {
                    var combineType = $("#selectDialogAddGroup_combineType").attr("value") || "";
                    var inverterType = $("#selectDialogAddGroup_inverterType").attr("value") || "";
                    var sname = $("#selectDialogAddGroup_sname").val() || "";
                    if (combineType == "ALL" || combineType == "") {
                        combineType = param.combineType.toString();
                    }
                    if (inverterType == "ALL" || inverterType == "") {
                        inverterType = param.inverterType.toString();
                    }
                    return {
                        combineType: combineType,
                        inverterType: inverterType,
                        stationName: sname
                    }
                };

                selectAll();
                delAll();
                createTree();
                delStation();
                addClick();
                addStationTypeEvent();
                addInverterTypeEvent();
                addSearchEvent();
                initSelect(param.input);
            });
        }
    })(jQuery);
});