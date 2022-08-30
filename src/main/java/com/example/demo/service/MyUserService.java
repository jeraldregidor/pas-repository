package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.model.Policy;
import com.example.demo.model.PolicyClaim;
import com.example.demo.model.PolicyHolder;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Vehicle;

public interface MyUserService {
    User saveUser(User user); 
    Role saveRole(Role role);  
    void addRole(String email, String roleName); 
    User getUser(String email);
    Page<User>getPaginatedUsers(int pageSize, int offSet);
    Policy addPolicy(String email, String effectiveDate);
    void addPolicyHolder(String policyNumber, PolicyHolder policyHolder);
    Vehicle addVehicle(String policyNumber, Vehicle vehicle);
    Policy cancelPolicy(String policyNumber, String updatedExpirationDate);
    PolicyClaim filePolicyClaim(String policyNumber, PolicyClaim policyClaim);
    User getUserByAccountNumber(String accountNumber);
    Policy getPolicyByPolicyNumber(String policyNumber);
    PolicyClaim getClaimByClaimNumber(String claimNumber);
    void deleteUser(String accountNumber);
}
