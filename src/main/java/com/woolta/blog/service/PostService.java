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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service(value = "boardService")
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    public PostDto.PostRes findPostByNo(Integer categoryNo, Integer boardNo) {

        BoardCategory boardCategory = boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);

        if (!boardCategory.getNo().equals(board.getCategory().getNo())) {
            throw new NotFoundException(
                    "this category is not found post categoryNo : " + categoryNo + " postNo:" + boardNo);
        }

        return PostDto.PostRes.builder()
                .postNo(board.getId())
                .author(board.getUser().getNickName())
                .content(board.getContents())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public List<PostDto.PostsRes> findPostByCategoryNo(Integer categoryNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        List<Board> boards = boardRepository.findBycategoryNo(categoryNo).orElseThrow(RuntimeException::new);

        List<PostDto.PostsRes> postResList = new LinkedList<>();

        boards.forEach(board ->
            postResList.add(
                    PostDto.PostsRes.builder()
                            .postNo(board.getId())
                            .author(board.getUser().getNickName())
                            .title(board.getTitle())
                            .subDescription(board.getSubDescription())
                            .createdAt(board.getCreatedAt())
                            .build()
            )
        );

        return postResList;
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

        if (req.getId() != 0) {
            Optional<Board> originBoard = boardRepository.findById(req.getId());
            originBoard.ifPresent(b -> board.setId(b.getId()));
        }

        boardRepository.save(board);
    }

    public void removePost(int categoryNo, int postNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(postNo).orElseThrow(RuntimeException::new);

        boardRepository.delete(board);
    }
}
