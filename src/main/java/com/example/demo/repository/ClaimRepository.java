package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PolicyClaim;

public interface ClaimRepository extends JpaRepository <PolicyClaim, Integer> {
    PolicyClaim findByClaimNumber(String claimNumber);
}
