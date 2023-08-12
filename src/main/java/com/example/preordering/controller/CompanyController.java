package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.model.CompanyProfile;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CompaniesResponse;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final JwtService jwtService;

    @PostMapping("/{categoryId}/companies")
    public ResponseEntity<?> addCompany(@RequestBody CompanyFilling company,
                                        @PathVariable Long categoryId,
                                        @NonNull HttpServletRequest request,
                                        @RequestParam MultipartFile multipartFile
    ){
        companyService.addCompany(company, categoryId,
                jwtService.getUsernameFromToken(request), multipartFile);

        return ResponseEntity.ok(new ApiResponse("Saved successfully"));
    }
    @GetMapping("/{categoryId}/companies")
    public ResponseEntity<?> findCompaniesOfCategory(@PathVariable Long categoryId){
        List<CompaniesResponse> responses = new ArrayList<>();
        List<Company> companies =
                companyService.findAllCompaniesOfCategory(categoryId);
        if (companies.isEmpty()){
            throw new BadRequestException("There is no company in this category");
        }

        for (Company c: companies){
            responses.add(new CompaniesResponse(c.getCompanyName(),
                    companyService.findServicesNamesOfCompany(c.getMastersId()),
                    c.getCompanyImageName(), c.getCompanyId(), c.getCategory().getCategoryId()));
        }



        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{categoryId}/companies/{companyId}")
    public ResponseEntity<?> findCompanyOfThisCategory(@PathVariable Long categoryId,
                                            @PathVariable Long companyId){
        Company foundCompany =
                companyService.findCompany(categoryId,companyId);

        List<Long> masterAndUserAdminOfCompany =
                companyService.findMastersOfCompany(foundCompany.getDirectorUsername(), foundCompany.getMastersId());

        double rate =
                companyService.countRate(masterAndUserAdminOfCompany);

        Long successfulOrders =
                companyService.countSuccessfulOrders(masterAndUserAdminOfCompany);

        List<String> userAdminsUsernames =
                companyService.findUsernamesOfUserAdmins(foundCompany.getMastersId());

        userAdminsUsernames.add(foundCompany.getDirectorUsername());

        List<Service> services =
                companyService.findServicesByTheirId(foundCompany.getServicesId());

        CompanyProfile companyProfile =
                CompanyProfile.builder()
                .companyName(foundCompany.getCompanyName())
                .description(foundCompany.getDescription())
                .address(foundCompany.getAddress())
                .directorName(foundCompany.getDirectorName())
                        .imageOfCompany(foundCompany.getCompanyImageName())
                        .rate(rate)
                        .successfulOrders(successfulOrders)
                        .masters(userAdminsUsernames)
                        .services(services)
                .build();


        return ResponseEntity.ok(companyProfile);
    }


}
