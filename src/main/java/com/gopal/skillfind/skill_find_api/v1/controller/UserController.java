package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/createUser")
    private Response createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    private Response Login(@RequestBody User user) {
        return userService.Login(user);
    }

    @PostMapping("/guestLogin")
    private Response GuestLogin() {
        return userService.guestLogin();
    }

    @PostMapping("/updatePersonalInfo")
    private Response UpdatePersonalInfo(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader) {
        return userService.updatePersonalInfo(user, authorizationHeader);
    }

    @PostMapping("/getUserInfo")
    private Response getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.getUserInfo(authorizationHeader);
    }

    @GetMapping("/getFeaturedProfile/{for}")
    private Response getFeaturedProfile(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("for") String context) {
        return userService.getFeaturedProfile(authorizationHeader, context);
    }

    @GetMapping("/getUserSkills")
    private Response getUSerSkills(@RequestHeader("Authorization") String authorizationHeader){
        return userService.getUserSkills(authorizationHeader);
    }
}
