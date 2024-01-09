package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.repository.FavoriteRepository;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.FavoriteService;
import com.gopal.skillfind.skill_find_api.v1.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/favorite")
@AllArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/createFavorite")
    private Response createFavorite(@RequestBody Favorite favorite, @RequestHeader("Authorization") String authorizationHeader) {
        return favoriteService.createFavorite(favorite, authorizationHeader);
    }

    @GetMapping("/getFavorites")
    private Response getFavorites(@RequestHeader("Authorization") String authorizationHeader) {
        return favoriteService.getFavorites(authorizationHeader);
    }

    @DeleteMapping("/deleteFavorite/{id}")
    private Response deleteFavorite(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        return favoriteService.deleteFavorite(authorizationHeader, id);
    }

}
