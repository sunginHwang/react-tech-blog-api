package com.woolta.blog.service;

import com.google.gson.Gson;
import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.AuthToken;
import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.domain.User;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.repository.BoardCategoryRepository;
import com.woolta.blog.repository.BoardRepository;
import com.woolta.blog.repository.UserRepository;
import com.woolta.blog.util.JwtUtil;
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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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
                .categoryLabel(board.getCategory().getCategoryName())
                .createdAt(board.getCreatedAt().toLocalDate())
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
                            .categoryLabel(board.getCategory().getCategoryName())
                            .subDescription(board.getSubDescription())
                            .createdAt(board.getCreatedAt().toLocalDate())
                            .build()
            )
        );

        return postResList;
    }

    public PostDto.UpsertRes upsertPost(PostDto.UpsertReq req) {

        log.info("upsert Start post :{}",new Gson().toJson(req));

        BoardCategory boardCategory = boardCategoryRepository.findById(req.getCategoryNo())
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + req.getCategoryNo()));

        AuthToken authInfo = jwtUtil.getAuthInfo();

        User user = userRepository.findByUserId(authInfo.getUserId()).orElseThrow(UserNotFoundException::new);

        Board board;
        board = Board.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .subDescription(req.getContents().substring(0,5)+"간략 설명")
                .user(user)
                .category(boardCategory)
                .build();

        if (req.getId() != 0) {
            Optional<Board> originBoard = boardRepository.findById(req.getId());
            originBoard.ifPresent(b -> board.setId(b.getId()));
        }

        Board upsertedPost = boardRepository.save(board);

        return PostDto.UpsertRes.builder()
                .postNo(upsertedPost.getId())
                .categoryNo(upsertedPost.getCategory().getNo())
                .build();

    }

    public void removePost(int categoryNo, int postNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(postNo).orElseThrow(RuntimeException::new);

        boardRepository.delete(board);
    }


}
