package com.taxinow.controller;

import com.taxinow.config.JwtUtil;
import com.taxinow.domain.UserRole;
import com.taxinow.exception.DriverException;
import com.taxinow.exception.UserException;
import com.taxinow.model.Driver;
import com.taxinow.model.User;
import com.taxinow.repository.DriverRepository;
import com.taxinow.repository.UserRepository;
import com.taxinow.request.DriverSignupRequest;
import com.taxinow.request.LoginRequest;
import com.taxinow.request.SignupRequest;
import com.taxinow.response.JwtResponse;
import com.taxinow.service.CustomUserDetailsService;
import com.taxinow.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;
    private final DriverService driverService;

    @Autowired
    public AuthController(UserRepository userRepository, DriverRepository driverRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService,
                          DriverService driverService) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.driverService = driverService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest req) throws UserException {
        String email = req.getEmail();
        String fullName = req.getFullName();
        String mobile = req.getMobile();
        String password = req.getPassword();

        User user = userRepository.findByEmail(email);

        if (user != null) {
            throw new UserException("User already exists with email " + email);
        }

        String encodedPassword = passwordEncoder.encode(password);

        user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setFullName(fullName);
        user.setMobile(mobile);
        user.setRole(UserRole.USER);

        user = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //create jsw response
        String jwt = jwtUtil.generateJwtToken(authentication);
        JwtResponse res = new JwtResponse();
        res.setJwt(jwt);
        res.setAuthenticated(true);
        res.setError(false);
        res.setErrorDetails(null);
        res.setType(UserRole.USER);
        res.setMessage("Account created successfully: " + user.getFullName());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/signup/driver")
    public ResponseEntity<JwtResponse> driverSignupHandler(@RequestBody DriverSignupRequest req) throws UserException, DriverException {

        Driver driver = driverRepository.findByEmail(req.getEmail());

        if (driver != null) {
            throw new DriverException("Driver already exists with email " + req.getEmail());
        }

        driver = driverService.registerDriver(req);

        Authentication authentication = new UsernamePasswordAuthenticationToken(driver.getEmail(), driver.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //create jsw response
        String jwt = jwtUtil.generateJwtToken(authentication);
        JwtResponse res = new JwtResponse();
        res.setJwt(jwt);
        res.setAuthenticated(true);
        res.setError(false);
        res.setErrorDetails(null);
        res.setType(UserRole.DRIVER);
        res.setMessage("Account created successfully: " + driver.getEmail());

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginHandler(@RequestBody LoginRequest req) throws UserException {
        String email = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //create jsw response
        String jwt = jwtUtil.generateJwtToken(authentication);
        JwtResponse res = new JwtResponse();
        res.setJwt(jwt);
        res.setAuthenticated(true);
        res.setError(false);
        res.setErrorDetails(null);
        res.setType(UserRole.USER);
        res.setMessage("LoggedIn to account successfully");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Email or Password from authenticate method");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password from authenticate method");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
