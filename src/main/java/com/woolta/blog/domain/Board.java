package com.woolta.blog.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "board")
public class Board {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String contents;
    private String subDescription;
    private Integer views;

    @OneToOne
    @JoinColumn(name = "user_no")
    private User user;

    @OneToOne
    @JoinColumn(name = "category_no")
    private BoardCategory category;



    @Column(updatable = false)
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;


    @PrePersist
    private void prePersist() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }


}
