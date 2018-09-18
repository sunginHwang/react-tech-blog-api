package com.woolta.blog;

import com.woolta.blog.controller.PostDto;
import com.woolta.blog.service.PostService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(properties = {"projectProfile=live"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

	@Autowired
	private PostService postService;

	@Test
	public void Post스플릿테스트() {

		PostDto.PostRes post = postService.findPostByNo(7,115);

		String urlReg = "(http|https|ftp|telnet|news|mms)?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?";
		String specialCharReg = "[-+.^:#,*!()!`/]";
		String htmlReg = "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>";

		String subContentReg = urlReg.concat("|").concat(specialCharReg).concat("|").concat(htmlReg);

		String subContent = post.getContent()
				.replaceAll(subContentReg,"")
				.replace("[","")
				.replace("]","");




		System.out.println(subContent);
	}



}
