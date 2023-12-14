package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.UserPreference;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.UserPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/userPref")
@AllArgsConstructor
public class UserPreferenceController {

    private UserPreferenceService userPreferenceService;

    @PostMapping("/createPref")
    private Response createPref(@RequestBody UserPreference userPref, @RequestHeader("Authorization") String authorizationHeader) {
        return userPreferenceService.createUserPref(userPref, authorizationHeader);
    }

}
