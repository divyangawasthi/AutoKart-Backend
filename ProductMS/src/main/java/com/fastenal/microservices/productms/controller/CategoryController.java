package com.fastenal.microservices.productms.controller;

import com.fastenal.microservices.productms.dtos.ApiResponse;
import com.fastenal.microservices.productms.dtos.Category;
import com.fastenal.microservices.productms.dtos.Product;
import com.fastenal.microservices.productms.exceptions.CustomException;
import com.fastenal.microservices.productms.repository.CategoryRepo;
import com.fastenal.microservices.productms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepo categoryRepo;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category){
        try {
            if(categoryService.checkCategoryFields(category)){
                categoryService.createCategory(category);
                return new ResponseEntity<>(new ApiResponse(true, "New Category Created"), HttpStatus.CREATED);
            }else{
                throw new CustomException("Invalid Fields Entered");
            }

        }catch (Exception e){
            throw new CustomException("Cannot create a new category\n"+e.getMessage());
        }

    }
    @GetMapping("/list")
    public List<Category> listCategory(){
        try {
            return categoryService.listCategory();
        }catch (Exception e){
            throw new CustomException("Cannot show list of categories\n"+e.getMessage());
        }
    }
    @PostMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category){
        try {
            if(!categoryService.findById(categoryId)){
                return new ResponseEntity<>(new ApiResponse(false, "Category Does Not Exist"), HttpStatus.NOT_FOUND);
            }
            categoryService.editCategory(categoryId, category);
            return new ResponseEntity<>(new ApiResponse(true, "Category Updated"), HttpStatus.OK);
        }catch (Exception e){
            throw new CustomException("Error updating category\n"+e.getMessage());
        }

    }
//    @DeleteMapping("/delete/{categoryId}")
//    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer categoryId) throws Exception {
//        try {
//            Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
//            if (!optionalCategory.isPresent()) {
//                return new ResponseEntity<>(new ApiResponse(false, "Category is Invalid!"), HttpStatus.BAD_REQUEST);
//            }
//            categoryService.deleteCategory(categoryId);
//            return new ResponseEntity<>(new ApiResponse(true, "Category is deleted"), HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new CustomException("Error deleting category\n" + e.getMessage());
//        }
//    }
}
