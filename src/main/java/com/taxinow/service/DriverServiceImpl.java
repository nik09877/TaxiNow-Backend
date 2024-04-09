package com.taxinow.service;

import com.taxinow.config.JwtUtil;
import com.taxinow.domain.RideStatus;
import com.taxinow.domain.UserRole;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    //    private final RideRepository rideRepository;
    private final LicenseRepository licenseRepository;

    private final CalculatorService calculatorService;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository,
                             VehicleRepository vehicleRepository,
//                             RideRepository rideRepository,
                             LicenseRepository licenseRepository,
                             PasswordEncoder passwordEncoder,
                             JwtUtil jwtUtil,
                             CalculatorService calculatorService
    ) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
//        this.rideRepository = rideRepository;
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
        createdVehicle.setCapacity(vehicle.getCapacity());
        createdVehicle.setColor(vehicle.getColor());
        createdVehicle.setId(vehicle.getId());
        createdVehicle.setLicensePlate(vehicle.getLicensePlate());
        createdVehicle.setMake(vehicle.getMake());
        createdVehicle.setModel(vehicle.getModel());
        createdVehicle.setYear(vehicle.getYear());
        Vehicle savedVehicle = vehicleRepository.save(createdVehicle);

        Driver driver = new Driver();
        String encodedPassword = passwordEncoder.encode(driverSignupRequest.getPassword());
        driver.setEmail(driverSignupRequest.getEmail());
        driver.setName(driverSignupRequest.getName());
        driver.setMobile(driverSignupRequest.getMobile());
        driver.setPassword(encodedPassword);
        driver.setLicense(savedLicense);
        driver.setVehicle(savedVehicle);
        driver.setRole(UserRole.DRIVER);
        driver.setLatitude(driverSignupRequest.getLatitude());
        driver.setLongitude(driverSignupRequest.getLongitude());

        Driver savedDriver = driverRepository.save(driver);
        savedLicense.setDriver(savedDriver);
        savedVehicle.setDriver(savedDriver);

        licenseRepository.save(savedLicense);
        vehicleRepository.save(savedVehicle);

        return savedDriver;
    }

    @Override
    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, double radius, Ride ride) {
        var allDrivers = driverRepository.findAll();

        List<Driver> availableDrivers = new ArrayList<>();

        for (var driver : allDrivers) {
            if (driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED)
                continue;
            if (ride.getDeclinedDrivers().contains(driver.getId()))
                continue;

            double distance = calculatorService.calculateDistance(driver.getLatitude(), driver.getLongitude(), pickupLatitude, pickupLongitude);
//            if(distance > radius)
//                continue;
            availableDrivers.add(driver);
        }

        return availableDrivers;
    }

    @Override
    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
        double minDist = Double.MAX_VALUE;
        Driver nearestDriver = null;

        for (var driver : availableDrivers) {
            double distance = calculatorService.calculateDistance(driver.getLatitude(), driver.getLongitude(), pickupLatitude, pickupLongitude);
            if (distance < minDist) {
                minDist = distance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    @Override
    public Driver getReqDriverProfile(String jwt) throws DriverException {
        String email = jwtUtil.getEmailFromJwt(jwt);
        Driver driver = driverRepository.findByEmail(email);
        if (driver == null)
            throw new DriverException("Driver does not exist with email " + email);
        return driver;
    }

    @Override
    public Ride getDriversCurrentRide(Integer driverId) throws DriverException {
        Driver driver = findDriverById(driverId);
        return driver.getCurrentRide();
    }

    @Override
    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {
        return driverRepository.getAllocatedRides(driverId);
    }

    @Override
    public Driver findDriverById(Integer driverId) throws DriverException {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (driver.isPresent())
            return driver.get();
        throw new DriverException("Driver does not exist with id " + driverId);
    }

    @Override
    public List<Ride> getCompletedRides(Integer driverId) throws DriverException {
        return driverRepository.getCompletedRides(driverId);
    }
}
