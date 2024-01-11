package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.Participants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;
    private String chatId;
    private Participants sender;
    private Participants receiver;
    private String content;
    private String timestamp;
}
