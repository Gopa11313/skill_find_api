package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends MongoRepository<AdminUsers, String> {
    AdminUsers findAdminUsersByEmail(String email);
    AdminUsers findAdminUsersByToken(String token);
}
