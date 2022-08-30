package com.example.demo.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Policy {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    @Column(nullable = false)
    private String policyNumber;
    @Column(nullable = false)
    private String effectiveDate;
    @Column(nullable = false)
    private String expirationDate;
    @Column(nullable = false)
    private double premium;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PolicyHolder policyHolder;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PolicyClaim policyClaim;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Vehicle> vehicle = new ArrayList<>();

}
