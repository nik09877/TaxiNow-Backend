package com.taxinow.controller;

import com.taxinow.exception.DriverException;
import com.taxinow.model.Driver;
import com.taxinow.model.Ride;
import com.taxinow.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Driver> getReqDriverProfileHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @GetMapping("/current_ride")
    public ResponseEntity<Ride> getDriversCurrentRideHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        Ride ride = driverService.getDriversCurrentRide(driver.getId());
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping("/allocated")
    public ResponseEntity<List<Ride>> getAllocatedRidesHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        List<Ride> rides = driverService.getAllocatedRides(driver.getId());
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }

    @GetMapping("rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws DriverException {
        Driver driver = driverService.getReqDriverProfile(jwt);
        var rides = driverService.getCompletedRides(driver.getId());
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }

}
