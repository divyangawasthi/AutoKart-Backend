package com.fastenal.microservices.productms.repository;

import com.fastenal.microservices.productms.dtos.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
