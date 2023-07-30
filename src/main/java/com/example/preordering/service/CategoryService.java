package com.example.preordering.service;

import com.example.preordering.entity.Category;
import com.example.preordering.repository.CategoryRepository;
import com.example.preordering.repository.ClientRepository;
import com.example.preordering.repository.UserAdminRepository;
import com.example.preordering.utils.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public Category addCategory(String title,
                                MultipartFile multipartFile){
        Category category = Category.builder()
                .title(title)
                .categoryImageName(title + "-" + multipartFile.getOriginalFilename())
                .build();
        Image.saveImage(multipartFile, Image.CATEGORY_IMAGE, title);
        return categoryRepository.save(category);
    }
    public void deleteCategory(Long categoryId){
         categoryRepository.deleteByCategoryId(categoryId);
    }




}
