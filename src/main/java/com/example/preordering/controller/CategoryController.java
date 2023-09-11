package com.example.preordering.controller;

import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;
import com.example.preordering.model.MainPage;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CategoryRequest;
import com.example.preordering.service.CategoryService;
import com.example.preordering.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CompanyService companyService;

    @GetMapping()
    public ResponseEntity<?> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "page_size", required = false, defaultValue = "15") int pageSize,
            @RequestParam(name = "categ", required = false, defaultValue = "cars") String cars
//            @RequestParam(name = "profile", required = false) String employeeName
    ){

        List<Category> categoryList =
                categoryService.getAllCategories();

        List<Company> companies = companyService.
                findAllCompaniesByParams(page, pageSize, cars);

        return ResponseEntity.ok(new MainPage(categoryList, companies));
    }
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest){
//                                         @RequestParam MultipartFile multipartFile){
        Category category = categoryService.addCategory(categoryRequest.getTitle());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(category.getCategoryId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse("category saved successfully!"));
    }
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok(new ApiResponse("category successfully deleted!"));
    }

}