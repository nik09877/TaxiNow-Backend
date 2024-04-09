package com.taxinow.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CalculatorService {

    private static final int EARTH_RADIUS = 6371;

    /*
     * This Java method calculates the distance between two geographical coordinates
     *  (latitude and longitude) using the Haversine formula, which is commonly used to
     *  calculate distances between two points on a sphere (such as the Earth)
     * given their latitudes and longitudes.
     */
    public double calculateDistance(double sourceLat, double sourceLng, double desLat, double desLng) {
        double dLat = Math.toRadians(desLat - sourceLat);
        double dLng = Math.toRadians(desLng - sourceLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(desLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return distance;

    }

    public long calculateDuration(LocalDateTime startTime, LocalDateTime endTime, boolean inMilliSeconds) {
        Duration duration = Duration.between(startTime, endTime);
        if (inMilliSeconds)
            return duration.toMillis();
        return duration.toSeconds();
    }

    public double calculateFare(double distance) {
        double baseFare = 11;
        double totalFare = baseFare * distance;
        return totalFare;
    }
}
