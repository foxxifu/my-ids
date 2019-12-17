package com.interest.ids.biz.web.dataintegrity.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 配置类，配置连接建立路径，处理器，拦截器
 * 
 * @author zl
 * @date 2018-3-9
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry register) {

        register.addHandler(getHandler(), "/websocket").addInterceptors(new WebSocketHandShakeInterceptor());

        // 浏览器不支持websocket时，采用sockjs方式
        register.addHandler(getHandler(), "/sockjs").addInterceptors(new WebSocketHandShakeInterceptor())
                .withSockJS();
    }

    @Bean
    public WebSocketKpiCalcTaskHandler getHandler() {
        return new WebSocketKpiCalcTaskHandler();
    }
}
