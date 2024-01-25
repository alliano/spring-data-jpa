package com.spring.data.jpa.springdatajpa.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class UserDetailResponse {
    
    private String username;

    private String country;

    private String city;
}
