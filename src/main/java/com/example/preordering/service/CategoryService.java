package com.example.preordering.service;

import com.example.preordering.entity.Category;
import com.example.preordering.repository.CategoryRepository;
import com.example.preordering.repository.ClientRepository;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public Category addCategory(String title){
        Category category = Category.builder()
                .title(title)
                .build();
        return categoryRepository.save(category);
    }
    public void deleteCategory(Long categoryId){
         categoryRepository.deleteByCategoryId(categoryId);
    }




}
