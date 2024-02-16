package com.gopal.skillfind.skill_find_api.model.respones;

import com.gopal.skillfind.skill_find_api.model.Post;
import com.gopal.skillfind.skill_find_api.model.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<Service> search;
    private List<Post> posts;
}
