package com.woolta.blog.controller;

import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
import com.woolta.blog.service.PostService;
import com.woolta.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final CategoryService categoryService;

    @PostMapping("")
    public Response upsertPost(@RequestBody PostDto.UpsertReq req) {
        postService.upsertPost(req);
        return new Response<>(ResponseCode.SUCCESS, "success upsert post");
    }

    @GetMapping("/categories/{categoryNo:[\\d]+}/posts")
    public Response<List<PostDto.PostsRes>> getPostByCategory(@PathVariable Integer categoryNo) {
        List<PostDto.PostsRes> postRes = postService.findPostByCategoryNo(categoryNo);

        return new Response<>(ResponseCode.SUCCESS, postRes);
    }


    @GetMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Response<PostDto.PostRes> getPost(@PathVariable Integer categoryNo,
                                             @PathVariable Integer postNo) {
        PostDto.PostRes postRes = postService.findPostByNo(categoryNo, postNo);
        return new Response<>(ResponseCode.SUCCESS, postRes);
    }


    @DeleteMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Response removePost(@PathVariable Integer categoryNo,
                               @PathVariable Integer postNo) {
        postService.removePost(categoryNo, postNo);
        return new Response<>(ResponseCode.SUCCESS, "success remove post");
    }


    @GetMapping("/categories")
    public Response<List<PostDto.CategoriesRes>> getCategories() {
        List<PostDto.CategoriesRes> categoriesRes = categoryService.getBoardCategories();
        return new Response<>(ResponseCode.SUCCESS, categoriesRes);
    }
}
