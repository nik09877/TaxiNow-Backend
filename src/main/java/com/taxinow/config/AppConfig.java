package com.taxinow.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {

//    @Bean
//    public SecurityFilterChain securityConfiguration(HttpSecurity http) throws Exception {
//
//        http
//                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
//                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/home").permitAll()
////                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(new JwtTokenValidationFilter(), BasicAuthenticationFilter.class)
//                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
//                        new CorsConfigurationSource() {
//                            @Override
//                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                                CorsConfiguration cfg = new CorsConfiguration();
//                                cfg.setAllowedOrigins(List.of(
//                                        "*"));
//                                //,"GET","POST","DELETE","PUT","PATCH"
//                                cfg.setAllowedMethods(Collections.singletonList("*"));
//                                cfg.setAllowCredentials(true);
//                                cfg.setAllowedHeaders(Collections.singletonList("*"));
//                                cfg.setExposedHeaders(List.of("Authorization"));
//                                cfg.setMaxAge(3600L);
//                                return cfg;
//                            }
//                        }
//                ))
//                .formLogin(loginConfigurer -> {
//                })
//                .httpBasic(httpSecurityHttpBasicConfigurer -> {
//                });


//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
////                .requestMatchers(HttpMethod.GET,"/swagger-ul/**","/").permitAll()
////                .requestMatchers(HttpMethod.GET,"/ws/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JwtTokenValidationFilter(), BasicAuthenticationFilter.class)
//                .csrf().disable()
//                .cors()
//                .configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration cfg = new CorsConfiguration();
//                        cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:3001",
//                                "*"));
//                        //,"GET","POST","DELETE","PUT","PATCH"
//                        cfg.setAllowedMethods(Collections.singletonList("*"));
//                        cfg.setAllowCredentials(true);
//                        cfg.setAllowedHeaders(Collections.singletonList("*"));
//                        cfg.setExposedHeaders(Arrays.asList("Authorization"));
//                        cfg.setMaxAge(3600L);
//                        return cfg;
//                    }
//                }).and().httpBasic().and().formLogin();
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityConfigration(HttpSecurity http)
            throws Exception {

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/home").permitAll()
                .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()
//                .requestMatchers("/api/user").authenticated()
//                .requestMatchers("/api/ride").authenticated()
//                .requestMatchers("/api/driver").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenValidationFilter(),
                        BasicAuthenticationFilter.class)

                .csrf().disable()
                .cors()
                .configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration
                            (HttpServletRequest request) {

                        CorsConfiguration cfg = new CorsConfiguration();

                        cfg.setAllowedOrigins(Arrays.asList(

                                "http://localhost:3000",
                                "http://localhost:3001"
                        ));
                        //cfg.setAllowedMethods(Arrays.asList("GET", "POST","DELETE","PUT"));
                        cfg.setAllowedMethods(Collections.singletonList("*"));
                        cfg.setAllowCredentials(true);
                        cfg.setAllowedHeaders(Collections.singletonList("*"));
                        cfg.setExposedHeaders(Arrays.asList("Authorization"));
                        cfg.setMaxAge(3600L);
                        return cfg;

                    }
                }).and().httpBasic().and().formLogin();

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
