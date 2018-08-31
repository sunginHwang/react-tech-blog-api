package com.woolta.blog.exception;

public class InvalidAuthorUserException extends RuntimeException{
    public InvalidAuthorUserException(String msg) {
        super(msg);
    }
}
