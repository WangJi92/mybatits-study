package com.websockethandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * descrption: 自己定义的websockethandler
 * authohr: wangji
 * date: 2017-08-09 13:52
 */
@Slf4j
public class MyWebSocketMessageHandler extends AbstractWebSocketHandler {

    //保存连接的信息
    private static ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

    //连接成功之后
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String ID = webSocketSession.getId();
        log.info("id:"+ID);
        String arrtibute = webSocketSession.getAttributes().toString();
        log.info("arrtibute:"+arrtibute);
        log.info("websocketinfo"+webSocketSession.toString());
        users.add(webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messageInfo = message.getPayload();//获取到消息
        log.info(messageInfo);
        //这里进行判断，根据消息的条件，可以自己定义
        //比如发送给某个用户，在users=》WebSocketSession.getAttributes中去查找时候有某个用户
        //找到这个用户的webSocketSession 然后发送消息即可
        //由于我们进行拦截的时候，会将一些信息复制到Map<String, Object> attributes这个属性中
        //在建立连接的时候可以获取得到这里传递的信息
        for(WebSocketSession webSocketSession :users){
            if(webSocketSession.getAttributes().size()>0){
                if(webSocketSession.getAttributes().get("test")!=null){
                    try {
                        if(session.isOpen()){
                            session.sendMessage(new TextMessage("返回消息为"+messageInfo));//接收到了之后发送消息
                        }
                    } catch (IOException e) {
                       log.error("发送消息失败",e);
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(session);
        log.info("websocketinfo-close"+session.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("error",exception);
    }
}
