package com.csuse.jpetstoressm.service.impl;

import com.csuse.jpetstoressm.domain.*;
import com.csuse.jpetstoressm.persistence.*;
import com.csuse.jpetstoressm.service.CatalogService;
import com.csuse.jpetstoressm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Autowired
    private LineItemMapper lineItemMapper;

    @Autowired
    private CartShopMapper cartShopMapper;

    @Override
    public void insertOrder(Order order) {
        order.setOrderId(getNextId("ordernum"));
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = order.getLineItems().get(i);
            String itemId = lineItem.getItemId();
            Integer increment = lineItem.getQuantity();

            Item item = itemMapper.getItem(itemId);
            item.setQuantity(itemMapper.getInventoryQuantity(itemId));
            lineItem.setItem(item);

            Map<String, Object> param = new HashMap<String, Object>(2);
            param.put("itemId", itemId);
            param.put("increment", increment);

            itemMapper.updateInventoryQuantity(param);
        }

        orderMapper.insertOrder(order);
        orderMapper.insertOrderStatus(order);
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = order.getLineItems().get(i);
            lineItem.setOrderId(order.getOrderId());
            lineItemMapper.insertLineItem(lineItem);
        }
    }

    @Override
    public Order getOrder(int orderId) {
        Order order = orderMapper.getOrder(orderId);
        order.setLineItems(lineItemMapper.getLineItemsByOrderId(orderId));

        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = order.getLineItems().get(i);
            Item item = itemMapper.getItem(lineItem.getItemId());
            item.setQuantity(itemMapper.getInventoryQuantity(lineItem.getItemId()));
            lineItem.setItem(item);
        }
        return order;
    }

    @Override
    public List<Order> getOrderByUsername(String username) {
        return orderMapper.getOrdersByUsername(username);
    }

    @Override
    public int getNextId(String name) {
        Sequence sequence = new Sequence(name, -1);
        sequence = (Sequence) sequenceMapper.getSequence(sequence);
        if (sequence == null) {
            throw new RuntimeException("Error: A null sequence was returned from the database (could not get next " + name
                    + " sequence).");
        }
        Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
        sequenceMapper.updateSequence(parameterObject);
        return sequence.getNextId();
    }

    @Override
    public List<CartItem> getCartShopItems(Account account) {
        List<CartItem> cartItems = cartShopMapper.getCartShopItems(account);
        for (int i=0 ; i<cartItems.size();i++) {
            Item item = itemMapper.getItem(cartItems.get(i).getItemId());
            Product product = productMapper.getProduct(item.getProductId());
            item.setProduct(product);
            cartItems.get(i).setItem(item);
        }
        return cartItems;
    }

    @Override
    public void insertIntoCartShop(Account account,CartItem cartItem) {
        cartShopMapper.insertIntoCartShop(account, cartItem);

    }

    @Override
    public void deleteItemFromCartShop(Account account,CartItem cartItem) {
        cartShopMapper.deleteItemFromCartShop(account, cartItem);
    }

    @Override
    public void updateItemQuantity(Account account,CartItem cartItem) {
        cartShopMapper.updateItemQuantity(account, cartItem);
    }

    @Override
    public void deleteAll(Account account) {
        cartShopMapper.deleteAll(account);
    }
}
