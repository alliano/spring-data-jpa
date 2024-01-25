package com.spring.data.jpa.springdatajpa.listeners;

import java.time.LocalDateTime;
import com.spring.data.jpa.springdatajpa.utils.BaseEntityListenerAware;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BaseEntityListener {
    
    @PrePersist
    public void setCreatedAt(BaseEntityListenerAware baseEntityListenerAware) {
        baseEntityListenerAware.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdatedAt(BaseEntityListenerAware baseEntityListenerAware) {
        baseEntityListenerAware.setUpdatedAt(LocalDateTime.now());
    }
}
