package com.woolta.blog.controller;

import com.woolta.blog.domain.PushNotification;
import com.woolta.blog.service.WebPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
public class PushController {

    private final WebPushService webPushService;


    @GetMapping("/notification")
    public void pushAll() {
        PushNotification pushNotification = PushNotification.builder()
                .title("woolta 기술 블로그")
                .content("새로운 포스트 출시")
                .url("https://blog.woolta.com/categories/10/posts/148")
                .build();
        webPushService.allSendPush(pushNotification);
    }

    @PostMapping("/subscription")
    public void subscription(@RequestBody PushDto.SaveReq saveReq) {

        webPushService.addPushSubscription(saveReq);
    }
}
