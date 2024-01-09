package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.repository.FavoriteRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.GetFavoritesResponse;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FavoriteService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired

    private LogService logService;

    public FavoriteService(UserRepository userRepository, FavoriteRepository favoriteRepository, LogService logService) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.logService = logService;
    }

    public Response createFavorite(Favorite favorite, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    favorite.setUserId(retriveUser.getId());
                    favorite.setCreateDate(DateUtils.getCurrentDate());

                    favoriteRepository.save(favorite);
                    response.setMessage("Success");
                    response.setSuccess(true);
                    response.setData(null);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
                } else {
                    response.setMessage("UnAuthorized User");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                }
            } else {
                response.setMessage("Missing Token");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/favorite/createFavorite");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);
            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response getFavorites(String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    List<Favorite> listOfFavorites = favoriteRepository.findAllByUserId(retriveUser.getId(), Sort.by(Sort.Order.desc("createdDate")));
                    List<Favorite> profileFavorites = new ArrayList<>();
                    List<Favorite> searchFavorites = new ArrayList<>();
                    if (listOfFavorites.size() > 0) {
                        for (Favorite favorite : listOfFavorites) {
                            if (Objects.equals(favorite.getType().toString(), "USER")) {
                                profileFavorites.add(favorite);
                            } else if (Objects.equals(favorite.getType().toString(), "SEARCH")) {
                                searchFavorites.add(favorite);
                            }
                        }
                    }
                    GetFavoritesResponse getFavoritesResponse = new GetFavoritesResponse();
                    getFavoritesResponse.setProfileFavorite(profileFavorites);
                    getFavoritesResponse.setSearchFavorite(searchFavorites);
                    response.setMessage("Success");
                    response.setSuccess(true);
                    response.setData(getFavoritesResponse);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
                } else {
                    response.setMessage("UnAuthorized User");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                }
            } else {
                response.setMessage("Missing Token");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/favorite/getFavorites");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);
            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }
}
