package com.woolta.blog.repository;

import com.woolta.blog.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>{
}
