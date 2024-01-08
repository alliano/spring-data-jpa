package com.spring.data.jpa.springdatajpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.spring.data.jpa.springdatajpa.entities.User;
import com.spring.data.jpa.springdatajpa.models.PaymentRequest;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;
import com.spring.data.jpa.springdatajpa.services.PaymentService;
import com.spring.data.jpa.springdatajpa.services.UserService;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserServiceTest {
    
    private @Autowired UserService userService;

    private @Autowired UserRepository userRepository;

    private @Autowired PaymentService paymentService;


    @BeforeEach
    public void setUp(){
        this.userRepository.deleteAll();
    }

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

    @Test
    public void testPropagation() throws JsonMappingException, JsonProcessingException{
        PaymentRequest paymentRequest = PaymentRequest.builder()
                    .amount(10.0000d)
                    .reciver("Alli")
                    .build();
        Assertions.assertDoesNotThrow(() -> {
            this.userService.userTranfer(paymentRequest);
        });
    }

    @Test
    public void testFailPropagation() throws JsonMappingException, JsonProcessingException {
        Assertions.assertThrows(IllegalTransactionStateException.class, () -> {
            PaymentRequest paymentRequest = PaymentRequest.builder()
                            .reciver("Nero")
                            .amount(30.000d)
                            .build();
            this.paymentService.tranfer(paymentRequest);
        });
    }

    @Test
    public void programmaticTransactionTest(){
        Assertions.assertThrows(RuntimeException.class, () -> userService.updateUser());
    }

    @Test
    public void testNamedQuery(){
        User save = User.builder()
                    .username("Abdillah")
                    .password("secret")
                    .build();
        this.userRepository.save(save);
        User user = this.userService.namedQuery("Abdillah");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("Abdillah", user.getUsername());
    }
}
