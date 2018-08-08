package com.woolta.blog.exception;

import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response handleDataAccessException(Exception e) {
        log.error("{}", e);
        return new Response<>(ResponseCode.UNKNOWN_ERROR, e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response handleDataAccessException(DataAccessException e) {
        log.error("{}", e);
        return new Response<>(ResponseCode.NOT_FOUND, e.getMessage());
    }
}
