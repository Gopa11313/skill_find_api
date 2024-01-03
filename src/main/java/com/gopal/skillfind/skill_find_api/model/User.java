package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.AccountType;
import com.gopal.skillfind.skill_find_api.utils.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String phNo;
    private String password;
    private String bio;
    private String location;
    private AccountType accountType;
    private LoginType loginType;
    private String token;
    private String refToken;
    private List<String> skills;
    private String profilePhoto;
    private String dob;
    private List<String> workImages;
    private String startedWorkingYear;
    private String workPreference;
    private String createdTimeStamp;

}
