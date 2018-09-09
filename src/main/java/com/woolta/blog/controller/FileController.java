package com.woolta.blog.controller;

import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
import com.woolta.blog.domain.response.UploadFileResponse;
import com.woolta.blog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload/image")
    public Response imageUpload(@RequestParam("imageFile") MultipartFile imageFile) {
        UploadFileResponse uploadFileResponse = fileService.uploadImage(imageFile);
        return new Response<>(ResponseCode.SUCCESS, "success upload Image", uploadFileResponse);
    }

    @PostMapping("/upload/images")
    public Response uploadMultipleFiles(@RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        List<UploadFileResponse> uploadFileResponses = imageFiles.stream()
                .map(fileService::uploadImage)
                .collect(Collectors.toList());

        return new Response<>(ResponseCode.SUCCESS, "success upload Images", uploadFileResponses);
    }
}
