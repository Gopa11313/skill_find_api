package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.UserPreference;
import com.gopal.skillfind.skill_find_api.repository.UserPreferenceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gopal.skillfind.skill_find_api.utils.FolderCreation.createFolder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired

    private LogService logService;
    private final String salt = "$2a$16$5qy3niSxBtkKQCqDPcvtWONCFy4fi4ALQTRPl18amIj8x40ERc/tq";

    private UserPreferenceRepository userPreferenceRepository;

    public UserService(UserRepository userRepository, LogService logService, UserPreferenceRepository userPreferenceRepository) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.userPreferenceRepository = userPreferenceRepository;
    }


    public Response createUser(User user) {
        Response response = new Response();
        try {
            if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getPassword() != null && !user.getPassword().isEmpty() && user.getLoginType() != null && !user.getLoginType().equals("")) {
                if (Validators.isValidEmail(user.getEmail())) {
                    User retirvedUser = userRepository.findUserByEmail(user.getEmail());
                    if (retirvedUser == null) {
                        String code = UserUtils.generateRandomNumber(5);
                        String token = UserUtils.generateToken(user.getEmail() + code);
                        String ref_token = UserUtils.generateToken(token);
                        String hashedPassword = UserUtils.hashWithSalt(user.getPassword(), salt);
                        user.setToken(token);
                        user.setRefToken(ref_token);
                        user.setPassword(hashedPassword);
                        user.setCreatedTimeStamp(DateUtils.getCurrentDate());
                        User savedUser = userRepository.insert(user);

                        UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserID(savedUser.getId());
                        Boolean askQuestion = false;
                        if (userPreference == null) {
                            askQuestion = true;
                        }
                        String folderPath = "/var/www/html/storage/" + savedUser.getId();
                        try {
                            createFolder(folderPath);
                            System.out.println("Folder created successfully.");
                        } catch (Exception e) {
                            System.err.println("Error creating folder: " + e.getMessage());
                        }
                        response.setData(new String[]{savedUser.getToken(), savedUser.getRefToken(), savedUser.getLoginType().toString(), askQuestion.toString()});
                        response.setMessage("Successfully Created.");
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                        response.setSuccess(true);
                    } else {
                        response.setData(null);
                        response.setMessage("User Already Exists");
                        response.setStatusCode(StatusCode.FORBIDDEN.getCode());
                        response.setSuccess(false);
                    }
                } else {
                    response.setData(null);
                    response.setMessage("Please provide valid email");
                    response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                    response.setSuccess(false);
                }
            } else {
                response.setData(null);
                response.setMessage("Please provide all the required field");
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/user/createUser");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response Login(User user) {
        Response response = new Response();
        try {
            if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getPassword() != null && !user.getPassword().isEmpty()) {
                if (Validators.isValidEmail(user.getEmail())) {
                    User retirvedUser = userRepository.findUserByEmail(user.getEmail());
                    if (retirvedUser != null) {
                        String databasePassword = retirvedUser.getPassword();
                        String encryptPassWord = UserUtils.hashWithSalt(user.getPassword(), salt);
                        if (encryptPassWord.equals(databasePassword)) {
                            String folderPath = "/var/www/html/storage/" + retirvedUser.getId();
                            try {
                                createFolder(folderPath);
                                System.out.println("Folder created successfully.");
                            } catch (Exception e) {
                                System.err.println("Error creating folder: " + e.getMessage());
                            }
                            UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserID(retirvedUser.getId());
                            Boolean askQuestion = false;
                            if (userPreference == null) {
                                askQuestion = true;
                            }
                            response.setSuccess(true);
                            response.setData(new String[]{retirvedUser.getToken(), retirvedUser.getRefToken(), retirvedUser.getLoginType().toString(),askQuestion.toString()});
                            response.setStatusCode(StatusCode.SUCCESS.getCode());
                            response.setMessage("Success");
                        } else {
                            response.setSuccess(false);
                            response.setData(null);
                            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
                            response.setMessage("Invalid credentials");
                        }

                    } else {
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
                        response.setMessage("User doesn't exist.");
                    }
                } else {
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                    response.setMessage("Please provide valid email.");
                }
            } else {
                response.setData(null);
                response.setMessage("Please provide all the required field");
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/user/login");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response guestLogin() {
        Response response = new Response();
        try {
            String email = "";
            while (true) {
                email = UserUtils.generateUniqueGuestEmail();
                User retirvedUser = userRepository.findUserByEmail(email);
                if (retirvedUser == null) {
                    break;
                }
            }
            String databasePassword = UserUtils.generateRandomPassword();
            User user = new User();
            String code = UserUtils.generateRandomNumber(5);
            String token = UserUtils.generateToken(email + code);
            String ref_token = UserUtils.generateToken(token);
            String hashedPassword = UserUtils.hashWithSalt(databasePassword, salt);
            user.setToken(token);
            user.setEmail(email);
            user.setRefToken(ref_token);
            user.setPassword(hashedPassword);
            user.setLoginType(LoginType.GUEST);
            userRepository.insert(user);
            response.setSuccess(true);
            response.setData(new String[]{token, ref_token, LoginType.GUEST.toString()});
            response.setStatusCode(StatusCode.SUCCESS.getCode());
            response.setMessage("Success");
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/user/guestLogin");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response updatePersonalInfo(User user, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    if (user.getName() != null &&
                            !user.getName().isEmpty() && user.getPhNo() != null && !user.getPhNo().isEmpty()) {
                        if (UserUtils.isValidPhoneNumber(user.getPhNo())) {
                            retriveUser.setName(user.getName());
                            retriveUser.setDob(user.getDob());
                            retriveUser.setPhNo(user.getPhNo());
                            retriveUser.setLocation(user.getLocation());
                            if (user.getProfilePhoto() != null) {
                                String imageUrl = UserUtils.convertAndSaveImage(user.getProfilePhoto(), "/var/www/html/storage/" + retriveUser.getId() + "/", retriveUser.getId());
                                retriveUser.setProfilePhoto(imageUrl);
                            }
                            userRepository.save(retriveUser);
                            response.setMessage("Updated SuccessFully");
                            response.setSuccess(true);
                            response.setData(null);
                            response.setStatusCode(StatusCode.SUCCESS.getCode());
                        } else {
                            response.setMessage("Please enter valid number.");
                            response.setSuccess(false);
                            response.setData(null);
                            response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                        }
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
            log.setSource("/api/skillFind/v1/user/guestLogin");
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
