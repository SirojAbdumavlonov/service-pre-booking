package com.example.preordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@CrossOrigin(origins = {"http://localhost:3000"})
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PreorderingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreorderingApplication.class, args);
    }


}
