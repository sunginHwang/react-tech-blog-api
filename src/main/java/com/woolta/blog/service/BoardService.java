package com.woolta.blog.service;

import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.domain.User;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.repository.BoardCategoryRepository;
import com.woolta.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "boardService")
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    public Board findPostByNo(Integer categoryNo, Integer boardNo) {

        BoardCategory boardCategory = boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);

        if(!boardCategory.getNo().equals(board.getCategory().getNo())){
            throw new NotFoundException(
                    "this category is not found post categoryNo : " + categoryNo + " postNo:"+boardNo);
        }

        return board;
    }

    public List<Board> findPostByCategoryNo(Integer categoryNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        return boardRepository.findBycategoryNo(categoryNo).orElseThrow(RuntimeException::new);
    }

    public void upsertPost(PostDto.UpsertReq req) {

        BoardCategory boardCategory = boardCategoryRepository.findById(req.getCategoryNo())
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + req.getCategoryNo()));

        Board board;
        board = Board.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .user(new User(1))//todo auth
                .category(boardCategory)
                .subDescription(req.getSubDescription())
                .build();

        if(req.getId() != 0){
            Optional<Board> originBoard = boardRepository.findById(req.getId());
            originBoard.ifPresent(b->board.setId(b.getId()));
        }

        boardRepository.save(board);
    }

    public void removePost(int categoryNo, int postNo){
        Board board = findPostByNo(categoryNo, postNo);
        boardRepository.delete(board);
    }
}
