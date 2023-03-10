package com.fastenal.microservices.cartms.controller;

import com.fastenal.microservices.cartms.dtos.ApiResponse;
import com.fastenal.microservices.cartms.dtos.Cart;
import com.fastenal.microservices.cartms.dtos.Product;
import com.fastenal.microservices.cartms.dtos.User;
import com.fastenal.microservices.cartms.exceptions.CustomException;
import com.fastenal.microservices.cartms.service.CartService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    Gson g = new Gson();

    @PostMapping("/add/{token}")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Product product,@PathVariable("token") String token){
        try{
            User user = cartService.getAuthorizedUser(token);
            Cart cart = new Cart(user.getId(), product.getId(), product.getQuantity());
            cartService.addToCart(cart);
            return new ResponseEntity<>(new ApiResponse(true,"Added to cart"), HttpStatus.CREATED);
        }catch (Exception e){
            throw new CustomException("Error adding product to the cart\n"+e.getMessage());
        }

    }

    @GetMapping("/{token}")
    public ResponseEntity<List<Product>> getCart(@PathVariable("token") String token){
        try {
            User user = cartService.getAuthorizedUser(token);
            List<Product> products = cartService.getCartForUser(user);

            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error fetching cart\n"+e.getMessage());
        }

    }

    @DeleteMapping("/clear-cart/{token}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable("token") String token){
        try {
            User user = cartService.getAuthorizedUser(token);
            cartService.clearCart(user);
            return new ResponseEntity<>(new ApiResponse(true,"Cart Cleared"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error clearing cart\n"+e.getMessage());
        }

    }

    @DeleteMapping("/delete/{pid}/{token}")
    public ResponseEntity<ApiResponse> deleteFromCart(@PathVariable("pid") int pid, @PathVariable("token") String token){
        try {
            User user = cartService.getAuthorizedUser(token);
            cartService.deleteFromCart(user.getId(),pid);
            return new ResponseEntity<>(new ApiResponse(true,"Item has been removed"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error deleting from cart\n"+e.getMessage());
        }


    }

    @PutMapping("/update/{pid}/{quantity}/{token}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable("pid") int pid,@PathVariable("quantity") int quantity, @PathVariable("token") String token){
        try {
            User user = cartService.getAuthorizedUser(token);
            cartService.updateCartItem(user.getId(),pid, quantity);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error updating cart item\n"+e.getMessage());
        }

    }

    @GetMapping("/total-price/{token}")
    public ResponseEntity<Float> getTotalPrice(@PathVariable("token") String token){
        try {
            User user = cartService.getAuthorizedUser(token);
            float total = cartService.getTotalCartAmount(user);

            return new ResponseEntity<>(total,HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error fetching cart\n"+e.getMessage());
        }
    }

    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<ApiResponse> deleteFromCart(@PathVariable("pid") int pid){
        try {
            cartService.deleteFromCart(pid);
            return new ResponseEntity<>(new ApiResponse(true,"Item has been removed"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error deleting from cart\n"+e.getMessage());
        }


    }
}
