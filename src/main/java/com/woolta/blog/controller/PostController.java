package com.woolta.blog.controller;

import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
import com.woolta.blog.service.PostService;
import com.woolta.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Response<List<Board>> getPostByCategory(@PathVariable Integer categoryNo) {
        List<Board> boards = postService.findPostByCategoryNo(categoryNo);
        return new Response<>(ResponseCode.SUCCESS, boards);
    }


    @GetMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Response<Board> getPost(@PathVariable Integer categoryNo,
                                   @PathVariable Integer postNo) {
        Board board = postService.findPostByNo(categoryNo, postNo);
        return new Response<>(ResponseCode.SUCCESS, board);
    }


    @DeleteMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Response removePost(@PathVariable Integer categoryNo,
                               @PathVariable Integer postNo) {
        postService.removePost(categoryNo, postNo);
        return new Response<>(ResponseCode.SUCCESS, "success remove post");
    }


    @GetMapping("/categories")
    public Response<List<BoardCategory>> getCategories() {
        List<BoardCategory> boardCategories = categoryService.getBoardCategories();
        return new Response<>(ResponseCode.SUCCESS, boardCategories);
    }
}
