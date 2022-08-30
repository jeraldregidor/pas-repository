package com.example.demo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pas")
public record PasConfigProperties(List<String> allowedOriginsArray, String admin1Em, String admin2Em, String admin1Pw, String admin2Pw, String secretPhrase) {} 
    
