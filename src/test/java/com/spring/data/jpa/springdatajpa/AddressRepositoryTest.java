package com.spring.data.jpa.springdatajpa;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.support.TransactionOperations;

import com.spring.data.jpa.springdatajpa.entities.Address;
import com.spring.data.jpa.springdatajpa.repositories.AddressRepository;
import com.spring.data.jpa.springdatajpa.repositories.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
    
    @Test
    public void testQueryAnnotationNative(){
        List<Address> addresses = this.addressRepository.getAllAddress();
        Assertions.assertNotNull(addresses.size());
        Assertions.assertSame(4, addresses.size());
    }

    @Test
    public void testQueryAnnotationWithPageResult(){
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.desc("id")));
        Page<Address> addressPage = this.addressRepository.getAllAddressUsingProvince("DKI Jakarta", pageable);
        Assertions.assertFalse(addressPage.getContent().isEmpty());
        Assertions.assertEquals(1, addressPage.getTotalElements());
        Assertions.assertEquals(1, addressPage.getTotalPages());
    }

    @Test
    public void testQueryAnnotation(){
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Order.desc("id")));
        List<Address> addressList = this.addressRepository.getAddressUsingCountry("Indonesian", pageable);
        Assertions.assertNotNull(addressList.size());
        Assertions.assertSame(1, addressList.size());
    }

    @Test
    public void testModifyigAnnotation(){
        List<Address> addresses = this.addressRepository.findAll();
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            
            Integer impactRows = this.addressRepository.updateCountryName(addresses.get(0).getId(), "Malaysia");
            Assertions.assertEquals(1, impactRows);

            impactRows = this.addressRepository.deleteAddressUsingId(10L);
            Assertions.assertEquals(0, impactRows);

            impactRows = this.addressRepository.deleteAddressUsingId(addresses.get(0).getId());
            Assertions.assertEquals(1, impactRows);
        });
    }

    @Test
    public void testStream(){
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            Supplier<Stream<Address>> addressSuplier = () -> this.addressRepository.streamByCountry("Indonesian");
            Assertions.assertEquals(1, addressSuplier.get().count());
            List<String> province = addressSuplier.get().map(c -> c.getProvince()).collect(Collectors.toList());
            Assertions.assertEquals("DKI Jakarta", province.getFirst());
        });
    }

    @Test
    public void testPesimisticLocing1(){
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            try {
                Address address = this.addressRepository.findById(getId()).get();
                address.setProvince("Maluku");
                Thread.sleep(15_000L);
                this.addressRepository.save(address);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * method ini akan dipanggil setelah method testPesimisticLocking1 selesai
     */
    @Test
    public void testPesimisticLocing2(){
        this.transactionOperations.executeWithoutResult(transactionStatus -> {
            Address address = this.addressRepository.findById(getId()).get();
            address.setProvince("Aceh");
            this.addressRepository.save(address);
        });
    }

    private Long getId() {
        return this.addressRepository.findAll().get(0).getId();
    }

    @Test
    public void testAudit(){
        Address address = this.addressRepository.findAll().get(0);
        address.setProvince("Maluku");
        address.setCity("Ambon");
        Address result = this.addressRepository.save(address);
        Assertions.assertNotNull(result.getCreatedAt());
    }

    @Test
    public void exampleQuery(){
        Address address = Address.builder()
                    .country("Indonesian")
                    .province("DKI Jakarta")
                    .build();
        Example<Address> example = Example.of(address);
        List<Address> result = this.addressRepository.findAll(example);

        Assertions.assertNotNull(result.size());
    }

    @Test
    public void testExampleQuery2(){
        Address address = Address.builder()
                    .country("Rusian")
                    .city("Moscow")
                    .build();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues();
        Example<Address> example = Example.of(address, exampleMatcher);
        List<Address> addresses = this.addressRepository.findAll(example);
        Assertions.assertNotNull(addresses.size());
    }

    @Test
    public void testCriteriaQuery(){
        // SELECT a FROM Address AS a WHERE a.country = ? OR a.province = ? ORDER BY a.country ASC;
        // with lambda expression
        Specification<Address> specification = (root, cirteriaQuery, criteriaBuilder) -> {
            return cirteriaQuery.where(
                criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("country"), "Rusian"),
                    criteriaBuilder.equal(root.get("province"), "Moscow")
                )
            ).orderBy(criteriaBuilder.asc(root.get("country"))).getRestriction();
        };
        List<Address> addresses = this.addressRepository.findAll(specification);
        Assertions.assertEquals(1, addresses.size());
        

        // without lambda expresioan
        Specification<Address> spec = new Specification<Address>() {
            @Override
            public Predicate toPredicate(Root<Address> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(
                    criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("country"), "Indonesian"),
                        criteriaBuilder.equal(root.get("province"), "Maluku")
                    )
                ).orderBy(criteriaBuilder.desc(root.get("country"))).getRestriction();
            }
        };
        List<Address> addressList = this.addressRepository.findAll(spec);
        Assertions.assertNotNull(addressList.size());
        Assertions.assertEquals(1, addressList.size());
    }
}
