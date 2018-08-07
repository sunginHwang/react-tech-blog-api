package com.woolta.blog.service;


import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.repository.BoardCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "categoryService")
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final BoardCategoryRepository categoryRepository;

    public List<BoardCategory> getBoardCategories(){
        return categoryRepository.findAll();
    }
}
