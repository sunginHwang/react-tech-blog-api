package com.woolta.blog.exception.login;

import lombok.Data;

@Data
public class FailLoginException extends RuntimeException{

    private String message;
    public FailLoginException(String message) {
        this.message = message;
    }
}
