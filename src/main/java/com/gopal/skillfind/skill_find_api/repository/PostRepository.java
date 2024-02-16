package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Post findPostByIdAndUserId(String id, String userId);


    List<Post> findAllByType(String type, Sort sort);

    List<Post> findAll(Sort sort);

    List<Post> findAllByUserId(String userId, Sort sort);


    @Query("{$or: [{'jobTitle': {$regex: ?0, $options: 'i'}}, {'jobDescription': {$regex: ?0, $options: 'i'}}], 'isActive': true, 'type': 'JOB'}")
    List<Post> findByTitleOrDescriptionRegex(String keyword);
}
