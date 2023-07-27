package com.example.preordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserAdminController {
    @GetMapping("/{username}")
    public ResponseEntity<?> mainPageOfUserAdminOrMaster(@PathVariable String username){
        return null;
    }


}
