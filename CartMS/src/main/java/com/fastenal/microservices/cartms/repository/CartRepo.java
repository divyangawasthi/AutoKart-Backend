package com.fastenal.microservices.cartms.repository;

import com.fastenal.microservices.cartms.dtos.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {
    List<Cart> findAllByUid(int uid);
    List<Cart> deleteAllByUid(int uid);
    List<Cart> deleteByUidAndPid(int uid, int pid);
    List<Cart> deleteByPid(int pid);
    Cart findByUidAndAndPid(int uid, int pid);
}
