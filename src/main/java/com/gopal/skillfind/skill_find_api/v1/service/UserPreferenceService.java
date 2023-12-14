package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.UserPreference;
import com.gopal.skillfind.skill_find_api.repository.UserPreferenceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private LogService logService;

    public UserPreferenceService(UserRepository userRepository, UserPreferenceRepository userPreferenceRepository, LogService logService) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.logService = logService;
    }


    public Response createUserPref(UserPreference userPref, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserID(retriveUser.getId());
                    if (userPreference == null) {
                        userPref.setId(retriveUser.getId());
                        userPreferenceRepository.insert(userPref);
                        response.setMessage("Successfully Created");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Already exists");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    }

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
            log.setSource("/api/skillFind/v1/post/createPost");
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
