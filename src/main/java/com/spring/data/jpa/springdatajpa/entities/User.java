package com.spring.data.jpa.springdatajpa.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.spring.data.jpa.springdatajpa.listeners.BaseEntityListener;
import com.spring.data.jpa.springdatajpa.utils.BaseEntityListenerAware;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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

@EntityListeners(value = {
    BaseEntityListener.class
})
@Builder @Entity @Table(name = "users")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class User implements Serializable, BaseEntityListenerAware {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    private Address address;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
