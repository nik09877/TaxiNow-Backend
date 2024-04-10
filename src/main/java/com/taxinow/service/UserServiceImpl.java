package com.taxinow.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import com.taxinow.config.JwtUtil;
import com.taxinow.exception.UserException;
import com.taxinow.model.Ride;
import com.taxinow.model.User;
import com.taxinow.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User createUser(User user) throws UserException {

        User emailExist = findUserByEmail(user.getEmail());

        if (emailExist != null) throw new UserException("Email Already Used With Another Account");

        return userRepository.save(user);

    }

    @Override
    public User findUserById(Integer userId) throws UserException {

        Optional<User> opt = userRepository.findById(userId);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("User not found with id " + userId);
    }

    @Override
    public User findUserByEmail(String email) throws UserException {

        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        }
        throw new UserException("uUser not found with email " + email);
    }

    @Override
    public User getReqUserProfile(String token) throws UserException {

        String email = jwtUtil.getEmailFromJwt(token);
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        }

        throw new UserException("invalid token...");

    }

    @Override
    public User findUserByToken(String token) throws UserException {
        String email = jwtUtil.getEmailFromJwt(token);
        if (email == null) {
            throw new BadCredentialsException("invalid token received");
        }
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        }
        throw new UserException("user not found with email " + email);
    }

    @Override
    public List<Ride> completedRides(Integer userId) throws UserException {
        return userRepository.getCompletedRides(userId);
    }
}
