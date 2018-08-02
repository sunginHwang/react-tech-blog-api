package com.woolta.blog.repository;

import com.woolta.blog.domain.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Integer>{
}
