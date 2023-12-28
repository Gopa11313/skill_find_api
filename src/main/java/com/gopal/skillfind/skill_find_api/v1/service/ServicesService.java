package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.AdminUsers;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.repository.AdminUserRepository;
import com.gopal.skillfind.skill_find_api.repository.ServiceRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import com.gopal.skillfind.skill_find_api.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesService {
    @Autowired
    public LogService logService;

    @Autowired
    public ServiceRepository serviceRepository;

    @Autowired
    public AdminUserRepository userRepository;

    public ServicesService(LogService logService, ServiceRepository serviceRepository, AdminUserRepository userRepository) {
        this.logService = logService;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public Response createService(com.gopal.skillfind.skill_find_api.model.Service service, String header) {
        Response response = new Response();
        try {
            if (service.getName() != null && service.getImage() != null) {
                AdminUsers dbAdmin = userRepository.findAdminUsersByToken(header);
                if (dbAdmin != null) {
                    String imageUrl = UserUtils.convertAndSaveImage(service.getImage(), "/var/www/html/storage/" + "admin" + "/", service.getName());
                    service.setName(imageUrl);
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

//            if(service.get)
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
