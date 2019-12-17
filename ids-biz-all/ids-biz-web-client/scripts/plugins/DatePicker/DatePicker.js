/**
 * 时间控件扩展
 */
+(function () {
    require(['jquery', 'plugins/DatePicker/WdatePicker'], function ($) {

        var scrollTop = -1; // 记录当前滚动条的高度
        $('#main_view').mousemove(function () {
            if (!$('#idsDatePicker').get(0) || $('#idsDatePicker').is(':hidden')) {
                scrollTop = $(this).scrollTop();
            }
        });
        $('#main_view').scroll(function () {
            //console.log(scrollTop, $('#idsDatePicker').is(':visible'));
            if (scrollTop != -1 && $('#idsDatePicker').is(':visible')) {
                $(this).scrollTop(scrollTop);
            }
        });
        $(window).resize(function () {
            $('#idsDatePicker').hide();
        });

        window.DatePicker = function (option) {
            var $lang = main.Lang || 'zh';
            option = $.extend({}, {
                skin: 'blue',
                // skin: 'black',
                dateFmt: 'yyyy-MM-dd',
                lang: $lang
            }, option);

            if ($lang.indexOf('zh') >= 0) {
                option.lang = 'zh-cn';
            } else if ($lang.indexOf('ja') >= 0) {
                option.lang = 'ja';
                option.dateFmt = option.dateFmt.replace(/-/ig, '\/');
            } else if ($lang.indexOf('en') >= 0) {
                if (main.region == "UK") {
                    option.lang = 'uk';
                } else {
                    option.lang = 'en';
                }
                var fullTimes = option.dateFmt.split(/\s/);
                var year = (fullTimes[0].match("y+") && fullTimes[0].match("y+")[0]) || "";
                var month = (fullTimes[0].match("M+") && fullTimes[0].match("M+")[0]) || "";
                var day = (fullTimes[0].match("d+") && fullTimes[0].match("d+")[0]) || "";
                if (month && day && year) {
                    fullTimes[0] = (option.lang == 'en') ? month + "\/" + day + "\/" + year : day + "\/" + month + "\/" + year;
                } else if (month && year) {
                    fullTimes[0] = month + "\/" + year;
                } else if (year) {
                    fullTimes[0] = year;
                }
                // fullTimes[0] = (fullTimes[0] &&
                // fullTimes[0].split('').reverse().join('').replace(/[年月\-]/ig,
                // '\/').replace('日', '')) || fullTimes[0];
                option.dateFmt = (option.lang == 'en') ? fullTimes.reverse().join(' ') : fullTimes.join(' ');
            }

            WdatePicker(option);
        };

        /**
         * 获取时间控件实际时间（返回JS Date对象）
         *
         * @param did
         *        时间控件元素ID
         * @returns {Date}
         */
        window.DatePicker.getRealDate = function (did) {
            var dpDate = $dp.$DV($dp.$D(did));
            return new Date(dpDate.y, dpDate.M, dpDate.d, dpDate.H, dpDate.m, dpDate.s);
        };
        /**
         * 获取时间控件实际时间（返回WdatePicker DpDate对象）
         *
         * @param did
         * @returns {*}
         */
        window.DatePicker.getRealDPDate = function (did) {
            return $dp.$DV($dp.$D(did));
        };

        var fnval = $.fn.val;
        $.fn.val = function () {
            var $this = this;
            var result = arguments[0];
            var d = '';
            if ($this.hasClass("Wdate") && arguments.length == 1 && (typeof arguments[0] == "string")) {
                if (+arguments[0]) {
                    d = new Date(+arguments[0]);
                } else {
                    var temp_arg = arguments[0];
                    if ((main.Lang == 'en' && main.region == "UK") && (arguments[0].substring(2, 3) == "/")) {
                        if (arguments[0].length == 10) {
                            temp_arg = arguments[0].substring(3, 5) + "/" + arguments[0].substring(0, 2) + "/" + arguments[0].substring(6, 10);
                        } else if (arguments[0].length == 19) {
                            temp_arg = arguments[0].substring(3, 5) + "/" + arguments[0].substring(0, 2) + "/" + arguments[0].substring(6, 19);
                        }
                    }
                    d = new Date(temp_arg);
                }
                if (main.Lang && main.Lang == "en") {
                    if (main.region == "US") {
                        if (arguments[0].length == 7) {
                            result = d.format("MM/yyyy", true);
                        } else if (arguments[0].length == 10) {
                            result = d.format("MM/dd/yyyy", true);
                        } else if (arguments[0].length == 19) {
                            result = d.format("hh:mm:ss MM/dd/yyyy", true);
                        }
                        arguments[0] = result;
                    }
                    else if (main.region == "UK") {
                        if (arguments[0].length == 7) {
                            result = d.format("MM/yyyy", true);
                        } else if (arguments[0].length == 10) {
                            result = d.format("dd/MM/yyyy", true);
                        } else if (arguments[0].length == 19) {
                            result = d.format("dd/MM/yyyy hh:mm:ss", true);
                        }
                        arguments[0] = result;
                    }
                }
                else if (main.Lang && main.Lang == "ja") {
                    if (arguments[0].length == 7) {
                        result = d.format("yyyy/MM", true);
                    } else if (arguments[0].length == 10) {
                        result = d.format("yyyy/MM/dd", true);
                    } else if (arguments[0].length == 19) {
                        result = d.format("yyyy/MM/dd hh:mm:ss", true);
                    }
                    arguments[0] = result;

                }
            }

            if ($this.hasClass("Wdate") && !arguments.length) {
                d = fnval.apply($this, arguments);
                result = d;

                if (d && main.Lang && (main.Lang == "en" || main.Lang == "ja")) {
                    if (d.length == 7) {
                        if (main.Lang == "ja") {
                            result = d.substring(0, 4) + "-" + d.substring(5, 7);
                        } else {
                            result = d.substring(3, 7) + "-" + d.substring(0, 2);
                        }
                    } else if (d.length == 10) {
                        if (main.Lang == "en" && main.region == "UK") {
                            if (d.substring(2, 3) == "/") {
                                var temp_d = d.substring(3, 5) + "/" + d.substring(0, 2) + "/" + d.substring(6, 10);
                                result = new Date(temp_d).format("yyyy-MM-dd", true);
                            } else {
                                var temp = new Date(d).format("yyyy-MM-dd", true);
                                result = temp.substring(0, 4) + "-" + temp.substring(8, 10) + "-" + temp.substring(5, 7);
                            }
                        } else {
                            result = new Date(d).format("yyyy-MM-dd", true);
                        }
                    } else if (d.length == 19) {
                        if (main.Lang == "en" && main.region == "UK") {
                            if (d.substring(2, 3) == "/") {
                                var temp_d = d.substring(3, 5) + "/" + d.substring(0, 2) + "/" + d.substring(6, 19);
                                result = new Date(temp_d).format("yyyy-MM-dd hh:mm:ss", true);
                            } else {
                                var temp = new Date(d).format("yyyy-MM-dd hh:mm:ss", true);
                                result = temp.substring(0, 4) + "-" + temp.substring(8, 10) + "-" + temp.substring(5, 7) + " " + temp.substring(11, 19);
                            }
                        } else {
                            result = new Date(d).format("yyyy-MM-dd hh:mm:ss", true);
                        }
                    }
                }

                return result;
            } else {
                return fnval.apply($this, arguments);
            }
        }

    });
})();