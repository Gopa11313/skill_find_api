package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.repository.AdminUserRepository;
import com.gopal.skillfind.skill_find_api.repository.LogRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import com.gopal.skillfind.skill_find_api.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminUserService {
    @Autowired
    public AdminUserRepository userRepository;
    @Autowired

    private LogService logService;

    private final String salt = "$2a$16$5qy3niSxBtkKQCqDPcvtWONCFy4fi4ALQTRPl18amIj8x40ERc/tq";

    public AdminUserService(AdminUserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    public Response createAdminUser(AdminUsers adminUsers) {
        Response response = new Response();
        try {
            if (adminUsers.getEmail() != null && adminUsers.getPassword() != null && adminUsers.getPermission() != null) {
                AdminUsers dbAdminUser = userRepository.findAdminUsersByEmail(adminUsers.getEmail());
                if (dbAdminUser == null) {
                    adminUsers.setTimeStamp(DateUtils.getCurrentDate());
                    String hashedPassword = UserUtils.hashWithSalt(adminUsers.getPassword(), salt);
                    String code = UserUtils.generateRandomNumber(5);
                    String token = UserUtils.generateToken(adminUsers.getEmail() + code);
                    adminUsers.setPassword(hashedPassword);
                    adminUsers.setToken(token);
                    userRepository.save(adminUsers);
                    response.setMessage("Successfully Created");
                    response.setSuccess(true);
                    response.setData(null);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
                } else {
                    response.setMessage("User Already Exists");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                }

            } else {
                response.setMessage("Please provide all the required data");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/adminUser/createAdminUser");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response adminLogin(AdminUsers adminUsers) {
        Response response = new Response();
        try {
            if (adminUsers.getEmail() != null && adminUsers.getPassword() != null) {
                AdminUsers adminUsers1 = userRepository.findAdminUsersByEmail(adminUsers.getEmail());
                if (adminUsers1 != null) {
                    String hashedPassword = UserUtils.hashWithSalt(adminUsers.getPassword(), salt);
                    if (Objects.equals(adminUsers1.getPassword(), hashedPassword)) {
                        response.setMessage("Success");
                        response.setSuccess(true);
                        response.setData(adminUsers1.getToken());
                        response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                    } else {
                        response.setMessage("Invalid credentials");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                    }

                } else {
                    response.setMessage("Admin doesn't exists");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                }
            } else {
                response.setMessage("Please provide all the required data");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/adminUser/createAdminUser");
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
