package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{ 'participants': { $elemMatch: { 'userId': ?0 } }, 'participants': { $elemMatch: { 'userId': ?1 } } }")
    Chat findByParticipantsUserIdAndParticipantsUserId(String userId1, String userId2);


    List<Chat> findAllByOrderByModifiedDateDesc(
            Pageable pageable);
}
