package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class PolicyClaim {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    @Column(nullable = false)
    private String claimNumber;
    private String addressOfAccident;
    private String descriptionOfAccident;
    private String descriptionOfDamage;
    @Column(nullable = false)
    private double estimatedCost;

    public PolicyClaim(String addressOfAccident, String descriptionOfAccident,
            String descriptionOfDamage, double estimatedCost) {
        this.addressOfAccident = addressOfAccident;
        this.descriptionOfAccident = descriptionOfAccident;
        this.descriptionOfDamage = descriptionOfDamage;
        this.estimatedCost = estimatedCost;
    }    
}