package com.taxinow.service;

import com.taxinow.exception.DriverException;
import com.taxinow.exception.RideException;
import com.taxinow.model.Driver;
import com.taxinow.model.Ride;
import com.taxinow.model.User;
import com.taxinow.request.RideRequest;

public interface RideService {
    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;

    public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude, double pickupLongitude, double destLatitude, double destLongitude, String pickupArea, String destinationArea);

    public void acceptRide(Integer rideId) throws RideException;

    public void declineRide(Integer rideId, Integer driverId) throws RideException, DriverException;

    public void startRide(Integer rideId, int otp) throws RideException;

    public void completeRide(Integer rideId) throws RideException;

    public void cancelRide(Integer rideId) throws RideException;

    public Ride findRideById(Integer rideId) throws RideException;
}
