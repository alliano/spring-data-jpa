package com.spring.data.jpa.springdatajpa.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class CustomErrorPageResponseController implements ErrorController {
    
    @RequestMapping("/error") @SneakyThrows
    public ResponseEntity<?> requestMethodName(HttpServletRequest httpServletRequest) {
        Integer statusCode = (Integer) httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = (String) httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        File file = ResourceUtils.getFile("classpath:page/customErrorPage.html");
        String rawString = new String(Files.readAllBytes(Path.of(file.getPath())));
        String html = rawString.replace("$statusCode", statusCode.toString()).replace("$errorMessage", errorMessage);
        return ResponseEntity.status(statusCode).body(html);
    }
    
}
