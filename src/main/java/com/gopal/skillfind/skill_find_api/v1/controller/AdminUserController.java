package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skillFind/adminUser")
@AllArgsConstructor
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/createAdminUser")
    private Response createAdmin(@RequestBody AdminUsers adminUsers) {
        return adminUserService.createAdminUser(adminUsers);
    }

    @PostMapping("/adminLogin")
    private Response adminLogin(@RequestBody AdminUsers adminUsers) {
        return adminUserService.adminLogin(adminUsers);
    }
}
