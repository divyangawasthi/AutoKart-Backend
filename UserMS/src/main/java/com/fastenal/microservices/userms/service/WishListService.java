package com.fastenal.microservices.userms.service;

import com.fastenal.microservices.userms.dtos.Product;
import com.fastenal.microservices.userms.dtos.User;
import com.fastenal.microservices.userms.dtos.WishList;
import com.fastenal.microservices.userms.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WishListService {
    @Autowired
    WishListRepo wishListRepo;

    Gson g = new Gson();
    public void createWishList(WishList wishList, User user) {
        WishList wishList1 = wishListRepo.findWishListByPidAndUser(wishList.getPid(), user);
        if(Objects.isNull(wishList1)){
            wishListRepo.save(wishList);
        }
    }

    public List<Product> getWishListForUser(User user) throws URISyntaxException {
        final List<WishList> wishLists= wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<Product> products = new ArrayList<>();

        for(WishList wishList: wishLists){
            int index = wishList.getPid();
            String uri = "http://localhost:8080/product/"+index;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri,String.class);
            Product product = g.fromJson(result, Product.class);
            product.setId(index);
            products.add(product);
        }
        return products;
    }

    public void deleteWishListProduct(WishList wishList) {
        wishListRepo.delete(wishList);
    }

    public WishList getProductByID(int pid, User user) {
        return wishListRepo.findWishListByPidAndUser(pid, user);
    }

    public void clearWishlist(User user) {
        wishListRepo.deleteAllByUser(user);
    }
}
