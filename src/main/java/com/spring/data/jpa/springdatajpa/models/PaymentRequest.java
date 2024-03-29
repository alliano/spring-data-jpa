package com.spring.data.jpa.springdatajpa.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class PaymentRequest implements Serializable {
    
    private String id;

    private String reciver;

    private Double amount;
}
