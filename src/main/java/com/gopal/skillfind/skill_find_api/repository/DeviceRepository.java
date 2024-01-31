package com.gopal.skillfind.skill_find_api.repository;

import com.gopal.skillfind.skill_find_api.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    Device findDeviceByDeviceTypeAndUserId(String deviceType, String userId);

}
