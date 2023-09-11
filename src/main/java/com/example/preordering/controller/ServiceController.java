package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.NewServiceRequest;
import com.example.preordering.model.OrderTime;
import com.example.preordering.model.ServiceProfile;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.ServiceRequest;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.ServicesService;
import com.example.preordering.utils.DaysGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceController {

    private final CompanyService companyService;
    private final ServicesService servicesService;
    @PostMapping("/{username}/company")
    public ResponseEntity<?> addServiceToCompany(@RequestBody ServiceRequest serviceRequest,
                                                 @PathVariable String username){
        Company company =
                companyService.findCompany(serviceRequest.getCategoryName(), username);

        if (serviceRequest.getUsernameOfMasters().isEmpty()){
            throw new BadRequestException("Please, add minimum 1 employee!");
        }
        if (servicesService.doesServiceWithThisTitleExist(serviceRequest.getTitle(), company.getCompanyId())) {
            throw new BadRequestException("Company has already had this type of service");
        }
        for(String masterUsername: serviceRequest.getUsernameOfMasters()) {
            if (!companyService.ifSuchMasterExists(masterUsername, company.getCompanyId())){
                throw new BadRequestException("Company does not have such employee");
            }
        }
        servicesService.addServiceToCompany(serviceRequest, company);

        return ResponseEntity.ok(new ApiResponse("Saved successfully"));
    }
    @PostMapping("/{username}/services")
    public ResponseEntity<?> addService(@PathVariable String username,
                                        @RequestBody NewServiceRequest serviceRequest){


        servicesService.addService(serviceRequest, username);

        return ResponseEntity.ok(new ApiResponse("Successfully added!"));
    }

    @GetMapping("/categories/{categoryId}/services")
    public ResponseEntity<?> getAllServices(@PathVariable Long categoryId){

        return ResponseEntity.ok(servicesService.getAllServicesOfCategory(categoryId));
    }

    @GetMapping("/categories/{categoryId}/companies/{companyId}/services/{serviceId}")
    public ResponseEntity<?> getService(@PathVariable Long companyId,
                             @PathVariable Long serviceId,
                             @RequestParam(value = "date", required = false) String date){

        Service service =
                servicesService.findServiceByCompanyIdAndServiceId(serviceId, companyId);
        String defaultMasterUsername =
                service.getUsernamesOfEmployees().get(0);
        System.out.println(defaultMasterUsername);
        List<OrderTime> availableTime =
                servicesService.availableTimeOfUserAdminOrMaster(defaultMasterUsername, service, date);
        ServiceProfile serviceProfile =
                ServiceProfile.builder()
                        .mastersUsernames(service.getUsernamesOfEmployees())
                        .occupationName(service.getOccupationName())
                        .price(service.getPrice())
                        .chosenDate(DaysGenerator.chosenDate(date))
                        .orderTimes(availableTime)
                        .chosenMaster(defaultMasterUsername)
                        .build();
        return ResponseEntity.ok(serviceProfile);

    }
}
