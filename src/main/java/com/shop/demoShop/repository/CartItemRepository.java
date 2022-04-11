package com.shop.demoShop.repository;

import com.shop.demoShop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}