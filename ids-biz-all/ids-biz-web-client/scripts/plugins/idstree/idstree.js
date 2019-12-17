define(['jquery', 'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function ($) {
    /**
     * 获取选中的节点值 包括未展开的节点（但是父节点是全选状态的）
     * @param idName 需获取的属性 默认是id
     * @param filterFun 过滤节点的方法 此方法不传默认使用 options中的filterFun，若options中未配置，则只要选中就返回
     * @returns {*} 只获取选中可使用ztree的原生方法
     */
    $.fn.idstreeSelected = function (idName, filterFun) {
        return $(this).idstree('idstreeSelected', idName, filterFun);
    };
    /**
     * 获取选中节点数据对象
     * @param filterFun
     * @returns {Object}
     */
    $.fn.idstreeSelectedObj = function (filterFun) {
        return $(this).idstree('idstreeSelectedObj', filterFun);
    };

    /**
     *
     * @returns {jQuery}
     */
    $.fn.idstreeBackinitTree = function () {
        return $(this).idstree('idstreeBackinitTree');
    };

    /**
     * type 0 修改(只针对名称，父id修改) 1添加  -1 删除 其它不处理
     * node 节点数组
     */
    $.fn.idstreeProtoDataChange = function (node, type) {
        return $(this).idstree('idstreeProtoDataChange', node, type);
    };

    /**
     * 在Ztree的基础上，添加配置 使可支持批量数据时，可前端实现自动加载已经搜索的功能
     */
    (function ($) {
        $.widget("ids.tree", {
            //   options._setting ztree 的 setting 配置 必选
            /**
             * 若外面 使用ztree 的expandNode 方法
             * 则需要保证  callbackFlag(是否触发expand回调方法) 参数为true 第5个参数
             *           sonSign(false 只影响此节点，对于其子孙节点无任何影响) 参数为false 第3个参数
             * 例如 treeObj.expandNode(nodes[0], true, false, true,true);
             */
            options: {
                formatNode: false, //节点的format 处理 单个单个节点处理 若不需要此节点 放回null 需要的话 处理后 返回对应对象 入参数是Node
                searchInput: false,//搜索的框的 jquery对象
                searchRecordSelected: false,//搜索时是否记录之前的选中节点 默认是false
                maxHeight: 300,//protoScrollAuto 为 true 必须指定
                lineHeight: 25,//每行的高度
                /**
                 * 默认第一次加载条数  rp 的计算为 Math.cell(maxHeight/lineHeight)+5 计算的值必rp的大 使用计算的值
                 * 可以不指定rp 但是一定要指定maxHeight，用户指定的rp可能会有问题
                 */
                rp: 15,
                url: '', //请求url 地址（也是搜索的地址） 比prototypeData 优先
                param: {},//查询参数
                //以下是使用原型数据加载的
                protoQueryName: "name",//原型数据时  默认搜索的属性字段
                ignoreCase: true,//查询默认是忽略大小的
                isExpandOne: true,//是否只允许展开一个子节点
                /**
                 * 正则字符串 protoQueryName 前的匹配
                 *  例如   "([^{]*)\"model\":\"STATION\""
				 * 此表示 匹配属性model=STATION 的 且model属性在protoQueryName 的前匹配 
				 * ([^{]*) 表示 model 并不是第一个属性值
				 */
                regProtoQueryBefore: false,//protoQueryName 前的匹配
                regProtoQueryAfter: false,//protoQueryName 后的匹配
                protoSearchParent: true,//查询时是否连带查询父节点 默认 不查询  为true 会降低效率
                prototypeData: false,//指定静态数据
                //	protoMaxAuto:true,//原型数据 是否支持自动加载  不自动加载 则加载全部   此时（protoMaxAutoExpand ，protoOneLoadGetNode protoBeginLoadRate）无效
                /**
                 * 是否是滚动加载剩余数据
                 * 默认是使用分页来加载
                 */
                protoScrollAuto: false,
                /**
                 * 数据达到多少条数时 才进行 分页 或者滚动加载
                 * -1 始终全加载
                 * 0 始终分页加载
                 * >0 达到多少条数时 才进行 分页 或者滚动加载
                 */
                protoMaxAutoExpand: 0,//原型数据 超过多少条数时，第一次加载不全部加载,  <0的值时 默认不使用自动加载,（超过时 加载每一级的子级 直到某个第一级的子级超过rp的值 或者某个节点是叶子节点）
                /**
                 * 参数 (ztreeData,ztreeDataTemp,rp,open)
                 * ztreeData 数型结构的数组，全部数据
                 * ztreeDataTemp 需获取节点放置的数组
                 * rp 每页的条数 对应 预置的rp值
                 * open {open:true} 展开的对象
                 * 不自定义则采用默认内部方法 _oneLoadGetNode
                 * 默认 加载每一级的第一级子级 直到第一级的子级超过rp的值 或者第一级的子级是叶子节点
                 */
                protoOneLoadGetNode: false,//自动加载时 第一次需要加载节点的自定义获取方法
                protoBeginLoadRate: 0.87,//当前滚动到 滚动条的高度*protoBeginLoadRate 时 进行自动加载 >0.4 <=1
                filterFun: false,//获取选中时的过滤方法  入参是node 节点  返回true 表示需要此值，否则不需要
                nameOfAllNode: false,
                _initTree: { // 用于树还原
                    _selectNode: false,// 调用 idstreeReInitSelect 获取当前数据
                    _curEle: false, // 调用idstreeCurEle 方法获取 当前页
                    _searchKey: false//调用idstreeSearchParam  获取当前的查询条件
                }
            },

            _preCreate: function () {
                var id = this.element.attr('id');
                if (!id) {
                    id = new Date().getTime();
                    this.element.attr('id', id);
                }
                this.options = $.extend(this.options, _m.getOptions(id));
                //滚动加载时 需重新计算rp
                if (this.options.protoScrollAuto && this.options.protoMaxAuto) {
                    var maxHeight = this.options.maxHeight;
                    var lineHeight = this.options.lineHeight;
                    var cRp = Math.ceil(maxHeight / lineHeight) + 5;
                    if (cRp > this.options.rp) {
                        this.options.rp = cRp;
                    }
                }
            },
            /***
             * 创建元素时使用
             * @private
             */
            _create: function () {
                this._preCreate();
                this._createDiv();
                this._common();
            },
            _createDiv: function () {
                var _gThis = this;
                var _data = _gThis.options;
                var _dom = $('#' + _data.id);
                //添加正在加载的div
                var loadingDiv = $('#' + _data._loading);
                if (!loadingDiv || loadingDiv.length <= 0) {
                    var time = new Date().getTime();
                    loadingDiv = $('<div hidden=true class="' + _data._loadingClass + '" id="' + _data._loading + '"></div>');
                    loadingDiv.append('<div class="ids_loading"><div>' + Msg.loading + '</div><div></div><div></div></div>');
                    $('body').append(loadingDiv);
                    loadingDiv.css({
                        'position': 'absolute',
                        'overflow': 'auto'
                    });
                }
                //输入框事件
                if (_data.searchInput) {
                    var _seach = $(_data.searchInput);
                    //输入变化后自动查询第一页的数据
                    _seach.off('input propertychange').on('input propertychange', function () {
                        _gThis._showLoading(_data);
                        //搜索时是否记录选中记录
                        if (!_data.searchRecordSelected) {
                            _data.SelectedRecord = new Map();
                        }
                        _data.seachParm = $(this).val();
                        _gThis._toAddData(_m._protoQueryFun($(this).val(), $.extend({}, _gThis)), _gThis);
                    });
                }
                //树滚动事件
                if (_dom && _dom.length > 0 && _data.protoScrollAuto && _data.protoMaxAuto) {
                    //滚动自动加载
                    _dom.off('scroll').on('scroll', function (e) {
                        if (!_data.protoMaxAuto) {
                            return;
                        }
                        var _$this = $(this);
                        var sTop = _$this.scrollTop();
                        var height = _$this.height();
                        var scHeight = _$this[0].scrollHeight;
                        var rate = _data.protoBeginLoadRate || 0.87;
                        rate = rate > 1 || rate <= 0.4 ? 0.87 : rate;
                        if (_data.prototypeData && sTop + height >= scHeight * rate) {
                            if (_data.lastTid && $(e.target).closest("#" + _data.lastTid).length == 0) {
                                _gThis._autoLoadProtoTypeData(_gThis, _data.lastTid);
                            } else if (_data.oneLevelLasId && $(e.target).closest("#" + _data.oneLevelLasId).length == 0) {
                                _gThis._autoLoadProtoTypeData(_gThis, _data.oneLevelLasId);
                            }
                        }
                    });
                }
            },
            /**
             * 内部方法 滚动加载原型数据时获取 需要动态加载的父节点
             * 并调用_protoAddData 方法动态加载数据
             */
            _autoLoadProtoTypeData: function (_gThis, tid) {
                var _data = _gThis.options;
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);
                var node = treeObj.getNodeByTId(tid);
                var treeNode = node.getParentNode();//treeNode是空场景是要加载第一级
                this._protoAddData(_gThis, treeObj, treeNode, true, !treeNode && node);
            },
            /**
             * 加载原型数据的处理方法  此方法 不处理默认选中的值
             * isMoreRp 表示是否允许加载超过 _data.rp 的条数
             * 自定义位置_index
             */
            _protoAddData: function (_gThis, treeObj, treeNode, isMoreRp, node, _index) {
                var _data = _gThis.options, _treeSet = _m._getTreeSet(_data);
                var _da = _data.prototypeSeachZtreeData;
                var checked = false, index = 0, pchilds;
                if (!treeNode && node) { // 加载第一级
                    if (_da.length < _da.rp || ( _da[_da.length - 1][_treeSet.id] == node[_treeSet.id])) {
                        return;
                    }
                    pchilds = $.extend(true, [], _da);
                } else {
                    var chkStyle = _m._getChkStyle(_data);
                    var hasChildren = (treeNode.children || (_index != undefined && _index > 0)) ? true : false;
                    if (chkStyle == "checkbox" && treeNode.checked) {
                        checked = true;
                    }
                    var pidArr = [];
                    if ("-1" != treeNode[_treeSet.id]) {
                        pidArr = [treeNode[_treeSet.id]];
                        var pNode = treeNode.getParentNode();
                        while (pNode && pNode[_treeSet.id] != "-1") {
                            pidArr.unshift(pNode[_treeSet.id]);
                            pNode = pNode.getParentNode();
                        }
                        if (treeNode.children) {
                            index = treeNode.children.length;
                            _data.lastTid = treeNode.children[index - 1].tId;
                        }
                        pchilds = _m._toGetChildrenNode(_da, pidArr, _treeSet.id);
                    } else {
                        pchilds = $.extend(true, [], _da);
                    }
                    if ((!isNaN(treeNode.childSize) && index >= treeNode.childSize) || (!isMoreRp && index > 0)) {
                        return;
                    }
                }
                if (pchilds) {
                    if (treeNode && isNaN(treeNode.childSize)) {
                        treeNode.childSize = pchilds.length;
                        treeObj.updateNode(treeNode);
                    }
                    var ztreeDataTemp = [];
                    var hasLoadLast = false;
                    var bt = _index || index;
                    $.each(pchilds, function (t, e) {
                        if (t >= bt && ztreeDataTemp.length < _data.rp && !isNaN(t)) {
                            e.children = null;
                            e.checked = false;
                            if ($.isFunction(_data.formatNode)) {
                                e = _data.formatNode(e);
                            }
                            if (!e) return true;
                            ztreeDataTemp.push($.extend(true, e, {open: false}));
                            if (pchilds.length - 1 == t) {
                                hasLoadLast = true;
                                if (!treeNode && node) {
                                    _data.oneLevelLasId = null;
                                } else {
                                    _data.lastTid = null;
                                }
                            }
                        }
                        if (ztreeDataTemp.length >= _data.rp || isNaN(t)) {
                            return false;
                        }
                    });
                    if (ztreeDataTemp && ztreeDataTemp.length > 0) {
                        treeObj.addNodes(treeNode, ztreeDataTemp);
                        if (checked) {
                            var pNodeId;
                            $.each(ztreeDataTemp, function (t, e) {
                                var tnodes = treeObj.getNodeByParam(_treeSet.id, e[_treeSet.id], null);
                                treeObj.checkNode(tnodes, true, false, true);
                                pNodeId = tnodes[_treeSet.pid];
                            });
                            var tpnodes = treeObj.getNodeByParam(_treeSet.id, pNodeId, null);
                            _data.SelectedRecord.get(pNodeId) && _data.SelectedRecord.set(pNodeId, $.extend(true, {}, tpnodes, {children: null}));
                        }
                        var lTid = treeNode.children[treeNode.children.length - 1].tId;
                        if (!hasLoadLast) {
                            if (!treeNode && node) {
                                _data.oneLevelLasId = lTid;
                            } else {
                                _data.lastTid = lTid;
                            }
                        }
                    }
                }
            },
            /**
             * 显示正在加载
             */
            _showLoading: function (_data) {
                var loading = _data._loading, tree = $("#" + _data.id).parent();
                if (!tree.is(':visible')) {
                    return;
                }
                $("#" + loading).css({
                    width: tree.outerWidth(),
                    height: tree.outerHeight(),
                    'background-color': 'rgba(0, 0, 0, .5)',
                    top: tree.offset().top,
                    'z-index': 201,
                    left: tree.offset().left
                });
                var _py_loading = $("#" + loading).find('.ids_loading');
                var ml = (tree.outerWidth() - _py_loading.outerWidth()) / 2;
                var mt = (tree.outerHeight() - _py_loading.outerHeight()) / 2;
                _py_loading.css({
                    "margin-left": ml < 0 ? 0 : ml,
                    "margin-top": mt < 0 ? 0 : mt
                });
                $("#" + loading).show();
            },
            /**
             * 隐藏正在加载
             */
            _hideLoading: function (_data) {
                $("#" + _data._loading).hide();
            },
            //控制页面滚动时 隐藏树的div
            _common: function () {
            },
            /**
             * 通过url 获取数据的方法
             */
            getData: function (callBack) {
                var _gThis = this;
                var _data = _gThis.options;
                if (!_data.url) {
                    return;
                }
                _gThis._showLoading(_data);
                var _this = $(this.element);
                var _setting = _data._setting;
                var _param = $.isFunction(_data.param) ? _data.param() : _data.param;
                $.http.post(_data.url, _param, function (data) {
                    var ro = data;
                    if (ro.success) {
                        var nodes = ro.data || [];
                        //加载数据
                        _gThis._toAddData($.extend(true, [], nodes), _gThis);
                    }
                    callBack && $.isFunction(callBack) && callBack();
                    _gThis._hideLoading(_data);
                }, function () {
                    _gThis._hideLoading(_data);
                });
            },
            _dealData: function (nodes, gThis) {
                var _data = gThis.options, data = [];
                var _treeSet = _m._getTreeSet(_data), _setting = _m.getMSetting(gThis, _data._setting),
                    open = {open: true};
                var isParent = _treeSet.isParent;
                var pTreeObj = $.fn.zTree.init($('#' + new Date().getTime()), _setting, []);
                var tData = pTreeObj.transformTozTreeNodes($.extend(true, [], nodes));
                var tDataArr = pTreeObj.transformToArray($.extend(true, [], tData));
                $.each(tDataArr, function (t, e) {
                    e = $.extend(e, open);
                    if (e.children) {
                        e[isParent] = true;
                        e.childSize = e.children.length;
                        if (e.childSize > _data.rp) {
                            e.PageTool = true, e.page = 1, e.curPage = 1;
                            e.maxPage = Math.floor((e.childSize + _data.rp - 1) / _data.rp);
                        } else {
                            e.PageTool = false, e.maxPage = 1, e.page = 1, e.curPage = 1;
                        }
                        e.children = null;
                    } else {
                        e[isParent] = false;
                        e.PageTool = false, e.maxPage = 1, e.page = 1, e.curPage = 1;
                    }
                    if ($.isFunction(_data.formatNode)) {
                        e = _data.formatNode(e);
                    }
                    e && data.push(e);
                });
                pTreeObj.destroy();
                return data;
            },
            /**
             * 内部方法 初始化ztree
             */
            _toAddData: function (nodes, gThis) {
                var _data = gThis.options, _this = $(gThis.element), data = gThis._dealData(nodes, gThis);
                var _treeSet = _m._getTreeSet(_data), _setting = _m.getMSetting(gThis, _data._setting),
                    open = {open: true};
                var idName = _treeSet.id, pidName = _treeSet.pid, isParent = _treeSet.isParent;
                var pTreeObj = $.fn.zTree.init($('#' + new Date().getTime()), _setting, []);
                tData = pTreeObj.transformTozTreeNodes($.extend(true, [], data));
                _data.prototypeSeachData = $.extend(true, [], data);
                _data.prototypeSeachZtreeData = $.extend(true, [], tData);
                if (!_data.protoTreeSet) {
                    _data.protoTreeSet = true;
                    _data.prototypeData = $.extend(true, [], data);
                    _data.SelectedRecord = new Map();
                }
                if (data.length >= _data.protoMaxAutoExpand
                    && _data.protoMaxAutoExpand >= 0) {
                    _data.protoMaxAuto = true;
                    var ztreeData = $.extend(true, [], tData);
                    var ztreeDataTemp = [];
                    if (_data.protoOneLoadGetNode && $.isFunction(_data.protoOneLoadGetNode)) {
                        _data.protoOneLoadGetNode(ztreeData, ztreeDataTemp, _data.rp, open);
                    } else {
                        gThis._oneLoadGetNode(ztreeData, ztreeDataTemp, _data.rp, open);
                    }
                    if (!ztreeDataTemp || ztreeDataTemp.length <= 0) {
                        var tObj = $.fn.zTree.init(_this, _setting, ztreeDataTemp);
                    } else {
                        var lastId = ztreeDataTemp[ztreeDataTemp.length - 1][idName];
                        //判断第一级是否可完全加载
                        if (ztreeData.length > _data.rp) {
                            //第一级存在分页 则需虚拟出一个根节点来进行 分页处理
                            var nName = _data.nameOfAllNode || Msg.allNode;
                            var node = {name: nName, isParent: true, childSize: ztreeData.length};
                            node[idName] = "-1";
                            node.PageTool = true, node.page = 1, node.curPage = 1;
                            node.maxPage = Math.floor((node.childSize + _data.rp - 1) / _data.rp);
                            var tObj = $.fn.zTree.init(_this, _setting, [node]);
                            var pnodes = tObj.getNodesByParam(idName, node.id, null);
                            tObj.addNodes(pnodes[0], ztreeDataTemp);
                            var nodes = tObj.getNodesByParam(idName, lastId, null);
                            _data.oneLevelLasId = nodes[0].tId;

                        } else {
                            var tObj = $.fn.zTree.init(_this, _setting, ztreeDataTemp);
                            var nodes = tObj.getNodesByParam(idName, lastId, null);
                            _data.lastTid = nodes[0].tId;
                        }
                    }
                } else {
                    _data.protoMaxAuto = false;
                    $.fn.zTree.init(_this, _setting, data);
                }
                pTreeObj.destroy();
                gThis._hideLoading(_data);
            },
            /**
             * 内部方法
             * 需动态加载时 获取第一次需要加载的数据
             * ztreeData ztree结构的数据，ztreeDataTemp 存放的数组，rp 获取rp条
             */
            _oneLoadGetNode: function (ztreeData, ztreeDataTemp, rp, open) {
                var gThis = this;
                var children = {};
                var isToloadChild = (ztreeData.length < rp);
                var _data = gThis.options;
                var _treeSet = _m._getTreeSet(_data);
                $.each(ztreeData, function (t, e) {
                    if (isNaN(t) || t >= rp) {
                        return false;
                    }
                    var temp = {children: null, open: false};
                    if (e.isParent && e.children) {
                        temp.childSize = e.children.length;
                    }
                    if (t == 0 && isToloadChild) {
                        children = e.children;
                        temp = $.extend(temp, open);
                    }
                    ztreeDataTemp.push($.extend(true, {}, e, temp));
                    if (t == ztreeData.length - 1 && isToloadChild && !$.isEmptyObject(children)) {
                        gThis._oneLoadGetNode(children, ztreeDataTemp, rp, open);
                    }
                });
            },
            /**
             * 初始化
             * @private
             */
            _init: function () {
                var _data = this.options;
                var prototypeData = _data.prototypeData;
                if (prototypeData) {
                    this._toAddData(prototypeData, this);
                    this._hasInitTree();
                } else {
                    this.getData(this._hasInitTree);
                }
                return this.element;
            },
            _hasInitTree: function () {
                var gThis = this;
                var _data = this.options;
                if (_data && _data._initTree) {
                    var _init = _data._initTree;
                    var _data = gThis.options, _this = $(gThis.element), data = [];
                    var _treeSet = _m._getTreeSet(_data), _setting = _m.getMSetting(gThis, _data._setting),
                        open = {open: true};
                    if (_init._searchKey) {
                        _data.searchInput && _data.searchInput.val(_init._searchKey);
                        var nodes = _m._protoQueryFun(_init._searchKey, $.extend({}, gThis));
                        var idName = _treeSet.id, pidName = _treeSet.pid, isParent = _treeSet.isParent;
                        var pTreeObj = $.fn.zTree.init($('#' + new Date().getTime()), _setting, []);
                        var tData = pTreeObj.transformTozTreeNodes($.extend(true, [], nodes));
                        var tDataArr = pTreeObj.transformToArray($.extend(true, [], tData));
                        $.each(tDataArr, function (t, e) {
                            e = $.extend(e, open);
                            if (e.children) {
                                e[isParent] = true;
                                e.childSize = e.children.length;
                                if (e.childSize > _data.rp) {
                                    e.PageTool = true, e.page = 1, e.curPage = 1;
                                    e.maxPage = Math.floor((e.childSize + _data.rp - 1) / _data.rp);
                                } else {
                                    e.PageTool = false, e.maxPage = 1, e.page = 1, e.curPage = 1;
                                }
                                e.children = null;
                            }
                            if ($.isFunction(_data.formatNode)) {
                                e = _data.formatNode(e);
                            }
                            e && data.push(e);
                        });
                        tData = pTreeObj.transformTozTreeNodes($.extend(true, [], data));
                        _data.prototypeSeachData = $.extend(true, [], data);
                        _data.prototypeSeachZtreeData = $.extend(true, [], tData);
                    }
                    if (_init._curEle) {
                        var datas = _init._curEle;
                        $.fn.zTree.init(_this, _setting, datas);
                    }
                    if (_init._selectNode) {
                        _data.SelectedRecord = $.extend(true, new Map(), _init._selectNode);
                    }
                }
            },
            _setOption: function (key, value) {
                $.Widget.prototype._setOption.apply(this, arguments);
            },

            _setOptions: function (options) {
                var key;
                for (key in options) {
                    this._setOption(key, options[key]);
                }
                return this;
            },
            printOptions: function () {
                console.info(this.options);
            },
            /**
             * 销毁
             * @private
             */
            _destroy: function () {
                var _gThis = this;
                $(this.element).find('#' + _data._loading).remove();
                return this._super();
            },
            /**
             * 获取选中的节点的idName 属性
             * nodes 选中的节点
             * idArr 存放idName属性值的数组
             * filterFun node的过滤方法
             */
            _getSelectedNodeIds: function (nodes, gThis, idName, filterFun) {
                var _this = $(gThis.element);
                var _data = gThis.options;
                var _da = $.extend(true, [], _data.prototypeSeachZtreeData);
                var _treeSet = _m._getTreeSet(_data);
                var idrrs = new Map();
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);

                $.each(nodes, function (t, e) {
                    var isToAdd = true;
                    if (filterFun && $.isFunction(filterFun)) {
                        isToAdd = filterFun($.extend(true, {}, e));
                    }
                    isToAdd && (idrrs.set(e[_treeSet.id], idName ? e[idName] : $.extend(true, {}, e, {children: null})));
                    if (e.checked && e.isParent) {
                        var index = -1;
                        var nodes = treeObj.getNodesByParam(_treeSet.id, e[_treeSet.id], null);
                        var hashChildren = e.children;
                        var chlength = e.children ? e.children.length : null;
                        if (nodes && nodes.length > 0) {
                            var ep = nodes[0];
                            hashChildren = ep.children ? true : false;
                            chlength = ep.children ? ep.children.length : null;
                        }
                        //父节点选中 存在子节点 且 子节点未加载完成 只加载一部分 且是全选状态 （不存在 子节点未选中）
                        if (e.check_Child_State == 2 && e.childSize && hashChildren &&
                            e.childSize != chlength) {
                            if (_data.protoScrollAuto) {
                                index = chlength;
                            } else {
                                index = 0;
                            }
                        } else if (e.childSize && !hashChildren) {
                            index = 0;//父节点选中 未开始加载
                        }
                        if (index >= 0) {
                            var pidArr = [];
                            if (e[_treeSet.id] != "-1") {
                                pidArr = [e[_treeSet.id]];
                                var pNode = e.getParentNode();
                                while (pNode && pNode[_treeSet.id] != "-1") {
                                    pidArr.unshift(pNode[_treeSet.id]);
                                    pNode = pNode.getParentNode();
                                }
                            }
                            var ids = _m._toGetChildrenId(_da, pidArr, idName, _treeSet.id, index, filterFun);
                            if (ids && ids.size > 0) {
                                $.each(ids.map, function (t, e) {
                                    idrrs.set(t, $.extend(true, {}, e, {children: null}));
                                })
                            }
                        }
                    }
                    if (e.children) {
                        var ids = gThis._getSelectedNodeIds(e.children, gThis, idName, filterFun);
                        if (ids && ids.size > 0) {
                            $.each(ids.map, function (t, e) {
                                idrrs.set(t, $.extend(true, {}, e, {children: null}));
                            })
                        }
                    }
                });
                return idrrs;
            },
            idstreeSelectedObj: function (filterFun) {
                var _this = $(this.element);
                var _data = this.options;
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);
                var idArr = [];
                var chkStyle = _m._getChkStyle(_data);
                var treeSet = _m._getTreeSet(_data);
                filterFun = filterFun || _data.filterFun;
                if (chkStyle && chkStyle == "checkbox" && _data.protoMaxAutoExpand >= 0
                    && _data.prototypeData && _data.prototypeData.length > _data.protoMaxAutoExpand
                    && treeObj.transformToArray(treeObj.getNodes()).length != _data.prototypeData.length) {
                    var selectedRecord = _data.SelectedRecord.values();
                    var nodes = treeObj.transformTozTreeNodes($.extend(true, [], selectedRecord));
                    var map = this._getSelectedNodeIds(nodes, this, null, filterFun);
                    idArr = $.extend(true, [], map.values());
                } else {
                    var nodeArr = $.extend(true, [], _data.SelectedRecord.values());
                    $.each(nodeArr, function (t, e) {
                        var isAdd = true;
                        if (filterFun && $.isFunction(filterFun)) {
                            isAdd = filterFun($.extend(true, {}, e));
                        }
                        isAdd && (idArr.push($.extend(true, {}, e)));
                    });
                }
                return $.extend([], idArr);
            },
            /**
             * 获取选中
             */
            idstreeSelected: function (idName, filterFun) {
                var _this = $(this.element);
                var _data = this.options;
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);
                var idArr = [];
                var chkStyle = _m._getChkStyle(_data);
                var treeSet = _m._getTreeSet(_data);
                idName = idName || treeSet.id;
                filterFun = filterFun || _data.filterFun;
                if (chkStyle && chkStyle == "checkbox" && _data.protoMaxAutoExpand >= 0
                    && _data.prototypeData && _data.prototypeData.length > _data.protoMaxAutoExpand
                    && treeObj.transformToArray(treeObj.getNodes()).length != _data.prototypeData.length) {
                    var selectedRecord = _data.SelectedRecord.values();
                    var nodes = treeObj.transformTozTreeNodes($.extend(true, [], selectedRecord));
                    var map = this._getSelectedNodeIds(nodes, this, idName, filterFun);
                    idArr = $.extend(true, [], map.values());
                } else {
                    var nodeArr = $.extend(true, [], _data.SelectedRecord.values());
                    $.each(nodeArr, function (t, e) {
                        var isAdd = true;
                        if (filterFun && $.isFunction(filterFun)) {
                            isAdd = filterFun($.extend(true, {}, e));
                        }
                        isAdd && (idArr.push(e[idName]));
                    });
                }
                return $.extend([], idArr);
            },
            idstreeProtoDataChange: function (nodes, type) {
                if (!nodes || nodes.length < 0) {
                    return;
                }
                var gThis = this;
                var _data = this.options;
                var treeSet = _m._getTreeSet(_data);
                var pData = $.extend(true, [], _data.prototypeData);
                var psData = $.extend(true, [], _data.prototypeSeachData);
                var pn = $.extend(true, [], nodes);
                var psn = $.extend(true, [], nodes);
                //修改
                if (type == '0') {
                    for (var i = 0; i < pData.length, pn.length > 0; i++) {
                        var ep = pData[i];
                        for (var j = 0; j < pn.length; j++) {
                            if (ep[treeSet.id] == pn[j][treeSet.id]) {
                                ep.name = pn[j].name;
                                ep[treeSet.pid] = pn[j][treeSet.pid];
                                pn.splice(j, 1);
                                break;
                            }
                        }
                    }
                    for (var i = 0; i < psData.length, psn.length > 0; i++) {
                        var ep = psData[i];
                        for (var j = 0; j < psn.length; j++) {
                            if (ep[treeSet.id] == psn[j][treeSet.id]) {
                                ep.name = psn[j].name;
                                ep[treeSet.pid] = psn[j][treeSet.pid];
                                psn.splice(j, 1);
                                break;
                            }
                        }
                    }
                    //添加
                } else if (type == "1") {
                    pData = pData.concat(pn);
                    psData = psData.concat(psn);
                    //删除
                } else if (type == "-1") {
                    for (var i = 0; i < pn.length; i++) {
                        var ep = pn[i];
                        for (var j = 0; j < pData.length; j++) {
                            if (ep[treeSet.id] == pData[j][treeSet.id]) {
                                pData.splice(j, 1);
                                break;
                            }
                        }
                    }
                    for (var i = 0; i < psn.length; i++) {
                        var ep = psn[i];
                        for (var j = 0; j < psData.length; j++) {
                            if (ep[treeSet.id] == psData[j][treeSet.id]) {
                                psData.splice(j, 1);
                                break;
                            }
                        }
                    }
                }
                var data = gThis._dealData(pData, gThis);
                _data.prototypeData = $.extend(true, [], data);

                var sdata = gThis._dealData(psData, gThis);
                _data.prototypeSeachData = $.extend(true, [], sdata);

                var _setting = _m.getMSetting(gThis, _data._setting)
                var pTreeObj = $.fn.zTree.init($('#' + new Date().getTime()), _setting, []);
                var tData = pTreeObj.transformTozTreeNodes($.extend(true, [], sdata));
                _data.prototypeSeachZtreeData = $.extend(true, [], tData);

                pTreeObj.destroy();

            },
            idstreeBackinitTree: function () {
                var _init = {};
                _init._searchKey = this.idstreeSearchParam();
                _init._selectNode = this.idstreeReInitSelect();
                _init._curEle = this.idstreeCurEle();
                return $.extend(true, {}, _init);
            },
            /**
             * 获取当前的查询条件
             */
            idstreeSearchParam: function () {
                var _data = this.options;
                return _data.seachParm || "";
            },
            /**
             * 获取 之前选中的记录
             */
            idstreeReInitSelect: function () {
                var _data = this.options;
//				_m._reGetSelectedRecord(this);//获取选中 并记录
                return $.extend(true, {}, _data.SelectedRecord);
            },
            idstreeCurEle: function () {
                var _data = this.options;
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);
                var nodes = treeObj.transformToArray(treeObj.getNodes());
                var datas = [];
                if (nodes && nodes.length > 0) {
                    $.each(nodes, function (t, e) {
                        datas.push($.extend(true, {}, e, {children: null}));
                    });
                }
                return datas;
            }
        });
        /**
         * 额外的方法 主要用于构建 options
         */
        var _m = {
            getOptions: function (id) {
                return {
                    id: id,
                    _loading: id + "_loading_l",
                    _loadingClass: "ids_loading",
                    protoMaxAuto: true
                }
            },
            getMSetting: function (excur, _s) {
                var _data = excur.options;
                var _treeSet = _m._getTreeSet(_data);
                var c_call = _s ? _s.callback : {};
                var c_view = _s ? _s.view : {};
                var chkStyle = _m._getChkStyle(_data);
                var _setting = {
                    view: {
                        addDiyDom: function (treeId, treeNode) {
                            if (_data.SelectedRecord.get(treeNode[_treeSet.id])) {
                                treeNode.checked = true;
                                treeNode.check_Child_State = _data.SelectedRecord.get(treeNode[_treeSet.id]).check_Child_State;
                                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                                treeObj.updateNode(treeNode);
                            }
                            !_data.protoScrollAuto && _data.protoMaxAuto && _m._addDiyDom(treeId, treeNode, excur);
                            c_view && c_view.addDiyDom && $.isFunction(c_view.addDiyDom) && c_view.addDiyDom(treeId, treeNode);
                        }
                    },
                    callback: {
                        onCheck: function (event, treeId, treeNode) {
                            if (!treeNode.checked) {
                                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                                var pNode = treeNode.getParentNode();
                                if (pNode) {
                                    treeObj.checkNode(pNode, false, false, true);
                                }
                                if (chkStyle != 'checkbox') {
                                    _data.SelectedRecord.clear();
                                } else {
                                    _data.SelectedRecord.delete(treeNode[_treeSet.id]);
                                }
                            } else {
                                _data.SelectedRecord.set(treeNode[_treeSet.id], $.extend(true, {}, treeNode, {children: null}));
                            }
                            c_call && c_call.onCheck && $.isFunction(c_call.onCheck) && c_call.onCheck(event, treeId, treeNode);
                        },
                        beforeExpand: function (treeId, treeNode) {
                            _data.lastTid = null;
                            if (_data.isExpandOne) {
                                var pNode = _data.curExpandNode ? _data.curExpandNode.getParentNode() : null;
                                var treeNodeP = treeNode.parentTId ? treeNode.getParentNode() : null;
                                var zTree = $.fn.zTree.getZTreeObj(treeId);
                                for (var i = 0, l = !treeNodeP ? 0 : treeNodeP.children.length; i < l; i++) {
                                    if (treeNode !== treeNodeP.children[i]) {
                                        zTree.expandNode(treeNodeP.children[i], false);
                                    }
                                }
                                while (pNode) {
                                    if (pNode === treeNode) {
                                        break;
                                    }
                                    pNode = pNode.getParentNode();
                                }
                                if (!pNode) {
                                    _m._singlePath(treeNode, treeId, _data);
                                }
                            }
                            c_call && c_call.beforeExpand && $.isFunction(c_call.beforeExpand) && c_call.beforeExpand(treeId, treeNode);
                        },
                        beforeCollapse: function (e, treeId, treeNode) {
                            _data.lastTid = null;
                            if (c_call.beforeCollapse && $.isFunction(c_call.beforeCollapse)) {
                                c_call.beforeCollapse(e, treeId, treeNode);
                            }
                        },
                        onCollapse: function (e, treeId, treeNode) {
                            _data.lastTid = null;
                            c_call && c_call.onCollapse && $.isFunction(c_call.onCollapse) && c_call.onCollapse(e, treeId, treeNode);
                        },
                        onExpand: function (e, treeId, treeNode) {
                            _data.curExpandNode = treeNode;
                            if (_data.prototypeData && treeNode[_treeSet.id] != "-1") {
                                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                                excur._protoAddData(excur, treeObj, treeNode, false);
                            }
                            c_call && c_call.onExpand && $.isFunction(c_call.onExpand) && c_call.onExpand(e, treeId, treeNode);
                        }
                    }
                };
                var check = chkStyle == "checkbox" ? {
                    check: {
                        autoCheckTrigger: true,
                        chkboxType: {"Y": "s", "N": "s"}
                    }
                } : {};
                return $.extend(true, {}, _s, _setting, check);
            },
            _addDiyDom: function (treeId, treeNode, gThis) {
                var _data = gThis.options;
                var _treeSet = _m._getTreeSet(_data);
                //非父类  未达到分页标准
                if (!treeNode.PageTool) return;
                var aObj = $("#" + treeNode.tId + "_a");
                if ($("#addBtn_" + treeNode.id).length > 0) return;
                var addStr = "<label class='pbutton pbutton_dis'><span class='button firstPage' id='firstBtn_" + treeNode.id
                    + "'  onfocus='this.blur();'></span></label><label class='pbutton pbutton_dis'><span class='button prevPage' id='prevBtn_" + treeNode.id
                    + "'  onfocus='this.blur();'></span></label><label class='showPage' id='curPageShow_" + treeNode.id + "' ></label><label class='pbutton pbutton_on'><span class='button nextPage' id='nextBtn_" + treeNode.id
                    + "'  onfocus='this.blur();'></span></label><label class='pbutton pbutton_on'><span class='button lastPage' id='lastBtn_" + treeNode.id
                    + "'  onfocus='this.blur();'></span></label>";
                aObj.after(addStr);
                var first = $("#firstBtn_" + treeNode.id);
                var prev = $("#prevBtn_" + treeNode.id);
                var next = $("#nextBtn_" + treeNode.id);
                var last = $("#lastBtn_" + treeNode.id);
                first.off('click').on('click', function () {
                    _m._goPage(treeNode, 1, treeId, gThis);
                });
                last.off('click').on('click', function () {
                    _m._goPage(treeNode, treeNode.maxPage, treeId, gThis);
                });
                prev.off('click').on('click', function () {
                    _m._goPage(treeNode, treeNode.page - 1, treeId, gThis);
                });
                next.off('click').on('click', function () {
                    _m._goPage(treeNode, treeNode.page + 1, treeId, gThis);
                });
                _m._delPageButton(_data.id, treeNode.page, treeNode);
            },
            _goPage: function (treeNode, page, treeId, gThis) {
                var _data = gThis.options;
                var _treeSet = _m._getTreeSet(_data);
                if (page < 1) page = 1;
                if (page > treeNode.maxPage) page = treeNode.maxPage;
                if (treeNode.curPage == page) return;
                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                treeObj.removeChildNodes(treeNode);
                gThis._protoAddData(gThis, treeObj, treeNode, true, null, (page - 1) * _data.rp);
                treeNode.page = page;
                treeNode.curPage = page;
                treeObj.updateNode(treeNode);
                _m._delPageButton(treeId, page, treeNode);
            },
            _delPageButton: function (treeId, page, treeNode) {
                $(".pbutton", "#" + treeId).addClass("pbutton_on");
                $(".pbutton", "#" + treeId).removeClass("pbutton_dis");
                if (page == 1) {
                    $('#firstBtn_' + treeNode.id).parent("label").removeClass("pbutton_on").addClass("pbutton_dis");
                    $('#prevBtn_' + treeNode.id).parent("label").removeClass("pbutton_on").addClass("pbutton_dis");
                }
                if (page == treeNode.maxPage) {
                    $('#nextBtn_' + treeNode.id).parent("label").removeClass("pbutton_on").addClass("pbutton_dis");
                    $('#lastBtn_' + treeNode.id).parent("label").removeClass("pbutton_on").addClass("pbutton_dis");
                }
                $('#curPageShow_' + treeNode.id).text(page);
            },
            _singlePath: function (newNode, treeId, _data) {
                if (newNode === _data.curExpandNode) return;
                var zTree = $.fn.zTree.getZTreeObj(treeId),
                    rootNodes, tmpRoot, tmpTId, i, j, n;
                if (!_data.curExpandNode) {
                    tmpRoot = newNode;
                    while (tmpRoot) {
                        tmpTId = tmpRoot.tId;
                        tmpRoot = tmpRoot.getParentNode();
                    }
                    rootNodes = zTree.getNodes();
                    for (i = 0, j = rootNodes.length; i < j; i++) {
                        n = rootNodes[i];
                        if (n.tId != tmpTId) {
                            zTree.expandNode(n, false);
                        }
                    }
                } else if (_data.curExpandNode && _data.curExpandNode.open) {
                    if (newNode.parentTId === _data.curExpandNode.parentTId) {
                        zTree.expandNode(_data.curExpandNode, false);
                    } else {
                        var newParents = [];
                        while (newNode) {
                            newNode = newNode.getParentNode();
                            if (newNode === _data.curExpandNode) {
                                newParents = null;
                                break;
                            } else if (newNode) {
                                newParents.push(newNode);
                            }
                        }
                        if (newParents != null) {
                            var oldNode = _data.curExpandNode;
                            var oldParents = [];
                            while (oldNode) {
                                oldNode = oldNode.getParentNode();
                                if (oldNode) {
                                    oldParents.push(oldNode);
                                }
                            }
                            if (newParents.length > 0) {
                                zTree.expandNode(oldParents[Math.abs(oldParents.length - newParents.length) - 1], false);
                            } else {
                                zTree.expandNode(oldParents[oldParents.length - 1], false);
                            }
                        }
                    }
                }
                _data.curExpandNode = newNode;
            },
            /**
             * 从_da 中获取 id = pidArr中 最后一个值的 node节点 的子节点对象 不深度查找
             */
            _toGetChildrenNode: function (_da, pidArr, _id) {
                var finData;
                if (pidArr.length == 0) {
                    finData = $.extend(true, [], _da);
                } else {
                    $.each(_da, function (t, e) {
                        if (e[_id] == pidArr[0]) {
                            pidArr.splice(0, 1);
                            if (pidArr.length == 0) {//最后一个
                                finData = $.extend(true, [], e.children);
                                return false;
                            } else {
                                finData = _m._toGetChildrenNode(e.children, pidArr, _id);
                            }
                        }
                    });
                }
                return finData;
            },
            /**
             * 从_da 中获取   id = pidArr最后一个值的 node节点 的子节点 cNode
             * 并调用 _toGetAllChildrenIds 方法 获取  cNode的idName 属性  index=0 则深度查找其下所有的子节点的idName 属性
             * filterFun 过滤的方法判断此值是否需要
             */
            _toGetChildrenId: function (_da, pidArr, idName, _id, index, filterFun) {
                var finData = new Map();
                if (pidArr.length == 0) {
                    _m._toGetAllChildrenIds(_da, index, finData, idName, filterFun, _id);
                } else {
                    $.each(_da, function (t, e) {
                        if (e[_id] == pidArr[0]) {
                            pidArr.splice(0, 1);
                            if (pidArr.length == 0) {//最后一个
                                _m._toGetAllChildrenIds(e.children, index, finData, idName, filterFun, _id);
                                return false;
                            } else {
                                finData = _m._toGetChildrenId(e.children, pidArr, idName, _id, index, filterFun);
                            }
                        }
                    });
                }
                return finData;
            },
            /**
             * 从nodes中获取  节点 的idName属性   index=0 则深度查找其下所有的子节点的idName 属性
             */
            _toGetAllChildrenIds: function (nodes, index, idArr, idName, filterFun, _id) {
                for (var i = index; i < nodes.length; i++) {
                    var et = nodes[i];
                    var isToAdd = true;
                    if (filterFun && $.isFunction(filterFun)) {
                        isToAdd = filterFun($.extend(true, {}, et));
                    }
                    isToAdd && (idArr.set(et[_id], idName ? et[idName] : $.extend(true, {}, et)));
                    if (et.children && et.children.length > 0) {
                        _m._toGetAllChildrenIds(et.children, 0, idArr, idName, filterFun, _id);
                    }
                }
            },
            _getTreeSet: function (_data) {
                return {
                    id: _data._setting.data.simpleData.idKey || "id",
                    pid: _data._setting.data.simpleData.pIdKey || "pId",
                    isParent: _data._setting.data.simpleData.isParentKey || "isParent",
                }
            },
            _getChkStyle: function (_data) {
                if (!_data._setting.check) {
                    return "";
                }
                var chkStyle = _data._setting.check.chkStyle;
                var chkboxType = _data._setting.check.chkboxType;
                return chkStyle ? chkStyle : chkboxType ? "checkbox" : "";
            },
            _reGetSelectedRecord: function (gThis) {
                return;
                var _data = gThis.options;
                var treeObj = $.fn.zTree.getZTreeObj(_data.id);
                var chkStyle = _m._getChkStyle(_data);
                var treeSet = _m._getTreeSet(_data);
                var curSelected = treeObj.getCheckedNodesNotChildren(true);
                if (chkStyle && chkStyle != "checkbox") {
                    _data.SelectedRecord.clear();
                }
                if (curSelected && curSelected.length > 0) {
                    $.each(curSelected, function (t, e) {
                        _data.SelectedRecord.set(e[treeSet.id], $.extend(true, {}, e, {children: null}));
                    });
                }
            },
            _escapeChars: function (chars) {
                if (!chars || chars.length <= 0) return chars;
                var arr = [];
                var special = "$()*+.[]?\\^{}|";
                for (var i = 0; i < chars.length; i++) {
                    var char = chars[i];
                    if (special.indexOf(char) >= 0) {
                        if (char == "\\") {
                            arr.push("\\\\\\" + char);
                        } else {
                            arr.push("\\" + char);
                        }
                    } else {
                        arr.push(char);
                    }
                }
                return arr.join("");
            },
            /**
             * 搜索的默认方法
             */
            _protoQueryFun: function (seachVal, gThis) {
                var _data = gThis.options;
                var _treeSet = _m._getTreeSet(_data);
                var data = _data.prototypeData, reg, protoSearchParent = _data.protoSearchParent,
                    pid = _treeSet.pid, id = _treeSet.id, protoQueryName = _data.protoQueryName;
                if (!data || data.length <= 0) {
                    return data;
                }
                if (seachVal == "") {
                    return $.extend(true, [], data);
                }
                seachVal = _m._escapeChars(seachVal);
                var isString = (typeof data[0][pid]) == "string";
                var sep = isString ? "\"" : "";
                var regB = _data.regProtoQueryBefore ? _data.regProtoQueryBefore : "";
                var regA = _data.regProtoQueryAfter ? _data.regProtoQueryAfter : "";
                var ig = _data.ignoreCase ? "ig" : "g";
                if (protoQueryName) {
                    //区分 值前面有无"  区分结尾有无,
                    reg = new RegExp("({)" + regB + "([^{]*)\"" + protoQueryName + "\"\:(\"([^,{]*)" + seachVal + "(([^{,\"]*\"," + regA + "[^{]*})|([^{,\"]*}))|([^,\"{]*)" + seachVal + "(([^{,\"]*," + regA + "[^{]*})|([^{,\"]*})))", ig);
                } else {
                    //无属性名称关键查询 全查询 未测试
                    reg = new RegExp("({)" + regB + "([^{]*)\"([^{\",]*)\"\:(\"([^,{]*)" + seachVal + "(([^{,\"]*\"," + regA + "[^{]*})|([^{,\"]*}))|([^,\"{]*)" + seachVal + "(([^{,\"]*," + regA + "[^{]*})|([^{,\"]*})))", ig);
                }
                var dataArr = [];
                var queryStr = JSON.stringify(data);
                var regt = new RegExp("\"" + pid + "\":" + sep + "([^{,]*)" + sep + "(,|})", ig);
                var arr = queryStr.match(reg);
                while (arr && arr.length > 0) {
                    var parentSeach = [];
                    dataArr = dataArr.concat(arr);
                    if (protoSearchParent) {
                        var pids = arr.toString().match(regt);
                        if (pids && pids.length > 0) {
                            $.each(pids, function (t, e) {
                                e = "(" + e.replace(",", "").replace(pid, id) + ")";
                                if (parentSeach.indexOf(e) < 0) {
                                    parentSeach.push(e);
                                }
                            })
                        }
                        if (parentSeach.length > 0) {
                            queryStr = queryStr.replace(reg, "");
                            reg = new RegExp("{([^{]*)(" + parentSeach.join("|") + ")([^{]*)\"isParent\"\:true([^{]*)}", ig);
                            arr = queryStr.match(reg);
                        }
                    } else {
                        arr = null;
                    }
                }
                return (dataArr && dataArr.length > 0) ? JSON.parse("[" + dataArr.join(',') + "]") : [];
            }
        };

        /**
         * Map 类型定义
         * @param obj
         * @constructor
         */
        function Map(obj) {
            this.map = obj || {};
            this.size = 0;
        }

        /**
         * Map 原型扩展
         */
        Object.extend(Map, {});
        Object.extend(Map.prototype, {
            set: function (key, value) {
                if (!this.map.hasOwnProperty(key)) {
                    this.size++;
                }
                this.map[key] = value;
            },
            get: function (key) {
                if (this.map.hasOwnProperty(key)) {
                    return this.map[key];
                }
                return null;
            },
            delete: function (key) {
                if (this.map.hasOwnProperty(key)) {
                    this.size--;
                    return delete this.map[key];
                }
                return false;
            },
            keys: function () {
                var resultArr = [];
                for (var key in this.map) {
                    if (this.map.hasOwnProperty(key)) {
                        resultArr.push(key);
                    }
                }
                return resultArr;
            },
            values: function () {
                var resultArr = [];
                for (var key in this.map) {
                    if (this.map.hasOwnProperty(key)) {
                        resultArr.push(this.map[key]);
                    }
                }
                return resultArr;
            },
            has: function (key) {
                return this.map.hasOwnProperty(key);
            },
            clear: function () {
                this.map = {};
                this.size = 0;
            }
        });
    })(jQuery);
    return $;
});