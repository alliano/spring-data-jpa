package com.spring.data.jpa.springdatajpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.spring.data.jpa.springdatajpa.entities.Payment;
import com.spring.data.jpa.springdatajpa.repositories.PaymentRepository;
import com.spring.data.jpa.springdatajpa.services.PaymentService;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class PaymetServiceTest {
    
    private @Autowired PaymentService paymentService;

    private @Autowired PaymentRepository paymentRepository;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");

    @BeforeEach
    public void setUp() throws ParseException{
        this.paymentRepository.deleteAll();

       
        Payment payment1 = Payment.builder()
                    .amount(10.000d)
                    .date(simpleDateFormat.parse("10-2-2024"))
                    .reciver("Abdillah")
                    .build();
        Payment payment2 = Payment.builder()
                    .amount(15.000d)
                    .date(simpleDateFormat.parse("2-5-2024"))
                    .reciver("Alli")
                    .build();
        Payment payment3 = Payment.builder()
                    .amount(16.000d)
                    .date(simpleDateFormat.parse("20-6-2025"))
                    .reciver("Alliano")
                    .build();
        Payment payment4 = Payment.builder()
                    .amount(30.000d)
                    .date(simpleDateFormat.parse("6-3-2023"))
                    .reciver("Allia")
                    .build();
        Payment payment5 = Payment.builder()
                    .amount(40.000d)
                    .date(simpleDateFormat.parse("11-2-2024"))
                    .reciver("Azahra")
                    .build();
        this.paymentRepository.saveAll(List.of(payment1, payment2, payment3, payment4, payment5));
    }

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

    @Test
    public void testSort(){
        Sort sort = Sort.by(Sort.Order.desc("date"));
        // select * from payments as p order by p.date desc;
        List<Payment> paymentList = this.paymentRepository.findAll(sort);
        Assertions.assertNotNull(!paymentList.isEmpty());
        Assertions.assertEquals("Alliano", paymentList.get(0).getReciver());
        Assertions.assertEquals("Azahra", paymentList.get(1).getReciver());
        Assertions.assertEquals("Abdillah", paymentList.get(2).getReciver());
    }

    @Test
    public void testPaging(){
        // halaman 1
        PageRequest page1 = PageRequest.of(0, 2, Sort.by(Sort.Order.desc("date")));
        List<Payment> paymentList = this.paymentRepository.findAll(page1).getContent();
        Assertions.assertEquals("Alliano", paymentList.get(0).getReciver());
        Assertions.assertEquals("Azahra", paymentList.get(1).getReciver());

        // halaman 2
        PageRequest page2 = PageRequest.of(1, 2, Sort.by(Sort.Order.desc("date")));
        paymentList = this.paymentRepository.findAll(page2).getContent();
        Assertions.assertEquals("Abdillah", paymentList.get(0).getReciver());
        Assertions.assertEquals("Alli", paymentList.get(1).getReciver());
    }
}
