package com.fastenal.microservices.userms.repository;

import com.fastenal.microservices.userms.dtos.AuthenticationToken;
import com.fastenal.microservices.userms.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationTokenRepo extends JpaRepository<AuthenticationToken, Integer> {
    AuthenticationToken findByUser(User user);
    AuthenticationToken findByToken(String token);
    void deleteByToken(String token);

    void deleteByUser(Optional<User> user);
}
