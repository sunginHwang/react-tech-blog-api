package com.woolta.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolta.blog.controller.UserDto;
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

    private static final String SALT_KEY = "wooltaToken";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> String create(String key, T data, String subject){

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setSubject(subject)
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
    }

    public boolean isValidToken(String jwt) {
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);
            System.out.println(claims.getSignature());
            System.out.println(claims.getBody());
            System.out.println(claims.getHeader());
            return true;

        }catch (Exception e) {
            log.error("Making JWT Key Error ::: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public UserDto.LoginRes getAuthInfo(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");

        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }

        return objectMapper.convertValue(claims.getBody().get(key),UserDto.LoginRes.class);
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
