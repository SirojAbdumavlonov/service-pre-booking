package com.example.preordering.controller;

import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.model.OrderTime;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.ServiceRequest;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ServiceController {

    private final CompanyService companyService;
    private final ServicesService servicesService;
    @PostMapping("/categories/{categoryId}/companies/{companyId}/services")
    public ResponseEntity<?> addServiceToCompany(@RequestBody ServiceRequest serviceRequest,
                                                 @PathVariable Long categoryId,
                                                 @PathVariable Long companyId){
        Company company =
                companyService.findCompany(categoryId, companyId);

        servicesService.addServiceToCompany(serviceRequest, company);

        return ResponseEntity.ok(new ApiResponse("saved successfully"));
    }
    @GetMapping("/categories/{categoryId}/companies/{companyId}/services/{serviceId}")
    public ResponseEntity<?> getService(@PathVariable Long companyId,
                                        @PathVariable Long serviceId,
                                        @RequestParam(value = "date", required = false) String date){

        Service service =
                servicesService.findServiceByCompanyIdAndServiceId(serviceId, companyId);
        String defaultMasterUsername =
                service.getUsernames().get(0);
        System.out.println(defaultMasterUsername);
        List<OrderTime> availableTime =
                servicesService.availableTimeOfUserAdminOrMaster(defaultMasterUsername, service, date);
        return ResponseEntity.ok(availableTime);
    }
}
