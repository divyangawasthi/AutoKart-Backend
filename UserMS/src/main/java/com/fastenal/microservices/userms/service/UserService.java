package com.fastenal.microservices.userms.service;

import com.fastenal.microservices.userms.dtos.*;
import com.fastenal.microservices.userms.encryption.MD5Hashing;
import com.fastenal.microservices.userms.exceptions.AuthenticationFailException;
import com.fastenal.microservices.userms.exceptions.CustomException;
import com.fastenal.microservices.userms.repository.UserRepo;
import com.fastenal.microservices.userms.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    WishListService wishListService;
    @Autowired
    AuthenticationService authenticationService;
    @Transactional
    public ResponseDTO signUp(User user) {
        if(Objects.nonNull(userRepo.findByEmail(user.getEmail()))){
            throw new CustomException("User Already Present");
        }

        String encryptedPassword = user.getPassword();
        try {
            encryptedPassword = MD5Hashing.hashPassword(user.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user.setPassword(encryptedPassword);
        userRepo.save(user);
        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);
        ResponseDTO responseDTO = new ResponseDTO("success","Signup Successful");
        return responseDTO;
    }

    public SignInResponseDTO signIn(SignInDTO signInDTO) {
        User user = userRepo.findByEmail(signInDTO.getEmail());
        if(Objects.isNull(user)){
            throw new AuthenticationFailException("User is not valid");
        }
        try{
            if(!user.getPassword().equals(MD5Hashing.hashPassword(signInDTO.getPassword()))){
                throw new AuthenticationFailException("Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        AuthenticationToken authenticationToken = authenticationService.getToken(user);
        if(Objects.isNull(authenticationToken)){
            authenticationToken = new AuthenticationToken(user);
            authenticationService.saveConfirmationToken(authenticationToken);
        }
        user.setPassword("");
        return new SignInResponseDTO("success",authenticationToken.getToken(), user);
    }

    public void signOut(String token){
        authenticationService.deleteToken(token);
    }

    public boolean checkUserFields(User user) {
        if(checkValidString(user.getFirstName()) && checkValidString(user.getLastName()) && checkValidPassword(user.getPassword()) && checkValidEmail(user.getEmail())){
            return true;
        }
        return false;
    }

    private boolean checkValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null){
            throw new CustomException("Email Invalid");

        }
        if (pat.matcher(email).matches() == true){
            return true;
        }
        throw new CustomException("Invalid Email Entered");
    }

//    It contains at least 8 characters and at most 20 characters.
//    It contains at least one digit.
//    It contains at least one upper case alphabet.
//    It contains at least one lower case alphabet.
//    It contains at least one special character which includes !@#$%&*()-+=^.
//    It doesn’t contain any white space.
    private boolean checkValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern pat = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        if (pat.matcher(password).matches() == true){
            return true;
        }
        throw new CustomException("Invalid Password Entered");
    }

    public boolean checkValidString(String s){
        if(s.isBlank() || s.isEmpty()){
            throw new CustomException("First Name or Last Name is Invalid");
        }
        return true;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        return allUsers;
    }

    public void deleteUser(int uid) {
        Optional<User> user = userRepo.findById(uid);
        authenticationService.deleteTokenByUser(user);
        userRepo.deleteById(uid);
    }
}
