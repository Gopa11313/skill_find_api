package com.gopal.skillfind.skill_find_api.v1.service;

import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.repository.PostRepository;
import com.gopal.skillfind.skill_find_api.repository.UserRepository;
import com.gopal.skillfind.skill_find_api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public PostService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Response createPost(Post post, String authorizationHeader) {
        Response response = new Response();
        if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
            User retriveUser = userRepository.findUserByToken(authorizationHeader);
            if (retriveUser != null) {
                if (post.getImages() != null) {
                    if (post.getImages().size() > 0) {
                        List<String> imageList = new ArrayList<>();
                        for (int i = 0; i < post.getImages().size(); i++) {
                            if (post.getImages().get(i) != null && !post.getImages().get(i).isEmpty()) {
                                String imageUrl = UserUtils.convertAndSaveImage(post.getImages().get(i), "/storage/" + retriveUser.getId() + "/", retriveUser.getId());
                                imageList.add(imageUrl);
                            }
                        }
                        post.setImages(imageList);
                    }
                }
                if (post.getType() == PostType.JOB) {
                    post.setPostContent(null);

                }
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
        return response;
    }

    public Response editPost(Post post, String authorizationHeader) {
        Response response = new Response();
        if (authorizationHeader != null || !authorizationHeader.isEmpty()) {
            User retriveUser = userRepository.findUserByToken(authorizationHeader);
            if (retriveUser != null) {
                System.out.println(post.getId()+","+retriveUser.getId());
                Post retrivePost  = postRepository.findPostByIdAndUserId(post.getId(),retriveUser.getId());
                if(retrivePost!=null){
                    if(post.getType().equals(PostType.OTHER) || post.getType().equals(PostType.THOUGHTS) || post.getType().equals(PostType.POST)){
                        retrivePost.setPostContent(post.getPostContent());

                    }
                    else if(post.getType() == PostType.JOB){
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
                }else{
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
        return response;
    }
}
