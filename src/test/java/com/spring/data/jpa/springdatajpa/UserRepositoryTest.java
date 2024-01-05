package com.spring.data.jpa.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.data.jpa.springdatajpa.entities.User;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserRepositoryTest {
    
    private @Autowired UserRepository userRepository;

    @Test
    public void testInsert(){
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret_pass")
                    .build();
        this.userRepository.save(user);
    }
}
