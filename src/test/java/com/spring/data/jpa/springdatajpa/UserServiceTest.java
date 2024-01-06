package com.spring.data.jpa.springdatajpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import com.spring.data.jpa.springdatajpa.services.UserService;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserServiceTest {
    
    private @Autowired UserService userService;

    @Test
    public void testIsert(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            this.userService.create();
        });
    }

    @Test
    public void testInsertFail(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            this.userService.call();
        });
    }

    @Test
    public void testTransactionPropagationNever(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            this.userService.add();
        });
    }
}
