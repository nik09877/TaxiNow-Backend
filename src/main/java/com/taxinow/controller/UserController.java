package com.taxinow.controller;

import com.taxinow.exception.UserException;
import com.taxinow.model.Ride;
import com.taxinow.model.User;
import com.taxinow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer userId) throws UserException {
        System.out.println("find by user id");
        User createdUser = userService.findUserById(userId);

        return new ResponseEntity<User>(createdUser, HttpStatus.ACCEPTED);

    }

    @GetMapping("/profile")
    public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.getReqUserProfile(jwt);

        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.getReqUserProfile(jwt);

        List<Ride> rides = userService.completedRides(user.getId());

        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }
}
