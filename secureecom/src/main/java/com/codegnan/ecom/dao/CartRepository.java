package com.codegnan.ecom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.ecom.model.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserId(Long userId);
}
