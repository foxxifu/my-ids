package com.interest.ids.biz.web.dataintegrity.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;

@Controller
public class WebSocketUrlController {

    @RequestMapping("/getWebsocketUrl")
    @ResponseBody
    public Response<String> getWebsocketUrl(HttpServletRequest request){
        
        Response<String> result = new Response<>();
        
        String ip = request.getServerName();
        int port = request.getServerPort();//request.getLocalPort();
        String contextPath = "/biz/websocket";
        
        StringBuffer websocketUrl = new StringBuffer("ws://").append(ip).append(":").append(port).append(contextPath);
        
        result.setCode(ResponseConstants.CODE_SUCCESS);
        result.setResults(websocketUrl.toString());
        
        return result;
    }
}
