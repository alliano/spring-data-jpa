package com.spring.data.jpa.springdatajpa.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.data.jpa.springdatajpa.entities.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> { 

    public Optional<Payment> findAllByReciver(String reciver);

    public List<Payment> findByAmountGreaterThan(Double amount);

    public List<Payment> findByReciverOrderByDateDesc(String reciver);

}