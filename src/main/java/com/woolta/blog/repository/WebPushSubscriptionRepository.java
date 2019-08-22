package com.woolta.blog.repository;

import com.woolta.blog.domain.WebPushSubscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface WebPushSubscriptionRepository extends CrudRepository<WebPushSubscription, Integer> {

    @Override
    List<WebPushSubscription> findAll();

    Optional<WebPushSubscription> findByAuthAndPushKey(String auth, String pushKey);
}
