package com.woolta.blog.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotification {

    private String title;
    private String content;
    private String url;
}
