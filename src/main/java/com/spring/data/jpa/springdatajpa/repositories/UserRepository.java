package com.spring.data.jpa.springdatajpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.data.jpa.springdatajpa.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
    
    public Optional<User> findFirstByUsernameEquals(String username);

    public Optional<User> findFirstByUsername(String username);

    public List<User> findByUsernameNotLike(String username);

    public Optional<User> findByUsernameContaining(String key);

    public Optional<User> findByUsernameAndPassword(String username, String password);

    /**
     * SELECT * FROM users LEFT JOIN addresses where address.name = ?
     * @param country
     * @return
     */
    public List<User> findAllUserByAddress_CountryEquals(String country);

    // Select * from users as u left join addresses as a on (u.addressId = a.id) where a.country = ? and a.city = ?;
    public List<User> findAllUserByAddress_CountryEqualsAndAddress_CityEquals(String country, String city);

    public Optional<User> findFirstUserByAddress_ProvinceEquals(String province);

}