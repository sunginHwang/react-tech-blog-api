package com.woolta.blog;

import com.google.gson.Gson;
import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.Board;
import com.woolta.blog.repository.BoardRepository;
import com.woolta.blog.service.PostService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"projectProfile=live"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

	@Autowired
	private PostService postService;

	@Autowired
	private BoardRepository boardRepository;


	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private HttpHeaders headers;


	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();

		String tokenId =  "";
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.set("Authorization", tokenId);
	}

	@Test
	public void Post스플릿테스트() {

		PostDto.PostRes post = postService.findPostByNo(7,115);

		String urlReg = "(http|https|ftp|telnet|news|mms)?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?";
		String specialCharReg = "[-+.^:#,*!()!`/]";
		String htmlReg = "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>";

		String subContentReg = urlReg.concat("|").concat(specialCharReg).concat("|").concat(htmlReg);

		String subContent = post.getContent()
				.replaceAll(subContentReg,"")
				.replaceAll("\n","")
				.replace("[","")
				.replace("]","");




		System.out.println(subContent.substring(0,200));
	}

	@Test
	public void Post배치테스트() throws Exception{

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



}
