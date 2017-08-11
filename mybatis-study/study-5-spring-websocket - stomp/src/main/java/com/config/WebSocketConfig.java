package com.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * descrption: websocket配置文件信息
 * authohr: wangji
 * date: 2017-08-09 13:46
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");//浏览器订阅的前缀
        registry.setApplicationDestinationPrefixes("/app");//浏览器发送的前缀

    }
}
