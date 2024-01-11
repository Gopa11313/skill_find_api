package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.respones.ModifiedChat;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/chat")
@AllArgsConstructor
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/createChat")
    private Response createChat(@RequestBody ModifiedChat chat, @RequestHeader("Authorization") String authorizationHeader) {
        return chatService.createChat(chat, authorizationHeader);
    }
    @GetMapping("/getChat/{page}")
    private Response getChats(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("page") String pageNum){
        return chatService.getChats(authorizationHeader,pageNum);
    }
}
