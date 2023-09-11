package com.example.preordering.controller;

import com.example.preordering.service.SearchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchingController {
    private final SearchingService service;

    @GetMapping("/search")
    public ResponseEntity<?> searchByParameters(
            @RequestParam(value = "search_size", required = false, defaultValue = "10") int searchSize,
            @RequestParam(value = "category", required = false, defaultValue = "cars") String category,
            @RequestParam(value = "search_type", required = false, defaultValue = "employee") String searchType,
            @RequestParam(value = "search_obj", required = false) String searchedObjectName){


        return ResponseEntity.ok(
                service.findByParameters(searchSize, category, searchType, searchedObjectName));
    }
}
