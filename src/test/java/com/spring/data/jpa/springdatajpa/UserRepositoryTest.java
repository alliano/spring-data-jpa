package com.spring.data.jpa.springdatajpa;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.data.jpa.springdatajpa.entities.Address;
import com.spring.data.jpa.springdatajpa.entities.User;
import com.spring.data.jpa.springdatajpa.repositories.AddressRepository;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserRepositoryTest {
    
    private @Autowired UserRepository userRepository;

    private @Autowired AddressRepository addressRepository;

    @BeforeEach
    public void setUp(){
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();;
        this.userRepository.deleteAll();
        Address address1 = Address.builder()
                    .country("Indonesian")
                    .city("Jakarta")
                    .province("DKI Jakarta")
                    .postalCode("94502")
                    .build();
        Address address2 = Address.builder()
                    .country("Indonesian")
                    .city("Jakarta")
                    .province("DKI Jakarta")
                    .postalCode("94502")
                    .build();
        Address address3 = Address.builder()
                    .country("Rusia")
                    .city("Moscow")
                    .province("Moscow")
                    .postalCode("3302")
                    .build();
        this.addressRepository.saveAll(List.of(address1, address2, address3));
        User user1 = User.builder()
                    .username("Abdillah")
                    .password("secret")
                    .address(address1)
                    .build();
        User user2 = User.builder()
                    .username("Azahra")
                    .password("secret")
                    .address(address2)
                    .build();
        User user3 = User.builder()
                    .username("Alli")
                    .password("secret")
                    .address(address3)
                    .build();
        this.userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    public void testInsert(){
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret_pass")
                    .build();
        this.userRepository.save(user);
    }

    @Test
    public void testEmbeded(){
        List<User> userByCountryAndAddress = this.userRepository.findAllUserByAddress_CountryEqualsAndAddress_CityEquals("Rusia", "Moscow");
        Assertions.assertTrue(!userByCountryAndAddress.isEmpty());
        Assertions.assertNotNull(userByCountryAndAddress);
    }
}
