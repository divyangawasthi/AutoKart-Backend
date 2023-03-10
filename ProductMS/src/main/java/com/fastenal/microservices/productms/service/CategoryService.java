package com.fastenal.microservices.productms.service;

import com.fastenal.microservices.productms.dtos.Category;
import com.fastenal.microservices.productms.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;
    public void createCategory(Category category){
        categoryRepo.save(category);
    }
    public List<Category> listCategory(){
        return categoryRepo.findAll();
    }
    public void editCategory(int categoryId, Category updatecategory){
        Category category = categoryRepo.getById(categoryId);
        category.setCategoryName(updatecategory.getCategoryName());
        category.setDescription(updatecategory.getDescription());
        category.setImageUrl(updatecategory.getImageUrl());
        categoryRepo.save(category);
    }

    public boolean findById(int categoryId) {
        return categoryRepo.findById(categoryId).isPresent();
    }

    public void deleteCategory(Integer categoryId) {
        categoryRepo.deleteById(categoryId);
    }

    public boolean checkCategoryFields(Category category) {
        if(category.getCategoryName().isEmpty() || category.getCategoryName().isBlank() ||
                category.getDescription().isBlank() || category.getDescription().isEmpty() ||
                category.getImageUrl().isEmpty() || category.getImageUrl().isBlank()){
            return false;
        }
        return true;

    }
}
