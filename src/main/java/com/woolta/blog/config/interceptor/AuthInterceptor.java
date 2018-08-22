package com.woolta.blog.config.interceptor;


import com.woolta.blog.exception.login.UnauthorizedException;
import com.woolta.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor{

    private static final String HEADER_AUTH = "Authorization";
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader(HEADER_AUTH);

        if(token != null && jwtUtil.isValidToken(token)){
            return true;
        }else{
            throw new UnauthorizedException();
        }
    }
}
