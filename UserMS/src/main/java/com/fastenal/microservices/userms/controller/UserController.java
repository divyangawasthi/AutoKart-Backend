package com.fastenal.microservices.userms.controller;

import com.fastenal.microservices.userms.dtos.*;
import com.fastenal.microservices.userms.exceptions.AuthenticationFailException;
import com.fastenal.microservices.userms.exceptions.CustomException;
import com.fastenal.microservices.userms.service.AuthenticationService;
import com.fastenal.microservices.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseDTO signup(@RequestBody User user){
        try {
            if(userService.checkUserFields(user)){
                return userService.signUp(user);
            }else {
                throw new CustomException("Invalid Fields Entered\n");
            }
        }catch (Exception e){
            throw new CustomException("Error registering a new user\n"+e.getMessage());
        }
    }

    @GetMapping("/{token}")
    public ResponseEntity<User> getUserDetails(@PathVariable("token") String token){
        try {
            authenticationService.authenticate(token);
            User user = authenticationService.getUser(token);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error fetching user details\n"+e.getMessage());
        }
    }

    @DeleteMapping("/delete/{uid}")
    public ApiResponse deleteUser(@PathVariable("uid") int uid){
        try {
            userService.deleteUser(uid);
            return new ApiResponse(true,"User deleted");
        }catch (Exception e){
            throw new AuthenticationFailException("Cannot delete user\n"+e.getMessage());
        }
    }

    @PostMapping("/signin")
    public SignInResponseDTO signin(@RequestBody SignInDTO signInDTO){
        try {
            return userService.signIn(signInDTO) ;
        }catch (Exception e){
            throw new AuthenticationFailException("Cannot sign in\n"+e.getMessage());
        }
    }

    @GetMapping("/sign-out/{token}")
    public ApiResponse signout(@PathVariable("token") String token){
        try {
            authenticationService.authenticate(token);
            userService.signOut(token);
            return new ApiResponse(true,"Sign out successful");
        }catch (Exception e){
            throw new CustomException("Cannot Sign Out\n"+e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            List<User> users =  userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error fetching users\n"+e.getMessage());
        }
    }
}
