package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.User;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/post")
@AllArgsConstructor
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/createPost")
    private Response createPost(@RequestBody Post post, @RequestHeader("Authorization") String authorizationHeader) {
        return postService.createPost(post, authorizationHeader);
    }

    @PostMapping("/editPost")
    private Response editPost(@RequestBody Post post, @RequestHeader("Authorization") String authorizationHeader) {
        return postService.editPost(post, authorizationHeader);
    }

    @PostMapping("/getPost")
    private Response getPosts(@RequestBody Post post) {
        return postService.getPost(post);
    }
}
