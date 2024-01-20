package com.spring.data.jpa.springdatajpa;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionOperations;

import com.spring.data.jpa.springdatajpa.entities.Address;
import com.spring.data.jpa.springdatajpa.repositories.AddressRepository;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

@SpringBootTest(classes = SpringDataJpaApplication.class)
public class AddressRepositoryTest {
    
    private @Autowired AddressRepository addressRepository;

    private @Autowired UserRepository userRepository;

    private @Autowired TransactionOperations transactionOperations;

    @BeforeEach
    public void setUp(){
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();
        Address address1 = Address.builder()
                    .country("Indonesian")
                    .city("Jakarta")
                    .province("DKI Jakarta")
                    .postalCode("00232")
                    .build();
        Address address2 = Address.builder()
                    .country("Rusian")
                    .city("Moscow")
                    .province("Moscow")
                    .postalCode("97574")
                    .build();
        Address address3 = Address.builder()
                    .country("Palestine")
                    .city("AL-Quds")
                    .province("Gaza")
                    .postalCode("11230")
                    .build();
        Address address4 = Address.builder()
                    .country("Yamen")
                    .city("Yamen")
                    .province("Yamen")
                    .postalCode("11203")
                    .build();
        this.addressRepository.saveAll(List.of(address1, address2, address3, address4));
    }

    @Test
    public void testExistsByCountry(){
        Boolean isIndonesianExist = this.addressRepository.existsByCountry("Indonesian");
        Boolean isRusianExist = this.addressRepository.existsByCountry("Rusian");
        Boolean isPalestineExist = this.addressRepository.existsByCountry("Palestine");
        Boolean isYamenExist = this.addressRepository.existsByCountry("Yamen");
        Boolean isIsraelExist = this.addressRepository.existsByCountry("Israel");
        Assertions.assertTrue((isIndonesianExist && isRusianExist && isPalestineExist && isYamenExist));

        Assertions.assertFalse(isIsraelExist);// this will be false because Israel isn't country
    }

    @Test
    public void deleteAddressFail(){
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.addressRepository.deleteByCountry("Rusian");
        });
    }

    @Test
    public void deleteAddressSuccess(){
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            this.addressRepository.deleteByCountry("Rusian");
        });
    }

    @Test
    public void testNamedQuery(){
        List<Address> addresses = this.addressRepository.getAddressUsingProvinceName("Jakarta");
        Assertions.assertNotNull(addresses.size());
    }
}
