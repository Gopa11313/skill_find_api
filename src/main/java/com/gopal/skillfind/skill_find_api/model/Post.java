package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.Coordinates;
import com.gopal.skillfind.skill_find_api.utils.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String id;
    private String userId;
    private String postContent;
    private String createdDate;
    private String location;
    private Coordinates coordinates;
    private String[] images;
    private PostType type;
}
