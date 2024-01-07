package com.spring.data.jpa.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.data.jpa.springdatajpa.services.PaymentService;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class PaymetServiceTest {
    
    private @Autowired PaymentService paymentService;

    @Test
    public void testPlatformTransactionMaager(){
        this.paymentService.manualTransaction();
    }
}
