package com.interest.ids.biz.web.dataintegrity.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.bean.sm.UserInfo;

/**
 * websocket 处理器，用于处理与客户端之间的通信
 * 
 * @author zl
 * @date 2018-3-9
 */

@Component("webSocketKpiReviseHandler")
public class WebSocketKpiCalcTaskHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketKpiCalcTaskHandler.class);

    // 存放客户端连接
    private static final Map<Long, WebSocketSession> webSocketSessions = new Hashtable<>();
    
    private static final Map<Long, List<String>> userHandlerMap = new Hashtable<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        
        if (session.getHandshakeAttributes() != null) {
            Object key = session.getHandshakeAttributes().get("websocket_sessionid");
            webSocketSessions.remove(key);
        }

        logger.info("Client: " + session.getRemoteAddress().getHostName() + " disconnected with server.");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        if (session.getHandshakeAttributes() != null) {
            Object key = session.getHandshakeAttributes().get("websocket_sessionid");
            if (key != null) {
                Long userId = (Long)key;
                webSocketSessions.put(userId, session);

                logger.info("Client: " + session.getRemoteAddress().getHostName() + " connected to the server.");
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

        if (session.isOpen()) {
            session.close();
        }

        if (session.getHandshakeAttributes() != null) {
            Object key = session.getHandshakeAttributes().get("websocket_sessionid");
            webSocketSessions.remove(key);
        }

        logger.error(exception.getMessage());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getHandshakeAttributes().get("websocket_sessionid");
        if (webSocketSessions.containsKey(userId)){
            String receivedMsg = message.getPayload();
            if ("HeartBeat".equalsIgnoreCase(receivedMsg)){
                session.sendMessage(new TextMessage("HeartBeat"));
                return;
            }
            
            JSONObject json = JSONObject.parseObject(message.getPayload());
            if (json != null){
                String channel = json.containsKey("channel") ? json.getString("channel") : null;
                if (channel == null || channel.trim().length() == 0){
                    return;
                }
                
                List<String> websocketBeans = userHandlerMap.get(userId);
                if (websocketBeans == null) {
                    websocketBeans = new ArrayList<>();
                    websocketBeans.add(channel);
                }else if (!websocketBeans.contains(channel)){
                    websocketBeans.add(channel);
                }
                
                userHandlerMap.put(userId, websocketBeans);
            }
        }
    }

    /**
     * 向特定的客户端发送信息
     * @param user
     * @param message
     */
    public void sendMessageToClient(UserInfo user, WebSocketResultBean result) {
        
        if (user == null || user.getId() == null || !webSocketSessions.containsKey(user.getId())){
            logger.warn("can't send kpi calculation result to client because can't identify the user{}", user);
            return;
        }
        
        WebSocketSession session = webSocketSessions.get(user.getId());
        result.setChannel("kpirevise");
        
        try {
            TextMessage message = new TextMessage(JSONObject.toJSONString(result));
            session.sendMessage(message);
        } catch (IOException e) {
            
            logger.error(e.getMessage(), e);
        }
    }
}
