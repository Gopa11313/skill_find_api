package com.gopal.skillfind.skill_find_api.model.respones;

import com.gopal.skillfind.skill_find_api.utils.AccountType;
import com.gopal.skillfind.skill_find_api.utils.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifiedUser {
    private String id;
    private String name;
    private String email;
    private String phNo;
    private String bio;
    private String location;
    private List<String> skills;
    private String profilePhoto;
    private String dob;
    private List<String> workImages;
    private String startedWorkingYear;
    private String workPreference;
    private Boolean isFavorite;
}
