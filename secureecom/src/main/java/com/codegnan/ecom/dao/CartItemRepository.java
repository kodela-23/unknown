package com.codegnan.ecom.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.ecom.model.cart.Cart;
import com.codegnan.ecom.model.cart.CartItem;
import com.codegnan.ecom.model.product.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
