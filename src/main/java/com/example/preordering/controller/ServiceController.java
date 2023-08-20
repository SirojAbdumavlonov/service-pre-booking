package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
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
    @PostMapping("/category/{categoryId}/companies/{companyId}/services")
    public ResponseEntity<?> addServiceToCompany(@RequestBody ServiceRequest serviceRequest,
                                                 @PathVariable Long categoryId,
                                                 @PathVariable Long companyId){
        Company company =
                companyService.findCompany(categoryId, companyId);
        if (serviceRequest.getUsernameOfMasters().isEmpty()){
            throw new BadRequestException("Please, add minimum 1 employee!");
        }
        if (servicesService.doesServiceWithThisTitleExist(serviceRequest.getTitle())) {
            throw new BadRequestException("Company has already had this type of service");
        }
        servicesService.addServiceToCompany(serviceRequest, company);

        return ResponseEntity.ok(new ApiResponse("Saved successfully"));
    }
    @GetMapping("/category/{categoryId}/services")
    public ResponseEntity<?> getAllServices(@PathVariable Long categoryId){

        return ResponseEntity.ok(servicesService.getAllServicesOfCategory(categoryId));
    }
    @GetMapping("/category/{categoryId}/companies/{companyId}/services/{serviceId}")
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
