package com.spring.data.jpa.springdatajpa.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    // menggunakan JPAQl
    @Query(name = "getAddressCountry",nativeQuery = false, value = "SELECT a FROM Address AS a WHERE a.country LIKE :country")
    public List<Address> getAddressUsingCountry(@Param(value = "country")String country, Pageable pageable);

    // menggunakan native query
    @Query(name = "getAllAddress", nativeQuery = true, value = "SELECT * FROM addresses")
    public List<Address> getAllAddress();

    @Query(
        name = "getAllAddressUsingProvice", 
        nativeQuery = false, value = "SELECT a FROM Address AS a WHERE a.province = :province", 
        countQuery = "SELECT COUNT(a) FROM Address AS a WHERE a.province = :province")
    public Page<Address> getAllAddressUsingProvince(@Param(value = "province")String province, Pageable pageable);

    @Modifying
    @Query(name = "deleteAddressUsingId", nativeQuery = false, value = "DELETE FROM Address AS a WHERE a.id = :id")
    public Integer deleteAddressUsingId(@Param(value = "id") Long id);

    @Modifying
    @Query(name = "updateCountryName", nativeQuery = false, value = "UPDATE Address AS a SET a.country = :country WHERE a.id = :id")
    public Integer updateCountryName(@Param(value = "id") Long id, @Param(value = "country") String country);

    public Stream<Address> streamByCountry(String country);
}
