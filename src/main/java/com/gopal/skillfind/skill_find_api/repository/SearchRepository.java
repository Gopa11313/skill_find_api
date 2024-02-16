package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Chat;
import com.gopal.skillfind.skill_find_api.model.Search;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends MongoRepository<Search, String> {
//    List<Search> findAllByUserIdAndOrderByTimeDesc(String userId,
//                                                   Pageable pageable);
}
