package com.example.preordering.controller;

import com.example.preordering.entity.Service;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.OrderRequest;
import com.example.preordering.service.OrderService;
import com.example.preordering.service.ServicesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
    private final ServicesService servicesService;
    private final OrderService orderService;
    @PostMapping("/categories/{categoryId}/companies/{companyId}/services/{serviceId}")
    public ResponseEntity<?> bookAnOrder(@PathVariable Long serviceId,
                                         @PathVariable Long companyId,
                                         @RequestBody OrderRequest orderRequest,
                                         @NonNull HttpServletRequest request){
        Service service =
                servicesService.findServiceByCompanyIdAndServiceId(serviceId, companyId);
        orderService.bookAnOrder(service, orderRequest, request);

        return ResponseEntity.ok(new ApiResponse("order successfully booked!"));
    }

}
