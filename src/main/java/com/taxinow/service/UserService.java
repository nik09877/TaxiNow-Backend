package com.taxinow.service;

import com.taxinow.exception.UserException;
import com.taxinow.model.Ride;
import com.taxinow.model.User;

import java.util.List;

public interface UserService {
    public User createUser(User user) throws UserException;

    public User getReqUserProfile(String token) throws UserException;

    public User findUserById(Integer Id) throws UserException;

    public User findUserByEmail(String email) throws UserException;

    public User findUserByToken(String token) throws UserException;

    public List<Ride> completedRides(Integer userId) throws UserException;
}
