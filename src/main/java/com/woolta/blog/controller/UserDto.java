package com.woolta.blog.controller;

import lombok.*;


public class UserDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class LoginRes {
        private String userId;
        private String imageUrl;
    }
}
