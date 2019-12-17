/**
 * 扩展树节点
 * 调用方法 var tree = $('.class').easyTree({callback:function(data,level)....}); 从新加载数据可以使用 tree.setDatas(datas);
 */
;(function (factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery', 'css!plugins/easyTree/css/easyTree.css'], factory);
    } else if (typeof module !== 'undefined') {
        module.exports = factory(require('jquery'));
    } else {
        factory(jQuery);
    }
})(function ($) {

    function TreeObject(ele, opts) {
        this.$element = ele;
        this.options = $.extend({
            type: 'treetype', //自定义属性的名称
            datas: [],
            callback: null, //点击树节点的回掉函数,这里的回调函数只有对于叶子节点才执行，对于非叶子节点就不执行
            eleWidth: 200,//宽度
            eleMinWidth: 50,//最小宽度
            iconABackClass: 'icon_a_back',// a背景图片的样式
            hasHidden: true,// 是否有隱藏的按钮，默认有隐藏按钮
            hasIcon: true,//是否有背景图片
            isDialog: false,//是否是弹出框,如果是弹出框，就不会去添加鼠标移动上去将弹出框隐藏的事件
            parentTree: undefined,// 如果是对话框就有弹出他的树节点
            isLoadChildren: false,//是否加载这个节点的子节点 默认是不加载的
            imgLength: 5,// 一共有多少张图片
            iconHeight: 34,//背景图片的高度
            isExpand: true,//是否是展开，false：折叠  true 展开 是否展开（针对有隐藏按钮的情况下，将树节点折叠为缩列图）,默认是展开的
            isSelect: false,//是否选中第一个，true选中 false：不选中
        }, opts);
        //this.$element.css('width',this.options.eleWidth+"px");
        this.$hiddenBtn = undefined;// 隐藏按钮
        this.isOpen = true;//当前的节点是否是展开更加宽
        this.select = undefined;//当前选中的
    }

    //扩展属性
    TreeObject.prototype = {
        /**
         * 初始树节点的方法
         */
        init: function () {
            this.loadDatas(this.options.datas);
            //初始事件
            return this;
        },
        /**
         * 设置数据，通常给外面的方法使用，如果外面的树节点的内容改变了，就使用这个调用
         * @param {Object} datas 树节点的数据，是一个数组，里面应该包含数据有=》 如下[
         *    {name:显示名称(必须有),  content:加载的html的名称(不包含.html)可以没有内容 ,id:数据在数据库中的id,children:[基本与datas数据相同]可以没有children是叶子节点},......
         * ]
         */
        setDatas: function (datas) {
            this.options.datas = datas && $.isArray(datas) && datas.length > 0 && datas || [];//如果满足就设置值，否则就设置为空
            return this.loadDatas(this.options.datas);
        },
        //加载预制时候的数据
        loadDatas: function (datas) {
            var self = this;
            self.$element.empty();// 清空内容
            if (!datas || !$.isArray(datas) || datas.length == 0) {
                console.log('no data to add tree');
                self.$element.html('没有任何需要加载的数据');
                return this;
            }
            // TODO 1.加载隐藏按的事情
            self.addHiddenBtn();
            // 2.加载数据
            self.realLoadDatas(datas);
            //TODO 3.加载完成后初始化事件
            self.initEvent();
            return self;
        },
        /**
         * 添加隐藏按钮的元素
         */
        addHiddenBtn: function () {
            var self = this;
            if (self.options.hasHidden) {//有隐藏按钮
                if (!this.$hiddenBtn) {
                    this.$hiddenBtn = $('<div class="easy_tree_menu"><div class="easy_tree_title">菜单</div><div class="hideen_btn"></div></div>');
                }
                self.$element.append(this.$hiddenBtn);
            } else {
                if (this.$hiddenBtn) {
                    this.$hiddenBtn = undefined;
                }
            }
        },
        /**
         * 真正的加载数据的方法
         * @param {Object} datas 需要加载的数据，是一个数组对象
         */
        realLoadDatas: function (datas) {
            //判断是否加载子节点
            var self = this;
            if (self.options.isLoadChildren) {//加载子节点
                self.loadAllChildrenDatas(datas, 0, 0);//最外层没有父节点
            } else {//只加载当前节点，不加载里面的子节点
                self.loadNoExcent(datas, 0);
            }
            return this;
        },
        /**
         * 只加载父节点的数据
         * @param {Object} datas
         */
        loadNoExcent: function (datas, level) {
            var self = this;
            var $ul = $('<ul></ul>');//判断是否加载图片的方式
            self.$element.append($ul);
            // 最外层的绘制，需要加载图片，这里图片就使用已经有了的,目前的需求是只有第一个需要图片，其他的都不行
            var hasImg = (self.options.hasIcon && level == 0);
            var imgLength = self.options.imgLength;
            var iconABackClass = this.options.iconABackClass;
            var iconHeight = this.options.iconHeight;
            var isSelect = this.options.isSelect;
            level++;//这里使用之后就加一，方便后面使用
            for (var i = 0; i < datas.length; i++) {
                var isSelect2 = isSelect && i == 0;
                var data = datas[i];
                var parentId = data.parendId || 0;//加载父节点
                var $tmpLi = $('<li ' + this.options.type + '="' + level + '" class="clear tree_name_info" id="' + data.id + '" parentId="' + parentId + '"></li>');
                var aIcon;
                if (hasImg) {
                    aIcon = $('<a style="background-position-y: -' + iconHeight * (i % imgLength) + 'px;" class="' + iconABackClass + '"></a><span class="text_show_btn useHidden">' + data.name + '</span>');
                } else {
                    aIcon = $('<span>' + data.name + '</span>');
                }
                $tmpLi.append(aIcon);
                $ul.append($tmpLi);

                var $moueOverDiv = $('<div class="easy_tree_mouseover"></div>');//鼠标移动上去后显示的树节点
                $tmpLi.append($moueOverDiv);
                if (data.children && $.isArray(data.children) && data.children.length > 0) {//对于叶子节点就不需要展开了
                    //鼠标移动上去的弹出框 开始
                    $moueOverDiv.easyTree({
                        "callback": self.options.callback, "datas": data.children,
                        hasIcon: false, hasHidden: false, isDialog: true, parentTree: self, isSelect: isSelect2
                    });
                    //鼠标移动上去的弹出框 结束
                } else if (isSelect2) {
                    this.select = $tmpLi;
                }
                ($tmpLi[0])['treeData'] = data;//数据
            }
            return self;
        },
        /**
         * //加载所有的子节点
         * @param {Object} datas
         */
        loadAllChildrenDatas: function (datas, level, parentId) {//TODO

        },
        initEvent: function () {//初始事件
            var self = this;
            //加载鼠标事件 进入显示抽屉的 开始
            self.$element.find('ul li').hover(function (event) {
                var $this = $(this);
                var $tmpDiv = $this.children('.easy_tree_mouseover');
                if ($tmpDiv && $tmpDiv.length > 0) {
                    $tmpDiv.removeClass('easy_tree_hide').addClass('easy_tree_show');
                    var pos = $this.offset();
                    var eleWidth = self.options.eleWidth;//当前元素的宽度
                    if (!$this.parent().parent().hasClass('easy_tree_mouseover')) {
                        eleWidth = $this.outerWidth();
                    }
                    $tmpDiv.css({
                        'top': pos.top,
                        'left': pos.left + eleWidth
                    });
                }
            }, function (event) {
                var $tmpDiv = $(this).children('.easy_tree_mouseover');
                $tmpDiv.removeClass('easy_tree_show').addClass('easy_tree_hide');
            });
            //加载鼠标事件 进入显示抽屉的 结束

            //添加对叶子节点的点击事件并且调用回调函数
            self.$element.find('ul li').off('click').on('click', function (event) {
                var data = this['treeData'];
                var children = data.children && $.isArray(data.children) || [];
                if (children.length == 0) {//当前点击的是叶子节点
                    self.options.callback && self.options.callback(this, data);//当前的元素和当前元素的事情
                    self.editorStyle($(this), true);//需要移除之前的样式
                } else {
                    console.log('当前点击的不会做事情');
                    //self.editorStyle($(this),false);//需要移除之前的样式
                }
                event.stopPropagation();//阻止事件冒泡
            });

            //添加隐藏和显示的事件
            if (self.$hiddenBtn) {//有隐藏按钮,添加他的事件
                self.$hiddenBtn.off('click').on('click', function (event) {
                    self.isOpen = !self.isOpen;
                    var spans = $('.easy_tree_parent').children('ul').children('li').children('span');//获取所有的显示文字的span对象
                    if (self.isOpen) {
                        $('.easy_tree_title').show();
                        $('.easy_tree_parent').removeClass('easy_tree_is_close');
                        $(this).find('.hideen_btn').removeClass('easy_tree_close');
                        spans.show();
                    } else {
                        $('.easy_tree_title').hide();
                        $('.easy_tree_parent').addClass('easy_tree_is_close');
                        $(this).find('.hideen_btn').addClass('easy_tree_close');
                        spans.hide();
                    }
                });
            }
            //判断是否需要有内容需要默认选中
            if (self.options.isSelect && self.select) {
                self.select.click();
            }
            //判断开始的时候是否需要折叠
            if (!self.options.isExpand && self.$hiddenBtn) {//如果是折叠的，就需要调用一下
                self.$hiddenBtn.click();
            }

            return self;
        },
        /**
         * 修改样式
         * @param {Object} ele
         * @param {Object} isRemove 是否移除class
         *
         */
        editorStyle: function (ele, isRemove) {
            if (isRemove) {
                $('.easy_tree_select').removeClass('easy_tree_select');//移除样式
            }
            ele.addClass('easy_tree_select');
            var parentId = ele.attr('parentId');
            if (parentId && parentId != 0) {
                this.editorStyle($('#' + parentId), false);//递归调用
            }
        }

    };

    /**
     * 扩展jquery对象的方法 简单的树节点的方法
     * @param {Object} opts
     */
    $.fn.easyTree = function (opts) {
        var treeObj = new TreeObject(this, opts || {});
        treeObj.init();
        return treeObj;
    };

});