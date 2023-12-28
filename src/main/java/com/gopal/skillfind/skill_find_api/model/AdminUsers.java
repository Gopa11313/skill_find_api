package com.gopal.skillfind.skill_find_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class AdminUsers {
    @Id
    private String id;
    private String email;
    private String password;
    private String timeStamp;
    private String permission;
}
