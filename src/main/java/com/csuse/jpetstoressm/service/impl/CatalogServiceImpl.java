package com.csuse.jpetstoressm.service.impl;

import com.csuse.jpetstoressm.domain.Category;
import com.csuse.jpetstoressm.domain.Item;
import com.csuse.jpetstoressm.domain.Product;
import com.csuse.jpetstoressm.persistence.CategoryMapper;
import com.csuse.jpetstoressm.persistence.ItemMapper;
import com.csuse.jpetstoressm.persistence.ProductMapper;
import com.csuse.jpetstoressm.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    @Override
    public Category getCategory(String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    @Override
    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }

    @Override
    public List<Product> searchProductList(String keyword) {
        return productMapper.searchProductList("%"+keyword.toLowerCase()+"%");
    }

    @Override
    public List<Product> getProductListByCategory(String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }

    @Override
    public List<Item> getItemListByProduct(String productId) {

        return itemMapper.getItemListByProduct(productId);
    }

    @Override
    public Item getItem(String itemId) {
        Item item = itemMapper.getItem(itemId);
        return item;
    }

    @Override
    public boolean isItemInStock(String itemId) {
        return itemMapper.getInventoryQuantity(itemId) > 0;
    }
}
