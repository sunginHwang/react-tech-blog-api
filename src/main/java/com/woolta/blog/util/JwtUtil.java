package com.woolta.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolta.blog.controller.UserDto;
import com.woolta.blog.domain.AuthToken;
import com.woolta.blog.exception.login.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class JwtUtil {

    private final String SALT_KEY = "woolta";
    private final String AUTH_KEY ="authKey";
    private final String SUB_KEY ="subKey";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> String create(T data){

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setSubject(SUB_KEY)
                .claim(AUTH_KEY, data)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
    }

    public boolean isValidToken(String jwt) {
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);
            return true;

        }catch (Exception e) {
            log.error("Making JWT Key Error ::: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public AuthToken getAuthInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");

        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);

            return objectMapper.convertValue(claims.getBody().get(AUTH_KEY),AuthToken.class);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }


    }


    private byte[] generateKey(){
        byte[] key = null;

        try {
            key = SALT_KEY.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            if(log.isInfoEnabled()){
                e.printStackTrace();
            }else{
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }

        return key;
    }


}
