package com.taxinow.service;

import com.taxinow.exception.DriverException;
import com.taxinow.model.Driver;
import com.taxinow.model.Ride;
import com.taxinow.request.DriverSignupRequest;

import java.util.List;

public interface DriverService {

    public Driver registerDriver(DriverSignupRequest driverSignupRequest);

    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, double radius, Ride ride);

    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);

    public Driver getReqDriverProfile(String jwt) throws DriverException;

    public Ride getDriversCurrentRide(Integer driverId) throws DriverException;

    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;

    public Driver findDriverById(Integer driverId) throws DriverException;

    public List<Ride> getCompletedRides(Integer driverId) throws DriverException;
}
