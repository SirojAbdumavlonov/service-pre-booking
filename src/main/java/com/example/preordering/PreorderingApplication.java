package com.example.preordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = {"http://localhost:5173"})
@SpringBootApplication
@EnableCaching
public class PreorderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreorderingApplication.class, args);
    }


}
