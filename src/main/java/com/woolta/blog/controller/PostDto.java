package com.woolta.blog.controller;

import com.woolta.blog.domain.vo.Writer;
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

    @NoArgsConstructor
    @Getter
    @Setter
    public static class deleteReq {
        @NotEmpty
        private int postNo;
        @NotEmpty
        private int categoryNo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpsertRes {
        private int postNo;
        private int categoryNo;
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
        private String categoryLabel;
        private int categoryNo;
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
        private String categoryLabel;
        private LocalDate createdAt;
        private Writer writer;
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
