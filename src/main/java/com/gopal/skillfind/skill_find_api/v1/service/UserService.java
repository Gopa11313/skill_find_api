package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.model.UserPreference;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedUser;
import com.gopal.skillfind.skill_find_api.model.respones.UserInfo;
import com.gopal.skillfind.skill_find_api.repository.FavoriteRepository;
import com.gopal.skillfind.skill_find_api.repository.ServiceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserPreferenceRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gopal.skillfind.skill_find_api.utils.FolderCreation.createFolder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogService logService;

    @Autowired
    private ServiceRepository serviceRepository;
    private final String salt = "$2a$16$5qy3niSxBtkKQCqDPcvtWONCFy4fi4ALQTRPl18amIj8x40ERc/tq";

    private UserPreferenceRepository userPreferenceRepository;
    private FavoriteRepository favoriteRepository;

    public UserService(UserRepository userRepository, LogService logService, UserPreferenceRepository userPreferenceRepository, ServiceRepository serviceRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.userPreferenceRepository = userPreferenceRepository;
        this.serviceRepository = serviceRepository;
        this.favoriteRepository = favoriteRepository;
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
                        user.setAccountType(AccountType.CLIENT);
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
                        response.setData(new String[]{savedUser.getToken(), savedUser.getRefToken(), savedUser.getLoginType().toString(), askQuestion.toString(), savedUser.getId()});
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
                            response.setData(new String[]{retirvedUser.getToken(), retirvedUser.getRefToken(), retirvedUser.getLoginType().toString(), askQuestion.toString(), retirvedUser.getId()});
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
            user.setAccountType(AccountType.GUEST);
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
//                        if (UserUtils.isValidPhoneNumber(user.getPhNo())) {
                        retriveUser.setName(user.getName());
                        retriveUser.setDob(user.getDob());
                        retriveUser.setPhNo(user.getPhNo());
                        retriveUser.setLocation(user.getLocation());
                        retriveUser.setBio(user.getBio());
                        retriveUser.setAccountType(AccountType.SUPERUSER);
                        if (user.getProfilePhoto() != null) {
                            if (retriveUser.getProfilePhoto() != null) {
                                UserUtils.deleteImage(retriveUser.getProfilePhoto());
                            }
                            String imageUrl = UserUtils.convertAndSaveImage(user.getProfilePhoto(), "/var/www/html/storage/" + retriveUser.getId() + "/", retriveUser.getId());
                            retriveUser.setProfilePhoto(imageUrl);
                        }
                        if (user.getWorkImages().size() > 0) {
                            if (retriveUser.getWorkImages() != null && retriveUser.getWorkImages().size() > 0) {
                                for (String path : user.getWorkImages()) {
                                    UserUtils.deleteImage(path);
                                }
                            }
                            List<String> savedImages = new ArrayList<>();
                            for (int i = 0; i < user.getWorkImages().size(); i++) {
                                String imageUrl = UserUtils.convertAndSaveImage(user.getWorkImages().get(i), "/var/www/html/storage/" + retriveUser.getId() + "/", retriveUser.getId());
                                savedImages.add(imageUrl);
                            }
                            retriveUser.setWorkImages(savedImages);

                        }
                        userRepository.save(retriveUser);
                        response.setMessage("Updated SuccessFully");
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
            log.setSource("/api/skillFind/v1/user/updatePersonalInfo");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;

    }

    public Response getUserInfo(String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    retriveUser.setId("");
                    retriveUser.setToken("");
                    retriveUser.setRefToken("");
                    retriveUser.setPassword("");
                    retriveUser.setCreatedTimeStamp("");

                    UserInfo userInfo = new UserInfo();
                    userInfo.setName(retriveUser.getName());
                    userInfo.setEmail(retriveUser.getEmail());
                    userInfo.setPhNo(retriveUser.getPhNo());
                    userInfo.setLocation(retriveUser.getLocation());
                    userInfo.setAccountType(retriveUser.getAccountType());
                    userInfo.setLoginType(retriveUser.getLoginType());
                    userInfo.setBio(retriveUser.getBio());
                    userInfo.setDob(retriveUser.getDob());
                    userInfo.setProfilePhoto(retriveUser.getProfilePhoto());
                    userInfo.setStartedWorkingYear(retriveUser.getStartedWorkingYear());
                    userInfo.setWorkImages(retriveUser.getWorkImages());
                    userInfo.setWorkPreference(retriveUser.getWorkPreference());
                    List<com.gopal.skillfind.skill_find_api.model.Service> serviceList = new ArrayList<>();
                    if (retriveUser.getSkills() != null && retriveUser.getSkills().size() > 0) {
                        for (String id : retriveUser.getSkills()) {
                            Optional<com.gopal.skillfind.skill_find_api.model.Service> service = serviceRepository.findById(id);
                            if (service.isPresent()) {
                                serviceList.add(service.get());
                            }
                        }
                    }
                    userInfo.setSkills(serviceList);

                    response.setMessage("Data");
                    response.setSuccess(true);
                    response.setData(userInfo);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
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
            log.setSource("/api/skillFind/v1/user/getUserInfo");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response getFeaturedProfile(String authorizationHeader, String context) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    Pageable pageable = PageRequest.of(0, 10);
                    if (context == "Home") {
                        pageable = PageRequest.of(0, 10);
                    } else {
                        pageable = PageRequest.of(0, 20);
                    }
                    Page<User> usersPage = userRepository.findByAccountType("SUPERUSER", pageable);
                    List<UserInfo> modifiedUsers = new ArrayList<>();
                    if (usersPage.getContent().size() > 0) {
                        for (User user : usersPage.getContent()) {
                            Favorite favorite = favoriteRepository.findFavoriteByUserIdAndContentId(retriveUser.getId(), user.getId());
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId(user.getId());
                            userInfo.setName(user.getName());
                            userInfo.setEmail(user.getEmail());
                            userInfo.setPhNo(user.getPhNo());
                            userInfo.setLocation(user.getLocation());
                            userInfo.setAccountType(user.getAccountType());
                            userInfo.setLoginType(user.getLoginType());
                            userInfo.setDob(user.getDob());
                            userInfo.setBio(user.getBio());
                            userInfo.setProfilePhoto(user.getProfilePhoto());
                            userInfo.setStartedWorkingYear(user.getStartedWorkingYear());
                            userInfo.setWorkImages(user.getWorkImages());
                            userInfo.setWorkPreference(user.getWorkPreference());
                            userInfo.setIsFavorite(favorite != null);
                            List<com.gopal.skillfind.skill_find_api.model.Service> serviceList = new ArrayList<>();
                            if (user.getSkills() != null && user.getSkills().size() > 0) {
                                for (String id : user.getSkills()) {
                                    Optional<com.gopal.skillfind.skill_find_api.model.Service> service = serviceRepository.findById(id);
                                    if (service.isPresent()) {
                                        serviceList.add(service.get());
                                    }
                                }
                            }
                            userInfo.setSkills(serviceList);
//                            ModifiedUser modifiedUser = new ModifiedUser(user.getId(), user.getName(), user.getEmail(), user.getPhNo(), user.getBio(), user.getLocation(), user.getSkills(), user.getProfilePhoto(), user.getDob(), user.getWorkImages(), user.getStartedWorkingYear(), user.getWorkPreference(), favorite != null);
                            modifiedUsers.add(userInfo);
                        }
                    }
                    response.setMessage("Data");
                    response.setSuccess(true);
                    response.setData(modifiedUsers);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
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
            log.setSource("/api/skillFind/v1/user/getUserInfo");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response getUserSkills(String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    List<com.gopal.skillfind.skill_find_api.model.Service> serviceList = new ArrayList<>();
                    if (retriveUser.getSkills() != null && retriveUser.getSkills().size() > 0) {
                        for (String skill : retriveUser.getSkills()) {
                            Optional<com.gopal.skillfind.skill_find_api.model.Service> service = serviceRepository.findById(skill);
                            service.ifPresent(serviceList::add);
                        }

                    }
                    response.setMessage("SuccessFul");
                    response.setSuccess(true);
                    response.setData(serviceList);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
                } else {
                    response.setMessage("User doesn't exists.");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
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
            log.setSource("/api/skillFind/v1/user/getUserSkills");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response updateUserSkills(String authorizationHeader, SkillBody skillBody) {
        Response response = new Response();
        try {
            if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    if (skillBody.ids.size() > 0) {
                        for (String i : skillBody.ids) {
                            Optional<com.gopal.skillfind.skill_find_api.model.Service> service = serviceRepository.findById(i);
                            if (!service.isPresent()) {
                                skillBody.ids.remove(i);
                            }
                        }
                        retriveUser.setSkills(skillBody.ids);
                        userRepository.save(retriveUser);
                        response.setMessage("Success");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Please send at least one skills");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
                    }
                } else {
                    response.setMessage("User doesn't exists.");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.NOT_FOUND.getCode());
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
            log.setSource("/api/skillFind/v1/user/updateUserSkills");
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
