package com.gopal.skillfind.skill_find_api.v1.webSocket.controller;

import com.gopal.skillfind.skill_find_api.model.Chat;
import com.gopal.skillfind.skill_find_api.model.Message;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.respones.ChatMessage;
import com.gopal.skillfind.skill_find_api.model.respones.ChatProfileResponse;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedChat;
import com.gopal.skillfind.skill_find_api.repository.ChatRepository;
import com.gopal.skillfind.skill_find_api.repository.MessageRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private UserRepository userRepository;
//    @MessageMapping("/chat.createChat")
//    public Response createChat(@Payload ModifiedChat chatCreateRequest, SimpMessageHeaderAccessor headerAccessor) {
//        String authToken = (String) headerAccessor.getSessionAttributes().get("token");
//        return chatService.createChat(chatCreateRequest, authToken);
//    }

    @MessageMapping("/chat/{senderIDString}/{receiverIDString}")
    @SendTo({"/topics/event/{senderIDString}/{receiverIDString}", "/topics/event/{receiverIDString}/{senderIDString}"})
    public Response getChatWithReceiverAndSender1(String content, @DestinationVariable String receiverIDString, @DestinationVariable String senderIDString) {
        System.out.println("getChatWithReceiverAndSender1" + senderIDString);
        String senderID = senderIDString;
        String receiverID = receiverIDString;
        Response chat = chatService.createChatByID(senderID, receiverID, content);
        System.out.println("=================getChatWithReceiverAndSender1====================");
        System.out.println(chat.getData());
        System.out.println("=====================================");
        Response response = new Response();
        if (!chat.getSuccess()) {
            return response;
        }
        Chat data = (Chat) chat.getData();
        Pageable pageable = PageRequest.of(0, 30);
        List<Message> messagesList = messageRepository.findAllByChatId(data.getId(), pageable);
        response.setData(messagesList);
        response.setMessage("Success");
        response.setSuccess(true);
        response.setStatusCode(StatusCode.SUCCESS.getCode());
        System.out.println("=====================================");
        System.out.println(response);
        System.out.println("=====================================");
        subscribeToChatProfile(senderIDString);
        subscribeToChatProfile(receiverIDString);
        return response;
    }


    @MessageMapping("/chat/{senderIDString}/{receiverIDString}/listen")
    @SendTo("/topics/event/{senderIDString}/{receiverIDString}")
    public Response subscribeToSenderReceiverChatList(@DestinationVariable String receiverIDString, @DestinationVariable String senderIDString) {
        System.out.println("subscribeToSenderReceiverChatList" + senderIDString);
        String senderID = senderIDString;
        String receiverID = receiverIDString;
        Chat chat = chatRepository.findByParticipantsUserIdAndParticipantsUserId(senderID, receiverID);
        System.out.println("===================subscribeToSenderReceiverChatList==================");
        System.out.println(chat);
        System.out.println("=====================================");
        Response response = new Response();
        if (chat == null) {
            return response;
        }
        Pageable pageable = PageRequest.of(0, 30);
        List<Message> messagesList = messageRepository.findAllByChatId(chat.getId(), pageable);
        response.setData(messagesList);
        response.setMessage("Success");
        response.setSuccess(true);
        response.setStatusCode(StatusCode.SUCCESS.getCode());
        System.out.println("=====================================");
        System.out.println(response);
        System.out.println("=====================================");
        return response;
    }

    @MessageMapping("/chat/profile/{receiverIDString}/listen")
    @SendTo("/topics/event/profile/{receiverIDString}")
    public Response subscribeToChatProfile(@DestinationVariable String receiverIDString) {
        Response response = new Response();
        List<Chat> chats = chatRepository.findByParticipantsUserIdOrderByModifiedDateDesc(receiverIDString);
        System.out.println("=================subscribeToChatProfile================");
        System.out.println(chats);
        System.out.println(receiverIDString);
        System.out.println("=================================");
        List<ChatProfileResponse> profileResponseList = new ArrayList<>();
        if (!chats.isEmpty()) {
            for (Chat chat : chats) {
                ChatProfileResponse chatProfileResponse = new ChatProfileResponse();
                Pageable pageable = PageRequest.of(0, 30);
                List<Message> messageList = messageRepository.findAllByChatId(chat.getId(), pageable);
                String userId = Objects.equals(chat.getParticipants().get(0).getUserId(), receiverIDString)
                        ? chat.getParticipants().get(1).getUserId()
                        : chat.getParticipants().get(0).getUserId();
                User user = userRepository.findUserById(userId);
                chatProfileResponse.setProfileId(user.getId());
                chatProfileResponse.setName(user.getName().isEmpty() ? "" : user.getName());
                chatProfileResponse.setProfileImage(user.getProfilePhoto().isEmpty() ? "" : user.getProfilePhoto());
                chatProfileResponse.setEmail(user.getEmail());
                chatProfileResponse.setMessageList(messageList);
                profileResponseList.add(chatProfileResponse);
            }

        }
        response.setData(profileResponseList);
        response.setMessage("Success");
        response.setStatusCode(StatusCode.SUCCESS.getCode());
        response.setSuccess(true);
        return response;
    }

    @MessageMapping("/hello/{receiverIDString}")
    @SendTo("/topics/event/{receiverIDString}")
    public List<Chat> getChatWithReceiver(String content, @DestinationVariable String receiverIDString) {
        System.out.println("getChatWithReceiver" + receiverIDString);
        int receiverID = Integer.parseInt(receiverIDString);
        return new ArrayList<>();
    }
}
