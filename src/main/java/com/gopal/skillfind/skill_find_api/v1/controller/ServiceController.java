package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Service;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.ServicesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/service")
@AllArgsConstructor
public class ServiceController {

    @Autowired
    public ServicesService servicesService;

    @PostMapping("/createService")
    public Response createService(@RequestBody Service service, @RequestHeader("Authorization") String header) {
        return servicesService.createService(service, header);

    }

    @PostMapping("/getServices")
    public Response getServices() {
        return servicesService.getServices();
    }
    @PostMapping("/deleteService")
    public Response deleteService (@RequestBody Service service,@RequestHeader("Authorization") String header) {
    return servicesService.deleteService(service, header);
    }
}
