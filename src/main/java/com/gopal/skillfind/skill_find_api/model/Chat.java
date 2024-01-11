package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.ChatType;
import com.gopal.skillfind.skill_find_api.utils.Participants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    private String id;
    private List<Participants> participants;
    private ChatType type;
}
