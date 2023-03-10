package com.fastenal.microservices.productms.controller;

import com.fastenal.microservices.productms.dtos.ApiResponse;
import com.fastenal.microservices.productms.dtos.Category;
import com.fastenal.microservices.productms.dtos.Product;
import com.fastenal.microservices.productms.exceptions.CustomException;
import com.fastenal.microservices.productms.repository.CategoryRepo;
import com.fastenal.microservices.productms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepo categoryRepo;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody Product product){
        try{
            if(productService.checkProductFields(product)){
                Optional<Category> optionalCategory = categoryRepo.findById(product.getCategoryId());
                if(optionalCategory.isEmpty()){
                    return new ResponseEntity<>(new ApiResponse(false,"Category is Invalid!"), HttpStatus.BAD_REQUEST);
                }
                productService.addProduct(product, optionalCategory.get());
                return new ResponseEntity<>(new ApiResponse(true,"Product is added"), HttpStatus.CREATED);
            }else {
                throw new CustomException("Invalid Fields Entered");
            }

        }catch (Exception e){
            throw new CustomException("Error adding product\n"+e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts(){
        try {
            List<Product> products =  productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error fetching products\n"+e.getMessage());
        }

    }

    @GetMapping("/{productId}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable("productId") Integer productId){
        try {
            Optional<Product> product = productService.getProductByID(productId);
            return new ResponseEntity<>(product, HttpStatus.FOUND);
        }catch (Exception e){
            throw new CustomException("Error fetching product\n"+e.getMessage());
        }

    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody Product product) {
        try {
            Optional<Category> optionalCategory = categoryRepo.findById(product.getCategoryId());
            if(optionalCategory.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(false,"Category is Invalid!"), HttpStatus.BAD_REQUEST);
            }
            productService.updateProduct(product, productId);
            return new ResponseEntity<>(new ApiResponse(true,"Product is added"), HttpStatus.CREATED);
        }catch (Exception e){
            throw new CustomException("Error updating product\n"+e.getMessage());
        }

    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Integer productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(new ApiResponse(true,"Product is deleted"), HttpStatus.CREATED);
        }catch (Exception e){
            throw new CustomException("Error deleting product\n"+e.getMessage());
        }
    }
}