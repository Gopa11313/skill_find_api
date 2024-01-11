package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Chat;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.Message;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedChat;
import com.gopal.skillfind.skill_find_api.repository.ChatRepository;
import com.gopal.skillfind.skill_find_api.repository.MessageRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Participants;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private LogService logService;

    public Response createChat(ModifiedChat chat, String token) {
        Response response = new Response();
        try {
            if (token != null && !token.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(token);
                if (retriveUser != null) {
                    Chat dbChat = chatRepository.findByParticipantsUserIdAndParticipantsUserId(retriveUser.getId(), chat.getParticipants().get(0).getUserId());
                    User receiver = userRepository.findUserById(chat.getParticipants().get(0).getUserId());
                    if (dbChat != null) {
                        Message message = new Message();
                        message.setChatId(dbChat.getId());
                        message.setSender(new Participants(retriveUser.getId(), retriveUser.getEmail()));
                        message.setReceiver(new Participants(receiver.getId(), receiver.getEmail()));
                        message.setContent(chat.getMessage());
                        message.setTimestamp(DateUtils.getCurrentDate());
                        messageRepository.save(message);
                    } else {
                        Chat toBeChat = new Chat();
                        List<Participants> participants = new ArrayList<>();
                        participants.add(new Participants(retriveUser.getId(), retriveUser.getEmail()));
                        participants.add(new Participants(receiver.getId(), receiver.getEmail()));
                        toBeChat.setParticipants(participants);
                        Chat savedChat = chatRepository.save(toBeChat);
                        Message message = new Message();
                        message.setChatId(savedChat.getId());
                        message.setSender(new Participants(retriveUser.getId(), retriveUser.getEmail()));
                        message.setReceiver(new Participants(receiver.getId(), receiver.getEmail()));
                        message.setContent(chat.getMessage());
                        message.setTimestamp(DateUtils.getCurrentDate());
                        messageRepository.save(message);
                    }
                } else {
                    response.setMessage("User Not Found. Login again");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                }
            } else {
                response.setMessage("Missing Token");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }

        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/favorite/createFavorite");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);
            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());

        }
        return response;
    }
}
