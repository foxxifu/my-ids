/**
 * 动态加载下拉 目前只支持1级
 * id 必须有(没有默认获取当前时间作为id) 唯一
 */
;
define(['jquery', 'zTree', 'zTree.excheck', 'zTree.exedit', 'zTree.exhide'], function ($) {

    if ($.fn.dynamic) {
        return $;
    }
    //存储选择的节点
    var selMap = new Map();
    /**
     * 获取选中 
     */
    $.fn.dynamicSelected = function(){
    	var _this = $(this);
    	var id = _this.attr('id');
    	var arr = [];
    	var mthis = selMap.get(id);
        if(mthis){
        	arr = mthis.get('ids');
        }
    	return arr;
    };
    
    /**
     * 获取选中  没有的选择默认获取第一个
     */
    $.fn.dynamicSelectsGetFirst = function(){
    	var _this = $(this);
    	var id = _this.attr('id');
    	var mthis = selMap.get(id);
    	var arr = [];
        if(mthis){
        	arr = mthis.get('ids');
        }
    	if(!arr || arr.length<=0){
    		var ids = _this.dynamicFirstId();
    		if(ids){
    			arr = ids;
    		}
    	}
    	return arr;
    };
    /**
     * 获取第一个
     */
    $.fn.dynamicFirstId = function(){
    	var _this = $(this);
    	var id = _this.attr('id');
    	var arr = [];
    	var mthis = selMap.get(id);
        if(mthis){
        	var id = mthis.get('first');
        	if(id){
        		arr.push(id);
        	}
        }
    	return arr;
    };
    /**
     * _p 基本配置 _c div样式 树配置
     */
    $.fn.dynamic = function ( _p,_c,_z) {
    	_this = $(this);
    	var id = _this.attr('id');
    	if(!id){
    		id = new Date().getTime();
    		_this.attr('id',id);
    	}
    	selMap.set(id,new Map());
    	_this.attr('readOnly',true);
    	var _data = $.extend({
			_treeDiv:id+"_select_tree_div", //展示的div的id
			_treeDivClass:"_select_tree_div", //展示div 的class
			_treeId:id+"_select_tree",//数id
			_seachInput:id+"_seach_input",//搜索输入框id
			_seachClass:'_seach_input_class',
			_allSelectId:id+"_all_select",
			checkParmIsToLoad:true,//检测参数判定是否需要重新查询 默认true 参数是之前的查询参数和现在查询参数
			onSeachChange:false,//搜索框发送变化时的触发
			isClickToLoad:false,//是否第一次点击才进行加载 树 
			isRepeatClickLoad:false,//是否每次点击都进行重新加载  比 isClickToLoad 优先级高
			hasAllButton:true,//是否有全选按钮 'checkbox' 时 才生效
			checkCallBack:false, //选中回调方法
			beforeQueryParms:false,//查询前对参数的处理
			formatNode:false,//节点的format 处理 单个单个节点处理 若不需要此节点 放回null 需要的话 处理后 返回对应对象
			queryName:'name',//搜索的条件名称 比如 {page：1，pageSize：10，name：'输入的关键字'}
			chStyle:false, //单选还是多选 默认多选   'checkbox' 'radio'
			seach:true,//是否支持搜索
			rp:15,//默认页码 不建议改小  必须保证数据多的时候出现滚动条 以支持滚动加载
			totalPage:1,//总页码
			onePageloadAfter:false,//此方法只会在第一加载成功后调用
			pageLoadAfter:false,//每次请求数据完成后调用 失败成功，error 都会  失败和成功 会返回后台返回的结果 error的情况放回空值
			url:'',//请求url 地址（也是搜索的地址）
			page:1//首次请求的页码
        }, _p);
    	_data._one = null; // 记录是否是第一次 防止修改
    	_data.parParms = null;
    	_data.id = id;
    	_data._this = _this;
    	
    	/**
    	 * 弹出样式默认
    	 */
    	var get_css = function(_this){
    		var inputPosition = _this.offset();
    		var _css = $.extend({
        		'position':'absolute',
                'top': inputPosition.top + _data._this.height() + 5 + "px",
                'left': inputPosition.left + "px",
                'max-height': 300,//最大高度
                'overflow': 'auto',
                'background': '#eee',
                'border': '1px solid #ccc',
                'z-index':999,
                'width':'auto',
                'max-width':300//最大宽度
        	},_c);
    		return _css;
    	};
    	
    	/**
    	 * 弹出样式默认
    	 */
    	var _css = get_css(_data._this);
    	
    	//数结构配置
    	var _treeSetting =  $.extend({
    		'id':'id',//配置id
    		'pid':'pid'//配置pid
    	},_z);
    	
    	//默认展开
    	var open = {'open':true};
    	
    	//树配置
    	var _setting = {
            check: {
                enable: true,
                autoCheckTrigger: true,
                chkStyle : (!_data.chStyle) ? "checkbox" : _data.chStyle
            },
            view: {
                dblClickExpand: true,
                showLine: false,
                selectedMulti: false,
                showIcon:false
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: _treeSetting.id,
                    pIdKey: _treeSetting.pid
                }
            },
            callback: {
            	onClick:function(e,treeId,treeNode){
            		 var treeObj = $.fn.zTree.getZTreeObj(treeId);
            		 treeObj.checkNode(treeNode,!treeNode.checked,false,true);
            	},
                onCheck: function (e, treeId, treeNode) {
                    var treeObj = $.fn.zTree.getZTreeObj(treeId);
                    treeObj.expandNode(treeNode, !treeNode.open, false, true);
                    var mthis = selMap.get(_data.id);
                    if(!mthis){
                    	mthis = new Map();
                    	selMap.set(_data.id,mthis);
                    }
                    var arrName = mthis.get('names');
                    if(!arrName){
                    	arrName = [];
                		mthis.set('names',arrName);
                	}
                    
                    var arr = mthis.get('ids');
                    if(!arr){
                		arr = [];
                		mthis.set('ids',arr);
                	}
                    
                    if(!_data.chStyle || _data.chStyle == 'checkbox'){
                    	if(treeNode.checked){
                        	arr.push(treeNode.id);
                        	arrName.push(treeNode.name);
                        }else{
                        	if(arr && arr.indexOf(treeNode.id) >=0){
                        		arr.splice(arr.indexOf(treeNode.id),1);
                        	}
                        	if(arrName && arrName.indexOf(treeNode.name) >=0){
                        		arrName.splice(arrName.indexOf(treeNode.name),1);
                        	}
                        }
                    }else{
                    	arr = [];
                    	arrName = [];
                    	if(treeNode.checked){
                        	arr.push(treeNode.id);
                        	arrName.push(treeNode.name);
                        }
                    }
                    mthis.set('ids',arr);
                    mthis.set('names',arrName);
                    _data._this.val(arrName.toString());
                    if(_data.hasAllButton && _data.chStyle && _data.chStyle =='checkbox'){
                    	var _thisCheck = $('#'+_data._allSelectId);
                    	if(!treeNode.checked){
                    		_thisCheck.removeClass('check-on');
                    		_thisCheck[0].checked = false;
                    	}
                    	if(arr.length == 0){
                    		_thisCheck.removeClass('check-on');
                    		_thisCheck[0].checked = false;
                    	}else if(arr.length == treeObj.getNodes().length){
                    		_thisCheck.addClass('check-on');
                    		_thisCheck[0].checked = true;
                    	}
                    }
                    $.isFunction(_data.checkCallBack) && _data.checkCallBack(e, treeId, treeNode);
                    
                }
            }
        };
    	var r = {
    			//初始化方法
    			init:function(){
					r.creatDiv();
					r.common();
					if(_data.url && !_data.isClickToLoad){
						r.getData();
					}
					//数隐藏
					$(document).on('click', function (e) {
		                if (($(e.target).closest("#"+_data._treeDiv).length == 0 && $(e.target).closest("#"+id).length == 0)) {
		                    $('#'+_data._treeDiv).hide();
		                }
		            });
					//树显示
					_data._this.off('click').on('click',function(){
						var treeDiv = $('#'+_data._treeDiv);
						//设置样式
						treeDiv.css(get_css(_data._this));
						treeDiv.show();
						if(_data.isRepeatClickLoad){
							r.getData();
						}else if(_data.isClickToLoad && !_data._one){
							r.getData();
						}
					});
				},
    			//树的div  树 
    			creatDiv:function(){
    				//数div
    				var treeDiv = $('#'+_data._treeDiv);
					if(!treeDiv || treeDiv.length<=0){
						treeDiv = $('<div hidden=true class="'+_data._treeDivClass+'" id="'+_data._treeDiv+'"></div>');
						$('body').append(treeDiv);
					}
					//设置样式
					treeDiv.css(_css);
					//滚动自动加载 
					treeDiv.off('scroll').on('scroll',function(){
						var _$this = $(this);
						var sTop = _$this.scrollTop();
						var height = _$this.height();
						var scHeight = _$this[0].scrollHeight;
						if(sTop +height >= scHeight - 10){
							_data.page = _data.page+1;
							if(_data.page > _data.totalPage){
								_data.page = _data.totalPage;
								return;
							}
							r.getData();
						}
					});
					//关键字查询
					if(_data.seach){
						var _seach = $('#'+_data._seachInput);
						if(!_seach || _seach.length<=0){
							_seach = $('<input class="'+_data._seachClass+'" id="'+_data._seachInput+'"></input>');
							_seach.attr('placeholder', Msg.inputTheKey); 
							treeDiv.append(_seach);
						}
						_seach.val('');
						_seach.css({
							'margin':'5px',
							'background':'#FBFBFB'
						});
						//输入变化后自动查询第一页的数据
						_seach.off('change').on('change',function(){
							if(_data.onSeachChange && $.isFunction(_data.onSeachChange)){
								_data.onSeachChange($(this).val());
							}
							r.getData(1);
						});
					}else{
						$('#'+_data._seachInput).remove();
					}
					//树结构
					var tree = $('#'+_data._treeId);
					if(_data.hasAllButton && _data.chStyle && _data.chStyle =='checkbox'){
						var _p = $('#'+_data._allSelectId+"_p");
						if((!_p || _p.length<=0)){
							var _all = $('#'+_data._allSelectId);
							var _span = $('#'+_data._allSelectId+"_span");
							var _label = $('#'+_data._allSelectId+"_label");
							if(!_p || _p.length<=0){
								_p = $("<p id='"+_data._allSelectId+"_p'></p>");
							}
							if(!_label || _label.length<=0){
								_label = $("<label id='"+_data._allSelectId+"_label'></label>");
							}
							if(!_all || _all.length<=0){
								_all = $("<input type='checkbox' class='check-un' id='"+_data._allSelectId+"'></input>");
							}
							if(!_span || _span.length<=0){
								_span = $("<span  id='"+_data._allSelectId+"_span'>"+Msg.allOrNotAll+"</span>");
							}
							_label.append(_all);
							_label.append(_span);
							_p.append(_label);
							if(tree && tree.length>0){
								tree.prepend(_p);
							}else{
								treeDiv.append(_p);
							}
							_span.css({'margin-left':'10px'});
							_label.css({'cursor': 'pointer','margin-left':'5px'});
							$('#'+_data._allSelectId+"_p").off('click').on('click',function(){
								var _thisCheck = $('#'+_data._allSelectId);
								r.toCheckedAll(_thisCheck[0].checked);
							});
						}
					}else{
						 $('#'+_data._allSelectId+"_p").remove();
						 $('#'+_data._allSelectId).remove();
						 $('#'+_data._allSelectId+"_span").remove();
					}
					//树结构
					if(!tree || tree.length<=0){
						tree = $('<ul class="ztree" id="'+_data._treeId+'"></ul>')
						treeDiv.append(tree);
						tree.css({
							'margin-left':-20
						});
					}
    			},
    			//控制页面滚动时 隐藏树的div
    			common:function(){
    				var scrollTop = -1;
    				$('.body').mousemove(function () {
				        if (!$('#'+_data._treeDiv).get(0) || $('#'+_data._treeDiv).is(':hidden')) {
				            scrollTop = $(this).scrollTop();
				        }
				    });
    				$('.body').scroll(function () {
				        if (scrollTop != -1 && $('#'+_data._treeDiv).is(':visible')) {
				            $(this).scrollTop(scrollTop);
				        }
				    });
				    $(window).resize(function () {
				    	$('#'+_data._treeDiv).hide();
				    });
    			},
    			//全选或者全部选方法
    			toCheckedAll:function(isChecked){
    				var _thisCheck = $('#'+_data._allSelectId);
    				if(isChecked){
    					_thisCheck.addClass('check-on');
    				}else{
    					_thisCheck.removeClass('check-on');
    				}
					var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
					if(treeObj){
						var nodes = treeObj.getNodes();
						$.each(nodes,function(t,e){
							treeObj.checkNode(e,isChecked,false,true);
						});
					}
    			},
    			//查询数据
				getData:function(page){
					var _param = {page:page ? page : _data.page,pageSize:_data.rp};
					//关键字查询
					var qName =  $('#'+_data._seachInput).val();
					
					if(_data.beforeQueryParms && $.isFunction(_data.beforeQueryParms)){
						var _Parms = _data.beforeQueryParms(_param,qName);
						_param =  _Parms || _param;
					}else{
						if(qName){
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
					if($.isFunction(_data.checkParmIsToLoad)){
						var toQuery = _data.checkParmIsToLoad(_data.parParms,_param);
						if(!toQuery){
							return;
						}
					}
					_data.parParms = Object.extend(_data.parParms,_param,true);
					
					$.http.post(_data.url, _param, function (data) {
		                 var ro = data;
		                 if (ro.success) {
		                	 if(!ro.data){
		                		 if(!_data._one){
		                			 $.isFunction(_data.onePageloadAfter) && _data.onePageloadAfter();
		                			 _data._one = true;
		                		 } 
		                		 return;
		                	 }
		                	 _data.page = ro.data.pageNo;
		                	 _data.totalPage = parseFloat((ro.data.total+ro.data.pageSize-1)/ro.data.pageSize).fixed(0);
		                	 var nodes = ro.data.list;
		                	 var data = [];
		                	 if(!nodes){
		                		 nodes = [];
		                	 }
		                	 $.each(nodes,function(t,e){
		                		 e = $.extend(e,open);
		                		 if($.isFunction(_data.formatNode)){
		                			 e = _data.formatNode(e);
		                		 }
		                		 e && data.push(e);
		                	 });
		                	 var treeObj = $.fn.zTree.getZTreeObj(_data._treeId);
		                	 if(_data.page > 1 && treeObj){
		                		 treeObj.addNodes(null,data);
		                	 }else{
		                		 var _thisCheck = $('#'+_data._allSelectId);
			                     if(_thisCheck){
			                    	_thisCheck.removeClass('check-on');
			                    	_thisCheck.checked = false;
			                     }
		                		 selMap.delete(_data.id);
		                		 _data._this.val('');
		                		 var mthis = selMap.get(_data.id);
		                         if(!mthis){
		                         	mthis = new Map();
		                         	selMap.set(_data.id,mthis);
		                         }
		                		 if(data.length>0){
		                			 mthis.set('first',data[0].id);
		                		 }else{
		                			 mthis.set('first',"");
		                		 }
		                		 $.fn.zTree.init($('#'+_data._treeId), _setting, data);
		                		 if(!_data._one){
		                			 $.isFunction(_data.onePageloadAfter) && _data.onePageloadAfter();
		                			 _data._one = true;
		                		 } 
		                	 }
		                	 $.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter(data);
		                 }else{
		                	 $.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter(data);
		                 } 
		            },function(){
		            	$.isFunction(_data.pageLoadAfter) && _data.pageLoadAfter();
		            });
				}
    	};
    	r.init();
        return r;
    };
    return $;
});
