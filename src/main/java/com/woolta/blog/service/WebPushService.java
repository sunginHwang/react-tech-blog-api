package com.woolta.blog.service;

import com.google.gson.Gson;
import com.woolta.blog.controller.PushDto;
import com.woolta.blog.domain.PushNotification;
import com.woolta.blog.domain.WebPushSubscription;
import com.woolta.blog.repository.WebPushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Service(value = "webPushService")
@Slf4j
@RequiredArgsConstructor
public class WebPushService {

    private final WebPushSubscriptionRepository webPushSubscriptionRepository;

    public void allSendPush(PushNotification pushNotification) {
        List<WebPushSubscription> webPushSubscriptions = webPushSubscriptionRepository.findAll();

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        PushService pushService = new PushService();

        try {
            pushService.setPublicKey("");
            pushService.setPrivateKey("");
        } catch (ArrayIndexOutOfBoundsException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("pushProvider register fail {}", e);
        }


        webPushSubscriptions.stream().forEach(pushSub -> {
            try {
                Notification notification = new Notification(pushSub.getEndPoint(), pushSub.getPushKey(), pushSub.getAuth(), new Gson().toJson(pushNotification));
                pushService.send(notification);
            } catch (Exception e) {
                log.error("notification fail endPoint:{}, key:{}, auth:{} ", pushSub.getEndPoint(), pushSub.getPushKey(), pushSub.getAuth(), e);
            }

        });
    }

    public void sendPush(PushNotification pushNotification, WebPushSubscription webPushSubscription) {

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        PushService pushService = new PushService();

        try {
            pushService.setPublicKey("");
            pushService.setPrivateKey("");
        } catch (ArrayIndexOutOfBoundsException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("pushProvider register fail {}", e);
        }


        try {
            Notification notification = new Notification(
                    webPushSubscription.getEndPoint(),
                    webPushSubscription.getPushKey(),
                    webPushSubscription.getAuth(),
                    new Gson().toJson(pushNotification));
            pushService.send(notification);
        } catch (Exception e) {
            log.error("notification fail endPoint:{}, key:{}, auth:{} ", webPushSubscription.getEndPoint(), webPushSubscription.getPushKey(), webPushSubscription.getAuth(), e);
        }
    }

    public void addPushSubscription(PushDto.SaveReq saveReq) {
        Optional<WebPushSubscription> sameWebPushSubscription = webPushSubscriptionRepository.findByAuthAndPushKey(saveReq.getAuth(), saveReq.getKey());

        if (sameWebPushSubscription.isPresent()) {
            return;
        }

        WebPushSubscription webPushSubscription = WebPushSubscription.builder()
                .auth(saveReq.getAuth())
                .pushKey(saveReq.getKey())
                .endPoint(saveReq.getEndPoint())
                .build();

        webPushSubscriptionRepository.save(webPushSubscription);

        PushNotification pushNotification = PushNotification.builder()
                .title("blog.woolta.com")
                .content("woolta블로그를 구독해주셔서 감사합니다. :)")
                .url("https://blog.woolta.com")
                .build();

        this.sendPush(pushNotification, webPushSubscription);


    }

    public void removePushSubscription(String subscriptionAuthKey) {
        Optional<WebPushSubscription> webPushSubscription = webPushSubscriptionRepository.findByAuth(subscriptionAuthKey);
        webPushSubscription.ifPresent(w -> webPushSubscriptionRepository.delete(w));
    }
}
