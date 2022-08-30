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
public class PolicyHolder {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    @Column(nullable = false)
    private String firstName;
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    private String dob;
    private String address;
    @Column(nullable = false)
    private String drivLicNum;
    @Column(nullable = false)
    private String drivLicIssueDate;

    public PolicyHolder(String firstName, String middleName, String lastName, String dob, String address,
        String drivLicNum, String drivLicIssueDate) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.dob = dob;
            this.address = address;
            this.drivLicNum = drivLicNum;
            this.drivLicIssueDate = drivLicIssueDate;
    }
}
