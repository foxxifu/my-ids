(function (factory) {
    "use strict";
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else {
        factory(window);
    }
}(function () {
    var ws = {
        wsUrl: "ws://" + window.location.host + "/websocket",
        wssUrl: "wss://" + window.location.host + "/websocket",
        isSSl: window.location.href.indexOf("https") == 0,
        wso: null,
        register: {},
        registerData: {}
    };

    var wsfn = function (option) {
    };
    wsfn.prototype = {
        fnRegister: function (channel, content, callback) {
            ws.register[channel] = callback;
            ws.registerData[channel] = content;
            var json = {channel: channel, content: content, type: 1};
            var jsonData = JSON.stringify(json);
            wsf.sendMsg(jsonData, function () {
                console.info("to register " + channel);
            });
        },
        fnStop: function (channel) {
            var json = {channel: channel, content: "", type: 2};
            var jsonData = JSON.stringify(json);
            wsf.sendMsg(jsonData, function () {
                delete ws.register[channel];
                delete ws.registerData[channel];
                console.info("to stopPush " + channel);
            });
        },
        fnStopAll: function () {
            if (ws.register && !$.isEmptyObject(ws.register)) {
                for (var k in ws.register) {
                    if (ws.register.hasOwnProperty(k)) {
                        wsf.fnStop(k);
                    }
                }
            }
        },
        sendMsg: function (msg, callBack) {
            if (ws.wso && 1 == ws.wso.readyState) {
                ws.wso.send(msg);
                callBack && typeof callBack == "function" && callBack();
            } else {
                console.info('can not send msg,try reconnet');
                this.reconnet();
            }
        },
        //重新注册
        fnReRegister: function () {
            if (ws.register && !$.isEmptyObject(ws.register)) {
                for (var k in ws.register) {
                    var tdata = ws.registerData.hasOwnProperty(k) ? ws.registerData[k] : "";
                    var json = {channel: k, content: tdata, type: 1};
                    var jsonData = JSON.stringify(json);
                    wsf.sendMsg(jsonData, function () {
                        console.info("to reRegister sucess " + k);
                    });
                }
            }
        }
    };
    var wsf = new wsfn();
    var lockReconnect = false;//避免重复连接
    var heartCheck = {
        timeout: 60000,//60秒
        timeoutObj: null,
        serverTimeoutObj: null,
        reset: function () {
            clearTimeout(this.timeoutObj);
            clearTimeout(this.serverTimeoutObj);
            return this;
        },
        start: function () {
            var self = this;
            this.timeoutObj = setTimeout(function () {
                //这里发送一个心跳，后端收到后，返回一个心跳消息，
                //onmessage拿到返回的心跳就说明连接正常
                wsf.sendMsg("HeartBeat");
                self.serverTimeoutObj = setTimeout(function () {//如果超过一定时间还没重置，说明后端主动断开了
                    ws.wso.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                }, self.timeout)
            }, this.timeout)
        }
    };

    var IDSWebSocket = {
        /**
         * 注册
         */
        startRegister: function (channel, content, callback) {
       
            if (!this.canUseWebSocket()) {
                return false;
            }
            wsf.fnRegister(channel, content, callback);
        },

        /**
         * 终止注册
         */
        stopPush: function (channel) {
            if (!this.canUseWebSocket()) {
                return false;
            }
            wsf.fnStop(channel);
        },
        /**
         * 停止所有的注册
         */
        stopAllPush: function () {
            wsf.fnStopAll();
        },
        /**
         * 获取websocket 状态
         */
        getWebSocktState: function () {
            return ws.wso ? ws.wso.readyState : -1;
        },
        /**
         * 检测是否支持websocket
         */
        checkSupport: function () {
            return "WebSocket" in window || "MozWebSocket" in window;
        },
        /**
         * 连接
         */
        initWebSocket: function (url) {
            var self = this;
            if (!this.checkSupport) {
                console.info("not support WebSocket");
                return;
            }
            if (ws.wso && ws.wso.readyState == 1) {
                console.info("WebSocket has connect");
                return;
            }
            try {
                var tokenUrl = ws.isSSl ? (url ? (ws.wssUrl =url) : ws.wssUrl) : (url ? (ws.wsUrl=url) : ws.wsUrl);
                tokenUrl += '?tokenId='+(Cookies.get('tokenId')||'');
                if ("WebSocket" in window) {
                    ws.wso = new WebSocket(tokenUrl);
                } else if ("MozWebSocket" in window) {
                    ws.wso = new MozWebSocket(tokenUrl);
                }
            } catch (e) {
                console.error(e);
            }

            if (ws.wso) {
                var wso = ws.wso;
                wso.onopen = function () {
                    console.log("connect success");
                    heartCheck.reset().start();
                    wsf.fnReRegister();
                };
                wso.onclose = function () {
                    console.info("wso onclose and try reconnet");
                    try {
                        self.reconnet();
                    } catch (e) {
                        console.info("reconnet error" + e);
                    }

                };
                wso.onerror = function () {
                    console.info("onerror try reconnet");
                    try {
                        self.reconnet();
                    } catch (e) {
                        console.info("reconnet error" + e);
                    }
                };
                wso.onmessage = function (receiveMsg) {
                    heartCheck.reset().start();
                    if (receiveMsg.data == "HeartBeat") {
                        return;
                    } else {
                        try {
                            var jsonObject = JSON.parse(receiveMsg.data);
                            var callback = ws.register[jsonObject.channel];
                            
                            callback && $.isFunction(callback) && callback(jsonObject.data);
                        } catch (e) {
                            console.info(receiveMsg);
                        }
                    }
                }
            }
        },
        /**
         * webSocket重连
         */
        reconnet: function () {
            var self = this;
            if (lockReconnect) return;
            lockReconnect = true;
            //没连接上会一直重连，设置延迟避免请求过多
            setTimeout(function () {
                self.initWebSocket();
                lockReconnect = false;
            }, 5000);
        },

        canUseWebSocket: function () {
            return !!(this.checkSupport() && this.getWebSocktState() == 1);
        }
    };

    window.IDSWebSocket = IDSWebSocket;

    return IDSWebSocket;
}));