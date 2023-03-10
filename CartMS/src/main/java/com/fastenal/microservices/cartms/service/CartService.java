package com.fastenal.microservices.cartms.service;

import com.fastenal.microservices.cartms.dtos.Cart;
import com.fastenal.microservices.cartms.dtos.Product;
import com.fastenal.microservices.cartms.dtos.User;
import com.fastenal.microservices.cartms.repository.CartRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CartService {
    @Autowired
    CartRepo cartRepo;
    Gson g = new Gson();


    public void addToCart(Cart cart) {
        Cart cart1 = cartRepo.findByUidAndAndPid(cart.getUid(), cart.getPid());
        if(Objects.isNull(cart1)){
            cartRepo.save(cart);
        }else {
            updateCartItem(cart1.getUid(),cart1.getPid(), cart1.getQuantity()+cart.getQuantity());
        }
    }

    public List<Product> getCartForUser(User user) {

        final List<Cart> carts = cartRepo.findAllByUid(user.getId());
        List<Product> products = new ArrayList<>();
        for(Cart cart: carts){
            int index = cart.getPid();
            String uri = "http://localhost:8080/product/"+index;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri,String.class);
            Product product = g.fromJson(result, Product.class);
            product.setId(index);
            product.setQuantity(cart.getQuantity());
            products.add(product);
        }
        return products;
    }

    public float getTotalCartAmount(User user){
        List<Product> products = getCartForUser(user);
        float total_price = 0;
        for(Product product: products){
            total_price += product.getQuantity()*product.getPrice();
        }
        return total_price;
    }

    public User getAuthorizedUser(String token){
        String auth_uri = "http://localhost:8081/auth/"+token;
        RestTemplate restTemplate = new RestTemplate();
        String auth_result = restTemplate.getForObject(auth_uri,String.class);
        User user = g.fromJson(auth_result, User.class);
        return user;
    }

    public void clearCart(User user) {
        cartRepo.deleteAllByUid(user.getId());
    }

    public void deleteFromCart(Integer uid, Integer pid) {
        cartRepo.deleteByUidAndPid(uid,pid);
    }

    public void updateCartItem(int uid, int pid, int quantity) {
        Cart cart = cartRepo.findByUidAndAndPid(uid,pid);
        cart.setQuantity(quantity);
        cart.setCreatedDate(new Date());
        cartRepo.save(cart);
    }

    public void deleteFromCart(int pid) {
        cartRepo.deleteByPid(pid);
    }
}
