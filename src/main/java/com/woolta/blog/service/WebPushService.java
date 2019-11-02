package com.woolta.blog.service;

import com.google.gson.Gson;
import com.woolta.blog.controller.PushDto;
import com.woolta.blog.domain.PushNotification;
import com.woolta.blog.domain.WebPushKey;
import com.woolta.blog.domain.WebPushSubscription;
import com.woolta.blog.exception.NotFoundException;
import com.woolta.blog.repository.WebPushKeyRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "webPushService")
@Slf4j
@RequiredArgsConstructor
public class WebPushService {

    private final WebPushSubscriptionRepository webPushSubscriptionRepository;
    private final WebPushKeyRepository webPushKeyRepository;

    public void allSendPush(PushNotification pushNotification) {
        List<WebPushSubscription> webPushSubscriptions = webPushSubscriptionRepository.findAll();

        List<Notification> notifications = new ArrayList<>();

        webPushSubscriptions.stream().forEach(pushSub -> {
            try {
                Notification notification = new Notification(pushSub.getEndPoint(), pushSub.getPushKey(), pushSub.getAuth(), new Gson().toJson(pushNotification));
                notifications.add(notification);
            } catch (Exception e) {
                log.error("notification fail endPoint:{}, key:{}, auth:{} ", pushSub.getEndPoint(), pushSub.getPushKey(), pushSub.getAuth(), e);
            }

        });

        sendPush(notifications);
    }

    public void sendPush(List<Notification> notifications) {

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        PushService pushService = new PushService();

        try {
            WebPushKey webPushKey = webPushKeyRepository.findById(1).orElseThrow(() -> new NotFoundException("not found webPushKey"));
            pushService.setPublicKey(webPushKey.getPublicKey());
            pushService.setPrivateKey(webPushKey.getPrivateKey());
        } catch (ArrayIndexOutOfBoundsException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("pushProvider register fail {}", e);
        }


        notifications.stream().forEach(notification -> {
            try {
                pushService.send(notification);
            } catch (Exception e) {
                log.error("notification fail payload:{}", notification.getPayload(), e);
            }

        });
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

        List<Notification> notifications = new ArrayList<>();

        try {
            Notification notification = new Notification(
                    webPushSubscription.getEndPoint(),
                    webPushSubscription.getPushKey(),
                    webPushSubscription.getAuth(),
                    new Gson().toJson(pushNotification));
            notifications.add(notification);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            log.error("fail to addPushSubscription converting notification webPushSubscription:{}, pushNotification:{}  ", new Gson().toJson(webPushSubscription), new Gson().toJson(pushNotification), e);
        }

        this.sendPush(notifications);


    }

    public void removePushSubscription(String subscriptionAuthKey) {
        Optional<WebPushSubscription> webPushSubscription = webPushSubscriptionRepository.findByAuth(subscriptionAuthKey);
        webPushSubscription.ifPresent(w -> webPushSubscriptionRepository.delete(w));
    }
}
