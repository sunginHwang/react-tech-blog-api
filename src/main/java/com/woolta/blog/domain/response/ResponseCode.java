package com.woolta.blog.domain.response;


public enum  ResponseCode {
    SUCCESS(200),
    ERROR_DATA_ACCESS(4000),
    NOT_FOUND(4001),
    UNAUTHORIZED(401),
    BAD_REQUEST(4002),
    UNKNOWN_ERROR(4003);

    private int resCode;

    ResponseCode(int resCode) {
        this.resCode = resCode;
    }

    public int getResCode(){
        return resCode;
    }

}
