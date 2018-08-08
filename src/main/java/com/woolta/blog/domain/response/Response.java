package com.woolta.blog.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private ResponseCode code;
    private String message;
    private T data;

    public Response(ResponseCode code) {
        this.code = code;
    }


    public Response(ResponseCode responseCode, String message) {
        this(responseCode, message, null);
    }

    public Response(ResponseCode code, T data) {
        this.code = code;
        this.data = data;
    }


    public Response(ResponseCode responseCode, String message, T data) {
        this.code = responseCode;
        this.message = message;
        this.data = data;
    }

}
