package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Device;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/device")
@AllArgsConstructor
public class DeviceController {
    @Autowired
    public DeviceService deviceService;

    @PostMapping("/registerDevice")
    public Response registerDevice(@RequestHeader("Authorization") String header, @RequestBody Device device) {
        return deviceService.registerDevice(header, device);
    }
}
