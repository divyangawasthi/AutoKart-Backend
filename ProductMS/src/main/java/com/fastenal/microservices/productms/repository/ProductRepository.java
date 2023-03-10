package com.fastenal.microservices.productms.repository;

import com.fastenal.microservices.productms.dtos.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findProductByName(String name);
}
