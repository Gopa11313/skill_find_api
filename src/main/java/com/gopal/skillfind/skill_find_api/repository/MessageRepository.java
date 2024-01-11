package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
