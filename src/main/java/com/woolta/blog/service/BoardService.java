package com.woolta.blog.service;

import com.woolta.blog.domain.Board;
import com.woolta.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "boardService")
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board findBoardByNo(Integer boardNo){
        return boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);
    }
}
