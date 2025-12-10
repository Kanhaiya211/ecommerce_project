package com.ecom.project.service.impl;

import com.ecom.project.entity.Cart;
import com.ecom.project.entity.CartItem;
import com.ecom.project.entity.Product;
import com.ecom.project.entity.User;
import com.ecom.project.repository.CartItemRepository;
import com.ecom.project.repository.CartRepository;
import com.ecom.project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws Exception {
        Cart cart=findUserCart(user);
        CartItem isPresent=cartItemRepository.findByCartAndProductAndSize(cart,product,size);
        if (isPresent==null){
            CartItem cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setSize(size);
            cart.getCartItems().add(cartItem);
           int total_price=quantity*product.getSellingPrice();
           cartItem.setSellingPrice(total_price);
            cartItem.setMrpPrice(quantity*product.getSellingPrice());
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setCart(cart);
            return  cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) throws Exception {
        Cart cart=cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            return cart;
        }
        int total_price=0;
        int total_discounted_price=0;
        int total_item=0;
        for (CartItem cartItem:cart.getCartItems()){
            total_price= cartItem.getMrpPrice();
            total_discounted_price+=cartItem.getSellingPrice();
            total_item=cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(total_price);
        cart.setDiscount(calculateDiscountPercentage(total_price,total_discounted_price));
        cart.setTotalItem(total_item);

        cart.setTotalSellingPrice(total_discounted_price);
        return cartRepository.save(cart);
    }
    private static int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice<=0){
//            throw new IllegalArgumentException("actual price must be greater than zero");
            return 0;
        }
        double discount=mrpPrice-sellingPrice;
        double discountPercentage=(discount/mrpPrice)*100;
        return (int) discountPercentage;
    }
}
