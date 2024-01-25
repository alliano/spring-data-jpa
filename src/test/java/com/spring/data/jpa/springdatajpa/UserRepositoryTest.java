package com.spring.data.jpa.springdatajpa;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;

import com.spring.data.jpa.springdatajpa.entities.Address;
import com.spring.data.jpa.springdatajpa.entities.User;
import com.spring.data.jpa.springdatajpa.models.UserDetailResponse;
import com.spring.data.jpa.springdatajpa.repositories.AddressRepository;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserRepositoryTest {
    
    private @Autowired UserRepository userRepository;

    private @Autowired AddressRepository addressRepository;

    private @Autowired TransactionOperations transactionOperations;

    @BeforeEach
    public void setUp(){
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();
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

    @Test
    public void testCount(){
        Long amountUserByAddress = this.userRepository.countByAddress_CountryEqualsAndAddress_ProvinceEquals("Indonesian", "DKI Jakarta");
        Assertions.assertEquals(2, amountUserByAddress);
    }

    @Test
    public void testProjection(){
        // lambda expression
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            Supplier<Stream<UserDetailResponse>> userSupplier = () -> this.userRepository.findAllUser();
            Assertions.assertNotNull(userSupplier.get().count());
        });
        
        // without lambda expression
        this.transactionOperations.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus arg0) {
                Supplier<Stream<UserDetailResponse>> userSupplier = new Supplier<Stream<UserDetailResponse>>() {
                    @Override
                    public Stream<UserDetailResponse> get() {
                        return userRepository.findAllUser();
                    }
                };
                Assertions.assertNotNull(userSupplier.get().count());
            }
        });
    }
}
