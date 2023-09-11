package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.JoinRequest;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.model.CompanyProfile;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CompaniesResponse;
import com.example.preordering.service.CategoryService;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final ServicesService servicesService;


    @PreAuthorize("hasAnyRole('SUPER_ADMIN,ROLE_EMPLOYER')")
    @DeleteMapping("/{username}/company")
    public ResponseEntity<?> deleteCompany(@PathVariable String username){
        companyService.deleteCompany(username);

        return null;
    }


//    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @PostMapping("/categories/{categoryId}/companies")
    public ResponseEntity<?> addCompany(@RequestBody CompanyFilling company,
                                        @PathVariable Long categoryId
//                                        @RequestParam MultipartFile multipartFile
    ){
        if(companyService.ifThisDirectorEverCreated(company.getDirectorUsername())){
            throw new BadRequestException("You have already had company!");
        }
        if(!categoryService.doesCategoryExist(categoryId)){
            throw new BadRequestException("There is no such category");
        }
        companyService.addCompany(company, categoryId,
                company.getDirectorUsername());

        return ResponseEntity.ok(new ApiResponse("Saved successfully"));
    }
    @GetMapping("/categories/{categoryId}/companies")
    public ResponseEntity<?> findCompaniesOfCategory(@PathVariable Long categoryId){
        List<CompaniesResponse> responses = new ArrayList<>();
        List<Company> companies =
                companyService.findAllCompaniesOfCategory(categoryId);
        if (companies.isEmpty()){
            throw new BadRequestException("There is no company in this category");
        }

        for (Company c: companies){
            responses.add(new CompaniesResponse(c.getCompanyName(),
                    companyService.findServicesNamesOfCompany(c.getServicesId()),
                    c.getCompanyImageName(), c.getCompanyId(), c.getCategory().getCategoryId()));
        }



        return ResponseEntity.ok(responses);
    }
    @GetMapping("/zoir_employer/company")
    public ResponseEntity<?> getCompanyPage(){
        String username = "zoir_employer";
        Company foundCompany =
                companyService.getCompanyByDirectorUsername(username);
            double rate =
                    companyService.countRate(foundCompany.getMastersUsernames());
            Long successfulOrders =
                    companyService.countSuccessfulOrders(foundCompany.getMastersUsernames());

            List<Service> services =
                    companyService.findServicesByTheirId(servicesService.getServicesIdOfCompany(foundCompany.getCompanyId()));

            CompanyProfile companyProfile =
                    CompanyProfile.builder()
                            .companyName(foundCompany.getCompanyName())
                            .description(foundCompany.getDescription())
                            .address(foundCompany.getAddress())
                            .directorName(foundCompany.getDirectorName())
                            .imageOfCompany(foundCompany.getCompanyImageName())
                            .directorUsername(foundCompany.getDirectorUsername())
                            .companyUsername(foundCompany.getCompanyUsername())
                            .rate(rate)
                            .successfulOrders(successfulOrders)
                            .masters(foundCompany.getMastersUsernames())
                            .services(services)
                            .lon(foundCompany.getLocation().getLon())
                            .lat(foundCompany.getLocation().getLat())
                            .build();

            return ResponseEntity.ok(companyProfile);

    }
    @GetMapping("/categories/{categoryId}/companies/{companyId}")
    public ResponseEntity<?> findCompanyOfThisCategory(@PathVariable Long categoryId,
                                            @PathVariable Long companyId){
        Company foundCompany =
                companyService.findCompanyByCategoryId(categoryId,companyId);
        double rate =
                companyService.countRate(foundCompany.getMastersUsernames());
        Long successfulOrders =
                companyService.countSuccessfulOrders(foundCompany.getMastersUsernames());

        List<Service> services =
                companyService.findServicesByTheirId(servicesService.getServicesIdOfCompany(companyId));

        CompanyProfile companyProfile =
                CompanyProfile.builder()
                .companyName(foundCompany.getCompanyName())
                .description(foundCompany.getDescription())
                .address(foundCompany.getAddress())
                .directorName(foundCompany.getDirectorName())
                        .imageOfCompany(foundCompany.getCompanyImageName())
                        .rate(rate)
                        .successfulOrders(successfulOrders)
                        .masters(foundCompany.getMastersUsernames())
                        .services(services)
                        .lon(foundCompany.getLocation().getLon())
                        .lat(foundCompany.getLocation().getLat())
                .build();

        return ResponseEntity.ok(companyProfile);
    }
    @PostMapping("/{username}/{companyUsername}")
    public ResponseEntity<?> sendRequestForJoiningCompany(@PathVariable String username,
                                                          @PathVariable String companyUsername,
                                                          @RequestParam("sender") String sender){

        companyService.sendRequest(username, companyUsername, sender);

        return ResponseEntity.ok("Request was sent successfully!");
    }
    @GetMapping("/{username}/company")
    public ResponseEntity<?> getRequestsForJoining(@PathVariable String username){

        List<JoinRequest> joinRequests =
                companyService.getCompanyJoinRequests(username);

        return ResponseEntity.ok(joinRequests);
    }


}
