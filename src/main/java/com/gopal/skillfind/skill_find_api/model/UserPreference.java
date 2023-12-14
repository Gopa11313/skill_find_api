package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.PreferenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference {
    @Id
    private String id;
    private String timeStamp;
    private PreferenceType lookingFor;
    private ArrayList<String> whatKind;
    private String userID;
}
