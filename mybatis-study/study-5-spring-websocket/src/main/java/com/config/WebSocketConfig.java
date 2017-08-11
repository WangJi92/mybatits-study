package com.config;

import com.websockethandler.MyWebSocketMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.Map;

/**
 * descrption: websocket配置文件信息
 * authohr: wangji
 * date: 2017-08-09 13:46
 */
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
       webSocketHandlerRegistry.addHandler(MyWebSocketMessageHandler(),"socket-connect")
              .addInterceptors(new HandshakeInterceptor() {//拦截器
                  /**
                   * 握手之前，若返回false，则不建立链接
                   * 拦截器
                   */
                  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                      log.info(attributes.toString());
                      attributes.put("test","传递到websocketsession中去");
                      return true;
                  }

                  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                  }
              }).withSockJS();
    }

    @Bean
    public WebSocketHandler MyWebSocketMessageHandler(){
        return new MyWebSocketMessageHandler();
    }

    @Bean
    //Each underlying WebSocket engine exposes
    // configuration properties that control runtime characteristics
    // such as the size of message buffer sizes, idle timeout, and others.
    //http://blog.csdn.net/yxb19870428vv/article/details/41495543
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean  container = new ServletServerContainerFactoryBean ();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
