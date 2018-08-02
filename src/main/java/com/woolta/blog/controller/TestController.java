package com.woolta.blog.controller;

import com.woolta.blog.domain.Board;
import com.woolta.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final BoardService boardService;

    @GetMapping("/func")
    public String testFunction(){
        return "success";
    }

    @GetMapping("/boards")
    public Board getBoard(@RequestParam Integer boardNo){
        return boardService.findBoardByNo(boardNo);
    }
}
