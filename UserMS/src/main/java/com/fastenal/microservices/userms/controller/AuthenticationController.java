package com.fastenal.microservices.userms.controller;

import com.fastenal.microservices.userms.dtos.ResponseDTO;
import com.fastenal.microservices.userms.dtos.SignInDTO;
import com.fastenal.microservices.userms.dtos.SignInResponseDTO;
import com.fastenal.microservices.userms.dtos.User;
import com.fastenal.microservices.userms.service.AuthenticationService;
import com.fastenal.microservices.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("/{token}")
    public ResponseEntity<User> authenticate(@PathVariable("token") String token){
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}

