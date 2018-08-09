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
@Table(name = "post_file")
public class PostFile {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String originFileName;
    private String fileName;
    private String fileDir;
    private String fileExt;
    private String fullPath;
    private long size;

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
