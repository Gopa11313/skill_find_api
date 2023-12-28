package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminUserRepository extends MongoRepository<AdminUsers, String> {
    AdminUsers findAdminUsersByEmail(String email);
    AdminUsers findAdminUsersByToken(String token);
}
