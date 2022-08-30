package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.model.Policy;
import com.example.demo.model.PolicyClaim;
import com.example.demo.model.PolicyHolder;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Vehicle;
import com.example.demo.service.MyUserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*", allowedHeaders = "*") 
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final MyUserService userService;
    
    @GetMapping("/admin/users")
    public ResponseEntity<Page<User>>getUsers(@RequestParam int offSet, @RequestParam int pageSize){
        return ResponseEntity.ok().body(userService.getPaginatedUsers(offSet, pageSize)); 
    }

    @GetMapping("/user/email")
    public ResponseEntity<User>getUser(@RequestBody Map<String,String> map){
        User user = userService.getUser(map.get("email"));
        return ResponseEntity.ok().body(user); 
    }

    @PostMapping("/register")
    public ResponseEntity<Object>saveUser(@Valid @RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        Map<String, String> fields = new HashMap<>();
        userService.saveUser(user);
        fields.put("message", "successfull");
        fields.put("firstName", user.getFirstName());
        fields.put("middleName", user.getMiddleName());
        fields.put("lastName", user.getLastName());
        fields.put("email", user.getEmail());
        fields.put("address", user.getAddress());
        fields.put("accountNumber", user.getAccountNumber());
        return ResponseEntity.created(uri).body(fields); 
    }

    @PostMapping("/admin/rolesave")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role)); 
    }

    @PostMapping("/admin/addroletouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form){
        userService.addRole(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build(); 
    }

    @PostMapping("/policy/add")
    public ResponseEntity<Policy>addPolicyToUser(@RequestBody PolicyToUserForm form){
        Policy policy = userService.addPolicy(form.getEmail(), form.getEffectiveDate());
        return ResponseEntity.ok().body(policy); 
    }

    @PostMapping("/policyholder/add")
    public ResponseEntity<Object>addPolicyHolderToPolicy(@RequestBody PolicyHoldertoPolicyForm form){
        userService.addPolicyHolder(form.getPolicyNumber(), form.getPolicyHolder());
        return ResponseEntity.ok().body(form.getPolicyHolder()); 
    }

    @PostMapping("/vehicle/add")
    public ResponseEntity<Object>addVehicleToPolicy(@RequestBody VehicletoPolicyForm form){
        Vehicle vehicle = userService.addVehicle(form.getPolicyNumber(), form.getVehicle());
        return ResponseEntity.ok().body(vehicle); 
    }

    @PutMapping("/policy/cancel")
    public ResponseEntity<Object>cancelPolicy(@RequestBody CancelPolicyForm form){
        Policy policy = userService.cancelPolicy(form.getPolicyNumber(), form.getUpdatedExpirationDate());
        return ResponseEntity.ok().body(policy); 
    }

    @PostMapping("/claim/file")
    public ResponseEntity<Object>filePolicyClaim(@RequestBody FileClaimForm  form){
        PolicyClaim policyClaim = userService.filePolicyClaim(form.getPolicyNumber(), form.getPolicyClaim());
        return ResponseEntity.ok().body(policyClaim); 
    }

    @GetMapping(path = "/admin/user/{accountNumber}")
    public ResponseEntity<User>getUserByAccountNumber(@PathVariable("accountNumber") String accountNumber){
        User user = userService.getUserByAccountNumber(accountNumber);
        return ResponseEntity.ok().body(user); 
    }

    @GetMapping(path = "/policy/{policyNumber}")
    public ResponseEntity<Policy>getPolicyByPolicyNumber(@PathVariable("policyNumber") String policyNumber){
        Policy policy = userService.getPolicyByPolicyNumber(policyNumber);
        return ResponseEntity.ok().body(policy); 
    }
    
    @GetMapping(path = "/admin/claim/{claimNumber}")
    public ResponseEntity<PolicyClaim>getClaimByClaimNumber(@PathVariable("claimNumber") String claimNumber){
        PolicyClaim policyClaim = userService.getClaimByClaimNumber(claimNumber);
        return ResponseEntity.ok().body(policyClaim); 
    }

    @DeleteMapping(path = "/admin/deleteuser/{accountNumber}")
    public ResponseEntity<?>deleteUserByAccountNumber(@PathVariable("accountNumber") String accountNumber){
        userService.deleteUser(accountNumber);
        return ResponseEntity.ok().build(); 
    }
}

@Data
class RoleToUserForm{
    private String email;
    private String roleName;
} 

@Data
class PolicyToUserForm{
    String email;
    String effectiveDate;
    Policy policy = new Policy();
}

@Data
class PolicyHoldertoPolicyForm{
    private String policyNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dob;
    private String address;
    private String drivLicNum;
    private String drivLicIssueDate;

    public PolicyHolder getPolicyHolder(){
        return new PolicyHolder(firstName, middleName, lastName, dob, address, drivLicNum, drivLicIssueDate);
    }
    
}

@Data
class VehicletoPolicyForm{
    private String policyNumber;
    private String make, model, type,fuelType, color; 
    private String year;
    private double purchasePrice; 

    public Vehicle getVehicle(){
        return new Vehicle(make, model, type, fuelType, color, year, purchasePrice);
    }
    
}

@Data
class CancelPolicyForm{
    String policyNumber;
    String updatedExpirationDate;
}

@Data
class FileClaimForm{
    String policyNumber;
    String addressOfAccident;
    String descriptionOfAccident;
    String descriptionOfDamage;
    double estimatedCost;

    public PolicyClaim getPolicyClaim(){
        return new PolicyClaim(addressOfAccident, descriptionOfAccident, descriptionOfDamage, estimatedCost);
    }
}