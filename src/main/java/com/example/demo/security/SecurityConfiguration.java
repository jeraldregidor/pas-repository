package com.example.demo.security;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.config.PasConfigProperties;

@Configuration @EnableWebSecurity 
public class SecurityConfiguration  {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasConfigProperties pasConfigProperties;

    private PasLogOutHandler logoutHandler;
    private JsonAuthenticationSuccess authSuccessHandler;
    private MyUserDetailsServiceImp userDetailsServiceImp;

    public SecurityConfiguration(JsonAuthenticationSuccess authSuccessHandler, MyUserDetailsServiceImp userDetailsServiceImp, PasLogOutHandler logoutHandler) {
        this.authSuccessHandler = authSuccessHandler;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                            .antMatchers(HttpMethod.POST,"/api/register/**").permitAll()
                            .antMatchers(HttpMethod.GET,"/api/admin/**").hasAuthority("ADMIN")
                            .antMatchers("/api/**").hasAnyAuthority("USER","ADMIN")
                            .anyRequest().authenticated()
                            // .anyRequest().permitAll()
                            .and()

                            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            .and()
                            .addFilter(authenticationFilter())
                            .addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsServiceImp, pasConfigProperties))
                            .logout(logout -> logout
                                    .logoutUrl("/api/logout")
                                    .clearAuthentication(true)
                                    .addLogoutHandler(logoutHandler))
                            .exceptionHandling()
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));              
                        
                    } catch (Exception e) {        
                        System.out.println("\nDUMAAAN AKO sa Catch !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"); /////////////////////////////////
                        throw new RuntimeException(e);
                    }
                }
            )
            .httpBasic(Customizer.withDefaults());
        System.out.println("\nDUMAAAN AKO sa TRY #######################\n"); ////////////////////////
        System.out.println(pasConfigProperties.allowedOriginsArray().get(0));
        return http.build();    
    }

        // @Bean
        // WebSecurityCustomizer webSecurityCustomizer() {
        //     return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
        // }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    JsonObjAuthenticationFilter authenticationFilter() {
        System.out.println("\nDUMAAN AKO SA AUTHENTICATION FILTER METHOD #######################\n"); ////////////////////////////////////////
        JsonObjAuthenticationFilter jsonFilter = new JsonObjAuthenticationFilter();
        jsonFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        jsonFilter.setAuthenticationManager(authenticationManager);
        jsonFilter.setFilterProcessesUrl("/api/login");
        return jsonFilter;
    }
     
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(pasConfigProperties.allowedOriginsArray());
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


