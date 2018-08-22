package com.woolta.blog.service;

import com.woolta.blog.domain.AuthToken;
import com.woolta.blog.domain.User;
import com.woolta.blog.exception.login.InvalidPasswordException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.repository.UserRepository;
import com.woolta.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "userService")
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User login(String id, String password){

        User user =  userRepository.findByUserId(id).orElseThrow(UserNotFoundException::new);

        if(!user.getPassword().equals(password)){
            throw new InvalidPasswordException();
        }

        return user;

    }

    public String makeAuthToken(User user){

        if(user == null){
            return "";
        }

        AuthToken authToken;

        authToken = AuthToken.builder()
                .no(user.getNo())
                .userId(user.getUserId())
                .imageUrl(user.getImageUrl())
                .build();

        return jwtUtil.create(authToken);
    }
}
