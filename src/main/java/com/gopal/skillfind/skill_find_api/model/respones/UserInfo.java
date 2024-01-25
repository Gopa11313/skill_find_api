package com.gopal.skillfind.skill_find_api.model.respones;

import com.gopal.skillfind.skill_find_api.model.Service;
import com.gopal.skillfind.skill_find_api.utils.AccountType;
import com.gopal.skillfind.skill_find_api.utils.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String id;
    private String name;
    private String email;
    private String phNo;
    private String bio;
    private String location;
    private List<Service> skills;
    private String profilePhoto;
    private String dob;
    private List<String> workImages;
    private String startedWorkingYear;
    private String workPreference;
    private AccountType accountType;
    private LoginType loginType;
    private Boolean isFavorite;
}
