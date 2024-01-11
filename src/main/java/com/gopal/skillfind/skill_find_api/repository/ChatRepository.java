package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{ 'participants': { $all: [ { 'userId': ?0 }, { 'userId': ?1 } ] } }")
    Chat findByParticipantsUserIdAndParticipantsUserId(String userId1, String userId2);
}
