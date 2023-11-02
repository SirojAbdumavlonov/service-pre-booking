package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.JoinRequest;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.ChangeableCompanyDetails;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.model.CompanyProfile;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.CompaniesResponse;
import com.example.preordering.service.CategoryService;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.ServicesService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final ServicesService servicesService;
    private final UserAdminService userAdminService;


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
            responses.add(new CompaniesResponse(c.getName(),
                    companyService.findServicesNamesOfCompany(c.getServicesId()),
                    c.getImage(), c.getId(), c.getCategory().getCategoryId()));
        }



        return ResponseEntity.ok(responses);
    }
    @GetMapping("/zoir_employer/company")
    public ResponseEntity<?> getCompanyPage(){
        String username = "zoir_employer";
        Company foundCompany =
                companyService.getCompanyByDirectorUsername(username);

            double rate =
                    companyService.countRate(foundCompany.getMastersId());

            Long successfulOrders =
                    companyService.countSuccessfulOrders(foundCompany.getMastersId());

//            List<Service> services =
//                    companyService.findServicesByTheirId(
//                            servicesService.getServicesIdOfCompany(foundCompany.getCompanyId()));
            List<String> services =
                    companyService.findServiceNamesByTheirId(
                            servicesService.getServicesIdOfCompany(foundCompany.getId())
                    );
        List<String> mastersUsernames =
                userAdminService.mastersUsernames(foundCompany.getMastersId());

            CompanyProfile companyProfile =
                    CompanyProfile.builder()
                            .id(foundCompany.getId())
                            .name(foundCompany.getName())
                            .description(foundCompany.getDescription())
                            .address(foundCompany.getAddress())
                            .directorName(foundCompany.getDirectorName())
                            .image(foundCompany.getImage())
                            .directorUsername(foundCompany.getDirectorUsername())
                            .username(foundCompany.getUsername())
                            .rate(rate)
                            .successfulOrders(successfulOrders)
                            .masters(mastersUsernames)
                            .services(services)
                            .lon(foundCompany.getLocation().getLon())
                            .lat(foundCompany.getLocation().getLat())
                            .phoneNumbers(foundCompany.getPhoneNumbers())
                            .build();

            return ResponseEntity.ok(companyProfile);

    }
    @GetMapping("/company/{companyUsername}")
    public ResponseEntity<?> findCompanyOfThisCategory(@PathVariable String companyUsername){
        Company foundCompany =
                companyService.findCompanyByItsUsername(companyUsername);
//        Company foundCompany =
//                companyService.findCompanyByCategoryId(categoryId,companyId);
        double rate =
                companyService.countRate(foundCompany.getMastersId());
        Long successfulOrders =
                companyService.countSuccessfulOrders(foundCompany.getMastersId());

//        List<Service> services =
//                companyService.findServicesByTheirId(servicesService.getServicesIdOfCompany(companyId));
        List<String> services =
                companyService.findServiceNamesByTheirId(
                        servicesService.getServicesIdOfCompany(foundCompany.getId())
                );
        List<String> mastersUsernames =
                userAdminService.mastersUsernames(foundCompany.getMastersId());


        CompanyProfile companyProfile =
                CompanyProfile.builder()
                .name(foundCompany.getName())
                .description(foundCompany.getDescription())
                .address(foundCompany.getAddress())
                .directorName(foundCompany.getDirectorName())
                        .image(foundCompany.getImage())
                        .rate(rate)
                        .successfulOrders(successfulOrders)
                        .masters(mastersUsernames)
                        .services(services)
                        .lon(foundCompany.getLocation().getLon())
                        .lat(foundCompany.getLocation().getLat())
                .build();

        return ResponseEntity.ok(companyProfile);
    }
    @PostMapping("/{userId}/{companyId}")
    public ResponseEntity<?> sendRequestForJoiningCompany(@PathVariable Long userId,
                                                          @PathVariable Long companyId,
                                                          @RequestParam("sender") String sender){

        companyService.sendRequest(userId, companyId, sender);

        return ResponseEntity.ok("Request was sent successfully!");
    }
    @GetMapping("/{username}/company")
    public ResponseEntity<?> getRequestsForJoining(@PathVariable String username,
                                                   @RequestParam(name = "opts", required = false) String options){
        List<JoinRequest> joinRequests = new ArrayList<>();
        if(options == null) {
            Company company =
                    companyService.getCompanyByDirectorUsername(username);
            if(company == null){
                return ResponseEntity.ok(new ApiResponse("You don't have any company"));
            }
            return ResponseEntity.ok(company);
        }
        else if(options.equals("opts")){
            joinRequests =
                    companyService.getCompanyJoinRequests(username);
        }
        return ResponseEntity.ok(joinRequests);
    }
    @PutMapping("/{companyId}/{employeeId}")
    public ResponseEntity<?> acceptOrDeclineRequest(@PathVariable Long companyId,
                                                    @PathVariable Long employeeId,
                                                    @RequestParam(name = "answ") String answer){//accept or decline

        if(answer.equals("accept")){
            companyService.acceptRequest(companyId, employeeId);
            return ResponseEntity.ok("Accepted!");
        }
        companyService.declineRequest(companyId, employeeId);
        return null;
    }
    @PutMapping("/company/{companyId}")
    public ResponseEntity<?> changeCompanyDetails(@PathVariable Long companyId,
                                                  @RequestBody(required = false) ChangeableCompanyDetails details,
                                                  @RequestParam(name = "opt") String option){


        if(option.equals("info")){
            companyService.changeCompanyInfo(companyId, details.getCompanyInfo());
        }
        else if(option.equals("detail") && details != null) {
            companyService.changeCompanyDetails(companyId, details);
        }
        return ResponseEntity.ok("Changed successfully!");
    }


}
