package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.repository.LogRepository;
import com.gopal.skillfind.skill_find_api.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void createLog(Log log) {
        logRepository.insert(log);

    }
}
