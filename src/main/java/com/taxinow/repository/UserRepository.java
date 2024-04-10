package com.taxinow.repository;

import com.taxinow.model.Ride;
import com.taxinow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    @Query("SELECT R FROM Ride R WHERE R.status = COMPLETED AND R.user.id = :userId")
    public List<Ride> getCompletedRides(@Param("userId") Integer userId);

}
