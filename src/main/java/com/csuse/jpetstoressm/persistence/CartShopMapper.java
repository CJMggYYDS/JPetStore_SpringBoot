package com.csuse.jpetstoressm.persistence;

import com.csuse.jpetstoressm.domain.Account;
import com.csuse.jpetstoressm.domain.CartItem;
import com.csuse.jpetstoressm.domain.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface CartShopMapper {
    List<CartItem> getCartShopItems(Account account);

    void insertIntoCartShop(@Param("account") Account account, @Param("cartItem") CartItem item);

    void deleteItemFromCartShop(@Param("account1") Account account ,@Param("cartItem1") CartItem item);

    void updateItemQuantity(@Param("account2") Account account , @Param("cartItem2") CartItem item);

    void deleteAll(Account account);

}
