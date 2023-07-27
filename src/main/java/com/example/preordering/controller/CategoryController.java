package com.example.preordering.controller;

import com.example.preordering.entity.Category;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CategoryRequest;
import com.example.preordering.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @PostMapping()
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.addCategory(categoryRequest.getTitle());

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
