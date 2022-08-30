package com.example.demo.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.config.PasConfigProperties;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonAuthenticationSuccess extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasConfigProperties pasConfigProperties;

    @Override @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        String token = JWT.create()
                        .withSubject(principal.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                        .sign(Algorithm.HMAC256(pasConfigProperties.secretPhrase()));
        User user = userRepo.findByEmail(principal.getUsername());
        Map<String, String> fields = new HashMap<>();
        fields.put("id",Integer.toString(user.getId()));
        fields.put("firstName", user.getFirstName());
        fields.put("middleName", user.getMiddleName());
        fields.put("lastName", user.getLastName());
        fields.put("accountNumber", user.getAccountNumber());
        fields.put("email", user.getEmail());
        fields.put("address", user.getAddress());
        fields.put("token", token);
        user.setToken(token);                         //    set a valid to TOKEN to DB for request
        fields.put("type", "bearer");
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), fields);
    }
}
    