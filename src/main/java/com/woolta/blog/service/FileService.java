package com.woolta.blog.service;

import com.woolta.blog.domain.PostFile;
import com.woolta.blog.domain.response.UploadFileResponse;
import com.woolta.blog.repository.PostFileRepository;
import com.woolta.blog.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service("fileService")
public class FileService {

    private final ImageUtil imageUtil;

    private final PostFileRepository postFileRepository;

    public UploadFileResponse uploadImage(MultipartFile file) {
        UploadFileResponse image = imageUtil.uploadImage(file);

        PostFile postFile;
        postFile = PostFile.builder()
                .fileName(image.getFileName())
                .originFileName(image.getOriginFileName())
                .fileDir(image.getFileDir())
                .fileExt(image.getFileExt())
                .fullPath(image.getFullPath())
                .size(image.getSize())
                .build();

        log.info("[POST] imageUpload : {}, {}, {}, {}, {}, {}",
                image.getFileDir(),image.getFileExt(), image.getFileName(),
                image.getOriginFileName(), image.getSize(),image.getFullPath());

        postFileRepository.save(postFile);

        return image;
    }
}
