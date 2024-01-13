package com.spring.data.jpa.springdatajpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Entity @Table(name = "addresses")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Address {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String country;
    
    @Column(length = 100, nullable = false)
    private String province;
    
    @Column(length = 100, nullable = false)
    private String city;
    
    @Column(length = 100, nullable = false, name = "postal_code")
    private String postalCode;

    @OneToOne(mappedBy = "address")
    private User user;
}
