package com.taxinow.service;

import com.taxinow.domain.RideStatus;
import com.taxinow.exception.DriverException;
import com.taxinow.exception.RideException;
import com.taxinow.model.Driver;
import com.taxinow.model.Ride;
import com.taxinow.model.User;
import com.taxinow.repository.DriverRepository;
import com.taxinow.repository.NotificationRepository;
import com.taxinow.repository.RideRepository;
import com.taxinow.request.RideRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RideServiceImpl implements RideService {

    private static final int RADIUS = 5;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    DriverService driverService;
    @Autowired
    CalculatorService calculatorService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {

        Ride existingRide = new Ride();
        List<Driver> availableDrivers = driverService.getAvailableDrivers(rideRequest.getPickupLatitude(), rideRequest.getPickupLongitude(), RADIUS, existingRide);
        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, rideRequest.getPickupLatitude(), rideRequest.getPickupLongitude());

        if (nearestDriver == null)
            throw new DriverException("Driver not available");

        Ride ride = createRideRequest(user, nearestDriver, rideRequest.getPickupLatitude(), rideRequest.getPickupLongitude(), rideRequest.getDestinationLatitude(), rideRequest.getDestinationLongitude(), rideRequest.getPickupArea(), rideRequest.getDestinationArea());

        /*
send notification to driver
        Notification notification = new Notification();
        notification.setDriver(nearestDriver);
        notification.setMessage("you have been allocated to a ride");
        notification.setRide(ride);
        notification.setType(NotificationType,RIDE_REQUEST);
        Notification savedNotification = notificationRepository.save(notification);
*/

        return ride;
    }

    @Override
    public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude, double pickupLongitude, double destLatitude, double destLongitude, String pickupArea, String destinationArea) {
        Ride ride = new Ride();
        ride.setDriver(nearestDriver);
        ride.setUser(user);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDestinationLatitude(destLatitude);
        ride.setDestinationLongitude(destLongitude);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPickupArea(pickupArea);
        ride.setDestinationArea(destinationArea);

        return rideRepository.save(ride);
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.ACCEPTED);
        Driver driver = ride.getDriver();
        driver.setCurrentRide(ride);

        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        ride.setOtp(otp);

        driverRepository.save(driver);
        rideRepository.save(ride);

/*
        Notification notification = new Notification();
        notification.setUser(ride.getUser());
        notification.setMessage("Your ride is confirmed, Driver will arrive at your pickup location soon");
        notification.setRide(ride);
        notification.setTimestamp(LocalDateTime.now());
        notification.setType(NotificatonType.RIDE_CONFIRMATION);
        Notification savedNotification = notificationRepository.save(notification);
*/
    }

    @Override
    public void declineRide(Integer rideId, Integer driverId) throws RideException, DriverException {
        Ride ride = findRideById(rideId);
        ride.getDeclinedDrivers().add(driverId);
        List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(), ride.getPickupLongitude(), RADIUS, ride);
        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(), ride.getPickupLongitude());
        if (nearestDriver == null)
            throw new DriverException("Driver not available");
        ride.setDriver(nearestDriver);
        rideRepository.save(ride);
    }

    @Override
    public void startRide(Integer rideId, int otp) throws RideException {
        Ride ride = findRideById(rideId);
        if (otp != ride.getOtp())
            throw new RideException("please provide a valid otp");

        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);

        /*
         Notification notification = new Notification();
         notification.setUser(ride.getUser());
         notification.setMessage("Driver has reached at your pickup location");
         notification.setRide(ride);
         notification.setTimestamp(LocalDateTime.now());
         notification.setType(NotificatonType.RIDE_CONFIRMATION);
         Notification savedNotification = notificationRepository.save(notification);
        */

    }

    @Override
    public void completeRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());
        double distance = calculatorService.calculateDistance(ride.getPickupLatitude(), ride.getPickupLongitude(), ride.getDestinationLatitude(), ride.getDestinationLongitude());
        long milliSeconds = calculatorService.calculateDuration(ride.getStartTime(), ride.getEndTime(), true);
        double fare = calculatorService.calculateFare(distance);

        ride.setDistance(Math.round(distance * 100.0) / 100.0);
        ride.setFare((int) Math.round(fare));
        ride.setDuration(milliSeconds);

        Driver driver = ride.getDriver();
        driver.getRides().add(ride);
        driver.setCurrentRide(null);

//        Integer driverRevenue = (int) (driver.getTotalRevenue() + Math.round(fare * 0.8));
//        driver.setTotalRevenue(driverRevenue);
//        driverRepository.save(driver);
        rideRepository.save(ride);
        /*
         Notification notification = new Notification();
         notification.setUser(ride.getUser());
         notification.setMessage("You have reached your destination");
         notification.setRide(ride);
         notification.setTimestamp(LocalDateTime.now());
         notification.setType(NotificatonType.RIDE_COMPLETED);
         Notification savedNotification = notificationRepository.save(notification);
        */
    }

    @Override
    public void cancelRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {
        Optional<Ride> ride = rideRepository.findById(rideId);
        if (ride.isPresent())
            return ride.get();
        throw new RideException("Ride doesn't exist with rideId " + rideId);
    }
}
