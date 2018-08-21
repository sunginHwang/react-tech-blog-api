package com.woolta.blog.service;

import com.woolta.blog.domain.User;
import com.woolta.blog.exception.login.InvalidPasswordException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "userService")
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(String id, String password){

        User user =  userRepository.findByUserId(id).orElseThrow(UserNotFoundException::new);

        if(!user.getPassword().equals(password)){
            throw new InvalidPasswordException();
        }

        return user;

    }
}
