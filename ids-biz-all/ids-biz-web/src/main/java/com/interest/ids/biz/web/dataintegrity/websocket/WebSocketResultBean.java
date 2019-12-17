package com.interest.ids.biz.web.dataintegrity.websocket;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class WebSocketResultBean implements Serializable {

    private static final long serialVersionUID = -2840549030637337903L;

    private String channel;

    private JSONObject data;

    /**
     * 1: 注册 2：移除注册信息
     */
    private int type;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject content) {
        this.data = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
