package com.woolta.blog.service;

import com.google.gson.Gson;
import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.*;
import com.woolta.blog.domain.vo.Writer;
import com.woolta.blog.exception.InvalidAuthorUserException;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.repository.BoardCategoryRepository;
import com.woolta.blog.repository.BoardRepository;
import com.woolta.blog.repository.UserRepository;
import com.woolta.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
    private final UserService userService;
    private final WebPushService webPushService;
    private final JwtUtil jwtUtil;


    public PostDto.PostRes readPost(Integer categoryNo, Integer boardNo) {

        Board board = getBoard(categoryNo, boardNo);
        increasePostViews(board);
        return PostDto.PostRes.builder()
                .postNo(board.getId())
                .writer(new Writer(board.getUser().getNo(), board.getUser().getNickName(), board.getUser().getImageUrl()))
                .content(board.getContents())
                .title(board.getTitle())
                .categoryLabel(board.getCategory().getCategoryName())
                .categoryNo(board.getCategory().getNo())
                .createdAt(board.getCreatedAt().toLocalDate())
                .build();
    }

    private void increasePostViews(Board board) {

        if (board == null) {
            return;
        }

        if (!userService.isAdmin()) {
            board.setViews(board.getViews() + 1);
            boardRepository.save(board);
        }

    }


    public Board getBoard(Integer categoryNo, Integer boardNo) {

        BoardCategory boardCategory = boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(boardNo).orElseThrow(RuntimeException::new);

        if (!boardCategory.getNo().equals(board.getCategory().getNo())) {
            throw new NotFoundException(
                    "this category is not found post categoryNo : " + categoryNo + " postNo:" + boardNo);
        }

        return board;
    }

    public List<PostDto.PostsRes> findPostByCategoryNo(Integer categoryNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        List<Board> boards = boardRepository.findBycategoryNo(categoryNo).orElseThrow(RuntimeException::new);

        return convertPostsRes(boards);
    }

    public List<PostDto.PostsRes> getRecentPosts() {


        List<Board> boards = boardRepository.findTop20ByOrderByCreatedAtDesc().orElseThrow(RuntimeException::new);

        return convertPostsRes(boards);
    }

    public List<PostDto.PostRes> getAllPosts() {
        List<Board> boards = (List<Board>) boardRepository.findAll();

        List<PostDto.PostRes> PostResList = new LinkedList<>();
        boards.forEach(board ->
                PostResList.add(
                        PostDto.PostRes.builder()
                                .postNo(board.getId())
                                .title(board.getTitle())
                                .categoryLabel(board.getCategory().getCategoryName())
                                .categoryNo(board.getCategory().getNo())
                                .content(this.removeMarkupLanguage(board.getContents()))
                                .createdAt(board.getCreatedAt().toLocalDate())
                                .writer(new Writer(board.getUser().getNo(), board.getUser().getNickName(), board.getUser().getImageUrl()))
                                .build()
                )
        );

        return PostResList;

    }

    public PostDto.UpsertRes upsertPost(PostDto.UpsertReq req) {

        log.info("upsert Start post :{}", new Gson().toJson(req));

        boolean isCreatedPost = false;

        BoardCategory boardCategory = boardCategoryRepository.findById(req.getCategoryNo())
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + req.getCategoryNo()));

        User user = userService.getUser().orElseThrow(UserNotFoundException::new);

        Board board = Board.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .subDescription(makePostSubDescription(req.getContents()))
                .user(user)
                .category(boardCategory)
                .views(0)
                .build();

        if (req.getId() != 0) {
            Optional<Board> originBoard = boardRepository.findById(req.getId());

            originBoard.ifPresent(b -> {
                board.setId(b.getId());
                board.setViews(b.getViews());
            });

            if (!board.getUser().getUserId().equals(user.getUserId())) {
                throw new InvalidAuthorUserException("board author is not match");
            }
        } else {
            isCreatedPost = true;
        }

        Board savedPost = boardRepository.save(board);

        if (isCreatedPost) {
            pushNewPost(savedPost);
        }

        return PostDto.UpsertRes.builder()
                .postNo(savedPost.getId())
                .categoryNo(savedPost.getCategory().getNo())
                .build();

    }

    @Async
    public void pushNewPost(Board board) {
        PushNotification pushNotification = PushNotification.builder()
                .title("신규 포스트 알림")
                .content(board.getTitle())
                .url("https://blog.woolta.com/categories/" + board.getCategory().getNo() + "/posts/" + board.getId())
                .build();

        webPushService.allSendPush(pushNotification);
    }

    public void removePost(int categoryNo, int postNo) {

        boardCategoryRepository.findById(categoryNo)
                .orElseThrow(() -> new NotFoundException("category is not found  categoryNo : " + categoryNo));

        Board board = boardRepository.findById(postNo).orElseThrow(RuntimeException::new);

        AuthToken authInfo = jwtUtil.getAuthInfo();

        User user = userRepository.findByUserId(authInfo.getUserId()).orElseThrow(UserNotFoundException::new);

        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new InvalidAuthorUserException("board author is not match");
        }

        boardRepository.delete(board);
    }

    private List<PostDto.PostsRes> convertPostsRes(List<Board> boards) {
        List<PostDto.PostsRes> postsRes = new LinkedList<>();

        boards.forEach(board ->
                postsRes.add(
                        PostDto.PostsRes.builder()
                                .postNo(board.getId())
                                .author(board.getUser().getNickName())
                                .title(board.getTitle())
                                .categoryLabel(board.getCategory().getCategoryName())
                                .categoryNo(board.getCategory().getNo())
                                .subDescription(board.getSubDescription())
                                .createdAt(board.getCreatedAt().toLocalDate())
                                .build()
                )
        );

        return postsRes;
    }

    private String makePostSubDescription(String content) {

        String subContent = this.removeMarkupLanguage(content);
        if (subContent.length() > 100) {
            return subContent.substring(0, 100) + "...";
        } else {
            return subContent + "...";
        }
    }

    private String removeMarkupLanguage(String markupContent) {

        String urlReg = "(http|https|ftp|telnet|news|mms)?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?";
        String specialCharReg = "[-+.^:#,*!()!`/]";
        String htmlReg = "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>";

        String subContentReg = urlReg.concat("|").concat(specialCharReg).concat("|").concat(htmlReg);

        return markupContent
                .replaceAll(subContentReg, "")
                .replaceAll("\n", "")
                .replace("[", "")
                .replace("]", "");

    }


}
