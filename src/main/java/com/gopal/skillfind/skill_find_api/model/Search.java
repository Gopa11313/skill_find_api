package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.PriceRange;
import com.gopal.skillfind.skill_find_api.utils.SearchingFor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Search {
    @Id
    private String id;
    private String search;
    private String time;
    private String userId;
    private String yearsOfExp;
    private PriceRange priceRance;
    private String distance;
    private SearchingFor searchingFor;
}
