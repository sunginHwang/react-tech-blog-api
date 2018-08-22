package com.woolta.blog.controller;

import com.woolta.blog.domain.AuthToken;
import com.woolta.blog.domain.User;
import com.woolta.blog.domain.response.Response;
import com.woolta.blog.domain.response.ResponseCode;
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


    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Response<UserDto.LoginRes> login( @RequestParam(value = "id") String id,
                         @RequestParam(value = "password") String password){
        try {
            User user = userService.login(id, password);
            String authToken = userService.makeAuthToken(user);

            UserDto.LoginRes loginRes = UserDto.LoginRes.builder()
                    .userId(user.getUserId())
                    .imageUrl(user.getImageUrl())
                    .authToken(authToken)
                    .build();

            return new Response<>(ResponseCode.SUCCESS, loginRes);
        }catch (UserNotFoundException e){
            return new Response<>(ResponseCode.NOT_FOUND, "존재하지 않는 아이디 입니다.");
        }catch (InvalidPasswordException e){
            return new Response<>(ResponseCode.UNAUTHORIZED, "비밀번호를 확인해주세요.");
        }
    }

    @GetMapping("/check/jwt")
    public AuthToken tokenCheck(
            @RequestHeader(value = "Authorization") String Authorization){

        jwtUtil.isValidToken(Authorization);

        return jwtUtil.getAuthInfo();
    }

}
