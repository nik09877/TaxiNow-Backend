package com.taxinow.service;

import com.taxinow.model.Driver;
import com.taxinow.model.User;
import com.taxinow.repository.DriverRepository;
import com.taxinow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(DriverRepository driverRepository, UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }

        Driver driver = driverRepository.findByEmail(email);
        if (driver != null) {
            return new org.springframework.security.core.userdetails.User(driver.getEmail(), driver.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("user not found with email : - " + email);
    }
}
