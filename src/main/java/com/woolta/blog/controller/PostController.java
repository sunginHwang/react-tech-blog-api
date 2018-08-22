package com.woolta.blog.controller;

import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.exception.login.UserNotFoundException;
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
    public Response<PostDto.UpsertRes> upsertPost(@RequestBody PostDto.UpsertReq req) {
        try {
            PostDto.UpsertRes upsertRes = postService.upsertPost(req);
            return new Response<>(ResponseCode.SUCCESS, "success upsert post", upsertRes);
        } catch (NotFoundException e) {
            return new Response<>(ResponseCode.NOT_FOUND, "존재 하지 않는 항목 카테고리 입니다.");
        } catch (UserNotFoundException e) {
            return new Response<>(ResponseCode.UNAUTHORIZED, "존재하지 않는 사용자입니다. 다시 로그인해주세요. ");
        }


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
