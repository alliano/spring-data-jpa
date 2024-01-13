package com.spring.data.jpa.springdatajpa.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.data.jpa.springdatajpa.entities.Payment;
import com.spring.data.jpa.springdatajpa.models.PaymentRequest;
import com.spring.data.jpa.springdatajpa.repositories.PaymentRepository;

import lombok.AllArgsConstructor;

@Service @AllArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;

    private final ObjectMapper objectMapper;

    private final PlatformTransactionManager platformTransactionManager;
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void tranfer(PaymentRequest request) throws JsonMappingException, JsonProcessingException {
        Payment payment = this.objectMapper.readValue(this.objectMapper.writeValueAsString(request), Payment.class);
        payment.setDate(new Date());
        this.paymentRepository.save(payment);
    }

    public void manualTransaction() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setTimeout(5);
        definition.setReadOnly(false);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = this.platformTransactionManager.getTransaction(definition);

        try {
            Payment payment = Payment.builder()
                            .reciver("Abdillah")
                            .amount(40.000d)
                            .date(new Date())
                            .build();
            this.paymentRepository.save(payment);
            error();
            this.platformTransactionManager.commit(transaction);
        } catch (Exception e) {
            this.platformTransactionManager.rollback(transaction);
        }
    }

    public void error() throws SQLException {
        throw new SQLException("Something error");
    }

    @Transactional(readOnly = true)
    public List<Payment> findByreciver(String reciver) {
        return this.paymentRepository.findAllByReciverOrderByDateDesc(reciver);
    }
}
