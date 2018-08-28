package com.woolta.blog.controller;

import lombok.*;


public class UserDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class UserInfoRes {
        private int no;
        private String userId;
        private String imageUrl;
        private String authToken;
    }
}
