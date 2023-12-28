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
public class Service {
    @Id
    private String id;
    private String name;
    private String image;
    private Boolean isActive;
}
