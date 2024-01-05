package com.spring.data.jpa.springdatajpa.repositories;


import com.spring.data.jpa.springdatajpa.models.Payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentRepository {

    private final EntityManagerFactory entityManagerFactory;

    public void save(Payment payment) {
       EntityManager entityManager = this.entityManagerFactory.createEntityManager();
       EntityTransaction transaction = entityManager.getTransaction();
       transaction.begin();
       entityManager.persist(payment);
       transaction.commit();
    }
}
