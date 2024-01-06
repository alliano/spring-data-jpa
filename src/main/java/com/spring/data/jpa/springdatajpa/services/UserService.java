package com.spring.data.jpa.springdatajpa.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.spring.data.jpa.springdatajpa.entities.User;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service @AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret")
                    .build();

        User user1 = User.builder()
                    .username("Alli")
                    .password("secret")
                    .build();
        User user2 = User.builder()
                    .username("Nabila")
                    .password("secret")
                    .build();
        List<User> userList = new ArrayList<>(List.of(user, user1, user2));
        userList.forEach(u -> {
            this.userRepository.save(u);
        });

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error");
    }

    public void call() {
        create();
    }

    @Transactional(propagation = Propagation.NEVER)
    public void add() {
        User user = User.builder()
                    .username("Asta")
                    .password("Noel Silvia")
                    .build();
        this.userRepository.save(user);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error");
    }
}
