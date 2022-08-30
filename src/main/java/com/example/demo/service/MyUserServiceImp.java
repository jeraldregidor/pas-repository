package com.example.demo.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Policy;
import com.example.demo.model.PolicyClaim;
import com.example.demo.model.PolicyHolder;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Vehicle;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class MyUserServiceImp implements MyUserService{
    @Autowired
    RatingEngine ratingEngine;

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PolicyRepository policyRepo;
    private final ClaimRepository claimRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        Random random = new Random();
        user.setAccountNumber(String.format("%04d", random.nextInt(10000)));
            boolean randomLoop = true;
            while(randomLoop){
                user.setAccountNumber(String.format("%04d", random.nextInt(10000)));  
                Optional<User> userByAccountNumber = Optional.ofNullable(userRepo.findByAccountNumber(user.getAccountNumber()));
                if(userByAccountNumber.isPresent()){
                    randomLoop = true;
                }
                else{
                    randomLoop = false;
                }   
            }
            log.info("Saving new user {} {} to database", user.getFirstName(), user.getLastName());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.getRoles().add(roleRepo.findByName("USER"));
            return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to database",role.getName());
        return roleRepo.save(role);
    }

    @Override 
    public void addRole(String email, String roleName) {
        log.info("Adding role {} to user {}" , roleName, email);
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new IllegalStateException("Account number not exist");
        }
        Role role = roleRepo.findByName(roleName);
        if(role == null){
            throw new IllegalStateException("Role name not exist");
        }
        user.getRoles().add(role);
        
    }

    @Override
    public User getUser(String email) {
        log.info("Fetching user with email {}" , email);
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new IllegalStateException("Email not exist");
        }
        return user;
    }

    @Override
    public Page<User> getPaginatedUsers(int offSet, int pageSize) {
        log.info("Fetching All users");
        Page<User> paginatedUser = userRepo.findAll(PageRequest.of(offSet, pageSize).withSort(Sort.Direction.DESC, "id"));
        return paginatedUser;
    }

    @Override
    public Policy addPolicy(String email, String effectiveDate) {
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new IllegalStateException("Email not exist");
        }
        Policy policy = new Policy();
        Random random = new Random();
        policy.setEffectiveDate(effectiveDate);
        
        //TODO: LOGIC TO PREVENT SAME POLICY NUMBER
        policy.setPolicyNumber(String.format("%06d", random.nextInt(1000000)));
        policy.setExpirationDate((LocalDate.parse(effectiveDate).plusMonths(6)).toString());
        user.getPolicy().add(policy);
        
        return policy;
    }

    @Override
    public void addPolicyHolder(String policyNumber, PolicyHolder policyHolder) {
        Policy policy = policyRepo.findByPolicyNumber(policyNumber);
        if(policy == null){
            throw new IllegalStateException("Policy number not exist");
        }
        policy.setPolicyHolder(policyHolder);
    }

    @Override
    public Vehicle addVehicle(String policyNumber, Vehicle vehicle) {
        Policy policy = policyRepo.findByPolicyNumber(policyNumber);
        if(policy == null){
            throw new IllegalStateException("Policy number not exist");
        }
        policy.getVehicle().add(vehicle);
        ratingEngine.settlePremium(policy);
        return vehicle;
    }

    @Override
    public Policy cancelPolicy(String policyNumber, String updatedExpirationDate) {
        Policy policy = policyRepo.findByPolicyNumber(policyNumber);
        if(policy == null){
            throw new IllegalStateException("Policy number not exist");
        }
        policy.setExpirationDate(updatedExpirationDate);
        return policy;
    }

    @Override
    public PolicyClaim filePolicyClaim(String policyNumber, PolicyClaim policyClaim) {
        Random random = new Random();
        
        //TODO: LOGIC TO PREVENT SAME CLAIM NUMBER
        policyClaim.setClaimNumber(String.format("C%05d", random.nextInt(100000)));
        Policy policy = policyRepo.findByPolicyNumber(policyNumber);
        if(policy == null){
            throw new IllegalStateException("Policy number not exist");
        }
        policy.setPolicyClaim(policyClaim);
        return policy.getPolicyClaim();
    }

    @Override
    public User getUserByAccountNumber(String accountNumber) {
        User user  = userRepo.findByAccountNumber(accountNumber);
        if(user == null){
            throw new IllegalStateException("Account number not exist");
        }
        return user;
    }

    @Override
    public Policy getPolicyByPolicyNumber(String policyNumber) {
        Policy policy = policyRepo.findByPolicyNumber(policyNumber);
        if(policy == null){
            throw new IllegalStateException("Policy number not exist");
        }
        return policy;
    }

    @Override
    public PolicyClaim getClaimByClaimNumber(String claimNumber) {
        PolicyClaim policyClaim = claimRepo.findByClaimNumber(claimNumber);
        if(policyClaim == null){
            throw new IllegalStateException("Claim number not exist");
        }
        return policyClaim;
    }

    @Override
    public void deleteUser(String accountNumber) {
        userRepo.deleteByAccountNumber(accountNumber);
    }

}
