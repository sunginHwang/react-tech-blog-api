package com.woolta.blog.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class PushDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class SaveReq {

        @NotNull
        private String key;
        @NotNull
        private String auth;
        @NotNull
        private String endPoint;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RemoveReq {

        @NotNull
        private String key;
    }
}
