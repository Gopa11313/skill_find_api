package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.repository.AdminUserRepository;
import com.gopal.skillfind.skill_find_api.repository.ServiceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import com.gopal.skillfind.skill_find_api.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {
    @Autowired
    public LogService logService;

    @Autowired
    public ServiceRepository serviceRepository;

    @Autowired
    public AdminUserRepository adminUserRepository;

    @Autowired
    public UserRepository userRepository;

    public ServicesService(LogService logService, ServiceRepository serviceRepository, AdminUserRepository adminUserRepository, UserRepository userRepository) {
        this.logService = logService;
        this.serviceRepository = serviceRepository;
        this.adminUserRepository = adminUserRepository;
        this.userRepository = userRepository;
    }

    public Response createService(com.gopal.skillfind.skill_find_api.model.Service service, String header) {
        Response response = new Response();
        try {
            if (service.getName() != null && service.getImage() != null) {
                AdminUsers dbAdmin = adminUserRepository.findAdminUsersByToken(header);
                if (dbAdmin != null) {
                    String imageUrl = UserUtils.convertAndSaveImage(service.getImage(), "/var/www/html/storage/" + "admin" + "/", service.getName());
                    service.setImage(imageUrl);
                    serviceRepository.save(service);
                    response.setMessage("Success");
                    response.setSuccess(true);
                    response.setData(imageUrl);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                } else {
                    response.setMessage("Please login again");
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
            response.setData(e.getMessage());
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response getServices(String header) {
        Response response = new Response();
        try {
            if (header != null && !header.isEmpty()) {
                AdminUsers dbUser = adminUserRepository.findAdminUsersByToken(header);
                if (dbUser != null) {
                    if (Integer.parseInt(dbUser.getPermission()) >= 2) {
                        List<com.gopal.skillfind.skill_find_api.model.Service> serviceList = serviceRepository.findByIsActiveTrue();
                        response.setMessage("Success");
                        response.setSuccess(true);
                        response.setData(serviceList);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Admin doesn't have permission");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                    }
                } else {
                    response.setMessage("Login again");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                }
            } else {
                response.setMessage("Missing token");
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
            response.setData(e.getMessage());
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }


    public Response deleteService(com.gopal.skillfind.skill_find_api.model.Service service, String header) {
        Response response = new Response();
        try {
            if (header != null && !header.isEmpty()) {
                AdminUsers dbUser = adminUserRepository.findAdminUsersByToken(header);
                if (dbUser != null) {
                    if (Integer.parseInt(dbUser.getPermission()) >= 2) {
                        Optional<com.gopal.skillfind.skill_find_api.model.Service> dbService = serviceRepository.findById(service.getId());
                        if (dbService.isPresent()) {
                            serviceRepository.delete(dbService.get());
                            response.setMessage("Success");
                            response.setSuccess(true);
                            response.setData(null);
                            response.setStatusCode(StatusCode.SUCCESS.getCode());
                        } else {
                            response.setMessage("Service not found");
                            response.setSuccess(false);
                            response.setData(null);
                            response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                        }
                    } else {
                        response.setMessage("Admin doesn't have permission");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                    }
                } else {
                    response.setMessage("Login again");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                }
            } else {
                response.setMessage("Missing token");
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
            response.setData(e.getMessage());
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }
}

