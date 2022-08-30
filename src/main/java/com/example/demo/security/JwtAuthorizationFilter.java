package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.config.PasConfigProperties;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
    
    private boolean isTokenValid = false;                   
    private static final String TOKEN_PREFIX = "Bearer ";
    private MyUserDetailsServiceImp userDetailsServiceImp;
    private PasConfigProperties pasConfigProperties;
    
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MyUserDetailsServiceImp userDetailsServiceImp, PasConfigProperties pasConfigProperties){
        super(authenticationManager);
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.pasConfigProperties = pasConfigProperties;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = getAuthentication(request);

        
        if(auth == null || !isTokenValid){
            chain.doFilter(request, response);            
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        System.out.println("\n\n\n" + token + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + "\n\n\n");

        if(token == null || !token.startsWith(TOKEN_PREFIX)){
            return null;
        }
        String email = JWT.require(Algorithm.HMAC256(pasConfigProperties.secretPhrase()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, ""))
            .getSubject();
        if(email == null) return null;
        MyUserDetails myUserDetails = userDetailsServiceImp.loadUserByUsername(email);
        String tokenFromUserDetails = myUserDetails.getToken();
        
        if(tokenFromUserDetails == null || !token.replace(TOKEN_PREFIX, "").equals(tokenFromUserDetails)){
            isTokenValid = false;
        }
        else{ isTokenValid = true;}

        return new UsernamePasswordAuthenticationToken(myUserDetails.getUsername(), null, myUserDetails.getAuthorities());
    }
}
