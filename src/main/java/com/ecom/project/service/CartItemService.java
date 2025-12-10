package com.ecom.project.service;

import com.ecom.project.entity.CartItem;


public interface CartItemService {
    public CartItem updateCartItem(Long userId,Long id,CartItem cartItem)throws Exception;
    public void removeCartItem(Long userId,Long cartItemId)throws Exception;
    public CartItem findCartItemById(Long userId)throws Exception;
}
