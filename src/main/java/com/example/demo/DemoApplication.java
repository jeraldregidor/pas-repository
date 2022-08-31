package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.demo.config.PasConfigProperties;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.MyUserService;


@SpringBootApplication
@EnableConfigurationProperties(PasConfigProperties.class)
public class DemoApplication {
	@Autowired
	PasConfigProperties pasConfigProperties;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner run(MyUserService userService){
		try {
			Optional<User> userOpt = Optional.of(userService.getUser(pasConfigProperties.admin1Em()));
			if(userOpt.isPresent()){
				System.out.println("\n		**********System Restart**********\n");
			}
			return null;
		} catch (Exception e) {
			return args -> {

				userService.saveRole(new Role("USER"));
				userService.saveRole(new Role("ADMIN"));

				userService.saveUser(new User("Jerald", "Herrera", "Regidor", pasConfigProperties.admin1Em(), pasConfigProperties.admin1Pw(), "Laguna"));
				userService.addRole(pasConfigProperties.admin1Em(), "ADMIN");

				userService.saveUser(new User("Charles", "Briones", "Pitagan", pasConfigProperties.admin2Em(), pasConfigProperties.admin2Pw(), "Cavite"));
				userService.addRole(pasConfigProperties.admin2Em(), "ADMIN");

				userService.saveUser(new User("Regem", "Pogi", "Martin", "regem@gmail.com", "regem123", "Laguna"));
			
			// for(int x = 0; x<50 ; x++){
			// 	String firstName = "firstname" + x;
			// 	String middleName = "middle" + x;
			// 	String lastName = "lastName" + x;
			// 	String email = "email" + x + "@gmail.com";
			// 	String password = "password" + x;
			// 	String address = "address" + x;
			// 	userService.saveUser(new User(firstName, middleName, lastName, email, password, address));	
			// }
			};
		}
	}
}
