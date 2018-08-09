package com.woolta.blog.repository;

import com.woolta.blog.domain.PostFile;
import org.springframework.data.repository.CrudRepository;

public interface PostFileRepository extends CrudRepository<PostFile, Integer> {
}
