package com.fastenal.microservices.userms.repository;

import com.fastenal.microservices.userms.dtos.User;
import com.fastenal.microservices.userms.dtos.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

    WishList findWishListByPidAndUser(int pid, User user);

    WishList findWishListByPid(int pid);

    List<WishList> deleteAllByUser(User user);


}
