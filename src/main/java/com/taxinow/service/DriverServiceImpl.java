package com.taxinow.service;

import com.taxinow.config.JwtUtil;
import com.taxinow.exception.DriverException;
import com.taxinow.model.Driver;
import com.taxinow.model.License;
import com.taxinow.model.Ride;
import com.taxinow.model.Vehicle;
import com.taxinow.repository.DriverRepository;
import com.taxinow.repository.LicenseRepository;
import com.taxinow.repository.RideRepository;
import com.taxinow.repository.VehicleRepository;
import com.taxinow.request.DriverSignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private VehicleRepository vehicleRepository;
    private RideRepository rideRepository;
    private LicenseRepository licenseRepository;

    private final CalculatorService calculatorService;
    private PasswordEncoder passwordEncoder;

    private JwtUtil jwtUtil;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository,
                             VehicleRepository vehicleRepository,
                             RideRepository rideRepository,
                             LicenseRepository licenseRepository,
                             PasswordEncoder passwordEncoder,
                             JwtUtil jwtUtil,
                             CalculatorService calculatorService
    ) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.rideRepository = rideRepository;
        this.licenseRepository = licenseRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.calculatorService = calculatorService;
    }


    @Override
    public Driver registerDriver(DriverSignupRequest driverSignupRequest) {
        License license = driverSignupRequest.getLicense();
        Vehicle vehicle = driverSignupRequest.getVehicle();

        License createdLicense = new License();

        createdLicense.setLicenseState(license.getLicenseState());
        createdLicense.setLicenseNumber(license.getLicenseNumber());
        createdLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
        createdLicense.setId(license.getId());

        License savedLicense = licenseRepository.save(createdLicense);

        Vehicle createdVehicle = new Vehicle();
        

        return new Driver();
    }

    @Override
    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, double radius, Ride ride) {
        return null;
    }

    @Override
    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
        return null;
    }

    @Override
    public Driver getReqDriverProfile(String jwt) throws DriverException {
        return null;
    }

    @Override
    public Ride getDriversCurrentRide(Integer driverId) throws DriverException {
        return null;
    }

    @Override
    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {
        return null;
    }

    @Override
    public Driver findDriverById(Integer driverId) throws DriverException {
        return null;
    }

    @Override
    public List<Ride> getCompletedRides(Integer driver) throws DriverException {
        return null;
    }
}
