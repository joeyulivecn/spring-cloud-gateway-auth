package com.love.plus.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @GetMapping("/api/v1/m/order")
    public ResponseEntity<String> getMobileOrder() {
        return ResponseEntity.ok("mobile order");
    }

    @GetMapping("/api/v1/order")
    public ResponseEntity<String> getOrder() {
        return ResponseEntity.ok("admin order");
    }
}
