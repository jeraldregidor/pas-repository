package com.example.demo.security;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

public class JsonObjAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        System.out.println("Attempt START #######################################"); /////////////////////////////////
        try {
            BufferedReader bReader = request.getReader();
            StringBuilder sBuilder = new StringBuilder();
            String line;
            while((line = bReader.readLine()) != null) {
                sBuilder.append(line);
            }
            LoginCred authRequest = new ObjectMapper().readValue(sBuilder.toString(),LoginCred.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            setDetails(request,token);

            System.out.println("Attempt DONE #######################################"); /////////////////////////////////
            return this.getAuthenticationManager().authenticate(token);

        } catch (Exception e) {
            System.out.println("ERROR SA attempt #######################################"); ///////////////////////////////
            throw new RuntimeException(e);
        }
    }
}

@Data 
class LoginCred{
    private String email;
    private String password;
}




