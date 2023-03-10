package com.fastenal.microservices.productms.service;

import com.fastenal.microservices.productms.dtos.Category;
import com.fastenal.microservices.productms.dtos.Product;
import com.fastenal.microservices.productms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public void addProduct(Product product, Category category) throws Exception {
        Optional<Product> optionalProduct = productRepository.findProductByName(product.getName());
        if(optionalProduct.isEmpty()){
            productRepository.save(product);
        }else {
            throw new Exception("Product already present");
        }
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    public void updateProduct(Product product, Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(!optionalProduct.isPresent()){
            throw new Exception("Product not present");
        }
        Product product1 = optionalProduct.get();
        product1.setDescription(product.getDescription());
        product1.setImageUrl(product.getImageUrl());
        product1.setName(product.getName());
        product1.setCategoryId(product.getCategoryId());
        product1.setPrice(product.getPrice());
        productRepository.save(product1);
    }

    public Optional<Product> getProductByID(Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(!optionalProduct.isPresent()){
            throw new Exception("Product not present");
        }
        return optionalProduct;
    }

    public void deleteProduct(Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(!optionalProduct.isPresent()){
            throw new Exception("Product not present");
        }
        productRepository.deleteById(productId);
    }

    public boolean checkProductFields(Product product) {
        if(checkValidString(product.getName()) && checkValidString(product.getDescription()) && 
                checkValidString(product.getImageUrl()) && checkValidDouble(product.getPrice())){
            return true;
        }
        return false;
    }

    private boolean checkValidDouble(double price) {
        try {
            Double.valueOf(price);
        } catch (Exception ex){ // Not a valid double value
            return (false);
        }
        return (true);
    }

    public boolean checkValidString(String s){
        if(s.isBlank() || s.isEmpty()){
            return false;
        }
        return true;
    }
}
