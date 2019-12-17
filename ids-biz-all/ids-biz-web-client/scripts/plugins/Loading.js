/**
 * 使用方式实例：
 * // 生成并打开过渡遮罩效果层
 *
 *
 */
define(['jquery'], function ($) {
    $.fn.extend({
        /**
         * 生成并打开过渡遮罩效果层
         * @param options
         * @returns {*}
         */
        toggleLoading: function (options) {
            // 扩展参数
            var op = $.extend({
                z: 9999,                                    //遮罩层层高
                msg: 'loading...',                          //提示信息
                iconUrl: '/images/loading.gif',             //图片
                width: 18,                                  //图标宽度
                height: 18,                                 //图标高度
                msgColor: '#fff',                           //消息文字颜色
                borderColor: '#ffffff',                     //消息边框颜色
                foregroundColor: "#429aff",                 //消息背景颜色
                backgroundColor: "#011428",                 //背景颜色
                opacity: 0.78,                              //透明度
                speed: 500
            }, options);

            // 当前操作的元素
            var $this = $(this);
            // 找到遮罩层
            var crust = $(".x-loading", $this);
            // 实现toogle(切换遮罩层出现与消失)效果的判断方法
            if (crust.length > 0) {
                if (crust.is(":visible")) {
                    crust.fadeOut(op.speed);
                } else {
                    crust.fadeIn(op.speed);
                }
                return this;
            }

            var w = document.documentElement.clientWidth,
                h = document.documentElement.clientHeight;

            // 外壳
            crust = $("<div/>").css({
                'position': 'fixed',
                'top': 0,
                'left': 0,
                'width': '100%',
                'height': '100%',
                'background-color': op.backgroundColor,
                'opacity': op.opacity,
                'filter': 'alpha(opacity=100)',
                'z-index': op.z
            }).attr("class", "x-loading");
            // 消息外壳
            var msgCrust = $("<div/>").css({
                'position': 'relative',
                'top': (h > 90 ? (h - 90) / 2 : 0) + 'px',
                'z-index': op.z + 1,
                'display': 'block',
                'text-align': 'center',
                'opacity': 0.9
            });
            // 消息主体
            var msg = $("<span>" + op.msg + "</span>").css({
                'position': 'relative',
                'margin': '0px',
                'z-index': op.z + 2,
                'color': op.msgColor,
                'font-size': '18px',
                'display': 'inline-block',
                'background-color': op.foregroundColor,
                'padding': '5px 5px 5px 5px',
                'border': '1px solid ' + op.borderColor,
                'border-radius': '5px',
                'vertical-align': 'middle',
                'text-align': 'left',
                'text-indent': '0'
            });
            // 图标
            var msgIcon = $("<img src=" + op.iconUrl + " />").css({
                'vertical-align': 'middle',
                'border': 'none',
                'padding': '5px'
            });
            // 拼装进度蒙层
            msg.prepend(msgIcon);
            msgCrust.prepend(msg);
            crust.prepend(msgCrust);
            crust.fadeIn(op.speed); //模态设置
            $this.prepend(crust);
            return $this;
        },

        /**
         * 关闭过渡遮罩效果层
         */
        cancelLoading: function () {
            $(".x-loading", this).remove();
            return this;
        }
    });
});