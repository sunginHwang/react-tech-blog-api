package com.woolta.blog.repository;

import com.woolta.blog.domain.BoardCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardCategoryRepository extends CrudRepository<BoardCategory, Integer> {

    @Override
    List<BoardCategory> findAll();
}
