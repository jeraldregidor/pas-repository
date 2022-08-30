package com.example.demo.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.config.PasConfigProperties;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service @Transactional
public class PasLogOutHandler implements LogoutHandler {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasConfigProperties pasConfigProperties;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        String email = JWT.require(Algorithm.HMAC256(pasConfigProperties.secretPhrase()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, ""))
            .getSubject();

        User user = userRepo.findByEmail(email);
        user.setToken(null);

        Map<String, String> fields = new HashMap<>();
        fields.put("messsage", "Logged out");
        response.setContentType("application/json");
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), fields);
        } catch (StreamWriteException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
