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
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    @Column(nullable = false)
    private String make;
    private String model, type,fuelType, color; 
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private double purchasePrice, eachPremium;

    public Vehicle(String make, String model, String type, String fuelType, String color, String year,
            double purchasePrice) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.fuelType = fuelType;
        this.color = color;
        this.year = year;
        this.purchasePrice = purchasePrice;
    } 
    
}
