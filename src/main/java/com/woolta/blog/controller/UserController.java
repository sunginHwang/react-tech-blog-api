package com.woolta.blog.controller;

import com.woolta.blog.domain.User;
import com.woolta.blog.exception.login.FailLoginException;
import com.woolta.blog.exception.login.InvalidPasswordException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.service.UserService;
import com.woolta.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final JwtUtil jwtUtil;

    private final UserService userService;


    @PostMapping("/login")
    public String login( @RequestParam(value = "id") String id,
                         @RequestParam(value = "password") String password){
        try {
            User user = userService.login(id, password);

            UserDto.LoginRes loginRes = UserDto.LoginRes.builder()
                    .userId(user.getUserId())
                    .imageUrl(user.getImageUrl())
                    .build();

            return jwtUtil.create("authKey", loginRes, "subjectUser");
        }catch (UserNotFoundException e){
            return "유저 없음";
        }catch (InvalidPasswordException e){
            return "비밀번호 틀림";
        }
    }

    @GetMapping("/check/jwt")
    public UserDto.LoginRes tokenCheck(
            @RequestHeader(value = "Authorization") String Authorization){

        jwtUtil.isValidToken(Authorization);

        return jwtUtil.getAuthInfo("authKey");
    }

}
