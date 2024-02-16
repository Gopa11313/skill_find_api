package com.gopal.skillfind.skill_find_api.v1.controller;

import com.gopal.skillfind.skill_find_api.model.Search;
import com.gopal.skillfind.skill_find_api.utils.Response;
import com.gopal.skillfind.skill_find_api.v1.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skillFind/v1/search")
@AllArgsConstructor
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/getRecentSearch")
    public Response getRecentSearches(@RequestHeader("Authorization") String header) {
        return searchService.getRecentSearches(header);
    }

    @PostMapping("/createSearch")
    public Response createSearch(@RequestHeader("Authorization") String header,@RequestBody  Search search) {
        return searchService.createSearch(header,search);
    }
}
