package com.csuse.jpetstoressm.service;

import com.csuse.jpetstoressm.domain.Category;
import com.csuse.jpetstoressm.domain.Item;
import com.csuse.jpetstoressm.domain.Product;

import java.util.List;

public interface CatalogService {

    List<Category> getCategoryList();

    Category getCategory(String categoryId);

    Product getProduct(String productId);

    List<Product> getProductListByCategory(String categoryId) ;

    // TODO enable using more than one keyword
    List<Product> searchProductList(String keyword) ;

    List<Item> getItemListByProduct(String productId) ;

    Item getItem(String itemId) ;

    boolean isItemInStock(String itemId) ;
}
