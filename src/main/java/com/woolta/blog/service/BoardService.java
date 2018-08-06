package com.woolta.blog.service;

import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.repository.BoardCategoryRepository;
import com.woolta.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "boardService")
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    public Board findBoardByNo(Integer categoryNo, Integer boardNo) {

        BoardCategory boardCategory = boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        return boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);
    }

    public Board upsertPost(Integer categoryNo, Integer boardNo) {

        BoardCategory boardCategory = boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));



        return boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);
    }
}
