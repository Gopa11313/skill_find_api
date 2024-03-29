package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Log;
import com.gopal.skillfind.skill_find_api.model.respones.ModifiedPost;
import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.repository.PostRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired

    private LogService logService;

    public PostService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }


    public Response createPost(Post post, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    if (post.getImages() != null) {
                        if (post.getImages().size() > 0) {
                            List<String> imageList = new ArrayList<>();
                            for (int i = 0; i < post.getImages().size(); i++) {
                                if (post.getImages().get(i) != null && !post.getImages().get(i).isEmpty()) {
                                    String imageUrl = UserUtils.convertAndSaveImage(post.getImages().get(i), "/var/www/html/storage/" + retriveUser.getId() + "/", retriveUser.getId());
                                    imageList.add(imageUrl);
                                }
                            }
                            post.setImages(imageList);
                        }
                    }
                    if (post.getType() == PostType.JOB) {
                        post.setPostContent(null);
                    }

//                    Coordinates coordinates = new Coordinates();
//                    System.out.println(post.getCoordinates().latitude + " , " + post.getCoordinates().longitude);
                    post.setUserId(retriveUser.getId());
                    post.setCreatedDate(DateUtils.getCurrentDate());
                    postRepository.save(post);
                    response.setMessage("Successfully Created");
                    response.setSuccess(true);
                    response.setData(null);
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
            log.setSource("/api/skillFind/v1/post/createPost");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response editPost(Post post, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    System.out.println(post.getId() + "," + retriveUser.getId());
                    Post retrivePost = postRepository.findPostByIdAndUserId(post.getId(), retriveUser.getId());
                    if (retrivePost != null) {
                        if (post.getType().equals(PostType.OTHER) || post.getType().equals(PostType.THOUGHTS) || post.getType().equals(PostType.POST)) {
                            retrivePost.setPostContent(post.getPostContent());

                        } else if (post.getType() == PostType.JOB) {
                            retrivePost.setJobTitle(post.getJobTitle());
                            retrivePost.setJobDescription(post.getJobDescription());
                            retrivePost.setJobtype(post.getJobtype());
                            retrivePost.setPerHour(post.getPerHour());
                            retrivePost.setExpReq(post.getExpReq());
                            retrivePost.setNote(post.getNote());
                        }
                        retrivePost.setLocation(post.getLocation());
                        postRepository.save(retrivePost);
                        response.setMessage("SuccessFully Updated");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Post Not Found");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.NOT_FOUND.getCode());
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
            log.setSource("/api/skillFind/v1/post/editPost");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }


    public Response getPost(Post post) {
        Response response = new Response();
        try {
            List<Post> postList = new ArrayList<>();
            List<ModifiedPost> modifiedPostList = new ArrayList<>();
            if (post.getType() == null || post.getType().equals("")) {
                postList = postRepository.findAll(Sort.by(Sort.Order.desc("createdDate")));
            } else {
                postList = postRepository.findAllByType(post.getType().toString(), Sort.by(Sort.Order.desc("createdDate")));
            }
            if (postList.size() > 0) {
                for (Post itemPost : postList) {
                    User dbUser = userRepository.findUserById(itemPost.getUserId());
                    modifiedPostList.add(new ModifiedPost(itemPost.getId(), itemPost.getUserId(), itemPost.getPostContent(), itemPost.getCreatedDate(), itemPost.getImages(), itemPost.getJobTitle(), itemPost.getJobDescription(), itemPost.getJobtype(), itemPost.getPerHour(), itemPost.getExpReq(), itemPost.getNote(), itemPost.getType(), itemPost.getLocation(), itemPost.getCoordinates(), (dbUser != null) ? dbUser.getName() : "", (dbUser != null) ? dbUser.getProfilePhoto() : "", (dbUser != null) ? dbUser.getEmail() : ""));
                }
            }
            response.setMessage("Success");
            response.setSuccess(true);
            response.setData(modifiedPostList);
            response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/post/getPost");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response deletePost(Post post, String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty() && post.getId() != null && !post.getId().isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    Post dbPost = postRepository.findPostByIdAndUserId(post.getId(), retriveUser.getId());
                    if (dbPost != null) {
                        postRepository.delete(dbPost);
                        response.setMessage("Deleted Successfully!");
                        response.setSuccess(true);
                        response.setData(null);
                        response.setStatusCode(StatusCode.SUCCESS.getCode());
                    } else {
                        response.setMessage("Post not found.");
                        response.setSuccess(false);
                        response.setData(null);
                        response.setStatusCode(StatusCode.NOT_FOUND.getCode());
                    }
                } else {
                    response.setMessage("Please login again");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                }
            } else {
                response.setMessage("Please provide all the required field.");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/post/deletePost");
            log.setTimeStamp(DateUtils.getCurrentDate());
            logService.createLog(log);

            response.setMessage("internal server error");
            response.setSuccess(false);
            response.setData(null);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
        }
        return response;
    }

    public Response getUserPosts(String authorizationHeader) {
        Response response = new Response();
        try {
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                User retriveUser = userRepository.findUserByToken(authorizationHeader);
                if (retriveUser != null) {
                    List<Post> postList = postRepository.findAllByUserId(retriveUser.getId(), Sort.by(Sort.Order.desc("createdDate")));
                    List<ModifiedPost> modifiedPostList = new ArrayList<>();
                    if (postList.size() > 0) {
                        for (Post itemPost : postList) {
                            User dbUser = userRepository.findUserById(itemPost.getUserId());
                            ModifiedPost newModifiedPost = new ModifiedPost(itemPost.getId(), itemPost.getUserId(), itemPost.getPostContent(), itemPost.getCreatedDate(), itemPost.getImages(), itemPost.getJobTitle(), itemPost.getJobDescription(), itemPost.getJobtype(), itemPost.getPerHour(), itemPost.getExpReq(), itemPost.getNote(), itemPost.getType(), itemPost.getLocation(), itemPost.getCoordinates(), (dbUser != null) ? dbUser.getName() : "", (dbUser != null) ? dbUser.getProfilePhoto() : "", (dbUser != null) ? dbUser.getEmail() : "");
                            modifiedPostList.add(newModifiedPost);
                        }
                    }
                    response.setMessage("Success");
                    response.setSuccess(true);
                    response.setData(modifiedPostList);
                    response.setStatusCode(StatusCode.SUCCESS.getCode());
                } else {
                    response.setMessage("Please login again");
                    response.setSuccess(false);
                    response.setData(null);
                    response.setStatusCode(StatusCode.UNAUTHORIZED.getCode());
                }

            } else {
                response.setMessage("Please provide all the required field.");
                response.setSuccess(false);
                response.setData(null);
                response.setStatusCode(StatusCode.BAD_REQUEST.getCode());
            }
        } catch (Exception e) {
            Log log = new Log();
            log.setError(e.getMessage());
            log.setSource("/api/skillFind/v1/post/getUserPosts");
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
