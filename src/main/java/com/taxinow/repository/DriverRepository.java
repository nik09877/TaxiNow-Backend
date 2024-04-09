package com.taxinow.repository;

import com.taxinow.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
    public Driver findByEmail(String email);

}
