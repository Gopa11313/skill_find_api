package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import com.gopal.skillfind.skill_find_api.utils.FavoriteType;
import com.gopal.skillfind.skill_find_api.utils.Response;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    List<Favorite> findAllByUserId(String userId, Sort sort);
}
