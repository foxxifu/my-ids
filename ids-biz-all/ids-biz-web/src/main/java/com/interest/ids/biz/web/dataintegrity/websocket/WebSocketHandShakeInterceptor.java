package com.interest.ids.biz.web.dataintegrity.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.redis.caches.SessionCache;

/**
 * websocket握手拦截器
 * 
 * @author zl
 * @date 2018-3-8
 */

public class WebSocketHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandShakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
            Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String tokenId = servletRequest.getParameter("tokenId");
            SessionCache sessionCache = (SessionCache) SystemContext.getBean("sessionCache");
            UserInfo user = sessionCache.getAttribute(tokenId, "user");
            if(user != null){
                attributes.put("websocket_sessionid", user.getId());
                return super.beforeHandshake(request, response, handler, attributes);
            }else{
                logger.warn("user doesn't authenticate, websocket handshake failed.");
            }
        }
        
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
            Exception exception) {
        
        super.afterHandshake(request, response, handler, exception);
    }
}
