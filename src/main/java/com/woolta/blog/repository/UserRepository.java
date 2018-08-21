package com.woolta.blog.repository;

import com.woolta.blog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer>{

    Optional<User> findByUserId(String id);

}
