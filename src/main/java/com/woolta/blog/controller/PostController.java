package com.woolta.blog.controller;

import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.service.BoardService;
import com.woolta.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    @PostMapping("")
    public String upsertPost(@RequestBody PostDto.UpsertReq req){
        boardService.upsertPost(req);
        return "success";
    }

    @GetMapping("/categories/{categoryNo:[\\d]+}/posts")
    public List<Board> getPostByCategory(@PathVariable Integer categoryNo){
        return boardService.findPostByCategoryNo(categoryNo);
    }



    @GetMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Board getPost( @PathVariable Integer categoryNo,
     @PathVariable Integer postNo){
        return boardService.findPostByNo(categoryNo ,postNo);
    }



    @DeleteMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public String removePost(@PathVariable Integer categoryNo,
                              @PathVariable Integer postNo){
        boardService.removePost(categoryNo, postNo);
        return "success";
    }



    @GetMapping("/categories")
    public List<BoardCategory> getCategories( ){
        return categoryService.getBoardCategories();
    }
}
