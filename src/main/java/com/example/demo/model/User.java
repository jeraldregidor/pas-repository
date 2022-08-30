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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.repository.EmailValidator.UniqueEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity @Data @NoArgsConstructor @AllArgsConstructor 
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;
    @Column(nullable = false) @NotBlank(message = "The first name field is required")
    private String firstName;
    private String middleName;
    @Column(nullable = false) @NotBlank(message = "The last name field is required")
    private String lastName;
    @Column(nullable = false, unique = true) 
    @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @UniqueEmail(message = "Email is already taken")
    @NotBlank(message = "The email field is required")
    private String email;
    @Column(nullable = false) @NotBlank(message = "The password field is required")
    private String password;
    @Column(nullable = false, unique = true) 
    private String accountNumber;
    @Column(nullable = false) @NotBlank(message = "The address field is required")
    private String address;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Policy> policy = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private String token;

    @NonNull
    public User(String firstName, String middleName, String lastName, String email, String password,
    String address  ) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
    }
    

}

