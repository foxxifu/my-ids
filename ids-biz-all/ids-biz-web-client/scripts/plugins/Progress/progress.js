/**
 * 进度条的样式扩展  调用 $(selector).myProgress({...})  设置进度$(selector).setProgressValue(0-100之间的值)
 * 销毁对象$(selector).progressDestory() 或者$(selector).closeProgressDialog()
 */
;(function($, window, document, undefined) {
	var indexNum = 0;
	function MyProgress(ele,opts){
		this.content = ele[0];//dom元素,容纳进度条的容器
		this.$element = $('<div/>');
		var defaults = {
			width:200,//默认进度条的整体的宽,可以支持百分比的字符串如'100%'，数字(如200)就是px像素值，后面就不能使用px了
			height:30,//默认进度条的整体的高,可以支持百分比的字符串如'100%'，数字(如30)就是px像素值，后面就不能使用px了
			r:15,//指定进度条的border-radius的宽度是10
			
			pw:100,//内部进度条占总体的宽度,目前就只能使用百分比,默认是占满宽度100%  注意:只能使用百分比
			ph:100,//内部进度条占总体的宽度,目前就只能使用百分比,默认占60%的高度   注意:只能使用百分比
			
			//其他的默认设置
			pType:'s',//展示的样式 s：长方形  c:圆形,b:小球 （目前主要使用a 、c两种，b暂时不要使用) 默认长方形

			boxClass:'progress-box',//皮肤的样式默认的class,如果想要修改某些皮肤的样式，将progresss.css文件中的所有class为progress-box修改为自己想要的样式的
			hasProgress:true,//是否有进度 true：有  false：无 默认有
			hasText:true,//是否有文字显示 true:显示文字 false：不显示文字  默认true显示文字
			showText:'',//显示文字
			show:'h',//展示方式 是垂直还是水平 h：水平 v：垂直 默认水平
			isDialog:false,//是否是弹出框的形式
			//proColor:'#33ccb8',//背景颜色 默认的背景颜色
			'text-align':'center',//文字显示的水平位置,left:靠左 center：居中 right：靠右,默认是居中
			'text-indent':0,//文字首行缩进的距离，取值与文本文字的text-indent相同
			'text-v':'middle',//文字垂直位置 top:居上 middle:居中 bottom:居下 ，默认居中显示,
			textFontSize:12,//文字大小,默认12 像素
			value:0,//默认的进度是0,
			/**
			 * textFnInit 文字的样式处理 定义方式如下
			 * @param {Object} value 进度的值
			 * @param {Object} pType 图形类型  s:长方形  c:圆形 b:球形//value是传递过来的百分数，// 提供给外界的设置文字的方法的回调函数，这里可以给用户设置文字 textFnInt:function(value){return value};
			 * @return {}返回值显示文字的css属性的key和value值{key:value}
			 * textFnInit:function(value,pType){
			 *	//可以根据进度确认进度条的颜色
			 *	var params = {};
			 *	var styles = {};
			 *	params.text= params.title = value +"%";//显示文字
			 *	params.styles = styles;//样式
			 *	styles['text-overflow'] = 'ellipsis';//文字超出隐藏
			 *	if(!pType || pType=='s'){// 如果是正方形
			 *		styles.color = 'red';
			 *		styles['font-weight'] = 900;
			 *	}else{//球形或者圆形都是这个样
			 *		styles.color = 'red';
			 *		styles['font-weight'] = 900;
			 *		styles.width = styles.height = '50%';//宽高
			 *		styles['border-radius']='50%';
			 *		if(pType=='c'){//如果是圆就设置背景设为白色
			 *			styles.background = '#fff';
			 *		}
			 *	}
			 *	return params;
			 *},
			 */
			textFnInit:undefined,//text的回调函数
			/**
			 * 进度条的样式
			 * @param {Object} value进度值
			 * @param {Object} pType 样式
			 *	function fnBgInit(value,pType){// 设置进度条的样式，这里主要是配置进度条进度的颜色,只能返回颜色的样式 如#333 或者red 或者 rgb 或者rgba等格式的数据,默认的处理，可以自己定义
			 *		var styles = {};
			 *		if(pType && pType=="c"){
			 *			styles.all={//所有的样式
			 *				background:'#33ccb8'
			 *			}
			 *			styles.lastBox = {//最后一个遮罩层的样式
			 *				'background':'rgba(22,82,118,1)'
			 *			}
			 *		}else{
			 *			styles['background']='#33ccb8';
			 *		}
			 *		return styles;
			 *	},
			 */
			fnBgInit:undefined,//进度的处理样式
		}
		this.options = $.extend({}, defaults, opts);
		this.index = indexNum;//第多少个进度条
		if(this.options.isDialog){//如果是弹出框才添加，其他的这个index都是一样的
			indexNum++;
		}
		ele.append(this.$element);
	}
	MyProgress.prototype = {
		init:function(){//初始化
			//1.验证是否可以创建
			if(!this.validate()){
				return;
			}
			//2.添加元素
			this.create();
			//3.配置内容
			//4.给当前元素的dom元素添加一个进度条的信息
            this.content.progress = this;
			this.$element.parent().resize(function(){//监听自适应方法
                this.progress && this.progress.setTextAlign();
			});
			return this;
		},
		validate:function(){//验证
			if(this.content.progress){
                this.clear();//如果存在就销毁了，避免垃圾数据
            }
			this.$element.hasClass(this.options.boxClass) || this.$element.addClass(this.options.boxClass);//如果没有添加class就添加这个class	信息
			this.$element.hasClass('progress-dialog-parent') && this.$element.removeClass('progress-dialog-parent');//如果是弹出框需要先去掉这个样式
			if(this.options.pType=="s"){//如果是长方形   半径
				this.boxR=this.options.r
			}else{
				this.options.r = '50%';
				if(this.options.show=="h"){//从左到右
					this.boxR = '50% 0 0 50%';
				}else{//从下到上
					this.boxR = '50% 50% 0 0';
				}
			}
            //判断是否是弹出框
            if(this.options.isDialog){//如果是弹出框，创建一个遮罩层
                this.$element.addClass('progress-dialog-parent');
                //创建遮罩层
                var $overlay = $('<div/>').addClass('progress-dialog-overlay').attr('id','progress_ovlery_'+this.index);
                this.$element.after($overlay);
            }
			this.$element.css({
				width:this.options.width,
				height:this.options.height
			});
			//判断是否是圆形，如果是圆形的就处理圆形的宽和高设置为相同的 ,如果是球形
			if(this.options.pType=="c" || this.options.pType=="b"){//如果是球和圆
				var width = this.$element.width();
				var height = this.$element.height();
				if(width>height){
					this.options.ph = 100;
					this.options.pw = height/(width*1.0)*100;
				}else{
					this.options.pw = 100;
					this.options.ph = width/(height*1.0)*100;
				}
			}
			return true;
		},
		create:function(){//创建元素
			
			if(this.options.pType=="c"){//如果是圆形
				//圆形需要修改他的宽高相同
				var $box1 = $('<span/>').addClass('progress-cirle-box box1');//实际进度的内容
				var $box2 = $('<span/>').addClass('progress-cirle-box box2');//实际进度的内容
				var $box3 = $('<span/>').addClass('progress-cirle-box box3 boxMaxZIndex');//实际进度的内容
				var $pb = $('<p/>').addClass('progress-border').append($box1).append($box2).append($box3).
					css({'border-radius':this.options.r,width:(this.options.pw+"%"),height:this.options.ph+"%",top:((100-this.options.ph)/2)+'%',left:((100-this.options.pw)/2)+'%'});//进度条的默认背景框
				var $dText = $('<div/>').addClass('progress-text')
				.css({'font-size':this.options.textFontSize,'text-indent':this.options['text-indent']});
//				var tmV = this.options['text-v'];//判断文字的垂直距离
//				var lineHeight = tmV=='top'?this.options.textFontSize:(tmV=='middle'?this.options.height:(this.options.height*2-this.options.textFontSize));
//				$dText.css('line-height',lineHeight+"px");
				this.$element.css({width:this.options.width,height:this.options.height}).append($pb).append($dText);
				this.options.hasText || $dText.addClass('progerss-text-close');
			}else{//其他的如球形  和长方形
				//如果是球形就需要修改他的宽高相同
				var $ps = $('<span/>').addClass('progress-content');//实际进度的内容
				var $pb = $('<p/>').addClass('progress-border').append($ps).
					css({'border-radius':this.options.r,width:(this.options.pw+"%"),height:this.options.ph+"%",top:((100-this.options.ph)/2)+'%',left:((100-this.options.pw)/2)+'%'});//进度条的默认背景框
				var $dText = $('<div/>').addClass('progress-text')
				.css({'font-size':this.options.textFontSize,'text-indent':this.options['text-indent']});
//				var tmV = this.options['text-v'];//判断文字的垂直距离
//				var lineHeight = tmV=='top'?this.options.textFontSize:(tmV=='middle'?this.options.height:(this.options.height*2-this.options.textFontSize));
//				$dText.css('line-height',lineHeight+"px");
				this.$element.css({width:this.options.width,height:this.options.height}).append($pb).append($dText);
				this.options.hasText || $dText.addClass('progerss-text-close');
			}

			this.setValue(this.options.value);
			return this;
		},
		//提供给外界的设置文本的方法
		setText:function(){
			if(!this.options.hasText){//不需要显示文字的就不用设置对于的信息
				return;
			}
			//进度的值,默认的构造方法
			/**
			 * 处理进度条的样式
			 * @param {Object} value
			 * @param {Object} pType
			 */
			function textFnInit(value,pType){//value是传递过来的百分数，// 提供给外界的设置文字的方法的回调函数，这里可以给用户设置文字 textFnInt:function(value){return value};
				//可以根据进度确认进度条的颜色
				var params = {};
				var styles = {};
				params.text= params.title = value +"%";//显示文字
				params.styles = styles;//样式
				styles['text-overflow'] = 'ellipsis';//文字超出隐藏
				if(!pType || pType=='s'){// 如果是正方形
					//console.log('设置条形进度条的样式');
//					styles.color = 'red';
//					styles['font-weight'] = 900;
				}else{//球形或者圆形都是这个样式
//					styles.color = 'red';
//					styles['font-weight'] = 900;
					//styles.width = styles.height = '50%';//宽高
					//styles['border-radius']='50%';
				}
				//return params;
			};
			var params = undefined;
			try{
				params = this.options.textFnInit && this.options.textFnInit(this.options.value,this.options.pType) || textFnInit(this.options.value,this.options.pType);
			}catch(e){
				console.log('获取文字时候出现了异常',e);
				params = undefined;
			}
			var $label = $('<label/>');
			if(this.options.pType=='c'){
				$label.addClass('psrogress-text-label-cirle');
				var tmpR = Math.min(this.$element.width(),this.$element.height());//直径，也就是显示文字的行高
				$label.css({top:(100-this.options.ph/2)/2+'%',left:(100-this.options.pw/2)+'%',width:this.options.pw/2+'%',height:this.options.ph/2+'%','border-radius':'50%','line-height':tmpR/2+'px','text-align':'center'});
			}else{
				$label.addClass('psrogress-text-label-squre')
			}
			
			this.$element.find('div.progress-text').empty().append($label);
			if(params){
				if(params.title){
					$label.attr('title',params.title);
				}
				if(params.text){
					$label.text(params.text);
				}
				var styles = params.styles;
				if(styles){//设置样式 label
					for(var key in params.styles){
						$label.css(key,styles[key]);
					}
				}
			}else{
                if(this.options.hasProgress){
                    $label.text(this.options.showText+this.options.value+"%");
                }else{
                    $label.text(this.options.showText);
                }
			}
			this.setTextAlign();//设置文字的水平和垂直方向的位置
		},
		setTextAlign:function(){//设置文字位置(水平和垂直方向都有 )
			var $label = this.$element.find('div.progress-text label');
			//设置label的位置垂直方向的位置 start
			var vp = this.options['text-v'] || 'middle';
			vp = vp +'';//强制转换为字符串
			if(vp=='top'){
				$label.css({top:0});
			}else if(vp=='bottom'){
				$label.css({top:$label.parent().height()-$label.height()});
			}else{
				if(/^\d+(.\d{1,5})?(px|%)$/.test(vp)){//设置的事px像素的或者百分比的
					$label.css({top:vp});
				}else if(/^\d+(.\d{1,5})?$/.test(vp)){//只设置了数字
					$label.css({top:vp-0});
				}else {//其他的都设置垂直居中
					$label.css({top:($label.parent().height()-$label.height())/2});
				}
			}
			//设置label的位置垂直方向的位置 end
			// 水平方向的label文字显示位置 start
			var vh = this.options['text-align'] || 'center';
			vh = vh+'';//转换为字符串
			if(vh=='left'){//居左
				$label.css({left:0});
			}else if(vh=='right'){//居右
				$label.css({left:$label.parent().width()-$label.width()});
			}else{
				if(/^\d+(.\d{1,5})?(px|%)$/.test(vh)){//设置的事px像素的或者百分比的
					$label.css({left:vh});
				}else if(/^\d+(.\d{1,5})?$/.test(vh)){//只设置了数字
					$label.css({left:vh-0});
				}else {//其他的都设置水平居中
					$label.css({left:($label.parent().width()-$label.width())/2});
				}
			}
			// 水平方向的label文字显示位置 end
		},
		setProGbColor:function(){//设置图像的背景
			/**
			 * 进度条的样式
			 * @param {Object} value
			 * @param {Object} pType
			 */
			function fnBgInit(value,pType){// 设置进度条的样式，这里主要是配置进度条进度的颜色,只能返回颜色的样式 如#333 或者red 或者 rgb 或者rgba等格式的数据,默认的处理，可以自己定义
				var styles = {};
				if(pType && pType=="c"){
					styles.all={//所有的样式
						background:'#33ccb8'
					}
					styles.lastBox = {//最后一个遮罩层的样式
						'background':'rgba(22,82,118,1)'
					}
				}else{
					styles['background']='#33ccb8';
				}
				//return styles;
			};
			try{
				var params = this.options.fnBgInit && this.options.fnBgInit(this.options.vlaue,this.options.pType) || fnBgInit(this.options.vlaue,this.options.pType);
				if(params){
					var spans = this.$element.find('.progress-border span');
					if(this.options.pType && this.options.pType=="c"){
						var all = params.all;
						if(all){
							for(var key in all){
								spans.css(key,all[key]);
							}
						}
						var lastBox = params.lastBox;
						if(lastBox){
							for(var key in lastBox){
								spans.eq(2).css(key,lastBox[key]);
							}
						}
					}else{
						for(key in params){
							spans.css(key,params[key]);
						}
					}
				}
			}catch(e){
				console.log('设置背景色出现异常',e)
			}
		},
		setValue:function(value){//设置进度 这里需要的事0-100之间的数值
			var $ps = this.$element.find('p.progress-border span');//查找进度的信息的显示颜色的对象 如果类型是c则能够获取3个，否则只能获取一个
			if(this.options.hasProgress){
				if(!value || value<0 || isNaN(Number(value))){//严重value的值
					value = 0;
				}else if(value>100){
					value = 100;
				}
				this.options.value = value;
				if(this.options.pType && this.options.pType=='c'){//设置圆形 因为圆形是3块来做的遮罩
					//获取遮罩的元素
					var $box1 = $ps.eq(0);//第一个有进度颜色的进度信息
					var $box3 = $ps.eq(2);//最后一个颜色是
					//判断value的值
					var deg = (value/100.0)*360;//获取当前value值对应的度数
					if(value<=50){
						$box3.hasClass('boxMaxZIndex') || $box3.addClass('boxMaxZIndex');//这个时候需要在最外层
						$box3.css('transform','rotate(0deg)');
						$box1.css('transform','rotate('+deg+'deg)');
					}else{//>50的就是box3在转动 
						$box3.hasClass('boxMaxZIndex') && $box3.removeClass('boxMaxZIndex');//不能够在最外层了，应该在中间层了
						$box1.css('transform','rotate(180deg)');
						$box3.css('transform','rotate('+(deg-180)+'deg)');
					}
				}else{
					if(this.options.show=="v"){//垂直方向
						$ps.css({'border-radius':this.boxR,width:'100%',height:this.options.value+'%'});
						if(this.options.pType && this.options.pType=='b'){//球形,在垂直方向时候就旋转180°
							$ps.parent().css('transform','rotate(180deg)');
						}
					}else{//水平方向
						$ps.css({'border-radius':this.boxR,width:this.options.value+'%',height:'100%'})
					}
				}
				this.setProGbColor();//设置进度的颜色
			}else{
				if(this.options.pType && this.options.pType=='c'){//园的无进度的进度框
					if(this.timer){
						clearInterval(this.timer);
					}
					if($ps.length>0){
						var $box1 = $ps.eq(0);
						var $box3 = $ps.eq(2);
						var num = 0;
						this.timer = setInterval(function(){
							if(num>100){
								num=0;
							}
							var deg = (num/100.0)*360;//获取当前value值对应的度数
							if(num<=50){
								$box3.hasClass('boxMaxZIndex') || $box3.addClass('boxMaxZIndex');//这个时候需要在最外层
								$box3.css('transform','rotate(0deg)');
								$box1.css('transform','rotate('+deg+'deg)');
							}else{//>50的就是box3在转动 
								$box3.hasClass('boxMaxZIndex') && $box3.removeClass('boxMaxZIndex');//不能够在最外层了，应该在中间层了
								$box1.css('transform','rotate(180deg)');
								$box3.css('transform','rotate('+(deg-180)+'deg)');
							}
							num ++;
						},50);
					}
				}else if(this.options.pType && this.options.pType=='b'){
					$ps.removeClass('progresss-no-progress').hasClass('progress-no-progress-cirle') || $ps.addClass('progress-no-progress-cirle');//添加无进度的进度条样式	
				}else{
					$ps.removeClass('progress-no-progress-cirle').hasClass('progresss-no-progress') || $ps.addClass('progresss-no-progress');//添加无进度的进度条样式	
				}
				this.$element.find('div.progress-text').text(this.options.showText);
			}
            this.setText();
			return this;
		},
		clear:function(){
            var _this = (this.$element.parent()[0]).progress;
            _this && _this.timer && clearInterval(_this.timer);//清除进度
            this.$element.parent()[0].progress = undefined;
		},
		destroy:function(){//移除组件,后面组件需要销毁的都最好调用这个方法
			this.clear();
            this.$element.hasClass(this.options.boxClass)  && this.$element.removeClass(this.options.boxClass);
            this.$element.empty();
			if(this.options.isDialog){//如果是弹出框
				var $overlay = $('#progress_ovlery_'+this.index);
				$overlay.length>0 && $overlay.remove();//移除创建的对象
				$overlay.hasClass('progress-dialog-overlay') && $overlay.removeClass('progress-dialog-overlay');
				this.$element.hasClass('progress-dialog-parent') && this.$element.removeClass('progress-dialog-parent');
			}
            this.$element.remove();
            return this;
		},
		/**
		 * 重新加载，对于分辨率改变了的就需要重新加载数据,相当于resize的方式
		 */
		reload:function(){
			this.destroy();//先销毁
			this.init();//在重新加载
			return this;
		}
	}
	//扩展进度条的插件
	$.fn.myProgress = function(opts){
		return this.each(function(){
			new MyProgress($(this),opts).init();
		});
	}
	//对做了进度条插件的设置进度值
	/**
	 * 
	 * @param {Object} value 进度的值 目前只支持0 -100之间的数
	 */
	$.fn.setProgressValue = function(value){
		return this.each(function(){
			this.progress && this.progress.setValue(value);//判断这个dom元素时刻扩展了
		});
	}
    /**
     * 设置文字的位置, 扩展有的元素在设置好之后的位置信息没有渲染完成，设置位置的位置不正确，就需要调用这个方法
     */
    $.fn.setProgressTextAlign = function(){
        return this.each(function(){
            this.progress && this.progress.setTextAlign();//设置文字的位置
        });
    }
	//主要作用是关闭进度条和销毁进度条对象，关闭弹出框的对象,以及消灭进度条的信息
	$.fn.closeProgressDialog = $.fn.progressDesroy = function(){
		return this.each(function(){
			this.progress && this.progress.destroy();//判断这个dom元素时刻扩展了
		});
	}
})(jQuery, window, document);
