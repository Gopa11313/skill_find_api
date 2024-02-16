package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends MongoRepository<Service,String> {

    List<Service> findByIsActiveTrue();

    Optional<Service> findById(String id);

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'isActive': true}")
    List<Service> findByNameRegexAndIsActiveTrue(String keyword);

}
