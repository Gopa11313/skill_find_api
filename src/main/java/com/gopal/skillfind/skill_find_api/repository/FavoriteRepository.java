package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import com.gopal.skillfind.skill_find_api.utils.FavoriteType;
import com.gopal.skillfind.skill_find_api.utils.Response;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    List<Favorite> findAllByUserId(String userId, Sort sort);

    Favorite findFavoriteByContentIdAndUserId(String contentId, String userId);

    Favorite findFavoriteByUserIdAndContentId(String userId, String contentId);
}
