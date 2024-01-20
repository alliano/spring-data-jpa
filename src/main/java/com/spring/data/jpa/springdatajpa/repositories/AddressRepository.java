package com.spring.data.jpa.springdatajpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;

import com.spring.data.jpa.springdatajpa.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    public Boolean existsByCountry(String country);

    // @Transactional(readOnly = false)
    public Integer deleteByCountry(String country);

    public List<Address> getAddressUsingProvinceName(@Param(value = "province") String province);

}
