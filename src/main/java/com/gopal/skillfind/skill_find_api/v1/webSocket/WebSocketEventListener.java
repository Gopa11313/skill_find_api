package com.gopal.skillfind.skill_find_api.v1.webSocket;

import com.gopal.skillfind.skill_find_api.model.respones.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
//    @EventListener
//    public void handleWebsocketDisconnect(SessionDisconnectEvent event) {
//        StompHeaderAccessor stompHeaders = StompHeaderAccessor.wrap(event.getMessage());
//        String userName =(String) stompHeaders.getSessionAttributes().get("id");
//        if(userName ==null){
//          log.info("User disconnected:{}",userName);
//          var chatMessage = ChatMessage.builder
//        }
//    }
}
