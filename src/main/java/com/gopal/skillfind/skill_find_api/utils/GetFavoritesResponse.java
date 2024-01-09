package com.gopal.skillfind.skill_find_api.utils;

import com.gopal.skillfind.skill_find_api.model.Favorite;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFavoritesResponse {
    private List<Favorite> profileFavorite;
    private List<Favorite> searchFavorite;

}
