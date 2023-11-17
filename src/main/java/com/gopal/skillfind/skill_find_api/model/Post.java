package com.gopal.skillfind.skill_find_api.model;

import com.gopal.skillfind.skill_find_api.utils.Coordinates;
import com.gopal.skillfind.skill_find_api.utils.JobType;
import com.gopal.skillfind.skill_find_api.utils.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private List<String> images;

    private String jobTitle;
    private String jobDescription;
    private JobType jobtype;
    private String perHour;
    private String expReq;
    private String note;
    private PostType type;
    private String location;
    private Coordinates coordinates;
}
