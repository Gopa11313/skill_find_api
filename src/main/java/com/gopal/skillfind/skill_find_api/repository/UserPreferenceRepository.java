package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.UserPreference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends MongoRepository<UserPreference,String> {
    UserPreference findUserPreferenceByUserID(String userID);
}
