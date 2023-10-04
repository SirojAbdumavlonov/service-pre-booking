package com.example.preordering.controller;

import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.model.EmployeeView;
import com.example.preordering.model.MainPage;
import com.example.preordering.model.ServiceView;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CategoryRequest;
import com.example.preordering.service.CategoryService;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.ServicesService;
import com.example.preordering.service.UserAdminService;
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
    private final UserAdminService userAdminService;
    private final ServicesService servicesService;

    @GetMapping()
    public ResponseEntity<?> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "page_size", required = false, defaultValue = "15") int pageSize,
            @RequestParam(name = "categ", required = false, defaultValue = "cars") String cars,
            @RequestParam(name = "show",required = false,defaultValue = "company") String showOption,
            @RequestParam(name = "sort", required = false) String sortingType
//            @RequestParam(name = "profile", required = false) String employeeName
    ){

        List<Category> categoryList =
                categoryService.getAllCategories();
        if (showOption.equals("company")) {
            List<Company> companies = companyService.
                    findAllCompaniesByParams(page, pageSize, cars);
            return ResponseEntity.ok(new MainPage<Company>(categoryList, companies));
        }
        else if(showOption.equals("service")){
            List<ServiceView> serviceViews =
                servicesService.getServicesOfCompanies(categoryList.get(0).getCategoryId(), page, pageSize);
        }
        else if (showOption.equals("employee")){
            List<EmployeeView> getEmployees =
                    userAdminService.getEmployees(categoryList.get(0).getCategoryId());
            return ResponseEntity.ok(new MainPage<EmployeeView>(categoryList, getEmployees));

        }
        return null;
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