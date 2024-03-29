package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.Search;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.respones.SearchResponse;
import com.gopal.skillfind.skill_find_api.repository.*;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PostRepository postRepository;


    public Response getRecentSearches(String header) {
        Response response = new Response();
        try {
            if (header != null && !header.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(header);
                if (retriveUser != null) {
                    Pageable pageable = PageRequest.of(0, 10);
//                    List<Search> savedSearches = searchRepository.findAllByUserIdAndOrderByTimeDesc(retriveUser.getId(), pageable);
//                    response.setMessage("Success");
//                    response.setSuccess(true);
//                    response.setData(savedSearches);
//                    response.setStatusCode(StatusCode.SUCCESS.getCode());
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
            log.setSource("/api/skillFind/v1/search/getRecentSearch");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;

    }

    public Response createSearch(String header, Search search) {
        Response response = new Response();
        try {
            if (header != null && !header.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(header);
                if (retriveUser != null) {
                    List<com.gopal.skillfind.skill_find_api.model.Service> listOfService = serviceRepository.findByNameRegexAndIsActiveTrue(search.getSearch());
                    List<Post> postList = postRepository.findByTitleOrDescriptionRegex(search.getSearch());
                    SearchResponse searchResponse = new SearchResponse();
                    searchResponse.setSearch(listOfService);
                    searchResponse.setPosts(postList);
////                    search.setUserId(retriveUser.getId());
//
                    response.setMessage("Success");
                    response.setSuccess(true);
                    response.setData(searchResponse);
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
            log.setSource("/api/skillFind/v1/search/createSearch");
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
