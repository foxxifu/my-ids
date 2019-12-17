/**
 * 数字滚筒插件
 * 调用方式 $(selector).NumberRoll({<选项，键值对>})
 */
;(function (factory) {
    "use strict";
    if (typeof define === 'function' && define.amd) {
        define(['jquery', 'css!plugins/NumberRoll/NumberRoll.css'], factory);
    } else {
        factory(jQuery);
    }
}(function ($) {

    var NumberRoll = function (container, options) {
        var p = $.extend({
            length: 0,
            value: 0,
            dts: 0,
            maxValue: 1e+15,
            speed: 2000,
            lastValue: 0
        }, options);

        var g = {

            /**
             * 获取组件根路径
             * @private
             */
            _path: function () {
                var path, idx, sList = document.getElementsByTagName("script");
                for (var i = 0; i < sList.length; i++) {
                    path = sList[i].getAttribute("src");
                    if (path) {
                        path = path.substr(0, path.toLowerCase().indexOf("numberroll.js"));
                        idx = path.lastIndexOf("/");
                        if (idx > 0)
                            path = path.substring(0, idx + 1);
                        if (path)
                            break;
                    }
                }
                p.path = path;
            },

            /**
             * 创建DOM结构
             * @param context
             * @param p
             */
            _create: function (context, p) {
                this._path();

                var num = this._number(p);
                var len = Math.max(num.length, p.length);
                var ul = $('<ul></ul>').css({margin: '-1px 0', padding: 0});

                for (var i = 0; i < len; i++) {
                    ul.prepend(this._createItem(p.path, i === len - 1, i === 0));
                }

                $(context).html(ul);
            },

            /**
             * 创建单个数字筒
             * @param path
             * @param isFirst 是否第一个
             * @param isLast 是否最后一个
             * @returns {HTMLLIElement}
             * @private
             */
            _createItem: function (path, isFirst, isLast) {
                var li = $('<li></li>').css({
                    'float': 'left', position: 'relative', width: 30, height: 35, 'overflow': 'hidden',
                    'border-top': '0px solid #323232'
                });
                var div = $("<div/>").css({
                    'position': 'absolute',
                    'width': '30px',
                    'height': '560px',
                    'margin': '0 2px'
                });
                var img_a = $('<img src="' + path + 'images/' + (isLast ? '1-2' : '1-1') + '.png" border="0" />')
                    .attr({'alt': '0'})
                    .css({'position': 'relative', 'height': 35 * 10, display: 'block'});

                var img_b = $('<img src="' + path + 'images/' + '1-2' + '.png" border="0" />')
                    .attr({'alt': '0'})
                    .css({'position': 'relative', 'height': 35 * 10, display: 'block'});

                div.append(img_a, img_b);
                li.append(div);

                return li;
            },

            /**
             * 赋值
             * @param context
             * @param p
             */
            _set: function (context, p) {
                var num = this._number(p, 'value');
                var preNum = this._number(p, 'lastValue');

                var nlen = num.length;
                var preNumLen = preNum.length;
                var len = Math.max(nlen, p.length);
                for (var j = 0; j < len - nlen; j++) {
                    num = "0" + num;
                }
                for (var k = 0; k < len - preNumLen; k++) {
                    preNum = "0" + preNum;
                }

                var ul = $('ul', $(context));
                var li = $('li', $(context));
                if (len > li.length) {
                    li.eq(0).find('img').attr('src', p.path + 'images/1-1.png').end();
                    for (var k = 0; k < len - li.length; k++) {
                        ul.prepend(this._createItem(p.path, k == len - li.length - 1, false));
                    }
                }
                else if (len < li.length) {
                    var diff = li.length - len;
                    for (var t = 0; t < diff; t++) {
                        li.eq(t).remove();
                    }
                    $('li', $(context)).eq(0).find('img').attr('src', p.path + 'images/1-2.png').end();
                }

                for (var i = len - 1; i >= 0; i--) {
                    if (num[i] === '.') {
                        $('li', $(context)).eq(i).css({width: 12, border: 'none', 'font-size': '20pt'}).html('.');
                    } else {
                        var numObj = g._compareNumber(preNum[i], num[i]);
                        var n = numObj.crossNum;
                        var img = $('li', $(context)).eq(i).find('img');

                        var imgDiv = img.parent('div');
                        if (i > len - nlen - 1) {
                            $(img[0]).attr("src", p.path + 'images/1-2.png');
                            $(img[1]).attr("src", p.path + 'images/1-2.png');
                        }
                        else {
                            $(img[0]).attr("src", p.path + 'images/1-1.png');
                            $(img[1]).attr("src", p.path + 'images/1-1.png');
                        }
                        var divTop = parseInt(imgDiv.css('top')) ? parseInt(imgDiv.css('top')) : 0;
                        if (divTop <= -35 * 10) {
                            imgDiv.css('top', divTop + 35 * 10 + "px");
                        }
                        imgDiv.stop(true, true).animate({'top': n ? -35 * n : 0}, p.speed);
                    }
                }

                p.lastValue = p.value;
            },

            /**
             * 传递数值域处理
             * @param p
             * @param property
             * @returns {string}
             * @private
             */
            _number: function (p, property) {
                var num = '0';
                if (Number(p[property])) {
                    if (Number(p[property]) < 0) {
                        num = '0';
                    }
                    else if (Number(p[property]) < Number(p.maxValue)) {
                        num = this._fixed(Number(p[property]), p.dts);
                    }
                    else {
                        num = this._fixed(Number(p.maxValue) - Number("1e-" + p.dts), p.dts);
                    }
                }
                return String(num);
            },

            /**
             * 对数字格式进行四舍五入
             * @param num {Number} 数字
             * @param length {int} 小数截断位数，默认为0
             */
            _fixed: function (num, length) {
                if (isNaN(num))
                    return 0;
                var s = Math.pow(10, Math.abs(parseInt(length || 0)));
                var s_x = parseFloat(Math.round(num * s) / s).toString();
                var pos_decimal = s_x.indexOf('.');
                if (pos_decimal < 0) {
                    pos_decimal = s_x.length;
                    if (length > 0) {
                        s_x += '.';
                    }
                }
                while (length > 0 && s_x.length <= pos_decimal + length) {
                    s_x += '0';
                }
                return s_x;
            },

            /**
             * 将前后两个值进行比较
             * @param  {String} preValue 前一个数字
             * @param  {String} value 当前数字
             * @return {Object}
             */
            _compareNumber: function (preValue, value) {
                var pre = parseInt(preValue) ? parseInt(preValue) : 0;
                var cur = parseInt(value) ? parseInt(value) : 0;

                if (pre < cur) {
                    return {
                        isCross: false, //是否需要转到第二张图片
                        crossNum: cur //相差的数量
                    }
                } else if (pre == cur) {
                    return {
                        isCross: false, //是否需要转到第二张图片
                        crossNum: cur //相差的数量
                    }
                } else {
                    return {
                        isCross: true,
                        crossNum: 10 + cur
                    }
                }
            }
        };

        this.element = container;
        this.p = p;
        this.g = g;
    };

    NumberRoll.prototype.create = function () {
        this.g._create($(this.element), this.p);
        this.g._set($(this.element), this.p);
    };
    NumberRoll.prototype.refresh = function (option) {
        var np = $.extend(this.p, option);
        this.g._set($(this.element), np);
    };

    $.fn.extend({
        /**
         * 创建数字滚筒
         * @param option
         */
        NumberRoll: function (option) {
            this.each(function () {
                this.numberRoll = new NumberRoll($(this), option);
                this.numberRoll.create();
            });
            return this;
        },

        /**
         * 数字滚筒刷新
         * @param option
         */
        NumberRollRefresh: function (option) {
            this.each(function () {
                this.numberRoll && this.numberRoll.refresh(option);
            });
            return this;
        }
    });

    return $;
}));