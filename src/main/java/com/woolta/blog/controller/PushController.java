package com.woolta.blog.controller;

import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.jose4j.lang.JoseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
public class PushController {


    @GetMapping("/notification")
    public void push(@RequestParam String key, @RequestParam String auth) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        PushService pushService = new PushService();

        pushService.setPublicKey("AAAA9TjSk74:APA91bGojZrGMIyzm-OXXgQHwGhqviRcANDBXspcv69dx7YN-63HM2r0z0mPxXzyyeDghUvjB3zkPGYW8Hwqsr9b8KBwwliOeghYmBLwP-bEtABXu4L_5AtrrKZmPv6PbNoWsIPSCVRv");
        Notification notification = new Notification("/", key, auth, "");
        pushService.send(notification);
    }
}
