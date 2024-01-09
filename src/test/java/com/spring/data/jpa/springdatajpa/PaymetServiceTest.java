package com.spring.data.jpa.springdatajpa;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.data.jpa.springdatajpa.entities.Payment;
import com.spring.data.jpa.springdatajpa.repositories.PaymentRepository;
import com.spring.data.jpa.springdatajpa.services.PaymentService;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class PaymetServiceTest {
    
    private @Autowired PaymentService paymentService;

    private @Autowired PaymentRepository paymentRepository;

    @Test
    public void testPlatformTransactionMaager(){
        this.paymentService.manualTransaction();
    }

    @Test
    public void testFindReciver(){
        Payment payment1 = Payment.builder()
                    .reciver("Abdillah")
                    .amount(10.000d)
                    .date(new Date())
                    .build();
        Payment payment2 = Payment.builder()
                    .reciver("Asta")
                    .amount(10.000d)
                    .date(new Date())
                    .build();
        Payment payment3 = Payment.builder()
                    .reciver("Alli")
                    .amount(10.000d)
                    .date(new Date())
                    .build();
        this.paymentRepository.saveAll(List.of(payment1, payment2, payment3));

        List<Payment> reciver = this.paymentService.findByreciver("Abdillah");
        Assertions.assertTrue(!reciver.isEmpty());
    }
}
