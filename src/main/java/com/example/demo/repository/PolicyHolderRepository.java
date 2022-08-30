package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.PolicyHolder;

public interface PolicyHolderRepository extends JpaRepository <PolicyHolder, Integer> {
    PolicyHolder findByFirstName(String firstName);
}

