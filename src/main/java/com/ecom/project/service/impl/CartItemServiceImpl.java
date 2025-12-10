package com.ecom.project.service.impl;

import com.ecom.project.entity.CartItem;
import com.ecom.project.entity.User;
import com.ecom.project.repository.CartItemRepository;
import com.ecom.project.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {

        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();


        if (cartItemUser.getId().equals(userId)) {

            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

            return cartItemRepository.save(item);
        } else {
            throw new Exception("You can't update another user's cart_item");
        }


    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem cartItem=findCartItemById(cartItemId);
        User cartItemUser=cartItem.getCart().getUser();
        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(cartItem);
        }
        else {
            throw new Exception("You can't delete this item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws Exception {
       return cartItemRepository.findById(cartItemId).orElseThrow(()->
               new Exception("cart item not find with this is"+cartItemId));
    }
}
