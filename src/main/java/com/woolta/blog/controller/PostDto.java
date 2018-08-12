package com.woolta.blog.controller;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class PostDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UpsertReq {
        @NotEmpty
        private int id;
        @NotEmpty
        private int categoryNo;
        @NotNull
        private String title;
        @NotNull
        private String contents;
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
        private LocalDate createdAt;
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
        private LocalDate createdAt;
        private String author;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CategoriesRes {
        private int value;
        private String label;
    }

}
