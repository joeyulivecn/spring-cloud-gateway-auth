package com.love.plus.authservice;

import com.love.plus.authservice.security.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

//    @PostMapping("/auth/signin")
//    public ResponseEntity<String> login(){
//        return ResponseEntity.ok("token");
//    }
//
//    @PostMapping("/auth/authorize")
//    public ResponseEntity<String> authorize(){
//        return ResponseEntity.ok("allow access");
//    }

    @Bean
    JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }

}
