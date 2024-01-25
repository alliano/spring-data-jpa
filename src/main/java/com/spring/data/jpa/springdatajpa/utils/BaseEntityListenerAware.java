package com.spring.data.jpa.springdatajpa.utils;

import java.time.LocalDateTime;

public interface BaseEntityListenerAware {
    
    public void setUpdatedAt(LocalDateTime localDateTime);

    public void setCreatedAt(LocalDateTime localDateTime);

    public void setDeletedAt(LocalDateTime localDateTime);
}
