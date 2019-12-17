(function ($) {
    /**
     * 创建和管理前端定时器
     *
     * 提供了三个调用方法：
     *     1. everyTimer(时间间隔, [定时器名称], 定时回调方法, [次数限制], [是否等待定时回调方法执行完成])
     *     2. oneTimer(时间间隔, [定时器名称], 定时回调方法)
     *     3. stopTimer([定时器名称])
     **/
    'use strict';
    $.fn.extend({
        /**
         * 定时循环执行
         * $(selector).everyTimer(时间间隔, [定时器名称], 定时回调方法, [次数限制], [是否等待定时回调方法执行完成])
         *
         * @param interval <String> 时间间隔：如 '1s'，单位可定制
         * @param label <String> 定时器名称：可选
         * @param fn <Function> 回调方法
         *       参数：counter 当前执行次数
         * @param times <Number> 次数限制：可选，默认 0 （不限次数，即无限循环）
         * @param belay <Boolean> 是否等待定时回调方法执行完成：可选，默认 false（不等待定时回调方法执行完成即可开始下次计时）
         */
        everyTimer: function (interval, label, fn, times, belay) {
            return this.each(function () {
                $.timer.add(this, interval, label, fn, times, belay);
            });
        },
        /**
         * 定时执行一次
         * $(selector).oneTimer(时间间隔, [定时器名称], 定时回调方法)
         *
         * @param interval <String> 时间间隔：如 '1s'，单位可定制
         * @param label <String> 定时器名称：可选
         * @param fn <Function> 回调方法
         */
        oneTimer: function (interval, label, fn) {
            return this.each(function () {
                $.timer.add(this, interval, label, fn, 1);
            });
        },
        /**
         * 停止定时器
         * $(selector).stopTimer([定时器名称])
         *
         * @param label <String> 定时器名称：可选，默认停止该元素上所有的定时任务
         */
        stopTimer: function (label) {
            return this.each(function () {
                $.timer.remove(this, label);
            });
        }
    });

    $.extend({
        timer: {
            global: [],
            guid: 1,
            dataKey: "iems.timer",
            regex: /^([0-9]+(?:\.[0-9]*)?)\s*(.*)?$/,
            powers: {  // 定义时间单位映射
                'ms': 1,
                's': 1000,
                'min': 60000,
                'h': 3600000
            },
            timeParse: function (value) {
                if (value == undefined || value == null)
                    return null;
                var result = this.regex.exec($.trim(value.toString()));
                if (result[2]) {
                    var num = parseFloat(result[1]);
                    var mult = this.powers[result[2]] || 1;
                    return num * mult;
                } else {
                    return value;
                }
            },
            add: function (element, interval, label, fn, times, belay) {
                var counter = 0;

                if ($.isFunction(label)) {
                    if (!times)
                        times = fn;
                    fn = label;
                    label = interval;
                }

                interval = $.timer.timeParse(interval);

                if (typeof interval != 'number' || isNaN(interval) || interval <= 0)
                    return;

                if (times && times.constructor != Number) {
                    belay = !!times;
                    times = 0;
                }

                times = times || 0;
                belay = belay || false;

                var timers = $.data(element, this.dataKey) || $.data(element, this.dataKey, {});

                if (!timers[label])
                    timers[label] = {};

                fn.timerID = fn.timerID || this.guid++;

                var _self = this;
                var handler = function () {
                    if (!timers[label])
                        return;

                    if (timers[label][fn.timerID]) {
                        window.clearTimeout(timers[label][fn.timerID]);
                        delete timers[label][fn.timerID];
                    }

                    if (belay && _self.inProgress)
                        return;

                    _self.inProgress = true;

                    if ($.isEmptyObject($.data(element, _self.dataKey))
                        || (++counter > times && times !== 0)
                        || fn.call(element, counter) === false) {
                        $.timer.remove(element, label, fn);
                    } else {
                        timers[label] && (timers[label][fn.timerID] = window.setTimeout(handler, interval));
                    }

                    _self.inProgress = false;
                };

                handler.timerID = fn.timerID;

                if (!timers[label][fn.timerID]) {
                    handler.call(element);
                    timers[label][fn.timerID] = window.setTimeout(handler, interval);
                }

                this.global.push(element);
            },
            remove: function (element, label, fn) {
                if ($.isFunction(label)) {
                    fn = label;
                    label = null;
                }

                var timers = $.data(element, this.dataKey), ret;

                if (timers) {

                    if (!label) {
                        for (label in timers)
                            this.remove(element, label, fn);
                    } else if (timers[label]) {
                        if (fn) {
                            if (fn.timerID) {
                                window.clearTimeout(timers[label][fn.timerID]);
                                delete timers[label][fn.timerID];
                            } else {
                                for (var timerID in timers[label]) {
                                    window.clearTimeout(timers[label][timerID]);
                                    delete timers[label][timerID];
                                }
                            }
                        } else {
                            for (var timerID in timers[label]) {
                                window.clearTimeout(timers[label][timerID]);
                                delete timers[label][timerID];
                            }
                        }

                        for (ret in timers[label]) break;
                        if (!ret) {
                            delete timers[label];
                        }
                        //$.isFunction(fn) && fn.call(element, label, timers);
                    }

                    for (ret in timers) break;
                    if (!ret) {
                        $.removeData(element, this.dataKey);
                        var temp = this.global.slice(0);
                        var i = temp.length;
                        while (i--) {
                            if (temp[i] === element) {
                                temp.splice(i, 1);
                            }
                        }
                        this.global = temp;
                    }
                }
            }
        }
    });

    $(window).bind("unload", function () {
        $.each($.timer.global, function (index, item) {
            $.timer.remove(item);
        });
    });
})(jQuery);