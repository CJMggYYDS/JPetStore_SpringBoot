package com.csuse.jpetstoressm.persistence;

import com.csuse.jpetstoressm.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<Category> getCategoryList();

    Category getCategory(String categoryID);
}
