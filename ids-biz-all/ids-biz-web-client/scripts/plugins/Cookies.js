define(function () {
    var max_size = 3500;

    var Cookies = {
        set: function (name, value) {
            if (Cookies.isGeMaxSize()) return;

            var argv = arguments;
            var argc = arguments.length;
            var expires = (argc > 2) ? argv[2] : null;
            var path = (argc > 3) ? argv[3] : '/';
            var domain = (argc > 4) ? argv[4] : null;
            var secure = (argc > 5) ? argv[5] : false;
            document.cookie = name + "=" + escape(value)
                + ((expires == null) ? "" : ("; expires=" + (expires == 'session' ? (!-[1,] ? "At the end of the Session" : "Session") : expires.toGMTString())))
                + ((path == null) ? "" : ("; path=" + path))
                + ((domain == null) ? "" : ("; domain=" + domain))
                + ((secure == true) ? "; secure" : "");
        },
        get: function (name) {
            if (document.cookie.length > 0) {
                var c_start = document.cookie.indexOf(name + "="), c_end;
                if (c_start != -1) {
                    c_start = c_start + name.length + 1;
                    c_end = document.cookie.indexOf(";", c_start);

                    if (c_end == -1)
                        c_end = document.cookie.length;

                    return unescape(document.cookie.substring(c_start, c_end));
                }
            }
            return undefined;
        },
        clear: function (name) {
            if (Cookies.get(name)) {
                var expdate = new Date();
                expdate.setTime(expdate.getTime() - (86400 * 1000 * 1));
                Cookies.set(name, "", expdate);
            }
        },
        /**
         * 获取Cookie大小
         */
        getSize: function () {
            return document.cookie.length;
        },
        /**
         * 是否达到最大长度
         */
        isGeMaxSize: function () {
            return Cookies.getSize() > Cookies.getMaxSize();
        },
        /**
         * 获取最大长度
         */
        setMaxSize: function (value) {
            max_size = value;
        },
        /**
         * 获取最大长度
         */
        getMaxSize: function () {
            return max_size;
        }
    };

    window.Cookies = Cookies;

    return Cookies;
});