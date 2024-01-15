package com.gopal.skillfind.skill_find_api.v1.webSocket.controller;

import com.gopal.skillfind.skill_find_api.model.Chat;
import com.gopal.skillfind.skill_find_api.model.Message;
import com.gopal.skillfind.skill_find_api.model.respones.ChatMessage;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedChat;
import com.gopal.skillfind.skill_find_api.repository.ChatRepository;
import com.gopal.skillfind.skill_find_api.repository.MessageRepository;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import com.gopal.skillfind.skill_find_api.v1.service.ChatService;
import com.gopal.skillfind.skill_find_api.v1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

//    @MessageMapping("/chat.createChat")
//    public Response createChat(@Payload ModifiedChat chatCreateRequest, SimpMessageHeaderAccessor headerAccessor) {
//        String authToken = (String) headerAccessor.getSessionAttributes().get("token");
//        return chatService.createChat(chatCreateRequest, authToken);
//    }

    @MessageMapping("/chat/{senderIDString}/{receiverIDString}")
    @SendTo({"/topics/event/{senderIDString}/{receiverIDString}", "/topics/event/{receiverIDString}/{senderIDString}"})
    public Response getChatWithReceiverAndSender1(String content, @DestinationVariable String receiverIDString, @DestinationVariable String senderIDString) {
        String senderID = senderIDString;
        String receiverID = receiverIDString;
        Response chat = chatService.createChatByID(senderID, receiverID, content);
        Response response = new Response();
        if (!chat.getSuccess()) {
            return chat;
        }
        Chat data = (Chat) chat.getData();
        Pageable pageable = PageRequest.of(1, 20);
        List<Message> messagesList = messageRepository.findAllById(data.getId(), pageable);
        response.setData(messagesList);
        response.setMessage("Success");
        response.setSuccess(true);
        response.setStatusCode(StatusCode.SUCCESS.getCode());
        return response;
    }


    @MessageMapping("/chat/{senderIDString}/{receiverIDString}/listen")
    @SendTo("/topics/event/{senderIDString}/{receiverIDString}")
    public Response subscribeToSenderReceiverChatList(@DestinationVariable String receiverIDString, @DestinationVariable String senderIDString) {

        String senderID = senderIDString;
        String receiverID = receiverIDString;
        Chat chat = chatRepository.findByParticipantsUserIdAndParticipantsUserId(senderID, receiverID);
        Response response = new Response();
        if (chat==null) {
            return response;
        }
        Pageable pageable = PageRequest.of(1, 20);
        List<Message> messagesList = messageRepository.findAllById(chat.getId(), pageable);
        response.setData(messagesList);
        response.setMessage("Success");
        response.setSuccess(true);
        response.setStatusCode(StatusCode.SUCCESS.getCode());
        return response;
    }

//    @MessageMapping("/hello/{receiverIDString}")
//    @SendTo("/topics/event/{receiverIDString}")
//    public List<Chat> getChatWithReceiver(String content, @DestinationVariable String receiverIDString) {
//        int receiverID = Integer.parseInt(receiverIDString);
//        return chatService.findByReceiver(receiverID);
//    }
}
