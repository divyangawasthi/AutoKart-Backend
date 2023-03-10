package com.fastenal.microservices.userms.controller;

import com.fastenal.microservices.userms.dtos.ApiResponse;
import com.fastenal.microservices.userms.dtos.Product;
import com.fastenal.microservices.userms.dtos.User;
import com.fastenal.microservices.userms.dtos.WishList;
import com.fastenal.microservices.userms.exceptions.CustomException;
import com.fastenal.microservices.userms.service.AuthenticationService;
import com.fastenal.microservices.userms.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/add/{token}")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product, @PathVariable("token") String token) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        WishList wishList = wishListService.getProductByID(product.getId(), user);
        if(Objects.isNull(wishList)){
            wishList = new WishList(user, product.getId());
            wishListService.createWishList(wishList, user);
            return new ResponseEntity<>(new ApiResponse(true, "Added to Wishlist"), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(new ApiResponse(true, "Product already in Wishlist"), HttpStatus.CREATED);
        }

    }

    @DeleteMapping("/remove/{pid}/{token}")
    public ResponseEntity<ApiResponse> removeFromWishList(@PathVariable("pid") int pid, @PathVariable("token") String token) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        WishList wishList = wishListService.getProductByID(pid, user);
        wishListService.deleteWishListProduct(wishList);

        return new ResponseEntity<>(new ApiResponse(true, "Removed from Wishlist"), HttpStatus.OK);
    }

    @GetMapping("/{token}")
    public ResponseEntity<List<Product>> getWishList(@PathVariable("token") String token) throws URISyntaxException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        List<Product> products = wishListService.getWishListForUser(user);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/clear-wishlist/{token}")
    public ResponseEntity<ApiResponse> clearWishlist(@PathVariable("token") String token){
        try{
            User user = authenticationService.getUser(token);
            wishListService.clearWishlist(user);
            return  new ResponseEntity<>(new ApiResponse(true, "Wishlist Cleared"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error clearing wishlist\n"+e.getMessage());
        }
    }
}
