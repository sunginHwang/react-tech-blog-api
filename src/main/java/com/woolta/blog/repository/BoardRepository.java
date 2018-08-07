package com.woolta.blog.repository;

import com.woolta.blog.domain.Board;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends CrudRepository<Board, Integer>{
    Optional<List<Board>> findBycategoryNo(Integer categoryNo);
}
