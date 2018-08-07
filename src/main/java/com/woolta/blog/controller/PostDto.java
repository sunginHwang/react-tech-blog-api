package com.woolta.blog.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

}
