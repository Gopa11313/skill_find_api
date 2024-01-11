package com.gopal.skillfind.skill_find_api.model.respones;

import com.gopal.skillfind.skill_find_api.utils.ChatType;
import com.gopal.skillfind.skill_find_api.utils.Participants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifiedChat {
    private String id;
    private List<Participants> participants;
    private ChatType type;
    private String message;
}
