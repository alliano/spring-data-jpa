package com.spring.data.jpa.springdatajpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class SpringDataJpaApplicationTests {

	private @Autowired EntityManagerFactory entityManagerFactory;

	@Test
	public void testEntityManager(){
		Assertions.assertNotNull(entityManagerFactory);
		EntityManager entityManager = this.entityManagerFactory.createEntityManager();
		Assertions.assertNotNull(entityManager);
		EntityTransaction transaction = entityManager.getTransaction();
		Assertions.assertNotNull(transaction);
		entityManager.close();
	}
}
