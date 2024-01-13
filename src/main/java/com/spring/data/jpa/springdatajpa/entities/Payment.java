package com.spring.data.jpa.springdatajpa.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "payments")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class Payment implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reciver;

    private Date date;

    private Double amount;
}
