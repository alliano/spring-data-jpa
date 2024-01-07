package com.spring.data.jpa.springdatajpa.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.data.jpa.springdatajpa.entities.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> { }