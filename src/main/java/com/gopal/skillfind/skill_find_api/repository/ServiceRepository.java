package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends MongoRepository<Service,String> {

    List<Service> findByIsActiveTrue();

    Service findByServiceId(String id);
}
