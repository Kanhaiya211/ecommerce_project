package com.ecom.project.service;

import com.ecom.project.entity.Cart;
import com.ecom.project.entity.CartItem;
import com.ecom.project.entity.Product;
import com.ecom.project.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface CartService{

    public CartItem addCartItem(User user,Product product,String size,int quantity) throws Exception;
    public Cart findUserCart(User user)throws Exception;
}
