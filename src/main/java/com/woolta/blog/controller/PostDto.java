package com.woolta.blog.controller;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;


public class PostDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UpsertReq {
        private int id;
        private int categoryNo;
        private String title;
        private String contents;
        private String subDescription;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class PostsRes {
        private int postNo;
        private String title;
        private String subDescription;
        private ZonedDateTime createdAt;
        private String author;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class PostRes {
        private int postNo;
        private String title;
        private String content;
        private ZonedDateTime createdAt;
        private String author;
    }

}
