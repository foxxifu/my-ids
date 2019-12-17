/**
 * 功能：用户选择框，显示内容和样式可通过属性配置
 * 日期：2015.4.9
 */
define(['jquery', 'ValidateForm', 'GridTable','css!plugins/userDialog/css/UserDialog.css','zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function ($) {

    $.addSelectUser = function (t, options) {
        var isGroupId = false;
        var userId = Cookies.get("userid");
        var p = $.extend({
        	id:"initUserDialogId",
            width: 1000,					                    // 选择框宽度
            height: 'auto',				                    // 选择框高度
            url: '/user/queryUsers',	                        // 选择框电站列表后台请求地址
            param: {"userid": userId},	 // 向后台请求的参数
            domainUrl: '/domain/queryDomainByUserId',       // 区域树后台请求地址
            submit: false,				                    // 点击确定后的回调函数
            refresh: false,				                    // 点击筛选控件是否执行刷新表格操作
            dialog: null,					                // 选择框结点
            singleSelect: false,			                // 信息列表中的单击回调函数
            initUserId: null,			                // 初始化userId
            onDoubleClick: false,
            selector: true,
            // 电站信息列表中的双击回调函数
            loaded: false,				                    // 信息列表加载完成的变量标示
            noButtons: false,				                // 不显示查询按钮
            // queryReset: true,                               // 查询重置选择项
            title: Msg.userDialog.choose,		                         // 选择对话框的标题
            columns:						                // 对话框里列表的行
                ['loginName', 'userName','userType', 'tel', 'occupLevel','status'],
            buttons:              		                    // 表单按钮
                [
                    [
                        {
                            input: 'button',
                            type: 'button',
                            show: Msg.sure,
                            name: '',
                            align: 'right',
                            width: '50%',
                            unit: '&nbsp;&nbsp;&nbsp;&nbsp;',
                            extend: {'class': 'btn blueBtn btnThemeA noIcon'},
                            fnClick: submit
                        },
                        {
                            input: 'button',
                            type: 'button',
                            show: Msg.cancel,
                            name: '',
                            align: 'left',
                            width: '49%',
                            extend: {'class': 'btn grayBtn btnThemeB noIcon'},
                            fnClick: cancel
                        }
                    ]
                ]
        }, options);
        p.id = p.id + '_selectStationDialog';
        
        var s = {
            /**
             * 插件执行函数入口
             */
            work: function () {
                s.init();
                s.createDialog();
                s.createDialogContent();
            },
            /**
             * 初始化插件所需要的变量
             */
            init: function () {
                t.p = p;
                p.toolbarID = $(t).attr('id') + 'SelectToolbar';
                p.tableID = $(t).attr('id') + 'SelectTable';
                p.buttonsID = $(t).attr('id') + 'SelectButtons';
                p.selector = $(t).attr('id');
            },
            /**
             * 插件对话框
             */
            createDialog: function () {
                p.dialog = App.dialog({
                	id: p.id,
                    title: p.title,
                    resizable: false,
                    width: p.width,
                    height: p.height
                });
            },
            /**
             * 向对话框添加内容
             */
            createDialogContent: function () {
            	s.creatFrame();
            	s.createField();
                s.createToolbar();
                s.createTable();
                s.createButtons();
            },
            /**
             * 创建整体框架
             */
            creatFrame: function () {
            	var mainDiv = $("<div/>").addClass('mainFrame').attr('id','user_main_frame');
            	p.dialog.append(mainDiv);
            	var leftDiv = $("<div/>").addClass('leftFrame').attr('id','user_left_frame').appendTo(mainDiv);
            	var right = $("<div/>").addClass('rightFrame').attr('id','user_right_frame').appendTo(mainDiv);
            },
            /**
             * 创建域结构
             */
            createField: function () {
            	
            	var userTree = $("<ul/>").addClass("ztree flow").attr('id','user_dialog_tree')
            	var TreeDiv = $("<div/>").addClass("i18n sp").html(Msg.userDialog.domainChoice);
            	TreeDiv.append(userTree);
            	$('#user_left_frame').append(TreeDiv);
            	s.initTree(userTree);
            	
            },
            initTree : function(userTree) {
            	var userid = Cookies.get("userId");
            	$.http.post(p.domainUrl, {
            		"userid":userid
            	}, function(data){
                    if(data && data.success){
                    	var zTreeObj;
                        var zNodes = main.getZnodes(data.data);
                        var zTreeSetting = {
	                        treeId: "Locations",
	                        callback:{
	                              onClick: zTreeOnClick
	                        },
	                        view: {
	            				dblClickExpand: false,
	            				showLine: false,
	            				selectedMulti: false
	            			},
	            			data: {
	            				simpleData: {
	            					enable:true
	            				}
	            			}
                        };
                        $.fn.zTree.init($("#user_dialog_tree"), zTreeSetting, zNodes);
                        zTreeObj = $.fn.zTree.getZTreeObj("user_dialog_tree");
                       
                        function zTreeOnClick(event, treeId, treeNode) {
                        	 p.param.domainid = zTreeObj.getSelectedNodes()[0].id;
                             s.search();
                        };
                  }else{
                  }
              });
            },
            
            /**
             * 添加工具条
             */
            createToolbar: function () {
                var div = $("<div/>").addClass('SelectToolbar').attr('id', p.toolbarID);
                var model = [[
                    {
                        input: 'input',
                        type: 'text',
                        show: Msg.partials.main.hp.poverty.name,
                        name: 'userName',
                        width: 185,
                        rule: {space: true, maxlength: 100},
                        extend: {id: p.toolbarID + '_selectUser_username'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: Msg.all,
                        name: 'selectTextDialog',
                        extend: {id: 'selectTextDialog'},
                        placeholder: Msg.all,
                        hide: true
                    }

                ]];
                $('#user_right_frame').append(div);
                //调用表单插件，横向表单，有查询按钮
                div.ValidateForm(p.toolbarID, {
                    show: 'horizontal',
                    noButtons: p.noButtons,
                    fnModifyData: s.fnGetData,
                    fnSubmit: s.search,
                    fnListSuccess: s.fnGetData,
                    model: model
                });
                
                
            },
            /**
             * 点击查询按钮，响应函数
             */
            search: function () {
                p.param.sortname = "orderNo";
                p.param.sortorder = "asc";
                var id = p.toolbarID + '_selectUser_username';
                p.param.userName = $('#'+id).val();
                $('#' + p.tableID).GridTableSearch({params: p.param});
                $('#selectTextDialog').val('');
            },
            /**
             * 获取ValidateForm工具条的初始化内容
             */
            fnGetData: function (data) {
                if ($("#" + p.selector).attr("sname")) {
                    var sNameValue = $("#" + p.selector).attr("sname");
                    $('#selectSNameDialog').val(sNameValue);
                }
                var page = $("#" + p.selector).attr("page") ? $("#" + p.selector).attr("page") : 1;
                var pageSize = $("#" + p.selector).attr("pageSize") ? $("#" + p.selector).attr("pageSize") : 6;

                p.page = parseInt(page) ? parseInt(page) : 1;
                p.pageSize = parseInt(pageSize) ? parseInt(pageSize) : 6;
                p.param.sortname = "orderNo";
                p.param.sortorder = "asc";
                p.param.query = $('#selectSNameDialog').val();

                return data;
            },
            /**
             * 为选择框创建表格内容
             */
            createTable: function () {
                var div = $("<div/>").addClass('SelectTable');
               // p.dialog.append(div);
                $('#user_right_frame').append(div);
                var table = $("<div/>").attr('id', p.tableID);
                div.append(table);
                var colModel = [
                    {display: 'ID', name: 'userid', hide: true},
                    {display: Msg.userDialog.userName, name: 'loginName', width: 0.2, align: 'center'},
                    {display: Msg.partials.main.hp.poverty.name, name: 'userName', width: 0.2, align: 'center'},
                    {
                        display:  Msg.systemSetting.tTel,
                        name: 'tel',
                        width: 0.2,
                        align: 'center'
                    },
                    {
                    	display:  Msg.intelAlarm.alarmLev.name,
                    	name: 'occupLevel',
                    	width: 0.13,
                    	align: 'center',
                    	fnInit: s.operateUserLevel
                    },
                    {
                        display:  Msg.userDialog.type,
                        name: 'userType',
                        width: 0.15,
                        align: 'center',
                        fnInit: s.operateUserType
                    },
                    {
                        display:  Msg.userDialog.status,
                        name: 'status',
                        width: 0.12,
                        align: 'center',
                        fnInit: s.operateUserStatus
                    }
                ];
                if (p.columns && p.columns instanceof Array) {
                    for (var i = 0; i < colModel.length; i++) {
                        if (!p.columns.contains(colModel[i].name)) {
                            colModel.splice(i--, 1);
                        }
                    }
                }
                var length = 0;
                for (var i = 0; i < colModel.length; i++) {
                    length += colModel[i].width;
                }
                for (var i = 0; i < colModel.length; i++) {
                    colModel[i].width = colModel[i].width / (length || 1);
                }
                $("#" + p.tableID).GridTable({
                    url: p.url,
                    title: false,
                    max_height: 247,
                    params: p.param,
                    currentPage: p.page,
                    rp: p.pageSize,
                    onLoadReady: s.onLoadReady,
                    onDoubleClick: s.onDoubleClick,
                    singleSelect: p.singleSelect,
                    clickSelect: true,
                    isRecordSelected: true,
                    isSearchRecordSelected: true,
                    colModel: colModel,
                    idProperty: 'userid'
                });
            },
            /**
             * 用户类型
             */
            operateUserType: function (dom, value, record) {
                if (record.type == 'NORMAL') {
                    $(dom).parent().attr("title", Msg.gridParam.normalCount);
                    $(dom).html(Msg.gridParam.normalCount);
                } else {
                    $(dom).parent().attr("title", Msg.gridParam.systemCount);
                    $(dom).html(Msg.gridParam.systemCount);
                }
            },
            /**
             * 用户类型
             */
            operateUserLevel: function (dom, value, record) {
            	if (value == 1) {
            		$(dom).parent().attr("title", Msg.systemSetting.ulevels[2]);
            		$(dom).html(Msg.systemSetting.ulevels[2]);
            	} else if(value == 2){
            		$(dom).parent().attr("title", Msg.systemSetting.ulevels[1]);
            		$(dom).html(Msg.systemSetting.ulevels[1]);
            	}else if(value == 3){
            		$(dom).parent().attr("title", Msg.systemSetting.ulevels[0]);
            		$(dom).html(Msg.systemSetting.ulevels[0]);
            	}
            },
            /**
             * 用户状态
             */
            operateUserStatus: function (dom, value, record) {
                if (value == 'ACTIVE') {
                    $(dom).parent().attr("title", Msg.systemSetting.tactive);
                    $(dom).html(
                        "<font color='green'>" + Msg.systemSetting.tactive
                        + "</font>");
                } else {
                    $(dom).parent().attr("title", Msg.systemSetting.tlocked);
                    $(dom).html(
                        "<font color ='red'>" + Msg.systemSetting.tlocked
                        + "</font>")
                }

            },
            /**
             * Gridtable加载完成后的执行函数
             */
            onLoadReady: function (data, btrs, htrs) {
                if (!p.loaded) {
                    p.loaded = true;
                    var selectedRecords = $(t).data('selectedRecords');
                    if (!selectedRecords || selectedRecords.length == 0) {
                    	var arr = [];
                        var values = ($(t).attr('value') && $(t).attr('value').split(','));
                        var names = ($(t).val() && $(t).val().split(','));
                        $(values).each(function (i) {
                            var o = {};
                            o.userid = values[i];
                            o.userName = names[i];
                            arr.push(o);
                        });
                        $('#' + p.tableID).GridTableInitSelectedRecords(arr);
                    }
                    else {
                    	$('#' + p.tableID).GridTableInitSelectedRecords(selectedRecords);
                    }
                    
                }
                //有初始化的 userId 时
                if(p.initUserId){
                    $(btrs).find("div[name='userid'][value='" + p.initUserId + "']").parentsUntil("tr").click();
                //单选时，如果未选中电站，默认选中第一个
                }else if (!$(t).data('selectedRecords') && p.singleSelect) {
                    $(btrs[0]).click();
                }
            },
            /**
             * 双击表格行事件
             */
            onDoubleClick: function (row, data, checked) {
                if (p.onDoubleClick) {
                    s.submit(data);
                }
            },

            /**
             * 作用:添加addButtonsTable
             * @validate:Form表单框架
             * @data:所要添加的一列内容的配置数据
             */
            createButtons: function () {
                var tableContent = $("<table/>")
                    .attr('width', '100%')
                    .attr('align', 'center')
                    .attr('id', p.buttonsID)
                    .addClass('SelectButtons')
                    .addClass("ui-dialog-buttonpane");

                p.dialog.append(tableContent);
                $.each(p.buttons, function () {
                    var trContent = $("<tr/>");
                    tableContent.append(trContent);
                    s.addButton(trContent, this);
                });
            },
            /**
             * 添加选择框按钮
             * @trContent：按钮行
             * @data：按钮数据
             */
            addButton: function (trContent, data) {
                $.each(data, function () {
                    var tdContent = $("<td/>");
                    tdContent.attr('align', this.align).attr('width', this.width);
                    trContent.append(tdContent);
                    var input = $("<" + this.input + "/>").attr("type", this.type).addClass(this.name);
                    var span = $("<span/>").addClass("ui-button-text");
                    this.show ? span.html(this.show) : 0;
                    tdContent.append(input);
                    input.append(span);
                    this.unit ? tdContent.append($("<span/>").html(this.unit)) : 0;
                    this.fnClick ? input.click(this.fnClick) : 0;
                    if (this.extend) {
                        for (var key in this.extend) {
                            if (this.extend.hasOwnProperty(key)) {
                                input.attr(key, this.extend[key]);
                            }
                        }
                    }

                    var replaceWith = $("<button type='" + input.attr("type") + "' class='" + input.attr("class") + "' >"
                    + "  <span class='btnL btnS'></span>"
                    + "  <span class='btnC btnS'>" + this.show + "</span>"
                    + "  <span class='btnR btnS'></span>"
                    + "</button>");
                    input.replaceWith(replaceWith);
                    this.fnClick && replaceWith.click(this.fnClick);
                });
            },
            /**
             * 关闭选择框
             */
            cancel: function () {
            	$("#"+p.id).modal("hide");
            },
            /**
             * 确定按钮执行函数
             */
            submit: function () {
                var selectedRecords = $('#' + p.tableID).GridTableSelectedRecords();
                var values = [];
                var names = [];
                $.each(selectedRecords, function () {
                    if (!values.contains(this.userid)) {
                        values.push(this.userid);
                        names.push(this.userName);
                    }
                });
                $('#selectTextDialog').val(names.toString()).attr('value', values.toString());
                $(t).attr('value', $('#selectTextDialog').attr('value'))
                    .attr('page', $('#' + p.tableID + 'GridTablePageBarplabel_currentPage1').html())
                    .attr('pageSize', $('#' + p.tableID + 'GridTablePageBarpselect_rps').val())
                    .attr('sname', $('#selectTextDialog').attr('sname'))
                    .attr('stationType', $('#selectTextDialog').attr('stationType'))
                    .attr('inverterType', $('#selectTextDialog').attr('inverterType'));
                $(t).val($('#selectTextDialog').val());
                if (p.submit) {
                    $(t).data('selectedRecords', selectedRecords);
                    p.submit($('#selectTextDialog').attr('value'), $('#selectTextDialog').val());
                }
                $("#"+p.id).modal("hide");
                $(t).focusout();
            }
        };

        function submit() {
            s.submit();
        }

        function cancel() {
            s.cancel();
        }

        s.work();
        return true;
    };

    var docLoaded = false;
    $(document).ready(function () {
        docLoaded = true;
    });

    $.fn.UserDialog = function (p) {
        return this.each(function () {
            if (!docLoaded) {
                $(this).hide();
                var t = this;
                $(document).ready(function () {
                    $.addSelectUser(t, p);
                });
            } else {
                $.addSelectUser(this, p);
            }
        });
    };

});