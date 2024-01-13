package com.spring.data.jpa.springdatajpa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.data.jpa.springdatajpa.services.UserService;

import lombok.AllArgsConstructor;

@RestController @AllArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping(path = "/user")
    public ResponseEntity<?> get(){
        return ResponseEntity.ok().body(this.userService.getAllUserByCountry("Indonesian"));
    }
}
