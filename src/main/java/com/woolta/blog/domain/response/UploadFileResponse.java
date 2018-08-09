package com.woolta.blog.domain.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileResponse {

    private String originFileName;
    private String fileName;
    private String fileDir;
    private String fileExt;
    private String fullPath;
    private long size;

}
