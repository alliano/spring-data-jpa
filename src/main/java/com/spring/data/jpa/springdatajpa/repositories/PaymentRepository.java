package com.spring.data.jpa.springdatajpa.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.data.jpa.springdatajpa.entities.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> { 

    // menampilkan semua reciver berdasarkan nama reciver
    public Optional<Payment> findByReciver(String reciver);

    // menampilkan amound yang lebih besar
    public List<Payment> findByAmountGreaterThan(Double amount);

    // menampilkan data berdasarkan nama reciver dan diurutkan DESC
    public List<Payment> findAllByReciverOrderByDateDesc(String reciver);

    public List<Payment> findAll(Sort sort);

    public List<Payment> findAllByReciverOrderByDateAsc(String reciver);

    public List<Payment> findAllByReciver(String reciver, Pageable pageable);

    public Page<Payment> findAll(Pageable pageable);


}