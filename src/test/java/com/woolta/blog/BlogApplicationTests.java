package com.woolta.blog;

import com.google.gson.Gson;
import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.Board;
import com.woolta.blog.domain.WebPushSubscription;
import com.woolta.blog.repository.BoardRepository;
import com.woolta.blog.repository.WebPushSubscriptionRepository;
import com.woolta.blog.service.PostService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"projectProfile=live"})
@RunWith(SpringRunner.class)
@Ignore
@SpringBootTest
public class BlogApplicationTests {

    @Autowired
    private PostService postService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WebPushSubscriptionRepository webPushSubscriptionRepository;


    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private HttpHeaders headers;


    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        String tokenId = "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTM2NTg4NzYzNzE3LCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWJLZXkiLCJhdXRoS2V5Ijp7Im5vIjoxLCJ1c2VySWQiOiJnb21tcG8iLCJpbWFnZVVybCI6Imh0dHBzOi8vaW1hZ2Uud29vbHRhLmNvbS9hZGRlYmM3M2JiNjE4YTM1YWRmNTI5MjgzY2Y3OGNlNy5wbmcifX0.gqKE606IldrVAI67d2p8IfSJPw9fMwLPXlxXrAeQXcQ12";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", tokenId);
    }

    @Test
    public void Post스플릿테스트() {

        PostDto.PostRes post = postService.readPost(7, 115);

        String urlReg = "(http|https|ftp|telnet|news|mms)?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?";
        String specialCharReg = "[-+.^:#,*!()!`/]";
        String htmlReg = "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>";

        String subContentReg = urlReg.concat("|").concat(specialCharReg).concat("|").concat(htmlReg);

        String subContent = post.getContent()
                .replaceAll(subContentReg, "")
                .replaceAll("\n", "")
                .replace("[", "")
                .replace("]", "");


        System.out.println(subContent.substring(0, 200));
    }

    @Test
    public void WebPush조회테스트(){
        List<WebPushSubscription> webPushSubscription = webPushSubscriptionRepository.findAll();

        webPushSubscription.stream().forEach(webPushSubscription1 -> System.out.println(webPushSubscription1.getAuth()));
    }

    @Test
    public void Post조회수테스트() {

        int categoryNo = 7;
        int boardNo = 115;

        Board prevBoard = postService.getBoard(categoryNo, boardNo);
        int prevViews = prevBoard.getViews();

        postService.readPost(categoryNo, boardNo);

        Board nextBoard = postService.getBoard(categoryNo, boardNo);
        int nextViews = nextBoard.getViews();

        Assert.assertEquals(prevViews + 1, nextViews);

    }

    @Test
    public void Post관리자조회수테스트() {

        int categoryNo = 7;
        int boardNo = 115;

        Board prevBoard = postService.getBoard(categoryNo, boardNo);
        int prevViews = prevBoard.getViews();

        try {
            mvc.perform(
                    get("/post/categories/" + categoryNo + "/posts/" + boardNo)
                            .headers(headers)
            )
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board nextBoard = postService.getBoard(categoryNo, boardNo);
        int nextViews = nextBoard.getViews();

        Assert.assertEquals(prevViews+1, nextViews);

    }

    @Test
    public void Post배치테스트() throws Exception {

        List<Board> boards = (List<Board>) boardRepository.findAll();

        boards.forEach(board -> {

            PostDto.UpsertReq req = new PostDto.UpsertReq();
            req.setCategoryNo(board.getCategory().getNo());
            req.setContents(board.getContents());
            req.setId(board.getId());
            req.setTitle(board.getTitle());


            try {
                mvc.perform(
                        post("/post")
                                .headers(headers)
                                .content(new Gson().toJson(req))
                )
                        .andDo(print())
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void Post_upsert_테스트() throws Exception {

        int categoryNo = 9;
        int postId = 136;
        String contents = "1212121";
        String title = "12121212";

        PostDto.UpsertReq req = new PostDto.UpsertReq();
        req.setCategoryNo(categoryNo);
        req.setContents(contents);
        req.setId(postId);
        req.setTitle(title);

        try {
            mvc.perform(
                    post("/post")
                            .headers(headers)
                            .content(new Gson().toJson(req))
            )
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
