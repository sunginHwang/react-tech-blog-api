package com.woolta.blog.domain.vo;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Writer {

    private int no;
    private String nickName;
    private String imageUrl;

}
