/**
 * jQuery
 */
define(['jquery', 'core/right', 'css!plugins/GridTable/css/GridTable.css'], function ($, Menu) {

    var addGrid = function (t, options) {
        var p = $.extend({}, {
            expandFlag: '',
            expandDisableFlag: '',         //列表展开是否显示为禁用图标判断函数

            theme: 'default',              //主题

            width: 'auto',                 //选择框宽度
            height: 'auto',                //选择框高度

            url: '',                       //表格数据后台请求地址
            list: 'list',                  //后台请求的数据列表名
            total: 'count',                //后台请求的数据总记录名
            params: {},                    //请求的参数

            header: true,                  //表格是否有表头，false不显示，true显示
            title: false,                  //表格是否有标题，false不显示，true显示
            pageBar: true,                 //表格是否有工具条，false不显示，true显示
            body: true,				       //表格是否有表格主体内容，false不显示，true显示
            toolBar: false,			       //表格是否有表格工具条，false不显示，true显示

            titleText: false,		       //表格的标题内容，string型
            title_cellspacing: '0',	       //title间隔
            header_cellspacing: '0',       //header间隔
            body_cellspacing: '0',	       //body间隔
            toolBar_cellspacing: '0',      //toolBar间隔
            overflow: true,                //是否启用滚动条
            titleModel:				       //表格的复杂标题内容模型
                [
                    [
                        {display: '电站名称', extend: {'id': '11'}, align: "center"},
                        {display: '电站名称', align: "center"},
                        {display: '电站名称', width: '0.1', align: "right"}
                    ]
                ],


            loadError: false,		       //数据请求失败的回调函数
            loadReady: false,		       //数据请求成功的回调函数

            data: false,				   //表格内所要填充的数据
            //prototypeData: false, 			//原型数据 list:[] 数组
            prototypeSort: function (prop, sort) { // 默认排序方法
                return function (obj1, obj2) {
                    if (!prop || !sort) {
                        return 0;
                    }
                    var val1 = obj1[prop];
                    var val2 = obj2[prop];
                    if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
                        val1 = Number(val1);
                        val2 = Number(val2);
                    }
                    if (val1 < val2) {
                        return "desc" == sort.toLowerCase() ? 1 : -1;
                    } else if (val1 > val2) {
                        return "desc" == sort.toLowerCase() ? -1 : 1;
                    } else {
                        return 0;
                    }
                }
            },

            onSingleClick: false,	       //表格行点击事件
            onDoubleClick: false,	       //表格行双击事件
            onLoadReady: false,		       //表格主体内容加载完成的回调函数

            isShowSelect: true,            //是否显示选择操作列
            singleSelect: false, 	       //可多选还是单选，true单选，false多选
            isAllSelect: true,             //是否可全选（当 singleSelect = false 时有效）
            clickSelect: false,		       //行是否可以点击

            max_height: 'auto',		       //最大高度
            line_height: 50,		       //单行行高
            preColumnWidth: 180,		   //默认列宽度
            scrollWidth: 8, 			   //默认滚动条宽度
            rp: 10,					       //表格当前每页可装载的记录条数
            rps: [5, 10, 15, 30, 50],	       //表格每页可装载的记录条数分配
            totalRecords: 0,		       //表格总记录数
            currentPage: 1,			       //表格当前页数
            totalPage: 1,			       //表格总页数
            toPage: 1,				       //表格要跳转的页数

            align: 'center',		       //默认排版类型

            colModel: null,			       //单行表头模型设置，使用方式与colModels类似

            singalLineHeaderTable: true,   //是否单行表头

            resizable: true,		       //是否自适应变化
            columnFilter: false,           //展示列可设置，默认：false，表示全部显示，不可设置；数组，指定当前显示列的列名[name属性值数组]，如["name", "age"]

            parent: false,			       //父窗

            isShowNoData: false,           //是否在无数据时在表格中显示提示信息
            isRecordSelected: false,       //跨页选择
            isSearchRecordSelected: false, //是否在查询时记录选中项, isRecordSelected=true时生效
            showSelectedName: false,       //显示选中项展示板
            idProperty: false,             //主键（默认配置模型中的第一列）

            expand: false,                 //{Boolean | Function (record, box, index)} 是否可展开，如果这里是一个方法，那么展开时会执行该方法
            fold: false,                   //{Boolean | Function (box, index)} 配套expand参数使用，如果expand为true或者方法，在展开层收起来时会执行该方法
            expandIndex: null,             //初始展开行号，基数为 0，默认 初始时都不展开
            expandBoxHeight: 'auto',       //展开容器的高度，默认为: 行高（line-height)

            colModels:				       //多行表头模型设置
                [					       //实例
                    [
                        {display: '电站名称', name: '0', rowspan: '2', width: '0.2', align: "center"},
                        {display: '并网类型', name: '7', rowspan: '2', width: '0.1', align: "center"},
                        {display: '逆变器类型', name: '8', rowspan: '2', width: '0.1', align: "center"},
                        {
                            display: '工作票',
                            name: '',
                            colspan: '2',
                            width: '0.2',
                            align: "center",
                            before: false,
                            after: false,
                            order: true  // 是否支持该列排序
                        },
                        {display: '操作票', name: '', colspan: '2', width: '0.2', align: "center"},
                        {display: '缺陷', name: '', colspan: '2', width: '0.2', align: "center"}
                    ],
                    [
                        {display: '总数(个)', name: '1', width: '0.1', align: "center"},
                        {display: '平均处理时长(h)', name: '2', width: '0.1', align: "center"},
                        {display: '总数(个)', name: '3', width: '0.1', align: "center"},
                        {display: '平均处理时长(h)', name: '4', width: '0.1', align: "center"},
                        {
                            display: '总数(个)',
                            name: '5',
                            width: '0.1',
                            align: "center",
                            unit: '%',
                            css: {'color': 'blue', 'font-size': '15px'}
                        },
                        {display: '平均处理时长(h)', name: '6', width: '0.1', align: "center"}
                    ]
                ],
            leftContent:		           //表格左边分页工具条内容
                [
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.pageShowCount,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_0'
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: 'rps',
                        fix: false,
                        name: 'pselect',
                        ids: 'pselect_rps',
                        right: 6
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.emptyMsg,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_totalRecords'
                    }
                ],
            rightContent:		           //表格左边分页工具条内容
                [
                    {
                        input: 'span',
                        type: 'button',
                        show: '',
                        fix: true,
                        name: 'pbutton',
                        ids: 'pbutton_first',
                        width: 24,
                        height: 24,
                        left: 3,
                        right: 3
                    },
                    {
                        input: 'span',
                        type: 'button',
                        show: '',
                        fix: true,
                        name: 'pbutton',
                        ids: 'pbutton_previous',
                        width: 24,
                        height: 24,
                        left: 3,
                        right: 3
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: 'currentPage',
                        fix: false,
                        name: 'plabel_curPage',
                        ids: 'plabel_currentPage1',
                        width: 'auto'
                    },
                    {
                        input: 'span',
                        type: 'button',
                        show: '',
                        fix: true,
                        name: 'pbutton',
                        ids: 'pbutton_next',
                        width: 24,
                        height: 24,
                        left: 3,
                        right: 3
                    },
                    {
                        input: 'span',
                        type: 'button',
                        show: '',
                        fix: true,
                        name: 'pbutton',
                        ids: 'pbutton_last',
                        width: 24,
                        height: 24,
                        left: 3,
                        right: 10
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.beforePageText,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_3',
                        width: 15
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: 'currentPage',
                        fix: false,
                        name: 'plabel',
                        ids: 'plabel_currentPage2',
                        width: 'auto'
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.afterPageText,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_4',
                        width: 10
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: 'totalPage',
                        fix: false,
                        name: 'plabel',
                        ids: 'plabel_totalPage',
                        width: 'auto'
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.mPageText,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_5',
                        width: '20'
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.jumpTo,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_6',
                        width: 26,
                        left: 10
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: 'toPage',
                        fix: false,
                        name: 'ptext',
                        ids: 'ptext_toPage',
                        width: 26,
                        height: 26,
                        right: 3
                    },
                    {
                        input: 'label',
                        type: 'label',
                        show: Msg.gridParam.mPageText,
                        fix: true,
                        name: 'plabel',
                        ids: 'plabel_7',
                        width: 15
                    },
                    {
                        input: 'span',
                        type: 'button',
                        show: 'GO',
                        fix: true,
                        name: 'pbutton_jumpTo',
                        ids: 'pbutton_jumpTo',
                        width: 26,
                        height: 26
                    }
                ]
        }, options);

        /**
         * 函数集合
         */
        var g = {
            /**
             * 初始化ID变量信息
             */
            init: function () {
                g.p = p;
                if ((main.getBrowser() && main.getBrowser().msie)) {
                    p.scrollWidth = 20;
                }
                p.selectedRecords = [];
                p.indexMap = {};
                p.defaultColumnFilter = p.columnFilter;

                p.selector = $(t).attr('id') || (function () {
                    var id = new Date().getTime();
                    $(t).attr('id', id);

                    return id;
                })();
                p.wholeID = 'GridTable_' + p.selector;
                p.titleID = 'GridTableTitle_' + p.selector;
                p.headerID = 'GridTableHeader_' + p.selector;
                p.bodyID = 'GridTableBody_' + p.selector;
                p.pageBarID = 'GridTablePageBar' + p.selector;

                g.createwhole();

                $(t).resize(function () {
                    g.resize();
                });
                g.resize();
            },
            settingScroll: function () {
                // var tabDivClass = "GridTableBodyDiv .wrapper";
                // var tabs = $('.GridTableDiv');
                // var scrollWidth = p.scrollWidth;
                // if (tabs && tabs.length > 0) {
                //     for (var i = 0; i < tabs.length; i++) {
                //         var tabChild = tabs[i];
                //         if ($(tabChild).find('.' + tabDivClass).length > 0) {
                //             var ow = tabChild.offsetWidth;
                //             var tabDiv = $(tabChild).find('.' + tabDivClass);
                //             if (tabDiv[0].scrollHeight > tabDiv[0].offsetHeight) {
                //                 tabDiv.css("width", ((ow + scrollWidth) / (ow + 1)) * 100 + "%");
                //             } else {
                //                 tabDiv.css("width", ((ow) / ow) * 100 + "%");
                //             }
                //         }
                //     }
                // }
            },
            /**
             * 数组复制函数
             */
            _arrayCopy: function (array) {
                var copy = null;
                if (array && array instanceof Array) {
                    copy = [];
                    if (array[0][0]) {
                        $.each(array, function () {
                            copy.push(this.concat());
                        });
                    } else {
                        copy = array.concat();
                    }
                }
                return copy;
            },
            /**
             * 父窗大小变化的回调函数
             */
            resize: function () {
                g.settingScroll();
                var w = g.getContentSize();
                if (!p.resizable) {
                    return;
                }
                p.body && g.resizeBox();
            },
            resizeBox: function () {
                setTimeout(function () {
                    if (p.resizable) {
                        $(t).find('table').filter(function (index, val) {
                            if (index < 2) {
                                var resizeDomList = $('>tbody>tr>.GridItem', $(val)).not('.lastItem, .noResize');
                                $.each(resizeDomList, function (i, t) {
                                    var w = $(t).attr('data-width');
                                    var rw = g.calWidth(w, p.col_num);
                                    $(t).width(rw);
                                    $(t).attr('width', rw);
                                });
                            }
                        });
                    }
                    if (p.pageBar) {
                        if (p.boxWidth < 678) {
                            $('.GridTableToolBarBodyLeft', $('#' + p.pageBarID)).hide();
                            if (p.boxWidth < 478) {
                                $('.GridTableToolBarBodyRight .plabel, .GridTableToolBarBodyRight .ptext, .GridTableToolBarBodyRight .pbutton_jumpTo', $('#' + p.pageBarID)).hide();
                            } else {
                                $('.GridTableToolBarBodyRight .plabel, .GridTableToolBarBodyRight .ptext, .GridTableToolBarBodyRight .pbutton_jumpTo', $('#' + p.pageBarID)).show();
                            }
                        } else {
                            $('.GridTableToolBarBodyLeft', $('#' + p.pageBarID)).show();
                            $('.GridTableToolBarBodyRight .plabel, .GridTableToolBarBodyRight .ptext, .GridTableToolBarBodyRight .pbutton_jumpTo', $('#' + p.pageBarID)).show();
                        }
                    }
                    if (!$('#' + p.bodyID + '>.wrapper').hasClass('autoHeightBody')) {
                        $('#' + p.bodyID + '>.wrapper').height(p.height - $('#' + p.headerID).height());
                    }
                }, 0);
            },
            /**
             * 获取父窗的宽度，当作改表格插件宽度
             */
            getBoxSize: function () {
                if (p.parent) {
                    return $('#' + p.parent).width();
                }
                var node = $(t), i = 0;
                while (!node.width() && i < 3) {
                    node = node.parent();
                    i++;
                }
                return node.width();
            },
            /**
             * 获取内容体的宽度
             */
            getContentSize: function () {
                var resultWidth;
                if (p.resizable) {
                    resultWidth = g.getBoxSize() - 1;
                } else {
                    var colNum = 0;
                    $.each(p.colModelCopy, function () {
                        if (!this.hide) {
                            colNum++;
                        }
                    });
                    colNum = p.columnFilter.length || p.col_num || colNum || 8;
                    resultWidth = Math.max(g.getBoxSize() - 1, p.preColumnWidth * colNum);
                }

                return resultWidth;
            },
            /**
             * 计算元素宽度
             * @param width 宽度表示值
             * @param cols 总列数
             * @returns {Number | *} 实际宽度值
             */
            calWidth: function (width, cols) {
                var resultWidth = g.getContentSize();
                (p.clickSelect && p.isShowSelect) && (resultWidth -= p.line_height);
                p.expand && (resultWidth -= p.line_height);
                if (width) {
                    if (width <= 1) {
                        if (cols) {
                            return Math.round(width * (resultWidth - p.col_num * 2 + cols / 2));
                        }
                        return Math.round(width * (resultWidth - p.col_num * 2));
                    }
                    else if ((width + "").indexOf('%') != -1) {
                        var index = (width + "").indexOf('%');
                        var w = width.substring(0, index - 1);
                        return Math.round((w * resultWidth) / 100);
                    }
                }
                else if (cols) {
                    return Math.round((resultWidth - p.col_num * 2) / cols - cols);
                }
                else {
                    return resultWidth;
                }
            },
            /**
             * 创建表格整体的DIV
             */
            createwhole: function () {
                // TODO
                if (p.colModel) {
                    p.colModelCopy = g._arrayCopy(p.colModel);
                } else {
                    g.resetColModel();
                }

                p.columnNames = [];
                p.col_num = 0;
                $.each(p.colModelCopy, function () {
                    if (!this.hide) {
                        p.columnNames.push({id: this.name, name: this.display, width: this.width, leaf: true});
                        p.col_num++;
                    }
                });

                p.boxWidth = g.getBoxSize();
                p.contentWidth = g.getContentSize();

                $(t).empty();

                var div = $("<div class='GridTableDiv' id='" + p.wholeID + "'/>");
                //div.css({'width': p.boxWidth});
                p.theme && div.addClass(p.theme);
                div.attr('resizable', p.resizable);
                !p.resizable && div.css({
                    'overflow-x': 'auto',
                    'overflow-y': 'hidden'
                });
                $(t).append(div);

                p.isRecordSelected && p.showSelectedName && g.createSelectedShowBox();
                p.title && g.createTitle();
                p.header && g.createHeader();
                p.body && g.createBody();
                if (p.pageBar) {
                    g.pageBar();
                    g.initEvents();
                }
                p.body && (p.data ? g.addData() : g.refreshPage());
            },
            /**
             * 创建表格选中项展示板
             */
            createSelectedShowBox: function () {
                var div = $('<ul></ul>').addClass('selectedShowBox');
                $('#' + p.selector).append(div);
            },
            /**
             * 创建表格标题
             */
            createTitle: function () {
                var div = $("<div class='GridTableTitleDiv' id='" + p.titleID + "'/>");
                div.css({'width': p.boxWidth, 'height': p.line_height});
                $('#' + p.selector).append(div);
                if (p.titleText) {
                    div.addClass('SingleTitle').html(p.titleText);
                    return;
                }
                var tableContent = $('<table width="100%" class="GridTableTitle" cellpadding="0" border="0"/>')
                    .attr('cellspacing', p.title_cellspacing);
                div.append(tableContent);
                if (p.titleModel) {
                    var trContent = $("<tr/>").css({'height': p.line_height});

                    tableContent.append(trContent);
                    if (p.titleModel[0]) {
                        g.createTitleLeft(trContent, p.titleModel[0]);
                    }
                    if (p.titleModel[1]) {
                        g.createTitleRight(trContent, p.titleModel[1]);
                    }
                }
            },
            /**
             * 创建表格标题左边内容
             */
            createTitleLeft: function (trContent, data) {
                var tdContent = $("<td style='text-align:left'; width='50%' class='GridTableTitleLeftTD'/>");
                trContent.append(tdContent);
                var tableContent = $('<table class="GridTableTitle" cellpadding="0" border="0"/>')
                    .attr('cellspacing', p.title_cellspacing);
                tdContent.append(tableContent);
                g.createTitleTR(tableContent, data);
            },
            /**
             * 创建表格标题右边内容
             */
            createTitleRight: function (trContent, data) {
                var tdContent = $("<td style='text-align:right' width='50%' class='GridTableTitleRightTD'/>");
                trContent.append(tdContent);
                var tableContent = $('<table class="GridTableTitle" cellpadding="0" border="0"/>')
                    .attr('cellspacing', p.title_cellspacing);
                tdContent.append(tableContent);
                g.createTitleTR(tableContent, data);
            },
            /**
             * 创建表格标题tr内容
             */
            createTitleTR: function (tableContent, data) {
                var trContent = $("<tr class='GridTableTitleTR'/>");
                $.each(data, function (i) {
                    var td = $('<td/>');
                    this.width && td.attr('width', g.calWidth(this.width));
                    var content = $("<div/>");
                    if (this.before) {
                        content.append(this.before.css({
                            'display': 'inline-block',
                            'vertical-align': 'middle',
                            'margin-right': '10px'
                        }));
                    }
                    content.append(this.display);
                    if (this.after) {
                        content.append(this.after.css({
                            'display': 'inline-block',
                            'vertical-align': 'middle',
                            'margin-left': '10px'
                        }));
                    }
                    td.append(content);
                    if (this.extend) {
                        for (var key in this.extend) {
                            if (this.extend.hasOwnProperty(key)) {
                                content.attr(key, this.extend[key]);
                            }
                        }
                    }
                    this.rowspan ? td.attr('rowspan', this.rowspan) : 0;
                    this.colspan ? td.attr('colspan', this.colspan) : 0;
                    this.css ? content.css(this.css) : 0;
                    this.fnClick ? content.click(this.fnClick) : 0;
                    this.hide ? content.hide() : 0;
                    this.content == '' ? content.html('') : 0;
                    this.align ? td.css('text-align', this.align) : td.css('text-align', p.align);
                    trContent.append(td);
                });
                tableContent.append(trContent);
            },
            /**
             * 创建表格表头div
             */
            createHeader: function () {
                var content = $('<div/>').addClass('wrapper');
                !p.resizable && content.css({'width': '100%'});
                $('#' + p.wholeID).append($("<div class='GridTableHeaderDiv' id='" + p.headerID + "'/>").append(content));
                g.createHeaderTable(content);

                p.columnFilter && g.createHeaderFilter();
            },
            /**
             * 创建表格表头table
             */
            createHeaderTable: function (div) {
                div.empty();
                var tableContent = $('<table class="GridTableHeader" width="100%" cellpadding="0" border="0"/>')
                    .attr('cellspacing', p.header_cellspacing);
                //if (p.resizable) {
                //    tableContent.css({"width": p.contentWidth});
                //}
                div.append(tableContent);

                if (p.colModel) {
                    p.col_num = g.createHeaderTR(tableContent, p.colModelCopy);
                    div.attr('singalLineHeaderTable', true);
                    p.singalLineHeaderTable = true;
                } else {
                    var rcols = 0;
                    p.col_num = p.colModelCopy.length;
                    div.attr('singalLineHeaderTable', false);
                    p.singalLineHeaderTable = false;
                    g.createMitlHeadeLayoutTR(tableContent, p.colModelCopy);
                    $.each(p.colModels, function () {
                        rcols = g.createHeaderTR(tableContent, this, rcols);
                    });
                }
            },

            /**
             * 创建列过滤器
             */
            createHeaderFilter: function () {
                /**
                 * 绘制列选项
                 * @param context
                 */
                var drawBox = function (context, data) {
                    var box = $('ul', context);
                    box.empty();

                    var item_all = $('<li>').addClass('filter-item');
                    var checkbox_all = $('<input type="checkbox">').attr('id', 'column_filter_all')
                        .attr('name', 'allOption');
                    item_all.append($('<label for="column_filter_all"/>').append(checkbox_all).append(Msg.all));
                    checkbox_all.click(function () {
                        $('.item :checkbox', box).prop('checked', !!this.checked);
                    });
                    box.append(item_all);

                    $.each(p.columnNames, function () {
                        var item = $('<li>').addClass('filter-item').addClass('item');
                        var checkbox = $('<input type="checkbox">').attr('id', 'item_' + this.id)
                            .attr('name', 'itemOptions').attr('value', this.id);

                        data.indexOf(this.id) != -1 && checkbox.prop('checked', true);

                        checkbox.click(function () {
                            checkbox_all.prop('checked', $('.item :checkbox', box).length == $('.item :checked', box).length);
                        });
                        item.append($('<label for="item_' + this.id + '"/>').append(checkbox).append(this.name));
                        box.append(item);
                    });
                    checkbox_all.prop('checked', $('.item :checkbox', box).length == $('.item :checked', box).length);
                };

                var settingContent = $('<div/>').addClass('GridTableColumnFilter');
                var settingBtn = $('<div/>').addClass('setting_button');
                var settingBox = $('<form/>').addClass('setting_box').hide();

                settingBox.append($('<h2>').text(Msg.gridParam.columnFilter));

                var defaultBtnBar = $('<div/>').addClass('default-button-bar');
                var defaultBtn = $('<button/>').attr('type', 'button').addClass('btn')
                    .text(Msg.gridParam.defaultSettings);

                // 事件 - 恢复默认按钮
                defaultBtn.click(function () {
                    drawBox(settingBox, p.defaultColumnFilter || p.columnFilter);
                });

                defaultBtnBar.append(defaultBtn);
                settingBox.append(defaultBtnBar);

                settingBox.append($('<ul/>'));

                var btnBar = $('<div/>').addClass('button-bar');
                var okBtn = $('<button/>').attr('type', 'button').addClass('btn').text(Msg.sure);
                var cancelBtn = $('<button/>').attr('type', 'button').addClass('btn').text(Msg.cancel);

                // 事件 - 确认按钮
                okBtn.click(function () {
                    var columns = [];
                    var sa = settingBox.serializeArray();
                    $.each(sa, function () {
                        if (this.name == 'itemOptions') {
                            columns.push(this.value);
                        }
                    });
                    if (columns.length <= 0) {
                        return App.alert(Msg.gridParam.selectOneLess);
                    }
                    p.columnFilter = columns;

                    g.createwhole();
                    settingBox.hide(250);
                });
                // 事件 - 取消按钮
                cancelBtn.click(function () {
                    settingBox.hide(250);
                });

                btnBar.append(okBtn).append(cancelBtn);

                settingBox.append(btnBar);

                // 事件 - 展开/收起设置面板
                settingBtn.click(function () {
                    drawBox(settingBox, p.columnFilter);
                    settingBox.toggle(250);
                });

                settingContent.append(settingBtn).append(settingBox);

                $('#' + p.selector).append(settingContent);
            },

            /**
             * 绘制多行表头布局行
             * @param tableContent
             * @param data
             * @returns {number}
             */
            createMitlHeadeLayoutTR: function (tableContent, data) {
                var trContent = $("<tr/>").addClass('tableLayoutLine');
                var rcols = 0;
                $.each(data, function () {
                    if (!this.hide) {
                        var counter = false;
                        if (p.columnFilter) {
                            if (p.columnFilter.indexOf(this.name) != -1) {
                                counter = true;
                            }
                        } else {
                            counter = true;
                        }
                        if (counter) {
                            rcols += (+this.colspan || 1);
                        }
                    }
                });
                if (p.clickSelect && p.isShowSelect) {
                    trContent.append(
                        $('<th/>').addClass('noResize').attr('width', p.line_height / 2).attr('height', 1)
                    );
                }
                if (p.expand) {
                    trContent.append(
                        $('<th/>').addClass('noResize').attr('width', p.line_height / 2).attr('height', 1)
                    );
                }

                $.each(data, function (i, t) {
                    var th = $('<th/>').addClass('GridItem').attr('data-width', t.width)
                        .attr('height', 1).attr('name', t.name);
                    th.attr('width', g.calWidth(t.width, rcols));

                    t.colspan ? th.attr('colspan', t.colspan) : 0;
                    if (t.hide || (p.columnFilter && p.columnFilter.indexOf(t.name) == -1)) {
                        th.hide();
                    }

                    trContent.append(th);
                });
                tableContent.append(trContent);
                return rcols;
            },

            /**
             * 创建表格表头行
             */
            createHeaderTR: function (tableContent, data, totalCols) {
                var trContent = $("<tr class='GridTableHeaderTH'/>");
                //if (p.clickSelect && !p.singleSelect && p.isAllSelect) trContent.attr('title', Msg.GridTable);
                if (p.isAllSelect) {
                    trContent.click(function () {
                        g.singleClickHeaderLine(trContent, false);
                    });
                    trContent.dblclick(function () {
                        g.doubleClickHeaderLine(trContent);
                    });
                }

                var shifting = 0;
                var rcols = 0, rrows = 0;
                $.each(data, function () {
                    if (!this.hide) {
                        var counter = false;
                        if (p.columnFilter) {
                            if (p.columnFilter.indexOf(this.name) != -1) {
                                counter = true;
                            }
                        } else {
                            counter = true;
                        }
                        if (counter) {
                            shifting++;
                            rcols += (+this.colspan || 1);
                            rrows < +this.rowspan && (rrows = this.rowspan);
                        }
                    }
                });

                if (!totalCols && (p.clickSelect && p.isShowSelect)) {
                    var th = $('<th/>').addClass('noResize').attr('rowspan', rrows)
                        .attr('width', p.line_height).attr('height', p.line_height).css('text-align', 'center');
                    var div = $('<div/>').css({'width': p.line_height, 'height': p.line_height});
                    var cthch;
                    if (p.singleSelect) {
                        cthch = $('<input type="radio" />')
                            .attr('name', p.selector + '_single_' + (p.idProperty || p.colModelCopy[0].name)).hide();
                    } else {
                        cthch = $('<input type="checkbox"/>');
                        !p.isAllSelect && cthch.hide();
                    }
                    var m = (p.line_height - 18) / 2;
                    cthch.css({'margin': m}).addClass('HeaderCheckBox')
                        .click(function () {
                            cthch[0].checked = !cthch[0].checked;
                        });
                    div.append(cthch);
                    th.append(div);
                    trContent.append(th);
                }
                if (!totalCols && p.expand) {
                    var th = $('<th/>').addClass('ExpandBox').addClass('noResize').attr('rowspan', rrows)
                        .attr('width', p.line_height).attr('height', p.line_height).css('text-align', 'center');
                    var div = $('<div/>').css({'width': p.line_height, 'height': p.line_height});
                    div.addClass('HeaderExpand');
                    th.append(div);
                    trContent.append(th);
                }

                $.each(data, function (i, t) {
                    var th = $('<th/>').addClass('GridItem').attr('data-width', t.width)
                        .attr('height', p.line_height).attr('name', t.name);
                    var content = $("<div/>");
                    totalCols -= (t.colspan || 1);
                    // TODO
                    var w = g.calWidth(t.width, rcols);
                    if (i != data.length - 1) {
                        th.attr('width', w);
                        //th.css('width', w);
                    } else {
                        p.resizable ? th.addClass('lastItem') : th.attr('width', w);
                        content.css({'width': '100%'});
                    }

                    if (t.before) {
                        content.append(t.before.css({
                            'display': 'inline-block',
                            'vertical-align': 'middle',
                            'margin-right': '10px'
                        }));
                    }
                    content.append(t.display);

                    if (t.after) {
                        content.append(t.after.css({
                            'display': 'inline-block',
                            'vertical-align': 'middle',
                            'margin-left': '10px'
                        }));
                    }

                    if (t.order) {
                        var sortBy = $('<i/>').addClass('sortBy');
                        sortBy.click(function (e) {
                            p.params['orderBy'] = t.name;
                            if ($(this).hasClass('asc')) {
                                $('.sortBy', tableContent).removeClass('asc desc');
                                $(this).removeClass('asc').addClass('desc');
                                p.params['sort'] = 'desc';
                            } else {
                                $('.sortBy', tableContent).removeClass('asc desc');
                                $(this).addClass('asc').removeClass('desc');
                                p.params['sort'] = 'asc';
                            }
                            g.refreshPage();
                            e.stopPropagation();
                        });
                        if (p.params && p.params['orderBy'] == t.name) {
                            sortBy.addClass(p.params['sort'] || 'asc');
                        }
                        content.append(sortBy);
                    }

                    th.append(content);
                    if (t.extend) {
                        for (var key in this.extend) {
                            if (t.extend.hasOwnProperty(key)) {
                                th.attr(key, t.extend[key]);
                            }
                        }
                    }
                    t.rowspan && th.attr('rowspan', t.rowspan);
                    t.colspan && th.attr('colspan', t.colspan);
                    t.content == '' && th.html('');
                    th.css('text-align', p.align);
                    if (t.hide || (p.columnFilter && p.columnFilter.indexOf(t.name) == -1)) {
                        th.addClass('hidden');
                    } else {
                        th.addClass('visibility');
                    }

                    trContent.append(th);
                });
                tableContent.append(trContent);
                return rcols;
            },
            /**
             * 复制json数据
             */
            clone: function (data) {
                var copy = {};
                for (var key in data) {
                    copy[key] = data[key];
                }
                return copy;
            },
            /**
             * 重新整理表头模型数据，去除合并单元格的影响
             */
            resetColModel: function () {
                var colModels = g._arrayCopy(p.colModels);
                $.each(colModels, function (i) {
                    for (var j = 0; ; j++) {
                        if (!colModels[i][j]) {
                            break;
                        }
                        if (colModels[i][j].colspan && !colModels[i][j].colCopy) {
                            var colspan = parseInt(colModels[i][j].colspan);
                            for (var k = j + 1; k < j + colspan; k++) {
                                var that = g.clone(colModels[i][j]);
                                that.colCopy = true;
                                colModels[i].splice(k, 0, that);
                            }
                            j += colspan - 1;
                        }
                    }
                });
                $.each(colModels, function (i) {
                    $.each(this, function (j) {
                        if (this.rowspan && !this.rowCopy) {
                            var rowspan = parseInt(this.rowspan);
                            for (var k = i + 1; k < colModels.length && k < i + rowspan; k++) {
                                var that = g.clone(this);
                                that.rowCopy = true;
                                colModels[k].splice(j, 0, that);
                            }
                        }
                    });
                });
                $.each(colModels, function (i) {
                    $.each(this, function (j) {
                        if (this.colspan && this.colCopy) {
                            colModels[i].splice(j, 1)
                        }
                    });
                });
                var max = 0;
                for (var i = 1; i < colModels.length; i++) {
                    if (colModels[i].length >= colModels[i - 1].length) {
                        max = i;
                    }
                }
                p.colModelCopy = colModels[max].concat();
            },
            /**
             * 创建表格主体
             */
            createBody: function () {
                var content = $('<div class="GridTableBodyDiv" id="' + p.bodyID + '" />');
                var div = $('<div/>').addClass('wrapper');
                !p.resizable && div.css({'width': p.contentWidth - 1});
                var tableContent = $('<table class="GridTableBody" width="100%" cellpadding="0" border="0"/>')
                    .attr('cellspacing', p.body_cellspacing);
                if (p.expand) {
                    tableContent.addClass("GridTableExpandBody");
                }
                //if (p.resizable) {
                //    tableContent.css({"width": p.contentWidth});
                //}
                var overflowY = 'auto';
                if (!p.overflow) {
                    overflowY = 'hidden';
                }
                p.max_height && div.css({
                    'max-height': p.max_height + 'px',
                    'overflow-y': (p.resizable ? overflowY : 'visible'),
                    'overflow-x': 'hidden'
                });
                if (p.height && p.height != 'auto') {
                    div.addClass('fixedHeightBody');
                    div.height(p.height - $('#' + p.selector + 'GridTableHeader').height());
                }

                div.append(tableContent);
                $('#' + p.wholeID).append(content.append(div));

                content.data(p.wholeID, g.createRowTemplate());
            },
            /**
             * 拓展列内容，实际上不显示，只是为了可以取到数据
             */
            expandColModel: function () {
                if (p.data.length > 0) {
                    if (!p.colModelCopy) {
                        p.colModelCopy = g._arrayCopy(p.colModel);
                    }
                    var d = p.data[0];
                    for (var key in d) {
                        if (d.hasOwnProperty(key)) {
                            var found = false;
                            for (var i = 0; i < p.colModelCopy.length; i++) {
                                if (p.colModelCopy[i].name == key) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                var line = {};
                                line.display = key;
                                line.name = key;
                                line.hide = true;
                                line.width = 0.1;
                                p.colModelCopy.unshift(line);
                            }
                        }
                    }
                }
            },

            /**
             * 自定义展开方式,
             */
            appendRow: function (index, dataList) {
                g.clearAppendRow();
                if (dataList.length <= 0) {
                    return;
                }
                g.createRows($(t).find("tr[index='" + index + "']"), dataList, "subset");
            },

            clearAppendRow: function () {
                $('#' + p.bodyID + '.GridTableBodyDiv tr[index="subset"]').each(function (i, row) {
                    var record = g.getData($(row));
                    g.map.remove(record[p.idProperty || p.colModelCopy[0].name]);
                    $(row).remove();
                });
                return true;
            },

            createRowTemplate: function () {
                var trContent = $("<tr/>").css({'height': p.line_height}).attr('index', '{{index}}');

                if (p.linePic) {
                    trContent.append($("<td/>").css('text-align', 'center'));
                }

                var shifting = 0;
                var rcols = 0;
                var colModel = p.colModelCopy;
                $.each(colModel, function (i) {
                    if (!this.hide) {
                        var counter = false;
                        if (p.columnFilter) {
                            if (p.columnFilter.indexOf(this.name) != -1) {
                                counter = true;
                            }
                        } else {
                            counter = true;
                        }
                        if (counter) {
                            rcols += (+this.colspan || 1);
                        } else {
                            shifting++;
                        }
                    }
                });
                if (p.clickSelect && p.isShowSelect) {
                    var td = $('<td/>').addClass('noResize')
                        .attr('width', p.line_height).attr('height', p.line_height).css('text-align', 'center');
                    var div = $('<div/>').css({
                        'width': p.line_height,
                        'height': p.line_height,
                        "position": "relative"
                    });
                    var cthch;
                    if (p.singleSelect) {
                        cthch = $('<input type="radio" />')
                            .attr('name', p.selector + '_single_' + (p.idProperty || p.colModelCopy[0].name));
                    } else {
                        cthch = $('<input type="checkbox"/>');
                    }
                    var m = (p.line_height - 18) / 2;
                    cthch.css({'margin': m}).addClass('BodyCheckBox');
                    var radioDiv = $("<div/>").css({
                        "position": "absolute",
                        "top": "0",
                        "left": "0",
                        "right": "0",
                        "bottom": "0"
                    });
                    div.append(radioDiv).append(cthch);
                    td.append(div);
                    trContent.append(td);
                }
                if (p.expand) {
                    var td = $('<td/>').addClass('noResize')
                        .attr('width', p.line_height).addClass('ExpandBox')
                        .attr('height', p.line_height).css('text-align', 'center');
                    var div = $('<div/>').css({
                        'width': p.line_height,
                        'height': p.line_height,
                        "position": "relative"
                    });
                    div.addClass('BodyExpand');
                    td.append(div);
                    trContent.append(td);
                }
                $.each(p.colModelCopy, function (i, t) {
                    var td = $("<td/>").addClass('GridItem').attr('data-width', t.width);
                    var div = $("<div/>").attr('name', t.name).addClass('BodyTdContent').attr('data-display', t.display).css({
                        'text-overflow': 'ellipsis',
                        'overflow': 'hidden',
                        'white-space': 'nowrap'
                    });

                    var w = g.calWidth(t.width, rcols);
                    if (i != p.colModelCopy.length - 1) { // TODO
                        td.attr('width', w);
                        //td.css('width', w);
                    } else {
                        p.resizable ? td.addClass('lastItem') : td.attr('width', w);
                        //div.css('width', '100%');
                    }

                    td.append(div);

                    t.css && div.css(t.css);
                    td.css('text-align', t.align || p.align);
                    if (t.type == 'image') {
                        td.html("<div class='trPicture'></div>");
                    }
                    if (t.hide || (p.columnFilter && p.columnFilter.indexOf(t.name) == -1)) {
                        td.addClass('hidden');
                    } else {
                        td.addClass('visibility');
                    }

                    trContent.append(td);
                });

                if (p.idProperty && $('.BodyTdContent[name="' + p.idProperty + '"]', trContent).length <= 0) {
                    trContent.before(
                        $("<td/>").addClass('GridItem hidden')
                            .append($("<div/>").attr('name', p.idProperty).addClass('BodyTdContent'))
                    );
                }

                return trContent.get(0).outerHTML;
            },

            createRow: function (record, index, type) {
                var trTemplate = $('#' + p.bodyID).data(p.wholeID);
                if (!trTemplate) return null;

                var trContent = $(trTemplate.replace(/\{\{(index)\}\}/g, index));

                trContent.addClass(index % 2 == 0 ? 'singleLine' : 'doubleLine');
                trContent.addClass('dataline');
                trContent.data('record', record);
                trContent.data('index', index);

                if (p.expand) {
                    var div = $('.BodyExpand', trContent);
                    var key = record[p.idProperty || p.colModelCopy[0].name];
                    if (key === undefined && (p.idProperty || p.colModelCopy[0].name))
                        key = eval('record.' + (p.idProperty || p.colModelCopy[0].name));
                    // TODO 1、当前行高亮， 2、刷新表格的响应和容器大小变化关系
                    div.click(function (e) {
                        if ($(this).hasClass('Disabled')) {
                            return;
                        }

                        if (p.expandIndex != null && p.expandIndex != undefined) {
                            var box = $(t).find("tr.child_" + p.expandIndex);
                            box.hasClass('expanded') && $.isFunction(p.fold) && p.fold(box, p.expandIndex);
                        }

                        $(this).parentsUntil('tr').parent().siblings('.child').removeClass('expanded');
                        $(this).parentsUntil('tr').parent().siblings('[index]').removeClass('intoExpand');
                        $(this).parentsUntil('tr').parent().siblings('[index]').find('.BodyExpand').removeClass('expanded');
                        if ($(this).hasClass('expanded')) {
                            p.expandIndex = null;
                            $(this).removeClass("expanded");
                            trContent.removeClass('intoExpand');
                            $(t).find("tr[index='child']").remove();
                            g.clearAppendRow();
                        } else {
                            p.expandIndex = key || index;
                            $(this).parentsUntil('tr').parent().siblings('.child_' + (key || index)).addClass('expanded');
                            $(this).addClass("expanded");
                            trContent.addClass('intoExpand');

                            var expandBox = $('div.expand-box', $(this).parentsUntil('tr').parent().siblings('.child_' + (key || index)));
                            $('div.expand-box', $(this).parentsUntil('tr').parent().siblings('.child')).empty();
                            if (g.clearAppendRow() && $.isFunction(p.expand)) {
                                p.expand(record, expandBox, index);
                            }
                        }
                        e.stopPropagation();
                    });
                    if (p.expandIndex == (key || index)) {
                        div.addClass('expanded');
                        trContent.addClass('intoExpand');
                    }
                    //如果没有expand按钮标记函数,或者函数判断当前行需要展开
                    if ($.isFunction(p.expandFlag) && !p.expandFlag(record)) {
                        div.remove();
                    }
                    //如果有expand按钮禁用标记函数,并且函数判断当前行需要禁用
                    if ($.isFunction(p.expandDisableFlag) && p.expandDisableFlag(record)) {
                        div.addClass('Disabled');
                    }
                }

                $.each(p.colModelCopy, function (i, t) {
                    var v = record[t.name];
                    if (v === undefined && t.name) v = eval('record.' + t.name);
                    var show = g.handleData(v) + g.handleData(this.unit);
                    var div = $('.BodyTdContent[name="' + t.name + '"]', trContent);

                    if (div.length == 0) return true;

                    div.html(show.replaceHTMLChar().replaceIllegalChar().replace(/「/g, '[').replace(/」/g, ']'));
                    div.parent().attr('title', show.replaceHTMLChar().replace(/「/g, '[').replace(/」/g, ']'));
                    div.attr('value', v);
                    div.data('value', v);

                    if (t.fnInit && $.isFunction(t.fnInit)) {
                        var cell = $('.BodyTdContent[name="' + t.name + '"][data-display="' + t.display + '"]', trContent);
                        cell.data('fnInit', t.fnInit);

                        var value = t.fnInit(cell, v, record, index, trContent);

                        if (value !== undefined) {
                            record[t.name] = value;
                            cell.attr('value', value);
                            cell.data('value', value);
                            cell.attr('title', value);
                            trContent.data('record', record);

                            if (g.map.contains(record[p.idProperty || p.colModelCopy[0].name])) {
                                g.map.put(record[p.idProperty || p.colModelCopy[0].name], record);
                            }
                        }
                    }
                });

                trContent.click(function () {
                    g.singleClickBodyLine(trContent, false);
                });
                trContent.dblclick(function () {
                    g.doubleClickBodyLine(trContent, true);
                });
                if (p.clickSelect && p.isShowSelect) {
                    $('.BodyCheckBox', trContent).click(function () {
                        this.checked = !this.checked;
                    });
                }

                return trContent;
            },

            createRows: function (context, dataList, type) {
                //console.time('createRows');
                if (!g.indexMap || type != "subset") g.indexMap = {};
                var dfd = $.Deferred();
                $.each(dataList, function (i, t) {
                    var trContent = g.createRow(t, i);
                    if (trContent) {
                        if (type == "subset") {
                            $(trContent).attr("index", "subset");
                            $(trContent).removeClass('singleLine').removeClass('doubleLine');
                            $(trContent).addClass(i % 2 == 0 ? 'singleLine' : 'doubleLine');
                            /*动态判断是否需要显示checkbox
                             $.isFunction(p.checkboxFlag) && !p.checkboxFlag(this) && $(trContent).find(".noResize input[type='checkbox']").css("visibility","hidden");*/
                            context.after(trContent);
                        } else {
                            g.indexMap[t[p.idProperty]] = i;
                            context.append(trContent);
                            p.expand && context.append(g.createExpandBox(t, i));
                        }
                    }
                });
                dfd.resolve();
                //console.timeEnd('createRows');
                return dfd.promise();
            },

            /**
             * 添加表格主体内容单元格数据
             */
            addData: function () {
                //console.time('addData');
                g.expandColModel();
                var tableContent = $('table', '#' + p.bodyID).eq(0);
                tableContent.empty();

                var render = function () {
                    var dfd = $.Deferred();
                    if (p.data.length <= 0) {
                        if (p.isShowNoData) {
                            var trContent = $("<tr/>").css({'height': p.line_height});
                            trContent.append($("<td/>").attr('colspan', (p.clickSelect && p.isShowSelect) ? p.col_num + 1 : p.col_num)
                                .css('text-align', 'center').html(Msg.reportTool.table[8]));
                            tableContent.append(trContent);
                            $('#' + p.bodyID).css('margin-top', '-1px');
                        }
                        dfd.resolve();
                    } else {
                        if (p.overflow && p.data.length * p.line_height > p.max_height) {
                            $('#' + p.headerID + ' .wrapper').css({'width': "100%"});
                        }
                        !p.singalLineHeaderTable && g.createMitlHeadeLayoutTR(tableContent, p.colModelCopy);
                        g.createRows(tableContent, p.data).done(function () {
                            dfd.resolve();
                        });
                    }
                    return dfd.promise();
                };

                if (!p.data || !(p.data instanceof Array)) {
                    return;
                }
                render().done(function () {
                    g.resizeBox();
                    g.loadSuccess();

                    Menu.hasElementRight();

                    var htrs = $('#' + p.headerID).find('tr:not(.child)');
                    var btrs = $('#' + p.bodyID).find('tr:not(.child)');

                    if (p.onLoadReady) {
                        p.onLoadReady(p.data, btrs, htrs, p.totalRecords, g.indexMap);
                    }
                    if (p.isSearchRecordSelected || p.isRecordSelected) {
                        var num = 0;
                        var values = g.map.getKeys() || [];
                        for (var i = 0; i < p.data.length; i++) {
                            var v = p.data[i][p.idProperty || p.colModelCopy[0].name];
                            if (v === undefined && (p.idProperty || p.colModelCopy[0].name))
                                v = eval('p.data[i].' + (p.idProperty || p.colModelCopy[0].name));
                            if (values.contains(v)) {
                                $(btrs[i]).addClass('SelectedBodyLine');
                                p.isShowSelect && ($(btrs[i]).find('.BodyCheckBox')[0].checked = true);
                                num++;
                            }
                        }
                        if (num && num == p.data.length && !p.singleSelect) {
                            $(htrs).addClass('SelectedHeaderLine');
                            p.isShowSelect && ($(htrs).find('.HeaderCheckBox')[0].checked = true);
                        }
                    }
                    //console.timeEnd('addData');
                });
            },

            /**
             * 创建展开内容
             * @param record
             * @param i
             * @returns {*|jQuery}
             */
            createExpandBox: function (record, i) {
                var index = record[p.idProperty || p.colModelCopy[0].name];
                if (index === undefined && (p.idProperty || p.colModelCopy[0].name))
                    index = eval('record.' + (p.idProperty || p.colModelCopy[0].name));
                var trContent = $("<tr/>").addClass('child child_' + (index || i));
                var colspan = p.col_num + 1;
                (p.clickSelect && p.isShowSelect) && colspan++;
                var tdContent = $("<td/>").attr('colspan', colspan);
                var content = $("<div/>").addClass('expand-box').css({
                    'height': p.expandBoxHeight,
                    'min-height': p.line_height
                });

                tdContent.append(content);
                trContent.append(tdContent);

                if (p.expandIndex == (index || i)) {
                    trContent.addClass('expanded');
                    if ($.isFunction(p.expand)) {
                        p.expand(record, content, i);
                    }
                }

                return trContent;
            },
            /**
             * 对单元格数据进行特殊处理
             */
            handleData: function (data) {
                if (data == null) return '';
                return data;
            },
            /**
             * 单击表格头行的处理函数
             */
            singleClickHeaderLine: function (trContent, doubleClick) {
                if (!p.clickSelect || p.singleSelect) return;
                var checkbox = trContent.find('.HeaderCheckBox')[0];
                if (doubleClick) {
                    trContent.addClass('SelectedHeaderLine');
                    checkbox.checked = true;
                } else {
                    trContent.toggleClass('SelectedHeaderLine');
                    checkbox.checked = !checkbox.checked;
                }

                if (trContent.hasClass('SelectedHeaderLine')) {
                    $('#' + p.bodyID + '.GridTableBodyDiv tr:not(.child)').each(function (i, row) {
                        g.singleClickBodyLine($(row), true);
                    });
                } else {
                    $('#' + p.bodyID + '.GridTableBodyDiv tr:not(.child)').each(function (i, row) {
                        g.singleClickBodyLine($(row), false);
                    });
                }
            },
            /**
             * 双击表格头行的处理函数
             */
            doubleClickHeaderLine: function (trContent) {
                g.singleClickHeaderLine(trContent, true);
            },
            /**
             * 单击表格主体行的处理函数
             *
             * @param trContent 操作行
             * @param doubleClick 是否是双击操作
             */
            singleClickBodyLine: function (trContent, doubleClick) {
                if (!p.clickSelect) return;
                if (p.singleSelect) {
                    g.map.clear();
                    trContent.siblings().each(function (i, row) {
                        $(row).removeClass('SelectedBodyLine');
                    });
                }
                var checkbox = trContent.find('.BodyCheckBox')[0];
                if (checkbox) {
                    if (doubleClick) {
                        trContent.addClass('SelectedBodyLine');
                        checkbox.checked = true;
                    } else {
                        if (p.singleSelect) {
                            checkbox.checked = trContent.hasClass('SelectedBodyLine');
                        }
                        trContent.toggleClass('SelectedBodyLine', !checkbox.checked);
                        checkbox.checked = !checkbox.checked;
                    }
                } else {
                    trContent.toggleClass('SelectedBodyLine', !trContent.hasClass('SelectedBodyLine'));
                }
                if (p.onSingleClick) {
                    p.onSingleClick(trContent, g.getData(trContent), trContent.hasClass('SelectedBodyLine'));
                }
                g.storageSelected(trContent);
                if (!p.singleSelect) {
                    var htrs = $('#' + p.headerID).find('tr:not(.child)');
                    var btrs = $('#' + p.bodyID).find('tr td.noResize input.BodyCheckBox');
//                    	$('#' + p.bodyID).find('tr:not(.child)');
                    var bstrs = $('.SelectedBodyLine', $('#' + p.bodyID));
                    if (bstrs.length && bstrs.length == btrs.length) {
                        htrs.addClass('SelectedHeaderLine');
                        htrs.find('.HeaderCheckBox')[0].checked = true;
                    } else {
                        htrs.removeClass('SelectedHeaderLine');
                        htrs.find('.HeaderCheckBox')[0].checked = false;
                    }
                }
            },
            /**
             * 双击表格主体行的处理函数
             */
            doubleClickBodyLine: function (trContent) {
                g.singleClickBodyLine(trContent, true);
                if (p.onDoubleClick) {
                    p.onDoubleClick(trContent, g.getData(trContent), trContent.hasClass('SelectedBodyLine'));
                }
            },
            /**
             * 记录选中项
             * @param trContent 操作行
             */
            storageSelected: function (trContent) {
                var record = g.getData(trContent);
                if (trContent.hasClass('SelectedBodyLine')) {
                    g.map.put(record[p.idProperty || p.colModelCopy[0].name], record);
                }
                else {
                    g.map.remove(record[p.idProperty || p.colModelCopy[0].name]);
                }
                p.showSelectedName && g.refreshSelectedShowBox();
            },
            /**
             * 刷新选中项展示框
             */
            refreshSelectedShowBox: function () {
                $('.selectedShowBox', $(t)).empty();
                var values = g.map.getValues() || [];
                for (var i = 0; i < values.length; i++) {
                    var name = p.idProperty || p.colModelCopy[0].name;
                    var text = values[i][p.showSelectedName || name];
                    var key = values[i][name];

                    if (text) {
                        var item = $('<li>').attr('title', text);
                        item.append($('<div>').addClass('t').text(text));
                        item.append($('<div>x</div>').addClass('close').click((function (item, key) {
                            return (function () {
                                g.map.remove(key);
                                item.remove();
                                g.addData();
                            });
                        })(item, key)));
                        $('.selectedShowBox', $(t)).append(item);
                    }
                    if (i == values.length - 1) {
                        $('.selectedShowBox', $(t)).append('<div class="clear"></div>')
                    }
                }
            },
            /**
             * 选中项记录 JSON 数据 —— 类Map操作
             */
            map: {
                put: function (key, value) {
                    for (var i = 0; i < p.selectedRecords.length; i++) {
                        if (p.selectedRecords[i].key === key) {
                            p.selectedRecords[i].value = value;
                            return;
                        }
                    }
                    p.selectedRecords.push({'key': key, 'value': value});
                },
                remove: function (key) {
                    for (var i = 0; i < p.selectedRecords.length; i++) {
                        var v = p.selectedRecords.pop();
                        if (v.key === key) {
                            continue;
                        }
                        p.selectedRecords.unshift(v);
                    }
                },
                /**
                 * 是否包含指定 key 的元素
                 */
                contains: function (key) {
                    for (var i = 0; i < p.selectedRecords.length; i++) {
                        if (p.selectedRecords[i].key === key) {
                            return true;
                        }
                    }
                    return false;
                },
                getKeys: function () {
                    var resultArr = [];
                    for (var i = 0; i < p.selectedRecords.length; i++) {
                        var v = p.selectedRecords[i];
                        resultArr.push(v.key);
                    }
                    return resultArr;
                },
                getValues: function () {
                    var resultArr = [];
                    for (var i = 0; i < p.selectedRecords.length; i++) {
                        var v = p.selectedRecords[i];
                        resultArr.push(v.value);
                    }
                    return resultArr;
                },
                clear: function () {
                    p.selectedRecords = [];
                }
            },
            /**
             * 获取表格行的一行数据，返回json格式
             */
            getData: function (trContent) {
                var record = trContent.data('record') || {};
                trContent.find('.BodyTdContent').each(function () {
                    var value = $(this).data('value');
                    if (value !== undefined) {
                        record[$(this).attr('name')] = value;
                    }
                });
                return record;
            },
            /**
             * 设置单元格的值
             * @param cell 指定单元格
             * @param value 值
             */
            setCellData: function (cell, value, isReInit) {
                var trContent = $(cell).parents('tr').eq(0);
                var name = $(cell).attr('name');
                var record = trContent.data('record') || {};

                if (isReInit) {
                    var fnInit = $(cell).data('fnInit');
                    if (fnInit && $.isFunction(fnInit)) {
                        fnInit($(cell), value, record, trContent.data('index'), trContent);
                    }
                }

                if (name) {
                    record[name] = value;
                    trContent.data('record', record);

                    if (g.map.contains(record[p.idProperty || p.colModelCopy[0].name])) {
                        g.map.put(record[p.idProperty || p.colModelCopy[0].name], record);
                    }
                }

                $(cell).data('value', value);

                return $(cell);
            },
            /**
             * 设置某行的值
             * @param record 指定单元格
             * @param np 值
             */
            setRowData: function (record, np, isReInit) {
                var index = g.indexMap[record[p.idProperty]];
                if (!index && index != 0) return false;
                var trContent = $('#' + p.bodyID).find('tr[index=' + index + ']');
                $.each(p.colModel, function (i, t) {
                    var name = t.name;
                    if (np) {
                        if (($.isArray(np) && $.inArray(name, np) >= 0) || np == name) {
                            return true;
                        }
                    }
                    var value = record[name];
                    var cell = trContent.find('.BodyTdContent[name="' + name + '"]').eq(0);
                    if (isReInit) {
                        var fnInit = $(cell).data('fnInit');
                        if (fnInit && $.isFunction(fnInit)) {
                            fnInit($(cell), value, record, trContent.data('index'), trContent);
                        }
                    }
                    var oldValue = $(cell).data('value');
                    record[name] = value;
                    if (oldValue != value) {
                        $(cell).attr('value', value);
                        $(cell).data('value', value);
                        $(cell).parent('title', value);
                    }

                });
                trContent.data('record', record);
                if (g.map.contains(record[p.idProperty || p.colModelCopy[0].name])) {
                    g.map.put(record[p.idProperty || p.colModelCopy[0].name], record);
                }
                return trContent;
            },
            /**
             * 创建分页工具条
             */
            pageBar: function () {
                g.addToolBar();
            },
            /**
             * 添加表格分页工具条
             */
            addToolBar: function () {
                var barDiv = $("<div />");
                barDiv.attr("id", p.pageBarID);
                barDiv.attr("class", "PageToolBar");
                barDiv.css({'height': p.toolBarHeight});

                $('#' + p.selector).append(barDiv);
                p.toolBar == 'hide' ? barDiv.hide() : 0;
                g.addBarTable(barDiv);
            },
            /**
             * 添加表格分页工具条框架
             */
            addBarTable: function (barDiv) {
                var tableContent = $("<div/>").addClass('GridTableBarBody');
                barDiv.append(tableContent);
                g.addToolBarContent(tableContent, 'Left');
                g.addToolBarContent(tableContent, 'Right');
            },
            /**
             * 添加表格分页工具条中的具体内容
             */
            addToolBarContent: function (trBarContent, type) {
                var content = [];
                var align = '';
                if (type == 'Left') {
                    content = p.leftContent;
                    align = "left";
                } else if (type == 'Right') {
                    content = p.rightContent;
                    align = "right";
                }
                var pan = $('<p/>').css('float', align).addClass('GridTableToolBarBody' + type);
                $.each(content, function (index, d) {
                    var input = $("<" + d.input + " />");
                    d.name && input.attr('class', d.name);
                    pan.append(input);
                    d.ids && input.attr('id', p.pageBarID + d.ids);
                    if (d.type == 'label') {
                        if (d.fix) {
                            input.css({'white-space': 'nowrap', 'text-overflow': 'ellipsis'});
                            d.show && input.html(d.show);
                        } else {
                            d.show && input.html(p[this.show]);
                            input.attr('size', '4');
                        }
                    } else if (d.type == 'select') {
                        d.width && input.css({"width": d.width});
                        d.height && input.css({"height": d.height});
                        var data = p[d.show];
                        for (var i = 0; i < data.length; i++) {
                            var option = $("<option />");
                            input.append(option);
                            option.val(data[i]);
                            option.html(data[i])
                        }
                    } else if (d.type == 'text') {
                        d.width && input.css({"width": d.width});
                        d.height && input.css({
                            "height": d.height, "text-align": "center"
                        });
                        d.show && input.val(p[d.show]);
                    } else if (d.type == 'button') {
                        input.css({
                            'vertical-align': 'middle',
                            'display': 'inline-block'
                        }).addClass("pbutton_on");
                        d.show && input.val(p[d.show]);
                        d.width && input.css({"width": d.width});
                        d.height && input.css({"height": d.height});
                        var div = $("<div/>");
                        d.ids && div.attr("class", d.ids);
                        d.width && div.css({"width": '100%'});
                        d.height && div.css({"height": '100%'});
                        input.append(div.text(d.show));
                    }
                    d.left && input.before($("<label/>").css("width", d.left));
                    d.right && input.after($("<label/>").css("width", d.right));
                });
                trBarContent.append(pan);
            },
            /**
             * 添加表格分页工具条中的事件响应
             */
            initEvents: function () {
                $('#' + p.pageBarID + "pselect_rps").change(function (data) {
                    p.currentPage = 1;
                    p.rp = p.rps[data.delegateTarget.selectedIndex];
                    g.refreshPage();
                });
                $('#' + p.pageBarID + "pbutton_first").click(function () {
                    if (p.currentPage != 1) {
                        p.currentPage = 1;
                        g.refreshPage();
                    }
                });
                $('#' + p.pageBarID + "pbutton_previous").click(function () {
                    if (p.currentPage > 1) {
                        p.currentPage -= 1;
                        g.refreshPage();
                    }
                });
                $('#' + p.pageBarID + "pbutton_next").click(function () {
                    if (p.currentPage < p.totalPage) {
                        p.currentPage = parseInt(p.currentPage) + 1;
                        g.refreshPage();
                    }
                });
                $('#' + p.pageBarID + "pbutton_last").click(function () {
                    if (p.currentPage != p.totalPage) {
                        p.currentPage = p.totalPage;
                        g.refreshPage();
                    }
                });
                $('#' + p.pageBarID + "ptext_toPage").keydown(function (event) {
                    if (event.keyCode == 13) {
                        g.jumpToPage();
                    }
                });
                $('#' + p.pageBarID + "pbutton_jumpTo").click(function () {
                    g.jumpToPage();
                });
                g.initToolBarSelect();
            },
            /**
             * go按钮跳转函数
             */
            jumpToPage: function () {
                var reg = /^[0-9]*[1-9][0-9]*$/;
                var pageS = $('#' + p.pageBarID + "ptext_toPage").val();
                if (reg.test(pageS)) {
                    pageS = parseInt(pageS);
                    if (pageS < 1) {
                        pageS = 1;
                    } else if (pageS > p.totalPage) {
                        pageS = p.totalPage;
                    } else if (pageS == p.currentPage) {
                        return;
                    }
                } else {
                    $('#' + p.pageBarID + "ptext_toPage").val(p.currentPage);
                    return;
                }
                p.currentPage = pageS;
                g.refreshPage();
            },
            /**
             * 初始化表格分页工具条中的select数据
             */
            initToolBarSelect: function () {
                var index = p.rps.indexOf(p.rp);
                if (index > -1) {
                    $('#' + p.pageBarID + "pselect_rps" + ' option:eq(' + index + ')').attr('selected', 'true');
                } else {
                    p.rp = p.rps[0];
                }

            },
            /**
             * 刷新表格整体内容
             */
            refreshPage: function (f) {
                if (f || !p.isRecordSelected) {
                    g.map.clear();
                }
                g.changeToolBarButtonStstus();
                g.changeParams();
                $.each($('#' + p.headerID + ' .GridTableHeaderTH'), function (i) {
                    var input = $(this).find('input[type=checkbox]');
                    if (input.length > 0) {
                        input[0].checked = false;
                        $(this).removeClass('SelectedHeaderLine');
                    }
                });
                $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.procMsg);
                if (p.url) {
                    p.params.index = p.params.page;
                    $.http.post(p.url, p.params, function (data) {
                        if (data.success) {
                            if (p.loadReady) {
                                data.data = p.loadReady(data.data) || data.data;
                            }
                            if (data.data) {
                                p.totalRecords = data.data[p.total] ? data.data[p.total] : 0;
                                p.data = data.data[p.list] ? data.data[p.list] : [];
                            } else {
                                $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                                p.totalRecords = 0;
                                p.data = [];
                            }
                        } else {
                            $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                            if (p.loadError) {
                                p.loadError(data.data);
                            }
                            p.totalRecords = 0;
                            p.data = 0;
                        }
                        g.addData();
                    }, function (data) {
                        $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                        if (p.loadError) {
                            p.loadError(data.data || data);
                        }
                        p.totalRecords = 0;
                        p.data = [];
                        g.addData();
                    });
                } else if (p.data) {
                    var temp = {};
                    temp.list = p.data.slice();
                    temp.total = temp.list.length;
                    var data = {};
                    data.data = temp;
                    if (data.data) {
                        if (p.loadReady) {
                            data.data = p.loadReady(data.data) || data.data;
                        }
                        p.totalRecords = data.data[p.total] ? data.data[p.total] : 0;
                        p.data = data.data[p.list] ? data.data[p.list] : [];
                    } else {
                        $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                        p.totalRecords = 0;
                        p.data = [];
                    }
                    if (p.prototypeSort && $.isFunction(p.prototypeSort)) {
                        p.data.sort(p.prototypeSort(p.params['orderBy'], p.params['sort']))
                    }
                    p.data = p.pageBar?p.data.slice((p.currentPage - 1) * p.rp, p.rp * p.currentPage):p.data;//是否有页面分页工具
                    g.addData();
                } else {
                    setTimeout(function () {
                        if (p.totalRecords == 0) {
                            $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                        } else {
                            $('#' + p.pageBarID + "plabel_totalRecords")
                                .html(String.format(Msg.gridParam.displayMsg, p.totalRecords));
                        }
                    }, 100);
                }
            },
            /**
             * 后台数据获取成功，的回调函数
             */
            loadSuccess: function () {
                p.totalPage = Math.ceil(p.totalRecords / p.rp);
                p.totalPage = p.totalPage ? p.totalPage : 1;
                g.changeToolBarButtonStstus();
                $('#' + p.pageBarID + "plabel_currentPage1").html(p.currentPage);
                $('#' + p.pageBarID + "plabel_currentPage2").html(p.currentPage);
                $('#' + p.pageBarID + "plabel_totalPage").html(p.totalPage);
                $('#' + p.pageBarID + "ptext_toPage").val(p.currentPage);
                if (p.totalRecords == 0) {
                    $('#' + p.pageBarID + "plabel_totalRecords").html(Msg.gridParam.emptyMsg);
                } else {
                    $('#' + p.pageBarID + "plabel_totalRecords")
                        .html(String.format(Msg.gridParam.displayMsg, p.totalRecords));
                }
            },
            /**
             * 根据当前页和总页数改变工具条按钮状态
             */
            changeToolBarButtonStstus: function () {
                $(".pbutton", "#" + p.pageBarID).addClass("pbutton_on");
                $(".pbutton", "#" + p.pageBarID).removeClass("pbutton_dis");
                if (p.currentPage == 1) {
                    $('#' + p.pageBarID + "pbutton_first").removeClass("pbutton_on").addClass("pbutton_dis");
                    $('#' + p.pageBarID + "pbutton_previous").removeClass("pbutton_on").addClass("pbutton_dis");
                }
                if (p.currentPage == p.totalPage) {
                    $('#' + p.pageBarID + "pbutton_next").removeClass("pbutton_on").addClass("pbutton_dis");
                    $('#' + p.pageBarID + "pbutton_last").removeClass("pbutton_on").addClass("pbutton_dis");
                }
            },
            /**
             * 每次刷新前修改参数内容
             */
            changeParams: function () {
                p.params.page = p.currentPage;
                p.params.pageSize = p.rp;
            }
        };

        g.init();
        t.grid = g;
        t.p = p;
        return true;
    };

    var docLoaded = false;
    $(document).ready(function () {
        docLoaded = true;
    });

    $.fn.extend({
        /**
         * 绘制/初始化 表格
         */
        GridTable: function (p) {
            return this.each(function () {
                if (!docLoaded) {
                    $(this).hide();
                    var t = this;
                    $(document).ready(function () {
                        addGrid(t, p);
                    });
                } else {
                    addGrid(this, p);
                }
            });
        },

        /**
         * 扩展结点查询事件
         */
        GridTableSearch: function (p) {
            return this.each(function () {
                if (this.grid) {
                    this.p = $.extend(this.p, p);
                    this.p.currentPage = 1;
                    p && p.data ? this.grid.addData() : this.grid.refreshPage(!this.p.isSearchRecordSelected);
                }
            });
        },

        GridTableSearchpData: function (p) {
            return this.each(function () {
                if (this.grid) {
                    this.p = $.extend(this.p, p);
                    this.p.currentPage = 1;
                    this.grid.addData();
                }
            });
        },

        /**
         * 获取选中记录的JSON原型格式
         */
        GridTableSelectedRecords: function () {
            var records = [];
            if (this[0] && this[0].grid)
                records = this[0].grid.map.getValues();
            return records;
        },

        /**
         * 初始化选中记录
         */
        GridTableInitSelectedRecords: function (records) {
            return this.each(function (i, g) {
                if (g && g.grid && records) {
                    $.each(records, function (n, t) {
                        g.grid.map.put(t[g.p.idProperty || g.p.colModel[0].name], t);
                    });
                    g.grid.refreshSelectedShowBox();
                    //g.grid.addData();
                }
            });
        },

        /**
         * 刷新表格
         */
        GridTableReload: function (p) {
            return this.each(function () {
                if (this.grid && p) $.extend(this.p, p);
                this.p.totalRecords = 0,
                    this.p.currentPage = 1,
                    this.p.totalPage = 1,
                    this.p.toPage = 1,
                    this.grid.refreshPage(true, true);
            });
        },

        /**
         * 刷新表格在当前页不做页面变更.但是查询是不能掉该方法的
         */
        GridTableRefreshPage: function (p) {
            return this.each(function () {
                if (this.grid && p) $.extend(this.p, p);
                this.grid.refreshPage(true);
            });
        },

        /**
         * 更新指定单元格的值
         * @param cell {{id: {*} 单元格所在记录的idProperty属性的值, index: {number} 单元格所在的行号, name: {string} 单元格所在的属性名 } | {jQuery}}
         * @param value
         * @param callback
         * @param isReInit {boolean} 是否重新初始化单元格
         */
        GridTableUpdateCell: function (cell, value, callback, isReInit) {
            isReInit = isReInit == undefined ? true : isReInit;
            return this.each(function () {
                var self = this;
                if (!(cell instanceof jQuery) || !((typeof HTMLElement === 'object') ?
                        function (obj) {
                            return obj instanceof HTMLElement;
                        } :
                        function (obj) {
                            return obj && typeof obj === 'object' && obj.nodeType === 1 && typeof obj.nodeName === 'string';
                        })) {
                    var index = cell.index || self.grid.indexMap[cell.id], name = cell.name;
                    if (index === undefined) {
                        $.each(self.p.data, function (i, t) {
                            if (t[self.p.idProperty || self.p.colModel[0].name] == cell.id) {
                                index = i;
                                return true;
                            }
                        });
                    }
                    cell = $('#' + self.p.bodyID).find('tr[index=' + index + ']').find('.BodyTdContent[name="' + name + '"]');
                }
                self.grid.setCellData(cell, value, isReInit);
                callback && callback instanceof Function && callback(cell);
            });
        },
        /**
         * 更新当前表格的内容 有个必备条件 idProperty 开始必须配置 且正确
         * @param data 原始数据
         * @param np 原始数据
         * @param callback
         * @param isReInit {boolean} 是否重新初始化单元格
         */
        GridTableUpdateRow: function (data, np, callback, isReInit) {
            isReInit = isReInit == undefined ? true : isReInit;
            return this.each(function () {
                var self = this;
                if (!data || !$.isArray(data)) {
                    callback && callback instanceof Function && callback(data, false);
                } else {
                    $.each(data, function (t, e) {
                        self.grid.setRowData(e, np, isReInit);
                    });
                    callback && callback instanceof Function && callback(data, true);
                }
            });
        },
        /**
         * 获取当前页页码
         */
        GridTableCurPage: function () {
            var curPage = 1;
            if (this[0] && this[0].grid)
                curPage = this[0].grid.p.currentPage;
            return curPage;
        },

        /**
         * 新增结构相同的行, expand的另一种方式
         */
        GridTableAppendRow: function (index, dataList) {
            this[0].grid.appendRow(index, dataList);
        }
    });

    return $;
});