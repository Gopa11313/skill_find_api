package com.gopal.skillfind.skill_find_api.v1.webSocket.controller;

import com.gopal.skillfind.skill_find_api.model.Chat;
import com.gopal.skillfind.skill_find_api.model.respones.ChatMessage;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedChat;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.ChatService;
import com.gopal.skillfind.skill_find_api.v1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.createChat")
    public Response createChat(@Payload ModifiedChat chatCreateRequest, SimpMessageHeaderAccessor headerAccessor) {
        String authToken = (String) headerAccessor.getSessionAttributes().get("token");
        return chatService.createChat(chatCreateRequest, authToken);
    }

//    @MessageMapping("/chat.sendMessage")
//    public void sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
//        // Assuming ChatMessageRequest contains senderUsername, receiverUsername, chatId, and content
//        Message message = messageService.sendMessage(
//                chatMessageRequest.getSenderUsername(),
//                chatMessageRequest.getReceiverUsername(),
//                chatMessageRequest.getChatId(),
//                chatMessageRequest.getContent()
//        );
//
//    }

}
