package com.fastenal.microservices.userms.repository;

import com.fastenal.microservices.userms.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

//    @Query("Select u from User u where u.email=?1")
//    Optional<User> findUserByEmail(String email);

}
