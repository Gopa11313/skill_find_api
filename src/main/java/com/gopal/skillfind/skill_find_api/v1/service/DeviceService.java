package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Device;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.repository.DeviceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.DateUtils;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public LogService logService;

    @Autowired
    public DeviceRepository deviceRepository;

    public Response registerDevice(String header, Device device) {
        Response response = new Response();
        try {
            if (header != null || !header.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(header);
                if (retriveUser != null) {
                    if (device.deviceId != null && !device.deviceId.isEmpty() && device.deviceType != null && !device.deviceType.isEmpty()) {
                        Device dbDevice = deviceRepository.findDeviceByDeviceTypeAndUserId(device.deviceType, retriveUser.getId());
                        if (dbDevice == null) {
                            device.setUserId(retriveUser.getId());
                            device.setRegisteredTime(DateUtils.getCurrentDate());
                            deviceRepository.save(device);
                        } else {
                            dbDevice.setRegisteredTime(DateUtils.getCurrentDate());
                            deviceRepository.save(dbDevice);
                        }
                        response.setMessage("Success");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Please provide all the required data");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
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
            log.setSource("/api/skillFind/v1/device/registerDevice");
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
