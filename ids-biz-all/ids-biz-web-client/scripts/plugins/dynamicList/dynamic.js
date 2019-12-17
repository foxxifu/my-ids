define(['jquery', 'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function($) {
	
	/**
	 * 只获取已经选中的节点
	 */
	$.fn.dynamicOnlySelected = function(idName,filterFun) {
		return $(this).dynamic('dynamicOnlySelected',idName,filterFun);
	};
	/**
	 * 兼容之前的方法 获取选中 包括未加载的数据
	 */
	$.fn.dynamicSelected = function(idName,filterFun) {
		return $(this).dynamic('dynamicSelected',idName,filterFun);
	};

	/**
	 * 兼容之前的方法  没有的选择默认获取第一个
	 */
	$.fn.dynamicSelectsGetFirst = function() {
		return $(this).dynamic('dynamicSelectsGetFirst');
	};
	/**
	 * 兼容之前的方法 获取第一个
	 */
	$.fn.dynamicFirstId = function() {
		return $(this).dynamic('dynamicFirstId');
	};
	(function($) {
		$.widget("cems.dynamic", {
			//其中 options._z 是配置 id,和pid名称,可查看getMtreeSet
			//   options._c 配置样式，可查看getMcss   
			//   options._s 配置 Ztree 属性，可查看getMSetting
			/**
			 * 若外面 使用ztree 的expandNode 方法 则需要保证 callbackFlag 参数为true 第5个参数
			 * 例如 treeObj.expandNode(nodes[0], true, true, true,true);
			 */
			options: {
				context: '#main_view',//父框
				checkParmIsToLoad: true, //检测参数判定是否需要重新查询 默认true 参数是之前的查询参数和现在查询参数
				onSeachChange: false, //搜索框发送变化时的触发
				isClickToLoad: false, //是否第一次点击才进行加载 树 
				isRepeatClickLoad: false, //是否每次点击都进行重新加载  比 isClickToLoad 优先级高
				hasAllButton: true, //是否有全选按钮 'checkbox' 时 才生效，radio无全选按钮 就算设置也不生效
				checkCallBack: false, //选中回调方法
				beforeQueryParms: false, //查询前对参数的处理
				formatNode: false, //节点的format 处理 单个单个节点处理 若不需要此节点 放回null 需要的话 处理后 返回对应对象 入参数是Node
				queryName: 'name', //搜索的条件名称 比如 {page：1，pageSize：10，name：'输入的关键字'}
				chStyle: 'checkbox', //单选还是多选 默认多选   'checkbox' 'radio'
				seach: true, //是否支持搜索
				rp: 15, //默认页码 不建议改小  必须保证数据多的时候出现滚动条 以支持滚动加载
				totalPage: 1, //总页码
				onePageloadAfter: false, //此方法只会在第一加载成功后调用
				pageLoadAfter: false, //每次请求数据完成后调用 失败成功，error 都会  失败和成功 会返回后台返回的结果 error的情况放回空值
				url: '', //请求url 地址 
				/**
				 * 如果不需要插件来实现滚动加载 ，而后台来实现分页 此参数为false
				 * 且url查询需要支持分页
				 */
				urlToProtoType:true,
				page: 1, //首次请求的页码
				selectNode:[],//默认选中的记录 数组 值为id 此属性 适用于 protoMaxAuto=false 或者  原型数据条数小于protoMaxAutoExpand时,其它场景使用不是很好
				//以下是直接传入原型数据加载的  
				prototypeData:false,//原型数据 传入{id:"",name:""} 至少的属性  
				protoQueryName:"name",//原型数据时  默认搜索的属性字段
				/**
				 * 正则字符串 protoQueryName 前的匹配
				 *  例如   "([^{]*)\"model\":\"STATION\""  
				 * 此表示 匹配属性model=STATION 的 且model属性在protoQueryName 的前匹配 
				 * ([^{]*) 表示 model 并不是第一个属性值
				 */
				regProtoQueryBefore:false,
				regProtoQueryAfter:false,//正则字符串 protoQueryName 后的匹配
				protoSeachParent:true,//原型数据搜索节点时 是否显示父节点
				protoMaxAuto:true,//原型数据 是否支持自动滚动加载 
				protoMaxAutoExpand:2000,//原型数据 超过多少条数时，第一次加载不全部加载,  <0的值时 默认不使用自动加载,（超过时 加载每一级的子级 直到某个第一级的子级超过rp的值 或者某个节点是叶子节点）
				/**
				 * 参数 (ztreeData,ztreeDataTemp,rp,open) 
				 * ztreeData 数型结构的数组，全部数据
				 * ztreeDataTemp 需获取节点放置的数组
				 * rp 每页的条数 对应 预置的rp值
				 * open {open:true} 展开的对象 
				 * 不自定义则采用默认内部方法 _oneLoadGetNode
				 * 默认 加载每一级的第一级子级 直到第一级的子级超过rp的值 或者第一级的子级是叶子节点
				 */
				protoOneLoadGetNode:false,//自动加载时 第一次需要加载节点的自定义获取方法 
				protoBeginLoadRate:0.87,//当前滚动到 滚动条的高度*protoBeginLoadRate 时 进行自动加载 >0.4 <=1
				filterFun:false
			},
			_preCreate: function() {
				var id = this.element.attr('id');
				if (!id) {
					id = new Date().getTime();
					this.element.attr('id', id);
				}
				this.element.attr('readonly',true);
				var _z = this.options._z;
				var _c = this.options._c;
				var _s = this.options._s;
				this.options = $.extend(this.options, _m.getMoptions(id));
				delete this.options._z;
				delete this.options._c;
				delete this.options._s;
				this.treeSet = _m.getMtreeSet(_z);
				this.selMap = new Map();
				this.treeSetting = _m.getMSetting(this,_s);
				this.open = _m.getMopen();
				this.css = _c;
			},
			/***
			 * 创建元素时使用
			 * @private
			 */
			_create: function() {
				this._preCreate();
				this._creatDiv();
				this._common();
			},
			_creatDiv: function() {
				var _gThis = this;
				var _data = _gThis.options;
				var _treeSet = _gThis.treeSet;
				var _css = _m.getMcss(_gThis);
				var _P = function(selector){
					return $(_data.context).find(selector);
				}
				//数div
				var treeDiv = _P('#' + _data._treeDiv);
				if (!treeDiv || treeDiv.length <= 0) {
					var time = new Date().getTime();
					treeDiv = $('<div t="' + time + '" hidden=true class="' + _data._treeDivClass + '" id="' + _data._treeDiv + '"></div>');
					$(_data.context).append(treeDiv);
				}
				
				var loadingDiv = _P('#' + _data._loading);
				if (!loadingDiv || loadingDiv.length <= 0) {
					var time = new Date().getTime();
					loadingDiv = $('<div hidden=true class="'+_data._loadingClass+'" id="' + _data._loading + '"></div>');
					loadingDiv.append('<div class="dy_loading"><div>'+Msg.loading+'</div><div></div><div></div></div>');
					$(_data.context).append(loadingDiv);
					loadingDiv.css({
						'position': 'absolute',
						'overflow': 'auto'
					});
				}
				//设置样式
				treeDiv.css(_css);
				//关键字查询
				if (_data.seach) {
					var _seach = _P('#' + _data._seachInput);
					if (!_seach || _seach.length <= 0) {
						_seach = $('<input class="' + _data._seachClass + '" id="' + _data._seachInput + '"></input>');
						_seach.attr('placeholder', Msg.inputTheKey);
						treeDiv.append(_seach);
					}
					_seach.val('');
					_seach.css({
						'margin': '5px',
						'background': '#FBFBFB'
					});
					//输入变化后自动查询第一页的数据
					_seach.off('input propertychange').on('input propertychange', function() {
						if (_data.onSeachChange && $.isFunction(_data.onSeachChange)) {
							_data.onSeachChange($(this).val());
						}
						if(_data.url && !_data.urlToProtoType){
							_gThis.getData(1);
						}else{
							if(_data.prototypeData ){
								_gThis._showLoading(_data);
								_gThis._toAddData(_m._protoQueryFun($(this).val(),$.extend({},_gThis)),_gThis,_gThis.open);
							}
						}
					});
				} else {
					_P('#' + _data._seachInput).remove();
				}
				//树结构
				var tree = _P('#' + _data._treeId);
				if (_data.hasAllButton && _data.chStyle && _data.chStyle == 'checkbox') {
					var _p = _P('#' + _data._allSelectId + "_p");
					if ((!_p || _p.length <= 0)) {
						var _all = _P('#' + _data._allSelectId);
						var _span = _P('#' + _data._allSelectId + "_span");
						var _label = _P('#' + _data._allSelectId + "_label");
						if (!_p || _p.length <= 0) {
							_p = $("<p id='" + _data._allSelectId + "_p'></p>");
						}
						if (!_label || _label.length <= 0) {
							_label = $("<label id='" + _data._allSelectId + "_label'></label>");
						}
						if (!_all || _all.length <= 0) {
							_all = $("<input type='checkbox' class='check-un' id='" + _data._allSelectId + "'></input>");
						}
						if (!_span || _span.length <= 0) {
							_span = $("<span  id='" + _data._allSelectId + "_span'>" + Msg.allOrNotAll + "</span>");
						}
						_label.append(_all);
						_label.append(_span);
						_p.append(_label);
						if (tree && tree.length > 0) {
							tree.prepend(_p);
						} else {
							treeDiv.append(_p);
						}
						_span.css({
							'margin-left': '10px'
						});
						_label.css({
							'cursor': 'pointer',
							'margin-left': '5px'
						});
						_P('#' + _data._allSelectId + "_p").off('click').on('click', function() {
							var _thisCheck = $('#' + _data._allSelectId);
							_gThis.toCheckedAll(_thisCheck[0].checked);
						});
					}
				} else {
					_P('#' + _data._allSelectId + "_p").remove();
					_P('#' + _data._allSelectId).remove();
					_P('#' + _data._allSelectId + "_span").remove();
				}
				//树结构
				if (!tree || tree.length <= 0) {
					var maxH = _css['max-height'] || 300 - (_P('#' + _data._seachInput).outerHeight() || 0)
					- (_P('#' + _data._allSelectId + "_p").outerHeight() || 0);
					tree = $('<ul class="ztree" id="' + _data._treeId + '"></ul>')
					treeDiv.append(tree);
					tree.css({
						'margin-left': -6,
						'max-height':maxH,
						'overflow-y': 'auto'
					});
					//滚动自动加载 
					tree.off('scroll').on('scroll', function(e) {
						var _$this = $(this);
						var sTop = _$this.scrollTop();
						var height = _$this.height();
						var scHeight = _$this[0].scrollHeight;
						if (_data.url && !_data.urlToProtoType) {
							if((sTop + height >= scHeight - 10)){
								_data.page = _data.page + 1;
								if (_data.page > _data.totalPage) {
									_data.page = _data.totalPage;
									return;
								}
								_gThis.getData();
							}
						}else{
							var rate = _data.protoBeginLoadRate || 0.87;
							rate = rate > 1 || rate <=0.4 ? 0.87 : rate;
							if(_data.protoMaxAuto && _data.prototypeData && sTop + height >= scHeight*rate){
								if(_data.lastTid && $(e.target).closest("#" +_data.lastTid).length == 0){
									_gThis._autoLoadProtoTypeData(_gThis,_data.lastTid);
								}else if(_data.oneLevelLasId && $(e.target).closest("#" +_data.oneLevelLasId).length == 0){
									_gThis._autoLoadProtoTypeData(_gThis,_data.oneLevelLasId);
								}
							}
						}
						
					});
				}
			},
			/**
			 * 内部方法 滚动加载原型数据时获取 需要动态加载的父节点
			 * 并调用_protoAddData 方法动态加载数据
			 */
			_autoLoadProtoTypeData:function(_gThis,tid){
				var _data = _gThis.options;
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				var node = treeObj.getNodeByTId(tid);
				var treeNode = node.getParentNode();//treeNode是空场景是要加载第一级
				this._protoAddData(_gThis,treeObj,treeNode,true,!treeNode && node);
			},
			//加载原型数据的处理方法  次方法 不处理默认选中的值
			//isMoreRp 表示  是否允许加载超过 _data.rp 的条数  
			//checked 所加载节点勾选状态 没有则默认使用之前的
			_protoAddData : function(_gThis,treeObj,treeNode,isMoreRp,node){
				var _data = _gThis.options,_treeSet = _gThis.treeSet;
				var _da = _data.prototypeSeachZtreeData;
				var checked = false,index = 0,pchilds;
				if(!treeNode && node){ // 加载第一级
					var _thisCheck = $(_data.context).find('#' + _data._allSelectId);
					if(_data.chStyle == "checkbox" && _thisCheck && _thisCheck[0].checked){
						checked = true;
					}
					if(_da.length<_da.rp || ( _da[_da.length-1][_treeSet.id] == node[_treeSet.id])){
						return;
					}
					pchilds = $.extend(true,[],_da);
				}else{
					if(_data.chStyle == "checkbox" && 
							((treeNode.children && treeNode.check_Child_State == 2) || 
									(!treeNode.children && treeNode.checked))){
						checked = true;
					}
					var pidArr = [treeNode[_treeSet.id]];
					var pNode = treeNode.getParentNode();
					while(pNode){
						pidArr.unshift(pNode[_treeSet.id]);
						pNode = pNode.getParentNode();
					}
					if(treeNode.children){
						index = treeNode.children.length;
						_data.lastTid = treeNode.children[index-1].tId;
					}
					if((!isNaN(treeNode.childSize) && index>=treeNode.childSize) || (!isMoreRp &&  index>=_data.rp)){
						return;
					}
					pchilds = _m._toGetChildrenNodes(_da,pidArr,_treeSet.id);
				}
				if(pchilds){
					if(treeNode && isNaN(treeNode.childSize)){
						treeNode.childSize = pchilds.length;
						treeObj.updateNode(treeNode);
					}
					var ztreeDataTemp = [];
					var hasLoadLast = false;
					$.each(pchilds,function(t,e){
						if(t>=index && t<index+_data.rp && !isNaN(t)){
							e.children = null;
							e.checked = checked;
							checked && _m._addSelectData(_gThis,e);
							ztreeDataTemp.push(e);
							if(pchilds.length-1 == t){
								hasLoadLast = true;
								if(!treeNode && node ){
									_data.oneLevelLasId = null;
								}else{
									_data.lastTid =  null;
								}
							}
						}
						if(t >= index+_data.rp || isNaN(t)){
							return false;
						}
					});
					if(ztreeDataTemp && ztreeDataTemp.length>0){
						treeObj.addNodes(treeNode,ztreeDataTemp);
						var lTid = treeNode.children[treeNode.children.length-1].tId;
						if(!hasLoadLast){
							if(!treeNode && node ){
								_data.oneLevelLasId = lTid;
							}else{
								_data.lastTid =  lTid;
							}
						}
					}
				}
			},
			/**
			 * 显示正在加载
			 */
			_showLoading : function(_data){
				var loading = _data._loading,treeDiv = _data._treeDiv;
				var _P = function(selector){
					return $(_data.context).find(selector);
				}
				var tree = _P('#'+treeDiv);
				if(!tree.is(':visible')){
					return;
				}
				_P("#"+loading).css({
					 width:tree.outerWidth(),
					 height:tree.outerHeight(),
				     'background-color': 'rgba(0, 0, 0, .5)',
				     top:tree.offset().top,
				     'z-index':201,
				     left:tree.offset().left
				});
				var _py_loading = _P("#"+loading).find('.dy_loading');
				var ml = (tree.outerWidth() - _py_loading.outerWidth())/2;
				var mt = (tree.outerHeight() - _py_loading.outerHeight())/2;
				_py_loading.css({
					"margin-left": ml <0 ? 0: ml,
					"margin-top": mt <0 ? 0: mt
				});
				_P("#"+loading).show();
			},
			/**
			 * 隐藏正在加载
			 */
			_hideLoading: function(_data,seach){
				var _P = function(selector){
					return $(_data.context).find(selector);
				}
				_P("#"+_data._loading).hide();
				seach && ($(seach).removeAttr('readOnly'));
			},
			//控制页面滚动时 隐藏树的div
			_common: function() {
				var _gThis = this;
				var _data = _gThis.options;
				var scrollTop = -1;
				$('.body').mousemove(function() {
					if (!$('#' + _data._treeDiv).get(0) || $('#' + _data._treeDiv).is(':hidden')) {
						scrollTop = $(this).scrollTop();
					}
				});
				$('.body').scroll(function() {
					if (scrollTop != -1 && $('#' + _data._treeDiv).is(':visible')) {
						$(this).scrollTop(scrollTop);
					}
				});
				$(window).resize(function() {
					$('#' + _data._treeDiv).hide();
					_gThis._hideLoading(_data);
				});
			},
			//全选或者全部选方法
			toCheckedAll: function(isChecked) {
				isChecked = isChecked || false;
				var _data = this.options;
				var _thisCheck = $('#' + _data._allSelectId);
				if (isChecked) {
					_thisCheck.addClass('check-on');
				} else {
					_thisCheck.removeClass('check-on');
				}
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				if (treeObj) {
					var nodes = treeObj.getNodes();
					$.each(nodes, function(t, e) {
						if(isChecked != e.checked){
							treeObj.checkNode(e, isChecked, true, true);
						}
					});
				}
			},
			/**
			 * 通过url 获取数据的方法
			 */
			getData: function(page) {
				var _gThis = this;
				var _data = this.options;
				if(!_data.url){
					return;
				}
				_gThis._showLoading(_data);
				var _this = $(this.element);
				var _setting = this.treeSetting;
				var _param = {
					page: page ? page : _data.page,
					pageSize: _data.rp
				};
				//关键字查询
				var qName = $('#' + _data._seachInput).val();

				if (_data.beforeQueryParms && $.isFunction(_data.beforeQueryParms)) {
					var _Parms = _data.beforeQueryParms(_param, qName);
					_param = _Parms || _param;
				} else {
					if (qName) {
						//放入到自定义的 queryName 中
						//目前还放到queryParms 中 再用 queryName 作为二级key
						var pname = _data.queryName;
						var map = {};
						map[pname] = qName;
						_param.queryParms = map;
						_param[pname] = qName;
					}
				}
				//检测是否需要查询 默认true
				if ($.isFunction(_data.checkParmIsToLoad)) {
					var toQuery = _data.checkParmIsToLoad(_data.parParms, _param);
					if (!toQuery) {
						_gThis._hideLoading(_data,$(_data.context).find('#' + _data._seachInput));
						return;
					}
				}
				_data.parParms = $.extend({}, _param);
				$.http.post(_data.url, _param, function(data) {
					var ro = data;
					if (ro.success) {
						_data.page = ro.data.pageNo;
						_data.totalPage = parseFloat((ro.data.total + ro.data.pageSize - 1) / ro.data.pageSize).fixed(0);
						var nodes = ro.data.list || ro.data;
						if(_data.urlToProtoType){
							_data.page = 1;
							_data.totalPage = 1;
							_data.prototypeData =$.extend(true,[],nodes);
							_data.prototypeSeachData = $.extend(true,[],nodes);
						}
						//加载数据
						_gThis._toAddData(nodes,_gThis);
					} else {
						_gThis._hideLoading(_data,$(_data.context).find('#' + _data._seachInput));
						$.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter(data);
					}
				}, function() {
					_gThis._hideLoading(_data,$(_data.context).find('#' + _data._seachInput));
					$.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter();
				});
			},
			/**
			 * 内部方法 初始化ztree 
			 */
			_toAddData: function(nodes,gThis,open){
				open = open || gThis.open;
				var btime = new Date().getTime();
				var _data = gThis.options,_this = $(gThis.element),
				 _setting = gThis.treeSetting,_treeSet = gThis.treeSet,data = [];
				if (!nodes) {
					nodes = [];
				}
				var isRestVal = false;
				var pTreeObj = $.fn.zTree.init($('#'+new Date().getTime()),_setting,[]);
				var tData = pTreeObj.transformTozTreeNodes($.extend(true,[],nodes));
				var tDataArr = pTreeObj.transformToArray($.extend(true,[],tData) );
				$.each(tDataArr,function(t,e){
					e = $.extend(e, open);
					if ($.isFunction(_data.formatNode)) {
						e = _data.formatNode(e);
					}
					if(e){
						if(!$.isEmptyObject(_data.selectNode) && _data.selectNode &&
								_data.selectNode.indexOf(e[_treeSet.id])>=0){
							e.checked = true;
							_m._addSelectData(gThis,e,nodes.length);
							isRestVal = true;
						}
						e.children = null;
						e && data.push(e);
					}
				});
				tData = pTreeObj.transformTozTreeNodes($.extend(true,[],data));
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				if (_data.page > 1 && treeObj) {
					treeObj.addNodes(null, data);
				} else {
					var _thisCheck = $(_data.context).find('#' + _data._allSelectId);
					if (_thisCheck) {
						_thisCheck.removeClass('check-on');
						_thisCheck.checked = false;
					}
					gThis.selMap = new Map();
					gThis.firstId = null;
					!isRestVal && (_this.val(''));
					if (data.length > 0) {
						gThis.firstId = data[0][_treeSet.id];
					}
					if(_data.prototypeData){
						_data.page = 1;
						_data.totalPage = 1;
						if(!_data.protoTreeSet){
							_data.protoTreeSet = true;
							_data.prototypeData = $.extend(true,[],data);
						}
						_data.prototypeSeachData = $.extend(true,[],data);
						_data.prototypeSeachZtreeData = $.extend(true,[],tData);
						if(_data.protoMaxAuto &&
								data.length >= _data.protoMaxAutoExpand 
								&& _data.protoMaxAutoExpand>0){
							var ztreeData = $.extend(true,[],tData);
							var  ztreeDataTemp = [];
							if(_data.protoOneLoadGetNode && $.isFunction(_data.protoOneLoadGetNode)){
								_data.protoOneLoadGetNode(ztreeData,ztreeDataTemp,_data.rp,open);
							}else{
								gThis._oneLoadGetNode(ztreeData,ztreeDataTemp,_data.rp,open);
							}
							var lastId = ztreeDataTemp[ztreeDataTemp.length-1][_treeSet.id];
							var tObj = $.fn.zTree.init($(_data.context).find('#' + _data._treeId), _setting, ztreeDataTemp);
							var nodes = tObj.getNodesByParamFuzzy(_treeSet.id, lastId, null);
							//判断第一级是否可完全加载
							if(ztreeData.length > _data.rp){
								_data.oneLevelLasId = nodes[0].tId;
							}else{
								_data.lastTid = nodes[0].tId;
							}
						}else{
							$.fn.zTree.init($(_data.context).find('#' + _data._treeId), _setting, data);
						}
					}else{
						$.fn.zTree.init($(_data.context).find('#' + _data._treeId), _setting, data);
					}
					if (!_data._one) {
						$.isFunction(_data.onePageloadAfter) && _data.onePageloadAfter();
						_data._one = true;
					}
					pTreeObj.destroy();
				}
				gThis._hideLoading(_data,$(_data.context).find('#' + _data._seachInput));
				$.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter(data);
			},
			/**
			 * 内部方法
			 * 需动态加载时 获取第一次需要加载的数据
			 * ztreeData ztree结构的数据，ztreeDataTemp 存放的数组，rp 获取rp条,isToChildren 从子节点中获取的条数
			 * isChild 是否在加载子节点
			 */
			_oneLoadGetNode : function(ztreeData,ztreeDataTemp,rp,open){
				var gThis = this;
				var children = {};
				var isToloadChild = (ztreeData.length<rp);
				$.each(ztreeData,function(t,e){
					if(isNaN(t) || t>=rp ){
						return false;
					}
					var temp = {children:null,open:false};
					if(e.isParent && e.children){
						temp.childSize = e.children.length;
					}
					if(t == 0 && isToloadChild){
						children = e.children;
						temp = $.extend(temp,open);
					}
					ztreeDataTemp.push($.extend(true,{},e,temp));
					if(t == ztreeData.length-1 && isToloadChild && !$.isEmptyObject(children)){
						gThis._oneLoadGetNode(children,ztreeDataTemp,rp,open);
					}
				});
			},
			/**
			 * 初始化
			 * @private
			 */
			_init: function() {
				var _gThis = this;
				var _data = _gThis.options;
				var _this = $(_gThis.element);
				var id = _data.id;
				if(_data.prototypeData){
					_data.prototypeSeachData = _data.prototypeData;
				}
				if(!_data.isClickToLoad){
					_data.url ? (_gThis.getData()) : _gThis._toAddData(_data.prototypeData,_gThis);
				}
				//数隐藏
				$(document).on('click', function(e) {
					if ($(e.target).closest("#" + _data._loading).length == 0 &&
							($(e.target).closest("#" + _data._treeDiv).length == 0 && $(e.target).closest("#" + id).length == 0)) {
						$(_data.context).find('#' + _data._treeDiv).hide();
						_gThis._hideLoading(_data);
					}
				});
				//树显示
				_this.off('click').on('click', function() {
					var treeDiv = $(_data.context).find('#' + _data._treeDiv);
					//设置样式
					var _css = _m.getMcss(_gThis);
					treeDiv.css(_css);
					treeDiv.show();
					var maxH = (_css['max-height'] || 300) - ($(_data.context).find('#' + _data._seachInput).outerHeight(true) || 0)
					- ($(_data.context).find('#' + _data._allSelectId + "_p").outerHeight(true) || 0);
					var tree = $(_data.context).find('#'+_data._treeId);
					tree.css({
						'max-height':maxH,
						'overflow-y': 'auto'
					});
					if(_data.isRepeatClickLoad){
						_data.url ? (_gThis.getData()) : _gThis._toAddData(_data.prototypeData,_gThis);
					}else if( _data.isClickToLoad && !_data._one){
						_data.url ? (_gThis.getData()) : _gThis._toAddData(_data.prototypeData,_gThis);
					}
				});
				//				this.element.data("dynamic",this);
				return this.element;
			},
			_setOption: function(key, value) {
				$.Widget.prototype._setOption.apply(this, arguments);
			},

			_setOptions: function(options) {
				var key;
				for (key in options) {
					this._setOption(key, options[key]);
				}
				return this;
			},
			printOptions: function() {
				console.info(this.options);
			},
			/**
			 * 销毁
			 * @private
			 */
			_destroy: function() {
				var _gThis = this;
				var _data = _gThis.options;
				$(_data.context).find('#' + _data._treeDiv).remove();
				$(_data.context).find('#' + _data._loading).remove();
				//$(this.element).removeData('dynamic')
				return this._super();
			},
			//获取选中的节点
			_getSelectedNodeIds : function(nodes,gThis,idName,filterFun){
				var _this = $(gThis.element);
				var _data = gThis.options;
				var _treeSet = gThis.treeSet;
				var _da = _$.extend(true,[],_data.prototypeSeachZtreeData);
				var idrr = [];
				$.each(nodes,function(t,e){
					var isToAdd = true;
					if(filterFun && $.isFunction(filterFun)){
						isToAdd = filterFun($.extend(true,{},e));
					}
					isToAdd && (idrr.push(e[idName]));
					if(e.checked && e.isParent){
						var index = -1;
						//父节点选中 存在子节点 且 子节点未加载完成 只加载一部分 且是全选状态 （不存在 子节点未选中） 
						if(e.check_Child_State == 2 && e.childSize && e.childSize!=e.children.length){
							index = e.children.length;
						}else if(!e.childSize){
							index = 0;//父节点选中 未开始加载
						}
						if(index >=0){
							var pidArr = [e[_treeSet.id]];
							var pNode = e.getParentNode();
							while(pNode){
								pidArr.unshift(e[_treeSet.id]);
								pNode = pNode.getParentNode();
							}
							var ids = _m._toGetChildrenId(_da,pidArr,idName,_treeSet.id,index,filterFun);
							(ids && ids.length>0) && (idrr = idrr.concat(ids));
						}
					}
					if(e.children){
						var ids = gThis._getSelectedNodeIds(e.children,gThis,idName,filterFun);
						(ids && ids.length>0) && (idrr = idrr.concat(ids));
					}
				});
				return idrr;
			},
			/**
			 * 只获取选中的节点
			 */
			dynamicOnlySelected : function(idName,filterFun){
				var _this = $(this.element);
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				var idArr = [];
				if(!treeObj) return idArr;
				idName = idName || this.treeSet.id;
				var nodeArr = treeObj.getCheckedNodes();
				if(nodeArr && nodeArr.length>0){
					$.each(nodeArr,function(t,e){
						var isAdd = true;
						if(filterFun && $.isFunction(filterFun)){
							isAdd = filterFun($.extend(true,{},e));
						}
						isAdd && (idArr.push(e[idName]));
					});
				}
				return $.extend([], idArr);
			},
			/**
			 * 获取选中 
			 */
			dynamicSelected: function(idName,filterFun) {
				var _this = $(this.element);
				var _data = this.options;
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				var idArr = [];
				if(!treeObj) return idArr;
				var treeSet = this.treeSet;
				idName = idName || treeSet.id;
				if(_data.protoMaxAuto && _data.chStyle == "checkbox" && _data.protoMaxAutoExpand > 0 && _data.prototypeData && 
						_data.prototypeData.length>_data.protoMaxAutoExpand
						&& treeObj.transformToArray(treeObj.getNodes()).length!=_data.prototypeData.length){
					var _thisCheck = $(_data.context).find('#' + _data._allSelectId);
					//全选
					if(_thisCheck && _thisCheck[0].checked){
						var sNodes = $.extend(true,[],_data.prototypeSeachData);
						$.each(sNodes,function(t,e){
							var isAdd = true;
							if(filterFun && $.isFunction(filterFun)){
								isAdd = filterFun($.extend(true,{},e));
							}
							isAdd && (idArr.push(e[idName]));
						});
					}else{
						var nodes = treeObj.transformTozTreeNodes(treeObj.getCheckedNodesNotChildren(true));
						idArr = this._getSelectedNodeIds(nodes,this,idName,filterFun);
					}
					
				}else{
					var nodeArr = treeObj.getCheckedNodes();
					if(nodeArr && nodeArr.length>0){
						$.each(nodeArr,function(t,e){
							var isAdd = true;
							if(filterFun && $.isFunction(filterFun)){
								isAdd = filterFun($.extend(true,{},e));
							}
							isAdd && (idArr.push(e[idName]));
						});
					}
				}
				return $.extend([], idArr);
			},
			/**
			 * 获取选中  没有的选择默认获取第一个
			 */
			dynamicSelectsGetFirst: function() {
				var arr = this.dynamicSelected();
				if (!arr || arr.length <= 0) {
					var ids = this.dynamicFirstId();
					if (ids) {
						arr = ids;
					}
				}
				return $.extend([], arr);
			},
			/**
			 * 获取第一个
			 */
			dynamicFirstId: function() {
				var arr = [];
				if(this.firstId){
					arr.push(this.firstId);
				}
				return $.extend([], arr);
			}
		});
		/**
		 * 额外的方法 主要用于构建 css options treeSetting
		 */
		var _m = {
			getMoptions: function(id) {
				return {
					_treeDiv: id + "_select_tree_div", //展示的div的id
					_treeDivClass: "_select_tree_div", //展示div 的class
					_treeId: id + "_select_tree", //数id
					_loading: id+"_loading",
					_loadingClass:"dyloading",
					_seachInput: id + "_seach_input", //搜索输入框id
					_seachClass: '_seach_input_class',
					_allSelectId: id + "_all_select",
					id: id
				}
			},
			getMcss: function(excur) {
				var _this = excur.element;
				var _data = excur.options;
				var _c = excur.css;
				var inputPosition = _this.offset();
				var _css = $.extend({
					'position': 'absolute',
					'top': inputPosition.top + _this.outerHeight() + 5 + "px",
					'left': inputPosition.left + "px",
					'max-height': 300, //最大高度 不可改变
					'overflow-y': 'hidden',
					'background': '#eee',
					'border': '1px solid #ccc',
					'z-index': 200,
					'width': 'auto',
					'min-width':_this.outerWidth(),
					'max-width': 300 //最大宽度
				}, _c,{'max-height': 300});
				return _css;
			},
			getMtreeSet: function(_z) {
				return $.extend({
					'id': 'id', //配置id
					'pid': 'pid', //配置pid
					'isParent':'isParent'
				}, _z);
			},
			getMopen: function() {
				return {
					'open': true
				};
			},
			getMSetting: function(excur,_s) {
				var _data = excur.options;
				var _treeSetting = excur.treeSet;
				var _this = $(excur.element);
				var _setting = {
					check: {
						enable: true,
						autoCheckTrigger: true,
						chkStyle: (!_data.chStyle) ? "checkbox" : _data.chStyle
					},
					view: {
						dblClickExpand: true,
						showLine: false,
						selectedMulti: false,
						showIcon: false
					},
					data: {
						simpleData: {
							enable: true,
							idKey: _treeSetting.id,
							pIdKey: _treeSetting.pid,
							isParentKey:_treeSetting.isParent
						}
					},
					callback: {
						onClick: function(e, treeId, treeNode) {
							var treeObj = $.fn.zTree.getZTreeObj(treeId);
							treeObj.checkNode(treeNode, !treeNode.checked, true, true);
						},
						onCheck: function(e, treeId, treeNode) {
							var treeObj = $.fn.zTree.getZTreeObj(treeId);
							_m._addSelectData(excur,treeNode);
							if(!treeNode.checked){
								treeObj.cancelSelectedNode(treeNode);
							}
							$.isFunction(_data.checkCallBack) && _data.checkCallBack(e, treeId, treeNode);
						},
						beforeExpand:function(treeId, treeNode){
							_data.lastTid = null;
							var pNode = _data.curExpandNode ? _data.curExpandNode.getParentNode():null;
							var treeNodeP = treeNode.parentTId ? treeNode.getParentNode():null;
							var zTree = $.fn.zTree.getZTreeObj(treeId);
							for(var i=0, l=!treeNodeP ? 0:treeNodeP.children.length; i<l; i++ ) {
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
								_m._singlePath(treeNode,treeId,_data);
							}
						},
						onCollapse:function(e,treeId, treeNode){
							_data.lastTid = null;
						},
						onExpand:function(e,treeId, treeNode){
							_data.curExpandNode = treeNode;
							if(_data.prototypeData){
								var treeObj = $.fn.zTree.getZTreeObj(treeId);
								excur._protoAddData(excur,treeObj,treeNode,false);
							}
						}
					}
				};
				if(_setting.check.chkStyle !== "checkbox"){
					_setting.check.radioType = "all";
				}else{
					_setting.check.chkboxType = { "Y": "ps", "N": "ps" };
				}
				return $.extend(_setting, _s);
			},
			_singlePath:function(newNode,treeId,_data){
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
	                for (i=0, j=rootNodes.length; i<j; i++) {
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
						if (newParents!=null) {
							var oldNode = _data.curExpandNode;
							var oldParents = [];
							while (oldNode) {
								oldNode = oldNode.getParentNode();
								if (oldNode) {
									oldParents.push(oldNode);
								}
							}
							if (newParents.length>0) {
								zTree.expandNode(oldParents[Math.abs(oldParents.length-newParents.length)-1], false);
							} else {
								zTree.expandNode(oldParents[oldParents.length-1], false);
							}
						}
					}
				}
	            _data.curExpandNode = newNode;
			},
			//从_da 中获取   id = pidArr最后一个值的 node节点 的子节点对象 不深度查找
			_toGetChildrenNodes : function(_da,pidArr,_id){
				var finData ;
				$.each(_da,function(t,e){
					if(e[_id] == pidArr[0]){
						var id = pidArr.splice(0,1);
						if(pidArr.length == 0){//最后一个
							finData = $.extend(true,[],e.children);
							return false;
						}else{
							finData = _m._toGetChildrenNodes(e.children,pidArr,_id);
						}
					}
				});
				return finData;
			},
			/**
			 * 从_da 中获取   id = pidArr最后一个值的 node节点 的子节点 cNode
			 * 并调用 _toGetAllChildrenIds 方法 获取  cNode的idName 属性  index=0 则深度查找其下所有的子节点的idName 属性
			 */
			_toGetChildrenId : function(_da,pidArr,idName,_id,index,filterFun){
				var finData =[];
				$.each(_da,function(t,e){
					if(e[_id] == pidArr[0]){
						pidArr.splice(0,1);
						if(pidArr.length == 0){//最后一个
							_m._toGetAllChildrenIds(e.children,index,finData,idName,filterFun);
							return false;
						}else{
							finData = _m._toGetChildrenId(e.children,pidArr,idName,_id,index,filterFun);
						}
					}
				});
				return finData;
			},
			/**
			 * 从nodes中获取  节点 的idName属性   index=0 则深度查找其下所有的子节点的idName 属性
			 */
			_toGetAllChildrenIds : function(nodes,index,idArr,idName,filterFun){
				for(var i = index;i<nodes.length;i++){
					var et = nodes[i];
					var isToAdd = true;
					if(filterFun && $.isFunction(filterFun)){
						isToAdd = filterFun($.extend(true,{},et));
					}
					isToAdd && (idArr.push(et[idName]));
					if(et.children && et.children.length>0){
						_m._toGetAllChildrenIds(et.children,index,idArr,idName,filterFun);
					}
				}
			},
			/**
			 * 添加选择的节点
			 */
			_addSelectData : function(excur,treeNode,totalNum){
				var _data = excur.options;
				var _treeSetting = excur.treeSet;
				var selMap = excur.selMap;
				var _this = $(excur.element);
				var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
				if (!_data.chStyle || _data.chStyle == 'checkbox') {
					if (treeNode.checked) {
						selMap.set(treeNode[_treeSetting.id],treeNode.name);
					} else {
						selMap.delete(treeNode[_treeSetting.id])
					}
				} else {
					if (treeNode.checked) {
						selMap = new Map();
						selMap.set(treeNode[_treeSetting.id],treeNode.name);
					}
				}
				_this.val(selMap.values().join(","));
				
				var total = totalNum;
				if(!totalNum && treeObj){
					total = treeObj.transformToArray(treeObj.getNodes()).length;
				}
				if (_data.hasAllButton && _data.chStyle && _data.chStyle == 'checkbox') {
					var _thisCheck = $(_data.context).find('#' + _data._allSelectId);
					if (!treeNode.checked) {
						_thisCheck.removeClass('check-on');
						_thisCheck[0].checked = false;
					}
					if (selMap.values().length == 0) {
						_thisCheck.removeClass('check-on');
						_thisCheck[0].checked = false;
					} else if (selMap.values().length == total) {
						_thisCheck.addClass('check-on');
						_thisCheck[0].checked = true;
					}
				}
			},
			_escapeChars:function(chars){
				if(!chars || chars.length<=0) return chars;
				var arr = [];
				var special  = "$()*+.[]?\\^{}|";
				for(var i = 0;i<chars.length;i++){
					var char = chars[i];
					if(special.indexOf(char)>=0){
						if(char == "\\"){
							arr.push("\\\\\\"+char);
						}else{
							arr.push("\\"+char);
						}
					}else{
						arr.push(char);
					}
				}
				return arr.join("");
			},
			//原型数据默认搜索方法   seachVal 搜索的关键词 gThis 当前的一个对象 有很多可用方法
			_protoQueryFun:function(seachVal,gThis){//原型数据默认搜索方法   seachVal 搜索的关键词 gThis 当前的一个对象 有很多可用方法
				var _data = gThis.options;
				var _treeSet = gThis.treeSet;
				var data = _data.prototypeData,reg,protoSeachParent =_data.protoSeachParent,pid = _treeSet.pid,id = _treeSet.id,protoQueryName=_data.protoQueryName;
				if(!data || data.length<=0){
					return data;
				}
				if(seachVal == ""){
					return $.extend(true,[],data);
				}
				seachVal = _m._escapeChars(seachVal);
				var isString = (typeof data[0][pid]) == "string";
				var sep = isString ? "\"" : "";
				var regB = _data.regProtoQueryBefore ? _data.regProtoQueryBefore: "";
				var regA = _data.regProtoQueryAfter ? _data.regProtoQueryAfter : "";
				if(protoQueryName){
					//区分 值前面有无"  区分结尾有无,
					//区分 值前面有无"  区分结尾有无,
					reg = new RegExp("({)"+regB+"([^{]*)\""+protoQueryName+"\"\:(\"([^,{]*)"+seachVal+"(([^{,\"]*\","+regA+"[^{]*})|([^{,\"]*}))|([^,\"{]*)"+seachVal+"(([^{,\"]*,"+regA+"[^{]*})|([^{,\"]*})))","ig");
				}else{
					//无属性名称关键查询 全查询 未测试
					reg = new RegExp("({)"+regB+"([^{]*)\"([^{\",]*)\"\:(\"([^,{]*)"+seachVal+"(([^{,\"]*\","+regA+"[^{]*})|([^{,\"]*}))|([^,\"{]*)"+seachVal+"(([^{,\"]*,"+regA+"[^{]*})|([^{,\"]*})))","ig");
				}
				var parentSeach = [];
				var datas = [];
				var queryStr = JSON.stringify(data);
				var arr = queryStr.match(reg);
				var regt = new RegExp("\""+pid+"\":"+sep+"([^{,]*)"+sep+"(,|})",'ig');
				while(arr && arr.length>0){
					var arrStr = "["+arr.join(',')+"]";
					datas = datas.concat(JSON.parse(arrStr));
					if(protoSeachParent){
						var pids = arr.toString().match(regt);
						if(pids && pids.length>0){
							$.each(pids,function(t,e){
								e = "("+e.replace(",","").replace(pid,id)+")";
								if(parentSeach.indexOf(e)<0){
									parentSeach.push(e);
								}
							})
						}
						if(parentSeach.length>0){
							queryStr = queryStr.replace(reg,"");
							reg = new RegExp("{([^{]*)("+parentSeach.join("|")+")([^{]*)\""+_treeSet.isParent+"\"\:true([^{]*)}",'ig');
							arr = queryStr.match(reg);
							parentSeach = [];
						}
					}else{
						arr = null;
					}
				}
				return datas;
			}
		};
		/**
		 * Map 类型定义
		 * @param obj
		 * @constructor
		 */
		function Map(obj) {
			this.map = {};
			this.size = 0;
		}
		/**
		 * Map 原型扩展
		 */
		Object.extend(Map, {});
		Object.extend(Map.prototype, {
			set: function(key, value) {
				if (!this.map.hasOwnProperty(key)) {
					this.size++;
				}
				this.map[key] = value;
			},
			get: function(key) {
				if (this.map.hasOwnProperty(key)) {
					return this.map[key];
				}
				return null;
			},
			delete: function(key) {
				if (this.map.hasOwnProperty(key)) {
					this.size--;
					return delete this.map[key];
				}
				return false;
			},
			keys: function() {
				var resultArr = [];
				for (var key in this.map) {
					if (this.map.hasOwnProperty(key)) {
						resultArr.push(key);
					}
				}
				return resultArr;
			},
			values: function() {
				var resultArr = [];
				for (var key in this.map) {
					if (this.map.hasOwnProperty(key)) {
						resultArr.push(this.map[key]);
					}
				}
				return resultArr;
			},
			has: function(key) {
				return this.map.hasOwnProperty(key);
			},
			clear: function() {
				this.map = {};
				this.size = 0;
			}
		});
	})(jQuery);
	return $;
});