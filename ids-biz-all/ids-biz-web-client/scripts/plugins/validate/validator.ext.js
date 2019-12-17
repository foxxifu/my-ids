;(function ($) {
    $.validator.setDefaults({
        errorElement : 'span',
        success: function (label, element) {
            $(element).tooltip('destroy');
        },
        errorPlacement : function(error, element) {
            element.tooltip('destroy');
            var errorText = $(error).text();
            element.tooltip({
                title: errorText,
                placement: "auto right",
                container: element.closest('dialog') || '#main_view'
            }).tooltip('show');
        }
    });

    $.extend($.validator.messages, {

        required: Msg.validator.required,
        remote: Msg.validator.remote,
        email: Msg.validator.email,
        url: Msg.validator.url,
        date: Msg.validator.date,
        dateISO: Msg.validator.dateISO,
        number: Msg.validator.number,
        digits: Msg.validator.digits,
        creditcard: Msg.validator.creditcard,
        equalTo: Msg.validator.equalTo,
        signsCheck: Msg.validator.signsCheck,
        maxlength: $.validator.format(Msg.validator.maxlength),
        minlength: $.validator.format(Msg.validator.minlength),
        rangelength: $.validator.format(Msg.validator.rangelength),
        range: $.validator.format(Msg.validator.range),
        max: $.validator.format(Msg.validator.max),
        min: $.validator.format(Msg.validator.min),
        /**扩展自定义message*/
        space: Msg.validator.space,
        mobile: Msg.validator.mobile,
        phone: Msg.validator.phone,
        vacTel: Msg.validator.phone,
        tel: Msg.validator.tel,
        zip: Msg.validator.zip,
        currency: Msg.validator.currency,
        qq: Msg.validator.qq,
        age: Msg.validator.age,
        idcard: Msg.validator.idcard,
        ip: Msg.validator.ip,
        ipPort: Msg.validator.ipPort,
        chrnum: Msg.validator.chrnum,
        chinese: Msg.validator.chinese,
        english: Msg.validator.english,
        selectNone: Msg.validator.selectNone,
        byteRangeLength: $.validator.format(Msg.validator.byteRangeLength),
        stringCheck: Msg.validator.stringCheck,
        same: Msg.validator.same,
        semiangle: Msg.validator.semiangle,
        passwordCheck: Msg.validator.userPasswordCheck,
        PSIDCheck: Msg.validator.PSIDCheck,
        PSNameCheck: Msg.validator.PSNameCheck,
        nullCheck: Msg.validator.nullCheck,
        dateCheck: Msg.validator.dateCheck,
        percentCheck: Msg.validator.percentCheck,
        spaceString: Msg.validator.spaceString,
        onlySpace: Msg.validator.onlySpace,
        decimalLength: $.validator.format(Msg.validator.decimalLength),
        charnumber: Msg.validator.charnumber,
        specialcharnumber: Msg.validator.specialcharnumber,
        specialspaceString: Msg.validator.specialspaceString,
        textSpecialString: Msg.validator.textSpecialString,
        specialchinese: Msg.validator.specialchinese,
        minTo: Msg.validator.minTo,
        maxTo: Msg.validator.maxTo,
        numCheck: "请输入0~480000的正整数",
        port: Msg.validator.port,
        notEqualToWriteBack: Msg.validator.notEqualToWriteBack,
        perNumCheck: Msg.validator.perNumCheck,
        devNameCheck: Msg.validator.devNameCheck,
        positiveInt: Msg.validator.positiveInt,
        validateSpecicalChars: Msg.validator.specialChars,
        vacSepecialString: Msg.validator.vacSepecialString,
        lt: Msg.validator.lt,
        le: Msg.validator.le,
        gt: Msg.validator.gt,
        ge: Msg.validator.ge,
        numberCheck: Msg.validator.valNumberCheck,
        comparGtTime: Msg.validator.comparGtTime,
        comparLtTime: Msg.validator.comparLtTime,
        notEqualTo: Msg.validator.notEqualTo

    });
    $.extend($.validator.methods, {
        semiangle: function (value, element) {
            var flag = true;
            for (var i = 0; i < value.length; i++) {
                var strCode = value.charCodeAt(i);
                if ((strCode > 65248) || (strCode == 12288)) {
                    flag = false;
                }
            }
            return this.optional(element) || flag;
        },
        space: function (value, element) {
            var flag = true;
            if (value.startWith(' ') || value.endWith(' ')) {
                flag = false;
            }
            return this.optional(element) || flag;
        },
        mobile: function (value, element) {
            //var mobile = /^1\d{10}$/;
            var mobile = /^[1][34578]\d{9}$/;
            return this.optional(element) || (mobile.test(value));
        },
        passwordCheck: function (value, element) {//用户密码校验
            var length = value.length;
            var flagArr = [];
            var flagNum, flagLow, flagUp, flagLetter, flagZf, flagCon;
            var flag = true;
            //	var especialChar =/^(([a-z]+[A-Z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([a-z]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([a-z]+[0-9]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([a-z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[a-z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[0-9]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([A-Z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[A-Z]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[a-z]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([0-9]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[0-9]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[A-Z]+[a-z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[A-Z]+[0-9]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[a-z]+[0-9]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[A-Z]+[a-z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*)|([!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~]+[0-9]+[a-z]+[A-Z]+[!\"#\$%&\'()*+,-./:;<=>?@[\]^_`{|}~a-zA-Z0-9]*))$/;
            //数字
            var regNum = /[0-9]/g;
            //小写
            var regLow = /[a-z]/g;
            //大写
            var regUp = /[A-Z]/g;
            //$!"#&%'()*+,-./:;<>?@[\]^_`{}|~
            var regZf = /[\$\!\"\#\&\%\'\(\)\*\+\,\-\.\/\:\;\<\>\?\@\[\\\]\^\_\`\{\}\|\~]/g;
//                    var booFlag = false;
//                    for (var j = 0; j < value.length - 2 && flag; j++) {
//                        if ((value.charAt(j) == value.charAt(j + 1)) || (value.charAt(j + 1) == value.charAt(j + 2))) {
//                            booFlag = true;
//                        }
//                    }
            flagNum = regNum.test(value);
            flagLow = regLow.test(value);
            flagUp = regUp.test(value);
            flagZf = regZf.test(value);
            flagArr.push(flagNum);
            flagArr.push(flagLow);
            flagArr.push(flagUp);
            flagArr.push(flagZf);
            //数字，大写字母，小写字母，特殊字符四种情况可以任选三种
            for (var i = 0; i < flagArr.length; i++) {
                if (!flagArr[i]) {
                    for (var j = i + 1; j < flagArr.length; j++) {
                        if (!flagArr[j]) {
                            flag = false;
                            return this.optional(element) || flag;
                        }
                    }
                }
            }
            return this.optional(element) || flag;
            //return this.optional(element) || (regNum.test(value) && regLow.test(value) && regUp.test(value) && regZf.test(value) && flag);
        },
        notEqualToWriteBack: function (value, element, notEqualToWriteBackId) {
            var val = $(notEqualToWriteBackId).val();
            if (val) {
                var wb = val.split("").reverse().join("");
                if (value == wb || value == val) {
                    return this.optional(element) || false;
                }
            }
            return this.optional(element) || true;
        },
        signsCheck: function (value, element) {
            return this.optional(element) || (!value.contains(',') && !value.contains('，'));
        },
        phone: function (value, element) {
            var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
            return this.optional(element) || (tel.test(value));
        },
        tel: function (value, element) {
            var tel = /(^[0-9]{3,4}[\-]{0,1}[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1\d{10}$)/;
            return this.optional(element) || (tel.test(value));
        },
        zip: function (value, element) {
            var tel = /^[0-9]{6}$/;
            return this.optional(element) || (tel.test(value));
        },
        currency: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        qq: function (value, element) {
            var tel = /^[1-9]\d{4,9}$/;
            return this.optional(element) || (tel.test(value));
        },
        age: function (value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        idcard: function (value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        ip: function (value, element) {
            var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            var flag1 = this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
            var ip_port = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):)(?:[1-9]([0-9]?){4})$/;
            var flag2 = this.optional(element) || (ip_port.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256 && RegExp.$5 < 65536));
            var realm = /([a-zA-Z]+\.){2,3}[a-zA-Z]+$/;
            var flag3 = this.optional(element) || realm.test(value);
            return flag1 || flag2 || flag3;
        },
        ipPort: function (value, element) {
            var ip_port = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):)(?:[1-9]([0-9]?){4})$/;
            var flag2 = this.optional(element) || (ip_port.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256 && RegExp.$5 < 65536));
            var realm = /([a-zA-Z]+\.){2,3}[a-zA-Z]+$/;
            var flag3 = this.optional(element) || realm.test(value);
            return flag2 || flag3;
        },
        chrnum: function (value, element) {
            var chrnum = /^([a-zA-Z0-9]+)$/;
            return this.optional(element) || (chrnum.test(value));
        },
        charnumber: function (value, element) {
            var chrnum = /^([\w\s]+)$/;
            return this.optional(element) || (chrnum.test(value));
        },
        specialcharnumber: function (value, element) {
            var chrnum = /^([a-zA-Z0-9\s_\!\"\#\&\%\&\'\(\)\*\+\,\-\.\/\:\;\<\=\>\?\@\[\\\]\^\_\`\{\}\|\~\$\ ]+)$/;
            return this.optional(element) || (chrnum.test(value));
        },
        chinese: function (value, element) {
            var chinese = /^[\u4e00-\u9fa5]+$/;
            return this.optional(element) || (chinese.test(value));
        },
        specialchinese: function (value, element) {
            var chinese = /^[\u4e00-\u9fa5\u0800-\u4e00\!\"\#\&\%\&\'\(\)\*\+\,\-\.\/\:\;\<\=\>\?\@\[\\\]\^\_\`\{\}\|\~\$\ ]+$/;
            return this.optional(element) || (chinese.test(value));
        },
        english: function (value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        byteRangeLength: function (value, element, param) {
            var length = value.length;
            for (var i = 0; i < value.length; i++) {
                if (value.charCodeAt(i) > 127) {
                    length++;
                }
            }
            return this.optional(element) || (length >= param[0] && length <= param[1]);
        },
        stringCheck: function (value, element) {
            return this.optional(element) || (value != "null" && /^[\u0800-\u4e00\u4e00-\u9fa5\w]+$/.test(value));
        },
        filterIllegal: function (value, element) {
            var str = value;
            str = str.replace(/</g, '&lt;');
            str = str.replace(/>/g, '&gt;');
            // str = str.replace(/ /g, '&nbsp;');
            // str = str.replace(/x22/g, '&quot;');
            // str = str.replace(/x27/g, '&#39;');
            value = str;
            $(element).val(value);
            return true;
        },
        //电站ID验证，只能包含数字、字母和下划线
        PSIDCheck: function (value, element) {
            var regPSID = /^\w*$/;
            return this.optional(element) || regPSID.test(value);
        },
        //电站名称验证，首尾的引号不能相同
        PSNameCheck: function (value, element) {
            var reg = /^(['"])([])(.)*\1$/;
            return this.optional(element) || !reg.test(value);
        },
        //用户名限制不能是null
        nullCheck: function (value, element) {
            return this.optional(element) || value.toLowerCase() != "null";
        },
        //验证日期 格式为yyyyMMdd
        dateCheck: function (value, element) {
            value = $.trim(value);
            var regDate = /^(?:(?!0000)[0-9]{4}(?:(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])(?:29|30)|(?:0[13578]|1[02])31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)0229)$/;
            return this.optional(element) || regDate.test(value);
        },
        //验证为数字(正整数、负数和零)
        numCheck: function (value, element) {
            var regNum = /^-?\d+$/;
            return this.optional(element) || regNum.test(value);
        },
        //限制百分比 0 ~ 100
        percentCheck: function (value, element) {
            var num = Number(value);
            var flag = false;
            if (num || num == 0) {
                if (num >= 0 && num <= 100) {
                    flag = true;
                }
            }
            return this.optional(element) || flag;
        },
        //检测只有中文、英文、空格、数字和下划线，不包含中文的特殊字符
        spaceString: function (value, element) {
            var reg = /^[\u0391-\uFFE5\w\&\s]+$/;
            var regChar = /[！…￥（）—，。“”：；、？【】《》]/;
            var flag = reg.test(value) && !regChar.test(value);
            return this.optional(element) || flag;
        },
        //spaceString的包含特殊字符版本
        specialspaceString: function (value, element) {
            var reg = /^[\u0391-\uFFE5\w\s\!\"\#\&\%\&\'\(\)\*\+\,\-\.\/\:\;\<\=\>\?\@\[\\\]\^\_\`\{\}\|\~\$\ ]+$/;
            var regChar = /[！…￥（）—，。“”：；、？【】《》]/;
            var flag = reg.test(value) && !regChar.test(value);
            return this.optional(element) || flag;
        },
        //文本特殊字符排除(文本中不能包含&(排除html实体字符),<>(排除html标签符))
        textSpecialString: function (value, element) {
            var regChar = /[&<>]/;
            var flag = !regChar.test(value);
            return this.optional(element) || flag;
        },
        //限制不能输入空格(半角和全角下的空格)
        onlySpace: function (value, element) {
            value = $.trim(value);
            if (value == '') {
                return this.optional(element) || false;
            }
            return this.optional(element) || true;
        },
        //限制小数点后位数
        decimalLength: function (value, element, param) {
            if (!isNaN(value)) {
                var dot = value.indexOf(".");
                if (dot != -1) {
                    var len = value.substring(dot + 1).length;
                    if (len > param) {
                        return this.optional(element) || false;
                    }
                }
            }
            return this.optional(element) || true;
        },
        // 是否比指定元素值小
        minTo: function (value, element, param) {
            var m = $(param).val();
            return this.optional(element) || +value < +m
        },
        // 是否比指定元素值大
        maxTo: function (value, element, param) {
            var m = $(param).val();
            return this.optional(element) || +value > +m
        },
        lt: function (value, element, param) {
            return this.optional(element) || value < param;
        },
        le: function (value, element, param) {
            return this.optional(element) || value <= param;
        },
        gt: function (value, element, param) {
            return this.optional(element) || value > param;
        },
        ge: function (value, element, param) {
            return this.optional(element) || value >= param;
        },
        numberCheck: function (value, element, param) {
            return this.optional(element) || $.isNumeric(value);
        },
        port: function (value) {
            if(!value || value==''){
                return true;
            }
            var regex = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
            return regex.test(value);
        },
        /**
         * 电话验证
         * 可输入11位手机或座机（010-XXXXXXXX，0731-XXXXXXX）
         */
        vacTel: function (tel, element) {
            var machPhone = tel.match(/(?:\(?[0\+]?\d{1,3}\)?)[\s-]?(?:0|\d{1,4})[\s-]?(?:(?:13\d{9})|(?:\d{7,8}))/g);
            if (machPhone && machPhone.length == 1 && tel == machPhone[0]) {
                return this.optional(element) || true;
            }
            var machTel = tel.match(/(?:\(?[0\+]\d{2,3}\)?)[\s-]?(?:(?:\(0{1,3}\))?[0\d]{1,4})[\s-](?:[\d]{7,8}|[\d]{3,4}[\s-][\d]{3,4})/g);
            if (machTel && machTel.length == 1 && tel == machTel[0]) {
                return this.optional(element) || true;
            }
            return this.optional(element) || false;
        },

        /**
         * 用户名验证
         * 不可输入['<', '>', '$']
         * @param userName 用户名
         * @param maxLength 输入长度最大值 默认64位
         * @param minLength 输入长度最小值 默认1位
         */
        vacUserName: function (userName, maxLength, minLength) {
            if (!maxLength) {
                maxLength = 64;
            }
            if (!minLength) {
                minLength = 1;
            }
            var nameLength = userName.length;
            if (nameLength > maxLength || nameLength < minLength || userName.trim() == 'null') {
                return false;
            }
            var speChara = ['<', '>', '|', '’', '?', '&', ','];
            for (var i = 0; i < speChara.length; i++) {
                if (userName.indexOf(speChara[i]) != -1) {
                    return false;
                }
            }
            return true;
        },

        /**
         * 密码验证
         * @param password 密码
         * @param maxLength 输入长度最大值 默认64位
         * @param minLength 输入长度最小值 默认6位
         */
        vacPassword: function (password, maxLength, minLength) {
            if (!maxLength) {
                maxLength = 64;
            }
            if (!minLength) {
                minLength = 6;
            }
            var pwdLength = password.length;
            if (pwdLength < minLength || pwdLength > maxLength) {
                return false;
            }
            var pwd = password;
            var regpwd = /[0-9a-zA-Z]/g;
            if (regpwd.test(pwd)) {
                pwd = pwd.replace(regpwd, '');
            }
            var regZf = /[\!\"\#\$\%\&\'\(\)\*\+\,\-\.\/\:\;\<\=\>\?\@\[\\\]\^\_\`\{\}\|\~]/g;
            if (regZf.test(pwd)) {
                pwd = pwd.replace(regZf, '');
            }
            if (pwd.length > 0) {
                return false;
            }
            return true;
        },

        /**
         * 登录名验证
         * 不可输入['<', '>', '$']
         * @param loginName 登录名
         * @param maxLength 输入长度最大值 默认64位
         * @param minLength 输入长度最小值 默认1位
         */
        vacLoginName: function (loginName, maxLength, minLength) {
            if (!maxLength) {
                maxLength = 64;
            }
            if (!minLength) {
                minLength = 1;
            }
            var nameLength = loginName.length;
            if (nameLength > maxLength || nameLength < minLength || loginName.trim() == 'null') {
                return false;
            }
            var speChara = ['<', '>', '|', '’', '?', '&', ',', ' '];
            for (var i = 0; i < speChara.length; i++) {
                if (loginName.indexOf(speChara[i]) != -1) {
                    return false;
                }
            }
            return true;
        },

        /**
         * QQ验证 只能输入数字
         */
        vacQQ: function (qq) {
            var vac = /^[0-9]*$/;
            if (!vac.test(qq)) {
                return false;
            }
            return true;
        },

        vacMail: function (mail) {
            var vac = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}$/;
            if (!vac.test(mail)) {
                return false;
            }
            return true;
        },
        /*
         * 1-365校验
         */
        dayOfyear: function (value, element) {
            var reg = /^(0|[1-9][0-9]*|-[1-9][0-9]*)$/;
            var flag = reg.test(value);//整数
            if (flag && value >= 1 && value <= 365) {
                flag = true;
            } else {
                flag = false;
            }
            return this.optional(element) || flag;
        },

        /**
         * 校验(0,100]的数据，小数点不超过param位
         */
        perNumCheck: function (value, elemet, param) {
            var reg = "/^\\d{1,3}(.\\d{1," + param + "}){0,1}$/";
            if (!main.eval(reg).test(value))
                return false;
            var num = Number(value);
            if (num > 0 && num <= 100)
                return true;
            return false;
        },

        /**
         * 名称验证，不包括< ' > & "
         */
        devNameCheck: function (value, element, type) {
            var reg = /^[^\'\<\>,\/\&\"'|]*$/;
            return this.optional(element) || (reg.test(value) && !value.contains("null"));
        },

        /**
         * 端口校验
         */
        vacPort: function (value) {
            var regex = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
            return regex.test(value);
        },


        /**
         * 名称类特殊字符校验：
         * 不包含| < > ' , & /
         */
        vacSepecialString: function (value) {
            var regChar = /[\|, <, >, ', \,, &, \/]/;
            var flag = regChar.test(value) || value.contains("null");
            return !flag;
        },

        /**
         * 特殊字符校验：
         * 不包含| < > ' ", & \ / { } null
         * 不包含上述特殊字符，返回 true, 否则返回false
         */
        validateSpecicalChars: function (value) {

            var regChar = /[\|, <, >, ',\", \,, &, \\,\/,{,}]/;
            var flag = regChar.test(value) || value.contains("null");
            return !flag;
        },


        /**
         * 正整数
         */
        positiveInt: function (value) {
            var regChar = /^[0-9]*[1-9][0-9]*$/;
            return regChar.test(value);
        },

        vacCombinerDCVolt: function (value, element, type) {
            var reg = /^[1-9]\d{2,3}$/;
            if (!reg.test(value))
                return false;
            var num = Number(value);
            if (num >= 300 && num <= 1000)
                return true;
            return false;
        },
        /**
         * 校验2级域设备数和用户数
         */
        vacMaxDevAndUserNum: function (value, element, type) {
            var reg = /^[1-9]\d{0,8}$/;
            if (reg.test(value))
                return true;
            return false;
        },
        /**
         * 校验2级域装机容量
         */
        vacMaxInstallCap: function (value, element, type) {
            var reg = /^(?:[1-9]\d{0,9}|0)(?:\.\d{1,3})?$/;
            if (reg.test(value) && value > 0)
                return true;
            return false;
        },
        /**
         * 比较时间 大于to的时间   用法如下  to表示开始时间   formatter 表示时间格式的规则
         * businesEndHours:{
						comparGtTime:{to:"#busines_begin",formatter:Msg.dateFormat.HHmmss}
					}
         */
        comparGtTime: function (val, e, obj) {
            var btime = Date.parse(val, obj.formatter).getTime();
            var etime = Date.parse($(obj.to).val(), obj.formatter).getTime();
            if (btime <= etime) {
                return false;
            }
            return true;
        },
        /**
         * 比较时间 小于to的时间   用法如下  to表示结束始时间   formatter 表示时间格式的规则
         * businesStartHours:{
						comparLtTime:{to:"#busines_end",formatter:Msg.dateFormat.HHmmss}
					}
         */
        comparLtTime: function (val, e, obj) {
            var btime = Date.parse($(obj.to).val(), obj.formatter).getTime();
            var etime = Date.parse(val, obj.formatter).getTime();
            if (btime <= etime) {
                return false;
            }
            return true;
        },
        //不等于
        notEqualTo: function (val, e, ele) {
            var oval = $(ele).val();
            if (val == oval) {
                return false;
            }
            return true;
        }
    });
})(jQuery);
