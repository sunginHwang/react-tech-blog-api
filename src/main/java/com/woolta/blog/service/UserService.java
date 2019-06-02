package com.woolta.blog.service;

import com.woolta.blog.domain.AuthToken;
import com.woolta.blog.domain.User;
import com.woolta.blog.exception.login.InvalidPasswordException;
import com.woolta.blog.exception.login.UnauthorizedException;
import com.woolta.blog.exception.login.UserNotFoundException;
import com.woolta.blog.repository.UserRepository;
import com.woolta.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service(value = "userService")
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User login(String id, String password) {

        User user = userRepository.findByUserId(id).orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }

        return user;

    }

    public String makeAuthToken(User user) {

        if (user == null) {
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

    public boolean isAdmin() {
        Optional<AuthToken> authToken = getAuthToken();

        if (!authToken.isPresent()) {
            return false;
        }

        try {
            User user = userRepository.findByUserId(authToken.get().getUserId()).orElseThrow(UserNotFoundException::new);
            return user.getIsAdmin();
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public Optional<User> getUser() {
        Optional<AuthToken> authToken = getAuthToken();

        if (!authToken.isPresent()) {
            return Optional.of(new User());
        }

        return userRepository.findByUserId(authToken.get().getUserId());
    }

    private Optional<AuthToken> getAuthToken() {
        Optional<AuthToken> result = Optional.of(new AuthToken());
        try {
            result = Optional.ofNullable(jwtUtil.getAuthInfo());
        } catch (UnauthorizedException e) {
            log.debug("auth token not found :{}", e);
        }
        return result;
    }


}
