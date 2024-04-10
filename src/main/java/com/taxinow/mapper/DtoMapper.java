package com.taxinow.mapper;

import com.taxinow.dto.DriverDTO;
import com.taxinow.dto.RideDTO;
import com.taxinow.dto.UserDTO;

import com.taxinow.model.Driver;
import com.taxinow.model.Ride;
import com.taxinow.model.User;

public class DtoMapper {
    public static DriverDTO toDriverDto(Driver driver) {

        DriverDTO driverDto = new DriverDTO();

        driverDto.setEmail(driver.getEmail());
        driverDto.setId(driver.getId());
        driverDto.setLatitude(driver.getLatitude());
        driverDto.setLongitude(driver.getLongitude());
        driverDto.setMobile(driver.getMobile());
        driverDto.setName(driver.getName());
        driverDto.setRating(driver.getRating());
        driverDto.setRole(driver.getRole());
        driverDto.setVehicle(driver.getVehicle());


        return driverDto;

    }

    public static UserDTO toUserDto(User user) {

        UserDTO userDto = new UserDTO();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setMobile(user.getMobile());
        userDto.setName(user.getFullName());

        return userDto;

    }

    public static RideDTO toRideDto(Ride ride) {
        DriverDTO driverDTO = toDriverDto(ride.getDriver());
        UserDTO userDto = toUserDto(ride.getUser());

        RideDTO rideDto = new RideDTO();

        rideDto.setDestinationLatitude(ride.getDestinationLatitude());
        rideDto.setDestinationLongitude(ride.getDestinationLongitude());
        rideDto.setDistance(ride.getDistance());
        rideDto.setDriver(driverDTO);
        rideDto.setDuration(ride.getDuration());
        rideDto.setEndTime(ride.getEndTime());
        rideDto.setFare(ride.getFare());
        rideDto.setId(ride.getId());
        rideDto.setPickupLatitude(ride.getPickupLatitude());
        rideDto.setPickupLongitude(ride.getPickupLongitude());
        rideDto.setStartTime(ride.getStartTime());
        rideDto.setStatus(ride.getStatus());
        rideDto.setUser(userDto);
        rideDto.setPickupArea(ride.getPickupArea());
        rideDto.setDestinationArea(ride.getDestinationArea());
        rideDto.setPaymentDetails(ride.getPaymentDetails());
        rideDto.setOtp(ride.getOtp());
        return rideDto;
    }
}
