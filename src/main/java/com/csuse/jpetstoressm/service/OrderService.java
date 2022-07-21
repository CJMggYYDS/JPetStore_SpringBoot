package com.csuse.jpetstoressm.service;

import com.csuse.jpetstoressm.domain.Account;
import com.csuse.jpetstoressm.domain.CartItem;
import com.csuse.jpetstoressm.domain.Order;

import java.util.List;

public interface OrderService {

    void insertOrder(Order order);

    Order getOrder(int orderId);

    List<Order> getOrderByUsername(String username);

    int getNextId(String name);

    List<CartItem> getCartShopItems(Account account);

    void insertIntoCartShop(Account account,CartItem cartItem);

    void deleteItemFromCartShop(Account account,CartItem cartItem);

    void updateItemQuantity(Account account,CartItem cartItem);

    void deleteAll(Account account);
}
