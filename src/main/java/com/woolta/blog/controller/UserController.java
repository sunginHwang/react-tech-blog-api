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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Response<UserDto.UserInfoRes> login( @RequestParam(value = "id") String id,
                         @RequestParam(value = "password") String password){

            User user = userService.login(id, password);
            String authToken = userService.makeAuthToken(user);

            UserDto.UserInfoRes loginRes = UserDto.UserInfoRes.builder()
                    .no(user.getNo())
                    .userId(user.getUserId())
                    .imageUrl(user.getImageUrl())
                    .authToken(authToken)
                    .build();

            return new Response<>(ResponseCode.SUCCESS, loginRes);

    }

    @GetMapping("/check/jwt")
    public Response<UserDto.UserInfoRes> tokenCheck(
            @RequestHeader(value = "Authorization") String Authorization){

         jwtUtil.isValidToken(Authorization);
        AuthToken authToken =jwtUtil.getAuthInfo();

        UserDto.UserInfoRes userInfoRes = UserDto.UserInfoRes.builder()
                .no(authToken.getNo())
                .userId(authToken.getUserId())
                .imageUrl(authToken.getImageUrl())
                .authToken(Authorization)
                .build();

        return new Response<>(ResponseCode.SUCCESS, userInfoRes);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response handleUserNotFoundException(UserNotFoundException e) {
        log.error("{}", e);
        return new Response<>(ResponseCode.UNAUTHORIZED, "존재하지 않는 아이디 입니다.");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response handleInvalidPasswordException(InvalidPasswordException e) {
        log.error("{}", e);
        return new Response<>(ResponseCode.UNAUTHORIZED, "비밀번호를 확인해주세요.");
    }



}
