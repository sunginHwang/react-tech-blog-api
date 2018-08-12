package com.woolta.blog.service;


import com.woolta.blog.controller.PostDto;
import com.woolta.blog.domain.BoardCategory;
import com.woolta.blog.repository.BoardCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service(value = "categoryService")
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final BoardCategoryRepository categoryRepository;

    public List<PostDto.CategoriesRes> getBoardCategories() {

        List<PostDto.CategoriesRes> CategoriesRes = new LinkedList<>();

        List<BoardCategory> boardCategories = categoryRepository.findAll();

        boardCategories.forEach(category ->
                CategoriesRes.add(
                        PostDto.CategoriesRes.builder()
                                .value(category.getNo())
                                .label(category.getCategoryName())
                                .build()
                )
        );

        return CategoriesRes;
    }
}
