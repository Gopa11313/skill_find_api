package com.gopal.skillfind.skill_find_api.model.respones;

import com.gopal.skillfind.skill_find_api.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatProfileResponse {
    private String profileId;
    private String name;
    private String email;
    private String profileImage;
    private List<Message> messageList;
}
