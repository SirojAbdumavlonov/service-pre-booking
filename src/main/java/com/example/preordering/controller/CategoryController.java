package com.example.preordering.controller;

import com.example.preordering.entity.Category;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CategoryRequest;
import com.example.preordering.service.CategoryService;
import com.example.preordering.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<?> getAllCategories(){

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/cate")
    public ResponseEntity<?> getAllCategoriesS(){

        return ResponseEntity.ok(categoryService.getAllCategoriesS());
    }
    @PostMapping()
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest,
                                         @RequestParam MultipartFile multipartFile){
        Category category = categoryService.addCategory(categoryRequest.getTitle(),
                multipartFile);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(category.getCategoryId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse("category saved successfully!"));
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok(new ApiResponse("category successfully deleted!"));
    }

}
