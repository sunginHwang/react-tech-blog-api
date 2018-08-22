package com.woolta.blog.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private int no;
    private String userId;
    private String imageUrl;
}
