package com.taxinow.controller;

import com.taxinow.service.DriverService;
import com.taxinow.service.RideService;
import com.taxinow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ride")
public class RideController {
    @Autowired
    private RideService rideService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private UserService userService;

}
