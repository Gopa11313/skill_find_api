package com.gopal.skillfind.skill_find_api.utils;

import lombok.Data;

@Data
public class Response<T> {
    private T data;
    private int statusCode;
    private String message;
    private Boolean success;

}
