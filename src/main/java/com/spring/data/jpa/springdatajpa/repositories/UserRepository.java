package com.spring.data.jpa.springdatajpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.data.jpa.springdatajpa.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
    
    public Optional<User> findByUsernameEquals(String username);

    public Optional<User> findFirstByUsername(String username);

    public List<User> findByUsernameNotLike(String username);

    public Optional<User> findByUsernameContaining(String key);

    public Optional<User> findByUsernameAndPassword(String username, String password);

}
