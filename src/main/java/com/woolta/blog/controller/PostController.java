package com.woolta.blog.controller;

import com.woolta.blog.domain.Board;
import com.woolta.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final BoardService boardService;

    @GetMapping("/categories/{categoryNo:[\\d]+}/posts/{postNo:[\\d]+}")
    public Board getBoard( @PathVariable Integer categoryNo,
     @PathVariable Integer postNo){
        return boardService.findBoardByNo(categoryNo ,postNo);
    }
}
