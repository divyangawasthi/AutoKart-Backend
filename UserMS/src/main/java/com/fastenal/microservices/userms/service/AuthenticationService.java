package com.fastenal.microservices.userms.service;

import com.fastenal.microservices.userms.dtos.AuthenticationToken;
import com.fastenal.microservices.userms.dtos.User;
import com.fastenal.microservices.userms.exceptions.AuthenticationFailException;
import com.fastenal.microservices.userms.repository.AuthenticationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    AuthenticationTokenRepo authenticationTokenRepo;


    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        authenticationTokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return authenticationTokenRepo.findByUser(user);
    }

    public User getUser(String token){
        AuthenticationToken authenticationToken = authenticationTokenRepo.findByToken(token);
        if(Objects.isNull(authenticationToken)){
            return null;
        }
        return authenticationToken.getUser();
    }
    public void authenticate(String token){
        if(Objects.isNull(token)){
            throw new AuthenticationFailException("Token not present");
        }
        if(Objects.isNull(getUser(token))){
            throw new AuthenticationFailException("Token not valid");
        }
    }

    public void deleteTokenByUser(Optional<User> user){
        authenticationTokenRepo.deleteByUser(user);
    }
    public void deleteToken(String token) {
        authenticationTokenRepo.deleteByToken(token);
    }
}
