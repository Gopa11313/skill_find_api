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
public class Device {
    @Id
    private String id;
    public String deviceId;
    public String fcmToken;
    public String deviceType;
    public String lat;
    public String lang;
    public String userId;
    public String registeredTime;
}
