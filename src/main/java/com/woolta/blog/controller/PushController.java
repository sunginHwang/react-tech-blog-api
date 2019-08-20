package com.woolta.blog.controller;

import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
public class PushController {


    @GetMapping("/notification")
    public void push(@RequestParam String key, @RequestParam String auth, @RequestParam String endPoint) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {


        // Add BouncyCastle as an algorithm provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        PushService pushService = new PushService();

        pushService.setPublicKey("");
        pushService.setPrivateKey("");
        Notification notification = new Notification(endPoint, key, auth, "1212121");
        pushService.send(notification);
    }
}
