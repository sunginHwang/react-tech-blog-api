package com.woolta.blog.repository;

import com.woolta.blog.domain.BoardCategory;
import org.springframework.data.repository.CrudRepository;

public interface BoardCategoryRepository extends CrudRepository<BoardCategory, Integer> {
}
